package org.szcloud.framework.workflow.core.business;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.szcloud.framework.workflow.core.base.CGlobalPara;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CResult;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EActivityConditionType;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.emun.EBodyAccessType;
import org.szcloud.framework.workflow.core.emun.ECaculateStatusType;
import org.szcloud.framework.workflow.core.emun.EConditionCompareType;
import org.szcloud.framework.workflow.core.emun.EConditionTumTrfType;
import org.szcloud.framework.workflow.core.emun.EConditionType;
import org.szcloud.framework.workflow.core.emun.ECurActivityTransType;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EDocumentRightType;
import org.szcloud.framework.workflow.core.emun.EDocumentType;
import org.szcloud.framework.workflow.core.emun.EDueAlertType;
import org.szcloud.framework.workflow.core.emun.EEntryRecipientType;
import org.szcloud.framework.workflow.core.emun.EEntryStatus;
import org.szcloud.framework.workflow.core.emun.EEntryType;
import org.szcloud.framework.workflow.core.emun.EFlowArithmeticType;
import org.szcloud.framework.workflow.core.emun.EFormAccessType;
import org.szcloud.framework.workflow.core.emun.EFormCellAccessType;
import org.szcloud.framework.workflow.core.emun.ELimitCompareType;
import org.szcloud.framework.workflow.core.emun.ELockType;
import org.szcloud.framework.workflow.core.emun.EPropDataType;
import org.szcloud.framework.workflow.core.emun.ERightType;
import org.szcloud.framework.workflow.core.emun.EScriptCaculateType;
import org.szcloud.framework.workflow.core.emun.EStatusTransLimitType;
import org.szcloud.framework.workflow.core.emun.ETimelimitAlertFlag;
import org.szcloud.framework.workflow.core.emun.ETimelimitAlertToType;
import org.szcloud.framework.workflow.core.emun.ETimelimitDueFlag;
import org.szcloud.framework.workflow.core.emun.ETransOrderByType;
import org.szcloud.framework.workflow.core.emun.EUserType;
import org.szcloud.framework.workflow.core.emun.EWorkItemFlowFlag;
import org.szcloud.framework.workflow.core.emun.EWorkItemStatus;
import org.szcloud.framework.workflow.core.entity.CActivity;
import org.szcloud.framework.workflow.core.entity.CCondition;
import org.szcloud.framework.workflow.core.entity.CCyclostyle;
import org.szcloud.framework.workflow.core.entity.CDocument;
import org.szcloud.framework.workflow.core.entity.CDueAlert;
import org.szcloud.framework.workflow.core.entity.CEntry;
import org.szcloud.framework.workflow.core.entity.CFileExchange;
import org.szcloud.framework.workflow.core.entity.CFlow;
import org.szcloud.framework.workflow.core.entity.CFlowLine;
import org.szcloud.framework.workflow.core.entity.CForm;
import org.szcloud.framework.workflow.core.entity.CFormCell;
import org.szcloud.framework.workflow.core.entity.CMoveEntry;
import org.szcloud.framework.workflow.core.entity.CProp;
import org.szcloud.framework.workflow.core.entity.CRight;
import org.szcloud.framework.workflow.core.entity.CRole;
import org.szcloud.framework.workflow.core.entity.CSuperUser;
import org.szcloud.framework.workflow.core.entity.CSupervise;
import org.szcloud.framework.workflow.core.entity.CTransact;
import org.szcloud.framework.workflow.core.entity.CWorkItem;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MFile;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hp.hpl.sparta.xpath.ThisNodeTest;

public class TWorkItem {

