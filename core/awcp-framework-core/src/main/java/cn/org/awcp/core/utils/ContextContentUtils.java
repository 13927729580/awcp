package cn.org.awcp.core.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @ClassName: ContextHolderUtils
 * @Description: TODO(上下文工具类)
 * @author venson
 * @date 2012-12-15 下午11:27:39
 * 
 */
public class ContextContentUtils {

	private static final ThreadLocal<HttpServletResponse> RESPONSE_THREAD_LOCAL = new ThreadLocal<>();
	private static final ThreadLocal<HttpServletRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

	public static HttpServletResponse getResponse() {
		return RESPONSE_THREAD_LOCAL.get();

	}

	public static void setResponse(HttpServletResponse response) {
		RESPONSE_THREAD_LOCAL.set(response);
	}

	public static HttpServletRequest getRequest() {
		return REQUEST_THREAD_LOCAL.get();

	}

	public static void setRequest(HttpServletRequest request) {
		REQUEST_THREAD_LOCAL.set(request);
	}

	public static void removeRequest(){
		REQUEST_THREAD_LOCAL.remove();
	}

	public static void removeResponse(){
		RESPONSE_THREAD_LOCAL.remove();
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
