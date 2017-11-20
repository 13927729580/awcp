package cn.org.awcp.venson.aop;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.dingding.Env;
import cn.org.awcp.venson.dingding.helper.AuthHelper;
import cn.org.awcp.venson.dingding.message.LinkMessage;
import cn.org.awcp.venson.dingding.service.DDRequestService;

//声明这是一个组件
@Component
// 声明这是一个切面Bean
@Aspect
public class WorkflowAspect {
	private final static Log logger = LogFactory.getLog(WorkflowAspect.class);
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	PunUserBaseInfoService userService;// 用户Service
	// 配置后置通知,使用在方法aspect()上注册的切入点

	@AfterReturning(pointcut = "execution(* cn.org.awcp.venson.service.WorkflowService.create(..)) && args(dataId,user)", returning = "workId")
	public void createAfterReturning(String dataId, String user, long workId) {
		send(workId);

		logger.debug("createAfterReturning---------------------" + dataId + user + workId);
	}

	// 配置后置通知,使用在方法aspect()上注册的切入点
	@AfterReturning(pointcut = "execution(* cn.org.awcp.venson.service.WorkflowService.transfer(..)) && args(workId,user)", returning = "msg")
	public void transferAfterReturning(long workId, String user, String msg) {

		send(workId);
		logger.debug("transferAfterReturning---------------------" + workId + msg);
	}

	// 配置后置通知,使用在方法aspect()上注册的切入点
	@AfterReturning(pointcut = "execution(* cn.org.awcp.venson.service.WorkflowService.execute(..)) && args(workId)", returning = "msg")
	public void executeAfterReturning(long workId, String msg) {
		send(workId);
		logger.debug("executeAfterReturning---------------------" + workId + msg);
	}

	public String getSendUsers(long workId) {
		try {
			String sql = "select group_concat(FK_EMP,'|') from wf_generworkerlist where ISPASS=0 and WORKID=?";
			String emps = jdbcTemplate.queryForObject(sql, String.class, workId);
			return emps;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public void send(long workId) {
		String users = getSendUsers(workId);
		HttpServletRequest request = ControllerContext.getRequest();
		String basePath = ControllerHelper.getBasePath();
		if (StringUtils.isNotBlank(users)) {
			String dynamicPageId = request.getParameter("dynamicPageId");
			String FK_Flow = request.getParameter("FK_Flow");
			String FK_Node = request.getParameter("FK_Node");
			String url = basePath + "dingding/goto.jsp?url=" + basePath + "workflow/wf/openTask.do?FK_Flow=" + FK_Flow
					+ "|WorkID=" + workId + "|FK_Node=" + FK_Node + "|dynamicPageId=" + dynamicPageId;
			// 发送微应用消息
			LinkMessage link = new LinkMessage(url, "@lALOACZwe2Rk", "流程处理", "您有新的待办消息，请及时处理！");
			DDRequestService.sendMessage(AuthHelper.getAccessToken(), users, "",
					StringUtils.defaultString(request.getParameter(Env.PARAM_AGENT_ID_NAME), Env.DEFAULT_AGENT_ID),
					"link", link);
		}

	}

}
