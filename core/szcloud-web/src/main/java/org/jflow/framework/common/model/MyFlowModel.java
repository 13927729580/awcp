package org.jflow.framework.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import BP.Port.WebUser;
import BP.Sys.Frm.MapData;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Template.Flow;
import BP.WF.Template.Node;
import BP.WF.Template.PubLib.NodeFormType;
import BP.WF.Template.WorkBase.Work;

public class MyFlowModel {
	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(MyFlowModel.class);
	private HttpServletRequest request;

	private HttpServletResponse response;

	private Work currWK;

	private Node currND;

	private GenerWorkFlow gwf;

	private long cWorkID;

	public String fk_flow;

	public String fk_node;

	public String fid;

	public long workId;

	private MapData mapData;

	private boolean isContinue = true;

	public String selfFromPub;

	public boolean getIsContinue() {
		return isContinue;
	}

	public boolean getHisFormType() {
		return currND.getHisFormType() == NodeFormType.SelfForm;
	}

	public MyFlowModel(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public String DealUrl(Node currND) {
		String url = currND.getFormUrl();
		String urlExt = request.getQueryString();

		// 防止查询不到.
		urlExt = urlExt.replace("?WorkID=", "&WorkID=");
		if (urlExt.contains("&WorkID") == false) {
			urlExt += "&WorkID=" + this.workId;
		} else {
			urlExt = urlExt.replace("&WorkID=0", "&WorkID=" + this.workId);
			urlExt = urlExt.replace("&WorkID=&", "&WorkID=" + this.workId + "&");
		}
		// SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上,珠海高凌提出.
		// url = url.replace("@SDKFromServHost",
		// SystemConfig.getAppSettings().get("SDKFromServHost").toString());

		urlExt += "&CWorkID=" + cWorkID;

		if (urlExt.contains("&NodeID") == false) {
			urlExt += "&NodeID=" + currND.getNodeID();
		}

		if (urlExt.contains("FK_Node") == false) {
			urlExt += "&FK_Node=" + currND.getNodeID();
		}

		if (urlExt.contains("&FID") == false) {
			urlExt += "&FID=" + currWK.getFID();
		}

		if (urlExt.contains("&UserNo") == false) {
			urlExt += "&UserNo=" + WebUser.getNo();
		}

		if (urlExt.contains("&SID") == false) {
			urlExt += "&SID=" + WebUser.getSID();
		}

		if (url.contains("?") == true) {
			url += "&" + urlExt;
		} else {
			url += "?" + urlExt;
		}

		url = url.replace("?&", "?");
		return url;
	}

	/**
	 * 初始化流程逻辑
	 */
	public void initFlow() {
		// 初始化变量.
		String workIdStr = request.getParameter("WorkID");
		String isRead = request.getParameter("IsRead");
		fid = request.getParameter("FID");
		fk_flow = request.getParameter("FK_Flow");
		fk_node = request.getParameter("FK_Node");

		boolean isCC = request.getQueryString().contains("IsCC=1");

		if (StringHelper.isNullOrEmpty(isRead)) {
			isRead = "";
		}
		if (StringHelper.isNullOrEmpty(fid)) {
			fid = "0";
		}

		if (StringHelper.isNullOrEmpty(fk_node) || fk_node.equals("0")) {
			fk_node = fk_flow + "01";
		}

		fk_node = String.valueOf(Integer.parseInt(fk_node));

		mapData = new MapData("ND" + fk_node);
		mapData.Retrieve();

		// region 校验用户是否被禁用。
		try {
			String no = WebUser.getNo();
			if (StringHelper.isNullOrEmpty(no)) {
				String userNo = request.getParameter("UserNo");
				try {
					Dev2Interface.Port_Login(userNo, request.getParameter("SID"));
				} catch (Exception e) {
					logger.info("ERROR", e);
				}
			}
		} catch (RuntimeException ex) {
			this.toMsg("@登录信息WebUser.No丢失，请重新登录。" + ex.getMessage(), "Info");
			return;
		}

		try {
			WebUser.getName();
		} catch (RuntimeException ex) {
			this.toMsg("@登录信息WebUser.Name丢失，请重新登录。错误信息:" + ex.getMessage(), "Info");
			isContinue = false;
			return;
		}

		if (BP.WF.Glo.getIsEnableCheckUseSta() == true) {
			try {
				if (BP.WF.Glo.CheckIsEnableWFEmp() == false) {
					WebUser.Exit();
					this.toMsg("<font color=red>您的帐号已经被禁用，如果有问题请与管理员联系。</font>", "Info");
					isContinue = false;
					return;
				}
			} catch (Exception e) {
				logger.info("ERROR", e);
			}
		}
		// endreigion

		workId = workIdStr == null ? 0 : Long.parseLong(workIdStr);

		// region 判断是否有IsRead
		try {
			if (isCC) {
				if (isRead.equals("0")) {
					BP.WF.Dev2Interface.Node_CC_SetRead(Integer.valueOf(fk_node), workId, WebUser.getNo());
				}
			} else {
				if (isRead.equals("0")) {
					Dev2Interface.Node_SetWorkRead(Integer.valueOf(fk_node), workId);
				}
			}
		} catch (Exception ex) {
			this.toMsg("设置读取状态错误。" + ex.getMessage(), "info");
			isContinue = false;
			return;
		}

		// 判断前置导航.
		Flow currFlow = new Flow(fk_flow);
		currND = new Node(fk_node);
		String IsCheckGuide = request.getParameter("IsCheckGuide");
		if (workId == 0 && currND.getIsStartNode() && IsCheckGuide == null) {
			switch (currFlow.getStartGuideWay()) {
			case None:
				break;
			case BySystemUrlMulti:
			case BySystemUrlMultiEntity:
				try {
					response.sendRedirect("StartGuide.jsp?FK_Flow=" + fk_flow);
				} catch (IOException e) {
					logger.info("ERROR", e);
				}
				isContinue = false;
				return;
			case ByHistoryUrl: // 历史数据.
				if (currFlow.getIsLoadPriData()) {
					this.toMsg("流程配置错误，您不能同时启用前置导航，自动装载上一笔数据两个功能。", "Info");
					isContinue = false;
					return;
				}
				try {
					response.sendRedirect("StartGuide.jsp?FK_Flow=" + fk_flow);
				} catch (IOException e) {
					logger.info("ERROR", e);
				}
				isContinue = false;
				return;
			case BySystemUrlOneEntity:
			case BySystemUrlOne:
				try {
					response.sendRedirect("StartGuideEntities.jsp?FK_Flow=" + fk_flow);
				} catch (IOException e) {
					logger.info("ERROR", e);
				}
				isContinue = false;
				return;

			case BySelfUrl:
				try {
					response.sendRedirect(currFlow.getStartGuidePara1());
				} catch (IOException e) {
					logger.info("ERROR", e);
				}
				isContinue = false;
				return;
			default:
				break;
			}
		}
		// end结束前置导航处理.

		// region 处理表单类型.
		if (currND.getHisFormType() == NodeFormType.SheetTree || currND.getHisFormType() == NodeFormType.WebOffice) {
			// 如果是多表单流程.
			String pFlowNo = request.getParameter("PFlowNo");
			String pWorkID = request.getParameter("PWorkID");
			String pNodeID = request.getParameter("PNodeID");
			String pEmp = request.getParameter("PEmp");
			if (StringHelper.isNullOrEmpty(pEmp)) {
				pEmp = WebUser.getNo();
			}

			if (workId == 0) {
				if (StringHelper.isNullOrEmpty(pFlowNo)) {
					try {
						workId = Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, WebUser.getNo(), null);
					} catch (Exception e) {
						logger.info("ERROR", e);
					}
				} else {
					workId = Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, WebUser.getNo(), null,
							Long.parseLong(pWorkID), pFlowNo, Integer.parseInt(pNodeID), null);
				}
				currWK = currND.getHisWork();
				currWK.setOID(workId);
				currWK.Retrieve();
				workId = currWK.getOID();
			} else {
				gwf = new GenerWorkFlow();
				gwf.setWorkID(workId);
				gwf.RetrieveFromDBSources();
				cWorkID = gwf.getCWorkID();
				pFlowNo = gwf.getPFlowNo();
				pWorkID = String.valueOf(gwf.getPWorkID());
			}

			String toUrl = "";
			if (currND.getHisFormType() == NodeFormType.SheetTree) {
				toUrl = "./FlowFormTree/Default.jsp?WorkID=" + workId + "&FK_Flow=" + fk_flow + "&UserNo="
						+ WebUser.getNo() + "&FID=" + fid + "&SID=" + WebUser.getSID() + "&CWorkID=" + cWorkID
						+ "&PFlowNo=" + pFlowNo + "&PWorkID=" + pWorkID;
			} else {
				toUrl = "./WebOffice/Default.jsp?WorkID=" + workId + "&FK_Flow=" + fk_flow + "&UserNo="
						+ WebUser.getNo() + "&FID=" + fid + "&SID=" + WebUser.getSID() + "&CWorkID=" + cWorkID
						+ "&PFlowNo=" + pFlowNo + "&PWorkID=" + pWorkID;
			}

			String[] ps = request.getQueryString().split("&");
			int ps_size = ps.length;
			for (int p = 0; p < ps_size; p++) {
				String s = ps[p];
				if (StringHelper.isNullOrEmpty(s)) {
					continue;
				}
				if (toUrl.contains(s)) {
					continue;
				}
				toUrl += "&" + s;
			}
			try {
				response.sendRedirect(toUrl);
			} catch (IOException e) {
				logger.info("ERROR", e);
			}
			isContinue = false;
			return;
		}

