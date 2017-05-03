package org.szcloud.framework.unit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.unit.service.PunMenuService;
import org.szcloud.framework.unit.service.PunRoleAccessService;
import org.szcloud.framework.unit.service.PunRoleInfoService;
import org.szcloud.framework.unit.service.PunSystemService;
import org.szcloud.framework.unit.utils.BackEndUserUtils;
import org.szcloud.framework.unit.utils.JsonFactory;
import org.szcloud.framework.unit.utils.PermissionException;
import org.szcloud.framework.unit.utils.ResourceTreeUtils;
import org.szcloud.framework.unit.utils.UnitBaseConstants;
import org.szcloud.framework.unit.vo.PunGroupVO;
import org.szcloud.framework.unit.vo.PunMenuVO;
import org.szcloud.framework.unit.vo.PunResourceTreeNode;
import org.szcloud.framework.unit.vo.PunResourceVO;
import org.szcloud.framework.unit.vo.PunSystemVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/unit")
public class PunSystemController {

	@Autowired
	@Qualifier("punSystemServiceImpl")
	PunSystemService sysService;
	@Autowired
	@Qualifier("punRoleAccessServiceImpl")
	PunRoleAccessService roleAccService;
	@Autowired
	@Qualifier("punRoleInfoServiceImpl")
	PunRoleInfoService roleService;
	@Autowired
	@Qualifier("punMenuServiceImpl")
	PunMenuService menuService;

