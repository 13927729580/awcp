package org.jflow.framework.designer.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.BaseModel;

import BP.Sys.Frm.FrmEvent;
import BP.Sys.Frm.FrmEventAttr;
import BP.Sys.Frm.FrmEvents;
import BP.Tools.StringHelper;
import BP.WF.XML.EventList;
import BP.WF.XML.EventLists;
import BP.WF.XML.EventSource;
import BP.WF.XML.EventSources;

public class ActionModel {

	public StringBuilder Pub1 = new StringBuilder();
	public StringBuilder Pub2 = new StringBuilder();

	private String basePath;
	private HttpServletRequest _request = null;

	// private HttpServletResponse _response = null;
	public ActionModel(HttpServletRequest request,
			HttpServletResponse response, String basePath) {
		this.basePath = basePath;
		this._request = request;
		// this._response = response;
	}

	public String getEvent() {
		if (_request.getParameter("Event") == null)
			return "";
		return _request.getParameter("Event");
	}

	private String Title = "";

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	/**
	 * 当前事件设置的名称
	 */
	private String CurrentEvent;

	public String getCurrentEvent() {
		return CurrentEvent;
	}

	public void setCurrentEvent(String value) {
		this.CurrentEvent = value;
	}

	/**
	 * 当前事件所属事件源名称
	 */
	private String CurrentEventGroup;

	public String getCurrentEventGroup() {
		return CurrentEventGroup;
	}

	public void setCurrentEventGroup(String value) {
		this.CurrentEventGroup = value;
	}

	private boolean HaveMsg;

	public boolean getHaveMsg() {
		return HaveMsg;
	}

	public void setHaveMsg(boolean value) {
		this.HaveMsg = value;
	}

	public String getNodeID() {
		if (_request.getParameter("NodeID") == null)
			return "";
		return _request.getParameter("NodeID");
	}

	public String getFK_MapData() {
		String fk_mapdata = _request.getParameter("FK_MapData");
		if (StringHelper.isNullOrEmpty(fk_mapdata)) {
			fk_mapdata = "ND" + this.getNodeID();
		}
		return fk_mapdata;
	}

	public String getDoType() {
		if (_request.getParameter("DoType") == null)
			return "";
		return _request.getParameter("DoType");
	}

	public String getFK_Flow() {
		if (_request.getParameter("FK_Flow") == null)
			return "";
		return _request.getParameter("FK_Flow");
	}

	public String getMyPK() {
		if (_request.getParameter("MyPK") == null)
			return "";
		return _request.getParameter("MyPK");
	}

