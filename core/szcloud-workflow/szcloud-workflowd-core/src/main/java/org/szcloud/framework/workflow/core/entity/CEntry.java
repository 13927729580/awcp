package org.szcloud.framework.workflow.core.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.emun.EDatabaseType;
import org.szcloud.framework.workflow.core.emun.EEntryRecipientType;
import org.szcloud.framework.workflow.core.emun.EEntryStatus;
import org.szcloud.framework.workflow.core.emun.EEntryType;
import org.szcloud.framework.workflow.core.emun.ERemindAddFlag;
import org.szcloud.framework.workflow.core.emun.EResponseRight;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 流转状态对象：记录动态公文流转数据变化的对象，它与流程对象中的步骤对象有很深关系。
 * 
 * @author Jackie.Wang
 * 
 */
public class CEntry extends CBase {
	/**
	 * 初始化
	 */
	public CEntry() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CEntry(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的流转实例
	 */
	public CWorkItem WorkItem = null;

	/**
	 * 当前状态的父状态
	 */
	public CEntry ParentEntry = null;

	/**
	 * 所包含的子状态的集合
	 */
	public ArrayList<CEntry> ChildEntries = null;

	/**
	 * 根据状态标识获取子状态对象
	 * 
	 * @param al_Id
	 * @return
	 */
	public CEntry getEntryById(int al_Id) {
		if (this.ChildEntries == null)
			return null;
		for (CEntry lo_Entry : this.ChildEntries) {
			if (lo_Entry.EntryID == al_Id)
				return lo_Entry;
		}
		return null;
	}

	/**
	 * 所属的步骤对象
	 */
	private CActivity mo_Activity = null;

	/**
	 * 移动应用状态
	 */
	public CMoveEntry MoveEntry = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 状态表自增标识：=0-新增；>0-修改；
	 */
	public int ID = 0;

	/**
	 * 状态标识
	 */
	public int EntryID = 0;

	/**
	 * 状态类型
	 */
	public int EntryType = EEntryType.NotAnyEntry;

	/**
	 * 状态类型
	 * 
	 * @param value
	 */
	public void setEntryType(int value) {
		if (this.EntryType == EEntryType.NotAnyEntry) {
			this.EntryType = value;
			if (this.EntryType == EEntryType.VirtualEntry) {
				this.ChildEntries = new ArrayList<CEntry>();
			}
		}

		this.EntryChanged = true;
	}

	/**
	 * 读取状态类型名称
	 * 
	 * @return
	 */
	public String getEntryTypeName() {
		switch (this.EntryType) {
		case EEntryType.NotAnyEntry:
			return "非任何状态";
		case EEntryType.VirtualEntry:
			return "虚拟状态";
		case EEntryType.ActualityEntry:
			return "流转状态";
		}
		return "";
	}

	/**
	 * 处理状态类型
	 */
	public int EntryStatus = EEntryStatus.NotTransactStatus;

	/**
	 * 处理状态类型
	 * 
	 * @param value
	 */
	public void setEntryStatus(int value) {
		if (this.EntryType == EEntryType.ActualityEntry) {
			this.EntryStatus = value;
			switch (this.EntryStatus) {
			case EEntryStatus.NotTransactStatus:
				this.InceptDate = new Date();
				break;
			case EEntryStatus.TransactingStatus:
				this.ReadingDate = new Date();
				break;
			case EEntryStatus.HadTransactStatus:
				this.FinishedDate = new Date();
				break;
			case EEntryStatus.HadOverdueStatus:
				this.OverdueDate = new Date();
				break;
			case EEntryStatus.HadRecallStatus:
				this.RecallDate = new Date();
				break;
			case EEntryStatus.TumbleInLaunchStatus:
				this.InceptDate = new Date();
				break;
			case EEntryStatus.OtherStatus:
				break;
			case EEntryStatus.SucceedFlowStatus:
				this.FinishedDate = new Date();
				break;
			case EEntryStatus.FailFlowStatus:
				this.FinishedDate = new Date();
				break;
			}
			this.EntryChanged = true;
		}
	}

	/**
	 * 内部设置处理状态类型
	 * 
	 * @param value
	 */
	public void setInterEntryStatus(int value) {
		this.EntryStatus = value;
	}

	/**
	 * 读取处理状态类型名称
	 * 
	 * @return
	 */
	public String getEntryStatusName() {
		switch (this.EntryStatus) {
		case EEntryStatus.NotTransactStatus:
			return "未处理";
		case EEntryStatus.TransactingStatus:
			return "已阅读";
		case EEntryStatus.HadTransactStatus:
			return "已完成";
		case EEntryStatus.HadOverdueStatus:
			return "已过期";
		case EEntryStatus.HadRecallStatus:
			return "已撤回";
		case EEntryStatus.TumbleInLaunchStatus:
			return "启动子流程";
		case EEntryStatus.OtherStatus:
			return "其他状态";
		case EEntryStatus.SucceedFlowStatus:
			return "成功流转";
		case EEntryStatus.FailFlowStatus:
			return "失败流转";
		}
		return "未知";
	}

	/**
	 * 状态处理人分类
	 */
	public int RecipientType = EEntryRecipientType.CommondEntryRecipient;

	/**
	 * 状态处理人员标识
	 */
	public int ParticipantID = 0;

	/**
	 * 设置状态处理人员标识和部门内容
	 * 
	 * @param value
	 * @throws SQLException
	 */
	public void setParticipantID2(int value) throws SQLException {
		this.ParticipantID = value;
		if (this.DeptID > 0)
			return; // 已经设置
		if (this.ParticipantID == 0)
			return; // 不需要设置

		// 设置部门信息
		String ls_Sql = "SELECT DeptID, DeptName FROM DeptInfo WHERE DeptID IN (SELECT DeptID FROM UserInfo WHERE UserID = "
				+ Integer.toString(this.ParticipantID) + ")";
		List<Map<String,Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
		if (lo_Set!=null && lo_Set.size()>0) {
			this.DeptID = MGlobal.readInt(lo_Set.get(0), "DeptID");
			this.DeptName = MGlobal.readString(lo_Set.get(0), "DeptName");
		}
		lo_Set = null;
	}

	/**
	 * 状态处理人员
	 */
	public String Participant = "";

	/**
	 * 状态处理人所属部门标识，<br>
	 * =0 - 表示当前状态处理人为无部门用户
	 */
	public int DeptID = 0;

	/**
	 * 状态处理人所属部门
	 */
	public String DeptName = "";

	/**
	 * 状态代理处理人员标识
	 */
	public int ProxyParticipantID = 0;

	/**
	 * 状态代理处理人员
	 */
	public String ProxyParticipant = "";

	/**
	 * 处理角色标识-岗位、岗位组、部门组
	 */
	public int PostParticipantID = 0;

	/**
	 * 处理角色标识-岗位、岗位组、部门组
	 */
	public String PostParticipant = "";

	/**
	 * 状态收到时间
	 */
	public Date InceptDate = MGlobal.getNow();

	/**
	 * 设置状态收到时间
	 * 
	 * @param value
	 */
	public void setInceptDate(Date value) {
		this.InceptDate = value;
		mo_Status.NotTransact = true;
	}

	/**
	 * 状态阅读时间
	 */
	public Date ReadingDate = MGlobal.getInitDate();

	/**
	 * 设置状态阅读时间
	 * 
	 * @param value
	 */
	public void setReadingDate(Date value) {
		this.ReadingDate = value;
		mo_Status.Transacting = true;
	}

	/**
	 * 状态完成时间
	 */
	public Date FinishedDate = MGlobal.getInitDate();

	/**
	 * 设置状态完成时间
	 * 
	 * @param value
	 */
	public void setFinishedDate(Date value) {
		this.FinishedDate = value;
		mo_Status.HadTransact = true;
	}

	/**
	 * 状态报警时间
	 */
	public Date AlterDate = MGlobal.getInitDate();

	/**
	 * 状态报警间隔（单位：分钟，=0-表示没有报警设置）
	 */
	public int AlterInteval = 0;

	/**
	 * 状态报警次数
	 */
	public int AlterNumber = 0;

	/**
	 * 状态报警人员，是用户标识的连接串
	 */
	public String AlterUsers = "";

	/**
	 * 设置状态报警人员，是用户标识的连接串
	 * 
	 * @param value
	 */
	public void setAlterUsers(String value) {
		if (MGlobal.BeyondOfLength(value, 2000) == false)
			return;
		this.AlterUsers = value;
	}

	/**
	 * 状态过期时间
	 */
	public Date OverdueDate = MGlobal.getInitDate();

	/**
	 * 设置状态过期时间
	 * 
	 * @param value
	 */
	public void setOverdueDate(Date value) {
		this.OverdueDate = value;
		mo_Status.HadOverdue = true;
	}

	/**
	 * 状态撤回时间
	 */
	public Date RecallDate = MGlobal.getInitDate();

	/**
	 * 设置状态撤回时间
	 * 
	 * @param value
	 */
	public void setRecallDate(Date value) {
		this.RecallDate = value;
		mo_Status.HadRecall = true;
	}

	/**
	 * 父状态标识
	 */
	public int ParentID = 0;

	/**
	 * 设置父状态标识
	 * 
	 * @param value
	 */
	public void setParentID(int value) {
		if (this.ParentID == value)
			return;
		if (this.ParentEntry != null) {
			this.ParentEntry.ChildEntries.remove(this);
			this.ParentEntry = null;
		}
		this.ParentID = value;
		if (this.ParentID > 0) {
			this.ParentEntry = this.WorkItem.getEntryById(this.ParentID);
			this.ParentEntry.ChildEntries.add(this);
		}
	}

	/**
	 * 发送（原始）状态标识
	 */
	public int OrginalID = 0;

	/**
	 * 取得发送（原始）状态
	 * 
	 * @return
	 */
	public CEntry getOrginalEntry() {
		return this.WorkItem.getEntryById(this.OrginalID);
	}

	/**
	 * 相同意义状态标识-该标识用来处理岗位组或部门组中采用多个用户流转的情况
	 */
	public int SameEntryID = 0;

	/**
	 * 取得相同意义状态
	 * 
	 * @return
	 */
	public CEntry getSameEntry() {
		return this.WorkItem.getEntryById(this.SameEntryID);
	}

	/**
	 * 状态对应的流程标识
	 */
	public int FlowID = 0;

	/**
	 * 状态对应的步骤标识
	 */
	public int ActivityID = 0;

	/**
	 * 状态对应的步骤名称
	 */
	public String ActivityName = "";

	/**
	 * 当前处理人所属角色标识，同状态所对应的步骤标识一起控制状态的权限<br>
	 * =-1 - 表示不属于任何角色<br>
	 * =0 - 系统角色<br>
	 * >0 - 用户角色
	 */
	public int RoleID = 0;

	/**
	 * 当前处理人所属角色名称
	 */
	public String RoleName;

	/**
	 * 状态批示内容索引<br>
	 * =-2 - 表示没有批示内容<br>
	 * =-1 - 表示步骤内部传阅<br>
	 * = 0 - 表示步骤转发情况<br>
	 * > 0 - 表示选取某一批示选项
	 */
	public int Choice = -2;

	/**
	 * 设置状态批示内容索引<br>
	 * =-2 - 表示没有批示内容<br>
	 * =-1 - 表示步骤内部传阅<br>
	 * = 0 - 表示步骤转发情况<br>
	 * > 0 - 表示选取某一批示选项
	 * 
	 * @param value
	 */
	public void setChoice(int value) {
		// 虚拟节点
		if (this.EntryType != EEntryType.ActualityEntry)
			return;
		this.EntryChanged = true;

		CActivity lo_Act = this.getActivity();
		if (lo_Act == null)
			return;
		// 如果是嵌入或启动步骤，不需要验证其他内容
		if (lo_Act.ActivityType == EActivityType.TumbleInActivity
				|| lo_Act.ActivityType == EActivityType.LaunchActivity) {
			this.Choice = value;
			return;
		}

		// 非开始或处理步骤
		if (lo_Act.ActivityType != EActivityType.StartActivity
				&& lo_Act.ActivityType != EActivityType.TransactActivity)
			return;

		// 没有批示选项时
		if (lo_Act.Transact.ResponseChoices.equals("") && value > 0)
			return;
		// 有批示选项时
		if (!lo_Act.Transact.ResponseChoices.equals("") && value == -2)
			return;

		// 表示没有批示内容
		if (value == -2) {
			this.Choice = value;
			//this.ResponseContent = "";
			return;
		}

		// 表示步骤内部传阅
		if (value == -1) {
			// 不许内部传阅
			if (!lo_Act.Transact.InsideReading)
				return;
			// 内部传阅
			this.Choice = value;
			if (MGlobal.isEmpty(this.ResponseContent)) this.ResponseContent = lo_Act.Transact.getReadingLabel();
			return;
		}

		// 表示步骤转发情况
		if (value == 0) {
			// 转发
			this.Choice = value;
			if (MGlobal.isEmpty(this.ResponseContent)) this.ResponseContent = lo_Act.Transact.getLapseToLabel();
			return;
		}

		// 表示选取某一批示选项
		String[] ls_Str = lo_Act.Transact.ResponseChoices.split(";");
		if ((ls_Str.length - 1) - 0 < value) {
			this.Choice = 1;
			this.ResponseContent = ls_Str[0];
		} else {
			this.Choice = value;
			this.ResponseContent = ls_Str[0 + value - 1];
		}
	}

	/**
	 * 读取状态批示内容
	 * 
	 * @return
	 */
	public String getChoiceContent() {
		if (this.Choice <= 0)
			return "";

		CActivity lo_Activity = this.getActivity();
		if (lo_Activity == null)
			return "";

		if (lo_Activity.ActivityType == EActivityType.StartActivity
				|| lo_Activity.ActivityType == EActivityType.TransactActivity) {
			if (MGlobal.isEmpty(lo_Activity.Transact.ResponseChoices))
				return "";
			String ls_Array[] = lo_Activity.Transact.ResponseChoices.split(";");
			return ls_Array[this.Choice - 1];
		}

		return "";
	}

	/**
	 * 设置状态批示内容
	 * 
	 * @param value
	 */
	public void setChoiceContent(String value) {
		CActivity lo_Activity = this.getActivity();
		if (lo_Activity == null)
			return;

		// 如果是嵌入或启动步骤，不需要验证其他内容
		if (lo_Activity.ActivityType == EActivityType.TumbleInActivity
				|| lo_Activity.ActivityType == EActivityType.LaunchActivity) {
			this.ResponseContent = value;
			return;
		}

		// 开始或处理步骤
		if (lo_Activity.ActivityType == EActivityType.StartActivity
				|| lo_Activity.ActivityType == EActivityType.TransactActivity) {
			String ls_Array[] = lo_Activity.Transact.ResponseChoices.split(";");
			for (int i = 0; i < ls_Array.length; i++) {
				if (MGlobal.isExist(ls_Array[i]) && ls_Array[i].equals(value)) {
					this.Choice = i + 1;
					this.ResponseContent = ls_Array[i];
					this.EntryChanged = true;
				}
			}
		}
	}

	/**
	 * 当步骤转发时选择择的转发步骤标识<br>
	 * =-2 - 表示转发给前步骤[如果该步骤没有则创建]<br>
	 * =-1 - 表示转发给后步骤[如果该步骤没有则创建]<br>
	 * =0 - 表示转发给特殊步骤[如果该步骤没有则创建]<br>
	 * >0 - 表示转发给指定步骤
	 */
	public int TransmitActivityID = 0;

	/**
	 * 状态批示内容
	 */
	public String ResponseContent = "";

	/**
	 * 设置状态批示内容
	 * 
	 * @param value
	 */
	public void setResponseContent(String value) {
		this.ResponseContent = MGlobal.getStaidString(value, 2000);
		this.EntryChanged = true;
	}

	/**
	 * 意见内容
	 */
	public String Comment = "";

	/**
	 * 签名内容
	 */
	public String Sign = "";

	/**
	 * 声音内容
	 */
	public String Voice = "";

	/**
	 * 当流程处理为业务处理时，需要流转与外部的业务应用相联系，此时使用以下两个参数供业务系统使用，<br>
	 * 以达到业务与流转核心构件的集成使用 状态调用外部参数设置一
	 */
	public String Parameter1 = "";

	/**
	 * 设置状态调用外部参数设置一
	 * 
	 * @param value
	 */
	public void setParameter1(String value) {
		if (MGlobal.BeyondOfLength(value, 255) == false)
			return;
		this.Parameter1 = value;
		this.EntryChanged = true;
	}

	/**
	 * 状态调用外部参数设置二
	 */
	public String Parameter2 = "";

	/**
	 * 设置状态调用外部参数设置二
	 * 
	 * @param value
	 */
	public void setParameter2(String value) {
		if (MGlobal.BeyondOfLength(value, 1000) == false)
			return;
		this.Parameter2 = value;
		this.EntryChanged = true;
	}

	/**
	 * 过期处理类型<br>
	 * =0 - 不需要处理；<br>
	 * =1 - 按正常流转处理；<br>
	 * =2 - 发送给指定用户；<br>
	 * =4 - 发送给指定步骤；<br>
	 * =8 - 结束公文流转；<br>
	 * =-1 - 已处理；
	 */
	public int DueTransactType = 0;

	/**
	 * 过期人数
	 */
	public int DueUserNumber = 0;

	/**
	 * 设置过期人数
	 * 
	 * @param value
	 */
	public void setDueUserNumber(int value) {
		this.DueUserNumber = (value < 0 ? 0 : value);
	}

	/**
	 * 公文报警的最大次数（超过规定数目公文将过期）
	 */
	public int AlertMaxNumber = 0;

	/**
	 * 督办次数
	 */
	public int SuperviseNumber = 0;

	/**
	 * 设置公文报警的最大次数（超过规定数目公文将过期）
	 * 
	 * @param value
	 */
	public void setAlertMaxNumber(int value) {
		this.AlertMaxNumber = (value > 0 ? value : 0);
	}

	/**
	 * 状态处理情况，内存数据不需要保存
	 */
	private CEntryStatus mo_Status = new CEntryStatus();

	/**
	 * 附加提醒方式
	 */
	public int RemindAddFlag = ERemindAddFlag.DefaultRemindFlag;

	/**
	 * 移动设备集成类型<br>
	 * =1 - 集成短信处理；<br>
	 * =2 - 集成PDA处理；<br>
	 * =4 - 是否返回回复意见；
	 */
	public int MobileType = 0;

	/**
	 * 读取审批打包内容
	 * 
	 * @return
	 */
	public String getResponseImageContent() {
		if (this.Comment.length() + this.Sign.length() + this.Voice.length() == 0)
			return "";
		MBag lo_Bag = new MBag("");
		lo_Bag.Write("ms_Comment", this.Comment);
		lo_Bag.Write("ms_Sign", this.Sign);
		lo_Bag.Write("ms_Voice", this.Voice);
		String ls_Value = lo_Bag.Content;
		lo_Bag = null;
		return ls_Value;
	}

	/**
	 * 设置审批打包内容
	 * 
	 * @param value
	 */
	public void setResponseImageContent(String value) {
		MBag lo_Bag = new MBag(value);
		this.Comment = (String) lo_Bag.Read("ms_Comment");
		this.Sign = (String) lo_Bag.Read("ms_Sign");
		this.Voice = (String) lo_Bag.Read("ms_Voice");
		lo_Bag = null;
	}

	/**
	 * 状态内容是否有更改，内存数据不需要保存
	 */
	public boolean EntryChanged = false;

	// #==========================================================================#
	// 常用过程或函数定义
	// #==========================================================================#

	/**
	 * 取得状对应的步骤
	 * 
	 * @return
	 */
	public CActivity getActivity() {
		// 保证在打公文以后
		if (this.WorkItem.CurFlow == null)
			return null;
		if (mo_Activity != null)
			return mo_Activity;

		CFlow lo_Flow = null;
		if (this.FlowID == 0 || this.FlowID == this.WorkItem.CurFlow.FlowID) {
			lo_Flow = this.WorkItem.CurFlow;
		} else {
			lo_Flow = this.WorkItem.getFlowByID(this.FlowID);
		}
		if (lo_Flow == null)
			return null;
		mo_Activity = lo_Flow.getActivityById(this.ActivityID);

		return mo_Activity;
	}

	/**
	 * 获取是否是并发处理状态
	 * 
	 * @return
	 * @throws Exception 
	 */
	public boolean getIsInterEntry() throws Exception {
		try {
			CActivity lo_Act = this.getActivity();
			if (lo_Act == null)
				return false;
			if (lo_Act.ActivityType != EActivityType.TransactActivity)
				return false;
			return lo_Act.Transact.Intercurrent;
		} catch (Exception e) {
			this.Raise(e, "getIsInterEntry", null);
		}
		return false;
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 当前状态的父状态
		this.ParentEntry = null;

		// 包含的子状态的集合
		if (this.ChildEntries != null) {
			while (this.ChildEntries.size() > 0) {
				this.ChildEntries.remove(0);
			}
			this.ChildEntries = null;
		}

		// 所属的步骤对象
		mo_Activity = null;

		// 所属的流转实例
		this.WorkItem = null;

		super.ClearUp();
	}

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return
	 * @throws Exception 
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			return "";
		} catch (Exception ex) {
			this.Raise(ex, "IsValid", null);
			return ex.toString();
		}
	}

	/**
	 * 是否是第一次打开
	 * 
	 * @return
	 */
	public boolean isFirstTrans() {
		if (this.EntryType != EEntryType.ActualityEntry)
			return false;
		if (this.ParentEntry == null)
			return false;

		for (CEntry lo_Entry : this.ParentEntry.ChildEntries) {
			if (lo_Entry.EntryStatus != EEntryStatus.NotTransactStatus) {
				if (lo_Entry != this)
					return false;
			}
		}
		return true;
	}

	/**
	 * 打开意见内容
	 * 
	 * @param ao_Set
	 * @throws Exception
	 */
	public void openComment(Map<String,Object> ao_Set) throws Exception {
		try {
			byte[] ly_Bytes = MGlobal.readBytes(ao_Set,"EntryContent");
			String ls_Read = new String(ly_Bytes);
			ls_Read = String.copyValueOf(ls_Read.toCharArray(), 0,
					ly_Bytes.length);
			MBag lo_Bag = new MBag(ls_Read);
			this.Comment = (String) lo_Bag.Read("Comment");
			this.Sign = (String) lo_Bag.Read("Sign");
			this.Voice = (String) lo_Bag.Read("Voice");
			lo_Bag.ClearUp();
			lo_Bag = null;
		} catch (Exception ex) {

		}
	}

	/**
	 * 保存意见内容
	 * 
	 * @param ao_Set
	 * @throws Exception
	 */
	public void saveComment(ResultSet ao_Set) throws SQLException {
		MBag lo_Bag = new MBag("");
		lo_Bag.Write("Comment", this.Comment);
		lo_Bag.Write("Sign", this.Sign);
		lo_Bag.Write("Voice", this.Voice);
		ao_Set.updateBytes("EntryContent", lo_Bag.Content.getBytes());
		lo_Bag.ClearUp();
		lo_Bag = null;
	}

	/**
	 * 保存意见内容
	 * 
	 * @param ao_State
	 * @param ai_Index
	 * @throws Exception
	 */
	public void saveComment(PreparedStatement ao_State, int ai_Index)
			throws SQLException {
		MBag lo_Bag = new MBag("");
		lo_Bag.Write("Comment", this.Comment);
		lo_Bag.Write("Sign", this.Sign);
		lo_Bag.Write("Voice", this.Voice);
		ao_State.setBytes(ai_Index, lo_Bag.Content.getBytes());
		lo_Bag.ClearUp();
		lo_Bag = null;
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
		this.ID = Integer.parseInt(ao_Node.getAttribute("ID"));
		this.EntryID = Integer.parseInt(ao_Node.getAttribute("EntryID"));

		this.setEntryType(Integer.parseInt(ao_Node.getAttribute("EntryType")));
		this.EntryStatus = Integer
				.parseInt(ao_Node.getAttribute("EntryStatus"));
		this.RecipientType = Integer.parseInt(ao_Node
				.getAttribute("RecipientType"));

		this.ParticipantID = Integer.parseInt(ao_Node
				.getAttribute("ParticipantID"));
		this.Participant = ao_Node.getAttribute("Participant");
		this.DeptID = Integer.parseInt(ao_Node.getAttribute("DeptID"));
		this.DeptName = ao_Node.getAttribute("DeptName");
		this.ProxyParticipantID = Integer.parseInt(ao_Node
				.getAttribute("ProxyParticipantID"));
		this.ProxyParticipant = ao_Node.getAttribute("ProxyParticipant");
		this.PostParticipantID = Integer.parseInt(ao_Node
				.getAttribute("PostParticipantID"));
		this.PostParticipant = ao_Node.getAttribute("PostParticipant");

		this.InceptDate = MGlobal.stringToDate(ao_Node
				.getAttribute("InceptDate"));
		this.ReadingDate = MGlobal.stringToDate(ao_Node
				.getAttribute("ReadingDate"));
		this.FinishedDate = MGlobal.stringToDate(ao_Node
				.getAttribute("FinishedDate"));

		this.AlterDate = MGlobal
				.stringToDate(ao_Node.getAttribute("AlterDate"));
		this.AlterInteval = Integer.parseInt(ao_Node
				.getAttribute("AlterInteval"));
		this.AlterNumber = Integer
				.parseInt(ao_Node.getAttribute("AlterNumber"));
		this.AlterUsers = ao_Node.getAttribute("AlterUsers");

		this.OverdueDate = MGlobal.stringToDate(ao_Node
				.getAttribute("OverdueDate"));
		this.RecallDate = MGlobal.stringToDate(ao_Node
				.getAttribute("RecallDate"));

		this.setParentID(Integer.parseInt(ao_Node.getAttribute("ParentID")));
		this.OrginalID = Integer.parseInt(ao_Node.getAttribute("OrginalID"));
		this.SameEntryID = Integer
				.parseInt(ao_Node.getAttribute("SameEntryID"));

		this.FlowID = Integer.parseInt(ao_Node.getAttribute("FlowID"));
		this.ActivityID = Integer.parseInt(ao_Node.getAttribute("ActivityID"));
		this.ActivityName = ao_Node.getAttribute("ActivityName");

		this.RoleID = Integer.parseInt(ao_Node.getAttribute("RoleID"));
		this.RoleName = ao_Node.getAttribute("RoleName");

		this.Choice = Integer.parseInt(ao_Node.getAttribute("Choice"));
		this.TransmitActivityID = Integer.parseInt(ao_Node
				.getAttribute("TransmitActivityID"));
		this.ResponseContent = ao_Node.getAttribute("ResponseContent");

		this.Parameter1 = ao_Node.getAttribute("Parameter1");
		this.Parameter2 = ao_Node.getAttribute("Parameter2");

		this.DueTransactType = Integer.parseInt(ao_Node
				.getAttribute("DueTransactType"));
		this.DueUserNumber = Integer.parseInt(ao_Node
				.getAttribute("DueUserNumber"));
		this.AlertMaxNumber = Integer.parseInt(ao_Node
				.getAttribute("AlertMaxNumber"));

		this.Comment = ao_Node.getAttribute("Comment");
		this.Sign = ao_Node.getAttribute("Sign");
		this.Voice = ao_Node.getAttribute("Voice");

		this.RemindAddFlag = Integer.parseInt(ao_Node
				.getAttribute("RemindAddFlag"));
		this.MobileType = Integer.parseInt(ao_Node.getAttribute("MobileType"));
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
		ao_Node.setAttribute("ID", String.valueOf(this.ID));
		ao_Node.setAttribute("EntryID", String.valueOf(this.EntryID));

		ao_Node.setAttribute("EntryType", String.valueOf(this.EntryType));
		ao_Node.setAttribute("EntryStatus", String.valueOf(this.EntryStatus));
		ao_Node.setAttribute("RecipientType",
				String.valueOf(this.RecipientType));

		ao_Node.setAttribute("ParticipantID",
				String.valueOf(this.ParticipantID));
		ao_Node.setAttribute("Participant", this.Participant);
		ao_Node.setAttribute("DeptID", String.valueOf(this.DeptID));
		ao_Node.setAttribute("DeptName", this.DeptName);
		ao_Node.setAttribute("ProxyParticipantID",
				String.valueOf(this.ProxyParticipantID));
		ao_Node.setAttribute("ProxyParticipant", this.ProxyParticipant);
		ao_Node.setAttribute("PostParticipantID",
				String.valueOf(this.PostParticipantID));
		ao_Node.setAttribute("PostParticipant", this.PostParticipant);

		ao_Node.setAttribute("InceptDate",
				MGlobal.dateToString(this.InceptDate));
		ao_Node.setAttribute("ReadingDate",
				MGlobal.dateToString(this.ReadingDate));
		ao_Node.setAttribute("FinishedDate",
				MGlobal.dateToString(this.FinishedDate));

		ao_Node.setAttribute("AlterDate", MGlobal.dateToString(this.AlterDate));
		ao_Node.setAttribute("AlterInteval", String.valueOf(this.AlterInteval));
		ao_Node.setAttribute("AlterNumber", String.valueOf(this.AlterNumber));
		ao_Node.setAttribute("AlterUsers", this.AlterUsers);

		ao_Node.setAttribute("OverdueDate",
				MGlobal.dateToString(this.OverdueDate));
		ao_Node.setAttribute("RecallDate",
				MGlobal.dateToString(this.RecallDate));

		ao_Node.setAttribute("ParentID", String.valueOf(this.ParentID));
		ao_Node.setAttribute("OrginalID", String.valueOf(this.OrginalID));
		ao_Node.setAttribute("SameEntryID", String.valueOf(this.SameEntryID));

		ao_Node.setAttribute("FlowID", String.valueOf(this.FlowID));
		ao_Node.setAttribute("ActivityID", String.valueOf(this.ActivityID));
		ao_Node.setAttribute("ActivityName", this.ActivityName);

		ao_Node.setAttribute("RoleID", String.valueOf(this.RoleID));
		ao_Node.setAttribute("RoleName", this.RoleName);

		ao_Node.setAttribute("Choice", String.valueOf(this.Choice));
		ao_Node.setAttribute("TransmitActivityID",
				String.valueOf(this.TransmitActivityID));
		ao_Node.setAttribute("ResponseContent", this.ResponseContent);

		ao_Node.setAttribute("Parameter1", this.Parameter1);
		ao_Node.setAttribute("Parameter2", this.Parameter2);

		ao_Node.setAttribute("DueTransactType",
				String.valueOf(this.DueTransactType));
		ao_Node.setAttribute("DueUserNumber",
				String.valueOf(this.DueUserNumber));
		ao_Node.setAttribute("AlertMaxNumber",
				String.valueOf(this.AlertMaxNumber));

		ao_Node.setAttribute("Comment", this.Comment);
		ao_Node.setAttribute("Sign", this.Sign);
		ao_Node.setAttribute("Voice", this.Voice);

		ao_Node.setAttribute("RemindAddFlag",
				String.valueOf(this.RemindAddFlag));
		ao_Node.setAttribute("MobileType", String.valueOf(this.MobileType));
	}

	/**
	 * 获取固定长度整型字符串，如：123,十位：0000000123
	 * 
	 * @param al_Value
	 * @param ai_Length
	 * @return
	 */
	private String getIntegerLenValue(int al_Value, int ai_Length) {
		String ls_Text = String.valueOf(al_Value);
		while (ls_Text.length() < ai_Length) {
			ls_Text = "0" + ls_Text;
		}
		return ls_Text;
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
	protected void Save(Map<String,Object> ao_Set, int ai_Type) throws Exception {
//		MGlobal.updateInt(ao_Set,"WorkItemID", this.WorkItem.WorkItemID);
//		MGlobal.updateInt(ao_Set,"EntryID", this.EntryID);
//		MGlobal.updateString(ao_Set,"WIEntryID",
//				getIntegerLenValue(this.WorkItem.WorkItemID, 10)
//						+ getIntegerLenValue(this.EntryID, 6));
//
//		MGlobal.updateInt(ao_Set,"StateID", this.EntryStatus);
//		MGlobal.updateInt(ao_Set,"EntryType", this.EntryType);
//		MGlobal.updateInt(ao_Set,"ReciType", this.RecipientType);
//
//		MGlobal.updateInt(ao_Set,"RecipientID", this.ParticipantID);
//		MGlobal.updateString(ao_Set,"Recipients", this.Participant);
//		MGlobal.updateInt(ao_Set,"DeptID", this.DeptID);
//		MGlobal.updateString(ao_Set,"DeptName", (this.DeptID == 0 ? null
//				: this.DeptName));
//		if (this.ProxyParticipantID > 0) {
//			MGlobal.updateInt(ao_Set,"ProxyID", this.ProxyParticipantID);
//			MGlobal.updateString(ao_Set,"Proxyor", this.ProxyParticipant);
//		}
//		if (this.PostParticipantID > 0) {
//			MGlobal.updateInt(ao_Set,"RUserID", this.PostParticipantID);
//			MGlobal.updateString(ao_Set,"RUserName", this.PostParticipant);
//		}
//
//		MGlobal.updateTimestamp(ao_Set,"InceptDate",
//				MGlobal.dateToSqlTime(this.InceptDate));
//		MGlobal.updateTimestamp(ao_Set,"ReadDate",
//				MGlobal.dateToSqlTime(this.ReadingDate));
//		MGlobal.updateTimestamp(ao_Set,"FinishedDate",
//				MGlobal.dateToSqlTime(this.FinishedDate));
//
//		MGlobal.updateObject(ao_Set,"AlterDate", MGlobal.dateToSqlTime(this.AlterDate));
//		MGlobal.updateInt(ao_Set,"AlterInterval", this.AlterInteval);
//		MGlobal.updateInt(ao_Set,"AlterNumber", this.AlterNumber);
//		MGlobal.updateString(ao_Set,"AlterUsers", (this.AlterUsers.equals("") ? null
//				: this.AlterUsers));
//
//		MGlobal.updateTimestamp(ao_Set,"OverDate",
//				MGlobal.dateToSqlTime(this.OverdueDate));
//		MGlobal.updateTimestamp(ao_Set,"CallDate",
//				MGlobal.dateToSqlTime(this.RecallDate));
//
//		MGlobal.updateInt(ao_Set,"ParentID", this.ParentID);
//		MGlobal.updateInt(ao_Set,"OriginalID", this.OrginalID);
//		MGlobal.updateInt(ao_Set,"SameEntryID", this.SameEntryID);
//
//		MGlobal.updateInt(ao_Set,"FlowID", this.FlowID);
//		MGlobal.updateInt(ao_Set,"ActivityID", this.ActivityID);
//		MGlobal.updateString(ao_Set, "ActivityName", this.ActivityName);
//
//		MGlobal.updateInt(ao_Set,"RoleID", this.RoleID);
//		MGlobal.updateString(ao_Set, "RoleName", this.RoleName);
//
//		MGlobal.updateInt(ao_Set,"ActivityChoice", this.Choice);
//		MGlobal.updateInt(ao_Set,"TransferActivityID", this.TransmitActivityID);
//		String ls_Text = "";
//		if (this.WorkItem.CurEntry == this
//				&& this.EntryStatus == EEntryStatus.HadTransactStatus
//				&& this.Choice > 0) {
//			ls_Text = this.getChoiceContent();
//			if (MGlobal.isExist(this.ResponseContent)) {
//				if (!ls_Text.equals(this.ResponseContent)) {
//					if (("[" + ls_Text + "]").equals(MGlobal.left(this.ResponseContent, ls_Text.length() + 2))) {
//						ls_Text = this.ResponseContent;
//					} else {
//						ls_Text = "[" + ls_Text + "]" + this.ResponseContent;
//					}
//				}
//			}
//		} else {
//			ls_Text = this.ResponseContent;
//		}
//		MGlobal.updateString(ao_Set, "ChoiceContent", ls_Text);
//
//		MGlobal.updateString(ao_Set, "ParameterOne", this.Parameter1);
//		MGlobal.updateString(ao_Set, "ParameterTwo", this.Parameter2);
//
//		MGlobal.updateInt(ao_Set,"DueTransactType", this.DueTransactType);
//		MGlobal.updateInt(ao_Set,"DueUserNumber", this.DueUserNumber);
//		MGlobal.updateInt(ao_Set,"AlertMaxNumber", this.AlertMaxNumber);
//		MGlobal.updateInt(ao_Set,"SuperviseNumber", this.SuperviseNumber);
//
//		MGlobal.updateInt(ao_Set,"RemindAddFlag", this.RemindAddFlag);
//		MGlobal.updateInt(ao_Set,"MobileType", this.MobileType);
	}

	/**
	 * 保存状态对应的权限内容
	 * 
	 * @param ao_Set
	 * @param ai_Type
	 * @param ai_UpdateType
	 * @param ao_Response
	 * @return
	 * @throws Exception
	 */
	public boolean Save(Map<String,Object> ao_Set, int ai_Type, int ai_UpdateType,
			Map<String,Object> ao_Response) throws Exception {
		Save(ao_Set, ai_Type);
		if (ai_UpdateType == 0 || this == this.WorkItem.CurEntry) {
			MBag lo_Bag = new MBag("");
			lo_Bag.Write("EntComment", this.Comment);
			lo_Bag.Write("EntSign", this.Sign);
			lo_Bag.Write("EntVoice", this.Voice);
			MGlobal.updateBytes(ao_Set,"EntryContent", lo_Bag.Content.getBytes());
			lo_Bag = null;
		}
		if (ai_Type == 1 || this.EntryType != EEntryType.ActualityEntry) {
			return true;
		} else {
			return SaveEntryRight(ao_Response);
		}
	}

	/**
	 * 保存获取保存数据库执行状态对象
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @return
	 * @throws SQLException
	 */
	public String getSaveState(int ai_SaveType, int ai_Type)
			throws SQLException {
		return getSaveState(
				(this.EntryID == this.WorkItem.CurEntry.EntryID), ai_SaveType,
				ai_Type, 0);
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ao_SqlHandle
	 *            数据库操作对象
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @return
	 * @throws SQLException
	 */
	public static String getSaveState(boolean ab_CurEntry, int ai_SaveType, int ai_Type)
			throws SQLException {
		return getSaveState(ab_CurEntry, ai_SaveType, ai_Type, 0);
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ao_Connection
	 *            数据库连接对象
	 * @param ai_DbType
	 *            数据库类型
	 * @param ab_CurEntry
	 *            是否当前状态
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @param ai_Index
	 *            实例类型：=0-正常公文；=1-历史公文；
	 * @return
	 * @throws SQLException
	 */
	public static String getSaveState(boolean ab_CurEntry, int ai_SaveType, int ai_Type,
			int ai_Index) throws SQLException {
		String ls_Sql = null;
		if (ab_CurEntry) {
			if (ai_SaveType == 0) {
				if (CSqlHandle.DbType == EDatabaseType.ORACLE) {
					ls_Sql = "INSERT INTO EntryInst"
							+ (ai_Index == 0 ? "" : String.valueOf(ai_Index))
							+ " (WEID, WorkItemID, EntryID, StateID, EntryType, ReciType, RecipientID, Recipients, "
							+ "ProxyID, Proxyor, RUserID, RUserName, DeptID, DeptName, InceptDate, ReadDate, "
							+ "FinishedDate, OverDate, AlterDate, AlterInterval, AlterNumber, AlterUsers, "
							+ "CallDate, ParentID, OriginalID, SameEntryID, FlowID, ActivityID, ActivityName, "
							+ "RoleID, RoleName, ActivityChoice, TransferActivityID, ChoiceContent, "
							+ "ParameterOne, ParameterTwo, EntryContent, DueTransactType, DueUserNumber, "
							+ "AlertMaxNumber, SuperviseNumber, RemindAddFlag, WIEntryID, MobileType) "
							+ "SELECT Entryinst_Weid.Nextval, "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? FROM Dual";
				} else {
					ls_Sql = "INSERT INTO EntryInst"
							+ (ai_Index == 0 ? "" : String.valueOf(ai_Index))
							+ " (WorkItemID, EntryID, StateID, EntryType, ReciType, RecipientID, Recipients, "
							+ "ProxyID, Proxyor, RUserID, RUserName, DeptID, DeptName, InceptDate, ReadDate, "
							+ "FinishedDate, OverDate, AlterDate, AlterInterval, AlterNumber, AlterUsers, "
							+ "CallDate, ParentID, OriginalID, SameEntryID, FlowID, ActivityID, ActivityName, "
							+ "RoleID, RoleName, ActivityChoice, TransferActivityID, ChoiceContent, "
							+ "ParameterOne, ParameterTwo, EntryContent, DueTransactType, DueUserNumber, "
							+ "AlertMaxNumber, SuperviseNumber, RemindAddFlag, WIEntryID, MobileType) "
							+ "VALUES ("
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				}
			} else {
				ls_Sql = "UPDATE EntryInst"
						+ (ai_Index == 0 ? "" : String.valueOf(ai_Index))
						+ " SET WorkItemID = ?, EntryID = ?, StateID = ?, EntryType = ?, ReciType = ?, RecipientID = ?, "
						+ "Recipients = ?, ProxyID = ?, Proxyor = ?, RUserID = ?, RUserName = ?, DeptID = ?, DeptName = ?, "
						+ "InceptDate = ?, ReadDate = ?, FinishedDate = ?, OverDate = ?, AlterDate = ?, AlterInterval = ?, "
						+ "AlterNumber = ?, AlterUsers = ?, CallDate = ?, ParentID = ?, OriginalID = ?, SameEntryID = ?, "
						+ "FlowID = ?, ActivityID = ?, ActivityName = ?, RoleID = ?, RoleName = ?, ActivityChoice = ?, "
						+ "TransferActivityID = ?, ChoiceContent = ?, ParameterOne = ?, ParameterTwo = ?, EntryContent = ?, "
						+ "DueTransactType = ?, DueUserNumber = ?, AlertMaxNumber = ?, SuperviseNumber = ?, RemindAddFlag = ?, "
						+ "WIEntryID = ?, MobileType = ? WHERE WEID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				if (CSqlHandle.DbType == EDatabaseType.ORACLE) {
					ls_Sql = "INSERT INTO EntryInst"
							+ (ai_Index == 0 ? "" : String.valueOf(ai_Index))
							+ " (WEID, WorkItemID, EntryID, StateID, EntryType, ReciType, RecipientID, Recipients, "
							+ "ProxyID, Proxyor, RUserID, RUserName, DeptID, DeptName, InceptDate, ReadDate, "
							+ "FinishedDate, OverDate, AlterDate, AlterInterval, AlterNumber, AlterUsers, "
							+ "CallDate, ParentID, OriginalID, SameEntryID, FlowID, ActivityID, ActivityName, "
							+ "RoleID, RoleName, ActivityChoice, TransferActivityID, ChoiceContent, "
							+ "ParameterOne, ParameterTwo, DueTransactType, DueUserNumber, "
							+ "AlertMaxNumber, SuperviseNumber, RemindAddFlag, WIEntryID, MobileType) "
							+ "SELECT Entryinst_Weid.Nextval, "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? FROM Dual";
				} else {
					ls_Sql = "INSERT INTO EntryInst"
							+ (ai_Index == 0 ? "" : String.valueOf(ai_Index))
							+ " (WorkItemID, EntryID, StateID, EntryType, ReciType, RecipientID, Recipients, "
							+ "ProxyID, Proxyor, RUserID, RUserName, DeptID, DeptName, InceptDate, ReadDate, "
							+ "FinishedDate, OverDate, AlterDate, AlterInterval, AlterNumber, AlterUsers, "
							+ "CallDate, ParentID, OriginalID, SameEntryID, FlowID, ActivityID, ActivityName, "
							+ "RoleID, RoleName, ActivityChoice, TransferActivityID, ChoiceContent, "
							+ "ParameterOne, ParameterTwo, DueTransactType, DueUserNumber, "
							+ "AlertMaxNumber, SuperviseNumber, RemindAddFlag, WIEntryID, MobileType) "
							+ "VALUES ("
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				}
			} else {
				ls_Sql = "UPDATE EntryInst"
						+ (ai_Index == 0 ? "" : String.valueOf(ai_Index))
						+ " SET WorkItemID = ?, EntryID = ?, StateID = ?, EntryType = ?, ReciType = ?, RecipientID = ?, "
						+ "Recipients = ?, ProxyID = ?, Proxyor = ?, RUserID = ?, RUserName = ?, DeptID = ?, DeptName = ?, "
						+ "InceptDate = ?, ReadDate = ?, FinishedDate = ?, OverDate = ?, AlterDate = ?, AlterInterval = ?, "
						+ "AlterNumber = ?, AlterUsers = ?, CallDate = ?, ParentID = ?, OriginalID = ?, SameEntryID = ?, "
						+ "FlowID = ?, ActivityID = ?, ActivityName = ?, RoleID = ?, RoleName = ?, ActivityChoice = ?, "
						+ "TransferActivityID = ?, ChoiceContent = ?, ParameterOne = ?, ParameterTwo = ?, "
						+ "DueTransactType = ?, DueUserNumber = ?, AlertMaxNumber = ?, SuperviseNumber = ?, RemindAddFlag = ?, "
						+ "WIEntryID = ?, MobileType = ? WHERE WEID = ?";
			}
		}
		return ls_Sql;
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ao_Connection
	 *            数据库连接对象
	 * @param ai_DbType
	 *            数据库类型
	 * @return
	 * @throws SQLException
	 */
	public static String getNewState() throws SQLException {
		String ls_Sql = null;
		if (CSqlHandle.DbType == EDatabaseType.ORACLE) {
			ls_Sql = "INSERT INTO EntryInst (WEID, WorkItemID, EntryID, StateID, EntryType, ReciType, RecipientID, Recipients, "
					+ "ProxyID, Proxyor, RUserID, RUserName, DeptID, DeptName, InceptDate, ReadDate, "
					+ "FinishedDate, OverDate, AlterDate, AlterInterval, AlterNumber, AlterUsers, "
					+ "CallDate, ParentID, OriginalID, SameEntryID, FlowID, ActivityID, ActivityName, "
					+ "RoleID, RoleName, ActivityChoice, TransferActivityID, ChoiceContent, "
					+ "ParameterOne, ParameterTwo, EntryContent, DueTransactType, DueUserNumber, "
					+ "AlertMaxNumber, SuperviseNumber, RemindAddFlag, WIEntryID, MobileType) SELECT Entryinst_Weid.Nextval, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? FROM Dual";
		} else {
			ls_Sql = "INSERT INTO EntryInst (WorkItemID, EntryID, StateID, EntryType, ReciType, RecipientID, Recipients, "
					+ "ProxyID, Proxyor, RUserID, RUserName, DeptID, DeptName, InceptDate, ReadDate, "
					+ "FinishedDate, OverDate, AlterDate, AlterInterval, AlterNumber, AlterUsers, "
					+ "CallDate, ParentID, OriginalID, SameEntryID, FlowID, ActivityID, ActivityName, "
					+ "RoleID, RoleName, ActivityChoice, TransferActivityID, ChoiceContent, "
					+ "ParameterOne, ParameterTwo, EntryContent, DueTransactType, DueUserNumber, "
					+ "AlertMaxNumber, SuperviseNumber, RemindAddFlag, WIEntryID, MobileType) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		}

		return ls_Sql;
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ao_Connection
	 *            数据库连接对象
	 * @param ai_DbType
	 *            数据库类型
	 * @param ab_CurEntry
	 *            是否当前状态
	 * @return
	 * @throws SQLException
	 */
	public static String getUpdateState(boolean ab_CurEntry) throws SQLException {
		String ls_Sql = null;
		ls_Sql = "UPDATE EntryInst SET StateID = ?, RecipientID = ?, Recipients = ?, ProxyID = ?, Proxyor = ?, "
				+ "RUserID = ?, RUserName = ?, DeptID = ?, DeptName = ?, RoleID = ?, RoleName = ?, "
				+ "InceptDate = ?, ReadDate = ?, FinishedDate = ?, OverDate = ?, AlterDate = ?, CallDate = ?, "
				+ "AlterInterval = ?, AlterNumber = ?, AlterUsers = ?, ActivityChoice = ?, TransferActivityID = ?, "
				+ "ChoiceContent = ?, ParameterOne = ?, ParameterTwo = ?, DueTransactType = ?, DueUserNumber = ?, "
				+ "AlertMaxNumber = ?, SuperviseNumber = ?, RemindAddFlag = ?, MobileType = ?";
		if (ab_CurEntry)
			ls_Sql += ", EntryContent = ?";
		ls_Sql += " WHERE WEID = ?";
		return ls_Sql;
	}

	/**
	 * 保存
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws SQLException
	 */
	public void NewSave(String str_State, final int ai_Update)
			throws SQLException {
		final CEntry entry = this;
		CSqlHandle.getJdbcTemplate().update(str_State,
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ao_State)
							throws SQLException {
						int li_Index = 1;
						ao_State.setInt(li_Index++, entry.WorkItem.WorkItemID);
						ao_State.setInt(li_Index++, entry.EntryID);
						ao_State.setInt(li_Index++, entry.EntryStatus);
						ao_State.setInt(li_Index++, entry.EntryType);
						// li_Index = 5;
						ao_State.setInt(li_Index++, entry.RecipientType);
						ao_State.setInt(li_Index++, entry.ParticipantID);
						ao_State.setString(li_Index++, entry.Participant);
						// li_Index = 8;
						ao_State.setInt(li_Index++, entry.ProxyParticipantID);
						MGlobal.writeString(ao_State, li_Index++,
								entry.ProxyParticipant);
						ao_State.setInt(li_Index++, entry.PostParticipantID);
						MGlobal.writeString(ao_State, li_Index++,
								entry.PostParticipant);
						ao_State.setInt(li_Index++, entry.DeptID);
						MGlobal.writeString(ao_State, li_Index++,
								entry.DeptName);
						// li_Index = 14;
						MGlobal.writeTime(ao_State, li_Index++,
								entry.InceptDate);
						MGlobal.writeTime(ao_State, li_Index++,
								entry.ReadingDate);
						MGlobal.writeTime(ao_State, li_Index++,
								entry.FinishedDate);
						MGlobal.writeTime(ao_State, li_Index++,
								entry.OverdueDate);
						MGlobal.writeTime(ao_State, li_Index++, entry.AlterDate);
						ao_State.setInt(li_Index++, entry.AlterInteval);
						ao_State.setInt(li_Index++, entry.AlterNumber);
						MGlobal.writeString(ao_State, li_Index++,
								entry.AlterUsers);
						MGlobal.writeTime(ao_State, li_Index++,
								entry.RecallDate);
						// li_Index = 23;
						ao_State.setInt(li_Index++, entry.ParentID);
						ao_State.setInt(li_Index++, entry.OrginalID);
						ao_State.setInt(li_Index++, entry.SameEntryID);
						// li_Index = 26;
						ao_State.setInt(li_Index++, entry.FlowID);
						ao_State.setInt(li_Index++, entry.ActivityID);
						MGlobal.writeString(ao_State, li_Index++,
								entry.ActivityName);
						// li_Index = 29;
						ao_State.setInt(li_Index++, entry.RoleID);
						MGlobal.writeString(ao_State, li_Index++,
								entry.RoleName);
						// li_Index = 31;
						ao_State.setInt(li_Index++, entry.Choice);
						ao_State.setInt(li_Index++, entry.TransmitActivityID);
						String ls_Text = "";
						if (entry.WorkItem.CurEntry == entry
								&& entry.EntryStatus == EEntryStatus.HadTransactStatus
								&& entry.Choice > 0) {
							ls_Text = entry.getChoiceContent();
							if (MGlobal.isExist(entry.ResponseContent)) {
								if (!ls_Text.equals(entry.ResponseContent)) {
									if (("[" + ls_Text + "]").equals(MGlobal
											.left(entry.ResponseContent,
													ls_Text.length() + 2))) {
										ls_Text = entry.ResponseContent;
									} else {
										ls_Text = "[" + ls_Text + "]"
												+ entry.ResponseContent;
									}
								}
							}
						} else {
							ls_Text = entry.ResponseContent;
						}
						// li_Index = 33;
						MGlobal.writeString(ao_State, li_Index++, ls_Text);
						// li_Index = 34;
						MGlobal.writeString(ao_State, li_Index++,
								entry.Parameter1);
						MGlobal.writeString(ao_State, li_Index++,
								entry.Parameter2);
						// li_Index = 36;
						if (entry.EntryID == entry.WorkItem.CurEntry.EntryID)
							entry.saveComment(ao_State, li_Index++);
						else
							ao_State.setBytes(li_Index++, null);
						// li_Index = 37;
						ao_State.setInt(li_Index++, entry.DueTransactType);
						ao_State.setInt(li_Index++, entry.DueUserNumber);
						ao_State.setInt(li_Index++, entry.AlertMaxNumber);
						ao_State.setInt(li_Index++, entry.SuperviseNumber);
						ao_State.setInt(li_Index++, entry.RemindAddFlag);
						ao_State.setString(
								li_Index++,
								getIntegerLenValue(entry.WorkItem.WorkItemID,
										10)
										+ getIntegerLenValue(entry.EntryID, 6));
						ao_State.setInt(li_Index++, entry.MobileType);

					}
				});
	}

	/**
	 * 保存
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws SQLException
	 */
	public void UpdateSave(String updateSave, int ai_Update)
			throws SQLException {
		final CEntry cEntry = this;
		CSqlHandle.getJdbcTemplate().update(  
				updateSave,   
                new PreparedStatementSetter(){  
                    @Override  
                    public void setValues(PreparedStatement ao_State) throws SQLException {  
                    	int li_Index = 1;
                		ao_State.setInt(li_Index++, cEntry.EntryStatus);
                		ao_State.setInt(li_Index++, cEntry.ParticipantID);
                		ao_State.setString(li_Index++, cEntry.Participant);
                		ao_State.setInt(li_Index++, cEntry.ProxyParticipantID);
                		MGlobal.writeString(ao_State, li_Index++, cEntry.ProxyParticipant);
                		// li_Index = 6
                		ao_State.setInt(li_Index++, cEntry.PostParticipantID);
                		MGlobal.writeString(ao_State, li_Index++, cEntry.PostParticipant);
                		ao_State.setInt(li_Index++, cEntry.DeptID);
                		MGlobal.writeString(ao_State, li_Index++, cEntry.DeptName);
                		ao_State.setInt(li_Index++, cEntry.RoleID);
                		MGlobal.writeString(ao_State, li_Index++, cEntry.RoleName);
                		// li_Index = 12;
                		MGlobal.writeTime(ao_State, li_Index++, cEntry.InceptDate);
                		MGlobal.writeTime(ao_State, li_Index++, cEntry.ReadingDate);
                		MGlobal.writeTime(ao_State, li_Index++, cEntry.FinishedDate);
                		MGlobal.writeTime(ao_State, li_Index++, cEntry.OverdueDate);
                		MGlobal.writeTime(ao_State, li_Index++, cEntry.AlterDate);
                		MGlobal.writeTime(ao_State, li_Index++, cEntry.RecallDate);
                		// li_Index = 18;
                		ao_State.setInt(li_Index++, cEntry.AlterInteval);
                		ao_State.setInt(li_Index++, cEntry.AlterNumber);
                		MGlobal.writeString(ao_State, li_Index++, cEntry.AlterUsers);
                		ao_State.setInt(li_Index++, cEntry.Choice);
                		ao_State.setInt(li_Index++, cEntry.TransmitActivityID);
                		// li_Index = 23;
                		String ls_Text = "";
                		if (cEntry.WorkItem.CurEntry == cEntry
                				&& cEntry.EntryStatus == EEntryStatus.HadTransactStatus
                				&& cEntry.Choice > 0) {
                			ls_Text = cEntry.getChoiceContent();
                			if (MGlobal.isExist(cEntry.ResponseContent)) {
                				if (!ls_Text.equals(cEntry.ResponseContent)) {
                					if (("[" + ls_Text + "]").equals(MGlobal.left(
                							cEntry.ResponseContent, ls_Text.length() + 2))) {
                						ls_Text = cEntry.ResponseContent;
                					} else {
                						ls_Text = "[" + ls_Text + "]" + cEntry.ResponseContent;
                					}
                				}
                			}
                		} else {
                			ls_Text = cEntry.ResponseContent;
                		}
                		MGlobal.writeString(ao_State, li_Index++, ls_Text);
                		// li_Index = 24;
                		MGlobal.writeString(ao_State, li_Index++, cEntry.Parameter1);
                		MGlobal.writeString(ao_State, li_Index++, cEntry.Parameter2);
                		ao_State.setInt(li_Index++, cEntry.DueTransactType);
                		ao_State.setInt(li_Index++, cEntry.DueUserNumber);
                		// li_Index = 28;
                		ao_State.setInt(li_Index++, cEntry.AlertMaxNumber);
                		ao_State.setInt(li_Index++, cEntry.SuperviseNumber);
                		ao_State.setInt(li_Index++, cEntry.RemindAddFlag);
                		ao_State.setInt(li_Index++, cEntry.MobileType);
                		// li_Index = 32;
                		if (cEntry.EntryID == cEntry.WorkItem.CurEntry.EntryID)
                			cEntry.saveComment(ao_State, li_Index++);
                		// li_Index = 33;
                		ao_State.setInt(li_Index++, cEntry.ID);
                    }  
                }  
        );  
		

		
	}

	/**
	 * 保存状态对应的权限内容
	 * 
	 * @param ao_Response
	 * @return
	 * @throws SQLException
	 */
	private Boolean SaveEntryRight(Map<String,Object> ao_Response) throws SQLException {
//		CActivity lo_Activity = this.getActivity();
//		if (lo_Activity == null)
//			return false;
//
//		if (!(lo_Activity.ActivityType == EActivityType.StartActivity
//				|| lo_Activity.ActivityType == EActivityType.TransactActivity || lo_Activity.ActivityType == EActivityType.FYIActivity))
//			return true;
//
//		String ls_RightID = "";
//		if (lo_Activity.ActivityType == EActivityType.FYIActivity) {
//			ls_RightID = lo_Activity.FYI.getUserRight();
//		} else {
//			ls_RightID = lo_Activity.Transact.getUserRight();
//		}
//
//		// 权限使用：在没有权限时采用缺省权限，否则采用第一个权限
//		if (ls_RightID.equals("")) {
//			ls_RightID = "1";
//		} else {
//			ls_RightID = MGlobal.left(ls_RightID, ls_RightID.indexOf(";"));
//		}
//
//		CRight lo_Right = this.WorkItem.Cyclostyle.getRightById(Integer
//				.parseInt(ls_RightID));
//		if (lo_Right == null)
//			return true;
//		if (lo_Right.ResponseRight == EResponseRight.PublicResponseRight)
//			return true;
//
//		ao_Response.insertRow();
//		ao_Response.updateInt("WorkItemID", this.WorkItem.WorkItemID);
//		ao_Response.updateInt("EntryID", this.EntryID);
//		ao_Response.updateInt("ResponseType", lo_Right.ResponseType);
//		ao_Response.updateInt("UserRight", lo_Right.ResponseRight);
//		ao_Response.updateString("ActivityIDs", lo_Right.ResActivityIDs);
//		ao_Response.updateString("RoleIDs", lo_Right.ResRoleIDs);
//		ao_Response.updateString("ExterUser", lo_Right.ResExterUser);
//		ao_Response.updateString("CacuScript", lo_Right.ResCacuScript);
//
		try
		{
			throw new Exception("33333");
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @return
	 * @throws SQLException
	 */
	public String getSaveEntryRightState(int ai_SaveType, int ai_Type)
			throws SQLException {
		return getSaveEntryRightState(this, ai_SaveType, ai_Type);
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ao_Entry
	 *            状态对象
	 * @param ao_Connection
	 *            数据库连接对象
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @return
	 * @throws SQLException
	 */
	public static String getSaveEntryRightState(CEntry ao_Entry, int ai_SaveType, int ai_Type)
			throws SQLException {
		CActivity lo_Activity = ao_Entry.getActivity();
		if (lo_Activity == null)
			return null;

		if (!(lo_Activity.ActivityType == EActivityType.StartActivity
				|| lo_Activity.ActivityType == EActivityType.TransactActivity || lo_Activity.ActivityType == EActivityType.FYIActivity))
			return null;

		String ls_RightID = "";
		if (lo_Activity.ActivityType == EActivityType.FYIActivity) {
			ls_RightID = lo_Activity.FYI.getUserRight();
		} else {
			ls_RightID = lo_Activity.Transact.getUserRight();
		}

		// 权限使用：在没有权限时采用缺省权限，否则采用第一个权限
		if (ls_RightID.equals("")) {
			ls_RightID = "1";
		} else {
			ls_RightID = MGlobal.left(ls_RightID, ls_RightID.indexOf(";"));
		}

		CRight lo_Right = ao_Entry.WorkItem.Cyclostyle.getRightById(Integer
				.parseInt(ls_RightID));
		if (lo_Right == null)
			return null;
		if (lo_Right.ResponseRight == EResponseRight.PublicResponseRight)
			return null;

		String ls_Sql = null;
		if (ai_SaveType == 0) {
			ls_Sql = "INSERT INTO ResponseLookupRight "
					+ "(WorkItemID, EntryID, ResponseType, UserRight, ActivityIDs, RoleIDs, ExterUser, CacuScript) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			ls_Sql = "UPDATE ResponseLookupRight SET "
					+ "ResponseType = ?, UserRight = ?, ActivityIDs = ?, RoleIDs = ?, ExterUser = ?, CacuScript = ? "
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
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws SQLException
	 */
	public void SaveEntryRight(String saveEntryRight, final int ai_SaveType,
			int ai_Type, final int ai_Update) throws SQLException {

		final CEntry cEntry = this;
		CSqlHandle.getJdbcTemplate().update(  
				saveEntryRight,   
                new PreparedStatementSetter(){  
                    @Override  
                    public void setValues(PreparedStatement ao_State) throws SQLException {  

                		int li_Index = 1;
                		CActivity lo_Activity = cEntry.getActivity();
                		String ls_RightID = "";
                		if (lo_Activity.ActivityType == EActivityType.FYIActivity) {
                			ls_RightID = lo_Activity.FYI.getUserRight();
                		} else {
                			ls_RightID = lo_Activity.Transact.getUserRight();
                		}

                		// 权限使用：在没有权限时采用缺省权限，否则采用第一个权限
                		if (ls_RightID.equals("")) {
                			ls_RightID = "1";
                		} else {
                			ls_RightID = MGlobal.left(ls_RightID, ls_RightID.indexOf(";"));
                		}

                		CRight lo_Right = cEntry.WorkItem.Cyclostyle.getRightById(Integer
                				.parseInt(ls_RightID));

                		if (ai_SaveType == 0) {
                			ao_State.setInt(li_Index++, cEntry.WorkItem.WorkItemID);
                			ao_State.setInt(li_Index++, cEntry.EntryID);
                		}

                		ao_State.setInt(li_Index++, lo_Right.ResponseType);
                		ao_State.setInt(li_Index++, lo_Right.ResponseRight);
                		ao_State.setString(li_Index++, lo_Right.ResActivityIDs);
                		ao_State.setString(li_Index++, lo_Right.ResRoleIDs);
                		ao_State.setString(li_Index++, lo_Right.ResExterUser);
                		ao_State.setString(li_Index++, lo_Right.ResCacuScript);

                		if (ai_SaveType == 1) {
                			ao_State.setInt(li_Index++, cEntry.WorkItem.WorkItemID);
                			ao_State.setInt(li_Index++, cEntry.EntryID);
                		}

 
                    }  
                }  
        );  
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
	public void Open(Map<String,Object> ao_Set, int ai_Type) throws Exception {
		this.ID = MGlobal.readInt(ao_Set,"WEID");
		this.EntryID = MGlobal.readInt(ao_Set,"EntryID");
		this.setEntryType(MGlobal.readInt(ao_Set,"EntryType"));
		this.EntryStatus = MGlobal.readInt(ao_Set,"StateID");
		this.RecipientType = MGlobal.readInt(ao_Set, "ReciType");
		this.ParticipantID = MGlobal.readInt(ao_Set,"RecipientID");
		this.Participant = MGlobal.readString(ao_Set,"Recipients");
		this.DeptID = MGlobal.readInt(ao_Set, "DeptID");
		this.DeptName = MGlobal.readString(ao_Set, "DeptName");
		this.ProxyParticipantID = MGlobal.readInt(ao_Set, "ProxyID");
		this.ProxyParticipant = MGlobal.readString(ao_Set, "Proxyor");
		this.PostParticipantID = MGlobal.readInt(ao_Set, "RUserID");
		this.PostParticipant = MGlobal.readString(ao_Set, "RUserName");
		this.InceptDate = MGlobal.readTime(ao_Set, "InceptDate");
		this.ReadingDate = MGlobal.readTime(ao_Set, "ReadDate");
		this.FinishedDate = MGlobal.readTime(ao_Set, "FinishedDate");
		this.AlterDate = MGlobal.readTime(ao_Set, "AlterDate");
		this.AlterInteval = MGlobal.readInt(ao_Set,"AlterInterval");
		this.AlterNumber = MGlobal.readInt(ao_Set,"AlterNumber");
		this.AlterUsers = MGlobal.readString(ao_Set, "AlterUsers");
		this.OverdueDate = MGlobal.readTime(ao_Set, "OverDate");
		this.RecallDate = MGlobal.readTime(ao_Set, "CallDate");
		this.setParentID(MGlobal.readInt(ao_Set, "ParentID"));
		this.OrginalID = MGlobal.readInt(ao_Set,"OriginalID");
		this.SameEntryID = MGlobal.readInt(ao_Set, "SameEntryID");
		this.FlowID = MGlobal.readInt(ao_Set,"FlowID");
		this.ActivityID = MGlobal.readInt(ao_Set, "ActivityID");
		this.ActivityName = MGlobal.readString(ao_Set, "ActivityName");
		this.RoleID = MGlobal.readInt(ao_Set, "RoleID");
		this.RoleName = MGlobal.readString(ao_Set, "RoleName");
		this.Choice = MGlobal.readInt(ao_Set,"ActivityChoice");
		this.TransmitActivityID = MGlobal.readInt(ao_Set,"TransferActivityID");
		this.ResponseContent = MGlobal.readString(ao_Set, "ChoiceContent");
		this.Parameter1 = MGlobal.readString(ao_Set, "ParameterOne");
		this.Parameter2 = MGlobal.readString(ao_Set, "ParameterTwo");
		this.DueTransactType = MGlobal.readInt(ao_Set, "DueTransactType");
		this.DueUserNumber = MGlobal.readInt(ao_Set, "DueUserNumber");
		this.AlertMaxNumber = MGlobal.readInt(ao_Set, "AlertMaxNumber");
		this.SuperviseNumber = MGlobal.readInt(ao_Set, "SuperviseNumber");
		this.RemindAddFlag = MGlobal.readInt(ao_Set, "RemindAddFlag");
		this.MobileType = MGlobal.readInt(ao_Set, "MobileType");

		if (MGlobal.readObject(ao_Set,"EntryContent") != null) {
			openComment(ao_Set);
		} else {
			this.Comment = MGlobal.readString(ao_Set, "ChoiceContent");
		}
		this.EntryChanged = false;
	}

	/**
	 * 从数据库行对象中读取数据到对象(管理方式)
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @throws Exception
	 */
	public void Open(Map<String,Object> ao_Set) throws Exception {
		this.ID = MGlobal.readInt(ao_Set,"WEID");
		this.EntryID = MGlobal.readInt(ao_Set,"EntryID");
		this.EntryType = MGlobal.readInt(ao_Set,"EntryType");
		this.EntryStatus = MGlobal.readInt(ao_Set,"StateID");
		this.RecipientType = MGlobal.readInt(ao_Set, "ReciType");
		this.ParticipantID = MGlobal.readInt(ao_Set,"RecipientID");
		this.Participant = MGlobal.readString(ao_Set,"Recipients");
		this.DeptID = MGlobal.readInt(ao_Set, "DeptID");
		this.DeptName = MGlobal.readString(ao_Set, "DeptName");
		this.ProxyParticipantID = MGlobal.readInt(ao_Set, "ProxyID");
		this.ProxyParticipant = MGlobal.readString(ao_Set, "Proxyor");
		this.PostParticipantID = MGlobal.readInt(ao_Set, "RUserID");
		this.PostParticipant = MGlobal.readString(ao_Set, "RUserName");
		this.InceptDate = MGlobal.readTime(ao_Set, "InceptDate");
		this.ReadingDate = MGlobal.readTime(ao_Set, "ReadDate");
		this.FinishedDate = MGlobal.readTime(ao_Set, "FinishedDate");
		this.AlterDate = MGlobal.readTime(ao_Set, "AlterDate");
		this.OverdueDate = MGlobal.readTime(ao_Set, "OverDate");
		this.RecallDate = MGlobal.readTime(ao_Set, "CallDate");
		this.ParentID = MGlobal.readInt(ao_Set, "ParentID");
		this.OrginalID = MGlobal.readInt(ao_Set,"OriginalID");
		this.ActivityID = MGlobal.readInt(ao_Set, "ActivityID");
		this.ActivityName = MGlobal.readString(ao_Set, "ActivityName");
		this.ResponseContent = MGlobal.readString(ao_Set, "ChoiceContent");
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
		ao_Bag.Write("ml_ID", this.ID);
		ao_Bag.Write("ml_EntryID", this.EntryID);

		ao_Bag.Write("mi_EntryType", this.EntryType);
		ao_Bag.Write("mi_EntryStatus", this.EntryStatus);
		ao_Bag.Write("mi_RecipientType", this.RecipientType);

		ao_Bag.Write("ml_ParticipantID", this.ParticipantID);
		ao_Bag.Write("ms_Participant", this.Participant);
		ao_Bag.Write("ml_DeptID", this.DeptID);
		ao_Bag.Write("ms_DeptName", this.DeptName);
		ao_Bag.Write("ml_ProxyParticipantID", this.ProxyParticipantID);
		ao_Bag.Write("ms_ProxyParticipant", this.ProxyParticipant);
		ao_Bag.Write("ml_PostParticipantID", this.PostParticipantID);
		ao_Bag.Write("ms_PostParticipant", this.PostParticipant);

		ao_Bag.Write("md_InceptDate", this.InceptDate);
		ao_Bag.Write("md_ReadingDate", this.ReadingDate);
		ao_Bag.Write("md_FinishedDate", this.FinishedDate);

		ao_Bag.Write("md_AlterDate", this.AlterDate);
		ao_Bag.Write("mi_AlterInteval", this.AlterInteval);
		ao_Bag.Write("mi_AlterNumber", this.AlterNumber);
		ao_Bag.Write("ms_AlterUsers", this.AlterUsers);

		ao_Bag.Write("md_OverdueDate", this.OverdueDate);
		ao_Bag.Write("md_RecallDate", this.RecallDate);

		ao_Bag.Write("ml_ParentID", this.ParentID);
		ao_Bag.Write("ml_OrginalID", this.OrginalID);
		ao_Bag.Write("ml_SameEntryID", this.SameEntryID);

		ao_Bag.Write("ml_FlowID", this.FlowID);
		ao_Bag.Write("ml_ActivityID", this.ActivityID);
		ao_Bag.Write("ms_ActivityName", this.ActivityName);

		ao_Bag.Write("ml_RoleID", this.RoleID);
		ao_Bag.Write("ms_RoleName", this.RoleName);

		ao_Bag.Write("mi_Choice", this.Choice);
		ao_Bag.Write("ml_TransmitActivityID", this.TransmitActivityID);
		ao_Bag.Write("ms_ResponseContent", this.ResponseContent);

		ao_Bag.Write("ms_Parameter1", this.Parameter1);
		ao_Bag.Write("ms_Parameter2", this.Parameter2);

		ao_Bag.Write("mi_DueTransactType", this.DueTransactType);
		ao_Bag.Write("mi_DueUserNumber", this.DueUserNumber);
		ao_Bag.Write("mi_AlertMaxNumber", this.AlertMaxNumber);

		ao_Bag.Write("ms_Comment", this.Comment);
		ao_Bag.Write("ms_Sign", this.Sign);
		ao_Bag.Write("ms_Voice", this.Voice);

		ao_Bag.Write("mi_RemindAddFlag", this.RemindAddFlag);
		ao_Bag.Write("mi_MobileType", this.MobileType);
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
		this.ID = ao_Bag.ReadInt("ml_ID");
		this.EntryID = ao_Bag.ReadInt("ml_EntryID");

		this.EntryType = ao_Bag.ReadInt("mi_EntryType");
		this.setEntryType(ao_Bag.ReadInt("mi_EntryType"));
		this.EntryStatus = ao_Bag.ReadInt("mi_EntryStatus");
		this.RecipientType = ao_Bag.ReadInt("mi_RecipientType");

		this.ParticipantID = ao_Bag.ReadInt("ml_ParticipantID");
		this.Participant = ao_Bag.ReadString("ms_Participant");
		this.DeptID = ao_Bag.ReadInt("ml_DeptID");
		this.DeptName = ao_Bag.ReadString("ms_DeptName");
		this.ProxyParticipantID = ao_Bag.ReadInt("ml_ProxyParticipantID");
		this.ProxyParticipant = ao_Bag.ReadString("ms_ProxyParticipant");
		this.PostParticipantID = ao_Bag.ReadInt("ml_PostParticipantID");
		this.PostParticipant = ao_Bag.ReadString("ms_PostParticipant");

		this.InceptDate = ao_Bag.ReadDate("md_InceptDate");
		this.ReadingDate = ao_Bag.ReadDate("md_ReadingDate");
		this.FinishedDate = ao_Bag.ReadDate("md_FinishedDate");

		this.AlterDate = ao_Bag.ReadDate("md_AlterDate");
		this.AlterInteval = ao_Bag.ReadInt("mi_AlterInteval");
		this.AlterNumber = ao_Bag.ReadInt("mi_AlterNumber");
		this.AlterUsers = ao_Bag.ReadString("ms_AlterUsers");

		this.OverdueDate = ao_Bag.ReadDate("md_OverdueDate");
		this.RecallDate = ao_Bag.ReadDate("md_RecallDate");

		// this.ParentID = ao_Bag.ReadInt("ml_ParentID");
		this.setParentID(ao_Bag.ReadInt("ml_ParentID"));
		this.OrginalID = ao_Bag.ReadInt("ml_OrginalID");
		this.SameEntryID = ao_Bag.ReadInt("ml_SameEntryID");

		this.FlowID = ao_Bag.ReadInt("ml_FlowID");
		this.ActivityID = ao_Bag.ReadInt("ml_ActivityID");
		this.ActivityName = ao_Bag.ReadString("ms_ActivityName");

		this.RoleID = ao_Bag.ReadInt("ml_RoleID");
		this.RoleName = ao_Bag.ReadString("ms_RoleName");

		this.Choice = ao_Bag.ReadInt("mi_Choice");
		this.TransmitActivityID = ao_Bag.ReadInt("ml_TransmitActivityID");
		this.ResponseContent = ao_Bag.ReadString("ms_ResponseContent");

		this.Parameter1 = ao_Bag.ReadString("ms_Parameter1");
		this.Parameter2 = ao_Bag.ReadString("ms_Parameter2");

		this.DueTransactType = ao_Bag.ReadInt("mi_DueTransactType");
		this.DueUserNumber = ao_Bag.ReadInt("mi_DueUserNumber");
		this.AlertMaxNumber = ao_Bag.ReadInt("mi_AlertMaxNumber");

		this.Comment = ao_Bag.ReadString("ms_Comment");
		this.Sign = ao_Bag.ReadString("ms_Sign");
		this.Voice = ao_Bag.ReadString("ms_Voice");

		this.RemindAddFlag = ao_Bag.ReadInt("mi_RemindAddFlag");
		this.MobileType = ao_Bag.ReadInt("mi_MobileType");
	}
	
	/**
	 * 获取当前状态的后继状态收件人，多个使用【,】分隔，最后一个不需要【,】
	 * @return
	 */
	public String getSendRecipients() {
		String ls_Text = "";
		for (CEntry lo_Entry : this.WorkItem.Entries.values()) {
			if (lo_Entry.OrginalID == this.EntryID) {
				if ((";" + ls_Text).indexOf("," + lo_Entry.Participant + ",") == -1)
					ls_Text += lo_Entry.Participant + ",";
			}
		}
		if (MGlobal.isExist(ls_Text)) ls_Text = ls_Text.substring(0, ls_Text.length()-1);
		return ls_Text;
	}
	
}
