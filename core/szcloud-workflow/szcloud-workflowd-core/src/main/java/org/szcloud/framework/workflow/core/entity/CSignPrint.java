package org.szcloud.framework.workflow.core.entity;

import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.ECellAppStyle;
import org.szcloud.framework.workflow.core.emun.EEntryType;
import org.szcloud.framework.workflow.core.emun.EFormCellAccessType;
import org.szcloud.framework.workflow.core.emun.EFormCellStyle;
import org.szcloud.framework.workflow.core.module.MFile;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 保存签名图象到文件中的数据处理对象
 * 
 * @author Jackie.Wang
 * 
 */
public class CSignPrint {

	/**
	 * 登录对象
	 */
	public CLogon Logon = null;

	/**
	 * 所属的公文对象
	 */
	public CWorkItem WorkItem = null;

	/**
	 * 公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public CSignPrint(CLogon ao_Logon) {
		this.Logon = ao_Logon;
	}

	/**
	 * 清除内存
	 */
	public void ClearUp() {
		this.WorkItem = null;
		this.Cyclostyle = null;
		this.Logon = null;
	}

	/**
	 * 读取签名图片存储于数据库中的内容的XML文本
	 * 
	 * @param ao_FormCell
	 * @return
	 * @throws Exception
	 */
	public String getCellSignPicXml(CFormCell ao_FormCell) throws Exception {
		if (ao_FormCell.CellStyle != EFormCellStyle.SignStyle)
			return "";
		if (ao_FormCell.Access == EFormCellAccessType.FormCellDisVisible)
			return "";// 不可见

		String ls_ActivityNames = ";" + ao_FormCell.getCellPropByCode("ActivityNames").getInitValue();
		String ls_RoleNames = ";" + ao_FormCell.getCellPropByCode("RoleNames").getInitValue();
		if (ls_ActivityNames.equals(";") && ls_RoleNames.equals(";"))
			return "";

		String ls_ActivityIDs = ";";
		for (CFlow lo_Flow : this.Cyclostyle.Flows) {
			for (CActivity lo_Activity : lo_Flow.Activities) {
				if (ls_ActivityNames.indexOf(";" + lo_Activity.ActivityName + ";") > -1) {
					ls_ActivityIDs += String.valueOf(lo_Flow.FlowID) + "-" + String.valueOf(lo_Activity.ActivityID) + ";";
				}
			}
		}

		String ls_RoleIDs = ";";
		for (CRole lo_Role : this.Cyclostyle.Roles) {
			if (ls_RoleNames.indexOf(";" + lo_Role.RoleName + ";") > -1)
				ls_RoleIDs += String.valueOf(lo_Role.RoleID) + ";";
		}
		if (ls_ActivityIDs.equals(";") && ls_RoleIDs.equals(";"))
			return "";

		String ls_Value = "";
		for (CEntry lo_Entry : this.WorkItem.getEntries()) {
			if (lo_Entry.EntryType == EEntryType.ActualityEntry) {
				if (ls_ActivityIDs.indexOf(";" + String.valueOf(lo_Entry.FlowID) + "-" + String.valueOf(lo_Entry.ActivityID) + ";") > -1
						|| ls_RoleIDs.indexOf(";" + String.valueOf(lo_Entry.RoleID + ";")) > -1) {
					ls_Value += lo_Entry.Sign;
				}
			}
		}
		if (ls_Value.equals(";"))
			return "";

		// 获取数据库中签名图象
		String ls_SignIDs = "";
		Document lo_Xml = MXmlHandle.LoadXml("<Root>" + ls_Value + "</Root>");
		String ls_SignValue, ls_Array[], ls_Sign;
		int i, j;
		if (lo_Xml != null) {
			for (int k = 0; k < lo_Xml.getDocumentElement().getChildNodes().getLength(); i++) {
				Element lo_Node = (Element) lo_Xml.getDocumentElement().getChildNodes().item(k);
				ls_SignValue = lo_Node.getAttribute("Value");
				i = ls_SignValue.indexOf("@Image=") + 1;
				while (i > 0) {
					j = ls_SignValue.indexOf("@", i + 1);
					if (j == 0) {
						ls_Array = MGlobal.right(ls_SignValue, ls_SignValue.length() - i - 6).split(",");
						ls_Sign = ls_Array[2] + ";";
						i = 0;
					} else {
						ls_Array = ls_SignValue.substring(i + 7, j - i - 7).split(",");
						ls_Sign = ls_Array[2] + ";";
						i = ls_SignValue.indexOf("@", j);
					}
					if ((";" + ls_SignIDs).indexOf(";" + ls_Sign) == -1)
						ls_SignIDs += ls_Sign;
				}
			}

		}
		lo_Xml = null;

		String ls_FileName = MFile.getTempFile("bmp", this.Logon.TempPath);

		if (ls_SignIDs.equals("")) {
			lo_Xml = MXmlHandle.LoadXml("<Root Type=\"0\" />");
		} else {
			lo_Xml = this.Logon.getWorkAdmin().getSignDesignObject(ls_SignIDs);
			lo_Xml.getDocumentElement().setAttribute("Type", "1");
		}
		int ll_Width, ll_Height;
		if (ao_FormCell.getCellAppStyle(ECellAppStyle.hSizeUnitAppStyle) == ECellAppStyle.hSizeUnitAppStyle) {
			ll_Width = (int) 0.01 * ao_FormCell.getRectangle().getRectWidth() * ao_FormCell.getParentTabCell().getCellWidth();
		} else {
			ll_Width = ao_FormCell.getRectangle().getRectWidth();
		}
		if (ao_FormCell.getCellAppStyle(ECellAppStyle.vSizeUnitAppStyle) == ECellAppStyle.vSizeUnitAppStyle) {
			ll_Height = (int) 0.01 * ao_FormCell.getRectangle().getRectHeight() * ao_FormCell.getParentTabCell().getCellHeight();
		} else {
			ll_Height = ao_FormCell.getRectangle().getRectHeight();
		}

		lo_Xml.getDocumentElement().setAttribute("Width", String.valueOf(ll_Width));
		lo_Xml.getDocumentElement().setAttribute("Height", String.valueOf(ll_Height));
		lo_Xml.getDocumentElement().setAttribute("Sign", "<Sign>" + ls_Value + "</Sign>");
		lo_Xml.getDocumentElement().setAttribute("FileName", ls_FileName);
		lo_Xml.getDocumentElement().setAttribute("UserID", String.valueOf(this.Logon.GlobalPara.UserID));

		return MXmlHandle.getXml(lo_Xml);
	}

}
