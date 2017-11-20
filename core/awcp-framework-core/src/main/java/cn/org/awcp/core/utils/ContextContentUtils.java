package cn.org.awcp.core.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @ClassName: ContextHolderUtils
 * @Description: TODO(上下文工具类)
 * @date 2012-12-15 下午11:27:39
 * 
 */
public class ContextContentUtils {

	private static ThreadLocal<HttpServletResponse> reponse_threadLocal = new ThreadLocal<HttpServletResponse>();
	private static ThreadLocal<HttpServletRequest> request_threadLocal = new ThreadLocal<HttpServletRequest>();

	public static HttpServletResponse getResponse() {
		return reponse_threadLocal.get();

	}

	public static void setResponse(HttpServletResponse response) {
		reponse_threadLocal.set(response);
	}

	public static HttpServletRequest getRequest() {
		return request_threadLocal.get();

	}

	public static void setRequest(HttpServletRequest request) {
		request_threadLocal.set(request);
	}

	/**
	 * SpringMvc下获取session
	 * 
	 * @return
	 */
	public static org.apache.shiro.session.Session getSession() {
		Subject subject = SecurityUtils.getSubject();
		return subject.getSession();
	}

	public static void addCookie(String name, int expiry, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
		cookie.setPath("/");
		getResponse().addCookie(cookie);
	}

	public static Cookie getCookie(String name) {
		Cookie cookies[] = getRequest().getCookies();
		if (cookies == null || name == null || name.length() == 0)
			return null;
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	public static void deleteCookie(String name) {
		Cookie cookies[] = getRequest().getCookies();
		if (cookies == null || name == null || name.length() == 0)
			return;
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				cookie.setValue("");
				cookie.setMaxAge(0);
				getResponse().addCookie(cookie);
				return;
			}
		}
	}

	public static void clearCookie() {
		Cookie[] cookies = getRequest().getCookies();
		if (null == cookies)
			return;
		for (Cookie cookie : cookies) {
			cookie.setValue("");
			cookie.setMaxAge(0);
			getResponse().addCookie(cookie);

		}
	}
}
