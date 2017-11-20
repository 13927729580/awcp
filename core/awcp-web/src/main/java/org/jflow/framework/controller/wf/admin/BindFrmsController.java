package org.jflow.framework.controller.wf.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.controller.wf.workopt.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.Sys.Frm.AppType;
import BP.Sys.Frm.FrmType;
import BP.Sys.Frm.MapData;
import BP.Sys.Frm.MapDataAttr;
import BP.Sys.Frm.MapDatas;
import BP.Tools.StringHelper;
import BP.WF.Template.Form.FrmNode;
import BP.WF.Template.Form.FrmNodeAttr;
import BP.WF.Template.Form.FrmNodes;
import BP.WF.Template.Form.WhoIsPK;

@Controller
@RequestMapping("/WF/Admin")
public class BindFrmsController extends BaseController {

	@RequestMapping(value = "/SaveFlowFrms", method = RequestMethod.POST)
	public ModelAndView SaveFlowFrms(HttpServletRequest request,
			HttpServletResponse response) {

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		MapDatas mds = new MapDatas();
		mds.Retrieve(MapDataAttr.AppType, AppType.Application.getValue());
		// Node nd = new Node(this.FK_Node);
		String ids = ",";
		for (MapData md : MapDatas.convertMapDatas(mds)) {
			String cb = request.getParameter("CB_" + md.getNo());
			if (cb == null) {
				continue;
			}
			ids += md.getNo() + ",";
		}

		// 删除已经删除的。
		for (FrmNode fn : FrmNodes.convertFrmNodes(fns)) {
			if (ids.contains("," + fn.getFK_Frm() + ",") == false) {
				fn.Delete();
				continue;
			}
		}

		// 增加集合中没有的。
		String[] strs = ids.split("[,]", -1);
		for (String s : strs) {
			if (StringHelper.isNullOrEmpty(s)) {
				continue;
			}
			if (fns.Contains(FrmNodeAttr.FK_Frm, s)) {
				continue;
			}

			FrmNode fn = new FrmNode();
			fn.setFK_Frm(s);
			fn.setFK_Flow(this.getFK_Flow());
			fn.setFK_Node(this.getFK_Node());
			fn.Save();
		}
		ModelAndView andView = new ModelAndView("redirect:BindFrms.jsp");
		andView.addObject("FK_Node", this.getFK_Node());
		andView.addObject("FK_Flow", this.getFK_Flow());
		return andView;
	}

	@RequestMapping(value = "/SavePowerOrders", method = RequestMethod.POST)
	public ModelAndView SavePowerOrders(HttpServletRequest request,
			HttpServletResponse response) {

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		for (FrmNode fn : FrmNodes.convertFrmNodes(fns)) {
			String cb_isEdit = getParamter(("CB_IsEdit_" + fn.getFK_Frm()));
			if (cb_isEdit == null) {
				fn.setIsEdit(false);
			} else {
				fn.setIsEdit(true);
			}

			String cb_isPrint = getParamter(("CB_IsPrint_" + fn.getFK_Frm()));
			if (cb_isPrint == null) {
				fn.setIsPrint(false);
			} else {
				fn.setIsPrint(true);
			}

			// 是否启
			String cb_isEnableLoadData = getParamter(("CB_IsEnableLoadData_" + fn
					.getFK_Frm()));
			if (cb_isEnableLoadData == null) {
				fn.setIsEnableLoadData(false);
			} else {
				fn.setIsEnableLoadData(true);
			}

			// 是否启
			String idx = getParamter(("TB_Idx_" + fn.getFK_Frm()));
			if (StringHelper.isNullOrEmpty(idx)) {
				fn.setIdx(0);
				;
			} else {
				fn.setIdx(Integer.parseInt(idx));
			}
			String frmType = getParamter("DDL_FrmType_" + fn.getFK_Frm());
			fn.setHisFrmType(FrmType.forValue(Integer.parseInt(frmType)));

			// 权限控制方案.
			String sln = getParamter("DDL_Sln_" + fn.getFK_Frm());
			fn.setFrmSln(Integer.parseInt(sln));

			String whoIsPK = getParamter("DDL_WhoIsPK_" + fn.getFK_Frm());
			fn.setWhoIsPK(WhoIsPK.forValue(Integer.parseInt(whoIsPK)));

			fn.setFK_Flow(this.getFK_Flow());
			fn.setFK_Node(this.getFK_Node());

			fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node() + "_"
					+ fn.getFK_Flow());

			fn.Update();
		}
		ModelAndView andView = new ModelAndView("redirect:BindFrms.jsp");
		andView.addObject("FK_Node", this.getFK_Node());
		andView.addObject("FK_Flow", this.getFK_Flow());
		andView.addObject("ShowType", "EditPowerOrder");
		return andView;
	}
}