	public void init() {
		if (!"".equals(this.getDoType()) && "Del".equals(this.getDoType())) {
			FrmEvent delFE = new FrmEvent();
			delFE.setMyPK(this.getFK_MapData() + "_"
					+ _request.getParameter("RefXml"));
			delFE.Delete();
		}

		FrmEvents ndevs = new FrmEvents();
		ndevs.Retrieve(FrmEventAttr.FK_MapData, this.getFK_MapData());

		EventLists xmls = new EventLists();
		xmls.RetrieveAll();

		EventSources ess = new EventSources();
		ess.RetrieveAll();

		String myEvent = this.getEvent();
		EventList myEnentXml = null;

		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#region //生成事件列表

		for (EventSource item : EventSources.convertEventSources(ess)) {
			this.Pub1
					.append(String
							.format("<div title='%1$s' style='padding:10px; overflow:auto' data-options=''>",
									item.getName()));
			this.Pub1.append(BaseModel.AddUL("class='navlist'"));

			for (EventList xml : EventLists.convertEventLists(xmls)) {
				if (!xml.getEventType().equals(item.getNo())) {
					continue;
				}

				Object tempVar = ndevs.GetEntityByKey(FrmEventAttr.FK_Event,
						xml.getNo());
				FrmEvent nde = (FrmEvent) ((tempVar instanceof FrmEvent) ? tempVar
						: null);

				if (nde == null) {
					if (myEvent != null && myEvent.equals(xml.getNo())) {
						this.setCurrentEventGroup(item.getName());
						myEnentXml = xml;
						this.Pub1
								.append(BaseModel.AddLi(String
										.format("<div style='font-weight:bold'><a href='javascript:void(0)'><span class='nav'>%1$s</span></a></div>%2$s",
												xml.getName(), "\r\n")));
					} else {
						this.Pub1
								.append(BaseModel.AddLi(String
										.format("<div><a href='"
												+ basePath
												+ "WF/Admin/Action.jsp?NodeID=%1$s&Event=%2$s&FK_Flow=%3$s&tk=%6$s'><span class='nav'>%4$s</span></a></div>%5$s",
												this.getNodeID(), xml.getNo(),
												this.getFK_Flow(), xml
														.getName(), "\r\n",
												new java.util.Random()
														.nextDouble())));
					}
				} else {
					if (myEvent != null && myEvent.equals(xml.getNo())) {
						setCurrentEventGroup(item.getName());
						myEnentXml = xml;
						this.Pub1
								.append(BaseModel.AddLi(String
										.format("<div style='font-weight:bold'><a href='javascript:void(0)'><span class='nav'>%1$s</span></a></div>%2$s",
												xml.getName(), "\r\n")));
					} else {
						this.Pub1
								.append(BaseModel.AddLi(String
										.format("<div><a href='"
												+ basePath
												+ "WF/Admin/Action.jsp?NodeID=%1$s&Event=%2$s&FK_Flow=%3$s&MyPK=%4$s&tk=%7$s'><span class='nav'>%5$s</span></a></div>%6$s",
												this.getNodeID(), xml.getNo(),
												this.getFK_Flow(), nde
														.getMyPK(), xml
														.getName(), "\r\n",
												new java.util.Random()
														.nextDouble())));
					}
				}
			}

			this.Pub1.append(BaseModel.AddULEnd());
			this.Pub1.append(BaseModel.AddDivEnd());
		}
		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#endregion

		if (myEnentXml == null) {
			this.setCurrentEvent("帮助");

			this.Pub2
					.append("<div style='width:100%; text-align:center' data-options='noheader:true'>");
			this.Pub2.append(BaseModel.AddH2("事件是ccflow与您的应用程序接口"));

			this.Pub2.append(BaseModel.AddUL());
			this.Pub2.append(BaseModel
					.AddLi("流程在运动的过程中会产生很多的事件，比如：节点发送前、发送成功时、发送失败时、退回前、退后后。"));
			this.Pub2
					.append(BaseModel
							.AddLi("在这些事件里ccflow允许调用您编写的业务逻辑，完成与界面交互、与其他系统交互、与其他流程参与人员交互。"));
			this.Pub2.append(BaseModel
					.AddLi("按照事件发生的类型，ccflow把事件分为：节点、表单、流程三类的事件。"));
			this.Pub2.append(BaseModel.AddULEnd());

			this.Pub2.append(BaseModel.AddDivEnd());
			return;
		}

		Object tempVar2 = ndevs.GetEntityByKey(FrmEventAttr.FK_Event, myEvent);
		FrmEvent mynde = (FrmEvent) ((tempVar2 instanceof FrmEvent) ? tempVar2
				: null);
		if (mynde == null) {
			mynde = new FrmEvent();
			mynde.setFK_Event(myEvent);
		}

		this.setTitle("");
		this.setCurrentEvent(myEnentXml == null ? "" : myEnentXml.getName());
		int col = 50;

		this.Pub2
				.append("<div id='tabMain' class='easyui-tabs' data-options='fit:true'>");

		this.Pub2.append("<div title='事件接口' style='padding:5px'>" + "\r\n");
		this.Pub2
				.append("<iframe id='src1' frameborder='0' src='' style='width:100%;height:100%' scrolling='auto'></iframe>");
		this.Pub2.append("</div>" + "\n");

		if (myEnentXml != null && myEnentXml.getIsHaveMsg()) {
			setHaveMsg(true);
			this.Pub2.append("<div title='向当事人推送消息' style='padding:5px'>"
					+ "\r\n");
			this.Pub2
					.append("<iframe id='src2' frameborder='0' src='' style='width:100%;height:100%' scrolling='auto'></iframe>");
			this.Pub2.append("</div>" + "\n");

			this.Pub2.append("<div title='向其他指定的人推送消息' style='padding:5px'>"
					+ "\r\n");
			this.Pub2
					.append("<iframe id='src3' frameborder='0' src='' style='width:100%;height:100%' scrolling='auto'></iframe>");
			this.Pub2.append("</div>" + "\n");
		}

		// BP.WF.Dev2Interface.Port_Login("zhoupeng");

		// BP.WF.Dev2Interface.Port_SigOut();

		this.Pub2.append("</div>");
	}
}
