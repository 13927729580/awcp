package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 实体类-公文转发登记
 * 
 * @author admin
 * 
 */
public class CWorkItemTran extends CBase {

	/**
	 * 初始化
	 */
	public CWorkItemTran() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CWorkItemTran(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 公文标识
	 */
	public int WorkItemId = 0;

	/**
	 * 状态标识：=0 - 表示为管理员转发；>0 - 表示为实际状态转发；
	 */
	public int EntryId = 0;

	/**
	 * 接受人标识
	 */
	public int AcceptId = 0;

	/**
	 * 转发人标识
	 */
	public int UserId = 0;

	/**
	 * 接收人名称
	 */
	public String AcceptName = "";

	/**
	 * 转发人员
	 */
	public String UserName = "";

	/**
	 * 转发状态：=0 - 未读；=1 - 已读；=2 - 已处理；
	 */
	public int TransStatus = 0;

	/**
	 * 转发时间
	 */
	public Date TransDate = MGlobal.getInitDate();

	/**
	 * 阅读时间
	 */
	public Date ReadDate = MGlobal.getInitDate();

	/**
	 * 转发意见
	 */
	public String TransComment = "";

	/**
	 * 处理意见
	 */
	public String ReadComment = "";

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
		WorkItemId = Integer.parseInt(ao_Node.getAttribute("WorkitemId"));
		EntryId = Integer.parseInt(ao_Node.getAttribute("EntryId"));
		AcceptId = Integer.parseInt(ao_Node.getAttribute("AcceptId"));
		UserId = Integer.parseInt(ao_Node.getAttribute("UserId"));
		AcceptName = ao_Node.getAttribute("AcceptName");
		UserName = ao_Node.getAttribute("UserName");
		TransStatus = Integer.parseInt(ao_Node.getAttribute("TransStatus"));
		TransDate = MGlobal.stringToDate(ao_Node.getAttribute("TransDate"));
		ReadDate = MGlobal.stringToDate(ao_Node.getAttribute("ReadDate"));
		TransComment = ao_Node.getAttribute("TransComment");
		ReadComment = ao_Node.getAttribute("ReadComment");
	}

