package cn.org.awcp.unit.dev.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.ResourceTypeEnum;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.unit.service.PunMenuService;
import cn.org.awcp.unit.vo.PunAJAXStatusVO;
import cn.org.awcp.unit.vo.PunMenuVO;
import cn.org.awcp.unit.vo.PunSystemVO;

@Controller
@RequestMapping("/dev")
public class PunDevMenuController {

	/**
	 * 日志对象
	 */
	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	@Qualifier("punMenuServiceImpl")
	PunMenuService menuService;
	@Autowired
	@Qualifier("formdesignerServiceImpl")
	FormdesignerService dyNamicPageService;
	@Autowired
	FormdesignerService formdesignerServiceImpl;

	private StringBuffer valMessage = null;// 校验信息

	/**
	 * 菜单编辑弹窗
	 * 
	 * @return
	 */
	@RequestMapping(value = "/menuEditDialog")
	public ModelAndView menuEditDialog(PunMenuVO vo, Model model) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/punMenu-edit");
		model.addAttribute("vo", vo);
		return mv;
	}

	/**
	 * 根据ID查找**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punMenuGet")
	public ModelAndView punMenuGet(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/punMenu-edit");
		try {
			PunMenuVO vo = menuService.findById(boxs[0]);
			if (null != vo.getDynamicPageId()) {
				DynamicPageVO pageVO = formdesignerServiceImpl.findById(vo.getDynamicPageId());
				vo.setDynamicpageName(pageVO.getName());
			}
			mv.addObject("vo", vo);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "异常");
		}
		return mv;
	}

	/**
	 * ljw 2014-12-15 获取动态表单信息，用于select2
	 */
	@ResponseBody
	@RequestMapping(value = "dynamicPageAjaxQuery")
	public List<DynamicPageVO> dynamicPageAjaxQuery(@RequestParam(required = false) String pageName,
			@RequestParam(required = false) String dynamicPageId, @RequestParam(required = false) Long sysId) {
		try {
			List<DynamicPageVO> vos = new ArrayList<DynamicPageVO>();
			if (null != dynamicPageId) {// 编辑
				vos = new ArrayList<DynamicPageVO>();
				DynamicPageVO vo = dyNamicPageService.findById(dynamicPageId);
				vos.add(vo);
			} else {// 新增
				BaseExample example = new BaseExample();
				Criteria criteria = example.createCriteria();
				if (sysId == null) {
					PunSystemVO object = (PunSystemVO) SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
					// 限制同一系统的动态表单
					if (null != object.getSysId()) {
						criteria.andEqualTo("SYSTEM_ID", object.getSysId());
					}
				} else {
					criteria.andEqualTo("SYSTEM_ID", sysId);
				}

				criteria.andLike("name", pageName + "%");
				vos = dyNamicPageService.selectPagedByExample(example, 1, 99999, null);
			}
			return vos;
		} catch (Exception e) {
			logger.info("ERROR", e);
			return null;
		}
	}

	/**
	 * ljw 2014-12-17 根据id获取动态页面
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "dynamicPageAjaxGet")
	public DynamicPageVO dynamicPageAjaxGet(String id) {
		return dyNamicPageService.findById(id);
	}

	/**
	 * 通过AJAX保存menu，如果保存成功，返回已保存的menu的json字符串；
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/punMenuAJAXSave")
	public PunAJAXStatusVO punMenuAJAXSave(@ModelAttribute("vo") PunMenuVO vo) {
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		try {
			if (validate(vo)) {
				if (null == vo.getParentMenuId()) {
					vo.setPid("");
				} else {
					PunMenuVO reVo = menuService.findById(vo.getParentMenuId());
					if (null == reVo) {
						logger.error("不存在该父菜单");
					}
					if (reVo != null && reVo.getMenuId() != null) {
						if (StringUtils.isNotEmpty(reVo.getPid())) {
							vo.setPid(reVo.getPid() + vo.getParentMenuId().toString() + ",");// ","用来根据PID查询某结点的子孙资源
						} else {
							vo.setPid(reVo.getMenuId().toString() + ","); // ","用来根据PID查询某结点的子孙资源
						}
					}
				}
				if (StringUtils.isNotEmpty(vo.getMenuAddress())) {// 菜单绑定的是地址
					vo.setMenuType(ResourceTypeEnum.MENU_LINK.getkey());
				} else if (vo.getDynamicPageId() != null) {// 菜单绑定的是动态表单
					vo.setMenuType(ResourceTypeEnum.MENU_DYNAMICPAGE.getkey());
				}
				menuService.addOrUpdate(vo);
				respStatus.setData(vo);
			} else {
				respStatus.setStatus(1);
				respStatus.setMessage("校验失败" + valMessage.toString());
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			logger.error("系统异常");
			respStatus.setStatus(2);
			respStatus.setMessage("系统异常:" + e.getMessage());
		}
		return respStatus;
	}

	/**
	 * 
	 * @Title: punMenuAJAXDelete
	 * @Description: 根据ID删除菜单
	 */
	@ResponseBody
	@RequestMapping(value = "punMenuAJAXDelete")
	public PunAJAXStatusVO punMenuAJAXDelete(@ModelAttribute("vo") PunMenuVO vo) {
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		try {
			menuService.delete(vo.getMenuId());
			respStatus.setData(vo);
		} catch (Exception e) {
			logger.info("ERROR", e);
			respStatus.setStatus(1);
			respStatus.setMessage("系统异常:" + e.getMessage());
		}
		return respStatus;
	}

	/**
	 * 校验必填项
	 * 
	 * @param vo
	 * @return boolean
	 */
	private boolean validate(PunMenuVO vo) {
		if (null == vo) {
			return false;
		}
		valMessage = new StringBuffer();

		if (StringUtils.isNotEmpty(vo.getMenuAddress()) && StringUtils.isNotBlank(vo.getDynamicPageId())) {
			valMessage.append(",资源地址和动态表单只能二者选其一");
			return false;
		}

		if (StringUtils.isEmpty(vo.getMenuName())) {
			valMessage.append(",资源名称必填");
			return false;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sysId", vo.getSysId());// 系统ID
		params.put("parentMenuId", vo.getParentMenuId());// 父结点
		params.put("menuName", vo.getMenuName());
		params.put("type", vo.getType());
		List<PunMenuVO> rVOs = null;
		// 新增时校验
		if (null == vo.getMenuId()) {
			// 同系统下，同一目录下，不能有相同的菜单
			rVOs = menuService.queryResult("eqQueryList", params);

		} else {// 更新时
			PunMenuVO menuVO = menuService.findById(vo.getMenuId());
			// 更改后的名字与原名字不符时
			if (!menuVO.getMenuName().equals(vo.getMenuName()) || vo.getType() != menuVO.getType()) {
				rVOs = menuService.queryResult("eqQueryList", params);
			}
		}
		if (null != rVOs && rVOs.size() > 0) {
			valMessage.append("，同一系统的同一目录下不允许有相同名称的资源");
			return false;
		}
		return true;
	}

}
