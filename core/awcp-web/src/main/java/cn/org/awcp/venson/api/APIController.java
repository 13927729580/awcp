package cn.org.awcp.venson.api;

import cn.org.awcp.venson.controller.base.BaseController;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class APIController extends BaseController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	@Resource(name = "apiService")
	private APIService apiService;

	@RequestMapping("/execute/{APIId}")
	public ReturnResult execute(@PathVariable("APIId") String APIId, HttpServletRequest request) {
		ReturnResult result = ReturnResult.get();
		PFMAPI api = PFMAPI.get(APIId);
		// 获取参数值
		Map<String, Object> params = wrapMap(request.getParameterMap(), "APIId");
		// 校验接口
		if (!apiService.validateAPI(result, api, request.getMethod().toLowerCase(),params)){
			return result;
		}
		// 执行脚本
		apiService.execute(result, api, params);
		return result;
	}

	@RequestMapping("/execute/merge")
	public ReturnResult execute(@RequestParam("ids") String[] ids, HttpServletRequest request) {
		ReturnResult result = ReturnResult.get();
		Map<String, Object> map = new HashMap<>(ids.length);
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

}
