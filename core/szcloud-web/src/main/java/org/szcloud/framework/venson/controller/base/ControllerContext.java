package org.szcloud.framework.venson.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class ControllerContext {

	private static final ThreadLocal<HttpServletRequest> request_threadLocal = new ThreadLocal<HttpServletRequest>();
	private static final ThreadLocal<HttpServletResponse> reponse_threadLocal = new ThreadLocal<HttpServletResponse>();

	public static void setRequest(HttpServletRequest request) {
		request_threadLocal.set(request);
	}

	public static HttpServletRequest getRequest() {
		return request_threadLocal.get();
	}

	public static void removeRequest() {
		request_threadLocal.remove();
	}

	public static HttpSession getSession() {
		return request_threadLocal.get().getSession();
	}

	public static void setResponse(HttpServletResponse response) {
		reponse_threadLocal.set(response);
	}

	public static HttpServletResponse getResponse() {
		return reponse_threadLocal.get();
	}

	public static void removeResponse() {
		reponse_threadLocal.remove();
	}
}