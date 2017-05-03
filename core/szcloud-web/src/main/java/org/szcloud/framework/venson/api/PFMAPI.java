package org.szcloud.framework.venson.api;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.szcloud.framework.core.domain.SzcloudJdbcTemplate;
import org.szcloud.framework.core.utils.Springfactory;

/**
 * api接口实体类
 * 
 * @author venson
 *
 */
public class PFMAPI {
	// APIID
	private int APIID;
	// API名称
	private String APIName;
	// API语句
	private String APISQL;
	// API状态
	private Integer APIState;
	// API类型
	private Integer APIType;
	// 是否登录
	private Integer APIIsLogin;
	// 请求方式
	private String APIMethod;
	// API对应的表
	private String APITable;
	// API描述
	private String APIDesc;

	public int getAPIID() {
		return APIID;
	}

	public void setAPIID(int aPIID) {
		APIID = aPIID;
	}

	public String getAPIName() {
		return APIName;
	}

	public void setAPIName(String aPIName) {
		APIName = aPIName;
	}

	public String getAPISQL() {
		return APISQL;
	}

	public void setAPISQL(String aPISQL) {
		APISQL = aPISQL;
	}

	public int getAPIState() {
		return APIState;
	}

	public void setAPIState(int aPIState) {
		APIState = aPIState;
	}

	public int getAPIIsLogin() {
		return APIIsLogin;
	}

	public void setAPIIsLogin(int aPIIsLogin) {
		APIIsLogin = aPIIsLogin;
	}

	public String getAPIMethod() {
		return APIMethod;
	}

	public void setAPIMethod(String aPIMethod) {
		APIMethod = aPIMethod;
	}

	public int getAPIType() {
		return APIType;
	}

	public void setAPIType(int aPIType) {
		APIType = aPIType;
	}

	public String getAPITable() {
		return APITable;
	}

	public void setAPITable(String aPITable) {
		APITable = aPITable;
	}

	public String getAPIDesc() {
		return APIDesc;
	}

	public void setAPIDesc(String aPIDesc) {
		APIDesc = aPIDesc;
	}

	/**
	 * 根据Id获取API记录
	 */
	public static PFMAPI get(String id) {
		SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		String sql = "select API_ID as APIID,API_Name as APIName,API_SQL as APISQL,API_DESC as APIDesc,"
				+ "API_State as APIState,API_Type as APIType,API_Table as APITable,API_IS_LOGIN as APIIsLogin,API_Method as APIMethod from p_fm_api where ";
		if (StringUtils.isNumeric(id)) {
			sql += " API_ID=?";
		} else {
			sql += " API_Name=?";
		}
		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<PFMAPI>(PFMAPI.class), id);
	}

	/**
	 * 获取表的所有字段
	 */
	public List<TableDesc> getAPITableFields(String apiTable) {
		SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		if (apiTable == null) {
			apiTable = this.APITable;
		}

		String sql = "desc " + apiTable;
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<TableDesc>(TableDesc.class));
	}

	public List<TableDesc> getAPITableFields() {
		return getAPITableFields(null);
	}

	/**
	 * 获取主键
	 */
	public String getPrimaryKey() {
		for (TableDesc tableDesc : this.getAPITableFields()) {
			if (tableDesc.isPrimaryKey()) {
				return tableDesc.getField();
			}
		}
		return null;
	}

}
