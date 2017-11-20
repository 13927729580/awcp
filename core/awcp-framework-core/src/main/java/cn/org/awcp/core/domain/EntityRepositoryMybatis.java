package cn.org.awcp.core.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * EntityRepository的Mybatis实现版本
 * 
 * @author caoyong
 * 
 */
@Service(value = "repository")
public class EntityRepositoryMybatis implements EntityRepository {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	public SqlSession getSession() {
		return sqlSessionFactory.openSession();
	}

	public <E, T extends Entity<E>> T save(T entity) {
		if (entity.getId() != null) {
			this.update(entity);
		} else {
			this.save(entity, null);
		}
		return entity;
	}

	@Override
	public <E, T extends Entity<E>> T update(T entity) {
		SqlSession session = getSession();
		try {
			if (entity.getId() != null) {
				session.update(entity.getClass().getName() + ".update", entity);
			} else {
				throw new RuntimeException("更新ID为空");
			}
			session.commit();
		} finally {
			session.close();
		}
		return entity;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E, T extends Entity<E>> T save(T entity, E id) {
		SqlSession session = getSession();
		try {
			if (id != null) {
				entity.setId(id);
			} else {
				if ("java.lang.String".equals(entity.getEntityClass().getTypeName())) {
					entity.setId((E) UUID.randomUUID().toString());
				}
			}
			session.insert(entity.getClass().getName() + ".insert", entity);
			session.commit();
		} finally {
			session.close();
		}
		return entity;
	}

	public <E, T> void remove(Entity<E> entity) {
		SqlSession session = getSession();
		try {
			session.delete(entity.getClass().getName() + ".remove", entity);
			session.commit();
		} finally {
			session.clearCache();
			session.close();
		}
	}

	public <E, T extends Entity<E>> boolean exists(Class<T> clazz, E id) {
		T t = get(clazz, id);
		if (t != null)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public <E, T extends Entity<E>> T get(Class<T> clazz, E id) {
		SqlSession session = getSession();
		try {
			T t = (T) session.selectOne(clazz.getName() + ".get", id);
			return t;
		} finally {
			session.close();
		}
	}

	public <E, T extends Entity<E>> T load(Class<T> clazz, E id) {
		return get(clazz, id);
	}

	public <E, T extends Entity<E>> T getUnmodified(Class<T> clazz, T entity) {
		throw new UnsupportedOperationException();
	}

	public <E, T extends Entity<E>> List<T> findAll(Class<T> clazz) {
		SqlSession session = getSession();
		List<T> lists = null;
		try {
			lists = session.selectList(clazz.getName() + ".getAll", null);
		} finally {
			session.close();
		}
		return lists;
	}

	@SuppressWarnings("unchecked")
	public <T> T getSingleResult(Class<T> clazz, Object[] params, Class<T> resultClass) {
		SqlSession session = getSession();
		T t = null;
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("params", params);
		try {
			t = (T) session.selectOne(clazz.getName() + ".get", paramsMap);
		} finally {
			session.close();
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public <T> T getSingleResult(Class<T> clazz, Map<String, Object> params, Class<T> resultClass) {
		SqlSession session = getSession();
		T t = null;
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("params", params);
		try {
			t = (T) session.selectOne(clazz.getName() + ".get", paramsMap);
		} finally {
			session.close();
		}
		return t;
	}

	public void clear() {

	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public boolean existed() throws Exception {
		return false;
	}

	public boolean notExisted() throws Exception {
		return false;
	}

	public <E, T extends Entity<E>> List<T> selectByExample(Class<T> clazz, BaseExample example) {
		SqlSession session = getSession();
		try {
			return session.selectList(clazz.getName() + ".selectByExample", example);
		} finally {
			session.close();
		}
	}

	public String getQueryStringOfNamedQuery(String queryName) {
		return null;
	}

	public void excuteSql(String sql) {
		SqlSession session = getSession();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sql", sql);
			session.update("executeSql", map);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void executeUpdate(String queryString, Map<String, Object> params, Class<?> clazz) {
		SqlSession session = getSession();
		try {
			session.update(clazz.getName() + "." + queryString, params);
			session.commit();
		} finally {
			session.close();
		}
	}

	public int executeUpdateForInt(String queryString, Map<String, Object> params, Class<?> clazz) {
		SqlSession session = getSession();
		int i = 0;
		try {
			i = session.update(clazz.getName() + "." + queryString, params);
			session.commit();
		} finally {
			session.close();
		}
		return i;
	}

	public void removeByFK(Class<?> clazz, String queryStr, long fkId) {
		SqlSession session = getSession();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fkId", fkId);
			session.delete(clazz.getName() + "." + queryStr, map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.clearCache();
			session.close();
		}
	}

	@Override
	public void removeByExample(Class<?> clazz, BaseExample example) {
		SqlSession session = getSession();
		try {
			session.delete(clazz.getName() + ".removeByExample", example);
			session.commit();
		} finally {
			session.clearCache();
			session.close();
		}
	}

	@Override
	public <E, T extends Entity<E>> T findOne(String statement, Object args) {
		SqlSession session = getSession();
		try {
			T t = session.selectOne(statement, args);
			return t;
		} finally {
			session.close();
		}
	}

}
