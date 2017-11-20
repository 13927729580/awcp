package cn.org.awcp.venson.dingding.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.venson.dingding.Env;
import cn.org.awcp.venson.dingding.exception.OApiException;
import cn.org.awcp.venson.dingding.exception.OApiResultException;
import cn.org.awcp.venson.dingding.service.DDRequestService;
import cn.org.awcp.venson.util.LocalStorage;

public class AuthHelper {

	private static final Log logger = LogFactory.getLog(AuthHelper.class);
	// 调整到1小时50分钟
	public static final long cacheTime = 1000 * 60 * 50 * 2;

	/*
	 * 在此方法中，为了避免频繁获取access_token， 在距离上一次获取access_token时间在两个小时之内的情况，
	 * 将直接从持久化存储中读取access_token
	 * 
	 * 因为access_token和jsapi_ticket的过期时间都是7200秒 所以在获取access_token的同时也去获取了jsapi_ticket
	 * 注：jsapi_ticket是在前端页面JSAPI做权限验证配置的时候需要使用的 具体信息请查看开发者文档--权限验证配置
	 */
	public static String getAccessToken() {
		return getDDAccess().getString("access_token");
	}

	public synchronized static JSONObject getDDAccess() {
		long curTime = System.currentTimeMillis();
		JSONObject access = LocalStorage.get(Env.CORP_ID, JSONObject.class);
		if (access == null || curTime - access.getLong("begin_time") >= cacheTime) {
			access = getToken(curTime);
		}
		return access;

	}

	private static JSONObject getToken(long curTime) {
		String accToken = DDRequestService.getToken(Env.CORP_ID, Env.CORP_SECRET);
		JSONObject jsontemp = new JSONObject();
		jsontemp.put("access_token", accToken);
		jsontemp.put("begin_time", curTime);
		String jsTicket = DDRequestService.getTikect(accToken).getString("ticket");
		jsontemp.put("ticket", jsTicket);
		LocalStorage.set(Env.CORP_ID, jsontemp);
		return jsontemp;
	}

	// 正常的情况下，jsapi_ticket的有效期为7200秒，所以开发者需要在某个地方设计一个定时器，定期去更新jsapi_ticket
	public static String getJsapiTicket() {
		return getDDAccess().getString("ticket");
	}

	public static String sign(String ticket, String nonceStr, long timeStamp, String url) throws OApiException {
		String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
				+ "&url=" + url;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			return bytesToHex(sha1.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new OApiResultException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new OApiResultException(e.getMessage());
		}
	}

	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public static String getConfig(HttpServletRequest request) {
		String urlString = request.getRequestURL().toString();
		String queryString = request.getQueryString();

		String queryStringEncode = null;
		String url;
		if (queryString != null) {
			try {
				queryStringEncode = URLDecoder.decode(queryString, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("ERROR", e);
			}
			url = urlString + "?" + queryStringEncode;
		} else {
			url = urlString;
		}
		return getConfig(url,
				StringUtils.defaultString(request.getParameter(Env.PARAM_AGENT_ID_NAME), Env.DEFAULT_AGENT_ID));
	}

	public static String getConfig(String url, String agentId) {

		long timeStamp = System.currentTimeMillis() / 1000;
		String signedUrl = url;
		String ticket = null;
		String signature = null;
		String agentid = null;

		try {

			ticket = AuthHelper.getJsapiTicket();
			signature = AuthHelper.sign(ticket, Env.NONCE, timeStamp, signedUrl);
			agentid = agentId;

		} catch (OApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String configValue = "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + Env.NONCE
				+ "',timeStamp:'" + timeStamp + "',corpId:'" + Env.CORP_ID + "',agentid:'" + agentid + "'}";
		logger.debug(configValue);
		return configValue;
	}

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i < 9; i++) {
			service.execute(new Runnable() {

				@Override
				public void run() {
					logger.debug(Thread.currentThread().getName() + "，accToken:" + AuthHelper.getAccessToken()
							+ ",ticket:" + AuthHelper.getJsapiTicket());
				}
			});
		}
		service.shutdown();
	}
}
