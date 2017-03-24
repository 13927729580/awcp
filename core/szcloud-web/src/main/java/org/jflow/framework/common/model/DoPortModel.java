package org.jflow.framework.common.model;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataSet;
import BP.DA.XmlWriteMode;
import BP.Sys.SystemConfig;
import BP.Sys.Frm.MapData;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Template.Flow;
import BP.WF.Template.Node;
import BP.WF.Template.PubLib.FlowAppType;
import BP.WF.Template.PubLib.NodeFormType;

public class DoPortModel extends BaseModel {

	public StringBuilder Pub1 = null;

	private HttpServletRequest resquest;

	private HttpServletResponse response;

	private String EnName;

	private String PK;

	private String DirType;

	private String ToNodeID;

	private String FK_Node;

	private String CondType;

	private String FK_Flow;

	private String RefNo;

	private String FK_MapData = this.get_request().getParameter("FK_MapData");

	private String FK_Attr;

	private String FK_MainNode;

	private String PassKey;

	private String Lang;

	private String doType;

	private String basePath;

	public DoPortModel(HttpServletRequest request, HttpServletResponse response, String EnName, String PK, String Lang,
			String DoType, String basePath) {
		super(request, response);
		this.response = response;
		this.resquest = resquest;
		this.EnName = EnName;
		this.Lang = Lang;
		this.PK = PK;
		this.doType = DoType;
		this.basePath = basePath;
		Pub1 = new StringBuilder();
	}

