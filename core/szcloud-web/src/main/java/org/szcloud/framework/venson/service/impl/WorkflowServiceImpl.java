package org.szcloud.framework.venson.service.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.szcloud.framework.formdesigner.utils.DocumentUtils;
import org.szcloud.framework.venson.service.WorkflowService;

import BP.WF.Dev2Interface;
import BP.WF.SendReturnObjs;
import BP.WF.Template.Flow;
import BP.WF.Template.PubLib.AskforHelpSta;
import BP.WF.Template.WorkBase.Work;

@Service("workflowServiceImpl")
@Transactional
public class WorkflowServiceImpl implements WorkflowService {
	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(WorkflowServiceImpl.class);
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public long create(String dataId, String user) {
		// 1.创建流程
		Flow currFlow = new Flow(defaut_flow);
		Work currWK = currFlow.NewWork();
		long workID = currWK.getOID();
		// 2.发送流程
		Dev2Interface.Node_SendWork(defaut_flow, workID, 0, user);
		// 3.建立流程表单关联关系
		String sql = "insert into workflow_form_relation(WORK_ID,FLOW_ID,NODE_ID,DATA_ID,FORM_NAME,CREATE_TIME) values(?,?,?,?,?,?)";
		jdbcTemplate.update(sql, workID, defaut_flow, Integer.parseInt(defaut_flow + "01"), dataId, "a_leave",
				DocumentUtils.getIntance().today());
		return workID;
	}

	@Override
	public String transfer(long workId, String user) {
		String msg = Dev2Interface.Node_Askfor(workId, AskforHelpSta.AfterDealSend, user, null);
		return msg;
	}

	@Override
	public String execute(long workId) {
		SendReturnObjs msg = Dev2Interface.Node_SendWork(defaut_flow, workId);
		return msg.ToMsgOfText().replaceAll("@", " ");
	}

}
