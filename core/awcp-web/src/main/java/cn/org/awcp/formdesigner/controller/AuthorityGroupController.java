package cn.org.awcp.formdesigner.controller;

import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.AuthorityCompoentService;
import cn.org.awcp.formdesigner.application.service.AuthorityGroupService;
import cn.org.awcp.formdesigner.application.service.AuthorityGroupWorkFlowNodeService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.AuthorityCompoentVO;
import cn.org.awcp.formdesigner.application.vo.AuthorityGroupVO;
import cn.org.awcp.formdesigner.application.vo.AuthorityGroupWorkFlowNodeVO;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.unit.core.domain.PunUserBaseInfo;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;

@Controller
@RequestMapping("/authority")
public class AuthorityGroupController extends BaseController {

	@Autowired
	@Qualifier("authorityGroupServiceImpl")
	private AuthorityGroupService authorityGroupService;

	@Autowired
	@Qualifier("storeServiceImpl")
	private StoreService storeService;

	@Autowired
	@Qualifier("authorityCompoentServiceImpl")
	private AuthorityCompoentService authorityCompoentService;

	@Autowired
	@Qualifier("authorityGroupWorkFlowNodeServiceImpl")
	private AuthorityGroupWorkFlowNodeService authorityGroupWorkFlowNodeService;

