package org.szcloud.framework.unit.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.szcloud.framework.core.utils.Security;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.utils.WhichEndEnum;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.common.SC;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;

import com.alibaba.fastjson.JSONObject;

import TL.ContextHolderUtils;

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
			Cookie secretKey = ContextHolderUtils.getCookie(SC.SECRET_KEY);
			Cookie userAccount = ContextHolderUtils.getCookie(SC.USER_ACCOUNT);
			if (userAccount != null && secretKey != null
					&& Security.encodeToMD5(userAccount.getValue() + SC.SALT).equals(secretKey.getValue())) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("userIdCardNumber", userAccount.getValue());
				PunUserBaseInfoService userService = Springfactory.getBean("punUserBaseInfoServiceImpl");
				List<PunUserBaseInfoVO> pvis = userService.queryResult("eqQueryList", m);
				if (!pvis.isEmpty()) {
					PunUserBaseInfoVO pvi = pvis.get(0);
					Subject subject = SecurityUtils.getSubject();
					String plainToke = pvi.getOrgCode() + "_" + pvi.getUserName() + "_"
							+ WhichEndEnum.FRONT_END.getCode();
					UsernamePasswordToken token = new UsernamePasswordToken(plainToke,
							pvi.getUserPwd() == null ? "" : Security.decryptPassword(pvi.getUserPwd()));
					subject.login(token);
					ControllerHelper.doLoginSuccess(pvi);
					return true;
				}
			}
			ContextHolderUtils.deleteCookie(SC.SECRET_KEY);
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
			ReturnResult returnResult = ReturnResult.get();
			response.setContentType("application/json");
			returnResult.setStatus(StatusCode.NO_LOGIN.setMessage("您还未登录,请先登录！"));
			PrintWriter out = response.getWriter();
			out.print(JSONObject.toJSON(returnResult));
			out.flush();
			out.close();
		}
	}

}
