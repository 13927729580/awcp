package cn.org.awcp.base;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import TL.ContextHolderUtils;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ReturnResult;

/**
 * Servlet Filter implementation class SystemFilter
 */
public class SystemFilter implements Filter {
	private String encoding;

	/**
	 * Default constructor.
	 */
	public SystemFilter() {

	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		/*************** 将request,response保存到当前线程中去 *****************/
		ControllerContext.setRequest(httpRequest);
		ControllerContext.setResponse(httpResponse);
		ControllerContext.setResult(new ReturnResult());
		ContextHolderUtils.setResponse(httpResponse);
		/**************** 结束 ****************/
		/*************** 设置字符编码 *****************/
		httpRequest.setCharacterEncoding(encoding);
		httpResponse.setCharacterEncoding(encoding);
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		encoding = fConfig.getInitParameter("encoding");
	}

}
