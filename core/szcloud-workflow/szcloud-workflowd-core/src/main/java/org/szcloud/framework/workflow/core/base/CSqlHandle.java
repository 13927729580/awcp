package org.szcloud.framework.workflow.core.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.PlatformTransactionManager;
import org.szcloud.framework.workflow.core.core.ApplicationContextHelper;
import org.szcloud.framework.workflow.core.emun.EDatabaseType;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 数据库处理类
 */
public class CSqlHandle {
	private static JdbcTemplate sqlServerJdbcTemplate = null;

	public static PlatformTransactionManager getTransactionManager() {
		return ApplicationContextHelper.getBean("txManager");
	}

	public static JdbcTemplate getJdbcTemplate() {
		if (sqlServerJdbcTemplate == null)
			sqlServerJdbcTemplate = ApplicationContextHelper
					.getBean("jdbcTemplate");
		return sqlServerJdbcTemplate;
	}
	
//	public static String getString(Map<String,Object> row,String key)
//	{
//		if(row.get(key)!=null && row.get(key).getClass().equals(String.class))
//			return (String)row.get(key);
//		return "";
//	}
//	
//	public static int getInt(Map<String,Object> row,String key)
//	{
//		if(row.get(key)!=null && row.get(key)getClass().equals(Integer.class))
//			return (Integer)row.get(key);
//		return -1;
//	}
//	public static Object getObject(Map<String,Object> row,String key)
//	{
//		return row.get(key);
//	}
	// public static ResultSet queryResult(final String sql) {
	// PreparedStatement ps = null;
	// try {
	// ps = getJdbcTemplate().getDataSource().getConnection()
	// .prepareStatement(sql);
	// ps.executeQuery();
	// return ps.getResultSet();
	// } catch (SQLException e) {
	// if (ps != null)
	// try {
	// ps.close();
	// } catch (SQLException e1) {
	// e1.printStackTrace();
	// }
	// }
	// return null;
	// }

//	public static Map<String, Object> queryResult(String sql) {
//		return getJdbcTemplate().queryForList(sql, args)
//	}
	

	/**
	 * 数据库连接代码，这个是为业务系统应用所设置，正常情况下无需使用
	 */
	public static String ConnCode = "";

	/**
	 * 数据库类型
	 */
	public static int DbType = EDatabaseType.MSSQL;

	/**
	 * 数据库服务器
	 */
	private static String ms_Server = "(local)";

	/**
	 * 数据库名称
	 */
	private static String ms_Database = "CMFLY_DEVELOP_GROUP";

	/**
	 * 数据库访问账号
	 */
	private static String ms_User = "sa";
	/**
	 * 数据库访问密码
	 */
	private static String ms_Password = "auto@1234";
	/**
	 * 数据库访问端口号
	 */
	private static int mi_Port = 1433;

	/**
	 * 内存变量：分页获取数据时的总记录数
	 */
	public static int TotalCount = 0;

	/**
	 * 根据Sql语句获取内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @return 返回内容
	 */
	public static String getValueBySql(String as_Sql) {
		return getValueBySql(as_Sql, "");
	}

