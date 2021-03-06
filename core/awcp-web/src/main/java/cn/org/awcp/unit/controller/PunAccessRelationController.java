package cn.org.awcp.unit.controller;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.ResourceTypeEnum;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.unit.core.domain.PunPosition;
import cn.org.awcp.unit.service.PunMenuService;
import cn.org.awcp.unit.service.PunPositionService;
import cn.org.awcp.unit.service.PunResourceService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.service.PunUserGroupService;
import cn.org.awcp.unit.service.PunUserRoleService;
import cn.org.awcp.unit.utils.JsonFactory;
import cn.org.awcp.unit.utils.ResourceTreeUtils;
import cn.org.awcp.unit.vo.PunGroupVO;
import cn.org.awcp.unit.vo.PunMenuVO;
import cn.org.awcp.unit.vo.PunResourceTreeNode;
import cn.org.awcp.unit.vo.PunResourceVO;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.unit.vo.PunUserGroupVO;

/**
 * 
 * 系统角色设置及权限管理
 *
 * @工程： szcloud-web @模块：
 *
 * @作者：huangmin
 * @创建日期：2015年7月23日 下午2:47:18
 * @页面名称：PunAccessRelationController
 *
 * @修改记录（修改时间、作者、原因）：
 */
@Controller
@RequestMapping("/punAccessRelationController")
public class PunAccessRelationController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;// 用户Service

	@Resource(name = "punUserGroupServiceImpl")
	private PunUserGroupService punUserGroupService;

	@Resource(name = "punPositionServiceImpl")
	private PunPositionService punPositionService;

	@Autowired
	@Qualifier("punUserRoleServiceImpl")
	PunUserRoleService userRoleService;// 用户角色关联Service
	@Autowired
	@Qualifier("punMenuServiceImpl")
	PunMenuService menuService;

	@Resource(name = "punResourceServiceImpl")
	private PunResourceService resoService;

	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	@Autowired
	private SzcloudJdbcTemplate jdbcTemplate;
	
	/**
	 * 岗位关联人员
	 * 
	 *
	 * @方法名称：getPosRelateUsers()
	 * 
	 * @作者：huangmin
	 * @创建日期：2015年7月23日 下午3:21:05
	 *
	 * @return ModelAndView
	 *
	 * @修改记录（修改时间、作者、原因）：
	 */
	@RequestMapping(value = "punAccessRelation")
	public ModelAndView getPosRelateUsers() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/unit/punAccessRelation");
		try {
			// 获取当前的机构（深圳市人民医院）
			PunGroupVO vo = (PunGroupVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER_GROUP);
			mv.addObject("groupId", vo.getGroupId());
		} catch (Exception e) {
			logger.info("ERROR", e);
			mv.addObject("result", "System error.");
		}
		return mv;
	}

	/**
	 * 角色关联功能
	 * 
	 *
	 * @方法名称：punRoleMenuAccessEdit()
	 * 
	 * @作者：huangmin
	 * @创建日期：2015年8月4日 上午11:19:59
	 *
	 * @param roleId
	 *            角色编号
	 * @return ModelAndView
	 *
	 * @修改记录（修改时间、作者、原因）：
	 */
	@RequestMapping(value = "punRoleMenuAccessEdit")
	public ModelAndView punRoleMenuAccessEdit(Long roleId,Integer moduleId) {
		PunSystemVO sysVO = (PunSystemVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
		Long sysId = sysVO.getSysId();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("unit/dev/accessAuthor");
		try {
			List<PunMenuVO> resoVOs = menuService.findAll();
			List<PunMenuVO> accVOS = new ArrayList<>();
			if (roleId != null) {
				PunRoleInfoVO vo = new PunRoleInfoVO();
				vo.setRoleId(roleId);
				vo.setSysId(sysId);
				mv.addObject("roleName", jdbcTemplate.queryForObject(
						"select ROLE_NAME from p_un_role_info where ROLE_ID=?", String.class,roleId));
				mv.addObject("vo", vo);
				accVOS = menuService.getByRoleAndSys(vo, sysVO);
			}

			this.setAccessRight(resoVOs, accVOS);
			ResourceTreeUtils rtu = new ResourceTreeUtils();
			List<PunMenuVO[]> list = rtu.generateTreeView(resoVOs);
			Map<Integer, List<PunResourceTreeNode>> resultMap = new HashMap<>();
			Integer index = 1;
			for (PunMenuVO[] prvo : list) {
				// 以目录根节点的index为key，以manyNodeTree为value
				List<PunResourceTreeNode> manyNodeTree = ResourceTreeUtils.getPlainDevAccessZNodes(prvo, roleId);
				resultMap.put(index, manyNodeTree);
				index++;
			}
			JsonFactory factory = new JsonFactory();
			mv.addObject("menuJson", factory.encode2Json(resultMap));

			String sql;
			if(moduleId!=null){
				sql = "select id from p_fm_dynamicpage where modular=" + moduleId;
			}
			else{
				sql = "select id from p_fm_dynamicpage where modular=(select ID from p_fm_modular limit 1)";
			}
			List<String> dynamicPageIds =	jdbcTemplate.queryForList(sql,String.class);
			sql = "select ID,modularName from p_fm_modular order by ID ";
			mv.addObject("modules", jdbcTemplate.queryForList(sql));
			mv.addObject("moduleId",moduleId);
			
			// 1、查找当前系统的所有按钮，按动态表单名分类
			// 2、查找资源，格式为Map<relateResoId,Resource>
			// 3、查找已授权的资源
			Map<String, List<StoreVO>> buttonMap = formdesignerServiceImpl.getSystemActs(dynamicPageIds);

			Map<String, Object> params = new HashMap<>();
			params.put("sysId", sysId);
			params.put("resouType", ResourceTypeEnum.RESO_BUTTON.getkey());

			Map<String, PunResourceVO> buttonResos = resoService.queryButtonMap("eqQueryList", params);
			Map<String, List<PunResourceVO>> resultsMap = new TreeMap<>();
			Set<String> keys = buttonMap.keySet();
			// 将按钮按照动态表单名称划分
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String s =  it.next();
				List<StoreVO> storeVOs = buttonMap.get(s);
				for (StoreVO storeVO : storeVOs) {
					// 如果resouce中有按钮ID
					if (buttonResos.containsKey(storeVO.getId())) {
						if (resultsMap.containsKey(s)) {
							List<PunResourceVO> newVos = resultsMap.get(s);
							newVos.add(buttonResos.get(storeVO.getId()));
						} else {
							List<PunResourceVO> temVOs = new ArrayList<>();
							temVOs.add(buttonResos.get(storeVO.getId()));
							resultsMap.put(s, temVOs);
						}
					}
				}
			}
			Map<String, Object> authorParams = new HashMap<>();
			authorParams.put("sysId", sysId);
			authorParams.put("roleId", roleId);
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
	 * 
	 * 通过参数获取用户列表
	 *
	 * @方法名称：getUserListByParas()
	 * 
	 * @作者：huangmin
	 * @创建日期：2015年7月30日 下午4:38:31
	 *
	 * @param name
	 * @return List<PunUserBaseInfoVO>
	 *
	 * @修改记录（修改时间、作者、原因）：
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserListByParas", method = RequestMethod.POST)
	public List<PunUserBaseInfoVO> getUserListByParas(String name, String groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<PunUserBaseInfoVO> users = null;
		if (name != null & name.length() > 0) {
			map.put("name", "%" + name + "%");
			map.put("groupId", groupId);
			users = userService.queryResult("queryUsersByGroupId", map);
		}
		return users;
	}

	/**
	 * 
	 * 通过部门编号获取人员
	 *
	 * @方法名称：getUserListByDeptId()
	 * 
	 * @作者：huangmin
	 * @创建日期：2015年7月31日 上午9:43:48
	 *
	 * @param deptId
	 *            部门编号
	 * @param groupId
	 *            机构编号
	 * @return List<PunUserBaseInfoVO>
	 *
	 * @修改记录（修改时间、作者、原因）：
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserListByDeptId", method = RequestMethod.POST)
	public List<PunUserBaseInfoVO> getUserListByDeptId(String deptId, String groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<PunUserBaseInfoVO> users = null;
		if (deptId != null && groupId != null) {
			map.put("deptId", deptId);
			map.put("groupId", groupId);
			users = userService.queryResult("queryUsersByGroupId", map);
		}
		return users;
	}

	/**
	 * 保存人员和岗位之间的关系
	 * 
	 *
	 * @方法名称：savePost()
	 * 
	 * @作者：huangmin
	 * @创建日期：2015年8月3日 下午5:42:32
	 *
	 * @param postId
	 * @param userIds
	 * @param deptIds
	 * @return int
	 *
	 * @修改记录（修改时间、作者、原因）：
	 */
	@ResponseBody
	@RequestMapping(value = "/savePost", method = RequestMethod.POST)
	public int savePost(String postId, String userIds, String deptIds) {
		int flag = 0;
		if (StringUtils.isNoneBlank(postId) && userIds != null && deptIds != null) {
			Long posId = Long.valueOf(postId.split(",")[0]);
			String[] userIdArr = userIds.split(",");
			String[] deptIdArr = deptIds.split(",");
			// 先删除原有的岗位信息
			PunPosition positionVO = new PunPosition();
			positionVO.setPositionId(posId);
			punPositionService.removeRelations(positionVO);
			flag = 1;
			if (StringUtils.isNoneBlank(userIds) && StringUtils.isNoneBlank(deptIds) && userIdArr.length > 0
					&& userIdArr.length == deptIdArr.length) {
				// 在增加现有的关联关系
				for (int i = 0; i < userIdArr.length; i++) {
					PunUserGroupVO userGroupVO = new PunUserGroupVO();
					userGroupVO.setGroupId(Long.valueOf(deptIdArr[i]));
					userGroupVO.setUserId(Long.valueOf(userIdArr[i]));
					userGroupVO.setPositionId(posId);
					try {
						punUserGroupService.save(userGroupVO);
					} catch (Exception e) {
						logger.info("ERROR", e);
					}

				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * 保存角色与人员之间的关系
	 *
	 * @方法名称：saveRole()
	 * 
	 * @作者：huangmin
	 * @创建日期：2015年8月4日 上午10:50:55
	 *
	 * @param roleId
	 * @param userIds
	 * @return boolean
	 *
	 * @修改记录（修改时间、作者、原因）：
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRole", method = RequestMethod.POST)
	public boolean saveRole(String roleId, String userIds) {
		boolean flag = false;
		if (StringUtils.isNoneBlank(roleId)) {
			String roleIdstrString = roleId.split(",")[0];
			flag = userRoleService.excuteRoleAndUser(roleIdstrString, userIds);
		}
		if (StringUtils.isBlank(userIds)){
			flag = true;
		}
		return flag;
	}

}
