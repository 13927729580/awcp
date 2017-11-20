package org.jflow.framework.controller.des;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;
import org.jflow.framework.common.model.TempObject;
import org.jflow.framework.controller.wf.workopt.BaseController;
import org.jflow.framework.system.ui.core.BaseWebControl;
import org.jflow.framework.system.ui.core.HtmlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import BP.Sys.Frm.FrmEvent;

@Controller
@RequestMapping(value="/DES")
public class ActionPush2CurrController extends BaseController{

	@RequestMapping(value="/ActionPushSave")//,method=RequestMethod.POST
	public String actionPushSave(HttpServletRequest request,HttpServletResponse response,int NodeID,String MyPK,String FK_MapData,String Event,String FK_Flow,TempObject object) throws IOException{
		HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), true);
		
		FrmEvent fe = new FrmEvent();
		
		if(FK_MapData==null){
			FK_MapData="ND"+NodeID;
		}
		
		fe.setMyPK(FK_MapData + "_" + Event);
		fe.RetrieveFromDBSources();
		fe=(FrmEvent)BaseModel(request, response).Copy(request, fe, null, fe.getEnMap(), controls);
//		fe = (FrmEvent)this.BaseModel.Copy(fe);
		fe.Save();

		//var pm = new PushMsg();
		//pm.Retrieve(PushMsgAttr.FK_Event, this.Event, PushMsgAttr.FK_Node, this.NodeID);

		return "redirect:/jflow-web/WF/Admin/ActionPush2Curr.jsp?NodeID=" + NodeID + "&MyPK=" + fe.getMyPK() + "&Event=" + Event + "&tk=" + new Random().nextInt();
	

	}

	private BaseModel BaseModel(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
