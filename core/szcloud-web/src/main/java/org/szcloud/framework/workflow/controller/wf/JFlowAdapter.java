package org.szcloud.framework.workflow.controller.wf;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.szcloud.framework.core.utils.DesUtils;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.utils.DocumentUtils;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.controller.base.ControllerHelper;

import BP.Port.WebUser;
import BP.WF.Dev2Interface;
import BP.WF.SendReturnObj;
import BP.WF.SendReturnObjs;
import BP.WF.Template.Node;
import BP.WF.Template.PubLib.AskforHelpSta;

public class JFlowAdapter {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(JFlowAdapter.class);

	/**
	 * 发送
	 * 
	 * @param masterDataSource
	 * @param utils
	 * @param user
	 * @param resultMap
	 * @param docVo
	 * @param fk_flow
	 * @param workID
	 * @param fk_node
	 * @throws Exception
	 */
	public static void Node_SendWork(String masterDataSource, DocumentUtils utils, PunUserBaseInfoVO user,
			Map resultMap, DocumentVO docVo, String fk_flow, String workID, String fk_node) throws Exception {
		resultMap.put("act", 1);
		Node currND = new Node(fk_node);
		if (!currND.getIsStartNode() && Dev2Interface.Flow_IsCanDoCurrentWork(fk_flow, Integer.parseInt(fk_node),
				Long.parseLong(workID), WebUser.getNo()) == false) {
			resultMap.put("success", true);
			resultMap.put("message",
					"@您好：" + WebUser.getNo() + "," + WebUser.getName() + "：<br> 当前工作已经被其它人处理，您不能再执行保存或者发送动作!!!");
			return;
		}
		Hashtable table = convertHashtable(masterDataSource, utils);
		String title = null;
		if (table.get("title") != null) {
			title = (String) table.get("title");
		}
		// SendReturnObjs result = Dev2Interface.Node_SendWork(fk_flow,
		// Long.valueOf(workID),
		// convertHashtable(masterDataSource, utils));
		SendReturnObjs result = Dev2Interface.Node_SendWork(fk_flow, Long.parseLong(workID), table, null, 0, null,
				WebUser.getNo(), WebUser.getName(), WebUser.getFK_Dept(), WebUser.getFK_DeptName(), title);

		boolean flag = saveExecuteData(utils, docVo, masterDataSource);
		if (flag) {
			resultMap.put("success", true);

			resultMap.put("message", genarateFlowResultInfo(1, result));
		} else {
			resultMap.put("success", false);
			resultMap.put("message", "表单数据保存失败");
		}
		// SendReturnObjs.ToMsgOfHtml();

	}

	private static void exceptionThrow() {
		int i = 0;
		logger.debug(4 / i + "");
	}

	public static void Flow_DoFlowOverByCoercion(Map resultMap, String flowNo, int nodeid, long workID, long fid,
			String msg) {
		try {
			resultMap.put("act", 1);
			resultMap.put("message", Dev2Interface.Flow_DoFlowOverByCoercion(flowNo, nodeid, workID, fid, msg));
			resultMap.put("success", true);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("message", e.getMessage());
		}
	}

	private static String jqString(String sourceString, String startString, String endString, int last) {
		if (endString.equals("")) {
			int start = sourceString.indexOf(startString) + startString.length();
			int end = start + last;
			return sourceString.substring(start, end);
		} else {
			int start = sourceString.indexOf(startString) + startString.length();
			int end = sourceString.indexOf(endString);
			return sourceString.substring(start, end);
		}
	}

	private static void convertSendReturnObjs(SendReturnObjs result) {
		for (SendReturnObj obj : result) {
			if (obj.MsgFlag.equals("AllotTask")) {

			}

			if (obj.MsgFlag.equals("UnDoNew")) {
				String[] rc = obj.MsgOfText.split("，");
				String WorkID = "\"" + jqString(rc[0], "DoType=UnSend&WorkID=", "&FK_Flow=", 0) + "\"";
				String FK_Flow = "\"" + jqString(rc[0], "&FK_Flow=", "", 3) + "\"";
				String ss = "@<a href='javascript:rollbackFlowAlert(" + FK_Flow + "," + WorkID + ");'><img src='"
						+ ControllerHelper.getBasePath() + "WF/Img/UnDo.gif' border=0/>Undo this send.</a>";
				obj.MsgOfText = ss + "，" + rc[1];

			}
		}
	}

