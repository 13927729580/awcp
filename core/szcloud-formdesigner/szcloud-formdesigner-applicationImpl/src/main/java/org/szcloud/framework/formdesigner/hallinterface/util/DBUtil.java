package org.szcloud.framework.formdesigner.hallinterface.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DBUtil {

	private static Logger logger = Logger.getLogger(DBUtil.class);
	private static final long GET_CONN_THRESHOLD = 5 * 1000; // 5 seconds

	public static String getString(String sql) {
		return (String) getObject(sql, java.sql.Types.VARCHAR);
	}

	public static Double getDouble(String sql) {
		return (Double) getObject(sql, java.sql.Types.DOUBLE);
	}

	public static Integer getInt(String sql) {
		return (Integer) getObject(sql, java.sql.Types.INTEGER);
	}
	
	public static Long getLong(String sql) {
		return (Long) getObject(sql, java.sql.Types.BIGINT);
	}

	public static Short getShort(String sql) {
		return (Short) getObject(sql, java.sql.Types.SMALLINT);
	}

	public static Date getDate(String sql) {
		return (Date) getObject(sql, java.sql.Types.DATE);
	}

	public static Time getTime(String sql) {
		return (Time) getObject(sql, java.sql.Types.TIME);
	}

	public static Timestamp getTimestamp(String sql) {
		return (Timestamp) getObject(sql, java.sql.Types.TIMESTAMP);
	}

	public static Date getCurrentDate() {
		return getDate("SELECT CURRENT_DATE()");
	}

	public static Time getCurrentTime() {
		return getTime("SELECT CURRENT_TIME()");
	}

	public static Timestamp getCurrentTimestamp() {
		return getTimestamp("SELECT CURRENT_TIMESTAMP()");
	}

	public static List<String> getStringList(String sql) {
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		logger.debug(sql);
		try {
			con = BaseDao.getconn();
			st = con.createStatement();
			rs = st.executeQuery(sql);
			List<String> list = new ArrayList<String>();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
		} catch (Exception e) {
			logger.error(sql, e);
		} finally {
			DAO.close(con, st, rs);
		}
		return null;
	}

	/**
	 * 获取selectSql的查询结果集，用于查询多条记录
	 * 
	 * @param sql sql语句
	 * @return
	 * 
	 * @author Lon
	 * @date 2012-1-15 上午1:00:45
	 */
	public static List<Map<String, Object>> getMapList(String sql) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		logger.debug(sql);
		try {
			con = BaseDao.getconn();
			st = con.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData rsmt = rs.getMetaData();
			if (null == rsmt) {
				return null;
			}
			// 获取到查询结果的列数
			int col = rsmt.getColumnCount();
			if (col < 1) {
				return null;
			}
			// while控制行数
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				// for循环控制列数
				for (int i = 1; i <= col; i++) {
					// 将每行的数据放入到map中。key=列名；value=数据
					map.put(rsmt.getColumnName(i), rs.getObject(i));
				}
				result.add(map);
			}
		} catch (Exception e) {
			logger.error(sql, e);
		} finally {
			DAO.close(con, st, rs);
		}
		return result;
	}

	/**
	 * 获取selectSql的查询结果集，仅用于查询一条记录
	 * 
	 * @param sql sql语句
	 * @return
	 * 
	 * @author Lon
	 * @date 2012-1-15 上午1:35:06
	 */
	public static Map<String, Object> getOneRow(String sql) {
		List<Map<String, Object>> mapList = getMapList(sql);
		if (mapList==null||mapList.size()==0) {
			return null;
		}
		return getMapList(sql).get(0);
	}

	/**
	 * 获取selectSql的查询结果集，用于查询多条记录
	 */

	// public static List<Map<String, Object>> getMapList(String sql) {
	// logger.debug("getList, SQL :" + sql);
	// JdbcTemplate jc = new JdbcTemplate();
	// jc.setDataSource(DAO.getInstance().getDataSource());
	// return (List) jc.queryForList(sql);
	// }

	/**
	 * 获取selectSql的查询结果集，仅用于查询一条记录,且该字段为字符型。
	 */

	// public static Map<String, Object> getOneRow(String sql) {
	// logger.debug("getList, SQL :" + sql);
	// JdbcTemplate jc = new JdbcTemplate();
	// jc.setDataSource(DAO.getInstance().getDataSource());
	// List dbList = (List) jc.queryForList(sql);
	// if (dbList == null || dbList.size() < 1) {
	// return null;
	// }
	// Map<String, Object> aMap = (org.apache.commons.collections.map.ListOrderedMap) dbList.get(0);
	// logger.debug("getOneRow result :\n" + aMap);
	// return aMap;
	// }

	public static List<Object> getObjectList(String sql, int type) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		logger.debug(sql);
		try {
			con = BaseDao.getconn();;
			st = con.createStatement();
			rs = st.executeQuery(sql);
			List<Object> list = new ArrayList<Object>();
			while (rs.next()) {
				switch (type) {
				case java.sql.Types.INTEGER:
					int res = rs.getInt(1);
					list.add(new Integer(res));
					break;
				case java.sql.Types.FLOAT:
					float fRes = rs.getFloat(1);
					list.add(new Float(fRes));
					break;
				case java.sql.Types.VARCHAR:
					list.add(rs.getString(1));
					break;
				case java.sql.Types.VARBINARY:
					byte[] ba = rs.getBytes(1);
					list.add(ba);
				}
			}
			return list;
		} catch (Exception e) {
			logger.error(sql, e);
		} finally {
			DAO.close(con, st, rs);
		}
		return null;
	}

	public static Object getObject(String sql, int type) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		logger.debug(sql);
		try {
			con = BaseDao.getconn();;
			st = con.createStatement();
			rs = st.executeQuery(sql);
			Object o = null;
			if (rs.next()) {
				switch (type) {
				case java.sql.Types.INTEGER:
					o = rs.getInt(1);
					break;
				case java.sql.Types.SMALLINT:
					o = rs.getShort(1);
					break;
				case java.sql.Types.BIGINT:
					o = rs.getLong(1);
					break;
				case java.sql.Types.FLOAT:
					o = rs.getFloat(1);
					break;
				case java.sql.Types.VARCHAR:
					o = rs.getString(1);
					break;
				case java.sql.Types.DOUBLE:
					o = rs.getDouble(1);
				case java.sql.Types.DATE:
					o = rs.getDate(1);
					break;
				case java.sql.Types.TIME:
					o = rs.getTime(1);
					break;
				case java.sql.Types.TIMESTAMP:
					o = rs.getTimestamp(1);
					break;
				case java.sql.Types.VARBINARY:
					InputStream is = rs.getBinaryStream(1);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int len = 0;
					while ((len = is.read(buf)) > 0) {
						bos.write(buf, 0, len);
					}
					is.close();
					o = bos;
				}
			}
			return o;
		} catch (Exception e) {
			logger.error(sql, e);
		} finally {
			DAO.close(con, st, rs);
		}
		return null;
	}

	/**
	 * @param sql
	 * @return
	 */
	public static int update(String sql) {
		Connection con = null;
		Statement st = null;
		int res = 0;
		try {
			long now = System.currentTimeMillis();
			con = BaseDao.getconn();;
			long cost = System.currentTimeMillis() - now;
			if (cost > GET_CONN_THRESHOLD) { // for debugging "ClientAbortException"
				logger.info("##getConnectionCostTooMuchTime:" + cost + ":" + sql);
			}
			st = con.createStatement();
			res = st.executeUpdate(sql);
		} catch (Exception e) {
			logger.error(sql, e);
			e.printStackTrace();
		} finally {
			DAO.close(con, st, null);
		}
		return res;
	}

	
	/**
	 * 新增
	 * @param sql
	 * @return
	 */
	public static long insert(String sql) {
		logger.debug(sql);
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int res = -1;
		try {
			long now = System.currentTimeMillis();
			con = BaseDao.getconn();;
			long cost = System.currentTimeMillis() - now;
			if (cost > GET_CONN_THRESHOLD) {
				logger.info("##getConnectionCostTooMuchTime:" + cost + ":" + sql);
			}
			st = con.createStatement();
			st.executeUpdate(sql);
		} catch (Exception e) {
			logger.error(sql, e);
			e.printStackTrace();
		} finally {
			DAO.close(con, st, rs);
		}
		return res;
	}

	public static void executeSqlList(List<String> aList) throws Exception {
		for (String sql : aList) {
			update(sql);
		}
	}

	/**
	 * 一次运行较大的sql语句,一般在1万以内 通常是insertList或updateList
	 * 
	 * @param aList
	 * @throws Exception
	 */
	public static void executeBatchSql(List<String> aList) throws Exception {
		if (aList == null) {
			logger.debug("In executeBatchSql sqlList is null");
			return;
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = BaseDao.getconn();
			long start = System.currentTimeMillis();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			if (aList.size() > 0) {
				logger.debug(aList.get(0));
			}
			for (String sql : aList) {
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			con.commit();
			con.setAutoCommit(true);
			long thisSpend = (System.currentTimeMillis() - start);
			logger.debug("sqlList size:" + aList.size() + " Finished In :" + thisSpend + " ms ");
		} catch (SQLException e) {
			throw e;
		} finally {
			DAO.close(pstmt);
			DAO.close(con);
		}
	}

	/**
	 * 分批次(切片)运行一个超大的sql语句，一般在一万以上 通常是insertList或updateList
	 * 
	 * @param sqlList
	 * @throws Exception
	 */
	public static void runBatch(List<String> sqlList) throws Exception {
		int batchCount = 1000;// 10000
		int count = sqlList.size();
		for (int i = 0; i < count; i += batchCount) {
			try {
				int end = 0;
				if (i + batchCount <= count) {
					end = i + batchCount;
				} else {
					end = count;
				}
				executeBatchSql(sqlList.subList(i, end));
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public static void main(String args[]) {
		logger.debug("123|456|789".replaceAll("\\|", "#"));
		logger.debug("DB Timestamp : " + getCurrentTimestamp());
		logger.debug("DB date : " + getCurrentDate());
		logger.debug("DB time : " + getCurrentTime());
		logger.debug("show databases : " + getMapList("show databases"));
	}
}
