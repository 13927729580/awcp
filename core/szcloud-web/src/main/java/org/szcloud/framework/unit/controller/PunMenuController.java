package org.szcloud.framework.unit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.Criteria;
import org.szcloud.framework.formdesigner.application.service.FormdesignerService;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.unit.service.PunGroupService;
import org.szcloud.framework.unit.service.PunMenuService;
import org.szcloud.framework.unit.service.PunRoleInfoService;
import org.szcloud.framework.unit.service.PunSystemService;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.utils.PermissionException;
import org.szcloud.framework.unit.vo.PunMenuVO;
import org.szcloud.framework.unit.vo.PunSystemVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/unit")
public class PunMenuController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("punMenuServiceImpl")
	PunMenuService  menuService;
	@Autowired
	@Qualifier("punGroupServiceImpl")
	PunGroupService groupService;
	@Autowired
	@Qualifier("punRoleInfoServiceImpl")
	PunRoleInfoService roleService;
	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;
	@Autowired
	@Qualifier("punSystemServiceImpl")
	private PunSystemService punSystemService;
	@Autowired
	FormdesignerService formdesignerServiceImpl;
	
	private StringBuffer valMessage = null;//校验信息
	
	@RequestMapping(value="intoPunMenuList")
	public ModelAndView intoPunMenuList(@RequestParam("boxs") Long sysId){
		ModelAndView mv = new ModelAndView();
		mv.addObject("sysId", sysId);
		mv.setViewName("redirect:/unit/punMenuList.do");
		return mv;
	}
		
	/**
	 * 
	* @Title: punMenuList 
	* @Description: 查询并以列表的形式显示
	* @author ljw 
	* @param @return    
	* @return ModelAndView
	* @throws
	 */
	@RequestMapping(value="punMenuList")
	public ModelAndView punMenuList(@ModelAttribute PunMenuVO vo,Model model,
			@RequestParam(value="sysId", required=true) Long sysId,
			@RequestParam(value="currentPage", defaultValue="1") int currentPage,
			@RequestParam(value="pageSize", defaultValue="10") int pageSize){
		
		try {
			if(currentPage <= 0)
				currentPage = 1;
			if(pageSize <= 5)
				pageSize = 10;
			PunSystemVO sys = punSystemService.findById(sysId);
			model.addAttribute("sys", sys);
			BaseExample example = new BaseExample();
			Criteria criteria = example.createCriteria();
			if(StringUtils.isNotBlank(vo.getMenuName())){
				criteria.andLike("MENU_NAME", vo.getMenuName());
			}
			criteria.andEqualTo("SYSTEM_ID", sysId);
			String sortString = "MENU_ID desc";
			PageList<PunMenuVO> vos = menuService.selectPagedByExample(example, currentPage, pageSize, sortString);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("vos", vos);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("sysId", sysId);
			//返回查询条件
			model.addAttribute("vo",vo);
		}catch(PermissionException ea){
			model.addAttribute("result", ea.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("result", "System error.");
		}
		return new ModelAndView("/unit/punMenu-list");
		
	}
	
	/**
	 * 保存
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/punMenuSave",method=RequestMethod.POST)
	public ModelAndView punMenuSave(
			@ModelAttribute("vo") PunMenuVO vo, Model model,
			RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("boxs", vo.getSysId());
			mv.setViewName("redirect:/unit/intoPunMenuList.do");
			if (validate(vo)) {
				if(null == vo.getParentMenuId()){
					vo.setPid(null);
				}else{
					PunMenuVO reVo = menuService.findById(vo.getParentMenuId());
					if(null == reVo){
						vo.setPid(null); 
					} else {
						if(reVo.getParentMenuId() == null){
							vo.setPid(reVo.getMenuId().toString());
						} else {
							vo.setPid(reVo.getPid()+","+reVo.getMenuId().toString());
						}
					}
				}
				menuService.addOrUpdate(vo);
				mv.addObject("result","Add/Update successfully.");
			} else {
				mv.addObject("result", "Failed" + valMessage.toString()
						+ "。");
			}
		}catch(PermissionException ea){
			mv.addObject("result", ea.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "System error.");
		}
		return mv;
	}
	
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	@RequestMapping(value="punMenuGet")
	public ModelAndView punMenuGet(Long[] boxs){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punMenu-edit");
		try {
			PunMenuVO vo = menuService.findById(boxs[0]);
			if(null != vo.getDynamicPageId()){
				DynamicPageVO pageVO = formdesignerServiceImpl.findById(vo.getDynamicPageId());
				vo.setDynamicpageName(pageVO.getName());
			}
			mv.addObject("vo", vo);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "System error.");
		}
		return mv;
	} 
	
	/**
	 * 校验必填项
	 * @param vo
	 * @return boolean
	 */
	private boolean validate(PunMenuVO vo){
		if(null == vo){
			return false;
		}
		valMessage = new StringBuffer();

		if(StringUtils.isEmpty(vo.getMenuName())){
			valMessage.append(",menu name required");
			return false;
		}
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sysId", vo.getSysId());//系统ID
		params.put("menuName", vo.getMenuName());
		List<PunMenuVO> rVOs = null;
		//新增时校验
		if (null == vo.getMenuId()) {
			//同系统下，不能有相同的菜单
			rVOs = menuService.queryResult("eqQueryList", params);		
		}else {//更新时
			PunMenuVO menuVO = menuService.findById(vo.getMenuId());
			//更改后的名字与原名字不符时
			if(!menuVO.getMenuName().equals(vo.getMenuName())){
				rVOs = menuService.queryResult("eqQueryList", params);
			}
		}
		if (null != rVOs && rVOs.size() > 0) {
			valMessage.append(",The same menu name has exsit");
			return false;
		}
		return true;
	}

}
