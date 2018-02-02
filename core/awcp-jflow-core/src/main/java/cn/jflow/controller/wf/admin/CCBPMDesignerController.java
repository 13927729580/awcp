package cn.jflow.controller.wf.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.HttpHandler.WF_Admin_CCBPMDesigner;
import BP.WF.HttpHandler.Base.HttpHandlerBase;

@Controller
@RequestMapping("/WF/Admin/CCBPMDesigner")
@ResponseBody
public class CCBPMDesignerController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ProcessRequest")
	public final void ProcessRequestPost() 
	{
		WF_Admin_CCBPMDesigner  CCBPMDHandler = new WF_Admin_CCBPMDesigner();
		super.ProcessRequest(CCBPMDHandler);
	}
	@Override
	public Class <WF_Admin_CCBPMDesigner>getCtrlType() {
		return WF_Admin_CCBPMDesigner.class;
	}

}
