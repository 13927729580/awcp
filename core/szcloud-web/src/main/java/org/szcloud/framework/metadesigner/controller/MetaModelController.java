package org.szcloud.framework.metadesigner.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.Criteria;
import org.szcloud.framework.core.domain.EntityRepositoryJDBC;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.metadesigner.application.DataSourceManageService;
import org.szcloud.framework.metadesigner.application.MetaModelClassService;
import org.szcloud.framework.metadesigner.application.MetaModelItemService;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.metadesigner.application.MetaModelService;
import org.szcloud.framework.metadesigner.application.ModelRelationService;
import org.szcloud.framework.metadesigner.util.ICreateTables;
import org.szcloud.framework.metadesigner.util.UpdateColumn;
import org.szcloud.framework.metadesigner.vo.DataSourceManageVO;
import org.szcloud.framework.metadesigner.vo.MetaModelClassVO;
import org.szcloud.framework.metadesigner.vo.MetaModelItemsVO;
import org.szcloud.framework.metadesigner.vo.MetaModelVO;
import org.szcloud.framework.metadesigner.vo.ModelRelationVO;
import org.szcloud.framework.unit.service.PunSystemService;
import org.szcloud.framework.unit.service.SysSourceRelationService;
import org.szcloud.framework.unit.vo.PunSystemVO;
import org.szcloud.framework.unit.vo.SysDataSourceVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Controller
@RequestMapping("/metaModel")
public class MetaModelController extends BaseController {
	/**
	 * 日志对象
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EntityRepositoryJDBC jdbcRepository;

	@Autowired
	private MetaModelService metaModelServiceImpl;

	@Autowired
	private MetaModelItemService metaModelItemsServiceImpl;

	@Autowired
	private MetaModelClassService metaModelClassServiceImpl;

	@Autowired
	private ModelRelationService modelRelationServiceImpl;

	@Autowired
	private MetaModelOperateService metaModelOperateServiceImpl;

	@Autowired
	private ICreateTables createTables;

	@Autowired
	@Qualifier("dataSourceManageServiceImpl")
	DataSourceManageService dataSourceService;

	@Autowired
	@Qualifier("punSystemServiceImpl")
	PunSystemService sysService;

	@Autowired
	@Qualifier("sysSourceRelationServiceImpl")
	SysSourceRelationService sysSourceRelationService;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@ResponseBody
	@RequestMapping(value = "/addByDb")
	public String addByDb(String datasourceJson, String[] tableNames, Long systemId) {
		JSONObject rtn = new JSONObject();

		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		PunSystemVO system = null;
		if (obj instanceof PunSystemVO) {
			system = (PunSystemVO) obj;
		}

		// compare tableNames is in metamodel (in system)
		BaseExample baseExample = new BaseExample();
		baseExample.createCriteria().andInStr("modelCode", Arrays.asList(tableNames));
		List<MetaModelVO> list = metaModelServiceImpl.selectPagedByExample(baseExample, 1, Integer.MAX_VALUE, null);
		if (list.size() > 0) {
			rtn.put("result", "-1");
			StringBuilder sb = new StringBuilder("以下table已经存在：");
			for (MetaModelVO vo : list) {
				sb.append(vo.getModelCode()).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			rtn.put("message", sb.toString());
		} else {
			// 获取数据库信息和用户选择的需要转换的table
			DataSourceManageVO vo = JSON.parseObject(StringEscapeUtils.unescapeHtml4(datasourceJson),
					DataSourceManageVO.class);
			Connection conn = null;
			Map<String, JSONObject> tmp = new HashMap<String, JSONObject>();
			try {
				Class.forName(vo.getSourceDriver());
				conn = DriverManager.getConnection(vo.getSourceUrl(), vo.getUserName(), vo.getUserPwd());
				DatabaseMetaData metaData = conn.getMetaData();
				for (int i = 0; i < tableNames.length; i++) {
					JSONObject modelObject = new JSONObject();

					MetaModelVO modelVO = new MetaModelVO();
					modelVO.setModelCode(tableNames[i]);
					modelVO.setModelName(tableNames[i]);
					modelVO.setTableName(tableNames[i]);
					modelObject.put("model", JSON.toJSONString(modelVO));
					Map<String, MetaModelItemsVO> itemMap = new HashMap<String, MetaModelItemsVO>();

					Map<String, JSONObject> fkMap = new HashMap<String, JSONObject>();
					Map<String, String> uniqueMap = new HashMap<String, String>();
					// 表的列数据
					ResultSet colRet = metaData.getColumns(null, "%", tableNames[i], "%");
					while (colRet.next()) {
						// COLUMN_NAME就是字段的名字，
						// TYPE_NAME就是数据类型，比如"int","int unsigned"等等，
						// COLUMN_SIZE返回整数，就是字段的长度，比如定义的int(8)的字段，返回就是8，
						// NULLABLE，返回1就表示可以是Null,而0就表示Not Null
						String columnName = colRet.getString("COLUMN_NAME");
						String columnType = colRet.getString("TYPE_NAME");
						int datasize = colRet.getInt("COLUMN_SIZE");
						int digits = colRet.getInt("DECIMAL_DIGITS");
						int nullable = colRet.getInt("NULLABLE");
						MetaModelItemsVO item = new MetaModelItemsVO();
						item.setItemName(columnName);
						item.setItemLength(String.valueOf(datasize));
						item.setUseNull(nullable);
						item.setItemCode(columnName);
						item.setItemType(columnType);
						item.setItemValid(1);
						itemMap.put(columnName, item);

						// logger.debug(columnName+" "+columnType+" "+datasize+"
						// "+digits+" "+nullable);
					}
					/*
					 * primaryKeyResultSet 结果集
					 * 
					 * TABLE_CAT String => 表类别（可为 null） TABLE_SCHEM String =>
					 * 表模式（可为 null） TABLE_NAME String => 表名称 COLUMN_NAME String
					 * => 列名称 KEY_SEQ short => 主键中的序列号（值 1 表示主键中的第一列，值 2
					 * 表示主键中的第二列）。 PK_NAME String => 主键的名称（可为 null）
					 * 
					 * 
					 */
					ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, "%", tableNames[i]);
					while (primaryKeyResultSet.next()) {
						String primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
						MetaModelItemsVO item = itemMap.get(primaryKeyColumnName);
						if (item != null) {
							item.setUsePrimaryKey(1);
						}
					}
					modelObject.put("items", JSON.toJSONString(itemMap));

					ResultSet indexSet = metaData.getIndexInfo(null, null, tableNames[i], true, false);
					while (indexSet.next()) {
						String columnName = indexSet.getString("COLUMN_NAME");
						uniqueMap.put(columnName, columnName);
						// int count = indexSet.getMetaData().getColumnCount();
						// for(int i = 0; i < count; i++){
						// logger.debug(indexSet.getMetaData().getColumnName(i));
						// }
					}
					modelObject.put("indexs", JSON.toJSONString(uniqueMap));

					/*
					 * foreignKeyResultSet 结果集
					 * 
					 * PKTABLE_CAT String => 被导入的主键表类别（可为 null） PKTABLE_SCHEM
					 * String => 被导入的主键表模式（可为 null） PKTABLE_NAME String =>
					 * 被导入的主键表名称 PKCOLUMN_NAME String => 被导入的主键列名称 FKTABLE_CAT
					 * String => 外键表类别（可为 null） FKTABLE_SCHEM String => 外键表模式（可为
					 * null） FKTABLE_NAME String => 外键表名称 FKCOLUMN_NAME String
					 * => 外键列名称 KEY_SEQ short => 外键中的序列号（值 1 表示外键中的第一列，值 2
					 * 表示外键中的第二列） UPDATE_RULE short => 更新主键时外键发生的变化 DELETE_RULE
					 * short => 删除主键时外键发生的变化 PK_NAME String => 主键的名称（可为 null）
					 * FK_NAME String => 外键的名称（可为 null） DEFERRABILITY short =>
					 * 是否可以将对外键约束的评估延迟到提交时间
					 * 
					 * 
					 */
					ResultSet foreignKeyResultSet = metaData.getImportedKeys(null, null, tableNames[i]);
					while (foreignKeyResultSet.next()) {
						String fkTableName = foreignKeyResultSet.getString("FKTABLE_NAME");
						String fkColumnName = foreignKeyResultSet.getString("FKCOLUMN_NAME");
						String pkTablenName = foreignKeyResultSet.getString("PKTABLE_NAME");
						String pkColumnName = foreignKeyResultSet.getString("PKCOLUMN_NAME");
						JSONObject o = new JSONObject();
						o.put("FKTABLE_NAME", fkTableName);
						o.put("FKCOLUMN_NAM", fkColumnName);
						o.put("PKTABLE_NAME", pkTablenName);
						o.put("PKCOLUMN_NAME", pkColumnName);
						fkMap.put(pkColumnName, o);
					}
					modelObject.put("fks", JSON.toJSONString(fkMap));

					tmp.put(tableNames[i], modelObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

			// 不带关联关系的导入
			for (Iterator<String> it = tmp.keySet().iterator(); it.hasNext();) {
				// 先判断是否有已经存在的table对应的元数据或者name为tableName的一样
				String tableName = it.next();
				JSONObject o = tmp.get(tableName);
				// 通过外键和index判断是 一对多还是 一对一关系
				// 通过外键和外键关联的表判断外键是否需要加入进来
				MetaModelVO metaModelVO = JSON.parseObject(o.getString("model"), MetaModelVO.class);
				metaModelVO.setSystemId(system.getSysId());
				metaModelServiceImpl.save(metaModelVO);
				JSONObject items = JSON.parseObject(o.getString("items"));
				// JSONObject fks = o.getJSONObject("fks");
				// JSONObject indexs = o.getJSONObject("index");

				for (Iterator<String> it2 = items.keySet().iterator(); it2.hasNext();) {
					String columName = it2.next();
					MetaModelItemsVO itemsVO = items.getObject(columName, MetaModelItemsVO.class);
					itemsVO.setModelId(metaModelVO.getId());
					metaModelItemsServiceImpl.save(itemsVO);
					// 判断是否为外键,且有用的外键（外键的关联的表存在）
					// if(fks.containsKey(columName)){
					// String fkTableName = fks.getString("FKTABLE_NAME");
					// //应该在数据库中查询
					// Map<String, Object> params = new HashMap<String,
					// Object>();
					// params.put("tableName", fkTableName);
					// List<MetaModelVO> vos =
					// metaModelServiceImpl.queryPagedResult(params, 1, 10,
					// null);
					//// String
					//// if()
					// }

					// items.getObject("", clazz)
				}
			}

			rtn.put("result", "1");
		}

		return rtn.toJSONString();
	}

	@RequestMapping(value = "/synchronizedMeta")
	public String synchronizedMeta(@RequestParam(value = "id") Long[] id) {
		JSONObject rtn = new JSONObject();

		Map<String, JSONObject> tmp = new HashMap<String, JSONObject>();
		try {
			DatabaseMetaData metaData = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource()
					.getConnection().getMetaData();
			for (int i = 0; i < id.length; i++) {
				JSONObject modelObject = new JSONObject();

				this.metaModelItemsServiceImpl.removeByFk(id[i]);

				MetaModelVO mmv = this.metaModelServiceImpl.get(id[i]);

				Map<String, MetaModelItemsVO> itemMap = new HashMap<String, MetaModelItemsVO>();

				Map<String, JSONObject> fkMap = new HashMap<String, JSONObject>();
				Map<String, String> uniqueMap = new HashMap<String, String>();
				// 表的列数据
				ResultSet colRet = metaData.getColumns(null, "%", mmv.getTableName(), "%");
				while (colRet.next()) {
					// COLUMN_NAME就是字段的名字，
					// TYPE_NAME就是数据类型，比如"int","int unsigned"等等，
					// COLUMN_SIZE返回整数，就是字段的长度，比如定义的int(8)的字段，返回就是8，
					// NULLABLE，返回1就表示可以是Null,而0就表示Not Null
					String columnName = colRet.getString("COLUMN_NAME");
					String columnType = colRet.getString("TYPE_NAME");
					int datasize = colRet.getInt("COLUMN_SIZE");
					int digits = colRet.getInt("DECIMAL_DIGITS");
					int nullable = colRet.getInt("NULLABLE");
					MetaModelItemsVO item = new MetaModelItemsVO();
					item.setItemName(columnName);
					item.setItemLength(String.valueOf(datasize));
					item.setUseNull(nullable);
					item.setItemCode(columnName);
					item.setItemType(columnType);
					item.setItemValid(1);
					itemMap.put(columnName, item);

					// logger.debug(columnName+" "+columnType+" "+datasize+"
					// "+digits+" "+nullable);
				}
				/*
				 * primaryKeyResultSet 结果集
				 * 
				 * TABLE_CAT String => 表类别（可为 null） TABLE_SCHEM String => 表模式（可为
				 * null） TABLE_NAME String => 表名称 COLUMN_NAME String => 列名称
				 * KEY_SEQ short => 主键中的序列号（值 1 表示主键中的第一列，值 2 表示主键中的第二列）。
				 * PK_NAME String => 主键的名称（可为 null）
				 * 
				 * 
				 */
				ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, "%", mmv.getTableName());
				while (primaryKeyResultSet.next()) {
					String primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
					MetaModelItemsVO item = itemMap.get(primaryKeyColumnName);
					if (item != null) {
						item.setUsePrimaryKey(1);
					}
				}
				modelObject.put("items", JSON.toJSONString(itemMap));

				ResultSet indexSet = metaData.getIndexInfo(null, null, mmv.getTableName(), true, false);
				while (indexSet.next()) {
					String columnName = indexSet.getString("COLUMN_NAME");
					uniqueMap.put(columnName, columnName);
					// int count = indexSet.getMetaData().getColumnCount();
					// for(int i = 0; i < count; i++){
					// logger.debug(indexSet.getMetaData().getColumnName(i));
					// }
				}
				modelObject.put("indexs", JSON.toJSONString(uniqueMap));

				/*
				 * foreignKeyResultSet 结果集
				 * 
				 * PKTABLE_CAT String => 被导入的主键表类别（可为 null） PKTABLE_SCHEM String
				 * => 被导入的主键表模式（可为 null） PKTABLE_NAME String => 被导入的主键表名称
				 * PKCOLUMN_NAME String => 被导入的主键列名称 FKTABLE_CAT String =>
				 * 外键表类别（可为 null） FKTABLE_SCHEM String => 外键表模式（可为 null）
				 * FKTABLE_NAME String => 外键表名称 FKCOLUMN_NAME String => 外键列名称
				 * KEY_SEQ short => 外键中的序列号（值 1 表示外键中的第一列，值 2 表示外键中的第二列）
				 * UPDATE_RULE short => 更新主键时外键发生的变化 DELETE_RULE short =>
				 * 删除主键时外键发生的变化 PK_NAME String => 主键的名称（可为 null） FK_NAME String
				 * => 外键的名称（可为 null） DEFERRABILITY short => 是否可以将对外键约束的评估延迟到提交时间
				 * 
				 * 
				 */
				ResultSet foreignKeyResultSet = metaData.getImportedKeys(null, null, mmv.getTableName());
				while (foreignKeyResultSet.next()) {
					String fkTableName = foreignKeyResultSet.getString("FKTABLE_NAME");
					String fkColumnName = foreignKeyResultSet.getString("FKCOLUMN_NAME");
					String pkTablenName = foreignKeyResultSet.getString("PKTABLE_NAME");
					String pkColumnName = foreignKeyResultSet.getString("PKCOLUMN_NAME");
					JSONObject o = new JSONObject();
					o.put("FKTABLE_NAME", fkTableName);
					o.put("FKCOLUMN_NAM", fkColumnName);
					o.put("PKTABLE_NAME", pkTablenName);
					o.put("PKCOLUMN_NAME", pkColumnName);
					fkMap.put(pkColumnName, o);
				}
				modelObject.put("fks", JSON.toJSONString(fkMap));

				tmp.put(mmv.getTableName(), modelObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 不带关联关系的导入
		for (Iterator<String> it = tmp.keySet().iterator(); it.hasNext();) {
			// 先判断是否有已经存在的table对应的元数据或者name为tableName的一样
			String tableName = it.next();
			JSONObject o = tmp.get(tableName);
			// 通过外键和index判断是 一对多还是 一对一关系
			// 通过外键和外键关联的表判断外键是否需要加入进来
			MetaModelVO metaModelVO = this.metaModelServiceImpl.queryByModelCode(tableName);

			JSONObject items = JSON.parseObject(o.getString("items"));

			for (Iterator<String> it2 = items.keySet().iterator(); it2.hasNext();) {
				String columName = it2.next();
				MetaModelItemsVO itemsVO = items.getObject(columName, MetaModelItemsVO.class);
				itemsVO.setModelId(metaModelVO.getId());
				metaModelItemsServiceImpl.save(itemsVO);

			}
		}

		rtn.put("result", "1");

		return "redirect:queryResult.do";
	}

	/**
	 * 复制
	 * 
	 * @param _selects
	 * @return
	 */
	@RequestMapping(value = "/copy")
	public ModelAndView copy(@RequestParam(value = "id") String[] _selects) {
		for (int i = 0; i < _selects.length; i++) {
			String id = _selects[i];
			MetaModelVO vo = metaModelServiceImpl.get(Long.valueOf(id));
			if (vo == null)
				continue;
			vo.setId(null);
			vo.setModelName(vo.getModelName() + " - copy");
			Long newId = metaModelServiceImpl.save(vo);

			BaseExample baseExample = new BaseExample();
			Criteria criteria = baseExample.createCriteria();
			criteria.andEqualTo("modelId", id);

			List<MetaModelItemsVO> items = metaModelItemsServiceImpl.selectPagedByExample(baseExample, 1, 900, null);
			if (items == null)
				continue;
			for (int k = 0; k < items.size(); k++) {
				MetaModelItemsVO it = items.get(k);
				it.setId(null);
				it.setModelId(newId);
				metaModelItemsServiceImpl.save(it);
			}
		}
		return new ModelAndView("redirect:queryResult.do");
	}

	/**
	 * 复制到指定系统
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copyToSys")
	public String copyToSys(String[] _selects, String systemId) {
		ModelAndView mv = new ModelAndView("redirect:queryResult.do");
		JSONObject o = new JSONObject();
		if (StringUtils.isNotBlank(systemId)) {
			for (int i = 0; i < _selects.length; i++) {
				String id = _selects[i];
				MetaModelVO vo = metaModelServiceImpl.get(Long.valueOf(id));
				if (vo == null)
					continue;
				vo.setId(null);
				vo.setModelName(vo.getModelName() + " - copy");
				vo.setSystemId(Long.valueOf(systemId));
				Long newId = metaModelServiceImpl.save(vo);

				BaseExample baseExample = new BaseExample();
				Criteria criteria = baseExample.createCriteria();
				criteria.andEqualTo("modelId", id);

				List<MetaModelItemsVO> items = metaModelItemsServiceImpl.selectPagedByExample(baseExample, 1, 900,
						null);
				if (items == null)
					continue;
				for (int k = 0; k < items.size(); k++) {
					MetaModelItemsVO it = items.get(k);
					it.setId(null);
					it.setModelId(newId);
					metaModelItemsServiceImpl.save(it);
				}
			}
			o.put("rtn", 0);
			o.put("msg", "复制成功");
		} else {
			o.put("rtn", 1);
			o.put("msg", "请选择系统");
		}
		return o.toJSONString();
	}

	/**
	 * 增加
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/save")
	public String save(@ModelAttribute MetaModelVO vo, Model model) {
		// 判读元数据是否存在 存在不增加 不存在则增加
		String str = "";
		try {
			List<MetaModelVO> mmo = this.metaModelServiceImpl.queryMetaModel("queryMetaModel", vo.getModelCode(),
					vo.getTableName(), vo.getProjectName());
			if (!(mmo.size() > 0)) {
				Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
				if (obj instanceof PunSystemVO) {
					PunSystemVO system = (PunSystemVO) obj;
					vo.setSystemId(system.getSysId());
				}
				vo.setModelSynchronization(false);
				vo.setModelValid(false);
				Long id = this.metaModelServiceImpl.save(vo);
				str = "{id:" + id + "}";
			} else {
				// 存在返回0
				Long id = 0L;
				str = "{id:" + id + "}";
				logger.debug("该元数据已经存在");
			}
		} catch (Exception e) {
			Long id = 0L;
			str = "{id:" + id + "}";
			e.printStackTrace();
			return null;
		}
		model.addAttribute("model", vo);
		return "redirect:queryResult.do";
	}

	/**
	 * 增加
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saves")
	public Map<String, Object> saves(@ModelAttribute MetaModelVO vo, Model model) {
		// 判读元数据是否存在 存在不增加 不存在则增加
		Map<String, Object> map = new HashMap<String, Object>();
		String str = "";
		try {
			List<MetaModelVO> mmo = this.metaModelServiceImpl.queryMetaModel("queryMetaModel", vo.getModelCode(),
					vo.getTableName(), vo.getProjectName());
			if (!(mmo.size() > 0)) {
				Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
				if (obj instanceof PunSystemVO) {
					PunSystemVO system = (PunSystemVO) obj;
					vo.setSystemId(system.getSysId());
				}
				vo.setModelSynchronization(false);
				vo.setModelValid(false);
				Long id = this.metaModelServiceImpl.save(vo);
				map.put("id", id);
			} else {
				logger.debug("该元数据已经存在");
				map.put("id", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("id", 0);
			return map;
		}
		model.addAttribute("model", vo);
		return map;
	}

	/**
	 * ljw 2015-1-12 删除元数据
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/removeModel")
	public String removeModel(Long[] id, Model model) {
		try {
			// 查询对应的关系
			for (int i = 0; i < id.length; i++) {
				List<ModelRelationVO> mro = this.modelRelationServiceImpl.queryByModelId(id[i]);
				if (!(mro.size() > 0)) {
					// 查询元数据
					MetaModelVO mmo = this.metaModelServiceImpl.get(id[i]);
					this.metaModelItemsServiceImpl.removeByFk(id[i]);
					this.metaModelServiceImpl.remove(mmo);
					// 删除关系表中的关系
					List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult", id[i]);
					for (MetaModelItemsVO mmi : ls) {
						ModelRelationVO vo = this.modelRelationServiceImpl.queryByItem(mmi.getId());
						if (vo != null) {
							this.modelRelationServiceImpl.remove(vo);
						}
					}
				} else {
					logger.debug("对不起，该模型有外键关系，不能删除");
				}

			}
			return "redirect:queryResult.do";
		} catch (Exception e) {
			e.printStackTrace();
			int i = 1;
			String str = "{id:" + i + "}";
			return str;
		}
	}

	/**
	 * 删除元数据并删除数据库表
	 * 
	 * @param classId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/remove")
	public String remove(Long id, Model model) {
		try {
			// 查询对应的关系
			List<ModelRelationVO> mro = this.modelRelationServiceImpl.queryByModelId(id);
			if (!(mro.size() > 0)) {
				// 查询元数据
				MetaModelVO mmo = this.metaModelServiceImpl.get(id);
				this.metaModelItemsServiceImpl.removeByFk(id);
				this.metaModelServiceImpl.remove(mmo);
				String sql = "drop table " + mmo.getTableName();
				this.metaModelServiceImpl.excuteSql(sql);
				// 删除关系表中的关系
				List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult", id);
				for (MetaModelItemsVO mmi : ls) {
					ModelRelationVO vo = this.modelRelationServiceImpl.queryByItem(mmi.getId());
					if (vo != null) {
						this.modelRelationServiceImpl.remove(vo);
					}
				}
			} else {
				logger.debug("对不起，该模型有外键关系，不能删除");
			}
			return "redirect:queryResult.do";
		} catch (Exception e) {
			e.printStackTrace();
			int i = 1;
			String str = "{id:" + i + "}";
			return str;
		}
	}

	/**
	 * 创建数据库表
	 * 
	 * @param idss
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/createTable")
	public String createTable(@RequestParam(value = "id") long idss, Model model) {
		MetaModelVO vo = this.metaModelServiceImpl.get(idss);
		// 判断表是否存在
		if (metaModelServiceImpl.tableIsExist(vo.getTableName())) {
			long id = vo.getId();
			List<MetaModelItemsVO> mmo = this.metaModelItemsServiceImpl.queryByState("queryByState", idss);
			// 去查找数据库是否有这一列
			for (MetaModelItemsVO mm : mmo) {
				if (!metaModelItemsServiceImpl.columnIsExist("columnIsExist", vo.getTableName(), mm.getItemName())) {
					// 需要判断是否有外键关系
					if (mm.getItemType().equals("多对一") || mm.getItemType().equals("一对一")) {
						StringBuffer sjs = new StringBuffer("alter table ").append(vo.getTableName())
								.append(" add constraint fk_").append(mm.getItemCode()).append("_")
								.append(vo.getTableName()).append(" foreign key(").append(mm.getItemCode())
								.append(") references ");
						// String sss="alter table "+vo.getTableName()+" add
						// constraint fk_"+mm.getItemCode()+" foreign
						// key("+mm.getItemCode()+") references ";
						String type = null;
						// 查询所对应的关系
						ModelRelationVO mr = this.modelRelationServiceImpl.queryByItem(mm.getId());
						// 查询所对应的表
						MetaModelVO mmmro = this.metaModelServiceImpl.get(mr.getModelId());
						sjs.append(mmmro.getTableName());
						// sss+=mmmro.getTableName();
						// 查询该表对应的主键列
						List<MetaModelItemsVO> list = this.metaModelItemsServiceImpl.queryResult("queryResult",
								mr.getModelId());
						for (MetaModelItemsVO mmi : list) {
							if (mmi.getUsePrimaryKey() == 1) {
								sjs.append("(");
								sjs.append(mmi.getItemCode());
								sjs.append(")");
								// sss+="("+mmi.getItemCode()+")";
								type = mmi.getItemType();
								continue;
							}
						}
						// 添加列
						StringBuffer str = new StringBuffer("alter table ").append(vo.getTableName())
								.append(" add column ").append(mm.getItemCode()).append(" ").append(type).append(";");
						// String strss="alter table "+vo.getTableName()+" add
						// column "+mm.getItemCode()+" "+type+";";
						sjs.append(";");
						// sss+=";";
						this.metaModelServiceImpl.excuteSql(str.toString());
						// 修改状态
						mm.setItemValid(1);
						this.metaModelItemsServiceImpl.update("updateSelective", mm);
						this.metaModelServiceImpl.excuteSql(sjs.toString());
					} else {
						String str = UpdateColumn.newAddColumn(mm, vo.getTableName());
						// 修改状态
						logger.debug(str);
						mm.setItemValid(1);
						this.metaModelItemsServiceImpl.update("updateSelective", mm);
						metaModelServiceImpl.excuteSql(str);
						model.addAttribute("msg", 1);
					}
				}
			}
			vo.setModelSynchronization(true);
			this.metaModelServiceImpl.update("update", vo);
			return "redirect:../metaModel/queryResult.do?currentPage=" + 1;
		} else {
			try {
				List<MetaModelItemsVO> pl = this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getId());
				String str = createTables.getSql(vo, pl);
				logger.debug(str);
				boolean b = metaModelServiceImpl.excuteSql(str);
				// 执行完之后，改变其状态为1
				for (MetaModelItemsVO mmm : pl) {
					mmm.setItemValid(1);
					this.metaModelItemsServiceImpl.update("updateSelective", mmm);
				}
				model.addAttribute("msg", 1);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("msg", 0);
			}

			vo.setModelSynchronization(true);
			this.metaModelServiceImpl.update("update", vo);
			return "redirect:../metaModel/queryResult.do?currentPage=" + 1;
		}
	}

	/**
	 * /点击发布
	 * 
	 * @param vo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/release")
	public String release(@RequestParam(value = "id") long idss, Model model,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		MetaModelVO vo = this.metaModelServiceImpl.get(idss);
		vo.setModelSynchronization(true);
		metaModelServiceImpl.update("update", vo);
		model.addAttribute("currentPage", currentPage);
		return "redirect:queryResult.do";
	}

	/**
	 * 页面跳转 用于所需的页面跳转 点击
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("toggle")
	public String toggle(Model model) {
		try {
			List<PunSystemVO> list = this.sysService.findAll();
			model.addAttribute("project", list);
			Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			PunSystemVO system = null;
			if (obj instanceof PunSystemVO) {
				system = (PunSystemVO) obj;
				List<MetaModelClassVO> classVos = metaModelClassServiceImpl.queryByProjectId(system.getSysId());
				model.addAttribute("classVos", classVos);
			}
			List<SysDataSourceVO> sysDataRela = sysService.getSystemDataSource(system.getSysId());
			List<DataSourceManageVO> dataS = new ArrayList<DataSourceManageVO>();
			if (sysDataRela != null && sysDataRela.size() > 0) {
				for (SysDataSourceVO dataVo : sysDataRela) {
					Long sourceId = dataVo.getDataSourceId();
					DataSourceManageVO datasource = dataSourceService.get(sourceId);
					dataS.add(datasource);

				}
				model.addAttribute("dataSources", dataS);
			}

			// 默认数据源
			BaseExample base = new BaseExample();
			base.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andEqualTo("ISDEFAULT", true);
			PageList<SysDataSourceVO> dataVos = sysSourceRelationService.selectPagedByExample(base, 1,
					Integer.MAX_VALUE, null);
			if (dataVos != null && dataVos.size() > 0) {
				model.addAttribute("defaultDataSourceId", dataVos.get(0).getDataSourceId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "metadesigner/metaModel/metaModel_add";
	}

	@ResponseBody
	@RequestMapping("getDataSourceManageVOById")
	public DataSourceManageVO getDataSourceManageVOById(Long sourceId) {
		DataSourceManageVO datasource = dataSourceService.get(sourceId);
		return datasource;
	}

	/**
	 * 查询所有
	 * 
	 * @param model
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findAll")
	public String findAll(Model model) throws Exception {
		try {
			List<MetaModelClassVO> list = this.metaModelClassServiceImpl.findAll(23);
			model.addAttribute("class", list);
			List<MetaModelVO> result = this.metaModelServiceImpl.findAll();
			logger.debug(result.size() + "");
			model.addAttribute("list", result);
			return "metadesigner/metaModel/findAll";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询一条数据 用于修改按钮
	 * 
	 * @param id
	 * @param pw
	 * @return
	 */
	@RequestMapping(value = "/get")
	public String get(long id, Model model) {
		MetaModelVO mmo = new MetaModelVO();
		try {
			mmo = this.metaModelServiceImpl.get(id);
			model.addAttribute("vo", mmo);
			List<PunSystemVO> psv = this.sysService.findAll();
			model.addAttribute("project", psv);
			Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			PunSystemVO system = null;
			if (obj instanceof PunSystemVO) {
				system = (PunSystemVO) obj;
				List<MetaModelClassVO> classVos = metaModelClassServiceImpl.queryByProjectId(system.getSysId());
				model.addAttribute("classVos", classVos);
			}
			List<SysDataSourceVO> sysDataRela = sysService.getSystemDataSource(system.getSysId());
			List<DataSourceManageVO> dataS = new ArrayList<DataSourceManageVO>();
			if (sysDataRela != null && sysDataRela.size() > 0) {
				for (SysDataSourceVO dataVo : sysDataRela) {
					Long sourceId = dataVo.getDataSourceId();
					DataSourceManageVO datasource = dataSourceService.get(sourceId);
					dataS.add(datasource);

				}
				model.addAttribute("dataSources", dataS);
			}

			// 默认数据源
			BaseExample base = new BaseExample();
			base.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andEqualTo("ISDEFAULT", true);
			PageList<SysDataSourceVO> dataVos = sysSourceRelationService.selectPagedByExample(base, 1,
					Integer.MAX_VALUE, null);
			if (dataVos != null && dataVos.size() > 0) {
				model.addAttribute("defaultDataSourceId", dataVos.get(0).getDataSourceId());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "metadesigner/metaModel/metaModel_edit";
	}

	/**
	 * 查询一条数据 用于修改按钮
	 * 
	 * @param id
	 * @param pw
	 * @return
	 */
	@RequestMapping(value = "/getDataSource")
	public String get(String _selects, Model model) {
		MetaModelVO mmo = new MetaModelVO();
		try {
			Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			PunSystemVO system = null;
			if (obj instanceof PunSystemVO) {
				system = (PunSystemVO) obj;
			}
			List<SysDataSourceVO> sysDataRela = sysService.getSystemDataSource(system.getSysId());
			List<DataSourceManageVO> dataS = new ArrayList<DataSourceManageVO>();
			if (sysDataRela != null && sysDataRela.size() > 0) {
				for (SysDataSourceVO dataVo : sysDataRela) {
					Long sourceId = dataVo.getDataSourceId();
					DataSourceManageVO datasource = dataSourceService.get(sourceId);
					dataS.add(datasource);

				}
				model.addAttribute("dataSources", dataS);
			}

			// 默认数据源
			BaseExample base = new BaseExample();
			base.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andEqualTo("ISDEFAULT", true);
			PageList<SysDataSourceVO> dataVos = sysSourceRelationService.selectPagedByExample(base, 1,
					Integer.MAX_VALUE, null);
			if (dataVos != null && dataVos.size() > 0) {
				model.addAttribute("defaultDataSourceId", dataVos.get(0).getDataSourceId());
			}
			model.addAttribute("_selects", _selects);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "metadesigner/metaModel/batchModifyDS";
	}

	/**
	 * 分页查询
	 * 
	 * @param vo
	 * @param currentPage
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/queryResult")
	public String queryResult(@ModelAttribute MetaModelVO vo,
			@RequestParam(required = false, defaultValue = "1") int currentPage, Model model) {
		try {
			int pageSize = 20;
			String queryStr = "queryList";
			Map<String, Object> map = new HashMap<String, Object>();
			if (currentPage == 0) {
				currentPage = 1;
			}
			String sortString = "id.desc";
			Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
			if (obj instanceof PunSystemVO) {
				PunSystemVO system = (PunSystemVO) obj;
				vo.setSystemId(system.getSysId());
				map.put("systemId", vo.getSystemId());
			}

			map.put("modelClassId", vo.getModelClassId());
			map.put("modelName", vo.getModelName());
			map.put("modelCode", vo.getModelCode());
			map.put("tableName", vo.getTableName());
			map.put("projectName", vo.getProjectName());
			PageList<MetaModelVO> pl = this.metaModelServiceImpl.queryResult(queryStr, map, currentPage, pageSize,
					sortString);
			model.addAttribute("list", pl);
			model.addAttribute("currentPage", currentPage);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "metadesigner/metaModel/findAll";
	}

	/**
	 * 修改
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/update")
	public String update(@ModelAttribute MetaModelVO vo) {
		vo.setModelSynchronization(false);
		vo.setModelValid(false);
		// 查看表名是否修改
		MetaModelVO vvo = this.metaModelServiceImpl.get(vo.getId());
		// 判断表名是否修改
		if (!vo.getTableName().equals(vvo.getTableName())) {
			// 查看这张表是否存在
			if (this.metaModelServiceImpl.tableIsExist(vvo.getTableName())) {
				// 修改表名
				StringBuffer sb = new StringBuffer("ALTER TABLE ").append(vvo.getTableName()).append(" RENAME TO ")
						.append(vo.getTableName());
				// String sql="ALTER TABLE "+vvo.getTableName()+" RENAME TO
				// "+vo.getTableName();
				this.metaModelServiceImpl.excuteSql(sb.toString());
				// 查找列
				List<MetaModelItemsVO> mm = this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getId());
				for (MetaModelItemsVO m : mm) {
					if (m.getItemType().equals("一对一") || m.getItemType().equals("多对一")) {
						// 修改外键名
						StringBuffer ss = new StringBuffer("alter table ").append(vo.getTableName())
								.append(" drop foreign key fk_").append(m.getItemCode()).append("_")
								.append(vvo.getTableName()).append(";");
						ss.append("alter table ");
						ss.append(vo.getTableName());
						ss.append(" add constraint fk_");
						ss.append(m.getItemCode());
						ss.append("_");
						ss.append(vo.getTableName());
						ss.append(" foreign key(");
						ss.append(m.getItemCode());
						ss.append(") references ");
						// 查询关联表
						ModelRelationVO mro = this.modelRelationServiceImpl.queryByItem(m.getId());
						MetaModelVO mmvo = this.metaModelServiceImpl.get(mro.getModelId());
						ss.append(mmvo.getTableName());
						ss.append("(");
						List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult",
								mmvo.getId());
						for (MetaModelItemsVO l : ls) {
							if (l.getUsePrimaryKey() == 1) {
								ss.append(l.getItemCode());
								ss.append(");");
								continue;
							}
						}
						this.metaModelServiceImpl.excuteSql(ss.toString());
					}
				}
			}
		}
		this.metaModelServiceImpl.update("update", vo);
		return "redirect:queryResult.do";
	}

	/**
	 * 批量修改 元数据配置的数据源
	 * 
	 * @param _selects
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batchModifyDS")
	public String batchModifyDS(@RequestParam(value = "_selects") String _selects, Long dataSourceId) {
		String[] ids = _selects.split(",");
		for (int i = 0; i < ids.length; i++) {

			MetaModelVO vo = new MetaModelVO();
			vo.setId(Long.parseLong(ids[i]));
			vo.setDataSourceId(dataSourceId);
			this.metaModelServiceImpl.update("updateDataSourceById", vo);
		}
		return "1";
	}

	/**
	 * 根据modelCode,modelName 模糊查询
	 * 
	 * @param modelCode
	 * @param modelName
	 * @param page
	 * @param rows
	 * @return
	 * @throws SQLException
	 */
	@ResponseBody
	@RequestMapping("/queryPageByModel")
	public List<Map<String, Object>> queryPageByModel(String modelCode, String modelName,
			@RequestParam(required = false, defaultValue = "1") int page) throws SQLException {
		StringBuffer b = new StringBuffer(
				"select modelClassId,modelCode,modelName,modelDesc,tableName,projectName,modelType,modelSynchronization,modelValid,id ,system_id from fw_mm_metaModel where 1=1");
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
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			b.append(" and SYSTEM_ID=").append(system.getSysId());
		}
		int rows = 10;
		List<Map<String, Object>> list = this.jdbcRepository.find(b.toString(), page, rows, objs);
		return list;

	}

	@RequestMapping(value = "/selectPage")
	public String selectPageByExample(@ModelAttribute MetaModelVO vo, Model model,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		BaseExample be = new BaseExample();
		String modelName = vo.getModelName();
		Criteria c = be.createCriteria();
		if (currentPage == 0) {
			currentPage = 1;
		}
		if (modelName != null && !model.equals("")) {
			c.andLike("modelName", "%" + modelName + "%");
		}
		String modelCode = vo.getModelCode();
		if (modelCode != null && !modelCode.equals("")) {
			c.andLike("modelCode", "%" + modelCode + "%");
		}
		String tableName = vo.getTableName();
		if (tableName != null && !tableName.equals("")) {
			c.andLike("tableName", "%" + tableName + "%");
		}
		String projectName = vo.getProjectName();
		if (projectName != null && !projectName.equals("")) {
			c.andLike("projectName", "%" + projectName + "%");
		}
		Object obj = Tools.getObjectFromSession(SessionContants.TARGET_SYSTEM);
		if (obj instanceof PunSystemVO) {
			PunSystemVO system = (PunSystemVO) obj;
			c.andEqualTo("SYSTEM_ID", system.getSysId());
		}
		PageList<MetaModelVO> pl = this.metaModelServiceImpl.selectPagedByExample(be, currentPage, 25, "id desc");
		model.addAttribute("list", pl);
		model.addAttribute("currentPage", currentPage);
		return "metadesigner/metaModel/findAll";

	}

	@ResponseBody
	@RequestMapping(value = "queryModelItemByModel")
	public List<MetaModelItemsVO> queryModelItemByModel(String modelCode) {
		try {
			MetaModelVO vo = this.metaModelServiceImpl.queryByModelCode(modelCode);
			List<MetaModelItemsVO> ls = this.metaModelItemsServiceImpl.queryResult("queryResult", vo.getId());
			return ls;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * 
	 * 返回界面
	 * 
	 */
	@RequestMapping(value = "getGenerator")
	public ModelAndView getGenerator() {
		// 如果希望传递进来的数据同样也要传回去，则使用@ModelAttribute
		ModelAndView mv = new ModelAndView();
		mv.setViewName("metadesigner/generator/GeneratorUI");
		return mv;
	}

	/*
	 * 调用代码生成器
	 * 
	 */
	@RequestMapping(value = "/generator", method = RequestMethod.POST)
	public ModelAndView generatorMain(String basePackage, String persistence, String outRoot, String templementSrc,
			String tableName) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("metadesigner/generator/GeneratorUI");
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("basePackage", basePackage);
			map.put("persistence", persistence);
			map.put("outRoot", outRoot);
			map.put("templementSrc", templementSrc);
			map.put("tableName", tableName);
			// GeneratorMain.StartGenerator(map);
			mv.addObject("result", "生成成功");
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("result", "生成失败");
		}
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/queryModel")
	public MetaModelVO queryModel(String modelCode) {
		MetaModelVO m = this.metaModelServiceImpl.queryByModelCode(modelCode);
		return m;
	}

	// --------------------------数据校验----------------------------
	/**
	 * 数据校验
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "dataValidate")
	public String queryMetaModel(@RequestParam(required = false, defaultValue = "") String modelCode,
			@RequestParam(required = false, defaultValue = "") String tableName,
			@RequestParam(required = false, defaultValue = "") String projectName,
			@RequestParam(required = false, defaultValue = "0") long id) {
		logger.debug(id + "");
		if (id == 0) {
			List<MetaModelVO> ls = this.metaModelServiceImpl.queryMetaModel("queryMetaModel", modelCode, tableName,
					projectName);
			int count = ls.size();
			String str = "{id:" + count + "}";
			return str;
		} else {
			MetaModelVO mm = this.metaModelServiceImpl.get(id);
			List<MetaModelVO> ls = this.metaModelServiceImpl.queryMetaModel("queryMetaModel", modelCode, tableName,
					projectName);

			for (int i = 0; i < ls.size(); i++) {
				MetaModelVO mmv = ls.get(i);
				if (mmv.getModelCode().equals(mm.getModelCode()) || mmv.getTableName().equals(mm.getTableName())
						|| mmv.getProjectName().equals(mm.getProjectName())) {
					ls.remove(i);
				}
			}
			int count = ls.size();
			String str = "{id:" + count + "}";
			return str;
		}

	}

	/**
	 * 将汉字转化为拼音
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	// @ResponseBody
	// @RequestMapping(value="/pinyin")
	// public Map<String,String> pinYin(String str){
	// String s=PinYin4jUtilImpl.getPinYin(str);
	// Map<String,String> map=new HashMap<String, String>();
	// map.put("str", s);
	// return map;
	// }

}
