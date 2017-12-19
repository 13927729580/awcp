package cn.org.awcp.formdesigner.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import BP.WF.Dev2Interface;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.venson.concurrent.TaskReminder;
import cn.org.awcp.venson.concurrent.TaskRepeater;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.dingding.Env;
import cn.org.awcp.venson.dingding.helper.AuthHelper;
import cn.org.awcp.venson.dingding.message.OAMessage;
import cn.org.awcp.venson.dingding.message.OAMessage.Body;
import cn.org.awcp.venson.dingding.message.OAMessage.Body.Form;
import cn.org.awcp.venson.dingding.message.OAMessage.Head;
import cn.org.awcp.venson.dingding.service.DDRequestService;
import cn.org.awcp.venson.service.FileService;
import cn.org.awcp.venson.util.LocalStorage;

/**
 * 文档操作
 */
public class DocumentUtils extends BaseUtils {

	private static DocumentUtils utils = new DocumentUtils();

	public static DocumentUtils getIntance() {
		return utils;
	}

	private DocumentUtils() {
	}

	/**
	 * 获取当前文档
	 * 
	 * @return
	 */
	public DocumentVO getCurrentDocument() {
		return ControllerContext.getDoc();
	}

	// *******************************欧盛文档管理-begin************************************************
	/**
	 * 获取某个父节点下面的所有子节点
	 * 
	 * @param menuList
	 * @param pid
	 * @return
	 */
	public List<Map<String, Object>> treeMenuList(List<Map<String, Object>> menuList, String pid) {
		List<Map<String, Object>> childMenu = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> mu : menuList) {
			// 遍历出父id等于参数的id，add进子节点集合
			if (pid.equals(mu.get("pid").toString())) {
				// 递归遍历下一级
				List<Map<String, Object>> temp = treeMenuList(menuList, mu.get("ID").toString());
				childMenu.add(mu);
				childMenu.addAll(temp);
			}
		}
		return childMenu;
	}

	/**
	 * oa_os_directory中获取当前节点所有子节点
	 * 
	 * @param id
	 * @return
	 */
	public String getchild(String id) {
		String parentIds = "";
		String sql = "select ID ,name,IFNULL(pid,'') as pid from oa_os_directory";
		List<Map<String, Object>> menuList = this.excuteQueryForList(sql);
		List<Map<String, Object>> list = treeMenuList(menuList, id);
		for (Map<String, Object> mu : list) {
			parentIds += mu.get("ID") + ",";
		}
		return parentIds.substring(0, parentIds.length() - 0);
	}

	/**
	 * oa_os_directorytemplate中获取所有子节点
	 * 
	 * @param id
	 * @return
	 */
	public String getDirTemplateChild(String id) {
		String parentIds = "";
		String sql = "select ID ,name,IFNULL(pid,'') as pid from oa_os_directorytemplate";
		List<Map<String, Object>> menuList = this.excuteQueryForList(sql);
		List<Map<String, Object>> list = treeMenuList(menuList, id);
		for (Map<String, Object> mu : list) {
			parentIds += mu.get("ID") + ",";
		}
		return parentIds.substring(0, parentIds.length() - 0);
	}

	/**
	 * 判断路径是否属于目录
	 * 
	 * @param path
	 * @return
	 */
	public String isFilePath(String path) {
		String message = "1";
		File file = new File(path);
		if (!file.isDirectory()) {
			message = "2";
		}
		return message;
	}

	// *******************************欧盛业务管理-end************************************************

	// *******************************钉钉发送OA消息-begin************************************************
	public boolean sendMessage(String url, String type, String json, String title, String ids, String agentId) {
		return sendMessage(url, type, JSON.parseObject(json, Map.class), title, ids, agentId, null, null);
	}

	public boolean sendMessage(String url, String type, String json, String title, String ids) {
		return sendMessage(url, type, JSON.parseObject(json, Map.class), title, ids, null, null, null);
	}

	public boolean sendMessage(String url, String type, Map<?, ?> content, String title, String ids) {
		return sendMessage(url, type, content, title, ids, null, null, null);
	}

	public boolean isCanDo() {
		HttpServletRequest request = ControllerContext.getRequest();
		String WorkID = request.getParameter("WorkID");
		String FK_Node = request.getParameter("FK_Node");
		if (StringUtils.isNumeric(WorkID) && StringUtils.isNotBlank(FK_Node))
			return Dev2Interface.Flow_IsCanDoCurrentWork(FK_Node, Long.parseLong(WorkID),
					ControllerHelper.getUser().getUserIdCardNumber());
		return false;
	}

	public boolean isStarter() {
		HttpServletRequest request = ControllerContext.getRequest();
		String WorkID = request.getParameter("WorkID");
		String userId = this.getUser().getUserIdCardNumber();
		if (StringUtils.isNumeric(WorkID))
			return this.jdbcTemplate.queryForObject(
					"select count(1) from wf_generworkflow where workid=? and starter=? and wfsta<>1 ", Integer.class,
					WorkID, userId) == 1;
		return false;
	}

	/**
	 * 钉钉OA消息发送
	 * 
	 * @param url
	 *            客户端点击消息时跳转到的H5地址
	 * @param type
	 *            0：用户，1：群组
	 * @param content
	 *            消息body键值对内容
	 * @param title
	 *            标题
	 * @param ids
	 *            推送用户或群组的ID，逗号分隔,
	 * @param agentId
	 *            微应用Id
	 * @param img
	 *            消息body的图片
	 * @param bodyContent
	 *            消息body的content
	 * @return
	 */
	public boolean sendMessage(String url, String type, Map<?, ?> content, String title, String ids, String agentId,
			String img, String bodyContent) {
		// ,替换成|
		ids = ids.replaceAll(",", "|");
		OAMessage oa = new OAMessage();
		Head head = new Head();
		head.bgcolor = "FFBBBBBB";
		head.text = title;
		oa.head = head;
		Body body = new Body();
		oa.body = body;
		String userName = ControllerHelper.getUser().getName();
		body.author = userName;
		body.title = title;
		// 如果agentId为空则先从参数中查找，如果没找到则赋予默认值
		if (StringUtils.isBlank(agentId)) {
			agentId = ControllerContext.getRequest().getParameter(Env.PARAM_AGENT_ID_NAME);
			if (StringUtils.isBlank(agentId)) {
				agentId = Env.DEFAULT_AGENT_ID;
			}
		}
		String insertSql = "insert into dd_send_message(MSG_ID,MSG_TYPE,MSG_CONTENT,MSG_URL,MSG_RECEIVE,CREATOR,CREATE_TIME,TO_USER,forbiddenUserId,invalidparty,invaliduser) values(?,?,?,?,?,?,?,?,?,?,?)";
		String gotoUrl = url;
		if (content.containsKey("validRepeat")) {
			gotoUrl += "&validRepeat="
					+ content.get("validRepeat").toString().replaceAll(StringUtils.SPACE, StringUtils.EMPTY).trim();
			content.remove("validRepeat");
			if (this.jdbcTemplate.queryForObject(
					"select COUNT(1) from dd_send_message where MSG_URL=? and MSG_RECEIVE=?", Integer.class, gotoUrl,
					ids) != 0) {
				logger.debug("钉钉消息发送:该消息已经发送过，禁止再发。");
				return false;
			}
		}
		// 表单内容
		List<Form> forms = new ArrayList<>();
		body.form = forms;
		for (Object m : content.keySet()) {
			Form form = new Form();
			form.key = m + "：";
			form.value = content.get(m) + "";
			if (StringUtils.isNotBlank(form.value)) {
				forms.add(form);
			}
		}
		if (StringUtils.isNotBlank(img)) {
			body.image = img;
		}
		if (StringUtils.isNotBlank(bodyContent)) {
			body.content = bodyContent;
		}
		// 打开时不显示分享和菜单
		if (gotoUrl.contains("?")) {
			gotoUrl += "&showmenu=false&dd_share=false";
		} else {
			gotoUrl += "?showmenu=false&dd_share=false";

		}
		oa.message_url = gotoUrl;
		oa.pc_message_url = gotoUrl;
		JSONObject jobj;
		if ("0".equals(type)) {
			jobj = DDRequestService.sendMessage(AuthHelper.getAccessToken(), ids, "", agentId, oa.type(), oa);
		} else {
			jobj = DDRequestService.sendMessage(AuthHelper.getAccessToken(), "", ids, agentId, oa.type(), oa);
		}
		if (jobj != null) {
			if (jobj.get("errcode") == null) {
				// 发送消息保存至服务器
				excuteUpdate(insertSql, jobj.get("messageId"), oa.type(), JSON.toJSONString(oa), gotoUrl, ids, userName,
						today(), type, jobj.get("forbiddenUserId"), jobj.get("invalidparty"), jobj.get("invaliduser"));
				logger.debug("钉钉消息发送成功:" + jobj.get("messageId"));
			} else {
				// 发送消息保存至服务器
				excuteUpdate(insertSql, "ERROR", oa.type(), JSON.toJSONString(oa), gotoUrl, ids, userName, today(),
						type, jobj.get("errmsg"), jobj.get("errmsg"), jobj.get("errmsg"));
				logger.debug("钉钉消息发送失败：" + jobj.get("errmsg"));
			}
		} else {
			// 发送消息保存至服务器
			excuteUpdate(insertSql, "ERROR", oa.type(), JSON.toJSONString(oa), gotoUrl, ids, userName, today(), type,
					"ERROR", "ERROR", "ERROR");
			logger.debug("钉钉消息发送失败");
			return false;
		}
		return true;
	}
	// *******************************钉钉发送OA消息-end************************************************

	/**
	 * 根据ID查询指定表并返回指定字段
	 * 
	 * @param table
	 *            要查询的表名
	 * @param field
	 *            要查询的字段
	 * @param idName
	 *            主键
	 * @param value
	 *            主键值
	 */
	public Object queryObjectById(String table, String field, String idName, Object value) {
		try {
			return this.excuteQuery("select " + field + " from " + table + " where " + idName + "='" + value + "'")
					.get(field);
		} catch (Exception e) {
			return null;
		}
	}

	public void addReminder(String id, int type) {
		new TaskReminder(id, type);
	}

	public void addRepeat(String id, int type) {
		try {
			// 得到默认的调度器
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			// 定义当前调度器的具体作业对象
			JobDetail jobDetail = JobBuilder.newJob(TaskRepeater.class)
					.withIdentity("cronTriggerDetail_" + id, "cronTriggerDetailGrounp").build();
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			jobDataMap.put("id", id);
			jobDataMap.put("type", type);
			jobDataMap.put("count", 1);
			// 在任务调度器中，使用任务调度器的 CronScheduleBuilder 来生成一个具体的 CronTrigger 对象
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger", "cronTrigger")
					.withSchedule(CronScheduleBuilder.cronSchedule(TaskRepeater.getCorn(type))).build();
			scheduler.scheduleJob(jobDetail, trigger);
			// 开始调度任务
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public String getLocal() {
		if (ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE) {
			return "zh";
		} else {
			return "en";
		}
	}

	public void clearDDToken() {
		LocalStorage.remove(Env.CORP_ID);
	}

	/**
	 * crmtile中佣金发放
	 * @param receiptId	: 收据ID
	 * @param money : 收据金额
	 * @param orderId : 订单ID
	 */
	public void setCommission(String receiptId,Double money,String orderId){
		String sql = "select salesman,shoppingGuide,designer,sender,customerType from crmtile_order where ID=?";
		Map<String,Object> orderInfo = jdbcTemplate.queryForMap(sql,orderId);
		long salesman =  orderInfo.get("salesman")==null?0:(long)orderInfo.get("salesman");
		long shoppingGuide = orderInfo.get("shoppingGuide")==null?0:(long)orderInfo.get("shoppingGuide");
		long designer =orderInfo.get("designer")==null?0:(long)orderInfo.get("designer");
		long sender = orderInfo.get("sender")==null?0:(long)orderInfo.get("sender");
		String customerType = orderInfo.get("customerType")==null?"":(String) orderInfo.get("customerType");
		String subSql = "";
		if("1".equals(customerType)){//整装客户
			subSql = "(select all_proportion from crmtile_employee_wages_info where user_id=?) ";
		} else{
			subSql = "(select half_proportion from crmtile_employee_wages_info where user_id=?) ";
		}		
		if(StringUtils.isNotBlank(receiptId)){//当订单审核通过后，分配佣金
			sql = "insert into crmtile_employee_commission_record(ID,receiptId,money,type) values(uuid(),?,?,?)";
			jdbcTemplate.update(sql,receiptId,money,"1");
			jdbcTemplate.update(sql,receiptId,money,"2");
			jdbcTemplate.update(sql,receiptId,money,"3");
			jdbcTemplate.update(sql,receiptId,money,"4");			
		} 
		sql = "update crmtile_employee_commission_record set user_id=?,created=?,money=money*" + subSql + 
			  "where receiptId in (select ID from crmtile_receipt where orderId=?) and type=? and user_id is null";
		if(salesman!=0 && salesman!=shoppingGuide){
			jdbcTemplate.update(sql,salesman,new Date(),salesman,orderId,"1");
		}
		if(shoppingGuide!=0){
			jdbcTemplate.update(sql,shoppingGuide,new Date(),shoppingGuide,orderId,"2");
		}
		if(designer!=0){
			jdbcTemplate.update(sql,designer,new Date(),designer,orderId,"3");
		}
		if(sender!=0){
			jdbcTemplate.update(sql,sender,new Date(),sender,orderId,"4");
		}		
	}
	
	public void setOrderTotalPrice(String orderId){
		String sql = "select ifnull(slFee,0)+ifnull(jgFee,0) as fee from crmtile_order where ID=?";
		double fee = jdbcTemplate.queryForObject(sql, Double.class,orderId);
		sql = "select ifnull(sum(ifnull(total_price,0)),0) as items from crmtile_order_item where orderID=?";
		double items = jdbcTemplate.queryForObject(sql, Double.class,orderId);
		sql = "update crmtile_order set totalPrice=? where ID=?";
		jdbcTemplate.update(sql, fee+items,orderId);
	}
	
	/**
	 * 通过fileId获取其输入流
	 * @param fileId
	 * @return
	 */
	public InputStream getInputStream(String fileId){
		FileService fileService = Springfactory.getBean("IFileService");
		return fileService.getInputStream(fileService.get(fileId));
	}
}
