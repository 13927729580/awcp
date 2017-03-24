package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EBodyAccessType;
import org.szcloud.framework.workflow.core.emun.ECommentAccessType;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EDocumentAccessType;
import org.szcloud.framework.workflow.core.emun.EFlowAccessType;
import org.szcloud.framework.workflow.core.emun.EResponseRight;
import org.szcloud.framework.workflow.core.emun.EResponseType;
import org.szcloud.framework.workflow.core.emun.ERightType;
import org.szcloud.framework.workflow.core.emun.EScriptCaculateType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 公文权限对象：公文处理对象模型中，控制各种数据在不同处理人不同状态下的权限，控制的权限数据有正文、附件和表单定义数据。
 * 
 * @author Jackie.wang
 * 
 */
public class CRight extends CBase {

	/**
	 * 初始化
	 */
	public CRight() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CRight(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 权限标识
	 */
	public int RightID = -1;

	/**
	 * 权限名称
	 */
	public String RightName = "";

	/**
	 * 设置权限名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setRightName(String value) throws Exception {
		if (value == null) {
			// #错误：公文数据权限的名称不能为空
			this.Raise(1091, "setRightName", "");
			return;
		}

		if (this.RightName == value)
			return;
		if (this.RightName == "缺省权限")
			return;
		if (MGlobal.BeyondOfLength(value, 50) == false)
			return;

		for (CRight lo_Right : this.Cyclostyle.Rights) {
			if (lo_Right.RightType == this.RightType) // #同一种权限的名称不能重复
			{
				if (lo_Right.RightName == value) {
					// #错误：公文数据权限的名称不能重复
					this.Raise(1092, "setRightName", "");
					return;
				}
			}
		}

		this.RightName = value;
	}

	/**
	 * 权限类别
	 */
	public int RightType = ERightType.OtherRightType;

	/**
	 * 读取权限类别名称
	 */
	public String getRightTypeName() {
		switch (this.RightType) {
		case ERightType.DefaultRightType:
			return "缺省权限";
		case ERightType.StartRightType:
			return "开始步骤权限";
		case ERightType.OtherRightType:
			return "其他权限";
		}
		return "";
	}

	/**
	 * 角色打开时计算权限，是角色标识的连接字符串
	 */
	public String RoleOpenAccesses = "";

	/**
	 * 角色发送时计算权限，是角色标识的连接字符串
	 */
	public String RoleSendAccesses = "";

	/**
	 * 角色发送后计算权限，是角色标识的连接字符串
	 */
	public String RoleSendAfterAccesses = "";

	/**
	 * 角色保存时计算权限，是角色标识的连接字符串
	 */
	public String RoleSaveAccesses = "";

	/**
	 * 表头属性打开时计算权限，是表头属性标识的连接字符串
	 */
	public String PropOpenAccesses = "";

	/**
	 * 表头属性发送时计算权限，是表头属性标识的连接字符串
	 */
	public String PropSendAccesses = "";

	/**
	 * 表头属性发送后计算权限，是表头属性标识的连接字符串
	 */
	public String PropSendAfterAccesses = "";

	/**
	 * 表头属性保存时计算权限，是表头属性标识的连接字符串
	 */
	public String PropSaveAccesses = "";

	/**
	 * 流程权限
	 */
	public int FlowAccess = EFlowAccessType.FlowReadOnly;

	/**
	 * 读取流程权限名称
	 */
	public String getFlowAccessName() {
		switch (this.FlowAccess) {
		case EFlowAccessType.FlowDisVisible:
			return "不可见";
		case EFlowAccessType.FlowReadOnly:
			return "只读";
		case EFlowAccessType.FlowReadWrite:
			return "可读写";
		}
		return "";
	}

	/**
	 * 正文权限
	 */
	public int BodyAccess = EBodyAccessType.BodyReadWrite;

	/**
	 * 读取正文权限名称
	 */
	public String getBodyAccessName() {
		switch (this.BodyAccess) {
		case EBodyAccessType.BodyDisVisible:
			return "不可见";
		case EBodyAccessType.BodyReadOnly:
			return "只读";
		case EBodyAccessType.BodyReadWrite:
			return "可读写";
		case EBodyAccessType.BodyLastVersion:
			return "只读(最终版本)";
		case EBodyAccessType.BodyLastReadWrite:
			return "可读写(最终版本)";
		}
		return "";
	}

	/**
	 * 附件权限
	 */
	public int DocumentAccess = EDocumentAccessType.DocumentReadWrite;

	/**
	 * 读取附件权限名称
	 */
	public String getDocumentAccessName() {
		switch (this.DocumentAccess) {
		case EDocumentAccessType.DocumentDisVisible:
			return "不可见";
		case EDocumentAccessType.DocumentReadOnly:
			return "只读";
		case EDocumentAccessType.DocumentReadWrite:
			return "可读写 (不可创建版本控制附件)";
		case EDocumentAccessType.DocumentWriteVersion:
			return "可读写 (可创建版本控制附件)";
		case EDocumentAccessType.DocumentDelete:
			return "可删除，赋予附件最高权限";
		}
		return "";
	}

	/**
	 * 数据表格权限，是数据表格标识连接串，连接起来表示不可见
	 */
	private String ms_DisVisibleFormIDs = "";

	/**
	 * 设置数据表格权限，是数据表格标识连接串，连接起来表示不可见
	 * 
	 * @param value
	 */
	public void setDisVisibleFormIDs(String value) {
		String ls_Str = "";
		for (CForm lo_Form : this.Cyclostyle.Forms) {
			if ((";" + value).indexOf(";" + Integer.toString(lo_Form.FormID)
					+ ";") > -1) {
				ls_Str += Integer.toString(lo_Form.FormID) + ";";
			}
		}

		ms_DisVisibleFormIDs = ls_Str;
	}

	/**
	 * 读取（不可见）数据表格权限名称连接串
	 */
	public String getDisVisibleFormNames() {
		String ls_Names = "";
		for (CForm lo_Form : Cyclostyle.Forms) {
			if ((";" + ms_DisVisibleFormIDs).indexOf(";"
					+ Integer.toString(lo_Form.FormID) + ";") > -1) {
				ls_Names += lo_Form.FormName + ";";
			}
		}
		return ls_Names;
	}

	/**
	 * 设置（不可见）数据表格权限名称连接串
	 */
	public void setDisVisibleFormNames(String value) {
		String ls_Str = "";
		for (CForm lo_Form : Cyclostyle.Forms) {
			if ((";" + value).indexOf(";" + lo_Form.FormName + ";") > -1) {
				ls_Str += Integer.toString(lo_Form.FormID) + ";";
			}
		}
		ms_DisVisibleFormIDs = ls_Str;
	}

	/**
	 * 读取（可见）数据表格权限，是数据表格标识连接串，连接起来表示可见
	 */
	public String getVisbleFormIDs() {
		String ls_IDs = "";
		for (CForm lo_Form : Cyclostyle.Forms) {
			if ((";" + ms_DisVisibleFormIDs).indexOf(";"
					+ Integer.toString(lo_Form.FormID) + ";") == -1) {
				ls_IDs += Integer.toString(lo_Form.FormID) + ";";
			}
		}
		return ls_IDs;
	}

	/**
	 * 设置（可见）数据表格权限，是数据表格标识连接串，连接起来表示可见
	 */
	public void setVisbleFormIDs(String value) {
		String ls_Str = "";
		for (CForm lo_Form : Cyclostyle.Forms) {
			if ((";" + value).indexOf(";" + Integer.toString(lo_Form.FormID)
					+ ";") == -1) {
				ls_Str += Integer.toString(lo_Form.FormID) + ";";
			}
		}

		ms_DisVisibleFormIDs = ls_Str;
	}

	/**
	 * 读取（可见）数据表格权限名称连接串
	 */
	public String getVisbleFormNames() {
		String ls_Names = "";
		for (CForm lo_Form : this.Cyclostyle.Forms) {
			if ((";" + ms_DisVisibleFormIDs).indexOf(";"
					+ Integer.toString(lo_Form.FormID) + ";") + 1 == 0) {
				ls_Names += lo_Form.FormName + ";";
			}
		}
		return ls_Names;
	}

	/**
	 * 设置（可见）数据表格权限名称连接串
	 */
	public void setVisbleFormNames(String value) {
		String ls_Str = "";
		for (CForm lo_Form : Cyclostyle.Forms) {
			if ((";" + value).indexOf(";" + lo_Form.FormName + ";") == -1) {
				ls_Str += Integer.toString(lo_Form.FormID) + ";";
			}
		}

		ms_DisVisibleFormIDs = ls_Str;
	}

	/**
	 * 表单图元权限 表单图元不可见权限，是表单图元标识的连接串，使用“;”分隔
	 */
	private String ms_CellDisVisibleIDs = "";

	/**
	 * 表单图元只读权限，使用缺省方式，不需要保存，除其他集中权限外的权限即可
	 */
	public String getCellDisVisibleIDs() {
		if (ms_CellDisVisibleIDs == null || ms_CellDisVisibleIDs.equals(""))
			return "";

		if (this.Cyclostyle.Forms.size() == 0) {
			ms_CellDisVisibleIDs = "";
			return "";
		}

		String ls_Str = "";
		for (CForm lo_Form : this.Cyclostyle.Forms) {
			for (CFormCell lo_Cell : lo_Form.FormCells) {
				if ((";" + ms_CellDisVisibleIDs).indexOf(";" + (lo_Cell.CellID)
						+ ";") + 1 > 0) {
					ls_Str += (lo_Cell.CellID) + ";";
				}
			}
		}

		ms_CellDisVisibleIDs = ls_Str;
		return ls_Str;
	}

	/**
	 * 表单图元只读权限，使用缺省方式，不需要保存，除其他集中权限外的权限即可
	 * 
	 * @param value
	 */
	public void setCellDisVisibleIDs(String value) {
		if (value == null || value.equals("")) {
			ms_CellDisVisibleIDs = "";
			return;
		}
		String ls_DisVis = "";
		String ls_Valids = ";" + ms_CellValidHandleIDs;
		String ls_Needs = ";" + ms_CellNeedHandleIDs;

		String ls_Str[] = value.split(";");
		for (int i = 0; i < ls_Str.length; i++) {
			if (ls_Str[i] != "") {
				ls_DisVis += ls_Str[i] + ";";
				ls_Valids = ls_Valids.replaceAll(";" + ls_Str[i] + ";", ";");
				ls_Needs = ls_Needs.replaceAll(";" + ls_Str[i] + ";", ";");
			}
		}

		ms_CellDisVisibleIDs = ls_DisVis;
		ms_CellValidHandleIDs = MGlobal
				.right(ls_Valids, ls_Valids.length() - 1);
		ms_CellNeedHandleIDs = MGlobal.right(ls_Needs, ls_Needs.length() - 1);
	}

	/**
	 * 表单图元只读权限，使用缺省方式，不需要保存，除其他集中权限外的权限即可
	 */
	public String getCellReadOnlyIDs() {
		String ls_Str = "";
		for (CForm lo_Form : Cyclostyle.Forms) {
			for (CFormCell lo_Cell : lo_Form.FormCells) {
				if (((";" + ms_CellDisVisibleIDs).indexOf(";"
						+ Integer.toString(lo_Cell.CellID) + ";") + 1 == 0)
						&& ((";" + ms_CellValidHandleIDs).indexOf(";"
								+ Integer.toString(lo_Cell.CellID) + ";") + 1 == 0)
						&& ((";" + ms_CellNeedHandleIDs).indexOf(";"
								+ Integer.toString(lo_Cell.CellID) + ";") + 1 == 0)) {
					ls_Str += Integer.toString(lo_Cell.CellID) + ";";
				}
			}
		}
		return ls_Str;
	}

	/**
	 * 表单图元只读权限，使用缺省方式，不需要保存，除其他集中权限外的权限即可
	 */
	public void setCellReadOnlyIDs(String value) {
		if (value == null || value.equals(""))
			return;

		String[] ls_Str = value.split(";");
		String ls_DisVis = ";" + ms_CellDisVisibleIDs;
		String ls_Valids = ";" + ms_CellValidHandleIDs;
		String ls_Needs = ";" + ms_CellNeedHandleIDs;

		for (int i = 0; i < ls_Str.length; i++) {
			if (ls_Str[i] != "") {
				ls_DisVis = ls_DisVis.replaceAll(";" + ls_Str[i] + ";", ";");
				ls_Valids = ls_Valids.replaceAll(";" + ls_Str[i] + ";", ";");
				ls_Needs = ls_Needs.replaceAll(";" + ls_Str[i] + ";", ";");
			}
		}

		ms_CellDisVisibleIDs = ls_DisVis.substring(1, ls_DisVis.length());
		ms_CellValidHandleIDs = ls_Valids.substring(1, ls_Valids.length());
		ms_CellNeedHandleIDs = ls_Needs.substring(1, ls_Needs.length());
	}

	/**
	 * 表单图元可读写权限，是表单图元标识的连接串，使用“;”分隔
	 */
	private String ms_CellValidHandleIDs = "";

	/**
	 * 表单图元可读写权限，是表单图元标识的连接串，使用“;”分隔
	 * 
	 * @return
	 */
	public String getCellValidHandleIDs() {
		if (ms_CellValidHandleIDs.equals(""))
			return "";
		if (this.Cyclostyle.Forms.size() == 0) {
			ms_CellValidHandleIDs = "";
			return "";
		}

		String ls_Str = "";

		for (CForm lo_Form : this.Cyclostyle.Forms) {
			for (CFormCell lo_Cell : lo_Form.FormCells) {
				if ((";" + ms_CellValidHandleIDs).indexOf(";"
						+ String.valueOf(lo_Cell.CellID) + ";") > -1) {
					ls_Str += String.valueOf(lo_Cell.CellID) + ";";
				}
			}
		}

		ms_CellValidHandleIDs = ls_Str;
		return ls_Str;
	}

	/**
	 * 表单图元可读写权限，是表单图元标识的连接串，使用“;”分隔
	 * 
	 * @param value
	 */
	public void setCellValidHandleIDs(String value) {
		if (value == null || value.equals(""))
			return;

		String ls_DisVis = ";" + ms_CellDisVisibleIDs;
		String ls_Valids = "";
		String ls_Needs = ";" + ms_CellNeedHandleIDs;

		String[] ls_Str = value.split(";");
		for (int i = 0; i < ls_Str.length; i++) {
			if (ls_Str[i] != "") {
				ls_DisVis = ls_DisVis.replaceAll(";" + ls_Str[i] + ";", ";");
				ls_Valids += ls_Str[i] + ";";
				ls_Needs = ls_Needs.replaceAll(";" + ls_Str[i] + ";", ";");
			}
		}

		ms_CellDisVisibleIDs = MGlobal.right(ls_DisVis, ls_DisVis.length() - 1);
		ms_CellValidHandleIDs = ls_Valids;
		ms_CellNeedHandleIDs = MGlobal.right(ls_Needs, ls_Needs.length() - 1);
	}

	/**
	 * 表单图元必读写权限，是表单图元标识的连接串，使用“;”分隔
	 */
	private String ms_CellNeedHandleIDs = "";

	/**
	 * 表单图元必读写权限，是表单图元标识的连接串，使用“;”分隔
	 * 
	 * @return
	 */
	public String getCellNeedHandleIDs() {
		if (ms_CellNeedHandleIDs == null || ms_CellNeedHandleIDs.equals(""))
			return "";
		if (this.Cyclostyle.Forms.size() == 0) {
			ms_CellNeedHandleIDs = "";
			return "";
		}
		String ls_Str = "";

		for (CForm lo_Form : this.Cyclostyle.Forms) {
			for (CFormCell lo_Cell : lo_Form.FormCells) {
				if ((";" + ms_CellNeedHandleIDs).indexOf(";"
						+ String.valueOf(lo_Cell.CellID) + ";") > -1) {
					ls_Str += String.valueOf(lo_Cell.CellID) + ";";
				}
			}
		}

		ms_CellNeedHandleIDs = ls_Str;
		return ls_Str;
	}

	/**
	 * 表单图元必读写权限，是表单图元标识的连接串，使用“;”分隔
	 * 
	 * @param value
	 */
	public void setCellNeedHandleIDs(String value) {
		if (value == null || value.equals(""))
			return;

		String ls_DisVis = ";" + ms_CellDisVisibleIDs;
		String ls_Valids = ";" + ms_CellValidHandleIDs;
		String ls_Needs = "";

		String[] ls_Str = value.split(";");
		for (int i = 0; i < ls_Str.length; i++) {
			if (ls_Str[i] != "") {
				ls_DisVis = ls_DisVis.replaceAll(";" + ls_Str[i] + ";", ";");
				ls_Valids = ls_Valids.replaceAll(";" + ls_Str[i] + ";", ";");
				ls_Needs += ls_Str[i] + ";";
			}
		}

		ms_CellDisVisibleIDs = MGlobal.right(ls_DisVis, ls_DisVis.length() - 1);
		ms_CellValidHandleIDs = MGlobal
				.right(ls_Valids, ls_Valids.length() - 1);
		ms_CellNeedHandleIDs = ls_Needs;
	}

	/**
	 * 意见填写权限
	 */
	public int CommentAccess = ECommentAccessType.ValidWriteResponse;

	/**
	 * 以下定义二次开发函数内容 计算函数内容集合
	 */
	private String[] ms_ScriptCacuContents = { "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "" };

	/**
	 * 以下内容定义意见签名权限设置 批示类型
	 */
	public int ResponseType = EResponseType.CommentResponseType
			+ EResponseType.SignResponseType + EResponseType.VoiceResponseType;

	/**
	 * 使用权限
	 */
	public int ResponseRight = EResponseRight.PublicResponseRight;

	/**
	 * 访问步骤
	 */
	public String ResActivityIDs = "";

	/**
	 * 访问角色
	 */
	public String ResRoleIDs = "";

	/**
	 * 外部访问用户
	 */
	public String ResExterUser = "";

	/**
	 * 函数判断
	 */
	public String ResCacuScript = "";

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属的公文模板对象
		this.Cyclostyle = null;

		// 计算函数内容集合
		ms_ScriptCacuContents = null;

		super.ClearUp();
	}

	/**
	 * 设置计算函数内容
	 * 
	 * @param ai_CaculateType
	 * @param as_ScriptContent
	 */
	public void setScriptCacuContent(int ai_CaculateType,
			String as_ScriptContent) {
		if (ai_CaculateType < EScriptCaculateType.CacuOpenForAllReadOnly
				|| ai_CaculateType > EScriptCaculateType.CacuOpenClientAnyStatus) {
			return;
		}

		ms_ScriptCacuContents[ai_CaculateType] = as_ScriptContent;
	}

	/**
	 * 当前对象是否可用
	 */
	public String IsValid(int ai_SpaceLength) {
		String ls_Msg = "";
		if (this.BodyAccess == EBodyAccessType.BodyDisVisible
				&& this.DocumentAccess == EDocumentAccessType.DocumentDisVisible
				&& this.getVisbleFormIDs().equals("")) {
			ls_Msg += MGlobal.addSpace(ai_SpaceLength) + "权限【" + this.RightName
					+ "】有错误：权限设计中正文、附件、表单至少有一页需要显示；\n";
		}
		return ls_Msg;
	}

	/**
	 * 读取计算函数内容
	 * 
	 * @param ai_CaculateType
	 *            计算类型0-16
	 * @return
	 * @throws Exception
	 */
	public String getScriptCacuContent(int ai_CaculateType) throws Exception {
		try {
			if (ai_CaculateType < EScriptCaculateType.CacuOpenForAllReadOnly
					|| ai_CaculateType > EScriptCaculateType.CacuOpenClientAnyStatus) {
				return "";
			}
			return ms_ScriptCacuContents[ai_CaculateType];
		} catch (Exception e) {
			this.Raise(e, "getScriptCacuContent", null);
		}
		return "";
	}

	/**
	 * 从Xml节点导入到对象
	 * 
	 * @param ao_Node
	 *            导入节点
	 * @param ai_Type
	 *            导入类型：=0-正常导入；=1-短属性导入；
	 */
	@Override
	protected void Import(Element ao_Node, int ai_Type) {
		if (ai_Type == 0 && ao_Node.hasAttribute("RightID")) {
			this.RightID = Integer.parseInt(ao_Node.getAttribute("RightID"));
			this.RightName = ao_Node.getAttribute("RightName");
			this.RightType = Integer
					.parseInt(ao_Node.getAttribute("RightType"));
		}
		this.RoleOpenAccesses = ao_Node.getAttribute("RoleOpenAccesses");
		this.RoleSendAccesses = ao_Node.getAttribute("RoleSendAccesses");
		this.RoleSaveAccesses = ao_Node.getAttribute("RoleSaveAccesses");
		this.RoleSendAfterAccesses = ao_Node
				.getAttribute("RoleSendAfterAccesses");
		this.PropOpenAccesses = ao_Node.getAttribute("PropOpenAccesses");
		this.PropSendAccesses = ao_Node.getAttribute("PropSendAccesses");
		this.PropSaveAccesses = ao_Node.getAttribute("PropSaveAccesses");
		this.PropSendAfterAccesses = ao_Node
				.getAttribute("PropSendAfterAccesses");
		this.FlowAccess = Integer.parseInt(ao_Node.getAttribute("FlowAccess"));
		this.BodyAccess = Integer.parseInt(ao_Node.getAttribute("BodyAccess"));
		this.DocumentAccess = Integer.parseInt(ao_Node
				.getAttribute("DocumentAccess"));
		ms_DisVisibleFormIDs = ao_Node.getAttribute("DisVisibleFormIDs");
		ms_CellDisVisibleIDs = ao_Node.getAttribute("CellDisVisibleIDs");
		ms_CellValidHandleIDs = ao_Node.getAttribute("CellValidHandleIDs");
		ms_CellNeedHandleIDs = ao_Node.getAttribute("CellNeedHandleIDs");
		this.CommentAccess = Integer.parseInt(ao_Node
				.getAttribute("CommentAccess"));
		this.ResponseType = Integer.parseInt(ao_Node
				.getAttribute("ResponseType"));
		this.ResponseRight = Integer.parseInt(ao_Node
				.getAttribute("ResponseRight"));
		this.ResActivityIDs = ao_Node.getAttribute("ResActivityIDs");
		this.ResRoleIDs = ao_Node.getAttribute("ResRoleIDs");
		this.ResExterUser = ao_Node.getAttribute("ResExterUser");
		this.ResCacuScript = ao_Node.getAttribute("ResCacuScript");

		importScriptContent(ao_Node, ai_Type);
	}

	/**
	 * 从XML导入二次函数
	 */
	private boolean importScriptContent(Element ao_Node, int ai_Type) {
		NodeList lo_List = ao_Node.getElementsByTagName("Script");
		for (int i = 0; i < lo_List.getLength(); i++) {
			Element lo_Child = (Element) lo_List.item(i);
			int li_Type = Integer.parseInt(lo_Child.getAttribute("Type"));
			setScriptCacuContent(li_Type, lo_Child.getAttribute("Value"));
		}
		return true;
	}

	/**
	 * 从对象导出到Xml节点
	 * 
	 * @param ao_Node
	 *            导出节点
	 * @param ai_Type
	 *            导出类型：=0-正常导出；=1-短属性导出；
	 * @throws Exception
	 */
	@Override
	protected void Export(Element ao_Node, int ai_Type) throws Exception {
		if (ai_Type == 0) {
			ao_Node.setAttribute("RightID", String.valueOf(this.RightID));
			ao_Node.setAttribute("RightName", this.RightName);
			ao_Node.setAttribute("RightType", String.valueOf(this.RightType));
		}
		ao_Node.setAttribute("RoleOpenAccesses", this.RoleOpenAccesses);
		ao_Node.setAttribute("RoleSendAccesses", this.RoleSendAccesses);
		ao_Node.setAttribute("RoleSaveAccesses", this.RoleSaveAccesses);
		ao_Node.setAttribute("RoleSendAfterAccesses",
				this.RoleSendAfterAccesses);
		ao_Node.setAttribute("PropOpenAccesses", this.PropOpenAccesses);
		ao_Node.setAttribute("PropSendAccesses", this.PropSendAccesses);
		ao_Node.setAttribute("PropSaveAccesses", this.PropSaveAccesses);
		ao_Node.setAttribute("PropSendAfterAccesses",
				this.PropSendAfterAccesses);
		ao_Node.setAttribute("FlowAccess", Integer.toString(this.FlowAccess));
		ao_Node.setAttribute("BodyAccess", Integer.toString(this.BodyAccess));
		ao_Node.setAttribute("DocumentAccess",
				Integer.toString(this.DocumentAccess));
		ao_Node.setAttribute("DisVisibleFormIDs", ms_DisVisibleFormIDs);
		ao_Node.setAttribute("CellDisVisibleIDs", ms_CellDisVisibleIDs);
		ao_Node.setAttribute("CellValidHandleIDs", ms_CellValidHandleIDs);
		ao_Node.setAttribute("CellNeedHandleIDs", ms_CellNeedHandleIDs);
		ao_Node.setAttribute("CommentAccess",
				Integer.toString(this.CommentAccess));

		for (int li_Type = EScriptCaculateType.CacuOpenForAllReadOnly; li_Type <= EScriptCaculateType.CacuOpenClientAnyStatus; li_Type++) {
			String ls_Value = getScriptCacuContent(li_Type);
			if (!ls_Value.equals("")) {
				Element lo_Node = ao_Node.getOwnerDocument().createElement(
						"Script");
				ao_Node.appendChild(lo_Node);
				lo_Node.setAttribute("Type", Integer.toString(li_Type));
				lo_Node.setAttribute("Value", ls_Value);
			}
		}
		ao_Node.setAttribute("ResponseType",
				Integer.toString(this.ResponseType));
		ao_Node.setAttribute("ResponseRight",
				Integer.toString(this.ResponseRight));
		ao_Node.setAttribute("ResActivityIDs", this.ResActivityIDs);
		ao_Node.setAttribute("ResRoleIDs", this.ResRoleIDs);
		ao_Node.setAttribute("ResExterUser", this.ResExterUser);
		ao_Node.setAttribute("ResCacuScript", this.ResCacuScript);

		exportScriptContent(ao_Node, ai_Type);
	}

	/**
	 * 导出计算函数内容
	 * 
	 * @throws Exception
	 */
	private boolean exportScriptContent(Element ao_Node, int ai_Type)
			throws Exception {
		for (int i = EScriptCaculateType.CacuOpenForAllReadOnly; i <= EScriptCaculateType.CacuOpenClientAnyStatus; i++) {
			String ls_Value = getScriptCacuContent(i);
			if (ls_Value != "") {
				Element lo_Node = ao_Node.getOwnerDocument().createElement(
						"Script");
				ao_Node.appendChild(lo_Node);
				lo_Node.setAttribute("Type", String.valueOf(i));
				lo_Node.setAttribute("Value", ls_Value);
			}
		}
		return true;
	}

	/**
	 * 对象保存到数据库行对象中
	 * 
	 * @param ao_Set
	 *            保存的结果集
	 * @param ai_Type
	 *            保存方式：=0-正常保存；=1-短属性保存；
	 * @throws Exception
	 */
	@Override
	public void Save(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		// Add Save Code...
		ao_Set.put("TemplateID", this.Cyclostyle.CyclostyleID);
		ao_Set.put("RightID", this.RightID);
		ao_Set.put("RightName", this.RightName);
		ao_Set.put("RightType", this.RightType);

		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				ao_Set.put("RightContent", "CR" + this.WriteContent(ai_Type));
			} else {
				ao_Set.put("RightContent", this.WriteContent(ai_Type));
			}
		} else {
			ao_Set.put("RightContent", this.ExportToXml("Right", ai_Type));
		}
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @param ab_Inst
	 *            是否实例
	 * @return
	 * @throws SQLException
	 */
	public static String getSaveState(int ai_SaveType, int ai_Type,
			boolean ab_Inst) {
		String ls_Sql = null;
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO RightInst "
						+ "(WorkItemID, RightID, RightName, RightType, RightContent) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE RightInst SET "
						+ "RightName = ?, RightType = ?, RightContent = ? "
						+ "WHERE WorkItemID = ? AND RightID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO RightList "
						+ "(TemplateID, RightID, RightName, RightType, RightContent) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE RightList SET "
						+ "RightName = ?, RightType = ?, RightContent = ? "
						+ "WHERE TemplateID = ? AND RightID = ?";
			}
		}
		return ls_Sql;
	}

	/**
	 * 保存
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @param ab_Inst
	 *            是否实例
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws Exception
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type,
			boolean ab_Inst, int ai_Update) throws Exception {
		List parasList = new ArrayList();
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.RightID);
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.RightID);
			}
		}

		parasList.add(this.RightName);
		parasList.add(this.RightType);

		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				parasList.add("CR" + this.WriteContent(ai_Type));
			} else {
				parasList.add(this.WriteContent(ai_Type));
			}
		} else {
			parasList.add(this.ExportToXml("Right", ai_Type));
		}

		if (ab_Inst) {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.RightID);
			}
		} else {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.RightID);
			}
		}

		return CSqlHandle.getJdbcTemplate().update(ao_State,
				parasList.toArray());
	}

	/**
	 * 从数据库行对象中读取数据到对象
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开方式：=0-正常打开；=1-短属性打开；
	 * @throws Exception
	 */
	@Override
	public void Open(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		// this.Cyclostyle.setCyclostyleID((Integer)ao_Set.getObject("TemplateID"));
		this.RightID = (Integer) ao_Set.get("RightID");
		this.RightName = (String) ao_Set.get("RightName");
		this.RightType = (Short) ao_Set.get("RightType");
		String ls_Text = (String) ao_Set.get("RightContent");

		if (this.Cyclostyle.OpenType == EDataHandleType.OrignType) {
			if (ls_Text.substring(0, 2).equals("CR")) {
				this.ReadContent(ls_Text.substring(0, ls_Text.length()),
						ai_Type);

			} else {
				this.ReadContent(ls_Text, ai_Type);
			}
		} else {
			this.ImportFormXml(ls_Text, ai_Type);
		}
	}

	/**
	 * 对象打包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 * @throws Exception
	 */
	@Override
	protected void Write(MBag ao_Bag, int ai_Type) throws Exception {
		ao_Bag.Write("ms_RoleOpenAccesses", this.RoleOpenAccesses);
		ao_Bag.Write("ms_RoleSendAccesses", this.RoleSendAccesses);
		ao_Bag.Write("ms_RoleSaveAccesses", this.RoleSaveAccesses);
		ao_Bag.Write("ms_RoleSendAfterAccesses", this.RoleSendAfterAccesses);
		ao_Bag.Write("ms_PropOpenAccesses", this.PropOpenAccesses);
		ao_Bag.Write("ms_PropSendAccesses", this.PropSendAccesses);
		ao_Bag.Write("ms_PropSaveAccesses", this.PropSaveAccesses);
		ao_Bag.Write("ms_PropSendAfterAccesses", this.PropSendAfterAccesses);
		ao_Bag.Write("mi_FlowAccess", this.FlowAccess);
		ao_Bag.Write("mi_BodyAccess", this.BodyAccess);
		ao_Bag.Write("mi_DocumentAccess", this.DocumentAccess);
		ao_Bag.Write("ms_DisVisibleFormIDs", ms_DisVisibleFormIDs);
		ao_Bag.Write("ms_CellDisVisibleIDs", ms_CellDisVisibleIDs);
		ao_Bag.Write("ms_CellValidHandleIDs", ms_CellValidHandleIDs);
		ao_Bag.Write("ms_CellNeedHandleIDs", ms_CellNeedHandleIDs);
		ao_Bag.Write("mi_CommentAccess", this.CommentAccess);
		writeScriptContent(ao_Bag, ai_Type);
		ao_Bag.Write("mi_ResponseType", this.ResponseType);
		ao_Bag.Write("mi_ResponseRight", this.ResponseRight);
		ao_Bag.Write("ms_ResActivityIDs", this.ResActivityIDs);
		ao_Bag.Write("ms_ResRoleIDs", this.ResRoleIDs);
		ao_Bag.Write("ms_ResExterUser", this.ResExterUser);
		ao_Bag.Write("ms_ResCacuScript", this.ResCacuScript);
	}

	/**
	 * 保存计算函数内容
	 * 
	 * @throws Exception
	 */
	private boolean writeScriptContent(MBag ao_Bag, int ai_Type)
			throws Exception {
		ao_Bag.Write("ScriptContentCount", getScriptCount());
		for (int i = EScriptCaculateType.CacuOpenForAllReadOnly; i <= EScriptCaculateType.CacuOpenClientAnyStatus; i++) {
			String ls_Value = getScriptCacuContent(i);
			if (ls_Value != "") {
				ao_Bag.Write("ScriptContentType", i);
				ao_Bag.Write("ScriptContentValue", ls_Value);
			}
		}
		return true;
	}

	/**
	 * 获取二次函数内容数量
	 * 
	 * @return
	 */
	private int getScriptCount() {
		int li_Count = 0;
		for (int i = EScriptCaculateType.CacuOpenForAllReadOnly; i <= EScriptCaculateType.CacuOpenClientAnyStatus; i++) {
			if (!ms_ScriptCacuContents[i].equals(""))
				li_Count++;
		}
		return li_Count;
	}

	/**
	 * 对象解包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导入；=1-短属性导入；
	 * @throws Exception
	 */
	@Override
	protected void Read(MBag ao_Bag, int ai_Type) throws Exception {
		this.RoleOpenAccesses = ao_Bag.ReadString("ms_RoleOpenAccesses");
		this.RoleSendAccesses = ao_Bag.ReadString("ms_RoleSendAccesses");
		this.RoleSaveAccesses = ao_Bag.ReadString("ms_RoleSaveAccesses");
		this.RoleSendAfterAccesses = ao_Bag
				.ReadString("ms_RoleSendAfterAccesses");
		this.PropOpenAccesses = ao_Bag.ReadString("ms_PropOpenAccesses");
		this.PropSendAccesses = ao_Bag.ReadString("ms_PropSendAccesses");
		this.PropSaveAccesses = ao_Bag.ReadString("ms_PropSaveAccesses");
		this.PropSendAfterAccesses = ao_Bag
				.ReadString("ms_PropSendAfterAccesses");
		this.FlowAccess = ao_Bag.ReadInt("mi_FlowAccess");
		this.BodyAccess = ao_Bag.ReadInt("mi_BodyAccess");
		this.DocumentAccess = ao_Bag.ReadInt("mi_DocumentAccess");
		ms_DisVisibleFormIDs = ao_Bag.ReadString("ms_DisVisibleFormIDs");
		ms_CellDisVisibleIDs = ao_Bag.ReadString("ms_CellDisVisibleIDs");
		ms_CellValidHandleIDs = ao_Bag.ReadString("ms_CellValidHandleIDs");
		ms_CellNeedHandleIDs = ao_Bag.ReadString("ms_CellNeedHandleIDs");
		this.CommentAccess = ao_Bag.ReadInt("mi_CommentAccess");
		readScriptContent(ao_Bag, ai_Type);
		this.ResponseType = ao_Bag.ReadInt("mi_ResponseType");
		this.ResponseRight = ao_Bag.ReadInt("mi_ResponseRight");
		this.ResActivityIDs = ao_Bag.ReadString("ms_ResActivityIDs");
		this.ResRoleIDs = ao_Bag.ReadString("ms_ResRoleIDs");
		this.ResExterUser = ao_Bag.ReadString("ms_ResExterUser");
		this.ResCacuScript = ao_Bag.ReadString("ms_ResCacuScript");
	}

	/**
	 * 打开计算函数内容
	 * 
	 * @throws Exception
	 */
	private boolean readScriptContent(MBag ao_Bag, int ai_Type)
			throws Exception {
		try {
			String ls_Value = "";
			int li_Count = ao_Bag.ReadInt("ScriptContentCount");
			for (int i = 0; i < li_Count; i++) {
				int li_Type = ao_Bag.ReadInt("ScriptContentType");
				ls_Value = ao_Bag.ReadString("ScriptContentValue");
				setScriptCacuContent(li_Type, ls_Value);
			}
			return true;
		} catch (Exception e) {
			this.Raise(e, "OpenScriptContent", null);
		}
		return false;
	}

}
