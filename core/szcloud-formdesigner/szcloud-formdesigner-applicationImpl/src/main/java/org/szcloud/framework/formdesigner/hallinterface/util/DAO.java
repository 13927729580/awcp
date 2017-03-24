package org.szcloud.framework.formdesigner.hallinterface.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class DAO {

	private static Logger logger = Logger.getLogger(DAO.class.getClass());

	private static Map<String, DAO> daoMap = new HashMap<String, DAO>();

	public static String defaultDbPoolName = "bin/db_connection.properties";

	public ConnectionManager pool = null;

	public synchronized static DAO getInstance(String propFile) {
		if (daoMap.containsKey(propFile)) {
			return daoMap.get(propFile);
		}
		DAO dao = new DAO(propFile);
		daoMap.put(propFile, dao);
		return dao;
	}

	/**
	 * 使用默认的class/dbpool.properties
	 * 
	 * @return
	 */
	public synchronized static DAO getInstance() {
		return getInstance(defaultDbPoolName);
	}

	private DAO(String propFile) {
		try {
			pool = ConnectionManager.getInstance(propFile);
			if (pool == null) {
				logger.debug("pool is null ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	/**
	 * 这个测试程序仅仅针对mysql, DB2 ,SQL SERVER及ORACLE的sql语句与此略有不同
	 * 
	 * @param propFile
	 */
	// private void testConnection() {
	// try {
	// Connection con = DAO.getInstance().getConnection();
	// Statement st = con.createStatement();
	// st.executeQuery("select 1 from dual");
	// close(st);
	// close(con);
	// logger.debug("Connection Pool init Successfully");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public Connection getConnection() throws SQLException {
		Connection con = null;
		try {
			con = pool.getConnection(defaultDbPoolName);
		} catch (SQLException sqle) {
			logger.error(sqle);
		}

		return con;
	}

	public Connection getConnection(String propFile) throws SQLException {
		Connection con = null;
		try {
			con = pool.getConnection(propFile);
		} catch (SQLException sqle) {
			logger.error(sqle);
		}

		return con;
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				logger.warn(rs, e);
			}
		}
	}

	public static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (Exception e) {
				logger.warn(st, e);
			}
		}
	}

	public static void close(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
			logger.warn(con, e);
		}
	}

	public static void close(Connection con, Statement st, ResultSet rs) {
		DAO.close(rs);
		DAO.close(st);
		DAO.close(con);
	}

	public static void close(Connection con, Statement st) {
		DAO.close(st);
		DAO.close(con);
	}

	public DataSource getDataSource() {
		return pool.getDataSource(defaultDbPoolName);
	}

	public DataSource getDataSource(String propFile) {
		return pool.getDataSource(propFile);
	}

}
