package org.szcloud.framework.venson.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

/**
 * mybatis 数据接口类
 * 
 * @author venson
 */
public interface BaseDao {

	String MAPPER_PREFIX = "Mapper.";
	String SAVE = "save";
	String UPDATE = "update";
	String DELETE = "delete";
	String GET = "get";
	String COUNT = "count";
	String FINDALL = "findAll";

	SqlSession getSession();

	void excute(String statement);

	int save(String statement, Object params);

	int update(String statement, Object params);

	void delete(String statement, Object params);

	<T> T get(String statement, Object params);

	<T> T get(String statement);

	<T> List<T> findAllByPage(String statement, Object params, int offset, int limit);

	<T> List<T> findAllByPage(String statement, int offset, int limit);

	<T> List<T> findAll(String statement, Object params);

	<T> List<T> findAll(String statement);

}
