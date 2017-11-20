package org.jflow.framework.controller.wf.workopt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jflow.framework.common.model.TempObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/WF/WorkOpt")
public class PressController extends BaseController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/Press", method = RequestMethod.POST)
	protected void execute(TempObject object, HttpServletRequest request, HttpServletResponse response) {

		String msg = request.getParameter("TB_Msg");
		String info = BP.WF.Dev2Interface.Flow_DoPress(object.getWorkID(), msg, true);
		try {
			printAlert(response, info);
			winClose(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e);
		}
		// ModelAndView mv = new ModelAndView("/WF/ShowMessage");
		// mv.addObject("showMessage", info);
		// return mv;
	}
}