package cn.org.awcp.venson.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;

import com.alibaba.fastjson.JSONObject;

import BP.Port.WebUser;

/**
 * 用户登录拦截器，
 * 
 * @author Venson Yang
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	private Log logger = LogFactory.getLog(getClass());

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