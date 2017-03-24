package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.ESuperviseStatus;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 公文被监督人描述
 * 
 * @author Jackie.Wang
 * 
 */
public class CSuperUser extends CBase {

	/**
	 * 初始化
	 */
	public CSuperUser() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CSuperUser(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的人工监督对象
	 */
	public CSupervise Supervise = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 被监督人员对应的状态标识
	 */
	public int EntryID = 0;

	/**
	 * 监督状态
	 */
	public int SuperviseStatus = ESuperviseStatus.NotReadSupervise;

	/**
	 * 阅读时间
	 */
	public Date ReadDate = MGlobal.getNow();

	/**
	 * 回复时间
	 */
	public Date ResponseDate = MGlobal.getNow();

	/**
	 * 回复内容
	 */
	public String ResponseContent = "";

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			// Add Get Valid Code...
			return "";
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
			return e.toString();
		}
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属的流转实例
		this.Supervise = null;
		super.ClearUp();
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
		// this.Supervise.WorkItem.setWorkItemID(Integer.parseInt(ao_Node.getAttribute("WorkItemID")));
		// this.Supervise.setSuperviseID(Integer.parseInt(ao_Node.getAttribute("SuperviseID")));
		this.EntryID = Integer.parseInt(ao_Node.getAttribute("EntryID"));
		this.SuperviseStatus = Integer.parseInt(ao_Node
				.getAttribute("SuperviseStatus"));
		this.ReadDate = MGlobal.stringToDate(ao_Node.getAttribute("ReadDate"));
		this.ResponseDate = MGlobal.stringToDate(ao_Node
				.getAttribute("ResponseDate"));
		this.ResponseContent = ao_Node.getAttribute("ResponseContent");
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
		ao_Node.setAttribute("WorkItemID",
				String.valueOf(this.Supervise.WorkItem.WorkItemID));
		ao_Node.setAttribute("SuperviseID",
				String.valueOf(this.Supervise.SuperviseID));
		ao_Node.setAttribute("EntryID", String.valueOf(this.EntryID));
		ao_Node.setAttribute("SuperviseStatus",
				String.valueOf(this.SuperviseStatus));
		ao_Node.setAttribute("ReadDate", MGlobal.dateToString(this.ReadDate));
		ao_Node.setAttribute("ResponseDate",
				MGlobal.dateToString(this.ResponseDate));
		ao_Node.setAttribute("ResponseContent", this.ResponseContent);
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
		ao_Set.put("WorkItemID", this.Supervise.WorkItem.WorkItemID);
		ao_Set.put("SuperviseID", this.Supervise.SuperviseID);
		ao_Set.put("EntryID", this.EntryID);
		ao_Set.put("SuperviseStatus", this.SuperviseStatus);
		ao_Set.put("ReadDate", MGlobal.dateToSqlTime(this.ReadDate));
		ao_Set.put("ResponseDate", MGlobal.dateToSqlTime(this.ResponseDate));
		ao_Set.put("ResponseContent", (this.ResponseContent.equals("") ? null
				: this.ResponseContent));
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
			ls_Sql = "INSERT INTO SuperviseUser "
					+ "(WorkItemID, SuperviseID, EntryID, SuperviseStatus, ReadDate, ResponseDate, ResponseContent) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		} else {
			ls_Sql = "UPDATE SuperviseUser SET "
					+ "EntryID = ?, SuperviseStatus = ?, ReadDate = ?, ResponseDate = ?, ResponseContent = ? "
					+ "WHERE WorkItemID = ? AND SuperviseID = ?";
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
			parasList.add(this.Supervise.WorkItem.WorkItemID);
			parasList.add(this.Supervise.SuperviseID);
		}

		parasList.add(this.EntryID);
		parasList.add(this.SuperviseStatus);
		parasList.add(MGlobal.dateToSqlTime(this.ReadDate));
		parasList.add(MGlobal.dateToSqlTime(this.ResponseDate));
		parasList.add((this.ResponseContent.equals("") ? null
				: this.ResponseContent));

		if (ai_SaveType == 1) {
			parasList.add(this.Supervise.WorkItem.WorkItemID);
			parasList.add(this.Supervise.SuperviseID);
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
		// this.Supervise.WorkItem.setWorkItemID(ao_Set.getInt("WorkItemID"));
		// this.Supervise.setSuperviseID(ao_Set.getInt("SuperviseID"));
		this.EntryID = (Integer) ao_Set.get("EntryID");
		this.SuperviseStatus = (Integer) ao_Set.get("SuperviseStatus");
		if (ao_Set.get("ReadDate") != null) {
			this.ReadDate = (Date) ao_Set.get("ReadDate");
		}
		if (ao_Set.get("ResponseDate") != null) {
			this.ResponseDate = (Date) ao_Set.get("ResponseDate");
		}
		if (ao_Set.get("ResponseContent") != null) {
			this.ResponseContent = (String) ao_Set.get("ResponseContent");
		}
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
		ao_Bag.Write("EntryID", this.EntryID);
		ao_Bag.Write("SuperviseStatus", this.SuperviseStatus);
		ao_Bag.Write("ReadDate", this.ReadDate);
		ao_Bag.Write("ResponseDate", this.ResponseDate);
		ao_Bag.Write("ResponseContent", this.ResponseContent);
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
		this.EntryID = ao_Bag.ReadInt("EntryID");
		this.SuperviseStatus = ao_Bag.ReadInt("SuperviseStatus");
		this.ReadDate = ao_Bag.ReadDate("ReadDate");
		this.ResponseDate = ao_Bag.ReadDate("ResponseDate");
		this.ResponseContent = ao_Bag.ReadString("ResponseContent");
	}

}
