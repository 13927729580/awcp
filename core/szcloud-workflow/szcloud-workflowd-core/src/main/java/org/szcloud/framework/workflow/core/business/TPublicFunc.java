package org.szcloud.framework.workflow.core.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.module.MGlobal;

public class TPublicFunc {

	/**
	 * 所属的登录对象
	 */
	public CLogon Logon = null;

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public TPublicFunc(CLogon ao_Logon) {
		this.Logon = ao_Logon;
	}

	/**
	 * 清除
	 */
	public void ClearUp() {
		this.Logon = null;
	}

	/**
	 * 字符串转化成集合
	 * 
	 * @param as_String
	 * @param as_SplitString
	 * @return
	 */
	public List<String> StringToColl(String as_String, String as_SplitString) {

		String[] s = as_String.split(as_SplitString);
		List<String> ls = new ArrayList<String>();
		for (int i = 0; i < s.length; i++) {
			ls.add(s[i]);
		}
		return ls;
	}

	/**
	 * 集合转化为字符串
	 * 
	 * @param ao_List
	 *            集合
	 * @param as_SplitString
	 *            分割符号
	 * @return
	 */
	public String CollToString(List<String> ao_List, String as_SplitString) {
		StringBuffer sb = new StringBuffer();
		for (String s : ao_List) {
			sb.append(s);
			sb.append(as_SplitString);
		}
		return sb.toString();

	}

