package org.szcloud.framework.workflow.core.business;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.module.MGlobal;

public class TUserAdmin {

	/**
	 * 所属的登录对象
	 */
	public CLogon Logon = null;

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public TUserAdmin(CLogon ao_Logon) {
		this.Logon = ao_Logon;
	}

	/**
	 * 清除
	 */
	public void ClearUp() {
		this.Logon = null;
	}

	/**
	 * 获取合法的用户
	 * 
	 * @param as_Users
	 *            所要筛选的用户
	 * @return
	 * @throws Exception
	 */
	public String validUsers(String as_Users) throws Exception {
		return validUsers(as_Users, 3, 1, true, true);
	}

	/**
	 * 筛选用户正确性
	 * 
	 * @param as_Users
	 *            所要筛选的用户
	 * @param ai_ValidType
	 *            如何筛选用户，=1——使用名称筛选，=2——使用代码筛选，=1+2——名称与代码同时筛选
	 * @param ai_UserType
	 *            筛选用户的类型，=1——用户，=2——组别，=1+2——用户和组别
	 * @param ab_ReturnName
	 *            返回值类型，=True——返回名称，=False——返回代码
	 * @param ab_OrderBy
	 *            排列类型，=True——按输入名称顺序返回，=False——按数据库中用户定义的先后顺序返回
	 * @return
	 * @throws Exception
	 */
	public String validUsers(String as_Users, int ai_ValidType,
			int ai_UserType, boolean ab_ReturnName, boolean ab_OrderBy)
			throws Exception {
		if (as_Users == null || as_Users.equals(""))
			return "";

		String ls_Sql = "SELECT UserCode, UserName, UserType, UserPrior FROM UserInfo"
				+ (ai_UserType == 1 ? " WHERE UserType = 0"
						: (ai_UserType == 2 ? " WHERE UserType = 0" : ""))
				+ " ORDER BY UserPrior";
		List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		String ls_Str = ";";
		String ls_Users = ";" + as_Users + ";";
		for(Map<String,Object> row : lo_Set) {
			String ls_Name = ";" + MGlobal.readString(row,"UserName") + ";";
			String ls_Code = ";" + MGlobal.readString(row,"UserCode") + ";";
			int i = ls_Users.indexOf(ls_Name) + 1;
			int j = ls_Users.indexOf(ls_Code) + 1;
			boolean lb_Boolean = false;
			if (ai_ValidType == 1)
				lb_Boolean = (i > 0);
			else {
				if (ai_ValidType == 2) {
					lb_Boolean = (j > 0);
				} else {
					lb_Boolean = ((i + j) > 0);
				}
			}
			if (lb_Boolean) {
				if (ab_ReturnName) {
					if (ls_Str.indexOf(ls_Name) == -1) {
						ls_Str += MGlobal.readString(row,"UserName") + ";";
					}
					if (ai_ValidType == 2 || ai_ValidType == 3) {
						ls_Users += ls_Users.replaceAll(ls_Code, ls_Name);
					}
				} else {
					if (ls_Str.indexOf(ls_Code) == -1) {
						ls_Str += MGlobal.readString(row,"UserCode") + ";";
					}
					if (ai_ValidType == 1 || ai_ValidType == 3) {
						ls_Users += ls_Users.replaceAll(ls_Name, ls_Code);
					}
				}
			}
		}


		if (ab_OrderBy) {
			String[] ls_Value = ls_Users.split(";");
			ls_Users = ";";
			for (int i = 0; i < ls_Value.length; i++) {
				if (ls_Str.indexOf(";" + ls_Value[i] + ";") > -1) {
					if (ls_Users.indexOf(";" + ls_Value[i] + ";") == -1) {
						ls_Users += ls_Value[i] + ";";
					}
				}
			}
			if (ls_Users.substring(0, 1).equals(";"))
				ls_Users = ls_Users.substring(1, ls_Users.length());
		} else {
			if (ls_Str.substring(0, 1).equals(";"))
				ls_Users = ls_Str.substring(1, ls_Str.length());
		}

		return ls_Users;
	}

