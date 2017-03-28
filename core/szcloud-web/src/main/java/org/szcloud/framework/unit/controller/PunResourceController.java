package org.szcloud.framework.unit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.core.utils.constants.ResourceTypeEnum;
import org.szcloud.framework.unit.service.PunResourceService;
import org.szcloud.framework.unit.service.PunRoleAccessService;
import org.szcloud.framework.unit.vo.PunResourceVO;

@Controller
@RequestMapping("/unit")
public class PunResourceController {
	
	@Resource(name="punResourceServiceImpl")
	private PunResourceService resoService;//资源Service
	
	@Resource(name="punRoleAccessServiceImpl")
	private PunRoleAccessService accessService;//权限Service
	
	/**
	 * 查找未授权的组件
	 * @param sysId 系统ID
	 * @param roleId 角色ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="queryComponent")
	public ModelAndView queryComponent(Long sysId,Long roleId) {
		ModelAndView mView = new ModelAndView();
		mView.setViewName("unit/componentAccess-edit");
		try {
			
			Map<String, Object> params = new HashMap<String, Object>();
			String resoTypes = "'"+ResourceTypeEnum.MENU_LINK.getkey()+"','"+ResourceTypeEnum.MENU_DYNAMICPAGE.getkey()+"'";
			params.put("sysId", sysId);
			params.put("resoTypes", resoTypes);
			List<PunResourceVO> vos = resoService.queryResult("eqQueryListGroup",
					params);//所有资源
			Map<String, Object> resMap = new HashMap<String, Object>();
			for (PunResourceVO punResourceVO : vos) {
				String mark = ResourceTypeEnum.getResourceTypeValue(punResourceVO.getResouType());
				if(!resMap.containsKey(mark)){
					List<PunResourceVO> punResourceVOs = new ArrayList<PunResourceVO>();
					punResourceVOs.add(punResourceVO);
					resMap.put(mark, punResourceVOs);
				}else {
					List<PunResourceVO> punResourceVOs = (List<PunResourceVO>) resMap.get(mark);
					punResourceVOs.add(punResourceVO);
				}
			}
			
			Map<String, Object> authorParams = new HashMap<String, Object>();
			authorParams.put("sysId", sysId);
			authorParams.put("roleId", roleId);
			//排除菜单地址和动态表单
			authorParams.put("resoTypes", resoTypes);
			//已授权的资源
			mView.addObject("resMap", resMap);
			mView.addObject("roleId", roleId);//角色ID
			mView.addObject("sysId", sysId);//系统ID
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mView;
	}
}