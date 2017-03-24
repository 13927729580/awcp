package org.szcloud.framework.workflow.core.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.szcloud.framework.workflow.core.business.TCyclostyle;
import org.szcloud.framework.workflow.core.business.TPublicFunc;
import org.szcloud.framework.workflow.core.business.TUserAdmin;
import org.szcloud.framework.workflow.core.business.TWorkAdmin;
import org.szcloud.framework.workflow.core.emun.EDatabaseType;
import org.szcloud.framework.workflow.core.emun.EUserType;
import org.szcloud.framework.workflow.core.entity.CCyclostyle;
import org.szcloud.framework.workflow.core.module.MDigest;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Document;

/**
 * 基础登录类
 */
@Resource
public class CLogon {

	/**
	 * 系统是否是调试情况
	 */
	public static int SYSTEM_DEBUG = 0;
	/**
	 * 对象引用变量定义
	 */

	/**
	 * 全局变量参数
	 */
	public CGlobalPara GlobalPara = new CGlobalPara();

	/**
	 * 错误处理类
	 */
	public CError Error = null;

	/**
	 * 框架数据库处理类
	 */
	private JdbcTemplate jdbcTemplate = null;

	@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	/**
	 * 数据库处理类
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	/**
	 * 用户管理对象
	 */
	private TUserAdmin mo_UserAdmin = null;

	/**
	 * 获取用户管理对象
	 * 
	 * @return
	 */
	public TUserAdmin getUserAdmin() {
		if (mo_UserAdmin == null)
			mo_UserAdmin = new TUserAdmin(this);
		return mo_UserAdmin;
	}


	/**
	 * 流转管理对象
	 */
	private TWorkAdmin mo_WorkAdmin = null;

	/**
	 * 获取流转管理对象
	 * 
	 * @return
	 */
	public TWorkAdmin getWorkAdmin() {
		if (mo_WorkAdmin == null)
			mo_WorkAdmin = new TWorkAdmin(this);
		return mo_WorkAdmin;
	}

	/**
	 * 公共函数对象
	 */
	private TPublicFunc mo_PublicFunc = null;