	/**
	 * 转化用户函数
	 * 
	 * @param ai_OldType
	 *            原用户表现类型
	 * @param ai_NewType
	 *            新用户表现类型
	 * @param as_UserValues
	 *            原用户表现值，用户表现类型定义如下： =1 - 用户标识连接串，如：“1;10;12;...” =2 -
	 *            用户名称连接串，如：“金虹;唐琦;邓雅琳;...” =3 -
	 *            用户名称+标识连接串，如：“金虹[2];唐琦[7];邓雅琳[14];...” =4 -
	 *            用户代码连接串，如：“jinhong;tangqi;dengyalin;...” =5 -
	 *            用户名称+标识连接串，如：“jinhong[2];tangqi[7];dengyalin[14];...” =6 -
	 *            用户名称+标识连接串，如：“金虹[jinhong];[唐琦tangqi];邓雅琳[dengyalin];...” =7 -
	 *            符号+用户标识连接串，如：“U1;G10;G12;U23;...”
	 * @return
	 * @throws Exception
	 */
	public String convertUsers(int ai_OldType, int ai_NewType,
			String as_UserValues) throws Exception {
		if (as_UserValues == null || as_UserValues.equals(""))
			return "";
		if (ai_OldType < 1 || ai_OldType > 7 || ai_NewType < 1
				|| ai_NewType > 7)
			return "";

		String ls_Sql = "";
		String[] ls_Array;

		switch (ai_OldType) {
		case 1:
			ls_Sql = as_UserValues.replaceAll(";", ",").trim();
			if (MGlobal.right(ls_Sql, 1).equals(","))
				ls_Sql = MGlobal.left(ls_Sql, ls_Sql.length() - 1);
			ls_Sql = "UserID IN (" + ls_Sql + ")";
			break;
		case 2:
		case 4:
			ls_Sql = as_UserValues.replaceAll("'", "''").trim();
			ls_Sql = ls_Sql.replaceAll(";", "','");
			if (MGlobal.right(ls_Sql, 3).equals("','"))
				ls_Sql = MGlobal.left(ls_Sql, ls_Sql.length() - 3);
			if (ai_OldType == 2) {
				ls_Sql = "UserName IN ('" + ls_Sql + "')";
			} else {
				ls_Sql = "UserCode IN ('" + ls_Sql + "')";
			}
			break;
		case 3:
		case 5:
		case 6:
			ls_Array = as_UserValues.split(";");
			ls_Sql = "";
			for (int i = 0; i < ls_Array.length; i++) {
				if (!ls_Array[i].equals("")) {
					int j = ls_Array[i].indexOf("[") + 1;
					int k = ls_Array[i].indexOf("]") + 1;
					if (ai_OldType == 6) {
						ls_Sql += "'"
								+ ls_Array[i].substring(j + 1, k).replaceAll(
										"'", "''") + "',";
					} else {
						ls_Sql += ls_Array[i].substring(j + 1, k) + ",";
					}
				}
			}
			if (ai_OldType == 6) {
				ls_Sql = "UserCode IN ("
						+ MGlobal.left(ls_Sql, ls_Sql.length() - 1);
			} else {
				ls_Sql = "UserID IN ("
						+ MGlobal.left(ls_Sql, ls_Sql.length() - 1);
			}
			break;
		case 7:
			ls_Sql = as_UserValues.replaceAll("U", "").replaceAll("G", "");
			ls_Sql = ls_Sql.replaceAll(";", ",").trim();
			if (MGlobal.right(ls_Sql, 1).equals(","))
				ls_Sql = MGlobal.left(ls_Sql, ls_Sql.length() - 1);
			ls_Sql = "UserID IN (" + ls_Sql + ")";
			break;
		default:
			break;
		}
		ls_Sql = "SELECT UserID, UserCode, UserName, UserType FROM UserInfo WHERE "
				+ ls_Sql;
		List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);

