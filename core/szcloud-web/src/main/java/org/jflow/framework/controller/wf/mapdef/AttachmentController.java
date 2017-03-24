package org.jflow.framework.controller.wf.mapdef;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.system.ui.core.Button;
import org.jflow.framework.system.ui.core.HtmlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.Sys.Frm.AthCtrlWay;
import BP.Sys.Frm.AthUploadWay;
import BP.Sys.Frm.AttachmentUploadType;
import BP.Sys.Frm.FileShowWay;
import BP.Sys.Frm.FrmAttachment;
import BP.Sys.Frm.GroupField;
import BP.Sys.Frm.GroupFields;
import BP.Tools.StringHelper;

@Controller
@RequestMapping("/mapdef")
public class AttachmentController {
	@RequestMapping(value = "/condByPara_btn_Click", method = RequestMethod.POST)
	public ModelAndView btn_Click(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView();
		FrmAttachment ath = new FrmAttachment();
		String TB_Name=req.getParameter("TB_Name")==null?"":req.getParameter("TB_Name");
		int FK_Node = req.getParameter("FK_Node") == null ? 0 : Integer
				.parseInt(req.getParameter("FK_Node"));
		String FK_MapData = req.getParameter("FK_MapData") == null ? "test"
				: req.getParameter("FK_MapData");
		String Ath = req.getParameter("Ath");
		String TB_FastKeyGenerRole = req.getParameter("TB_FastKeyGenerRole");
		String btnId = req.getParameter("btnId");
		// 下拉框选中的值
		int DDL_GroupField = Integer.parseInt(req
				.getParameter("DDL_GroupField"));
		int DDL_CtrlWay = Integer.parseInt(req.getParameter("DDL_CtrlWay"));
		int DDL_AthUploadWay = Integer.parseInt(req
				.getParameter("DDL_AthUploadWay"));
		int DDL_FileShowWay = Integer.parseInt(req
				.getParameter("DDL_FileShowWay"));
		String UploadType = req.getParameter("UploadType");
		String TB_Sort=req.getParameter("TB_Sort")==null?"":req.getParameter("TB_Sort");
		String TB_Exts=req.getParameter("TB_Exts")==null?"":req.getParameter("TB_Exts");
		if (FK_Node == 0)
			ath.setMyPK(FK_MapData + "_" + Ath);
		else
			ath.setMyPK(FK_MapData + "_" + Ath + "_" + FK_Node);

		ath.RetrieveFromDBSources();

		if (btnId.equals("Btn_Delete")) {
			// ath.MyPK = this.FK_MapData + "_" + this.Ath;
			ath.Delete();
			mv.addObject("FK_Node", FK_Node);
			mv.addObject("FK_MapData", FK_MapData);
			mv.addObject("Ath", Ath);
			mv.addObject("success", "删除成功");
			mv.setViewName("WF/MapDef/Attachment");
			// this.WinClose("删除成功.");
			return mv;
		}

		ath.setMyPK(FK_MapData + "_" + Ath);
		if (Ath != null)
			ath.RetrieveFromDBSources();
		// ath = this.Pub1.Copy(ath) as FrmAttachment;
		ath.setFK_MapData(FK_MapData);
		ath.setFK_Node(FK_Node);
		if (StringHelper.isNullOrEmpty(Ath) == false)
			ath.setNoOfObj(Ath);

		if (FK_Node == 0)
			ath.setMyPK(FK_MapData + "_" + ath.getNoOfObj());
		else
			ath.setMyPK(FK_MapData + "_" + ath.getNoOfObj() + "_" + FK_Node);

		GroupFields gfs1 = new GroupFields(FK_MapData);
		if (gfs1.size() == 1) {
			GroupField gf = (GroupField) gfs1.get(0);
			ath.setGroupID((int) gf.getOID());
		} else {
			ath.setGroupID(DDL_GroupField);// =
											// this.Pub1.GetDDLByID("DDL_GroupField").SelectedItemIntVal;
		}

		// 对流程的特殊判断.
		ath.setHisCtrlWay(AthCtrlWay.forValue(DDL_CtrlWay));// (AthCtrlWay)this.Pub1.GetDDLByID("DDL_CtrlWay").SelectedItemIntVal;
		ath.setAthUploadWay(AthUploadWay.forValue(DDL_AthUploadWay));// =
																		// (AthUploadWay)this.Pub1.GetDDLByID("DDL_AthUploadWay").SelectedItemIntVal;
		ath.setFileShowWay(FileShowWay.forValue(DDL_FileShowWay));// =
																	// (FileShowWay)this.Pub1.GetDDLByID("DDL_FileShowWay").SelectedItemIntVal;
																	// //文件展现方式.

		ath.setExts(TB_Exts);
		ath.setSort(TB_Sort);
		String CB_IsAutoSize = req.getParameter("CB_IsAutoSize") == null ? ""
				: req.getParameter("CB_IsAutoSize");
		if (CB_IsAutoSize.equals("on")) {
			ath.setIsAutoSize(true);
		} else {
			ath.setIsAutoSize(false);
		}
		String CB_IsNote = req.getParameter("CB_IsNote") == null ? ""
				: req.getParameter("CB_IsNote");
		if (CB_IsNote.equals("on")) {
			ath.setIsNote(true);
		} else {
			ath.setIsNote(false);
		}
		String CB_IsShowTitle = req.getParameter("CB_IsShowTitle") == null ? ""
				: req.getParameter("CB_IsShowTitle");
		if (CB_IsShowTitle.equals("on")) {
			ath.setIsShowTitle(true);
		} else {
			ath.setIsShowTitle(false);
		}
		String CB_IsDownload = req.getParameter("CB_IsDownload") == null ? ""
				: req.getParameter("CB_IsDownload");
		if (CB_IsDownload.equals("on")) {
			ath.setIsDownload(true);
		} else {
			ath.setIsDownload(false);
		}
		int DDL_IsDelete = req.getParameter("DDL_IsDelete") == null ? 0
				: Integer.parseInt(req.getParameter("DDL_IsDelete"));
			ath.setIsDeleteInt(DDL_IsDelete);
		String CB_IsUpload = req.getParameter("CB_IsUpload") == null ? ""
				: req.getParameter("CB_IsUpload");
		if (CB_IsUpload.equals("on")) {
			ath.setIsUpload(true);
		} else {
			ath.setIsUpload(false);
		}
		String CB_IsOrder = req.getParameter("CB_IsOrder") == null ? ""
				: req.getParameter("CB_IsOrder");
		if (CB_IsOrder.equals("on")) {
			ath.setIsOrder(true);
		} else {
			ath.setIsOrder(false);
		}
		String CB_IsWoEnableWF = req.getParameter("CB_IsWoEnableWF") == null ? ""
				: req.getParameter("CB_IsWoEnableWF");
		if (CB_IsWoEnableWF.equals("on")) {
			ath.setIsWoEnableWF(true);
		} else {
			ath.setIsWoEnableWF(false);
		}
		String CB_IsWoEnableSave = req.getParameter("CB_IsWoEnableSave") == null ? ""
				: req.getParameter("CB_IsWoEnableSave");
		if (CB_IsWoEnableSave.equals("on")) {
			ath.setIsWoEnableSave(true);
		} else {
			ath.setIsWoEnableSave(false);
		}
		String CB_IsWoEnableInsertFengXian = req.getParameter("CB_IsWoEnableInsertFengXian") == null ? ""
				: req.getParameter("CB_IsWoEnableInsertFengXian");
		if (CB_IsWoEnableInsertFengXian.equals("on")) {
			ath.setIsWoEnableInsertFengXian(true);
		} else {
			ath.setIsWoEnableInsertFengXian(false);
		}
		String CB_IsWoEnableReadonly = req
				.getParameter("CB_IsWoEnableReadonly") == null ? "" : req
				.getParameter("CB_IsWoEnableReadonly");
		if (CB_IsWoEnableReadonly.equals("on")) {
			ath.setIsWoEnableReadonly(true);
		} else {
			ath.setIsWoEnableReadonly(false);
		}
		String CB_IsWoEnableCheck = req
				.getParameter("CB_IsWoEnableCheck") == null ? "" : req
				.getParameter("CB_IsWoEnableCheck");
		if (CB_IsWoEnableCheck.equals("on")) {
			ath.setIsWoEnableCheck(true);
		} else {
			ath.setIsWoEnableCheck(false);
		}
		String CB_IsWoEnableInsertFlow = req
				.getParameter("CB_IsWoEnableInsertFlow") == null ? "" : req
				.getParameter("CB_IsWoEnableInsertFlow");
		if (CB_IsWoEnableInsertFlow.equals("on")) {
			ath.setIsWoEnableInsertFlow(true);
		} else {
			ath.setIsWoEnableInsertFlow(false);
		}
		String CB_IsWoEnableRevise = req.getParameter("CB_IsWoEnableRevise") == null ? ""
				: req.getParameter("CB_IsWoEnableRevise");
		if (CB_IsWoEnableRevise.equals("on")) {
			ath.setIsWoEnableRevise(true);
		} else {
			ath.setIsWoEnableRevise(false);
		}
		String CB_IsWoEnableViewKeepMark = req
				.getParameter("CB_IsWoEnableViewKeepMark") == null ? "" : req
				.getParameter("CB_IsWoEnableViewKeepMark");
		if (CB_IsWoEnableViewKeepMark.equals("on")) {
			ath.setIsWoEnableViewKeepMark(true);
		} else {
			ath.setIsWoEnableViewKeepMark(false);
		}
		String CB_IsWoEnablePrint = req.getParameter("CB_IsWoEnablePrint") == null ? ""
				: req.getParameter("CB_IsWoEnablePrint");
		if (CB_IsWoEnablePrint.equals("on")) {
			ath.setIsWoEnablePrint(true);
		} else {
			ath.setIsWoEnablePrint(false);
		}
		String CB_IsWoEnableSeal = req.getParameter("CB_IsWoEnableSeal") == null ? ""
				: req.getParameter("CB_IsWoEnableSeal");
		if (CB_IsWoEnableSeal.equals("on")) {
			ath.setIsWoEnableSeal(true);
		} else {
			ath.setIsWoEnableSeal(false);
		}
		String CB_IsWoEnableOver = req.getParameter("CB_IsWoEnableOver") == null ? ""
				: req.getParameter("CB_IsWoEnableOver");
		if (CB_IsWoEnableOver.equals("on")) {
			ath.setIsWoEnableOver(true);
		} else {
			ath.setIsWoEnableOver(false);
		}
		String CB_IsWoEnableTemplete = req
				.getParameter("CB_IsWoEnableTemplete") == null ? "" : req
				.getParameter("CB_IsWoEnableTemplete");
		if (CB_IsWoEnableTemplete.equals("on")) {
			ath.setIsWoEnableTemplete(true);
		} else {
			ath.setIsWoEnableTemplete(false);
		}
		String CB_IsWoEnableMarks = req.getParameter("CB_IsWoEnableMarks") == null ? ""
				: req.getParameter("CB_IsWoEnableMarks");
		if (CB_IsWoEnableMarks.equals("on")) {
			ath.setIsWoEnableMarks(true);
		} else {
			ath.setIsWoEnableMarks(false);
		}
		String CB_IsWoEnableDown = req.getParameter("CB_IsWoEnableDown") == null ? ""
				: req.getParameter("CB_IsWoEnableDown");
		if (CB_IsWoEnableDown.equals("on")) {
			ath.setIsWoEnableDown(true);
		} else {
			ath.setIsWoEnableDown(false);
		}
		String CB_FastKeyIsEnable = req.getParameter("CB_FastKeyIsEnable") == null ? ""
				: req.getParameter("CB_FastKeyIsEnable");
		if (CB_FastKeyIsEnable.equals("on")) {
			ath.setFastKeyIsEnable(true);
		} else {
			ath.setFastKeyIsEnable(false);
		}
		ath.setName(TB_Name);
		// ath.setIsWoEnableWF(true);//((CheckBox)this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableWF)).getChecked());
		// ath.IsWoEnableSave = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableSave).Checked;
		// ath.IsWoEnableReadonly = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableReadonly).Checked;
		// ath.IsWoEnableRevise = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableRevise).Checked;
		// ath.IsWoEnableViewKeepMark = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableViewKeepMark).Checked;
		// ath.IsWoEnablePrint = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnablePrint).Checked;
		// ath.IsWoEnableSeal = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableSeal).Checked;
		// ath.IsWoEnableOver = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableOver).Checked;
		// ath.IsWoEnableTemplete = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableTemplete).Checked;
		// ath.IsWoEnableCheck = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableCheck).Checked;
		// ath.IsWoEnableInsertFengXian = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableInsertFengXian).Checked;
		// ath.IsWoEnableInsertFlow = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableInsertFlow).Checked;
		// ath.IsWoEnableMarks = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableMarks).Checked;
		// ath.IsWoEnableDown = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.IsWoEnableDown).Checked;
		//
		//
		// ath.FastKeyIsEnable = this.Pub1.GetUIByID("CB_" +
		// FrmAttachmentAttr.FastKeyIsEnable).Checked;
		ath.setFastKeyGenerRole(TB_FastKeyGenerRole);// =
														// this.Pub1.GetTBByID("TB_"
														// +
														// FrmAttachmentAttr.FastKeyGenerRole).Text;

		if (ath.getFastKeyIsEnable() == true)
			if (ath.getFastKeyGenerRole().contains("*OID") == false)
				throw new Exception("@快捷键生成规则必须包含*OID,否则会导致文件名重复.");

		if (Ath == null) {
			ath.setUploadType(AttachmentUploadType.forValue(Integer
					.parseInt(UploadType)));

			if (FK_Node == 0)
				ath.setMyPK(FK_MapData + "_" + ath.getNoOfObj());
			else
				ath.setMyPK(FK_MapData + "_" + ath.getNoOfObj() + "_" + FK_Node);

			if (ath.getNoOfObj() == TB_FastKeyGenerRole) {
				mv.addObject("FK_Node", FK_Node);
				mv.addObject("FK_MapData", FK_MapData);
				mv.addObject("Ath", Ath);
				mv.addObject("success", "附件编号(" + ath.getNoOfObj() + ")已经存在");
				mv.setViewName("WF/MapDef/Attachment");
				// this.WinClose("删除成功.");
				return mv;
			}
			ath.Insert();
			mv.addObject("FK_Node", FK_Node);
			mv.addObject("FK_MapData", FK_MapData);
			mv.addObject("Ath", Ath);
			mv.addObject("success", "删除成功！");
			mv.setViewName("WF/MapDef/Attachment");
			// this.WinClose("删除成功.");
			return mv;
		} else {
			ath.setNoOfObj(Ath);
			if (FK_Node == 0)
				ath.setMyPK(FK_MapData + "_" + Ath);
			else
				ath.setMyPK(FK_MapData + "_" + Ath + "_" + FK_Node);

			ath.Update();
		}
		mv.addObject("FK_Node", FK_Node);
		mv.addObject("FK_MapData", FK_MapData);
		mv.addObject("Ath", Ath);
		mv.addObject("success", "保存成功！");
		mv.setViewName("WF/MapDef/Attachment");
		// this.WinClose("删除成功.");
		return mv;
	}
}
