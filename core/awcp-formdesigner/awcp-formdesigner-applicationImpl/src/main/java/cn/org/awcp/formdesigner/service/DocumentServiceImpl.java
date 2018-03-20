package cn.org.awcp.formdesigner.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.QueryChannelService;
import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.core.utils.ContextContentUtils;
import cn.org.awcp.core.utils.DateUtils;
import cn.org.awcp.core.utils.MySqlSmartCountUtil;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.core.constants.FormDesignerGlobal;
import cn.org.awcp.formdesigner.core.domain.Document;
import cn.org.awcp.formdesigner.core.domain.DynamicPage;
import cn.org.awcp.formdesigner.core.domain.design.context.data.DataDefine;
import cn.org.awcp.formdesigner.core.parse.bean.PageDataBeanWorker;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.metadesigner.application.MetaModelService;
import cn.org.awcp.metadesigner.application.SysSourceRelationService;
import cn.org.awcp.metadesigner.vo.MetaModelVO;
import cn.org.awcp.metadesigner.vo.SysDataSourceVO;
import cn.org.awcp.unit.vo.PunSystemVO;

@Service
public class DocumentServiceImpl implements DocumentService {
	private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Autowired
	@Qualifier("queryChannel")
	private QueryChannelService queryChannel;

	@Autowired
	private MetaModelOperateService metaModelOperateServiceImpl;

	@Autowired
	@Qualifier("sysSourceRelationServiceImpl")
	SysSourceRelationService sysSourceRelationService;

	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	@Autowired
	private MetaModelService metaModelServiceImpl;

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public String save(DocumentVO vo) {
		// 仅保存文档数据
		Document doc = BeanUtils.getNewInstance(vo, Document.class);
		String id = doc.save().getId();
		vo.setId(id);
		return id;
	}

	/**
	 * 删除文档
	 */
	public Boolean delete(DocumentVO vo) {
		Document doc = BeanUtils.getNewInstance(vo, Document.class);
		if (doc == null) {
			return false;
		}
		doc.remove();
		return true;

	}

	@Override
	public List<DocumentVO> findPageByExample(BaseExample baseExample, int currentPage, int pageSize,
			String sortString) {
		PageList<Document> list = queryChannel.selectPagedByExample(Document.class, baseExample, currentPage, pageSize,
				sortString);
		PageList<DocumentVO> ret = new PageList<DocumentVO>(list.getPaginator());
		if (list != null && list.size() > 0) {
			for (Document vo : list) {
				ret.add(BeanUtils.getNewInstance(vo, DocumentVO.class));
			}
		}
		return ret;
	}

	@Override
	public DocumentVO findById(String id) {
		DocumentVO vo = BeanUtils.getNewInstance(Document.get(Document.class, id), DocumentVO.class);
		return vo;
	}

	@Override
	public DocumentVO findDocByWorkItemId(String flowTemplateId, String workItemId) {
		DocumentVO vo = BeanUtils.getNewInstance(Document.findByWorkItemId(flowTemplateId, workItemId),
				DocumentVO.class);
		return vo;
	}