		String ls_Value = ";" + as_UserValues;
		String ls_OldValue = "", ls_NewValue = "";
		for(Map<String,Object> row : lo_Set) {
			switch (ai_OldType) {
			case 1:
				ls_OldValue = ";" + String.valueOf(MGlobal.readInt(row,"UserID"))
						+ ";";
				break;
			case 2:
				ls_OldValue = ";" + MGlobal.readString(row,"UserName") + ";";
				break;
			case 3:
				ls_OldValue = ";" + MGlobal.readString(row,"UserName") + "["
						+ String.valueOf(MGlobal.readInt(row,"UserID")) + "];";
				break;
			case 4:
				ls_OldValue = ";" + MGlobal.readString(row,"UserCode") + ";";
				break;
			case 5:
				ls_OldValue = ";" + MGlobal.readString(row,"UserCode") + "["
						+ String.valueOf(MGlobal.readInt(row,"UserID")) + "];";
				break;
			case 6:
				ls_OldValue = ";" + MGlobal.readString(row,"UserName") + "["
						+ MGlobal.readString(row,"UserCode") + "];";
				break;
			case 7:
				if (MGlobal.readInt(row,"UserType") == 1) {
					ls_OldValue = ";G"
							+ String.valueOf(MGlobal.readInt(row,"UserID")) + ";";
				} else {
					ls_OldValue = ";U"
							+ String.valueOf(MGlobal.readInt(row,"UserID")) + ";";
				}
				break;
			default:
				break;
			}
			switch (ai_NewType) {
			case 1:
				ls_NewValue = ";@" + String.valueOf(MGlobal.readInt(row,"UserID"))
						+ ";";
				break;
			case 2:
				ls_NewValue = ";@" + MGlobal.readString(row,"UserName") + ";";
				break;
			case 3:
				ls_NewValue = ";@" + MGlobal.readString(row,"UserName") + "["
						+ String.valueOf(MGlobal.readInt(row,"UserID")) + "];";
				break;
			case 4:
				ls_NewValue = ";@" + MGlobal.readString(row,"UserCode") + ";";
				break;
			case 5:
				ls_NewValue = ";@" + MGlobal.readString(row,"UserCode") + "["
						+ String.valueOf(MGlobal.readInt(row,"UserID")) + "];";
				break;
			case 6:
				ls_NewValue = ";@" + MGlobal.readString(row,"UserName") + "["
						+ MGlobal.readString(row,"UserCode") + "];";
				break;
			case 7:
				if (MGlobal.readInt(row,"UserType") == 1) {
					ls_NewValue = ";@G"
							+ String.valueOf(MGlobal.readInt(row,"UserID")) + ";";
				} else {
					ls_NewValue = ";@U"
							+ String.valueOf(MGlobal.readInt(row,"UserID")) + ";";
				}
				break;
			default:
				break;
			}
			ls_Value = ls_Value.replaceAll(ls_OldValue, ls_NewValue);
			int i = ls_Value.indexOf(ls_NewValue) + 1;
			int j = i + ls_NewValue.length() - 2;
			ls_Value = MGlobal.left(ls_Value, j)
					+ MGlobal.right(ls_Value, ls_Value.length() - j)
							.replaceAll(ls_NewValue, ";");
		}