	/**
	 * 获取公共函数对象
	 * 
	 * @return
	 */
	public TPublicFunc getPublicFunc() {
		if (mo_PublicFunc == null)
			mo_PublicFunc = new TPublicFunc(this);
		return mo_PublicFunc;
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#

	/**
	 * 登陆域(不同域采用不同的数据库)名称
	 */
	private String ms_dbDomain = "";

	/**
	 * 获取登陆域
	 * 
	 * @return
	 */
	public String getdbDomain() {
		return ms_dbDomain;
	}

	/**
	 * 設置登陆域
	 * 
	 * @param value
	 */
	public void setdbDomain(String value) {
		ms_dbDomain = value;
	}

	/**
	 * 是否不记录最近访问时间，缺省情况为需要记录
	 */
	public boolean IsNotRecordLogonDate = false;

	/**
	 * 系统参数集合
	 */
	private List<CSysParameter> mo_Parameters = new ArrayList<CSysParameter>();

	/**
	 * 客户端参数定义
	 */
	private Document mo_UserParameter = null;

	/**
	 * 获取客户端参数定义
	 * 
	 * @return
	 */
	public Document getUserParameter() {
		return mo_UserParameter;
	}

	/**
	 * 限制用户数量
	 */
	private int mi_LimitedUser = 0;

	/**
	 * 获取限制用户数量
	 * 
	 * @return
	 */
	public int getLimitedUser() {
		return mi_LimitedUser;
	}

	/**
	 * 限制流程数量
	 */
	private int mi_LimitedFlow = 0;

	/**
	 * 获取制流程数量
	 * 
	 * @return
	 */
	public int getLimitedFlow() {
		return mi_LimitedFlow;
	}

	/**
	 *
	 */
	public String LogonText = "";

	/**
	 * 用户编号
	 */
	private String ms_UserNo = "";

	/**
	 * 获取用户编号
	 * 
	 * @return
	 */
	public String getUserNo() {
		return ms_UserNo;
	}

	// 一些公共内存变量定义[在特定时候来传递某些值]
	/**
	 * 布尔型变量
	 */
	public boolean blnParameter = false;
	/**
	 * 短整型变量
	 */
	public short shtParameter = 0;
	/**
	 * 整型变量
	 */
	public int intParameter = 0;
	/**
	 * 长整型变量
	 */
	public long lngParameter = 0;
	/**
	 * 字符型变量
	 */
	public String strParameter = null;
	/**
	 * 日期型变量
	 */
	public Date datParameter = null;
	/**
	 * 对象型变量
	 */
	public Object objParameter = null;
	/**
	 * 对象型变量2
	 */
	public Object varParameter = null;

	/**
	 * 登录类型
	 */
	public int intLogonType = 0;

	/**
	 * 公文文件路径
	 */
	public String SaveDataPath = "";

	/**
	 * 临时文件路径
	 */
	public String TempPath = "";

	/**
	 * 初始化（登录） 登陆本系统，返回登陆系统的唯一标识（GUID）,以后登陆本系统可以使用该唯一标识（GUID）登陆
	 * 
	 * @param ai_Type
	 *            登录方式：=0-按代码；=1-按用户标识；=2-按手机号码；=3-按右键地址；
	 * @param as_User
	 *            用户代码（含域），使用【\或|或#分隔】
	 * @param as_Password
	 *            登录密码
	 * @param as_IpAddress
	 *            登录IP地址
	 */
	public CLogon(int ai_Type, String as_User, String as_Password,
			String as_IpAddress) {
		try {
			String ls_User = as_User.replace("\\", "|").replace("#", "|");
			int i = ls_User.indexOf("|");
			if (i > -1) {
				ms_dbDomain = ls_User.substring(0, i);
				ls_User = MGlobal.right(ls_User, ls_User.length() - i - 1);
			}

			String ls_Cond = "";
			switch (ai_Type) {
			case 1:
				ls_Cond = " AND u.UserID = " + ls_User;
				break;
			case 2:
				ls_Cond = " AND u.MobileNo = '" + ls_User.replaceAll("'", "''")
						+ "'";
				break;
			case 3:
				ls_Cond = " AND u.UserEMail = '"
						+ ls_User.replaceAll("'", "''") + "'";
				break;
			default:
				ls_Cond = " AND u.UserCode = '" + ls_User.replaceAll("'", "''")
						+ "'";
				break;
			}

			this.Error = new CError();

			this.GlobalPara.IPAddress = as_IpAddress;
			this.initParameter(1, "");
			initModuleParameter(); // 初始化参数

			String ls_Sql = "SELECT u.UserID, u.UserGuid, u.UserCode, u.UserName, u.OUserName, u.NUserName, u.UserPassword, ";
			ls_Sql += "u.UserPosition, u.UserTitle, u.UserFlag, u.UserIsValid, d.DeptID, d.BelongID, ";
			ls_Sql += "d.DeptName FROM UserInfo u LEFT JOIN DeptInfo d ON u.DeptID = d.DeptID";
			ls_Sql += " WHERE u.UserType = 0" + ls_Cond;

			List<Map<String,Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
			Boolean lb_Boolean = lo_Set.size()>0;
			if (!lb_Boolean) {
				lo_Set = null;
				// #错误[1003]：错误的用户名称或用户代码和密码
				this.Raise(1003, "Logon", as_User);
				return;
			}

			int li_Type = getUserType(MGlobal.readInt(lo_Set.get(0),"UserID"));
//			if (MGlobal.readInt(lo_Set.get(0),"UserIsValid") == 0
			if ((Short)lo_Set.get(0).get("UserIsValid") == 0
					|| li_Type == EUserType.InterdictUser) {
				lo_Set = null;
				// #错误[1052]：当前用户被禁止使用本系统
				this.Raise(1052, "Logon", as_User);
				return;
			}

			// 验证系统密码
			/*
			 * if (!(this.GlobalPara.IsIntegrationNT ||
			 * this.GlobalPara.IsInsideTransact)) { if
			 * (lo_Set.getObject("UserPassword") == null) { lo_Set.close();
			 * lo_Set = null; // #错误[1003]：错误的用户名称或用户代码和密码 this.Raise(1003,
			 * "Logon", as_User); return; } else { // 校验密码 if
			 * (!checkPassword(as_Password, lo_Set.getInt("UserID"),
			 * lo_Set.getString("UserPassword"))) { lo_Set.close(); lo_Set =
			 * null; // #错误[1052]：当前用户被禁止使用本系统 this.Raise(1052, "Logon",
			 * as_User); return; } } this.GlobalPara.UserPassword = as_Password;
			 * }
			 */
			this.GlobalPara.UserPassword = as_Password;

			setInitUserInfo(lo_Set, li_Type);
			lo_Set = null;

			// 验证NT密码
			if (this.GlobalPara.IsIntegrationNT
					&& (!this.GlobalPara.IsInsideTransact)) {
				if (!isValidNtUser(this.GlobalPara.DomainName,
						this.GlobalPara.UserCode, as_Password)) {
					// 错误[1004]：错误的NT登陆用户名称和密码
					this.Raise(1004, "Logon", as_User);
					return;
				}
			}

			if (this.GlobalPara.LogonGuid == null
					|| this.GlobalPara.LogonGuid.equals(""))
				setLogonGuid(ms_dbDomain);

			this.GlobalPara.IsValidSystem = true;
			if (!this.GlobalPara.IsInsideTransact)
				autoRunSystem();
		} catch (Exception ex) {
			ex.printStackTrace();
			this.Raise(ex, "Logon", as_User);
		}
	}

	/**
	 * 初始化用户信息
	 * 
	 * @param ao_Set
	 * @param ai_Type
	 */
	private void setInitUserInfo(List<Map<String,Object>> ao_Set, int ai_Type) {
		try {
			this.GlobalPara.UserID = MGlobal.readInt(ao_Set.get(0),"UserID");
			this.GlobalPara.UserGuid = MGlobal.readString(ao_Set.get(0),"UserGuid");
			this.GlobalPara.UserCode = MGlobal.readString(ao_Set.get(0),"UserCode");
			this.GlobalPara.UserName = MGlobal.readString(ao_Set.get(0),"UserName");
			if (MGlobal.readObject(ao_Set.get(0),"OUserName") != null)
				this.GlobalPara.OUserName = MGlobal.readString(ao_Set.get(0),"OUserName");
			if (MGlobal.readObject(ao_Set.get(0),"NUserName") != null)
				this.GlobalPara.NUserName = MGlobal.readString(ao_Set.get(0),"NUserName");
			this.GlobalPara.UserType = ai_Type;
			// 如果是代理处理公文时，所使用的用户职位和职称应该是被代理人的职位和职称
			if (MGlobal.readObject(ao_Set.get(0),"UserPosition") != null)
				this.GlobalPara.UserPosition = MGlobal.readString(ao_Set.get(0),"UserPosition");
			if (MGlobal.readObject(ao_Set.get(0),"UserTitle") != null)
				this.GlobalPara.UserPosition = MGlobal.readString(ao_Set.get(0),"UserTitle");
			// 部门信息
			if (MGlobal.readObject(ao_Set.get(0),"DeptID") != null) {
				this.GlobalPara.DeptID = MGlobal.readInt(ao_Set.get(0),"DeptID");
				if (MGlobal.readObject(ao_Set.get(0),"DeptName") != null)
					this.GlobalPara.DeptName = MGlobal.readString(ao_Set.get(0),"DeptName");
				if (MGlobal.readObject(ao_Set.get(0),"BelongID") != null)
					this.GlobalPara.BelongID = MGlobal.readInt(ao_Set.get(0),"BelongID");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 初始化（校验登陆信息）
	 * 
	 * @param as_LogonGuid
	 *            登录唯一标识（含域），使用【\或|或#分隔】
	 * @param as_IpAddress
	 *            登录IP地址 * @param ab_UpdateDate 是否更新登录时间
	 */
	public CLogon(String as_LogonGuid, String as_IpAddress,
			boolean ab_UpdateDate) {
		try {
			if (as_LogonGuid == null || as_LogonGuid.equals(""))
				return;

			String ls_Guid = as_LogonGuid.replace("\\", "|").replace("#", "|");
			int i = ls_Guid.indexOf("|");
			if (i > -1) {
				ms_dbDomain = ls_Guid.substring(0, i);
				ls_Guid = MGlobal.right(ls_Guid, ls_Guid.length() - i - 1);
			}

			this.GlobalPara.IPAddress = as_IpAddress;
			this.initParameter(1, "");
			initModuleParameter(); // 初始化参数

			String ls_Sql = "SELECT * FROM LogonInfo WHERE LogonGuid = '"
					+ ls_Guid + "'";
			if (this.getParaValue("CLIENT_IPADDRESS_CHANGED").equals("1"))
				ls_Sql += " AND ComputerName = '" + as_IpAddress + "'";
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql,
					(ab_UpdateDate = false));

			int ll_ErrorID = 0;
			int ll_UserID = 0;
			if(lo_Set.size()>0){
			for(Map<String,Object> row : lo_Set) {
				if (MGlobal.readInt(row,"LogoffType") > 0) {
					// 错误[1120]：登陆已被注销，请重新登陆
					if (MGlobal.readInt(row,"LogoffType") == 1)
						ll_ErrorID = 1120;
					// 错误[1121]：登陆已被强制注销(强制注销情况请咨询管理员)，请重新登陆
					if (MGlobal.readInt(row,"LogoffType") == 2)
						ll_ErrorID = 1121;
				} else {
					// 错误[1122]：系统超时，请重新登陆
					Date ld_Date = MGlobal.readDate(row,"LastLogDate");
					if (MGlobal.getTwoDateMinute(ld_Date, MGlobal.getNow()) > this.GlobalPara.OverTime)
						ll_ErrorID = 1122;
				}
				ll_UserID = MGlobal.readInt(row,"UserID");
			}} else {
				// 错误[1120]：登陆已被注销，请重新登陆
				ll_ErrorID = 1120;
			}
			if (ll_ErrorID > 0) {
				lo_Set = null;
				this.Raise(ll_ErrorID, "CLogon", as_LogonGuid + "("
						+ as_IpAddress + ")");
				return;
			}
			List<String> sqlList = new ArrayList<String>();
			if (ab_UpdateDate) {
				MGlobal.updateDate(lo_Set,"LastLogDate", MGlobal.getSqlNow());
				MGlobal.updateRow(lo_Set);
				//ResultSet rs = new ResultSet();
				//rs.updateDate(columnLabel, x);
				sqlList.add("UPDATE OnLineUser SET IsOnLine = 1 WHERE UserID = "
						+ String.valueOf(ll_UserID) + ";");
				String ls_Date = MGlobal.dateToString(MGlobal.getNow());
				if (this.GlobalPara.DbType == EDatabaseType.ORACLE) {
					sqlList.add("UPDATE UserLogonInfo SET LogonDate = TO_DATE('"
							+ ls_Date
							+ "'), LogoffDate = NULL WHERE LogonGuid = '"
							+ as_LogonGuid + "';");
				} else {
					sqlList.add("UPDATE UserLogonInfo SET LogonDate = '"
							+ ls_Date
							+ "', LogoffDate = NULL WHERE LogonGuid = '"
							+ as_LogonGuid + "';");
				}
				CSqlHandle.getJdbcTemplate().batchUpdate(
						(String[]) sqlList.toArray(new String[sqlList.size()]));
			}
			lo_Set = null;

			int li_Type = this.getUserType(ll_UserID);
			ls_Sql = "SELECT u.UserID, u.UserGuid, u.UserCode, u.UserName, u.OUserName, u.NUserName, u.UserPassword, ";
			ls_Sql += "u.UserPosition, u.UserTitle, u.UserFlag, u.UserIsValid, d.DeptID, d.BelongID, ";
			ls_Sql += "d.DeptName FROM UserInfo u LEFT JOIN DeptInfo d ON u.DeptID = d.DeptID";
			ls_Sql += " WHERE u.UserType = 0 AND u.UserID = "
					+ String.valueOf(ll_UserID);
			lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
			this.setInitUserInfo(lo_Set, li_Type);
			lo_Set = null;
			this.autoRunSystem();
		} catch (Exception ex) {
			this.Raise(ex, "Logon", as_LogonGuid + "(" + as_IpAddress + ")");
		}
	}

	/**
	 * 初始化模块基本参数
	 */
	private void initModuleParameter() {
		try {
			// 读取一些系统参数
			for (CSysParameter lo_Para : mo_Parameters) {
				if (lo_Para.Code.equals("FLOW_ARITHMETIC_TYPE")) { // 流转状态算法参数
					this.GlobalPara.FlowArithmeticType = Integer
							.parseInt(lo_Para.Value);
				} else if (lo_Para.Code.equals("WORK_ITEM_HANDLE")) { // 流转实例显示操作参数
					this.GlobalPara.WorkItemDisplay = Integer
							.parseInt(lo_Para.Value);
				} else if (lo_Para.Code.equals("USER_TABLE_SAVE_DATA")) { // 是否使用数据表格
					this.GlobalPara.UserTableSaveData = (lo_Para.Value == "1");
				} else if (lo_Para.Code.equals("DOMAIN_IP_ADDRESS")) { // 服务器IP地址
					this.GlobalPara.DomainIpAddress = lo_Para.Value;
				} else if (lo_Para.Code.equals("SAVE_DATA_PATH")) { // 公文中正文和附件文件存放位置
					this.GlobalPara.SaveDataPath = lo_Para.Value;
					this.SaveDataPath = lo_Para.Value;
				} else if (lo_Para.Code.equals("TEMP_READ_DATA_PATH")) { // 读取临时文件存放位置
					this.GlobalPara.TempReadDataPath = lo_Para.Value;
				} else if (lo_Para.Code.equals("TEMP_WRITE_DATA_PATH")) { // 设置临时文件存放位置
					this.GlobalPara.TempWriteDataPath = lo_Para.Value;
				} else if (lo_Para.Code.equals("TEMP_DATA_PATH")) { // 临时文件存放位置
					this.GlobalPara.TempDataPath = lo_Para.Value;
					this.TempPath = lo_Para.Value;
				} else if (lo_Para.Code.equals("NET_TEMP_DATA_PATH")) { // 新版本临时文件存放位置
					// this.GlobalPara.TempDataPath = lo_Para.Value;
				} else if (lo_Para.Code.equals("DBMS")) { // 读取数据库类型
					this.GlobalPara.DbType = EDatabaseType.MSSQL;
					if (lo_Para.Value.toUpperCase().indexOf("ORACLE") > 0)
						this.GlobalPara.DbType = EDatabaseType.ORACLE;
					if (lo_Para.Value.toUpperCase().indexOf("SYBASE") > 0)
						this.GlobalPara.DbType = EDatabaseType.SYBASE;
					if (lo_Para.Value.toUpperCase().indexOf("MYSQL") > 0)
						this.GlobalPara.DbType = EDatabaseType.MYSQL;
				} else if (lo_Para.Code.equals("INTERGRATION_NT")) { // 读取是否集成NT密码验证
					this.GlobalPara.IsIntegrationNT = (lo_Para.Value
							.toUpperCase().equals("TRUE"));
				} else if (lo_Para.Code.equals("DOMAIN_NAME")) { // 读取NTServer的域名
					this.GlobalPara.DomainName = lo_Para.Value;
				} else if (lo_Para.Code.equals("SYSTEM_OVER_TIME")) {
					this.GlobalPara.OverTime = Integer.parseInt(lo_Para.Value);
				} else if (lo_Para.Code.equals("RECORD_LOG_TYPE")) {
					this.GlobalPara.LogType = Integer.parseInt(lo_Para.Value);
				} else if (lo_Para.Code.equals("LOG_FILE_PATH")) {
					this.GlobalPara.LogPath = lo_Para.Value;
				} else {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			this.Raise(ex, "initModuleParameter", "");
		}
	}

	/**
	 * 读取用户的类型
	 * 
	 * @param al_UserID
	 *            用户标识
	 * @return
	 */
	private int getUserType(int al_UserID) {
		try {
			if (al_UserID == 1)
				return EUserType.SystemAdministrator;
			int ll_ModuleID = 3;
			String ls_Sql = "SELECT * FROM ModuleTypeRight WHERE TypeID = "
					+ String.valueOf(ll_ModuleID) + " AND ";
			ls_Sql += "(UserID = " + String.valueOf(al_UserID)
					+ " OR UserID IN (SELECT GroupID FROM UserGroup WHERE ";
			ls_Sql += "UserID = " + String.valueOf(al_UserID) + "))";
			List<Map<String,Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
			int li_Type = EUserType.CommondUser;
			for(Map<String,Object> row : lo_Set) {
				if (MGlobal.readInt(row,"UserFlag") == EUserType.InterdictUser) {
					li_Type = EUserType.InterdictUser;
					break;
				} else {
					if (MGlobal.readInt(row,"UserFlag") != EUserType.CommondUser) {
						li_Type = (li_Type | MGlobal.readInt(row,"UserFlag"));
					}
				}
			}
			lo_Set = null;

			return li_Type;
		} catch (Exception ex) {
			ex.printStackTrace();
			return EUserType.CommondUser;
		}
	}

	/**
	 * 校验密码
	 * 
	 * @param as_Password
	 * @param al_UserId
	 * @param as_CheckPassword
	 * @return
	 */
	private boolean checkPassword(String as_Password, int al_UserId,
			String as_CheckPassword) {
		if (digestOAPassword(as_Password, al_UserId).equals(as_CheckPassword))
			return true;
		if (digestStrToHexStr(as_Password).equals(as_CheckPassword))
			return true;
		return true;// return false;暂时不校验密码
	}

	/**
	 * 加密密码（需要重新实现）
	 * 
	 * @param as_Password
	 *            密码
	 * @param al_UserId
	 *            用户标识
	 * @return
	 */
	private String digestOAPassword(String as_Password, int al_UserId) {
		return "";
	}

	/**
	 * 原加密密码（需要重新实现）
	 * 
	 * @param as_Password
	 *            密码
	 * @return
	 */
	private String digestStrToHexStr(String as_Password) {
		return "";
	}

	/**
	 * 验证NT密码（需要重新实现）
	 * 
	 * @param as_Domain
	 * @param as_User
	 * @param as_Password
	 * @return
	 */
	private Boolean isValidNtUser(String as_Domain, String as_User,
			String as_Password) {
		return true;
	}

	/**
	 * 设置登录信息（需要重新实现）
	 * 
	 * @param as_DbDomain
	 * @throws SQLException
	 */
	private Integer setLogonGuid(String as_DbDomain) {
		try {
			String ls_Sql = "INSERT INTO LogonInfo (LogonGuid, UserID, LogonDate, ComputerName, LogonContent, LogoffType, LastLogDate)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
			// PreparedStatement lo_State = this.SqlHandle.getState(ls_Sql, 2);
			// int li_Index = 1;
			String ls_Guid = MGlobal.getGuid();
			java.sql.Timestamp ld_Date = MGlobal.getSqlTimeNow();
			// lo_State.setString(li_Index++, ls_Guid);
			// lo_State.setInt(li_Index++, this.GlobalPara.UserID);
			// lo_State.setTimestamp(li_Index++, ld_Date);
			// lo_State.setString(li_Index++, this.GlobalPara.IPAddress);
			// lo_State.setString(li_Index++, "用户【" + this.GlobalPara.UserName +
			// "】在" + MGlobal.dateToString(ld_Date, "yyyy-MM-dd HH:mm:ss") +
			// "时登录");
			// lo_State.setInt(li_Index++, 0);
			// lo_State.setTimestamp(li_Index++, ld_Date);
			// lo_State.executeUpdate();

			this.GlobalPara.LogonGuid = ls_Guid;
			return CSqlHandle.getJdbcTemplate().update(
					ls_Sql,
					new Object[] {
							ls_Guid,
							this.GlobalPara.UserID,
							ld_Date,
							this.GlobalPara.IPAddress,
							"用户【"
									+ this.GlobalPara.UserName
									+ "】在"
									+ MGlobal.dateToString(ld_Date,
											"yyyy-MM-dd HH:mm:ss") + "时登录", 0,
							ld_Date });
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 更新系统信息
	 * 
	 * @param as_Key
	 */
	public void updateSystem(String as_Key) {
		try {
			if (as_Key != "48CEECD9-5FE5-45A5-8B31-F80628A722E0")
				return;
			String ls_Text = "16C57968-C1F9-4DC0-BEE2-C8FB7C680B05["
					+ MGlobal.dateToString(MGlobal.getNow(),
							"yyyy-MM-dd HH:mm:ss")
					+ "]BF8BCAEC-F2F4-4E29-80EC-18CAD211443C"
					+ String.valueOf(Math.round(12345)) + "8&/"
					+ String.valueOf(Math.round(54321));
			ls_Text = MDigest.EncodeString(ls_Text);
			CSqlHandle
					.getJdbcTemplate()
					.update("DELETE FROM SYSTEM_INFO WHERE GUID = '0D5A7B19-8EBB-4CD0-B97E-C056C7204D23'");
			CSqlHandle
					.getJdbcTemplate()
					.update("INSERT INTO SYSTEM_INFO (GUID, CONTENT) VALUES ('0D5A7B19-8EBB-4CD0-B97E-C056C7204D23', '"
							+ ls_Text + "')");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 自动执行系统信息
	 */
	private void autoRunSystem() {
		try {
			String ls_Sql = "SELECT CONTENT FROM SYSTEM_INFO WHERE GUID = '0D5A7B19-8EBB-4CD0-B97E-C056C7204D23'";
			ls_Sql = MDigest.DecodeString(CSqlHandle.getJdbcTemplate()
					.queryForList(ls_Sql).get(0).toString());
			int i = ls_Sql.indexOf("[");
			int j = ls_Sql.indexOf("]");
			if (i > -1 && j > i) {
				if (MGlobal.left(ls_Sql, 36) == "16C57968-C1F9-4DC0-BEE2-C8FB7C680B05"
						&& ls_Sql.substring(j + 2, 36) == "BF8BCAEC-F2F4-4E29-80EC-18CAD211443C") {
					ls_Sql = ls_Sql.substring(i + 2, j - i - 1);
					if (MGlobal.compareDate(MGlobal.getDate(
							MGlobal.stringToDate(ls_Sql), 1, 0, 0, 0), MGlobal
							.getNow()) > 0)
						return;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 注销
	 */
	public void ClearUp() {
		if (this.GlobalPara != null) {
			this.GlobalPara.ClearUp();
			this.GlobalPara = null;
		}
		if (this.Error != null) {
			this.Error.ClearUp();
			this.Error = null;
		}
		if (mo_PublicFunc != null) {
			mo_PublicFunc.ClearUp();
			mo_PublicFunc = null;
		}
		if (mo_UserAdmin != null) {
			mo_UserAdmin.ClearUp();
			mo_UserAdmin = null;
		}
		if (mo_WorkAdmin != null) {
			mo_WorkAdmin.ClearUp();
			mo_WorkAdmin = null;
		}
		mo_UserParameter = null;
	}

	/**
	 * 获取公文是否存在扩展字段
	 * 
	 * @return
	 */
	public boolean getExistExtendField() {
		return getParaValue("WFIS_EXIST_EDTEND_FIELD").equals("1");
	}

	/**
	 * 根据系统参数代码读取系统参数值
	 * 
	 * @param as_Code
	 *            系统参数代码
	 * @return
	 */
	public String getParaValue(String as_Code) {
		CSysParameter lo_Para = getParameter(as_Code);
		if (lo_Para == null)
			return "";
		return lo_Para.Value;
	}

	/**
	 * 根据系统参数代码读取系统参数对象
	 * 
	 * @param as_Code
	 *            系统参数代码
	 * @return
	 */
	public CSysParameter getParameter(String as_Code) {
		try {
			if (mo_Parameters == null)
				for (CSysParameter lo_Para : mo_Parameters) {
					if (lo_Para.Code == as_Code)
						return lo_Para;
				}
			String ls_Sql = "SELECT * FROM SysParameter WHERE ParameterCode = '"
					+ as_Code.replaceAll("'", "''") + "'";
			List<Map<String,Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
			CSysParameter lo_Parameter = null;
			for(Map<String,Object> row : lo_Set) {
				lo_Parameter = new CSysParameter(row, 1);
				mo_Parameters.add(lo_Parameter);
			}
			lo_Set = null;
			return lo_Parameter;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 初始化系统参数
	 * 
	 * @param ai_Type
	 *            初始化类型：=1-初始化参数一数据；=2-初始化参数二数据；=4-初始化外部参数数据；=8-And操作(
	 *            缺省与as_TypeCode使用或操作)；
	 * @param as_TypeCode
	 *            分类代码
	 */
	private void initParameter(int ai_Type, String as_TypeCode) {
		try {
			String ls_Sql = "SELECT ParameterCode, ParameterName, ParameterType, ParameterValue FROM SysParameter WHERE (1=2";
			if ((ai_Type & 1) == 1)
				ls_Sql += " OR ParameterType = -1";
			if ((ai_Type & 2) == 2)
				ls_Sql += " OR ParameterType = 0";
			if ((ai_Type & 4) == 4)
				ls_Sql += " OR ParameterType = 1";
			ls_Sql += ")";
			if (as_TypeCode != null && as_TypeCode != "") {
				if ((ai_Type & 8) == 8)
					ls_Sql += " AND TypeCode = '" + as_TypeCode + "'";
				else
					ls_Sql += " OR TypeCode = '" + as_TypeCode + "'";
			}
			List<Map<String,Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CSysParameter lo_Parameter = new CSysParameter(row, 0);
				mo_Parameters.add(lo_Parameter);
			}
			//lo_Set.getStatement().close();
			lo_Set = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取公文文件存储路径
	 * 
	 * @param al_TempID
	 *            公文模板标识
	 * @param ai_Type
	 *            文件存储类型
	 * @param al_SaveId
	 *            文件存储位置标识
	 * @param ad_Date
	 *            日期
	 * @return
	 */
	public String getFileSavePath(int al_TempID, int ai_Type, int al_SaveId,
			Date ad_Date) {
		return null;
	}

	/**
	 * 错误处理函数
	 * 
	 * @param al_ErrorId
	 *            错误编号
	 * @param as_Position
	 *            产生位置
	 * @param as_Parameter
	 *            执行引起参数
	 * @throws Exception
	 */
	public void Raise(int al_ErrorId, String as_Position, String as_Parameter)
			throws Exception {
		Exception lo_Exception = new Exception();
		// lo_Exception.setStackTrace("ddddd");
		throw lo_Exception;
		// this.Error.Raise(al_ErrorId, this, as_Position, as_Parameter);
	}

	/**
	 * 错误处理函数
	 * 
	 * @param ao_Exception
	 *            错误对象
	 * @param ao_Base
	 *            发生对象
	 * @param as_Position
	 *            产生位置
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public void Raise(Exception ao_Exception, String as_Position,
			String as_Parameter) {
		// this.Error.Raise(ao_Exception, this, as_Position, as_Parameter);
	}

	/**
	 * 记录日志
	 * 
	 * @param as_Position
	 *            位置
	 * @param as_Parameter
	 */
	public void Record(String as_Position, String as_Parameter) {

	}

	/**
	 * 建立公文模板对象
	 * 
	 * @param al_Id
	 *            模板标识，=0-为新建；
	 * @return
	 * @throws Exception
	 */
	public CCyclostyle getCyclostyle(int al_Id) throws Exception {
		return getCyclostyle(al_Id, 0);
	}

	/**
	 * 建立公文模板对象
	 * 
	 * @param al_Id
	 *            模板标识，=0-为新建；
	 * @param al_Id
	 *            流程标识，=0-为缺省流程；
	 * @return
	 * @throws Exception
	 */
	public CCyclostyle getCyclostyle(int al_Id, int al_FlowId) throws Exception {
		if (!this.GlobalPara.IsValidSystem) {
			// 错误[1002]：未登录本系统
			this.Raise(1002, "getCyclostyle", String.valueOf(al_Id));
			return null;
		}

		CCyclostyle lo_Cyclostyle = new CCyclostyle(this);
		boolean lb_Boolean;
		if (al_Id == 0) {
			lb_Boolean = TCyclostyle.newCyclostyle(lo_Cyclostyle, 0);
		} else {
			lb_Boolean = TCyclostyle.openCyclostyle(lo_Cyclostyle, al_Id,
					al_FlowId, true);
		}

		if (lb_Boolean)
			return lo_Cyclostyle;

		lo_Cyclostyle.ClearUp();
		lo_Cyclostyle = null;
		return null;
	}

	/**
	 * 记录关键日志
	 * 
	 * @param as_Position
	 *            产生位置
	 * @param as_Title
	 *            日志标题
	 * @param as_Content
	 *            日志内容
	 */
	public Integer recordKeyLog(String as_Position, String as_Title,
			String as_Content) {
		try {
			// 如果是Oracle数据库，需要做相关调整
			String ls_Sql = "INSERT INTO KEY_RECORD "
					+ "(TYPE_ID, BRING_DATE, USER_ID, USER_NAME, IP_ADDRESS, "
					+ "BRING_POS, LOG_TITLE, LOG_CONTENT) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			// PreparedStatement lo_State = this.SqlHandle.getState(ls_Sql, 2);
			// int li_Index = 1;
			// lo_State.setInt(li_Index++, 3);
			// lo_State.setTimestamp(li_Index++, MGlobal.getSqlTimeNow());
			// lo_State.setInt(li_Index++, this.GlobalPara.UserID);
			// lo_State.setString(li_Index++, this.GlobalPara.UserName);
			// lo_State.setString(li_Index++, this.GlobalPara.IPAddress);
			// lo_State.setString(li_Index++, as_Position);
			// lo_State.setString(li_Index++, as_Title);
			// lo_State.setString(li_Index++, MGlobal.getDbValue(as_Content));
			// lo_State.executeUpdate();
			// lo_State.close();
			// lo_State = null;
			return CSqlHandle.getJdbcTemplate().update(
					ls_Sql,
					new Object[] { 3, MGlobal.getSqlTimeNow(),
							this.GlobalPara.UserID, this.GlobalPara.UserName,
							this.GlobalPara.IPAddress, as_Position, as_Title,
							MGlobal.getDbValue(as_Content) });
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前用户标识（字符串）
	 * 
	 * @return
	 */
	public String getUserID() {
		return String.valueOf(this.GlobalPara.UserID);
	}

}
