package org.szcloud.framework.venson.dao;

import java.util.List;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * mybatis 数据接口实现类
 * 
 * @author venson
 */
@Repository("baseDaoImpl")
public class BaseDaoImpl implements BaseDao {

	@Autowired
	private SqlSessionFactory sessionFactory;

	@Override
	public SqlSession getSession() {
		return sessionFactory.openSession();
	}

	@Override
	public void excute(String statement) {
		getSession().select(statement, Executor.NO_RESULT_HANDLER);
	}

	@Override
	public int save(String statement, Object params) {
		return getSession().insert(statement, params);
	}

	@Override
	public int update(String statement, Object params) {
		return getSession().update(statement, params);
	}

	@Override
	public void delete(String statement, Object params) {
		getSession().delete(statement, params);

	}

	@Override
	public <T> T get(String statement, Object params) {
		return getSession().selectOne(statement, params);
	}

	@Override
	public <T> T get(String statement) {
		return get(statement, null);
	}

	@Override
	public <T> List<T> findAllByPage(String statement, Object params, int offset, int limit) {
		return getSession().selectList(statement, params, new RowBounds(offset, limit));
	}

	@Override
	public <T> List<T> findAllByPage(String statement, int offset, int limit) {
		return findAllByPage(statement, null, offset, limit);
	}

	@Override
	public <T> List<T> findAll(String statement, Object params) {
		final RowBounds defaultBounds = RowBounds.DEFAULT;
		return findAllByPage(statement, params, defaultBounds.getOffset(), defaultBounds.getLimit());
	}

	@Override
	public <T> List<T> findAll(String statement) {
		return findAll(statement, null);
	}

}
