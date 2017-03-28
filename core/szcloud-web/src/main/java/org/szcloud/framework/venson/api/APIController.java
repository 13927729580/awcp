package org.szcloud.framework.venson.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.venson.controller.base.BaseController;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;

@Controller
@RequestMapping("api")
public class APIController extends BaseController {
	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	@RequestMapping("executeAPI")
	@ResponseBody
	public ReturnResult executeAPI(@RequestParam("APIId") String APIId, HttpServletRequest request) throws Exception {
		ReturnResult result = ReturnResult.get();
		result.setStatus(StatusCode.SUCCESS);
		PFMAPI api = PFMAPI.get(APIId);

		// 增加数据
		if (api.getAPIType() == APIType.ADD.getValue()) {
			executeAdd(request, result, api);
		} else if (api.getAPIType() == APIType.UPDATE.getValue()) {
			executeUpdate(request, api);
		} else if (api.getAPIType() == APIType.GET.getValue()) {
			return executeGet(request, result, api);
		} else if (api.getAPIType() == APIType.DELETE.getValue()) {
			executeDelete(request, api);
		} else if (api.getAPIType() == APIType.EXECUTE.getValue()) {
			return execute(request, result, api);
		} else if (api.getAPIType() == APIType.QUERY.getValue()) {
			return executeQuery(request, result, api);
		} else if (api.getAPIType() == APIType.PAGE.getValue()) {
			return executePage(request, result, api);
		}
		return result;
	}

	/**
	 * 执行分页查询
	 * 
	 * @param request
	 * @param result
	 * @param api
	 * @return
	 */
	private ReturnResult executePage(HttpServletRequest request, ReturnResult result, PFMAPI api) {
		// 获取参数值
		Map<String, Object> params = wrapMap(request.getParameterMap(), "APIId");
		String ps = request.getParameter("pageSize");
		String cp = request.getParameter("currentPage");
		int pageSize = Integer.parseInt(StringUtils.isNumeric(ps) ? ps : "5");
		int currentPage = Integer.parseInt(StringUtils.isNumeric(cp) ? cp : "1");
		// 执行分页语句
		List<Map<String, Object>> data = jdbcTemplate
				.queryForList(api.getAPISQL() + " limit " + (currentPage - 1) * pageSize + "," + pageSize, params);
		// 获取总数
		Object count = jdbcTemplate.queryForObject("select count(1) from(" + api.getAPISQL() + ") temp", params,
				Object.class);
		return result.setData(data).setTotal(count);
	}

	/**
	 * 执行语句
	 * 
	 * @param request
	 * @param result
	 * @param api
	 * @return
	 */
	private ReturnResult execute(HttpServletRequest request, ReturnResult result, PFMAPI api) {
		// 获取参数值
		Map<String, Object> params = wrapMap(request.getParameterMap(), "APIId");
		// 执行语句
		int data = jdbcTemplate.update(api.getAPISQL(), params);
		return result.setData(data);
	}

	/**
	 * 执行查询
	 * 
	 * @param request
	 * @param result
	 * @param api
	 * @return
	 */
	private ReturnResult executeQuery(HttpServletRequest request, ReturnResult result, PFMAPI api) {
		// 获取参数值
		Map<String, Object> params = wrapMap(request.getParameterMap(), "APIId");

		List<Map<String, Object>> data = jdbcTemplate.queryForList(api.getAPISQL(), params);
		return result.setData(data);
	}

	/**
	 * 执行删除
	 * 
	 * @param request
	 * @param api
	 */
	private void executeDelete(HttpServletRequest request, PFMAPI api) {
		// 获取主键
		String id = request.getParameter(api.getPrimaryKey());
		// 删除记录
		meta.delete(id, api.getAPITable());
	}

	/**
	 * 执行获取
	 * 
	 * @param request
	 * @param api
	 */
	private ReturnResult executeGet(HttpServletRequest request, ReturnResult result, PFMAPI api) {
		// 获取主键
		String id = request.getParameter(api.getPrimaryKey());
		// 根据获取记录
		Map<String, String> data = meta.get(id, api.getAPITable());
		return result.setData(data);
	}

	/**
	 * 执行更新
	 * 
	 * @param request
	 * @param api
	 */
	private void executeUpdate(HttpServletRequest request, PFMAPI api) throws Exception {
		List<TableDesc> fields = api.getAPITableFields();
		Map<String, String> params = new HashMap<>();
		// 获取参数值
		for (TableDesc field : fields) {
			String value = request.getParameter(field.getField());
			if (StringUtils.isNotBlank(value)) {
				params.put(field.getField(), value);
			}
		}
		// 更新
		meta.update(params, api.getAPITable());
	}

	/**
	 * 执行增加
	 * 
	 * @param request
	 * @param api
	 */
	private void executeAdd(HttpServletRequest request, ReturnResult result, PFMAPI api) throws Exception {
		List<TableDesc> fields = api.getAPITableFields();
		Map<String, String> params = new HashMap<>();
		for (TableDesc field : fields) {
			// 置入主键
			field.setDeafultValue(params);
			// 置入共同的值
			field.setPrimaryValue(params);
			String value = request.getParameter(field.getField());
			// 获取参数值
			if (StringUtils.isNotBlank(value)) {
				params.put(field.getField(), value);
			}
		}
		// 保存
		Long id = meta.save(params, api.getAPITable());
		result.setData(id);
	}
}
