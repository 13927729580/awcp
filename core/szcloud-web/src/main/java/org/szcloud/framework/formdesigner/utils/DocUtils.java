package org.szcloud.framework.formdesigner.utils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.formdesigner.application.vo.AuthorityCompoentVO;
import org.szcloud.framework.formdesigner.application.vo.AuthorityGroupWorkFlowNodeVO;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.application.vo.StoreVO;
import org.szcloud.framework.formdesigner.application.vo.WorkflowNodeVO;
import org.szcloud.framework.formdesigner.application.vo.WorkflowVariableVO;
import org.szcloud.framework.formdesigner.core.domain.design.context.act.PageAct;
import org.szcloud.framework.formdesigner.service.AuthorityCompoentServiceImpl;
import org.szcloud.framework.formdesigner.service.AuthorityGroupWorkFlowNodeServiceImpl;
import org.szcloud.framework.formdesigner.service.FormdesignerServiceImpl;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.workflow.core.entity.CWorkItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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

	public static void flowSendDataConvert(CWorkItem lo_Item, DocumentUtils du, int formToFlow, DocumentVO docVo)
			throws Exception {

		FormdesignerServiceImpl dynamicPageService = Springfactory.getBean("formdesignerServiceImpl");
		String nodeId = "";
		if (lo_Item.CurActivity != null && lo_Item.CurFlow != null) {
			nodeId = String.valueOf(lo_Item.CurActivity.ActivityID) + "_" + String.valueOf(lo_Item.CurFlow.FlowID);
		}

		// 查出节点对应的参数列表
		DynamicPageVO dVo = dynamicPageService.findById(Long.parseLong(docVo.getDynamicPageId()));
		WorkflowNodeVO nvo = null;
		if (dVo != null) {
			String jsonStr = dVo.getWorkflowNodeInfo();
			JSONObject o = StringUtils.isNotEmpty(jsonStr) ? JSON.parseObject(jsonStr) : new JSONObject();
			String nodeString = o.getString(nodeId);
			if (StringUtils.isNotBlank(nodeString)) {
				nvo = JSON.parseObject(nodeString, WorkflowNodeVO.class);
				for (WorkflowVariableVO vo : nvo.getVariables()) {
					/// map.put(vo.getVariableName(),vo.getValue());
					// lo_Item.setPropValue(vo.getV ariableName(), "");
					if (formToFlow == DocUtils.FormToFlow)
						lo_Item.setPropValue(vo.getVariableName(),
								du.getDataItem(vo.getValue().split(".")[0], vo.getValue().split(".")[1]));
					if (formToFlow == DocUtils.FlowToForm)
						du.setDataItem(vo.getValue().split(".")[0], vo.getValue().split(".")[1],
								lo_Item.getPropValue(vo.getVariableName()));
				}
			}
		}

		// lo_Item.setPropValue("", "");

		// return map;
	}

	// 解析组件权限
	public static Map<String, JSONObject> flowAuthorityResolve(CWorkItem lo_Item1, DocumentVO docVo,
			Map<String, JSONObject> map) {

		AuthorityCompoentServiceImpl authorityCompoentService = Springfactory.getBean("authorityCompoentServiceImpl");
		AuthorityGroupWorkFlowNodeServiceImpl authorityGroupWorkFlowNodeService = Springfactory
				.getBean("authorityGroupWorkFlowNodeServiceImpl");
		// if(lo_Item.CurActivity!=null){
		int nodes = Integer.valueOf(docVo.getEntryId());
		// String did="1446";
		// boolean workItemAccess=false;
		// int nodes=1;
		Map<String, JSONObject> status = new HashMap<String, JSONObject>();
		Map<String, Object> resParams = new HashMap<String, Object>();
		resParams.put("dynamicPageId", docVo.getDynamicPageId());
		resParams.put("flowNode", nodes);
		List<AuthorityGroupWorkFlowNodeVO> flvo = authorityGroupWorkFlowNodeService.queryResult("eqQueryList",
				resParams);
		for (AuthorityGroupWorkFlowNodeVO ao : flvo) {
			resParams.clear();
			resParams.put("authorityGroupId", ao.getAuthorityGroup());
			List<AuthorityCompoentVO> resoVOs = authorityCompoentService.queryResult("eqQueryList", resParams);
			JSONObject jcon = new JSONObject();
			// if(true){
			// for (AuthorityCompoentVO vo : resoVOs){
			// JSONObject o = new JSONObject();
			// jcon.clear();
			// jcon.put("relatePageId",docVo.getDynamicPageId());
			// jcon.put("name",vo.getComponentId());
			// jcon.put("componentType", "");
			// List<JSONObject> components =
			// dynamicPageService.getComponentByContainerWithColumn(jcon);
			// if(components!=null){
			// //JSONObject component = components.get(0);
			//
			// Set<String> key=status.keySet();
			// if(!key.contains(vo.getComponentId())){
			// o.put("readonly","true");
			// o.put("disabled", "true");
			// status.put(vo.getComponentId(), o);
			// }
			//
			// }
			// }
			// }else{

			for (AuthorityCompoentVO vo : resoVOs) {
				if (vo.getAuthorityValue() != null) {
					int type = Integer.parseInt(vo.getAuthorityValue());
					JSONObject o = new JSONObject();
					jcon.clear();
					jcon.put("relatePageId", docVo.getDynamicPageId());
					jcon.put("name", vo.getComponentId());
					jcon.put("componentType", "");
					// List<JSONObject> components =
					// dynamicPageService.getComponentByContainerWithColumn(jcon);
					// JSONObject component = components.get(0);
					Set<String> key = status.keySet();
					switch (type) {
					case 10:
						if (key.contains(vo.getComponentId())) {

							if (status.get(vo.getComponentId()).get("modify") == null) {
								status.remove(vo.getComponentId());
								o.put("readonly", "true");
								o.put("disabled", "true");
								status.put(vo.getComponentId(), o);

							}

						} else {

							o.put("readonly", "true");
							o.put("disabled", "true");
							status.put(vo.getComponentId(), o);
						}
						break;
					case 11:

						if (key.contains(vo.getComponentId())) {
							status.remove(vo.getComponentId());
							o.put("modify", "true");
						} else {
							o.put("modify", "true");
						}
						status.put(vo.getComponentId(), o);
						break;
					case 12:
						if (key.contains(vo.getComponentId())) {

							if (status.get(vo.getComponentId()).get("modify") != null) {
								status.remove(vo.getComponentId());
								o.put("modify", "true");
							} else if (status.get(vo.getComponentId()).get("readonly") != null) {
								status.remove(vo.getComponentId());
								o.put("readonly", "true");
								o.put("disabled", "true");
							} else {
								status.remove(vo.getComponentId());
								o.put("hidden", "true");
							}

						} else {

							o.put("hidden", "true");
						}
						status.put(vo.getComponentId(), o);
						break;
					default:
						break;
					}
				}

			}
		}
		// }

		Set<String> st = map.keySet();
		for (String s : st) {

			if (status.containsKey(s)) {
				map.put(s, status.get(s));
			}

		}
		// }
		return map;
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

	public static void calculateCompents(DocumentVO docVo, Map<String, String> others, Map<String, JSONObject> status,
			List<JSONObject> components, Map<String, List<Map<String, String>>> dataMap, ScriptEngine engine)
			throws ScriptException {
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
						// o.put("optionText", optionsText);
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
					case 1029:
					case 1030:
					case 1031:
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

					status.put(component.getString("name"), o);

				}
			}
		}
	}

	public static void calculateStores(Map<String, String> map, List<StoreVO> stores,
			Map<String, Map<String, Object>> pageActStatus, ScriptEngine engine, Map<String, String> others)
			throws ScriptException {
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
