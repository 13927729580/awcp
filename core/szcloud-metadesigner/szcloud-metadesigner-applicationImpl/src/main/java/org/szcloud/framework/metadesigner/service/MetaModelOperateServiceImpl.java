package org.szcloud.framework.metadesigner.service;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.EntityRepositoryJDBC;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.metadesigner.application.MetaModelItemService;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.metadesigner.application.MetaModelService;
import org.szcloud.framework.metadesigner.application.ModelRelationService;
import org.szcloud.framework.metadesigner.util.DataConvert;
import org.szcloud.framework.metadesigner.util.DataSourceFactory;
import org.szcloud.framework.metadesigner.vo.DataSourceManageVO;
import org.szcloud.framework.metadesigner.vo.MetaModelItemsVO;
import org.szcloud.framework.metadesigner.vo.MetaModelVO;
import org.szcloud.framework.metadesigner.vo.ModelRelationVO;
import org.szcloud.framework.unit.service.SysSourceRelationService;
import org.szcloud.framework.unit.vo.PunSystemVO;
import org.szcloud.framework.unit.vo.SysDataSourceVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Service(value = "metaModelOperateServiceImpl")
public class MetaModelOperateServiceImpl implements MetaModelOperateService {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(MetaModelOperateServiceImpl.class);
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MetaModelItemService metaModelItemsServiceImpl;

	@Autowired
	private MetaModelService metaModelServiceImpl;

	@Autowired
	private DataConvert dataConvertImpl;

	@Autowired
	private ModelRelationService modelRelationServiceImpl;

	@Autowired
	@Qualifier("sysSourceRelationServiceImpl")
	SysSourceRelationService sysSourceRelationService;

	/**
	 * 保存
	 */
	public Long save(Map<String, String> map, String modelCode) throws Exception {

		// 查询
		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode.trim());
		// 全局变量
		String fk_column = null;

		StringBuffer sb = new StringBuffer("insert into ").append(mm.getTableName()).append(" (");
		StringBuilder values = new StringBuilder();
		List<MetaModelItemsVO> mmi = this.metaModelItemsServiceImpl.queryResult("queryResult", mm.getId());
		Map<String, MetaModelItemsVO> map2 = new HashMap<String, MetaModelItemsVO>();
		Map<String, Object> maps = new HashMap<String, Object>();
		for (MetaModelItemsVO tmp : mmi) {
			map2.put(tmp.getItemCode(), tmp);
		}
		for (String s : map.keySet()) {
			MetaModelItemsVO tmp = map2.get(s);
			if (tmp != null) {
				Object o = this.dataConvertImpl.stringToObject(map.get(s), tmp.getItemType());
				maps.put(s, o);

				sb.append(s);
				sb.append(",");

				values.append(":");
				values.append(s);
				values.append(",");

			}
		}