	@ResponseBody
	@RequestMapping(value = "/getGroupListByPageId")
	public List<AuthorityGroupVO> findPowerByPageId(@RequestParam(required = true) String dynamicPageId, String nodes,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "30") int pageSize,
			@RequestParam(required = false, defaultValue = " T_ORDER ASC") String sortString) {
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId);
		PageList<AuthorityGroupVO> list = authorityGroupService.selectPagedByExample(baseExample, currentPage, pageSize,
				sortString);

		// nodes不为空为节点配置权限组回显
		if (nodes != null) {
			nodes = nodes.split("_")[0];
			Map<String, Object> resParams = new HashMap<String, Object>();
			for (AuthorityGroupVO ag : list) {
				resParams.clear();
				resParams.put("flowNode", nodes);
				resParams.put("authorityGroup", ag.getId());
				List<AuthorityGroupWorkFlowNodeVO> resoVOs = authorityGroupWorkFlowNodeService
						.queryResult("eqQueryList", resParams);
				if (resoVOs.size() > 0) {
					ag.setBakInfo("check");
				}
			}

		} else {
			for (AuthorityGroupVO ag : list) {
				PunUserBaseInfo vo = PunUserBaseInfo.get(PunUserBaseInfo.class, ag.getCreater());
				ag.setBakInfo(vo.getName());
			}
		}
		return list;

	}

	// 给节点配置权限组
	@ResponseBody
	@RequestMapping(value = "/addWorkflowNodeAuthority")
	public AuthorityGroupWorkFlowNodeVO addWorkflowNodeAuthority(String authorityGroupId, String nodes, String status,
			String dynamicPageId) {

		AuthorityGroupWorkFlowNodeVO vo = new AuthorityGroupWorkFlowNodeVO();

		Map<String, Object> resParams = new HashMap<String, Object>();
		if (nodes != null) {
			nodes = nodes.split("_")[0];
		}
		resParams.put("flowNode", nodes);
		resParams.put("authorityGroup", authorityGroupId);

		List<AuthorityGroupWorkFlowNodeVO> resoVOs = authorityGroupWorkFlowNodeService.queryResult("eqQueryList",
				resParams);

		if (resoVOs.size() > 0) {
			AuthorityGroupWorkFlowNodeVO v = resoVOs.get(0);
			// 修改操作
			/*
			 * UserInfoVO u = (UserInfoVO)user; AuthorityGroupWorkFlowNodeVO
			 * v=resoVOs.get(0); if(status.equals("1")){
			 * v.setAuthorityGroup(authorityGroupId); } if(status.equals("0")){
			 * v.setAuthorityGroup(""); } v.setLastUpdater(u.getId());
			 * v.setLastUpdateTime(new Date()); authorityGroupWorkFlowNodeService.save(vo);
			 */
			authorityGroupWorkFlowNodeService.delete(v);
		} else {
			// 添加操作
			Long userId = ControllerHelper.getUserId();
			vo.setCreater(userId);
			vo.setLastUpdater(userId);

			vo.setCreateTime(new Date());
			vo.setDynamicPageId(dynamicPageId);
			vo.setLastUpdateTime(new Date());
			vo.setAuthorityGroup(authorityGroupId);
			if (nodes != null) {
				nodes = nodes.split("_")[0];
			}
			vo.setFlowNode(nodes);
			String id = authorityGroupWorkFlowNodeService.save(vo);
			vo.setId(id);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("AuthorityGroupWorkFlowNodeVO", vo);
		return vo;
	}

	@ResponseBody
	@RequestMapping(value = "/getComponentListByPageId")
	public List<StoreVO> getComponentListByPageId(@RequestParam(required = true) String dynamicPageId,
			@RequestParam(required = true) String authorityGroup,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = " T_ORDER ASC") String sortString) {
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andEqualTo("DYNAMICPAGE_ID", dynamicPageId).andLike("CODE",
				StoreService.COMPONENT_CODE + "%");
		List<StoreVO> vo = storeService.selectPagedByExample(baseExample, currentPage, pageSize, sortString);
		for (StoreVO s : vo) {
			JSONObject obj = JSON.parseObject(s.getContent());
			Map<String, Object> resParams = new HashMap<String, Object>();
			resParams.put("authorityGroupId", authorityGroup);
			resParams.put("componentId", obj.getString("name"));
			List<AuthorityCompoentVO> resoVOs = authorityCompoentService.queryResult("eqQueryList", resParams);
			if (resoVOs.size() > 0) {
				AuthorityCompoentVO v = resoVOs.get(0);
				s.setScope(v.getAuthorityValue());
			}
		}
		return vo;
	}

	// 查询包含组件的权限值用于回显
	@ResponseBody
	@RequestMapping(value = "/getComponentValueByInclude")
	public String getComponentValueByInclude(@RequestParam(required = true) String componentId,
			@RequestParam(required = true) String authorityGroup) {
		String value = null;
		Map<String, Object> resParams = new HashMap<String, Object>();
		resParams.put("authorityGroupId", authorityGroup);
		resParams.put("componentId", componentId);
		List<AuthorityCompoentVO> resoVOs = authorityCompoentService.queryResult("eqQueryList", resParams);
		if (resoVOs.size() > 0) {
			AuthorityCompoentVO vo = resoVOs.get(0);
			value = vo.getAuthorityValue();
		}
		return value;
	}

	@ResponseBody
	@RequestMapping(value = "/authorityGroupSave")
	public AuthorityGroupVO authorityGroupSave(String order, String dynamicPageId, String destry, String name) {

		AuthorityGroupVO auth = new AuthorityGroupVO();
		Object o = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (o instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) o;
			auth.setSystemId(system.getSysId());
		}
		auth.setName(name);
		auth.setDynamicPageId(dynamicPageId);
		PunUserBaseInfoVO u = ControllerHelper.getUser();
		auth.setCreater(u.getUserId());
		auth.setLastupdater(u.getUserId());
		auth.setDescription(destry);
		auth.setCreateTime(new Date());
		auth.setLastupdateTime(new Date());
		auth.setOrder(order);

		String id = authorityGroupService.save(auth);
		auth.setId(id);

		ModelAndView mv = new ModelAndView();
		mv.addObject("authorityGroupVO", auth);
		return auth;
	}

	@ResponseBody
	@RequestMapping(value = "/authorityGroupUpdate")
	public AuthorityGroupVO authorityGroupUpdate(String order, String id, String destry, String name) {

		AuthorityGroupVO auth = new AuthorityGroupVO();
		auth = authorityGroupService.findById(id);
		auth.setName(name);

		auth.setLastupdater(ControllerHelper.getUserId());
		auth.setDescription(destry);
		auth.setLastupdateTime(new Date());
		auth.setOrder(order);
		authorityGroupService.save(auth);
		ModelAndView mv = new ModelAndView();
		mv.addObject("authorityGroupVO", auth);
		return auth;
	}

	// 配置权限
	@ResponseBody
	@RequestMapping(value = "/addAuthorityValue")
	public AuthorityCompoentVO addAuthorityValue(String componentId, String value, String authorityGroupId,
			String status, String includeComponent) {

		AuthorityCompoentVO vo = new AuthorityCompoentVO();

		Map<String, Object> resParams = new HashMap<String, Object>();
		resParams.put("authorityGroupId", authorityGroupId);
		resParams.put("componentId", componentId);

		List<AuthorityCompoentVO> resoVOs = authorityCompoentService.queryResult("eqQueryList", resParams);

		if (resoVOs.size() > 0) {
			// 修改操作
			AuthorityCompoentVO v = resoVOs.get(0);
			if (status.equals("1")) {
				if (v.getAuthorityValue() == null) {
					v.setAuthorityValue("");
				}
				v.setAuthorityValue(v.getAuthorityValue() + value);
			}
			if (status.equals("0")) {
				v.setAuthorityValue(v.getAuthorityValue().replace(value, ""));
			}

			v.setLastUpdater(ControllerHelper.getUserId());
			v.setLastUpdateTime(new Date());
			authorityCompoentService.save(v);

		} else {

			// 添加操作
			vo.setComponentId(componentId);
			Long userId = ControllerHelper.getUserId();
			vo.setCreater(userId);
			vo.setLastUpdater(userId);
			if (includeComponent != null) {
				vo.setIncludeComponent(includeComponent);
			} else {
				vo.setIncludeComponent("");
			}
			vo.setCreateTime(new Date());
			vo.setLastUpdateTime(new Date());
			vo.setAuthorityValue(value);
			vo.setAuthorityGroupId(authorityGroupId);
			String id = authorityCompoentService.save(vo);
			vo.setId(id);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("authorityCompoentVO", vo);
		return vo;
	}

	// 根据id查询
	@ResponseBody
	@RequestMapping(value = "/findAuthorityById")
	public AuthorityGroupVO authorityGroupSave(String id) {
		AuthorityGroupVO auth = authorityGroupService.findById(id);
		ModelAndView mv = new ModelAndView();
		mv.addObject("authorityGroupVO", auth);
		return auth;
	}

	@ResponseBody
	@RequestMapping(value = "deleteByAjax")
	public String deleteComponentByAjax(@RequestParam(value = "_selects") String selects) {
		String[] ids = selects.split(",");
		if (authorityGroupService.delete(ids)) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 根据name查询动态页面，分页显示
	 * 
	 * @param name
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(@RequestParam(required = false) String name,
			@RequestParam(required = false) String dynamicPageId,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(dynamicPageId)) {

		}
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}

		Object obj = SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			BaseExample example = new BaseExample();
			if (StringUtils.isNotBlank(name)) {
				example.createCriteria().andLike("name", "%" + name + "%");
			}
			PageList<AuthorityGroupVO> vos = authorityGroupService.selectPagedByExample(example, currentPage, pageSize,
					"ID desc");
			mv.addObject("vos", vos);
		}
		mv.addObject("name", name);
		mv.addObject("currentPage", currentPage);
		mv.addObject("pageSize", pageSize);
		mv.setViewName("formdesigner/page/authgroup/ag-list");
		return mv;
	}

	/**
	 * 查询动态页面，并跳转到编辑页面a
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView editPage(@RequestParam(value = "_selects", required = false) String id,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		// 根据id来查询，如果id为空，则为新增
		ModelAndView mv = new ModelAndView();
		mv.addObject("currentPage", currentPage);
		mv.setViewName("formdesigner/page/authgroup/ag-edit");
		if (id != null) {
			AuthorityGroupVO vo = authorityGroupService.findById(id);
			if (vo != null) {

			}
			mv.addObject("vo", vo);
		}
		return mv;
	}

}
