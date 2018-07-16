package cn.org.awcp.workflow.controller.wf;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("workflow/wf")
public class WorkflowManagerControl {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(WorkflowManagerControl.class);
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Qualifier(value = "jdbcTemplate")
	@Resource
	private JdbcTemplate sqlServerJdbcTemplate;

	/**
	 * 模板维护
	 * 
	 * @return
	 */
	@RequestMapping("templateMaintenance")
	public String templateMaintenance(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize, HttpServletRequest request,
			Model model) {

		String templateTypeId = request.getParameter("templateTypeId");
		String templateName = request.getParameter("templateName");

		logger.debug("=======================typeId:" + templateTypeId + "=====templateName:" + templateName);

		String countSql = "select count(*) from TemplateDefine t1, WorkType t3  WHERE t1.TypeID = t3.TypeID";
		String sql = "SELECT top " + pageSize
				+ " * from (select row_number() over(order by CreateDate DESC) as rownumber, "
				+ "TemplateID, HaveBody, HaveDocument, HaveForm, TemplateName, TemplateCode, Creator, "
				+ "CreateDate, Editor, EditDate, TemplateIsValid, OrderID, TypeName, t1.TypeID "
				+ "FROM TemplateDefine t1, WorkType t3  WHERE t1.TypeID = t3.TypeID ";
		if (StringUtils.isNotEmpty(templateTypeId)) {
			sql += " and t1.TypeID = " + templateTypeId + " ";
			countSql += " and t1.TypeID = " + templateTypeId + " ";
		}
		if (StringUtils.isNotEmpty(templateName)) {
			sql += " and TemplateName like '%" + templateName + "%'";
			countSql += " and TemplateName like '%" + templateName + "%'";
		}
		sql += " )a where rownumber > " + (currentPage - 1) * pageSize;
		logger.debug(sql);

		long count = sqlServerJdbcTemplate.queryForObject(countSql,Long.class);
		Paginator paginator = new Paginator(currentPage, pageSize, (int) count);
		List models = sqlServerJdbcTemplate.queryForList(sql);
		PageList<Model> pageList = new PageList<Model>(models, paginator);

		List templateTypes = sqlServerJdbcTemplate.queryForList("select TypeId,TypeName from WorkType");
		model.addAttribute("models", pageList);
		model.addAttribute("templateTypes", templateTypes);
		model.addAttribute("templateTypeId", templateTypeId);
		model.addAttribute("templateName", templateName);
		model.addAttribute("currentPage", currentPage);
		return "workflow/wf/templateMaintenance";
	}

	/**
	 * 模板维护
	 * 
	 * @return
	 */
	@RequestMapping("lockList")
	public String lockList(@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize, HttpServletRequest request,
			Model model) {

		String countSql = "select count(*) from WorkItemLockList";
		String sql = "SELECT top " + pageSize
				+ " * from (select row_number() over(order by LockDate DESC) as rownumber, "
				+ "WorkEntryID, WorkItemID, EntryID, UserID, WorkItemName, UserName, LockType, LockDate "
				+ "FROM WorkItemLockList";
		sql += " )a where rownumber > " + (currentPage - 1) * pageSize;
		logger.debug(sql);

		long count = sqlServerJdbcTemplate.queryForObject(countSql,Long.class);
		Paginator paginator = new Paginator(currentPage, pageSize, (int) count);
		List models = sqlServerJdbcTemplate.queryForList(sql);
		PageList<Model> pageList = new PageList<Model>(models, paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);
		return "workflow/wf/lockList";
	}