	/**
	 * 根据Sql语句获取内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Field
	 *            字段名称
	 * @return 返回内容
	 */
	public static String getValueBySql(String as_Sql, String as_Field) {
		try {
			List<Map<String, Object>> lo_Set = getJdbcTemplate().queryForList(as_Sql);
			String ls_Value = "";
			if (lo_Set!=null && lo_Set.size()>0) {
				if (as_Field == null || as_Field.equals("")) {
					ls_Value = MGlobal.readString(lo_Set.get(0),1);
				} else {
					ls_Value = MGlobal.readString(lo_Set.get(0),as_Field);
				}
			}
			lo_Set = null;
			return ls_Value;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据Sql语句获取多项内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @return 返回多项内容
	 */
	public static String getValuesBySql(String as_Sql) {
		return getValuesBySql(as_Sql, "", ";", true);
	}

	/**
	 * 根据Sql语句获取多项内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Field
	 *            字段名称
	 * @param as_Split
	 *            分隔符号
	 * @return 返回多项内容
	 */
	public static String getValuesBySql(String as_Sql, String as_Field) {
		return getValuesBySql(as_Sql, as_Field, "", true);
	}

	/**
	 * 根据Sql语句获取多项内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Field
	 *            字段名称
	 * @param as_Split
	 *            分隔符号
	 * @return 返回多项内容
	 */
	public static String getValuesBySql(String as_Sql, String as_Field,
			String as_Split) {
		return getValuesBySql(as_Sql, as_Field, as_Split, true);
	}

	/**
	 * 根据Sql语句获取多项内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Field
	 *            字段名称
	 * @param as_Split
	 *            分隔符号
	 * @param ab_LastSign
	 *            最后一个是否增加符号
	 * @return 返回多项内容
	 */
	public static String getValuesBySql(String as_Sql, String as_Field,
			String as_Split, Boolean ab_LastSign) {
		try {
			List<Map<String,Object>> lo_Set = getJdbcTemplate().queryForList(as_Sql);
			StringBuffer lo_Buffer = new StringBuffer();
			int i=0;
			for(Map<String,Object> row : lo_Set){
				if (as_Field == null || as_Field.equals("")) {
					lo_Buffer.append(MGlobal.readString(row,1));
				} else {
					lo_Buffer.append(MGlobal.readString(row,as_Field));
				}
				if (ab_LastSign && (i==(lo_Set.size()-1)))
					lo_Buffer.append(as_Split);
				i++;
			}
			lo_Set = null;
			return lo_Buffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据Sql语句获取结果集
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @return 返回结果集
	 */
	public static List<Map<String,Object>> getSetBySql(String as_Sql) {
		return getSetBySql(as_Sql, 0);
	}

	/**
	 * 根据Sql语句获取结果集
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param ab_ReadOnly
	 *            是否只读
	 * @return 返回结果集
	 */
	public static List<Map<String,Object>> getSetBySql(String as_Sql, boolean ab_ReadOnly) {
		return getSetBySql(as_Sql, (ab_ReadOnly ? 0 : 1));
	}

	/**
	 * 根据Sql语句获取结果集
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param ai_Type
	 *            获取类型：=0-只读；=1-自动更新；=2-非自动更新；=3-不设置更新标志；
	 * @return 返回结果集
	 */
	public static List<Map<String,Object>> getSetBySql(String as_Sql, int ai_Type) {
		try {
			if (ai_Type == 0) {
				List<Map<String,Object>> lo_Set = getJdbcTemplate().queryForList(as_Sql);
				return lo_Set;
			} else {
				// if (ai_Type == 1)
				// ao_Connection.setAutoCommit(true);
				// if (ai_Type == 2)
				// ao_Connection.setAutoCommit(false);
				// Statement lo_State =
				// ao_Connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				// ResultSet.CONCUR_UPDATABLE);
				List<Map<String,Object>> lo_Set = getJdbcTemplate().queryForList(as_Sql);
				return lo_Set;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 执行单句SQL语句
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @return 返回是否执行成功的标记
	 */
	public static boolean execSql(String as_Sql) {
		return execSql(as_Sql, "", false);
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Split
	 *            分隔符号，为空表示执行单句Sql语句
	 * @return 返回是否执行成功的标记
	 */
	public static boolean execSql(String as_Sql, String as_Split) {
		return execSql(as_Sql, as_Split, false);
	}

	/**
	 * 执行SQL语句
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Split
	 *            分隔符号，为空表示执行单句Sql语句
	 * @param as_Raise
	 *            如果出错是否抛出异常
	 * @return 返回是否执行成功的标记
	 */
	public static boolean execSql(String as_Sql, String as_Split,
			Boolean as_Raise) {
		try {
			// int li_Result = 0;
			if (as_Split == null || as_Split.equals("")) {
				getJdbcTemplate().execute(as_Sql);
			} else {
				try {
					List sqlList = new ArrayList();
					for (String ls_Sql : as_Sql.split(as_Split)) {
						if (ls_Sql != "") {
							sqlList.add(as_Sql);
						}
					}
					getJdbcTemplate().batchUpdate(
							(String[]) sqlList.toArray(new String[sqlList
									.size()]));
				} catch (Exception ex) {
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 在批量执行Sql语句动作时单句Sql语句执行
	 * 
	 * @param as_Sql
	 * @throws SQLException
	 */
	// public void execBatchSql(String as_Sql) throws SQLException {
	// PreparedStatement lo_State = lo_Conn.prepareStatement(as_Sql);
	// lo_State.addBatch();
	// lo_State.executeBatch();
	// }

	/**
	 * 在批量执行Sql语句动作时单句增加Sql语句执行
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @throws SQLException
	 */
	// public PreparedStatement addBatchSql(String as_Sql) throws SQLException {
	// return addBatchSql(as_Sql, false);
	// }

	/**
	 * 在批量执行Sql语句动作时单句增加Sql语句执行
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param ab_ExecuteBatch
	 *            是否执行批操作
	 * @throws SQLException
	 */
	// public PreparedStatement addBatchSql(String as_Sql, boolean
	// ab_ExecuteBatch) throws SQLException {
	// PreparedStatement lo_State =
	// this.getConnection().prepareStatement(as_Sql);
	// lo_State.addBatch();
	// if (ab_ExecuteBatch)
	// lo_State.executeBatch();
	// return lo_State;
	// }

	/**
	 * 在批量执行Sql语句动作时单句增加Sql语句执行
	 * 
	 * @param ao_Connection
	 *            数据库连接对象
	 * @param as_Sql
	 *            Sql语句
	 * @param ab_ExecuteBatch
	 *            是否执行批操作
	 * @throws SQLException
	 */
	// public static String addBatchSql(String as_Sql, boolean ab_ExecuteBatch)
	// throws SQLException {
	// PreparedStatement lo_State = ao_Connection.prepareStatement(as_Sql);
	// lo_State.addBatch();
	// if (ab_ExecuteBatch)
	// lo_State.executeBatch();
	// return lo_State;
	// }

	/**
	 * 根据SQL语句获取Xml内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @return 返回Xml内容
	 */
	public static String getXmlBySql(String as_Sql) {
		return getXmlBySql(as_Sql, "", "", 0);
	}

	/**
	 * 根据SQL语句获取Xml内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param ai_Type
	 *            类型：<br>
	 *            =0-按属性内容方式；<br>
	 *            =1-按多句方式（增加根节点<Root>..</Root>）；<br>
	 *            =2-按节点内容方式；<br>
	 *            =3-按多句节点方式（增加根节点<Root>..</Root>）
	 * @return 返回Xml内容
	 */
	public static String getXmlBySql(String as_Sql, int ai_Type) {
		return getXmlBySql(as_Sql, "", "", ai_Type);
	}

	/**
	 * 根据SQL语句获取Xml内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Table
	 *            Xml节点名称，为空采用缺省
	 * @return 返回Xml内容
	 */
	public static String getXmlBySql(String as_Sql, String as_Table) {
		return getXmlBySql(as_Sql, as_Table, "", 0);
	}

	/**
	 * 根据SQL语句获取Xml内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Table
	 *            Xml节点名称，为空采用缺省
	 * @param ai_Type
	 *            类型：<br>
	 *            =0-按属性内容方式；<br>
	 *            =1-按多句方式（增加根节点<Root>..</Root>）；<br>
	 *            =2-按节点内容方式；<br>
	 *            =3-按多句节点方式（增加根节点<Root>..</Root>）
	 * @return
	 */
	public static String getXmlBySql(String as_Sql, String as_Table, int ai_Type) {
		return getXmlBySql(as_Sql, as_Table, "", 0);
	}

	/**
	 * 根据SQL语句获取Xml内容
	 * 
	 * @param as_Sql
	 *            Sql语句
	 * @param as_Table
	 *            Xml节点名称，为空采用缺省
	 * @param as_Fields
	 *            需要显示的字段，为空表示采用缺省方式，采用【;】分隔，最后一个不加【;】
	 * @param ai_Type
	 *            类型：<br>
	 *            =0-按属性内容方式；<br>
	 *            =1-按多句方式（增加根节点<Root>..</Root>）；<br>
	 *            =2-按节点内容方式；<br>
	 *            =3-按多句节点方式（增加根节点<Root>..</Root>）
	 * @return
	 */
	public static String getXmlBySql(String as_Sql, String as_Table,
			String as_Fields, int ai_Type) {
		try {
			SqlRowSet lo_Set = getJdbcTemplate().queryForRowSet(as_Sql);
			String ls_Table = as_Table;
			if (as_Table == null || as_Table.equals(""))
				ls_Table = getNameBySql(as_Sql);
			Document lo_Xml = MXmlHandle.LoadXml("<" + ls_Table + " />");
			String ls_Fields = as_Fields;
			if (ls_Fields == null || ls_Fields.equals("")) {
				ls_Fields = getColumnName(lo_Set);
			} else {
				ls_Fields = ls_Fields.replaceAll(",", ";");
				if (ls_Fields.substring(ls_Fields.length() - 1,
						ls_Fields.length()) == ";")
					ls_Fields = ls_Fields.substring(0, ls_Fields.length() - 1);
			}
			String[] ls_Array = ls_Fields.split(";");
			while(lo_Set.next()) {
				Element lo_Node = lo_Xml.createElement(ls_Table);
				lo_Xml.getDocumentElement().appendChild(lo_Node);
				if (ai_Type < 2) {
					for (String ls_Field : ls_Array) {
						if (lo_Set.getObject(ls_Field) != null)
							lo_Node.setAttribute(ls_Field,
									lo_Set.getString(ls_Field));
					}
				} else {
					for (String ls_Field : ls_Array) {
						Element lo_Child = lo_Xml.createElement(ls_Field);
						lo_Node.appendChild(lo_Child);
						if (lo_Set.getObject(ls_Field) != null)
							lo_Child.setTextContent(lo_Set.getString(ls_Field));
					}
				}
			}
			lo_Set = null;
			if (ai_Type == 1 || ai_Type == 3) {
				ls_Table = MXmlHandle.getXml(lo_Xml);
			} else {
				ls_Table = MXmlHandle.getInterXml(lo_Xml);
			}
			lo_Xml = null;
			return ls_Table;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 更新结果集内容
	 * 
	 * @param ao_Set
	 *            结果集
	 * @return
	 */
	/*public static boolean updateSet(ResultSet ao_Set) {
		if (ao_Set == null)
			return false;
		try {

			return getJdbcTemplate().update(ao_Set.toString()) > 0;
		} catch (Exception ex) {
			return false;
		}
	}*/

	/**
	 * 批量更新结果集内容列表
	 * 
	 * @param ao_Sets
	 *            结果集列表
	 * @return
	 */
	// public boolean updateSets(List<ResultSet> ao_Sets) {
	// // int[] counts = null;
	// try {
	// // 关闭自动提交
	// // 判断传入对象是否为null;
	// if (ao_Sets != null && ao_Sets.size() > 0) {
	// for (int i = 0; i < ao_Sets.size(); i++) {
	// lo_State = lo_Conn.prepareStatement(ao_Sets.get(i).toString());
	// // 把这条执行语句加到PreparedStatement对象的批处理命令中 ;
	// lo_State.addBatch();
	// }
	// // 把以上添加到批处理命令中的所有命令一 次性提交给数据库来执行;
	// // counts = lo_State.executeBatch();
	// lo_State.executeBatch();
	// // 提交SQL语句的操作
	// lo_Conn.commit();
	// return true;
	// }
	// } catch (Exception ex) {
	// try {
	// // 回滚当前所有SQL语句的操作
	// lo_Conn.rollback();
	// logger.debug("操作已回滚!");
	// return false;
	// } catch (SQLException e) {
	// logger.debug("回滚过程出现异常");
	// e.printStackTrace();
	// return false;
	// }
	// }
	// return false;
	// }

	/**
	 * 批量更新数据（含结果集和SQL语句）列表
	 * 
	 * @param ao_Datas
	 *            对象列表
	 * @return
	 */
	// public boolean updateDatas(List<Object> ao_Datas) {
	// // int[] counts = null;
	// Connection lo_Conn = getConnection();
	// Statement lo_State = null;
	// // PreparedStatement lo_PState=null;
	// try {
	// // 关闭自动提交
	// lo_Conn.setAutoCommit(false);
	// lo_State = lo_Conn.createStatement();
	// // 判断传入对象是否为null;
	// if (ao_Datas != null && ao_Datas.size() > 0) {
	// for (int i = 0; i < ao_Datas.size(); i++) {
	// // 判断传入对象是否是增、删、改SQL语句;
	// if (ao_Datas.get(i).toString().indexOf("update") >= 0 ||
	// ao_Datas.get(i).toString().indexOf("delete") >= 0
	// || ao_Datas.get(i).toString().indexOf("insert") >= 0) {
	// // 把这条SQL语句加到Statement对象的批处理命令中 ;
	// lo_State.addBatch(ao_Datas.get(i).toString());
	// } else {
	// lo_State = lo_Conn.prepareStatement(ao_Datas.get(i).toString());
	// // 把这条执行语句加到PreparedStatement对象的批处理命令中 ;
	// ((PreparedStatement) lo_State).addBatch();
	//
	// // lo_PState=lo_Conn.prepareStatement(ao_Datas.get(i).toString());
	// // lo_PState.addBatch();
	//
	// }
	// }
	// // 把以上添加到批处理命令中的所有命令一 次性提交给数据库来执行;
	// // counts = lo_State.executeBatch();
	// lo_State.executeBatch();
	// // 提交SQL语句的操作
	// lo_Conn.commit();
	// return true;
	// }
	// } catch (Exception ex) {
	// try {
	// // 回滚当前所有SQL语句的操作
	// lo_Conn.rollback();
	// logger.debug("操作已回滚!");
	// return false;
	// } catch (SQLException e) {
	// logger.debug("回滚过程出现异常");
	// e.printStackTrace();
	// return false;
	// }
	// }
	// return false;
	// }

	/**
	 * 根据SQL语句获取SQL更新操作对象
	 * 
	 * @param as_Sql
	 *            SQL语句
	 * @param ai_Type
	 *            类型：=0-不设置自动提交处理；=1-设置自动提交处理为false；=2-设置自动提交处理为true；
	 * @return
	 * @throws SQLException
	 */
	public static String getState(String as_Sql, int ai_Type)
			throws SQLException {
		// if (ai_Type > 0)
		// lo_Conn.setAutoCommit((ai_Type == 2));
		return as_Sql;// lo_Conn.prepareStatement(as_Sql);
	}

	/**
	 * 获取结果集列数量
	 * 
	 * @param ao_Set
	 *            结果集
	 * @return 列数量
	 * @throws SQLException
	 */
	public static int getColumnCount(SqlRowSet ao_Set) throws SQLException {
		return ao_Set.getMetaData().getColumnCount();
	}

	/**
	 * 获取结果集列名称，使用【;】分隔，最后一个不需要【;】
	 * 
	 * @param ao_Set
	 *            结果集
	 * @return 列名称
	 * @throws SQLException
	 */
	public static String getColumnName(SqlRowSet ao_Set) throws SQLException {
		String ls_Name = "";
		int li_Count = getColumnCount(ao_Set);
		for (int i = 0; i < li_Count; i++) {
			ls_Name += ao_Set.getMetaData().getColumnName(i)
					+ (i < li_Count - 1 ? ";" : "");
		}
		return ls_Name;
	}

	/**
	 * 获取结果集列标签，使用【;】分隔，最后一个不需要【;】
	 * 
	 * @param ao_Set
	 *            结果集
	 * @return 列标签
	 * @throws SQLException
	 */
	public static String getColumnLabel(SqlRowSet ao_Set) throws SQLException {
		String ls_Name = "";
		int li_Count = getColumnCount(ao_Set);
		for (int i = 0; i < li_Count; i++) {
			ls_Name += ao_Set.getMetaData().getColumnLabel(i)
					+ (i < li_Count - 1 ? ";" : "");
		}
		return ls_Name;
	}

	/**
	 * 根据SQL语句获取表名称（只针对正常的SQL语句有效，如：SELECT * FROM Table 。。。）
	 * 
	 * @param as_Sql
	 * @return
	 */
	public static String getNameBySql(String as_Sql) {
		int i = as_Sql.toUpperCase().indexOf(" FROM ");
		if (i == -1)
			return "";
		String ls_Text = as_Sql.substring(i + 6, as_Sql.length());
		i = ls_Text.indexOf(" ");
		if (i == -1)
			return ls_Text;
		return ls_Text.substring(0, i);
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param as_TableName
	 *            表名称
	 * @return
	 */
	public static boolean isExistTable(String as_TableName) {
		try {
			String ls_Sql = "SELECT COUNT(*) FROM " + as_TableName;
			List<Map<String,Object>> lo_Set = getJdbcTemplate().queryForList(ls_Sql);
			if (lo_Set == null)
				return false;
			lo_Set = null;
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 获取行（row）数量
	 * 
	 * @param ao_Set
	 * @return
	 */
	public static int getCount(ResultSet ao_Set) {
		try {
			ao_Set.last();
			return ao_Set.getRow();
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 移动到某一行
	 * 
	 * @param ao_Set
	 * @param ai_Index
	 */
	public static boolean moveTo(ResultSet ao_Set, int ai_Index) {
		try {
			int li_Count = getCount(ao_Set);
			if (li_Count == 0)
				return false;
			if (ai_Index <= 1) {
				ao_Set.first();
				return true;
			}
			if (ai_Index >= li_Count) {
				ao_Set.last();
				return true;
			}
			ao_Set.first();
			int li_Index = 1;
			while (li_Index < ai_Index) {
				ao_Set.next();
				li_Index++;
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 查找
	 * 
	 * @param ao_Set
	 * @param as_Name
	 * @param ao_Object
	 * @return
	 */
	public static int find(ResultSet ao_Set, String as_Name, Object ao_Object) {
		try {
			ao_Set.first();
			boolean lb_Boolean = true;
			while (lb_Boolean) {
				if (ao_Set.getObject(as_Name) == null
						&& (ao_Object == null || ao_Object.toString()
								.equals("")))
					return ao_Set.getRow();
				if (ao_Set.getObject(as_Name).toString() == ao_Object
						.toString())
					return ao_Set.getRow();
				lb_Boolean = ao_Set.next();
			}
			return 0;
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 获取数据库表格中某一列的最大值+1
	 * 
	 * @param as_Table
	 *            表格名称
	 * @param as_Field
	 *            列名称
	 * @param as_Condition
	 *            限制条件，针对二级表使用
	 * @return
	 */
	public static int getTableFieldMaxValue(String as_Table, String as_Field) {
		return getTableFieldMaxValue(as_Table, as_Field, null);
	}

	/**
	 * 获取数据库表格中某一列的最大值+1
	 * 
	 * @param as_Table
	 *            表格名称
	 * @param as_Field
	 *            列名称
	 * @param as_Condition
	 *            限制条件，针对二级表使用
	 * @return
	 */
	public static int getTableFieldMaxValue(String as_Table, String as_Field,
			String as_Condition) {
		try {
			String ls_Sql = MessageFormat.format(
					"SELECT MAX({0}) AS MaxValue FROM {1}", as_Field, as_Table);
			if (!MGlobal.isEmpty(as_Condition))
				ls_Sql += " WHERE " + as_Condition;
			ls_Sql = CSqlHandle.getValueBySql(ls_Sql);
			if (MGlobal.isEmpty(ls_Sql))
				ls_Sql = "0";
			return Integer.parseInt(ls_Sql) + 1;
		} catch (Exception ex) {
			return 1;
		}
	}

	/**
	 * 获取MSSQL排序的结果集合
	 * 
	 * @param as_Table
	 *            表或视图名称
	 * @param as_PrimaryKey
	 *            主键列
	 * @param as_Fields
	 *            字段名称连接，使用[,]分隔，最后一个不为[,]；不能有空格
	 * @param as_Condition
	 *            限制条件
	 * @param as_Order
	 *            排列顺序，包含反向符号；
	 * @param al_CurPage
	 *            当前页
	 * @param ai_PageRows
	 *            每页数目
	 * @return 记录结果集和总记录数(this.TotalCount)[输出参数]；
	 */
	public static List<Map<String,Object>> getSetAsPage(String as_Table, String as_PrimaryKey,
			String as_Fields, String as_Condition, String as_Order,
			int al_CurPage, int ai_PageRows) {
		if (CSqlHandle.DbType == EDatabaseType.MSSQL) {
			return getMSSQLSetAsPage(as_Table, as_PrimaryKey, as_Fields,
					as_Condition, as_Order, al_CurPage, ai_PageRows);
		} else if (CSqlHandle.DbType == EDatabaseType.ORACLE) {
			return getOracleSetAsPage(as_Table, as_PrimaryKey, as_Fields,
					as_Condition, as_Order, al_CurPage, ai_PageRows);
		} else {
			return getMSSQLSetAsPage(as_Table, as_PrimaryKey, as_Fields,
					as_Condition, as_Order, al_CurPage, ai_PageRows);
		}
	}

	/**
	 * 获取MSSQL排序的结果集合
	 * 
	 * @param as_Table
	 *            表或视图名称
	 * @param as_PrimaryKey
	 *            主键列
	 * @param as_Fields
	 *            字段名称连接，使用[,]分隔，最后一个不为[,]；不能有空格
	 * @param as_Condition
	 *            限制条件
	 * @param as_Order
	 *            排列顺序，包含反向符号；
	 * @param al_CurPage
	 *            当前页
	 * @param ai_PageRows
	 *            每页数目
	 * @return 记录结果集和总记录数[输出参数]；
	 */
	private static List<Map<String,Object>> getMSSQLSetAsPage(String as_Table,
			String as_PrimaryKey, String as_Fields, String as_Condition,
			String as_Order, int al_CurPage, int ai_PageRows) {
		try {
			String ls_Cond = as_Condition;
			if (MGlobal.isExist(ls_Cond))
				ls_Cond = " WHERE " + ls_Cond;
			String ls_Sql = "SELECT COUNT(1) FROM " + as_Table + ls_Cond;
			CSqlHandle.TotalCount = Integer.parseInt(CSqlHandle
					.getValueBySql(ls_Sql));
			if (CSqlHandle.TotalCount == 0)
				return CSqlHandle.getSetBySql(MessageFormat.format(
						"SELECT {0} FROM {1} WHERE 1=2", as_Fields, as_Table));
			int ll_Start = (al_CurPage - 1) * ai_PageRows + 1;
			if (ll_Start < 1)
				ll_Start = 1;
			ls_Sql = "SELECT {0} FROM (SELECT ROW_NUMBER() OVER(ORDER BY {1}) AS DATA_ROW_NUM, {0} FROM {2}{3}) D WHERE DATA_ROW_NUM BETWEEN {4} AND {5}";
			ls_Sql = MessageFormat.format(ls_Sql, as_PrimaryKey, as_Order,
					as_Table, ls_Cond, String.valueOf(ll_Start),
					String.valueOf(ll_Start + ai_PageRows - 1));
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			ls_Cond = "";
			for(Map<String,Object> row : lo_Set) {
				ls_Cond += "'" + MGlobal.readString(row,1) + "',";
			}
			lo_Set = null;
			ls_Sql = MessageFormat.format("SELECT {0} FROM {1} WHERE ",
					as_Fields, as_Table);
			if (MGlobal.isEmpty(ls_Cond)) {
				ls_Sql += "1=2";
			} else {
				ls_Sql += MessageFormat.format("{0} IN ({1})", as_PrimaryKey,
						ls_Cond.substring(0, ls_Cond.length() - 1));
				if (MGlobal.isExist(as_Condition))
					ls_Sql += MessageFormat.format(" AND ({0})", as_Condition);
			}
			return CSqlHandle.getSetBySql(ls_Sql + " ORDER BY " + as_Order);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取Oracle排序的结果集合
	 * 
	 * @param as_Table
	 *            表或视图名称
	 * @param as_PrimaryKey
	 *            主键列
	 * @param as_Fields
	 *            字段名称连接，使用[,]分隔，最后一个不为[,]；不能有空格
	 * @param as_Condition
	 *            限制条件
	 * @param as_Order
	 *            排列顺序，包含反向符号；
	 * @param al_CurPage
	 *            当前页
	 * @param ai_PageRows
	 *            每页数目
	 * @return 记录结果集和总记录数[输出参数]；
	 */
	private static List<Map<String,Object>> getOracleSetAsPage(String as_Table,
			String as_PrimaryKey, String as_Fields, String as_Condition,
			String as_Order, int al_CurPage, int ai_PageRows) {
		try {
			String ls_Sql = "SELECT COUNT(1) FROM " + as_Table;
			if (MGlobal.isExist(as_Condition))
				ls_Sql += " WHERE " + as_Condition;
			CSqlHandle.TotalCount = Integer.parseInt(CSqlHandle
					.getValueBySql(ls_Sql));
			if (CSqlHandle.TotalCount == 0)
				return CSqlHandle.getSetBySql(MessageFormat.format(
						"SELECT {0} FROM {1} WHERE 1=2", as_Fields, as_Table));
			int ll_Start = (al_CurPage - 1) * ai_PageRows + 1;
			if (ll_Start < 1)
				ll_Start = 1;
			ls_Sql = "SELECT {0} FROM (SELECT ROWNUM AS DATA_ROW_NUM, {0} FROM {1} ORDER BY {2}) D WHERE DATA_ROW_NUM BETWEEN {3} AND {4}";
			ls_Sql = MessageFormat.format(ls_Sql, as_PrimaryKey, as_Table,
					as_Order, String.valueOf(ll_Start),
					String.valueOf(ll_Start + ai_PageRows - 1));
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			String ls_Cond = "";
			for(Map<String,Object> row : lo_Set) {
				ls_Cond += "'" + MGlobal.readString(row,1) + "',";
			}
			lo_Set = null;
			ls_Sql = MessageFormat.format("SELECT {0} FROM {1} WHERE ",
					as_Fields, as_Table);
			if (MGlobal.isEmpty(ls_Cond)) {
				ls_Sql += "1=2";
			} else {
				ls_Sql += MessageFormat.format("{0} IN ({1})", as_PrimaryKey,
						ls_Cond.substring(0, ls_Cond.length() - 1));
				if (MGlobal.isExist(as_Condition))
					ls_Sql += MessageFormat.format(" AND ({0})", as_Condition);
			}
			return CSqlHandle.getSetBySql(ls_Sql + " ORDER BY " + as_Order);
		} catch (Exception ex) {
			return null;
		}
	}

}