	/**
	 * 获取两个日期之间的实际天数，支持跨年
	 */
	public int getDaysBetween(Calendar start, Calendar end) {
		if (start.after(end)) {
			Calendar swap = start;
			start = end;
			end = swap;
		}
		int days = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
		int y2 = end.get(Calendar.YEAR);
		if (start.get(Calendar.YEAR) != y2) {
			start = (Calendar) start.clone();
			do {
				days += start.getActualMaximum(Calendar.DAY_OF_YEAR);
				start.add(Calendar.YEAR, 1);
			} while (start.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 获取指定长度字符的字节数
	 * 
	 * @param as_Value
	 * @param ai_Length
	 * @return
	 */
	public static int GetStaidString(String as_Value, int ai_Length) {
		String s = as_Value.substring(0, ai_Length);
		int len = s.getBytes().length;
		return len;
	}

	/**
	 * 取系统或用户定义的流水号(1个系统流水号)
	 * 
	 * @param as_Code
	 *            流水号代码
	 * @param as_Name
	 *            流水号名称
	 * @return
	 * @throws SQLException
	 */
	public int getSeqValue(String as_Code, String as_Name) {
		return getSeqValue(as_Code, as_Name, true, 1, null, null, null, null);
	}

	/**
	 * 取系统或用户定义的流水号
	 * 
	 * @param as_Code
	 *            流水号代码
	 * @param as_Name
	 *            流水号名称
	 * @param ab_SystemSeq
	 *            是否系统流水号
	 * @param al_Value
	 *            需要读取多少个系统流水号
	 * @return
	 * @throws SQLException
	 */
	public int getSeqValue(String as_Code, String as_Name, boolean ab_SystemSeq, int al_Value) {
		return getSeqValue(as_Code, as_Name, ab_SystemSeq, al_Value, null, null, null, null);
	}

	/**
	 * 取系统或用户定义的流水号
	 * 
	 * @param as_Code
	 *            流水号代码
	 * @param as_Name
	 *            流水号名称
	 * @param ab_SystemSeq
	 *            是否系统流水号
	 * @param al_Value
	 *            需要读取多少个系统流水号
	 * @param as_TableName
	 *            系统流水号对应系统表名称
	 * @param as_FieldName
	 *            系统流水号对应系统表中流水号列名称
	 * @param as_ConnCode
	 *            实际数据表定义的数据库代码
	 * @param as_LimitedCond
	 *            需要限制的数据条件
	 * @return
	 * @throws SQLException
	 */
	public int getSeqValue(String as_Code, String as_Name, boolean ab_SystemSeq, int al_Value, String as_TableName,
			String as_FieldName, String as_ConnCode, String as_LimitedCond) {
		return getSeqValue(this.Logon, as_Code, as_Name, ab_SystemSeq, al_Value, as_TableName, as_FieldName,
				as_ConnCode, as_LimitedCond);
	}

	/**
	 * 取系统或用户定义的流水号
	 * 
	 * @param as_Code
	 *            流水号代码
	 * @param as_Name
	 *            流水号名称
	 * @param ab_SystemSeq
	 *            是否系统流水号
	 * @param al_Value
	 *            需要读取多少个系统流水号
	 * @param as_TableName
	 *            系统流水号对应系统表名称
	 * @param as_FieldName
	 *            系统流水号对应系统表中流水号列名称
	 * @param as_ConnCode
	 *            实际数据表定义的数据库代码
	 * @param as_LimitedCond
	 *            需要限制的数据条件
	 * @return
	 * @throws SQLException
	 */
	public static int getSeqValue(CLogon ao_Logon, String as_Code, String as_Name, boolean ab_SystemSeq, int al_Value,
			String as_TableName, String as_FieldName, String as_ConnCode, String as_LimitedCond) {
		try {
			// 错误：非法的系统流水号代码
			if (as_Code == null || as_Code.equals(""))
				return 0;

			int ll_Seq = 0;
			boolean lb_Boolean = false;
			String ls_Sql = "SELECT SequenceValue FROM SysSequence WHERE SequenceCode = '"
					+ as_Code.replaceAll("'", "''") + "'";
			ls_Sql = CSqlHandle.getValueBySql(ls_Sql);
			if (MGlobal.isEmpty(ls_Sql)) {
				if (MGlobal.isEmpty(as_TableName)) {
					ll_Seq = 1;
				} else {
					try {
						ll_Seq = CSqlHandle.getTableFieldMaxValue(as_TableName, as_FieldName, as_LimitedCond);
					} catch (Exception ex) {
						ll_Seq = 1;
					}
				}

				ls_Sql = "INSERT INTO SysSequence "
						+ "(SequenceCode, SequenceName, SequenceType, SequenceValue, ConnCode, DataTable, PrimaryKey, LimitedCond)"
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				// PreparedStatement lo_State = CSqlHandle.getState(ls_Sql, 2);
				List parasList = new ArrayList();
				parasList.add(as_Code);
				parasList.add((MGlobal.isEmpty(as_Name) ? as_Code : as_Name));
				parasList.add((ab_SystemSeq ? 0 : 1));
				parasList.add(ll_Seq + (al_Value > 1 ? al_Value : 1));
				parasList.add(MGlobal.getDbValue(as_ConnCode));
				parasList.add(MGlobal.getDbValue(as_TableName));
				parasList.add(MGlobal.getDbValue(as_FieldName));
				parasList.add(MGlobal.getDbValue(as_LimitedCond));

				return CSqlHandle.getJdbcTemplate().update(ls_Sql, parasList.toArray());
			} else {
				ll_Seq = Integer.parseInt(ls_Sql);
				ls_Sql = "UPDATE SysSequence SET SequenceValue = SequenceValue + "
						+ String.valueOf(al_Value > 1 ? al_Value : 1);
				lb_Boolean = CSqlHandle.execSql(ls_Sql);
			}

			if (lb_Boolean)
				return ll_Seq;
			return 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取某一个表的具有固定格式的流水号值
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_UserID
	 *            用户标识
	 * @param ao_Set
	 *            对应表格
	 * @param as_SeqFormat
	 *            流水号格式，其中，<br>
	 *            可以具有以下内容的替换：[yyyy]、[yy]、[MM]、[M]、[dd]、[d]、[hh]、[h]、[mm]、[m]、[
	 *            ss]、[s]<br>
	 *            [UserID]、[UserID-n]-采用n位数加用户标识替换、[SEQID]、[SEQID-n]-采用n位数加流水号替换<br>
	 *            [(字段代码)]、[(字段代码-n)]-采用n位数加字段内容替换
	 * @param as_Code
	 *            流水号代码，其中，可以具有以下内容的替换：[yyyy]、[yy]、[MM]、[M]、[dd]、<br>
	 *            [d]、[hh]、[h]、[mm]、[m]、[ss]、[s]、[UserID]、[UserID-n]-
	 *            采用n位数加用户标识替换
	 * @param as_Name
	 *            流水号名称
	 * @param as_TableName
	 *            系统流水号对应系统表名称
	 * @param as_FieldName
	 *            系统流水号对应系统表中流水号列名称
	 * @param al_Value
	 *            需要读取多少个系统流水号
	 * @return
	 */
	public static String getSeqValue(CLogon ao_Logon, int al_UserID, Map<String, Object> ao_Set, String as_SeqFormat,
			String as_Code, String as_Name, String as_TableName, String as_FieldName, Integer al_Value) {
		String ls_Format = replaceSeqFormatValue(as_SeqFormat, al_UserID);
		String ls_Code = replaceSeqFormatValue(as_Code, al_UserID);
		int li_SeqValue = getSeqValue(ao_Logon, ls_Code, as_Name, false, al_Value, null, null, null, null);
		if (li_SeqValue == 0)
			return "";
		String ls_Seq = "";
		String ls_Text;
		int i = ls_Format.indexOf("[(");
		while (i > -1) {
			int j = ls_Format.indexOf(")]", i);
			if (j == -1) {
				i = ls_Format.indexOf("[(", i + 2);
			} else {
				ls_Text = ls_Format.substring(i + 2, j - 1);
				j = ls_Text.indexOf("-");
				if (j > -1)
					ls_Text = MGlobal.left(ls_Text, j - 1);
				ls_Format = replaceSeqValue(ls_Format, ls_Text, (String) ao_Set.get(ls_Text), "[(", ")]");
				i = ls_Format.indexOf("[(", i + 2);
			}
		}

		if (al_Value == 1) {
			ls_Seq = replaceSeqValue(ls_Format, "SEQID", String.valueOf(li_SeqValue), "[", "]");
		} else {
			for (int k = 0; k < al_Value; k++) {
				ls_Seq += replaceSeqValue(ls_Format, "SEQID", String.valueOf(li_SeqValue + k), "[", "]") + ";";
			}
		}
		String ls_Cond = MGlobal.replace(MGlobal.replace(ls_Seq, "'", "''"), ";", ",");
		if (MGlobal.right(ls_Cond, 1).equals(","))
			ls_Cond = MGlobal.left(ls_Cond, ls_Cond.length() - 1);
		ls_Cond = "'" + MGlobal.replace(ls_Cond, ",", "','") + "'";
		String ls_Sql = "SELECT 1 FROM " + as_TableName + " WHERE " + as_FieldName + " IN (" + ls_Cond + ")";
		if (MGlobal.isEmpty(CSqlHandle.getValueBySql(ls_Sql)))
			return ls_Seq;
		return getSeqValue(ao_Logon, al_UserID, null, as_SeqFormat, as_Code, as_Name, as_TableName, as_FieldName,
				al_Value);
	}

	/**
	 * 获取某一个表的具有固定格式的流水号值
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_UserID
	 *            用户标识
	 * @param as_SeqFormat
	 *            流水号格式，其中，可以具有以下内容的替换：[yyyy]、[yy]、[MM]、[M]、[dd]、[d]、[hh]、[h]、[
	 *            mm]<br>
	 *            [m]、[ss]、[s]、[UserID]、[UserID-n]-采用n位数加用户标识替换、[SEQID]、[SEQID-n
	 *            ]-采用n位数加流水号替换
	 * @param as_Code
	 *            流水号代码
	 * @param as_Name
	 *            流水号名称
	 * @param as_TableName
	 *            系统流水号对应系统表名称
	 * @param as_FieldName
	 *            系统流水号对应系统表中流水号列名称
	 * @param al_Value
	 *            需要读取多少个系统流水号
	 * @return
	 */
	public static String getSeqValue(CLogon ao_Logon, int al_UserID, String as_SeqFormat, String as_Code,
			String as_Name, String as_TableName, String as_FieldName, int al_Value) {
		String ls_Format = replaceSeqFormatValue(as_SeqFormat, al_UserID);
		// String ls_Code = replaceSeqFormatValue(as_Code, al_UserID);
		int li_SeqValue = getSeqValue(ao_Logon, as_Code, as_Name, false, al_Value, null, null, null, null);
		if (li_SeqValue == 0)
			return "";
		String ls_Seq = "";
		if (al_Value == 1) {
			ls_Seq = replaceSeqValue(ls_Format, "SEQID", String.valueOf(li_SeqValue), "[", "]");
		} else {
			for (int i = 0; i < al_Value; i++) {
				ls_Seq += replaceSeqValue(ls_Format, "SEQID", String.valueOf(li_SeqValue + i), "[", "]") + ";";
			}
		}
		if (MGlobal.isEmpty(as_TableName))
			return ls_Seq;

		String ls_Cond = MGlobal.replace(MGlobal.replace(ls_Seq, "'", "''"), ";", ",");
		if (MGlobal.right(ls_Cond, 1).equals(","))
			ls_Cond = MGlobal.left(ls_Cond, ls_Cond.length() - 1);
		ls_Cond = "'" + MGlobal.replace(ls_Cond, ",", "','") + "'";
		String ls_Sql = "SELECT 1 FROM " + as_TableName + " WHERE " + as_FieldName + " IN (" + ls_Cond + ")";
		if (MGlobal.isEmpty(CSqlHandle.getValueBySql(ls_Sql)))
			return ls_Seq;
		return getSeqValue(ao_Logon, al_UserID, as_SeqFormat, as_Code, as_Name, as_TableName, as_FieldName, al_Value);
	}

	/**
	 * 替换流水号值
	 * 
	 * @param as_SeqFormat
	 *            流水号格式
	 * @param al_UserID
	 *            用户标识
	 * @return
	 */
	private static String replaceSeqFormatValue(String as_SeqFormat, int al_UserID) {
		Date ld_Date = MGlobal.getNow();
		String ls_Format = MGlobal.replace(as_SeqFormat, "[yyyy]", String.valueOf(MGlobal.getYear(ld_Date)));
		ls_Format = MGlobal.replace(ls_Format, "[yy]", MGlobal.right(String.valueOf(MGlobal.getYear(ld_Date)), 2));
		ls_Format = MGlobal.replace(ls_Format, "[MM]",
				MGlobal.right("0" + String.valueOf(MGlobal.getMonth(ld_Date)), 2));
		ls_Format = MGlobal.replace(ls_Format, "[M]", String.valueOf(MGlobal.getMonth(ld_Date)));
		ls_Format = MGlobal.replace(ls_Format, "[dd]", MGlobal.right(String.valueOf(MGlobal.getDay(ld_Date)), 2));
		ls_Format = MGlobal.replace(ls_Format, "[d]", String.valueOf(MGlobal.getDay(ld_Date)));
		ls_Format = MGlobal.replace(ls_Format, "[hh]", MGlobal.right(String.valueOf(MGlobal.getHour(ld_Date)), 2));
		ls_Format = MGlobal.replace(ls_Format, "[h]", String.valueOf(MGlobal.getHour(ld_Date)));

		ls_Format = MGlobal.replace(ls_Format, "[mm]",
				MGlobal.right("0" + String.valueOf(MGlobal.getMinute(ld_Date)), 2));
		ls_Format = MGlobal.replace(ls_Format, "[m]", String.valueOf(MGlobal.getMinute(ld_Date)));
		ls_Format = MGlobal.replace(ls_Format, "[ss]",
				MGlobal.right("0" + String.valueOf(MGlobal.getSecond(ld_Date)), 2));
		ls_Format = MGlobal.replace(ls_Format, "[s]", String.valueOf(MGlobal.getSecond(ld_Date)));

		return replaceSeqValue(ls_Format, "UserID", String.valueOf(al_UserID), "[", "]");
	}

	/**
	 * 替换具有如此格式的内容*****[替换代码-n]******
	 * 
	 * @param as_SeqContent
	 *            原始内容
	 * @param as_Code
	 *            替换代码
	 * @param as_ReplaceValue
	 *            替换内容
	 * @param as_LeftSign
	 *            左符号
	 * @param as_RightSign
	 *            右符号
	 * @return
	 */
	private static String replaceSeqValue(String as_SeqContent, String as_Code, String as_ReplaceValue,
			String as_LeftSign, String as_RightSign) {
		String ls_Value = MGlobal.replace(as_SeqContent, as_LeftSign + as_Code + as_RightSign, as_ReplaceValue);
		int i = ls_Value.indexOf(as_LeftSign + as_Code + "-");
		if (i == -1)
			return ls_Value;
		int j = ls_Value.indexOf(as_RightSign, i);
		if (j == -1)
			return ls_Value;
		int li_Length = as_Code.length() + as_LeftSign.length() + as_RightSign.length();
		int k = Integer.parseInt(ls_Value.substring(i + li_Length, j));
		ls_Value = ls_Value.replace(ls_Value.substring(i, j + 1),
				MGlobal.right(MGlobal.addTexts(k, "0") + as_ReplaceValue, k));
		return ls_Value;
	}

	/**
	 * 取系统或用户定义的流水号
	 * 
	 * @param as_Code
	 *            流水号代码
	 * @param as_Name
	 *            流水号名称
	 * @param ab_SystemSeq
	 *            是否系统流水号
	 * @param as_TableName
	 *            系统流水号对应系统表名称
	 * @param as_FieldName
	 *            系统流水号对应系统表中流水号列名称
	 * @param al_Value
	 *            需要读取多少个系统流水号
	 * @return
	 * @throws SQLException
	 */
	public int getSeqValueBySet(String as_Code, String as_Name, boolean ab_SystemSeq, String as_TableName,
			String as_FieldName, int al_Value) {
		try {
			// 错误：非法的系统流水号代码
			if (as_Code == null || as_Code.equals(""))
				return 0;

			String ls_Sql = "SELECT SequenceCode, SequenceName, SequenceType, SequenceValue FROM SysSequence WHERE SequenceCode = '"
					+ as_Code.replaceAll("'", "''") + "'";
			Map<String, Object> lo_Set = CSqlHandle.getJdbcTemplate().queryForMap(ls_Sql, false);
			int ll_Seq = 0;
			if (lo_Set == null)
				return 0;
			if (lo_Set.size() > 0) {
				ll_Seq = (Integer) lo_Set.get("SequenceValue");
				int SequenceValue = ll_Seq + (al_Value > 1 ? al_Value : 1);
				CSqlHandle.getJdbcTemplate().update("update SysSequence set SequenceValue = " + SequenceValue
						+ "where SequenceCode = '" + as_Code.replaceAll("'", "''") + "'");
				// lo_Set.updateInt("SequenceValue", ll_Seq + (al_Value > 1 ?
				// al_Value : 1));
				// lo_Set.updateRow();
			} else {
				lo_Set.put("SequenceCode", as_Code);
				lo_Set.put("SequenceName", (as_Name == null || as_Name.equals("") ? as_Code : as_Name));
				lo_Set.put("SequenceType", (ab_SystemSeq ? 0 : 1));
				lo_Set.put("SequenceValue", (al_Value > 1 ? al_Value + 1 : 2));
				if (ab_SystemSeq) {
					if (as_TableName != null && as_TableName != "" && as_FieldName != null && as_FieldName != "")
						ls_Sql = "SELECT MAX(" + as_FieldName + ") AS MaxValue FROM " + as_TableName;
					ls_Sql = CSqlHandle.getValueBySql(ls_Sql);
					if (ls_Sql != "" && ls_Sql != "") {
						lo_Set.put("SequenceValue", Integer.parseInt(ls_Sql) + (al_Value > 1 ? al_Value + 1 : 2));
					}
				}
				CSqlHandle.getJdbcTemplate().update(
						"insert into SysSequence (SequenceCode, SequenceName, SequenceType, SequenceValue)value(?,?,?,?)",
						lo_Set);

				// lo_Set.moveToInsertRow();
				// lo_Set.updateString("SequenceCode", as_Code);
				// lo_Set.updateString("SequenceName", (as_Name == null ||
				// as_Name.equals("") ? as_Code : as_Name));
				// lo_Set.updateInt("SequenceType", (ab_SystemSeq ? 0 : 1));
				// lo_Set.updateInt("SequenceValue", (al_Value > 1 ? al_Value +
				// 1 : 2));
				// if (ab_SystemSeq) {
				// if (as_TableName != null && as_TableName != "" &&
				// as_FieldName != null && as_FieldName != "")
				// ls_Sql = "SELECT MAX(" + as_FieldName + ") AS MaxValue FROM "
				// + as_TableName;
				// ls_Sql = CSqlHandle.getValueBySql(ls_Sql);
				// if (ls_Sql != "" && ls_Sql != "") {
				// lo_Set.updateInt("SequenceValue", Integer.parseInt(ls_Sql) +
				// (al_Value > 1 ? al_Value + 1 : 2));
				// }
				//
				// }
				// lo_Set.insertRow();
			}
			return ll_Seq + 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * 取两个日期之间的工作日
	 * 
	 * @param ad_StartDate
	 *            开始时间
	 * @param ad_StopDate
	 *            结束时间
	 * @param ai_Type
	 *            计算节假日类型： =1 - 排除普通节假日； =2 - 排除特殊节假日； =4 - 包含调整工作日；
	 * @return
	 */
	public int getWorkdays(Date ad_StartDate, Date ad_StopDate, int ai_Type) {
		if (MGlobal.compareDate(MGlobal.getDate(ad_StartDate, 1, 0, 0, 0), ad_StopDate) == 1)
			return 0;

		int li_Day = 0;
		int li_Value = 1;

		while (true) {
			Date ld_Value = MGlobal.getDate(ad_StartDate, li_Value, 0, 0, 0);
			if (MGlobal.compareDate(ld_Value, ad_StopDate) == 1)
				return li_Day;

			// 判断ad_StartDate + li_Value是否为工作日
			if (isWorkingDate(ld_Value, ai_Type)) {
				li_Day++;
				li_Value++;
			}
		}
	}

	/**
	 * 取一个日期之后的n工作日的日期
	 * 
	 * @param ad_StartDate
	 *            开始时间
	 * @param ai_Days
	 *            间隔天数
	 * @param al_Minutes
	 *            间隔分钟：0-1439
	 * @param ai_Type
	 *            计算节假日类型： =1 - 排除普通节假日； =2 - 排除特殊节假日； =4 - 包含调整工作日；
	 * @return
	 */
	public Date getDateAfterWorks(Date ad_StartDate, int ai_Days, int al_Minutes, int ai_Type) {
		try {
			if (ai_Days < 0 || al_Minutes < 0)
				return ad_StartDate;
			Date ld_Date = ad_StartDate;
			for (int i = 1; i < ai_Days + al_Minutes % 1440; i++) {
				ld_Date = MGlobal.getDate(ld_Date, 1, 0, 0, 0);
				while (!isWorkingDate(ld_Date, ai_Type)) {
					ld_Date = MGlobal.getDate(ld_Date, 1, 0, 0, 0);
				}
			}
			ld_Date = MGlobal.getDate(ld_Date, 0, al_Minutes, 0, 0);
			while (!isWorkingDate(ld_Date, ai_Type)) {
				ld_Date = MGlobal.getDate(ld_Date, 1, 0, 0, 0);
			}

			return ld_Date;
		} catch (Exception ex) {
			return ad_StartDate;
		}
	}

	/**
	 * 判断日期是否是工作日
	 * 
	 * @param ad_Date
	 *            日期
	 * @param ai_Type
	 *            计算节假日类型： =1 - 排除普通节假日； =2 - 排除特殊节假日； =4 - 包含调整工作日；
	 * @return
	 */
	public Boolean isWorkingDate(Date ad_Date) {
		return isWorkingDate(ad_Date, 7);
	}

	/**
	 * 判断日期是否是工作日
	 * 
	 * @param ad_Date
	 *            日期
	 * @param ai_Type
	 *            判断类型： =1 - 排除普通节假日； =2 - 排除特殊节假日； =4 - 包含调整工作日；
	 * @return
	 */
	public Boolean isWorkingDate(Date ad_Date, int ai_Type) {
		if (ai_Type == 0)
			return true;

		// 正常节假日时间(1-7中的部分)
		String ls_CommondHoliday = ";" + this.Logon.getParameter("COMMOND_HOLIDAY").Value;
		// 特殊节假日(元旦、春节、五一、十一等)
		String ls_SpecialHoliday = ";" + this.Logon.getParameter("SPECIAL_HOLIDAY").Value.replaceAll("\n", ";");
		// 调整的工作日
		String ls_RejustWorkDay = ";" + this.Logon.getParameter("REJUST_WORKDAY").Value.replaceAll("\n", ";");

		String ls_Value = MGlobal.dateToString(ad_Date, "M.D");

		if ((ai_Type & 4) == 4) {
			if (ls_RejustWorkDay.indexOf(";" + ls_Value + ";") > 0)
				return true;
		}
		// MGlobal
		if ((ai_Type & 1) == 1) {
			if (ls_CommondHoliday.indexOf(";" + "" + ";") > -1)
				return false;
		}

		if ((ai_Type & 2) == 2) {
			if (ls_SpecialHoliday.indexOf(";" + ls_Value + ";") > -1)
				return false;
		}
		return true;
	}

}
