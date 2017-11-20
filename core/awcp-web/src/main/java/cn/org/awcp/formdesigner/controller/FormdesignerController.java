package cn.org.awcp.formdesigner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.core.utils.Tools;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.application.vo.PageActVO;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.core.constants.FormDesignGlobal;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.unit.service.PunResourceService;
import cn.org.awcp.unit.service.PunSystemService;
import cn.org.awcp.unit.vo.PunGroupVO;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;

/**
 * 表单设计Controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/fd")
public class FormdesignerController extends BaseController {

	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;

	@Autowired
	@Qualifier("punResourceServiceImpl")
	private PunResourceService punResourceService;

	@Autowired
	private PunSystemService punSystemServiceImpl;

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	/**
	 * 根据name查询动态页面，分页显示
	 * 
	 * @param name
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(String name, String modular, String pageType, String templateId, String menuId,
			String tips, @RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView();
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		BaseExample example = new BaseExample();
		Criteria criteria = example.createCriteria();
		if (obj instanceof PunSystemVO) {
			if (StringUtils.isNotBlank(name)) {
				criteria.andLike("name", "%" + name + "%");
			}
			if (StringUtils.isNotBlank(modular)) {
				criteria.andEqualTo("modular", modular);
			}
			if (StringUtils.isNotBlank(pageType)) {
				criteria.andEqualTo("page_type", pageType);
			}
			if (StringUtils.isNotBlank(templateId)) {
				criteria.andEqualTo("template_id", templateId);
			}
			if (StringUtils.isNotBlank(menuId)) {
				criteria.andEqualTo("id", menuId);
			}
			// 只能查看当前用户创建的表单
			criteria.andEqualTo("CREATED_USER", ControllerHelper.getUser().getName());
			PageList<DynamicPageVO> vos = formdesignerServiceImpl.selectPagedByExample(example, currentPage, pageSize,
					"created desc");
			mv.addObject("vos", vos);
		}
		mv.addObject("pageType", pageType);
		mv.addObject("tips", tips);
		mv.addObject("name", name);
		mv.addObject("currentPage", currentPage);
		mv.addObject("templateId", templateId);
		mv.addObject("menuId", menuId);
		mv.addObject("modular", modular);
		mv.addObject("templates", meta.search(
				"SELECT DISTINCT b.id,b.file_name as text FROM p_fm_dynamicpage a LEFT JOIN p_fm_template b ON a.template_id=b.id"));
		mv.addObject("menus", meta
				.search("SELECT dynamicpage_id id,menu_name text FROM p_un_menu a WHERE LENGTH(a.dynamicpage_id)>0"));
		mv.addObject("modulars", meta.search("select ID,modularName from p_fm_modular"));
		mv.setViewName("formdesigner/page/dynamicPage-list");
		return mv;
	}

	private static Map<String, String> baseDynamicPage = new HashMap<>();
	static {
		baseDynamicPage.put("API管理列表", "api_list");
		baseDynamicPage.put("API管理表单", "api_form");
		baseDynamicPage.put("API参数表单", "api_param_form");
		baseDynamicPage.put("页面模块列表", "page_modular_list");
		baseDynamicPage.put("页面模块新增表单", "page_modular_form");
		baseDynamicPage.put("模块添加页面", "modular_add_page");
		baseDynamicPage.put("模块列表页面", "modular_list_page");
		baseDynamicPage.put("钉钉微应用类型列表", "dd_micro_app_type_list");
		baseDynamicPage.put("钉钉微应用类型表单", "dd_micro_app_type_form");
		baseDynamicPage.put("钉钉微应用列表", "dd_micro_app_list");
		baseDynamicPage.put("钉钉微应用表单", "dd_micro_app_form");
		baseDynamicPage.put("意见组件（手机）", "suggest_component_mobile");
		baseDynamicPage.put("意见组件（电脑）", "suggest_componen_computer");
		baseDynamicPage.put("意见组件（PC）", "suggest_componen_PC");
		baseDynamicPage.put("意见列表片段（手机）", "suggest_list_fragment_mobile");
	}

	@ResponseBody
	@RequestMapping(value = "/updatePageID")
	public Map<String, Object> updatePageID(String oldID, String newID) {
		SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		jdbcTemplate.beginTranstaion();
		try {
			String sql = "update p_fm_dynamicpage set id=? where id=?";
			String updateMenuSQL = "update p_un_menu set DYNAMICPAGE_ID=? where DYNAMICPAGE_ID=?";
			jdbcTemplate.update(sql, newID, oldID);
			jdbcTemplate.update(updateMenuSQL, newID, oldID);
			PageList<StoreVO> stores = storeService.findByDyanamicPageId(oldID);
			for (StoreVO store : stores) {
				String content = store.getContent();
				JSONObject json = JSONObject.parseObject(content);
				if (StringUtils.isNotBlank(json.getString("dynamicPageId"))) {
					json.put("dynamicPageId", newID);
				}
				store.setContent(json.toJSONString());
				store.setDynamicPageId(newID);
				storeService.save(store);
			}
			jdbcTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
			jdbcTemplate.rollback();
		}
		return null;
	}

	/**
	 * 查询动态页面，并跳转到编辑页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView editPage(@RequestParam(value = "errorMsg", required = false) String errorMsg,
			@RequestParam(value = "_selects", required = false) String id,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		// 根据id来查询，如果id为空，则为新增
		ModelAndView mv = new ModelAndView();
		mv.addObject("currentPage", currentPage);
		mv.setViewName("formdesigner/page/dynamicPage-edit");
		if (errorMsg != null && !"".equals(errorMsg)) {
			mv.addObject("tips", errorMsg);
		}
		if (!validatorPage(mv, id)) {
			mv.setViewName("redirect:/fd/list.do");
			return mv;
		}
		if (id != null) {
			DynamicPageVO vo = formdesignerServiceImpl.findById(id);
			if (vo != null) {
				BaseExample actExample = new BaseExample();
				actExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", id).andLike("CODE",
						StoreService.PAGEACT_CODE + "%");
				PageList<StoreVO> actStore = storeService.selectPagedByExample(actExample, 1, Integer.MAX_VALUE,
						"BTN_GROUP ASC,T_ORDER ASC");
				List<PageActVO> acts = new ArrayList<PageActVO>();
				for (StoreVO store : actStore) {
					PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);
					acts.add(act);
				}
				actStore.clear();
				mv.addObject("acts", acts);
			}
			mv.addObject("vo", vo);
		}
		mv.addObject("modulars", meta.search("select ID,modularName from p_fm_modular"));
		mv.addObject("_COMPOENT_TYPE_NAME", new TreeMap<>(FormDesignGlobal.COMPOENT_TYPE_NAME));
		return mv;
	}

	/**
	 * 保存动态页面信息 校验不通过，返回到编辑页面
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView savePage(DynamicPageVO vo,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("currentPage", currentPage);
		Object o = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (o instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) o;
			vo.setSystemId(system.getSysId());
		}
		// 校验
		if (validatorPage(mv, vo.getId())) {
			if (StringUtils.isBlank(vo.getId())) {
				vo.setCheckOutUser(ControllerHelper.getUser().getName());
				vo.setIsCheckOut(1);
			}
			formdesignerServiceImpl.saveOrUpdate(vo);
			mv.addObject("_selects", vo.getId());
			mv.setViewName("redirect:/fd/edit.do"); // 保存后重定向编辑页面
		} else {
			mv.addObject("_selects", vo.getId());
			mv.setViewName("redirect:/fd/edit.do");
		}
		return mv;
	}

	/**
	 * 复制页面
	 * 
	 * @param ids
	 * @param currentPage
	 * @return
	 */
	@RequestMapping(value = "/copy")
	public ModelAndView copyDyanmiacPages(@RequestParam(value = "_selects", required = false) String[] ids,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("currentPage", currentPage);
		for (String id : ids) {
			formdesignerServiceImpl.copy(id);
		}
		mv.setViewName("redirect:/fd/list.do");
		return mv;
	}

	/**
	 * 点击复制到按钮弹窗页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listSystem")
	public ModelAndView listSystem() {
		ModelAndView mv = new ModelAndView();
		PunSystemVO sysVO = (PunSystemVO) SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		PunGroupVO groupVo = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", groupVo.getGroupId());
		List<PunSystemVO> list = punSystemServiceImpl.queryResult("eqQueryList", params);
		mv.addObject("list", list);
		mv.addObject("system", sysVO);
		mv.setViewName("formdesigner/page/select-system-edit");
		return mv;
	}

	/**
	 * 复制到系统
	 * 
	 * @param ids
	 * @param currentPage
	 * @param systemId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copyToSystem")
	public String copyDyanmiacPagesToSystem(@RequestParam(value = "_selects", required = false) String[] ids,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false) String systemId) {
		JSONObject o = new JSONObject();
		if (StringUtils.isNotBlank(systemId)) {
			for (String id : ids) {
				formdesignerServiceImpl.copyToSystem(id, systemId);
			}
			o.put("rtn", "1");
		} else {
			o.put("rtn", "-1");
			o.put("msg", "请选择目标系统！");
		}
		return o.toJSONString();
	}

	/**
	 * 删除选择的动态页面数据
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete")
	public ModelAndView deletePage(@RequestParam(value = "_selects") String ids,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/fd/list.do?currentPage=" + currentPage);
		try {
			String[] array = ids.split(",");
			List<String> temp = new ArrayList<String>();
			for (String id : array) {
				if (!validatorPage(mv, id)) {
					return mv;
				}
				temp.add(id);
				BaseExample example = new BaseExample();
				example.createCriteria().andEqualTo("dynamicPage_id", id);
				List<StoreVO> stores = storeService.selectPagedByExample(example, 1, Integer.MAX_VALUE, null);
				for (StoreVO store : stores) {
					storeService.delete(store);
					// 删除资源表中的按钮
					if (store.getCode().contains(StoreService.PAGEACT_CODE)) {
						punResourceService.removeByRelateResoAndType(store.getId(), "3");
					}
				}
				stores.clear();
			}
			formdesignerServiceImpl.delete(temp);
			temp.clear();
		} catch (Exception e) {
			logger.info("ERROR", e);
		}
		return mv;
	}

	/**
	 * 批量发布页面
	 * 
	 * @param ids
	 * @param currentPage
	 * @return
	 */
	@RequestMapping(value = "/publish")
	public ModelAndView publishPage(@RequestParam(value = "_selects", required = false) String[] ids,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("currentPage", currentPage);
		for (String id : ids) {
			formdesignerServiceImpl.publish(id);
		}
		mv.setViewName("redirect:/fd/list.do");
		return mv;
	}

	/**
	 * 发布当前页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/publishOnePage")
	public ModelAndView publishOnePage(String id) {
		ModelAndView mv = new ModelAndView();
		if (id == null) {
			mv.addObject("errorMsg", "ID是空.");
		} else {
			try {
				formdesignerServiceImpl.publish(id);
			} catch (Exception e) {
				logger.info("ERROR", e);
				mv.addObject("errorMsg", "发布错误.");
			}
			mv.addObject("_selects", id);
		}
		mv.setViewName("redirect:/fd/edit.do");
		return mv;
	}

	/**
	 * 签出页面
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkOutPage")
	public Map<String, Object> checkOutPage(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		formdesignerServiceImpl.checkOut(id);
		map.put("msg", "签出成功");
		map.put("userName", ControllerHelper.getUser().getName());
		return map;
	}

	/**
	 * 签入页面
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkInPage")
	public String checkInPage(String id) {
		DynamicPageVO vo = formdesignerServiceImpl.findById(id);
		if (vo.getIsCheckOut() != null && vo.getIsCheckOut() == 1) { // 已签出状态
			if (!ControllerHelper.getUser().getName().equals(vo.getCheckOutUser())) {
				String ret = vo.getName() + "页面已被  " + vo.getCheckOutUser() + "签出,你无法进行签入";
				return ret;
			}
		}
		formdesignerServiceImpl.checkIn(id);
		return "签入成功";
	}

	/**
	 * 查看发布后的内容
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/catTemplate")
	public ModelAndView catTemplate(@RequestParam(value = "_select") String id) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("formdesigner/page/catTemplate");
		if (StringUtils.isNotBlank(id)) {
			try {
				String content = formdesignerServiceImpl.getTemplateContext(id);
				mv.addObject("content", content);
			} catch (Exception e) {
				logger.info("ERROR", e);
				addMessage(mv, "啊哦~后台报错了，传入的id不能被转换为Long！");
			}
		} else {
			addMessage(mv, "请选择需要查看的动态页面！");
		}
		return mv;
	}

	/**
	 * 展示当前页面的直接父级和子级页面
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/relation")
	public ModelAndView refrenceRelation(@RequestParam(value = "_select") String id) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("formdesigner/page/refRelation");
		try {
			Map<String, DynamicPageVO> child = findChildComponentByDynamicPageId(id);
			// 找出包含该页面的包含组件（父亲）
			// 找出该包含组件所在页面
			Map<String, DynamicPageVO> parent = new HashMap<String, DynamicPageVO>();
			List<JSONObject> parentComponents = storeService.findParentComponentByDyanamicPageId(id);
			for (JSONObject component : parentComponents) {
				String relatePageId = component.getString("dynamicPageId");
				if (StringUtils.isNotBlank(relatePageId)) {
					DynamicPageVO vo = formdesignerServiceImpl.findById(relatePageId);
					if (vo != null) {
						parent.put(vo.getId(), vo);
					}
				}
			}
			mv.addObject("parent", parent);
			mv.addObject("child", child);
		} catch (Exception e) {
			logger.info("ERROR", e);
		}
		return mv;
	}

	/**
	 * 根据name查询动态页面，分页显示
	 * 
	 * @param name
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list-bind")
	public ModelAndView bind(@RequestParam(required = false) String name,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize, String mes) {
		ModelAndView mv = new ModelAndView();
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			BaseExample example = new BaseExample();
			if (StringUtils.isNotBlank(name)) {
				boolean isNum = name.matches("[0-9]+");
				if (isNum) {
					example.createCriteria().andEqualTo("id", name);
				} else {
					example.createCriteria().andLike("name", "%" + name + "%");
				}
			}
			PageList<DynamicPageVO> vos = formdesignerServiceImpl.selectPagedByExample(example, currentPage, pageSize,
					"ID desc");
			mv.addObject("vos", vos);
		}

		if (mes != null && "error".equals(mes)) {
			mv.addObject("mes", "error");
		}
		mv.addObject("name", name);
		mv.addObject("currentPage", currentPage);
		mv.addObject("pageSize", pageSize);
		mv.setViewName("formdesigner/page/dynamicPage-bind");
		return mv;
	}

	/**
	 * 根据name查询动态页面，分页显示
	 * 
	 * @param name
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/listSelect")
	public ModelAndView listSelect(@RequestParam(required = false) String name,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView();
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			BaseExample example = new BaseExample();
			if (StringUtils.isNotBlank(name)) {
				example.createCriteria().andLike("name", "%" + name + "%");
			}
			PageList<DynamicPageVO> vos = formdesignerServiceImpl.selectPagedByExample(example, currentPage, pageSize,
					"ID desc");
			mv.addObject("vos", vos);
		}
		mv.addObject("name", name);
		mv.addObject("currentPage", currentPage);
		mv.addObject("pageSize", pageSize);
		mv.setViewName("formdesigner/page/dynamicPage-list-select");
		return mv;
	}

	/**
	 * 根据name查询动态页面，分页显示
	 * 
	 * @param name
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listJson")
	public String listJson(@RequestParam(required = false) String name) {
		Long systemId = -1L;
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			systemId = system.getSysId();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(name)) {
			params.put("name", "%" + name + "%");
		}
		List<DynamicPageVO> vos = formdesignerServiceImpl.listNameAndIdInSystem(systemId, params);
		JSONArray array = new JSONArray();
		for (DynamicPageVO vo : vos) {
			JSONObject o = new JSONObject();
			o.put("id", vo.getId());
			o.put("name", vo.getName());
			array.add(o);
		}
		return array.toJSONString();
	}

	private Map<String, DynamicPageVO> findChildComponentByDynamicPageId(String id) {
		List<JSONObject> components = storeService.findComponentByDyanamicPageId(id);
		Map<String, DynamicPageVO> child = new HashMap<String, DynamicPageVO>();
		for (JSONObject component : components) {
			String relatePageId = component.getString("relatePageId");
			if (StringUtils.isNotBlank(relatePageId)) {
				DynamicPageVO vo = formdesignerServiceImpl.findById(relatePageId);
				if (!vo.getId().equals(id)) {
					child.putAll(findChildComponentByDynamicPageId(vo.getId()));
				}
				if (vo != null) {
					child.put(vo.getId(), vo);
				}
			}
		}
		return child;
	}

	/**
	 * 更动态页面新数据源
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateData")
	public String updateDataJson(DynamicPageVO vo) {
		ModelAndView mv = new ModelAndView();
		// 校验
		if (validatorPage(mv, vo.getId())) {
			formdesignerServiceImpl.updateModelInfo(vo);
			return "1";
		} else {
			return "0";
		}
	}

	private boolean validatorPage(ModelAndView mv, String id) {
		// 新增页面不需要判断
		if (StringUtils.isBlank(id)) {
			return true;
		}
		// 只有页面迁出者才可以修改
		DynamicPageVO vo = formdesignerServiceImpl.findById(id);
		if (vo.getIsCheckOut() == null || vo.getIsCheckOut() == 0) {
			mv.addObject("tips", vo.getName() + "页面还未签出，编辑修改前请先签出页面");
			return false;
		} else {
			if (vo.getIsCheckOut() != null && vo.getIsCheckOut() == 1) { // 已签出状态
				if (!ControllerHelper.getUser().getName().equals(vo.getCheckOutUser())) {
					String ret = vo.getName() + "页面已被  " + vo.getCheckOutUser() + "签出！";
					mv.addObject("tips", ret);
					return false;
				}
			}
		}
		return true;
	}

	@ResponseBody
	@RequestMapping(value = "/validateCheckOut")
	public Map<String, Object> validateCheckOut(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (id == null) {
			map.put("value", 1);
			// 新增页面，无需校验是否签出；
			return map;
		}

		DynamicPageVO vo = formdesignerServiceImpl.findById(id);
		if (vo.getIsCheckOut() != null && vo.getIsCheckOut() == 1) { // 已签出状态
			if (!ControllerHelper.getUser().getName().equals(vo.getCheckOutUser())) {
				String ret = vo.getName() + "页面已被  " + vo.getCheckOutUser() + "签出！";
				map.put("value", ret);
				return map;
			}
		}
		map.put("value", 1);
		return map;
	}

}
