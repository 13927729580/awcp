package cn.org.awcp.formdesigner.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.application.vo.AuthorityCompoentVO;
import cn.org.awcp.formdesigner.application.vo.AuthorityGroupWorkFlowNodeVO;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.core.domain.design.context.act.PageAct;
import cn.org.awcp.formdesigner.service.AuthorityCompoentServiceImpl;
import cn.org.awcp.formdesigner.service.AuthorityGroupWorkFlowNodeServiceImpl;
import cn.org.awcp.venson.controller.base.ControllerHelper;

public class DocUtils {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(DocUtils.class);
	public static int FormToFlow = 1;
	public static int FlowToForm = 2;

	/**
	 * 计算状态脚本
	 * 
	 * @param script
	 * @param engine
	 * @return
	 */
	public static boolean computeStatus(String script, ScriptEngine engine) {
		if (StringUtils.isNotBlank(script)) {
			script = StringEscapeUtils.unescapeHtml4(script);
			Boolean status = null;
			try {
				status = (Boolean) engine.eval(script);
			} catch (ScriptException e) {
				logger.info("ERROR", e);
			}
			if (status != null)
				return status;
		}
		return false;
	}

	/**
	 * 计算状态脚本
	 * 
	 * @param script
	 * @param engine
	 * @return
	 */
	public static boolean computeStatus(String script, ScriptEngine engine, boolean isReadPage) {
		if (StringUtils.isNotBlank(script)) {
			script = StringEscapeUtils.unescapeHtml4(script);
			Boolean status = null;
			try {
				status = (Boolean) engine.eval(script);
			} catch (ScriptException e) {
				logger.info("ERROR", e);
			}
			if (status != null)
				return status;
		}
		return isReadPage || false;
	}

	/**
	 * 根据组件配置的dataItemCode从dataMap中查询数据 同时执行默认值脚本，将结果放入容器中
	 * 
	 * @param c
	 * @param dataMap
	 * @return
	 */
	public static String getComponentValue(JSONObject c, Map<String, List<Map<String, String>>> dataMap,
			ScriptEngine engine, DocumentVO docVo) {
		try {
			String code = c.getString("dataItemCode");

			if (code != null) {
				String[] itemCode = code.split("\\.");
				if (itemCode != null && itemCode.length > 1) {
					if (dataMap != null && !dataMap.isEmpty()) {
						List<Map<String, String>> models = dataMap.get(itemCode[0] + "_list");
						// models.get(0).get("_NEW") == null
						// 说明是从数据库中取出的数据而不是手动给赋值的
						if (models != null && models.size() > 0 && models.get(0).get("_NEW") == null) {
							String v = models.get(0).get(itemCode[1]);
							// 如果数据库中的值为空，则去查找默认值
							if (StringUtils.isBlank(v)) {
								String defaultValueScript = c.getString("defaultValueScript");
								if (StringUtils.isNotBlank(defaultValueScript)) {
									String script = StringEscapeUtils.unescapeHtml4(defaultValueScript);
									Object o = engine.eval(script);
									v = String.valueOf(o);
									if (StringUtils.isNotBlank(v)) {
										models.get(0).put(itemCode[1], v);
									}
								}
							}
							return v;
						} else {
							String defaultValueScript = c.getString("defaultValueScript");
							if (StringUtils.isNotBlank(defaultValueScript)) {
								String script = StringEscapeUtils.unescapeHtml4(defaultValueScript);

								Object o = engine.eval(script);
								DocumentUtils docUtils = (DocumentUtils) engine.get("DocumentUtils");
								if (o != null) {
									docUtils.setDataItem(itemCode[0] + "_list", itemCode[1], String.valueOf(o));
									docUtils.setDataItem(itemCode[0] + "_list", "_NEW", "1");
								}
								return String.valueOf(o);

							}
						}
					}
				}
			}
		} catch (ScriptException e) {
			logger.info("ERROR", e);
		}
		return null;
	}

	// 解析组件权限
	public static void flowAuthorityResolve(DocumentVO docVo, Map<String, JSONObject> map) {

		AuthorityCompoentServiceImpl authorityCompoentService = Springfactory.getBean("authorityCompoentServiceImpl");
		AuthorityGroupWorkFlowNodeServiceImpl authorityGroupWorkFlowNodeService = Springfactory
				.getBean("authorityGroupWorkFlowNodeServiceImpl");
		Map<String, Object> resParams = new HashMap<String, Object>();
		resParams.put("dynamicPageId", docVo.getDynamicPageId());
		resParams.put("flowNode", docVo.getEntryId());
		List<AuthorityGroupWorkFlowNodeVO> flvo = authorityGroupWorkFlowNodeService.queryResult("eqQueryList",
				resParams);
		for (AuthorityGroupWorkFlowNodeVO ao : flvo) {
			resParams.clear();
			resParams.put("authorityGroupId", ao.getAuthorityGroup());
			List<AuthorityCompoentVO> resoVOs = authorityCompoentService.queryResult("eqQueryList", resParams);
			for (AuthorityCompoentVO vo : resoVOs) {
				if (vo.getAuthorityValue() != null) {
					if (!map.containsKey(vo.getComponentId())) {
						continue;
					}
					JSONObject o = map.get(vo.getComponentId());
					// 修改
					if (vo.getAuthorityValue().contains("11")) {
						o.put("readonly", "false");
						o.put("disabled", "false");
						o.put("hidden", "false");
					} else {
						// 只读
						if (vo.getAuthorityValue().contains("10")) {
							o.put("readonly", "true");
						}
						// 隐藏
						if (vo.getAuthorityValue().contains("12")) {
							o.put("hidden", "true");
						}
					}
				}

			}
		}
	}

