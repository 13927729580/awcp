package cn.org.awcp.venson.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.*;

/**
 * 数据库连接操作类
 *
 * @author venson
 * @version 20180612
 */
public class DBUtil {

    private static final Log LOGGER = LogFactory.getLog(DBUtil.class);
    private static ThreadLocal<Connection> CONN_LOCAL = new ThreadLocal<>();
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/awcp?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&useOldAliasMetadataBehavior=true";
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private DBUtil() {
    }

    // 静态语句块
    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (Exception e) {
            LOGGER.info("ERROR", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        // 当前线程第一次调用时，新建一个返回
        // 当前线程再次调用，返回第一次创建的
        Connection conn = CONN_LOCAL.get();
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            CONN_LOCAL.set(conn);
        }
        return conn;
    }

    /**
     * 更新语句
     *
     * @param sql        SQL语句
     * @param parameters 参数（可为空）
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(String sql, Object... parameters) throws SQLException {
        return executeUpdate(getConnection(), sql, Arrays.asList(parameters));
    }

    /**
     * 更新语句
     *
     * @param conn       数据库连接
     * @param sql        SQL语句
     * @param parameters 参数（可为空）
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(Connection conn, String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;

        int updateCount;
        try {
            stmt = conn.prepareStatement(sql);

            setParameters(stmt, parameters);

            updateCount = stmt.executeUpdate();
        } finally {
            close(stmt);
        }

        return updateCount;
    }

    /**
     * 执行语句
     *
     * @param sql        SQL语句
     * @param parameters 参数（可为空）
     * @return
     * @throws SQLException
     */
    public static void execute(String sql, Object... parameters) throws SQLException {
        execute(getConnection(), sql, Arrays.asList(parameters));
    }

    /**
     * 执行语句
     *
     * @param conn       数据库连接
     * @param sql        SQL语句
     * @param parameters 参数（可为空）
     * @return
     * @throws SQLException
     */
    public static void execute(Connection conn, String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            setParameters(stmt, parameters);

            stmt.executeUpdate();
        } finally {
            close(stmt);
        }
    }

    /**
     * 执行查询
     *
     * @param sql        SQL语句
     * @param parameters 参数（可为空）
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... parameters)
            throws SQLException {
        return executeQuery(sql, Arrays.asList(parameters));
    }

    /**
     * 执行查询
     *
     * @param sql        SQL语句
     * @param parameters 参数（可为空）
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery(String sql, List<Object> parameters)
            throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            return executeQuery(conn, sql, parameters);
        } finally {
            close(conn);
        }
    }

    /**
     * 执行查询
     *
     * @param conn       数据库连接
     * @param sql        SQL语句
     * @param parameters 参数（可为空）
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery(Connection conn, String sql, List<Object> parameters)
            throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);

            setParameters(stmt, parameters);

            rs = stmt.executeQuery();

            ResultSetMetaData rsMeta = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();

                for (int i = 0, size = rsMeta.getColumnCount(); i < size; ++i) {
                    String columName = rsMeta.getColumnLabel(i + 1);
                    Object value = rs.getObject(i + 1);
                    row.put(columName, value);
                }

                rows.add(row);
            }
        } finally {
            close(rs);
            close(stmt);
        }

        return rows;
    }

    /**
     * 设置参数
     *
     * @param stmt       声明
     * @param parameters 参数
     * @throws SQLException
     */
    private static void setParameters(PreparedStatement stmt, List<Object> parameters) throws SQLException {
        if (parameters == null) {
            return;
        }
        for (int i = 0, size = parameters.size(); i < size; ++i) {
            Object param = parameters.get(i);
            stmt.setObject(i + 1, param);
        }
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        // 清空threadlocal
        // 关闭conn
        Connection conn = CONN_LOCAL.get();
        CONN_LOCAL.remove();
        close(conn);
    }

    /**
     * 开启事务
     *
     * @throws SQLException
     */
    public static void beginTransaction() throws SQLException {
        beginTransaction(getConnection());
    }

    /**
     * 开启事务
     *
     * @param conn 数据库连接
     * @throws SQLException
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
    }


    /**
     * 提交事务
     *
     * @param conn 数据库连接
     * @throws SQLException
     */
    public static void commit(Connection conn) throws SQLException {
        if (conn == null) {
            return;
        }
        conn.commit();
    }

    /**
     * 提交事务
     *
     * @throws SQLException
     */
    public static void commit() throws SQLException {
        commit(getConnection());
    }

    /**
     * 回滚事务
     *
     * @param conn 数据库连接
     * @throws SQLException
     */
    public static void rollback(Connection conn) throws SQLException {
        if (conn == null) {
            return;
        }
        conn.rollback();
    }

    /**
     * 回滚事务
     *
     * @throws SQLException
     */
    public static void rollback() throws SQLException {
        rollback(getConnection());
    }

    public static void close(Connection x) {
        if (x == null) {
            return;
        }

        try {
            x.close();
        } catch (Exception e) {
            LOGGER.info("ERROR", e);
        }
    }

    public static void close(Statement x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            LOGGER.debug("close statement error", e);
        }
    }

    public static void close(ResultSet x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            LOGGER.debug("close result set error", e);
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println(DBUtil.executeQuery("select user_id,name from p_un_user_base_info"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
