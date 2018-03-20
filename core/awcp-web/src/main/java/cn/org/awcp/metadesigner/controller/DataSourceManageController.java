package cn.org.awcp.metadesigner.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
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
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;

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
	private DruidDataSource dataSource;

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

	/**
	 * 增加数据源
	 * 
	 * @param vo
	 * @return
	 */
	// @ResponseBody
	@RequestMapping(value = "/save")
	public String save(@ModelAttribute DataSourceManageVO vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		DataSourceManageVO dsmv = this.dataSourceManageServiceImpl.queryDataSourceByName(vo.getName());
		try {
			if (dsmv == null) {
				this.dataSourceManageServiceImpl.save(vo);
				map.put("id", 1);
			} else {
				map.put("id", 0);
				logger.debug("数据源已经存在，请重新定义数据源");
			}
		} catch (Exception e) {
			map.put("id", 0);
		}
		return "redirect:selectPage.do";// map;
	}

	@ResponseBody
	@RequestMapping(value = "/saveByAjax")
	public String saveByAjax(@ModelAttribute DataSourceManageVO vo) {
		DataSourceManageVO dsmv = null;
		Object obj = ControllerHelper.getUser();
		if (vo.getId() != null) {
			dsmv = dataSourceManageServiceImpl.get(vo.getId());
			dsmv.setName(vo.getName());
			dsmv.setSourceDriver(vo.getSourceDriver());
			dsmv.setSourceUrl(vo.getSourceUrl());
			dsmv.setSourceType(vo.getSourceType());
			dsmv.setUserName(vo.getUserName());
			dsmv.setUserPwd(vo.getUserPwd());
			if (obj instanceof PunUserBaseInfoVO) {
				PunUserBaseInfoVO user = (PunUserBaseInfoVO) obj;
				dsmv.setLastModifier(user.getName());
			}
			dsmv.setLastModifyTime(new Date(System.currentTimeMillis()));
			dataSourceManageServiceImpl.update(dsmv);
			return "2";
		} else {
			dsmv = this.dataSourceManageServiceImpl.queryDataSourceByName(vo.getName());
			if (dsmv == null) {
				vo.setMaximumActiveTime(5);
				vo.setMaximumConnectionCount(20);
				vo.setMinimumConnectionCount(2);
				vo.setPrototypeCount(3);
				vo.setSimultaneousBuildThrottle(6);
				if ("local".equals(vo.getDomain())) {
					vo.setSourceDriver("com.mysql.jdbc.Driver");
					String config = "useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true";
					String url = "jdbc:mysql://10.86.0.29:3306/" + vo.getName() + "?" + config;
					vo.setSourceUrl(url);
					vo.setSourceType("MYSQL");
					vo.setUserName("root");
					vo.setUserPwd("123456");
					vo.setDomain("local");
				} else {
					vo.setDomain("remote");
				}
				// TODO vo.setGroupId(groupId);

				vo.setCreateTime(new Date(System.currentTimeMillis()));
				this.dataSourceManageServiceImpl.save(vo);
				// metaModelOperateServiceImpl.createDatabase(vo);
				return "1";
			} else {
				return "0";
			}
		}
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	// @ResponseBody
	@RequestMapping(value = "/remove")
	public String remove(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		DataSourceManageVO dsmv = this.dataSourceManageServiceImpl.get(id);

		// 删除关联
		BaseExample base = new BaseExample();
		base.createCriteria().andEqualTo("DATASOURCE_ID", id);
		List<SysDataSourceVO> vos = sysSourceRelationService.selectPagedByExample(base, 1, Integer.MAX_VALUE, null);
		if (vos != null && vos.size() > 0) {
			for (SysDataSourceVO vo : vos) {
				sysSourceRelationService.remove(vo);
			}
		}
		try {
			this.dataSourceManageServiceImpl.delete(dsmv);
			map.put("id", 1);
		} catch (Exception e) {
			map.put("id", 0);
		}
		return "redirect:selectPage.do";
	}

	/**
	 * 修改
	 * 
	 * @param vo
	 * @return
	 */
	// @ResponseBody
	@RequestMapping(value = "/update")
	public String update(@ModelAttribute DataSourceManageVO vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.dataSourceManageServiceImpl.update(vo);
			map.put("id", 1);
		} catch (Exception e) {
			map.put("id", 0);
		}
		return "redirect:selectPage.do";
		// return map;
	}

	/**
	 * 查询一条数据
	 * 
	 * @param id
	 * @return
	 */
	// @ResponseBody
	@RequestMapping(value = "/get")
	public String get(String id, Model model) {
		if (StringUtils.isNotBlank(id)) {
			DataSourceManageVO dsm = this.dataSourceManageServiceImpl.get(id);
			model.addAttribute("vo", dsm);
			if ("local".equals(dsm.getDomain())) {
				return "metadesigner/dataSource/dataSourceInner_edit";
			} else if ("remote".equals(dsm.getDomain())) {
				return "metadesigner/dataSource/dataSourceRemote_edit";
			}
		}
		return "metadesigner/dataSource/dataSource_edit";
	}

	/**
	 * 查询所有
	 * 
	 * @param model
	 * @return
	 */
	// @RequestMapping(value = "/findAll")
	// public String findAll(Model model) {
	// List<DataSourceManageVO> list = this.dataSourceManageServiceImpl
	// .findAll();
	// model.addAttribute("list", list);
	// return "metadesigner/dataSource/dataSource_list";
	// }

	/**
	 * 测试数据源是否有效
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/testDataSource")
	public Map<String, Object> testDataSource(@ModelAttribute DataSourceManageVO vo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection conn = null;
		try {
			Class.forName(vo.getSourceDriver());
			conn = DriverManager.getConnection(vo.getSourceUrl(), vo.getUserName(), vo.getUserPwd());
			if (conn != null) {
				map.put("id", 1);
				conn.close();
			} else {
				map.put("id", 0);
			}
		} catch (Exception e) {
			map.put("id", 0);
		}
		return map;
	}

	@RequestMapping("/table")
	public ModelAndView tableList(String id) {
		ModelAndView mv = new ModelAndView();
		String queryStr = "queryList";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modelSynchronization", true);
		PageList<MetaModelVO> pl = metaModelServiceImpl.queryResult(queryStr, map, 1, Integer.MAX_VALUE, "id.desc");
		mv.addObject("tables", pl);
		/*
		 * if (pl != null && pl.size() > 0) { MetaModelVO metaModelVO = null; for
		 * (MetaModelVO modelVo : pl) { if (modelVo.getModelSynchronization()) {
		 * metaModelVO = modelVo; break; } } if (metaModelVO != null) {
		 * List<MetaModelItemsVO> itemVOs =
		 * metaModelItemsServiceImpl.queryResult("queryResult", metaModelVO.getId()); if
		 * (itemVOs != null && itemVOs.size() > 0) { mv.addObject("items", itemVOs); for
		 * (MetaModelItemsVO vo : itemVOs) { sb.append(vo.getItemCode() + ", "); } int
		 * index = sb.lastIndexOf(","); if (index > 0) { sb.substring(0, index - 1); }
		 * sb.append(" from " + metaModelVO.getTableName() + ";");
		 * mv.addObject("sqlScript", sb.toString()); List<Map<String, String>> maps =
		 * metaModelOperateServiceImpl.queryPageResult(metaModelVO.getModelCode( ));
		 * mv.addObject("maps", maps); } } else { mv.addObject("noneItem", "暂无属性！"); } }
		 */
		mv.setViewName("metadesigner/dataSource/table_edit");
		return mv;
	}

	/**
	 * 跳转至数据源编辑页面
	 * 
	 * @param datasourceJson
	 * @return
	 */
	@RequestMapping(value = "/edit-ds-info")
	public ModelAndView editDataSourceInfo(DataSourceManageVO vo) {
		ModelAndView mv = new ModelAndView();
		String domain = vo.getDomain();
		if (StringUtils.isBlank(domain)) {
			return null;
		} else {
			if ("local".equals(domain)) {
				mv.setViewName("metadesigner/dataSource/dataSourceInner_edit");
			} else {
				mv.setViewName("metadesigner/dataSource/dataSourceRemote_edit");
			}
		}
		if (vo.getId() != null) {
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
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/listTables")
	public ModelAndView listTables(DataSourceManageVO vo) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("metadesigner/dataSource/dataSource_tmp_metamodel_listTables");
		Connection conn = null;
		try {
			// 如果数据源没有值，则使用系统的数据源
			if (vo == null || vo.getSourceDriver() == null || vo.getSourceUrl() == null || vo.getUserName() == null) {
				Class.forName(dataSource.getDriverClassName());
				conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(),
						dataSource.getPassword());

			} else {
				Class.forName(vo.getSourceDriver());
				conn = DriverManager.getConnection(vo.getSourceUrl(), vo.getUserName(), vo.getUserPwd());
			}

			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet tableRet = metaData.getTables(null, "%", "%", new String[] { "TABLE" });
			List<String> tableNames = new ArrayList<String>();
			while (tableRet.next()) {
				tableNames.add(tableRet.getString("TABLE_NAME"));
			}
			mv.addObject("datasourceJson", StringEscapeUtils.escapeHtml4(JSON.toJSONString(vo)));
			mv.addObject("vo", vo);
			mv.addObject("tables", tableNames);
		} catch (Exception e) {
			logger.info("ERROR", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.info("ERROR", e);
				}
			}
		}
		return mv;
	}

}
