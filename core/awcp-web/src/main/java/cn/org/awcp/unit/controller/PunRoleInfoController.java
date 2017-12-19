package cn.org.awcp.unit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.ResourceTypeEnum;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.unit.service.PunMenuService;
import cn.org.awcp.unit.service.PunResourceService;
import cn.org.awcp.unit.service.PunRoleInfoService;
import cn.org.awcp.unit.service.PunSystemService;
import cn.org.awcp.unit.utils.BackEndUserUtils;
import cn.org.awcp.unit.utils.JsonFactory;
import cn.org.awcp.unit.utils.PermissionException;
import cn.org.awcp.unit.utils.ResourceTreeUtils;
import cn.org.awcp.unit.vo.PunMenuVO;
import cn.org.awcp.unit.vo.PunResourceTreeNode;
import cn.org.awcp.unit.vo.PunResourceVO;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunSystemVO;

@Controller
@RequestMapping("/unit")
public class PunRoleInfoController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	@Qualifier("punRoleInfoServiceImpl")
	PunRoleInfoService roleService;

	@Autowired
	@Qualifier("punSystemServiceImpl")
	private PunSystemService punSystemService;

	@Autowired
	@Qualifier("punMenuServiceImpl")
	PunMenuService menuService;// 菜单service

	@Resource(name = "punResourceServiceImpl")
	private PunResourceService resoService;// 资源Service

	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	@Autowired
	private SzcloudJdbcTemplate jdbcTemplate;

	private StringBuffer valMessage = null;// 校验信息

	@RequestMapping("/listRolesInSys")
	public ModelAndView listRolesInSys(@RequestParam(value = "boxs") Long sysId, PunRoleInfoVO vo,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView();
		if (currentPage <= 0) {
			currentPage = 1;
		}
		if (pageSize <= 5) {
			pageSize = 10;
		}

		BaseExample example = new BaseExample();
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("SYS_ID", sysId);
		if (StringUtils.isNotBlank(vo.getRoleName())) {
			criteria.andLike("ROLE_NAME", "%" + vo.getRoleName() + "%");
		}
		PageList<PunRoleInfoVO> vos = roleService.selectPagedByExample(example, currentPage, pageSize, "ROLE_ID ASC");

		PunSystemVO sys = punSystemService.findById(sysId);
		mv.addObject("sys", sys);
		mv.addObject("sysId", sysId);
		mv.addObject("currentPage", currentPage);
		mv.addObject("pageSize", pageSize);
		mv.addObject("vo", vo);
		mv.addObject("vos", vos);
		mv.setViewName("unit/punRoleInfo-sys-list");
		return mv;
	}

	@ResponseBody
	@RequestMapping("/listRolesInSysByAjax")
	public List<PunRoleInfoVO> listRolesInSysByAjax(@RequestParam(value = "boxs") Long sysId, PunRoleInfoVO vo,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("SYS_ID", sysId);

		List<PunRoleInfoVO> vos = roleService.queryResult("eqQueryList", params);
		mv.addObject("PunRoleInfoVO", vos);
		return vos;
	}

	@RequestMapping("editRoleInSys")
	public ModelAndView editRoleInSys(@RequestParam(value = "sysId") Long sysId,
			@RequestParam(value = "boxs", required = false) Long roleId) {
		ModelAndView mv = new ModelAndView();
		PunRoleInfoVO vo = new PunRoleInfoVO();
		if (roleId != null) {
			vo = roleService.findById(roleId);
		} else {
			vo.setSysId(sysId);
		}
		PunSystemVO sys = punSystemService.findById(sysId);

		mv.addObject("sys", sys);
		mv.addObject("vo", vo);
		mv.addObject("sysId", sysId);
		mv.setViewName("unit/punRoleInfo-sys-edit");
		return mv;
	}

	@RequestMapping("saveRoleInSys")
	public ModelAndView saveRoleInSys(PunRoleInfoVO vo) {
		ModelAndView mv = new ModelAndView();
		if (validate(vo)) {
			roleService.addOrUpdateRole(vo);
			mv.addObject("boxs", vo.getSysId());
			mv.setViewName("redirect:/unit/listRolesInSys.do");
		} else {
			mv.addObject("result", "Failed：" + valMessage.toString());
			mv.addObject("vo", vo);
			mv.setViewName("unit/punRoleInfo-sys-edit");
		}
		return mv;
	}

	@RequestMapping("delRoleInSys")
	public ModelAndView delRoleInSys(Long[] boxs, Long sysId) {
		if (boxs != null) {
			for (int i = 0; i < boxs.length; i++) {
				roleService.delete(boxs[i]);
			}
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("boxs", sysId);
		mv.setViewName("redirect:/unit/listRolesInSys.do");
		return mv;
	}

	/**
	 * 
	 * @Title: intoPunRoleInfo @Description: 进入编辑页面 @author ljw @param @param
	 *         session @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "/intoPunRoleInfo", method = RequestMethod.GET)
	public ModelAndView intoPunRoleInfo(Model model) {
		ModelAndView mv = new ModelAndView("/unit/punRoleInfo-sys-edit");
		try {
			// PunSystemVO sysVO = (PunSystemVO)
			// SessionUtils.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
			PunSystemVO sysVO = (PunSystemVO) SessionUtils.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if (null != sysVO) {
				PunRoleInfoVO vo = new PunRoleInfoVO();
				vo.setSysId(sysVO.getSysId());
				model.addAttribute("sysId", sysVO.getSysId());
				model.addAttribute("vo", vo);
			}
			return mv;
		} catch (Exception e) {
			logger.info("ERROR", e);
			return null;
		}
	}

	/**
	 * 保存
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/punRoleInfoSave", method = RequestMethod.POST)
	public ModelAndView punRoleInfoSave(@ModelAttribute("vo") PunRoleInfoVO vo, Model model, RedirectAttributes ra) {
		try {
			if (validate(vo)) {
				roleService.addOrUpdateRole(vo);
				ra.addFlashAttribute("result", "Add/Update successfully.");
				return new ModelAndView("redirect:/unit/punRoleInfoList.do?currentPage=0");
			} else {
				model.addAttribute("result", "Failed" + valMessage.toString() + "。");
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "System error.");
			ra.addFlashAttribute("result", "hello");
		}
		return new ModelAndView("/unit/punRoleInfo-edit");
	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punRoleInfoGet", method = RequestMethod.POST)
	public ModelAndView punRoleInfoGet(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punRoleInfo-edit");
		try {
			PunRoleInfoVO vo = roleService.findById(boxs[0]);
			mv.addObject("vo", vo);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	@RequestMapping(value = "intoPunRoleInfoList", method = RequestMethod.GET)
	public ModelAndView intoPunRoleInfoList() {
		ModelAndView mv = new ModelAndView("redirect:/unit/punRoleInfoList.do?currentPage=0");
		return mv;
	}

	/**
	 * 
	 * @Title: punRoleInfoList @Description: 分页查询并以列表的形式显示 @author
	 *         ljw @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "punRoleInfoList")
	public ModelAndView punRoleInfoList(PunRoleInfoVO vo, Model model, int currentPage) {
		ModelAndView mv = new ModelAndView("/unit/punRoleInfo-list");
		try {

			if (currentPage == 0) {
				currentPage = 1;
			}
			int pageSize = 10;
			BaseExample example = new BaseExample();
			Criteria critera = example.createCriteria();
			String query_rolename = vo.getRoleName();
			/**
			 * ljw 2014-12-9 增加查询限制条件
			 */
			Long sysId = vo.getSysId();// 系统ID
			if (null != sysId) {
				critera.andEqualTo("SYS_ID", sysId);
			}
			if (StringUtils.isNotBlank(query_rolename)) {
				critera.andLike("ROLE_NAME", "%" + query_rolename + "%");
			}
			String sortString = "ROLE_ID desc";
			PageList<PunRoleInfoVO> vos = roleService.selectPagedByExample(example, currentPage, pageSize, sortString);
			mv.addObject("vos", vos);
			mv.addObject("currentPage", currentPage);
			// 返回查询条件
			mv.addObject("vo", vo);
		} catch (PermissionException ea) {
			mv.addObject("result", "System error.");
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;

	}

	/**
	 * 角色管理
	 * 
	 * @param boxs
	 * @return
	 */
	@RequestMapping(value = "manageRole")
	public ModelAndView manageRole(@RequestParam(value = "roleName", required = false) String roleName,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView("/unit/punRoleInfo-choose-list");
		try {
			if (currentPage < 1)
				currentPage = 1;
			if (pageSize < 10) {
				pageSize = 10;
			}
			BaseExample example = new BaseExample();
			Criteria critera = example.createCriteria();
			if (StringUtils.isNotEmpty(roleName)) {
				critera.andLike("ROLE_NAME", "%" + roleName + "%");
			}
			String sortString = "ROLE_ID desc";
			PageList<PunRoleInfoVO> vos = roleService.selectPagedByExample(example, currentPage, pageSize, sortString);
			PunRoleInfoVO vo = new PunRoleInfoVO();
			vo.setRoleName(roleName);
			mv.addObject("vo", vo);
			mv.addObject("vos", vos);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
			return null;
		}
		return mv;
	}

	/**
	 * 
	 * @Title: punRoleInfoDelete @Description: 根据ID删除 @author ljw @param @param
	 *         id @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "punRoleInfoDelete", method = RequestMethod.POST)
	public ModelAndView punRoleInfoDelete(Long[] boxs, int currentPage) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/unit/punRoleInfoList.do?currentPage=" + currentPage);
		try {
			if (!BackEndUserUtils.isBackEndRole()) {// 判断是否为可操作改功能的角色
				throw new PermissionException("You has not permission to operate.");
			}
			for (Long id : boxs) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("roleId", id);
				roleService.delete(id);
			}
			mv.addObject("result", "Delete successfully.");
		} catch (PermissionException ea) {
			mv.addObject("result", ea.getMessage());
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 根据SystemId及roleId赋予资源权限
	 * 
	 * @author wsh
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "punRoleMenuAccessEdit")
	public ModelAndView punRoleMenuAccessEdit(Long boxs, Long sysId, String moduleId) {
		PunSystemVO sysVO = punSystemService.findById(sysId);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/accessAuthor");
		try {
			List<PunMenuVO> resoVOs = menuService.getPunMenuBySys(sysVO);
			List<PunMenuVO> accVOS = new ArrayList<PunMenuVO>();
			if (boxs != null) {
				PunRoleInfoVO vo = new PunRoleInfoVO();
				vo.setRoleId(boxs);
				vo.setSysId(sysId);
				mv.addObject("roleName", jdbcTemplate
						.queryForObject("select ROLE_NAME from p_un_role_info where ROLE_ID=" + boxs, String.class));
				mv.addObject("vo", vo);
				accVOS = menuService.getByRoleAndSys(vo, sysVO);
			}

			this.setAccessRight(resoVOs, accVOS);
			ResourceTreeUtils rtu = new ResourceTreeUtils();
			List<PunMenuVO[]> list = rtu.generateTreeView(resoVOs);
			Map<Integer, List<PunResourceTreeNode>> resultMap = new HashMap<Integer, List<PunResourceTreeNode>>();
			Integer index = 1;
			for (PunMenuVO[] prvo : list) {
				// 以目录根节点的index为key，以manyNodeTree为value
				List<PunResourceTreeNode> manyNodeTree = ResourceTreeUtils.getPlainDevAccessZNodes(prvo, boxs);
				resultMap.put(index, manyNodeTree);
				index++;
			}
			JsonFactory factory = new JsonFactory();
			mv.addObject("menuJson", factory.encode2Json(resultMap));

			String sql = "";
			if (StringUtils.isNoneBlank(moduleId)) {
				sql = "select id from p_fm_dynamicpage where modular=" + moduleId;
			} else {
				sql = "select id from p_fm_dynamicpage where modular=(select ID from p_fm_modular limit 1)";
			}
			List<String> dynamicPageIds = jdbcTemplate.queryForList(sql, String.class);
			sql = "select ID,modularName from p_fm_modular order by ID ";
			mv.addObject("modules", jdbcTemplate.queryForList(sql));
			mv.addObject("moduleId", moduleId);

			// 1、查找当前系统的所有按钮，按动态表单名分类
			// 2、查找资源，格式为Map<relateResoId,Resource>
			// 3、查找已授权的资源
			Map<String, List<StoreVO>> buttonMap = formdesignerServiceImpl.getSystemActs(dynamicPageIds);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sysId", sysId);
			params.put("resouType", ResourceTypeEnum.RESO_BUTTON.getkey());

			Map<String, PunResourceVO> buttonResos = resoService.queryButtonMap("eqQueryList", params);
			Map<String, List<PunResourceVO>> resultsMap = new HashMap<String, List<PunResourceVO>>();
			Set<String> keys = buttonMap.keySet();
			// 将按钮按照动态表单名称划分
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String s = (String) it.next();
				List<StoreVO> storeVOs = buttonMap.get(s);
				for (StoreVO storeVO : storeVOs) {
					// 如果resouce中有按钮ID
					if (buttonResos.containsKey(storeVO.getId())) {// 如果资源表中有该按钮
						if (resultsMap.containsKey(s)) {
							List<PunResourceVO> newVos = resultsMap.get(s);
							newVos.add(buttonResos.get(storeVO.getId()));
						} else {
							List<PunResourceVO> temVOs = new ArrayList<PunResourceVO>();
							temVOs.add(buttonResos.get(storeVO.getId()));
							resultsMap.put(s, temVOs);
						}
					}
				}
			}

			Map<String, Object> authorParams = new HashMap<String, Object>();
			authorParams.put("sysId", sysId);
			authorParams.put("roleId", boxs);
			// 排除菜单地址和动态表单
			authorParams.put("resouType", ResourceTypeEnum.RESO_BUTTON.getkey());
			// 已授权的资源
			List<String> resoAuthor = resoService.queryResoIds("queryResoAuthor", authorParams);

			mv.addObject("resultsMap", resultsMap);
			mv.addObject("resoAuthor", resoAuthor);

		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	private void setAccessRight(List<PunMenuVO> mainList, List<PunMenuVO> accessList) {
		for (int i = 0; i < accessList.size(); i++) {
			PunMenuVO temp = accessList.get(i);
			for (int j = 0; j < mainList.size(); j++) {
				if (temp.getMenuId().longValue() == mainList.get(j).getMenuId().longValue()) {
					mainList.get(j).setChecked(true);
					break;
				}
			}
		}
	}

	/**
	 * 校验必填项
	 * 
	 * @param vo
	 * @return boolean
	 */
	private boolean validate(PunRoleInfoVO vo) {
		if (null == vo) {
			return false;
		}
		valMessage = new StringBuffer();
		if (null == vo.getSysId()) {
			valMessage.append(",System Id required");
			return false;
		}
		if (null == vo.getRoleName()) {
			valMessage.append(",Role Name required");
			return false;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sysId", vo.getSysId());
		List<PunRoleInfoVO> rVOs = null;
		if (null == vo.getRoleId()) {// 新增时
			params.put("roleName", vo.getRoleName());
			rVOs = roleService.queryResult("eqQueryList", params);
		} else {// 修改时
			PunRoleInfoVO roleInfoVO = roleService.findById(vo.getRoleId());
			if (StringUtils.isNotEmpty(vo.getRoleName())// 角色名修改
					&& !vo.getRoleName().equals(roleInfoVO.getRoleName())) {
				params.put("roleName", vo.getRoleName());
				rVOs = roleService.queryResult("eqQueryList", params);
			}
		}
		if (null != rVOs && rVOs.size() > 0) {
			valMessage.append(",Role exsit");
			return false;
		}
		return true;
	}

}
