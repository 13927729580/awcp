package org.szcloud.framework.unit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.OperTypeEnum;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.unit.service.PdataDictionaryService;
import org.szcloud.framework.unit.service.PunMenuService;
import org.szcloud.framework.unit.service.PunRoleAccessService;
import org.szcloud.framework.unit.service.PunRoleInfoService;
import org.szcloud.framework.unit.utils.BackEndUserUtils;
import org.szcloud.framework.unit.utils.JsonFactory;
import org.szcloud.framework.unit.utils.PermissionException;
import org.szcloud.framework.unit.utils.ResourceTreeUtils;
import org.szcloud.framework.unit.vo.PunAJAXStatusVO;
import org.szcloud.framework.unit.vo.PunMenuVO;
import org.szcloud.framework.unit.vo.PunResourceTreeNode;
import org.szcloud.framework.unit.vo.PunRoleAccessVO;
import org.szcloud.framework.unit.vo.PunRoleInfoVO;
import org.szcloud.framework.unit.vo.PunSystemVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/unit")
public class PunRoleAccessController {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(PunRoleAccessController.class);
	@Autowired
	@Qualifier("punRoleAccessServiceImpl")
	PunRoleAccessService roleAccService;
	@Autowired
	@Qualifier("punRoleInfoServiceImpl")
	PunRoleInfoService roleService;// 角色Service
	@Autowired
	@Qualifier("punMenuServiceImpl")
	PunMenuService menuService;// 菜单service
	@Autowired
	@Qualifier("pdataDictionaryServiceImpl")
	PdataDictionaryService dicService;// 数据字典Service

	private StringBuffer valMessage;// 校验信息

