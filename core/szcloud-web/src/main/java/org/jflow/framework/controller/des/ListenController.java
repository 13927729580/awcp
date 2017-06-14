package org.jflow.framework.controller.des;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.TempObject;
import org.jflow.framework.controller.wf.workopt.BaseController;
import org.jflow.framework.system.ui.core.NamesOfBtn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.WF.Template.Listen;
import BP.WF.Template.Node;
import BP.WF.Template.Nodes;

@Controller
@RequestMapping("/WF/Admin")
public class ListenController extends BaseController {
	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(ListenController.class);

	@RequestMapping(value = "/btn_Click", method = RequestMethod.POST)
	public void btn_Click(TempObject object, HttpServletRequest request, HttpServletResponse response) {
		Listen li = new Listen();

		if (this.getRefOID() != 0) {
			li.setOID(this.getRefOID());
			li.Retrieve();
		}
		// Object tempVar = this.Pub1.Copy(li);
		// String body = "";
		// try {
		// body = HtmlUtils.getBodyString(request);
		// } catch (IOException e) {
		// logger.info("ERROR", e);
		// }
		// HashMap<String,BaseWebControl> ctrlMap = HtmlUtils.httpParser(body);
		// Object tempVar = BaseModel.Copy(request, li, null, li.getEnMap(),
		// ctrlMap);
		// li = (Listen) ((tempVar instanceof Listen) ? tempVar : null);
		String title = request.getParameter("TB_Title");
		String doc = request.getParameter("TB_Doc");
		li.setTitle(title);
		li.setDoc(doc);

		li.setOID(this.getRefOID());

		Node nd = new Node(this.getFK_Node());
		Nodes nds = new Nodes(nd.getFK_Flow());
		String strs = "";

		for (Node en : Nodes.convertNodes(nds)) {
			if (en.getNodeID() == this.getFK_Node()) {
				continue;
			}

			// CheckBox cb = this.Pub1.GetCBByID("CB_" + en.getNodeID());
			String cb = request.getParameter("CB_" + en.getNodeID());
			if (cb != null && cb.equals("on")) {
				strs += "@" + en.getNodeID();
			}
		}

		li.setNodes(strs);
		li.setFK_Node(this.getFK_Node());

		if (li.getOID() == 0) {
			li.Insert();
		} else {
			li.Update();
		}

		// C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit
		// typing in Java:
		// var btn = (LinkBtn) sender;
		// if (btn.getID() == NamesOfBtn.Save) {
		if (object.getBtnName().equals(NamesOfBtn.Save.toString())) {
			try {
				response.sendRedirect(
						"Listen.jsp?FK_Node=" + object.getFK_Node() + "&DoType=New&RefOID=" + li.getOID());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("ERROR", e);
			}
		} else {
			try {
				response.sendRedirect("Listen.jsp?FK_Node=" + object.getFK_Node() + "&DoType=New&RefOID=0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("ERROR", e);
			}
		}
	}

	@RequestMapping(value = "/btn_Del_Click", method = RequestMethod.POST)
	public void btn_Del_Click(TempObject object, HttpServletRequest request, HttpServletResponse response) {
		Listen li = new Listen();

		if (this.getRefOID() != 0) {
			li.setOID(this.getRefOID());
			li.Delete();
		}

		try {
			response.sendRedirect("Listen.jsp?FK_Node=" + this.getFK_Node() + "&DoType=New&RefOID=0");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("ERROR", e);
		}
	}

}
