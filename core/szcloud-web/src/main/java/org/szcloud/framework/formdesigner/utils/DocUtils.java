package org.szcloud.framework.formdesigner.utils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.formdesigner.application.vo.AuthorityCompoentVO;
import org.szcloud.framework.formdesigner.application.vo.AuthorityGroupWorkFlowNodeVO;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.application.vo.WorkflowNodeVO;
import org.szcloud.framework.formdesigner.application.vo.WorkflowVariableVO;
import org.szcloud.framework.formdesigner.service.AuthorityCompoentServiceImpl;
import org.szcloud.framework.formdesigner.service.AuthorityGroupWorkFlowNodeServiceImpl;
import org.szcloud.framework.formdesigner.service.FormdesignerServiceImpl;
import org.szcloud.framework.workflow.core.entity.CWorkItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class DocUtils {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(DocUtils.class);
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
				e.printStackTrace();
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
				e.printStackTrace();
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
			ScriptEngine engine) {
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
			e.printStackTrace();
		}
		return null;
	}

	public static void flowSendDataConvert(CWorkItem lo_Item, DocumentUtils du, int formToFlow) throws Exception {

		FormdesignerServiceImpl dynamicPageService = Springfactory.getBean("formdesignerServiceImpl");
		Map<String, Object> map = new HashMap<String, Object>();
		String nodeId = "";
		if (lo_Item.CurActivity != null && lo_Item.CurFlow != null) {
			nodeId = String.valueOf(lo_Item.CurActivity.ActivityID) + "_" + String.valueOf(lo_Item.CurFlow.FlowID);
		}

		// 查出节点对应的参数列表
		DynamicPageVO dVo = dynamicPageService.findById(Long.parseLong(du.getCurrentDocument().getDynamicPageId()));
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
		FormdesignerServiceImpl dynamicPageService = Springfactory.getBean("formdesignerServiceImpl");
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

	/*
	 * 根据组件name获取特殊组件的状态
	 */
	public static Map<String, JSONObject> getStatus(CWorkItem lo_Item1, DocumentVO docVo, String componentName) {

		AuthorityCompoentServiceImpl authorityCompoentService = Springfactory.getBean("authorityCompoentServiceImpl");
		AuthorityGroupWorkFlowNodeServiceImpl authorityGroupWorkFlowNodeService = Springfactory
				.getBean("authorityGroupWorkFlowNodeServiceImpl");
		Map<String, JSONObject> status = new HashMap<String, JSONObject>();
		int nodes = Integer.valueOf(docVo.getEntryId());

		Map<String, Object> resParams = new HashMap<String, Object>();
		resParams.put("dynamicPageId", docVo.getDynamicPageId());
		resParams.put("flowNode", nodes);
		List<AuthorityGroupWorkFlowNodeVO> flvo = authorityGroupWorkFlowNodeService.queryResult("eqQueryList",
				resParams);
		for (AuthorityGroupWorkFlowNodeVO ao : flvo) {
			resParams.clear();
			resParams.put("authorityGroupId", ao.getAuthorityGroup());
			resParams.put("componentId", componentName);
			List<AuthorityCompoentVO> resoVOs = authorityCompoentService.queryResult("eqQueryList", resParams);
			// if(lo_Item.WorkItemAccess==false){
			// for (AuthorityCompoentVO vo : resoVOs){
			// JSONObject o = new JSONObject();
			// Set<String> key=status.keySet();
			// if(!key.contains(vo.getComponentId())){
			// o.put("readonly","true");
			// o.put("disabled", "true");
			// status.put(vo.getComponentId(), o);
			//
			// }
			// }
			// }else{

			for (AuthorityCompoentVO vo : resoVOs) {
				if (vo.getAuthorityValue() != null) {
					int type = Integer.parseInt(vo.getAuthorityValue());
					JSONObject o = new JSONObject();
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
					case 13:
						if (key.contains(vo.getComponentId())) {

							if (status.get(vo.getComponentId()).get("modify") == null) {
								status.remove(vo.getComponentId());
								o.put("disabled", "true");
								status.put(vo.getComponentId(), o);

							}

						} else {

							o.put("disabled", "true");
							status.put(vo.getComponentId(), o);
						}
						break;
					default:
						break;
					}
				}
			}
		}
		// }
		return status;
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

}
