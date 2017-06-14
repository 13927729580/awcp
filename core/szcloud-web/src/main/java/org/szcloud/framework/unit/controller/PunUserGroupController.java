package org.szcloud.framework.unit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.formdesigner.utils.DocumentUtils;
import org.szcloud.framework.unit.service.PunGroupService;
import org.szcloud.framework.unit.service.PunPositionService;
import org.szcloud.framework.unit.service.PunRoleInfoService;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.service.PunUserGroupService;
import org.szcloud.framework.unit.vo.PunGroupVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.unit.vo.PunUserGroupVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/punUserGroupController")
public class PunUserGroupController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	public static final int pagesize = 8;

	@Resource(name = "punUserGroupServiceImpl")
	private PunUserGroupService punUserGroupService;

	@Resource(name = "punPositionServiceImpl")
	private PunPositionService punPositionService;

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;

	@Autowired
	@Qualifier("punRoleInfoServiceImpl")
	PunRoleInfoService roleService;

	@Autowired
	@Qualifier("punGroupServiceImpl")
	PunGroupService groupService;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/manager/punUserGroups", method = RequestMethod.GET)
	public ModelAndView listPunUserGroup(String pagenum, PunUserGroupVO punUserGroup) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("punUserGroups");
		mv.addObject("sidebar", "punUserGroups");
		int num = 1;
		if (null != pagenum) {
			num = Integer.parseInt(pagenum);
		}
		List<PunUserGroupVO> list = punUserGroupService.findAll();
		mv.addObject("punUserGrouptList", list);
		mv.addObject("length", list.size());
		mv.addObject("pagenum", num);
		mv.addObject("punUserGroup", punUserGroup);
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(Long[] positionId, Long[] boxs, Long groupId, Long rootGroupId, Long isManager) {
		ModelAndView mv = new ModelAndView(
				"redirect:/punUserGroupController/getUserList.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId);
		if (boxs != null && boxs.length > 0 && positionId != null && positionId.length > 0) {
			for (int i = 0; i < boxs.length; i++) {
				for (int j = 0; j < positionId.length; j++) {
					PunUserGroupVO p = new PunUserGroupVO();
					p.setGroupId(groupId);
					p.setUserId(boxs[i]);
					p.setPositionId(positionId[j]);
					if (isManager != null) {
						p.setIsManager(true);
					}
					punUserGroupService.save(p);
				}
			}
		}
		return mv;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public String remove(@ModelAttribute PunUserGroupVO vo, Model model) {
		punUserGroupService.remove(vo);
		return "/manager/punUserGroups";
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public String findAll(Model model, HttpServletResponse response) throws Exception {
		try {
			List<PunUserGroupVO> list = this.punUserGroupService.findAll();
			model.addAttribute("class", list);
			return "/manager/punUserGroups";
		} catch (Exception e) {
			logger.info("ERROR", e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ModelAndView get(Long groupId, Long rootGroupId, String userIdCardNumber, String name,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punUserGroupAdd");
		Map queryParam = new HashMap();
		queryParam.put("groupId", rootGroupId);
		BaseExample example = new BaseExample();
		List list = punPositionService.selectByExample(example);
		// PageList<PunPositionVO> list =
		// punPositionService.selectPagedByExample("queryList",queryParam, 1,
		// 99, null);
		/**
		 * ljw 2014-12-6 修改为查询未与组织关联用户
		 */
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("rootGroupId", rootGroupId);// 根组织ID
		userMap.put("groupId", groupId);// 组织ID
		if (StringUtils.isNotEmpty(userIdCardNumber)) {
			userMap.put("userIdCardNumber", "%" + userIdCardNumber + "%");// 身份证号码
		}
		if (StringUtils.isNotEmpty(name)) {
			userMap.put("name", "%" + name + "%");// 姓名
		}
		PageList<PunUserBaseInfoVO> userList = userService.queryPagedResult("queryUserNotRelateGroup", userMap,
				currentPage, pageSize, "USER_ID.desc");
		/////////////////////////////// end////////////////////////////////////////////
		mv.addObject("groupId", groupId);
		mv.addObject("rootGroupId", rootGroupId);
		mv.addObject("positionList", list);
		mv.addObject("vos", userList);
		mv.addObject("currentPage", currentPage);
		return mv;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(@RequestParam(value = "positionId", required = false) Long[] positionId,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "rootGroupId", required = false) Long rootGroupId,
			@RequestParam(value = "isManager", required = false) Boolean isManager) {

		ModelAndView mv = new ModelAndView(
				"redirect:/punUserGroupController/getUserList.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId);
		PunUserGroupVO vo = new PunUserGroupVO();
		vo.setGroupId(groupId);
		vo.setUserId(userId);
		punUserGroupService.remove(vo);
		if (positionId != null && positionId.length > 0) {
			for (int j = 0; j < positionId.length; j++) {
				PunUserGroupVO p = new PunUserGroupVO();
				p.setGroupId(groupId);
				p.setUserId(userId);
				p.setPositionId(positionId[j]);
				if (isManager != null) {
					p.setIsManager(true);
				}
				punUserGroupService.save(p);
			}
		}
		return mv;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping(value = "/pageList", method = RequestMethod.POST)
	public String selectPagedByExample(Long groupId) {
		Map paramMap = new HashMap();
		paramMap.put("groupId", groupId);
		PageList<PunUserBaseInfoVO> list = userService.queryPagedResult("selectUserList", paramMap, 0, 99999, null);
		return "/manager/punUserGroups";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getUserList", method = RequestMethod.GET)
	public ModelAndView getUserListByGroupId(Long groupId, Long rootGroupId) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punGroup-userEdit");

		PageList list = (PageList) punUserGroupService.queryUserListByGroupId(groupId);
		mv.addObject("userList", list);
		mv.addObject("groupId", groupId);
		mv.addObject("rootGroupId", rootGroupId);
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public ModelAndView getUsers(Long groupId, Long rootGroupId, String number, Long user) {

		String str = "";
		// 获取部门名称
		PunGroupVO pun = groupService.findById(groupId);

		// if(null!=pun.getNumber()&&!"".equals(pun.getNumber())){
		//
		// str=pun.getNumber()+"-";
		// }else{
		//
		// //获取父级的排序
		// if(null!=pun.getPid()&&!"".equals(pun.getPid())){
		// String ids[]=pun.getPid().split(",");
		// for (String s : ids) {
		// PunGroupVO tem=groupService.findById(Long.parseLong(s));
		// if("".equals(tem.getNumber())||null==tem.getNumber()){
		// tem.setNumber("NO");
		// }
		// String nu=tem.getNumber();
		// //只取父节点本身的排序
		// str+=nu.substring(nu.lastIndexOf("-")+1,nu.length())+"-";
		// }
		// //本身为空的number
		// str+="NO-";
		// }
		// }

		// 编号不为空的话修改编号
		if (!"".equals(number) && number != null && !"".equals(user) && user != null) {

			PunUserBaseInfoVO uvo = userService.findById(user);
			uvo.setNumber(number);
			userService.updateUser(uvo);
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punGroup-usersView");

		PageList<PunUserBaseInfoVO> list = punUserGroupService.queryUserListByGroupId(groupId);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setDeptName(pun.getGroupChName());
			}

		}

		mv.addObject("userList", list);
		mv.addObject("groupId", groupId);
		mv.addObject("rootGroupId", rootGroupId);
		// mv.addObject("deptName",pun.getGroupChName());
		return mv;
	}

	/**
	 * 从部门或者小组中删除指定的用户
	 * 
	 * @param boxs
	 *            用户Id
	 * @param groupId
	 *            部门或者小组Id
	 * @return
	 */
	@RequestMapping(value = "/removeUserFromGroup")
	public ModelAndView removeUserFromGroup(Long[] boxs, Long groupId, Long rootGroupId) {
		ModelAndView mv = new ModelAndView();
		for (Long userId : boxs) {
			if (userId != null && userId.longValue() != 0) {
				PunUserGroupVO vo = new PunUserGroupVO();
				vo.setGroupId(groupId);
				vo.setUserId(userId);
				punUserGroupService.remove(vo);
			}
		}
		mv.setViewName(
				"redirect:/punUserGroupController/getUserList.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId);
		return mv;
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/getUserListByAjax", method = RequestMethod.GET)
	public List getUserListByAjax(Long groupId, Long rootGroupId) {
		PageList list = (PageList) punUserGroupService.queryUserListByGroupId(groupId);
		return list;
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/getUserListByWhere", method = RequestMethod.POST)
	public List getUserListByWhere(Long groupId, Long rootGroupId, String role, String job, String eq,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", required = false, defaultValue = "9999") int pageSize) {
		String sortString = "USER_ID.desc";
		Map<String, Object> params = new HashMap<String, Object>();
		String sqlMap = "";

		if (role != null) {
			params.put("roleId", role);
			sqlMap = "queryUserRelateRole";
		}

		if (job != null) {
			params.put("position_id", job);
			sqlMap = "queryUserRelatePosition";
		}

		if (eq != null) {
			params.put("name", eq);
			List<PunUserBaseInfoVO> userVOs = userService.queryResult("eqQueryList", params);
			return userVOs;
		}

		List<PunUserBaseInfoVO> userVOs = userService.queryPagedResult(sqlMap, params, currentPage, pageSize,
				sortString);
		PunUserBaseInfoVO user = DocumentUtils.getUser();
		for (int i = 0; i < userVOs.size(); i++) {
			if (user.getUserId().equals(userVOs.get(i).getUserId())) {
				userVOs.remove(i);
				break;
			}
		}
		return userVOs;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getUserForEdit", method = RequestMethod.GET)
	public ModelAndView getUserForEdit(Long groupId, Long rootGroupId, Long userId) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punUserGroupEdit");
		Map queryParam = new HashMap();
		queryParam.put("groupId", rootGroupId);
		BaseExample example = new BaseExample();
		List list = punPositionService.selectByExample(example);

		List userList = punUserGroupService.queryUserByUserIdAndGroupId(groupId, userId);
		/**
		 * ljw 2014-12-8 新增：查询用户与组织的关系
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", groupId);
		params.put("userId", userId);
		List<PunUserGroupVO> userGroupVOs = punUserGroupService.queryResult("eqQueryList", params);
		if (null != userGroupVOs && userGroupVOs.size() > 0) {
			mv.addObject("userGroup", userGroupVOs.get(0));
		}
		/////////////////////////////// 结束//////////////////////////////////
		mv.addObject("groupId", groupId);
		mv.addObject("rootGroupId", rootGroupId);
		mv.addObject("positionList", list);
		mv.addObject("user", userList.get(0));
		return mv;
	}

	// 用户组织编辑
	@ResponseBody
	@RequestMapping(value = "/userGroupEdit", method = RequestMethod.POST)
	public Map<String, Object> userGroupEdit(Long groupId, String userIds, Long oldGroupId) {
		if (!"".equals(userIds) && userIds != null) {

			String ids[] = userIds.split(",");
			for (String s : ids) {

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("groupId", oldGroupId);
				params.put("userId", s);
				List<PunUserGroupVO> userGroupVOs = punUserGroupService.queryResult("eqQueryList", params);
				String str = "";
				if (null != userGroupVOs && userGroupVOs.size() > 0) {
					for (PunUserGroupVO pvo : userGroupVOs) {

						pvo.setGroupId(groupId);
						punUserGroupService.save(pvo);
					}
				}

				// 移动组织修改用户在组织的序号
				PunUserBaseInfoVO pu = userService.findById(Long.parseLong(s));
				PunGroupVO pun = groupService.findById(groupId);

				if (null != pun.getNumber() && !"".equals(pun.getNumber())) {

					str = pun.getNumber() + "-";
				} else {

					// 获取父级的排序
					if (null != pun.getPid() && !"".equals(pun.getPid())) {
						String pid[] = pun.getPid().split(",");
						for (String st : pid) {
							PunGroupVO tem = groupService.findById(Long.parseLong(st));
							if ("".equals(tem.getNumber()) || null == tem.getNumber()) {
								tem.setNumber("NO");
							}
							String nu = tem.getNumber();
							// 只取父节点本身的排序
							str += nu.substring(nu.lastIndexOf("-") + 1, nu.length()) + "-";
						}
						// 本身为空的number
						str += "NO-";
					}
				}
				if (null != pu.getNumber() && !"".equals(pu.getNumber())) {
					String nu = pu.getNumber();
					// 只取本身的排序
					pu.setNumber(nu.substring(nu.lastIndexOf("-") + 1, nu.length()));
				}
				pu.setNumber(str + pu.getNumber());
				userService.addOrUpdateUser(pu);

			}
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("result", "success");
			return res;

		}

		return null;
	}

}