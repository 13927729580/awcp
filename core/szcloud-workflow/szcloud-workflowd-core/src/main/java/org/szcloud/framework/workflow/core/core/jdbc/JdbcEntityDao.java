//package org.szcloud.framework.workflow.core.core.jdbc;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.apache.ibatis.annotations.Update;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.dao.IncorrectResultSizeDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.szcloud.framework.workflow.core.core.util.ConvertObject;
//
//import com.github.miemiedev.mybatis.paginator.domain.PageList;
//import com.github.miemiedev.mybatis.paginator.domain.Paginator;
//
//@SuppressWarnings({ "rawtypes", "unchecked" })
//public abstract class JdbcEntityDao<T> implements JdbcTemplateDao<T> {
//
//	private static Logger logger = LoggerFactory.getLogger(JdbcEntityDao.class);
//
//	@Resource
//	public JdbcTemplate jdbcTemplate;
//
//	protected String sqlPagedQueryCount = "";
//	protected String selectSql = "";
//	protected String insertSql = "";
//	protected String deleteSql = "";
//
//	public void setProperty(String sqlPagedQueryCount, String selectSql, String insertSql, String deleteSql) {
//		this.sqlPagedQueryCount = sqlPagedQueryCount;
//		this.selectSql = selectSql;
//		this.insertSql = insertSql;
//		this.deleteSql = deleteSql;
//	}
//
//	public T get(Long id) {//		String sql = selectSql + " where ID = ? ";
//		return convert(jdbcTemplate.queryForMap(sql, id));
//	}
//
//	public List<T> findAll() {
//		List<Map<String, Object>> lists = jdbcTemplate.queryForList(selectSql);
//		List<T> ts = new ArrayList<T>();
//		for (Map map : lists) {
//			ts.add(convert(map));
//		}
//		return ts;
//	}
//
//	public T findUnique(String whereSql, Serializable... param) {
//		String sql = selectSql + whereSql;
//		try {
////			if (param.length == 1) {
//				return convert(jdbcTemplate.queryForMap(sql, param));
////			} else if (param.length == 2) {
////				return convert(jdbcTemplate.queryForMap(sql, param[0], param[1]));
////			} else if (param.length == 3) {
////				return convert(jdbcTemplate.queryForMap(sql, param[0], param[1], param[2]));
////			}
//		} catch (Exception e) {
//			if (e instanceof IncorrectResultSizeDataAccessException) {
//				return null;
//			}
//		}
//		return null;
//	}
//
//	public List<T> findList(String whereSql, Serializable... param) {
//		String sql = selectSql + whereSql;
//		List<Map<String, Object>> lists = null;
//		try {
////			if (param.length == 1) {
//				lists = jdbcTemplate.queryForList(sql, param);
////			} else if (param.length == 2) {
////				lists = jdbcTemplate.queryForList(sql, param[0], param[1]);
////			} else if (param.length == 3) {
////				lists = jdbcTemplate.queryForList(sql, param[0], param[1], param[2]);
////			}
//		} catch (Exception e) {
//			if (e instanceof IncorrectResultSizeDataAccessException) {
//				return null;
//			}
//		}
//		List<T> ts = new ArrayList<T>();
//		if(lists != null){
//			for (Map map : lists) {
//				ts.add(convert(map));
//			}
//		}
//		return ts;
//	}
//
//	public List<Map<String, Object>> search(String sql, Serializable... param) {
//		List<Map<String, Object>> lists = null;
//		try {
////			if (param.length == 1) {
//				lists = jdbcTemplate.queryForList(sql, param);
////			} else if (param.length == 2) {
////				lists = jdbcTemplate.queryForList(sql, param[0], param[1]);
////			} else if (param.length == 3) {
////				lists = jdbcTemplate.queryForList(sql, param[0], param[1], param[2]);
////			}
//		} catch (Exception e) {
//			if (e instanceof IncorrectResultSizeDataAccessException) {
//				return null;
//			}
//		}
//		return lists;
//	}
//
//	public List<T> findByIds(List<Long> ids) {
//		String sql = selectSql + "where id in (?)";
//		List<Map<String, Object>> lists = jdbcTemplate.queryForList(sql, ConvertObject.toSqlString(ids.toArray()));
//		List<T> ts = new ArrayList<T>();
//		for (Map map : lists) {
//			ts.add(convert(map));
//		}
//		return ts;
//	}
//
//	public void remove(Long id) {
//		String sql = deleteSql + "where id = (?)";
//		try {
//			jdbcTemplate.update(sql, id);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
//	
//	public void remove(List<Long> ids) {
//		String sql = deleteSql + "where id in (?)";
//		try {
//			jdbcTemplate.update(sql, ConvertObject.toSqlString(ids.toArray()));
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
//
//
//	
//	public PageList<T> pagedQuery(int pageNo, int pageSize, String whereSql, List paramList) {
//		String sql = whereSql;
//		String countSql = sqlPagedQueryCount + " " + sql;
//		String selectSql = this.selectSql + " " + sql + " limit " + (pageNo - 1) * pageSize + "," + pageSize;
//
//		Object[] params = paramList.toArray();
//		int totalCount = jdbcTemplate.queryForObject(countSql, Integer.class, params);
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(selectSql, params);
//		List<T> ts = new ArrayList<T>();
//
//		for (Map<String, Object> map : list) {
//			ts.add(convert(map));
//		}
//
//		Paginator paginator = new Paginator(pageNo, pageSize, (int) totalCount);
//		PageList<T> pageList = new PageList<T>(ts, paginator);
//
//		return pageList;
//	}
//
//}
