package org.szcloud.framework.unit.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.Criteria;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.unit.service.PunGroupService;
import org.szcloud.framework.unit.service.PunRoleAccessService;
import org.szcloud.framework.unit.service.PunRoleInfoService;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.utils.EncryptUtils;
import org.szcloud.framework.unit.utils.WhichEndEnum;
import org.szcloud.framework.unit.vo.PunGroupVO;
import org.szcloud.framework.unit.vo.PunRoleAccessVO;
import org.szcloud.framework.unit.vo.PunRoleInfoVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;

public class ShiroDbRealm extends AuthorizingRealm {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(ShiroDbRealm.class);

	@Autowired
	@Qualifier("punGroupServiceImpl")
	private PunGroupService groupService;// 组

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	private PunUserBaseInfoService userService;// 用户

	@Autowired
	@Qualifier("punRoleInfoServiceImpl")
	private PunRoleInfoService roleService;// 角色

	@Autowired
	@Qualifier("punRoleAccessServiceImpl")
	private PunRoleAccessService roleAccessService;// 访问控制

	// 授权方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if (null == principals) {
			throw new AuthenticationException("principals can not be null");
		}
		// 获取当前登录的用户名
		String userName = (String) super.getAvailablePrincipal(principals);

		String[] result = userName.split("_");
		String whichEnd = result[2];// 端
		switch (WhichEndEnum.getOperChartType(whichEnd)) {
		case FRONT_END:// 前端
			info = frontAuthorizationInfo();
			break;
		default:
			break;
		}

		return info;
	}

	/**
	 * 前端授权
	 * 
	 * @return
	 */
	private SimpleAuthorizationInfo frontAuthorizationInfo() {
		SimpleAuthorizationInfo frontInfo = new SimpleAuthorizationInfo();
		List<String> roles = new ArrayList<String>();
		List<Long> roleIds = new ArrayList<Long>();// 角色ID

		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		Map<String, Object> roleParams = new HashMap<String, Object>();
		roleParams.put("userId", user.getUserId());

		List<PunRoleInfoVO> roleVos = roleService.queryResult("queryByUser", roleParams);
		for (PunRoleInfoVO vo : roleVos) {
			roles.add(vo.getRoleName());// 角色
			roleIds.add(vo.getRoleId());
			logger.debug("roleName:" + vo.getRoleName());
		}
		// 给当前用户设置角色
		frontInfo.addRoles(roles);

		if (!roleIds.isEmpty()) {
			// 获取所有角色的权限
			// 将所有角色的权限置入permission中。
			BaseExample example = new BaseExample();
			Criteria criteria = example.createCriteria();

			criteria.andInLong("ROLE_ID", roleIds);
			List<PunRoleAccessVO> resultsAccessVOs = roleAccessService.selectByExample(example);
			List<String> permissions = new ArrayList<String>();// 权限字符串
			for (PunRoleAccessVO vo : resultsAccessVOs) {
				// 权限组成模式->操作类型：资源ID
				permissions.add(vo.getOperType() + ":" + vo.getResourceId());
			}
			logger.debug(Arrays.toString(permissions.toArray()));
			// 给当前用户设置权限
			frontInfo.addStringPermissions(permissions);
		}
		return frontInfo;
	}

	// 验证方法
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		return verifyEnd(token);
	}

	/**
	 * 根据端验证
	 * 
	 * @return
	 */
	private SimpleAuthenticationInfo verifyEnd(UsernamePasswordToken token) {
		String nameAndOrgcodeAndEnd = token.getUsername();
		String[] result = nameAndOrgcodeAndEnd.split("_");
		String orgCode = result[0];// 组织机构代码
		String idCard = result[1];// 身份证号码
		String whichEnd = result[2];// 端
		String plainToken = "";
		if (result.length > 3)
			plainToken = result[3];
		String password = "";// 密码
		if (StringUtils.isEmpty(plainToken)) {
			switch (WhichEndEnum.getOperChartType(whichEnd)) {
			case FRONT_END:// 前端
				password = validateFrontUser(orgCode, idCard);
				break;

			default:
				break;
			}
		}
		return new SimpleAuthenticationInfo(token.getUsername(), password, this.getName());
	}

	/**
	 * 校验前端用户
	 * 
	 * @param orgCode
	 * @param idCard
	 * @return
	 */
	private String validateFrontUser(String orgCode, String idCard) {
		logger.debug("validateFrontUser");
		Map<String, Object> groupParams = new HashMap<String, Object>();
		groupParams.put("orgCode", orgCode);
		List<PunGroupVO> groups = groupService.queryResult("eqQueryList", groupParams);// 组信息
		if (groups == null || groups.size() == 0)
			return null; // no group found by orgCode;
		PunGroupVO group = groups.get(0);
		List<PunUserBaseInfoVO> users = userService.selectByIDCard(idCard);// 用户基础信息
		for (PunUserBaseInfoVO user : users) {
			if (group.getGroupId().equals(user.getGroupId()))
				return EncryptUtils.decript(user.getUserPwd());
		}
		return null;
	}

}
