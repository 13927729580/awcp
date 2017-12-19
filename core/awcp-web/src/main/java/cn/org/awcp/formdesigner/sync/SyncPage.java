package cn.org.awcp.formdesigner.sync;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 同步动态页面实体类
 * 
 * @author venson
 *
 */
public class SyncPage implements Serializable {

	private static final long serialVersionUID = 1L;
	private String content;
	private String pageId;
	private String modelXml;
	private String templateId;
	private List<String> sqls;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getModelXml() {
		return modelXml;
	}

	public void setModelXml(String modelXml) {
		this.modelXml = modelXml;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public JSONArray getArr(String model_xml) {
		return JSON.parseArray(StringEscapeUtils.unescapeHtml4(model_xml));
	}

	public List<String> getModelCodes(String model_xml) {
		return getModelCodes(getArr(model_xml));
	}

	public List<String> getModelCodes(JSONArray arr) {
		int size = arr.size();
		List<String> list = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			JSONObject json = arr.getJSONObject(i);
			list.add(json.getString("modelCode"));
		}
		return list;
	}

	public String extractEmpty(List<String> sqls) {
		StringBuffer buffer = new StringBuffer();
		for (String sql : sqls) {
			if (sql.startsWith("DELETE")) {
				buffer.append(sql + "\n");
			} else if (sql.startsWith("DROP")) {
				buffer.append(sql + "\n");
			}
		}
		return buffer.toString();
	}

	public List<String> extract(String content) {
		if (sqls != null) {
			return sqls;
		}
		sqls = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		String[] arr = content.split("\n");
		for (String line : arr) {
			if (StringUtils.isNotBlank(line)) {
				line = line.trim();
				if (line.startsWith("/*")) {
					continue;
				}
				sql.append(line);
				if (StringUtils.endsWith(sql, ";")) {
					String sqlStr = sql.toString();
					sqls.add(sqlStr);
					sql.replace(0, sql.length(), "");
				}
			}

		}
		if (sql.length() > 0) {
			sqls.add(sql.toString());
		}
		return sqls;
	}

}
