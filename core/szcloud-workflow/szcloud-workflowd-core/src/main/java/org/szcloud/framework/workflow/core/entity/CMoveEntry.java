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
 * 移动应用流转状态
 * 
 * @author Jackie.Wang
 * 
 */
public class CMoveEntry extends CBase {
	/**
	 * 初始化
	 */
	public CMoveEntry() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CMoveEntry(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属状态
		this.Entry = null;

		super.ClearUp();
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#

	/**
	 * 所属状态
	 */
	public CEntry Entry = null;

	/**
	 * 类型:=1-短信;=2-PDA;
	 */
	public int Flag = 0;

	/**
	 * 短信内容
	 */
	public String SmsMessage = "";

	/**
	 * PDA内容
	 */
	public String PdaContent = "";

	/**
	 * 处理状态 =0 - 未处理； =1 - 已提交处理； =2 - 已处理完成； =3 - 冲突处理完成（其他方式已处理）； =4 -
	 * 过期处理完成（系统处理）； =9 - 处理有错误；
	 */
	public int State = 0;

	/**
	 * 发出时间
	 */
	public Date SendDate = MGlobal.getNow();

	/**
	 * 意见数量
	 */
	public int CommentCount = 0;

	/**
	 * 处理意见
	 */
	public int CommentIndex = 0;

	/**
	 * 处理意见内容
	 */
	public String Comment = "";

	/**
	 * 处理时间
	 */
	public Date TransactDate = MGlobal.getInitDate();

	/**
	 * 发送编号
	 */
	public String SmsSendNo = "";

	/**
	 * 接收日期
	 */
	public Date RecieveDate = MGlobal.getInitDate();

	/**
	 * 接收编号
	 */
	public String SmsRecieveNo = "";

	/**
	 * 接收处理日期
	 */
	public Date RecieveTransactDate = MGlobal.getInitDate();

	/**
	 * 备注
	 */
	public String Memo = "";

	/**
	 * 是否是新增加的移动状态
	 */
	public boolean IsNewMoveEntry = true;

	/**
	 * 当前对象是否可用
	 * 
	 * @param as_ErrorMsg
	 * @param ai_SpaceLength
	 * @return
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
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
		// this.Entry.setEntryID(Integer.parseInt(ao_Node.getAttribute("EntryID")));
		this.Flag = Integer.parseInt(ao_Node.getAttribute("Flag"));
		this.SmsMessage = ao_Node.getAttribute("SmsMessage");
		this.PdaContent = ao_Node.getAttribute("PdaContent");
		this.State = Integer.parseInt(ao_Node.getAttribute("State"));
		this.SendDate = MGlobal.stringToDate(ao_Node.getAttribute("SendDate"));
		this.CommentCount = Integer.parseInt(ao_Node
				.getAttribute("CommentCount"));
		this.CommentIndex = Integer.parseInt(ao_Node
				.getAttribute("CommentIndex"));
		this.Comment = ao_Node.getAttribute("Comment");
		this.TransactDate = MGlobal.stringToDate(ao_Node
				.getAttribute("TransactDate"));
		this.SmsSendNo = ao_Node.getAttribute("SmsSendNo");
		this.SendDate = MGlobal.stringToDate(ao_Node.getAttribute("SendDate"));
		this.SmsRecieveNo = ao_Node.getAttribute("SmsRecieveNo");
		this.RecieveTransactDate = MGlobal.stringToDate(ao_Node
				.getAttribute("RecieveTransactDate"));
		this.Memo = ao_Node.getAttribute("Memo");
		this.IsNewMoveEntry = ao_Node.getAttribute("IsNewMoveEntry")
				.equals("1");
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
		// .setAttribute "Id", this.Id
		ao_Node.setAttribute("EntryID", Integer.toString(this.Entry.EntryID));
		ao_Node.setAttribute("Flag", Integer.toString(this.Flag));
		ao_Node.setAttribute("SmsMessage", this.SmsMessage);
		ao_Node.setAttribute("PdaContent", this.PdaContent);
		ao_Node.setAttribute("State", Integer.toString(this.State));
		ao_Node.setAttribute("SendDate", MGlobal.dateToString(this.SendDate));
		ao_Node.setAttribute("CommentCount",
				Integer.toString(this.CommentCount));
		ao_Node.setAttribute("CommentIndex",
				Integer.toString(this.CommentIndex));
		ao_Node.setAttribute("Comment", this.Comment);
		ao_Node.setAttribute("TransactDate",
				MGlobal.dateToString(this.TransactDate));
		ao_Node.setAttribute("SmsSendNo", this.SmsSendNo);
		ao_Node.setAttribute("SendDate", MGlobal.dateToString(this.SendDate));
		ao_Node.setAttribute("SmsRecieveNo", this.SmsRecieveNo);
		ao_Node.setAttribute("RecieveTransactDate",
				MGlobal.dateToString(this.RecieveTransactDate));
		ao_Node.setAttribute("Memo", this.Memo);
		ao_Node.setAttribute("IsNewMoveEntry",
				(this.IsNewMoveEntry ? "1" : "0"));
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
		ao_Set.put("MEI_FLAG", this.Flag);
		ao_Set.put("MEI_SMS", (this.SmsMessage.equals("") ? null
				: this.SmsMessage));
		ao_Set.put("MEI_TEXT", (this.PdaContent.equals("") ? null
				: this.PdaContent));
		ao_Set.put("MEI_STATE", this.State);
		ao_Set.put("SEND_DATE", MGlobal.dateToSqlTime(this.SendDate));
		ao_Set.put("COMM_COUNT", this.CommentCount);
		ao_Set.put("COMM_INDEX", this.CommentIndex);
		ao_Set.put("MEI_COMM", (this.Comment.equals("") ? null : this.Comment));
		if (this.State > 0) {
			ao_Set.put("TRAN_DATE", MGlobal.dateToSqlTime(this.TransactDate));
		}
		ao_Set.put("SMS_SEND_NO", (this.SmsSendNo.equals("") ? null
				: this.SmsSendNo));
		ao_Set.put("RECI_DATE", MGlobal.dateToSqlTime(this.RecieveDate));
		ao_Set.put("SMS_RECI_NO", (this.SmsRecieveNo.equals("") ? null
				: this.SmsRecieveNo));
		ao_Set.put("RECI_TRAN_DATE",
				MGlobal.dateToSqlTime(this.RecieveTransactDate));
		ao_Set.put("MEI_MEMO", (this.Memo.equals("") ? null : this.Memo));
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
	public String getSaveState(int ai_SaveType, int ai_Type) {
		String ls_Sql = null;
		if (ai_SaveType == 0) {
			ls_Sql = "INSERT INTO MoveEntryInst "
					+ "(WorkItemID, EntryID, MEI_FLAG, MEI_SMS, MEI_TEXT, MEI_STATE, SEND_DATE, "
					+ "COMM_COUNT, COMM_INDEX, MEI_COMM, TRAN_DATE, SMS_SEND_NO, RECI_DATE, "
					+ "SMS_RECI_NO, RECI_TRAN_DATE, MEI_MEMO) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			ls_Sql = "UPDATE MoveEntryInst SET MEI_FLAG = ?, MEI_SMS = ?, MEI_TEXT = ?, MEI_STATE = ?, "
					+ "SEND_DATE = ?, COMM_COUNT = ?, COMM_INDEX = ?, MEI_COMM = ?, TRAN_DATE = ?, "
					+ "SMS_SEND_NO = ?, RECI_DATE = ?, SMS_RECI_NO = ?, RECI_TRAN_DATE = ?, MEI_MEMO = ? "
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
	public int Save(String str_State, final int ai_SaveType, int ai_Type,
			int ai_Update) {
		final CMoveEntry move = this;

		List parasList = new ArrayList();
		if (ai_SaveType == 0) {
			parasList.add(move.Entry.WorkItem.WorkItemID);
			parasList.add(move.Entry.EntryID);
		}

		parasList.add(move.Flag);
		parasList.add((move.SmsMessage.equals("") ? null : move.SmsMessage));
		parasList.add((move.PdaContent.equals("") ? null : move.PdaContent));
		parasList.add(move.State);
		parasList.add(MGlobal.dateToSqlTime(move.SendDate));

		parasList.add(move.CommentCount);
		parasList.add(move.CommentIndex);
		parasList.add((move.Comment.equals("") ? null : move.Comment));
		if (move.State > 0) {
			parasList.add(MGlobal.dateToSqlTime(move.TransactDate));
		} else {
			parasList.add(null);
		}

		parasList.add((move.SmsSendNo.equals("") ? null : move.SmsSendNo));
		parasList.add(MGlobal.dateToSqlTime(move.RecieveDate));
		parasList
				.add((move.SmsRecieveNo.equals("") ? null : move.SmsRecieveNo));
		parasList.add(MGlobal.dateToSqlTime(move.RecieveTransactDate));

		parasList.add((move.Memo.equals("") ? null : move.Memo));

		if (ai_SaveType == 1) {
			parasList.add(move.Entry.WorkItem.WorkItemID);
			parasList.add(move.Entry.EntryID);
		}

		return CSqlHandle.getJdbcTemplate().update(str_State,
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
		this.Flag = (Integer) ao_Set.get("MEI_FLAG");
		if (ao_Set.get("MEI_SMS") != null) {
			this.SmsMessage = (String) ao_Set.get("MEI_SMS");
		}
		if ((ao_Set.get("MEI_TEXT") != null)) {
			this.PdaContent = (String) ao_Set.get("MEI_TEXT");
		}
		this.State = (Integer) ao_Set.get("MEI_STATE");
		this.SendDate = (Date) ao_Set.get("SEND_DATE");
		this.CommentCount = (Integer) ao_Set.get("COMM_COUNT");
		this.CommentIndex = (Integer) ao_Set.get("COMM_INDEX");
		if (ao_Set.get("MEI_COMM") != null) {
			this.Comment = (String) ao_Set.get("MEI_COMM");
		}
		if (this.State > 0) {
			this.TransactDate = (Date) ao_Set.get("TRAN_DATE");
		}

		if (ao_Set.get("SMS_SEND_NO") != null) {
			this.SmsSendNo = (String) ao_Set.get("SMS_SEND_NO");
		}
		if (ao_Set.get("RECI_DATE") != null) {
			this.RecieveDate = (Date) ao_Set.get("RECI_DATE");
		}
		if (ao_Set.get("SMS_RECI_NO") != null) {
			this.SmsRecieveNo = (String) ao_Set.get("SMS_RECI_NO");
		}
		if (ao_Set.get("RECI_TRAN_DATE") != null) {
			this.RecieveTransactDate = (Date) ao_Set.get("RECI_TRAN_DATE");
		}

		if (ao_Set.get("MEI_MEMO") != null) {
			this.Memo = (String) ao_Set.get("MEI_MEMO");
		}

		this.IsNewMoveEntry = false;
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
		// ao_Bag.Write("EntryID",this.Entry.getEntryID());
		ao_Bag.Write("Flag", this.Flag);
		ao_Bag.Write("SmsMessage", this.SmsMessage);
		ao_Bag.Write("PdaContent", this.PdaContent);
		ao_Bag.Write("State", this.State);
		ao_Bag.Write("SendDate", this.SendDate);
		ao_Bag.Write("CommentCount", this.CommentCount);
		ao_Bag.Write("CommentIndex", this.CommentIndex);
		ao_Bag.Write("Comment", this.Comment);
		ao_Bag.Write("TransactDate", this.TransactDate);
		ao_Bag.Write("SmsSendNo", this.SmsSendNo);
		ao_Bag.Write("SendDate", this.SendDate);
		ao_Bag.Write("SmsRecieveNo", this.SmsRecieveNo);
		ao_Bag.Write("RecieveTransactDate", this.RecieveTransactDate);
		ao_Bag.Write("Memo", this.Memo);
		ao_Bag.Write("IsNewMoveEntry", this.IsNewMoveEntry);
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
		// this.Entry.setEntryID(Integer.parseInt(ao_Node.getAttribute("EntryID")));
		this.Flag = ao_Bag.ReadInt("Flag");
		this.SmsMessage = ao_Bag.ReadString("SmsMessage");
		this.PdaContent = ao_Bag.ReadString("PdaContent");
		this.State = ao_Bag.ReadInt("State");
		this.SendDate = ao_Bag.ReadDate("SendDate");
		this.CommentCount = ao_Bag.ReadInt("CommentCount");
		this.CommentIndex = ao_Bag.ReadInt("CommentIndex");
		this.Comment = ao_Bag.ReadString("Comment");
		this.TransactDate = ao_Bag.ReadDate("TransactDate");
		this.SmsSendNo = ao_Bag.ReadString("SmsSendNo");
		this.SendDate = ao_Bag.ReadDate("SendDate");
		this.SmsRecieveNo = ao_Bag.ReadString("SmsRecieveNo");
		this.RecieveTransactDate = ao_Bag.ReadDate("RecieveTransactDate");
		this.Memo = ao_Bag.ReadString("Memo");
		this.IsNewMoveEntry = ao_Bag.ReadBoolean("IsNewMoveEntry");
	}

}