	/**
	 * 新建实例
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_CyclostyleID
	 *            公文模板标识
	 * @param al_FlowID
	 *            使用的流程模板标识:<br>
	 *            =-1 - 使用外部定义流程<br>
	 *            =0 - 使用缺省流程[如果没有缺省流程，则使用第一个流程，如果存在多个缺省流程，则打开所有缺省流程]<br>
	 *            >0 - 使用指定流程标识的流程
	 * @return
	 */
	public static CWorkItem newWorkItem(CLogon ao_Logon, int al_CyclostyleID, int al_FlowID) {
		try {
			/** 记录日志 **/
			ao_Logon.Record("TWorkItem->newWorkItem", String.valueOf(al_CyclostyleID) + "," + String.valueOf(al_FlowID));

			// CSqlHandle lo_SqlHandle = ao_Logon.SqlHandle;

			// 判断模板是否存在或合理
			String ls_Sql = "SELECT TemplateIsValid FROM TemplateDefine WHERE TemplateID = " + String.valueOf(al_CyclostyleID);
			ls_Sql = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
			if (MGlobal.isEmpty(ls_Sql)) {
				// 错误【1027】：不存在用于新建公文的公文模板
				ao_Logon.Raise(1027, "TWorkItem->newWorkItem", String.valueOf(al_CyclostyleID) + "," + String.valueOf(al_FlowID));
				return null;
			}

			if (ls_Sql.equals("0")) {
				// 错误【1028】：用于新建公文的公文模板设计不合理
				ao_Logon.Raise(1028, "TWorkItem->newWorkItem", String.valueOf(al_CyclostyleID) + "," + String.valueOf(al_FlowID));
				return null;
			}

			CWorkItem lo_Item = new CWorkItem(ao_Logon);
			// 打开模板
			lo_Item.Cyclostyle = ao_Logon.getCyclostyle(al_CyclostyleID, al_FlowID);
			lo_Item.Cyclostyle.WorkItem = lo_Item;

			CGlobalPara lo_Para = lo_Item.GlobalPara();
			// 设置初始化信息
			lo_Item.CreatorID = lo_Para.UserID;
			lo_Item.Creator = lo_Para.UserName;
			lo_Item.DeptID = lo_Para.DeptID;
			lo_Item.DeptName = lo_Para.DeptName;
			lo_Item.BelongID = lo_Para.BelongID;
			// lo_Item.WorkItemName = lo_Item.Cyclostyle.CyclostyleName + "实例";

			// 设置拟稿人的值
			CRole lo_Role = lo_Item.Cyclostyle.getRoleById(1);
			lo_Role.Value = String.valueOf(lo_Item.GlobalPara().UserID) + ";";

			// 当前工作流对象
			if (lo_Item.Cyclostyle.Flows.size() > 0) {
				lo_Item.CurFlow = lo_Item.Cyclostyle.getFlowById(al_FlowID);
				if (lo_Item.CurFlow == null)
					lo_Item.CurFlow = lo_Item.Cyclostyle.Flows.get(0);
				// 当前步骤
				lo_Item.CurActivity = lo_Item.CurFlow.getActivityById(1);
			}

			// 设置流转状态
			CEntry lo_RootEntry = insertEntry(lo_Item, EEntryType.VirtualEntry, null, null, null);
			CRight lo_Right = lo_Item.Cyclostyle.getRightById(2);
			CEntry lo_Entry = insertEntry(lo_Item, EEntryType.VirtualEntry, lo_Item.CurActivity, lo_Right, lo_RootEntry);

			// 实例流转的当前状态
			lo_Item.CurEntry = insertEntry(lo_Item, EEntryType.ActualityEntry, lo_Item.CurActivity, lo_Right, lo_Entry);

			// 设置附件状态标识
			for (CDocument lo_Document : lo_Item.Cyclostyle.Documents) {
				lo_Document.EntryID = lo_Item.CurEntry.EntryID;
			}

			// 当前权限（由本步骤所拥有的所有权限组成）
			lo_Item.CurRight = createCurrentRight(lo_Item, "2");

			// 设置公文读写属性
			lo_Item.WorkItemAccess = true;

			// 权限实例化套用
			if (rightToInst(lo_Item) == false) {
				lo_Item.ClearUp();
				lo_Item = null;
				return null;
			}

			// 设置公文实例缺省名称
			lo_Item.setWorkItemName(lo_Item.Cyclostyle.CyclostyleName + "实例");

			// 新建公文时，需要初始化表头属性和角色
			initPropRole(lo_Item);

			// 打开时----调用计算函数[二次开发]
			caculateScript(lo_Item, ECaculateStatusType.CaculateOpenStatus);

			caculateScriptContent(lo_Item, lo_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuOpenForAllReadOnly));
			caculateScriptContent(lo_Item, lo_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuOpenForAll));
			caculateScriptContent(lo_Item, lo_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuOpenForFirst));

			// 设置是否可以转发
			Element lo_Node = lo_Item.Cyclostyle.getExtendFieldsXml().getDocumentElement();
			if (!MGlobal.isEmpty(lo_Node.getAttribute("ConfigTransfer"))) {
				lo_Item.ConfigTransfer = Integer.parseInt(lo_Node.getAttribute("ConfigTransfer"));
			}

			return lo_Item;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取公文日志登记的参数
	 * 
	 * @param ao_Item
	 *            公文对象
	 * @return
	 */
	public static String getWrkLogPara(CWorkItem ao_Item) {
		if (ao_Item.CurEntry == null) {
			return String.valueOf(ao_Item.WorkItemID) + ",0";
		} else {
			return String.valueOf(ao_Item.WorkItemID) + "," + String.valueOf(ao_Item.CurEntry.EntryID);
		}
	}

	/**
	 * 获取公文日志登记的参数
	 * 
	 * @param al_WorkItemID
	 *            公文标识
	 * @param al_EntryID
	 *            状态标识
	 * @return
	 */
	public static String getWrkLogPara(int al_WorkItemID, int al_EntryID) {
		return String.valueOf(al_WorkItemID) + "," + String.valueOf(al_EntryID);
	}

	/**
	 * 打开实例
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemID
	 *            工作实例标识
	 * @param al_EntryID
	 *            实例状态标识，-1——表示缺省，-2——表示有管理权限的人以只读的方式打开查看,
	 *            -3——表示打开历史公文链接，-4——表示打开版本公文链接
	 * @return
	 * @throws Exception
	 */
	public static CWorkItem openWorkItem(CLogon ao_Logon, int al_WorkItemID, int al_EntryID) throws Exception {
		CWorkItem lo_Item = new CWorkItem(ao_Logon);
		if (openWorkItem(lo_Item, al_WorkItemID, al_EntryID))
			return lo_Item;
		lo_Item.ClearUp();
		lo_Item = null;
		return null;
	}

	/**
	 * 打开实例
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemID
	 *            工作实例标识
	 * @param al_EntryID
	 *            实例状态标识，-1——表示缺省，-2——表示有管理权限的人以只读的方式打开查看,
	 *            -3——表示打开历史公文链接，-4——表示打开版本公文链接
	 * @return
	 * @throws Exception
	 */
	public static boolean openWorkItem(CWorkItem ao_Item, final int al_WorkItemID, int al_EntryID) throws Exception {
		try {
			// 声明开始：在公文打开时利用公共变量定义来打开一些特殊使用情况的公文说明
			// 使用变量intParameter来确认打开公文的特殊状态
			// intParameter=101 表示打开公文以前版本
			// intParameter=102 表示打开公文链接(在al_EntryID=-2的情况下)
			// intParameter=103 表示打开公文历史版本
			// 需要配合intParameter2参数，表示打开公文历史版本
			// intParameter=104 表示打开传阅公文
			// 使用变量lngParameter表示公文的状态标识
			// 当intParameter=101时，查看状态标识为lngParameter的版本
			// 当intParameter=102时，打开状态标识为lngParameter的链接
			// 当intParameter=103时，打开历史版本标识为lngParameter
			// 声明结束

			// 记录日志
			ao_Item.Record("openWorkItem", getWrkLogPara(al_WorkItemID, al_EntryID));

			// 基本变量定义
			// CSqlHandle lo_SqlHandle = ao_Item.SqlHandle(); // 数据库访问对象
			CGlobalPara lo_Para = ao_Item.Logon.GlobalPara; // 全局访问变量
			String ls_UserId = String.valueOf(lo_Para.UserID); // 当前用户标识字符串值
			String ls_ItemId = String.valueOf(al_WorkItemID); // 当前公文标识字符串值
			int ll_CurEntryID = 0; // 打开状态标识
			// TWorkAdmin lo_Admin = ao_Item.Logon.getWorkAdmin();
			Date ld_Date = MGlobal.getNow(); // 当前时间
			final java.sql.Timestamp ld_SqlDate = MGlobal.dateToSqlTime(ld_Date); // 数据库更新使用的当前时间

			String ls_Index = ""; // 表示打开公文历史版本，缺省为空，打开历史公文为：1
			if (ao_Item.intParameter == 103)
				ls_Index = String.valueOf(ao_Item.intParameter2);

			// 判断是否存在将被打开的公文
			String ls_Sql = "SELECT '1' FROM WorkItemInst{0} WHERE WorkItemID = {1}";
			ls_Sql = CSqlHandle.getJdbcTemplate().queryForObject(MessageFormat.format(ls_Sql, ls_Index, ls_ItemId), String.class);
			if (MGlobal.isEmpty(ls_Sql)) {
				// 错误【1031】：不存在要被打开的公文
				ao_Item.Raise(1031, "openWorkItem", getWrkLogPara(al_WorkItemID, al_EntryID));
				return false;
			}

			// 判断是否有权限打开
			int ll_ID1 = 0;
			int ll_ID2 = 0;
			String ls_RoleUserIDs = "";
			String ls_DeptIDs = "";
			String ls_UserIDs = "";
			Boolean lb_Boolean = false;
			List<Map<String, Object>> lo_Set;

			// 表示有管理权限的人以只读的方式打开查看
			if (al_EntryID < -1) {
				// 判断当前用户是否有查看该公文的权限
				// 正常处理
				ls_Sql = "SELECT MAX(EntryID) AS MaxEntryID FROM EntryInst{0} WHERE WorkItemID = {1} AND (RecipientID = {2} OR ProxyID = {2})";
				ls_Sql = CSqlHandle.getJdbcTemplate().queryForObject(MessageFormat.format(ls_Sql, ls_Index, ls_ItemId, ls_UserId), String.class);
				if (MGlobal.isInt(ls_Sql))
					ll_CurEntryID = Integer.parseInt(ls_Sql);

				// 代理处理
				if (ll_CurEntryID == 0) {
					ls_Sql = "SELECT MAX(EntryID) AS MaxEntryID FROM ProxyWorkItemEntry WHERE WorkItemID = {0} AND ProxyID = {1}";
					ls_Sql = CSqlHandle.getJdbcTemplate().queryForObject(MessageFormat.format(ls_Sql, ls_ItemId, ls_UserId), String.class);
					if (MGlobal.isInt(ls_Sql))
						ll_CurEntryID = Integer.parseInt(ls_Sql);
				}

				// 岗位、岗位组、部门组
				ls_RoleUserIDs = TWorkAdmin.getUserRoleIDs(ao_Item.Logon, lo_Para.UserID);
				if (MGlobal.isExist(ls_RoleUserIDs)) {
					ls_Sql = "SELECT MAX(EntryID) AS MaxEntryID FROM EntryInst{0} WHERE WorkItemID = {1} AND (RecipientID IN ({2}) OR RUserID IN ({2}))";
					ls_Sql = CSqlHandle.getValueBySql(MessageFormat.format(ls_Sql, ls_Index, ls_ItemId, ls_RoleUserIDs));
					if (MGlobal.isInt(ls_Sql))
						ll_CurEntryID = Integer.parseInt(ls_Sql);
				}

				// 具有管理权限的用户
				if (ll_CurEntryID == 0) {
					if ((lo_Para.UserType & EUserType.SystemAdministrator) != EUserType.SystemAdministrator) {
						ls_Sql = "SELECT UserID, ModuleID, AdminType, DeptIDS, UserIDS, FlagID FROM WorkRight WHERE "
								+ "(UserID = {0} OR UserID IN (SELECT GroupID FROM UserGroup WHERE UserID = {0})) AND "
								+ "((ModuleID IN (SELECT TemplateID FROM TemplateInst WHERE WorkItemID = {1}) AND AdminType = 1) OR "
								+ "(ModuleID IN (SELECT TypeID FROM TemplateInst WHERE WorkItemID = {1}) AND AdminType = 0)) AND ReadRight = 1";
						lo_Set = CSqlHandle.getJdbcTemplate().queryForList(MessageFormat.format(ls_Sql, ls_UserId, ls_ItemId));
						ll_CurEntryID = -1;
						for (Map<String, Object> row : lo_Set) {
							if (MGlobal.readString(row, "DeptIDS").endsWith("") && MGlobal.readString(row, "UserIDS").equals("")) {
								ll_CurEntryID = 0;
							}
							if (!MGlobal.readString(row, "DeptIDS").equals("")) {
								ls_DeptIDs += MGlobal.readString(row, "DeptIDS");
							}
							if (!MGlobal.readString(row, "UserIDS").equals("")) {
								ls_UserIDs += MGlobal.readString(row, "UserIDS");
							}
						}
						lo_Set = null;

						if (ll_CurEntryID == -1) {
							ls_DeptIDs = ls_DeptIDs.replaceAll(";", ",");
							if (MGlobal.right(ls_DeptIDs, 1).equals(",")) {
								ls_DeptIDs = ls_DeptIDs.substring(0, ls_DeptIDs.length() - 1);
							}
							ls_UserIDs = ls_UserIDs.replaceAll(";", ",");
							if (MGlobal.right(ls_UserIDs, 1).equals(",")) {
								ls_UserIDs = ls_UserIDs.substring(0, ls_UserIDs.length() - 1);
							}
							ls_Sql = "SELECT UserID FROM UserInfo WHERE UserType = 0";
							if (ls_UserIDs.equals("")) {
								if (!ls_DeptIDs.equals(""))
									ls_Sql += MessageFormat.format(" AND DeptID IN ({0})", ls_DeptIDs);
							} else {
								if (ls_DeptIDs.equals("")) {
									ls_Sql += MessageFormat.format(
											" AND (UserID IN ({0}) OR UserID IN (SELECT UserID FROM UserGroup WHERE GroupID IN ({0})))", ls_UserIDs);
								} else {
									ls_Sql += MessageFormat.format(
											" AND ((UserID IN ({0}) OR UserID IN (SELECT UserID FROM UserGroup WHERE GroupID IN ({0})))", ls_UserIDs);
									ls_Sql += MessageFormat.format(" OR DeptID IN ({0}))", ls_DeptIDs);
								}
							}

							ls_Sql = MessageFormat.format("SELECT '1' FROM EntryInst{0} WHERE RecipientID IN ({1}) OR ProxyID IN ({1})", ls_Index,
									ls_Sql);
							if (CSqlHandle.getValueBySql(ls_Sql).equals("1"))
								ll_CurEntryID = 0;
						}
					}
				}
			} else { // al_EntryID > -2
				// 查找授权用户
				ls_Sql = "SELECT RecipientID FROM ProxyWorkItemEntry WHERE WorkItemID = {0} AND ProxyID = {1}";
				ls_UserIDs = CSqlHandle.getValuesBySql(MessageFormat.format(ls_Sql, ls_ItemId, ls_UserId), null, ",") + ls_UserId;

				// 表示缺省，在这种情况下首选择本人未处理的最大标识，如果没有则查看是否有代理
				// 情况，再没有最后查看已处理情况
				if (al_EntryID == -1) {
					// 正常处理和代理情况
					ls_Sql = "SELECT EntryID, RecipientID, ProxyID, StateID, RUserID FROM EntryInst{0} WHERE EntryType = 2"
							+ " AND WorkItemID = {1} AND (RecipientID IN ({2}) OR ProxyID IN ({2})";
					ls_Sql = MessageFormat.format(ls_Sql, ls_Index, ls_ItemId, ls_UserIDs);

					// 岗位、岗位组、部门组
					ls_RoleUserIDs = TWorkAdmin.getUserRoleIDs(ao_Item.Logon, lo_Para.UserID);
					if (ls_RoleUserIDs.equals("")) {
						ls_Sql += ")";
					} else {
						ls_Sql += MessageFormat.format(" OR RecipientID IN ({0}) OR RUserID IN ({0}))", ls_RoleUserIDs);
					}

					lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
					lb_Boolean = lo_Set.size() > 0; // 当前用户是否有权限打开
					if (!lb_Boolean) {
						ll_CurEntryID = -1;
					} else {
						ll_CurEntryID = 0;
						ll_ID1 = 0;
						ll_ID2 = 0;
						for (Map<String, Object> row : lo_Set) {
							// 选择本人未处理或正在处理的最大标识
							if (MGlobal.readInt(row, "StateID") >= 0 && MGlobal.readInt(row, "StateID") <= 2) {
								if (MGlobal.readInt(row, "RecipientID") == ao_Item.GlobalPara().UserID) {
									ll_CurEntryID = MGlobal.readInt(row, "EntryID");
								} else {
									// 选择本人代理其他人未处理的最大标识
									if (MGlobal.readInt(row, "StateID") != 2) {
										ll_ID1 = MGlobal.readInt(row, "EntryID");
									}
								}
							} else {
								// 选择本人已处理情况
								ll_ID2 = MGlobal.readInt(row, "EntryID");
							}
						}

						if (ll_CurEntryID == 0) {// 没有本人未处理或正在处理情况
							if (ll_ID1 == 0) {
								ll_CurEntryID = ll_ID2; // 没有本人代理其他人未处理情况
							} else {
								ll_CurEntryID = ll_ID1; // 本人代理其他人未处理情况
							}
						}
					}
					lo_Set = null;
				} else {
					ls_Sql = "SELECT EntryID, RecipientID, ProxyID, StateID, RUserID FROM EntryInst{0}"
							+ " WHERE EntryType = 2 AND WorkItemID = {1} AND EntryID = {2}";
					lo_Set = CSqlHandle.getJdbcTemplate().queryForList(MessageFormat.format(ls_Sql, ls_Index, ls_ItemId, String.valueOf(al_EntryID)));
					lb_Boolean = lo_Set.size() > 0;
					if (lb_Boolean == false) {// 不存在该状态
						ll_CurEntryID = -1;
					} else {
						if (MGlobal.readInt(lo_Set.get(0), "RecipientID") == lo_Para.UserID) {// 本人处理
							ll_CurEntryID = MGlobal.readInt(lo_Set.get(0), "EntryID");
						} else {
							// 岗位、岗位组、部门组
							ls_RoleUserIDs = TWorkAdmin.getUserRoleIDs(ao_Item.Logon, lo_Para.UserID);
							if (("," + ls_RoleUserIDs + ",").indexOf("," + MGlobal.readInt(lo_Set.get(0), "RecipientID") + ",") == -1) {
								if (MGlobal.readInt(lo_Set.get(0), "RUserID") == 0) {// 未处理情况
									// 代理别人处理或别人代理处理的公文
									if (MGlobal.readInt(lo_Set.get(0), "ProxyID") == 0) {// 代理别人处理
										if (("," + ls_UserIDs + ",").indexOf("," + MGlobal.readInt(lo_Set.get(0), "RecipientID") + ",") == -1) {
											ll_CurEntryID = -1; // 不存在代理
										} else {
											if (MGlobal.readInt(lo_Set.get(0), "StateID") == 0 || MGlobal.readInt(lo_Set.get(0), "StateID") == 1) {
												ll_CurEntryID = MGlobal.readInt(lo_Set.get(0), "EntryID"); // 可以代理处理
											} else {
												ll_CurEntryID = -1; // 不能代理处理
											}
										}
									} else {// 别人代理处理的公文
										if (MGlobal.readInt(lo_Set.get(0), "ProxyID") == lo_Para.UserID) {
											ll_CurEntryID = MGlobal.readInt(lo_Set.get(0), "EntryID"); // 别人代理处理
										} else {
											ll_CurEntryID = -1; // 非本人公文
										}
									}
								} else {// 别人代理岗位、岗位组、部门组处理的公文
									if (("," + ls_RoleUserIDs + ",").indexOf("," + MGlobal.readInt(lo_Set.get(0), "RUserID") + ",") == -1) {
										ll_CurEntryID = -1; // 不存在岗位、岗位组、部门组公文
									} else {
										ll_CurEntryID = MGlobal.readInt(lo_Set.get(0), "EntryID"); // 别人代理岗位、岗位组、部门组处理
									}
								}
							} else {
								// 岗位、岗位组、部门组公文
								if (MGlobal.readObject(lo_Set.get(0), "RUserID") == null) { // 未分配情况
									if (("," + ls_RoleUserIDs + ",").indexOf("," + MGlobal.readInt(lo_Set.get(0), "RecipientID") + ",") == -1) {
										ll_CurEntryID = -1; // 不存在岗位、岗位组、部门组公文
									} else {
										if (MGlobal.readInt(lo_Set.get(0), "StateID") == 0 || MGlobal.readInt(lo_Set.get(0), "StateID") == 1
												|| MGlobal.readInt(lo_Set.get(0), "StateID") == 7) {
											ll_CurEntryID = MGlobal.readInt(lo_Set.get(0), "EntryID"); // 可以代理岗位、岗位组、部门组处理
										} else {
											ll_CurEntryID = -1; // 不能代理岗位、岗位组、部门组处理
										}
									}
								} else { // 别人代理岗位、岗位组、部门组处理的公文
									if (("," + ls_RoleUserIDs + ",").indexOf("," + MGlobal.readInt(lo_Set.get(0), "RUserID") + ",") == -1) {
										ll_CurEntryID = -1; // 不存在岗位、岗位组、部门组公文
									} else {
										ll_CurEntryID = MGlobal.readInt(lo_Set.get(0), "EntryID"); // 别人代理岗位、岗位组、部门组处理
									}
								}
							}
						}
					}
					lo_Set = null;
				}
			}

			// 是否是打开转发的公文
			boolean lb_IsTransfer = false;
			if ((ll_CurEntryID == -1 && al_EntryID != -2) || ao_Item.intParameter == 104) {
				if (al_EntryID >= 0) {
					ls_Sql = " WHERE WorkItemID = {0} AND EntryID = {1} AND AcceptID = {2}";
					ls_Sql = MessageFormat.format(ls_Sql, ls_ItemId, String.valueOf(al_EntryID), ls_UserId);
					lb_IsTransfer = CSqlHandle.getValueBySql("SELECT '1' FROM WorkItemTransfer" + ls_Sql).equals("1");
					if (lb_IsTransfer)
						ll_CurEntryID = al_EntryID;

					ls_Sql = "UPDATE WorkItemTransfer SET TransStatus = 1, TransDate = ?" + ls_Sql + " AND TransStatus = 0";
					List parasList = new ArrayList();
					parasList.add(ld_SqlDate);
					CSqlHandle.getJdbcTemplate().update(ls_Sql, parasList.toArray());
				}
			}

			// 是否具有内部打开的管理权限
			if (ao_Item.getIsInterHandle() && ll_CurEntryID == -1) {
				ll_CurEntryID = 0;
			}

			// 是否打开公文链接/增加历史公文打开公文链接的权限
			if (ao_Item.intParameter == 102 || (ao_Item.intParameter == 103 && al_EntryID == -3)) {
				ll_CurEntryID = ao_Item.lngParameter;
			}

			if (ll_CurEntryID == -1) {// 判断是否是打开后继步骤
				ls_Sql = "SELECT '1' FROM WorkItemLookup WHERE WorkItemID = {0} AND LookupState = 1 AND UserID = {1}";
				ls_Sql = MessageFormat.format(ls_Sql, ls_ItemId, ls_UserId);
				if (CSqlHandle.getValueBySql(ls_Sql).equals("1")) {
					ll_CurEntryID = 0;
				}
			}

			if (ll_CurEntryID == -1) {
				// 错误【1032】：当前用户没有权限打开该篇公文
				ao_Item.Raise(1032, "openWorkItem", getWrkLogPara(al_WorkItemID, al_EntryID));
				return false;
			}

			// 判断是否是已经被撤回的公文
			ls_Sql = "SELECT '1' FROM EntryInst{0} WHERE StateID = 5 AND WorkItemID = {1} AND EntryID = {2}";
			ls_Sql = CSqlHandle.getValueBySql(MessageFormat.format(ls_Sql, ls_Index, ls_ItemId, String.valueOf(al_EntryID)));
			if (ls_Sql.equals("1")) { // 判断是否被打开过，如果没有被打开，则执行更新时间操作
				ls_Sql = "UPDATE EntryInst{0} SET FinishedDate = ? WHERE WorkItemID = ? AND EntryID = ? AND FinishedDate IS NOT NULL";
				final int dd = ll_CurEntryID;
				List parasList = new ArrayList();
				parasList.add(ld_SqlDate);
				parasList.add(al_WorkItemID);
				parasList.add(dd);
				CSqlHandle.getJdbcTemplate().update(ls_Sql, parasList.toArray());
				// 错误【1079】：当前公文已被撤回，您无法再打开公文查看
				ao_Item.Raise(1079, "openWorkItem", getWrkLogPara(al_WorkItemID, al_EntryID));
				return false;
			}

			int li_Type = 1;
			// 打开公文内容
			ls_Sql = "SELECT * FROM WorkItemInst{0} WHERE WorkItemID = {1}";
			lo_Set = CSqlHandle.getJdbcTemplate().queryForList(MessageFormat.format(ls_Sql, ls_Index, ls_ItemId));
			if (lo_Set != null && lo_Set.size() > 0) {
				ao_Item.Open(lo_Set.get(0), li_Type);
				lb_Boolean = true;
			}
			lo_Set = null;
			if (!lb_Boolean)
				return false;

			// 打开流转状态
			ls_Sql = "SELECT * FROM EntryInst{0} WHERE WorkItemID = {1} ORDER BY EntryID";
			lo_Set = CSqlHandle.getJdbcTemplate().queryForList(MessageFormat.format(ls_Sql, ls_Index, ls_ItemId));
			for (Map<String, Object> row : lo_Set) {
				CEntry lo_Entry = new CEntry(ao_Item.Logon);
				lo_Entry.WorkItem = ao_Item;
				lo_Entry.Open(row, li_Type);
				ao_Item.Entries.put(lo_Entry.EntryID, lo_Entry);
				if (lo_Entry.EntryID > ao_Item.EntryMaxID) {
					ao_Item.EntryMaxID = lo_Entry.EntryID;
				}
			}
			lo_Set = null;

			// 打开移动状态
			ls_Sql = "SELECT * FROM MoveEntryInst WHERE WorkItemID = {0} ORDER BY EntryID";
			lo_Set = CSqlHandle.getJdbcTemplate().queryForList(MessageFormat.format(ls_Sql, ls_ItemId));
			for (Map<String, Object> row : lo_Set) {
				CMoveEntry lo_MoveEntry = new CMoveEntry(ao_Item.Logon);
				lo_MoveEntry.Entry = ao_Item.getEntryById(MGlobal.readInt(row, "EntryID"));
				lo_MoveEntry.Entry.MoveEntry = lo_MoveEntry;
				lo_MoveEntry.Open(row, li_Type);
			}
			lo_Set = null;

			// 设置当前状态
			final CEntry lo_CurEntry = ao_Item.getEntryById(ll_CurEntryID == 0 ? 3 : ll_CurEntryID);
			ao_Item.CurEntry = lo_CurEntry;

			// 打开公文模板
			CCyclostyle lo_Cyclostyle = new CCyclostyle(ao_Item.Logon);
			ao_Item.Cyclostyle = lo_Cyclostyle;
			lo_Cyclostyle.WorkItem = ao_Item;
			TCyclostyle.openCyclostyleInst(lo_Cyclostyle, al_WorkItemID, lo_CurEntry.FlowID);
			ao_Item.CurFlow = lo_Cyclostyle.getFlowById(lo_CurEntry.FlowID);

			// 当前步骤对象
			CActivity lo_Act = lo_CurEntry.getActivity();
			ao_Item.CurActivity = lo_Act;
			if (ll_CurEntryID == 0 || al_EntryID == -2) {// 以管理者身份打开公文
				ao_Item.WorkItemAccess = false;
			} else {
				// intParameter = 101 - 判断是否是打开公文以前的版本
				// intParameter = 102 - 是否打开公文链接
				// intParameter = 103 - 是否打开历史公文
				// intParameter = 104 - 是否打开传阅公文
				if (ao_Item.intParameter > 100 && ao_Item.intParameter < 105) {
					ao_Item.WorkItemAccess = false;
				} else {
					int li_Status = lo_CurEntry.EntryStatus;
					lb_Boolean = true;
					if ((li_Status <= EEntryStatus.TransactingStatus || li_Status == EEntryStatus.TumbleInLaunchStatus)
							&& ao_Item.Status == EWorkItemStatus.WorkFlowing && lb_IsTransfer == false) {
						if (li_Status == EEntryStatus.TransactingStatus) {
							// 判断是否该篇公文已经正在被代理人处理
							if (lo_CurEntry.ProxyParticipantID == lo_Para.UserID) {
								ao_Item.WorkItemAccess = false;
								ao_Item.Information = "当前公文您已授权给 " + lo_CurEntry.Participant + " 处理[正在处理过程中]，您只能查看该公文";
								lb_Boolean = false;
							}
						}
					}

					if (lb_Boolean) {
						// 执行读取操作
						ls_Sql = MessageFormat.format("UPDATE EntryInst{0}", ls_Index)
								+ " SET StateID = 1, ReadDate = ? WHERE WorkItemID = ? AND EntryID = ? AND StateID = 0";
						List parasList = new ArrayList();
						parasList.add(ld_SqlDate);
						parasList.add(al_WorkItemID);
						parasList.add(lo_CurEntry.EntryID);
						CSqlHandle.getJdbcTemplate().update(ls_Sql, parasList.toArray());

						if (lo_Act != null) {
							// 通知步骤类型所对应的公文不需要处理
							if (lo_Act.ActivityType == EActivityType.FYIActivity) {
								if (lo_CurEntry.ParticipantID == lo_Para.UserID) {
									ls_Sql = MessageFormat.format("UPDATE EntryInst{0}", ls_Index)
											+ " SET StateID = 3, FinishedDate = ? WHERE WorkItemID = ? AND EntryID = ?";
								} else {
									ls_Sql = "UPDATE EntryInst{0} SET StateID = 3, FinishedDate = ?, "
											+ "ProxyID = {1}, Proxyor = '{2}' WHERE WorkItemID = ? AND EntryID = ?";
									ls_Sql = MessageFormat.format(ls_Sql, ls_Index, ls_UserId, lo_Para.UserName);
								}

								CSqlHandle.getJdbcTemplate().update(ls_Sql, parasList.toArray());

								updateInst(ao_Item, al_WorkItemID);
								lo_CurEntry.setEntryStatus(EEntryStatus.HadTransactStatus);
								ao_Item.WorkItemAccess = false;
							} else {
								// 判断当前步骤是否需要按顺序处理
								lb_Boolean = true;
								if (lo_Act.ActivityType == EActivityType.TransactActivity) {
									if (lo_Act.Transact.OrderBy > ETransOrderByType.NotAndOrder) {
										ao_Item.Information = "";
										String ls_Info = "";
										for (CEntry lo_Entry : lo_CurEntry.ParentEntry.ChildEntries) {
											if (lo_Entry.EntryID < lo_CurEntry.EntryID) {
												if (lo_Entry.EntryStatus < EEntryStatus.HadTransactStatus) {
													ls_Info += lo_Entry.Participant + "、";
												}
											}
										}
										if (!ls_Info.equals("")) {
											ls_Info = ls_Info.substring(0, ls_Info.length() - 1);
											ao_Item.Information = "当前公文需要按顺序处理，而处理人员【" + ls_Info + "】还未处理，您需要等他们处理完成后接着处理";
											ao_Item.WorkItemAccess = false;
											lb_Boolean = false;
										}
									}
								}
							}

							if (lb_Boolean) {
								// 添加公文锁
								if (lo_CurEntry.getIsInterEntry()) {
									ao_Item.WorkItemAccess = TWorkAdmin.addLock(ao_Item.Logon, al_WorkItemID, ll_CurEntryID, 0,
											ELockType.IntercurrentLock);
								} else {
									ao_Item.WorkItemAccess = TWorkAdmin.addLock(ao_Item.Logon, al_WorkItemID, ll_CurEntryID, 0,
											ELockType.RepellentLock);
								}

								if (ao_Item.WorkItemAccess) {
									// 增加公文针对没有意见选项又被撤回的情况恢复成原始参数
									switch (ao_Item.CurActivity.ActivityType) {
									case EActivityType.StartActivity:
									case EActivityType.TransactActivity:
										if (ao_Item.CurActivity.Transact.getChoicesCount() == 0) {
											ao_Item.CurEntry.setChoice(-2);
										}
										break;
									case EActivityType.TumbleInActivity:
										break;
									case EActivityType.LaunchActivity:
										break;
									default:
										break;
									}
								} else {
									ls_Sql = "SELECT UserID, UserName FROM WorkItemLock WHERE WorkItemID = " + ls_ItemId;
									lb_Boolean = false;
									lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
									String ls_Info = ao_Item.Information;
									for (Map<String, Object> row : lo_Set) {
										if (MGlobal.readInt(row, "UserID") == lo_Para.UserID)
											lb_Boolean = true;
										ls_Info += MGlobal.readString(row, "UserName") + "、";
									}
									lo_Set = null;

									if (!ls_Info.equals("")) {
										ls_Info = ls_Info.substring(0, ls_Info.length() - 1);
										ls_Info = "当前公文正被 " + ls_Info + " 打开处理，您只能以只读方式打开处理";
										if (lb_Boolean) {
											ls_Info += "\n注意：您不能对同一篇可处理的公文打开两次同时处理，" + "如果由于意外情况结束公文而保留公文锁无法让您继续处理的话，" + "请在[我的公文锁]中先删除后再打开该公文继续处理。";
										}
									}
									ao_Item.Information = ls_Info;
								}
							}
						} else {
							ao_Item.WorkItemAccess = false;
							// 处理系统帮忙处理完成的状态
							if (lo_CurEntry.EntryStatus == EEntryStatus.SystemTransactStatus && ao_Item.getIsInterHandle() == false) {
								// 执行状态信息更改操作
								lo_CurEntry.setEntryStatus(EEntryStatus.HadTransactStatus);
								ls_Sql = "UPDATE EntryInst" + ls_Index + " Set StateID = 3, ReadDate = ? WHERE WorkItemID = ? AND EntryID = ?";

								CSqlHandle.getJdbcTemplate().update(ls_Sql, parasList.toArray());
								ao_Item.Information = "当前公文在被其他人处理时同时处理过，您只能查看该公文信息";
							}
						}
					}
				}

				// 人工监督管理的集合
				ls_Sql = "SELECT * FROM UserSupervise WHERE WorkItemID = " + ls_ItemId;
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				for (Map<String, Object> row : lo_Set) {
					CSupervise lo_Supervise = new CSupervise(ao_Item.Logon);
					lo_Supervise.WorkItem = ao_Item;
					lo_Supervise.Open(row, li_Type);
					if (ao_Item.SuperviseMaxID < lo_Supervise.SuperviseID)
						ao_Item.SuperviseMaxID = lo_Supervise.SuperviseID;
					ao_Item.Supervises.add(lo_Supervise);
				}
				lo_Set = null;

				ls_Sql = "SELECT * FROM SuperviseUser WHERE WorkItemID = " + ls_ItemId;
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				for (Map<String, Object> row : lo_Set) {
					CSuperUser lo_SuperUser = new CSuperUser(ao_Item.Logon);
					lo_SuperUser.Supervise = ao_Item.getSuperviseById(MGlobal.readInt(row, "SuperviseID"));
					lo_SuperUser.Open(row, li_Type);
					lo_SuperUser.Supervise.SuperUsers.add(lo_SuperUser);
				}
				lo_Set = null;

				// 特殊步骤设置
				if (lo_Act != null) {
					if (lo_Act.ActivityType == EActivityType.TransactActivity) {// 特殊步骤是处理步骤的一种
						String ls_Name = lo_Act.ActivityName;
						if (ls_Name.equals(ao_Item.CurFlow.PreActivityName)) {
							ao_Item.TransType = ECurActivityTransType.SendPreTransType;
						} else if (ls_Name.equals(ao_Item.CurFlow.NextActivityName)) {
							ao_Item.TransType = ECurActivityTransType.SendNextTransType;
						} else if (ls_Name.equals(ao_Item.CurFlow.SpecialActivityName)) {
							ao_Item.TransType = ECurActivityTransType.SpecialTransType;
						} else {
							ao_Item.TransType = ECurActivityTransType.CommondTransType;
						}
						if (ao_Item.TransType != ECurActivityTransType.CommondTransType) {
							ao_Item.TransActivityID = lo_CurEntry.getOrginalEntry().ActivityID;
						}
					}
				}

				// 当前权限（由本步骤所拥有的所有权限组成）
				CRight lo_Right = null;
				if (ll_CurEntryID == 0) { // 管理员打开
					lo_Right = createCurrentRight(ao_Item, "1");
				} else if (lo_Act == null) {
					if (lo_Cyclostyle.FlowFlag == EWorkItemFlowFlag.FreeFlowRule) {
						lo_Right = createCurrentRight(ao_Item, String.valueOf(lo_CurEntry.ActivityID));
					} else {
						lo_Right = createCurrentRight(ao_Item, "2"); // 开始
					}
				} else {
					if (lo_Act.ActivityID == 1) {// 开始步骤
						// 如果当前步骤为某一个嵌入步骤流转后所得的结果，则该步骤的权限取得的是嵌入步骤的权限
						if (lo_CurEntry.ParentEntry.ParentEntry.EntryID == 1) {
							lo_Right = createCurrentRight(ao_Item, "2"); // 开始
						} else {
							CEntry lo_Entry = ao_Item.getRootEntry(lo_CurEntry);
							lo_Right = createCurrentRight(ao_Item, lo_Entry.getActivity().TumbleIn.getUserRight());
						}
					} else {
						if (lo_Act.ActivityType == EActivityType.FYIActivity) {
							lo_Right = createCurrentRight(ao_Item, lo_Act.FYI.getUserRightName());
						} else {
							if (ao_Item.TransType == ECurActivityTransType.CommondTransType) {
								if (lo_Act.ActivityType == EActivityType.TumbleInActivity) {
									lo_Right = createCurrentRight(ao_Item, lo_Act.TumbleIn.getUserRightName());
								} else if (ao_Item.CurActivity.ActivityType == EActivityType.TransactActivity) {
									lo_Right = createCurrentRight(ao_Item, lo_Act.Transact.getUserRight());
								} else {
									lo_Right = createCurrentRight(ao_Item, "1");
								}
							} else {
								CActivity lo_Activity = ao_Item.CurFlow.getActivityById(ao_Item.TransActivityID);
								if (ao_Item.TransType == ECurActivityTransType.SendPreTransType) {
									lo_Right = createCurrentRight(ao_Item, lo_Activity.Transact.getNewSendRightName());
								} else if (ao_Item.TransType == ECurActivityTransType.SendNextTransType) {
									lo_Right = createCurrentRight(ao_Item, lo_Activity.Transact.getNewSendRightName());
								} else { // ao_Item.TransType ==
											// ECurActivityTransType.SpecialTransType
									ao_Item.CurRight = createCurrentRight(ao_Item, lo_Activity.Transact.getSpecialRight());
								}
							}
						}
					}
				}
				ao_Item.CurRight = lo_Right;

				// 权限实例化套用
				if (rightToInst(ao_Item) == false)
					return false;

				// 取得动态修改的表头属性和角色
				importPropRoleValue(ao_Item);

				// 打开时计算[针对所有情况]
				caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuOpenForAllReadOnly));

				if (ao_Item.WorkItemAccess && ao_Item.CurEntry.EntryStatus < EEntryStatus.TransactingStatus) {
					// 打开时----调用计算函数[二次开发]
					caculateScript(ao_Item, ECaculateStatusType.CaculateOpenStatus);

					// 打开时计算
					caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuOpenForAll));
					if (ao_Item.CurEntry.isFirstTrans()) {
						caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuOpenForFirst));
					}
				}

				// 在打开时计算 - 任意时候
				caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuOpenAtAnyStatus));

				// 设置替换代理情况
				if (ao_Item.WorkItemAccess) {
					if (lo_CurEntry.ParticipantID == lo_Para.UserID) {
						if (lo_CurEntry.EntryStatus < EEntryStatus.TransactingStatus || lo_CurEntry.EntryStatus == EEntryStatus.TumbleInLaunchStatus) {
							lo_CurEntry.ProxyParticipantID = 0;
							lo_CurEntry.ProxyParticipant = "";
						}
					} else {
						ls_Sql = "SELECT UserType FROM UserInfo WHERE UserID = " + String.valueOf(lo_CurEntry.ParticipantID);
						int li_Value = Integer.parseInt(CSqlHandle.getValueBySql(ls_Sql));
						if (li_Value == 0) { // 代理处理
							lo_CurEntry.ProxyParticipantID = lo_CurEntry.ParticipantID;
							lo_CurEntry.ProxyParticipant = lo_CurEntry.Participant;
							lo_CurEntry.ParticipantID = lo_Para.UserID;
							lo_CurEntry.Participant = lo_Para.UserName;
						} else { // 岗位、岗位组、部门组
							switch (li_Value) {
							case 2:
								lo_CurEntry.RecipientType = EEntryRecipientType.PostEntryRecipient;
								break;
							case (short) 3:
								lo_CurEntry.RecipientType = EEntryRecipientType.PostEntryRecipient;
								break;
							case (short) 4:
								lo_CurEntry.RecipientType = EEntryRecipientType.DeptGroupEntryRecipient;
								break;
							}
							lo_CurEntry.PostParticipantID = lo_CurEntry.ParticipantID;
							lo_CurEntry.PostParticipant = lo_CurEntry.Participant;
							lo_CurEntry.ParticipantID = lo_Para.UserID;
							lo_CurEntry.Participant = lo_Para.UserName;
						}
					}
				}
			}

			ao_Item.CurEntry.EntryChanged = true;
			return true;
		} catch (Exception ex) {
			ao_Item.Raise(ex, "TWorkItem->openWorkItem", getWrkLogPara(al_WorkItemID, al_EntryID));
			return false;
		}
	}

	/**
	 * 公文更新时间
	 * 
	 * @param al_WorkItemID
	 *            公文标识
	 * @return
	 * @throws Exception
	 */
	private static void updateInst(final CWorkItem ao_Item, final int al_WorkItemID) throws Exception {
		String ls_Sql = "UPDATE WorkItemInst SET LastUpdateType = 1, LastUpdateDate = ? WHERE WorkItemStatus = 0 AND WorkItemID = ?";
		List parasList = new ArrayList();
		parasList.add(MGlobal.getSqlTimeNow());
		parasList.add(al_WorkItemID == 0 ? ao_Item.WorkItemID : al_WorkItemID);
		CSqlHandle.getJdbcTemplate().update(ls_Sql, parasList.toArray());
	}

	/**
	 * 插入状态
	 * 
	 * @param ao_Item
	 *            公文实例对象
	 * @param ai_EntryType
	 *            状态类型
	 * @param ao_Activity
	 *            对应步骤对象
	 * @param ao_Right
	 *            权限对象
	 * @param ao_ParentEntry
	 *            父状态对象
	 * @return
	 */
	private static CEntry insertEntry(CWorkItem ao_Item, int ai_EntryType, CActivity ao_Activity, CRight ao_Right, CEntry ao_ParentEntry) {
		try {
			CEntry lo_Entry = new CEntry(ao_Item.Logon);
			lo_Entry.WorkItem = ao_Item;
			ao_Item.EntryMaxID++;
			lo_Entry.EntryID = ao_Item.EntryMaxID;
			lo_Entry.setEntryType(ai_EntryType);
			if (ao_Item.CurEntry == null) {
				lo_Entry.OrginalID = 0;
			} else {
				lo_Entry.OrginalID = ao_Item.CurEntry.EntryID;
			}

			int li_FlowFlag = ao_Item.Cyclostyle.FlowFlag;
			Date ld_Date = MGlobal.getNow();

			if (ao_Activity != null
					&& ((li_FlowFlag & EWorkItemFlowFlag.OrderFlowRule) == EWorkItemFlowFlag.OrderFlowRule || (li_FlowFlag & EWorkItemFlowFlag.ConditionFlowRule) == EWorkItemFlowFlag.ConditionFlowRule)) {
				lo_Entry.ActivityID = ao_Activity.ActivityID;
				lo_Entry.ActivityName = ao_Activity.ActivityName;
				lo_Entry.FlowID = ao_Activity.Flow.FlowID;
				lo_Entry.setInceptDate(ld_Date);
				lo_Entry.RemindAddFlag = ao_Activity.RemindAddFlag;
				lo_Entry.MobileType = ao_Activity.getMobileType();

				// 拟稿人
				CRole lo_Role = ao_Item.Cyclostyle.getRoleById(1);
				if (lo_Entry.EntryType == EEntryType.ActualityEntry) {
					switch (ao_Activity.ActivityType) {
					case EActivityType.StartActivity:
						lo_Entry.setParticipantID2(ao_Item.CreatorID);
						lo_Entry.Participant = ao_Item.Creator;
						lo_Entry.setParameter1(ao_Activity.Transact.Parameter1);
						lo_Entry.setParameter2(ao_Activity.Transact.Parameter2);
						// 设置拟稿人信息
						lo_Entry.RoleID = lo_Role.RoleID;
						lo_Entry.RoleName = lo_Role.RoleRange;
						break;

					case EActivityType.TransactActivity:
						CDueAlert lo_DueAlert = ao_Activity.Transact.DueAlert;
						boolean lb_Boolean = false,
						lb_Bool;
						if (ao_Activity.Transact.UseTimeLimit) {
							switch (lo_DueAlert.Type) {
							case EDueAlertType.AutoDueAlertType:
								lb_Boolean = true;
								break;
							case EDueAlertType.UserDueAlertType:
								if (lo_DueAlert.Type == EDueAlertType.AutoUserDueAlertType) {
									lb_Boolean = true;
								} else {
									lb_Boolean = (ao_Activity.Transact.DueAlertType == EDueAlertType.AutoDueAlertType || ao_Activity.Transact.DueAlertType == EDueAlertType.AutoUserDueAlertType);
								}
								break;
							case EDueAlertType.AutoUserDueAlertType:
								lb_Boolean = true;
								break;
							default: // NotAnyDueAlertType
								break;
							}
						}
						if (lb_Boolean) {
							lb_Bool = true;
							switch (lo_DueAlert.DueFlag) {
							case ETimelimitDueFlag.TimelimitDueAbs:
								lo_Entry.setOverdueDate(lo_DueAlert.DueBy);
								break;
							case ETimelimitDueFlag.TimelimitDueBy:
								lo_Entry.setOverdueDate(ao_Item.PublicFunc().getDateAfterWorks(ld_Date, 0, lo_DueAlert.DueWithin,
										lo_DueAlert.CaculateType ^ 7));
								break;
							case ETimelimitDueFlag.TimelimitDueProp:
								lb_Bool = false;
								String ls_Value = ao_Item.Cyclostyle.getPropByName(lo_DueAlert.DuePropName).getValue();
								if (!MGlobal.isEmpty(ls_Value)) {
									if (MGlobal.stringToDate(ls_Value).getTime() > ld_Date.getTime()) {
										lo_Entry.setOverdueDate(MGlobal.stringToDate(ls_Value));
										lb_Bool = true;
									}
								}
								break;
							default: // TimelimitDueNone
								lb_Bool = false;
								break;
							}
							if (lb_Bool) {
								lo_Entry.DueTransactType = lo_DueAlert.DueTransactType;
								lo_Entry.setDueUserNumber(lo_DueAlert.DueUserNumber);
							}

							lb_Bool = true;
							switch (lo_DueAlert.AlertFlag) {
							case ETimelimitAlertFlag.TimelimitAlertAbs:
								lo_Entry.AlterDate = lo_Entry.AlterDate;
								break;
							case ETimelimitAlertFlag.TimelimitAlertBy:
								lo_Entry.AlterDate = ao_Item.PublicFunc().getDateAfterWorks(ld_Date, 0, lo_DueAlert.DueWithin,
										lo_DueAlert.CaculateType ^ 7);
								break;
							case ETimelimitAlertFlag.TimelimitAlertProp:
								lb_Bool = false;
								String ls_Value = ao_Item.Cyclostyle.getPropByName(lo_DueAlert.AlertPropName).getValue();
								if (!ls_Value.equals("")) {
									if (MGlobal.stringToDate(ls_Value).getTime() > ld_Date.getTime()) {
										lo_Entry.AlterDate = MGlobal.stringToDate(ls_Value);
										lb_Bool = true;
									}
								}
								break;
							default: // TimelimitAlertNone
								lb_Bool = false;
								break;
							}
							if (lb_Bool) {
								lo_Entry.AlterInteval = lo_DueAlert.Interval;
								lo_Entry.setAlertMaxNumber(lo_DueAlert.AlertMaxNumber);
								if ((lo_DueAlert.AlertType & ETimelimitAlertToType.TimelimitAlertToOther) == ETimelimitAlertToType.TimelimitAlertToOther) {
									lo_Entry.setAlterUsers("[" + String.valueOf(lo_DueAlert.AlertType) + "]"
											+ ao_Item.Logon.getUserAdmin().convertUsers(2, 1, lo_DueAlert.AlertTo));
								} else {
									lo_Entry.setAlterUsers("[" + String.valueOf(lo_DueAlert.AlertType) + "]");
								}
							}
						}
						lo_Entry.setParameter1(ao_Activity.Transact.Parameter1);
						lo_Entry.setParameter2(ao_Activity.Transact.Parameter2);

						appendMoveEntry(lo_Entry);
						break;
					case EActivityType.FYIActivity:
						lo_Entry.setParameter1(ao_Activity.FYI.Parameter1);
						lo_Entry.setParameter2(ao_Activity.FYI.Parameter2);
						break;
					case EActivityType.TumbleInActivity:
						lo_Entry.setEntryStatus(EEntryStatus.TumbleInLaunchStatus);
						break;
					case EActivityType.LaunchActivity:
						lo_Entry.setEntryStatus(EEntryStatus.TumbleInLaunchStatus);
						break;
					default: // NotAnyActivity, SplitActivity,
								// NotLimitedActivity
						break;
					}
				}
			}

			if (ao_Right != null && (li_FlowFlag & EWorkItemFlowFlag.FreeFlowRule) == EWorkItemFlowFlag.FreeFlowRule) {
				if (ao_Activity == null) {
					lo_Entry.ActivityID = ao_Right.RightID;
					lo_Entry.ActivityName = ao_Right.RightName;
					lo_Entry.FlowID = 0;
				}
				lo_Entry.setInceptDate(ld_Date);
				if (ao_Right.RightType == ERightType.StartRightType) {
					lo_Entry.setParticipantID2(ao_Item.GlobalPara().UserID);
					lo_Entry.Participant = ao_Item.GlobalPara().UserName;
					// 拟稿人
					CRole lo_Role = ao_Item.Cyclostyle.getRoleById(1);
					lo_Entry.RoleID = lo_Role.RoleID;
					lo_Entry.RoleName = lo_Role.RoleName;
				}
			}

			ao_Item.Entries.put(lo_Entry.EntryID, lo_Entry);

			if (ao_ParentEntry != null) {
				lo_Entry.setParentID(ao_ParentEntry.EntryID);
			}

			lo_Entry.EntryChanged = true;

			return lo_Entry;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 增加移动短信审批
	 * 
	 * @param ao_Entry
	 *            状态对象
	 * @throws Exception
	 */
	private static void appendMoveEntry(CEntry ao_Entry) throws Exception {
		if (ao_Entry.getActivity().ActivityType != EActivityType.TransactActivity)
			return;
		if ((ao_Entry.MobileType & 3) == 0)
			return;

		ao_Entry.MoveEntry = new CMoveEntry(ao_Entry.Logon);
		ao_Entry.MoveEntry.Entry = ao_Entry;
		String ls_Texts = ao_Entry.getActivity().Transact.ResponseChoices;
		String ls_Comment = "";

		int i = 0;
		if (!MGlobal.isEmpty(ls_Texts)) {
			String[] ls_Array = ls_Texts.split(";");
			for (int j = 0; j < ls_Array.length; j++) {
				if (!MGlobal.isEmpty(ls_Array[j])) {
					i++;
					ls_Comment += " " + String.valueOf(i) + "-" + ls_Array[j];
				}
			}
		}

		ao_Entry.MoveEntry.Flag = ao_Entry.MobileType;
		if ((ao_Entry.MoveEntry.Flag & 1) == 1) {
			CEntry lo_Orginal = ao_Entry.getOrginalEntry();
			ao_Entry.MoveEntry.SmsMessage = getSmsCommentTitle(ao_Entry.WorkItem, (i == 0 ? 0 : 1), lo_Orginal.Participant, lo_Orginal.FinishedDate,
					ls_Comment);
		}
		if ((ao_Entry.MoveEntry.Flag & 2) == 2) {
			// .PdaContent
		}
		ao_Entry.MoveEntry.State = 0;
		ao_Entry.MoveEntry.SendDate = MGlobal.getNow();
		ao_Entry.MoveEntry.CommentCount = i;
		ao_Entry.MoveEntry.CommentIndex = 0;
	}

	/**
	 * 获取移动短信审批标题
	 * 
	 * @param ai_Type
	 * @param as_Participant
	 * @param ad_FinishedDate
	 * @param as_Comment
	 * @return
	 * @throws Exception
	 */
	private static String getSmsCommentTitle(CWorkItem ao_Item, int ai_Type, String as_Participant, Date ad_FinishedDate, String as_Comment)
			throws Exception {
		String ls_Format = ao_Item.Logon.getParaValue("SMS_COMMENT_TF" + ai_Type + "_" + ao_Item.Cyclostyle.getAutoCyclostyleCode());
		if (ls_Format.equals("")) {
			ls_Format = ao_Item.Logon.getParaValue("SMS_COMMENT_TITLE_FORMAT" + ai_Type);
		}
		if (ls_Format.equals("")) {
			if (ai_Type == 0) {
				ls_Format = "[发送人]在[发送时间]发送给您的文件[标题]，请直接回复您对这个文件的处理意见，谢谢！";
			} else {
				ls_Format = "[发送人]在[发送时间]发送给您的文件[标题]，请回复[意见]，如：1-已阅，请相关业务部门协助办理，...";
			}
		}
		String ls_Text = ls_Format.replaceAll("[发送人]", as_Participant);
		ls_Text = ls_Text.replaceAll("[发送时间]", MGlobal.dateToString(ad_FinishedDate));
		ls_Text = ls_Text.replaceAll("[标题]", ao_Item.WorkItemName);
		ls_Text = ls_Text.replaceAll("[意见]", as_Comment);

		// 替换表头属性
		int i = ls_Format.indexOf("[P") + 1;
		while (i > 0) {
			int j = ls_Format.indexOf("]", i) + 1;
			if (j > i)
				ls_Text = ls_Text.replaceAll(ls_Format.substring(i, j), ao_Item.getPropValue(ls_Format.substring(i + 2, j)));
			i = ls_Format.indexOf("[P", i + 1);
		}

		// 替换字段1-10
		for (int k = 1; k <= 10; k++) {
			if (ls_Format.indexOf("[字段" + String.valueOf(k) + "]") > -1)
				ls_Text = ls_Text.replaceAll("[字段" + String.valueOf(k) + "]", ao_Item.getExtendValue(String.valueOf(k)).toString());
		}
		// 替换扩展字段F11-5/FSA1-5/FSB1-5
		for (int k = 1; k <= 5; k++) {
			if (ls_Format.indexOf("[FI" + String.valueOf(k) + "]") > -1)
				ls_Text = ls_Text.replaceAll("[FI" + String.valueOf(k) + "]", ao_Item.getExtendValue("FI" + String.valueOf(k)).toString());
			if (ls_Format.indexOf("[FSA" + String.valueOf(k) + "]") > -1)
				ls_Text = ls_Text.replaceAll("[FSA" + String.valueOf(k) + "]", ao_Item.getExtendValue("FSA" + String.valueOf(k)).toString());
			if (ls_Format.indexOf("[FSB" + String.valueOf(k) + "]") > -1)
				ls_Text = ls_Text.replaceAll("[FSB" + String.valueOf(k) + "]", ao_Item.getExtendValue("FSB" + String.valueOf(k)).toString());
		}
		return ls_Text;
	}

	// 生成权限
	// as_RightIDs 权限标识
	private static CRight createCurrentRight(CWorkItem ao_Item, String as_RightIDs) {
		try {
			CRight lo_CurRight = new CRight(ao_Item.Logon);
			lo_CurRight.Cyclostyle = ao_Item.Cyclostyle;
			boolean lb_Boolean = false;
			int li_Type = 0;

			for (CRight lo_Right : ao_Item.Cyclostyle.Rights) {
				if ((";" + as_RightIDs + ";").indexOf(";" + String.valueOf(lo_Right.RightID) + ";") > -1
						|| (lo_Right.RightID == 1 && MGlobal.isEmpty(as_RightIDs))) {
					if (lb_Boolean) {
						// 正文权限比较顺序：BodyDisVisible < BodyLastVersion <
						// BodyReadOnly < BodyLastReadWrite < BodyReadWrite
						switch (lo_CurRight.BodyAccess) {
						case EBodyAccessType.BodyDisVisible:
							lo_CurRight.BodyAccess = lo_Right.BodyAccess;
							break;
						case EBodyAccessType.BodyReadOnly:
							if (lo_Right.BodyAccess > EBodyAccessType.BodyReadOnly & lo_Right.BodyAccess != EBodyAccessType.BodyLastVersion) {
								lo_CurRight.BodyAccess = lo_Right.BodyAccess;
							}
							break;
						case EBodyAccessType.BodyReadWrite:
							break;
						// 最大权限，不需要修改
						case EBodyAccessType.BodyLastVersion:
							if (lo_Right.BodyAccess > EBodyAccessType.BodyDisVisible) {
								lo_CurRight.BodyAccess = lo_Right.BodyAccess;
							}
							break;
						case EBodyAccessType.BodyLastReadWrite:
							if (lo_Right.BodyAccess == EBodyAccessType.BodyReadWrite) {
								lo_CurRight.BodyAccess = lo_Right.BodyAccess;
							}
							break;
						}

						if (lo_Right.BodyAccess == EBodyAccessType.BodyLastVersion) {
							if (lo_CurRight.BodyAccess == EBodyAccessType.BodyDisVisible) {
								lo_CurRight.BodyAccess = lo_Right.BodyAccess;
							}
						} else {
							if (lo_CurRight.BodyAccess < lo_Right.BodyAccess) {
								lo_CurRight.BodyAccess = lo_Right.BodyAccess;
							}
						}
						if (lo_CurRight.DocumentAccess < lo_Right.DocumentAccess) {
							lo_CurRight.DocumentAccess = lo_Right.DocumentAccess;
						}
						if (lo_CurRight.FlowAccess < lo_Right.FlowAccess) {
							lo_CurRight.FlowAccess = lo_Right.FlowAccess;
						}
						lo_CurRight.setVisbleFormIDs(pileUpRights(lo_CurRight.getVisbleFormIDs(), lo_Right.getVisbleFormIDs()));
						lo_CurRight.PropOpenAccesses = pileUpRights(lo_CurRight.PropOpenAccesses, lo_Right.PropOpenAccesses);
						lo_CurRight.PropSendAccesses = pileUpRights(lo_CurRight.PropSendAccesses, lo_Right.PropSendAccesses);
						lo_CurRight.PropSaveAccesses = pileUpRights(lo_CurRight.PropSaveAccesses, lo_Right.PropSaveAccesses);
						lo_CurRight.PropSendAfterAccesses = pileUpRights(lo_CurRight.PropSendAfterAccesses, lo_Right.PropSendAfterAccesses);
						lo_CurRight.RoleOpenAccesses = pileUpRights(lo_CurRight.RoleOpenAccesses, lo_Right.RoleOpenAccesses);
						lo_CurRight.RoleSendAccesses = pileUpRights(lo_CurRight.RoleSendAccesses, lo_Right.RoleSendAccesses);
						lo_CurRight.RoleSaveAccesses = pileUpRights(lo_CurRight.RoleSaveAccesses, lo_Right.RoleSaveAccesses);
						lo_CurRight.RoleSendAfterAccesses = pileUpRights(lo_CurRight.RoleSendAfterAccesses, lo_Right.RoleSendAfterAccesses);
						lo_CurRight.setCellDisVisibleIDs(pileUpRights(lo_CurRight.getCellDisVisibleIDs(), lo_Right.getCellDisVisibleIDs()));
						lo_CurRight.setCellNeedHandleIDs(pileUpRights(lo_CurRight.getCellNeedHandleIDs(), lo_Right.getCellNeedHandleIDs()));
						lo_CurRight.setCellValidHandleIDs(pileUpRights(lo_CurRight.getCellValidHandleIDs(),
								weakenRights(lo_Right.getCellValidHandleIDs(), lo_CurRight.getCellNeedHandleIDs())));
						lo_CurRight.setCellReadOnlyIDs(pileUpRights(
								lo_CurRight.getCellReadOnlyIDs(),
								weakenRights(lo_Right.getCellReadOnlyIDs(),
										pileUpRights(lo_CurRight.getCellValidHandleIDs(), lo_CurRight.getCellNeedHandleIDs()))));
						if (lo_CurRight.CommentAccess < lo_Right.CommentAccess) {
							lo_CurRight.CommentAccess = lo_Right.CommentAccess;
						}
						for (li_Type = EScriptCaculateType.CacuOpenForAllReadOnly; li_Type <= EScriptCaculateType.CacuOpenClientAnyStatus; li_Type++) {
							lo_CurRight.setScriptCacuContent(li_Type,
									lo_CurRight.getScriptCacuContent(li_Type) + lo_Right.getScriptCacuContent(li_Type));
						}
					} else {
						lo_CurRight.BodyAccess = lo_Right.BodyAccess;
						lo_CurRight.DocumentAccess = lo_Right.DocumentAccess;
						lo_CurRight.FlowAccess = lo_Right.FlowAccess;
						lo_CurRight.setVisbleFormIDs(lo_Right.getVisbleFormIDs());
						lo_CurRight.PropOpenAccesses = lo_Right.PropOpenAccesses;
						lo_CurRight.PropSendAccesses = lo_Right.PropSendAccesses;
						lo_CurRight.PropSaveAccesses = lo_Right.PropSaveAccesses;
						lo_CurRight.PropSendAfterAccesses = lo_Right.PropSendAfterAccesses;
						lo_CurRight.RoleOpenAccesses = lo_Right.RoleOpenAccesses;
						lo_CurRight.RoleSendAccesses = lo_Right.RoleSendAccesses;
						lo_CurRight.RoleSaveAccesses = lo_Right.RoleSaveAccesses;
						lo_CurRight.PropSendAfterAccesses = lo_Right.RoleSendAfterAccesses;
						lo_CurRight.setCellDisVisibleIDs(lo_Right.getCellDisVisibleIDs());
						lo_CurRight.setCellNeedHandleIDs(lo_Right.getCellNeedHandleIDs());
						lo_CurRight.setCellValidHandleIDs(lo_Right.getCellValidHandleIDs());
						lo_CurRight.setCellReadOnlyIDs(lo_Right.getCellReadOnlyIDs());
						lo_CurRight.CommentAccess = lo_Right.CommentAccess;
						for (li_Type = EScriptCaculateType.CacuOpenForAllReadOnly; li_Type <= EScriptCaculateType.CacuOpenClientAnyStatus; li_Type++) {
							lo_CurRight.setScriptCacuContent(li_Type, lo_Right.getScriptCacuContent(li_Type));
						}
						lb_Boolean = true;
					}
				}
			}

			// 正文权限特殊设置
			if (lo_CurRight.BodyAccess == EBodyAccessType.BodyLastVersion) {
				if (ao_Item.Cyclostyle.HaveBody) {
					if (ao_Item.Cyclostyle.Body.DocumentType != EDocumentType.WordVersionBody) {
						lo_CurRight.BodyAccess = EBodyAccessType.BodyReadOnly;
					}
				}
			}

			return lo_CurRight;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 权限实例化套用
	 * 
	 * @return
	 * @throws Exception
	 */
	private static boolean rightToInst(CWorkItem ao_Item) throws Exception {
		try {
			if (ao_Item.CurRight == null)
				return false;

			// 正文权限[设置读写属性]
			if (ao_Item.Cyclostyle.HaveBody) {
				ao_Item.Cyclostyle.Body.RightType = ao_Item.CurRight.BodyAccess;
				if (!ao_Item.WorkItemAccess) {
					if (ao_Item.Cyclostyle.Body.RightType > EDocumentRightType.ReadOnlyDocument) {
						ao_Item.Cyclostyle.Body.RightType = EDocumentRightType.ReadOnlyDocument;
					}
				}
			}

			// 附件权限[设置读写属性]
			for (CDocument lo_Document : ao_Item.Cyclostyle.Documents) {
				lo_Document.RightType = ao_Item.CurRight.DocumentAccess;
				if (!ao_Item.WorkItemAccess) {
					if (lo_Document.RightType > EDocumentRightType.ReadOnlyDocument) {
						lo_Document.RightType = EDocumentRightType.ReadOnlyDocument;
					}
				}
			}

			String ls_Value = "";
			int li_Type;

			// 表单权限[设置可见性]
			String ls_DisVisibleIDs = ";" + ao_Item.CurRight.getCellDisVisibleIDs();
			String ls_ValidHandleIDs = ";" + ao_Item.CurRight.getCellValidHandleIDs();
			String ls_NeedHandleIDs = ";" + ao_Item.CurRight.getCellNeedHandleIDs();
			for (CForm lo_Form : ao_Item.Cyclostyle.Forms) {
				lo_Form.Access = ((";" + ao_Item.CurRight.getVisbleFormNames()).indexOf(";" + lo_Form.FormName + ";") + 1 == 0) ? EFormAccessType.FormDisVisible
						: EFormAccessType.FormVisble;
				if (lo_Form.Access == EFormAccessType.FormVisble) {
					for (CFormCell lo_Cell : lo_Form.FormCells) {
						ls_Value = ";" + lo_Cell.CellID + ";";
						if (ls_DisVisibleIDs.indexOf(ls_Value) + 1 > 0) {
							lo_Cell.Access = EFormCellAccessType.FormCellDisVisible;
						} else if (ls_ValidHandleIDs.indexOf(ls_Value) + 1 > 0) {
							lo_Cell.Access = EFormCellAccessType.FormCellValidHandle;
						} else if (ls_NeedHandleIDs.indexOf(ls_Value) + 1 > 0) {
							lo_Cell.Access = EFormCellAccessType.FormCellNeedHandle;
						} else {
							lo_Cell.Access = EFormCellAccessType.FormCellReadOnly;
						}
						if (!ao_Item.WorkItemAccess) {
							if (lo_Cell.Access > EFormCellAccessType.FormCellReadOnly) {
								lo_Cell.Access = EFormCellAccessType.FormCellReadOnly;
							}
						}
					}
				}
			}

			// 角色权限[设置计算方式]
			String ls_Opens = ";" + ao_Item.CurRight.RoleOpenAccesses;
			String ls_Sends = ";" + ao_Item.CurRight.RoleSendAccesses;
			String ls_Saves = ";" + ao_Item.CurRight.RoleSaveAccesses;
			String ls_SendAfters = ";" + ao_Item.CurRight.RoleSendAfterAccesses;
			for (CRole lo_Role : ao_Item.Cyclostyle.Roles) {
				ls_Value = ";" + lo_Role.RoleID + ";";
				li_Type = (ls_Opens.indexOf(ls_Value) + 1 == 0) ? ECaculateStatusType.NeedNotCaculate : ECaculateStatusType.CaculateOpenStatus;
				if (ls_Sends.indexOf(ls_Value) + 1 > 0) {
					li_Type = (int) li_Type + ECaculateStatusType.CaculateSendStatus;
				}
				if (ls_Saves.indexOf(ls_Value) + 1 > 0) {
					li_Type = (int) li_Type + ECaculateStatusType.CaculateSaveStatus;
				}
				if (ls_SendAfters.indexOf(ls_Value) + 1 > 0) {
					li_Type = (int) li_Type + ECaculateStatusType.CaculateSendAfterStatus;
				}
				lo_Role.CaculateStatus = li_Type;
				if (!ao_Item.WorkItemAccess) {
					lo_Role.CaculateStatus = ECaculateStatusType.NeedNotCaculate;
				}
			}

			// 属性权限[设置计算方式]
			ls_Opens = ";" + ao_Item.CurRight.PropOpenAccesses;
			ls_Sends = ";" + ao_Item.CurRight.PropSendAccesses;
			ls_Saves = ";" + ao_Item.CurRight.PropSaveAccesses;
			ls_SendAfters = ";" + ao_Item.CurRight.RoleSendAfterAccesses;
			for (CProp lo_Prop : ao_Item.Cyclostyle.Props) {
				ls_Value = ";" + String.valueOf(lo_Prop.PropID) + ";";
				li_Type = (ls_Opens.indexOf(ls_Value) + 1 == 0) ? ECaculateStatusType.NeedNotCaculate : ECaculateStatusType.CaculateOpenStatus;
				if (ls_Sends.indexOf(ls_Value) + 1 > 0) {
					li_Type = (int) li_Type + ECaculateStatusType.CaculateSendStatus;
				}
				if (ls_Saves.indexOf(ls_Value) + 1 > 0) {
					li_Type = (int) li_Type + ECaculateStatusType.CaculateSaveStatus;
				}
				if (ls_SendAfters.indexOf(ls_Value) + 1 > 0) {
					li_Type = (int) li_Type + ECaculateStatusType.CaculateSendAfterStatus;
				}
				lo_Prop.setCaculateStatus(li_Type);
				if (!ao_Item.WorkItemAccess) {
					lo_Prop.setCaculateStatus(ECaculateStatusType.NeedNotCaculate);
				}
			}

			return true;
		} catch (Exception ex) {
			ao_Item.Raise(ex, "rightToInst", String.valueOf(ao_Item.WorkItemID));
			return false;
		}
	}

	/**
	 * 权限叠加
	 * 
	 * @param as_HaveRights
	 *            已有权限，使用“;”分隔
	 * @param as_AddRights
	 *            添加权限，使用“;”分隔
	 * @return
	 */
	private static String pileUpRights(String as_HaveRights, String as_AddRights) {
		if (MGlobal.isEmpty(as_HaveRights) || MGlobal.isEmpty(as_AddRights))
			return as_HaveRights + as_AddRights;

		String ls_Right = as_HaveRights;
		String[] ls_Array = as_AddRights.split(";");
		for (int i = 0; i < ls_Array.length; i++) {
			if (!MGlobal.isEmpty(ls_Array[i])) {
				if ((";" + ls_Right).indexOf(";" + ls_Array[i] + ";") == -1) {
					ls_Right = ls_Right + ls_Array[i] + ";";
				}
			}
		}
		return ls_Right;
	}

	/**
	 * 权限削弱
	 * 
	 * @param as_HaveRights
	 *            已有权限，使用“;”分隔
	 * @param as_ReduceRights
	 *            添加权限，使用“;”分隔
	 * @return
	 */
	private static String weakenRights(String as_HaveRights, String as_ReduceRights) {
		try {
			if (MGlobal.isEmpty(as_HaveRights) || MGlobal.isEmpty(as_ReduceRights))
				return "";

			String ls_Right = ";" + as_HaveRights;
			String[] ls_Array = as_ReduceRights.split(";");
			for (int i = 0; i < ls_Array.length; i++) {
				if (!MGlobal.isEmpty(ls_Array[i])) {
					if (ls_Right.indexOf(";" + ls_Array[i] + ";") + 1 > 0) {
						ls_Right = ls_Right.replaceAll(";" + ls_Array[i] + ";", ";");
					}
				}
			}

			return ls_Right.substring(1, ls_Right.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 初始化表头属性和角色
	 * 
	 * @param ao_Item
	 */
	private static void initPropRole(CWorkItem ao_Item) {
		for (CProp lo_Prop : ao_Item.Cyclostyle.Props) {
			lo_Prop.setValue(lo_Prop.getInitValue());
		}

		for (CRole lo_Role : ao_Item.Cyclostyle.Roles) {
			// 非拟稿人
			if (lo_Role.RoleID > 1)
				lo_Role.Value = lo_Role.InitValue;
		}
	}

	/**
	 * 取得动态修改的表头属性和角色
	 * 
	 * @param ao_Item
	 * @throws SQLException
	 */
	private static void importPropRoleValue(CWorkItem ao_Item) throws SQLException {
		boolean lb_Boolean = false;// 判断是否是打开以前版本的数据
		if (ao_Item.intParameter == 101) {
			Element lo_Node = ao_Item.Cyclostyle.getExtendFieldsXml().getDocumentElement();
			if (lo_Node.getAttribute("SaveVersions").equals("")) {
				lb_Boolean = (lo_Node.getAttribute("SaveVersions") == "1");
			}
		}

		List<Map<String, Object>> lo_Set;
		if (lb_Boolean) {
			lo_Set = CSqlHandle.getJdbcTemplate().queryForList(
					"SELECT * FROM WorkEntryContent WHERE WorkItemID = " + String.valueOf(ao_Item.WorkItemID) + " AND EntryID = "
							+ String.valueOf(ao_Item.lngParameter));
		} else {
			lo_Set = CSqlHandle.getJdbcTemplate().queryForList(
					"SELECT * FROM WorkItemContent WHERE WorkItemID = " + String.valueOf(ao_Item.WorkItemID));
		}

		lb_Boolean = lo_Set != null && lo_Set.size() > 0;
		if (!lb_Boolean) {
			lo_Set = null;
			return;
		}

		for (Map<String, Object> row : lo_Set) {
			// 取得表头属性值
			if (MGlobal.readString(row, "ContentIndex").equals("PropValue")) {
				if (MGlobal.readString(row, "SaveFlag").equals("")) {
					setPackProps(ao_Item, EDataHandleType.OrignType, MGlobal.readString(row, "ContentValue"));
				} else {
					setPackProps(ao_Item, (MGlobal.readInt(row, "SaveFlag") == 0 ? EDataHandleType.XmlType : EDataHandleType.OrignType),
							MGlobal.readString(row, "ContentValue"));
				}
			}
			// 取得角色值
			if (MGlobal.readString(row, "ContentIndex").equals("RoleValue")) {
				if (MGlobal.readString(row, "SaveFlag").equals("")) {
					setPackRoles(ao_Item, EDataHandleType.OrignType, MGlobal.readString(row, "ContentValue"));
				} else {
					setPackRoles(ao_Item, (MGlobal.readInt(row, "SaveFlag") == 0 ? EDataHandleType.XmlType : EDataHandleType.OrignType),
							MGlobal.readString(row, "ContentValue"));
				}
			}
		}
		lo_Set = null;
	}

	/**
	 * 获取角色存储内容
	 * 
	 * @param ao_Item
	 *            公文对象
	 * @param ai_Type
	 *            存储类型：EDataHandleType.OrignType/EDataHandleType.XmlType
	 * @return
	 */
	public static String getPackRoles(CWorkItem ao_Item, int ai_Type) {
		String ls_Value = null;
		if (ai_Type == EDataHandleType.OrignType) {
			MBag lo_Bag = new MBag("");
			lo_Bag.Write("theRolesCount", ao_Item.Cyclostyle.Roles.size());
			for (CRole lo_Role : ao_Item.Cyclostyle.Roles) {
				lo_Bag.Write("RoleValue", lo_Role.Value);
			}
			ls_Value = lo_Bag.Content;
			lo_Bag.ClearUp();
			lo_Bag = null;
		} else {
			Document lo_Xml = MXmlHandle.LoadXml("<Role />");
			for (CRole lo_Role : ao_Item.Cyclostyle.Roles) {
				lo_Xml.getDocumentElement().setAttribute("R" + String.valueOf(lo_Role.RoleID), lo_Role.Value);
			}
			ls_Value = MXmlHandle.getXml(lo_Xml);
			lo_Xml = null;
		}
		return ls_Value;
	}

	/**
	 * 设置角色存储内容
	 * 
	 * @param ao_Item
	 * @param ai_Type
	 * @param as_Content
	 */
	private static void setPackRoles(CWorkItem ao_Item, int ai_Type, String as_Content) {
		if (ai_Type == EDataHandleType.OrignType) {
			MBag lo_Bag = new MBag(as_Content);
			lo_Bag.Position = 1;
			lo_Bag.Read("theRolesCount");
			for (CRole lo_Role : ao_Item.Cyclostyle.Roles) {
				lo_Role.Value = lo_Bag.ReadString("RoleValue");
			}
			lo_Bag.ClearUp();
			lo_Bag = null;
		} else {
			Document lo_Xml = MXmlHandle.LoadXml(as_Content);
			for (CRole lo_Role : ao_Item.Cyclostyle.Roles) {
				lo_Role.Value = lo_Xml.getDocumentElement().getAttribute("R" + String.valueOf(lo_Role.RoleID));
			}
			lo_Xml = null;
		}
	}

	/**
	 * 获取属性存储内容
	 * 
	 * @param ao_Item
	 * @param ai_Type
	 * @return
	 * @throws Exception
	 * @throws DOMException
	 */
	public static String getPackProps(CWorkItem ao_Item, int ai_Type) throws DOMException, Exception {
		String ls_Value = null;
		if (ai_Type == EDataHandleType.OrignType) {
			MBag lo_Bag = new MBag("");
			lo_Bag.Write("thePropsCount", ao_Item.Cyclostyle.Props.size());
			for (CProp lo_Prop : ao_Item.Cyclostyle.Props) {
				lo_Bag.Write("PropValue", lo_Prop.getValue());
			}
			ls_Value = lo_Bag.Content;
			lo_Bag.ClearUp();
			lo_Bag = null;
		} else {
			Document lo_Xml = MXmlHandle.LoadXml("<Prop />");
			for (CProp lo_Prop : ao_Item.Cyclostyle.Props) {
				lo_Xml.getDocumentElement().setAttribute("P" + String.valueOf(lo_Prop.PropID), lo_Prop.getValue());
			}
			ls_Value = MXmlHandle.getXml(lo_Xml);
			lo_Xml = null;
		}
		return ls_Value;
	}

	/**
	 * 设置属性存储内容
	 * 
	 * @param ao_Item
	 * @param ai_Type
	 * @param as_Content
	 */
	private static void setPackProps(CWorkItem ao_Item, int ai_Type, String as_Content) {
		if (ai_Type == EDataHandleType.OrignType) {
			MBag lo_Bag = new MBag(as_Content);
			lo_Bag.Position = 1;
			lo_Bag.Read("thePropsCount");
			for (CProp lo_Prop : ao_Item.Cyclostyle.Props) {
				lo_Prop.setValue((String) lo_Bag.Read("PropValue"));
			}
			lo_Bag.ClearUp();
			lo_Bag = null;
		} else {
			Document lo_Xml = MXmlHandle.LoadXml(as_Content);
			for (CProp lo_Prop : ao_Item.Cyclostyle.Props) {
				lo_Prop.setValue(lo_Xml.getDocumentElement().getAttribute("P" + String.valueOf(lo_Prop.PropID)));
			}
			lo_Xml = null;
		}
	}

	/**
	 * 调用计算函数[二次开发]
	 * 
	 * @param ai_Caculate
	 * @throws Exception
	 */
	private static void caculateScript(CWorkItem ao_Item, int ai_Caculate) throws Exception {
		if (ai_Caculate == ECaculateStatusType.NeedNotCaculate)
			return;

		// 初始化二次开发控件[加载公共函数和系统函数]
		ScriptEngine lo_Script = ao_Item.getScript();

		// 计算角色内容
		for (CRole lo_Role : ao_Item.Cyclostyle.Roles) {
			if ((lo_Role.CaculateStatus & ai_Caculate) == ai_Caculate && lo_Role.RoleID > 1) {
				String ls_Value = "";

				if (ai_Caculate == ECaculateStatusType.CaculateOpenStatus) {
					if (!lo_Role.InitCacuCont.equals("")) {
						ls_Value = lo_Script.eval(lo_Role.InitCacuCont).toString();
					}
				} else {
					if (!lo_Role.SendCacuCont.equals("")) {
						ls_Value = lo_Script.eval(lo_Role.SendCacuCont).toString();
					}
				}

				if (ls_Value.equals("")) {
					lo_Role.Value = "";
				} else {
					String ls_UserIDs = "";
					String ls_UserCodes = "";
					String ls_UserNames = "";
					String[] ls_Array = ls_Value.split(";");
					for (int i = 0; i < ls_Array.length; i++) {
						if (!ls_Array[i].equals("")) {
							// 标识
							if (MGlobal.isNumber(ls_Array[i])) {
								ls_UserIDs += ls_Array[i] + ",";
							} else if (MGlobal.getWordCount(ls_Array[i]) == ls_Array[i].length()) { // 代码
								ls_UserCodes += "'" + ls_Array[i].replaceAll("'", "''") + "',";
							} else {
								ls_UserNames += "'" + ls_Array[i].replaceAll("'", "''") + "',";
							}
						}
					}
					String ls_Sql = "SELECT UserID, UserType FROM UserInfo WHERE UserID IN (" + ls_UserIDs + "-1)";
					if (!MGlobal.isEmpty(ls_UserCodes)) {
						ls_Sql += " OR UserCode IN (" + ls_UserCodes.substring(0, ls_UserCodes.length() - 1) + ")";
					}
					if (!MGlobal.isEmpty(ls_UserNames)) {
						ls_Sql += " OR UserName IN (" + ls_UserNames.substring(0, ls_UserNames.length() - 1) + ")";
					}
					ls_UserIDs = "";
					List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
					for (Map<String, Object> row : lo_Set) {
						if (MGlobal.readInt(row, "UserType") == 1) {
							ls_UserIDs += "G" + MGlobal.readString(row, "UserID") + ";";
						} else {
							ls_UserIDs += "U" + MGlobal.readString(row, "UserID") + ";";
						}
					}
					lo_Set = null;
					lo_Role.Value = ls_UserIDs;
				}
			}
		}

		// 计算控制属性内容
		for (CProp lo_Prop : ao_Item.Cyclostyle.Props) {
			if ((lo_Prop.getCaculateStatus() & ai_Caculate) == ai_Caculate) {
				if (ai_Caculate == ECaculateStatusType.CaculateOpenStatus) {
					if (!lo_Prop.getOpenCacuContent().equals("")) {
						lo_Prop.setValue(lo_Script.eval(lo_Prop.getOpenCacuContent()).toString());
					}
				} else {
					if (!lo_Prop.getSendCacuContent().equals("")) {
						// System.out.print(lo_Prop.getSendCacuContent());
						lo_Prop.setValue(lo_Script.eval(lo_Prop.getSendCacuContent()).toString());
					}
				}
			}
		}
	}

	/**
	 * 保存实例
	 * 
	 * @param ao_Item
	 * @return
	 */
	public static boolean saveWorkItem(CWorkItem ao_Item) {
		try {
			// 记录日志
			ao_Item.Record("saveWorkItem", getWrkLogPara(ao_Item));

			if (ao_Item.WorkItemAccess == false) {
				// 错误【1033】：当前公文实例为只读，不能保存
				ao_Item.Raise(1033, "saveWorkItem", getWrkLogPara(ao_Item));
				return false;
			}

			ao_Item.LaunchParameter = "";

			// 并发步骤处理
			refreshInterData(ao_Item);

			ao_Item.CurEntry.setEntryStatus(EEntryStatus.TransactingStatus);

			// 保存时----调用计算函数[二次开发]
			caculateScript(ao_Item, ECaculateStatusType.CaculateSaveStatus);

			caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuBeforeSaveForAll));
			if (ao_Item.CurEntry.isFirstTrans()) {
				caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuBeforeSaveForFirst));
			}

			int ll_WorkItemID = ao_Item.WorkItemID;

			// 保存扩展字段的值
			saveExtendValue(ao_Item);
			boolean lb_Boolean = saveWorkItemData(ao_Item, true);
			if (lb_Boolean)
				refreshEntries(ao_Item);

			if (ll_WorkItemID == -1) {
				if (TWorkAdmin.addLock(ao_Item, ELockType.RepellentLock))
					ll_WorkItemID = ao_Item.WorkItemID;
			}

			caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuAfterSaveForAll));
			if (ao_Item.CurEntry.isFirstTrans()) {
				caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuAfterSaveForFirst));
			}

			return lb_Boolean;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 保存新建实例情况
	 * 
	 * @param ao_Item
	 * @throws SQLException
	 */
	private static void appendNewWorkItemSql(final CWorkItem ao_Item) throws SQLException {
		String ls_Id = String.valueOf(ao_Item.WorkItemID); // 公文标识
		String ls_TId = String.valueOf(ao_Item.Cyclostyle.CyclostyleID); // 模板标识

		// 保存公文模板基本信息：TemplateDefine -> TemplateInst
		String ls_Fields = "TemplateID, TemplateName, TypeID, CreateID, Creator, CreateDate, "
				+ "EditID, Editor, EditDate, HaveBody, HaveDocument, COMP_ID, HaveForm, FirstPage, "
				+ "FlowFlag, TemplateDescribe, TemplateIsValid, BindImage, ExtendFields, ";
		String ls_Sql = "INSERT INTO TemplateInst (WorkItemID, {0}FileSaveType, FileSaveID, FileSaveDate) "
				+ "(SELECT {1} AS WorkItemID, {0}? AS FileSaveType, ? AS FileSaveID, ? AS FileSaveDate"
				+ " FROM TemplateDefine WHERE TemplateID = {2})";

		List parasList = new ArrayList();
		parasList.add(ao_Item.Cyclostyle.FileSaveType);
		parasList.add(ao_Item.Cyclostyle.FileSaveID);
		parasList.add(MGlobal.dateToSqlTime(ao_Item.Cyclostyle.FileSaveDate));
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId), parasList.toArray());
		// MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId),
		// new PreparedStatementSetter() {
		// @Override
		// public void setValues(PreparedStatement lo_State)
		// throws SQLException {
		//
		// int li_Index = 1;
		// lo_State.setInt(li_Index++,
		// ao_Item.Cyclostyle.FileSaveType);
		// lo_State.setInt(li_Index++,
		// ao_Item.Cyclostyle.FileSaveID);
		// lo_State.setTimestamp(li_Index++, MGlobal
		// .dateToSqlTime(ao_Item.Cyclostyle.FileSaveDate));
		// }
		// });
		// ao_Item.Statements.add(lo_State);

		// 保存公文模板权限列表：RightList -> RightInst
		ls_Fields = "RightID, RightName, RightType, RightContent";
		ls_Sql = "INSERT INTO RightInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM RightList WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板表头属性：PropList -> PropInst
		ls_Fields = "PropID, PropName, DataType, PropContent";
		ls_Sql = "INSERT INTO PropInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM PropList WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板附件列表：DocumentList -> DocumentInst
		ls_Fields = "DocumentID, DocumentName, DocumentType, CreateID, Creator, CreateDate, EditID, "
				+ "Editor, EditDate, FileName, OriginalID, DocumentVersion, DocumentContent";
		ls_Sql = "INSERT INTO DocumentInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM DocumentList WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板数据存储表格定义：TableList -> TableInst
		ls_Fields = "TableID, TableName, TableCode, ConnCode, TableContent, TableDescribe";
		ls_Sql = "INSERT INTO TableInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM TableList WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板表格定义列表：FormList -> FormInst
		ls_Fields = "FormID, FormName, FormContent, FormDescribe";
		ls_Sql = "INSERT INTO FormInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM FormList WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板角色列表：RoleList -> RoleInst
		ls_Fields = "RoleID, RoleName, RoleType, RoleContent, RoleDescribe";
		ls_Sql = "INSERT INTO RoleInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM RoleList WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板专用二次开发函数：ScriptList -> ScriptInst
		ls_Fields = "ScriptID, ScriptName, ScriptContent, ScriptDescribe";
		ls_Sql = "INSERT INTO ScriptInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM ScriptList WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板公文发布定义：PublicTemplate -> PublicTemplateInst
		ls_Fields = "PublicType, RightType, PublicUsers, BodyType, DocumentType, FormType, PublicContent, AutoBuildSeq, SequenceCode, RepeatPublic, Describe";
		ls_Sql = "INSERT INTO PublicTemplateInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM PublicTemplate WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板流程定义：FlowDefine -> FlowInst
		ls_Fields = "FlowID, FlowName, FlowType, FlowIsValid, FlowDescribe, FlowImage, DefaultFlow, FlowContent";
		ls_Sql = "INSERT INTO FlowInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM FlowDefine WHERE TemplateID = {2})";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));

		// 保存公文模板流程步骤：FlowActivity -> ActivityInst
		ls_Fields = "FlowID, ActivityID, ActivityName, ActivityType, ActivityOrder, ActivityContent, ActivityDescribe, ImageID";
		ls_Sql = "INSERT INTO ActivityInst (WorkItemID, {0}) (SELECT {1} AS WorkItemID, {0} FROM FlowActivity WHERE"
				+ " FlowID IN (SELECT FlowID FROM FlowDefine WHERE TemplateID = {2}))";
		CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Fields, ls_Id, ls_TId));
	}

	/**
	 * 保存公文实例数据到数据库中
	 * 
	 * @param ao_Item
	 * @param ab_SaveDocument
	 *            是否保存附件
	 * @return
	 * @throws Exception
	 */
	private static boolean saveWorkItemData(CWorkItem ao_Item, boolean ab_SaveDocument) throws Exception {
		try {
			String ls_Sql = ""; // SQL语句变量
			int ll_Id = ao_Item.WorkItemID;
			String ls_Id = String.valueOf(ll_Id);
			// CSqlHandle lo_SqlHandle = ao_Item.SqlHandle();
			CGlobalPara lo_Para = ao_Item.Logon.GlobalPara;
			CLogon lo_Logon = ao_Item.Logon;

			boolean lb_IsNewWorkItem = true; // 是否新建实例的保存
			if (ll_Id > 0) { // 判断是否新建实例
				ls_Sql = "SELECT '1' FROM WorkItemInst WHERE WorkItemID = " + ls_Id;
				lb_IsNewWorkItem = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class).equals("");
			} else {
				ll_Id = ao_Item.PublicFunc().getSeqValue("WorkItemID", "公文实例标识流水号");
				ao_Item.WorkItemID = ll_Id;
				ls_Id = String.valueOf(ll_Id);
			}

			// ao_Item.Statements = new ArrayList<String>();
			// Connection lo_Conn = lo_SqlHandle.getConnection();
			Savepoint lo_Point = null;

			try {
				// lo_Conn.setAutoCommit(false);
				// lo_Point = lo_Conn.setSavepoint("SaveData");

				ao_Item.EditorID = lo_Para.UserID;
				ao_Item.Editor = lo_Para.UserName;
				ao_Item.EditDate = MGlobal.getNow();

				// 保存新建实例情况
				if (lb_IsNewWorkItem)
					appendNewWorkItemSql(ao_Item);

				int li_Type = 0;// =0-XmlType；=1-OrignType；
				int li_SaveType = (lb_IsNewWorkItem ? 0 : 1);
				// 保存主表信息
				String lo_State = ao_Item.getSaveState(li_SaveType, li_Type);
				ao_Item.Save(lo_State, li_SaveType, li_Type, 0);
				// lo_State.executeBatch();
				// ao_Item.Statements.add(lo_State);

				// =====保存公文模板更改开始=====

				// 保存流程更改
				String ls_Ids = ""; // 已被修改的流程标识连接串
				for (CFlow lo_Flow : ao_Item.Cyclostyle.Flows) {
					if (lo_Flow.Changed)
						ls_Ids += String.valueOf(lo_Flow.FlowID) + ",";
				}

				if (MGlobal.isExist(ls_Ids)) {// 说明流程有变化
					ls_Ids = "(" + ls_Ids.substring(0, ls_Ids.length() - 1) + ")";

					// 删除数据库中的流程信息
					// 删除流程步骤
					ls_Sql = "DELETE FROM ActivityInst WHERE WorkItemID = {0} AND FlowID IN {1}";
					// ao_Item.Statements.add(lo_SqlHandle.addBatchSql(MessageFormat.format(ls_Sql,
					// ls_Id, ls_Ids), true));
					CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Id, ls_Ids));
					// 删除流程定义
					ls_Sql = "DELETE FROM FlowInst WHERE WorkItemID = {0} AND FlowID IN {1}";
					// ao_Item.Statements.add(lo_SqlHandle.addBatchSql(MessageFormat.format(ls_Sql,
					// ls_Id, ls_Ids), true));
					CSqlHandle.getJdbcTemplate().update(MessageFormat.format(ls_Sql, ls_Id, ls_Ids));

					// 保存流程定义
					lo_State = CFlow.getSaveState(0, li_Type, true);
					for (CFlow lo_Flow : ao_Item.Cyclostyle.Flows) {
						if (lo_Flow.Changed) {
							lo_Flow.Save(lo_State, 0, li_Type, true, 1);
						}
					}
					// ao_Item.Statements.add(lo_State);

					lo_State = CActivity.getSaveState(0, li_Type, true);
					for (CFlow lo_Flow : ao_Item.Cyclostyle.Flows) {
						if (lo_Flow.Changed) {
							for (CActivity lo_Act : lo_Flow.Activities) {
								lo_Act.Save(lo_State, 0, li_Type, true, 1);
							}
						}
					}
					// ao_Item.Statements.add(lo_State);
				}

				// 以下动态内容的更改统一存储于动态数据表WorkItemContent
				// 公文状态内容存储表格WorkEntryContent：存储动态流转公文状态的变化内容，
				// 本表格是为了公文查询表单在不同的状态下的版本信息保存情况
				// 是否需要存储状态版本信息
				boolean lb_SaveEntryVersion = (ao_Item.Cyclostyle.getExtendFieldsXml().getDocumentElement().getAttribute("SaveVersions").equals("1"));
				String ls_XmlProps = getPackProps(ao_Item, li_Type); // 属性打包
				String ls_XmlRoles = getPackRoles(ao_Item, li_Type); // 角色打包
				if (lb_SaveEntryVersion) {
					ls_Sql = "SELECT '1' FROM WorkEntryContent WHERE WorkItemID = " + ls_Id + " AND EntryID = "
							+ String.valueOf(ao_Item.CurEntry.EntryID);
					ls_Sql = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
				}
				ao_Item.SaveContent(li_SaveType, li_Type, ls_XmlProps, ls_XmlRoles, lb_SaveEntryVersion, (ls_Sql.equals("1") ? 1 : 0));

				String ls_TempFiles = ""; // 临时文件名称
				String ls_DataFiles = "";// 实际文件名称
				String ls_DeleteFiles = "";// 删除实际文件名称
				String ls_OriginalFile = "";// 原始文件名称

				// 保存正文更改
				if (ao_Item.Cyclostyle.HaveBody) {
					// 保存正文的修改信息
					lo_State = CDocument.getSaveState(li_SaveType, li_Type, true);
					// ao_Item.Statements.add(lo_State);
					// 创建正文文件夹
					MFile.createFolder(lo_Logon.SaveDataPath + "WorkItem\\Data" + String.valueOf(ao_Item.Cyclostyle.CyclostyleID));
					// 正文对象
					CDocument lo_Body = ao_Item.Cyclostyle.Body;
					if (lb_IsNewWorkItem) {
						recordDocumentLog(lo_Body, 0);

						lo_Body.CreatorID = lo_Para.UserID;
						lo_Body.Creator = lo_Para.UserName;
						lo_Body.CreateDate = MGlobal.getNow();
						lo_Body.EditorID = lo_Para.UserID;
						lo_Body.Editor = lo_Para.UserName;
						lo_Body.EditDate = MGlobal.getNow();
						if (lo_Body.DocumentType == EDocumentType.FileVersionBody || lo_Body.DocumentType == EDocumentType.WordVersionBody) {
							lo_Body.OrignID = lo_Body.DocumentID;
							lo_Body.VersionID = 1;
						}
						if (lo_Body.DocumentType == EDocumentType.FileVersionBody) {
							lo_Body.EntryID = ao_Item.CurEntry.EntryID;
						}
						lo_Body.Save(lo_State, li_SaveType, li_Type, true, 0);

						// 删除原来的一条记录
						ls_Sql = "DELETE FROM DocumentInst WHERE WorkItemID = " + ls_Id + " AND DocumentID = " + String.valueOf(lo_Body.DocumentID);
						CSqlHandle.getJdbcTemplate().update(ls_Sql);

						if ((lo_Body.ChangeType & 2) == 2) {// 已修改了文件内容
							if (lo_Body.DocumentType == EDocumentType.NotAnyDocumentType) {
								// 不需要保存公文正文内容
							} else if (lo_Body.DocumentType == EDocumentType.TextTypeBody || lo_Body.DocumentType == EDocumentType.RTFTypeBody) {
								lo_Body.WriteFile = MFile.getTempFile(MFile.getFileExt(lo_Body.DocumentExt), lo_Logon.TempPath);
								if (ab_SaveDocument) {
									MFile.writeFile(lo_Body.WriteFile, lo_Body.getBytes());
								}
								lo_Body.WriteFile = MFile.getFileName(lo_Body.WriteFile);
							} else {// if lo_Body.DocumentType ==
									// EDocumentTypeCommondBody ||
									// lo_Body.DocumentType ==
									// EDocumentType.FileVersionBody ||
									// lo_Body.DocumentType ==
									// EDocumentType.WordVersionBody)
								// 保存修改内容
							}
						} else {
							if (lo_Body.DocumentType == EDocumentType.CommondBody || lo_Body.DocumentType == EDocumentType.FileVersionBody
									|| lo_Body.DocumentType == EDocumentType.WordVersionBody) {
								// 拷贝模板文件内容到实例中
								lo_Body.WriteFile = MFile.getTempFile(MFile.getFileExt(lo_Body.DocumentExt), ao_Item.Logon.TempPath);
								MFile.writeFile(lo_Body.WriteFile, lo_Body.getBytes());
								if (lo_Body.OldFileExt.equals("")) {
									// 当公文正文未被初始化时
									if (lo_Body.setDefaultContent() && ab_SaveDocument) {
										MFile.copyFile(lo_Logon.SaveDataPath + "Template\\" + lo_Body.OldFileExt, lo_Body.WriteFile);
									}
								} else {
									if (ab_SaveDocument) {
										MFile.copyFile(lo_Logon.SaveDataPath + "Template\\" + lo_Body.OldFileExt, lo_Body.WriteFile);
										MFile.copyFile(lo_Logon.SaveDataPath + "Template\\" + lo_Body.OldFileExt, lo_Body.WriteFile);
									}
								}
							}
						}

						ls_TempFiles = lo_Body.WriteFile + "|";
						ls_DataFiles = MessageFormat.format("File{0}_{1}.{2}", ls_Id, String.valueOf(lo_Body.DocumentID),
								MFile.getFileExt(lo_Body.DocumentExt) + "|");
					} else {
						if ((lo_Body.ChangeType & 1) == 1) {
							if (lo_Body.DocumentType == EDocumentType.FileVersionBody || lo_Body.DocumentType == EDocumentType.WordVersionBody) {// 版本控制
								if ((lo_Body.ChangeType & 2) == 2) {// 已修改了文件内容，生成一个新的版本
									ao_Item.Cyclostyle.DocumentMaxID++;
									lo_Body.DocumentID = ao_Item.Cyclostyle.DocumentMaxID;
									lo_Body.VersionID++;
									if (lo_Body.DocumentType == EDocumentType.FileVersionBody) {
										lo_Body.EntryID = ao_Item.CurEntry.EntryID;
									}
								} else {
									// 删除原来的一条记录
									ls_Sql = "DELETE FROM DocumentInst WHERE WorkItemID = " + ls_Id + " AND DocumentID = "
											+ String.valueOf(lo_Body.DocumentID);
									CSqlHandle.getJdbcTemplate().update(ls_Sql);
								}
							} else { // 非版本控制
								// 删除原来的一条记录
								ls_Sql = "DELETE FROM DocumentInst WHERE WorkItemID = " + ls_Id + " AND DocumentID = "
										+ String.valueOf(lo_Body.DocumentID);
								CSqlHandle.getJdbcTemplate().update(ls_Sql);
							}
							lo_Body.EditorID = lo_Para.UserID;
							lo_Body.Editor = lo_Para.UserName;
							lo_Body.EditDate = MGlobal.getNow();

							recordDocumentLog(lo_Body, 0);
						}

						if ((lo_Body.ChangeType & 2) == 2) {// 已修改了文件内容
							recordDocumentLog(lo_Body, 2);

							if (lo_Body.DocumentType == EDocumentType.TextTypeBody || lo_Body.DocumentType == EDocumentType.RTFTypeBody) {
								lo_Body.WriteFile = MFile.getTempFile(MFile.getFileExt(lo_Body.DocumentExt), lo_Logon.TempPath);
								MFile.writeFile(lo_Body.WriteFile, lo_Body.getBytes());
								lo_Body.WriteFile = MFile.getFileName(lo_Body.WriteFile);
							}

							ls_OriginalFile = MessageFormat.format("WorkItem\\Data{0}\\File{1}_{2}.{3}",
									String.valueOf(ao_Item.Cyclostyle.CyclostyleID), ls_Id, String.valueOf(lo_Body.DocumentID),
									MFile.getFileExt(lo_Body.OldFileExt));

							// 如果是非版本控制附件，需要删除原文件
							if (!(lo_Body.DocumentType == EDocumentType.FileVersionBody || lo_Body.DocumentType == EDocumentType.WordVersionBody)) {
								if (lo_Body.DocumentType == EDocumentType.FileVersionBody) {
									lo_Body.EntryID = ao_Item.CurEntry.EntryID;
								}
								if (!lo_Body.DocumentExt.equals(lo_Body.OldFileExt)) {
									ls_DeleteFiles += MessageFormat.format("WorkItem\\Data{0}\\File{1}_{2}.{3}|",
											String.valueOf(ao_Item.Cyclostyle.CyclostyleID), ls_Id, String.valueOf(lo_Body.DocumentID),
											MFile.getFileExt(lo_Body.OldFileExt));
									ls_DeleteFiles = MessageFormat.format("{0}File{1}_{2}.{3}|", ao_Item.getRelativelyFilePath(), ls_Id,
											String.valueOf(lo_Body.DocumentID), MFile.getFileExt(lo_Body.OldFileExt));
								}
							}

							ls_TempFiles = lo_Body.WriteFile + "|";
							ls_DataFiles += MessageFormat.format("File{0}_{1}.{2}|", ls_Id, String.valueOf(lo_Body.DocumentID),
									MFile.getFileExt(lo_Body.DocumentExt));
						}
					}
				}

				// 在删除正文实际文件以前，先保存到备份文件夹中
				if (!ls_OriginalFile.equals("") && ab_SaveDocument) {
					backFileToBpFolder(ao_Item, ls_OriginalFile);
				}

				// 保存附件更改
				ao_Item.Cyclostyle.HaveDocument = false;
				if (ao_Item.Cyclostyle.Documents.size() > 0) {
					lo_State = CDocument.getSaveState(li_SaveType, li_Type, true);
					// ao_Item.Statements.add(lo_State);
					for (CDocument lo_Document : ao_Item.Cyclostyle.Documents) {
						if (lo_Document.DocumentType != EDocumentType.DeletedFile) {
							ao_Item.Cyclostyle.HaveDocument = true;
						}
						if (lo_Document.ChangeType == 4) {// 新建
							recordDocumentLog(lo_Document, 0);

							if (lo_Document.DocumentType == EDocumentType.FileVersionDocument) {// 版本控制
								lo_Document.VersionID = 1;
								lo_Document.OrignID = lo_Document.DocumentID;
								lo_Document.EntryID = ao_Item.CurEntry.EntryID;
							}
							lo_Document.Save(lo_State, li_SaveType, li_Type, true, 0);
							if (lo_Document.ChangeType < 7 || lo_Document.DocumentType == EDocumentType.ScanTifDocument
									|| lo_Document.DocumentType == EDocumentType.ScanJpgDocument) {
								ls_TempFiles += lo_Document.WriteFile + "|";
								ls_DataFiles += MessageFormat.format("File{0}_{1}.{2}|", ls_Id, String.valueOf(lo_Document.DocumentID),
										MFile.getFileExt(lo_Document.DocumentExt));
								if (lo_Document.ChangeType == EDocumentType.ScanJpgDocument) {
									for (int i = 2; i <= lo_Document.FilePages; i++) {
										ls_TempFiles += lo_Document.WriteFile.replaceAll(".", "_" + String.valueOf(i) + ".") + "|";
										ls_DataFiles += MessageFormat.format("File{0}_{1}_{2}.{3}|", ls_Id, String.valueOf(lo_Document.DocumentID),
												String.valueOf(i), MFile.getFileExt(lo_Document.DocumentExt));
									}
								}
							}
						} else {// 打开
							if (lo_Document.DocumentType == EDocumentType.DeletedFile) {// 被删除
								recordDocumentLog(lo_Document, 1);

								// 删除临时文件
								if (!lo_Document.ReadFile.equals("")) {
									ls_DeleteFiles += lo_Logon.TempPath + lo_Document.ReadFile + "|";
								}
								if (!lo_Document.WriteFile.equals("")) {
									ls_DeleteFiles = lo_Logon.TempPath + lo_Document.WriteFile + "|";
								}

								if (lo_Document.VersionID == 0) {// 非版本控制
									// 删除文件
									ls_DeleteFiles += MessageFormat.format("{0}WorkItem\\Data{1}\\File{2}_{3}.{4}|", lo_Logon.TempPath,
											String.valueOf(ao_Item.Cyclostyle.CyclostyleID), ls_Id, String.valueOf(lo_Document.DocumentID),
											MFile.getFileExt(lo_Document.DocumentExt));
									if (lo_Document.DocumentType == EDocumentType.ScanJpgDocument) {
										ls_DeleteFiles = MessageFormat.format("{0}WorkItem\\Data{1}\\File{2}_{3}_*.{4}|", lo_Logon.TempPath,
												String.valueOf(ao_Item.Cyclostyle.CyclostyleID), ls_Id, String.valueOf(lo_Document.DocumentID),
												MFile.getFileExt(lo_Document.DocumentExt));
										ls_DeleteFiles = MessageFormat.format("{0}File{1}_{2}.{3};", lo_Logon.TempPath, ls_Id,
												String.valueOf(lo_Document.DocumentID), MFile.getFileExt(lo_Document.DocumentExt));
										if (lo_Document.DocumentType == EDocumentType.ScanJpgDocument) {
											ls_DeleteFiles = MessageFormat.format("{0}File{1}_{2}_*.{3};", lo_Logon.TempPath, ls_Id,
													String.valueOf(lo_Document.DocumentID), MFile.getFileExt(lo_Document.DocumentExt));
										}
									}
									// 修改标志位
									ls_Sql = "UPDATE DocumentInst SET DocumentType = -2 WHERE WorkItemID = " + ls_Id + " AND DocumentID = "
											+ String.valueOf(lo_Document.DocumentID);
									CSqlHandle.getJdbcTemplate().update(ls_Sql);
								} else {
									if (lo_Document.ChangeType > 0) {// 有修改过
										lo_Document.EditorID = lo_Para.UserID;
										lo_Document.Editor = lo_Para.UserName;
										lo_Document.EditDate = MGlobal.getNow();
										if (lo_Document.ChangeType == 1) {// 没有修改过文件内容
											ls_Sql = "DELETE FROM DocumentInst WHERE WorkItemID = " + ls_Id + " AND DocumentID = "
													+ String.valueOf(lo_Document.DocumentID);
											CSqlHandle.getJdbcTemplate().update(ls_Sql);
											lo_Document.Save(lo_State, li_SaveType, li_Type, true, 0);
										} else {
											recordDocumentLog(lo_Document, (lb_IsNewWorkItem ? 0 : 2));
											if (lo_Document.DocumentType == EDocumentType.FileVersionDocument) {// 版本控制
												// 生成新版本
												ao_Item.Cyclostyle.DocumentMaxID++;
												lo_Document.DocumentID = ao_Item.Cyclostyle.DocumentMaxID;
												lo_Document.VersionID++;
												lo_Document.EntryID = ao_Item.CurEntry.EntryID;
											} else {
												ls_Sql = "DELETE FROM DocumentInst WHERE WorkItemID = " + ls_Id + " AND DocumentID = "
														+ String.valueOf(lo_Document.DocumentID);
												CSqlHandle.getJdbcTemplate().update(ls_Sql);
											}
											lo_Document.Save(lo_State, li_SaveType, li_Type, true, 0);

											// 保存文件内容
											if (lo_Document.DocumentType < 7 || lo_Document.DocumentType == EDocumentType.ScanTifDocument
													|| lo_Document.DocumentType == EDocumentType.ScanJpgDocument) {
												ls_TempFiles += lo_Document.WriteFile + "|";
												ls_DataFiles += MessageFormat.format("File{0}_{1}.{2}|", ls_Id,
														String.valueOf(lo_Document.DocumentID), MFile.getFileExt(lo_Document.DocumentExt));
												if (lo_Document.DocumentType == EDocumentType.ScanJpgDocument) {
													for (int i = 2; i <= lo_Document.FilePages; i++) {
														ls_TempFiles += lo_Document.WriteFile.replaceAll(".", "_" + String.valueOf(i) + ".") + "|";
														ls_DataFiles += MessageFormat.format("File{0}_{1}_{2}.{3}|", ls_Id,
																String.valueOf(lo_Document.DocumentID), String.valueOf(i),
																MFile.getFileExt(lo_Document.DocumentExt));
													}
												}
											}
										}
									} else {
										if (lb_IsNewWorkItem) {
											recordDocumentLog(lo_Document, (lb_IsNewWorkItem ? 0 : 0));

											lo_Document.Save(lo_State, li_SaveType, li_Type, true, 0);
											if (lo_Document.DocumentType != EDocumentType.URLDocument) {
												// 拷贝模板文件内容到实例中
												lo_Document.WriteFile = MFile.getTempFile(MFile.getFileExt(lo_Document.DocumentExt),
														lo_Logon.TempPath);
												MFile.writeFile(lo_Document.WriteFile, lo_Document.getBytes());
												if (ab_SaveDocument) {
													MFile.copyFile(lo_Logon.SaveDataPath + "Template\\" + lo_Document.OldFileExt,
															lo_Document.WriteFile);
												}
												lo_Document.WriteFile = MFile.getFileName(lo_Document.WriteFile);
												ls_TempFiles += lo_Document.WriteFile + "|";
												ls_DataFiles += MessageFormat.format("File{0}_{1}.{2}|", ls_Id,
														String.valueOf(lo_Document.DocumentID), MFile.getFileExt(lo_Document.DocumentExt));
											}
										}
									}
								}
							}
						}
					}
					if (ao_Item.HaveDocument) {
						ls_Sql = "UPDATE WorkItemInst SET HaveDocument = 1 WHERE WorkItemID = " + ls_Id;
					} else {
						ls_Sql = "UPDATE WorkItemInst SET HaveDocument = 0 WHERE WorkItemID = " + ls_Id;
					}
					CSqlHandle.getJdbcTemplate().update(ls_Sql);
					// ao_Item.Statements.add(lo_SqlHandle.addBatchSql(ls_Sql));
				}

				// =====保存公文模板更改结束=====

				// 保存公文流转状态
				String ls_EntryIDs = ""; // 记录新生成流转状态或发生变化的流转状态标识连接串，使用“,”分隔
				for (CEntry lo_Entry : ao_Item.getEntries()) {
					if (lo_Entry.EntryChanged)
						ls_EntryIDs += String.valueOf(lo_Entry.EntryID) + ",";
				}

				if (MGlobal.isExist(ls_EntryIDs)) { // 状态有变化，需要保存处理
					ls_EntryIDs = ls_EntryIDs.substring(0, ls_EntryIDs.length() - 1);

					String lo_NewState = null; // 新增加的状态
					String lo_EntState = null; // 更新状态(非当前状态)
					String lo_RhtState = null; // 状态权限
					String lo_NewMovState = null; // 新增移动状态
					String lo_UpdateMovState = null; // 更新移动状态
					for (CEntry lo_Entry : ao_Item.getEntries()) {
						if (lo_Entry.EntryChanged) {
							// 保存状态信息
							if (lo_Entry.ID == 0) {
								if (lo_NewState == null) {
									lo_NewState = CEntry.getNewState();
									// ao_Item.Statements.add(lo_NewState);
								}
								lo_Entry.NewSave(lo_NewState, 1);
							} else {
								if (lo_Entry == ao_Item.CurEntry) { // 当前状态
									lo_State = CEntry.getUpdateState(true);
									lo_Entry.UpdateSave(lo_State, 1); // 直接提交
									// ao_Item.Statements.add(lo_State);
								} else { // 其他状态（非当前并需要提交的状态）
									if (lo_EntState == null) {
										lo_EntState = CEntry.getUpdateState(false);
										// ao_Item.Statements.add(lo_EntState);
									}
									lo_Entry.UpdateSave(lo_EntState, 1);
								}
							}
							// 保存状态权限
							if (lo_Entry.EntryType == EEntryType.ActualityEntry && lo_Entry.ID == 0) {
								lo_RhtState = lo_Entry.getSaveEntryRightState(0, li_Type);
								if (lo_RhtState != null) {
									// ao_Item.Statements.add(lo_RhtState);
									lo_Entry.SaveEntryRight(lo_RhtState, 0, li_Type, 1);
								}
							}
							// 保存移动状态
							if (lo_Entry.MoveEntry != null) {
								if (lo_Entry.MoveEntry.IsNewMoveEntry) {
									if (lo_NewMovState == null) {
										lo_NewMovState = lo_Entry.MoveEntry.getSaveState(0, li_Type);
										// ao_Item.Statements.add(lo_NewMovState);
									}
									lo_Entry.MoveEntry.Save(lo_NewMovState, 0, li_Type, 1);
								} else {
									if (lo_UpdateMovState == null) {
										lo_UpdateMovState = lo_Entry.MoveEntry.getSaveState(1, li_Type);
										// ao_Item.Statements.add(lo_UpdateMovState);
									}
									lo_Entry.MoveEntry.Save(lo_UpdateMovState, 1, li_Type, 1);
								}
							}
						}
					}
				}

				// 保存

				// 更新正文附件
				if (ls_TempFiles != "" && ab_SaveDocument) {
					if (ao_Item.DeleteTempFile == 0) {
						if (!TWorkAdmin.writeFileTempToData(lo_Logon, "WorkItem\\Data" + String.valueOf(ao_Item.Cyclostyle.CyclostyleID) + "\\",
								ls_TempFiles, ls_DataFiles, true))
							return false;
					} else {
						if (!TWorkAdmin.writeFileTempToData(lo_Logon, "WorkItem\\Data" + String.valueOf(ao_Item.Cyclostyle.CyclostyleID) + "\\",
								ls_TempFiles, ls_DataFiles, false))
							return false;
						if (!TWorkAdmin.writeFileTempToData(lo_Logon, ao_Item.getRelativelyFilePath(), ls_TempFiles, ls_DataFiles, false))
							return false;
					}
				}
				TWorkAdmin.deleleTempFileName(lo_Logon, ls_DeleteFiles);

				// 保存同步更新的内容

				// 公文流转中保存系统数据库同步更新的内容
				ao_Item.runUpdateStates();

				// transactTrumbleIn(ao_Item); // 处理嵌入情况
				// TransactLaunch(ao_Item); // 处理启动情况

				// 公文流转中保存外部数据库同步更新的内容
				ao_Item.runOperationStates();

				return true;
			} catch (Exception e) {
				ao_Item.Information = "保存公文失败，更新数据失败：" + e.toString();
				return false;
			}
		} catch (Exception ex) {
			ao_Item.Information = "保存公文失败：" + ex.toString();
			return false;
		}
	}

	/**
	 * 调用特定计算
	 * 
	 * @param as_ScriptContent
	 * @throws Exception
	 */
	public static void caculateScriptContent(CWorkItem ao_Item, String as_ScriptContent) throws Exception {
		if (MGlobal.isEmpty(as_ScriptContent))
			return;

		// 初始化二次开发控件[加载公共函数和系统函数]
		ScriptEngine lo_Script = ao_Item.getScript();

		// 计算
		lo_Script.eval(as_ScriptContent);
	}

	/**
	 * 获取水印信息
	 * 
	 * @param ao_WorkItem
	 *            公文实例对象
	 * @return
	 */
	public static String getWaterMarkText(CWorkItem ao_WorkItem) {
		return getWaterMarkText(ao_WorkItem.Logon.getParaValue("WATER_MARK_TEXT"));
	}

	/**
	 * 获取水印信息
	 * 
	 * @param as_WaterMarkText
	 *            水印内容
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getWaterMarkText(String as_WaterMarkText) {
		String ls_Text = as_WaterMarkText;
		Date ld_Date = MGlobal.getNow();

		String ls_Value = MGlobal.dateToString(ld_Date);
		ls_Text = ls_Text.replaceAll("[DATE]", ls_Value);
		ls_Text = ls_Text.replaceAll("[Now]", ls_Value);

		ls_Value = String.valueOf(ld_Date.getYear());
		ls_Text = ls_Text.replaceAll("[YYYY]", ls_Value);
		ls_Text = ls_Text.replaceAll("[yyyy]", ls_Value);
		ls_Text = ls_Text.replaceAll("[YY]", MGlobal.right(ls_Value, 2));
		ls_Text = ls_Text.replaceAll("[yy]", MGlobal.right(ls_Value, 2));

		ls_Value = String.valueOf(ld_Date.getMonth());
		ls_Text = ls_Text.replaceAll("[MM]", MGlobal.right("0" + ls_Value, 2));
		ls_Text = ls_Text.replaceAll("[M]", ls_Value);

		ls_Value = String.valueOf(ld_Date.getDay());
		ls_Text = ls_Text.replaceAll("[dd]", MGlobal.right("0" + ls_Value, 2));
		ls_Text = ls_Text.replaceAll("[d]", ls_Value);

		return ls_Text;
	}

	/**
	 * 增加服务打印内容
	 * 
	 * @param ao_WorkItem
	 *            公文实例对象
	 * @param al_EntryID
	 *            状态标识
	 * @return
	 * @throws SQLException
	 */
	public static int appendPrint(CWorkItem ao_WorkItem, int al_EntryID) throws SQLException {
		int ll_Seq = ao_WorkItem.PublicFunc().getSeqValue("WorkFlowPrint_ID", "公文表单打印流水号");
		String ls_Sql = "INSERT INTO WorkFlowPrint " + "(ID, WorkItemID, PrintTempID, FileName, Domain, IfPrint, CreateTime, "
				+ "PrintTime, UserName, Remark, PrintType, Parameter)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return CSqlHandle.getJdbcTemplate().update(
				ls_Sql,
				new Object[] { ll_Seq, ao_WorkItem.WorkItemID, al_EntryID, null, ao_WorkItem.Logon.getdbDomain(), 0, MGlobal.getSqlTimeNow(), null,
						ao_WorkItem.GlobalPara().UserName, null, 1, null });
		// PreparedStatement lo_State = ao_WorkItem.SqlHandle().getState(ls_Sql,
		// 2);
		// int li_Index = 1;
		// lo_State.setInt(li_Index++, ll_Seq);
		// lo_State.setInt(li_Index++, ao_WorkItem.WorkItemID);
		// lo_State.setInt(li_Index++, al_EntryID);
		// lo_State.setString(li_Index++, null);
		// lo_State.setString(li_Index++, ao_WorkItem.Logon.getdbDomain());
		// lo_State.setInt(li_Index++, 0);
		// lo_State.setTimestamp(li_Index++, MGlobal.getSqlTimeNow());
		// lo_State.setTimestamp(li_Index++, null);
		// lo_State.setString(li_Index++, ao_WorkItem.GlobalPara().UserName);
		// lo_State.setString(li_Index++, null);
		// lo_State.setInt(li_Index++, 1);
		// lo_State.setString(li_Index++, null);
		// if (lo_State.executeUpdate() == 0)
		// ll_Seq = 0;

		// lo_State.close();
		// lo_State = null;

		// return ll_Seq;
	}

	/**
	 * 记录附件修改日志
	 * 
	 * @param ao_Document
	 *            附件对象
	 * @param ai_Type
	 *            记录类型：=0-新建（增加）；=1-删除；=2-修改；
	 */
	public static void recordDocumentLog(CDocument ao_Document, int ai_Type) {
		String ls_Title, ls_Pos;
		switch (ai_Type) {
		case 1:
			ls_Pos = "TWorkItem->DeleteDocument";
			ls_Title = "删除附件[{0}]";
			break;
		case 2:
			ls_Pos = "TWorkItem->UpdateDocument";
			ls_Title = "更新附件[{0}]";
			break;
		default:
			ls_Pos = "TWorkItem->InsertDocument";
			ls_Title = "增加附件[{0}]";
			break;
		}

		String ls_Content = ao_Document.Logon.getParaValue("RECORD_WORKITEM_DOCUMENT_LOG");
		if (!MGlobal.isNumber(ls_Content))
			ls_Content = "3";

		int li_Type = Integer.parseInt(ls_Content);
		if (ao_Document.DocumentType < EDocumentType.CommondDocument) {
			if ((li_Type & 1) == 0)
				return;
			ls_Pos = ls_Pos.replaceAll("Document", "Body");
			ls_Title = ls_Title.replaceAll("附件", "正文");
		} else {
			if ((li_Type & 2) == 0)
				return;
		}

		ls_Title = ls_Title.replaceAll("{0}", ao_Document.DocumentName);
		ls_Content = MessageFormat.format("WorkItemID={0};CyclostyleID={1};DocumentID={2}",
				String.valueOf(ao_Document.Cyclostyle.WorkItem.WorkItemID), String.valueOf(ao_Document.Cyclostyle.CyclostyleID),
				String.valueOf(ao_Document.DocumentID));

		ao_Document.Logon.recordKeyLog(ls_Pos, ls_Title, ls_Content);
	}

	/**
	 * 刷新并发处理数据
	 * 
	 * @param ao_Item
	 */
	private static void refreshInterData(CWorkItem ao_Item) {
		try {
			// 判断是否是并发步骤
			if (ao_Item.CurEntry.getIsInterEntry() == false)
				return;

			boolean lb_Boolean = false;

			// 判断在当前公文处理人打开过程中是否有其他人打开处理过
			String ls_Sql = "SELECT EditDate FROM WorkItemInst WHERE WorkItemID = " + String.valueOf(ao_Item.WorkItemID);
			ls_Sql = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
			if (!ls_Sql.equals("")) {
				lb_Boolean = (MGlobal.stringToDate(ls_Sql).getTime() > ao_Item.EditDate.getTime());
			}
			if (!lb_Boolean)
				return;

			// 获取状态变化
			ls_Sql = "SELECT * FROM EntryInst WHERE WorkItemID = " + String.valueOf(ao_Item.WorkItemID);
			List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
			for (Map<String, Object> row : lo_Set) {
				CEntry lo_Entry = ao_Item.getEntryById(MGlobal.readInt(row, "EntryID"));
				if (lo_Entry == null) {
					lo_Entry = new CEntry(ao_Item.Logon);
					lo_Entry.WorkItem = ao_Item;
					lo_Entry.Open(row, 1);
					ao_Item.Entries.put(lo_Entry.EntryID, lo_Entry);
					if (lo_Entry.EntryID > ao_Item.EntryMaxID) {
						ao_Item.EntryMaxID = lo_Entry.EntryID;
					}
				} else {
					if (lo_Entry != ao_Item.CurEntry && lo_Entry.EntryType == EEntryType.ActualityEntry) {
						if (lo_Entry.EntryStatus != MGlobal.readInt(row, "StateID")) {
							lo_Entry.Open(row, 1);
							lo_Entry.EntryChanged = true;
						}
					}
				}
			}
			lo_Set = null;

			CTransact lo_Transact = ao_Item.CurEntry.getActivity().Transact;

			// 获取表头属性变化
			if (!lo_Transact.InterPropIDs.equals("")) {
				ls_Sql = "SELECT ContentValue FROM WorkItemContent WHERE WorkItemID = " + String.valueOf(ao_Item.WorkItemID)
						+ " AND ContentIndex = 'PropValue'";
				ls_Sql = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
				for (CProp lo_Prop : ao_Item.Cyclostyle.Props) {
					if ((";" + lo_Transact.InterPropIDs).indexOf(";" + String.valueOf(lo_Prop.PropID) + ";") > -1) {
						// lo_Prop.Value=) =
						// System.Convert.ToString(lo_Bag.ReadContent("PropValue"));
					} else {
						// lv_Variant = lo_Bag.ReadContent("PropValue");
					}
				}
			}

			// 获取角色变化
			if (MGlobal.isExist(lo_Transact.InterRoleIDs)) {
				ls_Sql = "SELECT ContentValue FROM WorkItemContent WHERE WorkItemID = " + String.valueOf(ao_Item.WorkItemID)
						+ " AND ContentIndex = 'PropValue'";
				ls_Sql = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
				// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
				// ADODB.CursorTypeEnum.adOpenKeyset,
				// ADODB.LockTypeEnum.adLockReadOnly, -1);
				// lo_Bag.Content =
				// System.Convert.ToString(lo_Rec.Fields["ContentValue"].GetChunk(lo_Rec.Fields["ContentValue"].ActualSize));
				// lo_Bag.Position = 1;
				// i =
				// System.Convert.ToInt16(lo_Bag.ReadContent("theRolesCount"));
				for (CRole lo_Role : ao_Item.Cyclostyle.Roles) {
					if ((";" + lo_Transact.InterRoleIDs).indexOf(";" + String.valueOf(lo_Role.RoleID) + ";") > -1) {
						// lo_Role.Value =
						// System.Convert.ToString(lo_Bag.ReadContent("RoleValue"));
					} else {
						// lv_Variant = lo_Bag.ReadContent("RoleValue");
					}
				}
			}

			// 执行二次开发函数
			caculateScriptContent(ao_Item, lo_Transact.InterScript);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 保存扩展字段的值
	 * 
	 * @param ao_Item
	 * @return
	 */
	private static boolean saveExtendValue(CWorkItem ao_Item) {
		try {
			// 保存公文处理有效周期
			ao_Item.ValidLimited = ao_Item.PublicFunc().getWorkdays(ao_Item.CreateDate, ao_Item.EditDate, 7);

			String ls_Sql = "SELECT * FROM SystemFieldCollate";
			List<Map<String, Object>> lo_Collate = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);

			// 初始化二次开发控件[加载公共函数和系统函数]
			ao_Item.getScript();

			Document lo_Xml = ao_Item.Cyclostyle.getExtendFieldsXml();

			NodeList lo_List = lo_Xml.getDocumentElement().getElementsByTagName("Field");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				String ls_Text = lo_Node.getAttribute("Code");
				if (ls_Text.equals("WorkItemSecret")) {// 公文秘密程度
					if (MGlobal.isExist(lo_Node.getAttribute("Source")))
						ao_Item.Secutity = Integer.parseInt(getExtendFieldValue(ao_Item, lo_Node, lo_Collate).toString());
				} else if (ls_Text.equals("ExtendOne")) { // 扩展字段一
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendOne = Integer.parseInt(getExtendFieldValue(ao_Item, lo_Node, lo_Collate).toString());
					}
				} else if (ls_Text.equals("ExtendTwo")) { // 扩展字段二
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendTwo = Integer.parseInt(getExtendFieldValue(ao_Item, lo_Node, lo_Collate).toString());
					}
				} else if (ls_Text.equals("ExtendThree")) { // 扩展字段三
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendThree = (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate);
					}
				} else if (ls_Text.equals("ExtendFour")) { // 扩展字段四
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendFour = (Date) getExtendFieldValue(ao_Item, lo_Node, lo_Collate);
					}
				} else if (ls_Text.equals("ExtendFive")) { // 扩展字段五
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendFive = (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate);
					}
				} else if (ls_Text.equals("ExtendSix")) { // 扩展字段六
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendSix = (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate);
					}
				} else if (ls_Text.equals("ExtendSeven")) { // 扩展字段七
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendSeven = (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate);
					}
				} else if (ls_Text.equals("ExtendEight")) { // 扩展字段八
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendEight = (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate);
					}
				} else if (ls_Text.equals("ExtendNine")) { // 扩展字段九
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendNine = (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate);
					}
				} else if (ls_Text.equals("ExtendTen")) { // 扩展字段十
					if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
						ao_Item.ExtendTen = (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate);
					}
				} else { // 扩展字段FI1~FI5,FSA1~FSA5,FSB1~FSB5
					if (MGlobal.left(lo_Node.getAttribute("Code"), 2).equals("FI")) {
						if (MGlobal.isExist(lo_Node.getAttribute("Source"))) {
							int li_Index = Integer.valueOf(MGlobal.right(lo_Node.getAttribute("Code"), 1));
							ao_Item.setExtendFI(li_Index, Integer.parseInt(getExtendFieldValue(ao_Item, lo_Node, lo_Collate).toString()));
						} else if (MGlobal.left(lo_Node.getAttribute("Code"), 3).equals("FSA")) {
							int li_Index = Integer.valueOf(MGlobal.right(lo_Node.getAttribute("Code"), 1));
							ao_Item.setExtendFSA(li_Index, (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate));
						} else if (MGlobal.left(lo_Node.getAttribute("Code"), 3).equals("FSB")) {
							int li_Index = Integer.valueOf(MGlobal.right(lo_Node.getAttribute("Code"), 1));
							ao_Item.setExtendFSB(li_Index, (String) getExtendFieldValue(ao_Item, lo_Node, lo_Collate));
						} else {
						}
					}
				}
			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取扩展字段值
	 * 
	 * @param ao_Item
	 * @param ao_Node
	 * @param ao_Set
	 * @return
	 */
	private static Object getExtendFieldValue(CWorkItem ao_Item, Element ao_Node, List<Map<String, Object>> ao_Set) {
		try {
			if (ao_Node.getAttribute("Type").equals("0")) {
				String ls_Script = getAnalisysScript(ao_Item, ao_Set, ao_Node.getAttribute("Source"));
				if (MGlobal.isExist(ls_Script)) {
					return ao_Item.caculateScriptContentEval(ls_Script);
				}
			} else if (ao_Node.getAttribute("Type").equals("1")) {
				return ao_Item.caculateScriptContentEval(ao_Node.getAttribute("Source"));
			} else if (ao_Node.getAttribute("Type").equals("2")) {
				return CSqlHandle.getJdbcTemplate().queryForMap(ao_Node.getAttribute("Source")).get(0);
			}

			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析简单计算，只需与加减乘除整除，其他运算不许存在
	 */
	private static String getAnalisysScript(CWorkItem ao_Item, List<Map<String, Object>> ao_Set, String as_FieldValue) {
		String ls_Value = " &+-*/\\()^,";
		String ls_Content = as_FieldValue;
		for (int i = 0; i < ls_Value.length(); i++) {
			ls_Content = ls_Content.replaceAll(ls_Value.substring(i, i + 1), "&");
		}

		String ls_Result = "";
		int j = 1;
		for (int i = 1; i <= ls_Content.length() + 1; i++) {
			if (ls_Content.substring(i - 1, 1) == "&" || (i == (ls_Content.length() + 1) && j < i)) {
				ls_Value = ls_Content.substring(j - 1, i - 1);
				if (MGlobal.left(ls_Value, 1).equals("[") && MGlobal.right(ls_Value, 1).equals("]")) {// #系统字段

					String ls_Str = getScriptContent(ao_Set, ls_Value);
					ls_Result += (ls_Str.equals("") ? ls_Value : ls_Str);
				} else if (MGlobal.left(ls_Value, 1).equals("{") && MGlobal.right(ls_Value, 1).equals("}")) {// #系统角色
					CRole lo_Role = ao_Item.Cyclostyle.getRoleByName(ls_Value.substring(1, ls_Value.length() - 1));
					if (lo_Role == null) {
						ls_Result += "\"\"";
					} else {
						ls_Result += "theWorkItem.getRoleValue(\"" + ls_Value.substring(1, ls_Value.length() - 1) + "\", 1)";
					}
				} else {// #表头属性
					CProp lo_Prop = ao_Item.Cyclostyle.getPropByName(ls_Value);
					if (lo_Prop == null) {
						ls_Result += ls_Value;
					} else {
						ls_Result += "theWorkItem.getPropValue(\"" + ls_Value + "\")";
					}
				}
				if (i < (ls_Content.length() + 1)) {
					ls_Result += as_FieldValue.substring(i - 1, i);
				}
				j = i + 1;
			}
		}

		return ls_Result;
	}

	/**
	 * 获取扩展字段开发内容
	 * 
	 * @param ao_Set
	 *            结果集
	 * @param as_FieldValue
	 *            字段值
	 * @return
	 */
	private static String getScriptContent(List<Map<String, Object>> ao_Set, String as_FieldValue) {
		try {
			// boolean lb_Boolean = ao_Set.first();
			for (Map<String, Object> row : ao_Set) {
				if (MGlobal.readString(row, "FieldName").equals("FieldName")) {
					return MGlobal.readString(row, "SystemValue");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 把一个文件备份到备份目录下
	 * 
	 * @param as_FileName
	 *            不包括绝对路径，但包括相对路径
	 * @return
	 */
	private static boolean backFileToBpFolder(CWorkItem ao_Item, String as_FileName) {
		String ls_Path = ao_Item.Logon.SaveDataPath;
		if (MFile.existFile(ls_Path + as_FileName)) {
			Date ld_Date = MGlobal.getNow();
			// 判断目标文件夹是否存在
			String ls_Name = ls_Path + "Back\\";
			MFile.createFolder(ls_Name);
			ls_Name += "F" + MGlobal.dateToString(ld_Date, "yyyyMM") + "\\";
			MFile.createFolder(ls_Name);
			ls_Name += "F" + MGlobal.dateToString(ld_Date, "dd") + "\\";
			MFile.createFolder(ls_Name);
			String ls_Ext = MFile.getFileExt(ls_Path + as_FileName);
			ls_Path = ls_Name;
			int i = 0;
			ls_Name = "F" + MGlobal.dateToString(ld_Date, "hhmmss");
			if (MFile.existFile(ls_Path + ls_Name + "." + ls_Ext)) {
				i = 1;
				while (i > 0) {
					if (MFile.existFile(ls_Path + ls_Name + String.valueOf(i) + "." + ls_Ext)) {
						i++;
					} else {
						ls_Name = ls_Path + ls_Name + String.valueOf(i) + "." + ls_Ext;
						i = 0;
					}
				}
			} else {
				ls_Name = ls_Path + ls_Name + "." + ls_Ext;
			}

			MFile.copyFile(ao_Item.Logon.SaveDataPath + as_FileName, ls_Name);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 处理实例
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean sendWorkItem(CWorkItem ao_Item) throws Exception {
		return sendWorkItem(ao_Item, 0, 0, null);
	}

	/**
	 * 处理实例
	 * 
	 * @param ai_Type
	 *            发送处理类型(=0-缺省；=1-发送处理类型；=2-处理转发到一个指定的步骤；=3-正常发送时的意见选项；=4-
	 *            处理意见的内容；=5-转发到具体人员；)
	 * @return
	 * @throws Exception
	 */
	public static boolean sendWorkItem(CWorkItem ao_Item, int ai_Type) throws Exception {
		return sendWorkItem(ao_Item, ai_Type, 0, null);
	}

	/**
	 * 处理实例
	 * 
	 * @param ai_Type
	 *            发送处理类型(=0-缺省；=1-发送处理类型；=2-处理转发到一个指定的步骤；=3-正常发送时的意见选项；=4-
	 *            处理意见的内容；=5-转发到具体人员；)
	 * @param al_Value
	 *            ai_Type=1时意见选择；ai_Type=2时转发到步骤标识；ai_Type=3时意见选择；
	 * @param as_Value
	 *            ai_Type=4时意见的内容；ai_Type=5收件人；
	 * @return
	 * @throws Exception
	 */
	public static boolean sendWorkItem(CWorkItem ao_Item, int ai_Type, int al_Value, String as_Value) throws Exception {
		try {
			// 记录日志
			ao_Item.Record("sendWorkItem", getWrkLogPara(ao_Item));

			if (!ao_Item.WorkItemAccess) {
				// 错误【1034】：当前公文实例为只读，不能处理
				ao_Item.Raise(1034, "sendWorkItem", getWrkLogPara(ao_Item));
				return false;
			}

			// int li_TransactType = 0; // 发送处理类型[0——正常发送、1——转发到一个指定的步骤]
			int li_Result = 0; // 流转结果=-1-流转结束；=0-流转不成功；=1-流转成功；
			ao_Item.LaunchParameter = "";

			CActivity lo_CurAct = ao_Item.CurActivity;
			CEntry lo_CurEntry = ao_Item.CurEntry;

			// 参数设置[由页面处理传入]
			switch (ai_Type) {
			case 1: // 发送处理类型
				if (al_Value == 0) {// 正常处理
					if (lo_CurAct.Transact.getChoicesCount() == 0) {
						lo_CurEntry.setChoice(-1); // 没有意见选项
					} else {
						lo_CurEntry.setChoice(1); // 缺省使用第一个意见选项
					}
				} else if (al_Value == 1) {// 转发
					lo_CurEntry.setChoice(0);
				} else { // 内部处理
					lo_CurEntry.setChoice(-1);
				}
				break;
			case 2:// 处理转发到一个指定的步骤
				lo_CurEntry.TransmitActivityID = al_Value;
				break;
			case 3: // 正常发送时的意见选项
				lo_CurEntry.setChoice(al_Value);
				break;
			case 4: // 处理意见的内容
				lo_CurEntry.setResponseContent(as_Value);
				break;
			case 5:
				lo_CurAct.Flow.getActivityById(al_Value).Participant.Participant = as_Value;
				lo_CurAct.Flow.Changed = true;
				break;
			}

			// 并发步骤处理
			refreshInterData(ao_Item);

			String ls_TransmitActivityNames = "";
			CActivity lo_Activity = null;

			// 处理转发或内部处理情况
			switch (lo_CurEntry.Choice) {
			case -2: // 没有意见选项
				if (lo_CurAct.Transact.getChoicesCount() > 0) {
					// 错误【1035】：未选取处理意见
					ao_Item.Information = "错误【1035】：未选取处理意见";
					ao_Item.Raise(1035, "sendWorkItem", getWrkLogPara(ao_Item));
					return false;
				}
				break;
			case -1: // 内部处理
				if (lo_CurAct.ActivityType == EActivityType.StartActivity) {
					// 错误【1036】：开始步骤不可以做内部转发
					ao_Item.Information = "错误【1036】：开始步骤不可以做内部转发";
					ao_Item.Raise(1036, "sendWorkItem", getWrkLogPara(ao_Item));
					return false;
				}
				lo_Activity = lo_CurAct;
				break;
			case 0: // 转发
				if (lo_CurAct.ActivityType == EActivityType.TumbleInActivity || lo_CurAct.ActivityType == EActivityType.LaunchActivity) {
					// 正常处理
				} else {
					if (lo_CurEntry.TransmitActivityID > 0) {// 转发到已存在的实际步骤
						lo_Activity = ao_Item.CurFlow.getActivityById(lo_CurEntry.TransmitActivityID);
					} else {// 转发到未确定的特殊步骤
						lo_Activity = ao_Item.CurFlow.insertSpecialActivity(lo_CurEntry.TransmitActivityID, lo_CurAct);
						lo_Activity.Participant.Participant = lo_Activity.Participant.Participant + ao_Item.TransParticipant;
					}
				}
				break;
			default: // 正常处理
				break;
			}

			// 发送时(流转处理前)----调用计算函数[二次开发]
			caculateScript(ao_Item, ECaculateStatusType.CaculateSendStatus);

			caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuBeforeSendForAll));
			if (lo_CurEntry.isFirstTrans()) {
				caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuBeforeSaveForFirst));
			}

			if (lo_CurAct.ActivityType == EActivityType.TumbleInActivity || lo_CurAct.ActivityType == EActivityType.LaunchActivity) {
				lo_CurEntry.setEntryStatus(EEntryStatus.SucceedFlowStatus);
			} else {
				lo_CurEntry.setEntryStatus(EEntryStatus.HadTransactStatus);
			}

			// 计算处理额外状态设置情况
			transactSuperStatus(ao_Item, false, EEntryStatus.SystemTransactStatus);

			// 计算同一用户在不同步骤同时具有处理公文的状态时的处理设置
			transactSameUserStatus(ao_Item);

			ao_Item.Information = "";
			ao_Item.CacuActIDs = ";";

			String[] ls_ActivityArrayIDs = null;
			int li_Index = 0;
			if (lo_Activity == null) {
				// 以下应用处理正常情况
				ao_Item.FlowSucceedSplits = ";";
				li_Result = autoCaculate(ao_Item, lo_CurAct, null);
				if (li_Result == 0) {
					ao_Item.Raise(9999, "sendWorkItem", "发送公文失败：" + ao_Item.Information);
					return false;
				}
			} else {
				// 以下程序段处理特殊情况
				li_Result = transactActivitySend(ao_Item, lo_Activity, null);
				if (li_Result == 0) {
					ao_Item.Raise(9999, "sendWorkItem", "发送公文失败：" + ao_Item.Information);
					return false;
				}
				ls_TransmitActivityNames = lo_Activity.ActivityName;
				if (li_Result == 1 && lo_CurEntry.Choice == 0 && MGlobal.isExist(ao_Item.TransmitActivityIDs)) {// 转发给多个步骤情况
					ls_ActivityArrayIDs = ao_Item.TransmitActivityIDs.split(";");
					for (li_Index = 0; li_Index < ls_ActivityArrayIDs.length; li_Index++) {
						if (MGlobal.isExist(ls_ActivityArrayIDs[li_Index])
								&& !ls_ActivityArrayIDs[li_Index].equals(String.valueOf(lo_CurEntry.TransmitActivityID))) {
							if (Integer.parseInt(ls_ActivityArrayIDs[li_Index]) > 0) {
								lo_Activity = ao_Item.CurFlow.getActivityById(Integer.getInteger(ls_ActivityArrayIDs[li_Index]));
							} else {
								lo_Activity = ao_Item.CurFlow.insertSpecialActivity(Integer.parseInt(ls_ActivityArrayIDs[li_Index]), lo_CurAct);
								lo_Activity.Participant.Participant = lo_Activity.Participant.Participant + ao_Item.TransParticipant;
							}
							li_Result = transactActivitySend(ao_Item, lo_Activity, null);
							if (li_Result == 0) {
								ao_Item.Raise(9999, "sendWorkItem", "发送公文失败：" + ao_Item.Information);
								return false;
							}
							ls_TransmitActivityNames += "、" + lo_Activity.ActivityName;
							if (li_Result == -1) {
								break;
							}
						}
					}
				}
				lo_CurEntry.setResponseImageContent(lo_CurEntry.getResponseImageContent() + "(" + ls_TransmitActivityNames + ")");
			}

			if (ao_Item.Information.equals("")) {
				ao_Item.Information = "发送成功";
			}

			// 发送时(流转处理后)----调用计算函数[二次开发] - 针对属性和角色
			caculateScript(ao_Item, ECaculateStatusType.CaculateSendAfterStatus);

			if (li_Result == -1) {// 公文流转结束
				caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuStopWorkItem));
				if (MGlobal.isExist(ao_Item.WorkItemSource)) {// 是本地嵌入或远程启动子公文
					transactWorkItemReturn(ao_Item);
				}
			}

			// 保存扩展字段的值
			saveExtendValue(ao_Item);

			// 保存数据
			boolean lb_Boolean = saveWorkItemData(ao_Item, true);

			// 流转处理并保存数据后 - 针对统一权限分配
			caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuAfterSendForAll));
			if (lo_CurEntry.isFirstTrans()) {
				caculateScriptContent(ao_Item, ao_Item.CurRight.getScriptCacuContent(EScriptCaculateType.CacuAfterSendForFirst));
			}

			// 解除公文锁
			if (ao_Item.WorkItemID > 0) {
				TWorkAdmin.releaseLock(ao_Item.Logon, ao_Item.WorkItemID, lo_CurEntry.EntryID);
			}

			return lb_Boolean;
		} catch (Exception ex) {
			ao_Item.Information = ex.getMessage();
			ao_Item.Raise(9999, "sendWorkItem", "发送公文失败：" + ex.getMessage());
			return false;
		}
	}

	/**
	 * 计算处理额外状态设置情况
	 * 
	 * @param ao_Item
	 * @param ab_Forced
	 *            是否强制设置
	 * @param ai_TransactStatus
	 *            强制设置状态情况
	 */
	private static void transactSuperStatus(CWorkItem ao_Item, boolean ab_Forced, int ai_TransactStatus) {
		try {
			if (ao_Item.CurActivity == null) {
				return;
			}
			if (ao_Item.CurActivity.ActivityType != EActivityType.TransactActivity) {
				return;
			}

			CEntry lo_ParentEntry = ao_Item.CurEntry.ParentEntry;
			if (lo_ParentEntry == null) {
				return;
			}
			if (lo_ParentEntry.ChildEntries.size() == 1) {
				return;
			}

			int li_Transact = 0;
			int li_LimitedNum = 0;

			if (ab_Forced) {
				li_Transact = 1; // 强制处理
			} else {
				if (ao_Item.CurActivity.Transact.TransLimit == EStatusTransLimitType.NotAnyLimit) {
					li_Transact = 0; // 不处理
					return;
				} else {
					if (ao_Item.CurActivity.Transact.TransLimit == ELimitCompareType.LimitCompareNumber) {
						li_LimitedNum = Integer.parseInt(ao_Item.CurActivity.Transact.CompareCont);
					} else {
						li_LimitedNum = (int) (0.5 + (Double.parseDouble(ao_Item.CurActivity.Transact.CompareCont)
								* lo_ParentEntry.ChildEntries.size() / 100));
					}
					if (ao_Item.CurActivity.Transact.TransLimit == EStatusTransLimitType.RoleTransMaxLimit) {
						li_Transact = 2; // 按角色处理
					} else {
						li_Transact = 3; // 按所有处理
					}
				}
			}

			// 强制处理
			if (li_Transact == 1) {
				for (CEntry lo_Entry : lo_ParentEntry.ChildEntries) {
					if (lo_Entry.EntryStatus == EEntryStatus.NotTransactStatus || lo_Entry.EntryStatus == EEntryStatus.HadReadStatus
							|| lo_Entry.EntryStatus == EEntryStatus.TransactingStatus) {
						lo_Entry.setEntryStatus(ai_TransactStatus);
					}
				}
				return;
			}

			// 需要所有的都处理
			if (li_LimitedNum >= lo_ParentEntry.ChildEntries.size()) {
				return;
			}

			// 按所有处理
			if (li_Transact == 3) {
				int i = 0;
				for (CEntry lo_Entry : lo_ParentEntry.ChildEntries) {
					if (lo_Entry.EntryStatus == EEntryStatus.NotTransactStatus || lo_Entry.EntryStatus == EEntryStatus.HadReadStatus
							|| lo_Entry.EntryStatus == EEntryStatus.TransactingStatus) {
						i++;
					}
				}
				// 为达到条件
				if (i + li_LimitedNum > lo_ParentEntry.ChildEntries.size()) {
					return;
				}

				for (CEntry lo_Entry : lo_ParentEntry.ChildEntries) {
					if (lo_Entry.EntryStatus == EEntryStatus.NotTransactStatus || lo_Entry.EntryStatus == EEntryStatus.HadReadStatus
							|| lo_Entry.EntryStatus == EEntryStatus.TransactingStatus) {
						lo_Entry.setEntryStatus(ai_TransactStatus);
					}
				}
				return;
			}

			// 按角色处理
			// 判断哪些状态对应的角色满足条件
			String ls_Str = ";[0];";
			int i, j = 0;
			for (CEntry lo_Entry : lo_ParentEntry.ChildEntries) {
				if (lo_Entry.EntryStatus == EEntryStatus.HadTransactStatus || lo_Entry.EntryStatus == EEntryStatus.SystemTransactStatus) {
					if (lo_Entry.RoleName == "") {
						if (ls_Str.indexOf(";[-1];") + 1 == 0) {
							i = ls_Str.indexOf("]") + 1;
							j = Integer.parseInt(ls_Str.substring(2, i - 1));
							if (j + 1 == li_LimitedNum) {
								ls_Str = ls_Str.replaceAll(";[" + j + "];", ";[-1];");
							} else {
								ls_Str = ls_Str.replaceAll(";[" + j + "];", ";[" + j + 1 + "];");
							}
						}
					} else {
						if (ls_Str.indexOf(";" + lo_Entry.RoleName + "[-1];") == -1) {
							i = ls_Str.indexOf(";" + lo_Entry.RoleName + "[") + 1;
							if (i == 0) {
								if (li_LimitedNum == 1) {
									ls_Str += lo_Entry.RoleName + "[-1];";
								} else {
									ls_Str += lo_Entry.RoleName + "[1];";
								}
							} else {
								i += lo_Entry.RoleName.length() + 1;
								j = Integer.parseInt(ls_Str.substring(i - 1, j));
								if (j + 1 == li_LimitedNum) {
									ls_Str = ls_Str.replaceAll(";" + lo_Entry.RoleName + "[" + String.valueOf(j) + "];", ";" + lo_Entry.RoleName
											+ "[-1];");
								} else {
									ls_Str = ls_Str.replaceAll(";" + lo_Entry.RoleName + "[" + String.valueOf(j) + "];", ";" + lo_Entry.RoleName
											+ "[" + String.valueOf(j + 1) + "];");
								}
							}
						}
					}
				}
			}

			// 处理那些满足条件的状态
			for (CEntry lo_Entry : lo_ParentEntry.ChildEntries) {
				if (lo_Entry.EntryStatus == EEntryStatus.NotTransactStatus || lo_Entry.EntryStatus == EEntryStatus.HadReadStatus
						|| lo_Entry.EntryStatus == EEntryStatus.TransactingStatus) {
					if (ls_Str.indexOf(";" + lo_Entry.RoleName + "[-1];") > -1) {
						lo_Entry.setEntryStatus(ai_TransactStatus);
						if (MGlobal.isExist(lo_Entry.getActivity().Transact.ResponseChoices)) {
							if (lo_Entry.getActivity() == ao_Item.CurActivity) {
								lo_Entry.setChoice(ao_Item.CurEntry.Choice);
							} else {
								lo_Entry.setChoice(1);
							}
						}
						lo_Entry.EntryChanged = true;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 计算同一用户在不同步骤同时具有处理公文的状态时的处理设置
	 * 
	 * @param ao_Item
	 */
	private static void transactSameUserStatus(CWorkItem ao_Item) {
		try {
			// 不需要处理
			if (ao_Item.Cyclostyle.SamePartDealType == 0) {
				return;
			}

			String ls_Array[];
			CActivity lo_Activity;
			boolean lb_Boolean;

			for (CEntry lo_Entry : ao_Item.getEntries()) {
				if (lo_Entry != ao_Item.CurEntry) {
					if (lo_Entry.EntryType == EEntryType.ActualityEntry
							&& (lo_Entry.EntryStatus == EEntryStatus.NotTransactStatus || lo_Entry.EntryStatus == EEntryStatus.HadReadStatus || lo_Entry.EntryStatus == EEntryStatus.TransactingStatus)) {
						lo_Activity = lo_Entry.getActivity();
						if (lo_Activity != null) {
							if (lo_Activity.ActivityType == EActivityType.StartActivity || lo_Activity.ActivityType == EActivityType.TransactActivity
									|| lo_Activity.ActivityType == EActivityType.FYIActivity) {
								if (lo_Entry.ParticipantID == ao_Item.CurEntry.ParticipantID
										|| lo_Entry.ParticipantID == ao_Item.CurEntry.ProxyParticipantID) {
									if (ao_Item.Cyclostyle.SamePartDealType == 1) {
										lb_Boolean = true;
									} else {
										lb_Boolean = false;
										if (MGlobal.isExist(ao_Item.Cyclostyle.getSamePartActivities())) {
											ls_Array = ao_Item.Cyclostyle.getSamePartActivities().split("|");
											for (int i = 0; i < ls_Array.length; i++) {
												if ((";" + ls_Array[i] + ";").indexOf(";" + ao_Item.CurEntry.ActivityName + ";") > -1) {
													if ((";" + ls_Array[i] + ";").indexOf(";" + lo_Entry.ActivityName + ";") > -1) {
														lb_Boolean = true;
													}
												}
											}
										}
									}
									if (lb_Boolean) {
										lo_Entry.setEntryStatus(EEntryStatus.HadTransactStatus);
										if (lo_Activity.ActivityType != EActivityType.FYIActivity) {
											if (MGlobal.isExist(lo_Activity.Transact.ResponseChoices)) {
												if (lo_Activity == ao_Item.CurActivity) {
													lo_Entry.setChoice(ao_Item.CurEntry.Choice);
												} else {
													lo_Entry.setChoice(1);
												}
											}
										}
										lo_Entry.EntryChanged = true;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 处理步骤发送情况，返回值如：=0-错误，并在ms_Information设置错误信息；=-1-结束流转；=1-正确流转，
	 * 并在ms_Information返回处理结果；
	 * 
	 * @param ao_Activity
	 * @param ao_Entry
	 * @return
	 */
	private static int transactActivitySend(CWorkItem ao_Item, CActivity ao_Activity, CEntry ao_Entry) {
		try {
			if (ao_Activity == null) {
				// 错误：传递非法参数
				ao_Item.Information = "传递非法参数";
				return 0;
			}

			// 判断是否是流程结束步骤
			if (ao_Activity.ActivityType == EActivityType.StopActivity) {
				ao_Item.Information = "流转结束";
				ao_Item.Status = EWorkItemStatus.HaveFinished;
				ao_Item.FinishedDate = MGlobal.getNow();
				return -1;
			}

			if (ao_Activity.Participant == null) {
				// 错误：非法的步骤处理类型
				ao_Item.Information = "非法的步骤处理类型";
				return 0;
			}

			CEntry lo_RootEntry = null;// 步骤虚拟根节点
			CEntry lo_NewEntry = null;// 新节点
			short li_Type = 0; // =0-使用本模板；=1-使用其他模板；=2-远程启动情况；
			CEntry lo_NewRoot = null;// 嵌入新节点虚拟状态的总节点
			CEntry lo_VNewEntry = null;// 嵌入新节点虚拟状态
			CEntry lo_ANewEntry; // 嵌入新节点虚拟状态
			CFlow lo_Flow = null;// 嵌入步骤使用本模板时的流程
			CActivity lo_Activity = null; // 嵌入步骤使用本模板时的流程开始步骤

			int ll_UserID = 0; // 用户标识
			String ls_UserName = ""; // 用户名称
			int ll_RoleID = 0; // 角色标识
			String ls_RoleName = ""; // 角色名称
			String ls_SendUsers = ""; // 发送触发的用户

			String[] ls_Array = null;
			boolean lb_Boolean = false;
			String ls_Value = "";

			li_Type = -1;
			// 返回用户格式：“用户名称[用户标识]用户角色{角色代号+标识};...”
			// 注：如果返回的用户中存在“U+用户标识”的用户则为非法用户
			// 步骤参与人员
			String ls_Participant = ao_Activity.resolveParticipant(); // 解析步骤参与人员
			if (ls_Participant.equals("")) {
				ls_Participant = getActivityParticipants(ao_Item, ao_Activity);
			}
			ls_Participant = appendCacuParticipant(ao_Item, ls_Participant, ao_Activity);
			if (ls_Participant.equals("")) {
				// 错误：步骤缺少参与人员
				ao_Item.Information = "步骤[" + ao_Activity.ActivityName + "]缺少参与人员";
				return 0;
			}

			// 嵌入或启动参数变量
			String ls_LaunchData = ""; // 嵌入或启动数据
			String ls_FlowID = ""; // 嵌入流程标识
			String ls_TemplateID = ""; // 嵌入公文模板标识
			String ls_Sql = "";

			// 如果是内部嵌入情况，判断是否存在嵌入的公文模板和流程
			if (ao_Activity.ActivityType == EActivityType.TumbleInActivity) {
				if (ao_Activity.TumbleIn.UseParentTemp) {
					// 判断是否存在流程
					ls_TemplateID = String.valueOf(ao_Item.Cyclostyle.CyclostyleID);
					ls_Sql = "SELECT FlowID FROM FlowDefine WHERE TemplateID = " + ls_TemplateID + " AND FlowName = \'"
							+ ao_Activity.TumbleIn.ImpressData.FlowName.replaceAll("\'", "\'\'") + "\'";
					ls_FlowID = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
					if (ls_FlowID.equals("")) {
						lo_Flow = ao_Item.getFlowByID(Integer.parseInt(ls_FlowID));
					}
					if (lo_Flow == null) {
						// 错误：嵌入的流程不存在，请通知管理员确认是否已经修改或删除了相关模板
						ao_Item.Information = "步骤[" + ao_Activity.ActivityName + "]嵌入的流程不存在，请通知管理员确认是否已经修改或删除了相关模板";
						return 0;
					}
					li_Type = 0;
				} else {
					// 判断是否存在公文模板和流程
					ls_TemplateID = String.valueOf(ao_Item.Logon.getWorkAdmin()
							.getCyclostyleIDByName(ao_Activity.TumbleIn.ImpressData.CyclostyleName));
					if (ls_TemplateID.equals("-1")) {
						// 错误：嵌入的公文模板不存在，请通知管理员确认是否已经修改或删除了相关模板
						ao_Item.Information = "步骤[" + ao_Activity.ActivityName + "]嵌入的公文模板不存在，请通知管理员确认是否已经修改或删除了相关模板";
						return 0;
					}
					ls_Sql = "SELECT TemplateID FROM TemplateDefine WHERE TemplateIsValid = 1 AND TemplateID = " + ls_TemplateID;
					if (CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class).equals("")) {
						// 错误：嵌入的公文模板设计不合理，无法继续流转，请通知管理员
						ao_Item.Information = "步骤[" + ao_Activity.ActivityName + "]嵌入的公文模板设计不合理，无法继续流转，请通知管理员";
						return 0;
					}
					if (ao_Activity.TumbleIn.ImpressData.FlowName.equals("")) {
						ls_FlowID = "0";
					} else {
						ls_Sql = "SELECT FlowID FROM FlowDefine WHERE TemplateID = " + ls_TemplateID + " AND FlowName = \'"
								+ ao_Activity.TumbleIn.ImpressData.FlowName.replaceAll("\'", "\'\'") + "\'";
						ls_FlowID = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
						if (ls_FlowID.equals("")) {
							ls_FlowID = "0";
						}
					}
					ls_LaunchData = "<Launch Type=\'0\' FlowID=\'" + String.valueOf(ao_Activity.Flow.FlowID) + "\' ActivityID=\'"
							+ String.valueOf(ao_Activity.ActivityID) + "\' TemplateID=\'" + ls_TemplateID + "\' LaunchFlowID=\'" + ls_FlowID
							+ "\' Users=\'";
					li_Type = 1;
				}
			}

			// 如果是远地启动情况，提取相关参数
			if (ao_Activity.ActivityType == EActivityType.LaunchActivity) {
				ls_LaunchData = "<Launch Type=\'1\' FlowID=\'" + ao_Activity.Flow.FlowID + "\' ActivityID=\'" + ao_Activity.ActivityID
						+ "\' Users=\'";
				li_Type = 2;
			}

			ls_Array = ls_Participant.split(";");
			String ls_Str = ";";
			if (ao_Item.CurEntry.Choice == -1) {// 内部处理
				if (ao_Item.CurEntry.ProxyParticipantID > 0) {// 代理情况
					ls_Str += ao_Item.CurEntry.ProxyParticipantID + ";";
				} else {
					ls_Str += ao_Item.CurEntry.ParticipantID + ";";
				}
			}

			String ls_CreateEntryIDs = ""; // 生成嵌入或启动状态标识连接串
			for (int i = 0; i < ls_Array.length; i++) {
				if (MGlobal.isExist(ls_Array[i])) {
					int j = ls_Array[i].indexOf("[") + 1;
					if (j > 0) {// 排除非法用户
						int k = ls_Array[i].indexOf("]", j);
						ll_UserID = Integer.parseInt(ls_Array[i].substring(j, k));
						// 判断是否在排除的用户之内或者是已处理过的用户
						if (ls_Str.indexOf(";" + ll_UserID + ";") == -1) {
							ls_UserName = ls_Array[i].substring(0, j - 1);
							if (lb_Boolean == false) {
								lb_Boolean = true;
								if (ao_Item.CurEntry.Choice == -1) {// 内部处理
									lo_RootEntry = ao_Item.CurEntry.ParentEntry;
									for (CEntry lo_Entry : lo_RootEntry.ChildEntries) {
										if (lo_Entry.EntryStatus == EEntryStatus.NotTransactStatus
												|| lo_Entry.EntryStatus == EEntryStatus.HadReadStatus
												|| lo_Entry.EntryStatus == EEntryStatus.TransactingStatus) {
											ls_Str += String.valueOf(lo_Entry.ParticipantID) + ";";
										}
									}
									if (ls_Str.indexOf(";" + ll_UserID + ";") == -1) {
										ls_Str += String.valueOf(ll_UserID) + ";";
									}
								} else {
									lo_RootEntry = insertEntry(ao_Item, EEntryType.VirtualEntry, ao_Activity, null, ao_Item.getEntryById(1));
								}
							} else {
								ls_Str += String.valueOf(ll_UserID) + ";";
							}
							j = ls_Array[i].indexOf("{") + 1;
							if (j == 0) {// 判断是否是角色
								ll_RoleID = -1;
								ls_RoleName = "";
							} else {
								ls_RoleName = ls_Array[i].substring(k + 1, j - 1);
								k = ls_Array[i].indexOf("}") + 1;
								ll_RoleID = Integer.parseInt(ls_Array[i].substring(j + 1, k - 1));
								String ls_Sign = ls_Array[i].substring(j, j + 1);
								if (ls_Sign.equals("@")) { // 用户角色
								} else if (ls_Sign.equals("&")) { // 系统角色<拟稿人的>
									ls_RoleName = ao_Item.Creator + "的" + ls_RoleName;
								} else if (ls_Sign.equals("#")) { // 系统角色<当前收件人的>
									ls_RoleName = ao_Item.CurEntry.Participant + "的" + ls_RoleName;
								} else {
								}
							}

							// 插入处理状态
							lo_NewEntry = insertEntry(ao_Item, EEntryType.ActualityEntry, ao_Activity, null, lo_RootEntry);
							lo_NewEntry.setParticipantID2(ll_UserID);
							lo_NewEntry.Participant = ls_UserName;
							lo_NewEntry.RoleID = ll_RoleID;
							lo_NewEntry.RoleName = ls_RoleName;
							// 添加嵌入/启动用户
							if (li_Type > -1) {
								if (li_Type == 0) {
									// 直接使用原模板，可以不增加嵌入流程来处理，直接添加启动流程的步骤状态即可
									lo_Activity = lo_Flow.getActivityById(1);
									lo_NewRoot = insertEntry(ao_Item, EEntryType.VirtualEntry, null, null, null);
									lo_NewRoot.OrginalID = lo_NewEntry.EntryID;
									lo_VNewEntry = insertEntry(ao_Item, EEntryType.VirtualEntry, lo_Activity, null, lo_NewRoot);
									lo_ANewEntry = insertEntry(ao_Item, EEntryType.ActualityEntry, lo_Activity, null, lo_VNewEntry);
									lo_ANewEntry.setParticipantID2(ll_UserID);
									lo_ANewEntry.Participant = ls_UserName;
									lo_ANewEntry.RoleID = ll_RoleID;
									lo_ANewEntry.RoleName = ls_RoleName;
								} else {
									ls_LaunchData += ls_Array[i] + ";";
									ls_CreateEntryIDs += String.valueOf(lo_NewEntry.EntryID) + ";";
								}
							}
							ls_SendUsers += ls_UserName + "、";
						}
					}
				}
			}

			if (lb_Boolean == false) {
				// 错误：步骤缺少参与人员
				ao_Item.Information = "步骤[" + ao_Activity.ActivityName + "]缺少参与人员";
				return 0;
			}

			if (li_Type > 0) {
				ao_Item.LaunchParameter = ao_Item.LaunchParameter + ls_LaunchData + "\' EntryIDs=\'" + ls_CreateEntryIDs + "\' />";
			}

			if (ls_SendUsers.equals("")) {
				ao_Item.Information += "发送成功" + "\n";
			} else {
				ls_SendUsers = ls_SendUsers.substring(0, ls_SendUsers.length() - 1);

				switch (ao_Activity.ActivityType) {
				case EActivityType.StartActivity:
				case EActivityType.TransactActivity:
					ls_Str = ao_Activity.Transact.getSendStyle();
					break;
				case EActivityType.FYIActivity:
					ls_Str = "通知{[<某某某>]}";
					break;
				case EActivityType.TumbleInActivity:
					if (li_Type == 0) {
						ls_Str = "送{[<某某某>]}嵌入子流程[{[<子流程>]}]流转";
					} else {
						ls_Str = "送{[<某某某>]}启动本地子公文[{[<子公文>]}]流转";
					}
					break;
				// 设置嵌入本地子流程流转参数
				case EActivityType.LaunchActivity:
					ls_Str = "送{[<某某某>]}启动远地子公文[{[<子公文>]}]流转";
					break;
				// 设置启动远地流程流转参数
				default:
					ls_Str = "送{[<某某某>]}处理";
					break;
				}
				ls_Str = MGlobal.replace(ls_Str, "{[<某某某>]}", ls_SendUsers);
				ls_Str = MGlobal.replace(ls_Str, "{[<步骤名称>]}", ao_Activity.ActivityName);
				if (li_Type > -1) {
					if (li_Type == 2) {
						ls_Value = ao_Activity.Launch.ImpressData.CyclostyleName;
						int i = ls_Value.lastIndexOf("\\") + 1;
						if (i == 0) {
							ls_Str = MGlobal.replace(ls_Str, "{[<子公文>]}", ls_Value);
						} else {
							ls_Str = MGlobal.replace(ls_Str, "{[<子公文>]}", ls_Value.substring(i, ls_Value.length()));
						}
						ls_Str = ls_Str.replace("{[<子流程>]}", ao_Activity.Launch.ImpressData.FlowName);
					} else {
						ls_Value = ao_Activity.TumbleIn.ImpressData.CyclostyleName;
						int i = ls_Value.lastIndexOf("\\") + 1;
						if (i == 0) {
							ls_Str = MGlobal.replace(ls_Str, "{[<子公文>]}", ls_Value);
						} else {
							ls_Str = MGlobal.replace(ls_Str, "{[<子公文>]}", ls_Value.substring(i, ls_Value.length()));
						}
						ls_Str = MGlobal.replace(ls_Str, "{[<子流程>]}", ao_Activity.TumbleIn.ImpressData.FlowName);
					}
				}
				ao_Item.Information += ls_Str + "\n";
			}

			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * 处理公文流转逻辑
	 * 
	 * @param ao_Activity
	 *            计算步骤
	 * @param ao_Entry
	 *            计算状态
	 * @return
	 */
	private static int autoCaculate(CWorkItem ao_Item, CActivity ao_Activity, CEntry ao_Entry) {
		try {
			CActivity lo_Goal;
			CEntry lo_Root; // 用于嵌入时使用的根节点
			CEntry lo_Entry; // 用于嵌入时使用的新节点

			int li_Result = 1;
			boolean lb_Boolean = false;// 记录根据条件连线是否存在下继步骤
			for (CFlowLine lo_Line : ao_Activity.Flow.FlowLines) {
				if (lo_Line.SourceActivity == ao_Activity) {
					lb_Boolean = true;
					lo_Goal = lo_Line.GoalActivity;
					if (ao_Item.CacuActIDs.indexOf(";" + String.valueOf(lo_Goal.Flow.FlowID) + "." + String.valueOf(lo_Goal.ActivityID) + ";") == -1) {
						if (caculateCondition(ao_Item, lo_Goal.Condition)) {
							ao_Item.CacuActIDs = ao_Item.CacuActIDs + String.valueOf(lo_Goal.Flow.FlowID) + "." + String.valueOf(lo_Goal.ActivityID)
									+ ";";
							switch (lo_Goal.ActivityType) {
							// 分支步骤继续流转
							case EActivityType.SplitActivity:
								// wnp2009/6/5修改逻辑
								// 默认流转成功，记录流转处理节点
								ao_Item.FlowSucceedSplits = ao_Item.FlowSucceedSplits + lo_Goal.ActivityName + ";";
								li_Result = autoCaculate(ao_Item, lo_Goal, ao_Entry);
								// wnp2009/3/6修改逻辑
								if (li_Result < 1) {
									ao_Item.FlowSucceedSplits = ao_Item.FlowSucceedSplits.substring(0, ao_Item.FlowSucceedSplits.length()
											- lo_Goal.ActivityName.length() - 1);
									return li_Result; // 流转结束或流转失败
								}
								break;
							// 嵌入和启动步骤，先处理后再继续流转
							case EActivityType.TumbleInActivity:
							case EActivityType.LaunchActivity:
								li_Result = transactActivitySend(ao_Item, lo_Goal, ao_Entry);
								if (li_Result == 1) {// 继续流转
									li_Result = autoCaculate(ao_Item, lo_Goal, ao_Entry);
								} else {
									return li_Result; // 流转结束或流转失败
								}
								break;
							// 开始、处理和通知步骤只做处理
							case EActivityType.StartActivity:
							case EActivityType.TransactActivity:
							case EActivityType.FYIActivity:
								li_Result = transactActivitySend(ao_Item, lo_Goal, ao_Entry);
								if (li_Result < 1)
									return li_Result;// 流转结束或流转失败
								break;
							// 结束步骤，结束公文流转
							case EActivityType.StopActivity:
								// 判断当前步骤是否是主流程的结束步骤
								lo_Root = ao_Item.getRootEntry(null);
								if (lo_Root.EntryID == 1) {
									ao_Item.Information += "流转结束";
									ao_Item.Status = EWorkItemStatus.HaveFinished;
									ao_Item.FinishedDate = MGlobal.getNow();
									return -1;
								} else {
									ao_Item.Information += "嵌入子流程[" + lo_Goal.Flow.FlowName + "]流转结束" + "\n";
									lo_Entry = ao_Item.getEntryById(lo_Root.ParentID);
									lo_Entry.setEntryStatus(EEntryStatus.SucceedFlowStatus);
									lo_Entry.setChoice(ao_Item.ResultType);
									lo_Entry.setChoiceContent(ao_Item.ResultDescribe);
									lo_Goal = lo_Entry.getActivity();
									// 返回主流程继续流转
									return autoCaculate(ao_Item, lo_Goal, lo_Entry);
								}
							default:
								return 0;
							}
						}
					}
				}
			}

			int ll_OrderID = 0;
			int ll_CurOrderID = 0;
			int li_TransType = 0;// default(enumCurActivityTransType);
			if (!lb_Boolean) {// 根据条件连线未能找到下继步骤情况，使用顺序流转方式
				// 计算当前步骤是否已全部处理
				ao_Item.ResultCount = 0;
				ao_Item.ResultFinishedCount = 0;
				ao_Item.ResultChoice = 0;
				ao_Item.ResultCompareContent = "";
				ao_Item.ResultCompareType = EConditionCompareType.EqualToType;
				caculateActivityEntry(ao_Item, ao_Activity);

				if (ao_Item.ResultCount == ao_Item.ResultFinishedCount && ao_Item.ResultCount > 0) {
					if (ao_Activity == ao_Item.CurActivity) {
						li_TransType = ao_Item.TransType;
					} else {
						li_TransType = ECurActivityTransType.CommondTransType;
					}
					switch (li_TransType) {
					case ECurActivityTransType.CommondTransType:
						ll_CurOrderID = ao_Activity.OrderID;
						ll_OrderID = -1;
						for (CActivity lo_Act : ao_Activity.Flow.Activities) {
							lo_Goal = lo_Act;
							if (lo_Goal.OrderID > ll_CurOrderID && lo_Goal.getTransType() == ECurActivityTransType.CommondTransType) {
								if (!(lo_Goal.ActivityType == EActivityType.FYIActivity || lo_Goal.ActivityType == EActivityType.SplitActivity)) {
									if (ll_OrderID > lo_Goal.OrderID || ll_OrderID == -1) {
										ll_OrderID = lo_Goal.OrderID;
									}
								}
							}
						}

						if (ll_OrderID == -1) {// 流转结束
							li_Result = -1;
							ao_Item.Information += "流转结束";
							ao_Item.Status = EWorkItemStatus.HaveFinished;
							ao_Item.FinishedDate = MGlobal.getNow();
						} else {
							lo_Goal = ao_Activity.Flow.getActivityByOrder(ll_OrderID);
							ao_Item.CacuActIDs = ao_Item.CacuActIDs + String.valueOf(lo_Goal.Flow.FlowID) + "." + String.valueOf(lo_Goal.ActivityID)
									+ ";";
							li_Result = transactActivitySend(ao_Item, lo_Goal, ao_Entry);
						}
						break;
					case ECurActivityTransType.SendPreTransType:
						// 送回给发送步骤
						lo_Goal = ao_Activity.Flow.getActivityById(ao_Item.TransActivityID);
						ao_Item.CacuActIDs = ao_Item.CacuActIDs + String.valueOf(lo_Goal.Flow.FlowID) + "." + String.valueOf(lo_Goal.ActivityID)
								+ ";";
						li_Result = transactActivitySend(ao_Item, lo_Goal, ao_Entry);
						break;
					case ECurActivityTransType.SendNextTransType:
						// 按照原转发过来步骤情况继续发送
						lo_Goal = ao_Activity.Flow.getActivityById(ao_Item.TransActivityID);
						li_Result = autoCaculate(ao_Item, lo_Goal, ao_Entry);
						break;
					case ECurActivityTransType.SpecialTransType:
						// 该种情况在特殊转发时处理
						break;
					}
				}
			}

			return li_Result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * 计算步骤状态完成的个数情况
	 * 
	 * @param ao_Activity
	 *            来源步骤
	 */
	private static void caculateActivityEntry(CWorkItem ao_Item, CActivity ao_Activity) {
		try {
			ao_Item.ResultCount = 0;
			ao_Item.ResultFinishedCount = 0;
			if (ao_Activity == null) {
				return;
			}

			int ll_ParentEntryID = 0;
			switch (ao_Item.GlobalPara().FlowArithmeticType) {
			case EFlowArithmeticType.ArithmeticAllEntry: // #计算步骤所对应的所有状态
				break;
			case EFlowArithmeticType.ArithmeticCurrentEntry: // #计算与步骤所对应的当前状态同时触发的状态
				if (ao_Activity == ao_Item.CurActivity) {// 当条件来源步骤为当前步骤时
					ll_ParentEntryID = ao_Item.CurEntry.ParentEntry.EntryID;
				} else {// 计算方式采用第一种方式计算
					// ll_ParentEntryID = 0;
				}
				break;
			case EFlowArithmeticType.ArithmeticLastEntry: // #计算与步骤最近触发状态同时触发的状态
				ll_ParentEntryID = 0;
				for (int i = ao_Item.Entries.size(); i > 0; i--) {
					CEntry lo_Entry = ao_Item.Entries.get(i - 1);
					if (lo_Entry.EntryType == EEntryType.ActualityEntry) {
						if (lo_Entry.ActivityID == ao_Activity.ActivityID) {
							ll_ParentEntryID = lo_Entry.ParentEntry.EntryID;
							break;
						}
					}
				}
				break;
			}

			ArrayList<CEntry> lo_Coll = getLastFlowEntriesByActivity(ao_Item, ao_Activity, ao_Item.CurEntry.ParentID);
			for (CEntry lo_Entry : lo_Coll) {
				switch (lo_Entry.EntryStatus) {
				case EEntryStatus.TumbleInLaunchStatus:
				case EEntryStatus.SucceedFlowStatus:
				case EEntryStatus.FailFlowStatus:
				case EEntryStatus.SucceedRecieved:
					ao_Item.ResultCount++;
					if ((lo_Entry.EntryStatus == EEntryStatus.TumbleInLaunchStatus && ao_Item.ResultChoice == 0)
							|| (lo_Entry.EntryStatus == EEntryStatus.SucceedFlowStatus && ao_Item.ResultChoice == 1)
							|| (lo_Entry.EntryStatus == EEntryStatus.FailFlowStatus && ao_Item.ResultChoice == 2)
							|| (lo_Entry.EntryStatus == EEntryStatus.SucceedRecieved && ao_Item.ResultChoice == 3)
							|| (lo_Entry.EntryStatus == EEntryStatus.FailRecieved && ao_Item.ResultChoice == 4)) {
						if (ao_Item.ResultCompareContent.equals("")) {// 需要比较
							if (compareFinishedEntry(Integer.parseInt(ao_Item.ResultCompareContent), ao_Item.ResultCompareType, lo_Entry.Choice,
									EPropDataType.IntegerDataType)) {
								ao_Item.ResultFinishedCount++;
							}
						} else {
							ao_Item.ResultFinishedCount++;
						}
					}
					break;

				default:
					if (lo_Entry.EntryStatus <= EEntryStatus.HadTransactStatus) {
						if (ll_ParentEntryID == 0) {
							ao_Item.ResultCount++;
							if (lo_Entry.EntryStatus == EEntryStatus.HadTransactStatus) {
								if (lo_Entry.Choice == ao_Item.ResultChoice || ao_Item.ResultChoice == 0) {
									ao_Item.ResultFinishedCount++;
								}
							}
						} else {
							if (lo_Entry.ParentEntry.EntryID == ll_ParentEntryID) {
								ao_Item.ResultCount++;
								if (lo_Entry.EntryStatus == EEntryStatus.HadTransactStatus) {
									if (lo_Entry.Choice == ao_Item.ResultChoice || ao_Item.ResultChoice == 0) {
										ao_Item.ResultFinishedCount++;
									}
								}
							}
						}
					}
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获取最近流转状态-计算流转时以先同批次触发的步骤计算，如没有则取最后一个流转的状态为准
	 * 
	 * @param ao_Item
	 * @param ao_Activity
	 * @param al_OrginalID
	 * @return
	 */
	private static ArrayList<CEntry> getLastFlowEntriesByActivity(CWorkItem ao_Item, CActivity ao_Activity, int al_OrginalID) {
		ArrayList<CEntry> lo_Coll = new ArrayList<CEntry>();
		int ll_MaxPEntryId = 0; // 判断最大的父状态标识，最为最近处理的状态
		boolean lb_Boolean = false;// 判断是否存在与当前状态同时触发的状态
		for (CEntry lo_Entry : ao_Item.getEntries()) {
			if (lo_Entry.EntryType == EEntryType.ActualityEntry && lo_Entry.ActivityID == ao_Activity.ActivityID
					&& lo_Entry.FlowID == ao_Activity.Flow.FlowID) {
				if (lo_Entry.ParentEntry.EntryID > ll_MaxPEntryId) {
					ll_MaxPEntryId = lo_Entry.ParentEntry.EntryID;
				}
				if (lo_Entry.OrginalID == al_OrginalID) {
					lb_Boolean = true;
				}
				lo_Coll.add(lo_Entry);
			}
		}
		for (int i = lo_Coll.size(); i > 0; i--) {
			CEntry lo_Entry = lo_Coll.get(i - 1);
			if (lb_Boolean) {
				if (lo_Entry.OrginalID != al_OrginalID) {
					lo_Coll.remove(i - 1);
				}
			} else {
				if (lo_Entry.ParentEntry.EntryID < ll_MaxPEntryId) {
					lo_Coll.remove(i - 1);
				}
			}
		}
		return lo_Coll;
	}

	/**
	 * 计算比较步骤处理状态是否完成
	 * 
	 * @param av_Value
	 * @param ai_CompareType
	 * @param av_CompareValue
	 * @param ai_Type
	 * @return
	 */
	public static boolean compareFinishedEntry(Object av_Value, int ai_CompareType, Object av_CompareValue, int ai_Type) {
		try {
			Object lv_Value = null;
			Object lv_CompareValue = null;

			if (ai_Type == EPropDataType.NotAnyDataType) {
				lv_Value = av_Value;
				lv_CompareValue = av_CompareValue;
			} else {
				switch (ai_Type) {
				case EPropDataType.BooleanDataType:
					lv_Value = av_Value;
					lv_CompareValue = av_CompareValue;
					break;
				case EPropDataType.DateDataType:
					lv_Value = av_Value;
					lv_CompareValue = av_CompareValue;
					break;
				case EPropDataType.DoubleDataType:
					lv_Value = av_Value;
					lv_CompareValue = av_CompareValue;
					break;
				case EPropDataType.ImageDataType:
					return false;
				case EPropDataType.IntegerDataType:
					lv_Value = av_Value;
					lv_CompareValue = av_CompareValue;
					break;
				case EPropDataType.LongDataType:
					lv_Value = av_Value;
					lv_CompareValue = av_CompareValue;
					break;
				case EPropDataType.RecordsetType:
					return false;
				case EPropDataType.SingleDataType:
					lv_Value = av_Value;
					lv_CompareValue = av_CompareValue;
					break;
				case EPropDataType.StringDataType:
					lv_Value = av_Value;
					lv_CompareValue = av_CompareValue;
					break;
				}
			}

			switch (ai_CompareType) {
			case EConditionCompareType.EqualToType:
				return (lv_Value == lv_CompareValue);
			case EConditionCompareType.LargeEqualToType:
				return (Integer.parseInt(lv_Value.toString()) >= Integer.parseInt(lv_CompareValue.toString()));
			case EConditionCompareType.LargeType:
				return (Integer.parseInt(lv_Value.toString()) > Integer.parseInt(lv_CompareValue.toString()));
			case EConditionCompareType.SmallEqualToType:
				return (Integer.parseInt(lv_Value.toString()) <= Integer.parseInt(lv_CompareValue.toString()));
			case EConditionCompareType.SmallType:
				return (Integer.parseInt(lv_Value.toString()) < Integer.parseInt(lv_CompareValue.toString()));
			case EConditionCompareType.NotEqualToType:
				return (lv_Value != lv_CompareValue);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	// 处理由于本地嵌入/远程启动触发的子公文流转结束后的信息回传处理
	private static boolean transactWorkItemReturn(CWorkItem ao_Item) throws Exception {
		try {
			// 非子公文，无须处理
			if (ao_Item.WorkItemSource.equals("")) {
				return false;
			}

			Document lo_Xml = MXmlHandle.LoadXml(ao_Item.WorkItemSource);
			if (lo_Xml == null)
				return false; // 处理参数存储错误

			// =0-本地嵌入；=1-远程启动；
			int li_Type = Integer.parseInt(lo_Xml.getDocumentElement().getAttribute("Type"));

			CFileExchange lo_File = new CFileExchange(ao_Item, li_Type);
			boolean lb_Boolean = false;
			if (li_Type == 0) {
				return transactReturnTrumbleIn(ao_Item, lo_Xml.getDocumentElement());
			} else {
				lb_Boolean = lo_File.transactReturnLaunch(lo_Xml.getDocumentElement());
				lo_File.ClearUp();
				lo_File = null;
			}
			lo_Xml = null;
			return lb_Boolean;
		} catch (Exception ex) {
			ao_Item.Raise(ex, "transactWorkItemReturn", "");
			return false;
		}
	}

	// 处理嵌入情况
	private static boolean transactReturnTrumbleIn(CWorkItem ao_Item, Element ao_Node) {
		return true;
		// String ls_Sql =
		// "SELECT RecipientID FROM EntryInst WHERE WorkItemID = {0} AND EntryID
		// = {1}";
		// ls_Sql = MessageFormat.format(ls_Sql,
		// ao_Node.getAttribute("WorkItemID"),
		// ao_Node.getAttribute("EntryID"));
		// int ll_ReciID = Integer.parseInt(ao_Item.SqlHandle().getValueBySql(
		// ls_Sql));
		// int ll_UserID = ao_Item.GlobalPara().UserID;
		//
		// // 切换登陆
		// ao_Item.Logon.SwitchUserLogon(ll_ReciID,
		// EUserLogonFlag.LogonByUserID);
		// CWorkItem lo_WorkItem = new CWorkItem(ao_Item.Logon);
		// if (TWorkItem.openWorkItem(lo_WorkItem,
		// Integer.parseInt(ao_Node.getAttribute("WorkItemID")),
		// Integer.parseInt(ao_Node.getAttribute("EntryID")))) {
		// //公文没有流转完成，可以继续处理
		// if (lo_WorkItem.Status == EWorkItemStatus.WorkFlowing
		// && lo_WorkItem.WorkItemAccess) {
		// Document lo_Xml =
		// MXmlHandle.LoadXml(lo_WorkItem.CurEntry.getActivity().TumbleIn.ImpressData.ExportToXml("WorkItem",
		// 1));
		// lo_Result = PackageWorkItem2(lo_Data.getDocumentElement(),
		// lo_WorkItem.theCurEntry.getMl_EntryID());
		// if (TransferTwoWorkItemInfo(lo_Result, lo_WorkItem)) {
		// // 设定返回值
		// lo_WorkItem.theCurEntry.setMi_Choice(Short.parseShort(lo_Result
		// .getDocumentElement().getAttribute("ResultType")));
		// // lo_WorkItem.theCurEntry.ChoiceContent =
		// //
		// System.Convert.ToString(lo_Result.getDocumentElement().getAttribute("ResultDescribe"));
		// // if (lo_WorkItem.SendWorkItem())
		// // {
		// if (ms_Information != ""
		// && ms_Information.substring(
		// ms_Information.length() - 2, 2) != "/n") {
		// ms_Information = ms_Information + "/n";
		// }
		// // if (lo_WorkItem.Information_Renamed == "")
		// // {
		// // ms_Information = ms_Information + "返回主公文[继续处理]";
		// // }
		// // else
		// // {
		// // ms_Information = ms_Information + "返回主公文[" +
		// // lo_WorkItem.Information_Renamed + "]";
		// // }
		// // }
		// }
		// // }
		// }
		// // else
		// // {
		// // //否则只更新单个状态信息-简单处理
		// // ls_Sql =
		// //
		// "SELECT WorkItemID, EntryID, StateID, FinishedDate, ActivityChoice,
		// ChoiceContent FROM EntryInst WHERE WorkItemID = ";
		// // ls_Sql = ls_Sql + (lo_WorkItem.WorkItemID).ToString() +
		// // " And EntryID = " + (lo_WorkItem.theCurEntry.EntryID).ToString();
		// // if
		// //
		// (lo_Data.loadXML(lo_WorkItem.theCurEntry.theActivity.theTumbleIn.theImpressData.ExportToXML()))
		// // {
		// // lo_Result = PackageWorkItem2(lo_Data.documentElement,
		// // lo_WorkItem.theCurEntry.EntryID);
		// // if (TransferTwoWorkItemInfo(lo_Result, lo_WorkItem))
		// // {
		// // lo_Rec = theEnvironment.thePublicFunc.GetRecordsetBySql(ls_Sql,
		// // false);
		// // lo_Rec.Fields["StateID"].Value = 11;
		// // lo_Rec.Fields["FinishedDate"].Value = DateTime.Now;
		// // lo_Rec.Fields["ActivityChoice"].Value =
		// //
		// System.Convert.ToInt16(lo_Result.documentElement.getAttribute("ResultType"));
		// // lo_Rec.Fields["ChoiceContent"].Value = ((String)
		// // lo_Result.documentElement.getAttribute("ResultDescribe") == "") ?
		// // System.DBNull.Value :
		// // (lo_Result.documentElement.getAttribute("ResultDescribe"));
		// // lo_Rec.UpdateBatch((ADODB.AffectEnum) 3);
		// // lo_Rec.Close();
		// // lo_Rec = null;
		// // }
		// // }
		// // }
		// // returnValue = true;
		// // }
		// // lo_WorkItem.ClearUp(false);
		// lo_WorkItem = null;
		// // theEnvironment.SwitchUserLogon(ll_UserID,
		// // TEnvironment.enumUserLogonFlag.LogonByUserID);
		// return false;
	}

	/**
	 * 获取选择以前选择的用户（一次选择情况）
	 * 
	 * @param ao_Item
	 * @param ao_Activity
	 * @return
	 */
	private static String getActivityParticipants(CWorkItem ao_Item, CActivity ao_Activity) {
		if (ao_Activity.Participant.SelectUserNum == 0) {
			return "";
		}

		int i = 0;
		String ls_Text = ",";
		int ll_FlowId = ao_Activity.Flow.FlowID;
		for (CEntry lo_Entry : ao_Item.getEntries()) {
			if (lo_Entry.EntryType == EEntryType.ActualityEntry && lo_Entry.FlowID == ll_FlowId && lo_Entry.ActivityID == ao_Activity.ActivityID) {
				if (ls_Text.indexOf("," + String.valueOf(lo_Entry.ParentEntry.EntryID) + ",") == -1) {
					ls_Text += lo_Entry.ParentEntry.EntryID + ",";
					i++;
				}
			}
		}

		if (ao_Activity.Participant.SelectUserNum >= i) {
			return "";
		}
		ls_Text = ls_Text.substring(0, ls_Text.length() - 1);
		i = ls_Text.lastIndexOf(",") + 1;
		CEntry lo_Parent = ao_Item.getEntryById(Integer.parseInt(MGlobal.right(ls_Text, ls_Text.length() - i)));
		ls_Text = "";
		for (CEntry lo_Entry : lo_Parent.ChildEntries) {
			ls_Text += lo_Entry.Participant + "[" + String.valueOf(lo_Entry.ParticipantID) + "];";
		}
		return ls_Text;
	}

	/**
	 * 增加计算公式人员
	 * 
	 * @param ao_Item
	 * @param as_Participant
	 * @param ao_Activity
	 * @return
	 * @throws Exception
	 */
	private static String appendCacuParticipant(CWorkItem ao_Item, String as_Participant, CActivity ao_Activity) throws Exception {
		if (ao_Activity == null) {
			return as_Participant;
		}
		if (ao_Activity.Participant == null) {
			return as_Participant;
		}
		if (ao_Activity.Participant.CaculateFormula.equals("")) {
			return as_Participant;
		}
		String ls_Text = ao_Item.caculateScriptContentEval(ao_Activity.Participant.CaculateFormula).toString();
		if (ls_Text.equals("")) {
			return as_Participant;
		}
		ls_Text = MGlobal.getSqlInText(ls_Text);
		int i = 0;
		if (ls_Text.substring(0, 3).equals("[0]")) { // 用户标识
			ls_Text = "UserID IN (" + MGlobal.right(ls_Text, ls_Text.length() - 3) + ")";
		} else if (ls_Text.substring(0, 3).equals("[1]")) {// 用户代码
			ls_Text = "UserCode IN (\'" + MGlobal.right(ls_Text, ls_Text.length() - 3).replaceAll(",", "\',\'") + "\')";
		} else {
			i = (short) (ls_Text.indexOf("]") + 1);
			if (i > 0) {
				ls_Text = ls_Text.substring(ls_Text.length() - (ls_Text.length() - i), ls_Text.length() - i);
			}
			ls_Text = "UserName IN (\'" + ls_Text.replace("\'", "\'\'").replace(",", "\',\'") + "\')";
		}
		ls_Text = "SELECT UserID FROM UserInfo WHERE " + ls_Text;
		ls_Text = "SELECT UserID, UserName FROM UserInfo WHERE UserType <> 1 AND (UserID IN (" + ls_Text
				+ ") OR UserID IN (SELECT UserID FROM UserGroup WHERE GroupID IN (" + ls_Text + ")))";
		List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Text);
		if (lo_Set == null) {
			return as_Participant;
		}
		ls_Text = as_Participant;
		for (Map<String, Object> row : lo_Set) {
			if ((";" + ls_Text).indexOf(";" + MGlobal.readString(row, "UserName") + "[" + MGlobal.readString(row, "UserID") + "];") == -1) {
				ls_Text += MGlobal.readString(row, "UserName") + "[" + MGlobal.readString(row, "UserID") + "];";
			}
		}
		lo_Set = null;
		return ls_Text;
	}

	/**
	 * 计算条件是否成立
	 * 
	 * @param ao_Condition
	 * @return
	 */
	private static boolean caculateCondition(CWorkItem ao_Item, CCondition ao_Condition) {
		try {
			// 条件不存在
			if (ao_Condition == null)
				return false;

			System.out.print("\n条件：" + ao_Condition.getConditionLabel());

			// 判断计算各种条件情况
			switch (ao_Condition.ConditionType) {
			case EConditionType.LogicOrCondition:
				for (CCondition lo_Child : ao_Condition.ChildConditions) {
					if (caculateCondition(ao_Item, lo_Child))
						return true;
				}
				return false;
			case EConditionType.LogicAndCondition:
				for (CCondition lo_Child : ao_Condition.ChildConditions) {
					if (caculateCondition(ao_Item, lo_Child) == false)
						return false;
				}
				return true;
			case EConditionType.ActivityCondition:
				CActivity lo_Activity = ao_Condition.getSourceActivity();
				if (lo_Activity == null)
					return false;

				if (lo_Activity.ActivityType == EActivityType.SplitActivity) {
					// 已经成功流转的分支步骤条件成立
					return (ao_Item.FlowSucceedSplits.indexOf(";" + lo_Activity.ActivityName + ";") > -1);
				}

				int li_Choice = 0;
				switch (lo_Activity.ActivityType) {
				case EActivityType.TumbleInActivity:
					li_Choice = ao_Condition.TumTrfType;
					break;
				case EActivityType.LaunchActivity:
					li_Choice = ao_Condition.TumTrfType;
					break;
				default:
					if (lo_Activity.Transact == null) {
						li_Choice = 0;
					} else {
						li_Choice = lo_Activity.Transact.getChoiceByContent(ao_Condition.ResponseChoice);
					}
					break;
				}

				if (lo_Activity.ActivityType == EActivityType.TumbleInActivity || lo_Activity.ActivityType == EActivityType.LaunchActivity) {
					ao_Item.ResultCount = 0;
					ao_Item.ResultFinishedCount = 0;
					ao_Item.ResultChoice = li_Choice;
					ao_Item.ResultCompareContent = ao_Condition.CompareContent;
					ao_Item.ResultCompareType = ao_Condition.CompareType;
					caculateActivityEntry(ao_Item, lo_Activity);

					if (ao_Item.ResultCount == 0) {
						return false; // 步骤状态未触发
					}
					// 处理嵌入或启动条件计算
					switch (ao_Condition.ConditionTumTrfType) {
					case EConditionTumTrfType.HaveSendType: // 所有情况默认为已发送
						return true;
					case EConditionTumTrfType.FinishedFlow: // 只有所有情况默认都成功流转，才认为条件成立(流转成功)
						return (ao_Item.ResultCount == ao_Item.ResultFinishedCount);
					case EConditionTumTrfType.UnFinishedFlow: // 只要存在一个未成功流转，都认为条件成立(流转不成功)
						return (ao_Item.ResultFinishedCount > 0);
					case EConditionTumTrfType.FinishedRecieve: // 只有所有情况默认都成功接收，才认为条件成立(流转成功)
						return (ao_Item.ResultCount == ao_Item.ResultFinishedCount);
					case EConditionTumTrfType.UnFinishedRecieve: // 只要存在一个未成功接收，都认为条件成立(流转不成功)
						return (ao_Item.ResultFinishedCount > 0);
					}
				} else {
					ao_Item.ResultCount = 0;
					ao_Item.ResultFinishedCount = 0;
					ao_Item.ResultChoice = li_Choice;
					ao_Item.ResultCompareContent = "";
					ao_Item.ResultCompareType = EConditionCompareType.EqualToType;
					caculateActivityEntry(ao_Item, lo_Activity);

					if (ao_Item.ResultCount == 0)
						return false; // 步骤状态未触发

					// 处理普通处理情况
					switch (ao_Condition.ActivityFNType) {
					case EActivityConditionType.AllFinished:
						return (ao_Item.ResultCount == ao_Item.ResultFinishedCount);
					case EActivityConditionType.AtListOneFinished:
						return (ao_Item.ResultFinishedCount > 0);
					case EActivityConditionType.FinishedNumber:
						return compareFinishedEntry(ao_Item.ResultFinishedCount, ao_Condition.CompareType,
								Short.parseShort(ao_Condition.CompareContent), EPropDataType.IntegerDataType);
					case EActivityConditionType.FinishedPercent:
						return compareFinishedEntry(ao_Item.ResultFinishedCount * 100 / ao_Item.ResultCount, ao_Condition.CompareType,
								Short.parseShort(ao_Condition.CompareContent), EPropDataType.SingleDataType);
					case EActivityConditionType.UnFinishedNumber:
						return compareFinishedEntry(ao_Item.ResultCount - ao_Item.ResultFinishedCount, ao_Condition.CompareType,
								Short.parseShort(ao_Condition.CompareContent), EPropDataType.IntegerDataType);
					case EActivityConditionType.UnFinishedPercent:
						return compareFinishedEntry((ao_Item.ResultCount - ao_Item.ResultFinishedCount) * 100 / ao_Item.ResultCount,
								ao_Condition.CompareType, Short.parseShort(ao_Condition.CompareContent), EPropDataType.SingleDataType);
					}
				}
				break;
			case EConditionType.FlowPropCondition:
				CProp lo_Prop = ao_Condition.getBindProp();
				if (lo_Prop == null) {
					String ls_Value = ao_Condition.getBindPropName();
					boolean lb_Boolean = (ao_Condition.CompareType == EConditionCompareType.EqualToType
							|| ao_Condition.CompareType == EConditionCompareType.LargeEqualToType || ao_Condition.CompareType == EConditionCompareType.SmallEqualToType);
					if (ls_Value.equals("{[<拟稿人岗位>]}")) {
						String ls_Position = convertRoleNameToSign(ao_Item, "拟稿人的" + ao_Condition.CompareContent, 0);
						if (lb_Boolean)
							return ((";" + ls_Position).indexOf(";" + ao_Item.Creator + ";") > -1);
						else
							return ((";" + ls_Position).indexOf(";" + ao_Item.Creator + ";") == -1);
					} else if (ls_Value.equals("{[<当前处理人岗位>]}")) {
						String ls_Position = convertRoleNameToSign(ao_Item, "当前处理人的" + ao_Condition.CompareContent, 0);
						if (ao_Item.CurEntry.ProxyParticipantID == 0) {
							if (lb_Boolean)
								return ((";" + ls_Position).indexOf(";" + ao_Item.CurEntry.Participant + ";") > -1);
							else
								return ((";" + ls_Position).indexOf(";" + ao_Item.CurEntry.Participant + ";") == -1);
						} else {
							if (lb_Boolean)
								return ((";" + ls_Position).indexOf(";" + String.valueOf(ao_Item.CurEntry.ProxyParticipantID) + ";") > -1);
							else
								return ((";" + ls_Position).indexOf(";" + String.valueOf(ao_Item.CurEntry.ProxyParticipantID) + ";") == -1);
						}
					} else {
						return false;
					}
				} else {
					return compareFinishedEntry(lo_Prop.getValues(), ao_Condition.CompareType, ao_Condition.CompareContent, lo_Prop.PropDataType);
				}
			default: // OtherCondition, NotAnyCondition
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 把角色名称转化成特殊符号的系统角色内容
	 * 
	 * @param as_SysRoleNames
	 * @param al_CurUserID
	 * @return
	 */
	public static String convertRoleNameToSign(CWorkItem ao_Item, String as_SysRoleNames, int al_CurUserID) {
		try {
			if (MGlobal.isEmpty(as_SysRoleNames))
				return "";

			String ls_PositionID = "";
			String ls_DeptID = "";
			String ls_Sql = "";

			String ls_Value = "";
			String[] ls_Array = as_SysRoleNames.split(";");
			for (int i = 0; i < ls_Array.length; i++) {
				if (MGlobal.isExist(ls_Array[i])) {
					// 拟稿人的
					if (MGlobal.left(ls_Array[i], 4).equals("拟稿人的")) {
						ls_Sql = MGlobal.right(ls_Array[i], ls_Array[i].length() - 4).replaceAll("\'", "\'\'");
						ls_Sql = "SELECT PositionID FROM UserPosition WHERE PositionName = \'" + ls_Sql + "\'";
						ls_PositionID = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
						if (MGlobal.isExist(ls_PositionID)) {
							ls_Value += TUserAdmin.getSystemRoleUsers(ao_Item.Logon, ao_Item.CreatorID, Integer.parseInt(ls_PositionID), "&", 2);
						}
					}
					// 当前处理人的
					if (MGlobal.left(ls_Array[i], 6).equals("当前处理人的")) {
						ls_Sql = MGlobal.right(ls_Array[i], ls_Array[i].length() - 6).replaceAll("\'", "\'\'");
						ls_Sql = "SELECT PositionID FROM UserPosition WHERE PositionName = \'" + ls_Sql + "\'";
						ls_PositionID = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
						if (MGlobal.isExist(ls_PositionID)) {
							if (al_CurUserID == 0) {
								if (ao_Item.CurEntry.ProxyParticipantID == 0) {
									ls_Value += TUserAdmin.getSystemRoleUsers(ao_Item.Logon, ao_Item.CurEntry.ParticipantID,
											Integer.parseInt(ls_PositionID), "&", 2);
								} else {
									ls_Value += TUserAdmin.getSystemRoleUsers(ao_Item.Logon, ao_Item.CurEntry.ProxyParticipantID,
											Integer.parseInt(ls_PositionID), "&", 2);
								}
							} else {
								ls_Value += TUserAdmin.getSystemRoleUsers(ao_Item.Logon, al_CurUserID, Integer.parseInt(ls_PositionID), "&", 2);
							}
						}
					}
					// 部门“XXXX”的经理
					if (ls_Array[i].substring(0, 3) == "部门[") {
						int j = ls_Array[i].indexOf("]的") + 1;
						if (j > 0) {
							ls_Sql = ls_Array[i].substring(3, j - 1).replaceAll("\'", "\'\'");
							ls_Sql = "SELECT DeptID FROM DeptInfo WHERE DeptName = \'" + ls_Sql + "\'";
							ls_DeptID = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);

							ls_Sql = MGlobal.right(ls_Array[i], ls_Array[i].length() - j - 1).replaceAll("\'", "\'\'");
							ls_Sql = "SELECT PositionID FROM UserPosition WHERE PositionName = \'" + ls_Sql + "\'";
							ls_PositionID = CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);

							if (MGlobal.isExist(ls_DeptID) && MGlobal.isExist(ls_PositionID)) {
								ls_Value += TUserAdmin.getSystemRoleUsers(ao_Item.Logon, Integer.parseInt(ls_DeptID),
										Integer.parseInt(ls_PositionID), "&", 22);
							}
						}
					}
				}
			}

			return ls_Value;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 处理嵌入情况
	 * 
	 * @param ao_Item
	 * @return
	 * @throws Exception
	 */
	private static boolean transactTrumbleIn(CWorkItem ao_Item) throws Exception {
		try {
			if (MGlobal.isEmpty(ao_Item.LaunchParameter))
				return true;

			Document lo_Xml = MXmlHandle.LoadXml("<Root>" + ao_Item.LaunchParameter + "</Root>");
			if (lo_Xml == null)
				return false;

			NodeList lo_List = lo_Xml.getDocumentElement().getElementsByTagName("Parameter");
			for (int k = 0; k < lo_List.getLength(); k++) {
				Element lo_Node = (Element) lo_List.item(k);
				if (lo_Node.getAttribute("Type").equals("0")) {
					// 导出参数
					int ll_FlowId = Integer.parseInt(lo_Node.getAttribute("FlowID"));// 触发嵌入流程标识
					int ll_ActivityID = Integer.parseInt(lo_Node.getAttribute("ActivityID"));// 触发嵌入步骤标识
					int ll_TemplateID = Integer.parseInt(lo_Node.getAttribute("TemplateID"));// 嵌入公文模板标识
					int ll_LaunchFlowID = Integer.parseInt(lo_Node.getAttribute("LaunchFlowID"));// 嵌入流程标识

					String ls_Users = lo_Node.getAttribute("Users");// 嵌入用户信息，格式为：用户名称[用户标识];...
					String ls_EntryIDs = lo_Node.getAttribute("EntryIDs");// 触发嵌入状态标识，格式为：状态标识1;状态标识2;...

					// 嵌入公文
					CWorkItem lo_WorkItem = TWorkItem.newWorkItem(ao_Item.Logon, ll_TemplateID, ll_LaunchFlowID);
					if (lo_WorkItem != null) {
						// 设置拟稿人的值
						CRole lo_Role = lo_WorkItem.Cyclostyle.getRoleById(1);
						CEntry lo_Entry2 = lo_WorkItem.getEntryById(2);
						CEntry lo_Entry3 = lo_WorkItem.getEntryById(3);

						// 数据传递(当前公文——》嵌入公文)
						CActivity lo_Activity = lo_WorkItem.CurFlow.getActivityById(ll_ActivityID);
						String[] ls_ArrEntryIds = ls_EntryIDs.split(";");
						// TransferTwoWorkItemInfo(PackageWorkItem2(lo_Activity.TumbleIn,
						// Integer.parseInt(ls_ArrEntryIds[0])), lo_WorkItem);
						if (MGlobal.right(ls_Users, 1).equals(";"))
							ls_Users = ls_Users.substring(0, ls_Users.length() - 1);

						String[] ls_Array = ls_Users.split(";");
						lo_WorkItem.setWorkItemName(lo_WorkItem.WorkItemName + "(" + ao_Item.WorkItemName + ")");
						lo_WorkItem.DeleteTempFile = 100;
						for (int i = 0; i < ls_Array.length; i++) {
							if (MGlobal.isExist(ls_Array[i])) {
								lo_WorkItem.WorkItemSource = "<Launch Type=\"0\" WorkItemID=\"" + String.valueOf(ao_Item.WorkItemID)
										+ "\" EntryID=\"" + ls_ArrEntryIds[i] + "\" />";

								int j = ls_Array[i].indexOf("[") + 1;
								String ls_UserName = ls_Array[i].substring(0, j - 1);
								int ll_UserID = Integer.parseInt(ls_Array[i].substring(j, ls_Array[i].length() - 1));
								// 修改部分数据
								lo_Role.Value = String.valueOf(ll_UserID) + ";";
								lo_WorkItem.Creator = ls_UserName;
								lo_WorkItem.CreatorID = ll_UserID;
								lo_WorkItem.EditorID = 0;
								lo_WorkItem.Editor = "";
								lo_WorkItem.WorkItemID = -1;
								lo_Entry2.Participant = ls_UserName;
								lo_Entry2.ParticipantID = ll_UserID;
								lo_Entry3.Participant = ls_UserName;
								lo_Entry3.ParticipantID = ll_UserID;
								lo_WorkItem.WorkItemAccess = true;
								// 最后一次保存并删除临时文件
								if (ls_Array.length - 1 == i)
									lo_WorkItem.DeleteTempFile = 0;
								// 保存
								if (TWorkItem.saveWorkItem(lo_WorkItem)) {
									String ls_Sql = "UPDATE EntryInst SET ParameterOne = ? WHERE WorkItemID = {0} AND EntryID = {1}";
									ls_Sql = MessageFormat.format(ls_Sql, String.valueOf(ao_Item.WorkItemID), ls_ArrEntryIds[i]);
									CSqlHandle.getJdbcTemplate().update(ls_Sql,
											new Object[] { "<Launch Type=\"0\" WorkItemID=\"" + String.valueOf(lo_WorkItem.WorkItemID) + "\" />" });
									// PreparedStatement lo_State =
									// ao_Item.SqlHandle().getState(ls_Sql, 0);
									// lo_State.setString(1,
									// "<Launch Type=\"0\" WorkItemID=\""
									// + String.valueOf(lo_WorkItem.WorkItemID)
									// + "\" />");
									// lo_State.addBatch();
									// lo_State.executeBatch();
									// ao_Item.Statements.add(lo_State);
									if (lo_WorkItem.WorkItemID > 0)
										TWorkAdmin.releaseLock(ao_Item.Logon, lo_WorkItem.WorkItemID, lo_WorkItem.CurEntry.EntryID);
								}
							}
						}
						lo_WorkItem.ClearUp();
						lo_WorkItem = null;
					}
				}
			}
			lo_Xml = null;
			return true;
		} catch (Exception ex) {
			ao_Item.Raise(ex, "TransactTrumbleIn", ao_Item.LaunchParameter);
			return false;
		}
	}

	/**
	 * 公文保存后的刷新动作，保证内存数据和数据库数据一致
	 * 
	 * @param ao_Item
	 * @throws Exception
	 */
	private static void refreshEntries(CWorkItem ao_Item) throws Exception {
		String ls_Ids = "";
		for (CEntry lo_Entry : ao_Item.getEntries()) {
			if (lo_Entry.ID == 0)
				ls_Ids += String.valueOf(lo_Entry.EntryID) + ",";
			if (lo_Entry.MoveEntry != null)
				lo_Entry.MoveEntry.IsNewMoveEntry = false;
		}
		if (MGlobal.isExist(ls_Ids)) {
			ls_Ids = ls_Ids.substring(0, ls_Ids.length() - 1);
			String ls_Sql = "SELECT WEID, EntryID FROM EntryInst WHERE WorkItemID = {0} AND EntryID IN ({1})";
			List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(
					MessageFormat.format(ls_Sql, String.valueOf(ao_Item.WorkItemID), ls_Ids));
			for (Map<String, Object> row : lo_Set) {
				ao_Item.getEntryById(MGlobal.readInt(row, "EntryID")).ID = MGlobal.readInt(row, "WEID");
			}
			lo_Set = null;
		}
	}

	/**
	 * 导出公文到某一个XML文件中
	 * 
	 * @param ao_Item
	 *            公文实例对象
	 * @return 返回临时文件名称（不带路径）
	 * @throws Exception
	 */
	public static String exportToFile(CWorkItem ao_Item) throws Exception {
		return exportToFile(ao_Item, "", ao_Item.Logon.TempPath);
	}

	/**
	 * 导出公文到某一个XML文件中
	 * 
	 * @param ao_Item
	 *            公文实例对象
	 * @param as_File
	 *            导出的文件名称，为空自动生成临时文件
	 * @param as_Path
	 *            导出的文件路径，为空为系统的临时文件路径
	 * @return 返回文件名称，如果as_File为空或不带路径的文件，返回也不带路径
	 * @throws Exception
	 */
	public static String exportToFile(CWorkItem ao_Item, String as_File, String as_Path) throws Exception {
		String ls_File = as_File.replaceAll("/", "\\");
		String ls_Path = as_Path.replaceAll("/", "\\");
		if (MGlobal.isEmpty(ls_Path)) {
			ls_Path = ao_Item.Logon.TempPath;
		} else {
			if (!ls_Path.substring(ls_Path.length() - 1).equals("\\"))
				ls_Path += "\\";
		}

		if (MGlobal.isEmpty(ls_File)) {
			ls_File = ls_Path + MFile.getTempFile("xml", ls_Path);
		} else {
			if (ls_File.indexOf(":\\") == -1)
				ls_File = ls_Path + ls_File;
		}

		if (MGlobal.isExist(MFile.writeTxtFile(ls_File, ao_Item.ExportToXml("WorkItem", 0))))
			return "";

		if (MGlobal.isEmpty(as_Path)) {
			if (as_File.replaceAll("/", "\\").indexOf(":\\") == -1)
				return ls_File;
		}
		return ls_File.substring(ls_File.lastIndexOf("\\") + 1);
	}

	/**
	 * 从某一个XML文件导入公文
	 * 
	 * @param ao_Logon
	 *            系统登录对象
	 * @param as_File
	 *            导入文件名称，如果不带路径，则使用系统临时路径
	 * @return 返回公文对象
	 * @throws Exception
	 */
	public static CWorkItem importFromFile(CLogon ao_Logon, String as_File) throws Exception {
		CWorkItem lo_Item = new CWorkItem(ao_Logon);
		String ls_File = as_File.replaceAll("/", "\\");
		if (ls_File.indexOf(":\\") == -1)
			ls_File = ao_Logon.TempPath + ls_File;
		lo_Item.ImportFormXml(MFile.readTxtFile(ls_File), 0);
		return lo_Item;
	}

	/**
	 * 设置流转的必须参数
	 * 
	 * @param ao_Item
	 *            公文实例对象
	 * @param ai_Type
	 *            类型：0-保存;1-发送;2-转发;3-内部传阅;
	 * @param as_Text
	 *            传递内容
	 * @return
	 */
	public static boolean setNeedParameter(CWorkItem ao_Item, int ai_Type, String as_Text) {
		return setNeedParameter(ao_Item, ai_Type, as_Text, null);
	}

	/**
	 * 设置流转的必须参数
	 * 
	 * @param ao_Item
	 *            公文实例对象
	 * @param ai_Type
	 *            类型：0-保存;1-发送;2-转发;3-内部传阅;
	 * @param as_Text
	 *            传递内容
	 * @return
	 */
	public static boolean setNeedParameter(CWorkItem ao_Item, int ai_Type, String as_Text, Map<String, Object> ao_Extend) {
		if (!ao_Item.WorkItemAccess)
			return false;

		if (ao_Extend != null) {
			for (int i = 1; i <= 10; i++) {
				if (ao_Extend.containsKey(String.valueOf(i))) {
					switch (i) {
					case 1:
						ao_Item.ExtendOne = Integer.parseInt(ao_Extend.get(String.valueOf(i)).toString());
						break;
					case 2:
						ao_Item.ExtendTwo = Integer.parseInt(ao_Extend.get(String.valueOf(i)).toString());
						break;
					case 3:
						ao_Item.ExtendThree = ao_Extend.get(String.valueOf(i)).toString();
						break;
					case 4:
						if (ao_Extend.get(String.valueOf(i)).toString().length() > 10)
							ao_Item.ExtendFour = MGlobal.stringToDate((ao_Extend.get(String.valueOf(i))).toString() + ":00:00");
						else
							ao_Item.ExtendFour = MGlobal.stringToDate((ao_Extend.get(String.valueOf(i))).toString() + " 00:00:00");
						break;
					case 5:
						ao_Item.ExtendFive = ao_Extend.get(String.valueOf(i)).toString();
						break;
					case 6:
						ao_Item.ExtendSix = ao_Extend.get(String.valueOf(i)).toString();
						break;
					case 7:
						ao_Item.ExtendSeven = ao_Extend.get(String.valueOf(i)).toString();
						break;
					case 8:
						ao_Item.ExtendEight = ao_Extend.get(String.valueOf(i)).toString();
						break;
					case 9:
						ao_Item.ExtendNine = ao_Extend.get(String.valueOf(i)).toString();
						break;
					case 10:
						ao_Item.ExtendTen = ao_Extend.get(String.valueOf(i)).toString();
						break;
					default:
						break;
					}
				}
			}
		}

		JSONArray lo_Array = JSONArray.parseArray(as_Text);
		JSONObject lo_Json = null;

		// 设置公文名称
		if (ao_Item.CurEntry.EntryID == 3) {
			lo_Json = getJSONObject(lo_Array, "name");
			if (lo_Json != null)
				ao_Item.WorkItemName = lo_Json.getString("value");
		}

		// 获取发送类型
		switch (ai_Type) {
		case 0:// 保存
			break;
		case 1:// 发送
			if (MGlobal.isExist(ao_Item.CurActivity.Transact.ResponseChoices)) {
				// 设置意见选项
				lo_Json = getJSONObject(lo_Array, "choice");
				if (lo_Json != null)
					ao_Item.CurEntry.setChoiceContent(lo_Json.getString("value"));
				else {
					ao_Item.CurEntry.setChoice(1);
				}
			} else {
				ao_Item.CurEntry.setChoice(-2);
			}
			for (int i = 0; i < lo_Array.size(); i++) {
				// 设置后继收件人
				lo_Json = lo_Array.getJSONObject(i);
				String ls_Name = lo_Json.getString("name");
				if (MGlobal.left(ls_Name, "activity".length()).equals("activity")) {
					int ll_ActId = Integer.parseInt(MGlobal.right(ls_Name, ls_Name.length() - "activity".length()));
					CActivity lo_Act = ao_Item.CurFlow.getActivityById(ll_ActId);
					if (lo_Act != null)
						lo_Act.Participant.Participant += transactUsers(lo_Json.getString("value"));
				}
			}
			break;
		case 2:// 转发
			ao_Item.CurEntry.setChoice(0);
			// lo_Json = getJSONObject(lo_Array, "transmit");
			lo_Json = getJSONObject(lo_Array, "choice");
			ao_Item.CurEntry.TransmitActivityID = lo_Json.getIntValue("value");
			for (int i = 0; i < lo_Array.size(); i++) {
				// 设置后继收件人
				lo_Json = lo_Array.getJSONObject(i);
				String ls_Name = lo_Json.getString("name");
				if (MGlobal.left(ls_Name, "activity".length()).equals("activity")) {
					int ll_ActId = Integer.parseInt(MGlobal.right(ls_Name, ls_Name.length() - "activity".length()));
					CActivity lo_Act = ao_Item.CurFlow.getActivityById(ll_ActId);
					if (lo_Act != null)
						lo_Act.Participant.Participant += transactUsers(lo_Json.getString("value"));
				}
			}
			break;
		case 3:// 内部传阅
			ao_Item.CurEntry.setChoice(-1);
			lo_Json = getJSONObject(lo_Array, "activity0");
			if (lo_Json == null)
				lo_Json = getJSONObject(lo_Array, "activity" + String.valueOf(ao_Item.CurActivity.ActivityID));
			if (lo_Json != null) {
				ao_Item.CurActivity.Participant.Participant = transactUsers(lo_Json.getString("value"));
			}
			break;
		default:
			return false;
		}
		// 设置意见内容
		lo_Json = getJSONObject(lo_Array, "comment");
		if (lo_Json != null)
			ao_Item.CurEntry.ResponseContent = lo_Json.getString("value");
		if (MGlobal.isEmpty(ao_Item.CurEntry.ResponseContent))
			ao_Item.CurEntry.ResponseContent = ao_Item.CurEntry.Comment;

		return true;
	}

	/**
	 * 转换用户格式信息
	 * 
	 * @param as_Users
	 * @return
	 */
	private static String transactUsers(String as_Users) {
		if (MGlobal.isEmpty(as_Users))
			return as_Users;
		String[] ls_Array = as_Users.split(",");
		String ls_User = "";
		for (int i = 0; i < ls_Array.length; i++) {
			if (MGlobal.isExist(ls_Array[i])) {
				if (MGlobal.isNumber(ls_Array[i].substring(0, 1)))
					ls_User += "U" + ls_Array[i] + ";";
				else
					ls_User += ls_Array[i] + ";";
			}
		}
		return ls_User;
	}

	/**
	 * 根据名称获取JSON数组中的JSON对象
	 * 
	 * @param ao_JSONArray
	 *            JSON数组
	 * @param as_Name
	 *            名称
	 * @return
	 */
	private static JSONObject getJSONObject(JSONArray ao_JSONArray, String as_Name) {
		for (int i = 0; i < ao_JSONArray.size(); i++) {
			JSONObject lo_Json = ao_JSONArray.getJSONObject(i);
			if (lo_Json.getString("name").equals(as_Name))
				return lo_Json;
		}
		return null;
	}

	/**
	 * 判断是否需要弹出窗口
	 * 
	 * @param ao_Inner
	 *            公文流转接口实体类
	 * @param ai_Action
	 *            动作类型：=0-保存；=1-发送；=2-转发；=3-内部传阅；
	 * @return
	 */
	public static boolean existSelectChoice(InnerWorkItem ao_Inner, int ai_Action) {
		if (ao_Inner.getEntryID() == 3)
			return true;

		switch (ai_Action) {
		case 0:
			return false;
		case 1:
			for (NextActivityPara lo_Para : ao_Inner.getNextParas()) {
				if (lo_Para.getType() > -1)
					return true;
			}
			if (MGlobal.isExist(ao_Inner.getResponseChoices())) {
				if (ao_Inner.getResponseChoices().length() - ao_Inner.getResponseChoices().replaceAll(";", "").length() > 1)
					return true;
			}
			return false;
		case 2:
			return true;
		case 3:
			NextActivityPara lo_Para = ao_Inner.getInsideReadingPara();
			if (lo_Para != null)
				return (lo_Para.getType() > -1);
			return false;
		}
		return false;
	}

	/**
	 * 公文撤回
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemID
	 *            工作实例标识
	 * @param al_EntryID
	 *            实例状态标识：=-1-表示管理员代替别人撤回最近状态；=0-表示个人撤回最近状态；>0表示撤回某一个特定状态
	 * @return
	 */
	public static CResult recallWorkItem(final CLogon ao_Logon, final int al_WorkItemID, final int al_EntryID) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkItem->recallWorkItem", getWrkLogPara(al_WorkItemID, al_EntryID));

			String ls_Sql = "";
			if (al_EntryID == -1) {
				ls_Sql = "SELECT EntryID FROM EntryInst WHERE StateID = 3 AND WorkItemID = " + al_WorkItemID;
				ls_Sql += " AND EntryType = 2 ORDER BY FinishedDate DESC";
			} else if (al_EntryID == 0) {
				ls_Sql = "SELECT EntryID FROM EntryInst WHERE StateID = 3 AND WorkItemID = " + al_WorkItemID;
				ls_Sql += " AND EntryType = 2 AND RecipientID = " + ao_Logon.getUserID() + " ORDER BY FinishedDate DESC";
			} else {
				ls_Sql = "SELECT EntryID FROM EntryInst WHERE StateID = 3 AND WorkItemID = " + al_WorkItemID;
				ls_Sql += " AND EntryID = " + String.valueOf(al_EntryID);
			}
			String ls_EntryID = null;
			try {
				ls_EntryID=CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql, String.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 错误：当前公文不可执行撤回操作
			if (MGlobal.isEmpty(ls_EntryID))
				return CResult.getResult(9999, null, "TWorkItem->recallWorkItem", "当前公文不可执行撤回操作");

			// 判断是否存在将被打开的公文
			ls_Sql = "SELECT WorkItemID, EntryID, StateID, EntryType, RecipientID, FinishedDate FROM EntryInst";
			ls_Sql += " WHERE WorkItemID = " + String.valueOf(al_WorkItemID) + " AND EntryID = " + ls_EntryID;
			int ll_ErrorID = 0;
			Date ld_FinishDate = MGlobal.getInitDate();
			List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
			if (lo_Set != null && lo_Set.size() > 0) {
				if (MGlobal.readInt(lo_Set.get(0), "RecipientID") != ao_Logon.GlobalPara.UserID) {
					// 只有公文参与人员或系统管理员才可以有撤回公文的权限
					if ((ao_Logon.GlobalPara.UserType & EUserType.SystemAdministrator) != EUserType.SystemAdministrator) {
						// 错误：您没有撤回公文的权限
						ll_ErrorID = 1075;
						return CResult.getResult(1075, null, "TWorkItem->recallWorkItem", "您没有撤回公文的权限");
					}
				}
				if (ll_ErrorID == 0) {
					if (MGlobal.readInt(lo_Set.get(0), "StateID") == 3) {
						ld_FinishDate = MGlobal.readTime(lo_Set.get(0), "FinishedDate");
					} else {
						// 错误：当前公文不可执行撤回操作
						ll_ErrorID = 1076;
						return CResult.getResult(1076, null, "TWorkItem->recallWorkItem", "当前公文不可执行撤回操作");
					}
				}
			} else {
				// 错误：不存在您要被撤回的公文
				ll_ErrorID = 1074;
				return CResult.getResult(1074, null, "TWorkItem->recallWorkItem", "不存在您要被撤回的公文");
			}
			lo_Set = null;

			// 缺省或者RECALL_END_WORKFLOW=1的情况公文允许在结束后被撤回，否则不许公文结束后被撤回
			ls_Sql = ao_Logon.getParaValue("RECALL_END_WORKFLOW");
			if (ll_ErrorID == 0 && ls_Sql.equals("0")) {
				// 判断当前公文是否还在流转中
				ls_Sql = "SELECT '1' as sign  FROM WorkItemInst WHERE WorkItemStatus = 0 AND WorkItemID = " + String.valueOf(al_WorkItemID);
				if (!CSqlHandle.getJdbcTemplate().queryForMap(ls_Sql).get("sign").equals("1")) {
					// 错误：当前公文不是正常的流转状态，您不能撤回公文
					ll_ErrorID = 1134;
					return CResult.getResult(1134, null, "TWorkItem->recallWorkItem", "当前公文不是正常的流转状态，您不能撤回公文");
				}
			}

			if (ll_ErrorID == 0) {
				// 判断当前公文是否正被某个用户打开处理
				ls_Sql = "SELECT '1' as sign FROM WorkItemLock WHERE WorkItemID = " + String.valueOf(al_WorkItemID);
				if (CSqlHandle.getValueBySql(ls_Sql, "sign").equals("1")) {
					// 错误：当前公文正被其他用户打开处理，您不能撤回公文
					ll_ErrorID = 1077;
					return CResult.getResult(1077, null, "TWorkItem->recallWorkItem", "当前公文正被其他用户打开处理，您不能撤回公文");
				}
			}

			if (ll_ErrorID == 0) {
				// 判断公文最近是否是当前用户处理的
				ls_Sql = "SELECT EntryID, StateID, ReadDate, FinishedDate FROM EntryInst WHERE WorkItemID = ";
				ls_Sql += String.valueOf(al_WorkItemID) + " AND StateID IN (2, 3) AND EntryID != " + ls_EntryID;
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				if (lo_Set != null && lo_Set.size() > 0 && ll_ErrorID == 0) {
					if (MGlobal.readInt(lo_Set.get(0), "StateID") == 2) {// 已被阅读并保存的状态
						if (MGlobal.compareDate(MGlobal.readTime(lo_Set.get(0), "ReadDate"), ld_FinishDate) == 1) {
							// 错误：您发出去的公文已经被以后的公文接收人处理过，您不能撤回公文
							ll_ErrorID = 1078;
							return CResult.getResult(1078, null, "TWorkItem->recallWorkItem", "您发出去的公文已经被以后的公文接收人处理过，您不能撤回公文");
						}
					} else {// 已被处理的状态
						if (MGlobal.compareDate(MGlobal.readTime(lo_Set.get(0), "FinishedDate"), ld_FinishDate) == 1) {
							// 错误：您发出去的公文已经被以后的公文接收人处理过，您不能撤回公文
							ll_ErrorID = 1078;
							return CResult.getResult(1078, null, "TWorkItem->recallWorkItem", "您发出去的公文已经被以后的公文接收人处理过，您不能撤回公文");
						}
					}
				}
				lo_Set = null;
			}

			try {

				final java.sql.Timestamp ld_CurDate = MGlobal.getSqlTimeNow();
				// 处理撤回情况
				ls_Sql = "UPDATE WorkItemInst SET EditDate = ?, WorkItemStatus = 0, IsFinished = 0, FinishedDate = NULL, ";
				ls_Sql += "LastUpdateType = 1, LastUpdateDate = ? WHERE WorkItemID = " + String.valueOf(al_WorkItemID);

				CSqlHandle.getJdbcTemplate().update(ls_Sql, new Object[] { ld_CurDate, ld_CurDate });

				ls_Sql = "SELECT WEID, WorkItemID, EntryID FROM EntryInst WHERE EntryType = 2 AND WorkItemID = {0}";
				ls_Sql += " AND EntryID >= {1} AND (OriginalID = {1} OR EntryID = {1})";
				ls_Sql = MessageFormat.format(ls_Sql, String.valueOf(al_WorkItemID), ls_EntryID);
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				for (Map<String, Object> row : lo_Set) {
					if (MGlobal.readString(row, "EntryID").equals(ls_EntryID)) {
						ls_Sql = "UPDATE EntryInst SET StateID = 2, FinishedDate = ?, CallDate = ? WHERE WEID = "
								+ MGlobal.readString(row, "WEID");
						CSqlHandle.getJdbcTemplate().update(ls_Sql, new Object[] { ld_CurDate, ld_CurDate });

					} else {
						ls_Sql = "UPDATE EntryInst SET StateID = 5, CallDate = ? WHERE WEID = " + MGlobal.readString(row, "WEID");
						CSqlHandle.getJdbcTemplate().update(ls_Sql, new Object[] { ld_CurDate });
					}

				}
				lo_Set = null;

				return CResult.getResult();
			} catch (Exception ex) {
				ao_Logon.Raise(ex, "TWorkItem->recallWorkItem", "");
				return CResult.getResult(ex, null, "TWorkItem->recallWorkItem", null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ao_Logon.Raise(ex, "TWorkItem->recallWorkItem", "");
			return CResult.getResult(ex, null, "TWorkItem->recallWorkItem", null);
		}
		// }
		// });
	}

	/**
	 * 获取流转的步骤标识连接串
	 * 
	 * @param ao_Item
	 *            公文对象
	 * @param al_FlowID
	 *            流程标识
	 * @return 格式：已经流转的步骤标识连接串，多个使用【;】分隔||已经流转的未处理的步骤标识连接串，多个使用【;】分隔||
	 *         已经流转的已处理的步骤标识连接串 ，多个使用【;】分隔
	 */
	public static String getFlowActIDs(CWorkItem ao_Item, int al_FlowID) {
		int ll_FlowId = (al_FlowID == 0 ? ao_Item.CurFlow.FlowID : al_FlowID);

		String ls_Ids1 = ";";
		String ls_Ids2 = ";";
		String ls_FlowIds = ";";

		for (CEntry lo_Entry : ao_Item.getEntryById(1).ChildEntries) {
			if (lo_Entry.ActivityID > 0 && lo_Entry.FlowID == ll_FlowId) {
				if (ls_FlowIds.indexOf(";" + String.valueOf(lo_Entry.ActivityID) + ";") == -1) {
					ls_FlowIds += String.valueOf(lo_Entry.ActivityID) + ";";
				}
				for (CEntry lo_Child : lo_Entry.ChildEntries) {
					if (lo_Child.EntryStatus == EEntryStatus.NotTransactStatus || lo_Child.EntryStatus == EEntryStatus.HadReadStatus
							|| lo_Child.EntryStatus == EEntryStatus.TumbleInLaunchStatus) {
						if (ls_Ids1.indexOf(";" + String.valueOf(lo_Entry.ActivityID) + ";") == -1) {
							ls_Ids1 += String.valueOf(lo_Entry.ActivityID) + ";";
						}
					} else {
						if (ls_Ids2.indexOf(";" + String.valueOf(lo_Entry.ActivityID) + ";") == -1) {
							ls_Ids2 += String.valueOf(lo_Entry.ActivityID) + ";";
						}
					}
				}
			}
		}

		return MessageFormat.format("{0}@{1}@{2}", ls_FlowIds, ls_Ids1, ls_Ids2);
	}

}
