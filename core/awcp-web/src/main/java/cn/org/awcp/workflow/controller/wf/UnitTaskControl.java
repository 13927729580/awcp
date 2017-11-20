package cn.org.awcp.workflow.controller.wf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.service.FormdesignerService;
import cn.org.awcp.formdesigner.application.service.PrintService;
import cn.org.awcp.formdesigner.application.service.StoreService;
import cn.org.awcp.metadesigner.application.MetaModelService;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

@Controller
@RequestMapping("workflow/wf")
public class UnitTaskControl extends BaseController {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Qualifier(value = "jdbcTemplate")
	@Resource
	private JdbcTemplate sqlServerJdbcTemplate;

	private static final int MAX_EXCEL_PAGE_SIZE = 64000;
	@Autowired
	private FormdesignerService formdesignerServiceImpl;
	@Autowired
	private DocumentService documentServiceImpl;

	@Autowired
	private StoreService storeServiceImpl;

	@Autowired
	private PrintService printServiceImpl;

	@Autowired
	private MetaModelService metaModelServiceImpl;

	// 1：表示待办任务，2：表示已办任务，在打开任务时赋值
	private String taskType = "";

	/**
	 * 单位-待办任务
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("unitPersonalTasks")
	public String unitPersonalTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request, Model model)
			throws IOException {
		// 获取当前登录用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);

		String workItemName = request.getParameter("workItemName");

		String extendSix = request.getParameter("extendSix");

		logger.debug("============================workItemName:" + workItemName);

		String sql = "SELECT top " + pageSize
				+ " * from (select row_number() over(order by CreateDate DESC) as rownumber,* from V_WF_INST   "
				+ "WHERE WorkItemStatus = 0 ";
		// + "AND StateID IN (0, 1, 2, 7) "
		// + "AND (TypeID IN (SELECT TypeID FROM WorkType WHERE (AppType & 1) =
		// 1))";
		String countSql = "select COUNT(*) from V_WF_INST   " + "WHERE WorkItemStatus = 0 ";
		// + "AND StateID IN (0, 1, 2, 7) "
		// + "AND (TypeID IN (SELECT TypeID FROM WorkType WHERE (AppType & 1) =
		// 1)) ";

		if (StringUtils.isNotEmpty(workItemName)) {
			sql += " and WorkItemName like '%" + workItemName + "%' ";
			countSql += " and WorkItemName like '%" + workItemName + "%' ";
		}
		if (StringUtils.isNotEmpty(extendSix)) {
			sql += " and ExtendSix = '" + extendSix + "'";
			countSql += " and ExtendSix = '" + extendSix + "'";
		}
		sql += " )a where rownumber > " + (currentPage - 1) * pageSize;

		long count = sqlServerJdbcTemplate.queryForLong(countSql);
		Paginator paginator = new Paginator(currentPage, pageSize, (int) count);
		List<Map<String, Object>> models = sqlServerJdbcTemplate.queryForList(sql);

		int size = models.size();
		List modelList = new ArrayList();
		for (int i = 0; i < size; i++) {
			Map<String, Object> map = models.get(i);
			map.put("ID", String.format("%0" + String.valueOf(count).length() + "d",
					count - ((currentPage - 1) * pageSize) - i));
			modelList.add(map);
		}

		PageList<Model> pageList = new PageList<Model>(modelList, paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("workItemName", workItemName);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("ExtendSix", extendSix);

		String message = request.getParameter("message");
		if (null != message) {
			model.addAttribute("message", message);
		} else {
			model.addAttribute("message", "");
		}
		return "workflow/wf/unitPersonalTasks";
	}

	/**
	 * 单位-总办任务
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("unitAllTasks")
	public String unitAllTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request, Model model)
			throws IOException {
		// 获取当前登录用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);

		String workItemName = request.getParameter("workItemName");

		String extendSix = request.getParameter("extendSix");

		logger.debug("============================workItemName:" + workItemName);

		String sql = "SELECT top " + pageSize
				+ " * from (select row_number() over(order by CreateDate DESC) as rownumber,* "
				+ "from V_WF_INST  WHERE 1=1 " + "AND WorkItemStatus IN (0, 1) ";
		// + "AND TypeID IN (SELECT TypeID FROM WorkType WHERE (AppType & 1) =
		// 1)) ";
		String countSql = "select COUNT(*) from V_WF_INST  WHERE 1=1 " + "AND WorkItemStatus IN (0, 1) ";
		// + "AND TypeID IN (SELECT TypeID FROM WorkType WHERE (AppType & 1) =
		// 1)) ";

		if (StringUtils.isNotEmpty(workItemName)) {
			sql += " and WorkItemName like '%" + workItemName + "%' ";
			countSql += " and WorkItemName like '%" + workItemName + "%' ";
		}
		if (StringUtils.isNotEmpty(extendSix)) {
			sql += " and ExtendSix = '" + extendSix + "'";
			countSql += " and ExtendSix = '" + extendSix + "'";
		}
		sql += " )a where rownumber > " + (currentPage - 1) * pageSize;

		long count = sqlServerJdbcTemplate.queryForLong(countSql);
		Paginator paginator = new Paginator(currentPage, pageSize, (int) count);
		List<Map<String, Object>> models = sqlServerJdbcTemplate.queryForList(sql);

		int size = models.size();
		List modelList = new ArrayList();
		for (int i = 0; i < size; i++) {
			Map<String, Object> map = models.get(i);
			map.put("ID", String.format("%0" + String.valueOf(count).length() + "d",
					count - ((currentPage - 1) * pageSize) - i));
			modelList.add(map);
		}

		PageList<Model> pageList = new PageList<Model>(modelList, paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("workItemName", workItemName);
		model.addAttribute("ExtendSix", extendSix);

		String message = request.getParameter("message");
		if (null != message) {
			model.addAttribute("message", message);
		} else {
			model.addAttribute("message", "");
		}
		return "workflow/wf/unitAllTasks";
	}

	/**
	 * 单位-已办任务
	 * 
	 * @return
	 */
	@RequestMapping("unitHistoryTasks")
	public String unitHistoryTasks(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request,
			Model model) {
		// 获取当前登录用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);

		String workItemName = request.getParameter("workItemName");

		String extendSix = request.getParameter("extendSix");

		logger.debug("============================workItemName:" + workItemName);

		String sql = "SELECT top " + pageSize
				+ " * from (select row_number() over(order by CreateDate DESC) as rownumber,* "
				+ "from V_WF_INST    WHERE 1 = 1 " + "AND WorkItemStatus IN (1) ";
		// + "AND TypeID IN (SELECT TypeID FROM WorkType WHERE (AppType & 1) =
		// 1)) ";
		String countSql = "select COUNT(*) from V_WF_INST   WHERE 1 = 1 " + "AND WorkItemStatus IN (1) ";
		// + "AND TypeID IN (SELECT TypeID FROM WorkType WHERE (AppType & 1) =
		// 1)) ";

		if (StringUtils.isNotEmpty(workItemName)) {
			sql += " and WorkItemName like '%" + workItemName + "%' ";
			countSql += " and WorkItemName like '%" + workItemName + "%' ";
		}
		if (StringUtils.isNotEmpty(extendSix)) {
			sql += " and ExtendSix = '" + extendSix + "'";
			countSql += " and ExtendSix = '" + extendSix + "'";
		}
		sql += " )a where rownumber > " + (currentPage - 1) * pageSize;

		long count = sqlServerJdbcTemplate.queryForLong(countSql);
		Paginator paginator = new Paginator(currentPage, pageSize, (int) count);
		List<Map<String, Object>> models = sqlServerJdbcTemplate.queryForList(sql);

		int size = models.size();
		List modelList = new ArrayList();
		for (int i = 0; i < size; i++) {
			Map<String, Object> map = models.get(i);
			map.put("ID", String.format("%0" + String.valueOf(count).length() + "d",
					count - ((currentPage - 1) * pageSize) - i));
			modelList.add(map);
		}

		PageList<Model> pageList = new PageList<Model>(modelList, paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("workItemName", workItemName);
		model.addAttribute("ExtendSix", extendSix);

		String message = request.getParameter("message");
		if (null != message) {
			model.addAttribute("message", message);
		} else {
			model.addAttribute("message", "");
		}
		return "workflow/wf/unitHistoryTasks";
	}

	/**
	 * 任务状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("unitTaskStatus")
	public String unitTaskStatus(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "15") int pageSize, HttpServletRequest request, Model model)
			throws Exception {
		if (!StringUtils.isNotEmpty(request.getParameter("id")))
			return null;

		int workItemID = Integer.parseInt(request.getParameter("id"));

		String dialog = request.getParameter("dialog");

		String taskTypes = request.getParameter("taskType");
		if (StringUtils.isNotEmpty(taskTypes))
			taskType = taskTypes;

		String sql = "SELECT WEID, WorkItemID, EntryID, StateID, EntryType, ReciType, RecipientID, Recipients, ProxyID, Proxyor, "
				+ "RUserID, RUserName, DeptID, DeptName, InceptDate, ReadDate, FinishedDate, OverDate, AlterDate, "
				+ "CallDate, ParentID, OriginalID, ActivityID, ActivityName, ChoiceContent, EntryContent, "
				+ "dbo.FN_SendRecipients(WorkItemID, EntryID) AS SendRecipients FROM EntryInst WHERE  EntryType = 2 and WorkItemID = "
				+ String.valueOf(workItemID);

		List models = sqlServerJdbcTemplate.queryForList(sql);

		Paginator paginator = new Paginator(currentPage, pageSize, (int) models.size());
		PageList<Model> pageList = new PageList<Model>(models, paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("taskType", taskType);

		String dynamicPageId = request.getParameter("dynamicPageId");
		if (null != dynamicPageId) {
			model.addAttribute("dynamicPageId", dynamicPageId);
		} else {
			model.addAttribute("dynamicPageId", dynamicPageId);
		}

		String id = request.getParameter("id");
		if (null != id) {
			model.addAttribute("id", id);
		} else {
			model.addAttribute("id", "");
		}

		if (StringUtils.isNotEmpty(dialog))
			return "/workflow/wf/taskStatusDialogList";
		else
			return "/workflow/wf/unitTaskStatusList";
	}

}
