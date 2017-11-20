package cn.org.awcp.venson.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.engine.util.MyScriptEngine;
import cn.org.awcp.formdesigner.utils.ScriptEngineUtils;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.BaseController;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.interceptor.ExceptionHandler;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@RestController
public class APIController extends BaseController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	@Autowired
	private CacheManager cacheManager;

	private static final String CACHE_NAME = "api";

	@RequestMapping("/execute/{APIId}")
	public ReturnResult execute(@PathVariable("APIId") String APIId, HttpServletRequest request) {
		ReturnResult result = ReturnResult.get();
		PFMAPI api = PFMAPI.get(APIId);
		// 校验接口
		if (!validateAPI(result, api, request)) {
			return result;
		}
		// 获取参数值
		Map<String, Object> params = wrapMap(request.getParameterMap(), "APIId");
		result.setStatus(StatusCode.SUCCESS);
		// 是否缓存
		if (api.isCache()) {
			// 从缓存中读取值
			Cache cache = cacheManager.getCache(CACHE_NAME);
			if (cache != null) {
				Element obj = cache.get(api.getAPIName() + "_" + params.toString());
				if (obj != null) {
					result.setData(obj.getValue());
					return result;
				}

			}
		}
		// 增加数据
		if (api.getAPIType() == APIType.ADD.getValue()) {
			executeAdd(request, result, api);
		} else if (api.getAPIType() == APIType.UPDATE.getValue()) {
			executeUpdate(request, result, api);
		} else if (api.getAPIType() == APIType.GET.getValue()) {
			executeGet(request, result, api);
		} else if (api.getAPIType() == APIType.DELETE.getValue()) {
			executeDelete(request, api);
		} else if (api.getAPIType() == APIType.EXECUTE.getValue()) {
			execute(request, result, api, params);
		} else if (api.getAPIType() == APIType.QUERY.getValue()) {
			executeQuery(request, result, api, params);
		} else if (api.getAPIType() == APIType.PAGE.getValue()) {
			executePage(request, result, api, params);
		} else if (api.getAPIType() == APIType.EXECUTE_SCRIPT.getValue()) {
			executeScript(request, result, api);
		}
		String resName = request.getParameter("resName");
		if (StringUtils.isNotBlank(resName)) {
			result.setRows(result.getData());
			result.setData(null);
		}
		if (api.isCache()) {
			Cache cache = cacheManager.getCache(CACHE_NAME);
			if (cache != null) {
				Element element = new Element(api.getAPIName() + "_" + params.toString(), result.getData());
				cache.put(element);
			}
		}
		return result;
	}

	@RequestMapping("/execute/merge")
	public ReturnResult execute(@RequestParam("ids") String[] ids, HttpServletRequest request) {
		ReturnResult result = ReturnResult.get();
		Map<String, Object> map = new HashMap<>();
		for (String id : ids) {
			ReturnResult subResult = execute(id, request);
			// 判断是否执行成功
			if (subResult.getStatus() == StatusCode.SUCCESS.getStatus()) {
				map.put(id, subResult.getData());
			} else {
				return subResult.setData(null);
			}
		}
		return result.setData(map).setStatus(StatusCode.SUCCESS);
	}

	private boolean validateAPI(ReturnResult result, PFMAPI api, HttpServletRequest request) {
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
				result.setStatus(StatusCode.NO_LOGIN.setMessage("您还未登录，请先登录！"));
				return false;
			}
		}
		// 判断请求方式
		String method = request.getMethod().toLowerCase();
		if (!api.getAPIMethod().equals("both") && !api.getAPIMethod().equals(method)) {
			result.setStatus(StatusCode.NO_ACCESS.setMessage("非法请求，禁止访问！"));
			return false;
		}
		List<APIRule> rules = api.getRules();
		// 接口权限认证
		if (rules != null && !rules.isEmpty()) {
			for (APIRule rule : rules) {
				if (!checkRule(result, rule))
					return false;
			}
		}
		return true;
	}

	/**
	 * 权限校验
	 * 
	 * @param result
	 * @param rule
	 * @return
	 */
	private boolean checkRule(ReturnResult result, APIRule rule) {
		// 0为脚本规则
		if ("0".equals(rule.getType())) {
			ScriptEngine engine = getEngine();
			try {
				Object ret = engine.eval(rule.getRules());
				// 返回值为false则校验不通过
				if (!"success".equals(ret)) {
					result.setStatus(StatusCode.FAIL.setMessage(String.valueOf(ret)));
					return false;
				}
			} catch (ScriptException e) {
				result.setStatus(StatusCode.NO_ACCESS.setMessage(e.toString()));
				return false;
			}
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
	private void executeScript(HttpServletRequest request, ReturnResult result, PFMAPI api) {
		// 获取参数值
		String script = api.getAPISQL();
		ScriptEngine engine = getEngine();
		if (StringUtils.isNotBlank(script)) {
			SzcloudJdbcTemplate jdbcTemplate1 = Springfactory.getBean("jdbcTemplate");
			jdbcTemplate1.beginTranstaion();
			try {
				Object ret = engine.eval(script);
				jdbcTemplate1.commit();
				result.setData(ret);
			} catch (Exception e) {
				ExceptionHandler.handler(e, result);
				logger.info("ERROR", e);
				try {
					jdbcTemplate1.rollback();
				} catch (Exception e1) {
					logger.info("ERROR", e1);
				}
			}
		}
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
	private void executePage(HttpServletRequest request, ReturnResult result, PFMAPI api, Map<String, Object> params) {

		String ps = request.getParameter("pageSize");
		String cp = request.getParameter("currentPage");
		int pageSize = Integer.parseInt(StringUtils.isNumeric(ps) ? ps : "5");
		int currentPage = Integer.parseInt(StringUtils.isNumeric(cp) ? cp : "1");
		String script = api.getAPISQL();
		ScriptEngine engine = getEngine();
		String sql = null;
		if (StringUtils.isNotBlank(script)) {
			try {
				sql = (String) engine.eval(script);
			} catch (ScriptException e) {
				result.setStatus(StatusCode.FAIL.setMessage(e.toString()));
				logger.info("ERROR", e);
				return;
			}
		}
		// 执行分页语句
		List<Map<String, Object>> data = jdbcTemplate
				.queryForList(sql + " limit " + (currentPage - 1) * pageSize + "," + pageSize, params);
		// 获取总数
		Object count = jdbcTemplate.queryForObject("select count(1) from(" + sql + ") temp", params, Object.class);
		result.setData(data).setTotal(count);
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
	private void execute(HttpServletRequest request, ReturnResult result, PFMAPI api, Map<String, Object> params) {
		String script = api.getAPISQL();
		ScriptEngine engine = getEngine();
		engine.put("jdbcTemplate", Springfactory.getBean("jdbcTemplate"));
		String sql = null;
		if (StringUtils.isNotBlank(script)) {
			try {
				sql = (String) engine.eval(script);
			} catch (ScriptException e) {
				result.setStatus(StatusCode.FAIL.setMessage(e.toString()));
				logger.info("ERROR", e);
				return;
			}
		}
		// 执行语句
		int data = jdbcTemplate.update(sql, params);
		result.setData(data);
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
	private void executeQuery(HttpServletRequest request, ReturnResult result, PFMAPI api, Map<String, Object> params) {
		String script = api.getAPISQL();
		ScriptEngine engine = getEngine();
		String sql = null;
		if (StringUtils.isNotBlank(script)) {
			try {
				sql = (String) engine.eval(script);
			} catch (ScriptException e) {
				result.setStatus(StatusCode.FAIL.setMessage(e.toString()));
				logger.info("ERROR", e);
				return;
			}
		}
		// 执行语句
		List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, params);
		result.setData(data);
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
	private void executeGet(HttpServletRequest request, ReturnResult result, PFMAPI api) {
		// 获取主键
		String id = request.getParameter("id");
		// 根据获取记录
		Map<String, Object> data = meta.get(id, api.getAPITable());
		result.setData(data);
	}

	/**
	 * 执行更新
	 * 
	 * @param request
	 * @param api
	 */
	private void executeUpdate(HttpServletRequest request, ReturnResult result, PFMAPI api) {
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
		try {
			meta.update(params, api.getAPITable());
		} catch (Exception e) {
			result.setStatus(StatusCode.FAIL.setMessage(e.toString()));
			logger.info("ERROR", e);
		}
	}

	/**
	 * 执行增加
	 * 
	 * @param request
	 * @param api
	 */
	private void executeAdd(HttpServletRequest request, ReturnResult result, PFMAPI api) {
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
		try {
			meta.save(params, api.getAPITable());
		} catch (Exception e) {
			result.setStatus(StatusCode.FAIL.setMessage(e.toString()));
			logger.info("ERROR", e);
		}
	}
}
