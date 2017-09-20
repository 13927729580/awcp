package org.szcloud.framework.formdesigner.hallinterface.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BaseDao {

	/**
	 * 数据连接基类
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	private static String url;
	private static String driver;
	private static String username;
	private static String pass;
	public static Connection getconn()throws ClassNotFoundException, SQLException{
		Connection conn=null;
		//加载属性文件
		Properties props =new Properties();
		try {
			props.load(BaseDao.class.getClassLoader().getResourceAsStream("db_connection.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}
		url = props.getProperty("url");
		driver = props.getProperty("driver");
		username = props.getProperty("username");
		pass = props.getProperty("pass");

		//连接数据库
		Class.forName(driver);
		conn = DriverManager.getConnection(url, username , pass);
		//返回conn 连接对象
		return conn;
	}

	/**
	 * 关闭连接
	 */
	public static void closeAll(Connection conn,PreparedStatement ps,ResultSet rs){
		try {
			if(conn!=null){
				conn.close();
			}
			if(ps!=null){
				ps.close();
			}
			if(rs!=null){
				rs.close();
			}
		} catch (SQLException e) {
			// 打印信息
			e.printStackTrace();
		}
	}
}
