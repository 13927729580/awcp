package cn.org.awcp.venson.dingding;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.org.awcp.core.utils.Springfactory;

public class Env {

	/** 钉钉api接口前缀 */
	public final static String OAPI_HOST = "https://oapi.dingtalk.com";
	/** 企业Id */
	public final static String CORP_ID;

	/** 企业密钥 */
	public final static String CORP_SECRET;

	public final static String TOKEN;
	public final static String ENCODING_AES_KEY;

	/** 随机数 */
	public final static String NONCE = "abcdefg";

	/** 默认的应用Id */
	public final static String DEFAULT_AGENT_ID;
	/** 应用Id参数名称 */
	public final static String PARAM_AGENT_ID_NAME = "agnetId";
	// 静态语句块
	static {
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select c_key,c_value from dd_config");
		Map<String, String> data = list.stream()
				.collect(Collectors.toMap(m -> (String) m.get("c_key"), m -> (String) m.get("c_value")));
		CORP_ID = data.get("corpId");
		CORP_SECRET = data.get("corpSecret");
		TOKEN = data.get("token");
		ENCODING_AES_KEY = data.get("aes_key");
		DEFAULT_AGENT_ID = data.get("default_agent_id");
	}

}
