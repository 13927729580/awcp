package org.szcloud.framework.venson.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;

import com.alibaba.fastjson.JSONObject;

import BP.Port.WebUser;

/**
 * 用户登录拦截器，
 * 
 * @author Venson Yang
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String path = request.getContextPath();
		String uri = request.getRequestURI();
		uri = uri.substring(path.length(), uri.length());
		logger.debug("当前请求地址:" + uri);
		if (filterCurrUrl(request)) {
			response.setContentType("application/json");
			ReturnResult returnResult = ReturnResult.get();
			returnResult.setStatus(StatusCode.NO_LOGIN.setMessage("您还未登录,请先登录！"));
			logger.debug("非法用户,禁止访问");
			response.getWriter().print(JSONObject.toJSON(returnResult));
			return false;
		}
		return true;
	}

	private boolean filterCurrUrl(HttpServletRequest res) throws IOException {
		Object user = SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);

		if (null == user && WebUser.getName() == null) {
			return true;
		} else {
			return false;
		}
	}

}