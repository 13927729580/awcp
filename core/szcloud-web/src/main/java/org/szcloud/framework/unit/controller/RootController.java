package org.szcloud.framework.unit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.formdesigner.core.domain.DynamicPage;
import org.szcloud.framework.formdesigner.core.domain.design.context.data.DataDefine;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.venson.controller.base.ControllerHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping("/")
public class RootController {

	@Autowired
	private MetaModelOperateService metaModelOperateServiceImpl;
	@Resource(name = "namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

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

		List<Map<String, Object>> documentList = metaModelOperateServiceImpl.search(sql, null);

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
	public Object getTreeData(Long dynamicPageId) {
		DynamicPage page = DynamicPage.getRepository().get(DynamicPage.class, dynamicPageId);
		if (StringUtils.isNotBlank(page.getDataJson())) {
			JSONArray array = JSON.parseArray(StringEscapeUtils.unescapeHtml4(page.getDataJson()));
			DataDefine dd = JSON.parseObject(array.getString(0), DataDefine.class);
			String sql = dd.getSqlScript();
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

}
