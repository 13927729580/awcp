package cn.org.awcp.venson.api;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.List;

public class APIParameter implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String type;
	private boolean requires;
	private String describes;
	private String defaultValue;
	private String place;

	public static List<APIParameter> get(String APIId, JdbcTemplate jdbcTemplate) {
		String sql = "SELECT a.id,a.name,a.type,IF(a.ISNECESSARY='1',true,false) requires,a.describes,DEFAULTVAL defaultValue,a.PARAM_PLACE place FROM p_fm_api_describe a  WHERE a.APIID=?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(APIParameter.class), APIId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequires() {
		return requires;
	}

	public void setRequires(boolean requires) {
		this.requires = requires;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
}
