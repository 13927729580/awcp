package cn.org.awcp.metadesigner.controller;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.common.db.DynamicDataSource;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.metadesigner.application.DataSourceManageService;
import cn.org.awcp.metadesigner.application.MetaModelService;
import cn.org.awcp.metadesigner.application.SysSourceRelationService;
import cn.org.awcp.metadesigner.vo.DataSourceManageVO;
import cn.org.awcp.metadesigner.vo.MetaModelVO;
import cn.org.awcp.metadesigner.vo.SysDataSourceVO;
import cn.org.awcp.unit.service.PunSystemService;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import com.alibaba.druid.pool.DruidDataSource;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/dataSourceManage")
public class DataSourceManageController extends BaseController {

    @Autowired
    private DataSourceManageService dataSourceManageServiceImpl;
    @Autowired
    private MetaModelService metaModelServiceImpl;
    @Autowired
    @Qualifier("sysSourceRelationServiceImpl")
    SysSourceRelationService sysSourceRelationService;
    @Autowired
    @Qualifier("punSystemServiceImpl")
    PunSystemService sysService;

    @Autowired
    @Qualifier("dataSourceManageServiceImpl")
    DataSourceManageService dataSourceService;

    @Autowired
    private DynamicDataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 显示该单位下的所有数据源
     */
    @RequestMapping(value = "/selectPage")
    public String selectPagedByExample(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                       @RequestParam(required = false, defaultValue = "10") int pageSize, @ModelAttribute DataSourceManageVO vo,
                                       Model model) {
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        BaseExample be = new BaseExample();
        Criteria c = be.createCriteria();
        String name = vo.getName();
        if (name != null && !"".equals(name)) {
            c.andLike("name", "%" + name + "%");
        }
        String url = vo.getSourceUrl();
        if (url != null && !"".equals(url)) {
            c.andLike("sourceUrl", "%" + url + "%");
        }

        PageList<DataSourceManageVO> pl = this.dataSourceManageServiceImpl.selectPagedByExample(be, currentPage,
                pageSize, "id desc");
        model.addAttribute("list", pl);
        model.addAttribute("currentPage", currentPage);

        return "metadesigner/dataSource/dataSource_list";
    }

    /**
     * 列出某系统下关联的数据源
     *
     * @param vo
     * @param systemId
     * @return
     */
    @RequestMapping(value = "/listDataSourceInSys")
    public ModelAndView listDataSourceInSys(DataSourceManageVO vo, Long systemId,
                                            @RequestParam(required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 10 : pageSize;
        ModelAndView mv = new ModelAndView();
        mv.setViewName("metadesigner/dataSource/dataSource_list");
        BaseExample example = new BaseExample();
        Criteria c = example.createCriteria();
        c.andEqualTo("systemId", systemId);
        String name = vo.getName();
        if (StringUtils.isNotBlank(name)) {
            c.andLike("name", "%" + name + "%");
        }
        String url = vo.getSourceUrl();
        if (StringUtils.isNotBlank(url)) {
            c.andLike("sourceUrl", "%" + url + "%");
        }
        PageList<DataSourceManageVO> list = dataSourceManageServiceImpl.selectPagedByExample(example, currentPage,
                pageSize, "id desc");
        mv.addObject("vos", list);
        mv.addObject("currentPage", currentPage);
        mv.addObject("pageSize", pageSize);
        return mv;
    }


    @ResponseBody
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ReturnResult saveByAjax(DataSourceManageVO vo) {
        ReturnResult result=ReturnResult.get();
        vo.setSourceUrl(StringEscapeUtils.unescapeHtml4(vo.getSourceUrl()));
        if (StringUtils.isNotBlank(vo.getId())) {
            DataSourceManageVO dataSourceVo = this.dataSourceManageServiceImpl.get(vo.getId());
            BeanUtils.copyProperties(vo, dataSourceVo, true);
            dataSource.put(dataSourceVo.getName(), getDruidDataSource(dataSourceVo));
            dataSourceManageServiceImpl.update(dataSourceVo);
            result.setStatus(StatusCode.SUCCESS).setData(1);
        } else {
            if (this.dataSourceManageServiceImpl.queryDataSourceByName(vo.getName()) == null) {
                vo.setDomain("remote");
                vo.setCreateTime(new Date(System.currentTimeMillis()));
                vo.setLastModifyTime(vo.getCreateTime());
                vo.setLastModifier(ControllerHelper.getUserId().toString());
                vo.setMaximumActiveTime(60000);
                vo.setSimultaneousBuildThrottle(6);
                this.dataSourceManageServiceImpl.save(vo);
                dataSource.put(vo.getName(), getDruidDataSource(vo));
                result.setStatus(StatusCode.SUCCESS).setData(1);
            } else {
                result.setStatus(StatusCode.SUCCESS).setData(0);
            }
        }
        return result;
    }

    private DruidDataSource getDruidDataSource(@ModelAttribute DataSourceManageVO vo) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(vo.getSourceUrl());
        druidDataSource.setDriverClassName(vo.getSourceDriver());
        druidDataSource.setUsername(vo.getUserName());
        druidDataSource.setPassword(vo.getUserPwd());
        druidDataSource.setInitialSize(vo.getPrototypeCount());
        druidDataSource.setMaxActive(vo.getMaximumConnectionCount());
        druidDataSource.setMinIdle(vo.getMinimumConnectionCount());
        return druidDataSource;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    // @ResponseBody
    @RequestMapping(value = "/remove")
    public String remove(String[] id) {
        if(id!=null){
            for(String i:id){
                if(StringUtils.isNotBlank(i)){
                    DataSourceManageVO dsmv = this.dataSourceManageServiceImpl.get(i);
                    this.dataSourceManageServiceImpl.delete(dsmv);
                    if(dsmv!=null){
                        dataSource.remove(dsmv.getName());
                    }
                }
            }
        }
        return "redirect:selectPage.do";
    }


    /**
     * 查询一条数据
     *
     * @param id
     * @return
     */
    // @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String get(String id, Model model) {
        if (StringUtils.isNotBlank(id)) {
            model.addAttribute("vo", this.dataSourceManageServiceImpl.get(id));
        }
        return "metadesigner/dataSource/dataSourceRemote_edit";
    }


