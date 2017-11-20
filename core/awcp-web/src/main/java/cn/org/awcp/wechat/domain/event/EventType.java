package cn.org.awcp.wechat.domain.event;

public class EventType {
	/**
	 * 自定义菜单点击事件
	 */
	public static String CLICK = "CLICK";
	
	/**
	 * 点击菜单跳转链接时的事件
	 */
	public static String VIEW = "VIEW";
	
	/**
	 * 订阅事件
	 */
	public static String SUBSCRIBE = "subscribe";
	
	/**
	 * 取消订阅事件
	 */
	public static String UNSUBSCRIBE = "unsubscribe";
	
	/**
	 * 如果用户已经关注公众号,扫描事件
	 */
	public static String SCAN = "SCAN";
	
	/**
	 * 上报地理位置事件
	 */
	public static String LOCATION = "LOCATION";
}
