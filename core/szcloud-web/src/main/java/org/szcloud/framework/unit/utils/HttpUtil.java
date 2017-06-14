package org.szcloud.framework.unit.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtil {
	

	/**
	 * 日志对象
	 */
	protected static Log logger = LogFactory.getLog(HttpUtil.class);
	
	protected static JsonFactory jsonFactory = new JsonFactory();
	
	/**
	 * 向Response中写入数据；
	 * @param response
	 * @param data
	 */
	public static void writeDataToResponse(HttpServletResponse response, Object obj){
		try {
			String data = jsonFactory.encode2Json(obj);
			response.setContentType("application/json; charset=UTF-8");
			if (data != null || !"".equals(data)) {
				response.getWriter().write(data);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 向Response中写入数据；
	 * @param response
	 * @param data
	 */
	public static void writeDataToResponse(HttpServletResponse response, String data){
		try {
			response.setContentType("application/json; charset=UTF-8");
			if (data != null || !"".equals(data)) {
				response.getWriter().write(data);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
