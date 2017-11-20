package cn.org.awcp.core.domain;

import java.util.List;
import java.util.Map;

/**
 * 仓储访问接口。用于存取和查询数据库中的各种类型的实体。
 */
public interface EntityRepository {

	/**
	 * 是否存在
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean existed() throws Exception;

	/**
	 * 不存在
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean notExisted() throws Exception;

	/**
	 * 执行保存或跟新 如果ID为空，则增加(insert)。不为空，则修改(update)
	 * 
	 * @param entity
	 * @return
	 */
	<E, T extends Entity<E>> T save(T entity);

	/**
	 * 执行更新
	 * 
	 * @param entity
	 * @return
	 */
	<E, T extends Entity<E>> T update(T entity);

	/**
	 * 执行保存或跟新 如果ID为空，则增加(insert)。不为空，则修改(update)
	 * 
	 * @param entity
	 * @return
	 */
	<E, T extends Entity<E>> T save(T entity, E id);

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	<E, T> void remove(Entity<E> entity);

	/**
	 * 是否存在
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	<E, T extends Entity<E>> boolean exists(Class<T> clazz, E id);

	/**
	 * 删除外键
	 * 
	 * @param fkId
	 */
	void removeByFK(Class<?> clazz, String queryStr, Object fkId);

	/**
	 * 条件删除
	 * 
	 * @param fkId
	 */
	void removeByExample(Class<?> clazz, BaseExample example);

	/**
	 * 查询一条数据
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	<E, T extends Entity<E>> T get(Class<T> clazz, E id);

	/**
	 * 查询一条数据
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	<E, T extends Entity<E>> T load(Class<T> clazz, E id);

	<E, T extends Entity<E>> T getUnmodified(Class<T> clazz, T entity);

	<E, T extends Entity<E>> List<T> findAll(Class<T> clazz);

	<E, T extends Entity<E>> T findOne(String statement, Object args);

	/**
	 * 修改
	 * 
	 * @param queryString
	 * @param params
	 * @param clazz
	 */
	public void executeUpdate(String queryString, Map<String, Object> params, Class<?> clazz);

	/**
	 * 修改
	 * 
	 * @param queryString
	 * @param params
	 * @param clazz
	 */
	public int executeUpdateForInt(String queryString, Map<String, Object> params, Class<?> clazz);

	/**
	 * 模糊查询
	 * 
	 * @param clazz
	 * @param example
	 * @return
	 */
	<E, T extends Entity<E>> List<T> selectByExample(Class<T> clazz, BaseExample example);

	String getQueryStringOfNamedQuery(String queryName);

	/**
	 * 清空
	 */
	void clear();

	/**
	 * 动态Sql
	 * 
	 * @param sql
	 */
	void excuteSql(String sql);

}
