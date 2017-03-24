package org.szcloud.framework.workflow.controller.wf;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.formdesigner.application.service.DocumentService;
import org.szcloud.framework.formdesigner.application.service.FormdesignerService;
import org.szcloud.framework.formdesigner.application.service.StoreService;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.application.vo.PageActVO;
import org.szcloud.framework.formdesigner.application.vo.StoreVO;
import org.szcloud.framework.formdesigner.core.domain.design.context.act.PageAct;
import org.szcloud.framework.formdesigner.core.engine.FreeMarkers;
import org.szcloud.framework.formdesigner.utils.DocUtils;
import org.szcloud.framework.formdesigner.utils.DocumentUtils;
import org.szcloud.framework.formdesigner.utils.ScriptEngineUtils;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.unit.service.PunPositionService;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.service.PunUserGroupService;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;
import org.szcloud.framework.venson.service.QueryService;
import org.szcloud.framework.workflow.controller.util.HttpRequestDeviceUtils;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CResult;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.business.TWorkAdmin;
import org.szcloud.framework.workflow.core.business.TWorkItem;
import org.szcloud.framework.workflow.core.entity.CWorkItem;
import org.szcloud.framework.workflow.core.module.FlowChartUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

import BP.Port.WebUser;
import BP.Sys.Frm.MapData;
import BP.WF.Dev2Interface;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Template.Flow;
import BP.WF.Template.Node;
import BP.WF.Template.PubLib.WFState;
import BP.WF.Template.WorkBase.Work;
import net.sf.json.JSONArray;

