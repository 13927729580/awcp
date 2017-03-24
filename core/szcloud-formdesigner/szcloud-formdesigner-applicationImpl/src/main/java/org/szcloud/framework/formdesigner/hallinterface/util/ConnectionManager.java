package org.szcloud.framework.formdesigner.hallinterface.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

/**
 * 数据库连接池管理类
 * @author Lon
 * @date 2012-1-15 上午4:52:06
 * 
 */
public class ConnectionManager {
	
  private static Map<String, SharedPoolDataSource> dataSourceMap = new HashMap<String, SharedPoolDataSource>();

  private static Map<String, ConnectionManager> poolMap = new HashMap<String, ConnectionManager>();

  public static synchronized ConnectionManager getInstance(String propFile) throws Exception {
    if (poolMap.containsKey(propFile)) {
      return poolMap.get(propFile);
    }
    ConnectionManager pool = new ConnectionManager(propFile);
    poolMap.put(propFile, pool);
    return pool;
  }

  private ConnectionManager(String propFileName) throws Exception {
    Properties dbProps = new Properties();
    InputStream is = new FileInputStream(new File(propFileName));//getClass().getResourceAsStream(propFile);
    dbProps.load(is);
    init(dbProps,propFileName);
    is.close();
  }

  public void init(Properties p,String propFileName) throws Exception {
//  	BasicDataSource dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
//
//		dataSourceMap.put(propFileName, dataSource);
		
    String driverName = p.getProperty("driverClassName");
    String url = p.getProperty("url");
    String desc = p.getProperty("description");
    String username = p.getProperty("dbUserName"); 
    String password = p.getProperty("password"); 

    boolean defaultAutoCommit = true;
    if (p.getProperty("defaultAutoCommit") != null) {
      defaultAutoCommit = p.getProperty("defaultAutoCommit").equalsIgnoreCase("true");
    }

    boolean defaultReadOnly = false;
    if (p.getProperty("defaultReadOnly") != null) {
      defaultReadOnly = p.getProperty("defaultReadOnly").equalsIgnoreCase("true");
    }
    
    int maxIdle = 20; 
    try {
      maxIdle = Integer.parseInt(p.getProperty("maxIdle"));
    } catch (Exception e) {
    }
    
    int maxActive = 50; 
    try {
      maxActive = Integer.parseInt(p.getProperty("maxActive"));
    } catch (Exception e) {
    }

    int maxWait = 1000; 
    try {
      maxWait = Integer.parseInt(p.getProperty("maxWait"));
    } catch (Exception e) {
    }
    
    int minEvictableIdleTimeMillis = 30 * 60 * 1000;
    try {
      minEvictableIdleTimeMillis = Integer.parseInt(p.getProperty("minEvictableIdleTimeMillis"));
    } catch (Exception e) {
    }
    
    boolean testOnBorrow = false;
    try {
    	testOnBorrow = Boolean.parseBoolean(p.getProperty("testOnBorrow"));
    }catch(Exception e){
    }
    
    boolean testOnReturn = false;
    try {
    	testOnReturn = Boolean.parseBoolean(p.getProperty("testOnReturn"));
    }catch(Exception e){
    }
    
    boolean testWhileIdle = true;
    try {
    	testWhileIdle = Boolean.parseBoolean(p.getProperty("testWhileIdle"));
    }catch(Exception e){
    }
    int timeBetweenEvictionRunsMillis = 30 * 1000; 
    try {
    	timeBetweenEvictionRunsMillis = Integer.parseInt(p.getProperty("timeBetweenEvictionRunsMillis"));
    } catch (Exception e) {
    }
    
    // MYSQL默认的校验sql语句，不同数据库修改成对应的sql语句
    String validationQuery = "SELECT CURRENT_TIMESTAMP()";
    if(null != p.getProperty("validationQuery") && "".equals(p.getProperty("validationQuery").trim())) {
    	validationQuery = p.getProperty("validationQuery");
    }
    
    int numTestsPerEvictionRun = 60 * 1000; 
    try {
    	numTestsPerEvictionRun = Integer.parseInt(p.getProperty("numTestsPerEvictionRun"));
    } catch (Exception e) {
    }

    DriverAdapterCPDS adapter = new DriverAdapterCPDS();
    adapter.setDriver(driverName);
    adapter.setUrl(url);
    adapter.setUser(username);
    adapter.setPassword(password);
    
    SharedPoolDataSource dataSource = new SharedPoolDataSource();
    dataSource.setConnectionPoolDataSource(adapter);
    dataSource.setDescription(desc);
    
    dataSource.setDefaultAutoCommit(defaultAutoCommit);
    dataSource.setDefaultReadOnly(defaultReadOnly);
    dataSource.setMaxIdle(maxIdle);
    dataSource.setMaxActive(maxActive);
    dataSource.setMaxWait(maxWait);
    dataSource.setTestOnBorrow(testOnBorrow);
    dataSource.setTestOnReturn(testOnReturn);
    dataSource.setTestWhileIdle(testWhileIdle);
    dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    dataSource.setValidationQuery(validationQuery);
    dataSource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    dataSourceMap.put(propFileName, dataSource);
  }

  public Connection getConnection(String propFileName) throws SQLException {
    return dataSourceMap.get(propFileName).getConnection();
  }

//  public int getNumIdle() {
//    return dataSource.getNumIdle();
//  }
//
//  public int getNumActive() {
//    return dataSource.getNumActive();
//  }
//
//  public int getNumTestsPerEvictionRun() {
//    return dataSource.getNumTestsPerEvictionRun();
//  }

	public SharedPoolDataSource getDataSource(String propFileName) {
		return dataSourceMap.get(propFileName);
	}

//	public void setDataSource(SharedPoolDataSource dataSource) {
//		this.dataSource = dataSource;
//	}
  
  

}
