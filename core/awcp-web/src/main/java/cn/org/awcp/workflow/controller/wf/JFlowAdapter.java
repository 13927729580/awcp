package cn.org.awcp.workflow.controller.wf;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Paras;
import BP.Port.Emp;
import BP.Sys.*;
import BP.Tools.DateUtils;
import BP.WF.*;
import BP.Web.WebUser;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.I18nKey;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;


public class JFlowAdapter {

	public static void Flow_DoFlowOverByCoercion(Map resultMap, String flowNo, int nodeid, long workID, long fid,
			String msg) {
		try {
			resultMap.put("act", 1);
			Dev2Interface.Flow_DoFlowOverByCoercion(flowNo, nodeid, workID, fid, msg);
			resultMap.put("success", true);
			resultMap.put("message", ControllerHelper.getMessage("wf_execute_complete"));
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_fail));
		}
	}

	public static void Flow_DoFlowOverByCoercion(Map resultMap, String flowNo, int nodeid, long workID, long fid,
			WFState state) {
		try {
			resultMap.put("act", 1);
			Dev2Interface.Flow_DoFlowOverByCoercion(flowNo, nodeid, workID, fid, state);
			resultMap.put("success", true);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success));
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_fail));
		}
	}

	/**
	 * 执行流程结束:强制的流程结束.
	 *
	 * @param flowNo
	 *            流程编号
	 * @param flowNo
	 *            当前节点编号
	 * @param workID
	 *            工作ID
	 * @param fid
	 *            工作ID
	 * @param state
	 *            流程状态
	 * @return 执行强制结束流程
	 */
	public static void Flow_DoFlowOverByCoercion(String flowNo, int nodeid, long workID, long fid, WFState state) {
		// 转化成编号.
		WorkFlow wf = new WorkFlow(flowNo, workID);

		Node currNode = new Node(nodeid);

		// 处理明细数据的copy问题。 首先检查：当前节点（最后节点）是否有明细表。
		MapDtls dtls = currNode.getMapData().getMapDtls(); // new MapDtls("ND" +
		int i = 0;
		for (MapDtl dtl : MapDtls.convertMapDtls(dtls)) {
			i++;
			// 查询出该明细表中的数据。
			GEDtls dtlDatas = new GEDtls(dtl.getNo());
			dtlDatas.Retrieve(GEDtlAttr.RefPK, wf.getWorkID());

			GEDtl geDtl = null;
			try {
				// 创建一个Rpt对象。
				geDtl = new GEDtl(
						"ND" + Integer.parseInt(wf.getHisFlow().getNo()) + "RptDtl" + (new Integer(i)).toString());
				geDtl.ResetDefaultVal();
			} catch (java.lang.Exception e) {
				/// #warning 此处需要修复。
				continue;
			}
		}
		wf._IsComplete = 1;
		/// #endregion 处理明细表的汇总.

		/// #region 处理后续的业务.

		String dbstr = BP.Sys.SystemConfig.getAppCenterDBVarStr();
		Paras ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerFH WHERE FID=" + dbstr + "FID";
		ps.Add(GenerFHAttr.FID, wf.getWorkID());
		DBAccess.RunSQL(ps);

		// 求出参与人,以方便已经完成的工作查询.
		ps = new Paras();
		ps.SQL = "SELECT EmpFrom FROM ND" + Integer.parseInt(wf.getHisFlow().getNo()) + "Track WHERE WorkID=" + dbstr
				+ "WorkID OR FID=" + dbstr + "FID ";
		ps.Add("WorkID", wf.getWorkID());
		ps.Add("FID", wf.getWorkID());
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(ps);
		String emps = "";
		for (DataRow dr : dt.Rows) {
			if (emps.contains("@" + dr.getValue(0).toString()) == true) {
				continue;
			}
			emps += "@" + dr.getValue(0).toString();
		}
		emps = emps + "@";

		// 更新流程注册信息.
		ps = new Paras();
		ps.SQL = "UPDATE WF_GenerWorkFlow SET WFState=" + dbstr + "WFState,WFSta=" + dbstr + "WFSta,Emps=" + dbstr
				+ "Emps,MyNum=1 WHERE WorkID=" + dbstr + "WorkID ";
		ps.Add("WFState", state.getValue());
		ps.Add("WFSta", WFSta.Complete.getValue());
		ps.Add("Emps", emps);
		ps.Add("WorkID", wf.getWorkID());
		DBAccess.RunSQL(ps);

		// 清除工作者.
		ps = new Paras();
		ps.SQL = "DELETE FROM WF_GenerWorkerlist WHERE WorkID=" + dbstr + "WorkID1 OR FID=" + dbstr + "WorkID2 ";
		ps.Add("WorkID1", wf.getWorkID());
		ps.Add("WorkID2", wf.getWorkID());
		DBAccess.RunSQL(ps);

		// 设置流程完成状态.
		ps = new Paras();
		ps.SQL = "UPDATE " + wf.getHisFlow().getPTable() + " SET WFState=" + dbstr + "WFState WHERE OID=" + dbstr
				+ "OID";
		ps.Add("WFState", WFState.Complete.getValue());
		ps.Add("OID", wf.getWorkID());
		DBAccess.RunSQL(ps);

		// 加入轨迹.
		WorkNode wn = new WorkNode(wf.getWorkID(), wf.getHisGenerWorkFlow().getFK_Node());
		wn.AddToTrack(ActionType.FlowOverByCoercion, WebUser.getNo(), WebUser.getName(), wn.getHisNode().getNodeID(),
				wn.getHisNode().getName(), "");

		/// #endregion 处理后续的业务.

		// string dbstr = BP.Sys.SystemConfig.AppCenterDBVarStr;

		/// #region 处理审核问题,更新审核组件插入的审核意见中的 到节点，到人员。
		ps = new Paras();
		ps.SQL = "UPDATE ND" + Integer.parseInt(currNode.getFK_Flow()) + "Track SET NDTo=" + dbstr + "NDTo,NDToT="
				+ dbstr + "NDToT,EmpTo=" + dbstr + "EmpTo,EmpToT=" + dbstr + "EmpToT WHERE NDFrom=" + dbstr
				+ "NDFrom AND EmpFrom=" + dbstr + "EmpFrom AND WorkID=" + dbstr + "WorkID AND ActionType="
				+ ActionType.WorkCheck.getValue();
		ps.Add(TrackAttr.NDTo, currNode.getNodeID());
		ps.Add(TrackAttr.NDToT, "");
		ps.Add(TrackAttr.EmpTo, "");
		ps.Add(TrackAttr.EmpToT, "");

		ps.Add(TrackAttr.NDFrom, currNode.getNodeID());
		ps.Add(TrackAttr.EmpFrom, WebUser.getNo());
		ps.Add(TrackAttr.WorkID, wf.getWorkID());
		BP.DA.DBAccess.RunSQL(ps);
	}

	public static void Flow_DoUnSend(Map resultMap, String flowNo, String workID) throws Exception {
		try {
			Dev2Interface.Flow_DoUnSend(flowNo, Long.valueOf(workID));
			resultMap.put("success", true);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success));

		} catch (Exception e) {

			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_fail));
		}
	}

	public static void Flow_returnWork(Map resultMap, long fid, String msg, String userId, String toNode,
			String masterDataSource, DocumentUtils utils, DocumentVO docVo,boolean isBackToThisNode) throws Exception {
		try {
			resultMap.put("act", 1);
			String flowNo = docVo.getFlowTempleteId();
			String nodeid = docVo.getEntryId();
			String workID = docVo.getWorkItemId();
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
								+ user.getUserIdCardNumber() + "@',1), LENGTH(substring_index(EMPS,'"
								+ user.getUserIdCardNumber() + "@',1)) - LOCATE('@', REVERSE(substring_index(EMPS,'"
								+ user.getUserIdCardNumber() + "@',1)))), '@', -1) from wf_generworkflow where WorkID="
								+ workID + ")";
						map = jdbcTemplate.queryForMap(sqls);
					} catch (Exception ex) {
						map = jdbcTemplate.queryForMap(sql);
					}

					NDForm = Integer.parseInt(map.get("NDFrom").toString());
					returnEmp = map.get("EmpFrom").toString();
				}

			}
			Dev2Interface.Node_ReturnWork(flowNo, Long.parseLong(workID), fid, Integer.parseInt(nodeid), NDForm,
					returnEmp, msg, isBackToThisNode);
			saveExecuteData(utils, docVo, masterDataSource);
			String nextExecutor = "";
			if(StringUtils.isNotBlank(returnEmp)){
				nextExecutor = "," + ControllerHelper.getMessage("wf_next_step_executor") + returnEmp;
			}	
			resultMap.put("message", ControllerHelper.getMessage("wf_send_return_success") + nextExecutor);
			resultMap.put("success", true);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_fail));
			throw e;
		}
	}

	/**
	 * 流程发送
	 * 
	 * @param masterDataSource
	 *            保存的主数据源
	 * @param utils
	 *            文档工具类
	 * @param resultMap
	 *            返回结果
	 * @param docVo
	 *            文档类
	 *            流程节点编号
	 * @param toUsers
	 *            下一步接收用户，如果为空则根据流程设置条件查找
	 * @throws Exception
	 */
	public static void Node_SendWork(String masterDataSource, DocumentUtils utils, Map resultMap, DocumentVO docVo,
			String toUsers) throws Exception {
		resultMap.put("act", 1);
		String fk_flow = docVo.getFlowTempleteId();
		String fk_node = docVo.getEntryId();
		String workID = docVo.getWorkItemId();
		Node currND = new Node(fk_node);
		Hashtable table = convertHashtable(masterDataSource, utils);
		String title = null;
		if (table != null && table.get("title") != null) {
			title = (String) table.get("title");
		}
		SendReturnObjs result = Dev2Interface.Node_SendWork(fk_flow, Long.parseLong(workID), table, null, 0, toUsers,
				WebUser.getNo(), WebUser.getName(), WebUser.getFK_Dept(), WebUser.getFK_DeptName(), title);
		boolean flag = saveExecuteData(utils, docVo, masterDataSource);
		if (flag) {
			resultMap.put("success", true);
			String nextExecutor = result.getVarAcceptersName();
			if(StringUtils.isNotBlank(nextExecutor)){
				nextExecutor = "," + ControllerHelper.getMessage("wf_next_step_executor") + nextExecutor;
			} else{
				nextExecutor = "";
			}		
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success) + nextExecutor);
		} else {
			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_fail));
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
	 * @param toUsers
	 * @throws Exception
	 */
	public static void Node_AskFor(String masterDataSource, DocumentUtils utils, PunUserBaseInfoVO user, Map resultMap,
			DocumentVO docVo, String toUsers) throws Exception {
		resultMap.put("act", 1);
		String fk_node = docVo.getEntryId();
		String workID = docVo.getWorkItemId();
		String result=Dev2Interface.Node_Askfor(Long.parseLong(workID), AskforHelpSta.AfterDealSend, toUsers, fk_node);
		boolean flag = saveExecuteData(utils, docVo, masterDataSource);
		if (flag) {
			resultMap.put("success", true);
			String nextExecutor = result;
			if(StringUtils.isNotBlank(nextExecutor)){
				nextExecutor = "," + ControllerHelper.getMessage("wf_next_step_executor") + nextExecutor;
			} else{
				nextExecutor = "";
			}
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success) + nextExecutor);
		} else {
			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_fail));
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
		Dev2Interface.Node_Shift(fk_flow, Integer.parseInt(fk_node), Long.parseLong(workID), Long.parseLong(fid),
				toUsers, "");
		boolean flag = saveExecuteData(utils, docVo, masterDataSource);
		if (flag) {
			resultMap.put("success", true);

			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success));
		} else {
			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_fail));
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
			Map resultMap, DocumentVO docVo) throws Exception {
		resultMap.put("act", 0);
		Hashtable table = convertHashtable(masterDataSource, utils);
		String fk_flow = docVo.getFlowTempleteId();
		String fk_node = docVo.getEntryId();
		String workID = docVo.getWorkItemId();
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
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success));
		} else {
			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_fail));
			throw new Exception(result);
		}

	}

	public static boolean saveExecuteData(DocumentUtils utils, DocumentVO docVo, String masterDataSource)
			throws Exception {
		// 更新数据
		if (StringUtils.isBlank(masterDataSource)) {
			return true;
		}
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

}
