package cn.org.awcp.wechat.util;

/**
 * 常量,在微信公众品台的开发中的基本设置中
 * @author yqtao
 *
 */
public class ConstantURL {
	
	/**
	 * 获取ACCESS_TOKEN接口
	 */
	public static String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?" +
											   "grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	/**
	 * 获取jsapi_ticket接口
	 */
	public static String GET_JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?"
			+ "access_token=ACCESS_TOKEN&type=jsapi";
	
	/**
	 * 获取微信服务器IP地址接口
	 */
	public static String GET_SERVER_IP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单接口
	 */
	public static String CREATE_MENU_URL = " https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	/**
	 * 获取菜单接口
	 */
	public static String GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	
	/**
	 * 删除菜单接口
	 */
	public static String DEL_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	/**
	 * 获取多媒体信息
	 */
	public static String DOWN_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?"
			+ "access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	
	/**
	 * 创建分组接口	POST
	 */
	public static String CREATE_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
	
	/**
	 * 查询所有分组接口	GET
	 */
	public static String GET_GROUPS_URL = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
	
	/**
	 * 查询用户所在的分组	POST
	 */
	public static String GET_GROUP_ID_BY_USER_URL =	"https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";
	
	/**
	 * 修改分组名称	POST
	 */
	public static String UPDATE_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
	
	
	/**
	 * 移动用户的分组	POST
	 */
	public static String MOVE_USER_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
	
	/**
	 * 批量移动用户的分组	POST
	 */
	public static String MOVE_USERS_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?"
			+ "access_token=ACCESS_TOKEN";
	
	/**
	 * 删除分组	POST
	 */
	public static String DELETE_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=ACCESS_TOKEN";
	
	/**
	 * 修改用户备注	POST
	 */
	public static String UPDATE_USER_REMARK_URL = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?"
			+ "access_token=ACCESS_TOKEN";
	
	/**
	 * 获取用户的基本信息	GET
	 */
	public static String GET_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?"
			+ "access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	
	/**
	 * 批量获取用户基本信息	POST
	 * 最多100条
	 */
	public static String GET_USERS_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";
	
	/**
	 * 获取用户列表	GET
	 * 最多10000条
	 * 超过用下一个URL
	 */
	public static String GET_USERS_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
	public static String GET_NEXT_USERS_URL = "https://api.weixin.qq.com/cgi-bin/user/get?"
			+ "access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
	
	/**
	 * 网页授权获取code
	 */
	public static String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID"
			+ "&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	
	/**
	 * 通过code获取网页授权的access_token
	 */
	public static String GET_WEB_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID"
			+ "&secret=SECRET&code=CODE&grant_type=authorization_code";
	
	/**
	 * GET
	 * 检验授权凭证(access_token)是否有效
	 */
	public static String CHECK_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
		
	/**
	 * 由于access_token拥有较短的有效期,当access_token超时后,可以使用refresh_token进行刷新,
	 * refresh_token拥有较长的有效期（7天、30天、60天、90天）,当refresh_token失效的后,需要用户重新授权.
	 */
	public static String REFRESH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID"
			+ "&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	
	/**
	 * GET
	 * 如果网页授权作用域为snsapi_userinfo,则此时开发者可以通过access_token和openid拉取用户信息了.
	 */
	public static String GET_SNS_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN"
			+ "&openid=OPENID&lang=zh_CN";
	
	/**
	 * GET
	 * 通过openid获取用户的信息
	 */
	public static String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN"
			+ "&openid=OPENID&lang=zh_CN";
	
	/**
	 * 根据OpenID列表群发
	 */
	public static String POST_SEND_MSG = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
		
}
