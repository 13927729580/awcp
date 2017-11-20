package cn.org.awcp.mobile.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.service.QueryService;

@Controller
@RequestMapping(value = "/oa/app")
public class AppController {
	@Resource(name = "queryServiceImpl")
	private QueryService query;

	@Autowired
	private MetaModelOperateService metaModelOperateServiceImpl;

	// 公文（待办已办）
	@RequestMapping(value = "/getEntryList")
	@ResponseBody
	public ModelAndView getEntry(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, value = "flow") String flow) {
		ModelAndView mv = new ModelAndView();
		String userName = ControllerHelper.getUser().getUserIdCardNumber();
		Map<String, Object> untreated = query.getUntreatedData(pageSize, (currentPage - 1) * pageSize, flow, null,
				userName, true);
		Map<String, Object> handle = query.getHandledData(pageSize, (currentPage - 1) * pageSize, flow, null, userName);
		Map<String, Object> compile = query.getCompileData(pageSize, (currentPage - 1) * pageSize, flow, null,
				userName);
		mv.addObject("entryList1", untreated.get("data"));
		mv.addObject("totalPage1", untreated.get("count"));// 待办
		mv.addObject("entryList2", handle.get("data"));
		mv.addObject("totalPage2", handle.get("count"));// 已办
		mv.addObject("entryList3", compile.get("data"));
		mv.addObject("totalPage3", compile.get("count"));// 办结
		mv.setViewName("/apps/mobile/oa/document");
		return mv;
	}

	@RequestMapping("getUntreatedData")
	@ResponseBody
	public Object getUntreatedData(
			@RequestParam(required = false, defaultValue = "1", value = "currentPage") int currentPage,
			@RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		// 未处理
		Map<String, Object> untreated = query.getUntreatedData(pageSize, (currentPage - 1) * pageSize, null, null,
				user.getUserIdCardNumber(), true);
		return untreated.get("data");
	}

	@RequestMapping("getHandledData")
	@ResponseBody
	public Object getHandledData(
			@RequestParam(required = false, defaultValue = "1", value = "currentPage") int currentPage,
			@RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		// 已处理
		Map<String, Object> handled = query.getHandledData(pageSize, (currentPage - 1) * pageSize, null, null,
				user.getUserIdCardNumber());
		return handled.get("data");
	}

	@RequestMapping("getCompileData")
	@ResponseBody
	public Object getCompileData(
			@RequestParam(required = false, defaultValue = "1", value = "currentPage") int currentPage,
			@RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize) {
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		// 已处理
		Map<String, Object> compile = query.getCompileData(pageSize, (currentPage - 1) * pageSize, null, null,
				user.getUserIdCardNumber());
		return compile.get("data");
	}

	/**
	 * 
	 * 获取常用部门及其所属人员
	 *
	 * @方法名称：getCommonDeptList()
	 * 
	 * @作者：huangmin
	 * @创建日期：2015年8月18日 下午5:33:52
	 *
	 * @return Map<String,List<Map<String,Object>>>
	 *
	 * @修改记录（修改时间、作者、原因）：
	 */
	@ResponseBody
	@RequestMapping(value = "/commonDeptList")
	public Map<String, List<Map<String, Object>>> getCommonDeptList() {
		Map<String, List<Map<String, Object>>> commonDeptMap = new HashMap<String, List<Map<String, Object>>>();
		// 查出所有部门
		String dept_sql = "SELECT\n" + "	p_un_group.GROUP_ID,\n" + "	p_un_group.GROUP_CH_NAME\n" + "FROM\n"
				+ "	p_un_common_group,\n" + "	p_un_group\n" + "WHERE\n"
				+ "	p_un_common_group.group_id = p_un_group.GROUP_ID\n" + "AND p_un_common_group.user_id = '"
				+ ((PunUserBaseInfoVO) DocumentUtils.getIntance().getUser()).getUserId() + "'\n" + "GROUP BY\n"
				+ "	p_un_group.GROUP_ID\n" + "ORDER BY\n" + "	click_number DESC\n" + "LIMIT 15";

		List<Map<String, Object>> deptlist = this.metaModelOperateServiceImpl.search(dept_sql, null);
		// 查出部门下所有人员
		String mem_sql = "SELECT\n" + "	p_un_user_base_info.USER_ID,\n" + "	p_un_user_base_info.`NAME` USER_NAME,\n"
				+ "	p_un_user_group.GROUP_ID\n" + "FROM\n" + "	p_un_user_base_info,\n" + "	p_un_user_group\n"
				+ "WHERE\n" + "	p_un_user_base_info.USER_ID = p_un_user_group.USER_ID\n" + "GROUP BY\n"
				+ "	p_un_user_base_info.USER_ID";
		List<Map<String, Object>> memlist = this.metaModelOperateServiceImpl.search(mem_sql, null);

		if (deptlist != null && deptlist.size() > 0) {
			for (Map<String, Object> deptMap : deptlist) {
				List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
				String deptId = deptMap.get("GROUP_ID").toString();
				String deptName = deptMap.get("GROUP_CH_NAME").toString();
				if (memlist != null && memlist.size() > 0) {
					for (Map<String, Object> memMap : memlist) {
						String userId = memMap.get("USER_ID").toString();
						String userName = memMap.get("USER_NAME").toString();
						String user_GroupId = memMap.get("GROUP_ID").toString();
						// 将同部门的人员放在一起
						if (user_GroupId.equalsIgnoreCase(deptId)) {
							Map<String, Object> tempMap = new HashMap<String, Object>();
							tempMap.put("userId", userId);
							tempMap.put("userName", userName);
							userList.add(tempMap);
						}
					}
				}
				commonDeptMap.put(deptName, userList);
			}
		}
		return commonDeptMap;
	}

	/**
	 * 
	 * 获取所有部门及其所属人员
	 *
	 * @方法名称：getAllDeptList()
	 * 
	 * @作者：huangmin
	 * @创建日期：2015年8月18日 下午5:33:52
	 *
	 * @return Map<String,List<Map<String,Object>>>
	 *
	 * @修改记录（修改时间、作者、原因）：
	 */
	@ResponseBody
	@RequestMapping(value = "/allDeptList")
	public Map<String, List<Map<String, Object>>> getAllDeptList() {
		Map<String, List<Map<String, Object>>> allDeptMap = new HashMap<String, List<Map<String, Object>>>();
		String dept_sql = "SELECT\n" + "	p_un_group.GROUP_ID,\n" + "	p_un_group.GROUP_CH_NAME\n" + "FROM\n"
				+ "	p_un_group,\n" + "	p_un_user_group\n" + "WHERE\n"
				+ "	p_un_group.GROUP_ID = p_un_user_group.GROUP_ID\n" + "AND p_un_group.GROUP_TYPE = 2\n" + "GROUP BY\n"
				+ "	p_un_group.GROUP_ID";
		List<Map<String, Object>> deptlist = this.metaModelOperateServiceImpl.search(dept_sql, null);
		// 查出部门下所有人员
		String mem_sql = "SELECT\n" + "	p_un_user_base_info.USER_ID,\n" + "	p_un_user_base_info.`NAME` USER_NAME,\n"
				+ "	p_un_user_group.GROUP_ID\n" + "FROM\n" + "	p_un_user_base_info,\n" + "	p_un_user_group\n"
				+ "WHERE\n" + "	p_un_user_base_info.USER_ID = p_un_user_group.USER_ID\n" + "GROUP BY\n"
				+ "	p_un_user_base_info.USER_ID";
		List<Map<String, Object>> memlist = this.metaModelOperateServiceImpl.search(mem_sql, null);
		if (deptlist != null && deptlist.size() > 0) {
			for (Map<String, Object> deptMap : deptlist) {
				List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
				String deptId = deptMap.get("GROUP_ID").toString();
				String deptName = deptMap.get("GROUP_CH_NAME").toString();
				if (memlist != null && memlist.size() > 0) {
					for (Map<String, Object> memMap : memlist) {
						String userId = memMap.get("USER_ID").toString();
						String userName = memMap.get("USER_NAME").toString();
						String user_GroupId = memMap.get("GROUP_ID").toString();
						// 将同部门的人员放在一起
						if (user_GroupId.equalsIgnoreCase(deptId)) {
							Map<String, Object> tempMap = new HashMap<String, Object>();
							tempMap.put("userId", userId);
							tempMap.put("userName", userName);
							userList.add(tempMap);
						}
					}
				}
				allDeptMap.put(deptName, userList);
			}
		}
		return allDeptMap;
	}

	@RequestMapping(value = "/newTask")
	@ResponseBody
	public ModelAndView templateList() {
		ModelAndView mv = new ModelAndView();

		List<Map<String, Object>> workTypeList = metaModelOperateServiceImpl.search("select No,Name from wf_flowsort");
		mv.addObject("workTypeList", workTypeList);
		mv.setViewName("/apps/mobile/oa/newTask");
		return mv;
	}

	@RequestMapping(value = "/gettemplateListById")
	@ResponseBody
	public List<Map<String, Object>> getTaskListById(int id) {
		String sql = "SELECT WF_Flow.No, WF_Flow.Name,IFNULL(WF_Flow.FK_FlowSort, '01') "
				+ "FK_FlowSort, WF_FlowSort_FK_FlowSort.Name AS FK_FlowSortText,"
				+ "IFNULL(WF_Flow.FlowRunWay,0) FlowRunWay, WF_Flow.RunObj,"
				+ " WF_Flow.Note, WF_Flow.RunSQL,IFNULL(WF_Flow.NumOfBill,0)"
				+ " NumOfBill,IFNULL(WF_Flow.NumOfDtl,0) NumOfDtl," + "IFNULL(WF_Flow.FlowAppType,0) FlowAppType,"
				+ "IFNULL(WF_Flow.ChartType,1) ChartType, "
				+ "IFNULL(WF_Flow.IsCanStart,1) IsCanStart, IFNULL( round(WF_Flow.AvgDay ,4) ,0.0) AvgDay,IFNULL(WF_Flow.IsFullSA,0) IsFullSA,IFNULL(WF_Flow.IsMD5,0) IsMD5,IFNULL(WF_Flow.Idx,0) Idx,IFNULL(WF_Flow.TimelineRole,0) TimelineRole, WF_Flow.Paras, WF_Flow.PTable,"
				+ "IFNULL(WF_Flow.Draft,0) Draft,IFNULL(WF_Flow.DataStoreModel,0) DataStoreModel, WF_Flow.TitleRole, WF_Flow.FlowMark, WF_Flow.FlowEventEntity, WF_Flow.HistoryFields,IFNULL(WF_Flow.IsGuestFlow,0) IsGuestFlow, WF_Flow.BillNoFormat,"
				+ " WF_Flow.FlowNoteExp,IFNULL(WF_Flow.DRCtrlType,0) DRCtrlType,IFNULL(WF_Flow.StartLimitRole,0) StartLimitRole, WF_Flow.StartLimitPara, WF_Flow.StartLimitAlert,IFNULL(WF_Flow.StartLimitWhen,0) "
				+ "StartLimitWhen,IFNULL(WF_Flow.StartGuideWay,0)" + " StartGuideWay, WF_Flow.StartGuidePara1,"
				+ " WF_Flow.StartGuidePara2, WF_Flow.StartGuidePara3," + "IFNULL(WF_Flow.IsResetData,0) IsResetData,"
				+ "IFNULL(WF_Flow.IsLoadPriData,0) IsLoadPriData,"
				+ "IFNULL(WF_Flow.CFlowWay,0) CFlowWay, WF_Flow.CFlowPara,"
				+ "IFNULL(WF_Flow.IsBatchStart,0) IsBatchStart, "
				+ "WF_Flow.BatchStartFields,IFNULL(WF_Flow.IsAutoSendSubFlowOver,0) "
				+ "IsAutoSendSubFlowOver, WF_Flow.Ver,IFNULL(WF_Flow.DTSWay,0) DTSWay, WF_Flow.DTSBTable, "
				+ "WF_Flow.DTSBTablePK,IFNULL(WF_Flow.DTSTime,0) DTSTime, "
				+ "WF_Flow.DTSSpecNodes,IFNULL(WF_Flow.DTSField,0) DTSField,"
				+ " WF_Flow.DTSFields FROM WF_Flow LEFT JOIN WF_FlowSort AS WF_FlowSort_FK_FlowSort ON "
				+ "WF_Flow.FK_FlowSort=WF_FlowSort_FK_FlowSort.No WHERE (1=1) AND"
				+ " ( ( WF_Flow.No IN ( SELECT FK_Flow FROM V_FlowStarter " + "WHERE FK_Emp='"
				+ ControllerHelper.getUser().getUserIdCardNumber() + "' ) ) AND "
				+ "( WF_Flow.IsCanStart = 1)) AND WF_Flow.FK_FlowSort='" + id + "' ORDER BY WF_Flow.FK_FlowSort,"
				+ "WF_Flow.Idx";

		return metaModelOperateServiceImpl.search(sql);
	}

}
