package cn.org.awcp.venson.dingding.helper;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.venson.dingding.Env;
import cn.org.awcp.venson.dingding.exception.OApiException;

public class EventChangeHelper {
	// 注册事件回调接口
	public static String registerEventChange(String accessToken, List<String> callBackTag, String token, String aesKey,
			String url) throws OApiException {
		String signUpUrl = Env.OAPI_HOST + "/call_back/register_call_back?" + "access_token=" + accessToken;
		JSONObject args = new JSONObject();
		args.put("call_back_tag", callBackTag);
		args.put("token", token);
		args.put("aes_key", aesKey);
		args.put("url", url);

		JSONObject response = HttpHelper.httpPost(signUpUrl, args);
		if (response.containsKey("errcode")) {
			return response.getString("errcode");
		} else {
			return null;
		}
	}

	// 查询事件回调接口
	public static String getEventChange(String accessToken) throws OApiException {
		String url = Env.OAPI_HOST + "/call_back/get_call_back?" + "access_token=" + accessToken;
		JSONObject response = HttpHelper.httpGet(url);
		return response.toString();
	}

	// 更新事件回调接口
	public static String updateEventChange(String accessToken, List<String> callBackTag, String token, String aesKey,
			String url) throws OApiException {
		String signUpUrl = Env.OAPI_HOST + "/call_back/update_call_back?" + "access_token=" + accessToken;
		JSONObject args = new JSONObject();
		args.put("call_back_tag", callBackTag);
		args.put("token", token);
		args.put("aes_key", aesKey);
		args.put("url", url);

		JSONObject response = HttpHelper.httpPost(signUpUrl, args);
		if (response.containsKey("errcode")) {
			return response.getString("errcode");
		} else {
			return null;
		}
	}

	// 删除事件回调接口
	public static String deleteEventChange(String accessToken) throws OApiException {
		String url = Env.OAPI_HOST + "/call_back/delete_call_back?" + "access_token=" + accessToken;
		JSONObject response = HttpHelper.httpGet(url);
		return response.toString();
	}

	public static String getFailedResult(String accessToken) throws OApiException {
		String url = Env.OAPI_HOST + "/call_back/get_call_back_failed_result?" + "access_token=" + accessToken;
		JSONObject response = HttpHelper.httpGet(url);
		return response.toString();
	}

	public static void main(String[] args) {
		try {
			// 此处要注意JDK的证书要去下载
			// JDK8证书下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
			// 下载后将证书替换JAVA_HOME/jre/lib/security下的文件
			System.out.println(AuthHelper.getAccessToken());
			String result = EventChangeHelper.updateEventChange(AuthHelper.getAccessToken(),
					Arrays.asList("user_add_org", "user_modify_org", "user_leave_org", "org_dept_create",
							"org_dept_modify", "org_dept_remove"),
					Env.TOKEN, Env.ENCODING_AES_KEY, "http://119.29.226.152:8083/awcp/dingding/eventChangeReceive.do");
			System.out.println(result);
		} catch (OApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
