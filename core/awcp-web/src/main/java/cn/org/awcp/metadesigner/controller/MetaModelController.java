package cn.org.awcp.metadesigner.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.metadesigner.application.DataSourceManageService;
import cn.org.awcp.metadesigner.application.MetaModelClassService;
import cn.org.awcp.metadesigner.application.MetaModelItemService;
import cn.org.awcp.metadesigner.application.MetaModelService;
import cn.org.awcp.metadesigner.application.SysSourceRelationService;
import cn.org.awcp.metadesigner.util.CreateTables;
import cn.org.awcp.metadesigner.vo.DataSourceManageVO;
import cn.org.awcp.metadesigner.vo.MetaModelClassVO;
import cn.org.awcp.metadesigner.vo.MetaModelItemsVO;
import cn.org.awcp.metadesigner.vo.MetaModelVO;
import cn.org.awcp.metadesigner.vo.SysDataSourceVO;
import cn.org.awcp.unit.service.PunSystemService;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.util.BeanUtil;

@Controller
@RequestMapping("/metaModel")
public class MetaModelController extends BaseController {
    /**
     * 日志对象
     */
    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private MetaModelService metaModelServiceImpl;

    @Autowired
    private MetaModelItemService metaModelItemsServiceImpl;

    @Autowired
    private MetaModelClassService metaModelClassServiceImpl;

    @Autowired
    private CreateTables createTables;

    @Autowired
    @Qualifier("dataSourceManageServiceImpl")
    DataSourceManageService dataSourceService;

    @Autowired
    @Qualifier("punSystemServiceImpl")
    PunSystemService sysService;

    @Autowired
    @Qualifier("sysSourceRelationServiceImpl")
    SysSourceRelationService sysSourceRelationService;
    @Autowired
    private SzcloudJdbcTemplate jdbcTemplate;

    @ResponseBody
    @RequestMapping(value = "/addByDb")
    public String addByDb(String datasourceJson, String[] tableNames, Long systemId) {
        JSONObject rtn = new JSONObject();
        try {
            for (int i = 0; i < tableNames.length; i++) {
                MetaModelVO modelVO = metaModelServiceImpl.queryByModelCode(tableNames[i]);
                modelVO = BeanUtil.instance(modelVO, MetaModelVO.class);
                if (StringUtils.isBlank(modelVO.getTableName()) || StringUtils.isBlank(modelVO.getId())) {
                    modelVO.setModelCode(tableNames[i]);
                    modelVO.setModelName(tableNames[i]);
                    modelVO.setTableName(tableNames[i]);
                    modelVO.setSystemId(ControllerHelper.getSystemId());
                    String id = metaModelServiceImpl.save(modelVO);
                    modelVO.setId(id);
                } else {
                    this.metaModelItemsServiceImpl.removeByFk(modelVO.getId());
                }
                syncMeta(modelVO);
            }
        } catch (Exception e) {
            rtn.put("result", "-1");
            logger.info("ERROR", e);
        }

        rtn.put("result", "1");

        return rtn.toJSONString();
    }

    @RequestMapping(value = "/synchronizedMeta")
    public String synchronizedMeta(@RequestParam(value = "id") String[] id) {
        JSONObject rtn = new JSONObject();
        try {
            for (int i = 0; i < id.length; i++) {
                this.metaModelItemsServiceImpl.removeByFk(id[i]);
                MetaModelVO mmv = this.metaModelServiceImpl.get(id[i]);
                syncMeta(mmv);
            }
        } catch (Exception e) {
            logger.info("ERROR", e);
            rtn.put("result", "0");
        }
        rtn.put("result", "1");

        return "redirect:queryResult.do";
    }

