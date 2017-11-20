package cn.org.awcp.wechat.service;

import java.util.Map;

public interface Handler {

	/**
	 * 处理文本消息
	 * @param requestMap
	 * @return String
	 */
	String onText(Map<String,String> requestMap);
	
	/**
	 * 处理事件消息
	 * @param requestMap
	 * @return String
	 */
	String onEvent(Map<String,String> requestMap);
	
	/**
	 * 处理图片消息
	 * @param requestMap
	 * @return String
	 */
	String onImage(Map<String,String> requestMap);
	
	/**
	 * 处理地理消息
	 * @param requestMap
	 * @return String
	 */
	String onLocation(Map<String,String> requestMap);
	
}
