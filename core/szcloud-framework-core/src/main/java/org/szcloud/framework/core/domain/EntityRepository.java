package org.szcloud.framework.core.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 仓储访问接口。用于存取和查询数据库中的各种类型的实体。
 */
public interface EntityRepository {

//	public boolean insert(String statementId ,Object o) throws Exception;
//    public  List<Object> queryForList(String statementId,Object parameters)  throws Exception;
//	public List queryForPageList(String statementId ,java.util.Map param , int curPage, int recordPerPage)  throws Exception;
//    public <T> T queryForObject(String statementId, Object parameters)  throws Exception;
//	public <T> T queryForObject(String statementId,Object parameters,String out)  throws Exception;
//    public boolean update(String statementId, Object parameters)  throws Exception;
//    public boolean delete(String statementId, Object parameters)  throws Exception;

	
	/**
	 * 是否存在
	 * @return
	 * @throws Exception
	 */
    public boolean existed()  throws Exception;
	
    /**
     * 不存在
     * @return
     * @throws Exception
     */
    public boolean notExisted()  throws Exception;
    
    /**
     * 执行保存或跟新
     * 如果ID为空，则增加(insert)。不为空，则修改(update)
     * @param entity
     * @return
     */
    <T extends Entity> T save(T entity);

    /**
     * 删除
     * @param entity
     */
    void remove(Entity entity);

    /**
     * 是否存在
     * @param clazz
     * @param id
     * @return
     */
    <T extends Entity> boolean exists(Class<T> clazz, Serializable id);
    
    /**
     * 删除外键
     * @param fkId
     */
    void removeByFK(Class clazz,String queryStr,long fkId);
    
    
    /**
     * 条件删除
     * @param fkId
     */
    void removeByExample(Class clazz,BaseExample example);
    
   /**
    * 查询一条数据
    * @param clazz
    * @param id
    * @return
    */
    <T extends Entity> T get(Class<T> clazz, Serializable id);

   /**
    * 查询一条数据
    * @param clazz
    * @param id
    * @return
    */
    <T extends Entity> T load(Class<T> clazz, Serializable id);

    /**
     * 浠庝粨鍌ㄤ腑鑾峰彇entity鍙傛暟鎵�唬琛ㄧ殑鏈慨鏀圭殑瀹炰綋
     *
     * @param <T> 瀹炰綋绫诲瀷
     * @param clazz 瀹炰綋鐨勭被
     * @param entity 瑕佹煡璇㈢殑瀹炰綋
     * @return 鍙傛暟entity鍦ㄤ粨鍌ㄤ腑鐨勬湭淇敼鐗堟湰
     */
    <T extends Entity> T getUnmodified(Class<T> clazz, T entity);
    

    /**
     * 鏌ユ壘鎸囧畾绫诲瀷鐨勬墍鏈夊疄浣�     *
     * @param <T> 瀹炰綋绫诲瀷
     * @param clazz 瀹炰綋鐨勭被
     * @return 绗﹀悎鏉′欢鐨勫疄浣撻泦鍚�     */
    <T extends Entity> List<T> findAll(Class<T> clazz);

    /**
     * 修改
     * @param queryString
     * @param params
     * @param clazz
     */
    public void executeUpdate(String queryString, Map<String, Object> params,Class clazz);
    /**
     * 修改
     * @param queryString
     * @param params
     * @param clazz
     */
    public int executeUpdateForInt(String queryString, Map<String, Object> params,Class clazz);

    /**
     * 根据属性查找
     * @param clazz
     * @param propertyName
     * @param propertyValue
     * @return
     */
    <T extends Entity> List<T> findByProperty(Class<T> clazz, String propertyName, Object propertyValue);

    /**
     * 模糊查询
     * @param clazz
     * @param example
     * @return
     */
    <T extends Entity> List<T> selectByExample(Class<T> clazz,BaseExample example);
    
    /**
     * 根据属性查找
     * @param clazz
     * @param properties
     * @return
     */
    <T extends Entity> List<T> findByProperties(Class<T> clazz, NamedParameters properties);
    
    String getQueryStringOfNamedQuery(String queryName);


    
    void flush();

   /**
    * 更新
    * @param entity
    */
    void refresh(Entity entity);

    /**
     * 清空
     */
    void clear();
    
    /**
     * 动态Sql
     * @param sql
     */
    void excuteSql(String sql);
    
  
    
}