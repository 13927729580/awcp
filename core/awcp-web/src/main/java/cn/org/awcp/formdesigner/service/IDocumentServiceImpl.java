package cn.org.awcp.formdesigner.service;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.application.vo.PageActVO;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.core.engine.FreeMarkers;
import cn.org.awcp.formdesigner.utils.DocUtils;
import cn.org.awcp.formdesigner.utils.ScriptEngineUtils;
import cn.org.awcp.venson.exception.PlatformException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档操作类
 *
 * @author Veonson
 * @version 20180301
 */
@Service("iDocumentServiceImpl")
public class IDocumentServiceImpl implements IDocumentService {



    @Autowired
    private FormdesignerService formdesignerServiceImpl;

    @Autowired
    private DocumentService documentServiceImpl;

    @Autowired
    private StoreService storeServiceImpl;

    @Override
    public String view(String dynamicPageId, HttpServletRequest request) throws ScriptException {
        //判断dynamicPageId是否合法
        if (StringUtils.isBlank(dynamicPageId)) {
            throw new PlatformException("动态页面ID不存在");
        }
        DynamicPageVO pageVO = formdesignerServiceImpl.findById(dynamicPageId);
        if (pageVO == null) {
            throw new PlatformException("动态页面未找到");
        }
        DocumentVO docVo;
        String docId = request.getParameter("docId");
        if (StringUtils.isBlank(docId)) {
            String id = request.getParameter("id");
            if (StringUtils.isBlank(id)) {
                docVo=new DocumentVO();
                docVo.setUpdate(false);
            } else {
                //通过记录ID和动态页面ID查找文档类
                BaseExample example = new BaseExample();
                Criteria criteria = example.createCriteria();
                criteria.andEqualTo("DYNAMICPAGE_ID",dynamicPageId);
                criteria.andEqualTo("RECORD_ID",id);
                List<DocumentVO> list =  documentServiceImpl.selectPagedByExample(example,1,1,null);
                if(list!=null&&!list.isEmpty()){
                    docVo = list.get(0);
                    docVo.setUpdate(true);
                }else{
                    docVo=new DocumentVO();
                    docVo.setUpdate(false);
                }
            }
        } else {
            docVo = documentServiceImpl.findById(docId);
            docVo.setUpdate(true);
        }
        Map<String, String[]> enumeration = request.getParameterMap();
        Map<String, String> params = new HashMap<>(enumeration.size());
        for (Map.Entry<String, String[]> entry : enumeration.entrySet()) {
            params.put(entry.getKey(), StringUtils.join(entry.getValue(), ";"));
        }
        docVo.setRequestParams(params);
        docVo.setDynamicPageId(pageVO.getId());
        docVo.setDynamicPageName(pageVO.getName());
        // 把列表页面配置的属性加到docVo中，在模板解析的时候使用
        docVo.setShowTotalCount(pageVO.getShowTotalCount());
        docVo.setIsLimitPage(pageVO.getIsLimitPage());
        docVo.setPageSize(pageVO.getPageSize());
        docVo.setShowReverseNum(pageVO.getShowReverseNum());
        docVo.setReverseNumMode(pageVO.getReverseNumMode());
        docVo.setReverseSortord(pageVO.getReverseSortord());


        String isRead = params.get("IsRead");

        Map<String, Object> root = new HashMap<>(16);
        // 拿脚本执行引擎
        ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
        engine.put("request", request);
        engine.put("session", SessionUtils.getCurrentSession());
        engine.put("root", root);
        // 分页
        String currentPage = params.get("currentPage");
        String pageSize = params.get("pageSize");
        if (!StringUtils.isNumeric(currentPage)) {
            currentPage = "1";
        }
        if (!StringUtils.isNumeric(pageSize)) {
            pageSize = docVo.getPageSize() == null ? "10" : docVo.getPageSize() + "";
        }
        String orderBy = params.get("orderBy");
        String allowOrderBy = params.get("allowOrderBy");
        docVo.setAllowOrderBy(allowOrderBy);
        docVo.setOrderBy(orderBy);
        Map<String, List<Map<String, String>>> dataMap = documentServiceImpl
                .initDocumentData(Integer.parseInt(currentPage), Integer.parseInt(pageSize), docVo, engine, pageVO);
        docVo.setListParams(dataMap);
        Map<String, String> others = new HashMap<>(16);
        Map<String, JSONObject> status = new HashMap<>(16);

        JSONObject jcon = new JSONObject();
        jcon.put("relatePageId", pageVO.getId());
        jcon.put("componentType", "");
        List<JSONObject> components = formdesignerServiceImpl.getComponentByContainerWithColumn(jcon);
        DocUtils.calculateCompents(docVo, others, status, components, dataMap, engine, isRead);

        BaseExample actExample = new BaseExample();
        actExample.createCriteria().andEqualTo("dynamicPage_id", pageVO.getId()).andLike("code",
                StoreService.PAGEACT_CODE + "%");
        List<StoreVO> stores = storeServiceImpl.selectPagedByExample(actExample, 1, Integer.MAX_VALUE, null);
        Map<String, Map<String, Object>> pageActStatus = new HashMap<>(16);

        DocUtils.calculateStores(params, stores, pageActStatus, engine, others, isRead);

        //如果存在流程编号则属于流程渲染
        String flowNo=params.get("FK_Flow");
        if (StringUtils.isNotBlank(flowNo)) {
            String entryId = params.get("FK_Node");
            String fid = params.get("FID");
            fid = StringUtils.isNumeric(fid) ? fid : "0";
            String workItemId = params.get("WorkID");

            docVo.setFlowTempleteId(flowNo);
            docVo.setWorkItemId(workItemId);
            docVo.setEntryId(entryId);
            docVo.setWorkflowId(flowNo);
            docVo.setFid(fid);
            // 计算流程节点绑定隐藏只读禁用值
            DocUtils.flowAuthorityResolve(docVo, status);
        }
        root.put("pageActStatus", pageActStatus);

        // 执行页面加载前脚本
        String preLoadScript = StringEscapeUtils.unescapeHtml4(pageVO.getPreLoadScript());
        if (StringUtils.isNotBlank(preLoadScript)) {
            engine.eval(preLoadScript);
        }

        String dataJson = pageVO.getDataJson();

        if (StringUtils.isNotBlank(dataJson)) {
            for (Map.Entry entry:dataMap.entrySet()) {
                Object values = entry.getValue();
                if (values != null) {
                    Paginator page = (((PageList) values).getPaginator());
                    root.put(entry.getKey() + "_paginator", page);
                }
            }
        }
        String templateString = documentServiceImpl.getTemplateString(pageVO);
        root.putAll(docVo.getListParams());
        root.put("others", others);
        root.put("status", status);
        root.put("request", request);
        root.put("session", SessionUtils.getCurrentSession());
        root.put("vo", docVo);
        root.put("currentPage", currentPage);
        root.put("IsRead", isRead);
        return FreeMarkers.renderString(templateString, root);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object execute(String actId,String dynamicPageId,DocumentVO docVo, HttpServletRequest request) throws ScriptException{

        DynamicPageVO pageVO=setDataSorceFromRequestParameter(docVo,dynamicPageId,request);
        return eval(actId,pageVO,docVo,request);
    }

    @Override
    public DynamicPageVO setDataSorceFromRequestParameter(DocumentVO docVo,String dynamicPageId, HttpServletRequest request){
        //如果为空不执行因为手机端渲染没有用动态页面
        if (StringUtils.isNotBlank(dynamicPageId)) {
            DynamicPageVO pageVO = formdesignerServiceImpl.findById(dynamicPageId);
            if (pageVO != null) {
                docVo.setDynamicPageId(dynamicPageId);
                docVo.setDynamicPageName(pageVO.getName());
                //将request中的参数转成map
                Map<String, String[]> enumeration = request.getParameterMap();
                Map<String, String> params = new HashMap<>(enumeration.size());
                for (Map.Entry<String, String[]> entry : enumeration.entrySet()) {
                    params.put(entry.getKey(), StringUtils.join(entry.getValue(), ";"));
                }
                //设置request参数
                docVo.setRequestParams(params);
                //将参数设置到数据源
                documentServiceImpl.processParams(docVo);
                return pageVO;
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object eval(String actId, DynamicPageVO pageVO, DocumentVO docVo, HttpServletRequest request)  throws ScriptException{
        //如果为空不执行，因为可能执行流程时不需要执行动作脚本
        if(StringUtils.isNotBlank(actId)) {
            StoreVO store = storeServiceImpl.findById(actId);
            if (store != null) {
                PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);
                ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
                //服务端脚本不为空则执行
                if (StringUtils.isNotBlank(act.getServerScript())) {
                    String script = StringEscapeUtils.unescapeHtml4(act.getServerScript());
                    engine.put("request", request);
                    engine.put("session", SessionUtils.getCurrentSession());
                    return engine.eval(script);
                }
                //将动作类型保存到docVo里，流程上下文需要使用
                if(docVo!=null&&docVo.getActType()==0){
                    docVo.setActType(act.getActType());
                }

            }

        }
        return null;
    }
}
