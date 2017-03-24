package org.szcloud.framework.venson.service.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface CRUDService {

	/**
	 * 查找所有对象
	 * 
	 * @param entityClass
	 *            对象的类型
	 * @return 查询结果集
	 */
	<T> List<T> findAll(Class<T> entityClass);

	/**
	 * 分页查找所有对象
	 * 
	 * @param entityClass
	 *            对象的类型
	 * @param offset
	 *            过滤记录数
	 * @param limit
	 *            显示记录数
	 * @return 查询结果集
	 */
	<T> List<T> findAll(Class<T> entityClass, int offset, int limit);

	/**
	 * 保存对象
	 * 
	 * @param entity
	 *            对象实体类
	 * @return 主键id
	 */
	<T> Serializable save(T entity);

	/**
	 * 更新对象
	 * 
	 * @param entity
	 *            对象实体类
	 */
	<T> void update(T entity);

	/**
	 * 删除对象
	 * 
	 * @param entityClass
	 *            对象的类型
	 * @param id
	 *            对象的id
	 */
	<T> void delete(Class<T> entityClass, Object id);

	/**
	 * 获取对象
	 * 
	 * @param entityClass
	 *            对象的类型
	 * @param id
	 *            对象的id
	 */
	<T> T get(Class<T> entityClass, Object id);

	/**
	 * 根据查询参数分页查找所有对象
	 * 
	 * @param entityClass
	 *            对象的类型
	 * @param params
	 *            查询参数
	 * @param offset
	 *            过滤记录数
	 * @param limit
	 *            显示记录数
	 * @return 查询结果集，包含总记录数和数据
	 */
	<T> Map<String, Object> query(Class<T> entityClass, Map<String, String[]> params, int offset, int limit);

}
