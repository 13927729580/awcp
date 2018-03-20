package cn.org.awcp.dingding;

public class URLConstant {
	/**
	 * 获取gettoken
	 */
	public static final String gettoken = Env.OAPI_HOST + "/gettoken";
	/**
	 * 获取ticket
	 */
	public static final String get_jsapi_ticket = Env.OAPI_HOST + "/get_jsapi_ticket";
	/**
	 * 获取所有部门
	 */
	public static final String department_list = Env.OAPI_HOST + "/department/list";
	/**
	 * 获取部门下的所有用户
	 */
	public static final String user_list = Env.OAPI_HOST + "/user/list";
	/**
	 * 获取部门下的所有用户
	 */
	public static final String simple_user_list = Env.OAPI_HOST + "/user/simplelist";
	/**
	 * 根据code获取用户信息
	 */
	public static final String getuserinfo = Env.OAPI_HOST + "/user/getuserinfo";
	/**
	 * 消息推送
	 */
	public static final String message_send = Env.OAPI_HOST + "/message/send";
	/**
	 * 根据用户Id获取用户信息
	 */
	public static final String user_get = Env.OAPI_HOST + "/user/get";
	/**
	 * 根据部门Id获取部门信息
	 */
	public static final String department_get = Env.OAPI_HOST + "/department/get";
	/**
	 * 获取钉盘分配空间ID
	 */
	public static final String get_custom_space = Env.OAPI_HOST + "/cspace/get_custom_space";
	/**
	 * 授权用户访问钉盘
	 */
	public static final String grant_custom_space = Env.OAPI_HOST + "/cspace/grant_custom_space";
}
