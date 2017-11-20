package cn.org.awcp.workflow.controller.wf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

import BP.Port.Emp;
import BP.Port.WebUser;
import BP.Sys.Frm.MapData;
import BP.WF.Dev2Interface;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Template.Flow;
import BP.WF.Template.Node;
import BP.WF.Template.PubLib.WFState;
import BP.WF.Template.WorkBase.Work;
import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.application.vo.PageActVO;
import cn.org.awcp.formdesigner.application.vo.StoreVO;
import cn.org.awcp.formdesigner.core.engine.FreeMarkers;
import cn.org.awcp.formdesigner.utils.DocUtils;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.formdesigner.utils.PageBindUtil;
import cn.org.awcp.formdesigner.utils.ScriptEngineUtils;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.unit.service.PunPositionService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.service.PunUserGroupService;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.I18nKey;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.dingding.controller.DdUtil;
import cn.org.awcp.venson.service.QueryService;
import cn.org.awcp.workflow.controller.util.HttpRequestDeviceUtils;

@Controller
@RequestMapping("workflow/wf")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class WorkflowTaskControl extends BaseController {
	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(WorkflowTaskControl.class);
	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;// 用户Service

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	@Resource(name = "punUserGroupServiceImpl")
	private PunUserGroupService punUserGroupService;

	@Autowired
	private FormdesignerService formdesignerServiceImpl;
	@Autowired
	private DocumentService documentServiceImpl;

	@Autowired
	private StoreService storeServiceImpl;

	// 0：表示待办任务，1：表示已办任务，在打开任务时赋值 2，归档件
	private String taskType = "";

	@Resource(name = "punPositionServiceImpl")
	private PunPositionService punPositionService;
	@Resource(name = "queryServiceImpl")
	private QueryService query;

	/**
	 * 待办任务（个人任务）
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("listPersonalTasks")
	public String listPersonalTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize,
			@RequestParam(required = false, defaultValue = "") String FK_Flow, HttpServletRequest request, Model model)
			throws IOException {
		// 获取当前登录用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		String workItemName = request.getParameter("workItemName");
		model.addAttribute("workItemName", workItemName);
		model.addAttribute("FK_Flow", FK_Flow);
		Map<String, Object> data = query.getUntreatedData(pageSize, (currentPage - 1) * pageSize, FK_Flow, workItemName,
				user.getUserIdCardNumber(), true);

		Paginator paginator = new Paginator(currentPage, pageSize, (Integer) data.get("count"));

		PageList<Map<String, Object>> pageList = new PageList<Map<String, Object>>(
				(List<Map<String, Object>>) data.get("data"), paginator);

		model.addAttribute("currentPage", currentPage);
		model.addAttribute("models", pageList);
		model.addAttribute("count", data.get("count"));
		String message = request.getParameter("message");
		if (null != message) {
			model.addAttribute("message", message);
		} else {
			model.addAttribute("message", "");
		}
		return "workflow/wf/listPersonalTasks";
	}

	/**
	 * 查询待办件数量
	 * 
	 */
	@RequestMapping("getUntreatedCount")
	@ResponseBody
	public ReturnResult getUntreatedCount(@RequestParam(required = false, value = "FK_Flow") String FK_Flow,
			@RequestParam(required = false, value = "workItemName") String workItemName) throws IOException {
		ReturnResult result = ReturnResult.get();
		// 获取当前登录用户
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		Map<String, Object> data = query.getUntreatedData(Integer.MAX_VALUE, 1, FK_Flow, workItemName,
				user.getUserIdCardNumber(), true);
		result.setData(data.get("count")).setStatus(StatusCode.SUCCESS);
		return result;
	}

	/**
	 * 已处理
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("inDoingTasks")
	public String inDoingTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize,
			@RequestParam(required = false, defaultValue = "") String FK_Flow, HttpServletRequest request, Model model)
			throws IOException {

		// 获取当前登录用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);

		String workItemName = request.getParameter("workItemName");

		model.addAttribute("workItemName", workItemName);
		model.addAttribute("FK_Flow", FK_Flow);

		Map<String, Object> data = query.getHandledData(pageSize, (currentPage - 1) * pageSize, FK_Flow, workItemName,
				user.getUserIdCardNumber());

		Paginator paginator = new Paginator(currentPage, pageSize, (Integer) data.get("count"));

		PageList<Map<String, Object>> pageList = new PageList<Map<String, Object>>(
				(List<Map<String, Object>>) data.get("data"), paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("count", data.get("count"));

		return "workflow/wf/inDoingTasks";
	}

	/**
	 * 归档
	 * 
	 * @return
	 */
	@RequestMapping("listHistoryTasks")
	public String listHistoryTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize,
			@RequestParam(required = false, defaultValue = "") String FK_Flow, HttpServletRequest request,
			Model model) {
		// 获取当前登录用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		String workItemName = request.getParameter("workItemName");
		model.addAttribute("workItemName", workItemName);
		model.addAttribute("FK_Flow", FK_Flow);
		Map<String, Object> data = query.getCompileData(pageSize, (currentPage - 1) * pageSize, FK_Flow, workItemName,
				user.getUserIdCardNumber());

		Paginator paginator = new Paginator(currentPage, pageSize, (Integer) data.get("count"));

		PageList<Map<String, Object>> pageList = new PageList<Map<String, Object>>(
				(List<Map<String, Object>>) data.get("data"), paginator);
		model.addAttribute("count", data.get("count"));
		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);

		return "workflow/wf/listHistoryTasks";
	}

	/**
	 * 流程挂起（暂停）
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doHungUp", method = RequestMethod.POST)
	public Map<String, Object> doHungUp(HttpServletRequest request, HttpServletResponse response) {
		long WorkID = Long.parseLong(request.getParameter("WorkID").toString());
		String FK_Flow = request.getParameter("FK_Flow");
		String RB_HungWay = request.getParameter("RB_HungWay");
		String TB_RelData = request.getParameter("TB_RelData");
		String TB_Note = request.getParameter("FK_Note");
		Map<String, Object> map = new HashMap<String, Object>();
		String message = "";
		try {
			GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
			if (gwf.getWFState() == WFState.HungUp) {
				BP.WF.Dev2Interface.Node_UnHungUpWork(FK_Flow, WorkID, TB_Note);
			} else {
				message = BP.WF.Dev2Interface.Node_HungUpWork(FK_Flow, WorkID, Integer.parseInt(RB_HungWay), TB_RelData,
						TB_Note);
			}
		} catch (Exception ex) {
			logger.info("ERROR", ex);
			message = ex.getMessage();
		}
		map.put("message", message);
		return map;
	}

	/**
	 * 模板列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("templateList")
	public String templateList(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "5") int pageSize, HttpServletRequest request, Model model)
			throws Exception {
		// 获取当前登录用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);

		List<Map<String, Object>> lsM = new ArrayList<Map<String, Object>>();

		// 第一步:查询所有的流程类别
		String sql = "select * from wf_flowsort";

		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sql);
		/**
		 * 遍历所有的流程类别，然后根据流程类别查找流程
		 */
		for (Map<String, Object> l : ls) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PID", "");
			map.put("Name", l.get("Name"));
			map.put("ID", l.get("No"));
			map.put("NodeID", "0");
			lsM.add(map);

			// 查询当前登录人可以发起的流程
			String str = "SELECT WF_Flow.No, WF_Flow.Name,IFNULL(WF_Flow.FK_FlowSort, '01') "
					+ "FK_FlowSort, WF_FlowSort_FK_FlowSort.Name AS FK_FlowSortText,"
					+ "IFNULL(WF_Flow.FlowRunWay,0) FlowRunWay, WF_Flow.RunObj,"
					+ " WF_Flow.Note, WF_Flow.RunSQL,IFNULL(WF_Flow.NumOfBill,0)"
					+ " NumOfBill,IFNULL(WF_Flow.NumOfDtl,0) NumOfDtl," + "IFNULL(WF_Flow.FlowAppType,0) FlowAppType,"
					+ "IFNULL(WF_Flow.ChartType,1) ChartType, "
					+ "IFNULL(WF_Flow.IsCanStart,1) IsCanStart, IFNULL( round(WF_Flow.AvgDay ,4) ,0.0) AvgDay,IFNULL(WF_Flow.IsFullSA,0) IsFullSA,IFNULL(WF_Flow.IsMD5,0) IsMD5,IFNULL(WF_Flow.Idx,0) Idx,IFNULL(WF_Flow.TimelineRole,0) TimelineRole, WF_Flow.Paras, WF_Flow.PTable,"
					+ "IFNULL(WF_Flow.Draft,0) Draft,IFNULL(WF_Flow.DataStoreModel,0) DataStoreModel, WF_Flow.TitleRole, WF_Flow.FlowMark, WF_Flow.FlowEventEntity, WF_Flow.HistoryFields,IFNULL(WF_Flow.IsGuestFlow,0) IsGuestFlow, WF_Flow.BillNoFormat,"
					+ " WF_Flow.FlowNoteExp,IFNULL(WF_Flow.DRCtrlType,0) DRCtrlType,IFNULL(WF_Flow.StartLimitRole,0) StartLimitRole, WF_Flow.StartLimitPara, WF_Flow.StartLimitAlert,IFNULL(WF_Flow.StartLimitWhen,0) "
					+ "StartLimitWhen,IFNULL(WF_Flow.StartGuideWay,0)" + " StartGuideWay, WF_Flow.StartGuidePara1,"
					+ " WF_Flow.StartGuidePara2, WF_Flow.StartGuidePara3,"
					+ "IFNULL(WF_Flow.IsResetData,0) IsResetData," + "IFNULL(WF_Flow.IsLoadPriData,0) IsLoadPriData,"
					+ "IFNULL(WF_Flow.CFlowWay,0) CFlowWay, WF_Flow.CFlowPara,"
					+ "IFNULL(WF_Flow.IsBatchStart,0) IsBatchStart, "
					+ "WF_Flow.BatchStartFields,IFNULL(WF_Flow.IsAutoSendSubFlowOver,0) "
					+ "IsAutoSendSubFlowOver, WF_Flow.Ver,IFNULL(WF_Flow.DTSWay,0) DTSWay, WF_Flow.DTSBTable, "
					+ "WF_Flow.DTSBTablePK,IFNULL(WF_Flow.DTSTime,0) DTSTime, "
					+ "WF_Flow.DTSSpecNodes,IFNULL(WF_Flow.DTSField,0) DTSField,"
					+ " WF_Flow.DTSFields FROM WF_Flow LEFT JOIN WF_FlowSort AS WF_FlowSort_FK_FlowSort ON "
					+ "WF_Flow.FK_FlowSort=WF_FlowSort_FK_FlowSort.No WHERE "
					+ " ( ( WF_Flow.No IN ( SELECT FK_Flow FROM V_FlowStarter " + "WHERE FK_Emp='"
					+ user.getUserIdCardNumber() + "' ) ) AND " + "( WF_Flow.IsCanStart = 1)) AND WF_Flow.FK_FlowSort='"
					+ l.get("No") + "' ORDER BY WF_Flow.FK_FlowSort," + "WF_Flow.Idx";

			List<Map<String, Object>> lm = jdbcTemplate.queryForList(str);
			for (Map<String, Object> m : lm) {
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("PID", m.get("FK_FlowSort"));
				maps.put("Name", m.get("Name"));
				maps.put("ID", m.get("No"));
				maps.put("NodeID", Integer.parseInt(m.get("No").toString()) + "01");
				lsM.add(maps);
			}
		}

		model.addAttribute("tableJson", JSON.toJSON(lsM));

		return "workflow/wf/templateList";
	}

	/**
	 * 新建任务
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("openTemplate")
	public void openTemplate(HttpServletResponse response, HttpServletRequest request) throws Exception {

		response.setContentType("text/html;");
		response.getWriter().write(renderDocument(request, response));

	}

	/**
	 * 初始化流程，获取流程参数
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getFlowInitParams")
	@ResponseBody
	public ReturnResult getFlowInitParams(HttpServletRequest request) {
		ReturnResult result = ReturnResult.get();
		try {
			// 初始化变量.
			String fk_flow = request.getParameter("FK_Flow");
			String fk_node = request.getParameter("FK_Node");
			Flow currFlow = new Flow(fk_flow);
			Node currND = new Node(fk_node);
			MapData mapData = new MapData("ND" + fk_node);
			mapData.Retrieve();
			Map<String, Object> resultMap = new HashMap<>();
			Work currWK = currFlow.NewWork();
			long workId = currWK.getOID();
			resultMap.put("workId", workId);
			resultMap.put("cWorkID", 0);
			resultMap.put("NodeID", currND.getNodeID());
			resultMap.put("flowId", fk_flow);
			resultMap.put("FID", 0);
			resultMap.put("UserNo", WebUser.getNo());
			resultMap.put("SID", WebUser.getSID());
			result.setStatus(StatusCode.SUCCESS).setData(resultMap);
		} catch (Exception e) {
			result.setStatus(StatusCode.FAIL.setMessage("参数获取失败！"));
		}
		return result;
	}

	private static final String preset = "005";

	private Map executeAct(final HttpServletRequest request, HttpServletResponse response) {

		SzcloudJdbcTemplate jdbcTemplate1 = Springfactory.getBean("jdbcTemplate");

		String docId = request.getParameter("docId");
		String pageId = request.getParameter("dynamicPageId");
		String flowTempleteId = request.getParameter("FK_Flow");
		String fid = request.getParameter("FID");
		String workItemId = request.getParameter("WorkID");
		boolean isNewWork = false;
		// 如果是新增流程则创建workId
		if (!StringUtils.isNumeric(workItemId)) {
			Flow currFlow = new Flow(flowTempleteId);
			Work currWK = currFlow.NewWork();
			workItemId = currWK.getOID() + "";
			isNewWork = true;
		}

		String entryId = request.getParameter("FK_Node");
		String workflowId = request.getParameter("workflowId");
		String actId = request.getParameter("actId");
		String update = request.getParameter("update");
		String masterDataSource = request.getParameter("masterDataSource");
		// json格式的对话框参数
		String slectsUserIds = request.getParameter("slectsUserIds");
		String CC_slectsUserIds = request.getParameter("CC_slectsUserIds");
		String acte = request.getParameter("actType");

		Integer actType = 0;

		String toNode = request.getParameter("toNode");

		logger.debug("===================slectsUserIds:" + slectsUserIds);

		// 返回信息
		Map<String, Object> resultMap = new HashMap();
		DynamicPageVO pageVO = null;
		PunUserBaseInfoVO user = null;
		DocumentVO docVo = new DocumentVO();
		Map<String, String> map = null;
		StoreVO store = null;
		PageActVO act = null;
		ScriptEngine engine = null;
		Enumeration enumeration = null;
		Map<String, Object> extMap = new HashMap<String, Object>();
		DocumentUtils utils;
		try {
			// 获取当前登录用户
			jdbcTemplate1.beginTranstaion();
			user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
			if (StringUtils.isNotBlank(pageId)) {
				pageVO = formdesignerServiceImpl.findById(pageId);
			}
			boolean isUpdate = false;
			if (StringUtils.isNotBlank(update)) {
				isUpdate = update.equalsIgnoreCase("true");
			}

			// 表单页面
			if (pageVO.getPageType() == 1002) {// 初始化表单数据
				docVo = documentServiceImpl.findById(docId);
				docVo.setUpdate(isUpdate);
				docVo.setDynamicPageId(pageId);
				docVo.setDynamicPageName(pageVO.getName());
				docVo.setFlowTempleteId(flowTempleteId);
				docVo.setWorkItemId(workItemId);
				docVo.setEntryId(entryId);
				docVo.setWorkflowId(workflowId);

				docVo.setFid(fid);
			}
			map = new HashMap<String, String>();
			enumeration = request.getParameterNames();
			for (; enumeration.hasMoreElements();) {
				Object o = enumeration.nextElement();
				String name = o.toString();
				String[] values = request.getParameterValues(name);
				map.put(o.toString(), StringUtils.join(values, ";"));
			}
			docVo.setRequestParams(map);
			docVo = documentServiceImpl.processParams(docVo);
			store = storeServiceImpl.findById(actId);
			act = JSON.parseObject(store.getContent(), PageActVO.class);

			// 初始化脚本解释执行器,加载全局工具类
			engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
			engine.put("request", request);
			engine.put("session", request.getSession());
			if (act != null && StringUtils.isNotBlank(act.getServerScript())) {
				String script = StringEscapeUtils.unescapeHtml4(act.getServerScript());
				engine.eval(script);
			}

			// 设置主数据源
			if (docVo.getListParams().size() >= 1) {
				masterDataSource = docVo.getListParams().keySet().iterator().next();
			}

			// 校验文档
			utils = (DocumentUtils) engine.get("DocumentUtils");
			if (StringUtils.isNotEmpty(docVo.getRecordId()))
				utils.setDataItem(masterDataSource, "ID", docVo.getRecordId());
			if (preset.equals(docVo.getFlowTempleteId())) {
				boolean isCH = ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE;
				String field = isCH ? "title" : "enTitle";
				String sql = "select " + field + " from dd_apps where dynamicPageId=?";
				Object value = DocumentUtils.getIntance().excuteQueryForObject(sql, docVo.getDynamicPageId());
				String title = user.getName() + (isCH ? "的" : "'s ")
						+ (value instanceof String ? (String) value : ControllerHelper.getMessage("wf_approval"));
				utils.setDataItem(masterDataSource, "title", title);
			}
			// 判断是否有权限处理
			if (!"2026".equals(acte) && isNewWork == false) {
				if (Dev2Interface.Flow_IsCanDoCurrentWork(flowTempleteId, Integer.parseInt(entryId),
						Long.parseLong(workItemId), WebUser.getNo()) == false) {
					resultMap.put("success", false);
					resultMap.put("message", ControllerHelper.getMessage("wf_not_can_do"));
					jdbcTemplate1.commit();
					return resultMap;
				}
			}
			if (preset.equals(flowTempleteId)) {
				// ------------------------流程预设 modify by venson------------------------
				if (act.getActType() == 2019 || act.getActType() == 2024) {
					perSetWorkFlow(request, pageId, flowTempleteId, workItemId, isNewWork, entryId, slectsUserIds);
					// 抄送预设
					presetCC(pageId, flowTempleteId, workItemId, CC_slectsUserIds, isNewWork);
					if (request.getAttribute("slectsUserIds") != null) {
						slectsUserIds = (String) request.getAttribute("slectsUserIds");
					}
					if (request.getAttribute("acte") != null) {
						acte = (String) request.getAttribute("acte");
					}
				}
			}
			// ------------------------流程预设 end------------------------
			// 根据actType 执行其默认操作
			actType = StringUtils.isNumeric(acte) ? Integer.parseInt(acte) : act.getActType();
			switch (actType) {
			case 2000:
				break;
			case 2002:
			case 2006:
				// 流程回撤
				JFlowAdapter.Flow_DoUnSend(resultMap, flowTempleteId, workItemId);
				break;

			case 2008:
				// 根据选中的人员ID，组装信息
				JFlowAdapter.Node_SaveWork(masterDataSource, utils, user, resultMap, docVo);

				break;
			case 2011:// 流程流转(发送)
			case 2018:// 流程转发
				// 根据选中的人员ID，组装信息
				if (StringUtils.isNotBlank(slectsUserIds)) {
					JFlowAdapter.Node_SendWork(masterDataSource, utils, resultMap, docVo,
							getToUsers(request, slectsUserIds));
					afterExecuteFlow(docVo, masterDataSource, jdbcTemplate1, resultMap);
				} else {
					resultMap.put("success", false);
					resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_select_least_person));
					return resultMap;
				}
				break;
			case 2019:// 流程传阅
				JFlowAdapter.Node_SendWork(masterDataSource, utils, resultMap, docVo, null);
				afterExecuteFlow(docVo, masterDataSource, jdbcTemplate1, resultMap);
				break;
			case 2026:// 流程撤销
				// 状态撤销
				request.setAttribute("state", 3);
				JFlowAdapter.Flow_DoFlowOverByCoercion(resultMap, flowTempleteId, Integer.valueOf(entryId),
						Long.valueOf(workItemId), Long.valueOf(fid), WFState.Undo);
				insertIntoLogs(docVo, user.getUserIdCardNumber(),
						ControllerHelper.getMessage(I18nKey.wf_approval_undo));
				break;
			case 2027:// 流程拒绝
				// 状态拒绝
				request.setAttribute("state", 1);
				JFlowAdapter.Flow_DoFlowOverByCoercion(resultMap, flowTempleteId, Integer.valueOf(entryId),
						Long.valueOf(workItemId), Long.valueOf(fid), WFState.Reject);
				afterExecuteFlow(docVo, masterDataSource, jdbcTemplate1, resultMap);
				break;
			case 2017:// 流程办结
				JFlowAdapter.Flow_DoFlowOverByCoercion(resultMap, flowTempleteId, Integer.valueOf(entryId),
						Long.valueOf(workItemId), Long.valueOf(fid), "");

				break;
			case 2023:// 流程退回
				JFlowAdapter.Flow_returnWork(resultMap, Long.valueOf(fid), "", user.getUserIdCardNumber(), toNode,
						masterDataSource, utils, docVo);
				break;
			case 2024:// 加签
				// 根据选中的人员ID，组装信息
				if (StringUtils.isNotBlank(slectsUserIds)) {
					JFlowAdapter.Node_AskFor(masterDataSource, utils, user, resultMap, docVo,
							getToUsers(request, slectsUserIds));
					afterExecuteFlow(docVo, masterDataSource, jdbcTemplate1, resultMap);
				} else {
					resultMap.put("success", false);
					resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_select_least_person));
					return resultMap;
				}
				break;
			case 2025:// 已阅
				Dev2Interface.Node_DoCCCheckNote(workflowId, Integer.parseInt(entryId), Long.parseLong(workItemId),
						Long.parseLong(fid), I18nKey.wf_send_success);
				resultMap.put("success", true);
				resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_send_success));
				break;
			}

			resultMap.put("docId", docVo.getId());
			resultMap.put("WorkItemID", docVo.getWorkItemId());
			resultMap.put("EntryID", docVo.getEntryId());
			resultMap.put("dynamicPageId", pageId);
			resultMap.put("flowTempleteId", flowTempleteId);
			resultMap.put("FID", fid);
			jdbcTemplate1.commit();
			return resultMap;
		} catch (Exception e) {
			logger.info("ERROR", e);
			try {
				jdbcTemplate1.rollback();
			} catch (Exception e1) {
			}
			resultMap.put("success", false);
			resultMap.put("message", ControllerHelper.getMessage(I18nKey.wf_operation_failure));

		} finally {
			pageVO = null;
			user = null;
			docVo = null;
			if (map != null)
				map.clear();
			store = null;
			act = null;
			engine = null;
			utils = null;
			enumeration = null;
			if (extMap != null)
				extMap.clear();

		}
		return resultMap;
	}

	/**
	 * 抄送预设
	 * 
	 * @param pageId
	 * @param flowTempleteId
	 * @param workItemId
	 * @param CC_slectsUserIds
	 */
	private void presetCC(String pageId, String flowTempleteId, String workItemId, String CC_slectsUserIds,
			boolean isNewWork) {
		// 查看是否属于预置流程并且是新建流程
		if (isNewWork) {
			// 如果没有选择抄送人则取数据库查找
			if (StringUtils.isBlank(CC_slectsUserIds)) {
				// 去数据库取默认预置人员
				String sql = "select JSON from p_fm_cc_flow where PAGE_ID=?";
				CC_slectsUserIds = (String) DocumentUtils.getIntance().excuteQueryForObject(sql, pageId);
			}
			// 如果存在抄送人
			if (StringUtils.isNotBlank(CC_slectsUserIds)) {
				com.alibaba.fastjson.JSONArray jsonArr = JSON.parseArray(CC_slectsUserIds);
				// 插入抄送记录
				int len = jsonArr.size();
				for (int i = 0; i < len; i++) {
					Emp emp = new Emp(jsonArr.getString(i));
					Dev2Interface.Node_CC(flowTempleteId, 999, Long.parseLong(workItemId), emp.getNo(), emp.getName(),
							null, null, null, 0);
				}
			}
		}
	}

	/**
	 * 流程预设
	 * 
	 * @param request
	 * @param pageId
	 * @param flowTempleteId
	 * @param workItemId
	 * @param isNewWork
	 * @param entryId
	 * @param slectsUserIds
	 */
	private void perSetWorkFlow(final HttpServletRequest request, String pageId, String flowTempleteId,
			String workItemId, boolean isNewWork, String entryId, String slectsUserIds) {
		String currentUserID = ControllerHelper.getUser().getUserIdCardNumber();
		// 流程新建立
		if (isNewWork) {
			// 查看是否有选择流程人员
			if (StringUtils.isBlank(slectsUserIds)) {
				// 去数据库取默认预置人员
				String sql = "select JSON from p_fm_flow where PAGE_ID=?";
				slectsUserIds = (String) DocumentUtils.getIntance().excuteQueryForObject(sql, pageId);
				if (slectsUserIds == null) {
					return;
				}
			}
			com.alibaba.fastjson.JSONArray jsonArr = JSON.parseArray(slectsUserIds);
			// 将人员数据保存
			String sql = " insert into p_fm_work(JSON,CREATOR,WORK_ID,FLOW_ID,NODE_ID,CURRENT_NODE,PAGE_ID) values(?,?,?,?,?,?,?)";
			this.jdbcTemplate.update(sql, slectsUserIds, currentUserID, workItemId, preset, entryId, 0, pageId);
			// 将人员记录到流程日志表中
			final String insertSql = "insert into p_fm_work_logs(WORK_ID,PAGE_ID,CREATOR,CURRENT_NODE) values(?,?,?,?)";
			this.jdbcTemplate.update(insertSql, workItemId, pageId, currentUserID, 0);
			int len = jsonArr.size();
			for (int i = 0; i < len; i++) {
				String v = jsonArr.getString(i);
				String[] arr = v.split(",");
				for (String str : arr) {
					if (StringUtils.isNotBlank(str)) {
						this.jdbcTemplate.update(insertSql, workItemId, pageId, str, i);
					}
				}
			}
			// 取出第一步人员
			slectsUserIds = jsonArr.getString(0);
			// 让getToUsers方法直接返回人员
			request.setAttribute("direct", true);
			// 设置下一节点人员
			request.setAttribute("slectsUserIds", slectsUserIds);
			// 第一步走转发
			request.setAttribute("acte", "2011");
			// 设置当前节点
			request.setAttribute("CURRENT_NODE", 0);
			// 状态设置为同意
			request.setAttribute("state", 0);
		} else {
			// 去数据库取默认预置人员
			String sql = "select ID,JSON,CURRENT_NODE from p_fm_work where PAGE_ID=? and WORK_ID=? limit 0,1";
			Map<String, Object> map = this.jdbcTemplate.queryForMap(sql, pageId, workItemId);
			int CURRENT_NODE = (int) map.get("CURRENT_NODE");
			// 如果存在选择的人员则属于加签，否则是属于传阅
			if (StringUtils.isNotBlank(slectsUserIds)) {
				// 如果是加签，则将加签人员添加到人员记录表中(未实现)sql = "update p_fm_work set JSON=? where ID=?";
				// 将加签人保存到流程日志表
				final String insertSql = "insert into p_fm_work_logs(WORK_ID,PAGE_ID,CREATOR,CURRENT_NODE) values(?,?,?,?)";
				String[] arr = slectsUserIds.split(",");
				for (String str : arr) {
					if (StringUtils.isNotBlank(str)) {
						// 如果当前节点不存在相同处理人则插入
						if (this.jdbcTemplate.queryForObject(
								"select count(1) from p_fm_work_logs where WORK_ID=? and CREATOR=? and CURRENT_NODE=? and send_time is null",
								Integer.class, workItemId, str, CURRENT_NODE) == 0) {
							this.jdbcTemplate.update(insertSql, workItemId, pageId, str, CURRENT_NODE);
						}
					}
				}
				// 设置下一节点人员
				request.setAttribute("slectsUserIds", slectsUserIds);
				// 将流程动作类型改为加签
				request.setAttribute("acte", "2024");
				// 状态移交
				request.setAttribute("state", 3);
			} else {
				String text = map.get("JSON") + "";
				com.alibaba.fastjson.JSONArray jsonArr = JSON.parseArray(text);
				int nextNode = CURRENT_NODE + 1;
				int len = jsonArr.size();
				// 查询是否还有下一节点人员
				if (nextNode < len) {
					// 查询当前节点所有人员是否已经处理完
					sql = "select count(1) from wf_generworkerlist where workid=? and ispass=0 and fk_emp<>?";
					int count = this.jdbcTemplate.queryForObject(sql, Integer.class, workItemId, currentUserID);
					if (count == 0) {
						Object ID = map.get("ID");
						// 当前节点已经处理完就去获取定制的下一节点接收人
						slectsUserIds = jsonArr.getString(nextNode);
						// 更新当前节点到数据库
						sql = "update p_fm_work set CURRENT_NODE=? where ID=?";
						this.jdbcTemplate.update(sql, nextNode, ID);
						// 让getToUsers方法直接返回人员
						request.setAttribute("direct", true);
						// 设置下一节点人员
						request.setAttribute("slectsUserIds", slectsUserIds);
						// 将流程动作类型改为加签
						request.setAttribute("acte", "2024");
					} else {
						// 将流程动作类型改为传阅
						request.setAttribute("acte", "2019");
					}
				} else {
					// 将流程动作类型改为传阅
					request.setAttribute("acte", "2019");
				}
			}
			// 设置当前节点
			request.setAttribute("CURRENT_NODE", CURRENT_NODE);
			// 状态设置为同意
			request.setAttribute("state", 0);
		}
	}

	/**
	 * 获取转发用户
	 * 
	 * @param request
	 * @param slectsUserIds
	 * @returnR
	 */
	private String getToUsers(final HttpServletRequest request, String slectsUserIds) {
		Object direct = request.getAttribute("direct");
		if (HttpRequestDeviceUtils.isMobileDevice(request) || HttpRequestDeviceUtils.isDingDing(request)
				|| direct != null) {
			// List<Map<String, Object>> person = (List<Map<String, Object>>)
			// JSON.parse(slectsUserIds);
			// slectsUserIds = person.get(0).get("value").toString();
			return slectsUserIds;
		}
		StringBuffer sb = new StringBuffer();
		String[] strs = slectsUserIds.split(",");
		for (String s : strs) {
			if (s != null && !s.equals("")) {
				PunUserBaseInfoVO pbi = userService.findById(Long.parseLong(s));
				sb.append(pbi.getUserIdCardNumber());
				sb.append(",");
			}

		}
		return sb.substring(0, sb.length() - 1).toString();
	}

	private void afterExecuteFlow(DocumentVO docVo, String masterDataSource, SzcloudJdbcTemplate jdbcTemplate1,
			Map<String, Object> resultMap) {
		if (preset.equals(docVo.getFlowTempleteId()) && resultMap.containsKey("success")
				&& (Boolean) resultMap.get("success")) {
			// 执行消息推送
			addPush(docVo, masterDataSource, jdbcTemplate1);
			// 流程日志
			addLogs(docVo);
		}
	}

	private void addLogs(DocumentVO docVo) {
		// 查看是否属于预置流程
		if (preset.equals(docVo.getFlowTempleteId())) {

			String userId = ControllerHelper.getUser().getUserIdCardNumber();
			HttpServletRequest request = ControllerContext.getRequest();
			String content = request.getParameter("work_logs_content");
			Object CURRENT_NODE = request.getAttribute("CURRENT_NODE");
			Object state = request.getAttribute("state");
			int currentNode = 0;
			if (CURRENT_NODE != null) {
				// 先从流程预设中获取
				currentNode = Integer.parseInt(CURRENT_NODE + "");
			}
			// 默认意见为同意。
			if (StringUtils.isBlank(content)) {
				content = ControllerHelper.getMessage(I18nKey.wf_agree);
			}
			if (state == null) {
				state = 0;
			}
			String today = DocumentUtils.getIntance().today();
			try {
				// 取出发送时间为空，并且创建时间最早的那条记录ID
				Integer id = this.jdbcTemplate.queryForObject(
						"select ID from p_fm_work_logs where WORK_ID=? and creator=? and CURRENT_NODE=? and send_time is null order by create_time limit 0,1",
						Integer.class, docVo.getWorkItemId(), userId, currentNode);
				// 如果记录表已经存在该成员的日志则直接更新,没有则插入
				String sql = "update p_fm_work_logs set send_time=?, content=?,state=? where id=?";
				this.jdbcTemplate.update(sql, today, content, state, id);
			} catch (DataAccessException e) {
				insertIntoLogs(docVo, userId, content);
			}
		}
	}

	private void insertIntoLogs(DocumentVO docVo, String userId, String content) {
		if (preset.equals(docVo.getFlowTempleteId())) {
			HttpServletRequest request = ControllerContext.getRequest();
			Object CURRENT_NODE = request.getAttribute("CURRENT_NODE");
			String today = DocumentUtils.getIntance().today();
			String sql = "insert into p_fm_work_logs(CONTENT,WORK_ID,PAGE_ID,CREATOR,SEND_TIME,CURRENT_NODE,state) values(?,?,?,?,?,?,?)";
			Object state = request.getAttribute("state");
			if (state == null) {
				state = 0;
			}
			// 如果没有指定排序则自动生成排序
			if (CURRENT_NODE == null) {
				// 流程预设中不存在则自动生成
				Integer currentNode = this.jdbcTemplate.queryForObject(
						"select max(CURRENT_NODE) from p_fm_work_logs where WORK_ID=?", Integer.class,
						docVo.getWorkItemId());
				this.jdbcTemplate.update(sql, content, docVo.getWorkItemId(), docVo.getDynamicPageId(), userId, today,
						currentNode, state);
			} else {
				this.jdbcTemplate.update(sql, content, docVo.getWorkItemId(), docVo.getDynamicPageId(), userId, today,
						CURRENT_NODE, state);
			}
		}
	}

	/**
	 * 增加消息推送
	 * 
	 * @param workItemId
	 * @param entryId
	 */
	private void addPush(DocumentVO vo, String masterDataSource, SzcloudJdbcTemplate jdbcTemplate1) {
		String workItemId = vo.getWorkItemId();
		String dynamicPageId = vo.getDynamicPageId();
		String fid = vo.getFid();
		if (fid != null && !fid.equals("0")) {
			workItemId = fid;
		}
		String sql;
		String wf_approval_result = ControllerHelper.getMessage(I18nKey.wf_approval_result);
		String wf_approval_reject = ControllerHelper.getMessage(I18nKey.wf_approval_reject);
		sql = "select starter FK_EMP,workid,FK_Node,RDT,FID,concat(title,(CASE wfstate WHEN 13 THEN '"
				+ wf_approval_reject + "' ELSE  '" + wf_approval_result
				+ "' END)) title from wf_generworkflow where wfstate<>12 and workid=? and wfsta=1";
		List<Map<String, Object>> emps = this.jdbcTemplate.queryForList(sql, workItemId);
		if (!emps.isEmpty()) {
			if ((emps.get(0).get("title") + "").contains(wf_approval_result)) {
				sql = "select a.CCTo FK_EMP,a.workid,a.FK_Node,a.RDT,a.FID,concat(b.title,'"
						+ ControllerHelper.getMessage(I18nKey.wf_approval_CC)
						+ "') title from wf_cclist a left join wf_generworkflow b on a.workid=b.workid where a.workid=?";
				emps.addAll(this.jdbcTemplate.queryForList(sql, workItemId));
			}
		} else {
			sql = "SELECT DISTINCT a.FID,a.FK_Node,a.workid,a.FK_EMP,a.RDT,b.title  FROM wf_generworkerlist a left join "
					+ "wf_generworkflow b on a.workid=b.workid WHERE IsPass=0 AND (a.workid=? or a.FID=?) ";
			emps = jdbcTemplate.queryForList(sql, workItemId, workItemId);
		}

		sql = "select optionStr,fieldName from dd_select_option "
				+ "where dynamicPageId=(select dynamicPageId from dd_apps where pcDynamicPageId=? or dynamicPageId=?)";
		List<Map<String, Object>> options = jdbcTemplate.queryForList(sql,dynamicPageId,dynamicPageId);
		if (!emps.isEmpty()) {

			Map<String, Object> data = jdbcTemplate.queryForMap("select * from " + vo.getTableName() + " where id=? ",
					vo.getRecordId());
			// 去app表中查找需要显示的字段
			Object FIELDS = meta.queryObject("SELECT FIELDS FROM dd_apps WHERE dynamicPageId=? or pcDynamicPageId=?",
					dynamicPageId,dynamicPageId);
			Map<String, String> content = new LinkedHashMap<>();
			boolean isEnglish = ControllerHelper.getLang() == Locale.ENGLISH;
			if (FIELDS instanceof String) {
				String[] fields = ((String) FIELDS).split(",");
				for (String str : fields) {
					if (StringUtils.isNotBlank(str)) {
						String[] arr = str.split("=");
						String key = arr[0];
						Object v = data.get(key);
						v = dealSelectVal(options, v + "", key);
						if (v != null && !v.toString().isEmpty()) {
							String name;
							// 如果是英文默认取第三个，如果没配置则取键值
							if (isEnglish) {
								if (arr.length > 2) {
									name = arr[2];
								} else {
									name = key;
								}
							} else {
								name = arr[1];
							}
							content.put(name, v + "");
						}
					}
				}
			}
			String baseUrl = ControllerHelper.getBasePath();
			String gotoUrl;
			for (Map<String, Object> map : emps) {
				String user = map.get("FK_EMP") + "";
				gotoUrl = baseUrl + "dingding/wf/openTask.do?FK_Flow=" + vo.getFlowTempleteId() + "&WorkID="
						+ map.get("workid") + "&FID=" + map.get("FID") + "&FK_Node=" + map.get("FK_Node")
						+ "&dynamicPageId=" + vo.getDynamicPageId();
				content.put("validRepeat", map.get("RDT") + "");
				String title = (String) map.get("title");
				DocumentUtils.getIntance().sendMessage(gotoUrl, "0", content, title, user);
			}
		}
	}

	private String dealSelectVal(List<Map<String, Object>> options, String val, String field) {
		for (int i = 0; i < options.size(); i++) {
			if (field.equals(options.get(i).get("fieldName"))) {
				String optionStr = (String) options.get(i).get("optionStr");
				return DdUtil.getVal(val, optionStr);
			}
		}
		return val;
	}

	/**
	 * 删除指定人以外的待办
	 * 
	 * @param userIds
	 *            指定人
	 * @param WorkID
	 * @param NodeID
	 * @param FK_Flow
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/specifyReceive")
	public Map<String, Object> specifyReceive(String[] userIds, String WorkID, String NodeID, String FK_Flow) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> Emps = new ArrayList<String>();

		String message = "任务发送给指定的人员：";

		for (String userId : userIds) {
			String sql = "select User_Name,Name from p_un_user_base_info where User_ID=" + userId;
			Map<String, Object> maps = this.jdbcTemplate.queryForMap(sql);
			Emps.add(maps.get("User_Name").toString());
			message += "(";
			message += maps.get("User_Name").toString();
			message += ",";
			message += maps.get("Name").toString();
			message += ")";
		}
		// 删除其它人的待办
		String sql = "delete from wf_generworkerlist where fk_Node=" + NodeID + " and workID=" + WorkID
				+ " and fk_flow=" + FK_Flow + " and FK_Emp not in (";
		for (String emp : Emps) {
			sql += emp + ",";
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ")";
		try {
			this.jdbcTemplate.update(sql);
			map.put("success", true);
			map.put("message", "操作成功<br/>" + message);
		} catch (Exception e) {
			map.put("success", true);
			map.put("message", "操作失败");
		}
		return map;
	}

	private String findDynamicpageByflowTempleteId(String flowTempleteId, String entryId) {
		String sql = "SELECT a.id FROM p_fm_dynamicpage a WHERE WORKFLOW_NODE_INFO LIKE CONCAT('%workflowId\":\"',?,'%') AND WORKFLOW_NODE_INFO LIKE CONCAT('%id\":\"',?,'%')  ORDER BY created DESC";
		List<Map<String, Object>> vo = meta.search(sql, flowTempleteId, entryId);
		if (vo.size() == 1) {
			return String.valueOf(vo.get(0).get("id"));
		} else if (vo.size() > 0) {
			sql = "SELECT a.id FROM p_fm_dynamicpage a LEFT JOIN p_un_page_binding b ON a.id=b.PAGEID_PC "
					+ " WHERE b.PAGEID_PC IS NOT NULL AND WORKFLOW_NODE_INFO LIKE CONCAT('%workflowId\":\"',?,'%') AND WORKFLOW_NODE_INFO LIKE CONCAT('%id\":\"',?,'%') "
					+ " ORDER BY created DESC ";
			vo = meta.search(sql, flowTempleteId, entryId);
			if (!vo.isEmpty()) {
				return String.valueOf(vo.get(0).get("id"));
			}
		}
		return null;
	}

	private String renderDocument(HttpServletRequest request, HttpServletResponse response) {
		String dynamicPageId = "";

		taskType = request.getParameter("flag");

		String entryId = request.getParameter("FK_Node");
		String flowTempleteId = request.getParameter("FK_Flow");
		String templateString = "";
		String fid = request.getParameter("FID");
		fid = StringUtils.isNumeric(fid) ? fid : "0";
		String workItemId = request.getParameter("WorkID");
		DocumentVO docVo = null;
		DynamicPageVO pageVO = null;
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> root = new HashMap<String, Object>();
		Map<String, String> others = new HashMap<String, String>();
		Map<String, JSONObject> status = new HashMap<String, JSONObject>();
		List<StoreVO> stores = null;
		Map<String, Map<String, Object>> pageActStatus = new HashMap<String, Map<String, Object>>();
		Map<String, Object> sessionMap = new HashMap<String, Object>();
		List<JSONObject> components = null;
		Map dataMap = null;
		ScriptEngine engine = null;
		String isRead = request.getParameter("IsRead");
		dynamicPageId = request.getParameter("dynamicPageId");
		try {
			// 如果页面指定动态页面则以传来页面为准
			if (StringUtils.isBlank(dynamicPageId)) {
				if (StringUtils.isNotBlank(flowTempleteId)) {// 通过模板打开表单
					// 如果是阅知节点则默认打开1301的绑定页面
					if (entryId.equals("0")) {
						dynamicPageId = findDynamicpageByflowTempleteId(flowTempleteId,
								Integer.parseInt(flowTempleteId) + "01");
					} else {
						dynamicPageId = findDynamicpageByflowTempleteId(flowTempleteId, entryId);

					}
					if (StringUtils.isBlank(dynamicPageId))
						throw new Exception("表单配置错误，没有关联流程!");
					// 新增移动参数配置
					if (HttpRequestDeviceUtils.isMobileDevice(request)) {
						dynamicPageId = PageBindUtil.getInstance().getMPageIDByDefaultId(dynamicPageId);
						if (StringUtils.isBlank(dynamicPageId))
							throw new Exception("表单配置错误，手机表单没做关联!");

					}
				}
			}

			docVo = documentServiceImpl.findDocByWorkItemId(flowTempleteId, workItemId);
			docVo.setDynamicPageId(dynamicPageId);
			docVo.setFlowTempleteId(flowTempleteId);
			docVo.setWorkItemId(workItemId);
			docVo.setEntryId(entryId);
			docVo.setWorkflowId(flowTempleteId);
			docVo.setFid(fid);
			docVo.setTaskId(taskType);

			if (docVo.getId() != null) {
				docVo.setUpdate(true);
				pageVO = formdesignerServiceImpl.findById(docVo.getDynamicPageId());
				docVo.setDynamicPageId(pageVO.getId().toString());
				docVo.setDynamicPageName(pageVO.getName());
			} else {
				pageVO = formdesignerServiceImpl.findById(dynamicPageId);
				// docVo = new DocumentVO();
				// docVo.setFlowTempleteId(flowTempleteId);
				docVo.setUpdate(false);
				docVo.setDynamicPageId(pageVO.getId().toString());
				docVo.setDynamicPageName(pageVO.getName());
			}

			Map<String, String[]> parameterMap = request.getParameterMap();
			for (String key : parameterMap.keySet()) {
				map.put(key, StringUtils.join(parameterMap.get(key), ";"));
			}
			docVo.setRequestParams(map);
			// 拿脚本执行引擎
			engine = ScriptEngineUtils.getScriptEngine(docVo, pageVO);
			engine.put("request", request);
			engine.put("session", request.getSession());
			engine.put("root", root);
			// 分页
			Integer currentPage = 1;
			Integer pageSize = 50;
			if (StringUtils.isNotBlank(request.getParameter("currentPage"))) {
				currentPage = Integer.parseInt(request.getParameter("currentPage"));
			}
			if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			}
			String orderBy = docVo.getRequestParams().get("orderBy");
			String allowOrderBy = docVo.getRequestParams().get("allowOrderBy");
			docVo.setAllowOrderBy(allowOrderBy);
			docVo.setOrderBy(orderBy);
			dataMap = documentServiceImpl.initDocumentDataFlow(currentPage, pageSize, docVo, engine, pageVO);

			docVo.setListParams(dataMap);
			/**
			 * key: componentId value: component 相关信息
			 */
			logger.debug("start find components ");

			JSONObject jcon = new JSONObject();
			jcon.put("relatePageId", pageVO.getId());
			jcon.put("componentType", "");
			components = formdesignerServiceImpl.getComponentByContainerWithColumn(jcon);
			DocUtils.calculateCompents(docVo, others, status, components, dataMap, engine);

			// Map<String, Object> actAttr = new HashMap<String, Object>();
			BaseExample actExample = new BaseExample();
			actExample.createCriteria().andEqualTo("dynamicPage_id", pageVO.getId()).andLike("code",
					StoreService.PAGEACT_CODE + "%");
			stores = storeServiceImpl.selectPagedByExample(actExample, 1, Integer.MAX_VALUE, null);
			DocUtils.calculateStores(map, stores, pageActStatus, engine, others);

			root.put("pageActStatus", pageActStatus);
			// 执行页面加载前脚本
			String preLoadScript = StringEscapeUtils.unescapeHtml4(pageVO.getPreLoadScript());
			if (StringUtils.isNotBlank(preLoadScript)) {
				engine.eval(preLoadScript);
			}

			String dataJson = pageVO.getDataJson();
			if (StringUtils.isNotBlank(dataJson)) {
				for (Iterator<String> it = dataMap.keySet().iterator(); it.hasNext();) {
					String key = it.next();
					Object values = dataMap.get(key);
					if (values != null) {
						Paginator page = (((PageList) values).getPaginator());
						root.put(key + "_paginator", page);
					}
				}
			}

			templateString = documentServiceImpl.getTemplateString(pageVO);
			root.putAll(docVo.getListParams());
			root.put("others", others);
			root.put("status", status);
			root.put("request", request);

			Enumeration sessionEnumeration = request.getSession().getAttributeNames();
			for (; sessionEnumeration.hasMoreElements();) {
				Object o = sessionEnumeration.nextElement();
				String sessionName = o.toString();
				Object values = request.getSession().getAttribute(sessionName);
				sessionMap.put(o.toString(), values);
			}
			root.put("session", sessionMap);
			root.put("vo", docVo);
			root.put("currentPage", currentPage);
			String message = request.getParameter("message");
			if (null != message) {
				root.put("message", message);
			} else {
				root.put("message", "");
			}
			root.put("IsRead", isRead);
			return FreeMarkers.renderString(templateString, root);
		} catch (Exception e) {
			logger.info("ERROR", e);
			return "<html>" + e.getMessage() + "</html>";
		} finally {
			templateString = null;
			docVo = null;
			pageVO = null;
			engine = null;
			if (map != null)
				map.clear();
			if (root != null)
				root.clear();
			if (dataMap != null)
				dataMap.clear();
			if (others != null)
				others.clear();
			if (status != null)
				status.clear();
			if (components != null)
				components.clear();
			if (stores != null)
				stores.clear();
			if (pageActStatus != null)
				pageActStatus.clear();
			if (sessionMap != null)
				sessionMap.clear();
		}

	}

	/**
	 * 打开任务
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("openTask")
	public void openTask(HttpServletResponse response, HttpServletRequest request) throws Exception {

		response.setContentType("text/html;");
		response.getWriter().write(renderDocument(request, response));

	}

	/**
	 * 保存任务
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("excute")
	public Map saveTask(HttpServletResponse response, HttpServletRequest request) throws Exception {
		return executeAct(request, response);
	}

	/**
	 * 发送任务
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("sendTask")
	public Map sendTask(HttpServletResponse response, HttpServletRequest request) throws Exception {
		return executeAct(request, response);
	}

	/**
	 * 撤销任务
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doUnSend")
	public Map doUnSend(HttpServletRequest request) {
		SzcloudJdbcTemplate jdbcTemplate1 = Springfactory.getBean("jdbcTemplate");

		Map resultMap = new HashMap();
		String flowTempleteId = request.getParameter("FK_Flow");
		String workItemId = request.getParameter("WorkID");
		try {
			jdbcTemplate1.beginTranstaion();
			JFlowAdapter.Flow_DoUnSend(resultMap, flowTempleteId, workItemId);
			jdbcTemplate1.commit();
		} catch (Exception e) {
			logger.info("ERROR", e);
			try {
				jdbcTemplate1.rollback();
			} catch (Exception e1) {
				logger.info("ERROR", e1);
			}
			resultMap.put("success", false);
			if (e.getMessage() != null)
				resultMap.put("message", e.getMessage());
			else
				resultMap.put("message", "操作失败!");
		}
		return resultMap;
	}

	@ResponseBody
	@RequestMapping(value = "/shiftWork")
	public Map<String, Object> shiftWork(Long userId) {

		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		Map<String, Object> map = query.getUntreatedData(10000, 0, null, null, user.getUserIdCardNumber(), true);
		List<Map<String, Object>> ls = (List<Map<String, Object>>) map.get("data");
		int count = ls.size();
		PunUserBaseInfoVO punUserBaseInfoVO = userService.findById(userId);
		for (int i = 0; i < count; i++) {

			Dev2Interface.Node_Shift(ls.get(i).get("FK_Flow").toString(),
					Integer.parseInt(ls.get(i).get("FK_Node").toString()),
					Long.parseLong(ls.get(i).get("WorkID").toString()), Long.parseLong(ls.get(i).get("FID").toString()),
					punUserBaseInfoVO.getUserIdCardNumber(), "");
		}
		Map<String, Object> mm = new HashMap<String, Object>();
		mm.put("statu", true);
		return mm;
	}

}