	public static void Flow_DoUnSend(Map resultMap, String flowNo, String workID) throws Exception {
		try {
			String str = Dev2Interface.Flow_DoUnSend(flowNo, Long.valueOf(workID));
			resultMap.put("message", "撤回成功，您可以在代办里找到此业务!");
			resultMap.put("success", true);

		} catch (Exception e) {

			resultMap.put("success", false);
			resultMap.put("message", e.getMessage());
			throw e;
		}
	}

	public static void Flow_returnWork(Map resultMap, String flowNo, int nodeid, long workID, long fid, String msg,
			String userId, String toNode, String masterDataSource, DocumentUtils utils, DocumentVO docVo)
			throws Exception {
		try {
			resultMap.put("act", 1);
			/**
			 * 查找上一个节点的NODEID
			 */
			JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
			int flowId = Integer.parseInt(flowNo);
			Map<String, Object> map = null;
			int NDForm = 0;
			String returnEmp = null;
			if (toNode != null && !toNode.equals("") && Integer.parseInt(toNode) != 0) {
				NDForm = Integer.parseInt(toNode);
			} else {
				try {
					String sql = "select distinct NDFrom,EmpFrom from ND" + flowId + "Track where (WorkID=" + workID
							+ " or FID=" + workID + " or WorkID=" + fid + ") and EmpTo='" + userId + "' and NDTo="
							+ nodeid + " and (ActionType=1 or ActionType=27)";
					map = jdbcTemplate.queryForMap(sql);
					NDForm = Integer.parseInt(map.get("NDFrom").toString());
					returnEmp = map.get("EmpFrom").toString();
				} catch (Exception e) {
					PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils
							.getObjectFromSession(SessionContants.CURRENT_USER);
					String sql = "select distinct NDFrom,EmpFrom from ND" + flowId + "Track where (WorkID=" + workID
							+ " or FID=" + workID + " or WorkID=" + fid + ") and NDTo=" + nodeid + " and ActionType=1";
					try {
						String sqls = sql + " AND EMPFROM=(select SUBSTRING_INDEX(LEFT(substring_index(EMPS,'"
								+ user.getUserName() + "@',1), LENGTH(substring_index(EMPS,'" + user.getUserName()
								+ "@',1)) - LOCATE('@', REVERSE(substring_index(EMPS,'" + user.getUserName()
								+ "@',1)))), '@', -1) from wf_generworkflow where WorkID=" + workID + ")";
						map = jdbcTemplate.queryForMap(sqls);
					} catch (Exception ex) {
						map = jdbcTemplate.queryForMap(sql);
					}

					NDForm = Integer.parseInt(map.get("NDFrom").toString());
					returnEmp = map.get("EmpFrom").toString();
				}

			}
			resultMap.put("message",
					Dev2Interface.Node_ReturnWork(flowNo, workID, fid, nodeid, NDForm, returnEmp, msg, true));
			boolean flag = saveExecuteData(utils, docVo, masterDataSource);
			resultMap.put("success", true);

		} catch (Exception e) {
			resultMap.put("message", e.getMessage());
			resultMap.put("success", false);
			throw e;
		}
	}

	public static String genarateFlowResultInfo(int type, SendReturnObjs result) {
		convertSendReturnObjs(result);
		return "<style>.ui-dialog-body{overflow-x:hidden;overflow-y:auto;}</style>" + "<p style-\"margin-top:0px;\">"
				+ result.ToMsgOfHtmlSz().replace("@", "<br>") + "</p>";
	}

