package org.szcloud.framework.formdesigner.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.venson.controller.base.BaseController;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;

@Controller
@RequestMapping("/fd/plugin")
public class PluginController extends BaseController {
	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;
	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	// 获取当前用户信息
	@RequestMapping("excuteSql")
	@ResponseBody
	public ReturnResult excuteSql(String sql, HttpServletRequest request) {
		ReturnResult result = ReturnResult.get();
		if (!sql.toLowerCase().contains("select")) {
			result.setStatus(StatusCode.FAIL.setMessage("非法请求"));
		} else {
			Map<String, String[]> map = request.getParameterMap();
			Map<String, Object> params = wrapMap(map, "sql");
			result.setStatus(StatusCode.SUCCESS).setData(jdbcTemplate.queryForList(sql, params));
		}
		return result;
	}
}
