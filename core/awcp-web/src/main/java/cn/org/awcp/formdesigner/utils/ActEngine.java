package cn.org.awcp.formdesigner.utils;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;

import cn.org.awcp.extend.formdesigner.DocumentUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.application.vo.PageActVO;
import cn.org.awcp.formdesigner.application.vo.WorkflowVariableVO;
import cn.org.awcp.formdesigner.core.domain.design.context.act.PageAct;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
//import cn.org.awcp.workflow.application.vo.ResultVO;
//import cn.org.awcp.workflow.applicationimpl.bpm.component.WorkflowProcessService;

/**
 * @ClassName: ActEngine
 * @Description: 动作的默认操作执行引擎
 * @author zyg zyg166@163.com
 * @date 2014年11月27日 下午4:03:09
 */
public class ActEngine {
	private DocumentVO docVo = null;
	private DynamicPageVO pageVO = null;
	private String[] _selects = null;
	private ScriptEngine engine = null;
	private DocumentService documentServiceImpl = null;
	// private WorkflowProcessService workflowProcessService = null;

	private ActEngine() {
	}

	// public ActEngine(DocumentVO docVo, DynamicPageVO pageVO, String[]
	// _selects, ScriptEngine engine, DocumentService documentServiceImpl,
	// WorkflowProcessService workflowProcessService) {
	// this.docVo = docVo;
	// this.pageVO = pageVO;
	// this._selects = _selects;
	// this.engine = engine;
	// this.documentServiceImpl = documentServiceImpl;
	// this.workflowProcessService = workflowProcessService;
	// }

	public ActEngine(DocumentVO docVo, DynamicPageVO pageVO, String[] _selects, ScriptEngine engine,
			DocumentService documentServiceImpl) {
		this.docVo = docVo;
		this.pageVO = pageVO;
		this._selects = _selects;
		this.engine = engine;
		this.documentServiceImpl = documentServiceImpl;
		// this.workflowProcessService = workflowProcessService;
	}

	public void execute(PageActVO vo) {
		int type = vo.getActType();
		switch (type) {
		case 2001:// 保存--带校验不带流程
			saveWithValidatNoFlow();
		case 2002:
			back();
		case 2003:
			delete();
		case 2004:
			// return executeSaveWithWorkflow();
		case 2008:

		case 2009:
			// return executeNew(vo);
		case 2010:
			// return executeUpdate(vo, _selects);
		default:
			// return executeDefault();
		}
	}