		ls_Array = ls_Value.split(";");
		ls_Value = "";
		for (int i = 0; i < ls_Array.length; i++) {
			if (MGlobal.left(ls_Array[i], 1).equals("@")) {
				ls_Value += ls_Array[i] + ";";
			}
		}
		return ls_Value.replaceAll("@", "");
	}

	/**
	 * 查找系统角色对应的用户 返回格式：用户名称一[用户标识一]角色名称[&角色标识];用户名称二[用户标识二]角色名称[&角色标识];...
	 * 
	 * @param al_UserID
	 * @param al_SystemRoleID
	 * @param as_RoleSign
	 * @param ai_Return
	 * @return
	 * @throws SQLException
	 */
	public static String getSystemRoleUsers(CLogon ao_Logon, int al_UserID,
			int al_SystemRoleID, String as_RoleSign, int ai_Return)
			throws SQLException {
		// 得到该角色的用户标识
		String ls_RoleUserIDs = "";
		int li_Return = ai_Return;
		int ll_DeptID = 0;

		// 读取参照用户的部门以及上级部门
		if (ai_Return < 20) {// 按用户标识计算
			String ls_Sql = "SELECT DeptID FROM UserInfo WHERE UserID = "
					+ String.valueOf(al_UserID);
			ls_Sql = CSqlHandle.getValueBySql(ls_Sql);
			if (MGlobal.isExist(ls_Sql)) 
				ll_DeptID = Integer.parseInt(ls_Sql);
		} else {// 按部门标识计算
			li_Return = ai_Return - 20;
			ll_DeptID = al_UserID;
		}
		if (ll_DeptID == 0)
			return "";

		switch (ai_Return) {
		case 1: // 保留原来的方式
			ls_RoleUserIDs = getDeptRoleUsers(ao_Logon, ll_DeptID,
					al_SystemRoleID, 4, as_RoleSign);
			break;
		case 4:
			ls_RoleUserIDs = getDeptRoleUsers(ao_Logon, ll_DeptID,
					al_SystemRoleID, 1, "");
			break;
		default: // 2，3，0
			ls_RoleUserIDs = getDeptRoleUsers(ao_Logon, ll_DeptID,
					al_SystemRoleID, ai_Return, "");
			break;
		}

		return ls_RoleUserIDs;
	}

	/**
	 * 查询部门中角色对应的用户
	 * 
	 * @param al_DeptID
	 * @param al_SystemRoleID
	 * @param ai_Return
	 * @param as_RoleSign
	 * @return
	 * @throws SQLException
	 */
	public static String getDeptRoleUsers(CLogon ao_Logon, int al_DeptID,
			int al_SystemRoleID, int ai_Return, String as_RoleSign)
			throws SQLException {
		String ls_Sql = "SELECT dp.*, u.UserName, p.PositionName FROM UserDeptPosition dp"
				+ " JOIN UserInfo u ON dp.UserID = u.UserID"
				+ " JOIN UserPosition p ON dp.PositionID = p.PositionID"
				+ " WHERE dp.DeptID = "
				+ String.valueOf(al_DeptID)
				+ " AND dp.PositionID = " + String.valueOf(al_SystemRoleID);

		// 得到该角色的用户标识
		String ls_RoleUserIDs = "";
		// 所有参照部门及其上级部门的部门标识
		List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			switch (ai_Return) {
			case 1:
				ls_RoleUserIDs += MGlobal.readString(row,"UserName") + "["
						+ MGlobal.readString(row,"UserID") + "];";
				break;
			case 2:
				ls_RoleUserIDs += MGlobal.readString(row,"UserName") + ";";
				break;
			case 3:
				ls_RoleUserIDs += MGlobal.readString(row,"UserCode") + ";";
				break;
			case 4:
				ls_RoleUserIDs += MGlobal.readString(row,"UserName") + "["
						+ MGlobal.readString(row,"UserID") + "]";
				ls_RoleUserIDs += MGlobal.readString(row,"PositionName") + "{"
						+ as_RoleSign + MGlobal.readString(row,"PositionID") + "};";
				break;
			default: // 0
				ls_RoleUserIDs += MGlobal.readString(row,"UserID") + ";";
				break;
			}
		}
		lo_Set = null;

		if (MGlobal.isEmpty(ls_RoleUserIDs)) {
			ls_Sql = "SELECT ParentID FROM DeptInfo WHERE DeptID = "
					+ String.valueOf(al_DeptID);
			ls_Sql = CSqlHandle.getValueBySql(ls_Sql);
			if (MGlobal.isExist(ls_Sql)) {
				if (!ls_Sql.equals("0")) // 采用递归获取上级部门的系统角色
					ls_RoleUserIDs = getDeptRoleUsers(ao_Logon,
							Integer.parseInt(ls_Sql), al_SystemRoleID,
							ai_Return, as_RoleSign);
			}
		}

		return ls_RoleUserIDs;
	}

}
