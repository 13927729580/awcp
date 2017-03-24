package org.szcloud.framework.core.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageList;


public interface QueryChannelService extends Serializable {

	@SuppressWarnings("rawtypes")
	public long queryResultSize(Class clazz,String queryStr, Map<String, Object> params);
//
//	public <T> List<T> queryResult(String queryStr, Object[] params, long firstRow, int pageSize);
//
	@SuppressWarnings("rawtypes")
	public <T> T querySingleResult(Class clazz,String queryStr, Map<String, Object> params);
//
//	public <T> List<T> queryResult(String queryStr, Object[] params);
//
//	public List<Map<String, Object>> queryMapResult(String queryStr, Object[] params);
//
//	public <T> Page<T> queryPagedResult(String queryStr, Object[] params, long firstRow, int pageSize);
//
//	public <T> Page<T> queryPagedResultByPageNo(String queryStr, Object[] params, int currentPage, int pageSize);
//
//	public <T> Page<T> queryPagedResultByNamedQuery(String queryName, Object[] params, long firstRow, int pageSize);
//
//	public <T> Page<T> queryPagedResultByPageNoAndNamedQuery(String queryName, Object[] params, int currentPage,
//			int pageSize);
//
//	public Page<Map<String, Object>> queryPagedMapResult(String queryStr, Object[] params, int currentPage, int pageSize);
//
//	public Page<Map<String, Object>> queryPagedMapResultByNamedQuery(String queryName, Object[] params,
//			int currentPage, int pageSize);
//
//	public <T extends Entity> Page<T> queryPagedByQuerySettings(QuerySettings<T> settings, int currentPage, int pageSize);
	
	//主要用这个做分页
//	new PageBounds();//默认构造函数不提供分页，返回ArrayList  
//	new PageBounds(int limit);//取TOPN操作，返回ArrayList  
//	new PageBounds(Order... order);//只排序不分页，返回ArrayList  
//	new PageBounds(int page, int limit);//默认分页，返回PageList  
//	new PageBounds(int page, int limit, Order... order);//分页加排序，返回PageList  
//	new PageBounds(int page, int limit, List<Order> orders,boolean containsTotalCount);//使用containsTotalCount来决定查不查询totalCount，即返回ArrayList还是PageList  
	@SuppressWarnings("rawtypes")
	public PageList queryPagedResult(Class clazz,String queryStr, Map<String, Object> params, int currentPage, int pageSize,String sortString);
	public <T> List<T> queryResult(Class<T> clazz,String queryStr, Map<String, Object> params);
	@SuppressWarnings("rawtypes")
	public PageList eqQueryPagedResult(Class clazz,Object entity, int currentPage, int pageSize,String sortString);
	@SuppressWarnings("rawtypes")
	public PageList selectPagedByExample(Class clazz,	BaseExample example,int currentPage, int pageSize,String sortString);
	/**
	 * 
	 * @param countId 查询记录条数的select标签的ID （mybatis中xml）
	 * @param selectId 查询记录的select标签的ID （mybatis中xml）
	 * @param clazz
	 * @param example
	 * @param currentPage
	 * @param pageSize
	 * @param sortString
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageList selectPagedByExample(String countId, String selectId, Class clazz,	BaseExample example,int currentPage, int pageSize,String sortString);
	
	/**
	 * 返回为hashMap<String,Object> 带分页 可联合查询
	 * @param clazz
	 * @param queryStr
	 * @param params
	 * @param currentPage
	 * @param pageSize
	 * @param sortString
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageList selectMapByPage(Class clazz,String queryStr,Map<String,Object> params,int currentPage,int pageSize,String sortString);
	
	/**
	 * 条件查询   可联合查询
	 * select a.id,a.modelName,b.itemCode,b.itemName from fw_mm_metamodel a left join fw_mm_metamodelitems b on a.id=b.modelId where a.id=#{id}
	 * @param clazz
	 * @param queryStr
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> selectMap(Class clazz,String queryStr,Map<String,Object> params);
	
	
	public boolean excuteMethod(Class clazz,String method,Map<String, Object> params);
	
	public List queryPagedResult(Class clazz,String queryStr, Map<String, Object> params);
}
	
