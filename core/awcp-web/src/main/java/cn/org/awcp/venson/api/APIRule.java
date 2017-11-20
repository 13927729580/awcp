package cn.org.awcp.venson.api;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class APIRule {

	private String id;
	private String name;
	private String type;
	private String rules;
	private String describes;
	private String remark;
	private String message;

	public static List<APIRule> get(String APIId, JdbcTemplate jdbcTemplate) {
		String sql = "SELECT a.id,a.name,a.message,a.type,a.rules,a.describes,a.remark FROM p_fm_api_privilege a LEFT JOIN p_fm_api_privilege_relation b ON a.id=b.pri_id WHERE b.api_id=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<APIRule>(APIRule.class), APIId);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getRules() {
		return rules;
	}

	public String getDescribes() {
		return describes;
	}

	public String getRemark() {
		return remark;
	}

	public String getMessage() {
		return message;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
