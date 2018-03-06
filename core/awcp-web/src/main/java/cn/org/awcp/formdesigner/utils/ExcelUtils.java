package cn.org.awcp.formdesigner.utils;

import cn.org.awcp.core.utils.DateUtils;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.ComponentVO;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.core.constants.FormDesignerGlobal;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.venson.service.FileService;
import cn.org.awcp.venson.util.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.InputStream;
import java.util.*;

public class ExcelUtils {
    protected static final Log logger = LogFactory.getLog(ExcelUtils.class);

    public static Map<String, Object> importExcel(String fileId, String modelCode, String pageFormId, Map<String,String> customNameMap, Map<String,String> customDefaultMap){
        FileService fileService= Springfactory.getBean("IFileService");
        StoreService storeService = Springfactory.getBean("storeServiceImpl");
        FormdesignerService formdesignerServiceImpl = Springfactory.getBean("formdesignerServiceImpl");
        MetaModelOperateService metaModelOperateService = Springfactory.getBean("metaModelOperateServiceImpl");

        if (customDefaultMap == null || customDefaultMap.size() == 0){
            customDefaultMap = new HashMap<String,String>();
        }

        if(customNameMap == null || customNameMap.size() == 0){
            customNameMap = new HashMap<String,String>();
        }

        //处理导入Excel的问题
        InputStream input=fileService.getInputStream(fileId);

        List<Map<String, String>> data = ExcelUtil.getDataFromExcel(input, "xlxs");
        if (data == null || data.isEmpty()) {
            logger.error("导入的Excel文档为空  ！");
        }

        DynamicPageVO pageVO = formdesignerServiceImpl.findById(pageFormId);
        DocumentVO docVo = new DocumentVO();
        docVo.setDynamicPageId(String.valueOf(pageVO.getId()));

        ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
        PageList<StoreVO> stores = storeService.findByDyanamicPageId(pageFormId);

        Map<String,String> defaultValuesMap = new HashMap<String,String>();

        Map<String,ComponentVO> titleStoreMap = new HashMap<String,ComponentVO>();
        Map<String,Map<String,String>> titleSelectMap = new HashMap<String,Map<String,String>>();

        if (stores != null && stores.size() > 0) {
            for (StoreVO store : stores) {
                ComponentVO component = ComponentVO.parseFromJson(store.getContent());

                String dataItemCode = component.getDataItemCode();
                String defaultValueScript =  component.getDefaultValueScript();
                String optionScript = component.getOptionScript();
                Map<String,String> selectOptionsMap = new HashMap<String,String>();

                if(component.isValueType()){
                    titleStoreMap.put(component.getTittle(), component);

                    //处理默认值问题,如果传值没有自定义，则到form component里头动态取值
                    boolean notSetCustomDefaultAndHasDefaultScript = !customDefaultMap.containsKey(dataItemCode) && StringUtils.isNotBlank(defaultValueScript);
                    if (notSetCustomDefaultAndHasDefaultScript) {
                        try {
                            String defaultValue = (String) engine.eval(defaultValueScript);

                            customDefaultMap.put(dataItemCode, defaultValue);
                        } catch (ScriptException e) {
                            logger.error("ERROR", e);
                        }
                    }

                    //缓存所有select的options
                    if (component.isSelectType()) {
                        try {
                            String optionScriptValues = (String) engine.eval(optionScript);

                            Arrays.stream(optionScriptValues.split(";"))
                                    .forEach(x -> selectOptionsMap.put(x.split("=")[1],x.split("=")[0]));
                        } catch (ScriptException e) {
                            logger.error("ERROR", e);
                        }

                        titleSelectMap.put(component.getTittle(), selectOptionsMap);
                    }
                }
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        int insertSize = 0;

        for (Map<String, String> dataItem: data){
            Map<String, String> dataMap = new HashMap<String,String>();
            String resultValue = "";

            for(String excelTitleName: dataItem.keySet()){
                String v = dataItem.get(excelTitleName);
                String mappingComName = excelTitleName;

                if(customNameMap.containsKey(excelTitleName)){
                    mappingComName = customNameMap.get(excelTitleName);
                }

                if (titleStoreMap.containsKey(mappingComName)){
                    ComponentVO com = titleStoreMap.get(mappingComName);
                    resultValue = v;

                    if (v == null || v.isEmpty()){
                        //获取默认值
                        resultValue = customDefaultMap.get(com.getDataItemCode());
                    }
                    else if(com.isSelectType()){ // it is select
                        if(titleSelectMap.containsKey(com.getTittle())){
                            Map<String,String> optionsMap = titleSelectMap.get(com.getTittle());

                            if(optionsMap.containsKey(v)){
                                resultValue = optionsMap.get(v);
                            }
                            else{
                                logger.info(v + " option is not correct");
                            }

                        }
                        else{
                            logger.info(com.getTittle() + " options is not exist");
                        }
                    }

                    dataMap.put(com.getDataItemCode().replace(modelCode + ".", ""), resultValue);
                }
                else{
                    logger.info("excel header column "+ excelTitleName +" is not exist");
                }
            }

            //如果自定义的默认值没有在控件上体现，则设置。
            for(String customKey: customDefaultMap.keySet()){
                if (!dataMap.containsKey(customKey)){
                    dataMap.put(customKey.replace(modelCode + ".", ""), customDefaultMap.get(customKey));
                }
            }

            dataMap.put("ID", UUID.randomUUID().toString());

            handleFormDataByComponents(pageFormId,modelCode, dataMap);
            boolean saveResult =  metaModelOperateService.save(dataMap, modelCode);
            if(saveResult){ insertSize++; }
        }

        result.put("count", data.size());
        //result.put("updateSize", updateSize);
        result.put("insertSize", insertSize);
        return result;
    }

    private static void handleFormDataByComponents(String pageFormId,String modelCode,Map<String, String> map) {
        StoreService storeService = Springfactory.getBean("storeServiceImpl");

        PageList<StoreVO> stores = storeService.findByDyanamicPageId(pageFormId);
        if (stores != null && stores.size() > 0) {
            for (StoreVO store : stores) {
                ComponentVO component = ComponentVO.parseFromJson(store.getContent());

                if(component.isValueType()){
                    String code = component.getDataItemCode().replace(modelCode + ".", "");
                    if (!map.containsKey(code)) {
                        continue;
                    }

                    String value = map.get(code);
                    JSONObject comObj = component.getJsonObj();

                    if (component.isDateTimeType()) {
                        String dateType = comObj.getString("dateType");
                        value = handleDateType(value, dateType);

                        map.put(code, value);
                    }
                    if (component.getComponentType() == 1032) {
                        value = value.replaceAll(",", "");
                        map.put(code, value);
                    }
                }
            }
        }
    }

    private static String handleDateType(String value, String dateType) {
        if ("yyyy-mm-dd".equals(dateType)) { // yyyy-MM
            value = DateUtils.format(DateUtils.parseDate(value, "yyyy-MM-dd"));
        } else if ("dd/mm/yyyy".equals(dateType)) {
            value = DateUtils.format(DateUtils.parseDate(value, "dd/MM/yyyy"));
        } else if ("yyyy-mm-dd HH:ii".equals(dateType)) {
            value = DateUtils.format(DateUtils.parseDate(value, "yyyy-MM-dd HH:mm"));
        } else if ("dd/mm/yyyy HH:ii".equals(dateType)) {
            value = DateUtils.format(DateUtils.parseDate(value, "dd/MM/yyyy HH:mm"));
        } else if ("yyyy-mm-dd HH:ii:ss".equals(dateType)) {
            value = DateUtils.format(DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss"));
        } else if ("yyyy-mm".equals(dateType)) {
            value = DateUtils.format(DateUtils.parseDate(value, "yyyy-MM"));
        } else if ("yyyy".equals(dateType)) {
            value = DateUtils.format(DateUtils.parseDate(value, "yyyy"));
        } else if ("HH:ii".equals(dateType)) {
            value = DateUtils.format(DateUtils.parseDate(value, "HH:mm"));
        }
        return value;
    }
}
