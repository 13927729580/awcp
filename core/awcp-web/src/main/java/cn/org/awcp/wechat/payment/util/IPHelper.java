package cn.org.awcp.wechat.payment.util;

import javax.servlet.http.HttpServletRequest;

public class IPHelper {
	
	/**
	 * 得到用户的真实地址,如果有多个就取第一个
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		String[] ips = ip.split(",");
		ip = ips[0].trim();
		return "0:0:0:0:0:0:0:1".equals(ip) ?"127.0.0.1":ip;
	}

}
