package cn.jflow.common.util;

import cn.org.awcp.core.utils.ContextContentUtils;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.Springfactory;
import org.apache.shiro.session.Session;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * JFlow上下文工具类
 * @version 2016-5-9
 */
public class ContextHolderUtils{

	private static ContextHolderUtils contextHolder;

	// 数据源设置
	private DataSource dataSource;
	
	// 第三方系统session中的用户编码，设置后将不用再调用登录方法，直接获取当前session进行登录。
	private String userNoSessionKey;

	public static ContextHolderUtils getInstance() {
		if(contextHolder == null){
			synchronized (ContextHolderUtils.class){
				if (contextHolder == null) {
					contextHolder = Springfactory.getBean(ContextHolderUtils.class);
				}
			}
		}
		return contextHolder;
	}

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
	 * @return
	 */
	public static Session getSession() {
//		java.util.Enumeration e = session.getAttributeNames();
//		while (e.hasMoreElements()) {
//			String name = (String) e.nextElement();
//			String value = session.getAttribute(name).toString();
//			System.out.println(name + " = " + value);
//		}
		return SessionUtils.getCurrentSession();
	}
	private static Pattern p =Pattern.compile("[\u4e00-\u9fa5]");
	public static void addCookie(String name, int expiry, String value) {
		if(p.matcher(value).find()){
			try {
				ContextContentUtils.addCookie(name,expiry, URLEncoder.encode(value, "UTF-8"));
			}catch (UnsupportedEncodingException e){
			}
		}else{
			ContextContentUtils.addCookie(name,expiry,value);
		}
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

	/**
	 * 获取数据源
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 获取数据源
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 获取第三方系统session中的用户编码
	 * @return
	 */
	public String getUserNoSessionKey() {
		return userNoSessionKey;
	}

	/**
	 * 设置第三方系统session中的用户编码
	 * @param userNoSessionKey
	 */
	public void setUserNoSessionKey(String userNoSessionKey) {
		this.userNoSessionKey = userNoSessionKey;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return Springfactory.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return Springfactory.getBean(requiredType);
	}



}
