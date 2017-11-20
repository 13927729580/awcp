package cn.org.awcp.unit.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.util.CookieUtil;
import cn.org.awcp.workflow.controller.util.HttpRequestDeviceUtils;

public class MyAuthorizationFilter extends FormAuthenticationFilter {
	private static final Log log = LogFactory.getLog(MyAuthorizationFilter.class);

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			} else {
				if (log.isTraceEnabled()) {
					log.trace("Login page view.");
				}
				// allow them to see the login page ;)
				return true;
			}
		} else {
			if (log.isTraceEnabled()) {
				log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
						+ "Authentication url [" + getLoginUrl() + "]");
			}
			if (ControllerHelper.loginByCookie()) {
				return true;
			} else {
				// 判断请求是否来自于钉钉
				HttpServletRequest req = (HttpServletRequest) request;
				if (HttpRequestDeviceUtils.isDingDing(req)) {
					HttpServletResponse res = (HttpServletResponse) response;
					String query = req.getQueryString();
					query = query == null ? "" : "?" + query;
					String url = req.getRequestURI() + query;
					url = URLEncoder.encode(url);
					res.sendRedirect(ControllerHelper.getBasePath() + "dingding/goto.html?url=" + url);
					return true;
				}

			}
			CookieUtil.deleteCookie(SC.SECRET_KEY);
			tologin(request, response);
			return false;
		}
	}

	private void tologin(ServletRequest request, ServletResponse response) throws IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestType = httpServletRequest.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {

			// saveRequestAndRedirectToLogin(request, response);
			saveRequest(request);
			response.setContentType("application/json");
			ReturnResult returnResult = ReturnResult.get();
			returnResult.setStatus(StatusCode.NO_LOGIN.setMessage("您还未登录,请先登录！"));
			PrintWriter out = response.getWriter();
			out.print(JSONObject.toJSON(returnResult));
			out.flush();
			out.close();
		} else {
			WebUtils.issueRedirect(request, response, getLoginUrl());
		}
	}

}
