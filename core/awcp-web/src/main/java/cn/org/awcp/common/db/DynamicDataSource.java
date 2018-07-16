package cn.org.awcp.common.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 数据源
 * @author venson
 * @version 20180515
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 主数据源
     */
    public static final String MASTER_DATA_SOURCE = "masterDataSource";

    /**
     * 过滤的属性
     */
    public static final String[] IGNORE_PROPERTIES = new String[]{"url", "driverClassName", "username", "password", "initialSize", "maxActive", "minIdle", "maxIdle"};

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    private Map<Object, Object> targetDataSources;

    private Object defaultTargetDataSource;

    private boolean lenientFallback = true;


    private Map<Object, DataSource> resolvedDataSources;

    private DataSource resolvedDefaultDataSource;

    @Autowired
    private DataSource masterDataSource;


    @Override
    public void afterPropertiesSet() {
        if (this.targetDataSources == null) {
            throw new IllegalArgumentException("Property 'targetDataSources' is required");
        }

        this.resolvedDataSources = new HashMap<>(16);
        for (Map.Entry<Object, Object> entry : this.targetDataSources.entrySet()) {
            Object lookupKey = resolveSpecifiedLookupKey(entry.getKey());
            DataSource dataSource = resolveSpecifiedDataSource(entry.getValue());
            this.resolvedDataSources.put(lookupKey, dataSource);
        }
        if (this.defaultTargetDataSource != null) {
            this.resolvedDefaultDataSource = resolveSpecifiedDataSource(this.defaultTargetDataSource);
        }
        /**
         * 读取数据库中已经有的数据源
         */
        String sql = "select NAME,SOURCE_URL,SOURCE_DRIVER,USERNAME,PASSWORD,PROTOTYPE_COUNT,MINIMUM_CONNECTION_COUNT,MAXIMUM_CONNECTION_COUNT from fw_mm_datasourcemanager where NAME <>?";
        List<Map<String, Object>> data;
        try {
            data = JdbcUtils.executeQuery(masterDataSource, sql,MASTER_DATA_SOURCE);
            for (Map<String, Object> vo : data) {
                Object name = vo.get("NAME");
                DruidDataSource dataSource = new DruidDataSource();
                dataSource.setUrl((String) vo.get("SOURCE_URL"));
                dataSource.setDriverClassName((String) vo.get("SOURCE_DRIVER"));
                dataSource.setUsername((String) vo.get("USERNAME"));
                dataSource.setPassword((String) vo.get("PASSWORD"));
                dataSource.setInitialSize((int) vo.get("PROTOTYPE_COUNT"));
                dataSource.setMinIdle((int) vo.get("MINIMUM_CONNECTION_COUNT"));
                dataSource.setMaxActive((int) vo.get("MAXIMUM_CONNECTION_COUNT"));
                put(name, dataSource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加数据源
     *
     * @param key        键
     * @param dataSource 数据源
     */
    public void put(Object key, DataSource dataSource) {
        try {
            BeanUtils.copyProperties(resolvedDefaultDataSource, dataSource, IGNORE_PROPERTIES);
            resolvedDataSources.put(key, dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据源
     *
     * @param key 键
     */
    public DataSource get(Object key) {
        return resolvedDataSources.get(key);
    }

    /**
     * 移除数据源
     *
     * @param key 键
     */
    public void remove(Object key) {
        resolvedDataSources.remove(key);
    }

    /**
     * 当前数据源个数
     *
     * @return int
     */
    public int size() {
        return resolvedDataSources.size();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        Object lookupKey = determineCurrentLookupKey();
        DataSource dataSource = this.resolvedDataSources.get(lookupKey);
        if (dataSource == null && (this.lenientFallback || lookupKey == null)) {
            dataSource = this.resolvedDefaultDataSource;
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        }
        return dataSource;
    }


    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = CONTEXT_HOLDER.get();
        // 如果没有指定数据源，抛出异常
        if (null == dataSource) {
            CONTEXT_HOLDER.set(MASTER_DATA_SOURCE);
        }
        return CONTEXT_HOLDER.get();
    }


    /**
     * 设置数据源
     *
     * @param dataSource
     */
    public static void setDataSource(String dataSource) {
        CONTEXT_HOLDER.set(dataSource);
    }

    /**
     * 获取当前线程数据源名称
     * @return dataSourceName
     */
    public  static String getDataSource(){
        return  CONTEXT_HOLDER.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
    }

    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }
}
