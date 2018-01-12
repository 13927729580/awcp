package cn.org.awcp.unit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.unit.core.domain.PunUserBaseInfo;
import cn.org.awcp.unit.service.PunGroupService;
import cn.org.awcp.unit.service.PunPositionService;
import cn.org.awcp.unit.service.PunRoleInfoService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.service.PunUserGroupService;
import cn.org.awcp.unit.service.PunUserRoleService;
import cn.org.awcp.unit.utils.EncryptUtils;
import cn.org.awcp.unit.utils.Security;
import cn.org.awcp.unit.vo.PunAJAXStatusVO;
import cn.org.awcp.unit.vo.PunGroupVO;
import cn.org.awcp.unit.vo.PunPositionVO;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.unit.vo.PunUserRoleVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.service.FileService;

/**
 * 用户管理Controller
 */
@Controller
@RequestMapping("/unit")
public class PunUserBaseInfoController extends BaseController {

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;// 用户Service

	@Autowired
	@Qualifier("punUserRoleServiceImpl")
	PunUserRoleService userRoleService;// 用户角色关联Service

	@Autowired
	@Qualifier("punRoleInfoServiceImpl")
	PunRoleInfoService roleService;// 角色Service

	@Autowired
	@Qualifier("punUserGroupServiceImpl")
	PunUserGroupService userGroupService;// 用户部门关联Service

	@Resource(name = "punPositionServiceImpl")
	private PunPositionService punPositionService;// 职位Service

	@Resource(name = "punGroupServiceImpl")
	private PunGroupService punGroupService;// 部门Service

	@Autowired
	private MetaModelOperateService metaModelOperateServiceImpl;// 元数据操作Service

	/**
	 * 获取当前系统角色
	 * 
	 * @param roleId
	 * @return
	 */
	private List<PunRoleInfoVO> queryRoles() {
		PunSystemVO vo = (PunSystemVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_SYSTEM);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sysId", vo.getSysId());
		List<PunRoleInfoVO> roleVos = roleService.queryResult("eqQueryList", params);
		return roleVos;
	}

