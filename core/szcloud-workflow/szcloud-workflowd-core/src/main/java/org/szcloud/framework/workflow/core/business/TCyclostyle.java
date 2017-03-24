package org.szcloud.framework.workflow.core.business;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CGlobalPara;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.ECommentAccessType;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EDocumentType;
import org.szcloud.framework.workflow.core.emun.EFlowType;
import org.szcloud.framework.workflow.core.emun.ERightType;
import org.szcloud.framework.workflow.core.emun.EWorkItemFlowFlag;
import org.szcloud.framework.workflow.core.entity.CActivity;
import org.szcloud.framework.workflow.core.entity.CCyclostyle;
import org.szcloud.framework.workflow.core.entity.CDocument;
import org.szcloud.framework.workflow.core.entity.CDocumentFileList;
import org.szcloud.framework.workflow.core.entity.CFlow;
import org.szcloud.framework.workflow.core.entity.CForm;
import org.szcloud.framework.workflow.core.entity.CProp;
import org.szcloud.framework.workflow.core.entity.CPublicTemplate;
import org.szcloud.framework.workflow.core.entity.CRight;
import org.szcloud.framework.workflow.core.entity.CRole;
import org.szcloud.framework.workflow.core.entity.CScript;
import org.szcloud.framework.workflow.core.entity.CTable;
import org.szcloud.framework.workflow.core.module.MFile;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class TCyclostyle {

	/**
	 * 新建公文模板
	 * 
	 * @param ao_Cyclostyle
	 *            公文模板对象
	 * @param al_FormerID
	 *            公文样板标识，=0-表示直接新建
	 * @return 返回是否新建成功
	 * @throws Exception
	 */
	public static boolean newCyclostyle(CCyclostyle ao_Cyclostyle,
			int al_FormerID) throws Exception {
		try {
			boolean lb_Return = false;
			if (al_FormerID > 0) {
				List<Map<String,Object>> lo_Set = CSqlHandle
						.getSetBySql("SELECT * FROM TemplateFormer WHERE FormerID = "
								+ Integer.toString(al_FormerID));
				if(lo_Set.size()>0)
				{
					for(Map<String,Object> row : lo_Set) {
						lb_Return = importFromFile(1, ao_Cyclostyle, "", MGlobal.readObject(row,"FormerContent").toString());
					}
				} else {
					// 错误[?]：不存在公文样板
					ao_Cyclostyle.Raise(1000, "newCyclostyle",
							String.valueOf(al_FormerID));
				}

				lo_Set = null;
				return lb_Return;
			}

			ao_Cyclostyle.AuthorID = ao_Cyclostyle.Logon.GlobalPara.UserID;
			ao_Cyclostyle.Author = ao_Cyclostyle.Logon.GlobalPara.UserName;
			ao_Cyclostyle.CreateDate = MGlobal.getNow();
			ao_Cyclostyle.BelongID = ao_Cyclostyle.Logon.GlobalPara.BelongID;

			// 新建模板时需要插入系统角色：拟稿人
			insertRole(ao_Cyclostyle, "拟稿人");
			// 新建模板时需要插入权限：缺省权限和开始步骤权限
			CRight lo_Right = insertRight(ao_Cyclostyle, "缺省权限",
					ERightType.DefaultRightType);
			lo_Right.CommentAccess = ECommentAccessType.NotWriteResponse;
			lo_Right = insertRight(ao_Cyclostyle, "开始权限",
					ERightType.StartRightType);
			lo_Right.CommentAccess = ECommentAccessType.NotWriteResponse;
			// 插入缺省的流程
			CFlow lo_Flow = insertFlow(ao_Cyclostyle, "未命名流程");
			lo_Flow.DefaultFlow = true;
			lo_Flow.FlowType = EFlowType.ComplexFlowType;
			// 插入缺省的正文
			ao_Cyclostyle.Body = new CDocument(ao_Cyclostyle.Logon);
			ao_Cyclostyle.Body.Cyclostyle = ao_Cyclostyle;
			ao_Cyclostyle.Body.initDocument();
			ao_Cyclostyle.DocumentMaxID++;
			ao_Cyclostyle.Body.DocumentID = ao_Cyclostyle.DocumentMaxID;
			ao_Cyclostyle.Body.DocumentName = "公文正文";
			ao_Cyclostyle.Body.DocumentType = EDocumentType.TextTypeBody;
			ao_Cyclostyle.Body.DocumentExt = "txt";
			ao_Cyclostyle.setHaveBody(true);

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			ao_Cyclostyle.Raise(ex, "newCyclostyle",
					Integer.toString(al_FormerID));
			return false;
		}
	}

	/**
	 * 插入流程
	 * 
	 * @param ao_Cyclostyle
	 * @param as_FlowName
	 * @return
	 * @throws Exception
	 */
	public static CFlow insertFlow(CCyclostyle ao_Cyclostyle, String as_FlowName)
			throws Exception {
		CFlow lo_Flow = new CFlow(ao_Cyclostyle.Logon);
		lo_Flow.Cyclostyle = ao_Cyclostyle;
		if (!lo_Flow.newFlow(as_FlowName)) {
			lo_Flow.ClearUp();
			lo_Flow = null;
			return null;
		}
		ao_Cyclostyle.FlowIndexMaxID++;
		;
		lo_Flow.FlowIndex = ao_Cyclostyle.FlowIndexMaxID;
		ao_Cyclostyle.Flows.add(lo_Flow);

		return lo_Flow;
	}

	/**
	 * 插入权限
	 * 
	 * @param ao_Cyclostyle
	 * @param as_RightName
	 * @param ai_RightType
	 * @return
	 * @throws Exception
	 */
	public static CRight insertRight(CCyclostyle ao_Cyclostyle,
			String as_RightName, int ai_RightType) throws Exception {
		for (CRight lo_Right : ao_Cyclostyle.Rights) {
			if (lo_Right.RightName.equals(as_RightName)) {
				// 错误[1008]：权限名称不能相同
				ao_Cyclostyle.Raise(1008, "insertRight", as_RightName);
				return null;
			}
		}

		if (ao_Cyclostyle.Rights.size() >= 2) {
			if (ai_RightType != ERightType.OtherRightType) {
				// 错误[1009]：不能插入缺省或开始步骤权限
				ao_Cyclostyle.Raise(1009, "insertRight", as_RightName);
				return null;
			}
		}

		CRight lo_Right = new CRight(ao_Cyclostyle.Logon);
		lo_Right.Cyclostyle = ao_Cyclostyle;
		ao_Cyclostyle.RightMaxID++;
		lo_Right.RightID = ao_Cyclostyle.RightMaxID;
		lo_Right.setRightName(as_RightName);
		lo_Right.RightType = ai_RightType;
		ao_Cyclostyle.Rights.add(lo_Right);

		return lo_Right;
	}

	/**
	 * 插入角色
	 * 
	 * @param ao_Cyclostyle
	 * @param as_RoleName
	 * @return
	 * @throws Exception
	 */
	public static CRole insertRole(CCyclostyle ao_Cyclostyle, String as_RoleName)
			throws Exception {
		for (CRole lo_Role : ao_Cyclostyle.Roles) {
			if (lo_Role.RoleName.equals(as_RoleName)) {
				// 错误[1006]：角色名称不能相同
				ao_Cyclostyle.Raise(1006, "insertRole", as_RoleName);
				return null;
			}
		}

		CRole lo_Role = new CRole(ao_Cyclostyle.Logon);
		lo_Role.Cyclostyle = ao_Cyclostyle;
		ao_Cyclostyle.RoleMaxID++;
		lo_Role.RoleID = ao_Cyclostyle.RoleMaxID;
		lo_Role.setRoleName(as_RoleName);
		ao_Cyclostyle.Roles.add(lo_Role);

		return lo_Role;
	}

	/**
	 * 打开公文模板
	 * 
	 * @param ao_Cyclostyle
	 *            公文模板对象
	 * @param al_CyclostyleID
	 *            公文模板标识
	 * @param al_FlowID
	 *            流程标识：=-2-表示不打开流程；=-1-表示打开所有流程；=0-表示打开缺省流程[如果没有缺省流程，则使用第一个流程，
	 *            如果存在多个缺省流程，则打开所有缺省流程]；>0 表示打开指定流程标识的流程；
	 * @param ab_OpenData
	 *            是否打开数据
	 * @return
	 * @throws SQLException
	 */
	public static boolean openCyclostyle(CCyclostyle ao_Cyclostyle,
			int al_CyclostyleID, int al_FlowID, boolean ab_OpenData)
			throws Exception {
		// 打开基本信息
		String ls_Sql = "SELECT * FROM TemplateDefine WHERE TemplateID = "
				+ Integer.toString(al_CyclostyleID);
		List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		if (!(lo_Set.size()>0)) {
			lo_Set = null;
			// 错误：不存在所要打开的公文模板
			ao_Cyclostyle.Raise(1005, "openCyclostyle", "");
			return false;
		}

		ao_Cyclostyle.Open(lo_Set.get(0), 0, false);
		lo_Set = null;

		int li_Type = 1;// 打开方式：=0-完整；=1-简洁；

		// 判断是否需要打开流程
		Boolean lb_OpenFlow = true;
		if (ao_Cyclostyle.WorkItem != null) {
			if (!((ao_Cyclostyle.FlowFlag & EWorkItemFlowFlag.OrderFlowRule) == EWorkItemFlowFlag.OrderFlowRule || (ao_Cyclostyle.FlowFlag & EWorkItemFlowFlag.ConditionFlowRule) == EWorkItemFlowFlag.ConditionFlowRule)) {
				lb_OpenFlow = false;
			}
		}

		if (lb_OpenFlow) {
			// 打开流程
			if (al_FlowID == -2) {
				ls_Sql = "";
			} else if (al_FlowID == -1) {
				ls_Sql = "SELECT * FROM FlowDefine WHERE TemplateID = "
						+ Integer.toString(al_CyclostyleID);
			} else if (al_FlowID == 0) {
				ls_Sql = "SELECT * FROM FlowDefine WHERE DefaultFlow = 1 AND TemplateID = "
						+ Integer.toString(al_CyclostyleID);
			} else {
				ls_Sql = "SELECT * FROM FlowDefine WHERE FlowID = "
						+ Integer.toString(al_FlowID) + " AND TemplateID = "
						+ Integer.toString(al_CyclostyleID);
			}

			if (!ls_Sql.equals("")) {
				lo_Set = CSqlHandle.getSetBySql(ls_Sql);
				Boolean lb_Boolean = lo_Set.size()>0;
				if (!lb_Boolean) {
					if (al_FlowID == 0) {
						ls_Sql = "SELECT * FROM FlowDefine WHERE TemplateID = "
								+ Integer.toString(al_CyclostyleID);
						lo_Set = CSqlHandle.getSetBySql(ls_Sql);
						//lb_Boolean = lo_Set.next();
					}
				}

				for(Map<String,Object> row : lo_Set) {
					CFlow lo_Flow = new CFlow(ao_Cyclostyle.Logon);
					lo_Flow.Cyclostyle = ao_Cyclostyle;
					if (!lo_Flow.Open(row, li_Type, true)) {
						lo_Set = null;
						return false;
					}
					ao_Cyclostyle.FlowIndexMaxID++;
					lo_Flow.FlowIndex = ao_Cyclostyle.FlowIndexMaxID;
					ao_Cyclostyle.Flows.add(lo_Flow);
				}
				lo_Set = null;
			}
		}

		// 打开公文模板中的数据
		if (ab_OpenData) {
			// 打开角色
			ls_Sql = "SELECT * FROM RoleList WHERE TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CRole lo_Role = new CRole(ao_Cyclostyle.Logon);
				lo_Role.Cyclostyle = ao_Cyclostyle;
				lo_Role.Open(row, li_Type);
				ao_Cyclostyle.Roles.add(lo_Role);
				if (ao_Cyclostyle.RoleMaxID < lo_Role.RoleID) {
					ao_Cyclostyle.RoleMaxID = lo_Role.RoleID;
				}
			}

			// 打开权限
			ls_Sql = "SELECT * FROM RightList WHERE TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CRight lo_Right = new CRight(ao_Cyclostyle.Logon);
				lo_Right.Cyclostyle = ao_Cyclostyle;
				lo_Right.Open(row, li_Type);
				ao_Cyclostyle.Rights.add(lo_Right);
				if (ao_Cyclostyle.RightMaxID < lo_Right.RightID) {
					ao_Cyclostyle.RightMaxID = lo_Right.RightID;
				}
			}

			// 打开表头属性
			ls_Sql = "SELECT * FROM PropList WHERE TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CProp lo_Prop = new CProp(ao_Cyclostyle.Logon);
				lo_Prop.Cyclostyle = ao_Cyclostyle;
				lo_Prop.Open(row, li_Type);
				ao_Cyclostyle.Props.add(lo_Prop);
				if (ao_Cyclostyle.PropMaxID < lo_Prop.PropID) {
					ao_Cyclostyle.PropMaxID = lo_Prop.PropID;
				}
			}

			// 打开正文描述
			ls_Sql = "SELECT * FROM DocumentList WHERE DocumentType IN (-2, -1, 0, 1, 2, 3) AND TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				ao_Cyclostyle.Body = new CDocument(ao_Cyclostyle.Logon);
				ao_Cyclostyle.Body.Cyclostyle = ao_Cyclostyle;
				ao_Cyclostyle.Body.Open(row, li_Type, true);
				if (ao_Cyclostyle.DocumentMaxID < ao_Cyclostyle.Body.DocumentID)
					ao_Cyclostyle.DocumentMaxID = ao_Cyclostyle.Body.DocumentID;

				// 打开正文模板描述内容
				ls_Sql = "SELECT FileID, FileName, FilePath, IsDefault, Describe FROM DocumentFileList WHERE TemplateID = "
						+ Integer.toString(al_CyclostyleID)
						+ " AND DocumentID = "
						+ Integer.toString(ao_Cyclostyle.Body.DocumentID);
				ao_Cyclostyle.Body.DocumentFileListXml = CSqlHandle
						.getXmlBySql(ls_Sql); // ls_Value
			}

			// 打开附件描述
			ls_Sql = "SELECT * FROM DocumentList WHERE DocumentType IN (-2, 4, 5, 6, 7, 8, 9, 10) AND TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CDocument lo_Document = new CDocument(ao_Cyclostyle.Logon);
				lo_Document.Cyclostyle = ao_Cyclostyle;
				lo_Document.initDocument();
				ao_Cyclostyle.Body.Open(row, li_Type, true);
				ao_Cyclostyle.Documents.add(lo_Document);
				if (ao_Cyclostyle.DocumentMaxID < ao_Cyclostyle.Body.DocumentID)
					ao_Cyclostyle.DocumentMaxID = ao_Cyclostyle.Body.DocumentID;
			}

			// 打开表单定义
			ls_Sql = "SELECT * FROM FormList WHERE TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CForm lo_Form = new CForm(ao_Cyclostyle.Logon);
				lo_Form.Cyclostyle = ao_Cyclostyle;
				lo_Form.Open(row, li_Type);
				ao_Cyclostyle.Forms.add(lo_Form);
				if (ao_Cyclostyle.FormMaxID < lo_Form.FormID)
					ao_Cyclostyle.FormMaxID = lo_Form.FormID;
			}

			// 打开二次函数定义
			ls_Sql = "SELECT * FROM ScriptList WHERE TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CScript lo_Script = new CScript(ao_Cyclostyle.Logon);
				lo_Script.Cyclostyle = ao_Cyclostyle;
				lo_Script.Open(row, li_Type);
				ao_Cyclostyle.Scripts.add(lo_Script);
				if (ao_Cyclostyle.ScriptMaxID < lo_Script.ScriptID) {
					ao_Cyclostyle.ScriptMaxID = lo_Script.ScriptID;
				}
			}

			// 打开数据存储表格
			ls_Sql = "SELECT * FROM TableList WHERE TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CTable lo_Table = new CTable(ao_Cyclostyle.Logon);
				lo_Table.Cyclostyle = ao_Cyclostyle;
				lo_Table.Open(row, li_Type);
				ao_Cyclostyle.Tables.add(lo_Table);
				if (ao_Cyclostyle.TableMaxID < lo_Table.TableID) {
					ao_Cyclostyle.TableMaxID = lo_Table.TableID;
				}
			}

			// 打开公文发布对象
			ls_Sql = "SELECT * FROM PublicTemplate WHERE TemplateID = "
					+ Integer.toString(al_CyclostyleID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				ao_Cyclostyle.PublicTemplate = new CPublicTemplate(
						ao_Cyclostyle.Logon);
				ao_Cyclostyle.PublicTemplate.Cyclostyle = ao_Cyclostyle;
				ao_Cyclostyle.PublicTemplate.Open(row, 0);
			}
		}
		lo_Set = null;
		return true;
	}

	/**
	 * 打开公文模板实例
	 * 
	 * @param ao_Cyclostyle
	 *            公文模板对象
	 * @param al_WorkItemID
	 *            公文实例标识
	 * @param al_FlowID
	 *            流程标识
	 * @return
	 * @throws SQLException
	 */
	public static boolean openCyclostyleInst(CCyclostyle ao_Cyclostyle,
			int al_WorkItemID, int al_FlowID) throws Exception {
		// 打开基本信息
		String ls_Sql = "SELECT * FROM TemplateInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		if (!(lo_Set.size()>0)) {
			lo_Set = null;
			// 错误[1029]：不存在所要打开的公文模板实例
			ao_Cyclostyle.Raise(
					1029,
					"openCyclostyleInst",
					String.valueOf(al_WorkItemID) + ","
							+ String.valueOf(al_FlowID));
			return false;
		}

		ao_Cyclostyle.Open(lo_Set.get(0), 0, true);

		lo_Set = null;

		// 判断是否需要打开流程
		Boolean lb_OpenFlow = true;
		if (ao_Cyclostyle.WorkItem != null) {
			if (!((ao_Cyclostyle.FlowFlag & EWorkItemFlowFlag.OrderFlowRule) == EWorkItemFlowFlag.OrderFlowRule || (ao_Cyclostyle.FlowFlag & EWorkItemFlowFlag.ConditionFlowRule) == EWorkItemFlowFlag.ConditionFlowRule)) {
				lb_OpenFlow = false;
			}
		}

		if (lb_OpenFlow) {
			// 打开当前流程
			ls_Sql = "SELECT * FROM FlowInst WHERE WorkItemID = "
					+ Integer.toString(al_WorkItemID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			Boolean lb_Boolean = lo_Set.size()>0;
			if (!lb_Boolean) {
				lo_Set = null;
				// 错误【1030】：不存在所要打开的流程实例
				ao_Cyclostyle.Raise(
						1030,
						"openCyclostyleInst",
						String.valueOf(al_WorkItemID) + ","
								+ String.valueOf(al_FlowID));
				return false;
			}
			for(Map<String,Object> row : lo_Set) {
				CFlow lo_Flow = new CFlow(ao_Cyclostyle.Logon);
				lo_Flow.Cyclostyle = ao_Cyclostyle;
				if (!lo_Flow.Open(row, 0, false)) {
					lo_Set = null;
					return false;
				}
				ao_Cyclostyle.FlowIndexMaxID++;
				lo_Flow.FlowIndex = ao_Cyclostyle.FlowIndexMaxID;
				ao_Cyclostyle.Flows.add(lo_Flow);
			}
		}

		// 打开公文模板中的数据

		// 打开角色
		ls_Sql = "SELECT * FROM RoleInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			CRole lo_Role = new CRole(ao_Cyclostyle.Logon);
			lo_Role.Cyclostyle = ao_Cyclostyle;
			lo_Role.Open(row, 0);
			ao_Cyclostyle.Roles.add(lo_Role);
			if (ao_Cyclostyle.RoleMaxID < lo_Role.RoleID) {
				ao_Cyclostyle.RoleMaxID = lo_Role.RoleID;
			}
		}

		// 打开权限
		ls_Sql = "SELECT * FROM RightInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			CRight lo_Right = new CRight(ao_Cyclostyle.Logon);
			lo_Right.Cyclostyle = ao_Cyclostyle;
			lo_Right.Open(row, 0);
			ao_Cyclostyle.Rights.add(lo_Right);
			if (ao_Cyclostyle.RightMaxID < lo_Right.RightID) {
				ao_Cyclostyle.RightMaxID = lo_Right.RightID;
			}
		}

		// 打开表头属性
		ls_Sql = "SELECT * FROM PropInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			CProp lo_Prop = new CProp(ao_Cyclostyle.Logon);
			lo_Prop.Cyclostyle = ao_Cyclostyle;
			lo_Prop.Open(row, 0);
			ao_Cyclostyle.Props.add(lo_Prop);
			if (ao_Cyclostyle.PropMaxID < lo_Prop.PropID) {
				ao_Cyclostyle.PropMaxID = lo_Prop.PropID;
			}
		}

		// 打开正文模板
		ls_Sql = "SELECT * FROM DocumentInst WHERE DocumentType IN (-1, 0, 1, 2, 3) AND WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			ao_Cyclostyle.setHaveBody(true);
			ao_Cyclostyle.Body.Open(row, 0, true);
			if (ao_Cyclostyle.DocumentMaxID < ao_Cyclostyle.Body.DocumentID)
				ao_Cyclostyle.DocumentMaxID = ao_Cyclostyle.Body.DocumentID;
		}

		// 打开附件描述
		ls_Sql = "SELECT * FROM DocumentInst WHERE DocumentType > 3 AND WorkItemID = "
				+ Integer.toString(al_WorkItemID)
				+ " ORDER BY OriginalID, DocumentVersion DESC";
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			CDocument lo_Document = new CDocument(ao_Cyclostyle.Logon);
			lo_Document.Cyclostyle = ao_Cyclostyle;
			lo_Document.initDocument();
			ao_Cyclostyle.Body.Open(row, 0, true);
			ao_Cyclostyle.Documents.add(lo_Document);
			if (ao_Cyclostyle.DocumentMaxID < ao_Cyclostyle.Body.DocumentID)
				ao_Cyclostyle.DocumentMaxID = ao_Cyclostyle.Body.DocumentID;
		}

		// 设置附件的最大值
		ls_Sql = "SELECT MAX(DocumentID) MAXID FROM DocumentInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			if (MGlobal.readObject(row,"MAXID") != null)
				ao_Cyclostyle.DocumentMaxID = MGlobal.readInt(row,"MAXID");
		}

		// 打开表单定义
		ls_Sql = "SELECT * FROM FormInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			CForm lo_Form = new CForm(ao_Cyclostyle.Logon);
			lo_Form.Cyclostyle = ao_Cyclostyle;
			lo_Form.Open(row, 0);
			ao_Cyclostyle.Forms.add(lo_Form);
			if (ao_Cyclostyle.FormMaxID < lo_Form.FormID)
				ao_Cyclostyle.FormMaxID = lo_Form.FormID;
		}

		// 打开二次函数定义
		ls_Sql = "SELECT * FROM ScriptInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			CScript lo_Script = new CScript(ao_Cyclostyle.Logon);
			lo_Script.Cyclostyle = ao_Cyclostyle;
			lo_Script.Open(row, 0);
			ao_Cyclostyle.Scripts.add(lo_Script);
			if (ao_Cyclostyle.ScriptMaxID < lo_Script.ScriptID) {
				ao_Cyclostyle.ScriptMaxID = lo_Script.ScriptID;
			}
		}

		// 打开数据存储表格
		ls_Sql = "SELECT * FROM TableInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			CTable lo_Table = new CTable(ao_Cyclostyle.Logon);
			lo_Table.Cyclostyle = ao_Cyclostyle;
			lo_Table.Open(row, 0);
			ao_Cyclostyle.Tables.add(lo_Table);
			if (ao_Cyclostyle.TableMaxID < lo_Table.TableID) {
				ao_Cyclostyle.TableMaxID = lo_Table.TableID;
			}
		}

		// 打开公文发布对象
		ls_Sql = "SELECT * FROM PublicTemplateInst WHERE WorkItemID = "
				+ Integer.toString(al_WorkItemID);
		lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		for(Map<String,Object> row : lo_Set) {
			ao_Cyclostyle.PublicTemplate = new CPublicTemplate(
					ao_Cyclostyle.Logon);
			ao_Cyclostyle.PublicTemplate.Cyclostyle = ao_Cyclostyle;
			ao_Cyclostyle.PublicTemplate.Open(row, 0);
		}
		lo_Set = null;

		return true;
	}

	/**
	 * 保存公文模板
	 * 
	 * @param ao_Cyclostyle
	 *            公文模板对象
	 * @param al_CyclostyleID
	 *            公文模板标识：=-1 - 表示为另存，=0 - 表示为正常保存，>0 - 表示为覆盖保存
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static boolean saveCyclostyle(CCyclostyle ao_Cyclostyle,
			int al_CyclostyleID) throws Exception {
		// 基本参数定义
		int ll_CyclostyleID = 0; // 保存的公文模板标识
		int ll_FlowId = 0; // 保存的流程最小标识
		int li_Count = 0; // 保存模板新增流程的个数
		String ls_FlowIDs = ""; // 保存模板删除的流程标识
		String ls_Sql;

		CGlobalPara lo_Para = ao_Cyclostyle.Logon.GlobalPara;

		if (al_CyclostyleID == -1) {
			ao_Cyclostyle.CyclostyleID = -1;
			ao_Cyclostyle.CreateDate = MGlobal.getNow();
			ao_Cyclostyle.AuthorID = lo_Para.UserID;
			ao_Cyclostyle.Author = lo_Para.UserName;
			ao_Cyclostyle.BelongID = lo_Para.BelongID;
		}
		if (al_CyclostyleID > 0)
			ao_Cyclostyle.CyclostyleID = al_CyclostyleID;

		// 取流水号
		if (ao_Cyclostyle.CyclostyleID == -1) {
			// 标准版本需要判断使用情况
			if (MGlobal.WF_SOFT_VERSION == 0) {
				ls_Sql = CSqlHandle
						.getValueBySql("SELECT COUNT(*) FROM TemplateDefine");
				if (ls_Sql == null || ls_Sql.equals(""))
					ls_Sql = "0";
				if (Integer.parseInt(ls_Sql) >= MGlobal.MaxTemplate) {
					// 错误[1136]：标准版本不能超过规定的公文模板个数
					ao_Cyclostyle.Raise(1136, "saveCyclostyle",
							String.valueOf(al_CyclostyleID));
					return false;
				}
			}

			// 公文模板标识流水号
			ll_CyclostyleID = ao_Cyclostyle.Logon.getPublicFunc().getSeqValue(
					"CyclostyleID", "公文模板标识流水号");

			// 流程标识流水号
			if (ao_Cyclostyle.Flows.size() > 0) {
				ll_FlowId = ao_Cyclostyle.Logon.getPublicFunc().getSeqValue(
						"FlowID", "流程标识流水号");
			}
		} else {
			li_Count = 0;
			// 正常保存情况
			if (al_CyclostyleID == 0) {
				for (CFlow lo_Flow : ao_Cyclostyle.Flows) {
					if (lo_Flow.FlowID == -1) {
						li_Count++;
					}
				}
			} else {
				ls_FlowIDs = ";"
						+ CSqlHandle.getValuesBySql(
								"SELECT FlowID FROM FlowDefine WHERE TemplateID = "
										+ String.valueOf(al_CyclostyleID),
								"FlowID");
				for (CFlow lo_Flow : ao_Cyclostyle.Flows) {
					if (ls_FlowIDs.indexOf(";" + String.valueOf(lo_Flow.FlowID)
							+ ";") == -1) {
						lo_Flow.FlowID = -1;
						li_Count++;
					}
				}
			}

			if (li_Count > 0) {
				ll_FlowId = ao_Cyclostyle.Logon.getPublicFunc().getSeqValue(
						"FlowID", "流程标识流水号", true, li_Count);
			}
		}

		li_Count = 0;
		ls_FlowIDs = "";
		for (CFlow lo_Flow : ao_Cyclostyle.Flows) {
			if (ao_Cyclostyle.CyclostyleID == -1) {
				lo_Flow.FlowID = ll_FlowId + li_Count;
				li_Count++;
			} else {
				if (lo_Flow.FlowID == -1) {
					lo_Flow.FlowID = ll_FlowId + li_Count;
					li_Count++;
				}
			}
			ls_FlowIDs += String.valueOf(lo_Flow.FlowID) + ",";
		}
		if (ls_FlowIDs != "")
			ls_FlowIDs = ls_FlowIDs.substring(0, ls_FlowIDs.length() - 1);

		// 更新数据
		// 模板标识
		String ls_Id = String.valueOf(ao_Cyclostyle.CyclostyleID);

		// 删除已有数据
		if (ll_CyclostyleID == 0) {
			// 删除模板
			ls_Sql = "DELETE FROM TemplateDefine WHERE TemplateID = " + ls_Id;
			// lo_SqlHandle.execBatchSql(ls_Sql);
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			if (!ao_Cyclostyle.DeletedFlowIDs.equals("")) {
				ls_FlowIDs = ao_Cyclostyle.DeletedFlowIDs.replaceAll(";", ",")
						+ ls_FlowIDs;
				if (ls_FlowIDs.substring(ls_FlowIDs.length() - 1, 1) == ",") {
					ls_FlowIDs = ls_FlowIDs.substring(0,
							ls_FlowIDs.length() - 1);
				}
			}
			// 覆盖保存
			if (al_CyclostyleID > 0) {
				// 删除流程步骤定义
				ls_Sql = "DELETE FROM FlowActivity WHERE FlowID IN (SELECT FlowID FROM"
						+ " FlowDefine WHERE TemplateID = "
						+ String.valueOf(al_CyclostyleID) + ")";
				CSqlHandle.getJdbcTemplate().execute(ls_Sql);
				// 删除流程定义
				ls_Sql = "DELETE FROM FlowDefine WHERE TemplateID = "
						+ String.valueOf(al_CyclostyleID);
				CSqlHandle.getJdbcTemplate().execute(ls_Sql);
			} else {
				if (!ls_FlowIDs.equals("")) {
					// 删除流程步骤定义
					ls_Sql = "DELETE FROM FlowActivity WHERE FlowID IN ("
							+ ls_FlowIDs + ")";
					CSqlHandle.getJdbcTemplate().execute(ls_Sql);
					// 删除流程定义
					ls_Sql = "DELETE FROM FlowDefine WHERE FlowID IN ("
							+ ls_FlowIDs + ")";
					CSqlHandle.getJdbcTemplate().execute(ls_Sql);
				}
			}

			// 删除权限
			ls_Sql = "DELETE FROM RightList WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			// 删除角色
			ls_Sql = "DELETE FROM RoleList WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			// 删除表头属性
			ls_Sql = "DELETE FROM PropList WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			// 删除表格定义
			ls_Sql = "DELETE FROM TableList WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			// 删除二次开发函数
			ls_Sql = "DELETE FROM ScriptList WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			// 删除表单定义
			ls_Sql = "DELETE FROM FormList WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			// 删除附件模板使用列表
			ls_Sql = "DELETE FROM DocumentFileList WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			// 删除正文附件
			ls_Sql = "DELETE FROM DocumentList WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			// 删除公文发布模板定义
			ls_Sql = "DELETE FROM PublicTemplate WHERE TemplateID = " + ls_Id;
			CSqlHandle.getJdbcTemplate().execute(ls_Sql);

			ao_Cyclostyle.EditorID = lo_Para.UserID;
			ao_Cyclostyle.Editor = lo_Para.UserName;
			ao_Cyclostyle.EditDate = MGlobal.getNow();
		} else {
			ao_Cyclostyle.CyclostyleID = ll_CyclostyleID;
			ao_Cyclostyle.EditorID = 0;
			ao_Cyclostyle.Editor = "";
		}

		// 保存内容
		// ls_Sql = "SELECT '1' FROM TemplateDefine WHERE TemplateID = " +
		// String.valueOf(ao_Cyclostyle.getCyclostyleID());
		// int li_SaveType = (lo_SqlHandle.getValuesBySql(ls_Sql).equals("1") ?
		// 1 : 0);
		int li_SaveType = 0;
		int li_Type = 1;
		// Connection lo_Conn = CSqlHandle.getConnection();

		// 保存模板基本信息
		String lo_State = ao_Cyclostyle.getSaveState(li_SaveType, li_Type,
				false);
		ao_Cyclostyle.Save(lo_State, li_SaveType, li_Type, false, 1);

		if (ao_Cyclostyle.Flows.size() > 0) {
			// 保存流程定义
			lo_State = CFlow.getSaveState(li_SaveType, li_Type, false);
			for (CFlow lo_Flow : ao_Cyclostyle.Flows) {
				lo_Flow.Save(lo_State, li_SaveType, li_Type, false, 0);
			}
			// lo_State.executeBatch();

			// 保存流程步骤信息定义
			lo_State = CActivity.getSaveState(li_SaveType, li_Type, false);
			for (CFlow lo_Flow : ao_Cyclostyle.Flows) {
				for (CActivity lo_Activity : lo_Flow.Activities) {
					lo_Activity.Save(lo_State, li_SaveType, li_Type, false, 0);
				}
			}
			// lo_State.executeBatch();
		}

		// 保存权限
		if (ao_Cyclostyle.Rights.size() > 0) {
			lo_State = CRight.getSaveState(li_SaveType, li_Type, false);
			for (CRight lo_Right : ao_Cyclostyle.Rights) {
				lo_Right.Save(lo_State, li_SaveType, li_Type, false, 0);
			}
			// lo_State.executeBatch();
		}

		// 保存角色
		if (ao_Cyclostyle.Roles.size() > 0) {
			lo_State = CRole.getSaveState(li_SaveType, li_Type, false);
			for (CRole lo_Role : ao_Cyclostyle.Roles) {
				lo_Role.Save(lo_State, li_SaveType, li_Type, false, 0);
			}
			// lo_State.executeBatch();
		}

		// 保存表头属性
		if (ao_Cyclostyle.Props.size() > 0) {
			lo_State = CProp.getSaveState(li_SaveType, li_Type, false);
			for (CProp lo_Prop : ao_Cyclostyle.Props) {
				lo_Prop.Save(lo_State, li_SaveType, li_Type, false, 0);
			}
			// lo_State.executeBatch();
		}

		// 保存表格定义
		if (ao_Cyclostyle.Tables.size() > 0) {
			lo_State = CTable.getSaveState(li_SaveType, li_Type, false);
			for (CTable lo_Table : ao_Cyclostyle.Tables) {
				lo_Table.Save(lo_State, li_SaveType, li_Type, false, 0);
			}
			// lo_State.executeBatch();
		}

		// 保存二次开发函数
		if (ao_Cyclostyle.Scripts.size() > 0) {
			lo_State = CScript.getSaveState(li_SaveType, li_Type, false);
			for (CScript lo_Script : ao_Cyclostyle.Scripts) {
				lo_Script.Save(lo_State, li_SaveType, li_Type, false, 0);
			}
			// lo_State.executeBatch();
		}

		// 保存表单定义
		if (ao_Cyclostyle.Forms.size() > 0) {
			lo_State = CForm.getSaveState(li_SaveType, li_Type, false);
			for (CForm lo_Form : ao_Cyclostyle.Forms) {
				lo_Form.Save(lo_State, li_SaveType, li_Type, false, 0);
			}
			// lo_State.executeBatch();
		}

		// 保存正文附件
		// 正文
		if (ao_Cyclostyle.Body != null || ao_Cyclostyle.Documents.size() > 0) {
			lo_State = CDocument.getSaveState(li_SaveType, li_Type, false);

			if (ao_Cyclostyle.Body != null) {
				ao_Cyclostyle.Body.Save(lo_State, li_SaveType, li_Type, false,
						0);
			}
			// 附件
			for (CDocument lo_Document : ao_Cyclostyle.Documents) {
				lo_Document.Save(lo_State, li_SaveType, li_Type, false, 0);
			}
			// lo_State.executeBatch();
		}

		// 保存正文模板列表定义信息
		if (ao_Cyclostyle.Body != null) {
			Document lo_Xml = ao_Cyclostyle.Body.getDocumentFileList();
			if (lo_Xml != null) {
				NodeList lo_List = lo_Xml.getDocumentElement().getChildNodes();
				if (lo_List.getLength() > 0) {
					lo_State = CDocumentFileList.getSaveState(li_SaveType, 0);
					for (int i = 0; i < lo_List.getLength(); i++) {
						Element lo_Node = (Element) lo_List.item(i);
						CDocumentFileList lo_File = new CDocumentFileList();
						lo_File.Import(lo_Node, 1);
						lo_File.Save(lo_State, li_SaveType, li_Type, 0);
						lo_File = null;
					}
					// lo_State.executeBatch();
				}
			}
			lo_Xml = null;
		}

		// 保存公文发布信息
		if (ao_Cyclostyle.PublicTemplate != null) {
			lo_State = CPublicTemplate
					.getSaveState(li_SaveType, li_Type, false);
			ao_Cyclostyle.PublicTemplate.Save(lo_State, li_SaveType, li_Type,
					false, 1);
		}

		ao_Cyclostyle.DeletedFlowIDs = "";

		return true;
	}

	/**
	 * 从文件导入
	 * 
	 * @param ai_Type
	 *            导入方式：=0-原始；=1-新方式；
	 * @param ao_Cyclostyle
	 *            公文模板对象
	 * @param as_FileName
	 *            文件名称（取其一）
	 * @param as_FileContent
	 *            文件内容（取其一）
	 * @return
	 * @throws Exception
	 */
	public static boolean importFromFile(int ai_Type,
			CCyclostyle ao_Cyclostyle, String as_FileName, String as_FileContent)
			throws Exception {
		String ls_Content = as_FileContent;
		if (as_FileName != null && as_FileName != "") {
			ls_Content = MFile.readTxtFile(as_FileName);
		}

		if (ai_Type == CCyclostyle.Const_ExImport_Type) {
			ao_Cyclostyle.ImportFormXml(ls_Content, 0);
		} else {
			ao_Cyclostyle.ReadContent(ls_Content, 0);
		}

		if ((ao_Cyclostyle.FlowFlag & 8) == 8) {
			ao_Cyclostyle.FlowFlag = ao_Cyclostyle.FlowFlag - 8;
			ao_Cyclostyle.OpenType = EDataHandleType.XmlType;
		}

		return true;
	}

	/**
	 * 导出到文件
	 * 
	 * @param ao_Cyclostyle
	 *            公文模板对象
	 * @param as_FileName
	 *            文件名称，为空则导出内容
	 * @return
	 * @throws Exception
	 */
	public static String exportToFile(CCyclostyle ao_Cyclostyle,
			String as_FileName) throws Exception {
		// 全部采用新方式导出
		String ls_Text = ao_Cyclostyle.ExportToXml("Cyclostyle", 0);
		// String ls_Text = ao_Cyclostyle.WriteContent(0);

		if (as_FileName == null || as_FileName.equals(""))
			return ls_Text;
		return MFile.writeTxtFile(as_FileName, ls_Text);
	}

	/**
	 * 获取扩展内容
	 * 
	 * @param ao_Cyclostyle
	 * @return
	 */
	public static String getExtendXmlValue(CCyclostyle ao_Cyclostyle) {
		String ls_Xml = "<Extend>";
		ls_Xml += MXmlHandle.getXml(ao_Cyclostyle.getFileFieldXml());
		ls_Xml += MXmlHandle.getXml(ao_Cyclostyle.getPrintTempXml());
		ls_Xml += MXmlHandle.getXml(ao_Cyclostyle.getPublicTempXml());
		Document lo_Xml = MXmlHandle.LoadXml(ls_Xml + "</Extend>");
		Document lo_Extend = ao_Cyclostyle.getExtendFieldsXml();
		NamedNodeMap lo_Map = lo_Extend.getDocumentElement().getAttributes();
		for (int i = 0; i < lo_Map.getLength(); i++) {
			Attr lo_Attr = (Attr) lo_Map.item(i);
			lo_Xml.getDocumentElement().setAttribute(lo_Attr.getName(),
					lo_Attr.getValue());
		}

		lo_Xml.getDocumentElement().setAttribute("PageType",
				Integer.toString(ao_Cyclostyle.PageType));
		lo_Xml.getDocumentElement().setAttribute("FirstType",
				Integer.toString(ao_Cyclostyle.FirstType));
		lo_Xml.getDocumentElement().setAttribute("FormStyle",
				Integer.toString(ao_Cyclostyle.FormStyle));
		lo_Xml.getDocumentElement().setAttribute("SamePartDealType",
				Integer.toString(ao_Cyclostyle.SamePartDealType));
		lo_Xml.getDocumentElement().setAttribute("SamePartActivities",
				ao_Cyclostyle.getSamePartActivities());
		lo_Xml.getDocumentElement().setAttribute("CssFiles",
				ao_Cyclostyle.CssFiles);
		lo_Xml.getDocumentElement().setAttribute("IncludeFiles",
				ao_Cyclostyle.IncludeFiles);
		lo_Xml.getDocumentElement().setAttribute("VBScript",
				ao_Cyclostyle.VBScript);
		lo_Xml.getDocumentElement().setAttribute("JavaScript",
				ao_Cyclostyle.JavaScript);

		return MXmlHandle.getXml(lo_Xml);
	}

	/**
	 * 插入正文附件
	 * 
	 * @param as_DocumentName
	 * @param ai_DocumentType
	 * @return
	 * @throws Exception
	 */
	public static CDocument insertDocument(CCyclostyle ao_Cyclostyle,
			String as_DocumentName, int ai_DocumentType) throws Exception {
		try {
			CDocument lo_Document = new CDocument(ao_Cyclostyle.Logon);
			lo_Document.Cyclostyle = ao_Cyclostyle;
			lo_Document.initDocument();
			ao_Cyclostyle.DocumentMaxID++;
			lo_Document.DocumentID = ao_Cyclostyle.DocumentMaxID;
			if (ai_DocumentType <= EDocumentType.WordVersionBody) {
				lo_Document.setDocumentType(EDocumentType.CommondDocument);
			} else {
				lo_Document.setDocumentType(ai_DocumentType);
			}
			lo_Document.DocumentName = as_DocumentName;
			ao_Cyclostyle.Documents.add(lo_Document);
			if (ao_Cyclostyle.WorkItem != null) {
				if (ao_Cyclostyle.WorkItem.CurEntry != null) {
					lo_Document.EntryID = ao_Cyclostyle.WorkItem.CurEntry.EntryID;
				}
			}

			ao_Cyclostyle.HaveDocument = true;

			return lo_Document;
		} catch (Exception ex) {
			ao_Cyclostyle.Raise(ex, "InsertDocument", as_DocumentName + "("
					+ String.valueOf(ai_DocumentType) + ")");
			return null;
		}
	}

}