	/**
	 * 根据组件配置的dataItemCode从dataMap中查询数据
	 * 
	 * @param c
	 * @param dataMap
	 * @return
	 */
	public static String getComponentValue(JSONObject c, Map<String, List<Map<String, String>>> dataMap) {
		String code = c.getString("dataItemCode");
		if (code != null) {
			String[] itemCode = code.split("\\.");
			if (itemCode != null && itemCode.length > 1) {
				if (dataMap != null && !dataMap.isEmpty()) {
					List<Map<String, String>> models = dataMap.get(itemCode[0] + "_list");
					if (models != null && models.size() > 0) {
						return models.get(0).get(itemCode[1]);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 根据选项脚本和值，得到选项的html代码
	 *
	 * @param ret
	 * @param value
	 * @param markerText
	 * @return
	 */
	public static String getOptionText(String ret, String value, String markerText) {
		StringBuilder sb = new StringBuilder("");
		if (markerText != null && markerText.contains("<option")) {
			sb.append(MessageFormat.format(markerText, "", "", ""));
		}
		if (StringUtils.isBlank(value)) {
			value = "";
		}
		String[] optionStr = null;
		if (StringUtils.isNotBlank(ret)) {
			optionStr = ret.split("\\;");
		}
		String[] values = value.split(";");
		if (optionStr != null && optionStr.length > 0) {
			for (String str : optionStr) {
				String[] entry = str.split("\\=");

				if (entry != null && entry.length == 2) {
					if (Arrays.binarySearch(values, entry[0]) > -1) {
						sb.append(MessageFormat.format(markerText, entry[0],
								"checked=\"checked\" selected=\"selected\"", entry[1]));
					} else {
						logger.debug(str);
						sb.append(MessageFormat.format(markerText, entry[0], "", entry[1]));
					}
				}
			}
			return sb.toString();
		} else {
			return sb.toString();
		}
	}

	private static String getDataItem(String dataSource, Map<String, List<Map<String, String>>> dataMap) {
		if (StringUtils.isNotBlank(dataSource)) {
			String[] arr = dataSource.split("\\.");
			if (arr.length == 2) {
				String container = arr[0];
				String item = arr[1];
				List<Map<String, String>> list = dataMap.get(container + "_list");
				if (list != null && list.size() > 0) {
					return list.get(0).get(item);
				}
			}
		}
		return "";
	}

	public static void calculateCompents(DocumentVO docVo, Map<String, String> others, Map<String, JSONObject> status,
			List<JSONObject> components, Map<String, List<Map<String, String>>> dataMap, ScriptEngine engine,
			String isRead) throws ScriptException {
		boolean isCH = ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE;
		if (components != null && components.size() > 0) {
			for (int i = 0; i < components.size(); i++) {
				JSONObject component = components.get(i);
				if (component != null) {
					JSONObject o = new JSONObject();
					String value = DocUtils.getComponentValue(component, dataMap, engine, docVo);
					int type = component.getIntValue("componentType");
					String zhTitle = component.getString("title");
					String enTitle = component.getString("enTitle");
					if (isCH) {
						others.put("title_" + component.getString("name"), zhTitle);
					} else {
						others.put("title_" + component.getString("name"), enTitle);
					}
					String optionsText = null;
					switch (type) {
					case 1010: // 计算值、禁用
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						break;
					case 1008:// 列框
						// if (pageVO.getPageType() == 1003) {
						String showScript = component.getString("showScript");
						if (StringUtils.isNotBlank(showScript)) {
							Boolean rtn = Boolean.parseBoolean(engine.eval(showScript) + "");
							if (rtn) {
								break;
							} else {
								// 报错
							}
						}
						// }
						// 计算列框的disabled值
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						// 下面三个 计算值、隐藏
					case 1014:
					case 1015:
					case 1009:

						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						break;
					case 1017:

						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						break;
					case 1006:

						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));

						o.put("readonly",
								String.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						String script = component.getString("optionScript");
						String ret = "";
						String markerText = "";
						if (StringUtils.isNotBlank(script)) {
							ret = (String) engine.eval(script);
						}
						markerText = "<option value=\"{0}\" {1}>{2}</option>";
						optionsText = DocUtils.getOptionText(ret, value, markerText);
						others.put("options_" + component.getString("name"), ret);
						others.put(component.getString("name"), optionsText);
						break;
					case 1003:
						ret = "";

						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						o.put("readonly",
								String.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						script = component.getString("optionScript");
						if (StringUtils.isNotBlank(script)) {
							ret = (String) engine.eval(script);
						}

						markerText = "<label class='radio-inline'><input type='checkbox' name='"
								+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";

						optionsText = DocUtils.getOptionText(ret, value, markerText);
						others.put(component.getString("name"), optionsText);
						break;
					case 1004:
						ret = "";
						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						o.put("readonly",
								String.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						script = component.getString("optionScript");
						if (StringUtils.isNotBlank(script)) {
							ret = (String) engine.eval(script);
						}

						markerText = "<label class='radio-inline'><input type='radio' name='"
								+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";

						optionsText = DocUtils.getOptionText(ret, value, markerText);
						others.put(component.getString("name"), optionsText);
						break;
					case 1034:
						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						o.put("readonly",
								String.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						script = component.getString("tab_url");
						Object val = (String) engine.eval(script);
						others.put(component.getString("name"), val + "");
						break;
					case 1041:
						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						o.put("readonly",
								String.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						script = component.getString("dataSource");
						Object vale = (String) engine.eval(script);
						others.put(component.getString("name"), vale + "");
						break;
					case 1042:
						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						o.put("readonly",
								String.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						script = component.getString("tableDataSources");
						Object vale2 = (String) engine.eval(script);
						others.put(component.getString("name"), vale2 + "");
						break;
					case 1102:
						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						o.put("readonly",
								String.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						String extra = component.getString("extra");
						List<String> valueList = new ArrayList<String>();
						List<String> nameList = new ArrayList<String>();
						for (String str : extra.split(",")) {
							valueList.add(getDataItem(str, dataMap));
						}
						if (valueList.size() == 3) {
							String sql = "select name from p_attr_province where code=? union all "
									+ "select name from p_attr_city where code=? union all "
									+ "select name from p_attr_area where code=?";
							JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
							nameList = jdbcTemplate.queryForList(sql, String.class, valueList.toArray());
						}
						others.put("pcaValue_" + component.getString("name"), getListStr(valueList));
						others.put("pcaName_" + component.getString("name"), getListStr(nameList));
						break;
					// 下面几种，只需执行同一段代码，所以没有break;计算值、隐藏、只读、禁用
					case 1001:
					case 1002:
					case 1005:
					case 1007:
					case 1011:
					case 1012:
					case 1013:
					case 1016:
					case 1020:
					case 1019:
					case 1032:
					case 1033:
					case 1035:
					case 1036:
					case 1037:
					case 1101:
						o.put("hidden",
								String.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
						o.put("readonly",
								String.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
						o.put("disabled",
								String.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
					default:
						break;
					}
					if ("1".equals(isRead) && type!=1010) {
						o.put("disabled", "true");
					}
					status.put(component.getString("name"), o);

				}
			}
		}
	}

	private static String getListStr(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String str : list) {
			if (StringUtils.isNotBlank(str)) {
				sb.append(str + ",");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static void calculateStores(Map<String, String> map, List<StoreVO> stores,
			Map<String, Map<String, Object>> pageActStatus, ScriptEngine engine, Map<String, String> others,
			String isRead) throws ScriptException {
		boolean isCH = ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE;
		if (stores != null && stores.size() > 0) {
			for (StoreVO store : stores) {
				Map<String, Object> actState = new HashMap<String, Object>();
				PageAct act = JSON.parseObject(store.getContent(), PageAct.class);
				String hiddenScript = StringEscapeUtils.unescapeHtml4(act.getHiddenScript());
				Boolean chooseValidate = act.isChooseValidate();
				String chooseScript = StringEscapeUtils.unescapeHtml4(act.getChooseScript());
				if (act.getActType() == 2002) {// 初始化返回按钮的返回页面ID
					actState.put("backId", map.get("backId"));
				}
				// 执行隐藏脚本
				Boolean hiddenStatus = (Boolean) engine.eval(hiddenScript);
				String actName = act.getName();
				if("1".equals(isRead) && !"返回".equals(actName) && actName.indexOf("导出")==-1 
						&& !"关闭".equals(actName) && !"打印".equals(actName)) {
					hiddenStatus = true;
				}
				actState.put("chooseValidate", chooseValidate);
				if (chooseValidate != null) {
					Double chooseNum = (Double) engine.eval(chooseScript);
					actState.put("chooseNum", chooseNum);
				}
				if (isCH) {
					others.put("title_" + act.getPageId(), act.getName());
				} else {
					others.put("title_" + act.getPageId(), act.getEnName());
				}
				actState.put("hidden", hiddenStatus);
				pageActStatus.put(act.getPageId(), actState);
			}
		}
	}
}