	/**
	 * 转发
	 * 
	 * @param masterDataSource
	 * @param utils
	 * @param user
	 * @param resultMap
	 * @param docVo
	 * @param fk_flow
	 * @param workID
	 * @param fk_node
	 * @throws Exception
	 */
	public static void Node_SendWork(String masterDataSource, DocumentUtils utils, PunUserBaseInfoVO user,
			Map resultMap, DocumentVO docVo, String fk_flow, String workID, String fk_node, String toUsers)
			throws Exception {
		resultMap.put("act", 1);
		Node currND = new Node(fk_node);
		if (!currND.getIsStartNode() && Dev2Interface.Flow_IsCanDoCurrentWork(fk_flow, Integer.parseInt(fk_node),
				Long.parseLong(workID), WebUser.getNo()) == false) {
			resultMap.put("success", true);
			resultMap.put("message",
					"@您好：" + WebUser.getNo() + "," + WebUser.getName() + "：<br> 当前工作已经被其它人处理，您不能再执行保存或者发送动作!!!");
			return;
		}
		Hashtable table = convertHashtable(masterDataSource, utils);
		String title = null;
		if (table.get("title") != null) {
			title = (String) table.get("title");
		}
		SendReturnObjs result = Dev2Interface.Node_SendWork(fk_flow, Long.parseLong(workID), table, null, 0, toUsers,
				WebUser.getNo(), WebUser.getName(), WebUser.getFK_Dept(), WebUser.getFK_DeptName(), title);
		// exceptionThrow();
		boolean flag = saveExecuteData(utils, docVo, masterDataSource);
		if (flag) {
			resultMap.put("success", true);
			// logger.debug("原始的字符串为:"+result.ToMsgOfHtml());
			String str = result.ToMsgOfHtml();

			resultMap.put("message", genarateFlowResultInfo(1, result));
		} else {
			resultMap.put("success", false);
			resultMap.put("message", "@表单数据保存失败");
		}

	}

	/**
	 * 加签
	 * 
	 * @param masterDataSource
	 * @param utils
	 * @param user
	 * @param resultMap
	 * @param docVo
	 * @param fk_flow
	 * @param workID
	 * @param fk_node
	 * @param toUsers
	 * @throws Exception
	 */
	public static void Node_AskFor(String masterDataSource, DocumentUtils utils, PunUserBaseInfoVO user, Map resultMap,
			DocumentVO docVo, String fk_flow, String workID, String fk_node, String toUsers) throws Exception {
		resultMap.put("act", 1);
		Node currND = new Node(fk_node);
		if (!currND.getIsStartNode() && Dev2Interface.Flow_IsCanDoCurrentWork(fk_flow, Integer.parseInt(fk_node),
				Long.parseLong(workID), WebUser.getNo()) == false) {
			resultMap.put("success", true);
			resultMap.put("message",
					"@您好：" + WebUser.getNo() + "," + WebUser.getName() + "：<br> 当前工作已经被其它人处理，您不能再执行保存或者发送动作!!!");
			return;
		}
		String result = Dev2Interface.Node_Askfor(Long.parseLong(workID), AskforHelpSta.AfterDealSend, toUsers,
				fk_node);
		boolean flag = saveExecuteData(utils, docVo, masterDataSource);
		if (flag) {
			resultMap.put("success", true);

			DesUtils des = new DesUtils("leemenz");
			resultMap.put("message", result);
		} else {
			resultMap.put("success", false);
			resultMap.put("message", "表单数据保存失败");
		}
	}

	/**
	 * 移交
	 * 
	 * @param masterDataSource
	 * @param utils
	 * @param user
	 * @param resultMap
	 * @param docVo
	 * @param fk_flow
	 * @param workID
	 * @param fk_node
	 * @param toUsers
	 * @throws Exception
	 */
	public static void Node_Shift(String masterDataSource, DocumentUtils utils, PunUserBaseInfoVO user, Map resultMap,
			DocumentVO docVo, String fk_flow, String workID, String fk_node, String fid, String toUsers)
			throws Exception {
		resultMap.put("act", 1);
		Node currND = new Node(fk_node);
		if (!currND.getIsStartNode() && Dev2Interface.Flow_IsCanDoCurrentWork(fk_flow, Integer.parseInt(fk_node),
				Long.parseLong(workID), WebUser.getNo()) == false) {
			resultMap.put("success", true);
			resultMap.put("message",
					"@您好：" + WebUser.getNo() + "," + WebUser.getName() + "：<br> 当前工作已经被其它人处理，您不能再执行保存或者发送动作!!!");
			return;
		}
		String result = Dev2Interface.Node_Shift(fk_flow, Integer.parseInt(fk_node), Long.parseLong(workID),
				Long.parseLong(fid), toUsers, "");
		boolean flag = saveExecuteData(utils, docVo, masterDataSource);
		if (flag) {
			resultMap.put("success", true);

			DesUtils des = new DesUtils("leemenz");
			resultMap.put("message", result);
		} else {
			resultMap.put("success", false);
			resultMap.put("message", "表单数据保存失败");
		}
	}

