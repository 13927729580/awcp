package cn.org.awcp.venson.interceptor;

import cn.org.awcp.common.db.DynamicDataSource;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static cn.org.awcp.venson.common.SC.DATA_SOURCE_COOKIE_KEY;

public class AuthorityFilter implements Filter {


	/**
	 * 数据源request参数键值名称
	 */
	private static final String DATA_SOURCE_REQUEST_KEY = "request_group_name";

	private DynamicDataSource dataSource= Springfactory.getBean("dataSource");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(!determineTargetDataSource(request)){
			DynamicDataSource.clearDataSource();
			ControllerHelper.logout();
			return;
		}
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

	private boolean determineTargetDataSource(ServletRequest request) {
		String dataSourceName = request.getParameter(DATA_SOURCE_REQUEST_KEY);
		if (StringUtils.isNotBlank(dataSourceName) && dataSource.get(dataSourceName) != null) {
			DynamicDataSource.setDataSource(dataSourceName);
			CookieUtil.addCookie(DATA_SOURCE_COOKIE_KEY, dataSourceName);
		} else {
			//从cookie中查找
			dataSourceName = CookieUtil.findCookie(DATA_SOURCE_COOKIE_KEY);
			if (StringUtils.isNotBlank(dataSourceName) && dataSource.get(dataSourceName) != null) {
				DynamicDataSource.setDataSource(dataSourceName);
			} else {
				DynamicDataSource.setDataSource(DynamicDataSource.MASTER_DATA_SOURCE);
			}

		}
		String currentUserDataSource = (String) SessionUtils.getObjectFromSession(SC.CURRENT_USER_DATA_SOURCE);
		//查看当前的数据源是否和登录用户的数据源一致
		if (currentUserDataSource != null && !currentUserDataSource.equals(DynamicDataSource.getDataSource())) {
			return false;
		}
		return true;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