	/**
	 * 
	 * @Title: intoPunUserBaseInfo @Description: 进入用户管理 @author
	 *         ljw @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "/intoPunUserBaseInfo", method = RequestMethod.GET)
	public ModelAndView intoPunUserBaseInfo(Model model) {
		// 1、获取当前系统
		// 2、获取当前系统的角色
		try {
			List<PunRoleInfoVO> roleVos = queryRoles();
			List<PunPositionVO> posiVos = punPositionService.findAll();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roleVos", roleVos);
			params.put("posiVos", posiVos);
			return new ModelAndView("/unit/punUserBaseInfo-edit", params);
		} catch (Exception e) {
			logger.info("ERROR", e);
			return new ModelAndView("redirect:/logout.do");
		}
	}

	/**
	 * 保存
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/punUserBaseInfoSave", method = RequestMethod.POST)
	public ModelAndView punUserBaseInfoSave(@ModelAttribute("vo") PunUserBaseInfoVO vo, Model model,
			RedirectAttributes ra, HttpServletRequest request) {
		ModelAndView mView = new ModelAndView("/unit/punUserBaseInfo-edit");
		try {
			PunGroupVO groupVO = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			vo.setGroupId(groupVO.getGroupId());// 设置所属组织ID
			String msg = validate(vo);
			if (msg == null) {
				if (null == vo.getUserId() || "yes".equals(vo.getUpdatePassword())) {// 新增用户或者修改密码
					vo.setUserPwd(EncryptUtils.encrypt(vo.getUserPwd()));// 密码加密
				} else {
					PunUserBaseInfoVO existVO = userService.findById(vo.getUserId());
					vo.setUserPwd(existVO.getUserPwd());
				}
				boolean isNew = vo.getUserId() == null ? true : false;
				String userHeadImg = StringUtils.defaultString(vo.getUserHeadImg(),
						ControllerHelper.getBasePath() + "template/AdminLTE/images/user2-160x160.jpg");
				vo.setUserHeadImg(userHeadImg);
				Long userId = userService.addOrUpdateUser(vo);// 新增用户
				// 新增用户时，增加部门和岗位
				if (vo.getPositionGroupId() != null && vo.getPositionId() != null) {
					String insertSql = "insert into p_un_user_group(USER_ID,GROUP_ID,POSITION_ID) values(?,?,?)";
					if (isNew) {
						metaModelOperateServiceImpl.updateBySql(insertSql, userId, vo.getPositionGroupId(),
								vo.getPositionId());
					} else {
						String sql = "delete FROM p_un_user_group WHERE user_id=?";
						metaModelOperateServiceImpl.updateBySql(sql, userId);
						metaModelOperateServiceImpl.updateBySql(insertSql, userId, vo.getPositionGroupId(),
								vo.getPositionId());
					}
				}

				ra.addFlashAttribute("result", "用户名为" + vo.getUserIdCardNumber() + "的用户新增/更新成功！");
				return new ModelAndView("redirect:/unit/punUserBaseInfoList.do");
			} else {
				model.addAttribute("result", "校验失败" + msg + "。");
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "系统异常");
		}
		List<PunRoleInfoVO> roleVos = queryRoles();
		mView.addObject("roleVos", roleVos);
		return mView;
	}

	/**
	 * 根据ID查找
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "punUserBaseInfoGet", method = RequestMethod.POST)
	public ModelAndView punUserBaseInfoGet(Long[] boxs) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punUserBaseInfo-edit");
		try {
			PunUserBaseInfoVO vo = userService.findById(boxs[0]);
			List<PunRoleInfoVO> roleVos = queryRoles();// 获取当前系统角色
			// 根据userId获取用户角色
			List<String> selectedRole = roleService.queryByUser(vo.getUserId());
			String sql = "SELECT group_id groupId,position_id positionId FROM p_un_user_group WHERE user_id=?";
			List<Map<String, Object>> data = metaModelOperateServiceImpl.search(sql, boxs[0]);
			Object groupId = "";
			Object positionId = "";
			if (!data.isEmpty()) {
				Map<String, Object> map = data.get(0);
				groupId = map.get("groupId");
				positionId = map.get("positionId");
			}
			List<PunPositionVO> posiVos = punPositionService.findAll();
			vo.setUserPwd(Security.decryptPassword(vo.getUserPwd()));
			mv.addObject("vo", vo);
			mv.addObject("selectedRole", StringUtils.join(selectedRole.toArray(new String[] {})));
			mv.addObject("selectedGroup", groupId);
			mv.addObject("selectedPosition", positionId);
			mv.addObject("roleVos", roleVos);
			mv.addObject("posiVos", posiVos);

		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "异常");
		}
		return mv;
	}

	/**
	 * 用户列表
	 * 
	 * @param vo
	 *            查询参数
	 * @param model
	 * @param currentPage
	 *            当前页数
	 * @param pageSize
	 *            每页记录条数
	 * @return
	 */
	@RequestMapping(value = "punUserBaseInfoList")
	public ModelAndView punUserBaseInfoList(@ModelAttribute PunUserBaseInfoVO vo, Model model,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		try {
			String sortString = "p_un_user_base_info.number";
			BaseExample example = new BaseExample();
			Criteria criteria = example.createCriteria();
			String query_deptName = vo.getDeptName();// 部门名称
			String query_name = vo.getName();// 姓名
			String query_mobile = vo.getMobile();// 手机号
			String query_userTitle = vo.getUserTitle();// 职称
			if (StringUtils.isNotBlank(query_deptName)) {
				criteria.andLike("p_un_group.GROUP_CH_NAME", "%" + query_deptName + "%");
			}
			if (StringUtils.isNotBlank(query_name)) {
				criteria.andLike("p_un_user_base_info.NAME", "%" + query_name + "%");
			}
			if (StringUtils.isNotBlank(query_mobile)) {
				criteria.andLike("p_un_user_base_info.MOBILE", "%" + query_mobile + "%");
			}
			if (StringUtils.isNotBlank(query_userTitle)) {
				criteria.andLike("d.NAME", "%" + query_userTitle + "%");
			}
			PageList<PunUserBaseInfoVO> vos = userService.selectByExample_UserList(example, currentPage, pageSize,
					sortString);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("vos", vos);
			model.addAttribute("vo", vo);
			return new ModelAndView("/unit/punUserBaseInfo-list");
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "系统异常");
			return new ModelAndView("/unit/punUserBaseInfo-list");
		}
	}

	/**
	 * 
	 * @Title: userNotInGroup @Description: 未分入特定组的用户 @author ljw @param @param
	 *         groupId 组ID @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "userNotInGroupList", method = RequestMethod.GET)
	public ModelAndView queryUserNotInGroup(@ModelAttribute PunUserBaseInfoVO vo, Model model, int currentPage,
			Long mGroupId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();// 参数
			if (null != vo.getUserId()) {
				map.put("userId", vo.getUserId());
			}
			if (StringUtils.isNotEmpty(vo.getUserName())) {
				map.put("userName", vo.getUserName());
			}
			if (StringUtils.isNotEmpty(vo.getName())) {
				map.put("name", vo.getName());
			}
			if (StringUtils.isNotEmpty(vo.getUserIdCardNumber())) {
				map.put("userIdCardNumber", vo.getUserIdCardNumber());
			}
			if (null != mGroupId) {
				map.put("groupId", mGroupId);
			}
			String queryStr = "queryUserNotInGroup";
			PageList<PunUserBaseInfoVO> vos = userListCommon(queryStr, map, currentPage, model);
			if (null != mGroupId) {// 用于用户与组关联
				model.addAttribute("groupId", mGroupId);
			}
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("vos", vos);
			return new ModelAndView("/unit/punUserGroup-list");
		} catch (Exception e) {
			logger.info("ERROR", e);
			model.addAttribute("result", "系统异常");
			return new ModelAndView("/unit/punUserGroup-list");
		}
	}

	/**
	 * 
	 * @Title: userListCommon @Description: 查询通用方法 @author
	 *         ljw @param @return @param @throws Exception @return
	 *         ModelAndView @throws
	 */
	private PageList<PunUserBaseInfoVO> userListCommon(String queryStr, Map<String, Object> params, int currentPage,
			Model model) throws Exception {

		// 可优化，直接增加
		int pageSize = 10;

		if (currentPage == 0) {
			currentPage = 1;
		}
		String quiry_name = (String) params.get("name");
		String quiry_username = (String) params.get("username");
		String quiry_userIdCardNumber = (String) params.get("userIdCardNumber");
		String sortString = "USER_ID.desc";
		BaseExample example = new BaseExample();

		if (StringUtils.isNotBlank(quiry_name)) {
			example.createCriteria().andLike("name", "%" + quiry_name + "%");
		}
		if (StringUtils.isNotBlank(quiry_name)) {
			example.createCriteria().andLike("username", "%" + quiry_username + "%");
		}
		if (StringUtils.isNotBlank(quiry_name)) {
			example.createCriteria().andLike("userIdCardNumber", "%" + quiry_userIdCardNumber + "%");
		}
		PageList<PunUserBaseInfoVO> vos = userService.selectPagedByExample(example, currentPage, pageSize, sortString);
		return vos;
	}

	/**
	 * 
	 * @Title: punUserBaseInfoDelete @Description: 根据ID删除 @author ljw @param @param
	 *         id @param @return @return ModelAndView @throws
	 */
	@RequestMapping(value = "punUserBaseInfoDelete", method = RequestMethod.POST)
	public ModelAndView punUserBaseInfoDelete(Long[] boxs, int currentPage, RedirectAttributes ra) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:punUserBaseInfoList.do?currentPage=" + currentPage);
		try {

			for (Long box : boxs) {

				userService.delete(box);
			}
			ra.addFlashAttribute("result", "成功删除" + boxs.length + "个用户");
		} catch (Exception e) {
			logger.info("ERROR", e);
			ra.addFlashAttribute("result", "操作失败：系统异常！");
		}
		return mv;
	}

	/**
	 * 物理删除用户 删除用户关联的组织和角色
	 * 
	 * @param _selects
	 * @param currentPage
	 * @param ra
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "physicalUserDelete", method = RequestMethod.POST)
	public Map<String, Object> physicalUserDelete(Long[] _selects, int currentPage, RedirectAttributes ra) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (Long box : _selects) {
				PunUserBaseInfoVO vo = userService.findById(box);
				PunUserBaseInfo pbi = BeanUtils.getNewInstance(vo, PunUserBaseInfo.class);
				userService.removeRelations(pbi);
			}
			map.put("success", 1);
		} catch (Exception e) {
			logger.info("ERROR", e);
			map.put("success", 0);
		}
		return map;
	}

	/**
	 * 进入角色关联用户
	 * 
	 * @param boxs
	 * @return
	 */
	@RequestMapping(value = "intoRoleRelateuser")
	public ModelAndView intoRoleRelateuser(Long[] boxs) {
		if (null == boxs || boxs.length < 1) {
			return null;
		}
		ModelAndView mv = new ModelAndView("redirect:roleRelateUserQuery.do?roleId=" + boxs[0]);
		return mv;
	}

	/**
	 * 查询与角色已关联的用户
	 * 
	 * @param vo
	 * @param roleId
	 *            角色ID
	 * @param currentPage
	 *            当前页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "roleRelateUserQuery")
	public ModelAndView roleRelateUserQuery(PunUserBaseInfoVO vo,
			@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView("unit/user-relate-role");
		try {
			String sortString = "USER_ID.desc";

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roleId", roleId);
			if (null != vo) {
				if (StringUtils.isNotEmpty(vo.getUserIdCardNumber())) {// 身份证号
					params.put("userIdCardNumber", "%" + vo.getUserIdCardNumber() + "%");
				}
				if (StringUtils.isNotEmpty(vo.getName())) {// 姓名
					params.put("name", "%" + vo.getName() + "%");
				}
				if (vo.getGroupId() == null) {
					PunUserBaseInfoVO currentUser = (PunUserBaseInfoVO) SessionUtils
							.getObjectFromSession(SessionContants.CURRENT_USER);
					vo.setGroupId(currentUser.getGroupId());
					params.put("groupId", vo.getGroupId());
				} else {
					params.put("groupId", vo.getGroupId());
				}
			}
			List<PunUserBaseInfoVO> userVOs = userService.queryPagedResult("queryUserRelateRole", params, currentPage,
					pageSize, sortString);

			mv.addObject("roleId", roleId);
			mv.addObject("vos", userVOs);
			mv.addObject("currentPage", currentPage);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "系统异常");
		}
		return mv;
	}

	/**
	 * 查询与角色未关联的用户
	 * 
	 * @param vo
	 * @param roleId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "roleNotRelateUserQuery")
	public ModelAndView roleNotRelateUserQuery(PunUserBaseInfoVO vo, Long roleId,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		ModelAndView mv = new ModelAndView("unit/user-not-relate-role");
		try {
			String sortString = "USER_ID.desc";

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roleId", roleId);
			if (null != vo) {
				if (StringUtils.isNotEmpty(vo.getUserIdCardNumber())) {// 身份证号
					params.put("userIdCardNumber", "%" + vo.getUserIdCardNumber() + "%");
				}
				if (StringUtils.isNotEmpty(vo.getName())) {// 姓名
					params.put("name", "%" + vo.getName() + "%");
				}
				if (vo.getGroupId() == null) {
					PunUserBaseInfoVO currentUser = (PunUserBaseInfoVO) SessionUtils
							.getObjectFromSession(SessionContants.CURRENT_USER);
					vo.setGroupId(currentUser.getGroupId());
					params.put("groupId", vo.getGroupId());
				} else {
					params.put("groupId", vo.getGroupId());
				}
			}
			List<PunUserBaseInfoVO> userVOs = userService.queryPagedResult("queryRoleNotRealteUser", params,
					currentPage, pageSize, sortString);

			mv.addObject("roleId", roleId);
			mv.addObject("vos", userVOs);
			mv.addObject("currentPage", currentPage);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "系统异常");
		}
		return mv;
	}

	/**
	 * 用户关联角色
	 * 
	 * @param roleId
	 *            角色ID
	 * @param boxs
	 *            用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "userRelateRole", method = RequestMethod.POST)
	public PunAJAXStatusVO userRelateRole(Long roleId, Long[] boxs) {

		PunAJAXStatusVO vo = new PunAJAXStatusVO();
		try {
			for (Long box : boxs) {
				PunUserRoleVO userRole = new PunUserRoleVO();
				userRole.setRoleId(roleId);
				userRole.setUserId(box);
				userRoleService.addOrUpdate(userRole);
			}
			vo.setMessage("完成" + boxs.length + "个用户的关联操作");
		} catch (Exception e) {
			logger.info("ERROR", e);
			vo.setMessage("关联操作失败");
		}
		return vo;
	}

	/**
	 * 取消角色与用户关联关系
	 * 
	 * @param roleId角色Id
	 * @param boxs用户Id
	 * @return
	 */
	@RequestMapping(value = "cancelUserRoleRelation", method = RequestMethod.POST)
	public ModelAndView cancelUserRoleRelation(Long roleId, Long[] boxs) {
		ModelAndView mv = new ModelAndView("redirect:roleRelateUserQuery.do?&roleId=" + roleId);
		try {
			userRoleService.deleteByRoleIdAndUserId(roleId, boxs);
			mv.addObject("result", "删除成功");
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "删除失败");
		}
		return mv;
	}

	/**
	 * 校验必填项
	 * 
	 * @param vo
	 * @return boolean
	 */
	private String validate(PunUserBaseInfoVO vo) {
		List<PunUserBaseInfoVO> users = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", vo.getGroupId());// 所属组织ID
		params.put("userIdCardNumber", vo.getUserIdCardNumber());// 身份证号
		if (null == vo.getUserId()) {// 若新增
			users = userService.queryResult("eqQueryList", params);
		} else {// 若修改
			PunUserBaseInfoVO userVo = userService.findById(vo.getUserId());
			// 判断新录入的身份证号与原身份证号不一致，校验
			if (!userVo.getUserIdCardNumber().equals(vo.getUserIdCardNumber())) {
				users = userService.queryResult("eqQueryList", params);
			}
		}
		if (null != users && users.size() > 0) {
			return "该用户名已注册过";
		}
		return null;
	}

	/**
	 * 修改密码
	 * 
	 * @param oldPwd
	 *            旧密码
	 * @param newPwd
	 *            新密码
	 * @param newPwd2
	 *            重复输入密码
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	public ReturnResult updatePwd(String oldPwd, String newPwd, HttpServletResponse response) {
		ReturnResult result = ReturnResult.get();
		try {
			PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_USER);
			if (!oldPwd.equals(EncryptUtils.decript(user.getUserPwd()))) {
				result.setStatus(StatusCode.PARAMETER_ERROR.setMessage("原密码输入错误"));
			} else {
				user.setUserPwd(EncryptUtils.encrypt(newPwd));
				userService.updateUser(user);
				result.setStatus(StatusCode.SUCCESS.setMessage("修改成功"));
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
			result.setStatus(StatusCode.FAIL.setMessage("修改失败，请重试或联系管理员"));
		}
		return result;
	}

	/**
	 * 修改个人信息
	 * 
	 * @param vo
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "updateSelfInfo")
	public ReturnResult updateSelfInfo(PunUserBaseInfoVO vo, HttpServletResponse response) {
		ReturnResult result = ReturnResult.get();
		try {
			PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_USER);
			vo.setUserPwd(user.getUserPwd());
			vo.setUserIdCardNumber(vo.getUserName());
			userService.updateUser(vo);
			SessionUtils.addObjectToSession(SessionContants.CURRENT_USER, vo);
			result.setStatus(StatusCode.SUCCESS.setMessage("更新成功"));
		} catch (Exception e) {
			logger.info("ERROR", e);
			result.setStatus(StatusCode.FAIL.setMessage("更新失败，请重试或联系管理员"));
		}
		return result;
	}

	/**
	 * 获取个人信息
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "getSelfInfo")
	public ModelAndView getSelfInfo() {
		ModelAndView mv = new ModelAndView();
		try {
			mv.setViewName("/unit/punUserBaseInfo-self-edit");
			PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_USER);
			mv.addObject("vo", user);
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "系统异常");
		}
		return mv;
	}

	/**
	 * 用户选择框回显用户名字
	 */
	@ResponseBody
	@RequestMapping(value = "getNamesByUserIds", method = RequestMethod.POST)
	public String getNamesByUserIds(String userIds) {
		if (StringUtils.isBlank(userIds)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String[] userIdArr = userIds.split(",");
		if (userIdArr != null && userIdArr.length > 0) {
			for (int i = 0; i < userIdArr.length; i++) {
				if ("".equals(userIdArr[i])) {
					continue;
				} else {
					PunUserBaseInfoVO user = userService.findById(Long.parseLong(userIdArr[i]));
					if (user != null) {
						sb.append(user.getName() + ",");
					}
				}
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}

	@ResponseBody
	@RequestMapping("batchImportSignature")
	public ReturnResult batchImportSignature() {
		ReturnResult result = ReturnResult.get();
		String path = ControllerHelper.getUploadPath("/upload/signature");
		File folder = new File(path);
		folder.listFiles(new FilenameFilter() {
			// 只返回图片格式的文件
			@Override
			public boolean accept(File dir, String fileName) {
				File file = new File(dir + File.separator + fileName);
				fileName = fileName.toLowerCase();
				if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif")) {
					// 根据文件名称查找对应的用户
					String name = fileName.substring(0, fileName.lastIndexOf("."));
					Map<String, Object> params = new HashMap<>();
					params.put("name", name);
					List<PunUserBaseInfoVO> pvis = userService.queryResult("eqQueryList", params);
					if (!pvis.isEmpty() && pvis.size() == 1) {
						PunUserBaseInfoVO pvi = pvis.get(0);
						saveUserSignature(pvi, file);
					}
					return true;
				}
				return false;
			}
		});
		result.setStatus(StatusCode.SUCCESS);
		return result;
	}

	@Resource(name = "IFileService")
	private FileService fileService;

	/**
	 * 将用户的签名上传到mongoDB并保存到用户表中的签名字段
	 * 
	 * @param vo
	 * @param file
	 * @param userId
	 */
	private void saveUserSignature(PunUserBaseInfoVO vo, File file) {
		try {
			Serializable uuid = fileService.save(new FileInputStream(file),
					Files.probeContentType(Paths.get(file.getAbsolutePath())), file.getName());
			// 更新数据库
			vo.setSignatureImg(String.valueOf(uuid));
			userService.updateUser(vo);
		} catch (IOException e) {
			logger.info("ERROR", e);
		}
	}

}
