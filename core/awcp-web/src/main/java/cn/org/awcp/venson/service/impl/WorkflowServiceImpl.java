package cn.org.awcp.venson.service.impl;

import BP.WF.*;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.venson.service.WorkflowService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("workflowServiceImpl")
@Transactional(rollbackFor = {Exception.class})
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