	/**
	 * @Title: executeSave @Description: 执行保存动作的默认操作 @return String 执行结果 @throws
	 */
	private void saveWithValidatNoFlow() {
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		DocumentUtils utils = (DocumentUtils) engine.get("DocumentUtils");
		docVo.setLastmodifier(String.valueOf(user.getUserId()));
		docVo.setAuditUser(String.valueOf(user.getUserId()));
		docVo.setState("草稿");
		if (docVo.isUpdate()) {
			// 更新数据
			for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
				String o = it.next();
				utils.updateData(o);
			}
			docVo.setLastmodified(new Date(System.currentTimeMillis()));
			utils.saveDocument();
		} else {
			docVo.setCreated(new Date(System.currentTimeMillis()));
			docVo.setAuthorId(user.getUserId());
			docVo.setLastmodified(docVo.getCreated());
			for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
				docVo.setId("");
				String o = it.next();
				utils.saveData(o);
				utils.saveDocument();
				utils.saveDocument();
			}
		}
	}

	/**
	 * @Title: executeSave @Description: 执行保存动作的默认操作 @return String 执行结果 @throws
	 */
	private void saveWithValidatAndFlow() {
		// 保存带流程
		// 保存--判断有没有流程实例id 有-更新，
		// 没有-判断有没有workflowId，有--启动流程，向document中插入一条记录 ，没有，插入一条记录
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		DocumentUtils utils = (DocumentUtils) engine.get("DocumentUtils");
		if (StringUtils.isNotBlank(docVo.getInstanceId())) {
			// 更新数据
			if (StringUtils.isNotBlank(docVo.getRecordId())) {
				for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
					String o = it.next();
					utils.updateData(o);
				}
			} else {
				for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
					String o = it.next();
					utils.saveData(o);
				}
			}
			// 更新document 记录
			utils.saveDocument();

		} else {
			// 无流程实例，根据按钮获取流程ID，根据流程ID，在表单中查找第一个节点的参数，并给参数赋值
			// 启动流程
			String workflowInfo = pageVO.getWorkflowNodeInfo();
			if (StringUtils.isNotBlank(workflowInfo)) {
				JSONObject nodes = JSON.parseObject(workflowInfo);
				String processDefinationId = null;// TODO
													// act.getExtbute().get("workflowId");
				// 根据流程找到第一个节点的id
				Collection<Object> objects = nodes.values();
				for (Object o : objects) {
					JSONObject node = (JSONObject) o;
					if (node.getString("workflowId").equalsIgnoreCase(processDefinationId)
							&& node.getIntValue("priority") == 1) {
						List<WorkflowVariableVO> variables = JSON.parseArray(node.getString("variables"),
								WorkflowVariableVO.class);
						Map<String, Object> workflowParams = new HashMap<String, Object>();
						if (variables != null && variables.size() > 0) {
							for (WorkflowVariableVO variable : variables) {
								String name = variable.getVariableName();
								String[] modelInfo = variable.getValue().split("\\.");
								String value = docVo.getListParams().get(modelInfo[0]).get(0).get(modelInfo[1]);
								workflowParams.put(name, value);
								break;
							}
						}
						String processInstanceId = null;
						/*
						 * ResultVO resultVO = null; try { resultVO =
						 * workflowProcessService.startProcessInstancebyUser(
						 * user.getUserId().toString(), processDefinationId, workflowParams);
						 * if(resultVO.isSuccess){ processInstanceId = resultVO.errorDescription;
						 * docVo.setInstanceId(processInstanceId);
						 * docVo.setLastmodifier(String.valueOf(user.getUserId() ));
						 * docVo.setAuditUser(String.valueOf(user.getUserId())); List<Task> task =
						 * workflowProcessService.findTasksByInstanceId( processInstanceId); if (task !=
						 * null && task.size() > 0) { // 设置nodeId
						 * docVo.setNodeId(task.get(0).getTaskDefinitionKey());
						 * docVo.setTaskId(task.get(0).getId()); docVo.setWorkflowId(task.get(0).
						 * getProcessDefinitionId()); } // 设置doc 记录为草稿状态 docVo.setState("草稿"); // 最后修改时间
						 * docVo.setLastmodified(new Date(System.currentTimeMillis())); } } catch
						 * (Exception e) { logger.info("ERROR", e); }
						 */
					}
				}
			}
			// 保存数据
			// 向document插入数据
			for (Iterator<String> it = docVo.getListParams().keySet().iterator(); it.hasNext();) {
				String o = it.next();
				utils.saveData(o);
			}
			utils.saveDocument();
		}
	}

	/**
	 * @Title: executeBack @Description: 执行后退操作 @return String 执行结果 @throws
	 */
	public String back() {

		return null;
	}

	/**
	 * 
	 * @Title: executeDelete @Description: 执行删除动作的默认操作 @return String 执行结果 @throws
	 */
	public void delete() {
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		DocumentUtils utils = (DocumentUtils) engine.get("DocumentUtils");
		if (_selects != null && _selects.length >= 1) {
			for (String select : _selects) {
				utils.deleteData(pageVO, select);
				BaseExample base = new BaseExample();
				base.createCriteria().andEqualTo("RECORD_ID", select);
				documentServiceImpl.deleteByExample(base);
			}
			// mv.setViewName("redirect:/document/view.do?dynamicPageId=" +
			// pageId);
			// return mv;
		} else {
			// TODO 返回并提示没有选择记录
		}
	}

	public String executeUpdate(PageAct act, String[] _selects) {
		if (_selects.length < 1) {
			return "error";
		}
		return "redirect:/document/view.do?recordId=" + _selects[0] + "&dynamicPageId="
				+ act.getExtbute().get("target");
	}

	/**
	 * 
	 * @Title: execute @Description: 默认无类型的动作执行操作 @return String 执行结果 @throws
	 */
	public String executeDefault() {
		return null;
	}

	/**
	 * 
	 * @Title: executeSaveWithWorkflow @Description: 保存带流程的默认操作 @return
	 *         String @throws
	 */
	public String executeSaveWithWorkflow() {
		DocumentUtils docUtil = DocumentUtils.getIntance();
		/*
		 * for(Iterator<String> it = doc.getParams().keySet().iterator();
		 * it.hasNext();){ docUtil.saveData(it.next()); }
		 */
		docUtil.saveDocument();
		// 调用流程接口

		return null;
	}

	public String executeNew(PageAct act) {
		return "redirect:/document/view.do?dynamicPageId=" + act.getExtbute().get("target");
	}
}
