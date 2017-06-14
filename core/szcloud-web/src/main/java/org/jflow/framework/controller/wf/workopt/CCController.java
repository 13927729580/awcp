package org.jflow.framework.controller.wf.workopt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.WF.Template.PubLib.ActionType;
import BP.WF.Template.PubLib.CCWriteTo;

@Controller
@RequestMapping("/WF/WorkOpt")
@Scope("request")
public class CCController extends BaseController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/CC", method = RequestMethod.POST)
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		String errMsg = "";
		try {
			String accepters = request.getParameter("TB_Accepter");
			String title = request.getParameter("TB_Title");
			String doc = request.getParameter("doc");

			/* 检查人员是否有问题. */
			String[] emps = accepters.split(",");
			BP.Port.Emp myemp = new BP.Port.Emp();
			for (String emp : emps) {
				if (emp == null || "".equals(emp))
					continue;
				myemp.setNo(emp);// 这行干啥的？ myemp.No = emp;
				if (myemp.getIsExits() == false)
					errMsg += "@人员(" + emp + ")拼写错误。";
			}
			if (errMsg != null && !"".equals(errMsg)) {
				printAlert(response, errMsg);
				return null;
			}
			// 节点.
			BP.WF.Template.Node nd = new BP.WF.Template.Node(Integer.valueOf(this.getFK_Node()));
			// 抄送信息.
			String msg = "";
			for (String emp : emps) {
				if (StringUtils.isEmpty(emp))
					continue;

				myemp.setNo(emp);
				myemp.Retrieve();
				String empName = myemp.getName();
				msg += "(" + myemp.getNo() + "," + empName + ")";
				CCWriteTo ccWriteTo = nd.getCCWriteTo();
				// 根据节点属性的配置写入数据.
				if (ccWriteTo == CCWriteTo.All) {
					BP.WF.Dev2Interface.Node_CC_WriteTo_CClist(getFK_Node(), getFK_Node(), getWorkID(), emp, empName,
							title, doc);
					BP.WF.Dev2Interface.Node_CC_WriteTo_Todolist(getFK_Node(), getFK_Node(), getWorkID(), emp, empName);
				} else if (ccWriteTo == CCWriteTo.CCList) {
					BP.WF.Dev2Interface.Node_CC_WriteTo_CClist(getFK_Node(), getFK_Node(), getWorkID(), emp, empName,
							title, doc);
				} else if (ccWriteTo == CCWriteTo.Todolist) {
					BP.WF.Dev2Interface.Node_CC_WriteTo_Todolist(getFK_Node(), getFK_Node(), getWorkID(), emp, empName);
				}
			}

			// 写入日志.
			BP.WF.Dev2Interface.WriteTrack(nd.getFK_Flow(), nd.getNodeID(), getWorkID(), getFID(), "抄送给:" + msg,
					ActionType.CC, null, null, null);
			// winCloseWithMsg(response, "抄送成功...");
			request.getSession().setAttribute("info", "已成功抄送给：" + msg);
			mv.addObject("DoType", "Msg");
			mv.setViewName("redirect:" + "/WF/MyFlowInfo.jsp");
		} catch (Exception e) {
			logger.info("ERROR", e);
			try {
				// winCloseWithMsg(response, "抄送失败...");
				mv.addObject("errMsg", "工作抄送出错：" + e.getMessage());
				mv.setViewName("redirect:" + "/WF/WorkOpt/CC.jsp");
				return mv;
			} catch (Exception e1) {
				logger.info("ERROR", e1);
			}
		}
		return mv;
	}

}
