package org.szcloud.framework.venson.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据库连接操作类
 */
public class DBUtil {
	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(DBUtil.class);
	private static ThreadLocal<Connection> connLocal = new ThreadLocal<Connection>();
	private static final String user = "root";
	private static final String password = "root";
	private static final String driverClass = "com.mysql.jdbc.Driver";
	private static final String jdbcUrl = "jdbc:mysql://localhost:3306/awcp?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true&amp;useOldAliasMetadataBehavior=true";

	private DBUtil() {
	}

	// 静态语句块
	static {
		try {
			Class.forName(driverClass);
		} catch (Exception e) {
			logger.info("ERROR", e);
		}
	}

	public static Connection getConnection() throws Exception {
		// 当前线程第一次调用时，新建一个返回
		// 当前线程再次调用，返回第一次创建的
		Connection conn = connLocal.get();
		if (conn == null || conn.isClosed()) {
			conn = DriverManager.getConnection(jdbcUrl, user, password);
			connLocal.set(conn);
		}
		return conn;
	}

	public static void closeConnection() {
		// 清空threadlocal
		// 关闭conn
		Connection conn = connLocal.get();
		connLocal.set(null);
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.info("ERROR", e);
			} // 放回连接池
		}
	}

	public static void beginTransaction() throws Exception {
		Connection conn = getConnection();
		conn.setAutoCommit(false);
	}

	public static void commit() {
		try {
			Connection conn = getConnection();
			conn.commit();
		} catch (Exception ex) {
		}
	}

	public static void rollback() {
		try {
			Connection conn = getConnection();
			conn.rollback();
		} catch (Exception ex) {
		}
	}

	public static void main(String[] args) {
		try {
			DBUtil.beginTransaction();
			ResultSet rs = DBUtil.getConnection().createStatement().executeQuery("show tables;");
			while (rs.next()) {
				String table = rs.getString(1);
				// 如果不是组织表，则重置所有组ID
				// if (!table.equalsIgnoreCase("p_un_group")) {
				// ResultSet rs1 = DBUtil.getConnection().createStatement().executeQuery("desc "
				// + table + ";");
				// while (rs1.next()) {
				// String colName = rs1.getString(1);
				// if (colName.equalsIgnoreCase("GROUP_ID")) {
				// System.out.println(table + "," + colName);
				// DBUtil.getConnection().createStatement().execute("update " + table + " set
				// GROUP_ID=1;");
				// }
				// }
				// }
				if (table.startsWith("nd")) {
					System.out.print("drop table " + table + ";\n");
				}
			}
			DBUtil.commit();
		} catch (Exception e) {
			DBUtil.rollback();
			logger.info("ERROR", e);
		} finally {
			DBUtil.closeConnection();
		}
	}

}
