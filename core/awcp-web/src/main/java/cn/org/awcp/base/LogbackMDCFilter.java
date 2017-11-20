package cn.org.awcp.base;

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

	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		boolean successfulRegistration = false;
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		if(session != null){
			successfulRegistration = registerUsername(req.getSession().getId());
		}
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

}
