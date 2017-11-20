package cn.org.awcp.venson.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.org.awcp.formdesigner.engine.util.HttpClientUtil;

/**
 * Created by venson on 2017/01/12.
 */
public class SMSUtil {

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
		return randomVcode;
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

}
