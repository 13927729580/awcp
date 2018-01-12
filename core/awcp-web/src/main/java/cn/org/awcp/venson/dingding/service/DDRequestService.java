package cn.org.awcp.venson.dingding.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.venson.dingding.URLConstant;
import cn.org.awcp.venson.dingding.helper.AuthHelper;
import cn.org.awcp.venson.dingding.helper.HttpHelper;

public class DDRequestService {

	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(DDRequestService.class);

	private DDRequestService() {
	}

	public static JSONObject getTikect(String access_token) {
		return HttpHelper.httpGet(URLConstant.get_jsapi_ticket + "?access_token=" + access_token);
	}

	public static String getToken(String corpId, String corpSecret) {
		return HttpHelper.httpGet(URLConstant.gettoken + "?corpid=" + corpId + "&corpsecret=" + corpSecret)
				.getString("access_token");
	}

	public static JSONObject getSimpleDeptUser(String accessToken, long departmentId) {
		String url = URLConstant.simple_user_list + "?access_token=" + accessToken + "&department_id=" + departmentId;
		return HttpHelper.httpGet(url);
	}

	public static JSONObject getDeptList(String accessToken) {
		return HttpHelper.httpGet(URLConstant.department_list + "?access_token=" + accessToken);
	}

	public static JSONObject getUserInfo(String accessToken, String code) {
		return HttpHelper.httpGet(URLConstant.getuserinfo + "?access_token=" + accessToken + "&code=" + code);
	}

	public static JSONObject getUserInfoByUId(String accessToken, String userId) {
		return HttpHelper.httpGet(URLConstant.user_get + "?access_token=" + accessToken + "&userid=" + userId);
	}

	public static JSONObject getDeptById(String accessToken, String deptId) {
		return HttpHelper.httpGet(URLConstant.department_get + "?access_token=" + accessToken + "&id=" + deptId);
	}

	public static JSONObject sendMessage(String accessToken, String touser, String toparty, String agentid,
			String msgtype, Object obj) {
		Map<String, Object> params = new HashMap<>();
		params.put("touser", touser);
		params.put("toparty", toparty);
		params.put("agentid", agentid);
		params.put("msgtype", msgtype);
		params.put(msgtype, obj);
		return HttpHelper.httpPost(URLConstant.message_send + "?access_token=" + accessToken, params);
	}

	public static JSONObject getSpace(String accessToken, String domain, String agent_id) {
		String url = URLConstant.get_custom_space + "?access_token=" + accessToken;
		if (domain != null) {
			url += "&domain=" + domain;
		}
		if (agent_id != null) {
			url += "&agent_id=" + agent_id;
		}
		return HttpHelper.httpGet(url);
	}

	public static JSONObject grantSpace(String accessToken, String domain, String agent_id, String type, String userid,
			String path, String fileids, String duration) {

		String url = URLConstant.grant_custom_space + "?access_token=" + accessToken + "&domain=" + domain + "&type="
				+ type + "&userid=" + userid;
		if (duration != null) {
			url += "&duration=" + duration;
		}
		if (fileids != null) {
			url += "&fileids=" + fileids;
		}
		if (agent_id != null) {
			url += "&agent_id=" + agent_id;
		}
		if (path != null) {
			try {
				path = URLEncoder.encode(path, "utf-8");
				url += "&path=" + path;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return HttpHelper.httpGet(url);
	}

	public static void main(String[] args) {
		// JSONObject result = DDRequestService.getSpace(AuthHelper.getAccessToken(),
		// "attachment", null);
		JSONObject result1 = DDRequestService.grantSpace(AuthHelper.getAccessToken(), "attachment", null, "download",
				"085322151226274156", "/", "666368082", "300");
		// System.out.println(result);
		System.out.println(result1);
	}

}
