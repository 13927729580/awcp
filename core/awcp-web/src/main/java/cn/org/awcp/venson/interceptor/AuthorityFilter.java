package cn.org.awcp.venson.interceptor;

import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class AuthorityFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		if (user != null) {
			String uri = httpRequest.getRequestURI();
			if (uri.endsWith("jsp") || uri.endsWith("do")) {
				if (uri.contains("/dev") || uri.contains("/fd") || uri.contains("/dataSys")
						|| uri.contains("/metaModel") || uri.contains("/dataSourceManage")) {
					@SuppressWarnings("unchecked")
					List<PunRoleInfoVO> roles = (List<PunRoleInfoVO>) SessionUtils
							.getObjectFromSession(SessionContants.CURRENT_ROLES);
					if (roles == null) {
						httpRequest.getRequestDispatcher("/errorHandle.jsp").forward(request, response);
						return;
					}
					boolean isSuperAdmin = false;
					for (PunRoleInfoVO role : roles) {
						if ("超级后台管理员".equals(role.getRoleName())) {
							isSuperAdmin = true;
							break;
						}
					}
					if (!isSuperAdmin) {
						httpRequest.getRequestDispatcher("/errorHandle.jsp").forward(request, response);
						return;
					}
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