	/**
	 * 模板权限
	 * 
	 * @return
	 */
	@RequestMapping("templateAuthority")
	public String templateAuthority(@RequestParam(value = "id", required = false) Long typeID,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize, HttpServletRequest request,
			Model model) {

		String countSql = "select count(*) from WorkRightDisplay WHERE AdminType = 1 ";
		String sql = "SELECT top " + pageSize
				+ " * from (select row_number() over(order by RightIndex DESC) as rownumber, "
				+ "RightIndex, ModuleName, UserName, AdminType, ReadRight, CreateRight, DeleteRight, "
				+ "QueryRight, FileRight, TailRight, AppRight, AllRight "
				+ "FROM WorkRightDisplay WHERE AdminType = 1 ";
		typeID = 52L;
		if (typeID != null) {
			sql += " and ModuleID =" + typeID + " ";
			countSql += " and ModuleID =" + typeID + " ";
		}
		sql += " )a where rownumber > " + (currentPage - 1) * pageSize;
		logger.debug(sql);

		long count = sqlServerJdbcTemplate.queryForObject(countSql,Long.class);
		Paginator paginator = new Paginator(currentPage, pageSize, (int) count);
		List models = sqlServerJdbcTemplate.queryForList(sql);
		PageList<Model> pageList = new PageList<Model>(models, paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);
		return "workflow/wf/templateAuthority";
	}

	/**
	 * 新建模板权限
	 * 
	 * @return
	 */
	@RequestMapping("createTemplateAuthority")
	public String createTemplateAuthority(@RequestParam(value = "id", required = false) Long typeID,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize, HttpServletRequest request,
			Model model) {

		String countSql = "select count(*) from WorkRightDisplay WHERE AdminType = 1 ";
		String sql = "SELECT top " + pageSize
				+ " * from (select row_number() over(order by RightIndex DESC) as rownumber, "
				+ "RightIndex, ModuleName, UserName, AdminType, ReadRight, CreateRight, DeleteRight, "
				+ "QueryRight, FileRight, TailRight, AppRight, AllRight "
				+ "FROM WorkRightDisplay WHERE AdminType = 1 ";
		if (typeID != null) {
			sql += " and ModuleID =" + typeID + " ";
			countSql += " and ModuleID =" + typeID + " ";
		}
		sql += " )a where rownumber > " + (currentPage - 1) * pageSize;
		logger.debug(sql);

		long count = sqlServerJdbcTemplate.queryForObject(countSql,Long.class);
		Paginator paginator = new Paginator(currentPage, pageSize, (int) count);
		List models = sqlServerJdbcTemplate.queryForList(sql);
		PageList<Model> pageList = new PageList<Model>(models, paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);
		return "workflow/wf/createTemplateAuthority";
	}

	/**
	 * 删除模板权限
	 * 
	 * @return
	 */
	@RequestMapping("deleteTemplateAuthority")
	public String deleteTemplateAuthority(@RequestParam(value = "id", required = false) List<Long> typeID,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "10") int pageSize, HttpServletRequest request,
			Model model) {

		String countSql = "select count(*) from WorkRightDisplay WHERE AdminType = 1 ";
		String sql = "SELECT top " + pageSize
				+ " * from (select row_number() over(order by RightIndex DESC) as rownumber, "
				+ "RightIndex, ModuleName, UserName, AdminType, ReadRight, CreateRight, DeleteRight, "
				+ "QueryRight, FileRight, TailRight, AppRight, AllRight "
				+ "FROM WorkRightDisplay WHERE AdminType = 1 ";
		if (typeID != null) {
			sql += " and ModuleID =" + typeID + " ";
			countSql += " and ModuleID =" + typeID + " ";
		}
		sql += " )a where rownumber > " + (currentPage - 1) * pageSize;
		logger.debug(sql);

		long count = sqlServerJdbcTemplate.queryForObject(countSql,Long.class);
		Paginator paginator = new Paginator(currentPage, pageSize, (int) count);
		List models = sqlServerJdbcTemplate.queryForList(sql);
		PageList<Model> pageList = new PageList<Model>(models, paginator);

		model.addAttribute("models", pageList);
		model.addAttribute("currentPage", currentPage);
		return "workflow/wf/templateAuthority";
	}
}