	@RequestMapping(value = "/intoPunRoleAccess", method = RequestMethod.GET)
	public ModelAndView intoSave(Model model) {
		if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
			return null;
		}
		return new ModelAndView("/unit/punRoleAccess-edit");
	}

	/**
	 * 进入授权页面
	 * 
	 * @param sysId
	 *            系统ID
	 * @param roleId
	 *            角色Id
	 * @return
	 */
	@RequestMapping(value = "intoAccessAuthor")
	public ModelAndView intoAccessAuthor(Long boxs, Long sysId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("sysId", boxs);
		mv.addObject("roleId", sysId);
		mv.setViewName("unit/dev/accessAuthor");
		return mv;
	}

	/**
	 * 保存菜单权限 1.当传一个菜单结点，且传入的父节点ID不为空，则将该结点下所有的子节点赋权限； 2.如果传入的父节点Id为空，则对该结点赋权限
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/punRoleAccessAJAXSave")
	public PunAJAXStatusVO punRoleAccessAJAXSave(PunRoleAccessVO vo) {
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		try {
			if (vo != null && vo.getRoleId() != null && vo.getResourceId() != null) {
				roleAccService.assignMenuAccessRight(vo.getRoleId(), vo.getResourceId(), vo.getOperType());
				respStatus.setStatus(0);
			} else {
				respStatus.setStatus(1);

			}
		} catch (Exception e) {
			respStatus.setStatus(1);
			respStatus.setMessage(e.getMessage());
		}

		return respStatus;
	}

	/**
	 * 保存菜单权限 1.当传一个菜单结点，且传入的父节点ID不为空，则将该结点下所有的子节点赋权限； 2.如果传入的父节点Id为空，则对该结点赋权限
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/punRoleAccessAJAXDelete")
	public PunAJAXStatusVO punRoleAccessAJAXDelete(PunRoleAccessVO vo) {

		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		try {
			roleAccService.removeMenuAccessRight(vo.getRoleId(), vo.getResourceId());
			respStatus.setStatus(0);

		} catch (Exception e) {
			respStatus.setStatus(1);
			respStatus.setMessage(e.getMessage());
		}
		return respStatus;
	}

	/**
	 * 保存资源权限
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/punResoAccessAJAXSave")
	public PunAJAXStatusVO punResoAccessAJAXSave(PunRoleAccessVO vo) {
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		try {
			if (vo != null && vo.getRoleId() != null && vo.getResourceId() != null) {
				vo.setOperType(OperTypeEnum.OPER_ALL.getkey());
				roleAccService.addOrUpdateRole(vo);
				respStatus.setStatus(0);
			} else {
				respStatus.setStatus(1);
			}
		} catch (Exception e) {
			respStatus.setStatus(1);
			respStatus.setMessage(e.getMessage());
		}

		return respStatus;
	}

	/**
	 * 保存菜单权限 1.当传一个菜单结点，且传入的父节点ID不为空，则将该结点下所有的子节点赋权限； 2.如果传入的父节点Id为空，则对该结点赋权限
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/punResoAccessAJAXDelete")
	public PunAJAXStatusVO punResoAccessAJAXDelete(PunRoleAccessVO vo) {

		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roleId", vo.getRoleId());
			params.put("resourceId", vo.getResourceId());
			roleAccService.resoAuthorDel(params);
			respStatus.setStatus(0);

		} catch (Exception e) {
			respStatus.setStatus(1);
			respStatus.setMessage(e.getMessage());
		}
		return respStatus;
	}

	/**
	 * 保存
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/punRoleAccessSave", method = RequestMethod.POST)
	public ModelAndView punRoleAccessSave(PunRoleAccessVO vo, Model model, RedirectAttributes ra) {
		try {
			if (validate(vo)) {
				if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
					throw new PermissionException("You has not permission to operate.");
				}
				roleAccService.addOrUpdateRole(vo);

				ra.addFlashAttribute("result", "Add successfully.");
				return new ModelAndView("redirect:/unit/intoPunRoleAccessList.do?currentPage=0");
			} else {
				model.addAttribute("vo", vo);
				model.addAttribute("result", "Validate failed" + valMessage.toString() + "。");
			}
		} catch (PermissionException ea) {
			model.addAttribute("result", ea.getMessage());
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "System error.");
		}
		return new ModelAndView("/unit/punRoleAccess-edit");
	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punRoleAccessGet", method = RequestMethod.POST)
	public ModelAndView punRoleAccessGet(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punRoleAccess-edit");
		try {
			PunRoleAccessVO vo = roleAccService.findById(boxs[0]);
			mv.addObject("vo", vo);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "异常");
		}
		return mv;
	}

	/**
	 * 根据SystemId,Membership返回系统的菜单树(json)和相应的权限；
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punSysMenuAccessEdit")
	public ModelAndView punSysMenuAccessEdit(@ModelAttribute("vo") PunRoleInfoVO vo, HttpServletRequest request) {
		PunSystemVO sysVO = (PunSystemVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/punSysMenuAccess-edit");
		try {
			List<PunMenuVO> resoVOs = menuService.getPunMenuBySys(sysVO);
			List<PunMenuVO> accVOS = new ArrayList<PunMenuVO>();
			if (vo != null && vo.getRoleId() != null)
				accVOS = menuService.getByRoleAndSys(vo, sysVO);

			this.setAccessRight(resoVOs, accVOS);
			ResourceTreeUtils rtu = new ResourceTreeUtils();
			List<PunMenuVO[]> list = rtu.generateTreeView(resoVOs);
			Map<Integer, List<PunResourceTreeNode>> resultMap = new HashMap<Integer, List<PunResourceTreeNode>>();
			Integer index = 1;
			for (PunMenuVO[] prvo : list) {
				// 以目录根节点的index为key，以manyNodeTree为value
				List<PunResourceTreeNode> manyNodeTree = ResourceTreeUtils.getPlainAccessZNodes(prvo);
				resultMap.put(index, manyNodeTree);
				index++;
			}
			JsonFactory factory = new JsonFactory();
			mv.addObject("menuJson", factory.encode2Json(resultMap));
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	@RequestMapping(value = "intoPunRoleAccessList", method = RequestMethod.GET)
	public ModelAndView intoPunRoleAccessList() {
		if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
			return null;
		}
		return new ModelAndView("redirect:/unit/punRoleAccessList.do?currentPage=0");
	}

	/**
	 * 
	 * @Title: punRoleAccessList @Description: 分页查询并以列表的形式显示 @author
	 *         ljw @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "punRoleAccessList", method = RequestMethod.GET)
	public ModelAndView punRoleAccessList(@ModelAttribute PunRoleAccessVO vo, Model model, int currentPage,
			HttpSession session) {
		try {
			if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
				throw new PermissionException("You has not permission to operate.");
			}
			int maxSize = this.roleAccService.findAll().size();
			String queryStr = "eqQueryList";
			int pageSize = 10;
			int maxPage = 0;
			if (maxSize % pageSize > 0) {
				maxPage = maxSize / pageSize + 1;
			} else {
				maxPage = maxSize / pageSize;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			if (currentPage == 0) {
				currentPage = 1;
			}
			if (maxPage < currentPage) {
				currentPage = maxPage;
			}
			String sortString = "ROLE_ID.desc";

			if (null != vo.getRoleId()) {
				map.put("roleId", vo.getRoleId());
			}
			PageList<PunRoleAccessVO> vos = roleAccService.queryPagedResult(queryStr, map, currentPage, pageSize,
					sortString);
			model.addAttribute("maxPage", maxPage);
			model.addAttribute("vos", vos);
			model.addAttribute("currentPage", currentPage);
			// 返回查询条件
			model.addAttribute("vo", vo);
			return new ModelAndView("/unit/punRoleAccess-list");
		} catch (PermissionException ea) {
			model.addAttribute("result", ea.getMessage());
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "System error.");
		}
		return new ModelAndView("/unit/punRoleAccess-list");

	}

	/**
	 * 
	 * @Title: punRoleAccessDelete @Description: 根据ID删除 @author
	 *         ljw @param @param id @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "punRoleAccessDelete", method = RequestMethod.POST)
	public ModelAndView punRoleAccessDelete(Long[] boxs, int currentPage, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/unit/punRoleAccessList.do?currentPage=" + currentPage);
		try {
			for (Long id : boxs) {
				roleAccService.delete(id);
			}
			ra.addFlashAttribute("result", "Delete successfully");
		} catch (Exception e) {
			logger.info("ERROR", e);
			ra.addFlashAttribute("result", "System error.");
		}
		return mv;
	}

	/**
	 * 
	 * @Title: roleAcces @Description: 角色关联权限 @author ljw @param @param
	 *         roleId @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "roleAcces", method = RequestMethod.POST)
	public ModelAndView roleAcces(Long roleId, Model model, RedirectAttributes ra, int currentPage,
			HttpSession session) {
		try {
			if (null == roleId) {
				ra.addFlashAttribute("result", "Please select one to operate.");
				return new ModelAndView("redirect:/unit/punRoleInfoList.do?currentPage=" + currentPage);
			}
			PunRoleAccessVO vo = new PunRoleAccessVO();
			vo.setRoleId(roleId);
			model.addAttribute("vo", vo);
			// 获取当前系统
			PunSystemVO sysVO = (PunSystemVO) session.getAttribute(SessionContants.CURRENT_SYSTEM);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("bakLong", roleId);
			if (null != sysVO) {
				params.put("bakLong2", sysVO.getSysId());
			}
			return new ModelAndView("/unit/punRoleAccess-edit");
		} catch (Exception e) {
			logger.info("ERROR", e);
			ra.addFlashAttribute("result", "System error.");
			return new ModelAndView("redirect:/unit/punRoleAccessList.do?currentPage=" + currentPage);
		}
	}

	/**
	 * 校验
	 * 
	 * @param vo
	 * @return boolean
	 */
	private boolean validate(PunRoleAccessVO vo) {
		valMessage = new StringBuffer();
		if (null == vo.getRoleId()) {
			valMessage.append(",Role required");
			return false;
		} else if (null == vo.getOperType()) {
			valMessage.append(",Operation required");
			return false;
		}
		// 执行新增操作时,校验录入角色和资源是否已有关联
		else if (null == vo.getRoleAccId() && null != vo.getRoleId() && null != vo.getResourceId()) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roleId", vo.getRoleId());
			params.put("resourceId", vo.getResourceId());
			List<PunRoleAccessVO> rAVOs = roleAccService.queryResult("eqQueryList", params);
			if (null != rAVOs && rAVOs.size() > 0) {
				valMessage.append(",Role exsit");
				return false;
			}
		}
		return true;
	}

	private void setAccessRight(List<PunMenuVO> mainList, List<PunMenuVO> accessList) {
		for (int i = 0; i < accessList.size(); i++) {
			PunMenuVO temp = accessList.get(i);
			for (int j = 0; j < mainList.size(); j++) {
				if (temp.getMenuId().longValue() == 167) {
					logger.debug("lll");
				}
				if (temp.getMenuId().longValue() == mainList.get(j).getMenuId().longValue()) {
					mainList.get(j).setChecked(true);
					break;
				}
			}
		}
	}

}
