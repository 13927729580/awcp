package cn.org.awcp.wechat.util;

import cn.org.awcp.wechat.domain.AccessToken;
import cn.org.awcp.wechat.domain.JsapiTicket;
import cn.org.awcp.wechat.domain.menu.WeChatMenu;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.ParseException;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static cn.org.awcp.wechat.util.Constant.APPID;
import static cn.org.awcp.wechat.util.Constant.APPSECRET;

/**
 * 
 * @author yqtao
 *
 */
public class WeChatUtil {

	private static Logger logger = LoggerFactory.getLogger(WeChatUtil.class);

	/**
	 * 验证签名
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 将token,timestamp,nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		String tmpStr = (new Sha1Hash(content.toString())).toString();
		logger.info("微信服务器传过来的signature为:" + signature);
		logger.info("token,timestamp,nonce处理后结果为:" + tmpStr);

		// 将sha1加密后的字符串可与signature对比,标识该请求来源于微信(不区分大小写)
		return tmpStr != null ? tmpStr.equalsIgnoreCase(signature) : false;
	}

	/**
	 * 获取js签名参数
	 * 
	 * @param url
	 * @return Map
	 */
	public static Map<String, Object> getJsSignature(String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		String noncestr = RandomStringUtils.randomAlphanumeric(16);
		long timestamp = System.currentTimeMillis();
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
		treeMap.put("noncestr", noncestr);
		treeMap.put("timestamp", timestamp);
		treeMap.put("jsapi_ticket", JsapiTicket.getTicket());
		treeMap.put("url", url);
		StringBuffer sb = new StringBuffer();
		for (String key : treeMap.keySet()) {
			sb.append(key + "=" + treeMap.get(key) + "&");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		map.put("timestamp", timestamp);
		map.put("nonceStr", noncestr);
		map.put("appid", APPID);
		map.put("signature", new Sha1Hash(sb.toString()).toString());
		return map;
	}

	/**
	 * 获取基础接口access_token
	 * 
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String getAccessToken() {
		// 请求url,get请求
		String requestUrl = ConstantURL.GET_ACCESSTOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		try {
			String responseContent = RequestUtil.doGet(requestUrl);
			if (responseContent != null) { // 如果请求成功
				return JSONObject.parseObject(responseContent).getString("access_token");
			}
		} catch (Exception e) {
			logger.info("获取token失败 ");
		}
		return "";
	}

	/**
	 * 获取jsapi_ticket
	 */
	public static String getJsapiTicket() {
		String accessToken = AccessToken.getAccessToken();
		if (accessToken != null) {
			String requestUrl = ConstantURL.GET_JSAPI_TICKET_URL.replace("ACCESS_TOKEN", AccessToken.getAccessToken());
			try {
				String responseContent = RequestUtil.doGet(requestUrl);
				if (responseContent != null) { // 如果请求成功
					JSONObject jsonObject = JSONObject.parseObject(responseContent);
					return jsonObject.getString("ticket");
				}
			} catch (Exception e) {
				logger.info("获取ticket失败");
			}
		}
		return "";
	}

	/**
	 * 获取微信服务器IP地址
	 * 
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static List<String> getServerIp() {
		List<String> ipList = new ArrayList<String>();
		// 请求url,get请求
		String requestUrl = ConstantURL.GET_SERVER_IP_URL.replace("ACCESS_TOKEN", AccessToken.getAccessToken());
		try {
			String responseContent = RequestUtil.doGet(requestUrl);
			// 如果请求成功
			if (responseContent != null) {
				JSONObject jsonObject = JSONObject.parseObject(responseContent);
				String str = jsonObject.getString("ip_list");
				ipList = JsonFactory.parseList(str, String.class);
				logger.info("获取的server ip为：" + ipList);
			}
		} catch (Exception e) {
			logger.info("获取server ip 失败.");
		}
		return ipList;
	}

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 */
	public static void createMenu(WeChatMenu menu) {
		String json = JsonFactory.encode2Json(menu);
		String requestUrl = ConstantURL.CREATE_MENU_URL.replace("ACCESS_TOKEN", AccessToken.getAccessToken());
		try {
			String responseContent = RequestUtil.doPost(requestUrl, json);
			if (responseContent != null) { // 如果请求成功
				JSONObject jsonObject = JSONObject.parseObject(responseContent);
				String errmsg = jsonObject.getString("errmsg");
				if ("ok".equals(errmsg)) {
					logger.info("创建菜单成功");
				} else {
					logger.info("创建菜单失败");
				}
			}
		} catch (Exception e) {
			logger.info("创建菜单失败");
			logger.info(e.getMessage());
		}
	}

	/**
	 * 查询菜单
	 * 
	 * @return
	 */
	public static WeChatMenu getMenu() {
		String requestUrl = ConstantURL.GET_MENU_URL.replace("ACCESS_TOKEN", AccessToken.getAccessToken());
		WeChatMenu menu = null;
		try {
			String responseContent = RequestUtil.doGet(requestUrl);
			if (responseContent != null) {
				JSONObject menuObject = JSONObject.parseObject(responseContent);
				menu = menuObject.getObject("menu", WeChatMenu.class);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return menu;
	}

	/**
	 * 删除菜单
	 * 
	 * @return
	 */
	public static void delMenu() {
		String requestUrl = ConstantURL.DEL_MENU_URL.replace("ACCESS_TOKEN", AccessToken.getAccessToken());
		try {
			String responseContent = RequestUtil.doGet(requestUrl);
			if (responseContent != null) {
				JSONObject jsonObject = JSONObject.parseObject(responseContent);
				String errmsg = jsonObject.getString("errmsg");
				logger.info(errmsg);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * 通过网页授权获取openid
	 * 
	 * @param code
	 * @return
	 */
	public static String getOpenid(String code) {
		String requestUrl = ConstantURL.GET_WEB_ACCESS_TOKEN.replace("APPID", APPID).replace("SECRET", APPSECRET)
				.replace("CODE", code);
		try {
			String responseContent = RequestUtil.doGet(requestUrl);
			if (responseContent != null) { // 如果请求成功
				JSONObject jsonObject = JSONObject.parseObject(responseContent);
				String openid = jsonObject.getString("openid");
				logger.info("openid:" + openid);
				return openid;
			}
		} catch (Exception e) {
			logger.info("获取token失败 ");
		}
		return "";
	}

	/**
	 * 通过openid获取用户信息
	 * 
	 * @param openid
	 * @return
	 */
	public static Map<String, Object> getUserInfoByOpenid(String openid) {
		Map<String, Object> map = new HashMap<String, Object>();
		String requestUrl = ConstantURL.GET_USER_INFO.replace("OPENID", openid).replace("ACCESS_TOKEN",
				AccessToken.getAccessToken());
		try {
			String responseContent = RequestUtil.doGet(requestUrl);
			if (responseContent != null) { // 如果请求成功
				map = JsonFactory.parseMap(responseContent);
			}
		} catch (Exception e) {
			logger.info("获取userInfo失败 ");
		}

		return map;
	}
}
