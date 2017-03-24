package org.szcloud.framework.base;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.unit.vo.PunSystemVO;
import org.szcloud.framework.venson.controller.base.ControllerContext;

import TL.ContextHolderUtils;

/**
 * Servlet Filter implementation class SystemFilter
 */
public class SystemFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public SystemFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		/*************** 将request,response保存到当前线程中去 *****************/
		ControllerContext.setRequest(httpServletRequest);
		ControllerContext.setResponse((HttpServletResponse) response);
		ContextHolderUtils.setResponse((HttpServletResponse) response);
		/**************** 结束 ****************/
		String url = httpServletRequest.getContextPath();
		if (url.indexOf("/fd") != -1 || url.indexOf("/component") != -1 || url.indexOf("/layout") != -1
				|| url.indexOf("/pfmTemplateController") != -1) {
			Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if (obj == null) {
				httpServletRequest.getRequestDispatcher("/error.jsp").forward(request, response);
			} else {
				if (obj instanceof PunSystemVO) {
					PunSystemVO system = (PunSystemVO) obj;

				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
