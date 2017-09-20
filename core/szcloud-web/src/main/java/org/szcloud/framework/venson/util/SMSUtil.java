package org.szcloud.framework.venson.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.szcloud.framework.formdesigner.engine.util.HttpClientUtil;
import org.szcloud.framework.unit.utils.EncryptUtils;

/**
 * Created by venson on 2017/01/12.
 */
public class SMSUtil {

	public static Map<String, Date> MAP = new HashMap<String, Date>();

	private SMSUtil() {
	}

	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(SMSUtil.class);

	/***
	 * 发送短信验证码
	 * 
	 * @param iphone
	 *            发送的手机号
	 * @throws UnsupportedEncodingException
	 */
	public static String send(String phone) {
		Date date = MAP.get(phone);
		// 一分钟内不再发送同一手机号
		if (date == null || new Date().getTime() >= (date.getTime() + 1000 * 59)) {
			String randomVcode = createRandomVcode();
			String content = "【" + PlatfromProp.getValue("smsCompany") + "】验证码：" + randomVcode + "，如非本人操作，请忽略本短信。"; // 注意测试时，也请带上公司简称或网站签名，发送正规内容短信。千万不要发送无意义的内容：例如
			// 测一下、您好。否则可能会收不到
			StringBuffer httpArg = new StringBuffer("http://api.smsbao.com/sms?");
			httpArg.append("u=").append(PlatfromProp.getValue("smsuserName")).append("&");
			httpArg.append("p=").append(MD5Util.getMD5String(PlatfromProp.getValue("smsPassword"))).append("&");
			httpArg.append("m=").append(phone).append("&");
			try {
				httpArg.append("c=").append(java.net.URLEncoder.encode(content, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
			HttpClientUtil.get(httpArg.toString());
			logger.debug("SMSCode------------------------" + randomVcode);
			MAP.put(phone, new Date());
			return randomVcode;
		} else {
			new ClearSMS(phone).clear();
			return null;
		}
	}

	static class ClearSMS {
		private static Set<String> Store = new HashSet<>();
		private String id;

		public ClearSMS(String id) {
			this.id = id;
		}

		public void clear() {
			if (!Store.contains(id)) {
				Store.add(id);
				new Thread(() -> {
					try {
						Thread.sleep(1000 * 60 * 10);
					} catch (InterruptedException e) {
					}
					SMSUtil.MAP.remove(id);
					Store.remove(id);
				}).start();
			}
		}
	}

	/***
	 * 随机生成6位验证码
	 * 
	 * @param phone
	 *            手机号
	 * @return
	 */
	public static String createRandomVcode() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			builder.append((int) (Math.random() * 9));
		}
		String result = builder.toString();
		return result;
	}

	public static void main(String[] args) {
		System.out.println(EncryptUtils.decript("66acb3e05a08bc624d2a810a7225e563f5c347db403518a7397a7c6f31077b4e"));
	}

}
