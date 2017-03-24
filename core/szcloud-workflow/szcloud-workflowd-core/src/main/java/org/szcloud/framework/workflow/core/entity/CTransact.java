package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.emun.EDueAlertType;
import org.szcloud.framework.workflow.core.emun.ELapseToType;
import org.szcloud.framework.workflow.core.emun.ELimitCompareType;
import org.szcloud.framework.workflow.core.emun.EStatusTransLimitType;
import org.szcloud.framework.workflow.core.emun.ETransOrderByType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 处理步骤子对象：流程对象模型中继承步骤对象的扩展对象，是步骤处理功能的一种表现形式。
 * 
 * @author Jackie.Wang
 * 
 */
public class CTransact extends CBase {

	/**
	 * 初始化
	 */
	public CTransact() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CTransact(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的步骤
	 */
	public CActivity Activity = null;

	/**
	 * 所包含的期限报警
	 */
	public CDueAlert DueAlert = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 步骤批示选项信息
	 */
	public String ResponseChoices = "";

	/**
	 * 读取步骤批示选项信息的个数
	 */
	public int getChoicesCount() {
		try {
			if (this.ResponseChoices == null || this.ResponseChoices.equals("")) {
				return 0;
			} else {
				String[] ls_Array = this.ResponseChoices.split(";");
				if (this.ResponseChoices.substring(this.ResponseChoices.length() - 1).equals(";")) {
					return ls_Array.length - 1;
				} else {
					return ls_Array.length;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * 是否具有内部传阅功能
	 */
	public boolean InsideReading = false;

	/**
	 * 内部传阅显示标题
	 */
	private String ms_ReadingLabel = "";

	/**
	 * 内部传阅显示标题
	 * 
	 * @return
	 */
	public String getReadingLabel() {
		if (ms_ReadingLabel == null || ms_ReadingLabel.equals("")) {
			return "已阅";
		} else {
			return ms_ReadingLabel;
		}
	}

	/**
	 * 内部传阅显示标题
	 * 
	 * @param value
	 */
	public void setReadingLabel(String value) {
		ms_ReadingLabel = value;
	}

	/**
	 * 是否具有公文发布功能
	 */
	public boolean PublicWorkItem = false;

	/**
	 * 公文发布标题
	 */
	private String ms_PublicLabel = "";

	/**
	 * 公文发布标题
	 * 
	 * @return
	 */
	public String getPublicLabel() {
		if (ms_PublicLabel == null || ms_PublicLabel.equals("")) {
			return "发布";
		} else {
			return ms_PublicLabel;
		}
	}

	/**
	 * 公文发布标题
	 * 
	 * @param value
	 */
	public void setPublicLabel(String value) {
		ms_PublicLabel = value;
	}

	/**
	 * 是否并发处理步骤
	 */
	public boolean Intercurrent = false;

	/**
	 * 是否具有公文成文功能
	 */
	public boolean BuildFile = false;

	/**
	 * 公文成文标题
	 */
	private String ms_BuildLabel = "";

	/**
	 * 公文成文标题
	 * 
	 * @return
	 */
	public String getBuildLabel() {
		if (ms_BuildLabel == null || ms_BuildLabel.equals("")) {
			return "成文";
		} else {
			return ms_BuildLabel;
		}
	}

	/**
	 * 公文成文标题
	 * 
	 * @param value
	 */
	public void setBuildLabel(String value) {
		ms_BuildLabel = value;
	}

	/**
	 * 公文成文模板文件列表的XML字符串，格式为： <Files> <File ID='#' Name='*' File='*' /> ... ...
	 * </Files>
	 */
	public String BuildFileXml = "";

	/**
	 * 发送标题
	 */
	public String SendLabel = "";

	/**
	 * 是否需要保存标题 =0 - 需要; =1 - 不需要; =2 - 根据权限访问;
	 */
	public int Save1LabelFlag = 0;

	/**
	 * 保存标题
	 */
	public String Save1Label = "";

	/**
	 * 是否需要保存并返回标题 =0 - 需要; =1 - 不需要; =2 - 根据权限访问;
	 */
	public int Save2LabelFlag = 0;

	/**
	 * 保存并返回标题
	 */
	public String Save2Label = "";

	/**
	 * 发送时名称显示方式 存储格式表现：送{[<某某某]}{[<步骤名称>]}
	 */
	private String ms_SendStyle = "";

	/**
	 * 发送时名称显示方式
	 * 
	 * @return
	 */
	public String getSendStyle() {
		if (ms_SendStyle == null || ms_SendStyle.equals("")) {
			return "送{[<某某某]}{[<步骤名称>]}";
		} else {
			return ms_SendStyle;
		}
	}

	/**
	 * 发送时名称显示方式
	 * 
	 * @param value
	 */
	public void setSendStyle(String value) {
		ms_SendStyle = value;
	}

	/**
	 * 步骤调用缺省外部参数设置1
	 */
	public String Parameter1 = "";

	/**
	 * 设置步骤调用缺省外部参数设置1
	 * 
	 * @param value
	 */
	public void setParameter1(String value) {
		if (MGlobal.BeyondOfLength(value, 255) == false)
			return;
		this.Parameter1 = value;
	}

	/**
	 * 步骤调用缺省外部参数设置2
	 */
	public String Parameter2 = "";

	/**
	 * 设置步骤调用缺省外部参数设置2
	 * 
	 * @param value
	 */
	public void setParameter2(String value) {
		if (MGlobal.BeyondOfLength(value, 1000) == false) {
			return;
		}
		this.Parameter2 = value;
	}

	/**
	 * 是否使用期限报警
	 */
	public boolean UseTimeLimit = false;

	/**
	 * 是否使用期限报警
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setUseTimeLimit(boolean value) throws Exception {
		if (this.UseTimeLimit == value) {
			return;
		}
		this.UseTimeLimit = value;
		if (this.UseTimeLimit) {
			this.DueAlert = new CDueAlert(this.Logon);
			this.DueAlert.Transact = this;
		} else {
			this.DueAlert.ClearUp();
			this.DueAlert = null;
		}
	}

	/**
	 * 多状态处理限制
	 */
	public int TransLimit = EStatusTransLimitType.NotAnyLimit;

	/**
	 * 多状态处理限制比较类型
	 */
	public int LimitCompare = ELimitCompareType.LimitCompareNumber;

	/**
	 * 多状态处理限制比较类型
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setLimitCompare(int value) throws Exception {
		this.LimitCompare = value;
		this.setCompareCont(this.CompareCont);
	}

	/**
	 * 多状态处理限制比较内容
	 */
	public String CompareCont = "";

	/**
	 * 多状态处理限制比较内容
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setCompareCont(String value) throws Exception {
		try {
			this.CompareCont = Integer.toString(Integer.parseInt(value));
			if (Integer.parseInt(this.CompareCont) < 1) {
				this.CompareCont = "1";
			}
			if (this.LimitCompare == ELimitCompareType.LimitComparePercent) {
				if (Integer.parseInt(this.CompareCont) > 99) {
					this.CompareCont = "99";
				}
			}
		} catch (Exception e) {
			this.Raise(e, "setCompareCont", null);
		}
	}

	/**
	 * 顺序处理类型
	 */
	public int OrderBy = ETransOrderByType.NotAndOrder;

	/**
	 * 顺序处理内容
	 */
	public String OrderByCont = "";

	/**
	 * 步骤转发类型
	 */
	public int LapseTo = ELapseToType.LapseToNotAny;

	/**
	 * 步骤转发标题
	 */
	private String ms_LapseToLabel = "";

	/**
	 * 步骤转发标题
	 * 
	 * @return
	 */
	public String getLapseToLabel() {
		if (ms_LapseToLabel == null || ms_LapseToLabel.equals("")) {
			return "转发";
		} else {
			return ms_LapseToLabel;
		}
	}

	/**
	 * 步骤转发标题
	 * 
	 * @param value
	 */
	public void setLapseToLabel(String value) {
		ms_LapseToLabel = value;
	}

	/**
	 * 转发内容，如发送
	 */
	public String TransmitContent = "";

	/**
	 * 是否直接使用转发[不需要发送，对某些特殊步骤只能使用转发而不许使用发送情况]
	 */
	public boolean UseTransOnly = false;

	/**
	 * 转发步骤数目限制 n=-1 选择任意多个步骤转发 n=0 缺省使用多个步骤转发 n>0 使用1到N个步骤转发
	 */
	public int TransActivityNum = 1;

	/**
	 * 转发步骤数目限制
	 * 
	 * @param value
	 */
	public void setTransActivityNum(int value) {
		if (value < 0) {
			this.TransActivityNum = -1;
		} else {
			this.TransActivityNum = value;
		}
	}

	/**
	 * 使用权限设置，是权限定义标识连接的字符串，用“;”分隔
	 */
	private String ms_UserRight = "1;";

	/**
	 * 使用权限设置
	 * 
	 * @return
	 */
	public String getUserRight() {
		if (this.Activity.ActivityType == EActivityType.StartActivity) {
			return ms_UserRight;
		}
		return "";
	}

	/**
	 * 使用权限设置
	 * 
	 * @param value
	 */
	public void setUserRight(String value) {
		if (this.Activity.ActivityType == EActivityType.StartActivity) {
			ms_UserRight = "2;";
		}
		return;
	}

	/**
	 * 读取使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	public String getUserRightName() {
		ms_UserRight = getUserRight();
		CRight lo_Right = new CRight();
		String ls_Str = "";
		ls_Str = "";
		for (CRight tempLoopVar_lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			lo_Right = tempLoopVar_lo_Right;
			if ((";" + ms_UserRight).indexOf(";" + Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str = ls_Str + lo_Right.RightName + ";";
			}
		}
		return ls_Str;
	}

	/**
	 * 设置使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	public void setUserRightName(String value) {
		if (this.Activity.ActivityType == EActivityType.StartActivity) {
			return;
		}
		if (value == null) {
			ms_UserRight = "1;";
		}
		return;
	}

	/**
	 * 转发到特殊身份人员定义
	 */
	public String SpecialUser = "";

	/**
	 * 读取显示特殊身份人员定义
	 * 
	 * @throws Exception
	 */
	public String getDisplaySpecialUser() throws Exception {
		return this.Activity.Flow.Cyclostyle.convertUserValueToDisplay(this.SpecialUser);
	}

	/**
	 * 外部设置特殊身份人员定义
	 * 
	 * @throws Exception
	 */
	public void setExteriorSpecialUser(String value) throws Exception {
		this.SpecialUser = this.Activity.Flow.Cyclostyle.filterValidUserValue(value);
	}

	/**
	 * 转发到特殊身份人员时使用的权限设置
	 */
	private String ms_SpecialRight = "1;";

	/**
	 * 转发到特殊身份人员时使用的权限设置
	 * 
	 * @return
	 */
	public String getSpecialRight() {
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_SpecialRight).indexOf(";" + Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str += Integer.toString(lo_Right.RightID) + ";";
			}
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";

		ms_SpecialRight = ls_Str;
		return ms_SpecialRight;
	}

	/**
	 * 转发到特殊身份人员时使用的权限设置
	 * 
	 * @param value
	 */
	public void setSpecialRight(String value) {
		if (value == null || value.equals("")) {
			ms_SpecialRight = "1;";
			return;
		}
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value + ";").indexOf(";" + String.valueOf(lo_Right.RightID) + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_SpecialRight = ls_Str;
	}

	/**
	 * 读取特殊使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	public String getSpecialRightName() {
		ms_SpecialRight = getSpecialRight();

		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_SpecialRight).indexOf(";" + Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str += lo_Right.RightName + ";";
			}
		}
		return ls_Str;
	}

	/**
	 * 设置特殊使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	public void setSpecialRightName(String value) {
		if (value == null || value.equals("")) {
			ms_SpecialRight = "1;";
			return;
		}
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value + ";").indexOf(";" + lo_Right.RightName + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_SpecialRight = ls_Str;
	}

	/**
	 * 转发到一个新步骤，该新步骤处理完成后再转回本步骤时的权限设置
	 */
	private String ms_NewBackRight = "1;";

	/**
	 * 转发到一个新步骤，该新步骤处理完成后再转回本步骤时的权限设置
	 * 
	 * @return
	 */
	public String getNewBackRight() {
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_NewBackRight).indexOf(";" + Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str += Integer.toString(lo_Right.RightID) + ";";
			}
		}
		if (ls_Str.equals("")) {
			ls_Str = "1;";
		}
		ms_NewBackRight = ls_Str;
		return ls_Str;
	}

	/**
	 * 转发到一个新步骤，该新步骤处理完成后再转回本步骤时的权限设置
	 * 
	 * @param value
	 */
	public void setNewBackRight(String value) {
		if (value == null || value.equals("")) {
			ms_NewBackRight = "1;";
			return;
		}
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value + ";").indexOf(";" + lo_Right.RightID + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_NewBackRight = ls_Str;
	}

	/**
	 * 读取新步骤[前]使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	public String getNewBackRightName() {
		ms_NewBackRight = getNewBackRight();
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_NewBackRight).indexOf(";" + Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str += lo_Right.RightName + ";";
			}
		}
		return ls_Str;
	}

	/**
	 * 设置新步骤[前]使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	public void setNewBackRightName(String value) {
		if (value == null || value.equals("")) {
			ms_NewBackRight = "1;";
			return;
		}
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value + ";").indexOf(";" + lo_Right.RightName + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_NewBackRight = ls_Str;
	}

	/**
	 * 转发到一个新步骤，该新步骤处理完成后不需要转回本步骤直接继续后继流转时的权限设置
	 */
	private String ms_NewSendRight = "1;";

	/**
	 * 转发到一个新步骤，该新步骤处理完成后不需要转回本步骤直接继续后继流转时的权限设置
	 * 
	 * @return
	 */
	public String getNewSendRight() {
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_NewSendRight).indexOf(";" + Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str += Integer.toString(lo_Right.RightID) + ";";
			}
		}
		if (ls_Str.equals("")) {
			ls_Str = "1;";
		}
		ms_NewSendRight = ls_Str;
		return ls_Str;
	}

