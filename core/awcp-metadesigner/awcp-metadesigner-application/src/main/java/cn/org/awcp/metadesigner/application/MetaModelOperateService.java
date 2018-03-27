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

	String SQL="sql";
	String PARAMS="params";
	String IS_NUMBER_PRIMARY_KEY="isNumberPrimaryKey";


	/**
	 * 制作元数据保存数据需要的语句，参数
	 * @param map
	 * @param modelCode
	 * @return
	 */
	Map<String,Object>  markMetaInsert(Map<String, String> map, String modelCode);

	/**
	 * 增加
	 * 
	 * @param map
	 * @param modelCode
	 * @return
	 * @throws Exception
	 */
	 boolean save(Map<String, String> map, String modelCode);

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @param modelCode
	 * @return
	 */
	 boolean delete(Object id, String modelCode);

	/**
	 * 根据条件删除
	 * 
	 * @param map
	 * @param modelCode
	 * @return
	 * @throws ParseException
	 */
	 boolean deleteByParams(Map<String, String> map, String modelCode);

	/**
	 * 修改
	 * 
	 * @param map
	 * @param modelCode
	 * @return
	 * @throws Exception
	 */
	 boolean update(Map<String, String> map, String modelCode);

	/**
	 * 根据modelCode查找
	 * 
	 * @param modelCode
	 * @return
	 */
	 List findAll(String modelCode);

	/**
	 * 查询单个
	 * 
	 * @param id
	 * @param modelCode
	 * @return
	 */
	 Map<String, Object> get(Object id, String modelCode);

	 List<Map<String, Object>> search(String sql, Object... obj);

	 int queryOne(String sql, Object... obj);

	 Object queryObject(String sql, Object... obj);

	 int updateBySql(String sql, Object... obj);

	/**
	 * 根据元数据code获取到数据源ID，如果数据源没配置，则获取系统默认数据源Id
	 * @param modelCode
	 * @return
	 */
	String getDataSourceIdByModelCode(String modelCode);

}
