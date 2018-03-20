package cn.org.awcp.metadesigner.application;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
	public boolean save(Map<String, String> map, String modelCode);

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @param modelCode
	 * @return
	 */
	public boolean delete(Object id, String modelCode);

	/**
	 * 根据条件删除
	 * 
	 * @param map
	 * @param modelCode
	 * @return
	 * @throws ParseException
	 */
	public boolean deleteByParams(Map<String, String> map, String modelCode);

	/**
	 * 修改
	 * 
	 * @param map
	 * @param modelCode
	 * @return
	 * @throws Exception
	 */
	public boolean update(Map<String, String> map, String modelCode);

	/**
	 * 根据modelCode查找
	 * 
	 * @param modelCode
	 * @return
	 */
	public List findAll(String modelCode);

	/**
	 * 查询单个
	 * 
	 * @param id
	 * @param modelCode
	 * @return
	 */
	public Map<String, Object> get(Object id, String modelCode);

	public List<Map<String, Object>> search(String sql, Object... obj);

	public int queryOne(String sql, Object... obj);

	public Object queryObject(String sql, Object... obj);

	public int updateBySql(String sql, Object... obj);

	/**
	 * 根据元数据code获取到数据源ID，如果数据源没配置，则获取系统默认数据源Id
	 * @param modelCode
	 * @return
	 */
	String getDataSourceIdByModelCode(String modelCode);

}