	/**
	 * 转发到一个新步骤，该新步骤处理完成后不需要转回本步骤直接继续后继流转时的权限设置
	 * 
	 * @param value
	 */
	public void setNewSendRight(String value) {
		if (value == null || value.equals("")) {
			ms_NewSendRight = "1;";
			return;
		}
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value + ";").indexOf(";" + lo_Right.RightID + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_NewSendRight = ls_Str;
	}

	/**
	 * 读取新步骤[后]使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	public String getNewSendRightName() {
		ms_NewSendRight = getNewSendRight();

		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_NewSendRight).indexOf(";" + Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str += lo_Right.RightName + ";";
			}
		}
		return ls_Str;
	}

	/**
	 * 设置新步骤[后]使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	public void setNewSendRightName(String value) {
		if (value == null || value.equals("")) {
			ms_NewSendRight = "1;";
			return;
		}
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value + ";").indexOf(";" + lo_Right.RightName + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_NewSendRight = ls_Str;
	}

	/**
	 * 并发处理重新获取的属性标识连接串
	 */
	public String InterPropIDs = "";

	/**
	 * 并发处理重新获取的属性标识连接串
	 * 
	 * @param value
	 */
	public void setInterPropIDs(String value) {
		this.InterPropIDs = "";
		for (CProp lo_Prop : this.Activity.Flow.Cyclostyle.Props) {
			if ((";" + value + ";").indexOf(";" + Integer.toString(lo_Prop.PropID) + ";") + 1 > 0) {
				this.InterPropIDs += Integer.toString(lo_Prop.PropID) + ";";
			}
		}
	}

