package cn.org.awcp.unit.shiro;

import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * shiro 授权过滤器
 * 根据cookie中的secretKey值实现免登
 * @author venson
 * @version 20180518
 */
public class AwcpAuthorizationFilter extends AuthenticationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		boolean isLogin = (session != null && session.getAttribute(SessionContants.CURRENT_USER) != null) || ControllerHelper.loginByCookie();
		return isLogin;

	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		ControllerHelper.logout();
		return false;
	}


}