    /**
     * 同步表到元数据
     */
    private void syncMeta(MetaModelVO mmv) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SHOW FULL FIELDS FROM " + mmv.getTableName());
        for (Map<String, Object> map : list) {
            MetaModelItemsVO item = new MetaModelItemsVO();
            String Field = (String) map.get("Field");
            String Type = (String) map.get("Type");
            String key = (String) map.get("Key");
            String Null = (String) map.get("Null");
            String Default = (String) map.get("Default");
            String Comment = (String) map.get("Comment");
            if ("PRI".equals(key)) {
                item.setUsePrimaryKey(1);
                item.setUseIndex(-1);
            } else if ("UNI".equals(key)) {
                item.setUseIndex(1);
            } else if ("MUL".equals(key)) {
                item.setUseIndex(2);
            } else {
                item.setUseIndex(0);
            }
            int index = Type.indexOf("(");
            if (index != -1) {
                String length = Type.substring(index + 1, Type.length() - 1);
                item.setItemLength(length);
                Type = Type.replace("(" + length + ")", "");
            }
            if ("NO".equals(Null)) {
                item.setUseNull(1);
            } else {
                item.setUseNull(0);
            }
            item.setRemark(Comment);
            item.setModelId(mmv.getId());
            item.setItemName(Field);
            item.setItemCode(Field);
            item.setItemType(Type);
            item.setItemValid(1);
            item.setDefaultValue(Default);
            metaModelItemsServiceImpl.save(item);
        }
    }

    /**
     * 复制
     *
     * @param _selects
     * @return
     */
    @RequestMapping(value = "/copy")
    public ModelAndView copy(@RequestParam(value = "id") String[] _selects) {
        for (int i = 0; i < _selects.length; i++) {
            String id = _selects[i];
            MetaModelVO vo = metaModelServiceImpl.get(id);
            if (vo == null) {
                continue;
            }
            vo.setId(null);
            vo.setModelName(vo.getModelName() + " - copy");
            String newId = metaModelServiceImpl.save(vo);

            BaseExample baseExample = new BaseExample();
            Criteria criteria = baseExample.createCriteria();
            criteria.andEqualTo("modelId", id);

            List<MetaModelItemsVO> items = metaModelItemsServiceImpl.selectPagedByExample(baseExample, 1, 900, null);
            if (items == null) {
                continue;
            }
            for (int k = 0; k < items.size(); k++) {
                MetaModelItemsVO it = items.get(k);
                it.setId(null);
                it.setModelId(newId);
                metaModelItemsServiceImpl.save(it);
            }
        }
        return new ModelAndView("redirect:queryResult.do");
    }

    /**
     * 复制到指定系统
     *
     * @param _selects
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/copyToSys")
    public String copyToSys(String[] _selects, String systemId) {
        JSONObject o = new JSONObject();
        if (StringUtils.isNotBlank(systemId)) {
            for (int i = 0; i < _selects.length; i++) {
                String id = _selects[i];
                MetaModelVO vo = metaModelServiceImpl.get(id);
                if (vo == null) {
                    continue;
                }
                vo.setId(null);
                vo.setModelName(vo.getModelName() + " - copy");
                vo.setSystemId(Long.valueOf(systemId));
                String newId = metaModelServiceImpl.save(vo);

                BaseExample baseExample = new BaseExample();
                Criteria criteria = baseExample.createCriteria();
                criteria.andEqualTo("modelId", id);

                List<MetaModelItemsVO> items = metaModelItemsServiceImpl.selectPagedByExample(baseExample, 1, 900,
                        null);
                if (items == null) {
                    continue;
                }
                for (int k = 0; k < items.size(); k++) {
                    MetaModelItemsVO it = items.get(k);
                    it.setId(null);
                    it.setModelId(newId);
                    metaModelItemsServiceImpl.save(it);
                }
            }
            o.put("rtn", 0);
            o.put("msg", "复制成功");
        } else {
            o.put("rtn", 1);
            o.put("msg", "请选择系统");
        }
        return o.toJSONString();
    }

    /**
     * 增加
     *
     * @param vo
     * @param model
     * @return
     */
    @RequestMapping(value = "/save")
    public String save(@ModelAttribute MetaModelVO vo, Model model) {
        // 判读元数据是否存在 存在不增加 不存在则增加
        try {
            List<MetaModelVO> mmo = this.metaModelServiceImpl.queryMetaModel("queryMetaModel", vo.getModelCode(),
                    vo.getTableName(), vo.getProjectName());
            if (mmo.isEmpty()) {
                vo.setSystemId(ControllerHelper.getSystemId());
                vo.setModelSynchronization(false);
                vo.setModelValid(false);
                this.metaModelServiceImpl.save(vo);
            } else {
                logger.debug("该元数据已经存在");
            }
        } catch (Exception e) {
            logger.info("ERROR", e);
            return null;
        }
        model.addAttribute("model", vo);
        return "redirect:queryResult.do";
    }

    /**
     * 增加
     *
     * @param vo
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saves")
    public Map<String, Object> saves(@ModelAttribute MetaModelVO vo, Model model) {
        // 判读元数据是否存在 存在不增加 不存在则增加
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<MetaModelVO> mmo = this.metaModelServiceImpl.queryMetaModel("queryMetaModel", vo.getModelCode(),
                    vo.getTableName(), vo.getProjectName());
            if (!(mmo.size() > 0)) {
                Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
                if (obj instanceof PunSystemVO) {
                    PunSystemVO system = (PunSystemVO) obj;
                    vo.setSystemId(system.getSysId());
                }
                vo.setModelSynchronization(false);
                vo.setModelValid(false);
                String id = this.metaModelServiceImpl.save(vo);
                map.put("id", id);
            } else {
                logger.debug("该元数据已经存在");
                map.put("id", 0);
            }
        } catch (Exception e) {
            logger.info("ERROR", e);
            map.put("id", 0);
            return map;
        }
        model.addAttribute("model", vo);
        return map;
    }

    /**
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/removeModel")
    public String removeModel(String[] id, Model model) {
        try {
            // 查询对应的关系
            for (int i = 0; i < id.length; i++) {
                if (StringUtils.isNotBlank(id[i])) {
                    // 查询元数据
                    MetaModelVO mmo = this.metaModelServiceImpl.get(id[i]);
                    this.metaModelItemsServiceImpl.removeByFk(id[i]);
                    this.metaModelServiceImpl.remove(mmo);
                }
            }
            return "redirect:queryResult.do";
        } catch (Exception e) {
            logger.info("ERROR", e);
            int i = 1;
            String str = "{id:" + i + "}";
            return str;
        }
    }

    /**
     * 删除元数据并删除数据库表
     *
     * @param classId
     * @param model
     * @return
     */
    @RequestMapping(value = "/remove")
    public String remove(String[] id, Model model) {
        try {
            for (String i : id) {
                if (StringUtils.isNotBlank(i)) {
                    // 查询元数据
                    MetaModelVO mmo = this.metaModelServiceImpl.get(i);
                    if (mmo != null) {
                        this.metaModelItemsServiceImpl.removeByFk(i);
                        this.metaModelServiceImpl.remove(mmo);
                        String sql = "drop table " + mmo.getTableName();
                        try {
                            this.jdbcTemplate.execute(sql);
                        } catch (Exception e) {
                        }
                        return "redirect:queryResult.do";
                    }
                }
            }

        } catch (Exception e) {
            logger.info("ERROR", e);
        }
        String str = "{id:0}";
        return str;
    }

    /**
     * 创建数据库表
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/createTable", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult createTable(@RequestParam(value = "id") String id) {
        ReturnResult result = ReturnResult.get();
        MetaModelVO vo = this.metaModelServiceImpl.get(id);
        // 判断表是否存在
        try {
            jdbcTemplate.beginTranstaion();
            if (metaModelServiceImpl.tableIsExist(vo.getTableName())) {
                jdbcTemplate.update("drop table " + vo.getTableName());
            }
            List<MetaModelItemsVO> pl = this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getId());
            String sql = createTables.getSql(vo, pl);
            jdbcTemplate.update(sql);
            vo.setModelSynchronization(true);
            this.metaModelServiceImpl.update("update", vo);
            jdbcTemplate.commit();
            result.setStatus(StatusCode.SUCCESS);
        } catch (Exception e) {
            jdbcTemplate.rollback();
            logger.info("ERROR", e);
            result.setStatus(StatusCode.FAIL.setMessage(e.getMessage()));
        }

        return result;
    }

    /**
     * /点击发布
     *
     * @param vo
     * @param model
     * @return
     */
    @RequestMapping(value = "/release")
    public String release(@RequestParam(value = "id") String idss, Model model,
                          @RequestParam(required = false, defaultValue = "1") int currentPage) {
        MetaModelVO vo = this.metaModelServiceImpl.get(idss);
        vo.setModelSynchronization(true);
        metaModelServiceImpl.update("update", vo);
        model.addAttribute("currentPage", currentPage);
        return "redirect:queryResult.do";
    }

    /**
     * 页面跳转 用于所需的页面跳转 点击
     *
     * @param model
     * @return
     */
    @RequestMapping("toggle")
    public String toggle(Model model) {
        // try {
        // List<PunSystemVO> list = this.sysService.findAll();
        // model.addAttribute("project", list);
        // Object obj =
        // SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
        // PunSystemVO system = null;
        // if (obj instanceof PunSystemVO) {
        // system = (PunSystemVO) obj;
        // List<MetaModelClassVO> classVos =
        // metaModelClassServiceImpl.queryBySystemId(system.getSysId());
        // model.addAttribute("classVos", classVos);
        // }
        // List<SysDataSourceVO> sysDataRela =
        // sysSourceRelationService.getSystemDataSource(system.getSysId());
        // List<DataSourceManageVO> dataS = new ArrayList<DataSourceManageVO>();
        // if (sysDataRela != null && sysDataRela.size() > 0) {
        // for (SysDataSourceVO dataVo : sysDataRela) {
        // String sourceId = dataVo.getDataSourceId();
        // DataSourceManageVO datasource = dataSourceService.get(sourceId);
        // dataS.add(datasource);
        //
        // }
        // model.addAttribute("dataSources", dataS);
        // }

        // 默认数据源
        // BaseExample base = new BaseExample();
        // base.createCriteria().andEqualTo("SYSTEM_ID",
        // system.getSysId()).andEqualTo("ISDEFAULT", true);
        // PageList<SysDataSourceVO> dataVos =
        // sysSourceRelationService.selectPagedByExample(base, 1,
        // Integer.MAX_VALUE, null);
        // if (dataVos != null && dataVos.size() > 0) {
        // model.addAttribute("defaultDataSourceId", dataVos.get(0).getDataSourceId());
        // }

        // } catch (Exception e) {
        // logger.info("ERROR", e);
        // }
        return "metadesigner/metaModel/metaModel_add";
    }

    @ResponseBody
    @RequestMapping("getDataSourceManageVOById")
    public DataSourceManageVO getDataSourceManageVOById(String sourceId) {
        DataSourceManageVO datasource = dataSourceService.get(sourceId);
        return datasource;
    }

    /**
     * 查询所有
     *
     * @param model
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findAll")
    public String findAll(Model model) throws Exception {
        try {
            List<MetaModelClassVO> list = this.metaModelClassServiceImpl.findAll();
            model.addAttribute("class", list);
            List<MetaModelVO> result = this.metaModelServiceImpl.findAll();
            logger.debug(result.size() + "");
            model.addAttribute("list", result);
            return "metadesigner/metaModel/findAll";
        } catch (Exception e) {
            logger.info("ERROR", e);
        }
        return null;
    }

    /**
     * 查询一条数据 用于修改按钮
     *
     * @param id
     * @param pw
     * @return
     */
    @RequestMapping(value = "/get")
    public String get(String id, Model model) {
        MetaModelVO mmo = new MetaModelVO();
        try {
            mmo = this.metaModelServiceImpl.get(id);
            model.addAttribute("vo", mmo);
        } catch (Exception e) {
            logger.info("ERROR", e);
            return null;
        }
        return "metadesigner/metaModel/metaModel_edit";
    }

    /**
     * 查询一条数据 用于修改按钮
     *
     * @param id
     * @param pw
     * @return
     */
    @RequestMapping(value = "/getDataSource")
    public String getDataSource(String _selects, Model model) {
        try {
            Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
            PunSystemVO system = null;
            if (obj instanceof PunSystemVO) {
                system = (PunSystemVO) obj;
            }
            List<SysDataSourceVO> sysDataRela = sysSourceRelationService.getSystemDataSource(system.getSysId());
            List<DataSourceManageVO> dataS = new ArrayList<DataSourceManageVO>();
            if (sysDataRela != null && sysDataRela.size() > 0) {
                for (SysDataSourceVO dataVo : sysDataRela) {
                    String sourceId = dataVo.getDataSourceId();
                    DataSourceManageVO datasource = dataSourceService.get(sourceId);
                    dataS.add(datasource);

                }
                model.addAttribute("dataSources", dataS);
            }

            // 默认数据源
            BaseExample base = new BaseExample();
            base.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andEqualTo("ISDEFAULT", true);
            PageList<SysDataSourceVO> dataVos = sysSourceRelationService.selectPagedByExample(base, 1,
                    Integer.MAX_VALUE, null);
            if (dataVos != null && dataVos.size() > 0) {
                model.addAttribute("defaultDataSourceId", dataVos.get(0).getDataSourceId());
            }
            model.addAttribute("_selects", _selects);
        } catch (Exception e) {
            logger.info("ERROR", e);
            return null;
        }
        return "metadesigner/metaModel/batchModifyDS";
    }

    /**
     * 分页查询
     *
     * @param vo
     * @param currentPage
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/queryResult")
    public String queryResult(@ModelAttribute MetaModelVO vo,
                              @RequestParam(required = false, defaultValue = "1") int currentPage, Model model) {
        int pageSize = 20;
        String queryStr = "queryList";
        Map<String, Object> map = new HashMap<String, Object>();
        if (currentPage == 0) {
            currentPage = 1;
        }
        map.put("modelClassId", vo.getModelClassId());
        map.put("modelName", vo.getModelName());
        map.put("modelCode", vo.getModelCode());
        map.put("tableName", vo.getTableName());
        map.put("projectName", vo.getProjectName());
        PageList<MetaModelVO> pl = this.metaModelServiceImpl.queryResult(queryStr, map, currentPage, pageSize,
                null);
        model.addAttribute("list", pl);
        model.addAttribute("currentPage", currentPage);
        return "metadesigner/metaModel/findAll";
    }

    /**
     * 修改
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/update")
    public String update(@ModelAttribute MetaModelVO vo) {
        vo.setModelSynchronization(false);
        vo.setModelValid(false);
        // 查看表名是否修改
        MetaModelVO vvo = this.metaModelServiceImpl.get(vo.getId());
        // 判断表名是否修改
        if (!vo.getTableName().equals(vvo.getTableName())) {
            // 查看这张表是否存在
            if (this.metaModelServiceImpl.tableIsExist(vvo.getTableName())) {
                // 修改表名
                StringBuffer sb = new StringBuffer("ALTER TABLE ").append(vvo.getTableName()).append(" RENAME TO ")
                        .append(vo.getTableName());
                this.metaModelServiceImpl.excuteSql(sb.toString());
            }
        }
        this.metaModelServiceImpl.update("update", vo);
        return "redirect:queryResult.do";
    }

    /**
     * 批量修改 元数据配置的数据源
     *
     * @param _selects
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "batchModifyDS")
    public String batchModifyDS(@RequestParam(value = "_selects") String _selects, String dataSourceId) {
        String[] ids = _selects.split(",");
        for (int i = 0; i < ids.length; i++) {

            MetaModelVO vo = new MetaModelVO();
            vo.setId(ids[i]);
            vo.setDataSourceId(dataSourceId);
            this.metaModelServiceImpl.update("updateDataSourceById", vo);
        }
        return "1";
    }

    /**
     * 根据modelCode,modelName 模糊查询
     *
     * @param modelCode
     * @param modelName
     * @param page
     * @param rows
     * @return
     * @throws SQLException
     */
    @ResponseBody
    @RequestMapping("/queryPageByModel")
    public List<Map<String, Object>> queryPageByModel(String modelCode, String modelName,
                                                      @RequestParam(required = false, defaultValue = "1") int page) throws SQLException {
        StringBuffer b = new StringBuffer(
                "select modelClassId,modelCode,modelName,modelDesc,tableName,projectName,modelType,modelSynchronization,modelValid,id ,system_id from fw_mm_metaModel where 1=1");
        Object[] objs = null;
        if (modelCode != null && "".equals(modelCode)) {
            b.append(" and modelCode like %");
            b.append(modelCode).append("%");

        }
        if (modelName != null && "".equals(modelName)) {
            b.append(" and modelName like %");
            b.append(modelName).append("%");
        }
        Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
        if (obj instanceof PunSystemVO) {
            PunSystemVO system = (PunSystemVO) obj;
            b.append(" and SYSTEM_ID=").append(system.getSysId());
        }
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(b.toString(), objs);
        return list;

    }

    @RequestMapping(value = "/selectPage")
    public String selectPageByExample(@ModelAttribute MetaModelVO vo, Model model,
                                      @RequestParam(required = false, defaultValue = "1") int currentPage,
                                      @RequestParam(required = false, defaultValue = "25") int pageSize) {
        BaseExample be = new BaseExample();
        String modelName = vo.getModelName();
        Criteria c = be.createCriteria();
        if (currentPage == 0) {
            currentPage = 1;
        }
        if (modelName != null) {
            c.andLike("modelName", "%" + modelName + "%");
        }
        String modelCode = vo.getModelCode();
        if (modelCode != null && !"".equals(modelCode)) {
            c.andLike("modelCode", "%" + modelCode + "%");
        }
        String tableName = vo.getTableName();
        if (tableName != null && !"".equals(tableName)) {
            c.andLike("tableName", "%" + tableName + "%");
        }
        String projectName = vo.getProjectName();
        if (projectName != null && !"".equals(projectName)) {
            c.andLike("projectName", "%" + projectName + "%");
        }
        Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
        if (obj instanceof PunSystemVO) {
            PunSystemVO system = (PunSystemVO) obj;
            c.andEqualTo("SYSTEM_ID", system.getSysId());
        }
        PageList<MetaModelVO> pl = this.metaModelServiceImpl.selectPagedByExample(be, currentPage, pageSize,
                "seq desc");
        model.addAttribute("list", pl);
        model.addAttribute("currentPage", currentPage);
        return "metadesigner/metaModel/findAll";

    }

    @ResponseBody
    @RequestMapping(value = "queryModelItemByModel")
    public List<MetaModelItemsVO> queryModelItemByModel(String modelCode) {
        try {
            MetaModelVO vo = this.metaModelServiceImpl.queryByModelCode(modelCode);
            List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getId());
            return ls;
        } catch (Exception e) {
            return null;
        }
    }

    /*
     *
     * 返回界面
     *
     */
    @RequestMapping(value = "getGenerator")
    public ModelAndView getGenerator() {
        // 如果希望传递进来的数据同样也要传回去，则使用@ModelAttribute
        ModelAndView mv = new ModelAndView();
        mv.setViewName("metadesigner/generator/GeneratorUI");
        return mv;
    }

    /*
     * 调用代码生成器
     *
     */
    @RequestMapping(value = "/generator", method = RequestMethod.POST)
    public ModelAndView generatorMain(String basePackage, String persistence, String outRoot, String templementSrc,
                                      String tableName) throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("metadesigner/generator/GeneratorUI");
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("basePackage", basePackage);
            map.put("persistence", persistence);
            map.put("outRoot", outRoot);
            map.put("templementSrc", templementSrc);
            map.put("tableName", tableName);
            // GeneratorMain.StartGenerator(map);
            mv.addObject("result", "生成成功");
        } catch (Exception e) {
            logger.info("ERROR", e);
            mv.addObject("result", "生成失败");
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/queryModel")
    public MetaModelVO queryModel(String modelCode) {
        MetaModelVO m = this.metaModelServiceImpl.queryByModelCode(modelCode);
        return m;
    }

    // --------------------------数据校验----------------------------

    /**
     * 数据校验
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dataValidate")
    public Object queryMetaModel(@RequestParam(required = false, defaultValue = "") String modelCode,
                                 @RequestParam(required = false, defaultValue = "") String tableName,
                                 @RequestParam(required = false, defaultValue = "") String projectName,
                                 @RequestParam(required = false) String id) {
        logger.debug(id + "");
        if (StringUtils.isBlank(id)) {
            List<MetaModelVO> ls = this.metaModelServiceImpl.queryMetaModel("queryMetaModel", modelCode, tableName,
                    projectName);
            return ls.size();
        } else {
            MetaModelVO mm = this.metaModelServiceImpl.get(id);
            List<MetaModelVO> ls = this.metaModelServiceImpl.queryMetaModel("queryMetaModel", modelCode, tableName,
                    projectName);

            for (int i = 0; i < ls.size(); i++) {
                MetaModelVO mmv = ls.get(i);
                if (mmv.getModelCode().equals(mm.getModelCode()) || mmv.getTableName().equals(mm.getTableName())
                        || mmv.getProjectName().equals(mm.getProjectName())) {
                    ls.remove(i);
                }
            }
            return ls.size();
        }

    }

    /**
     * 将汉字转化为拼音
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    // @ResponseBody
    // @RequestMapping(value="/pinyin")
    // public Map<String,String> pinYin(String str){
    // String s=PinYin4jUtilImpl.getPinYin(str);
    // Map<String,String> map=new HashMap<String, String>();
    // map.put("str", s);
    // return map;
    // }

}
