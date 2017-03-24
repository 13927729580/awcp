package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.ESupervisedType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 人工监督对象：公文流转过程中人工监视流转情况，主要是使用在一些 处理时间相对较长，而又需要实时的人为控制而设计，起到对公文处理
 * 监督的作用，监督是赋予参与公文处理人员的特殊权利，每一个参与公 文的人员，都有权限发送监督，超级系统管理员，参与公文的人员（只
 * 针对本人发起的监督）、当前公文分类的管理员有权删除监督，而对于 参与公文的人员，有权把设置监督的情况更改成没有被监督人员情况。
 * 
 * @author Jackie.Wang
 * 
 */
public class CSupervise extends CBase {

	/**
	 * 初始化
	 */
	public CSupervise() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CSupervise(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 包含的流转实例
	 */
	public CWorkItem WorkItem = null;

	/**
	 * 包含的公文被监督人描述集合
	 */
	public ArrayList<CSuperUser> SuperUsers = new ArrayList<CSuperUser>();

	/**
	 * 根据标识获取被监督人描述对象
	 * 
	 * @param al_Id
	 * @return
	 */
	public CSuperUser getSuperUserById(int al_Id) {
		for (CSuperUser lo_Super : SuperUsers) {
			if (lo_Super.EntryID == al_Id) {
				return lo_Super;
			}
		}
		return null;
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		this.WorkItem = null;
		if (this.SuperUsers != null) {
			while (this.SuperUsers.size() > 0) {
				CSuperUser lo_User = this.SuperUsers.get(0);
				this.SuperUsers.remove(lo_User);
				lo_User.ClearUp();
				lo_User = null;
			}
			this.SuperUsers = null;
		}
		super.ClearUp();
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 监督的唯一标识
	 */
	public int SuperviseID = -1;

	/**
	 * 监督人员标识
	 */
	public int UserID = 0;

	/**
	 * 监督人员
	 */
	public String Superintendent = "";

	/**
	 * 监督人员对应的状态标识，当this.EntryID=0时，由管理员监督
	 */
	public int EntryID = 0;

	/**
	 * 读取监督人员对应的状态对象
	 */
	public CEntry getEntry() {
		if (this.WorkItem == null)
			return null;
		return this.WorkItem.getEntryById(this.EntryID);
	}

	/**
	 * 监督发起时间
	 */
	public Date StartDate = MGlobal.getNow();

	/**
	 * 监督有效截止时间
	 */
	public Date StopDate = MGlobal.getNow();

	/**
	 * 被监督人的类型
	 */
	public int SuperviseType = ESupervisedType.NotAnyBody;

	/**
	 * 设置被监督人的类型
	 * 
	 * @param value
	 */
	public void setSuperviseType(int value) {
		this.SuperviseType = value;
		if (this.SuperviseType == ESupervisedType.OtherTransactor) {
			this.BySuperintendent = "";
		}
	}

	/**
	 * 被监督人，当this.SuperviseType = OtherTransactor时有效
	 */
	public String BySuperintendent = "";

	/**
	 * 设置被监督人，当this.SuperviseType = OtherTransactor时有效
	 * 
	 * @param value
	 */
	public void setBySuperintendent(String value) {
		if (this.SuperviseType == ESupervisedType.OtherTransactor) {
			this.BySuperintendent = value;
		}
	}

	/**
	 * 监督叙述内容
	 */
	public String SuperviseContent = "";

	/**
	 * 附加消息类型
	 */
	public int AddMsgType = 0;

	/**
	 * 当前对象是否可用
	 * 
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
	 * 从Xml节点导入到对象
	 * 
	 * @param ao_Node
	 *            导入节点
	 * @param ai_Type
	 *            导入类型：=0-正常导入；=1-短属性导入；
	 */
	@Override
	protected void Import(Element ao_Node, int ai_Type) {
		// this.WorkItem.setWorkItemID(ao_Node.getAttribute("WorkItemID"));
		this.SuperviseID = Integer
				.parseInt(ao_Node.getAttribute("SuperviseID"));
		this.UserID = Integer.parseInt(ao_Node.getAttribute("UserID"));
		this.Superintendent = ao_Node.getAttribute("Superintendent");
		this.EntryID = Integer.parseInt(ao_Node.getAttribute("EntryID"));
		this.StartDate = MGlobal
				.stringToDate(ao_Node.getAttribute("StartDate"));
		this.StopDate = MGlobal.stringToDate(ao_Node.getAttribute("StopDate"));
		this.SuperviseType = Integer.parseInt(ao_Node
				.getAttribute("SupervisedType"));
		this.SuperviseContent = ao_Node.getAttribute("SuperviseContent");
		this.AddMsgType = Integer.parseInt(ao_Node.getAttribute("AddMsgType"));
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
		// ao_Bag.Write("WorkItemID",String.valueOf(this.WorkItem.WorkItemID));
		ao_Node.setAttribute("SuperviseID", String.valueOf(this.SuperviseID));
		ao_Node.setAttribute("UserID", String.valueOf(this.UserID));
		ao_Node.setAttribute("Superintendent", this.Superintendent);
		ao_Node.setAttribute("EntryID", String.valueOf(this.EntryID));
		ao_Node.setAttribute("StartDate", MGlobal.dateToString(this.StartDate));
		ao_Node.setAttribute("StopDate", MGlobal.dateToString(this.StopDate));
		ao_Node.setAttribute("SupervisedType",
				String.valueOf(this.SuperviseType));
		ao_Node.setAttribute("SuperviseContent", this.SuperviseContent);
		ao_Node.setAttribute("AddMsgType", String.valueOf(this.AddMsgType));
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
		ao_Set.put("WorkItemID", this.WorkItem.WorkItemID);
		ao_Set.put("SuperviseID", this.SuperviseID);
		ao_Set.put("UserID", this.UserID);
		ao_Set.put("Superintendent", this.Superintendent);
		ao_Set.put("EntryID", this.EntryID);
		ao_Set.put("StartDate", MGlobal.dateToSqlTime(this.StartDate));
		ao_Set.put("StopDate", MGlobal.dateToSqlTime(this.StopDate));
		ao_Set.put("SupervisedType", this.SuperviseType);
		ao_Set.put("SuperviseContent", (this.SuperviseContent.equals("") ? null
				: this.SuperviseContent));
		ao_Set.put("AddMsgType", this.AddMsgType);
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
			ls_Sql = "INSERT INTO UserSupervise "
					+ "(WorkItemID, SuperviseID, UserID, Superintendent, EntryID, StartDate, StopDate, "
					+ "SupervisedType, SuperviseContent, AddMsgType) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			ls_Sql = "UPDATE UserSupervise SET "
					+ "UserID = ?, Superintendent = ?, EntryID = ?, StartDate = ?, StopDate = ?, "
					+ "SupervisedType = ?, SuperviseContent = ?, AddMsgType = ? "
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
			parasList.add(this.WorkItem.WorkItemID);
			parasList.add(this.SuperviseID);
		}

		parasList.add(this.UserID);
		parasList.add(this.Superintendent);
		parasList.add(this.EntryID);
		parasList.add(MGlobal.dateToSqlTime(this.StartDate));
		parasList.add(MGlobal.dateToSqlTime(this.StopDate));
		parasList.add(this.SuperviseType);
		parasList.add((this.SuperviseContent.equals("") ? null
				: this.SuperviseContent));
		parasList.add(this.AddMsgType);

		if (ai_SaveType == 1) {
			parasList.add(this.WorkItem.WorkItemID);
			parasList.add(this.SuperviseID);
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
		// this.WorkItem.setWorkItemID(ao_Set.get("WorkItemID"));
		this.SuperviseID = (Integer) ao_Set.get("SuperviseID");
		this.UserID = (Integer) ao_Set.get("UserID");
		this.Superintendent = (String) ao_Set.get("Superintendent");
		this.EntryID = (Integer) ao_Set.get("EntryID");
		this.StartDate = (Date) ao_Set.get("StartDate");
		this.StopDate = (Date) ao_Set.get("StopDate");
		this.SuperviseType = (Integer) ao_Set.get("SupervisedType");
		if (ao_Set.get("SuperviseContent") != null) {
			this.SuperviseContent = (String) ao_Set.get("SuperviseContent");
		}
		if (ao_Set.get("AddMsgType") != null) {
			this.AddMsgType = (Integer) ao_Set.get("AddMsgType");
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
		// ao_Bag.Write("WorkItemID", this.WorkItem.WorkItemID);
		ao_Bag.Write("SuperviseID", this.SuperviseID);
		ao_Bag.Write("UserID", this.UserID);
		ao_Bag.Write("Superintendent", this.Superintendent);
		ao_Bag.Write("EntryID", this.EntryID);
		ao_Bag.Write("StartDate", this.StartDate);
		ao_Bag.Write("StopDate", this.StopDate);
		ao_Bag.Write("SupervisedType", this.SuperviseType);
		ao_Bag.Write("SuperviseContent", this.SuperviseContent);
		ao_Bag.Write("AddMsgType", this.AddMsgType);
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
		// this.WorkItem.WorkItemID = ao_Bag.ReadInt("WorkItemID");
		this.SuperviseID = ao_Bag.ReadInt("SuperviseID");
		this.UserID = ao_Bag.ReadInt("UserID");
		this.Superintendent = ao_Bag.ReadString("Superintendent");
		this.EntryID = ao_Bag.ReadInt("EntryID");
		this.StartDate = ao_Bag.ReadDate("StartDate");
		this.StopDate = ao_Bag.ReadDate("StopDate");
		this.SuperviseType = ao_Bag.ReadInt("SupervisedType");
		this.SuperviseContent = ao_Bag.ReadString("SuperviseContent");
		this.AddMsgType = ao_Bag.ReadInt("AddMsgType");
	}

}
