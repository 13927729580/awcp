package cn.org.awcp.venson.controller;

import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.service.WorkflowService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("workflow")
public class WorkflowController {
	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(WorkflowController.class);
	@Resource(name = "workflowServiceImpl")
	private WorkflowService workflowService;

	@RequestMapping(value = "/create/{dataId}", method = RequestMethod.POST)
	public ReturnResult create(@PathVariable("dataId") String dataId, @RequestParam("user") String user) {
		ReturnResult result = ReturnResult.get();
		result.setData(workflowService.create(dataId, user)).setStatus(StatusCode.SUCCESS);
		return result;
	}

	@RequestMapping(value = "/transfer/{workId}", method = RequestMethod.POST)
	public ReturnResult transfer(@PathVariable("workId") long workId, @RequestParam("user") String user) {
		ReturnResult result = ReturnResult.get();
		result.setStatus(StatusCode.SUCCESS).setMessage(workflowService.transfer(workId, user));
		return result;
	}

	@RequestMapping(value = "/execute/{workId}", method = RequestMethod.POST)
	public ReturnResult execute(@PathVariable("workId") long workId) {
		ReturnResult result = ReturnResult.get();

		result.setStatus(StatusCode.SUCCESS).setMessage(workflowService.execute(workId));
		return result;
	}

}
