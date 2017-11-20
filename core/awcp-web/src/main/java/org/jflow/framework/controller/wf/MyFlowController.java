package org.jflow.framework.controller.wf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jflow.framework.common.model.BaseModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entity;
import BP.Port.WebUser;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Sys.Frm.FrmEventList;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapAttrs;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Glo;
import BP.WF.WorkNode;
import BP.WF.Data.GERpt;
import BP.WF.Data.GERptAttr;
import BP.WF.Template.CondModel;
import BP.WF.Template.Node;
import BP.WF.Template.Condition.TurnTo;
import BP.WF.Template.Condition.TurnTos;
import BP.WF.Template.PubLib.FlowAppType;
import BP.WF.Template.PubLib.SaveModel;
import BP.WF.Template.PubLib.WFState;
import BP.WF.Template.WorkBase.StartWorkAttr;
import BP.WF.Template.WorkBase.Work;
import TL.ConvertTools;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/WF/MyFlow")
public class MyFlowController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;

	// 流程编号
	private String fk_flow;
	// 工作id
	private String workID;
	// 节点id
	private String fk_node;
	private String fid;

	/**
	 * 初始化参数
	 * 
	 * @param request
	 */
	private void initParameter() {
		// 获取参数
		workID = getParameter("WorkID");
		fk_node = getParameter("FK_Node");
		fk_flow = getParameter("FK_Flow");
		fid = getParameter("FID");

	}

	/**
	 * 发送工作
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/SendWork", method = RequestMethod.POST)
	public ModelAndView SendWork(HttpServletRequest request, HttpServletResponse response) throws IOException {
		_request = request;
		_response = response;

		// 初始参数
		initParameter();

		try {
			Send(false);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			printResult(toJson("flowMsg", BaseModel.AddMsgOfWarning("错误", e.getMessage())));
		}

		return null;
	}

	/**
	 * 保存工作
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/SaveWork", method = RequestMethod.POST)
	public ModelAndView SaveWork(HttpServletRequest request, HttpServletResponse response) throws IOException {
		_request = request;
		_response = response;
		// 初始参数
		initParameter();

		try {
			Send(true);
		} catch (Exception e) {
			logger.info("ERROR", e);
			printResult(toJson("flowMsg", BaseModel.AddMsgOfWarning("错误", e.getMessage())));
		}
		return null;
	}

	/**
	 * 发送，保存公共方法
	 * 
	 * @param isSave
	 * @throws Exception
	 */
	public void Send(boolean isSave) throws Exception {
		if (StringHelper.isNullOrEmpty(fk_node)) {
			fk_node = getParameter("FK_Node");
		}
		Node currND = new Node(fk_node);
		Work currWK = currND.getHisWork();
		currWK.SetValByKey("OID", workID);
		currWK.Retrieve();

		// 判断当前人员是否有执行该人员的权限。
		if (!currND.getIsStartNode() && Dev2Interface.Flow_IsCanDoCurrentWork(this.fk_flow, Integer.parseInt(fk_node),
				Long.parseLong(workID), WebUser.getNo()) == false)
			throw new Exception(
					"您好：" + WebUser.getNo() + "," + WebUser.getName() + "：<br> 当前工作已经被其它人处理，您不能再执行保存或者发送动作!!!");

		Paras ps = new Paras();
		// String dtStr = SystemConfig.getAppCenterDBVarStr();
		try {
			switch (currND.getHisFormType()) {
			case SelfForm:
				break;
			case FixForm:
			case FreeForm:
				// 绑定数据
				PubClass.copyFromRequest(currWK, _request);
				// 设置默认值....
				MapAttrs mattrs = currND.getMapData().getMapAttrs();
				for (MapAttr attr : MapAttrs.convertMapAttrs(mattrs)) {
					if (attr.getTBModel() == 2) {
						/* 如果是富文本 */
						currWK.SetValByKey(attr.getKeyOfEn(), getParameter("TB_" + attr.getKeyOfEn()));
					}

					if (attr.getUIIsEnable())
						continue;
					if (attr.getDefValReal().contains("@") == false)
						continue;
					currWK.SetValByKey(attr.getKeyOfEn(), attr.getDefVal());
				}
				break;
			case DisableIt:
				currWK.Retrieve();
				break;
			default:
				throw new Exception("@未涉及到的情况。");
			}
		} catch (Exception ex) {
			// this.Btn_Send.Enabled = true;
			throw new Exception("@在保存前执行逻辑检查错误。" + ex.getMessage() + " @StackTrace:"
					+ ConvertTools.getStackTraceString(ex.getStackTrace()));
		}
		// region 判断特殊的业务逻辑
		String dbStr = SystemConfig.getAppCenterDBVarStr();
		if (currND.getIsStartNode()) {
			if (currND.getHisFlow().getHisFlowAppType() == FlowAppType.PRJ) {
				/* 对特殊的流程进行检查，检查是否有权限。 */
				String prjNo = currWK.GetValStringByKey("PrjNo");
				ps = new Paras();
				ps.SQL = "SELECT * FROM WF_NodeStation WHERE FK_Station IN ( SELECT FK_Station FROM Prj_EmpPrjStation WHERE FK_Prj="
						+ dbStr + "FK_Prj AND FK_Emp=" + dbStr + "FK_Emp )  AND  FK_Node=" + dbStr + "FK_Node ";
				ps.Add("FK_Prj", prjNo);
				ps.AddFK_Emp();
				ps.Add("FK_Node", fk_node);

				if (DBAccess.RunSQLReturnTable(ps).Rows.size() == 0) {
					String prjName = currWK.GetValStringByKey("PrjName");
					ps = new Paras();
					ps.SQL = "SELECT * FROM Prj_EmpPrj WHERE FK_Prj=" + dbStr + "FK_Prj AND FK_Emp=" + dbStr
							+ "FK_Emp ";
					ps.Add("FK_Prj", prjNo);
					ps.AddFK_Emp();
					// ps.AddFK_Emp();

					if (DBAccess.RunSQLReturnTable(ps).Rows.size() == 0) {
						throw new Exception("您不是(" + prjNo + "," + prjName + ")成员，您不能发起改流程。");
					} else {
						throw new Exception("您属于这个项目(" + prjNo + "," + prjName + ")，但是在此项目下您没有发起改流程的岗位");
					}
				}
			}
		}
		// #endregion 判断特殊的业务逻辑。
		currWK.SetValByKey("Rec", WebUser.getNo());
		currWK.SetValByKey("FK_Dept", WebUser.getFK_Dept());
		currWK.SetValByKey("FK_NY", BP.DA.DataType.getCurrentYearMonth());

		// 处理节点表单保存事件.
		currND.getMapData().getFrmEvents().DoEventNode(FrmEventList.SaveBefore, currWK);
		try {
			if (currND.getIsStartNode()) {
				currWK.setFID(0);
			}

			if (currND.getHisFlow().getIsMD5()) {
				/* 重新更新md5值. */
				currWK.SetValByKey("MD5", BP.WF.Glo.GenerMD5(currWK));
			}

			if (currND.getIsStartNode() && isSave)
				currWK.SetValByKey(StartWorkAttr.Title, WorkNode.GenerTitle(currND.getHisFlow(), currWK));

			currWK.Update();
			/* 如果是保存 */
		} catch (Exception ex) {
			try {
				currWK.CheckPhysicsTable();
			} catch (Exception ex1) {
				throw new Exception("@保存错误:" + ex.getMessage() + "@检查物理表错误：" + ex1.getMessage());
			}
			throw new Exception(ex.getMessage() + "@有可能此错误被系统自动修复,请您从新保存一次.");
			// this.Btn_Send.Enabled = true;
			// this.Pub1.AlertMsg_Warning("错误", ex.getMessage() +
			// "@有可能此错误被系统自动修复,请您从新保存一次.");
		}

		// region 处理保存后事件
		boolean isHaveSaveAfter = false;
		try {
			// 处理表单保存后。
			String s = currND.getMapData().getFrmEvents().DoEventNode(FrmEventList.SaveAfter, currWK);
			if (s != null) {
				/* 如果不等于null,说明已经执行过数据保存，就让其从数据库里查询一次。 */
				currWK.RetrieveFromDBSources();
				isHaveSaveAfter = true;
			}
		} catch (Exception ex) {
			// this.Response.Write(ex.Message);
			printResult(toJson("alert", ex.getMessage().replace("'", "‘")));
			return;
		}
		// endregion

		// region 2012-10-15 数据也要保存到Rpt表里.
		if (currND.getSaveModel() == SaveModel.NDAndRpt) {
			/* 如果保存模式是节点表与Node与Rpt表. */
			WorkNode wn = new WorkNode(currWK, currND);
			GERpt rptGe = currND.getHisFlow().getHisGERpt();
			rptGe.SetValByKey("OID", workID);
			wn.rptGe = rptGe;
			if (rptGe.RetrieveFromDBSources() == 0) {
				rptGe.SetValByKey("OID", workID);
				wn.DoCopyRptWork(currWK);

				rptGe.SetValByKey(GERptAttr.FlowEmps, "@" + WebUser.getNo() + "," + WebUser.getName());
				rptGe.SetValByKey(GERptAttr.FlowStarter, WebUser.getNo());
				rptGe.SetValByKey(GERptAttr.FlowStartRDT, DataType.getCurrentDataTime());
				rptGe.SetValByKey(GERptAttr.WFState, 0);

				rptGe.setWFState(WFState.Draft);

				rptGe.SetValByKey(GERptAttr.FK_NY, DataType.getCurrentYearMonth());
				rptGe.SetValByKey(GERptAttr.FK_Dept, WebUser.getFK_Dept());
				rptGe.Insert();
			} else {
				wn.DoCopyRptWork(currWK);
				rptGe.Update();
			}
		}
		// #endregion

		if (Glo.getIsEnableDraft() && currND.getIsStartNode()) {
			/* 如果启用草稿, 并且是开始节点. */
			BP.WF.Dev2Interface.Node_SaveWork(fk_flow, Integer.parseInt(fk_node), Long.parseLong(workID));
		}

		String msg = "";
		// 调用工作流程，处理节点信息采集后保存后的工作。
		if (isSave) {
			// return;
			if (isHaveSaveAfter) {
				/* 如果有保存后事件，就让其重新绑定. */
				currWK.RetrieveFromDBSources();
				// this.UCEn1.ResetEnVal(currWK);
				printResult(toJson(currWK));
				return;
			}

			if (StringHelper.isNullOrEmpty(workID))
				return;

			currWK.RetrieveFromDBSources();
			// this.UCEn1.ResetEnVal(currWK);
			printResult(toJson(currWK));

			// printResult("<script>window.location.reload();</script>");
			return;
		}
		// GenerWorkFlow gwf = new GenerWorkFlow(Long.parseLong(workID));
		// //检查是否是退回?
		// if (gwf.getWFState() == WFState.ReturnSta &&
		// gwf.getParas_IsTrackBack() == false)
		// {
		// /* 如果是退回 */
		// }
		// else
		// {
		if (currND.getCondModel() == CondModel.ByUserSelected && currND.getHisToNDNum() > 1) {
			// 如果是用户选择的方向条件.
			printResult(toJson("url", Glo.getCCFlowAppPath() + "WF/WorkOpt/ToNodes.jsp?FK_Flow=" + fk_flow + "&FK_Node="
					+ fk_node + "&WorkID=" + workID + "&FID=" + fid));
			return;
		}
		// }

		// 执行发送.
		WorkNode firstwn = new WorkNode(currWK, currND);
		try {
			msg = firstwn.NodeSend().ToMsgOfHtml();
		} catch (Exception exSend) {
			String msge = StringHelper.isEmpty(exSend.getMessage(), "");
			if (msge.contains("请选择下一步骤工作")) {
				/* 如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员. */
				String url = Glo.getCCFlowAppPath() + "WF/WorkOpt/ToNodes.jsp?FK_Flow=" + this.fk_flow + "&FK_Node="
						+ fk_node + "&WorkID=" + workID + "&FID=" + this.fid;
				printResult(toJson("url", url));
				return;
			} else {
				logger.info("ERROR", exSend);
			}

			StringBuffer errorMsg = new StringBuffer();
			errorMsg.append(BaseModel.AddFieldSetGreen("错误"));
			errorMsg.append(msge.replace("@@", "@").replace("@", "<BR>@"));
			errorMsg.append(BaseModel.AddFieldSetEnd());

			printResult(toJson("flowMsg", errorMsg.toString()));
			return;
		}

		// #region 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
		try {
			// 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
			BP.WF.Glo.DealBuinessAfterSendWork(fk_flow, Long.parseLong(workID), getParameter("DoFunc"),
					getParameter("WorkIDs"), getParameter("CFlowNo"), 0, null);
		} catch (Exception ex) {
			throw new Exception(msg + ex.getMessage());
		}
		// #endregion 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.

		// this.Btn_Send.Enabled = false;
		/* 处理转向问题. */
		switch (firstwn.getHisNode().getHisTurnToDeal()) {
		case SpecUrl:
			String myurl = firstwn.getHisNode().getTurnToDealDoc();
			if (myurl.contains("&") == false)
				myurl += "?1=1";
			Attrs myattrs = firstwn.getHisWork().getEnMap().getAttrs();
			Work hisWK = firstwn.getHisWork();
			for (Attr attr : Attrs.convertAttrs(myattrs)) {
				if (myurl.contains("@") == false)
					break;
				myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
			}
			if (myurl.contains("@")) {
				throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);
			}
			myurl += "&FromFlow=" + fk_flow + "&FromNode=" + fk_node + "&PWorkID=" + workID + "&UserNo="
					+ WebUser.getNo() + "&SID=" + WebUser.getSID();
			printResult(toJson("url", myurl));
			return;

		case TurnToByCond:
			TurnTos tts = new TurnTos(fk_flow);
			if (tts.size() == 0) {
				throw new Exception("@您没有设置节点完成后的转向条件。");
			}
			for (TurnTo tt : TurnTos.convertTurnTos(tts)) {
				tt.HisWork = firstwn.getHisWork();
				if (tt.getIsPassed() == true) {
					String url = tt.getTurnToURL();
					if (url.contains("&") == false)
						url += "?1=1";
					Attrs attrs = firstwn.getHisWork().getEnMap().getAttrs();
					Work hisWK1 = firstwn.getHisWork();
					for (Attr attr : Attrs.convertAttrs(attrs)) {
						if (url.contains("@") == false)
							break;
						url = url.replace("@" + attr.getKey(), hisWK1.GetValStrByKey(attr.getKey()));
					}
					if (url.contains("@")) {
						throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);
					}

					url += "&PFlowNo=" + fk_flow + "&FromNode=" + fk_node + "&PWorkID=" + workID + "&UserNo="
							+ WebUser.getNo() + "&SID=" + WebUser.getSID();
					printResult(toJson("url", url));
					return;
				}
			}

			// #warning 为上海修改了如果找不到路径就让它按系统的信息提示。
			printResult(toJson("url", getToMsg(msg)));
			// throw new Exception("您定义的转向条件不成立，没有出口。");
			break;
		default:
			printResult(toJson("url", getToMsg(msg)));
			break;
		}
		return;
	}

	/**
	 * 输出Alert
	 * 
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	private void printResult(String result) {
		_response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = _response.getWriter();
			out.write(result);

			out.flush();
		} catch (IOException e) {
			logger.info("ERROR", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 重定向到myflowInfo.jsp
	 * 
	 * @param msg
	 */
	public final String getToMsg(String msg) {
		_request.getSession().setAttribute("info", msg.trim());
		try {
			BP.WF.Glo.setSessionMsg(msg);
			return BP.WF.Glo.getCCFlowAppPath() + "WF/MyFlowInfo.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node
					+ "&WorkID=" + workID + "&FID=" + fid + "&FK_Emp=" + WebUser.getNo() + "&SID=" + WebUser.getSID();

		} catch (Exception e) {
			logger.info("ERROR", e);
		}
		return "";
	}

	/**
	 * 获取vlaue
	 * 
	 * @param key
	 * @return
	 */
	private String getParameter(String key) {
		String value = _request.getParameter(key);
		if (StringHelper.isNullOrEmpty(value)) {
			value = "";
		}
		return value;
	}

	/**
	 * 处理返回值
	 * 
	 * @param type
	 * @param action
	 * @return
	 */
	private String toJson(String type, String action) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", type);
		jsonObject.put("action", action);

		return jsonObject.toString();
	}

	/**
	 * 保存返回值
	 * 
	 * @param en
	 * @return
	 */
	private String toJson(Entity en) {
		// 控件id:控件值
		HashMap<String, String> map = new HashMap<String, String>();
		// 获取数据
		Attrs attrs = en.getEnMap().getAttrs();
		String ctlid = "";
		String value = "";
		for (Attr attr : attrs) {
			switch (attr.getUIContralType()) {
			case TB:
				ctlid = "TB_" + attr.getKeyLowerCase();
				break;
			case DDL:
				ctlid = "DDL_" + attr.getKeyLowerCase();
				break;
			case CheckBok:
				ctlid = "CB_" + attr.getKeyLowerCase();
				break;
			default:
				break;
			}
			value = en.GetValStrByKey(attr.getKey());

			map.put(ctlid, value);
		}

		// 转换json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "data");
		jsonObject.put("rows", JSONObject.fromObject(map));

		return jsonObject.toString();
	}

}