    /**
     * 测试数据源是否有效
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/testDataSource")
    public ReturnResult testDataSource(@ModelAttribute DataSourceManageVO vo) throws Exception {
        ReturnResult result = ReturnResult.get();
        Connection conn = null;
        try {
            Class.forName(vo.getSourceDriver());
            conn = DriverManager.getConnection(vo.getSourceUrl(), vo.getUserName(), vo.getUserPwd());
            result.setStatus(StatusCode.SUCCESS).setData(1);
        } catch (Exception e) {
            result.setStatus(StatusCode.SUCCESS).setData(0);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return result;
    }

    @RequestMapping("/table")
    public ModelAndView tableList(String id) {
        ModelAndView mv = new ModelAndView();
        String queryStr = "queryList";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("modelSynchronization", true);
        PageList<MetaModelVO> pl = metaModelServiceImpl.queryResult(queryStr, map, 1, Integer.MAX_VALUE, "id.desc");
        mv.addObject("tables", pl);
        mv.setViewName("metadesigner/dataSource/table_edit");
        return mv;
    }

    /**
     * 跳转至数据源编辑页面
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/edit-ds-info")
    public ModelAndView editDataSourceInfo(DataSourceManageVO vo) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("metadesigner/dataSource/dataSourceRemote_edit");
        if (StringUtils.isNotBlank(vo.getId())) {
            vo = dataSourceManageServiceImpl.get(vo.getId());
            mv.addObject("vo", vo);
        }
        return mv;
    }

    @RequestMapping(value = "/edit-ds-info-temp")
    public ModelAndView editDataSourceInfoTemp(DataSourceManageVO vo) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("metadesigner/dataSource/dataSource_tmp_metamodel");
        if (vo.getId() != null) {
            vo = dataSourceManageServiceImpl.get(vo.getId());
            mv.addObject("vo", vo);
        }
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
            mv.addObject("vo", dataS != null ? dataS.get(0) : null);
        }

        // 默认数据源
        BaseExample base = new BaseExample();
        base.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andEqualTo("ISDEFAULT", true);
        PageList<SysDataSourceVO> dataVos = sysSourceRelationService.selectPagedByExample(base, 1, Integer.MAX_VALUE,
                null);
        if (dataVos != null && dataVos.size() > 0) {
            mv.addObject("defaultDataSourceId", dataVos.get(0).getDataSourceId());
        }
        return mv;
    }

    /**
     * 根据用户输入的数据库连接信息，获取该数据库的所有表名，并返回给用户，同时应该保存一份用户输入的数据库连接信息
     *
     * @return
     */
    @RequestMapping(value = "/listTables")
    public ModelAndView listTables() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("metadesigner/dataSource/dataSource_tmp_metamodel_listTables");
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tableRet = metaData.getTables(null, "%", "%", new String[] { "TABLE" });
            List<String> tableNames = new ArrayList<>();
            while (tableRet.next()) {
                tableNames.add(tableRet.getString("TABLE_NAME"));
            }
            mv.addObject("tables", tableNames);
        } catch (Exception e) {
            logger.info("ERROR", e);
        }
        return mv;
    }
}
