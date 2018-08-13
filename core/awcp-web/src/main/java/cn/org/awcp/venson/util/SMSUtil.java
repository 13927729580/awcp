package cn.org.awcp.venson.util;

import java.io.UnsupportedEncodingException;

import cn.org.awcp.core.utils.Security;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static cn.org.awcp.venson.common.SC.SMS_CODE_SALT;


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


	public static String send(String phone,String content) {
		String randomVcode = createRandomVcode();
		if(StringUtils.isBlank(content)){
			content = "【" + PlatfromProp.getValue("smsCompany") + "】验证码：" + randomVcode + "，如非本人操作，请忽略本短信。";
		}
		StringBuffer httpArg = new StringBuffer("http://api.smsbao.com/sms?");
		httpArg.append("u=").append(PlatfromProp.getValue("smsuserName")).append("&");
		httpArg.append("p=").append(MD5Util.getMD5String(PlatfromProp.getValue("smsPassword"))).append("&");
		httpArg.append("m=").append(phone).append("&");
		try {
			httpArg.append("c=").append(java.net.URLEncoder.encode(content, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.debug("ERROR",e);
		}
		HttpUtils.sendGet(httpArg.toString());
		logger.debug("SMSCode------------------------" + randomVcode);
		return randomVcode;
	}

	/***
	 * 发送短信验证码
	 *
	 * @param phone
	 *            发送的手机号
	 */
	public static String send(String phone) {
		return send(phone,null);
	}

	/***
	 * 随机生成6位验证码
	 * 
	 * @return
	 */
	public static String createRandomVcode() {
		return String.valueOf((int)((Math.random() * 9+1)*100000));
	}

}
