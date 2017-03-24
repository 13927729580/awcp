package org.szcloud.framework.workflow.core.entity;

import java.util.Date;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.ECaculateHoliday;
import org.szcloud.framework.workflow.core.emun.EDueAlertType;
import org.szcloud.framework.workflow.core.emun.EDueTransactType;
import org.szcloud.framework.workflow.core.emun.ETimelimitAlertFlag;
import org.szcloud.framework.workflow.core.emun.ETimelimitAlertToType;
import org.szcloud.framework.workflow.core.emun.ETimelimitDueFlag;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 期限报警对象：流程对象模型中处理步骤对象的一个子对象，是处理公文流转中公文过期、<br>
 * 报警处理的对象。
 * 
 * @author Jackie.Wang
 * 
 */
public class CDueAlert extends CBase {

	/**
	 * 初始化
	 * 
	 */
	public CDueAlert() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public CDueAlert(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 所属的处理步骤子对象
	 */
	public CTransact Transact = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 期限报警执行类型
	 */
	public int Type = EDueAlertType.AutoDueAlertType;

	/**
	 * 过期类型
	 */
	public int DueFlag = ETimelimitDueFlag.TimelimitDueNone;

	/**
	 * 绝对过期时间
	 */
	public Date DueBy = MGlobal.getInitDate();

	/**
	 * 设置绝对过期时间
	 * 
	 * @param value
	 */
	public void setDueBy(Date value) {
		if (this.DueFlag == ETimelimitDueFlag.TimelimitDueAbs)
			this.DueBy = value;
	}

	/**
	 * 相对过期时间，单位：分钟
	 */
	public int DueWithin = 0;

	/**
	 * 设置相对过期时间，单位：分钟
	 * 
	 * @param value
	 */
	public void setDueWithin(int value) {
		if (this.DueFlag == ETimelimitDueFlag.TimelimitDueBy)
			this.DueWithin = value;
	}

	/**
	 * 相对应的过期属性名称
	 */
	public String DuePropName = "";

	/**
	 * 设置相对应的过期属性名称
	 * 
	 * @param value
	 */
	public void setDuePropName(String value) {
		if (this.DueFlag == ETimelimitDueFlag.TimelimitDueProp)
			this.DuePropName = value;
	}

	/**
	 * 报警类型
	 */
	public int AlertFlag = ETimelimitAlertFlag.TimelimitAlertNone;

	/**
	 * 绝对报警时间
	 */
	public Date AlertBy = MGlobal.getInitDate();

	/**
	 * 设置绝对报警时间
	 * 
	 * @param value
	 */
	public void setAlertBy(Date value) {
		if (this.AlertFlag == ETimelimitAlertFlag.TimelimitAlertAbs)
			this.AlertBy = value;
	}

	/**
	 * 相对报警时间时间，单位：分钟
	 */
	public int AlertWithin = 0;

	/**
	 * 设置相对报警时间时间，单位：分钟
	 * 
	 * @param value
	 */
	public void setAlertWithin(int value) {
		if (this.AlertFlag == ETimelimitAlertFlag.TimelimitAlertBy)
			this.AlertWithin = value;
	}

	/**
	 * 相对应的报警属性名称
	 */
	public String AlertPropName = "";

	/**
	 * 设置相对应的报警属性名称
	 * 
	 * @param value
	 */
	public void setAlertPropName(String value) {
		if (this.AlertFlag == ETimelimitAlertFlag.TimelimitAlertProp)
			this.AlertPropName = value;
	}

	/**
	 * 报警间隔，单位：分钟
	 */
	public int Interval = 0;

	/**
	 * 设置报警间隔，单位：分钟
	 * 
	 * @param value
	 */
	public void setInterval(int value) {
		if (this.AlertFlag != ETimelimitAlertFlag.TimelimitAlertNone)
			this.Interval = value;
	}

	/**
	 * 报警给..的类型
	 */
	public int AlertType = ETimelimitAlertToType.TimelimitAlertToNotResp;

	/**
	 * 报警给
	 */
	public String AlertTo = "";

	/**
	 * 设置报警给
	 * 
	 * @param value
	 * @throws Exception 
	 */
	public void setAlertTo(String value) throws Exception {
		if ((this.AlertType & ETimelimitAlertToType.TimelimitAlertToOther) == ETimelimitAlertToType.TimelimitAlertToOther) {
			if (this.AlertTo.equals(value))
				return;
			this.AlertTo = this.Logon.getUserAdmin().validUsers(value, 3, 1,
					true, true);
		}
	}

	/**
	 * 计算时间类型
	 */
	public int CaculateType = ECaculateHoliday.NotIncludeHoliday;

	/**
	 * 过期需要触发处理的人员个数（=0-表示所有；>0-表示至少有几个处理人过期）
	 */
	public int DueUserNumber = 0;

	/**
	 * 设置过期需要触发处理的人员个数（=0-表示所有；>0-表示至少有几个处理人过期）
	 * 
	 * @param value
	 */
	public void setDueUserNumber(int value) {
		this.DueUserNumber = (value < 0 ? 0 : value);
	}

	/**
	 * 过期处理类型
	 */
	public int DueTransactType = EDueTransactType.NotAnyDueTransact;

	/**
	 * 当公文处理过期后转发给指定人员处理
	 */
	public String DueTransactParticiants = "";

	/**
	 * 当公文处理过期后转发给指定步骤
	 */
	private String ms_DueTransactActivities = "";

	/**
	 * 当公文处理过期后转发给指定步骤
	 * 
	 * @return
	 */
	public String getDueTransactActivities() {
		if (ms_DueTransactActivities == null)
			return null;

		String ls_Name = "";
		for (CActivity lo_Activity : this.Transact.Activity.Flow.Activities) {
			if ((";" + ms_DueTransactActivities).indexOf(";"
					+ Integer.toString(lo_Activity.ActivityID) + ";") > -1)
				ls_Name += lo_Activity.ActivityName + ";";
		}
		return ls_Name;
	}

	/**
	 * 当公文处理过期后转发给指定步骤
	 * 
	 * @param value
	 */
	public void setDueTransactActivities(String value) {
		ms_DueTransactActivities = "";
		if (value == null || value.equals(""))
			return;

		String ls_Value = "";
		for (CActivity lo_Activity : this.Transact.Activity.Flow.Activities) {
			if ((";" + value + ";").indexOf(";" + lo_Activity.ActivityName
					+ ";") > -1)
				ls_Value += Integer.toString(lo_Activity.ActivityID) + ";";
		}
		ms_DueTransactActivities = ls_Value;
	}

	/**
	 * 读取显示当公文处理过期后转发给指定人员处理
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String getDisplayDueTransactParticiants() throws Exception {
		return this.Transact.Activity.Flow.Cyclostyle
				.convertUserValueToDisplay(this.DueTransactParticiants);
	}

	/**
	 * 读取当公文处理过期后转发给指定步骤处理
	 * 
	 * @return
	 */
	public String getDueTransactActivityIDs() {
		return ms_DueTransactActivities;
	}

	/**
	 * 公文报警的最大次数（超过最大数目公文将过期）
	 */
	public int AlertMaxNumber = 0;

	/**
	 * 设置公文报警的最大次数（超过最大数目公文将过期）
	 * 
	 * @param value
	 */
	public void setAlertMaxNumber(int value) {
		if (value > 0) {
			this.AlertMaxNumber = value;
		} else {
			this.AlertMaxNumber = 0;
		}
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 所属的处理步骤子对象
		this.Transact = null;
		
		super.ClearUp();
	}

	/**
	 * 当前对象是否可用
	 * 
	 * @param as_ErrorMsg
	 * @param ai_SpaceLength
	 * @return
	 * @throws Exception 
	 */
	public int IsValid(String as_ErrorMsg, int ai_SpaceLength) throws Exception {
		try {
			return 1;
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
		}
		return 0;
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
		this.Type = (Integer.parseInt(ao_Node.getAttribute("Type")));
		this.DueFlag = (Integer.parseInt(ao_Node.getAttribute("DueFlag")));
		this.DueBy = MGlobal.stringToDate(ao_Node.getAttribute("DueBy"));
		this.DueWithin = Integer.parseInt(ao_Node.getAttribute("DueWithin"));
		this.DuePropName = (ao_Node.getAttribute("DuePropName"));
		this.AlertFlag = (Integer.parseInt(ao_Node.getAttribute("AlertFlag")));
		this.AlertBy = MGlobal.stringToDate(ao_Node.getAttribute("AlertBy"));
		this.AlertWithin = Integer.parseInt(ao_Node.getAttribute("AlertWithin"));
		this.AlertPropName = (ao_Node.getAttribute("AlertPropName"));
		this.Interval = Integer.parseInt(ao_Node.getAttribute("Interval"));
		this.AlertType = (Integer.parseInt(ao_Node.getAttribute("AlertType")));
		this.AlertTo = ao_Node.getAttribute("AlertTo");
		this.CaculateType = (Integer.parseInt(ao_Node
				.getAttribute("CaculateType")));
		this.DueTransactType = (Integer.parseInt(ao_Node
				.getAttribute("DueTransactType")));
		this.DueUserNumber = Integer.parseInt(ao_Node
				.getAttribute("DueUserNumber"));
		this.DueTransactParticiants = (ao_Node
				.getAttribute("DueTransactParticiants"));
		ms_DueTransactActivities = (ao_Node
				.getAttribute("DueTransactActivities"));
		this.AlertMaxNumber = Integer.parseInt(ao_Node
				.getAttribute("AlertMaxNumber"));

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
		try {
			ao_Node.setAttribute("Type", Integer.toString(this.Type));
			ao_Node.setAttribute("DueFlag", Integer.toString(this.DueFlag));
			ao_Node.setAttribute("DueBy", this.DueBy.toString());
			ao_Node.setAttribute("DueWithin", Integer.toString(this.DueWithin));
			ao_Node.setAttribute("DuePropName", this.DuePropName);
			ao_Node.setAttribute("AlertFlag", Integer.toString(this.AlertFlag));
			ao_Node.setAttribute("AlertBy", this.AlertBy.toString());
			ao_Node.setAttribute("AlertWithin",
					Integer.toString(this.AlertWithin));
			ao_Node.setAttribute("AlertPropName", this.AlertPropName);
			ao_Node.setAttribute("Interval", Integer.toString(this.Interval));
			ao_Node.setAttribute("AlertType", Integer.toString(this.AlertType));
			ao_Node.setAttribute("AlertTo", this.AlertTo);
			ao_Node.setAttribute("CaculateType",
					Integer.toString(this.CaculateType));
			ao_Node.setAttribute("DueTransactType",
					Integer.toString(this.DueTransactType));
			ao_Node.setAttribute("DueUserNumber",
					Integer.toString(this.DueUserNumber));
			ao_Node.setAttribute("DueTransactParticiants",
					this.DueTransactParticiants);
			ao_Node.setAttribute("DueTransactActivities",
					ms_DueTransactActivities);
			ao_Node.setAttribute("AlertMaxNumber",
					Integer.toString(this.AlertMaxNumber));
		} catch (Exception e) {
			this.Raise(e, "Export", null);
		}

	}

	/**
	 * 对象保存到数据库行对象中
	 * 
	 * @param ao_Set
	 *            保存的结果集
	 * @param ai_Type
	 *            保存方式：=0-正常保存；=1-短属性保存；
	 */
	@Override
	protected void Save(Map<String, Object> ao_Set, int ai_Type) {
		// 不需要保存 - 无需实现
	}
	/**
	 * 从数据库行对象中读取数据到对象
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开方式：=0-正常打开；=1-短属性打开；
	 */
	@Override
	public void Open(Map<String, Object> ao_Set, int ai_Type) {
		// 不需要打开 - 无需实现
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
		try {
			if (ai_Type == 1) {
				ao_Bag.Write("T", this.Type);
				ao_Bag.Write("DF", this.DueFlag);
				ao_Bag.Write("DB", this.DueBy);
				ao_Bag.Write("DW", this.DueWithin);
				ao_Bag.Write("DP", this.DuePropName);
				ao_Bag.Write("AF", this.AlertFlag);
				ao_Bag.Write("AB", this.AlertBy);
				ao_Bag.Write("AW", this.AlertWithin);
				ao_Bag.Write("AN", this.AlertPropName);
				ao_Bag.Write("IN", this.Interval);
				ao_Bag.Write("AT", this.AlertType);
				ao_Bag.Write("AO", this.AlertTo);
				ao_Bag.Write("CT", this.CaculateType);
				ao_Bag.Write("DTT", this.DueTransactType);
				ao_Bag.Write("DUN", this.DueUserNumber);
				ao_Bag.Write("DTP", this.DueTransactParticiants);
				ao_Bag.Write("DTA", ms_DueTransactActivities);
				ao_Bag.Write("AMN", this.AlertMaxNumber);
			} else {
				ao_Bag.Write("mi_Type", this.Type);
				ao_Bag.Write("mi_DueFlag", this.DueBy);
				ao_Bag.Write("md_DueBy", this.DueBy);
				ao_Bag.Write("ml_DueWithin", this.DueWithin);
				ao_Bag.Write("ms_DuePropName", this.DuePropName);
				ao_Bag.Write("mi_AlertFlag", this.AlertFlag);
				ao_Bag.Write("md_AlertBy", this.AlertBy);
				ao_Bag.Write("ml_AlertWithin", this.AlertWithin);
				ao_Bag.Write("ms_AlertPropName", this.AlertPropName);
				ao_Bag.Write("ml_Interval", this.Interval);
				ao_Bag.Write("mi_AlertType", this.AlertType);
				ao_Bag.Write("ms_AlertTo", this.AlertTo);
				ao_Bag.Write("mi_CaculateType", this.CaculateType);
				ao_Bag.Write("mi_DueTransactType", this.DueTransactType);
				ao_Bag.Write("mi_DueUserNumber", this.DueUserNumber);
				ao_Bag.Write("ms_DueTransactParticiants",
						this.DueTransactParticiants);
				ao_Bag.Write("ms_DueTransactActivities",
						ms_DueTransactActivities);
				ao_Bag.Write("mi_AlertMaxNumber", this.AlertMaxNumber);
			}
		} catch (Exception e) {
			this.Raise(e, "ExportContent", null);
		}
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
		if (ai_Type == 1) {
			this.Type = ao_Bag.ReadInt("T");
			this.DueFlag = ao_Bag.ReadInt("DF");
			this.DueBy = ao_Bag.ReadDate("DB");
			this.DueWithin = ao_Bag.ReadInt("DW");
			this.DuePropName = ao_Bag.ReadString("DP");
			this.AlertFlag = ao_Bag.ReadInt("AF");
			this.AlertBy = ao_Bag.ReadDate("AB");
			this.AlertWithin = ao_Bag.ReadInt("AW");
			this.AlertPropName = ao_Bag.ReadString("AN");
			this.Interval = ao_Bag.ReadInt("IN");
			this.AlertType = ao_Bag.ReadInt("AT");
			this.AlertTo = ao_Bag.ReadString("AO");
			this.CaculateType = ao_Bag.ReadInt("CT");
			this.DueTransactType = ao_Bag.ReadInt("DTT");
			this.DueUserNumber = ao_Bag.ReadInt("DUN");
			this.DueTransactParticiants = ao_Bag.ReadString("DTP");
			ms_DueTransactActivities = ao_Bag.ReadString("DTA");
			this.AlertMaxNumber = ao_Bag.ReadInt("AMN");
		} else {
			this.Type = ao_Bag.ReadInt("mi_DueFlag");
			this.DueFlag = ao_Bag.ReadInt("mi_DueFlag");
			this.DueBy = ao_Bag.ReadDate("md_DueBy");
			this.DueWithin = ao_Bag.ReadInt("ml_DueWithin");
			this.DuePropName = ao_Bag.ReadString("ms_DuePropName");
			this.AlertFlag = ao_Bag.ReadInt("mi_AlertFlag");
			this.AlertBy = ao_Bag.ReadDate("md_AlertBy");
			this.AlertWithin = ao_Bag.ReadInt("ml_AlertWithin");
			this.AlertPropName = ao_Bag.ReadString("ms_AlertPropName");
			this.Interval = ao_Bag.ReadInt("ml_Interval");
			this.AlertType = ao_Bag.ReadInt("mi_AlertType");
			this.AlertTo = ao_Bag.ReadString("ms_AlertTo");
			this.CaculateType = ao_Bag.ReadInt("mi_CaculateType");
			this.DueTransactType = ao_Bag.ReadInt("mi_DueTransactType");
			this.DueUserNumber = ao_Bag.ReadInt("mi_DueUserNumber");
			this.DueTransactParticiants = ao_Bag.ReadString("ms_DueTransactParticiants");
			ms_DueTransactActivities = ao_Bag.ReadString("ms_DueTransactActivities");
			this.AlertMaxNumber = ao_Bag.ReadInt("mi_AlertMaxNumber");
		}
	}

}
