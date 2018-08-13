package cn.org.awcp.base;

import cn.org.awcp.common.db.DynamicDataSource;
import cn.org.awcp.core.utils.ContextContentUtils;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ReturnResult;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
	@Override
	public void destroy() {

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		/*************** 将request,response保存到当前线程中去 *****************/
		ControllerContext.setResult(new ReturnResult());
		ContextContentUtils.setRequest(httpRequest);
		ContextContentUtils.setResponse(httpResponse);
		/**************** 结束 ****************/
		/*************** 设置字符编码 *****************/
		httpRequest.setCharacterEncoding(encoding);
		httpResponse.setCharacterEncoding(encoding);
		chain.doFilter(request, response);
		ControllerContext.removeResult();
		ControllerContext.removePage();
		ControllerContext.removeDoc();
		ContextContentUtils.removeRequest();
		ContextContentUtils.removeResponse();
		DynamicDataSource.clearDataSource();
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		encoding = fConfig.getInitParameter("encoding");
	}

}