		if (currND.getHisFormType() == NodeFormType.SLForm) {
			if (workId == 0) {
				try {
					workId = Dev2Interface.Node_CreateBlankWork(fk_flow, null, null, WebUser.getNo(), null);
				} catch (Exception e) {
					logger.info("ERROR", e);
				}
				currWK = currND.getHisWork();
				currWK.setOID(workId);
				currWK.Retrieve();
				workId = currWK.getOID();
			}
			String myflowSlUrl = "MyFlowSL.jsp?WorkID=" + workId + "&FK_Flow=" + fk_flow + "&FK_Node=" + fk_node
					+ "&UserNo=" + WebUser.getNo() + "&CWorkID=" + cWorkID;
			try {
				response.sendRedirect(myflowSlUrl);
			} catch (IOException e) {
				logger.info("ERROR", e);
			}
			isContinue = false;
			return;
		}

		if (currND.getHisFormType() == NodeFormType.SDKForm) {
			if (workId == 0) {
				currWK = currFlow.NewWork();
				workId = currWK.getOID();
			}

			String url = currND.getFormUrl();
			if (StringHelper.isNullOrEmpty(url)) {
				this.toMsg("设置读取状流程设计错误态错误。", "没有设置表单url。");
				isContinue = false;
				return;
			}

			String urlExt = request.getQueryString();

			// 防止查询不到.
			urlExt = urlExt.replace("?WorkID=", "&WorkID=");
			if (urlExt.contains("&WorkID") == false) {
				urlExt += "&WorkID=" + workId;
			} else {
				urlExt = urlExt.replace("&WorkID=0", "&WorkID=" + workId);
				urlExt = urlExt.replace("&WorkID=&", "&WorkID=" + workId + "&");
			}

			urlExt += "&CWorkID=" + cWorkID;

			if (urlExt.contains("&NodeID") == false) {
				urlExt += "&NodeID=" + currND.getNodeID();
			}

			if (urlExt.contains("FK_Node") == false) {
				urlExt += "&FK_Node=" + currND.getNodeID();
			}

			if (urlExt.contains("&FID") == false) {
				urlExt += "&FID=" + currWK.getFID();
			}

			if (urlExt.contains("&UserNo") == false) {
				urlExt += "&UserNo=" + WebUser.getNo();
			}

			if (urlExt.contains("&SID") == false) {
				urlExt += "&SID=" + WebUser.getSID();
			}

			if (url.contains("?") == true) {
				url += "&" + urlExt;
			} else {
				url += "?" + urlExt;
			}

			url = url.replace("?&", "?").replace("aspx", "jsp");
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				logger.info("ERROR", e);
			}
			isContinue = false;
			return;
		}

		// region 判断是否有 workid
		if (workId == 0) {
			currWK = currFlow.NewWork();
			workId = currWK.getOID();
		} else {
			currWK = currFlow.GenerWork(workId, currND, true);
		}

		if (currND.getHisFormType() == NodeFormType.SelfForm) {
			currWK.Save();
			if (workId == 0)
				workId = currWK.getOID();

			String url = this.DealUrl(currND);

			selfFromPub = "<iframe ID='SelfForm' src='" + url
					+ "' frameborder=0  style='width:100%; height:800px' leftMargin='0' topMargin='0' />";
			selfFromPub += "\t\n</iframe>";
			return;
		}
		// endregion 处理表单类型.

		if (!isCC && currND.getIsStartNode() == false && Dev2Interface.Flow_IsCanDoCurrentWork(fk_flow,
				Integer.parseInt(fk_node), workId, WebUser.getNo()) == false) {
			this.toMsg(" @当前的工作已经被处理，或者您没有执行此工作的权限。", "Info");
			isContinue = false;
			return;
		}

		// 处理传递过来的参数。
		String[] paramterKeys = request.getQueryString().split("&");
		for (int i = 0; i < paramterKeys.length; i++) {
			if (StringHelper.isNullOrEmpty(paramterKeys[i]) || !paramterKeys[i].contains("=")) {
				continue;
			}
			String[] key_value = paramterKeys[i].split("=");
			if (key_value.length < 2) {
				continue;
			}
			currWK.SetValByKey(key_value[0], key_value[1]);
		}
		currWK.ResetDefaultVal();

		// 需要把默认值保存里面去，不然，就会导致当前默认信息存储不了。
		currWK.DirectUpdate();

		request.setAttribute("Work", currWK);
	}

	/**
	 * 返回表单高度
	 * 
	 * @return
	 */
	public String getFromH() {
		return String.valueOf(mapData.getFrmH());
	}

	/**
	 * 返回表单宽
	 * 
	 * @return
	 */
	public String getFromW() {
		return String.valueOf(mapData.getFrmW());
	}

	public void toMsg(String msg, String type) {
		try {
			request.getSession().setAttribute("info", msg);
			// application.setAttribute("info" + WebUser.getNo(), msg);
			Glo.setSessionMsg(msg);
			String url = "MyFlowInfo.jsp?FK_Flow=" + fk_flow + "&FK_Type=" + type + "&FK_Node=" + fk_node + "&WorkID="
					+ workId;
			response.sendRedirect(url);
		} catch (Exception ex) {
			try {
				request.getSession().setAttribute("info", ex.getMessage());
				response.sendRedirect(Glo.getCCFlowAppPath() + "WF/Comm/Port/ToErrorPage.jsp");
			} catch (IOException io) {
			}
		}
	}

}
