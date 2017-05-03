package org.szcloud.framework.formdesigner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.utils.BeanUtils;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.ResourceTypeEnum;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.formdesigner.application.service.FormdesignerService;
import org.szcloud.framework.formdesigner.application.service.StoreService;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.application.vo.PageActVO;
import org.szcloud.framework.formdesigner.application.vo.StoreVO;
import org.szcloud.framework.unit.service.PunResourceService;
import org.szcloud.framework.unit.vo.PunResourceVO;
import org.szcloud.framework.unit.vo.PunSystemVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/fd/act")
public class PageActController extends BaseController {

	private final static Map<Integer, String> actTypes = new HashMap<Integer, String>();

	static {
		actTypes.put(2000, "普通");
		actTypes.put(2001, "保存带校验");
		actTypes.put(2002, "返回");
		actTypes.put(2003, "删除");
		actTypes.put(2004, "启动流程");
		actTypes.put(2005, "流程调整");
		actTypes.put(2006, "流程回撤");
		actTypes.put(2007, "编辑审批人");
		actTypes.put(2008, "保存(带流程)");
		actTypes.put(2009, "新增");
		actTypes.put(2010, "更新");
		actTypes.put(2011, "流程发送");
		actTypes.put(2012, "打开");
		actTypes.put(2013, "审批");
		actTypes.put(2014, "Pdf打印");
		actTypes.put(2015, "保存不带校验");
		actTypes.put(2016, "Excel导出");
		actTypes.put(2017, "保存并返回");
		actTypes.put(2018, "流程转发");
		actTypes.put(2019, "流程传阅");
		actTypes.put(2020, "流程图");
		actTypes.put(2021, "流程归档");
		actTypes.put(2022, "流程办结");
		actTypes.put(2023, "流程退回");
		actTypes.put(2024, "加签");
		actTypes.put(2025, "移交");
	}

	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;

	@Autowired
	@Qualifier("formdesignerServiceImpl")
	private FormdesignerService formdesignerService;

	@Autowired
	@Qualifier("punResourceServiceImpl")
	private PunResourceService punResourceService;

