package org.szcloud.framework.metadesigner.application;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.metadesigner.vo.DataSourceManageVO;

/**
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
public interface MetaModelOperateService {

	/**
	 * 增加
	 * 
	 * @param map
	 * @param modelCode
	 * @return
	 * @throws Exception
	 */
	public Long save(Map<String, String> map, String modelCode) throws Exception;

	public Map saveReturnMap(Map<String, String> map, String modelCode) throws Exception;

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @param modelCode
	 * @return
	 */
	public boolean delete(Object id, String modelCode);

	public Map deleteReturnMap(Object id, String modelCode);

	/**
	 * 根据条件删除
	 * 
	 * @param map
	 * @param modelCode
	 * @return
	 * @throws ParseException
	 */
	public boolean deleteByParams(Map<String, String> map, String modelCode) throws ParseException;

	public Map deleteByParamsReturnMap(Map<String, String> map, String modelCode) throws ParseException;

	/**
	 * 修改
	 * 
	 * @param map
	 * @param modelCode
	 * @return
	 * @throws Exception
	 */
	public boolean update(Map<String, String> map, String modelCode) throws Exception;

	public Map updateReturnMap(Map<String, String> map, String modelCode) throws Exception;

	/**
	 * 根据modelCode查找
	 * 
	 * @param modelCode
	 * @return
	 */
	public List findAll(String modelCode);

	/**
	 * 分页查询
	 * 
	 * @param modelCode
	 * @param modelName
	 * @param page
	 * @param rows
	 * @return
	 */
	public String queryPageByModel(String modelCode, String modelName, int page, int rows);

	/**
	 * 根据modelCode查找
	 * 
	 * @param modelCode
	 * @return
	 */
	public List<Map<String, String>> queryPageResult(String modelCode);

	/**
	 * 查询单个
	 * 
	 * @param id
	 * @param modelCode
	 * @return
	 */
	public Map<String, String> get(Object id, String modelCode);

	// public List<Map<String,String>> queryResultByParams(Map<String,String>
	// map,String modelCode) throws ParseException;
	/**
	 * 带参数的分页查询
	 * 
	 * @param page
	 * @param rows
	 * @param modelCode
	 * @param map
	 * @return
	 * @throws ParseException
	 */
	public List<Map<String, String>> queryResultByParamsOrPage(int page, int rows, String modelCode,
			Map<String, String> map) throws ParseException;

	/**
	 * 根据数据源创建数据库
	 * 
	 * @param sql
	 * @return
	 */
	Integer createDatabase(DataSourceManageVO vo);

	public List<Map<String, Object>> search(String sql, Object... obj);

	public int queryOne(String sql, Object... obj);

	public Object queryObject(String sql, Object... obj);

	public int updateBySql(String sql, Object... obj);

}