	/**
	 * 并发处理重新获取的角色标识连接串
	 */
	public String InterRoleIDs = "";

	/**
	 * 并发处理重新获取的角色标识连接串
	 * 
	 * @param value
	 */
	public void setInterRoleIDs(String value) {
		this.InterRoleIDs = "";
		for (CRole lo_Role : this.Activity.Flow.Cyclostyle.Roles) {
			if ((";" + value + ";").indexOf(";" + Integer.toString(lo_Role.RoleID) + ";") + 1 > 0
					&& lo_Role.RoleID > 1) {
				this.InterRoleIDs += Integer.toString(lo_Role.RoleID) + ";";
			}
		}
	}

	/**
	 * 并发处理执行的二次开发函数
	 */
	public String InterScript = "";

	/**
	 * 期限报警执行类型，内存变量，当设置为>NotAnyDueAlertType后， 针对人工或自动转人工的后续触发步骤，????设置催办报警处理
	 */
	public int DueAlertType = EDueAlertType.NotAnyDueAlertType;

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
		} catch (Exception e) {
			this.Raise(e, "IsValid()", null);
			return e.toString();
		}
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属的步骤
		this.Activity = null;
		// 所包含的期限报警
		if (!(this.DueAlert == null)) {
			this.DueAlert.ClearUp();
			this.DueAlert = null;
		}

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
		this.ResponseChoices = ao_Node.getAttribute("ResponseChoices");
		this.InsideReading = Boolean.parseBoolean(ao_Node.getAttribute("InsideReading"));
		ms_ReadingLabel = ao_Node.getAttribute("ReadingLabel");
		this.PublicWorkItem = Boolean.parseBoolean(ao_Node.getAttribute("PublicWorkItem"));
		ms_PublicLabel = ao_Node.getAttribute("PublicLabel");
		this.Intercurrent = Boolean.parseBoolean(ao_Node.getAttribute("Intercurrent"));
		this.BuildFile = Boolean.parseBoolean(ao_Node.getAttribute("BuildFile"));
		ms_BuildLabel = ao_Node.getAttribute("BuildLabel");
		this.BuildFileXml = ao_Node.getAttribute("BuildFileXML");
		this.SendLabel = ao_Node.getAttribute("SendLabel");
		this.Save1LabelFlag = MXmlHandle.readInt(ao_Node, "Save1LabelFlag");
		this.Save1Label = ao_Node.getAttribute("Save1Label");
		this.Save2LabelFlag = MXmlHandle.readInt(ao_Node, "Save2LabelFlag");
		this.Save2Label = ao_Node.getAttribute("Save2Label");
		ms_SendStyle = ao_Node.getAttribute("SendStyle");
		this.UseTimeLimit = Boolean.parseBoolean(ao_Node.getAttribute("UseTimeLimit"));
		this.TransLimit = Integer.parseInt(ao_Node.getAttribute("TransLimit"));
		this.LimitCompare = Integer.parseInt(ao_Node.getAttribute("LimitCompare"));
		this.CompareCont = ao_Node.getAttribute("CompareCont");
		this.OrderBy = Integer.parseInt(ao_Node.getAttribute("OrderBy"));
		this.OrderByCont = ao_Node.getAttribute("OrderByCont");
		this.LapseTo = Integer.parseInt(ao_Node.getAttribute("LapseTo"));
		ms_LapseToLabel = ao_Node.getAttribute("LapseToLabel");
		this.UseTransOnly = Boolean.parseBoolean(ao_Node.getAttribute("UseTransOnly"));
		this.TransActivityNum = Integer.parseInt(ao_Node.getAttribute("TransActivityNum"));
		this.TransmitContent = ao_Node.getAttribute("TransmitContent");
		ms_UserRight = ao_Node.getAttribute("UserRight");
		this.SpecialUser = ao_Node.getAttribute("SpecialUser");
		ms_SpecialRight = ao_Node.getAttribute("SpecialRight");
		ms_NewBackRight = ao_Node.getAttribute("NewBackRight");
		ms_NewSendRight = ao_Node.getAttribute("NewSendRight");
		this.Parameter1 = ao_Node.getAttribute("Parameter1");
		this.Parameter2 = ao_Node.getAttribute("Parameter2");
		this.InterPropIDs = ao_Node.getAttribute("InterPropIDs");
		this.InterRoleIDs = ao_Node.getAttribute("InterRoleIDs");
		this.InterScript = ao_Node.getAttribute("InterScript");
		if (this.UseTimeLimit) {
			this.DueAlert = new CDueAlert(this.Logon);
			this.DueAlert.Transact = this;
			Element lo_Node = (Element) ao_Node.getElementsByTagName("TimeLimit").item(0);
			this.DueAlert.Import(lo_Node, ai_Type);
		}
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
		Document lo_Xml = MXmlHandle.LoadXml("<Activity />");
		Element lo_Node = lo_Xml.getDocumentElement();
		ao_Node.setAttribute("ResponseChoices", this.ResponseChoices);
		ao_Node.setAttribute("InsideReading", Boolean.toString(this.InsideReading));
		ao_Node.setAttribute("ReadingLabel", ms_ReadingLabel);
		ao_Node.setAttribute("PublicWorkItem", Boolean.toString(this.PublicWorkItem));
		ao_Node.setAttribute("PublicLabel", ms_PublicLabel);
		ao_Node.setAttribute("Intercurrent", Boolean.toString(this.Intercurrent));
		ao_Node.setAttribute("BuildFile", Boolean.toString(this.BuildFile));
		ao_Node.setAttribute("BuildLabel", ms_BuildLabel);
		ao_Node.setAttribute("BuildFileXML", this.BuildFileXml);
		ao_Node.setAttribute("SendLabel", this.SendLabel);
		ao_Node.setAttribute("Save1LabelFlag", Integer.toString(this.Save1LabelFlag));
		ao_Node.setAttribute("Save1Label", this.Save1Label);
		ao_Node.setAttribute("Save2LabelFlag", Integer.toString(this.Save2LabelFlag));
		ao_Node.setAttribute("Save2Label", this.Save2Label);
		ao_Node.setAttribute("SendStyle", ms_SendStyle);
		ao_Node.setAttribute("UseTimeLimit", Boolean.toString(this.UseTimeLimit));
		ao_Node.setAttribute("TransLimit", Integer.toString(this.TransLimit));
		ao_Node.setAttribute("LimitCompare", Integer.toString(this.LimitCompare));
		ao_Node.setAttribute("CompareCont", this.CompareCont);
		ao_Node.setAttribute("OrderBy", Integer.toString(this.OrderBy));
		ao_Node.setAttribute("OrderByCont", this.OrderByCont);
		ao_Node.setAttribute("LapseTo", Integer.toString(this.LapseTo));
		ao_Node.setAttribute("LapseToLabel", ms_LapseToLabel);
		ao_Node.setAttribute("UseTransOnly", Boolean.toString(this.UseTransOnly));
		ao_Node.setAttribute("TransActivityNum", Integer.toString(this.TransActivityNum));
		ao_Node.setAttribute("TransmitContent", this.TransmitContent);
		ao_Node.setAttribute("UserRight", ms_UserRight);
		ao_Node.setAttribute("SpecialUser", this.SpecialUser);
		ao_Node.setAttribute("SpecialRight", ms_SpecialRight);
		ao_Node.setAttribute("NewBackRight", ms_NewBackRight);
		ao_Node.setAttribute("NewSendRight", ms_NewSendRight);
		ao_Node.setAttribute("Parameter1", this.Parameter1);
		ao_Node.setAttribute("Parameter2", this.Parameter2);
		// 20060307 Add
		ao_Node.setAttribute("InterPropIDs", this.InterPropIDs);
		ao_Node.setAttribute("InterRoleIDs", this.InterRoleIDs);
		ao_Node.setAttribute("InterScript", this.InterScript);
		if (this.UseTimeLimit) {
			lo_Node = ao_Node.getOwnerDocument().createElement("TimeLimit");
			ao_Node.appendChild(lo_Node);
			this.DueAlert.Export(lo_Node, ai_Type);
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
		// 不需要保存-无需实现
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
		// 不需要打开-无需实现
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
		if (ai_Type == 1) {
			ao_Bag.Write("RC", this.ResponseChoices);
			ao_Bag.Write("IR", this.InsideReading);
			ao_Bag.Write("RL", ms_ReadingLabel);
			ao_Bag.Write("PW", this.PublicWorkItem);
			ao_Bag.Write("PL", ms_PublicLabel);
			ao_Bag.Write("IU", this.Intercurrent);
			ao_Bag.Write("BF", this.BuildFile);
			ao_Bag.Write("BL", ms_BuildLabel);
			ao_Bag.Write("BX", this.BuildFileXml);
			ao_Bag.Write("SL", this.SendLabel);
			ao_Bag.Write("S1L", this.Save1Label);
			ao_Bag.Write("S2L", this.Save2Label);
			ao_Bag.Write("SS", ms_SendStyle);
			ao_Bag.Write("UT", this.UseTimeLimit);
			ao_Bag.Write("TL", this.TransLimit);
			ao_Bag.Write("LC", this.LimitCompare);
			ao_Bag.Write("CC", this.CompareCont);
			ao_Bag.Write("OB", this.OrderBy);
			ao_Bag.Write("OBC", this.OrderByCont);
			ao_Bag.Write("LT", this.LapseTo);
			ao_Bag.Write("LTL", ms_LapseToLabel);
			ao_Bag.Write("UTO", this.UseTransOnly);
			ao_Bag.Write("TAN", this.TransActivityNum);
			ao_Bag.Write("TC", this.TransmitContent);
			ao_Bag.Write("UR", ms_UserRight);
			ao_Bag.Write("SU", this.SpecialUser);
			ao_Bag.Write("SR", ms_SpecialRight);
			ao_Bag.Write("NBR", ms_NewBackRight);
			ao_Bag.Write("NSR", ms_NewSendRight);
			ao_Bag.Write("P1", this.Parameter1);
			ao_Bag.Write("P2", this.Parameter2);
			ao_Bag.Write("PID", this.InterPropIDs);
			ao_Bag.Write("RID", this.InterRoleIDs);
			ao_Bag.Write("IS", this.InterScript);
		} else {
			ao_Bag.Write("ms_ResponseChoices", this.ResponseChoices);
			ao_Bag.Write("mb_InsideReading", this.InsideReading);
			ao_Bag.Write("ms_ReadingLabel", ms_ReadingLabel);
			ao_Bag.Write("mb_PublicWorkItem", this.PublicWorkItem);
			ao_Bag.Write("ms_PublicLabel", ms_PublicLabel);
			ao_Bag.Write("mb_Intercurrent", this.Intercurrent);
			ao_Bag.Write("mb_BuildFile", this.BuildFile);
			ao_Bag.Write("ms_BuildLabel", ms_BuildLabel);
			ao_Bag.Write("ms_BuildFileXML", this.BuildFileXml);
			ao_Bag.Write("ms_SendLabel", this.SendLabel);
			ao_Bag.Write("mi_Save1LabelFlag", this.Save1LabelFlag);
			ao_Bag.Write("ms_Save1Label", this.Save1Label);
			ao_Bag.Write("mi_Save2LabelFlag", this.Save2LabelFlag);
			ao_Bag.Write("ms_Save2Label", this.Save2Label);
			ao_Bag.Write("ms_SendStyle", ms_SendStyle);
			ao_Bag.Write("mb_UseTimeLimit", this.UseTimeLimit);
			ao_Bag.Write("mi_TransLimit", this.TransLimit);
			ao_Bag.Write("mi_LimitCompare", this.LimitCompare);
			ao_Bag.Write("ms_CompareCont", this.CompareCont);
			ao_Bag.Write("mi_OrderBy", this.OrderBy);
			ao_Bag.Write("ms_OrderByCont", this.OrderByCont);
			ao_Bag.Write("mi_LapseTo", this.LapseTo);
			ao_Bag.Write("ms_LapseToLabel", ms_LapseToLabel);
			ao_Bag.Write("mb_UseTransOnly", this.UseTransOnly);
			ao_Bag.Write("mi_TransActivityNum", this.TransActivityNum);
			ao_Bag.Write("ms_TransmitContent", this.TransmitContent);
			ao_Bag.Write("ms_UserRight", ms_UserRight);
			ao_Bag.Write("ms_SpecialUser", this.SpecialUser);
			ao_Bag.Write("ms_SpecialRight", ms_SpecialRight);
			ao_Bag.Write("ms_NewBackRight", ms_NewBackRight);
			ao_Bag.Write("ms_NewSendRight", ms_NewSendRight);
			ao_Bag.Write("ms_Parameter1", this.Parameter1);
			ao_Bag.Write("ms_Parameter2", this.Parameter2);
			ao_Bag.Write("ms_InterPropIDs", this.InterPropIDs);
			ao_Bag.Write("ms_InterRoleIDs", this.InterRoleIDs);
			ao_Bag.Write("ms_InterScript", this.InterScript);
		}
		if (this.UseTimeLimit) {
			this.DueAlert.Write(ao_Bag, ai_Type);
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
			this.ResponseChoices = ao_Bag.ReadString("RC");
			this.InsideReading = ao_Bag.ReadBoolean("IR");
			ms_ReadingLabel = ao_Bag.ReadString("RL");
			this.PublicWorkItem = ao_Bag.ReadBoolean("PW");
			ms_PublicLabel = ao_Bag.ReadString("PL");
			this.Intercurrent = ao_Bag.ReadBoolean("IU");
			this.BuildFile = ao_Bag.ReadBoolean("BF");
			ms_BuildLabel = ao_Bag.ReadString("BL");
			this.BuildFileXml = ao_Bag.ReadString("BX");
			this.SendLabel = ao_Bag.ReadString("SL");
			this.Save1Label = ao_Bag.ReadString("S1L");
			this.Save2Label = ao_Bag.ReadString("S2L");
			ms_SendStyle = ao_Bag.ReadString("SS");
			this.UseTimeLimit = ao_Bag.ReadBoolean("UT");
			this.TransLimit = ao_Bag.ReadInt("TL");
			this.LimitCompare = ao_Bag.ReadInt("LC");
			this.CompareCont = ao_Bag.ReadString("CC");
			this.OrderBy = ao_Bag.ReadInt("OB");
			this.OrderByCont = ao_Bag.ReadString("OBC");
			this.LapseTo = ao_Bag.ReadInt("LT");
			ms_LapseToLabel = ao_Bag.ReadString("LTL");
			this.UseTransOnly = ao_Bag.ReadBoolean("UTO");
			this.TransActivityNum = ao_Bag.ReadInt("TAN");
			this.TransmitContent = ao_Bag.ReadString("TC");
			ms_UserRight = ao_Bag.ReadString("UR");
			this.SpecialUser = ao_Bag.ReadString("SU");
			ms_SpecialRight = ao_Bag.ReadString("SR");
			ms_NewBackRight = ao_Bag.ReadString("NBR");
			ms_NewSendRight = ao_Bag.ReadString("NSR");
			this.Parameter1 = ao_Bag.ReadString("P1");
			this.Parameter2 = ao_Bag.ReadString("P2");
			this.InterPropIDs = ao_Bag.ReadString("PID");
			this.InterRoleIDs = ao_Bag.ReadString("RID");
			this.InterScript = ao_Bag.ReadString("IS");
		} else {
			this.ResponseChoices = ao_Bag.ReadString("ms_ResponseChoices");
			this.InsideReading = ao_Bag.ReadBoolean("mb_InsideReading");
			ms_ReadingLabel = ao_Bag.ReadString("ms_ReadingLabel");
			this.PublicWorkItem = ao_Bag.ReadBoolean("mb_PublicWorkItem");
			ms_PublicLabel = ao_Bag.ReadString("ms_PublicLabel");
			this.Intercurrent = ao_Bag.ReadBoolean("mb_Intercurrent");
			this.BuildFile = ao_Bag.ReadBoolean("mb_BuildFile");
			ms_BuildLabel = ao_Bag.ReadString("ms_BuildLabel");
			this.BuildFileXml = ao_Bag.ReadString("ms_BuildFileXML");
			this.SendLabel = ao_Bag.ReadString("ms_SendLabel");
			this.Save1LabelFlag = ao_Bag.ReadInt("mi_Save1LabelFlag");
			this.Save1Label = ao_Bag.ReadString("ms_Save1Label");
			this.Save2LabelFlag = ao_Bag.ReadInt("mi_Save2LabelFlag");
			this.Save2Label = ao_Bag.ReadString("ms_Save2Label");
			ms_SendStyle = ao_Bag.ReadString("ms_SendStyle");
			this.UseTimeLimit = ao_Bag.ReadBoolean("mb_UseTimeLimit");
			this.TransLimit = ao_Bag.ReadInt("mi_TransLimit");
			this.LimitCompare = ao_Bag.ReadInt("mi_LimitCompare");
			this.CompareCont = ao_Bag.ReadString("ms_CompareCont");
			this.OrderBy = ao_Bag.ReadInt("mi_OrderBy");
			this.OrderByCont = ao_Bag.ReadString("ms_OrderByCont");
			this.LapseTo = ao_Bag.ReadInt("mi_LapseTo");
			ms_LapseToLabel = ao_Bag.ReadString("ms_LapseToLabel");
			this.UseTransOnly = ao_Bag.ReadBoolean("mb_UseTransOnly");
			this.TransActivityNum = ao_Bag.ReadInt("mi_TransActivityNum");
			this.TransmitContent = ao_Bag.ReadString("ms_TransmitContent");
			ms_UserRight = ao_Bag.ReadString("ms_UserRight");
			this.SpecialUser = ao_Bag.ReadString("ms_SpecialUser");
			ms_SpecialRight = ao_Bag.ReadString("ms_SpecialRight");
			ms_NewBackRight = ao_Bag.ReadString("ms_NewBackRight");
			ms_NewSendRight = ao_Bag.ReadString("ms_NewSendRight");
			this.Parameter1 = ao_Bag.ReadString("ms_Parameter1");
			this.Parameter2 = ao_Bag.ReadString("ms_Parameter2");
			this.InterPropIDs = ao_Bag.ReadString("ms_InterPropIDs");
			this.InterRoleIDs = ao_Bag.ReadString("ms_InterRoleIDs");
			this.InterScript = ao_Bag.ReadString("ms_InterScript");
		}
		if (this.UseTimeLimit) {
			this.DueAlert = new CDueAlert(this.Logon);
			this.DueAlert.Transact = this;
			this.DueAlert.Read(ao_Bag, ai_Type);
		}
	}

	/**
	 * 根据步骤批示选项信息的确定索引值
	 * 
	 * @param as_Content
	 * @return
	 */
	public int getChoiceByContent(String as_Content) {
		if (this.ResponseChoices.equals(""))
			return 0;

		if ((";" + this.ResponseChoices).indexOf(";" + as_Content + ";") == -1)
			return 0;

		String[] ls_Array = this.ResponseChoices.split(";");
		for (int i = 0; i < ls_Array.length; i++) {
			if (ls_Array[i].equals(as_Content)) {
				return i + 1;
			}
		}
		return 0;
	}

}