	/**
	 * 从对象导出到Xml节点
	 * 
	 * @param ao_Node
	 *            导出节点
	 * @param ai_Type
	 *            导出类型：=0-正常导出；=1-短属性导出；
	 */
	@Override
	protected void Export(Element ao_Node, int ai_Type) {
		ao_Node.setAttribute("WorkitemId", String.valueOf(WorkItemId));
		ao_Node.setAttribute("EntryId", String.valueOf(EntryId));
		ao_Node.setAttribute("AcceptId", String.valueOf(AcceptId));
		ao_Node.setAttribute("UserId", String.valueOf(UserId));
		ao_Node.setAttribute("AcceptName", AcceptName);
		ao_Node.setAttribute("UserName", UserName);
		ao_Node.setAttribute("TransStatus", String.valueOf(TransStatus));
		ao_Node.setAttribute("TransDate", MGlobal.dateToString(TransDate));
		ao_Node.setAttribute("ReadDate", MGlobal.dateToString(ReadDate));
		ao_Node.setAttribute("TransComment", TransComment);
		ao_Node.setAttribute("ReadComment", ReadComment);
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
	protected void Save(Map<String, Object> ao_Set, int ai_Type)
			throws Exception {
		ao_Set.put("WorkItemID", WorkItemId);
		ao_Set.put("EntryID", EntryId);
		ao_Set.put("AcceptID", AcceptId);
		ao_Set.put("UserID", UserId);
		ao_Set.put("AcceptName", AcceptName);
		ao_Set.put("UserName", UserName);
		ao_Set.put("TransStatus", TransStatus);
		ao_Set.put("TransDate", MGlobal.dateToSqlTime(TransDate));
		ao_Set.put("ReadDate", MGlobal.dateToSqlTime(ReadDate == MGlobal
				.getInitDate() ? null : ReadDate));
		ao_Set.put("TransComment", (TransComment.equals("") ? null
				: TransComment));
		ao_Set.put("ReadComment", (ReadComment.equals("") ? null : ReadComment));
	}

	/**
	 * 保存
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @return
	 * @throws SQLException
	 */
	public String Save(int ai_SaveType, int ai_Type) {
		String ls_Sql = null;
		if (ai_SaveType == 0) {
			ls_Sql = "INSERT INTO WorkItemTransfer "
					+ "(WorkItemID, EntryID, AcceptID, UserID, AcceptName, UserName, "
					+ "TransStatus, TransDate, ReadDate, TransComment, ReadComment) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			ls_Sql = "UPDATE WorkItemTransfer SET "
					+ "AcceptID = ?, UserID = ?, AcceptName = ?, UserName = ?, "
					+ "TransStatus = ?, TransDate = ?, ReadDate = ?, TransComment = ?, ReadComment = ? "
					+ "WHERE WorkItemID = ? AND EntryID = ?";
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
	 * @throws SQLException
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type) {
		List parasList = new ArrayList();
		if (ai_SaveType == 0) {
			parasList.add(WorkItemId);
			parasList.add(EntryId);
		}

		parasList.add(AcceptId);
		parasList.add(UserId);
		parasList.add(AcceptName);
		parasList.add(UserName);
		parasList.add(TransStatus);
		parasList.add(MGlobal.dateToSqlTime(TransDate));
		parasList
				.add(MGlobal.dateToSqlTime(ReadDate == MGlobal.getInitDate() ? null
						: ReadDate));
		parasList.add((TransComment.equals("") ? null : TransComment));
		parasList.add((ReadComment.equals("") ? null : ReadComment));

		if (ai_SaveType == 1) {
			parasList.add(WorkItemId);
			parasList.add(EntryId);
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
		WorkItemId = (Integer) ao_Set.get("WorkItemID");
		EntryId = (Integer) ao_Set.get("EntryID");
		AcceptId = (Integer) ao_Set.get("AcceptID");
		UserId = (Integer) ao_Set.get("UserID");
		AcceptName = (String) ao_Set.get("AcceptName");
		UserName = (String) ao_Set.get("UserName");
		TransStatus = (Integer) ao_Set.get("TransStatus");
		TransDate = (Date) ao_Set.get("TransDate");
		if (ao_Set.get("ReadDate") != null)
			ReadDate = (Date) ao_Set.get("ReadDate");
		if (ao_Set.get("TransComment") != null)
			TransComment = (String) ao_Set.get("TransComment");
		if (ao_Set.get("ReadComment") != null)
			ReadComment = (String) ao_Set.get("ReadComment");
	}

	/**
	 * 对象打包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 */
	@Override
	protected void Write(MBag ao_Bag, int ai_Type) {
		ao_Bag.Write("WorkitemId", WorkItemId);
		ao_Bag.Write("EntryId", EntryId);
		ao_Bag.Write("AcceptId", AcceptId);
		ao_Bag.Write("UserId", UserId);
		ao_Bag.Write("AcceptName", AcceptName);
		ao_Bag.Write("UserName", UserName);
		ao_Bag.Write("TransStatus", TransStatus);
		ao_Bag.Write("TransDate", TransDate);
		ao_Bag.Write("ReadDate", ReadDate);
		ao_Bag.Write("TransComment", TransComment);
		ao_Bag.Write("ReadComment", ReadComment);
	}

	/**
	 * 对象解包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导入；=1-短属性导入；
	 */
	@Override
	protected void Read(MBag ao_Bag, int ai_Type) {
		WorkItemId = ao_Bag.ReadInt("WorkitemId");
		EntryId = ao_Bag.ReadInt("EntryId");
		AcceptId = ao_Bag.ReadInt("AcceptId");
		UserId = ao_Bag.ReadInt("UserId");
		AcceptName = ao_Bag.ReadString("AcceptName");
		UserName = ao_Bag.ReadString("UserName");
		TransStatus = ao_Bag.ReadInt("TransStatus");
		TransDate = ao_Bag.ReadDate("TransDate");
		ReadDate = ao_Bag.ReadDate("ReadDate");
		TransComment = ao_Bag.ReadString("TransComment");
		ReadComment = ao_Bag.ReadString("ReadComment");
	}

}
