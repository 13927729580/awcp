package cn.org.awcp.unit.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.unit.service.PunGroupService;
import cn.org.awcp.unit.service.PunRoleAccessService;
import cn.org.awcp.unit.service.PunRoleInfoService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.utils.EncryptUtils;
import cn.org.awcp.unit.utils.WhichEndEnum;
import cn.org.awcp.unit.vo.PunRoleAccessVO;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.util.CookieUtil;

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

	public final static String SPLIT = "-_-";

	@Autowired
	@Qualifier("punRoleAccessServiceImpl")
	private PunRoleAccessService roleAccessService;// 访问控制

	private UsernamePasswordToken token;

	// 授权方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if (null == token) {
			throw new AuthenticationException("token can not be null");
		}
		// 获取当前登录的用户名

		String[] result = token.getUsername().split(SPLIT);
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
		this.token = token;
		return verifyEnd(token);
	}

	/**
	 * 根据端验证
	 * 
	 * @return
	 */
	private SimpleAuthenticationInfo verifyEnd(UsernamePasswordToken token) {
		String nameAndOrgcodeAndEnd = token.getUsername();
		String[] result = nameAndOrgcodeAndEnd.split(SPLIT);
		String idCard = result[0];// 身份证号码
		String whichEnd = result[1];// 端
		PunUserBaseInfoVO pvi = null;
		String password = null;
		// 查看是否属于免密校验
		if (result.length > 2) {
			password = result[2];
			pvi = findFrontUser(idCard);
		} else {
			switch (WhichEndEnum.getOperChartType(whichEnd)) {
			case FRONT_END:// 前端
				pvi = findFrontUser(idCard);
				password = pvi.getUserPwd();
				password = EncryptUtils.decript(password);
				break;

			default:
				break;
			}
		}
		return new SimpleAuthenticationInfo(pvi, password, this.getName());
	}

	/**
	 * 校验前端用户
	 * 
	 * @param orgCode
	 * @param idCard
	 * @return
	 */
	private PunUserBaseInfoVO findFrontUser(String idCard) {
		logger.debug("validateFrontUser");
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("userIdCardNumber", idCard);
		List<PunUserBaseInfoVO> pvis = this.userService.queryResult("eqQueryList", m); // 查询用户
		if (pvis.isEmpty()) {
			CookieUtil.deleteCookie(SC.USER_ACCOUNT);
			CookieUtil.deleteCookie(SC.SECRET_KEY);
			throw new PlatformException("账号未找到");
		} else if (pvis.size() != 1) {
			CookieUtil.deleteCookie(SC.USER_ACCOUNT);
			CookieUtil.deleteCookie(SC.SECRET_KEY);
			throw new PlatformException("账号信息有误");
		} else {
            return pvis.get(0);
        }
	}

}