	/**
	 * 
	 * @Title: intoPunSystem @Description: 进入组编辑页面 @author
	 *         ljw @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "/intoPunSystem", method = RequestMethod.GET)
	public ModelAndView intoPunSystem() {
		if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
			return null;
		}
		return new ModelAndView("/unit/punSystem-edit");
	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punSystemGet")
	public ModelAndView punSystemGet(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/punSystem-edit");
		try {
			PunSystemVO vo = sysService.findById(boxs[0]);
			mv.addObject("vo", vo);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 使用方查找系统
	 * 
	 * @param boxs
	 * @param sysStatus
	 * @return
	 */
	@RequestMapping(value = "punSysQuery")
	public ModelAndView punSysQuery(String sysName, String relateOrNot) {
		ModelAndView mv = new ModelAndView();

		try {
			List<PunSystemVO> syses = null;
			Map<String, Object> sysParams = new HashMap<String, Object>();
			PunGroupVO group = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			sysParams.put("groupId", group.getGroupId());
			if (StringUtils.isNotEmpty(sysName)) {
				sysParams.put("sysName", sysName);
			}
			// 查询已发布待关联系统
			if (StringUtils.isEmpty(relateOrNot) || relateOrNot.equals(UnitBaseConstants.SYS_NOT_RELATED)) {
				sysParams.put("sysStatus", UnitBaseConstants.SYS_TATUS_RELEASE);
				syses = sysService.queryPagedResult("not_related_sys", sysParams, 1, 99999, null);
				mv.addObject("relateOrNot", "0");
				// syses = sysService.queryResult("not_related_sys", sysParams);
				mv.addObject("vos", syses);
				mv.setViewName("/unit/pun-group-not-related-sys");
			} else if (relateOrNot.equals(UnitBaseConstants.SYS_RELATED)) { // 查询已关联系统
				sysParams.put("sysStatus", UnitBaseConstants.SYS_TATUS_RELEASE);
				syses = sysService.queryPagedResult("related_sys", sysParams, 1, 99999, null);
				// syses = sysService.queryResult("related_sys", sysParams);
				mv.addObject("vos", syses);
				mv.addObject("relateOrNot", "1");
				mv.setViewName("/unit/pun-group-related-sys");
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 使用方查找系统
	 * 
	 * @param boxs
	 * @param sysStatus
	 * @return
	 */
	@RequestMapping(value = "punRelateSys")
	public ModelAndView punRelateSys(String sysName,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView();
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		mv.setViewName("/unit/pun-group-related-sys");
		try {
			List<PunSystemVO> syses = null;
			Map<String, Object> sysParams = new HashMap<String, Object>();
			PunGroupVO group = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			sysParams.put("groupId", group.getGroupId());
			if (StringUtils.isNotEmpty(sysName)) {
				sysParams.put("sysName", "%" + sysName + "%");
			}
			sysParams.put("sysStatus", UnitBaseConstants.SYS_TATUS_RELEASE);
			syses = sysService.queryPagedResult("related_sys", sysParams, currentPage, pageSize, null);
			mv.addObject("vos", syses);
			mv.addObject("currentPage", currentPage);
			mv.addObject("pageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	@RequestMapping(value = "punNotRelateSys")
	public ModelAndView punNotRelateSys(String sysName,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView();
		if (pageSize < 10) {
			pageSize = 10;
		}
		if (currentPage <= 0) {
			currentPage = 1;
		}
		mv.setViewName("/unit/pun-group-not-related-sys");
		try {
			List<PunSystemVO> syses = null;
			Map<String, Object> sysParams = new HashMap<String, Object>();
			PunGroupVO group = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			sysParams.put("groupId", group.getGroupId());
			if (StringUtils.isNotEmpty(sysName)) {
				sysParams.put("sysName", "%" + sysName + "%");
			}
			// 查询已发布待关联系统
			sysParams.put("sysStatus", UnitBaseConstants.SYS_TATUS_RELEASE);
			syses = sysService.queryPagedResult("not_related_sys", sysParams, currentPage, pageSize, null);
			mv.addObject("vos", syses);

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 根据SystemId返回系统的菜单树(json)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punSysMenuEdit", method = RequestMethod.GET)
	public ModelAndView punSysMenuEdit(@ModelAttribute("sysVO") PunSystemVO sysVO) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/punSysMenu-edit");
		try {
			List<PunMenuVO> resoVOs = menuService.getPunMenuBySys(sysVO);
			ResourceTreeUtils rtu = new ResourceTreeUtils();
			List<PunMenuVO[]> list = rtu.generateTreeView(resoVOs);
			Map<Integer, List<PunResourceTreeNode>> resultMap = new HashMap<Integer, List<PunResourceTreeNode>>();
			Integer index = 1;
			for (PunMenuVO[] prvo : list) {
				// 以目录根节点的index为key，以manyNodeTree为value
				List<PunResourceTreeNode> manyNodeTree = ResourceTreeUtils.getPlainZNodes(prvo);
				resultMap.put(index, manyNodeTree);
				index++;
			}
			JsonFactory factory = new JsonFactory();
			mv.addObject("menuJson", factory.encode2Json(resultMap));
			mv.addObject("sysVO", sysVO);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 根据SystemId返回系统的菜单树(json)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "dev/punSysMenuEdit", method = RequestMethod.GET)
	public ModelAndView devPunSysMenuEdit(@ModelAttribute("sysVO") PunSystemVO sysVO) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/punSysMenu-edit");
		try {
			List<PunMenuVO> resoVOs = menuService.getPunMenuBySys(sysVO);
			ResourceTreeUtils rtu = new ResourceTreeUtils();
			List<PunMenuVO[]> list = rtu.generateTreeView(resoVOs);
			Map<Integer, List<PunResourceTreeNode>> resultMap = new HashMap<Integer, List<PunResourceTreeNode>>();
			Integer index = 1;
			for (PunMenuVO[] prvo : list) {
				// 以目录根节点的index为key，以manyNodeTree为value
				List<PunResourceTreeNode> manyNodeTree = ResourceTreeUtils.getPlainZNodes(prvo);
				resultMap.put(index, manyNodeTree);
				index++;
			}
			JsonFactory factory = new JsonFactory();
			mv.addObject("menuJson", factory.encode2Json(resultMap));
			mv.addObject("sysVO", sysVO);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "异常");
		}
		return mv;
	}

	/**
	 * 开发方系统列表 @Title: punUserBaseInfoList @Description: 分页查询并以列表的形式显示 @author
	 * ljw @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "punSystemList", method = RequestMethod.GET)
	public ModelAndView punSystemList(@ModelAttribute PunSystemVO vo, Model model,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		try {
			if (currentPage <= 0)
				currentPage = 1;
			if (pageSize <= 5)
				pageSize = 10;
			BaseExample example = new BaseExample();
			Criteria criteria = example.createCriteria();
			if (StringUtils.isNotBlank(vo.getSysName())) {
				criteria.andLike("SYS_NAME", vo.getSysName());
			}
			if (StringUtils.isNotBlank(vo.getSysShortName())) {
				criteria.andLike("SYS_SHORT_NAME", vo.getSysShortName());
			}
			PunGroupVO group = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			criteria.andEqualTo("GROUP_ID", group.getGroupId());
			String sortString = "SYS_ID desc";
			PageList<PunSystemVO> vos = sysService.selectPagedByExample(example, currentPage, pageSize, sortString);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("vos", vos);
			model.addAttribute("currentPage", currentPage);
			// 返回查询条件
			model.addAttribute("vo", vo);
			return new ModelAndView("unit/punSystem-list");
		} catch (PermissionException ea) {
			model.addAttribute("result", ea.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("result", "System error.");
		}
		return new ModelAndView("unit/punSystem-list");
	}

	/**
	 * 
	 * @Title: sysChoose @Description: 选择系统，添加资源 @author ljw @param @param
	 *         box @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "sysChoose", method = RequestMethod.POST)
	public ModelAndView sysChoose(Long chooseSys, Model model, RedirectAttributes ra) {
		if (null == chooseSys) {// 未选择待增加资源的系统
			ra.addFlashAttribute("result", "Please choose system");
			return new ModelAndView("redirect:/unit/punSystemList.do?currentPage=0");
		}
		PunResourceVO vo = new PunResourceVO();
		vo.setSysId(chooseSys);
		model.addAttribute("vo", vo);
		return new ModelAndView("/unit/punResource-edit");

	}

}
