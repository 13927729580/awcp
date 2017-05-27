package org.szcloud.framework.venson.interceptor;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.unit.vo.PunRoleInfoVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.controller.base.ControllerHelper;

public class AuthorityFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		if (user != null) {
			String uri = httpRequest.getRequestURI();
			if (uri.contains("/dev")) {
				List<PunRoleInfoVO> roles = (List<PunRoleInfoVO>) SessionUtils
						.getObjectFromSession(SessionContants.CURRENT_ROLES);
				boolean isSuperAdmin = false;
				for (PunRoleInfoVO role : roles) {
					if (role.getRoleName().equals("超级后台管理员"))
						isSuperAdmin = true;
				}
				if (!isSuperAdmin) {
					httpRequest.getRequestDispatcher("/errorHandle.jsp").forward(request, response);
					return;
				}
			}
		}
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