	private static Hashtable convertHashtable(String masterDataSource, DocumentUtils utils) {
		List<Map<String, String>> wt = utils.getDataContainer(masterDataSource);
		if (wt == null)
			return null;
		Map<String, String> tt = wt.get(0);
		Hashtable ht = new Hashtable();
		for (Map.Entry<String, String> entry : tt.entrySet()) {
			ht.put(entry.getKey().toLowerCase(), entry.getValue());
		}
		return ht;
	}

	public static void Node_SaveWork(String masterDataSource, DocumentUtils utils, PunUserBaseInfoVO user,
			Map resultMap, DocumentVO docVo, String fk_flow, String fk_node, String workID) throws Exception {
		resultMap.put("act", 0);
		Hashtable table = convertHashtable(masterDataSource, utils);
		String result = Dev2Interface.Node_SaveWork(fk_flow, Integer.valueOf(fk_node), Long.valueOf(workID), table);

		if (!result.startsWith("保存失败")) {
			docVo.setFlowTempleteId(fk_flow);
			docVo.setWorkItemId(workID);
			docVo.setEntryId(String.valueOf(fk_node));

			// mv.setViewName("redirect:/jflow/wf/listPersonalTasks.do");
			if (user.getUserId() != null) {
				docVo.setLastmodifier(String.valueOf(user.getUserId()));
				docVo.setAuditUser(String.valueOf(user.getUserId()));
			}
			// 设置doc 记录为草稿状态
			docVo.setState("草稿");
			boolean flag = false;
			if (docVo.isUpdate()) {
				// 更新数据
				flag = saveExecuteData(utils, docVo, masterDataSource);
			} else {
				// 创建时间
				docVo.setCreated(new Date(System.currentTimeMillis()));
				docVo.setAuthorId(user.getUserId());
				flag = saveExecuteData(utils, docVo, masterDataSource);
			}
			resultMap.put("success", true);
			resultMap.put("message", "Save successful！");
			resultMap.put("WorkItemID", workID);
			resultMap.put("EntryID", fk_node);
		} else {
			resultMap.put("success", false);
			resultMap.put("message", "Save failed！");
			throw new Exception(result);
		}

	}

