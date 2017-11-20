package TL;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.org.awcp.core.utils.ContextContentUtils;

/**
 * @ClassName: ContextHolderUtils
 * @Description: TODO(上下文工具类)
 * @date 2012-12-15 下午11:27:39
 * 
 */
public class ContextHolderUtils {

	private static ThreadLocal<HttpServletResponse> reponse_threadLocal = new ThreadLocal<HttpServletResponse>();

	/**
	 * SpringMvc下获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ContextContentUtils.getRequest();

	}

	public static HttpServletResponse getResponse() {
		return ContextContentUtils.getResponse();

	}

	/**
	 * SpringMvc下获取session
	 * 
	 * @return
	 */
	public static org.apache.shiro.session.Session getSession() {
		return ContextContentUtils.getSession();
	}

	public static void addCookie(String name, int expiry, String value) {
		ContextContentUtils.addCookie(name, expiry, value);
	}

	public static Cookie getCookie(String name) {
		return ContextContentUtils.getCookie(name);
	}

	public static void deleteCookie(String name) {
		ContextContentUtils.deleteCookie(name);
	}

	public static void clearCookie() {
		ContextContentUtils.clearCookie();
	}
}
