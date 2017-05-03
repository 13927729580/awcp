package org.szcloud.framework.venson.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.core.domain.SzcloudJdbcTemplate;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.formdesigner.engine.util.MyScriptEngine;
import org.szcloud.framework.formdesigner.utils.ScriptEngineUtils;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.venson.common.SC;
import org.szcloud.framework.venson.controller.base.BaseController;
import org.szcloud.framework.venson.controller.base.ControllerContext;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
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
		PFMAPI api = PFMAPI.get(APIId);
		// 校验接口
		if (!validateAPI(result, api)) {
			return result;
		}
		result.setStatus(StatusCode.SUCCESS.setMessage(api.getAPIDesc()));
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
		} else if (api.getAPIType() == APIType.EXECUTE_SCRIPT.getValue()) {
			return executeScript(request, result, api);
		}
		return result;
	}

	private boolean validateAPI(ReturnResult result, PFMAPI api) {
		if (api == null) {
			result.setStatus(StatusCode.FAIL.setMessage("无效接口！"));
			return false;
		}
		// 是否已禁用
		if (api.getAPIState() == 0) {
			result.setStatus(StatusCode.FAIL.setMessage("接口已禁用！"));
			return false;
		}
		// 是否需要登录校验
		if (api.getAPIIsLogin() == 1) {
			if (ControllerHelper.getUser() == null) {
				result.setStatus(StatusCode.NO_LOGIN.setMessage("您还未登录，清先登录！"));
				return false;
			}
		}
		// 判断请求方式
		String method = ControllerContext.getRequest().getMethod().toLowerCase();
		if (!api.getAPIMethod().equals("both") && !api.getAPIMethod().equals(method)) {
			result.setStatus(StatusCode.NO_ACCESS.setMessage("非法请求，禁止访问！"));
			return false;
		}
		return true;
	}

	/**
	 * 执行脚本
	 * 
	 * @param request
	 * @param result
	 * @param api
	 * @return
	 * @throws ScriptException
	 */
	private ReturnResult executeScript(HttpServletRequest request, ReturnResult result, PFMAPI api)
			throws ScriptException {
		// 获取参数值
		String script = api.getAPISQL();
		ScriptEngine engine = getEngine();
		if (StringUtils.isNotBlank(script)) {
			SzcloudJdbcTemplate jdbcTemplate1 = Springfactory.getBean("jdbcTemplate");
			jdbcTemplate1.beginTranstaion();
			try {
				Object ret = engine.eval(script);
				jdbcTemplate1.commit();
				return result.setData(ret);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					jdbcTemplate1.rollback();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
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
	 * @throws ScriptException
	 */
	private ReturnResult executePage(HttpServletRequest request, ReturnResult result, PFMAPI api)
			throws ScriptException {
		// 获取参数值
		Map<String, Object> params = wrapMap(request.getParameterMap(), "APIId");
		String ps = request.getParameter("pageSize");
		String cp = request.getParameter("currentPage");
		int pageSize = Integer.parseInt(StringUtils.isNumeric(ps) ? ps : "5");
		int currentPage = Integer.parseInt(StringUtils.isNumeric(cp) ? cp : "1");
		String script = api.getAPISQL();
		ScriptEngine engine = getEngine();
		String sql = null;
		if (StringUtils.isNotBlank(script)) {
			sql = (String) engine.eval(script);
		}
		// 执行分页语句
		List<Map<String, Object>> data = jdbcTemplate
				.queryForList(sql + " limit " + (currentPage - 1) * pageSize + "," + pageSize, params);
		// 获取总数
		Object count = jdbcTemplate.queryForObject("select count(1) from(" + sql + ") temp", params, Object.class);
		return result.setData(data).setTotal(count);
	}

	/**
	 * 执行语句
	 * 
	 * @param request
	 * @param result
	 * @param api
	 * @return
	 * @throws ScriptException
	 */
	private ReturnResult execute(HttpServletRequest request, ReturnResult result, PFMAPI api) throws ScriptException {
		// 获取参数值
		Map<String, Object> params = wrapMap(request.getParameterMap(), "APIId");
		String script = api.getAPISQL();
		ScriptEngine engine = getEngine();
		engine.put("jdbcTemplate", Springfactory.getBean("jdbcTemplate"));
		String sql = null;
		if (StringUtils.isNotBlank(script)) {
			sql = (String) engine.eval(script);
		}
		// 执行语句
		int data = jdbcTemplate.update(sql, params);
		return result.setData(data);
	}

	private ScriptEngine getEngine() {
		ScriptEngine engine = new MyScriptEngine(ScriptEngineUtils.getScriptEngine(), SC.SYSTEM_ID);
		HttpServletRequest request = ControllerContext.getRequest();

		engine.put("request", request);
		engine.put("session", request.getSession());
		return engine;
	}

	/**
	 * 执行查询
	 * 
	 * @param request
	 * @param result
	 * @param api
	 * @return
	 * @throws ScriptException
	 */
	private ReturnResult executeQuery(HttpServletRequest request, ReturnResult result, PFMAPI api)
			throws ScriptException {
		// 获取参数值
		Map<String, Object> params = wrapMap(request.getParameterMap(), "APIId");
		String script = api.getAPISQL();
		ScriptEngine engine = getEngine();
		String sql = null;
		if (StringUtils.isNotBlank(script)) {
			sql = (String) engine.eval(script);
		}
		// 执行语句
		List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, params);
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
		String id = request.getParameter("id");
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