	// 设置附加数据保存到流程
	private static Map setWorkflowExtData(DocumentVO docVo, String masterDataSource) {
		Map<String, Object> extMap = new HashMap<String, Object>();
		for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Map<String, String> data = null;
			if (docVo.getListParams().size() != 1) {
				data = docVo.getListParams().get(masterDataSource).get(0);
			} else {
				data = docVo.getListParams().get(key).get(0);
			}
			if (data != null && !data.isEmpty()) {
				if (data.containsKey("BWSX")) {
					extMap.put("4", data.get("BWSX"));
				}
				if (data.containsKey("JJCD")) {
					extMap.put("5", data.get("JJCD"));
				}
				if (data.containsKey("XTBH")) {
					extMap.put("6", data.get("XTBH"));
				}
			}
		}
		return extMap;
	}

	public static boolean saveExecuteData(DocumentUtils utils, DocumentVO docVo, String masterDataSource)
			throws Exception {
		// 更新数据
		String recordId = "";
		Map resultMap = null;
		for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
			String o = it.next();

			// if(temp.equals("false"))s
			// return false;
			if (StringUtils.equals(o, masterDataSource)) {
				resultMap = utils.saveDataFlow(o, true);
				recordId = resultMap.get("id").toString();
			} else {
				resultMap = utils.saveDataFlow(o, false);
			}
		}
		// 更新document 记录
		// 最后修改时间
		docVo.setLastmodified(docVo.getCreated());
		if (StringUtils.isNotEmpty(recordId)) {
			utils.getCurrentDocument().setRecordId(recordId);
		}
		utils.saveDocument();
		return true;
	}

	// public void Send(boolean isSave,String fk_node,String workID,String
	// fk_flow,String fid) throws Exception
	// {
	// Node currND = new Node(fk_node);
	// Work currWK = currND.getHisWork();
	// currWK.SetValByKey("OID", workID);
	// currWK.Retrieve();
	//
	// // 判断当前人员是否有执行该人员的权限。
	// if (!currND.getIsStartNode() &&
	// Dev2Interface.Flow_IsCanDoCurrentWork(fk_flow, Integer.parseInt(fk_node),
	// Long.parseLong(workID), WebUser.getNo()) == false)
	// throw new Exception("您好：" + WebUser.getNo() + "," + WebUser.getName() +
	// "：<br> 当前工作已经被其它人处理，您不能再执行保存或者发送动作!!!");
	//
	// Paras ps = new Paras();
	// //String dtStr = SystemConfig.getAppCenterDBVarStr();
	//
	// // region 判断特殊的业务逻辑
	// String dbStr = SystemConfig.getAppCenterDBVarStr();
	// if (currND.getIsStartNode())
	// {
	// if (currND.getHisFlow().getHisFlowAppType() == FlowAppType.PRJ)
	// {
	// /*对特殊的流程进行检查，检查是否有权限。*/
	// String prjNo = currWK.GetValStringByKey("PrjNo");
	// ps = new Paras();
	// ps.SQL = "SELECT * FROM WF_NodeStation WHERE FK_Station IN ( SELECT
	// FK_Station FROM Prj_EmpPrjStation WHERE FK_Prj=" + dbStr + "FK_Prj AND
	// FK_Emp=" + dbStr + "FK_Emp ) AND FK_Node=" + dbStr + "FK_Node ";
	// ps.Add("FK_Prj", prjNo);
	// ps.AddFK_Emp();
	// ps.Add("FK_Node", fk_node);
	//
	// if (DBAccess.RunSQLReturnTable(ps).Rows.size() == 0)
	// {
	// String prjName = currWK.GetValStringByKey("PrjName");
	// ps = new Paras();
	// ps.SQL = "SELECT * FROM Prj_EmpPrj WHERE FK_Prj=" + dbStr + "FK_Prj AND
	// FK_Emp=" + dbStr + "FK_Emp ";
	// ps.Add("FK_Prj", prjNo);
	// ps.AddFK_Emp();
	// // ps.AddFK_Emp();
	//
	// if (DBAccess.RunSQLReturnTable(ps).Rows.size() == 0){
	// throw new Exception("您不是(" + prjNo + "," + prjName + ")成员，您不能发起改流程。");
	// }else{
	// throw new Exception("您属于这个项目(" + prjNo + "," + prjName +
	// ")，但是在此项目下您没有发起改流程的岗位");
	// }
	// }
	// }
	// }
	// // #endregion 判断特殊的业务逻辑。
	// currWK.SetValByKey("Rec", WebUser.getNo());
	// currWK.SetValByKey("FK_Dept", WebUser.getFK_Dept());
	// currWK.SetValByKey("FK_NY", BP.DA.DataType.getCurrentYearMonth());
	//
	//
	//
	//
	// if (Glo.getIsEnableDraft() && currND.getIsStartNode())
	// {
	// /*如果启用草稿, 并且是开始节点. */
	// BP.WF.Dev2Interface.Node_SaveWork(fk_flow, Integer.parseInt(fk_node),
	// Long.parseLong(workID));
	// }
	//
	// String msg = "";
	// // 调用工作流程，处理节点信息采集后保存后的工作。
	//
	//// GenerWorkFlow gwf = new GenerWorkFlow(Long.parseLong(workID));
	//// //检查是否是退回?
	//// if (gwf.getWFState() == WFState.ReturnSta && gwf.getParas_IsTrackBack()
	// == false)
	//// {
	//// /* 如果是退回 */
	//// }
	//// else
	//// {
	// if (currND.getCondModel() == CondModel.ByUserSelected &&
	// currND.getHisToNDNum() > 1)
	// {
	// //如果是用户选择的方向条件.
	// // printResult(toJson("url",
	// Glo.getCCFlowAppPath()+"WF/WorkOpt/ToNodes.jsp?FK_Flow=" + fk_flow +
	// "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid));
	// return;
	// }
	//// }
	//
	// //执行发送.
	// WorkNode firstwn = new WorkNode(currWK, currND);
	// try
	// {
	// msg = firstwn.NodeSend().ToMsgOfHtml();
	// }
	// catch (Exception exSend)
	// {
	// String msge = StringHelper.isEmpty(exSend.getMessage(), "");
	// if (msge.contains("请选择下一步骤工作"))
	// {
	// /*如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员. */
	// String url = Glo.getCCFlowAppPath()+"WF/WorkOpt/ToNodes.jsp?FK_Flow=" +
	// fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid;
	// //printResult(toJson("url", url));
	// return;
	// }else{
	// exSend.logger.info("ERROR", e)();
	// }
	//
	//
	// StringBuffer errorMsg = new StringBuffer();
	// errorMsg.append(BaseModel.AddFieldSetGreen("错误"));
	// errorMsg.append(msge.replace("@@", "@").replace("@", "<BR>@"));
	// errorMsg.append(BaseModel.AddFieldSetEnd());
	//
	// //printResult(toJson("flowMsg", errorMsg.toString()));
	// return;
	// }
	//
	//
	// //this.Btn_Send.Enabled = false;
	// /*处理转向问题.*/
	// switch (firstwn.getHisNode().getHisTurnToDeal())
	// {
	// case SpecUrl:
	// String myurl = firstwn.getHisNode().getTurnToDealDoc();
	// if (myurl.contains("&") == false)
	// myurl += "?1=1";
	// Attrs myattrs = firstwn.getHisWork().getEnMap().getAttrs();
	// Work hisWK = firstwn.getHisWork();
	// for(Attr attr : Attrs.convertAttrs(myattrs))
	// {
	// if (myurl.contains("@") == false)
	// break;
	// myurl = myurl.replace("@" + attr.getKey(),
	// hisWK.GetValStrByKey(attr.getKey()));
	// }
	// if (myurl.contains("@")){
	// throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);
	// }
	// myurl += "&FromFlow=" + fk_flow + "&FromNode=" + fk_node + "&PWorkID=" +
	// workID + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
	// printResult(toJson("url", myurl));
	// return;
	//
	// case TurnToByCond:
	// TurnTos tts = new TurnTos(fk_flow);
	// if (tts.size() == 0){
	// throw new Exception("@您没有设置节点完成后的转向条件。");
	// }
	// for (TurnTo tt : TurnTos.convertTurnTos(tts))
	// {
	// tt.HisWork = firstwn.getHisWork();
	// if (tt.getIsPassed() == true)
	// {
	// String url = tt.getTurnToURL();
	// if (url.contains("&") == false)
	// url += "?1=1";
	// Attrs attrs = firstwn.getHisWork().getEnMap().getAttrs();
	// Work hisWK1 = firstwn.getHisWork();
	// for (Attr attr : Attrs.convertAttrs(attrs))
	// {
	// if (url.contains("@") == false)
	// break;
	// url = url.replace("@" + attr.getKey(),
	// hisWK1.GetValStrByKey(attr.getKey()));
	// }
	// if (url.contains("@")){
	// throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);
	// }
	//
	// url += "&PFlowNo=" + fk_flow + "&FromNode=" + fk_node + "&PWorkID=" +
	// workID + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
	// printResult(toJson("url", url));
	// return;
	// }
	// }
	//
	// //#warning 为上海修改了如果找不到路径就让它按系统的信息提示。
	// printResult(toJson("url", getToMsg(msg)));
	// //throw new Exception("您定义的转向条件不成立，没有出口。");
	// break;
	// default:
	// printResult(toJson("url", getToMsg(msg)));
	// break;
	// }
	// return;
	// }

}