	/**
	 * 根据code、name、description字段进行查询分页显示列表
	 * 
	 * @param actCode
	 * @param name
	 * @param description
	 * @param currentPage
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView listPageActs(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "actDes", required = false) String description,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "dynamicPageId", required = false, defaultValue = "1") Long dynamicPageId,
			@RequestParam(value = "order", required = false, defaultValue = "0") int order,
			@RequestParam(value = "dialog", required = false, defaultValue = "0") int dialog) {
		BaseExample baseExample = new BaseExample();
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		ModelAndView mv = new ModelAndView();
		baseExample.createCriteria().andLike("code", StoreService.PAGEACT_CODE + "%");
		if (StringUtils.isNoneBlank(name)) {
			baseExample.getOredCriteria().get(0).andLike("name", "%" + name + "%");
		}
		if (StringUtils.isNoneBlank(description)) {
			baseExample.getOredCriteria().get(0).andLike("description", "%" + description + "%");
		}
		PageList<StoreVO> list = storeService.selectPagedByExample(baseExample, currentPage, pageSize, "code desc");

		PageList<PageActVO> vos = new PageList<PageActVO>(list.getPaginator());
		for (StoreVO vo : list) {
			PageActVO act = JSON.parseObject(vo.getContent(), PageActVO.class);
			vos.add(act);
		}
		list.clear();
		mv.addObject("vos", vos);
		mv.addObject("currentPage", currentPage);
		mv.addObject("pageSize", pageSize);
		mv.addObject("name", name);
		mv.addObject("actDes", description);

		mv.addObject("actTypes", actTypes);

		if (dialog == 1) {
			mv.addObject("dynamicPageId", dynamicPageId);
			mv.addObject("order", order);
			mv.setViewName("formdesigner/page/act/act-select");
		} else {
			mv.setViewName("formdesigner/page/act/act-list");
		}
		return mv;
	}

	/**
	 * 返回单个pageact对象json格式字符串
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getByAjax")
	public String getPageActByAjax(String id) {
		StoreVO vo = storeService.findById(id);
		return vo.getContent();
	}

	/**
	 * 返回多个pageact对象json格式字符串
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllByAjax")
	public List<PageActVO> getAllByAjax() {
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andLike("code", StoreService.PAGEACT_CODE + "%");
		List<StoreVO> list = storeService.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, "code desc");
		List<PageActVO> acts = new ArrayList<PageActVO>();
		for (StoreVO vo : list) {
			PageActVO style = JSON.parseObject(vo.getContent(), PageActVO.class);
			acts.add(style);
		}

		list.clear();
		return acts;
	}

	/**
	 * 
	 * @Title: getWorkflowByPageId @Description:查找动态页面相关联的流程 @param id
	 * dynamicPageId @return String 流程信息(json字符串) [{}] @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/getWorkflowByPageId")
	public String getWorkflowByPageId(Long id) {
		String ret = formdesignerService.getWfByStartNode(id);
		return ret;
	}

	/**
	 * 跳转到编辑页面，编辑页面动作信息 1.点击某个页面动作进行编辑 2.保存时校验失败，跳转到编辑页面，带有错误信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView editPageAct(@RequestParam(value = "_selects", required = false) String id,
			@RequestParam(value = "dynamicPageId", required = false) Long dynamicPageId,
			@RequestParam(value = "order", required = false, defaultValue = "0") int order,
			@RequestParam(value = "dialog", required = false, defaultValue = "0") int dialog,
			@RequestParam(value = "type", required = false, defaultValue = "0") Integer type) {
		ModelAndView mv = new ModelAndView();
		Long systemId = -1L;
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			systemId = system.getSysId();
		}

		PageActVO vo = new PageActVO();
		if (StringUtils.isNoneBlank(id)) {
			StoreVO store = storeService.findById(id);
			vo = JSON.parseObject(store.getContent(), PageActVO.class);
		}

		if (type == 2014 || (vo.getActType() != null && vo.getActType() == 2014)) {
			BaseExample printExample = new BaseExample();
			printExample.createCriteria().andLike("CODE", StoreService.PRINT_CODE + "%");
			List<StoreVO> templets = storeService.selectPagedByExample(printExample, 1, Integer.MAX_VALUE, null);
			mv.addObject("printTemplets", templets);
		}

		if (1 == dialog) {
			vo.setDynamicPageId(dynamicPageId);
			List<DynamicPageVO> pages = new ArrayList<DynamicPageVO>();
			DynamicPageVO dynamicPageVO = formdesignerService.findById(dynamicPageId);
			pages.add(dynamicPageVO);
			mv.addObject("pages", pages);

		}

		List<DynamicPageVO> pages = formdesignerService.listNameAndIdInSystem(systemId, null);
		mv.addObject("pages", pages);
		// 加入当前动态页面的类型
		if (dynamicPageId != null && !dynamicPageId.equals("")) {
			DynamicPageVO dynamicPageVO = formdesignerService.findById(dynamicPageId);
			int pageType = dynamicPageVO.getPageType();
			mv.addObject("pageType", pageType);
		} else {
			dynamicPageId = vo.getDynamicPageId();
			if (dynamicPageId != null) {
				DynamicPageVO dynamicPageVO = formdesignerService.findById(dynamicPageId);
				int pageType = dynamicPageVO.getPageType();
				mv.addObject("pageType", pageType);
			}
		}

		mv.addObject("act", vo);

		if (type == 0) {
			type = vo.getActType();
		}
		mv.setViewName(getViewName(type));
		return mv;
	}

	/**
	 * 保存页面动作 保存成功跳转到list页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save")
	public ModelAndView savePageAct(PageActVO vo,
			@RequestParam(value = "dialog", required = false, defaultValue = "0") int dialog) {
		ModelAndView mv = new ModelAndView();
		if (validate(mv, vo)) {
			Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if (obj instanceof PunSystemVO) {
				PunSystemVO system = (PunSystemVO) obj;
				vo.setSystemId(system.getSysId());
			}
			StoreVO store = convert(vo);
			PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);
			act.setButtonGroup(act.getButtonGroup());
			String id = storeService.save(store);
			if (StringUtils.isBlank(vo.getPageId())) { // 新增,则往resource表中写上数据
				PunResourceVO resource = new PunResourceVO();
				resource.setSysId(vo.getSystemId());
				resource.setResouType("3");// 菜单;
				resource.setResourceName(vo.getName());
				resource.setRelateResoId(id);
				punResourceService.addOrUpdate(resource);
			} else {
				PunResourceVO resource = punResourceService.getResourceByRelateId(vo.getPageId(),
						ResourceTypeEnum.RESO_BUTTON.getkey());
				resource.setResourceName(vo.getName());
				punResourceService.addOrUpdate(resource);
			}

			vo.setPageId(id);
			if (dialog == 1) {
				mv.setViewName("redirect:/fd/act/edit.do?_select=" + id);
			} else {
				mv.setViewName("redirect:/fd/act/list.do");
			}

		} else {
			mv.addObject("vo", vo);
			mv.setViewName("formdesigner/page/act/act-edit");// 编辑页面
		}
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/saveByAjax")
	public PageActVO saveByAjax(PageActVO vo) {
		/**
		 * TODO dynamicPageId 不能为空
		 */
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			vo.setSystemId(system.getSysId());
		}
		StoreVO store = convert(vo);
		String id = storeService.save(store);
		if (StringUtils.isBlank(vo.getPageId())) { // 新增,则往resource表中写上数据
			PunResourceVO resource = new PunResourceVO();
			resource.setSysId(vo.getSystemId());
			resource.setResouType(ResourceTypeEnum.RESO_BUTTON.getkey());// 菜单;
			resource.setResourceName(vo.getName());
			resource.setRelateResoId(id);
			punResourceService.addOrUpdate(resource);
		} else {
			PunResourceVO resource = punResourceService.getResourceByRelateId(vo.getPageId(),
					ResourceTypeEnum.RESO_BUTTON.getkey());
			resource.setResourceName(vo.getName());
			punResourceService.addOrUpdate(resource);
		}
		vo.setPageId(id);
		return vo;

	}

	/**
	 * @Title: getByAjaxAndPageId @Description: 根据pageId查找act 数据 @param
	 * id @return List<PageActVO> json字符串 @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/getByPageId")
	public List<PageActVO> getByAjaxAndPageId(@RequestParam(value = "dynamicPageId", required = false) Long id) {
		BaseExample actExample = new BaseExample();
		actExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", id).andLike("CODE", StoreService.PAGEACT_CODE + "%");
		PageList<StoreVO> actStore = storeService.selectPagedByExample(actExample, 1, Integer.MAX_VALUE,
				"BTN_GROUP ASC,T_ORDER ASC");
		List<PageActVO> acts = new ArrayList<PageActVO>();
		for (StoreVO store : actStore) {
			PageActVO act = JSON.parseObject(store.getContent(), PageActVO.class);
			acts.add(act);
		}
		actStore.clear();
		return acts;
	}

	/**
	 * 删除选中的页面动作数据,更改数据状态
	 * 
	 * @param selects
	 * @return
	 */
	@RequestMapping(value = "delete")
	public ModelAndView deletePageAct(@RequestParam(value = "_selects") String selects) {
		ModelAndView mv = new ModelAndView();
		String[] ids = selects.split(",");
		if (storeService.delete(ids)) {
			addMessage(mv, "删除成功");
		}
		mv.setViewName("redirect:/fd/act/list.do");
		return mv;
	}

	/**
	 * 异步ajax删除选中的页面动作数据,更改数据状态
	 * 
	 * @param selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteByAjax")
	public String deletePageActByAjax(@RequestParam(value = "_selects") String selects) {
		String[] ids = selects.split(",");
		if (storeService.delete(ids)) {
			for (int i = 0; i < ids.length; i++) {
				punResourceService.removeByRelateResoAndType(ids[i], "3");
			}
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 彻底删除选中的页面动作数据
	 * 
	 * @param selects
	 * @return
	 */
	@RequestMapping(value = "/remove")
	public ModelAndView removePageAct(@RequestParam(value = "_selects") String selects) {
		// TODO
		ModelAndView mv = new ModelAndView();
		String[] ids = selects.split(",");
		try {
			storeService.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 从session中获取当前页？
		mv.setViewName("redirect:/fd/act/list.do");
		return mv;
	}

	/**
	 * 根据传回的id，进行复制,并将复制的对象(json格式)返回给页面
	 * 
	 * @param actIds
	 * @param dynamicPageId
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copy")
	public List<PageActVO> copyPageActs(@RequestParam(value = "_selects") String actIds,
			@RequestParam(value = "dynamicPageId") Long dynamicPageId,
			@RequestParam(value = "order", defaultValue = "1") int order) {
		List<StoreVO> storeList = storeService.copy(actIds, dynamicPageId, order);
		List<PageActVO> vos = new ArrayList<PageActVO>();
		for (StoreVO store : storeList) {
			vos.add(JSON.parseObject(store.getContent(), PageActVO.class));
		}
		storeList.clear();
		return vos;
	}

	/**
	 * 校验
	 * 
	 * @param mv
	 * @param vo
	 * @return
	 */
	private boolean validate(ModelAndView mv, PageActVO vo) {
		if (StringUtils.isEmpty(vo.getName())) {
			addMessage(mv, " 名称必填 ");
			return false;
		}
		if (StringUtils.isEmpty(Long.toString(vo.getDynamicPageId()))) {
			addMessage(mv, "未指定所属页面");
		}
		if (StringUtils.isEmpty(vo.getActType().toString())) {
			addMessage(mv, "请选择动作类型");
			return false;
		}
		return true;
	}

	private StoreVO convert(PageActVO vo) {
		StoreVO store = new StoreVO();
		if (StringUtils.isBlank(vo.getCode())) {
			vo.setCode(StoreService.PAGEACT_CODE + System.currentTimeMillis());
		}
		store = BeanUtils.getNewInstance(vo, StoreVO.class);
		store.setContent(JSON.toJSONString(vo));
		store.setId(vo.getPageId());
		return store;
	}

	private String getViewName(Integer type) {
		StringBuilder viewName = new StringBuilder("formdesigner/page/act/");
		switch (type) {
		case 2000:
			viewName.append("act");
			break;
		case 2001:
			viewName.append("saveAct");
			break;
		case 2002:
			viewName.append("backAct");
			break;
		case 2003:
			viewName.append("deleteAct");
			break;
		case 2004:
			viewName.append("startWorkflowAct");
			break;
		case 2005:
			viewName.append("rebuildWorkflowAct");
			break;
		case 2006:
			viewName.append("backWorkflowAct");
			break;
		case 2007:
			viewName.append("editApprovalAct");
			break;
		case 2008:
			viewName.append("saveWithWorkflowAct");
			break;
		case 2009:
			viewName.append("newAct");
			break;
		case 2010:
			viewName.append("updateAct");
			break;
		case 2011:
			viewName.append("compeleteWithWorkflowAct");
			break;
		case 2012:
			viewName.append("openAct");
			break;
		case 2013:
			viewName.append("auditAct");
			break;
		case 2014:
			viewName.append("printPdfAct");
			break;
		case 2015:
			viewName.append("saveActWithNoValidators");
			break;
		case 2016:
			viewName.append("excelAct");
			break;
		case 2017:
			viewName.append("saveAndReturnAct");
			break;
		case 2018:
			viewName.append("forwardWithWorkflowAct");// 流程转发
			break;
		case 2019:
			viewName.append("handRoundWithWorkflowAct");// 流程传阅
			break;
		case 2020:
			viewName.append("diagramWithWorkflowAct");// 流传图
			break;
		case 2021:
			viewName.append("filingWithWorkflowAct");// 流程归档
			break;
		case 2022:
			viewName.append("finishedWorkflowAct");// 流程归档
			break;
		case 2023:
			viewName.append("returnWorkflowAct");// 流程归档
			break;
		case 2024:
			viewName.append("askFor");// 加签
			break;
		case 2025:
			viewName.append("shift");// 加签
			break;
		default:
			viewName.append("newAct");
		}
		viewName.append("-edit");
		return viewName.toString();

	}

	/**
	 * 批量修改动作类型
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyActType")
	public String batchModifyActType(@RequestParam(value = "_selects") String selects, Long dynamicPageId,
			String actType) {
		String[] ids = selects.split(",");
		for (int i = 0; i < ids.length; i++) {
			StoreVO s = storeService.findById(ids[i]);
			// 置Id为空，表示新增
			JSONObject json = JSON.parseObject(s.getContent());
			json.put("actType", actType);
			s.setContent(json.toJSONString());
			storeService.save(s); // 保存
		}
		return "1";
	}
}