		/**
		 * 去除groupId modify by venson 20170320
		 * 
		 */
		sb.delete(sb.length() - 1, sb.length());
		sb.append(") value(");
		sb.append(values.delete(values.length() - 1, values.length())).append(");");

		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
			namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
			SqlParameterSource paramSource = new MapSqlParameterSource(maps);
			namedParameterJdbcTemplate.update(sb.toString(), paramSource);

		} catch (Exception e) {
			e.printStackTrace();
		}

		StringBuffer sbs = new StringBuffer("select max(").append(fk_column).append(") from ")
				.append(mm.getTableName());
		Long l = null;
		try {

			l = jdbcTemplate.queryForLong(sbs.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	/**
	 * 保存
	 */

	public Map saveReturnMap(Map<String, String> map, String modelCode) throws Exception {

		// Long dataSourceId = getDataSourceIdByModelCode(modelCode);
		// EntityRepositoryJDBC jdbcRepository =
		// DataSourceFactory.getEntityRepositoryJDBCById(dataSourceId);

		// 查询
		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode);
		// 全局变量
		String fk_column = null;

		StringBuffer sb = new StringBuffer("insert into ").append(mm.getTableName()).append(" (");
		StringBuilder values = new StringBuilder();
		List<MetaModelItemsVO> mmi = this.metaModelItemsServiceImpl.queryResult("queryResult", mm.getId());
		Map<String, MetaModelItemsVO> map2 = new HashMap<String, MetaModelItemsVO>();
		Map<String, Object> maps = new HashMap<String, Object>();
		for (MetaModelItemsVO tmp : mmi) {
			map2.put(tmp.getItemCode(), tmp);
		}
		for (String s : map.keySet()) {
			MetaModelItemsVO tmp = map2.get(s);
			if (tmp != null) {
				Object o = this.dataConvertImpl.stringToObject(map.get(s), tmp.getItemType());
				maps.put(s, o);

				sb.append(s);
				sb.append(",");

				values.append(":");
				values.append(s);
				values.append(",");

			}
		}

		/**
		 * 去除groupId modify by venson 20170320
		 * 
		 */
		sb.delete(sb.length() - 1, sb.length());
		sb.append(") value(");
		sb.append(values.delete(values.length() - 1, values.length())).append(");");

		Map resultMap = new HashMap();
		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
			namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
			SqlParameterSource paramSource = new MapSqlParameterSource(maps);
			namedParameterJdbcTemplate.update(sb.toString(), paramSource);
			resultMap.put("success", "true");
		} catch (Exception e) {
			resultMap.put("success", "false");
			resultMap.put("message", e.getMessage());
			return resultMap;
		}

		StringBuffer sbs = new StringBuffer("select max(").append(fk_column).append(") from ")
				.append(mm.getTableName());
		Long l = null;
		try {

			l = jdbcTemplate.queryForLong(sbs.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 根据modelCode删除
	 */
	public boolean delete(Object id, String modelCode) {

		// Long dataSourceId = getDataSourceIdByModelCode(modelCode);
		// EntityRepositoryJDBC jdbcRepository =
		// DataSourceFactory.getEntityRepositoryJDBCById(dataSourceId);

		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode);
		StringBuffer sb = new StringBuffer("delete from ").append(mm.getTableName()).append(" where ");
		List<MetaModelItemsVO> lss = this.metaModelItemsServiceImpl.queryResult("queryResult", mm.getId());
		for (MetaModelItemsVO vo : lss) {
			if (vo.getUsePrimaryKey() != null && (vo.getUsePrimaryKey() == 1)) {
				sb.append(vo.getItemCode());
			}
		}
		sb.append("=");
		if (id instanceof String) {
			sb.append("'" + id + "'");
		} else {
			sb.append(id);
		}
		// 查询是否有外键
		List<ModelRelationVO> list = this.modelRelationServiceImpl.queryByModelId(mm.getId());
		if (list.size() > 0) {
			for (ModelRelationVO ls : list) {
				// 查询外键列
				MetaModelItemsVO mmi = this.metaModelItemsServiceImpl.get(ls.getItemId());
				// 查询对应的外键表
				MetaModelVO mmv = this.metaModelServiceImpl.get(mmi.getModelId());
				StringBuffer sbb = new StringBuffer("delete from ");
				sbb.append(mmv.getTableName());
				sbb.append(" where ");
				sbb.append(mmi.getItemCode());
				sbb.append("=");
				sbb.append(id);
				try {
					jdbcTemplate.execute(sbb.toString());
				} catch (Exception e) {
					return false;
				}
			}
		}
		try {
			jdbcTemplate.execute(sb.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 根据modelCode删除
	 */

	public Map deleteReturnMap(Object id, String modelCode) {

		// Long dataSourceId = getDataSourceIdByModelCode(modelCode);
		// EntityRepositoryJDBC jdbcRepository =
		// DataSourceFactory.getEntityRepositoryJDBCById(dataSourceId);

		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode);
		StringBuffer sb = new StringBuffer("delete from ").append(mm.getTableName()).append(" where ");
		List<MetaModelItemsVO> lss = this.metaModelItemsServiceImpl.queryResult("queryResult", mm.getId());
		for (MetaModelItemsVO vo : lss) {
			if (vo.getUsePrimaryKey() != null && (vo.getUsePrimaryKey() == 1)) {
				sb.append(vo.getItemCode());
			}
		}
		sb.append("=");
		if (id instanceof String) {
			sb.append("'" + id + "'");
		} else {
			sb.append(id);
		}
		Map resultMap = new HashMap();
		// 查询是否有外键
		List<ModelRelationVO> list = this.modelRelationServiceImpl.queryByModelId(mm.getId());
		if (list.size() > 0) {
			for (ModelRelationVO ls : list) {
				// 查询外键列
				MetaModelItemsVO mmi = this.metaModelItemsServiceImpl.get(ls.getItemId());
				// 查询对应的外键表
				MetaModelVO mmv = this.metaModelServiceImpl.get(mmi.getModelId());
				StringBuffer sbb = new StringBuffer("delete from ");
				sbb.append(mmv.getTableName());
				sbb.append(" where ");
				sbb.append(mmi.getItemCode());
				sbb.append("=");
				sbb.append(id);
				try {
					jdbcTemplate.execute(sbb.toString());
					resultMap.put("success", "true");
				} catch (Exception e) {
					resultMap.put("success", "false");
					resultMap.put("message", e.getMessage());
					return resultMap;
				}
			}
		}
		try {
			jdbcTemplate.execute(sb.toString());
			resultMap.put("success", "true");
			return resultMap;
		} catch (Exception e) {
			resultMap.put("success", "false");
			resultMap.put("message", e.getMessage());
			return resultMap;
		}
	}

	/**
	 * 根据条件删除
	 * 
	 * @params:map 条件：map中的键（key）需要有前缀 “=”--"et_" ">"--"gt_" "<"--"lt_"
	 *             "!="--"nt_" "like"--"lt_"
	 * @param :modelCode
	 */
	public Map deleteByParamsReturnMap(Map<String, String> map, String modelCode) throws ParseException {

		// Long dataSourceId = getDataSourceIdByModelCode(modelCode);
		// EntityRepositoryJDBC jdbcRepository =
		// DataSourceFactory.getEntityRepositoryJDBCById(dataSourceId);

		StringBuffer sb = new StringBuffer("delete from ");
		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode);
		sb.append(mm.getTableName());
		sb.append(" where 1=1");
		if (map != null && map.size() > 0) {
			for (String s : map.keySet()) {
				sb.append(" and ");
				sb.append(s.substring(3, s.length()));
				sb.append(this.markCheck(s));
				sb.append(":");
				sb.append(s);
			}
		}

		Map resultMap = new HashMap();

		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
			namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
			SqlParameterSource paramSource = new MapSqlParameterSource(map);
			namedParameterJdbcTemplate.update(sb.toString(), paramSource);
			resultMap.put("success", "true");
			return resultMap;
		} catch (Exception e) {
			resultMap.put("success", "false");
			resultMap.put("message", e.getMessage());
			return resultMap;
		}
	}

	/**
	 * 根据条件删除
	 * 
	 * @params:map 条件：map中的键（key）需要有前缀 “=”--"et_" ">"--"gt_" "<"--"lt_"
	 *             "!="--"nt_" "like"--"lt_"
	 * @param :modelCode
	 */
	public boolean deleteByParams(Map<String, String> map, String modelCode) throws ParseException {

		StringBuffer sb = new StringBuffer("delete from ");
		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode);
		sb.append(mm.getTableName());
		sb.append(" where 1=1");
		if (map != null && map.size() > 0) {
			for (String s : map.keySet()) {
				sb.append(" and ");
				sb.append(s);
				sb.append(this.markCheck(s));
				sb.append(":");
				sb.append(s);
			}
		}

		try {
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
			namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
			SqlParameterSource paramSource = new MapSqlParameterSource(map);
			namedParameterJdbcTemplate.update(sb.toString(), paramSource);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 根据条件删修改 map为将要更新的数据
	 * 
	 * @params:map map中的key为 modelCode中的字段名
	 * @param :modelCode
	 */
	public boolean update(Map<String, String> map, String modelCode) throws ParseException {

		// Long dataSourceId = getDataSourceIdByModelCode(modelCode);
		// EntityRepositoryJDBC jdbcRepository =
		// DataSourceFactory.getEntityRepositoryJDBCById(dataSourceId);

		// 根据modelcode查出模型
		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode.trim());
		StringBuffer sb = new StringBuffer("update ").append(mm.getTableName()).append(" set ");
		StringBuilder where = new StringBuilder(" where ");
		// 根据modelid找到所有的属性
		List<MetaModelItemsVO> lss = this.metaModelItemsServiceImpl.queryResult("queryResult", mm.getId());
		Map<String, MetaModelItemsVO> map2 = new HashMap<String, MetaModelItemsVO>();
		// 将查出的list转换成map，key为属性code
		for (MetaModelItemsVO tmp : lss) {
			map2.put(tmp.getItemCode(), tmp);
		}
		// 遍历数据，拼装SQL语句
		Map<String, Object> maps = new HashMap<String, Object>();
		int size = 0;// 更新的字段数，如果为空则不更新
		for (String s : map.keySet()) {
			// 根据数据的key（也就是数据字段的名字），找到对应的模型属性
			MetaModelItemsVO tmp = map2.get(s);
			if (tmp != null) {
				// 将数据进行转化，因为提交的数据都是字符串类型，而数据库中有多种
				Object o = this.dataConvertImpl.stringToObject(map.get(s), tmp.getItemType());
				maps.put(s, o);
				// 如果模型属性是主键，则特殊处理，否则写成itemcode=:itemcode的形式
				if (tmp.getUsePrimaryKey() != null && (tmp.getUsePrimaryKey() == 1)) {
					if (tmp.getItemType().equalsIgnoreCase("varchar") || tmp.getItemType().equalsIgnoreCase("char")) {
						where.append(tmp.getItemCode()).append("=").append("'").append(map.get(tmp.getItemCode()))
								.append("'");
					} else {
						where.append(tmp.getItemCode());
						where.append("=");
						where.append(map.get(tmp.getItemCode()));
					}
				} else {
					sb.append(tmp.getItemCode());
					sb.append("=");
					sb.append(":");
					sb.append(tmp.getItemCode());
					sb.append(",");
					size++;
				}

			}
		}

		sb.deleteCharAt(sb.length() - 1).append(where);

		try {
			if (size > 0) {// 如果要更新的字段数大于0，则更新
				NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
				namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
				SqlParameterSource paramSource = new MapSqlParameterSource(maps);
				namedParameterJdbcTemplate.update(sb.toString(), paramSource);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据条件删修改 map为将要更新的数据
	 * 
	 * @params:map map中的key为 modelCode中的字段名
	 * @param :modelCode
	 */
	public Map updateReturnMap(Map<String, String> map, String modelCode) throws ParseException {

		// 根据modelcode查出模型
		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode);
		StringBuffer sb = new StringBuffer("update ").append(mm.getTableName()).append(" set ");
		StringBuilder where = new StringBuilder(" where ");
		// 根据modelid找到所有的属性
		List<MetaModelItemsVO> lss = this.metaModelItemsServiceImpl.queryResult("queryResult", mm.getId());
		Map<String, MetaModelItemsVO> map2 = new HashMap<String, MetaModelItemsVO>();
		// 将查出的list转换成map，key为属性code
		for (MetaModelItemsVO tmp : lss) {
			map2.put(tmp.getItemCode(), tmp);
		}
		// 遍历数据，拼装SQL语句
		Map<String, Object> maps = new HashMap<String, Object>();
		int size = 0;// 更新的字段数，如果为空则不更新
		for (String s : map.keySet()) {
			// 根据数据的key（也就是数据字段的名字），找到对应的模型属性
			MetaModelItemsVO tmp = map2.get(s);
			if (tmp != null) {
				// 将数据进行转化，因为提交的数据都是字符串类型，而数据库中有多种
				Object o = this.dataConvertImpl.stringToObject(map.get(s), tmp.getItemType());
				maps.put(s, o);
				// 如果模型属性是主键，则特殊处理，否则写成itemcode=:itemcode的形式
				if (tmp.getUsePrimaryKey() != null && (tmp.getUsePrimaryKey() == 1)) {
					if (tmp.getItemType().equalsIgnoreCase("varchar") || tmp.getItemType().equalsIgnoreCase("char")) {
						where.append(tmp.getItemCode()).append("=").append("'").append(map.get(tmp.getItemCode()))
								.append("'");
					} else {
						where.append(tmp.getItemCode());
						where.append("=");
						where.append(map.get(tmp.getItemCode()));
					}
				} else {
					sb.append(tmp.getItemCode());
					sb.append("=");
					sb.append(":");
					sb.append(tmp.getItemCode());
					sb.append(",");
					size++;
				}

			}
		}

		sb.deleteCharAt(sb.length() - 1).append(where);

		Map resultMap = new HashMap();
		try {
			if (size > 0) {// 如果要更新的字段数大于0，则更新
				NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
				namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
				SqlParameterSource paramSource = new MapSqlParameterSource(maps);
				namedParameterJdbcTemplate.update(sb.toString(), paramSource);
			}
			resultMap.put("success", "true");
			return resultMap;
		} catch (Exception e) {
			resultMap.put("success", "false");
			resultMap.put("message", e.getMessage());
			return resultMap;
		}
	}

	/**
	 * 查询
	 * 
	 * @param :modelCode
	 */
	public List<Map<String, String>> findAll(String modelCode) {

		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode);
		StringBuffer sb = new StringBuffer("select ");
		List<MetaModelItemsVO> lss = this.metaModelItemsServiceImpl.queryResult("queryResult", mm.getId());
		for (MetaModelItemsVO vo : lss) {
			sb.append(vo.getItemCode());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" from ");
		sb.append(mm.getTableName());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sb.toString());
		List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
		for (Map<String, Object> m : list) {
			Map<String, String> maps = new HashMap<String, String>();
			for (String s : m.keySet()) {
				maps.put(s, String.valueOf(m.get(s)));
			}
			ls.add(maps);
		}
		return ls;
	}

	/**
	 * 分页查询
	 * 
	 * @param :modelCode
	 */
	public List<Map<String, String>> queryPageResult(String modelCode) {
		//
		MetaModelVO mm = this.metaModelServiceImpl.queryByModelCode(modelCode);
		StringBuffer sb = new StringBuffer("select ");
		List<MetaModelItemsVO> lss = this.metaModelItemsServiceImpl.queryResult("queryResult", mm.getId());
		for (MetaModelItemsVO vo : lss) {
			sb.append(vo.getItemCode());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" from ");
		sb.append(mm.getTableName());
		List<Map<String, Object>> list = null;
		try {
			// 封装分页SQL
			String sqls = jeecgCreatePageSql(sb.toString(), 1, 3);
			jdbcTemplate.queryForList(sqls);
			// list=jdbcRepository.find(sb.toString(), 1, 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
		for (Map<String, Object> m : list) {
			Map<String, String> maps = new HashMap<String, String>();
			for (String s : m.keySet()) {
				if (m.get(s) != null)
					maps.put(s, String.valueOf(m.get(s)));
				else
					maps.put(s, null);
			}
			ls.add(maps);
		}
		return ls;
	}

	/**
	 * 根据modelCode,modelName 模糊查询
	 * 
	 * @param modelCode
	 * @param modelName
	 * @param page
	 * @param rows
	 * @return
	 */
	@SuppressWarnings("null")
	public String queryPageByModel(String modelCode, String modelName, int page, int rows) {

		Long dataSourceId = getDataSourceIdByModelCode(modelCode);
		EntityRepositoryJDBC jdbcRepository = DataSourceFactory.getEntityRepositoryJDBCById(dataSourceId);

		StringBuffer b = new StringBuffer(
				"select modelClassId,modelCode,modelName,modelDesc,tableName,projectName,modelType,modelSynchronization,modelValid,id from fw_mm_metaModel where 1=1");
		Object[] objs = null;
		if (modelCode != null && modelCode.equals("")) {
			objs[1] = modelCode;
			b.append(" and modelCode like %");
			b.append(modelCode).append("%");

		}
		if (modelName != null && modelName.equals("")) {
			objs[objs.length + 1] = modelName;
			b.append(" and modelName like %");
			b.append(modelName).append("%");
		}

		List<Map<String, Object>> list = null;
		try {
			list = jdbcRepository.find(b.toString(), page, rows, objs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				sb.append("\"").append(key).append("\":\"").append(map.get(key)).append("\"").append(",");
			}
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("}");

		return sb.toString();
	}

	/**
	 * 单个查询
	 * 
	 * @param :modelCode
	 */
	public Map<String, String> get(Object id, String modelCode) {

		MetaModelVO mms = this.metaModelServiceImpl.queryByModelCode(modelCode);
		StringBuffer sb = new StringBuffer("select ");
		List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult", mms.getId());
		String name = "";
		for (MetaModelItemsVO mm : ls) {
			String itemCode = mm.getItemCode();
			if (itemCode.equalsIgnoreCase("CREATOR")) {
				sb.append("(SELECT NAME FROM p_un_user_base_info WHERE user_id=creator) AS CREATOR");
				sb.append(",");
			} else {
				sb.append(itemCode);
				sb.append(",");
			}

			if ((mm.getUsePrimaryKey() != null) && (mm.getUsePrimaryKey() == 1)) {
				name = mm.getItemCode();
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" from ");
		MetaModelVO mmv = this.metaModelServiceImpl.get(mms.getId());
		sb.append(mmv.getTableName());
		sb.append(" where ");
		sb.append(name);
		sb.append("='");
		sb.append(id);
		sb.append("'");
		List<Map<String, Object>> map = jdbcTemplate.queryForList(sb.toString());
		Map<String, String> maps = new HashMap<String, String>();
		if (map != null && map.size() > 0) {
			for (String s : map.get(0).keySet()) {
				maps.put(s, String.valueOf(map.get(0).get(s)));
			}
		}
		return maps;
	}

	/**
	 * 根据条件查询
	 * 
	 * @params:page
	 * @params:map 条件：map中的键（key）需要有前缀 “=”--"et_" ">"--"gt_" "<"--"lt_"
	 *             "!="--"nt_" "like"--"lt_"
	 * @param :modelCode
	 * @rows
	 */
	public List<Map<String, String>> queryResultByParamsOrPage(int page, int rows, String modelCode,
			Map<String, String> map) throws ParseException {

		MetaModelVO mmv = this.metaModelServiceImpl.queryByModelCode(modelCode);
		StringBuffer sb = new StringBuffer("select ");
		List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult", mmv.getId());
		for (MetaModelItemsVO mm : ls) {
			sb.append(mm.getItemCode());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" from ");
		sb.append(mmv.getTableName());
		sb.append(" where 100>1");
		if (map != null && map.size() > 0) {
			for (String s : map.keySet()) {
				sb.append(" and ");
				sb.append(s.subSequence(3, s.length()));
				sb.append(this.markCheck(s));
				if (s.substring(0, 3).equals("in_")) {
					sb.append("(");
					logger.debug(s.split(",").length + "");
					for (int i = 0; i < map.get(s).split(",").length; i++) {
						sb.append("?,");
					}
					sb.deleteCharAt(sb.length() - 1);
					sb.append(")");
				} else {
					sb.append("?");
				}
			}
		}
		List<Object> list = new ArrayList<Object>();
		if (map != null && map.size() > 0) {
			for (String s : map.keySet()) {
				for (MetaModelItemsVO m : ls) {
					if (m.getItemCode().equals(s.substring(3, s.length()))) {
						if (s.substring(0, 3).equals("in_")) {
							String[] ss = map.get(s).split(",");
							for (String sh : ss) {
								if (m.getItemType().equals("一对一") || m.getItemType().equals("多对一")) {
									ModelRelationVO mro = this.modelRelationServiceImpl.queryByItem(m.getId());
									MetaModelVO mmvo = this.metaModelServiceImpl.get(mro.getModelId());
									List<MetaModelItemsVO> mms = this.metaModelItemsServiceImpl
											.queryResult("queryResult", mmvo.getId());
									for (MetaModelItemsVO ms : mms) {
										if (m.getUsePrimaryKey() != null && (m.getUsePrimaryKey() == 1)) {
											Object os = this.dataConvertImpl.stringToObject(sh, ms.getItemType());
											list.add(os);
										}
									}
								} else {
									Object o = this.dataConvertImpl.stringToObject(sh, m.getItemType());
									list.add(o);
								}
							}
						} else {
							if (m.getItemType().equals("一对一") || m.getItemType().equals("多对一")) {
								ModelRelationVO mro = this.modelRelationServiceImpl.queryByItem(m.getId());
								MetaModelVO mmvo = this.metaModelServiceImpl.get(mro.getModelId());
								List<MetaModelItemsVO> mms = this.metaModelItemsServiceImpl.queryResult("queryResult",
										mmvo.getId());
								for (MetaModelItemsVO ms : mms) {
									if (m.getUsePrimaryKey() != null && (m.getUsePrimaryKey() == 1)) {
										Object os = this.dataConvertImpl.stringToObject(map.get(s), ms.getItemType());
										list.add(os);
									}
								}

							} else {
								Object o = this.dataConvertImpl.stringToObject(map.get(s), m.getItemType());
								list.add(o);
							}
						}

					}
				}
			}
		}
		List<Map<String, Object>> lss = null;
		try {
			String sqls = jeecgCreatePageSql(sb.toString(), page, rows);
			lss = jdbcTemplate.queryForList(sqls, list.toArray());
		} catch (Exception e) {
			logger.debug("没有");
		}

		List<Map<String, String>> lsss = new ArrayList<Map<String, String>>();
		try {
			for (Map<String, Object> m : lss) {
				Map<String, String> mapi = new HashMap<String, String>();
				for (String s : m.keySet()) {
					if (m.get(s) != null)
						mapi.put(s, String.valueOf(m.get(s)));
					else
						mapi.put(s, null);
				}
				lsss.add(mapi);
			}
		} catch (Exception e) {
			logger.debug("查询出来的值是空");
		}
		return lsss;
	}

	public String markCheck(String str) {

		String s = str.substring(0, 3);
		if (s.equals("lk_")) {
			return " like ";
		}
		if (s.equals("gt_")) {
			return ">=";
		}
		if (s.equals("lt_")) {
			return "<=";
		}
		if (s.equals("nt_")) {
			return "!=";
		}
		if (s.equals("et_")) {
			return "=";
		}
		if (s.equals("in_")) {
			return " in ";
		}
		return "=";
	}

	@Override
	public Integer createDatabase(DataSourceManageVO vo) {
		String sql = "create database if not exists " + vo.getName() + " default charset='utf8';";
		return jdbcTemplate.update(sql);
	}

	public List<Map<String, Object>> search(String sql, Object... obj) {
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sql, obj);
		return ls;
	}

	public int queryOne(String sql, Object... obj) {
		int count = jdbcTemplate.queryForInt(sql, obj);
		return count;
	}

	/**
	 * 
	 * update
	 */
	public int updateBySql(String sql, Object... obj) {
		int count = jdbcTemplate.update(sql, obj);
		return count;
	}

	/**
	 * 根据元数据code获取到数据源ID，如果数据源没配置，则获取系统默认数据源Id
	 */
	public Long getDataSourceIdByModelCode(String modelCode) {
		MetaModelVO mmv = metaModelServiceImpl.queryByModelCode(modelCode);
		if (mmv != null && mmv.getDataSourceId() != null) { // 如果有配置数据源
			return mmv.getDataSourceId();
		} else {
			// 系统默认数据源
			Object obj = Tools.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
			PunSystemVO system = null;
			if (obj instanceof PunSystemVO) {
				system = (PunSystemVO) obj;
			}
			BaseExample base = new BaseExample();
			base.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andEqualTo("ISDEFAULT", true);
			PageList<SysDataSourceVO> dataVos = sysSourceRelationService.selectPagedByExample(base, 1,
					Integer.MAX_VALUE, null);
			if (dataVos != null && dataVos.size() > 0) {
				return dataVos.get(0).getDataSourceId();
			}
		}
		return null;
	}

	/**
	 * 数据库类型
	 */
	public static final String DATABSE_TYPE_MYSQL = "mysql";
	public static final String DATABSE_TYPE_POSTGRE = "postgresql";
	public static final String DATABSE_TYPE_ORACLE = "oracle";
	public static final String DATABSE_TYPE_SQLSERVER = "sqlserver";
	/**
	 * 分页SQL
	 */
	public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}"; // mysql
	public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";// postgresql
	public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; // oracle
	public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; // sqlserver

	/**
	 * 按照数据库类型，封装SQL
	 * 
	 * @throws SQLException
	 */
	public String jeecgCreatePageSql(String sql, int page, int rows) throws SQLException {
		int beginNum = (page - 1) * rows;
		String[] sqlParam = new String[3];
		sqlParam[0] = sql;
		sqlParam[1] = beginNum + "";
		sqlParam[2] = rows + "";
		if (jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName()
				.indexOf(DATABSE_TYPE_MYSQL) != -1) {
			sql = MessageFormat.format(MYSQL_SQL, sqlParam);
		} else if (jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName()
				.indexOf(DATABSE_TYPE_POSTGRE) != -1) {
			sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
		} else {
			int beginIndex = (page - 1) * rows;
			int endIndex = beginIndex + rows;
			sqlParam[2] = Integer.toString(beginIndex);
			sqlParam[1] = Integer.toString(endIndex);
			if (jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName()
					.indexOf(DATABSE_TYPE_ORACLE) != -1) {
				sql = MessageFormat.format(ORACLE_SQL, sqlParam);
			} else if (jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName()
					.indexOf(DATABSE_TYPE_SQLSERVER) != -1) {
				sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
				sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
			}
		}
		return sql;
	}

	private int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

	@Override
	public Object queryObject(String sql, Object... obj) {
		List<Map<String, Object>> data = this.search(sql, obj);
		if (data == null || data.isEmpty()) {
			return 0;
		} else {
			return data.get(0).values().iterator().next();
		}
	}

}
