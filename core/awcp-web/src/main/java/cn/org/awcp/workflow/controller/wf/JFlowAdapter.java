package cn.org.awcp.workflow.controller.wf;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import BP.Port.WebUser;
import BP.WF.Dev2Interface;
import BP.WF.SendReturnObjs;
import BP.WF.Template.Node;
import BP.WF.Template.PubLib.AskforHelpSta;
import BP.WF.Template.PubLib.WFState;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.I18nKey;
import cn.org.awcp.venson.controller.base.ControllerHelper;

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
			String masterDataSource, DocumentUtils utils, DocumentVO docVo) throws Exception {
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
					returnEmp, msg, true);
			saveExecuteData(utils, docVo, masterDataSource);
			String nextExecutor = "";
			if(StringUtils.isNotBlank(returnEmp)){
				nextExecutor = "," + ControllerHelper.getMessage("wf_next_step_executor") + returnEmp;
			}	
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success) + nextExecutor);
			resultMap.put("success", true);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success));

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
	 * @param fk_flow
	 *            流程编号
	 * @param workID
	 *            流程实例编号
	 * @param fk_node
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
	 * @param fk_flow
	 * @param workID
	 * @param fk_node
	 * @param toUsers
	 * @throws Exception
	 */
	public static void Node_AskFor(String masterDataSource, DocumentUtils utils, PunUserBaseInfoVO user, Map resultMap,
			DocumentVO docVo, String toUsers) throws Exception {
		resultMap.put("act", 1);
		String fk_node = docVo.getEntryId();
		String workID = docVo.getWorkItemId();
		Dev2Interface.Node_Askfor(Long.parseLong(workID), AskforHelpSta.AfterDealSend, toUsers, fk_node);
		boolean flag = saveExecuteData(utils, docVo, masterDataSource);
		if (flag) {
			resultMap.put("success", true);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success));
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
