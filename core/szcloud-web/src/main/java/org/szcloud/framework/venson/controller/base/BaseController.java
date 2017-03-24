package org.szcloud.framework.venson.controller.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContext;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;

public abstract class BaseController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected Map<String, Object> wrapMap(Map<String, String[]> map, String... filters) {
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
				param.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		return param;
	}

	protected Map<String, String> mapObj2Str(Map<String, Object> map) {
		return (Map) map;

	}

	public static final char UNDERLINE = '_';

	// 驼峰转下划线
	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(Character.toLowerCase(c));
			}
		}
		return sb.toString();
	}

	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	protected String getMessage(HttpServletRequest request, String code) {
		RequestContext context = new RequestContext(request);
		return context.getMessage(code);
	}

	protected PunUserBaseInfoVO getUser() {
		return (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
	}

	protected Long getUserId() {
		if (getUser() == null)
			return null;

		return getUser().getUserId();
	}

}
