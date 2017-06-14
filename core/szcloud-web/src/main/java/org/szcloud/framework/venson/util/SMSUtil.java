package org.szcloud.framework.venson.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.szcloud.framework.formdesigner.engine.util.HttpClientUtil;

/**
 * Created by venson on 2017/01/12.
 */
public class SMSUtil {
	private static String username = null;
	private static String password = null;
	private static String name = null;

	private SMSUtil() {
	}

	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(SMSUtil.class);
	/*
	 * 获取配置信息
	 */
	static {
		InputStream in = SMSUtil.class.getClassLoader().getResourceAsStream("conf/smsBao.properties");
		BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
		Properties p = new Properties();
		try {
			p.load(bfr);
			username = p.getProperty("smsuserName");
			password = p.getProperty("smsPassword");
			name = p.getProperty("smsCompany");
		} catch (IOException e) {
			logger.info("ERROR", e);
		}
	}

	/***
	 * 发送短信验证码
	 * 
	 * @param iphone
	 *            发送的手机号
	 * @throws UnsupportedEncodingException
	 */
	public static String send(String iphone) throws UnsupportedEncodingException {
		String randomVcode = createRandomVcode(iphone);
		String content = "【" + name + "】验证码：" + randomVcode + "，如非本人操作，请忽略本短信。"; // 注意测试时，也请带上公司简称或网站签名，发送正规内容短信。千万不要发送无意义的内容：例如
																					// 测一下、您好。否则可能会收不到
		StringBuffer httpArg = new StringBuffer("http://api.smsbao.com/sms?");
		httpArg.append("u=").append(username).append("&");
		httpArg.append("p=").append(MD5Util.getMD5String(password)).append("&");
		httpArg.append("m=").append(iphone).append("&");
		httpArg.append("c=").append(java.net.URLEncoder.encode(content, "UTF-8"));
		String result = HttpClientUtil.get(httpArg.toString());
		logger.debug(result);
		return randomVcode;
	}

	/***
	 * 随机生成6位验证码
	 * 
	 * @param phone
	 *            手机号
	 * @return
	 */
	public static String createRandomVcode(String phone) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			builder.append((int) (Math.random() * 9));
		}
		String result = builder.toString();
		logger.info(result);
		return result;
	}

}