@Controller
@RequestMapping("workflow/wf")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class WorkflowTaskControl extends BaseController {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(WorkflowTaskControl.class);
	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;// 用户Service

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	private CWorkItem lo_Item = null;

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
			ex.printStackTrace();
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

		JSONArray json = JSONArray.fromObject(lsM);
		model.addAttribute("tableJson", json);

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

	private Map executeAct(final HttpServletRequest request, HttpServletResponse response) {

		org.szcloud.framework.core.domain.SzcloudJdbcTemplate jdbcTemplate1 = Springfactory.getBean("jdbcTemplate");

		String docId = request.getParameter("docId");
		String pageId = request.getParameter("dynamicPageId");
		String flowTempleteId = request.getParameter("FK_Flow");
		String fid = request.getParameter("FID");
		String workItemId = request.getParameter("WorkID");
		String entryId = request.getParameter("FK_Node");
		String workflowId = request.getParameter("workflowId");
		String actId = request.getParameter("actId");
		String update = request.getParameter("update");
		String masterDataSource = request.getParameter("masterDataSource");
		// json格式的对话框参数
		String paras = request.getParameter("slectsUserIds");

		Integer actType = 0;

		String toNode = request.getParameter("toNode");

		logger.debug("===================paras:" + paras);

		// 返回信息
		Map resultMap = new HashMap();
		DynamicPageVO pageVO = null;
		PunUserBaseInfoVO user = null;
		DocumentVO docVo = new DocumentVO();
		Map<String, String> map = null;
		StoreVO store = null;
		PageActVO act = null;
		ScriptEngine engine = null;
		DocumentUtils utils = null;
		Enumeration enumeration = null;
		Map<String, Object> extMap = new HashMap<String, Object>();
		try {
			// 获取当前登录用户
			jdbcTemplate1.beginTranstaion();
			user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
			if (StringUtils.isNotBlank(pageId)) {
				pageVO = formdesignerServiceImpl.findById(Long.parseLong(pageId));
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
			if (docVo.getListParams().size() == 1) {
				masterDataSource = docVo.getListParams().keySet().iterator().next();
			}

			// 校验文档
			utils = (DocumentUtils) engine.get("DocumentUtils");
			if (StringUtils.isNotEmpty(docVo.getRecordId()))
				utils.setDataItem(masterDataSource, "ID", docVo.getRecordId());
			if (utils.validateDocument(docVo)) {
				actType = act.getActType();
				// 根据actType 执行其默认操作
				String acte = request.getParameter("actType");
				actType = StringUtils.isNumeric(acte) ? Integer.parseInt(acte) : act.getActType();
				switch (actType) {
				case 2000:
					break;
				case 2002:
				case 2006:
					// 流程回撤
					JFlowAdapter.Node_SaveWork(masterDataSource, utils, user, resultMap, docVo, flowTempleteId, entryId,
							workItemId);
					break;

				case 2008:
					// 根据选中的人员ID，组装信息
					JFlowAdapter.Node_SaveWork(masterDataSource, utils, user, resultMap, docVo, flowTempleteId, entryId,
							workItemId);

					break;
				case 2011:// 流程流转(发送)
					// 根据选中的人员ID，组装信息
					if (paras != null && !paras.equals("")) {

						JFlowAdapter.Node_SendWork(masterDataSource, utils, user, resultMap, docVo, flowTempleteId,
								workItemId, entryId, getToUsers(request, paras));
					}
					break;
				case 2018:// 流程转发
					// 根据选中的人员ID，组装信息

					JFlowAdapter.Node_SendWork(masterDataSource, utils, user, resultMap, docVo, flowTempleteId,
							workItemId, entryId, getToUsers(request, paras));

					break;
				case 2019:// 流程传阅
					JFlowAdapter.Node_SendWork(masterDataSource, utils, user, resultMap, docVo, flowTempleteId,
							workItemId, entryId);
					break;
				case 2020:// 流程图

					break;
				case 2021:// 流程归档
					break;
				case 2022:// 流程办结
					JFlowAdapter.Flow_DoFlowOverByCoercion(resultMap, flowTempleteId, Integer.valueOf(entryId),
							Long.valueOf(workItemId), Long.valueOf(fid), "");
					break;
				case 2023:// 流程退回
					JFlowAdapter.Flow_returnWork(resultMap, flowTempleteId, Integer.valueOf(entryId),
							Long.valueOf(workItemId), Long.valueOf(fid), "", user.getUserIdCardNumber(), toNode,
							masterDataSource, utils, docVo);
					break;
				case 2024:// 加签
					// 根据选中的人员ID，组装信息
					if (paras != null && !paras.equals("")) {
						String users = getToUsers(request, paras);
						if (users.contains(",")) {
							resultMap.put("message", "只能转发一个对象!");
						} else {
							JFlowAdapter.Node_AskFor(masterDataSource, utils, user, resultMap, docVo, flowTempleteId,
									workItemId, entryId, users);
						}
					}
					break;
				case 2025:// 已阅
					Dev2Interface.Node_DoCCCheckNote(workflowId, Integer.parseInt(entryId), Long.parseLong(workItemId),
							Long.parseLong(fid), "已阅");
					boolean flag = JFlowAdapter.saveExecuteData(utils, docVo, masterDataSource);
					if (flag) {
						resultMap.put("success", true);
						resultMap.put("message", "已阅。");
					} else {
						resultMap.put("success", false);
						resultMap.put("message", "表单数据保存失败");
					}
					break;
				}

			}
			resultMap.put("docId", docVo.getId());
			resultMap.put("WorkItemID", docVo.getWorkItemId());
			resultMap.put("EntryID", docVo.getEntryId());
			resultMap.put("dynamicPageId", pageId);
			jdbcTemplate1.commit();
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			// arg0.setRollbackOnly();
			try {
				jdbcTemplate1.rollback();
			} catch (Exception e1) {
				// Auto-generated catch block
				e1.printStackTrace();
			}
			resultMap.put("success", false);
			if (e.getMessage() != null)
				resultMap.put("message", e.getMessage());
			else
				resultMap.put("message", "操作失败!");

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

	private String getToUsers(final HttpServletRequest request, String paras) {
		StringBuffer sb = new StringBuffer();
		if (HttpRequestDeviceUtils.isMobileDevice(request)) {
			List<Map<String, Object>> person = (List<Map<String, Object>>) JSON.parse(paras);
			paras = person.get(0).get("value").toString();
		}
		if (StringUtils.isBlank(paras)) {
			throw new RuntimeException("请至少选择一个人员。");
		}
		String[] strs = paras.split(",");
		for (String s : strs) {
			if (s != null && !s.equals("")) {
				PunUserBaseInfoVO pbi = userService.findById(Long.parseLong(s));
				sb.append(pbi.getUserIdCardNumber());
				sb.append(",");
			}

		}
		return sb.substring(0, sb.length() - 1).toString();
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
		Enumeration enumeration = null;
		List<JSONObject> components = null;
		Map dataMap = null;
		ScriptEngine engine = null;
		String isRead = request.getParameter("IsRead");

		try {
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
					dynamicPageId = DocumentUtils.getMPageIDByDefaultId(dynamicPageId);
					if (StringUtils.isBlank(dynamicPageId))
						throw new Exception("表单配置错误，手机表单没做关联!");

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
				pageVO = formdesignerServiceImpl.findById(Long.parseLong(docVo.getDynamicPageId()));
				docVo.setDynamicPageId(pageVO.getId().toString());
				docVo.setDynamicPageName(pageVO.getName());
			} else {
				pageVO = formdesignerServiceImpl.findById(Long.parseLong(dynamicPageId));
				// docVo = new DocumentVO();
				// docVo.setFlowTempleteId(flowTempleteId);
				docVo.setUpdate(false);
				docVo.setDynamicPageId(pageVO.getId().toString());
				docVo.setDynamicPageName(pageVO.getName());
			}

			enumeration = request.getParameterNames();
			for (; enumeration.hasMoreElements();) {
				Object o = enumeration.nextElement();
				String name = o.toString();
				String[] values = request.getParameterValues(name);
				map.put(o.toString(), StringUtils.join(values, ";"));
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
			// engine.put("DocumentUtils", new DocumentUtils(docVo, pageVO));
			// Map dataMap = request.getParameterMap();
			// if(dataMap == null)
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
			String value = "";
			if (components != null && components.size() > 0) {
				for (int i = 0; i < components.size(); i++) {
					JSONObject component = components.get(i);
					if (component != null) {
						JSONObject o = new JSONObject();
						value = DocUtils.getComponentValue(component, dataMap, engine);
						int type = component.getIntValue("componentType");
						String optionsText = null;
						switch (type) {
						case 1010: // 计算值、禁用
							o.put("disabled", String
									.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
							break;
						case 1008:// 列框
							// if (pageVO.getPageType() == 1003) {
							String showScript = component.getString("showScript");
							if (StringUtils.isNotBlank(showScript)) {
								Boolean rtn = (Boolean) engine.eval(showScript);
								if (rtn) {
									break;
								} else {
									// 报错
								}
							}
							// }
							// 下面三个 计算值、隐藏
						case 1014:
						case 1015:
						case 1009:
							o.put("hidden", String
									.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
							o.put("isRequired", component.getString("isRequired"));
							break;
						case 1017:
							o.put("hidden", String
									.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
							o.put("disabled", String
									.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
							break;
						case 1006:
							o.put("hidden", String
									.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly", String
									.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled", String
									.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
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
							o.put("hidden", String
									.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly", String
									.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled", String
									.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
							script = component.getString("optionScript");
							if (StringUtils.isNotBlank(script)) {
								ret = (String) engine.eval(script);
							}

							Map<String, JSONObject> mapResult = DocUtils.getStatus(lo_Item, docVo,
									component.getString("name"));
							if (mapResult != null && mapResult.size() > 0) {

								if (mapResult.get(component.getString("name")).get("readonly") != null) {

									markerText = "<label class='radio-inline'><input type='checkbox' disabled='disabled' name='"
											+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";

								} else {

									markerText = "<label class='radio-inline'><input type='checkbox' name='"
											+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";

								}

							} else {

								markerText = "<label class='radio-inline'><input type='checkbox' name='"
										+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";

							}

							optionsText = DocUtils.getOptionText(ret, value, markerText);
							// o.put("optionText", optionsText);
							others.put(component.getString("name"), optionsText);
							break;
						case 1004:
							ret = "";
							o.put("hidden", String
									.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly", String
									.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled", String
									.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
							script = component.getString("optionScript");
							if (StringUtils.isNotBlank(script)) {
								ret = (String) engine.eval(script);
							}

							Map<String, JSONObject> checkbox = DocUtils.getStatus(null, docVo,
									component.getString("name"));
							if (checkbox != null && checkbox.size() > 0) {

								if (checkbox.get(component.getString("name")).get("readonly") != null) {

									markerText = "<label class='radio-inline'><input type='radio' disabled='disabled' name='"
											+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";
								} else {

									markerText = "<label class='radio-inline'><input type='radio' name='"
											+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";
								}

							} else {

								markerText = "<label class='radio-inline'><input type='radio' name='"
										+ component.getString("name") + "' value=\"{0}\" {1}>{2}</label>";
							}

							optionsText = DocUtils.getOptionText(ret, value, markerText);
							// o.put("optionText", optionsText);
							others.put(component.getString("name"), optionsText);
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
						case 1029:
						case 1030:
						case 1031:
						case 1032:
						case 1033:
						case 1037:
						case 1019:
							o.put("hidden", String
									.valueOf(DocUtils.computeStatus(component.getString("hiddenScript"), engine)));
							o.put("readonly", String
									.valueOf(DocUtils.computeStatus(component.getString("readonlyScript"), engine)));
							o.put("disabled", String
									.valueOf(DocUtils.computeStatus(component.getString("disabledScript"), engine)));
						default:
							break;
						}

						status.put(component.getString("name"), o);

					}
				}
			}
			// Map<String, Object> actAttr = new HashMap<String, Object>();
			BaseExample actExample = new BaseExample();
			actExample.createCriteria().andEqualTo("dynamicPage_id", pageVO.getId()).andLike("code",
					StoreService.PAGEACT_CODE + "%");
			stores = storeServiceImpl.selectPagedByExample(actExample, 1, Integer.MAX_VALUE, null);
			if (stores != null && stores.size() > 0) {
				for (StoreVO store : stores) {
					Map<String, Object> actState = new HashMap<String, Object>();
					PageAct act = JSON.parseObject(store.getContent(), PageAct.class);
					String hiddenScript = StringEscapeUtils.unescapeHtml4(act.getHiddenScript());
					if (act.getActType() == 2002) {// 初始化返回按钮的返回页面ID
						actState.put("backId", map.get("backId"));
					}
					// 执行隐藏脚本
					Boolean hiddenStatus = (Boolean) engine.eval(hiddenScript);
					if (hiddenStatus == null) {
						hiddenStatus = false;
					}
					if (act.getActType() == 2002) {// 初始化返回按钮的返回页面ID
						actState.put("hidden", false); // 不隐藏返回按钮
					} else {
						actState.put("hidden", hiddenStatus);
					}

					pageActStatus.put(act.getPageId(), actState);
				}
			}

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
					Paginator page = ((PageList) dataMap.get(key)).getPaginator();
					root.put(key + "_paginator", page);
				}
			}

			templateString = documentServiceImpl.getTemplateString(pageVO);
			root.putAll(docVo.getListParams());
			root.put("others", others);
			root.put("status", status);
			root.put("status", DocUtils.flowAuthorityResolve(lo_Item, docVo, status));
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
			e.printStackTrace();
			return "<html>" + e.getMessage() + "</html>";
		} finally {
			templateString = null;
			docVo = null;
			pageVO = null;
			enumeration = null;
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
	 * 
	 * 抄送已阅
	 * 
	 * Node_CC_SetRead
	 */
	@ResponseBody
	@RequestMapping("ccRead")
	public Map ccRead(HttpServletResponse response, HttpServletRequest request) throws Exception {

		// Dev2Interface.Node_CC_SetRead(nodeID, workid, empNo);
		return null;
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
	 * 删除待办任务
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deletePersonalTask")
	public ModelAndView deletePersonalTask(@RequestParam(value = "id", required = false) List<Long> workItemIDs,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request, Model model)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		if (workItemIDs != null && workItemIDs.size() > 0) {
			String flag = "";
			for (int i = 0; i < workItemIDs.size(); i++) {
				String sql = "select distinct FK_Flow from WF_EmpWorks where WorkID=" + workItemIDs.get(i);
				Map<String, Object> map = this.jdbcTemplate.queryForMap(sql);
				String flowNo = "";
				if (map != null) {
					flowNo = map.get("FK_Flow") == null ? "" : map.get("FK_Flow").toString();
				}
				flag = Dev2Interface.Flow_DoDeleteFlowByFlag(flowNo, workItemIDs.get(i), "删除待办列表", false);
			}

			mv.addObject("message", flag);

		}

		mv.setViewName("redirect:/workflow/wf/listPersonalTasks.do");
		return mv;
	}

	/**
	 * 删除已办任务
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deleteHistoryTask")
	public ModelAndView deleteHistoryTask(@RequestParam(value = "id", required = false) List<Long> workItemIDs,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request, Model model)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		String as_WorkItemIds = "";
		for (long workItemID : workItemIDs) {
			as_WorkItemIds += workItemID + ";";
		}
		if (as_WorkItemIds.indexOf(";") > 0)
			as_WorkItemIds = as_WorkItemIds.substring(0, as_WorkItemIds.lastIndexOf(";"));
		if (StringUtils.isNotEmpty(as_WorkItemIds)) {
			// 获取当前登录用户
			PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_USER);

			CLogon lo_Logon = new CLogon(0, user.getUserIdCardNumber(), user.getName(), "192.168.0.1");

			boolean flag = TWorkAdmin.workItemToRelyed(lo_Logon, as_WorkItemIds);
			if (flag) {
				mv.addObject("message", "删除成功！");
			} else {
				mv.addObject("message", "你没有删除权限！");
			}
		}

		mv.setViewName("redirect:/workflow/wf/listHistoryTasks.do");
		return mv;
	}

	/**
	 * 删除公文模板
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deleteTemplate")
	public ModelAndView deleteTemplate(@RequestParam(value = "id", required = false) List<Long> templateIDs,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request, Model model)
			throws Exception {
		String as_CyclostyleIDs = "";
		for (long templateID : templateIDs) {
			as_CyclostyleIDs += templateID + ";";
		}
		if (as_CyclostyleIDs.indexOf(";") > 0)
			as_CyclostyleIDs = as_CyclostyleIDs.substring(0, as_CyclostyleIDs.lastIndexOf(";"));
		if (StringUtils.isNotEmpty(as_CyclostyleIDs)) {
			// 获取当前登录用户
			PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_USER);

			CLogon lo_Logon = new CLogon(0, user.getUserIdCardNumber(), user.getName(), "192.168.0.1");

			TWorkAdmin.deleteCyclostyles(lo_Logon, as_CyclostyleIDs);
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/workflow/wf/templateList.do");
		return mv;
	}

	/**
	 * 删除草稿
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deleteDraft")
	public ModelAndView deleteDraft(@RequestParam(value = "id", required = false) List<Long> workItemIDs,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request, Model model)
			throws Exception {
		String as_WorkItemIds = "";
		for (long workItemID : workItemIDs) {
			as_WorkItemIds += workItemID + ";";
		}
		if (as_WorkItemIds.indexOf(";") > 0)
			as_WorkItemIds = as_WorkItemIds.substring(0, as_WorkItemIds.lastIndexOf(";"));
		if (StringUtils.isNotEmpty(as_WorkItemIds)) {
			// 获取当前登录用户
			PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_USER);

			CLogon lo_Logon = new CLogon(0, user.getUserIdCardNumber(), user.getName(), "192.168.0.1");

			TWorkAdmin.workItemToRelyed(lo_Logon, as_WorkItemIds);
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/workflow/wf/draftList.do");
		return mv;
	}

	/**
	 * 任务状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("taskStatus")
	public String taskStatus(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request, Model model)
			throws Exception {

		String WorkID = request.getParameter("WorkID");
		int FlowNo = Integer.parseInt(request.getParameter("FlowNo").toString());
		int FID = Integer.parseInt(request.getParameter("FID").toString());

		String sqlOfWhere1 = "";
		if (FID == 0) {
			sqlOfWhere1 = " WHERE (FID=" + WorkID + " OR WorkID=" + WorkID + ")";
		} else {
			sqlOfWhere1 = " WHERE (FID=" + FID + " OR WorkID=" + FID + ")";
		}

		String dialog = request.getParameter("dialog");

		String sql = "SELECT MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan,Msg,NodeData,Exer,Tag FROM ND"
				+ FlowNo + "Track " + sqlOfWhere1 + " ORDER BY RDT asc";

		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sql);

		String dynamicPageId = request.getParameter("dynamicPageId");

		model.addAttribute("ls", ls);

		if (null != dynamicPageId) {
			model.addAttribute("dynamicPageId", dynamicPageId);
		} else {
			model.addAttribute("dynamicPageId", dynamicPageId);
		}

		if (StringUtils.isNotEmpty(dialog))
			return "/workflow/wf/taskStatusDialogList";
		else
			return "/workflow/wf/taskStatusList";

	}

	/**
	 * 流程图
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("flowChart")
	public void flowChart(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		CWorkItem lo_Item = (CWorkItem) SessionUtils.getObjectFromSession("WorkItemKey");
		String flowChartData = FlowChartUtils.getImageBase64String(lo_Item);

		PrintWriter writer = response.getWriter();
		writer.print(flowChartData);
		writer.flush();
		writer.close();
		logger.debug(flowChartData);
	}

	/**
	 * 撤回任务
	 * 
	 * @return
	 */
	@RequestMapping("recallWorkItem")
	public String recallWorkItem(@RequestParam(value = "id", required = false) List<Integer> workItemIDs,
			@RequestParam(value = "id", required = false) List<String> workItemNames, HttpServletRequest request,
			Model model) {
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		CLogon lo_Logon = new CLogon(0, user.getUserIdCardNumber(), user.getName(), "192.168.0.1");
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < workItemIDs.size(); i++) {
			lo_Item = (CWorkItem) SessionUtils.getObjectFromSession("WorkItemKey");
			CResult result = TWorkItem.recallWorkItem(lo_Logon, workItemIDs.get(i), 0);
			if (result.Result) {
				sBuffer.append(workItemNames.get(i) + " : " + result.Information + ";");
			} else {
				sBuffer.append(workItemNames.get(i) + " : " + result.ErrorContent + ";");
			}
		}
		model.addAttribute("message", sBuffer.toString());
		return "workflow/wf/listHistoryTasks";
	}

	/**
	 * 保存任务
	 * 
	 * @return
	 */
	@RequestMapping("toSaveTask")
	public String toSaveTask(HttpServletRequest request, Model model) {

		return "success";
	}

	/**
	 * 撤销任务
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doUnSend")
	public Map doUnSend(HttpServletRequest request) {
		org.szcloud.framework.core.domain.SzcloudJdbcTemplate jdbcTemplate1 = Springfactory.getBean("jdbcTemplate");

		Map resultMap = new HashMap();
		String flowTempleteId = request.getParameter("FK_Flow");
		String workItemId = request.getParameter("WorkID");
		try {
			jdbcTemplate1.beginTranstaion();
			JFlowAdapter.Flow_DoUnSend(resultMap, flowTempleteId, workItemId);
			jdbcTemplate1.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				jdbcTemplate1.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			resultMap.put("success", false);
			if (e.getMessage() != null)
				resultMap.put("message", e.getMessage());
			else
				resultMap.put("message", "操作失败!");
		}
		return resultMap;
	}

	/**
	 * 发送任务
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("toSendTask")
	public ModelAndView toSendTask(@RequestParam(value = "users", required = false) String users,
			HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		ModelAndView mv = new ModelAndView();
		logger.debug("++++++++++++++++++++++++++++++++++++" + users);
		mv.setViewName("redirect:/workflow/wf/listPersonalTasks.do");
		return mv;
	}

	String getFullURL(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		if (request.getQueryString() != null) {
			url.append('?');
			url.append(request.getQueryString());
		}
		return url.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/shiftWork")
	public Map<String, Object> shiftWork(Long userId) {

		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		Map<String, Object> map = query.getUntreatedData(10000, 0, null, null, user.getUserName(), true);
		List<Map<String, Object>> ls = (List<Map<String, Object>>) map.get("data");
		int count = ls.size();
		PunUserBaseInfoVO punUserBaseInfoVO = userService.findById(userId);
		for (int i = 0; i < count; i++) {

			Dev2Interface.Node_Shift(ls.get(i).get("FK_Flow").toString(),
					Integer.parseInt(ls.get(i).get("FK_Node").toString()),
					Long.parseLong(ls.get(i).get("WorkID").toString()), Long.parseLong(ls.get(i).get("FID").toString()),
					punUserBaseInfoVO.getUserName(), "");
		}
		Map<String, Object> mm = new HashMap<String, Object>();
		mm.put("statu", true);
		return mm;
	}

	/**
	 * 转在办
	 */
	private synchronized int taskAlreadyHandle(int item_id, String item_name) {
		// 获取当前登录的用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		// 查询在WF_FAVORITE
		String sql = "SELECT * FROM WF_FAVORITE where USER_ID=" + user.getUserId();
		List<Map<String, Object>> ls = CSqlHandle.getSetBySql(sql);
		int FAV_ID = 0;
		if (ls != null && ls.size() > 0) {
			Map<String, Object> map = ls.get(0);
			FAV_ID = Integer.parseInt(map.get("FAV_ID").toString());
		} else {
			StringBuffer sb = new StringBuffer(
					"INSERT INTO WF_FAVORITE(FAV_NAME,PARENT_ID,USER_ID,USER_NAME,FAV_MEMO) VALUES(");
			sb.append("'已办件',0," + user.getUserId() + ",'" + user.getName() + "','')");
			boolean b = CSqlHandle.execSql(sb.toString());
			if (b) {
				String sql1 = "select max(FAV_ID) from WF_FAVORITE";
				FAV_ID = Integer.parseInt(CSqlHandle.getValueBySql(sql1));
			}
		}
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer(
				"INSERT INTO WF_FAV_ITEM(ITEM_ID,ITEM_NAME,FAV_ID,USER_ID,FAV_DATE,FAV_MEMO) VALUES(");
		sb.append("'" + item_id + "',");
		sb.append("'" + item_name + "',");
		sb.append(FAV_ID + ",");
		sb.append(user.getUserId() + ",");
		sb.append("'" + sdFormat.format(new Date()) + "',");
		sb.append("'')");
		boolean success = CSqlHandle.execSql(sb.toString());
		if (success) {
			return 1;
		}
		return 0;
	}

	/**
	 * 转在办
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("convertTask")
	public ModelAndView convertTask(@RequestParam(value = "id", required = true) List<Integer> workItemIDs,
			HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		ModelAndView modelAndView = new ModelAndView();
		for (Integer itemId : workItemIDs) {
			if (taskAlreadyHandle(itemId, null) > 0) {
				modelAndView.addObject("message", "操作成功！");
			} else {
				modelAndView.addObject("message", "操作失败！");
				break;
			}
		}
		modelAndView.setViewName("redirect:/workflow/wf/inDoingTasks.do");
		return modelAndView;
	}

	/**
	 * 转已办
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("convertToHistoryTask")
	public ModelAndView convertToHistoryTask(@RequestParam(value = "id", required = true) List<Integer> workItemIDs,
			HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		ModelAndView modelAndView = new ModelAndView();
		if (workItemIDs.size() > 0) {
			for (Integer itemId : workItemIDs) {
				String deleteSql = "DELETE FROM WF_FAV_ITEM WHERE USER_ID = ? AND ITEM_ID = ?";
				if (CSqlHandle.getJdbcTemplate().update(deleteSql, user.getUserId(), itemId) > 0) {
					modelAndView.addObject("message", "操作成功！");
				} else {
					modelAndView.addObject("message", "操作失败！");
				}
			}
		}
		modelAndView.setViewName("redirect:/workflow/wf/inDoingTasks.do");
		return modelAndView;
	}

}
