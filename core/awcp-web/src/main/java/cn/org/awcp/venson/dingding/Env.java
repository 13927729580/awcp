package cn.org.awcp.venson.dingding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Env {

	/** 钉钉api接口前缀 */
	public static String OAPI_HOST;
	public static String OA_BACKGROUND_URL;
	/** 企业Id */
	public static String CORP_ID;

	/** 企业密钥 */
	public static String CORP_SECRET;
	public static String SSO_Secret;

	public static String suiteTicket;
	public static String authCode;
	public static String suiteToken;

	public static String CREATE_SUITE_KEY;
	public final static String SUITE_KEY = "";
	public static String SUITE_SECRET;
	public static String TOKEN;
	public static String ENCODING_AES_KEY;

	/** 随机数 */
	public final static String NONCE = "abcdefg";

	/** 默认的应用Id */
	public static String DEFAULT_AGENT_ID;
	/** 应用Id参数名称 */
	public final static String PARAM_AGENT_ID_NAME = "agnetId";
	// 静态语句块
	static {
		InputStream in = Env.class.getClassLoader().getResourceAsStream("conf/dingding/dd.properties");
		BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
		Properties p = new Properties();
		try {
			p.load(bfr);
			OAPI_HOST = p.getProperty("oapi_host");
			CORP_ID = p.getProperty("corpId");
			CORP_SECRET = p.getProperty("corpSecret");
			TOKEN = p.getProperty("token");
			ENCODING_AES_KEY = p.getProperty("aes_key");
			DEFAULT_AGENT_ID = p.getProperty("default_agent_id");
		} catch (IOException e) {
			// logger.info("ERROR", e);
		}
	}

}