	public void init() throws Exception {
		// if (request.Browser.Cookies == false)
		// {
		// //this.Response.Write("您的浏览器不支持cookies功能，无法使用该系统。");
		// //return;
		// }

		if (this.PassKey != SystemConfig.getAppSettings().get("PassKey"))
			return;

		// BP.Sys.SystemConfig.DoClearCash();
		// BP.Port.Emp emp = new BP.Port.Emp("admin");
		// WebUser.SignInOfGenerLang(emp, "CH");

		String fk_flow = FK_Flow;
		String fk_Node = FK_Node;

		// String FK_MapData = FK_MapData
		if (StringHelper.isNullOrEmpty(FK_MapData)) {
			FK_MapData = this.get_request().getParameter("PK");
		}
		if (doType.equals("DownFormTemplete")) {

			MapData md = new MapData(FK_MapData);
			DataSet ds = md.GenerHisDataSet();
			String file = SystemConfig.getPathOfTemp() + File.separator + md.getNo() + ".xml";
			ds.WriteXml(file, XmlWriteMode.IgnoreSchema, ds);
			// BP.Sys.PubClass.DownloadFile(file..FullName, md.getName() +
			// ".xml");
			this.Pub1.append(this.AddFieldSet("下载提示"));

			String url = Glo.getCCFlowAppPath() + File.separator + "DataUser" + File.separator + "Temp" + File.separator
					+ md.getNo() + ".xml";
			this.Pub1.append(this.AddH2("ccflow 已经完成模板的生成了，正在执行下载如果您的浏览器没有反应请<a href='" + url + "' >点这里进行下载</a>。"));
			this.Pub1.append("如果该xml文件是在ie里直接打开的，请把鼠标放在连接上右键目标另存为，保存该模板。");
			this.Pub1.append(this.AddFieldSetEnd());
			return;
		} else if (doType.equals("Ens")) {
			response.sendRedirect("../../Comm/Batch.jsp?EnsName=" + EnName);
		} else if (doType.equals("En")) {
			if (EnName.equals("BP.WF.Template.Flow")) {
				Flow fl = new Flow(this.PK);
				if (fl.getFlowAppType() == FlowAppType.DocFlow)
					response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowDocs&No=" + this.PK);
				else
					response.sendRedirect(
							"../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowSheets&No=" + this.PK);
				return;
			} else if (EnName.equals("BP.WF.Template.FlowSheet")) {
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowSheets&No=" + this.PK);
				return;
			} else if (EnName.equals("BP.WF.Node")) {
				Node nd = new Node(this.PK);
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.NodeSheets&PK=" + this.PK);
				return;
			} else if (EnName.equals("BP.WF.FlowSort")) {
				response.sendRedirect("../../Comm/RefFunc/UIEn.jsp?EnsName=BP.WF.Template.FlowSorts&PK=" + this.PK);
				return;
			} else {
				throw new Exception("err");
			}
		} else if (doType.equals("FrmLib")) {
			response.sendRedirect("../FlowFrms.jsp?ShowType=FrmLab&FK_Flow="
					+ this.get_request().getParameter("FK_Flow") + "&FK_Node="
					+ this.get_request().getParameter("FK_Node") + "&Lang=" + BP.Port.WebUser.getSysLang());
		} else if (doType.equals("FlowFrms")) {
			response.sendRedirect("../FlowFrms.jsp?ShowType=FlowFrms&FK_Flow="
					+ this.get_request().getParameter("FK_Flow") + "&FK_Node="
					+ this.get_request().getParameter("FK_Node") + "&Lang=" + BP.Port.WebUser.getSysLang());
		} else if (doType.equals("StaDef")) {
			response.sendRedirect(
					"../../Comm/RefFunc/Dot2Dot.jsp?EnName=BP.WF.Template.NodeSheet&AttrKey=BP.WF.Template.NodeStations&PK="
							+ this.PK + "&NodeID=" + this.PK + "&RunModel=0&FLRole=0&FJOpen=0&r=" + this.PK);
		} else if (doType.equals("WFRpt")) {
			response.sendRedirect(
					"../../Rpt/OneFlow.jsp?FK_MapData=ND" + Integer.parseInt(this.PK) + "Rpt&FK_Flow=" + this.PK);
		} else if (doType.equals("MapDef")) {
			int nodeid = Integer.parseInt(this.PK.replace("ND", ""));
			Node nd1 = new Node();
			nd1.setNodeID(nodeid);
			nd1.RetrieveFromDBSources();
			if (nd1.getHisFormType() == NodeFormType.FreeForm)
				response.sendRedirect("../../MapDef/CCForm/Frm.jsp?FK_MapData=" + this.PK + "&FK_Flow="
						+ nd1.getFK_Flow() + "&FK_Node=" + this.FK_Node);
			else
				response.sendRedirect("../../MapDef/MapDef.jsp?PK=" + this.PK + "&FK_Flow=" + nd1.getFK_Flow()
						+ "&FK_Node=" + this.FK_Node);
		} else if (doType.equals("MapDefFixModel")) {
		} else if (doType.equals("FromFixModel")) {
			response.sendRedirect("../../MapDef/MapDef.jsp?FK_MapData=" + FK_MapData + "&FK_Flow=" + this.FK_Flow
					+ "&FK_Node=" + this.FK_Node);
		} else if (doType.equals("MapDefFreeModel")) {
		} else if (doType.equals("FormFreeModel")) {
			response.sendRedirect("../../MapDef/CCForm/Frm.jsp?FK_MapData=" + FK_MapData + "&FK_Flow=" + this.FK_Flow
					+ "&FK_Node=" + this.FK_Node);
		} else if (doType.equals("Map.DefFree")) {
			int nodeidFree = Integer.parseInt(this.PK.replace("ND", ""));
			Node ndFree = new Node(nodeidFree);
			response.sendRedirect("../MapDef/CCForm/Frm.jsp?FK_MapData=" + this.PK + "&FK_Flow=" + ndFree.getFK_Flow()
					+ "&FK_Node=" + ndFree.getNodeID());

		} else if (doType.equals("MapDefF4")) {
			int nodeidF4 = Integer.parseInt(this.PK.replace("ND", ""));
			Node ndF4 = new Node(nodeidF4);
			response.sendRedirect(
					"../../MapDef/MapDef.jsp?PK=" + this.PK + "&FK_Flow=" + ndF4.getFK_Flow() + "&FK_Node=" + nodeidF4);

		} else if (doType.equals("Dir")) {
			this.response.sendRedirect("../Admin/Cond.jsp?CondType=" + CondType + "&FK_Flow=" + FK_Flow
					+ "&FK_MainNode=" + FK_MainNode + "&FK_Node=" + FK_Node + "&FK_Attr=" + FK_Attr + "&DirType="
					+ DirType + "&ToNodeID=" + ToNodeID);
		} else if (doType.equals("RunFlow")) {
			response.sendRedirect(
					"../Admin/StartFlow.jsp?FK_Flow=" + fk_flow + "&Lang=" + BP.Port.WebUser.getSysLang());
		} else if (doType.equals("FlowCheck")) {
			response.sendRedirect("../Admin/DoType.jsp?RefNo=" + RefNo + "&DoType=" + doType);
		} else if (doType.equals("ExpFlowTemplete")) {
			Flow flow = new Flow(this.getFK_Flow());
			String fileXml = flow.GenerFlowXmlTemplete();

			BP.Sys.PubClass.DownloadFile(fileXml, flow.getName() + ".xml");
			this.winCloseWithMsg("导出完成");

		} else if (doType.equals("UploadShare")) {
		} else if (doType.equals("ShareToFtp")) {

		} else {
			throw new Exception("Error:" + doType);
		}

	}

}
