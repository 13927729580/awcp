package org.szcloud.framework.venson.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库连接操作类
 */
public class DBUtil {
	private static ThreadLocal<Connection> connLocal = new ThreadLocal<Connection>();
	private static final String user = "root";
	private static final String password = "123456";
	private static final String driverClass = "com.mysql.jdbc.Driver";
	private static final String jdbcUrl = "jdbc:mysql://192.168.0.44:3306/platformcloud_jf?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true&amp;useOldAliasMetadataBehavior=true";

	private DBUtil() {
	}

	// 静态语句块
	static {
		try {
			Class.forName(driverClass);
		} catch (Exception ex) {
			ex.printStackTrace();
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
				e.printStackTrace();
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
				try {
					DBUtil.getConnection().createStatement()
							.execute("ALTER TABLE " + table + " DROP COLUMN BWSX,DROP COLUMN JJCD,DROP COLUMN XTBH;");
				} catch (Exception e) {
					System.out.println(table);
				}
			}

		} catch (Exception e) {
			DBUtil.rollback();
			e.printStackTrace();
		} finally {
			DBUtil.closeConnection();
		}
	}

}
