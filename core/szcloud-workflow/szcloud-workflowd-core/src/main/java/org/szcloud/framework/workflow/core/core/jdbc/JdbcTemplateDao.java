package org.szcloud.framework.workflow.core.core.jdbc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface JdbcTemplateDao<T> {

	public T get(Long id);

	public List<T> findAll();

	public T findUnique(String whereSql, Serializable... param);

	public List<T> findList(String whereSql, Serializable... param);

	public List<T> findByIds(List<Long> ids);

	public List<Map<String, Object>> search(String sql, Serializable... param);

	public Long save(T entity);

	public void remove(Long id);
	
	public void remove(List<Long> ids);

	public PageList<T> pagedQuery(int pageNo, int pageSize, String whereSql, List paramList);

	public T convert(Map<String, Object> map);
}
