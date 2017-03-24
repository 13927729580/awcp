package org.szcloud.framework.base;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.MDC;

public class LogbackMDCFilter implements Filter {
	private final String USER_KEY = "sessionId";

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		boolean successfulRegistration = false;
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		if(session != null){
			successfulRegistration = registerUsername(req.getSession().getId());
		}
//		if(MDC.get(USER_KEY) == null || MDC.get(USER_KEY).isEmpty()) {
//			successfulRegistration = registerUsername(req.getSession().getId());
////			MDC.put(USER_KEY, req.getSession().getId());
//		}
		try {
			chain.doFilter(request, response);
		} finally {
			if (successfulRegistration) {
				MDC.remove(USER_KEY);
			}
		}

	}
	private boolean registerUsername(String sessionId) {
		if (sessionId != null && sessionId.trim().length() > 0) {
			MDC.put(USER_KEY, sessionId);
			return true;
		}
		return false;
	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