	@Override
	public Map<String, List<Map<String, String>>> initDocumentData(Integer currentPage, Integer pageSize,
			DocumentVO docVo, ScriptEngine engine, DynamicPageVO pageVo) {
		logger.debug("start initDocumentData ");
		Map<String, List<Map<String, String>>> listParams = null;
		listParams = new HashMap<String, List<Map<String, String>>>();
		Map<String, DataDefine> map = PageDataBeanWorker
				.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(pageVo.getDataJson()));
		List<DataDefine> datas = new ArrayList<DataDefine>(map.values());
		docVo.setListParams(listParams);
		String allowOrderBy = docVo.getAllowOrderBy();
		String orderByList = docVo.getOrderBy();
		logger.debug("start find data in datajson");
		for (DataDefine dd : datas) {
			if (!StringUtils.isNumeric(allowOrderBy) || Integer.parseInt(allowOrderBy) != 1) {
				orderByList = "";
			}
			PageList<Map<String, String>> pageList = getDataListByDataDefine(dd, engine, currentPage, pageSize,
					orderByList);
			if (pageList != null) {
				listParams.put(dd.getName() + "_list", pageList);
			}
		}
		logger.debug("end find data in datajson");
		logger.debug("end initDocumentData with no cache");
		return listParams;
	}

	@Override
	public Map<String, List<Map<String, String>>> initDocumentDataFlow(Integer currentPage, Integer pageSize,
			DocumentVO docVo, ScriptEngine engine, DynamicPageVO pageVo) {
		logger.debug("start initDocumentData ");
		Map<String, List<Map<String, String>>> listParams = null;
		listParams = new HashMap<String, List<Map<String, String>>>();
		Map<String, DataDefine> map = PageDataBeanWorker
				.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(pageVo.getDataJson()));
		List<DataDefine> datas = new ArrayList<DataDefine>(map.values());
		docVo.setListParams(listParams);
		String allowOrderBy = docVo.getAllowOrderBy();
		String orderByList = docVo.getOrderBy();// ebaseinfo.id,desc;ebaseinfo.name,desc;
		logger.debug("start find data in datajson");
		for (DataDefine dd : datas) {
			StringBuilder orderBy = new StringBuilder();
			String alias = dd.getName();
			String keyAlias = alias + ".";
			if (StringUtils.isNotBlank(allowOrderBy) && allowOrderBy.equalsIgnoreCase("1")) {
				logger.debug("allow orderby");
				int begin = orderByList.indexOf(keyAlias);
				while (begin != -1) {
					orderBy.append(", ");
					int end = orderByList.indexOf(";", begin);
					if (end != -1) {
						String itemCode = orderByList.substring(begin + keyAlias.length(), end);
						logger.debug("itemcode is {}", itemCode);
						if (itemCode.indexOf(",") != -1) {
							orderBy.append(itemCode.replaceAll(",", " "));
						} else {
							orderBy.append(itemCode).append(" DESC");
						}
						begin = orderByList.indexOf(keyAlias, end);
					} else {
						begin = orderByList.indexOf(keyAlias, begin);
					}
				}
				if (orderBy.length() == 1) {
					orderBy.deleteCharAt(1);
				}
			} else {
				logger.debug("deny orderby");
			}
			PageList<Map<String, String>> pageList = getDataListByDataDefine(dd, engine, currentPage, pageSize,
					orderBy.toString());
			if (pageList != null) {
				listParams.put(dd.getName() + "_list", pageList);
			}
		}
		logger.debug("end find data in datajson");
		logger.debug("end initDocumentData with no cache");
		return listParams;
	}

	/**
	 * 根据数据源和脚本engine获取数据列表
	 * 
	 * @param dd
	 *            数据源
	 * @param engine
	 *            脚本engine
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页条数
	 * @return
	 */
	public PageList<Map<String, String>> getDataListByDataDefine(DataDefine dd, ScriptEngine engine,
			Integer currentPage, Integer pageSize, String orderBy) {
		logger.debug("start find {} ", dd.getName());
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String script = dd.getSqlScript();
		String sql = null;
		if (StringUtils.isNotBlank(script)) {
			try {
				sql = (String) engine.eval(script);
			} catch (ScriptException e) {
				logger.error("ERROR", e);
			}
		}
		logger.debug("datasource[{}] compute sql is {} ", dd.getName(), sql);
		if (StringUtils.isNotBlank(sql)) {
			// 去掉最后的";"，这样就可以对sql进行外包或者增加limit了
			if (sql.endsWith(";")) {
				sql = sql.substring(0, sql.length() - 1);
			}

			// 执行查询时，首先去掉order by 因为会很慢
			String sql2 = sql.toUpperCase();
			Integer totalCount = 1;// 默认为查询单条数据
			int index = sql2.lastIndexOf("ORDER BY");
			if (index != -1) {
				// 如果没有有排序条件，则将排序条件置为sql语句中的排序条件，也有可能sql语句中也没有排序条件，那么orderBy为空
				if (StringUtils.isBlank(orderBy)) {
					orderBy = sql.substring(index + 8);
					logger.debug("datasource[{}] : sql has order by \"{}\" ", dd.getName(), orderBy);
				} else {
					logger.debug("datasource[{}] : sql use param \"{}\" ", dd.getName(), orderBy);
				}
				sql = sql.substring(0, index);
			}
			HttpServletRequest request = ContextContentUtils.getRequest();
			// 增加
			sql = addSQLParam(sql, request);
			Map<String, Object> param = wrapMap(request.getParameterMap());
			Paginator paginator = null;
			if (dd.getIsSingle() == 0) {
				logger.debug("datasource[{}] : sql is single ", dd.getName());
				// 如果是单行数据，则无需查询数据条数
				paginator = new Paginator(currentPage, 1, totalCount);
			} else {
				//
				// 如果是多行数据，需分页，则需查询数据条数
				if (dd.getIsPage() > 0) {
					logger.debug("datasource[{}] : sql is mutiple and should be paged ", dd.getName());
					// 修改成具名参数
					// totalCount
					// =jdbcTemplate.queryForInt(countSql.toString());
					String countSql = MySqlSmartCountUtil.getCountSql(sql, false);
					totalCount = namedParameterJdbcTemplate.queryForObject(countSql, param, Integer.class);
					logger.debug("datasource[{}] : countSql is {} data count {}", dd.getName(), countSql, totalCount);
					paginator = new Paginator(currentPage, pageSize, totalCount);
				} else {
					if (dd.getLimitCount() <= 0) {
						// 多行数据，且不分页,且无固定记录数
						if (pageSize > 0) {
							dd.setLimitCount(pageSize);
						} else {
							dd.setLimitCount(10);
						}
					}
					logger.debug("datasource[{}] : sql data is mutiple and has no pager the total count is {}",
							dd.getName(), dd.getLimitCount());
					paginator = new Paginator(1, dd.getLimitCount(), dd.getLimitCount());
				}
			}
			if (StringUtils.isNotBlank(orderBy)) {
				sql += " ORDER BY " + orderBy;
			}
			sql += " limit " + paginator.getOffset() + "," + paginator.getLimit();
			logger.debug("datasource[{}] : actualSql is {} ", dd.getName(), sql);
			// 修改成具名参数
			// List<Map<String, Object>> retList =
			// jdbcTemplate.queryForList(sql, new Object[] {});
			List<Map<String, Object>> retList = namedParameterJdbcTemplate.queryForList(sql, param);
			logger.debug("datasource[{}] : start convert data. ", dd.getName());
			for (Map<String, Object> retMap : retList) {
				Map<String, String> temp = new HashMap<String, String>();
				for (Map.Entry<String, Object> entrySet : retMap.entrySet()) {
					String key = entrySet.getKey();
					Object obj = entrySet.getValue();
					String value = null;
					if (obj != null) {
						value = String.valueOf(obj);
					}
					temp.put(key, value);
				}
				list.add(temp);
			}
			PageList<Map<String, String>> pageList = new PageList<Map<String, String>>(list, paginator);
			logger.debug("datasource[{}] : end convert data. ", dd.getName());
			return pageList;
		}
		logger.debug("datasource[{}] : sql is blank. ", dd.getName());
		return null;
	}

	private Map<String, Object> wrapMap(Map<String, String[]> map, String... filters) {
		Map<String, Object> param = new HashMap<String, Object>();
		for (Entry<String, String[]> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> filtersList = new ArrayList<String>();
			Collections.addAll(filtersList, "currentPage", "pageSize", "offset", "limit", "privilegesID");
			if (filters != null) {
				Collections.addAll(filtersList, filters);
			}
			if (!filtersList.contains(key)) {
				// 添加参数，取第一个value值
				param.put(entry.getKey(), StringUtils.join(entry.getValue(), ";"));
			}
		}
		return param;
	}

	private String addSQLParam(String sql, HttpServletRequest request) {
		StringBuilder builder = new StringBuilder(sql);
		StringBuilder where = new StringBuilder();
		int index = builder.indexOf("1=1") + 3;
		Enumeration<String> param = request.getParameterNames();
		for (; param.hasMoreElements();) {
			String key = param.nextElement();
			if (key.contains(".") && StringUtils.isNotBlank(request.getParameter(key))) {
				if (key.toLowerCase().contains("name") || key.toLowerCase().contains("title")) {
					where.append(" and " + key + " like concat ('%',:" + key + ",'%') ");
				} else {
					where.append(" and " + key + "=:" + key + " ");
				}
			}
		}
		builder.insert(index, where);
		return builder.toString();
	}

	@Override
	public String getTemplateString(DynamicPageVO pageVO) {
		DynamicPage page = DynamicPage.get(DynamicPage.class, pageVO.getId());
		return page.getTemplateContext();
	}

	@Override
	public String getStoreString(DynamicPageVO pageVO) {
		DynamicPage page = DynamicPage.get(DynamicPage.class, pageVO.getId());
		return page.getStores();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.szcloud.framework.formdesigner.application.service.DocumentService
	 * #processParams (org.szcloud.framework.formdesigner.application.vo.DocumentVO)
	 */
	public DocumentVO processParams(DocumentVO vo) {
		Map<String, String> map = vo.getRequestParams();
		String pageId = (String) map.get("dynamicPageId");
		JSONObject jcon = new JSONObject();
		jcon.put("relatePageId", pageId);
		jcon.put("componentType", "");
		// 需要把tab页包含的页面的组件都找出来
		List<JSONObject> components = formdesignerServiceImpl.getComponentByContainer(jcon);

		Map<String, String> tmpMap = new HashMap<String, String>();
		Map<String, List<Map<String, String>>> listParams = vo.getListParams();
		for (JSONObject c : components) {
			// 有值组件
			if (FormDesignerGlobal.isValueComponent(c)) {
				// ValuedComponent.name 为request里面的key
				String key = c.getString("name");
				// 由于checkbox当不选择的时候，会默认
				if (!map.containsKey(key)) {
					logger.debug("有值组件 {} 类型 : {} 没有提交数据", key, c.getIntValue("componentType"));
					continue;
				}
				String value = map.get(key);
				logger.debug("有值组件 {} 提交数据为：{}", key, value);

				int type = c.getIntValue("componentType");
				if (type == 1032) {
					value = value.replaceAll(",", "");
				}

				String code = c.getString("dataItemCode");// "datadefine.name,modelitemcode"
				logger.debug("有值组件 {} 数据源为 ：{}", key, code);
				String[] codes = code.split("\\.");
				if (codes.length < 2)
					continue;
				String dataCode = codes[0];
				String itemCode = codes[1];

				List<Map<String, String>> list = listParams.get(dataCode);
				Map<String, String> data = null;
				if (list == null) {
					list = new ArrayList<Map<String, String>>();
					data = new HashMap<String, String>();
					data.put(itemCode, value);
					tmpMap.put(code, key);
				} else {
					data = list.get(0);
					if (data == null) {
						data = new HashMap<String, String>();
						data.put(itemCode, value);
						tmpMap.put(code, key);
					} else {
						data.put(itemCode, value);
					}
				}
				List<Map<String, String>> arr = new ArrayList<Map<String, String>>();
				arr.add(data);
				listParams.put(dataCode, arr);
			}

		}
		tmpMap.clear();
		vo.setListParams(listParams);
		return vo;
	}

	/**
	 * 根据元数据code获取到数据源ID，如果数据源没配置，则获取系统默认数据源Id
	 */
	public String getDataSourceIdByModelCode(DataDefine dd) {
		return metaModelOperateServiceImpl.getDataSourceIdByModelCode(dd.getModelCode());
	}

	/**
	 * 根据元数据code获取到数据源ID，如果数据源没配置，则获取系统默认数据源Id
	 */
	public String getDataSourceIdByModelCode(DataDefine dd, Long systemId) {
		MetaModelVO mmv = metaModelServiceImpl.queryByModelCode(dd.getModelCode(), systemId);
		if (mmv != null && mmv.getDataSourceId() != null) { // 如果有配置数据源
			return mmv.getDataSourceId();
		} else {
			BaseExample base = new BaseExample();
			base.createCriteria().andEqualTo("SYSTEM_ID", systemId).andEqualTo("ISDEFAULT", true);
			PageList<SysDataSourceVO> dataVos = sysSourceRelationService.selectPagedByExample(base, 1,
					Integer.MAX_VALUE, null);
			if (dataVos != null && dataVos.size() > 0) {
				return dataVos.get(0).getDataSourceId();
			}
		}
		return null;
	}

	/**
	 * 获取当前系统的默认数据源Id
	 */
	public String getDataSourceIdByCurrentSystem() {
		Object obj = SessionUtils.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
		PunSystemVO system = null;
		if (obj instanceof PunSystemVO) {
			system = (PunSystemVO) obj;
		}
		BaseExample base = new BaseExample();
		base.createCriteria().andEqualTo("SYSTEM_ID", system.getSysId()).andEqualTo("ISDEFAULT", true);
		PageList<SysDataSourceVO> dataVos = sysSourceRelationService.selectPagedByExample(base, 1, Integer.MAX_VALUE,
				null);
		if (dataVos != null && dataVos.size() > 0) {
			return dataVos.get(0).getDataSourceId();
		}
		return null;
	}

	@Override
	public boolean updateModelDataFlow(DynamicPageVO pageVO, DocumentVO vo, String datadefineName) {
		DynamicPage page = BeanUtils.getNewInstance(pageVO, DynamicPage.class);
		DataDefine dd = PageDataBeanWorker.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(page.getDataJson()))
				.get(datadefineName);
		if (dd != null) {
			DataDefine md = (DataDefine) dd;
			if (vo != null && StringUtils.isNotBlank(vo.getId()) && pageVO != null && pageVO.getId() != null) {
			}
			if (metaModelOperateServiceImpl.update(vo.getListParams().get(datadefineName).get(0), md.getModelCode()))
				return true;
		}
		return false;
	}

	@Override
	public boolean updateModelData(DynamicPageVO pageVO, DocumentVO vo, String datadefineName) {
		DynamicPage page = BeanUtils.getNewInstance(pageVO, DynamicPage.class);
		logger.debug("start convert datadefines in {} [{}]", pageVO.getId(), pageVO.getName());
		DataDefine dd = PageDataBeanWorker.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(page.getDataJson()))
				.get(datadefineName);
		logger.debug("end convert datadefines in {} [{}]", pageVO.getId(), pageVO.getName());
		if (dd != null) {
			// get data from doc.requestMap, and put it into map with model
			// processParams(vo);executeAct 方法中已经做处理
			DataDefine md = (DataDefine) dd;
			boolean flag = true;
			if (flag) {
				logger.debug("start update {} data in {} [{}] is be changed", datadefineName, pageVO.getId(),
						pageVO.getName());
				return metaModelOperateServiceImpl.update(vo.getListParams().get(datadefineName).get(0),
						md.getModelCode());
			}
		}
		return false;
	}

	@Override
	public String saveModelData(DynamicPageVO pageVO, DocumentVO vo, String datadefineName) {
		DynamicPage page = BeanUtils.getNewInstance(pageVO, DynamicPage.class);
		DataDefine dd = PageDataBeanWorker.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(page.getDataJson()))
				.get(datadefineName);
		if (dd != null) {
			// get data from doc.requestMap, and put it into map with model
			// processParams(vo);executeAct 方法中已经做处理
			DataDefine md = (DataDefine) dd;
			String id = null;
			Map<String, String> data = vo.getListParams().get(datadefineName).get(0);
			if (data != null && !data.isEmpty()) {
				if (data.containsKey("ID")) {
					id = data.get("ID");
					if (StringUtils.isBlank(id)) {
						id = UUID.randomUUID().toString();
						data.put("ID", id);
						vo.setRecordId(id);
					}
				}
			}

			if (metaModelOperateServiceImpl.save(data, md.getModelCode())) {
				vo.setTableName(metaModelServiceImpl.queryByModelCode(md.getModelCode()).getTableName());
				return id;
			}
		}
		return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map saveModelDataFlow(DynamicPageVO pageVO, DocumentVO vo, String datadefineName, boolean masterDateSource) {
		DynamicPage page = BeanUtils.getNewInstance(pageVO, DynamicPage.class);
		DataDefine dd = PageDataBeanWorker.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(page.getDataJson()))
				.get(datadefineName);
		Map resultMap = null;
		if (dd != null) {
			DataDefine md = (DataDefine) dd;
			String id = null;
			Map<String, String> data = vo.getListParams().get(datadefineName).get(0);
			if (data != null && !data.isEmpty()) {
				if (data.containsKey("ID") || data.containsKey("id")) {
					if (!StringUtils.isBlank(data.get("ID"))) {
						id = data.get("ID");
					} else if (!StringUtils.isBlank(data.get("id"))) {
						id = data.get("id");
					} else if (masterDateSource) {
						id = vo.getRecordId();
					}
					if (StringUtils.isBlank(id)) {
						id = UUID.randomUUID().toString();
						data.put("ID", id);
						vo.setRecordId(id);
					}
				}
			}

			Map<String, Object> map = metaModelOperateServiceImpl.get(id, md.getModelCode());
			resultMap = new HashMap();
			if (map != null && map.size() > 0) {
				if (metaModelOperateServiceImpl.update(data, md.getModelCode())) {
					resultMap.put("success", "true");
				} else {
					resultMap.put("success", "false");
					resultMap.put("message", "更新失败");
				}
			} else {

				if (metaModelOperateServiceImpl.save(data, md.getModelCode())) {
					resultMap.put("success", "true");
				} else {
					resultMap.put("success", "false");
					resultMap.put("message", "保存失败");
				}
			}
			vo.setTableName(metaModelServiceImpl.queryByModelCode(md.getModelCode()).getTableName());
			resultMap.put("id", id);
		}
		return resultMap;

	}

	@Override
	public void excuteUpdate(String sql) {
		jdbcTemplate.execute(sql);
	}

	@Override
	public Map<String, Object> excuteQuery(String sql) {
		// Long dataSourceId = getDataSourceIdByCurrentSystem();
		// JdbcTemplate jdbcTemplate =
		// DataSourceFactory.getJdbcTemplateById(dataSourceId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = jdbcTemplate.queryForMap(sql);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> excuteQueryForList(String sql) {
		// Long dataSourceId = getDataSourceIdByCurrentSystem();
		// JdbcTemplate jdbcTemplate =
		// DataSourceFactory.getJdbcTemplateById(dataSourceId);
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public void excuteUpdate(String sql, String dsName) {

		jdbcTemplate.execute(sql);
	}

	@Override
	public Map<String, Object> excuteQuery(String sql, String dsName) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = jdbcTemplate.queryForMap(sql);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> excuteQueryForList(String sql, String dsName) {

		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public String insertModelData(Map<String, String> map, String modelCode) {
		String id = null;
		if (map != null && !map.isEmpty()) {
			if (map.containsKey("ID")) {
				id = map.get("ID");
				if (!StringUtils.isNotBlank(id)) {
					id = UUID.randomUUID().toString();
					map.put("ID", id);
				}
			}
			metaModelOperateServiceImpl.save(map, modelCode);
		}
		return id;
	}

	@Override
	public String updateModelData(Map<String, String> map, String modelCode) {
		if (map != null && !map.isEmpty()) {
			if (metaModelOperateServiceImpl.update(map, modelCode)) {
				String _primay_key_ = map.get("_primay_key_");
				return map.get(_primay_key_);
			}

		}
		return null;
	}

	public String print(String docId) {
		return null;
	}

	@Override
	public Boolean deleteModelData(DynamicPageVO pageVo, String recordId) {
		DynamicPage page = BeanUtils.getNewInstance(pageVo, DynamicPage.class);
		Map<String, DataDefine> dds = PageDataBeanWorker
				.convertConfToDataDefines(StringEscapeUtils.unescapeHtml4(page.getDataJson()));
		boolean flag = true;
		for (Entry<String, DataDefine> entry : dds.entrySet()) {
			DataDefine dd = entry.getValue();
			if (dd != null) {
				DataDefine md = (DataDefine) dd;
				if (!metaModelOperateServiceImpl.delete(recordId, md.getModelCode())) {
					flag = false;
				}
			} else {
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public PageList<DocumentVO> selectPagedByExample(BaseExample baseExample, int currentPage, int pageSize,
			String sortString) {
		PageList<Document> result = queryChannel.selectPagedByExample(Document.class, baseExample, currentPage,
				pageSize, sortString);
		PageList<DocumentVO> resultVO = new PageList<DocumentVO>(result.getPaginator());
		for (Document doc : result) {
			resultVO.add(BeanUtils.getNewInstance(doc, DocumentVO.class));
		}
		result.clear();
		return resultVO;
	}

	@Override
	public boolean deleteByExample(BaseExample example) {
		List<DocumentVO> list = this.selectPagedByExample(example, 1, Integer.MAX_VALUE, null);
		boolean flag = true;
		for (DocumentVO doc : list) {
			if (!delete(doc)) {
				flag = false;
			}
		}
		return flag;
	}

	@Override // 计划废弃
	public Long findWorkId(String definationId) {
		return 0L;
	}

	@Override // 计划废弃
	public Long findNodeId(String taskDefinationKey, Long workId) {
		return 0L;
	}

}
