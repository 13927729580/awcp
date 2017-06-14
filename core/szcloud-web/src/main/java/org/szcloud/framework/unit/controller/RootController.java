package org.szcloud.framework.unit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.formdesigner.application.service.FormdesignerService;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.core.domain.design.context.data.DataDefine;
import org.szcloud.framework.formdesigner.utils.ScriptEngineUtils;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.venson.controller.base.ControllerContext;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/")
public class RootController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private MetaModelOperateService metaModelOperateServiceImpl;
	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;
	@Autowired
	private FormdesignerService formdesignerServiceImpl;

	/**
	 * 获取部门树数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "taskManagement/persons", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> personDocuments(String type) {

		String sql = "select Group_ID as ID,PARENT_GROUP_ID as PID,GROUP_CH_NAME as Name,NUMBER as NUMBER"
				+ " from p_un_group b order by case when NUMBER is null then 0 end , NUMBER asc";

		List<Map<String, Object>> documentList = metaModelOperateServiceImpl.search(sql);

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : documentList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", map.get("ID"));
			// 为了不引起冲突的判断

			if ("manage".equals(type)) {
				if (!"".equals(map.get("NUMBER")) && map.get("NUMBER") != null) {
					m.put("name", map.get("Name") + "-" + map.get("NUMBER"));
				} else {

					m.put("name", map.get("Name"));
				}

			} else {
				m.put("name", map.get("Name"));
			}

			// m.put("name", map.get("Name"));
			m.put("pId", map.get("PID"));
			// m.put("number",map.get("NUMBER"));
			// m.put("url", "./oa/task/task.jsp?id="+map.get("ID"));
			m.put("url",
					"punUserGroupController/getUsers.do?groupId=" + map.get("ID") + "&rootGroupId=" + map.get("PID"));
			m.put("target", "sysEditFrame");
			m.put("open", true);
			// 只有简单的选择功能
			if ("simple".equals(type)) {
				m.remove("url");
			}

			returnList.add(m);
		}

		return returnList;
	}

	@RequestMapping(value = "getTreeData", method = RequestMethod.POST)
	@ResponseBody
	public Object getTreeData(Long dynamicPageId) throws ScriptException {
		DynamicPageVO page = formdesignerServiceImpl.findById(dynamicPageId);
		if (StringUtils.isNotBlank(page.getDataJson())) {
			DocumentVO docVo = getDocVo(dynamicPageId, page);
			JSONArray array = JSON.parseArray(StringEscapeUtils.unescapeHtml4(page.getDataJson()));
			DataDefine dd = JSON.parseObject(array.getString(0), DataDefine.class);
			ScriptEngine engine = getEngine(page, docVo);
			String script = dd.getSqlScript();
			String sql = null;
			if (StringUtils.isNotBlank(script)) {
				sql = (String) engine.eval(script);
			}
			List<Map<String, Object>> data = metaModelOperateServiceImpl.search(sql);
			// 处理url路径
			for (Map<String, Object> map : data) {
				String url = (String) map.get("url");
				if (StringUtils.isNotBlank(url)) {
					url = ControllerHelper.getBasePath() + url;
					map.put("url", url);
				}

			}
			return data;
		}

		return null;
	}

	private ScriptEngine getEngine(DynamicPageVO page, DocumentVO docVo) {
		ScriptEngine engine = ScriptEngineUtils.getScriptEngine(docVo, page);
		Map<String, Object> root = new HashMap<String, Object>();
		HttpServletRequest request = ControllerContext.getRequest();
		engine.put("request", request);
		engine.put("session", request.getSession());
		engine.put("root", root);
		return engine;
	}

	@RequestMapping(value = "getListData", method = RequestMethod.POST)
	@ResponseBody
	public ReturnResult getListData(HttpServletRequest request, Long dynamicPageId) {
		ReturnResult result = ReturnResult.get();
		result.setStatus(StatusCode.SUCCESS);
		try {
			if (dynamicPageId == null) {
				result.setStatus(StatusCode.FAIL.setMessage("动态页面Id不能为空！"));
			}
			DynamicPageVO page = formdesignerServiceImpl.findById(dynamicPageId);

			if (page == null) {
				result.setStatus(StatusCode.FAIL.setMessage("动态页面没找到！"));
			}
			if (StringUtils.isBlank(page.getDataJson())) {
				result.setStatus(StatusCode.FAIL.setMessage("动态页面数据源脚本为空！"));
			}
			DocumentVO docVo = getDocVo(dynamicPageId, page);
			JSONArray array = JSON.parseArray(StringEscapeUtils.unescapeHtml4(page.getDataJson()));
			DataDefine dd = JSON.parseObject(array.getString(0), DataDefine.class);
			ScriptEngine engine = getEngine(page, docVo);
			String script = dd.getSqlScript();
			String sql = null;
			if (StringUtils.isNotBlank(script)) {

				sql = (String) engine.eval(script);

			}
			String ps = request.getParameter("pageSize");
			String cp = request.getParameter("currentPage");
			int pageSize = Integer.parseInt(StringUtils.isNumeric(ps) ? ps : "10");
			int currentPage = Integer.parseInt(StringUtils.isNumeric(cp) ? cp : "1");
			List<Map<String, Object>> data = metaModelOperateServiceImpl
					.search(sql + " limit " + (currentPage - 1) * pageSize + "," + pageSize);
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(*) from (").append(sql).append(") temp");
			result.setData(data).setTotal(countSql);
		} catch (Exception e) {
			result.setStatus(StatusCode.FAIL.setMessage("执行脚本出错！"));
			logger.info("ERROR", e);
		}
		return result;
	}

	private DocumentVO getDocVo(Long dynamicPageId, DynamicPageVO page) {
		DocumentVO docVo = new DocumentVO();
		docVo.setDynamicPageId(page.getId().toString());
		docVo.setUpdate(true);
		docVo.setRecordId(dynamicPageId + "");
		docVo.setDynamicPageName(page.getName());
		return docVo;
	}

}
