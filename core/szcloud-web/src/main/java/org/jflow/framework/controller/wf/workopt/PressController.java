package org.jflow.framework.controller.wf.workopt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.TempObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/WF/WorkOpt")
public class PressController extends BaseController{

	@RequestMapping(value = "/Press", method = RequestMethod.POST)
	protected void execute(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		

		String msg = request.getParameter("TB_Msg");
		String info = BP.WF.Dev2Interface.Flow_DoPress(object.getWorkID(), msg, true);
		try {
			printAlert(response,info);
			winClose(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ModelAndView mv = new ModelAndView("/WF/ShowMessage");
//		mv.addObject("showMessage", info);
//		return mv;
	}
}