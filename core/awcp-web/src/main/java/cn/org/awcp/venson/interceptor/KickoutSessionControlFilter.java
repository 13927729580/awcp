package cn.org.awcp.venson.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.util.CookieUtil;

/**
 * 只保留一个在线用户 <br>
 * 类说明:增加、查找、删除cookie
 * 
 * @author venson
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

	private String kickoutUrl; // 踢出后到的地址
	private boolean kickoutAfter = false; // 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
	private int maxSession = 1; // 同一个帐号最大会话数 默认1

	private SessionManager sessionManager;
	private Cache<String, Deque<Serializable>> cache;

	public void setKickoutUrl(String kickoutUrl) {
		this.kickoutUrl = kickoutUrl;
	}

	public void setKickoutAfter(boolean kickoutAfter) {
		this.kickoutAfter = kickoutAfter;
	}

	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cache = cacheManager.getCache("shiroCache");
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			// 如果没有登录，直接进行之后的流程
			return true;
		}

		Session session = subject.getSession();
		String userAccount = CookieUtil.findCookie(SC.USER_ACCOUNT);
		Serializable sessionId = session.getId();

		// TODO 同步控制
		Deque<Serializable> deque = cache.get(userAccount);
		if (deque == null) {
			deque = new LinkedList<Serializable>();
			cache.put(userAccount, deque);
		}

		// 如果队列里没有此sessionId，且用户没有被踢出；放入队列
		if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
			deque.push(sessionId);
		}

		// 如果队列里的sessionId数超出最大会话数，开始踢人
		while (deque.size() > maxSession) {
			Serializable kickoutSessionId = null;
			if (kickoutAfter) { // 如果踢出后者
				kickoutSessionId = deque.removeFirst();
			} else { // 否则踢出前者
				kickoutSessionId = deque.removeLast();
			}
			try {
				Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
				if (kickoutSession != null) {
					// 设置会话的kickout属性表示踢出了
					kickoutSession.setAttribute("kickout", true);
				}
			} catch (Exception e) {// ignore exception
			}
		}

		// 如果被踢出了，直接退出，重定向到踢出后的地址
		if (session.getAttribute("kickout") != null) {
			CookieUtil.deleteCookie(SC.USER_ACCOUNT);
			CookieUtil.deleteCookie(SC.SECRET_KEY);
			// 会话被踢出了
			try {
				subject.logout();
			} catch (Exception e) { // ignore
			}
			tologin(request, response);
			return false;
		}

		return true;
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
			WebUtils.issueRedirect(request, response, kickoutUrl);
		}
	}

}
