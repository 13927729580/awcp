package cn.org.awcp.venson.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;

import cn.org.awcp.venson.controller.base.ControllerContext;

/**
 * cookie操作类 <br>
 * 类说明:增加、查找、删除cookie
 */
public class CookieUtil {
	private static String default_path = "/";
	private static int default_age = 7 * 24 * 60 * 60;

	private CookieUtil() {
	}

	// 设置age
	public static void addCookie(String name, String value, int age) {
		try {
			value = URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(age);
		cookie.setPath(default_path);
		ControllerContext.getResponse().addCookie(cookie);

	}

	// 默认的
	public static void addCookie(String name, String value) {
		addCookie(name, value, default_age);

	}

	public static String findCookie(String name) {
		Cookie[] cookies = ControllerContext.getRequest().getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals(name)) {
					try {
						return URLDecoder.decode(cookie.getValue(), "utf-8");
					} catch (UnsupportedEncodingException e) {
					}

				}
			}
		}
		return null;

	}

	public static void deleteCookie(String name) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		cookie.setPath(default_path);
		ControllerContext.getResponse().addCookie(cookie);

	}

}
