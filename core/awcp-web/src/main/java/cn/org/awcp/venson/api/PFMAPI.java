package cn.org.awcp.venson.api;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.Cache;
import cn.org.awcp.core.utils.Springfactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.io.Serializable;
import java.util.List;

/**
 * api接口实体类
 * 
 * @author venson
 *
 */
public class PFMAPI implements Serializable {

	private static final long serialVersionUID = 1L;

	// APIID
	private String APIID;
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

	// API缓存
	private String APICache;

	private List<APIRule> rules;

	private List<APIParameter> parameters;

	public List<APIParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<APIParameter> parameters) {
		this.parameters = parameters;
	}

	public List<APIRule> getRules() {
		return rules;
	}

	public void setRules(List<APIRule> rules) {
		this.rules = rules;
	}

	public String getAPIID() {
		return APIID;
	}

	public void setAPIID(String aPIID) {
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

	public boolean isCache() {
		return "1".equals(APICache);
	}

	public void setAPIState(Integer aPIState) {
		APIState = aPIState;
	}

	public void setAPIType(Integer aPIType) {
		APIType = aPIType;
	}

	public void setAPIIsLogin(Integer aPIIsLogin) {
		APIIsLogin = aPIIsLogin;
	}

	public void setAPICache(String aPICache) {
		APICache = aPICache;
	}

	private static final Cache cache = Springfactory.getBean("cache");

	/**
	 * 根据Id获取API记录
	 */
	public static PFMAPI get(String apiName) {
		if (StringUtils.isBlank(apiName)) {
			return null;
		} else {
			apiName = apiName.trim();
		}
		// 判断是否启用缓存
		// 先去缓存中查找
		Object value = cache.get(apiName);
		if (value instanceof PFMAPI) {
			return (PFMAPI) value;
		}
		SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		String sql = "select API_ID as APIID,API_Name as APIName,API_SQL as APISQL,API_DESC as APIDesc,"
				+ "API_State as APIState,API_Type as APIType,API_Table as APITable,API_IS_LOGIN as APIIsLogin,"
				+ "API_Method as APIMethod,API_ISCACHE as APICache from p_fm_api where API_Name=?";
		try {
			PFMAPI result = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PFMAPI.class), apiName);
			result.setRules(APIRule.get(result.getAPIID(), jdbcTemplate));
			result.setParameters(APIParameter.get(result.getAPIID(),jdbcTemplate));
			cache.put(apiName, result);
			return result;
		} catch (DataAccessException e) {
			return null;
		}
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
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TableDesc.class));
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
