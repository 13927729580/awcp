package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.business.TUserAdmin;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.emun.EConditionType;
import org.szcloud.framework.workflow.core.emun.ECurActivityTransType;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EEntryType;
import org.szcloud.framework.workflow.core.emun.ERemindAddFlag;
import org.szcloud.framework.workflow.core.emun.EWorkItemFlowFlag;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 步骤对象：流程对象模型中的基本对象，它是流转系统中的一个环节，是一个活动的对象，<br>
 * 在对象模型图中，它其实代表一个有向图的节点。
 * 
 * @author Jackie.Wang
 */
public class CActivity extends CBase {

	/**
	 * 初始化
	 */
	public CActivity() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CActivity(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 常量定义
	// #==========================================================================#
	/**
	 * 步骤显示图形缺省宽度尺寸
	 */
	private final int constDefaultActivityWidth = 32;
	/**
	 * 步骤显示图形缺省高度尺寸
	 */
	private final int constDefaultActivityHeight = 32;

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属流程
	 */
	public CFlow Flow = null;

	/**
	 * 包含的步骤处理子对象，当步骤类型为开始步骤或处理步骤是才有
	 */
	public CTransact Transact = null;

	/**
	 * 包含的条件对象，当步骤类型为开始时可以没有，其他步骤都必须有，并且为逻辑处理条件
	 */
	public CCondition Condition = null;

	/**
	 * 包含的步骤嵌入子对象，当步骤类型为嵌入步骤是才存在
	 */
	public CTumbleIn TumbleIn = null;

	/**
	 * 包含的步骤启动子对象，当步骤类型为启动步骤是才存在
	 */
	public CLaunch Launch = null;

	/**
	 * 包含的步骤通知子对象，当步骤类型为通知步骤是才存在
	 */
	public CFYI FYI = null;

	/**
	 * 包含的步骤参与人，当步骤类型为开始步骤时，步骤处理人为：[<拟稿>] 当步骤为结束或分支步骤时没有该对象
	 */
	public CParticipant Participant = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 步骤标识
	 */
	public int ActivityID = 0;

	/**
	 * 步骤名称
	 */
	public String ActivityName = null;

	/**
	 * 设置步骤名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setActivityName(String value) throws Exception {
		// 步骤名称不可以为空
		if (MGlobal.isEmpty(value)) {
			return;
		}

		// 步骤名称不能相同
		if (!this.GlobalPara().IsInsideTransact) {
			if (value.equals(this.Flow.SpecialActivityName)
					|| value.equals(this.Flow.PreActivityName)
					|| value.equals(this.Flow.NextActivityName)) {
				// 错误[1016]：步骤名称不能相同
				this.Raise(1016, "setActivityName", value);
				return;
			}
		}

		CActivity lo_Activity = new CActivity();
		for (CActivity lo_Act : this.Flow.Activities) {
			if (lo_Act != this) {
				if (lo_Activity.ActivityName.equals(value)) {
					// 错误[1016]：步骤名称不能相同
					this.Raise(1016, "setActivityName", value);
					return;
				}
			}
		}

		this.ActivityName = value;
	}

	/**
	 * 步骤类型
	 */
	public int ActivityType = EActivityType.NotAnyActivity;

	/**
	 * 设置步骤类型
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setActivityType(int value) throws Exception {
		try {
			// #步骤类型只能被初始化
			if (this.ActivityType != EActivityType.NotAnyActivity)
				return;

			this.ActivityType = value;

			// 只有分支和结束步骤没有步骤参与人对象
			if (!(this.ActivityType == EActivityType.SplitActivity || this.ActivityType == EActivityType.StopActivity)) {
				this.Participant = new CParticipant(this.Logon);
				this.Participant.Activity = this;
				// 拟稿步骤的参与人为：[拟稿人]
				if (this.ActivityType == EActivityType.StartActivity)
					this.Participant.Participant = "@1;";
			}

			switch (this.ActivityType) {
			case EActivityType.StartActivity: // #开始步骤和处理步骤
			case EActivityType.TransactActivity:
				this.Transact = new CTransact(this.Logon);
				this.Transact.Activity = this;
				break;
			case EActivityType.FYIActivity: // #通知步骤
				this.FYI = new CFYI(this.Logon);
				this.FYI.Activity = this;
				break;
			case EActivityType.TumbleInActivity: // #嵌入步骤
				this.TumbleIn = new CTumbleIn(this.Logon);
				this.TumbleIn.Activity = this;
				break;
			case EActivityType.LaunchActivity: // #启动步骤
				this.Launch = new CLaunch(this.Logon);
				this.Launch.Activity = this;
				break;
			case EActivityType.SplitActivity: // #分支和结束步骤
			case EActivityType.StopActivity:
				break;
			case EActivityType.NotLimitedActivity: // #无限制条件步骤类型
				break;
			default:
				break;
			}
		} catch (Exception e) {
			this.Raise(e, "setActivityType", String.valueOf(value));
		}
	}

	/**
	 * 步骤移动事件
	 * 
	 * @param al_Left
	 * @param al_Top
	 * @param al_Width
	 * @param al_Height
	 */
	public void Move(int al_Left, int al_Top, int al_Width, int al_Height) {

	}

	/**
	 * 左边距，单位：Pixel(象素)
	 */
	public int Left = 0;

	/**
	 * 设置左边距
	 * 
	 * @param value
	 */
	public void setLeft(int value) {
		if (this.Left >= 0) {
			this.Left = value;
			this.Move(this.Left, this.Top, this.Width, this.Height);
		}
	}

	/**
	 * 上边距，单位：Pixel(象素)
	 */
	public int Top = 0;

	/**
	 * 设置上边距
	 * 
	 * @param value
	 */
	public void setTop(int value) {
		if (this.Top >= 0) {
			this.Top = value;
			this.Move(this.Left, this.Top, this.Width, this.Height);
		}
	}

	/**
	 * 步骤宽度，单位：Pixel(象素)
	 */
	public int Width = constDefaultActivityWidth;

	/**
	 * 设置步骤宽度
	 * 
	 * @param value
	 */
	public void setWidth(int value) {
		if (value > 0) {
			this.Width = value;
			this.Move(this.Left, this.Top, this.Width, this.Height);
		}
	}

	/**
	 * 步骤高度，单位：Pixel(象素)
	 */
	public int Height = constDefaultActivityHeight;

	/**
	 * 设置步骤高度
	 * 
	 * @param value
	 */
	public void setHeight(int value) {
		if (value > 0) {
			this.Height = value;
			this.Move(this.Left, this.Top, this.Width, this.Height);
		}
	}

	/**
	 * 步骤说明信息
	 */
	public String Summary = "";

	/**
	 * 步骤的顺序排列号
	 */
	public int OrderID = 0;

	/**
	 * 步骤使用的图象标识，缺省使用时为0
	 */
	public int ImageID = 0;

	/**
	 * 附加提醒方式
	 */
	public int RemindAddFlag = ERemindAddFlag.DefaultRemindFlag;

	/**
	 * 附加信息设置的XML字符串，格式请参数属性ExtendXml
	 */
	private String ms_ExtendXml = null;
	/**
	 * 附加信息XML对象临时变量
	 */
	private Document mo_ExtendXml = null;

	/**
	 * 附加信息XML对象临时变量
	 * 
	 * @return
	 */
	public String getExtendXml() {
		this.initExtendXml();
		return ms_ExtendXml;
	}

	/**
	 * 附加信息XML对象临时变量
	 * 
	 * @param as_ExtendXml
	 */
	public void setExtendXml(String as_ExtendXml) {
		Document lo_Xml = MXmlHandle.LoadXml(as_ExtendXml);
		if (lo_Xml != null) {
			mo_ExtendXml = null;
			mo_ExtendXml = lo_Xml;
			ms_ExtendXml = MXmlHandle.getXml(lo_Xml);
		}
	}

	/**
	 * 获取角色选择设置的XML字符串
	 * 
	 * @return
	 */
	public String getSelectRoleXml() {
		this.initExtendXml();
		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendXml.getDocumentElement(), "Roles");
		return lo_Node.getTextContent();
	}

	/**
	 * 设置角色选择设置的XML字符串
	 * 
	 * @param as_XmlData
	 */
	public void setSelectRoleXml(String as_XmlData) {
		Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
		this.initExtendXml();
		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendXml.getDocumentElement(), "Roles");
		mo_ExtendXml.getDocumentElement().removeChild(lo_Node);
		mo_ExtendXml.getDocumentElement().appendChild(
				lo_Xml.getDocumentElement());
		lo_Xml = null;
	}

	/**
	 * 获取移动应用的XML定义
	 * 
	 * @return
	 */
	public String getMobileXml() {
		this.initExtendXml();
		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendXml.getDocumentElement(), "Mobile");
		return lo_Node.getTextContent();
	}

	/**
	 * 设置移动应用的XML定义
	 * 
	 * @param as_XmlData
	 */
	public void setMobileXml(String as_XmlData) {
		Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
		this.initExtendXml();
		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendXml.getDocumentElement(), "Mobile");
		mo_ExtendXml.getDocumentElement().removeChild(lo_Node);
		mo_ExtendXml.getDocumentElement().appendChild(
				lo_Xml.getDocumentElement());
		lo_Xml = null;
	}

	//

	/**
	 * 获取移动设备集成类型
	 * 
	 * @return
	 */
	public int getMobileType() {
		this.initExtendXml();
		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendXml.getDocumentElement(), "Mobile");
		if (lo_Node.getAttribute("Type") != null) {
			return 0;
		} else {
			return Integer.parseInt(lo_Node.getAttribute("Type"));
		}
	}

	/**
	 * 设置移动设备集成类型
	 * 
	 * @param value
	 */
	public void setMobileType(int value) {
		this.initExtendXml();
		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendXml.getDocumentElement(), "Mobile");
		lo_Node.setAttribute("Type", String.valueOf(value));
	}

	/**
	 * 获取正式打印的XML内容定义
	 * 
	 * @return
	 */
	public String getPrintXml() {
		this.initExtendXml();
		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendXml.getDocumentElement(), "Print");
		if (lo_Node == null) {
			return "<Print />";
		}
		return lo_Node.getTextContent();
	}

	/**
	 * 设置正式打印的XML内容定义
	 * 
	 * @param as_XmlData
	 */
	public void setPrintXml(String as_XmlData) {
		this.initExtendXml();
		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendXml.getDocumentElement(), "Print");
		if (lo_Node != null)
			mo_ExtendXml.getDocumentElement().removeChild(lo_Node);

		Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
		if (lo_Xml == null)
			return;

		mo_ExtendXml.getDocumentElement().appendChild(lo_Node);
		lo_Xml = null;
	}

	/**
	 * 读取步骤使用的图象名称
	 * 
	 * @return
	 */
	public String getImageName() {
		if (this.ImageID == 0)
			return null;

		String ls_Sql = "SELECT ImageID, ImageName FROM ImageInfo WHERE ImageID = "
				+ Integer.toString(this.ImageID);
		// return
		// CSqlHandle.getJdbcTemplate().queryForObject(ls_Sql,String.class);
		return CSqlHandle.getValueBySql(ls_Sql);
	}

	/**
	 * 设置步骤使用的图象名称
	 * 
	 * @param value
	 */
	public void setImageName(String value) {
		this.ImageID = 0;

		if (value == null || value.equals(""))
			return;

		String ls_Sql = "SELECT ImageID FROM ImageInfo WHERE ImageName = \'"
				+ value.replace("\'", "\'\'") + "\'";
		ls_Sql = CSqlHandle.getValueBySql(ls_Sql);

		if (MGlobal.isExist(ls_Sql))
			this.ImageID = Integer.parseInt(ls_Sql);
	}

	/**
	 * 读取步骤处理类型
	 * 
	 * @return
	 */
	public int getTransType() {
		if (this.ActivityName != null) {
			if (this.ActivityName.equals(this.Flow.PreActivityName))
				return ECurActivityTransType.SendPreTransType;
			if (this.ActivityName.equals(this.Flow.NextActivityName))
				return ECurActivityTransType.SendNextTransType;
			if (this.ActivityName.equals(this.Flow.SpecialActivityName))
				return ECurActivityTransType.SpecialTransType;
		}
		return ECurActivityTransType.CommondTransType;
	}

	/**
	 * 初始化附加信息
	 */
	private void initExtendXml() {
		if (mo_ExtendXml != null)
			return;

		String ls_Xml = "<Extend><Summary";
		ls_Xml += " Save=\"保存新建公文或公文的修改内容\"";
		ls_Xml += " SaveExit=\"保存并关闭新建公文或公文的修改内容\"";
		ls_Xml += " Send=\"把公文发送给其他人继续处理\"";
		ls_Xml += " InterRead=\"内部人员传阅本公文\"";
		ls_Xml += " Public=\"发布公文\"";
		ls_Xml += " Build=\"成文公文\"";
		ls_Xml += " SendTo=\"把公文转发给其他人继续处理\"";
		ls_Xml += " Print=\"打印公文表单\"";
		ls_Xml += " Lookup=\"查看本公文已经流转的状态（公文处理情况）\"";
		ls_Xml += " Exit=\"退出公文处理\"";
		ls_Xml += " Help=\"查看帮助文档\"";
		ls_Xml += " Flow=\"查看或修改本公文所使用的处理流程\"";
		ls_Xml += " Version=\"查看公文在已流转过程中表单内容修改的版本信息\" />";
		ls_Xml += "<Roles /><Mobile /></Extend>";

		mo_ExtendXml = MXmlHandle.LoadXml(ls_Xml);
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 注销所属的流程
		this.Flow = null;

		// 包含的步骤处理子对象，当步骤类型为开始或处理步骤是才有
		if (this.Transact != null) {
			this.Transact.ClearUp();
			this.Transact = null;
		}

		// 包含的条件对象，当步骤类型为开始时可以没有，其他步骤都必须有，并且为逻辑处理条件
		if (this.Condition != null) {
			this.Condition.ClearUp();
			this.Condition = null;
		}

		// 包含的步骤嵌入子对象，当步骤类型为嵌入步骤是才有
		if (this.TumbleIn != null) {
			this.TumbleIn.ClearUp();
			this.TumbleIn = null;
		}

		// 包含的步骤启动子对象，当步骤类型为启动步骤是才有
		if (this.Launch != null) {
			this.Launch.ClearUp();
			this.Launch = null;
		}

		// 包含的步骤通知子对象，当步骤类型为通知步骤是才有
		if (this.FYI != null) {
			this.FYI.ClearUp();
			this.FYI = null;
		}

		// 包含的步骤参与人，当步骤类型为开始时，步骤处理人为：[<拟稿人>]
		// 当步骤为结束或分支步骤时没有该对象
		if (this.Participant != null) {
			this.Participant.ClearUp();
			this.Participant = null;
		}

		super.ClearUp();
	}

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return 返回空表示可用，否则不可用
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			String ls_ErrorMsg = "";
			// 复杂流转必须存在触发条件
			if (!((this.Flow.Cyclostyle.FlowFlag & EWorkItemFlowFlag.FreeFlowRule) == EWorkItemFlowFlag.FreeFlowRule || (this.Flow.Cyclostyle.FlowFlag & EWorkItemFlowFlag.OrderFlowRule) == EWorkItemFlowFlag.OrderFlowRule)) {
				if (this.Condition == null) {
					if (this.ActivityType != EActivityType.StartActivity) {
						ls_ErrorMsg += MGlobal.addSpace(ai_SpaceLength + 4)
								+ "步骤缺少触发条件" + "\n";
					}
				} else {
					if (ExistOneCondition(this.Condition,
							EConditionType.ActivityCondition) == false) {
						ls_ErrorMsg += MGlobal.addSpace(ai_SpaceLength + 4)
								+ "步骤缺少步骤类型条件" + "\n";
					}
					ls_ErrorMsg += this.Condition.IsValid(ai_SpaceLength + 4);
				}
			}

			if (this.Transact != null)
				ls_ErrorMsg += this.Transact.IsValid(ai_SpaceLength + 4);

			if (this.TumbleIn != null)
				ls_ErrorMsg += this.TumbleIn.IsValid(ai_SpaceLength + 4);

			if (this.Launch != null)
				ls_ErrorMsg += this.Launch.IsValid(ai_SpaceLength + 4);

			if (this.FYI != null)
				ls_ErrorMsg += this.FYI.IsValid(ai_SpaceLength + 4);

			if (this.Participant != null)
				ls_ErrorMsg += this.Participant.IsValid(ai_SpaceLength + 4);

			if (ls_ErrorMsg.equals(""))
				return ls_ErrorMsg;

			return "步骤【" + this.ActivityName + "】有错误：\n" + ls_ErrorMsg;
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
			return e.toString();
		}
	}

	/**
	 * 判断是否存在某种类型的条件
	 * 
	 * @param ao_Condition
	 * @param ai_Type
	 * @return
	 * @throws Exception
	 */
	private boolean ExistOneCondition(CCondition ao_Condition, int ai_Type)
			throws Exception {
		try {
			if (ao_Condition == null)
				return false;
			if (ao_Condition.ConditionType == ai_Type)
				return true;

			if (ao_Condition.ConditionType == EConditionType.LogicAndCondition
					| ao_Condition.ConditionType == EConditionType.LogicOrCondition) {
				for (CCondition lo_Cond : ao_Condition.ChildConditions) {
					if (ExistOneCondition(lo_Cond, ai_Type))
						return true;
				}
			}

		} catch (Exception e) {
			this.Error().Raise(e, this, "ExistOneCondition", null);
		}
		return false;
	}

	/**
	 * 移动步骤
	 * 
	 * @param al_Left
	 *            左边距
	 * @param al_Top
	 *            上边距
	 * @param al_Width
	 *            宽度
	 * @param al_Height
	 *            高度
	 * @return
	 */
	public void activityMove(int al_Left, int al_Top, int al_Width,
			int al_Height) {
		if (al_Left >= 0) {
			this.Left = al_Left;
		}
		if (al_Top >= 0) {
			this.Top = al_Top;
		}
		if (al_Width > 0) {
			this.Width = al_Width;
		}
		if (al_Height > 0) {
			this.Height = al_Height;
		}

		// if (MoveEvent != null)
		// MoveEvent(this.Left, this.Top, this.Width, this.Height);
	}

	/**
	 * 增加条件
	 * 
	 * @param ao_SourceActivity
	 *            条件源步骤对象
	 * @return
	 * @throws Exception
	 */
	public CCondition insertCondition(CActivity ao_SourceActivity)
			throws Exception {
		try {
			if (ao_SourceActivity != null) {
				if (ao_SourceActivity.ActivityType == EActivityType.NotAnyActivity
						|| ao_SourceActivity.ActivityType == EActivityType.FYIActivity
						|| ao_SourceActivity.ActivityType == EActivityType.StopActivity) {
					// 错误：未知步骤、通知或结束类型的步骤不能作为条件的源步骤
					return null;
				}
			}

			// 插入缺省条件
			if (this.Condition == null) {
				this.Condition = new CCondition();
				this.Condition.Activity = this;
				this.Flow.ConditionMaxID = this.Flow.ConditionMaxID + 1;
				this.Condition.ConditionID = this.Flow.ConditionMaxID;
				this.Condition
						.setConditionType(EConditionType.LogicOrCondition);
			}

			if (ao_SourceActivity == null) {
				return this.Condition.InsertChildCondition(
						EConditionType.NotAnyCondition, null, null);
			} else {
				return this.Condition.InsertChildCondition(
						EConditionType.ActivityCondition, ao_SourceActivity,
						null);
			}
		} catch (Exception e) {
			this.Error().Raise(e, this, "InsertCondition",
					ao_SourceActivity.ActivityName);
			return null;
		}
	}

	/**
	 * 通过连线删除所有对应的条件
	 * 
	 * @param ao_Line
	 * @return
	 * @throws Exception
	 */
	public boolean deleteConditionByLine(CFlowLine ao_Line) throws Exception {
		try {
			if (this.Condition == null)
				return true;
			return this.Condition.DeleteByLine(ao_Line);
		} catch (Exception e) {
			this.Raise(e, "DeleteConditionByLine()", "ao_Line.getLineID()");
			return false;
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
		if (ai_Type == 1)
			ImportB(ao_Node);
		else
			ImportA(ao_Node);
	}

	/**
	 * 导入XML（正常）
	 */
	private void ImportA(Element ao_Node) {
		this.ActivityID = Integer.parseInt(ao_Node.getAttribute("ActivityID"));
		this.ActivityName = ao_Node.getAttribute("ActivityName");
		this.ActivityType = Integer.parseInt(ao_Node
				.getAttribute("ActivityType"));
		this.Left = Integer.parseInt(ao_Node.getAttribute("Left"));
		this.Top = Integer.parseInt(ao_Node.getAttribute("Top"));
		this.Width = Integer.parseInt(ao_Node.getAttribute("Width"));
		this.Height = Integer.parseInt(ao_Node.getAttribute("Height"));
		this.Summary = ao_Node.getAttribute("Summary");
		this.OrderID = Integer.parseInt(ao_Node.getAttribute("OrderID"));
		this.ImageID = Integer.parseInt(ao_Node.getAttribute("ImageID"));
		this.RemindAddFlag = Integer.parseInt(ao_Node
				.getAttribute("RemindAddFlag"));

		Element lo_Node = null;

		// 所包含的步骤处理子对象，当步骤类型为开始或处理步骤是才有
		if (ao_Node.getAttribute("Transact").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Transact");
			this.Transact = new CTransact(this.Logon);
			this.Transact.Activity = this;
			this.Transact.Import(lo_Node, (int) 0);
		}

		// 所包含的条件对象，当步骤类型为开始时可以没有，其他步骤都必须有，并且为逻辑处理条件
		if (ao_Node.getAttribute("Condition").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Condition");
			this.Condition = new CCondition(this.Logon);
			this.Condition.Activity = this;
			this.Condition.Import(lo_Node, (int) 0);
		}

		// 所包含的步骤嵌入子对象，当步骤类型为嵌入步骤是才有
		if (ao_Node.getAttribute("TumbleIn").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "TumbleIn");
			this.TumbleIn = new CTumbleIn(this.Logon);
			this.TumbleIn.Activity = this;
			this.TumbleIn.Import(lo_Node, (int) 0);
		}

		// 所包含的步骤启动子对象，当步骤类型为启动步骤是才有
		if (ao_Node.getAttribute("Launch").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Launch");
			this.Launch = new CLaunch(this.Logon);
			this.Launch.Activity = this;
			this.Launch.Import(lo_Node, 0);
		}

		// 所包含的步骤通知子对象，当步骤类型为通知步骤是才有
		if (ao_Node.getAttribute("FYI").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "FYI");
			this.FYI = new CFYI(this.Logon);
			this.FYI.Activity = this;
			this.FYI.Import(lo_Node, 0);
		}

		// 所包含的步骤参与人，当步骤类型为开始时，步骤处理人为“[<拟稿人>]”
		// 当步骤为结束或分支步骤时没有该对象
		if (ao_Node.getAttribute("Participant").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Participant");
			this.Participant = new CParticipant(this.Logon);
			this.Participant.Activity = this;
			this.Participant.Import(lo_Node, 0);
		}

		// 扩展信息
		this.setExtendXml(ao_Node.getAttribute("ExtendXml"));
	}

	/**
	 * 导入XML（短属性）
	 */
	private void ImportB(Element ao_Node) {
		this.Left = Integer.parseInt(ao_Node.getAttribute("Left"));
		this.Top = Integer.parseInt(ao_Node.getAttribute("Top"));
		this.Width = Integer.parseInt(ao_Node.getAttribute("Width"));
		this.Height = Integer.parseInt(ao_Node.getAttribute("Height"));
		if (StringUtils.isEmpty(ao_Node.getAttribute("RAddFlag")))
			this.RemindAddFlag = 0;
		else
			this.RemindAddFlag = Integer.parseInt(ao_Node
					.getAttribute("RAddFlag"));

		Element lo_Node = null;

		// 所包含的步骤处理子对象，当步骤类型为开始或处理步骤是才有
		if (ao_Node.getAttribute("Transact").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Transact");
			this.Transact = new CTransact(this.Logon);
			this.Transact.Activity = this;
			this.Transact.Import(lo_Node, (int) 1);
		}

		// 所包含的条件对象，当步骤类型为开始时可以没有，其他步骤都必须有，并且为逻辑处理条件
		if (ao_Node.getAttribute("Condition").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Condition");
			this.Condition = new CCondition(this.Logon);
			this.Condition.Activity = this;
			this.Condition.Import(lo_Node, (int) 1);
		}

		// 所包含的步骤嵌入子对象，当步骤类型为嵌入步骤是才有
		if (ao_Node.getAttribute("TumbleIn").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "TumbleIn");
			this.TumbleIn = new CTumbleIn(this.Logon);
			this.TumbleIn.Activity = this;
			this.TumbleIn.Import(lo_Node, (int) 1);
		}

		// 所包含的步骤启动子对象，当步骤类型为启动步骤是才有
		if (ao_Node.getAttribute("Launch").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Launch");
			this.Launch = new CLaunch(this.Logon);
			this.Launch.Activity = this;
			this.Launch.Import(lo_Node, 1);
		}

		// 所包含的步骤通知子对象，当步骤类型为通知步骤是才有
		if (ao_Node.getAttribute("FYI").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "FYI");
			this.FYI = new CFYI(this.Logon);
			this.FYI.Activity = this;
			this.FYI.Import(lo_Node, (int) 1);
		}

		// 所包含的步骤参与人，当步骤类型为开始时，步骤处理人为“[<拟稿人>]”
		// 当步骤为结束或分支步骤时没有该对象
		if (ao_Node.getAttribute("Participant").equals("1")) {
			lo_Node = MXmlHandle.getNodeByName(ao_Node, "Participant");
			this.Participant = new CParticipant(this.Logon);
			this.Participant.Activity = this;
			this.Participant.Import(lo_Node, (int) 1);
		}

		// 扩展信息
		lo_Node = MXmlHandle.getNodeByName(ao_Node, "Extend");
		if (lo_Node == null) {
			this.initExtendXml();
		} else {
			this.setExtendXml(lo_Node.getTextContent());
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
		if (ai_Type == 1)
			ExportB(ao_Node);
		else
			ExportA(ao_Node);
	}

	/**
	 * 导出XML（正常）
	 * 
	 * @throws Exception
	 */
	private void ExportA(Element ao_Node) throws Exception {
		ao_Node.setAttribute("ActivityID", String.valueOf(this.ActivityID));
		ao_Node.setAttribute("ActivityName", this.ActivityName);
		ao_Node.setAttribute("ActivityType", String.valueOf(this.ActivityType));
		ao_Node.setAttribute("Left", String.valueOf(this.Left));
		ao_Node.setAttribute("Top", String.valueOf(this.Top));
		ao_Node.setAttribute("Width", String.valueOf(this.Width));
		ao_Node.setAttribute("Height", String.valueOf(this.Height));
		ao_Node.setAttribute("Summary", this.Summary);
		ao_Node.setAttribute("OrderID", String.valueOf(this.OrderID));
		ao_Node.setAttribute("ImageID", String.valueOf(this.ImageID));
		ao_Node.setAttribute("RemindAddFlag",
				String.valueOf(this.RemindAddFlag));

		Element lo_Node = null;
		// 所包含的步骤处理子对象，当步骤类型为开始或处理步骤是才有
		if (this.Transact == null) {
			ao_Node.setAttribute("Transact", "0");
		} else {
			ao_Node.setAttribute("Transact", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("Transact");
			this.Transact.Export(lo_Node, (int) 0);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的条件对象，当步骤类型为开始时可以没有，其他步骤都必须有，并且为逻辑处理条件
		if (this.Condition == null) {
			ao_Node.setAttribute("Condition", "0");
		} else {
			ao_Node.setAttribute("Condition", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("Condition");
			this.Condition.Export(lo_Node, (int) 0);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的步骤嵌入子对象，当步骤类型为嵌入步骤是才有
		if (this.TumbleIn == null) {
			ao_Node.setAttribute("TumbleIn", "0");
		} else {
			ao_Node.setAttribute("TumbleIn", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("TumbleIn");
			this.TumbleIn.Export(lo_Node, (int) 0);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的步骤启动子对象，当步骤类型为启动步骤是才有
		if (this.Launch == null) {
			ao_Node.setAttribute("Launch", "0");
		} else {
			ao_Node.setAttribute("Launch", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("Launch");
			this.Launch.Export(lo_Node, 0);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的步骤通知子对象，当步骤类型为通知步骤是才有
		if (this.FYI == null) {
			ao_Node.setAttribute("FYI", "0");
		} else {
			ao_Node.setAttribute("FYI", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("FYI");
			this.FYI.Export(lo_Node, (int) 0);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的步骤参与人，当步骤类型为开始时，步骤处理人为“[<拟稿人>]”
		// 当步骤为结束或分支步骤时没有该对象
		if (this.Participant == null) {
			ao_Node.setAttribute("Participant", "0");
		} else {
			ao_Node.setAttribute("Participant", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("Participant");
			this.Participant.Export(lo_Node, (int) 0);
			ao_Node.appendChild(lo_Node);
		}

		// 扩展信息-需要调试修改
		lo_Node = ao_Node.getOwnerDocument().createElement("Extend");
		ao_Node.appendChild(lo_Node);
		lo_Node.setTextContent(mo_ExtendXml.getDocumentElement()
				.getTextContent());
	}

	/**
	 * 导出XML（短属性）
	 * 
	 * @throws Exception
	 */
	private void ExportB(Element ao_Node) throws Exception {
		ao_Node.setAttribute("Left", String.valueOf(this.Left));
		ao_Node.setAttribute("Top", String.valueOf(this.Top));
		ao_Node.setAttribute("Width", String.valueOf(this.Width));
		ao_Node.setAttribute("Height", String.valueOf(this.Height));
		ao_Node.setAttribute("RAddFlag", String.valueOf(this.RemindAddFlag));

		Element lo_Node = null;
		// 所包含的步骤处理子对象，当步骤类型为开始或处理步骤是才有
		if (this.Transact == null) {
			ao_Node.setAttribute("Transact", "0");
		} else {
			ao_Node.setAttribute("Transact", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("Transact");
			this.Transact.Export(lo_Node, (int) 1);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的条件对象，当步骤类型为开始时可以没有，其他步骤都必须有，并且为逻辑处理条件
		if (this.Condition == null) {
			ao_Node.setAttribute("Condition", "0");
		} else {
			ao_Node.setAttribute("Condition", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("Condition");
			this.Condition.Export(lo_Node, (int) 1);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的步骤嵌入子对象，当步骤类型为嵌入步骤是才有
		if (this.TumbleIn == null) {
			ao_Node.setAttribute("TumbleIn", "0");
		} else {
			ao_Node.setAttribute("TumbleIn", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("TumbleIn");
			this.TumbleIn.Export(lo_Node, (int) 1);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的步骤启动子对象，当步骤类型为启动步骤是才有
		if (this.Launch == null) {
			ao_Node.setAttribute("Launch", "0");
		} else {
			ao_Node.setAttribute("Launch", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("Launch");
			this.Launch.Export(lo_Node, 1);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的步骤通知子对象，当步骤类型为通知步骤是才有
		if (this.FYI == null) {
			ao_Node.setAttribute("FYI", "0");
		} else {
			ao_Node.setAttribute("FYI", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("FYI");
			this.FYI.Export(lo_Node, (int) 1);
			ao_Node.appendChild(lo_Node);
		}

		// 所包含的步骤参与人，当步骤类型为开始时，步骤处理人为“[<拟稿人>]”
		// 当步骤为结束或分支步骤时没有该对象
		if (this.Participant == null) {
			ao_Node.setAttribute("Participant", "0");
		} else {
			ao_Node.setAttribute("Participant", "1");
			lo_Node = ao_Node.getOwnerDocument().createElement("Participant");
			this.Participant.Export(lo_Node, (int) 1);
			ao_Node.appendChild(lo_Node);
		}

		// 扩展信息-需要调试修改
		ao_Node.setAttribute("ExtendXML", this.getExtendXml());
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
		ao_Set.put("FlowID", this.Flow.FlowID);
		ao_Set.put("ActivityID", this.ActivityID);
		ao_Set.put("ActivityName", this.ActivityName);
		ao_Set.put("ActivityType", this.ActivityType);
		ao_Set.put("ActivityOrder", this.OrderID);
		ao_Set.put("ActivityDescribe", MGlobal.getDbValue(this.Summary));
		ao_Set.put("ImageID", this.ImageID);

		if (this.Flow.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				ao_Set.put("ActivityContent", "AC" + this.WriteContent(ai_Type));
			} else {
				ao_Set.put("ActivityContent", this.WriteContent(ai_Type));
			}
		} else {
			ao_Set.put("ActivityContent", this.ExportToXml("Activity", 1));
		}
	}

	/**
	 * 保存获取保存数据库执行状态对象
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
			boolean ab_Inst) throws SQLException {
		String ls_Sql = null;
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO ActivityInst "
						+ "(WorkItemID, FlowID, ActivityID, ActivityName, ActivityType, ActivityOrder, "
						+ "ActivityContent, ActivityDescribe, ImageID) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE ActivityInst SET "
						+ "ActivityName = ?, ActivityType = ?, ActivityOrder = ?, "
						+ "ActivityContent = ?, ActivityDescribe = ?, ImageID = ? "
						+ "WHERE WorkItemID = ? AND FlowID = ? AND ActivityID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO FlowActivity "
						+ "(FlowID, ActivityID, ActivityName, ActivityType, ActivityOrder, "
						+ "ActivityContent, ActivityDescribe, ImageID) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE FlowActivity SET "
						+ "ActivityName = ?, ActivityType = ?, ActivityOrder = ?, ActivityContent = ?, "
						+ "ActivityDescribe = ?, ImageID = ? WHERE FlowID = ? AND ActivityID = ?";
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
	 *            存储类型：=0-XmlType；=1-OrignType；
	 * @param ab_Inst
	 *            是否实例
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws SQLException
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type,
			boolean ab_Inst, int ai_Update) throws Exception {
		List parasList = new ArrayList();
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				parasList.add(this.Flow.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.Flow.FlowID);
				parasList.add(this.ActivityID);
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(this.Flow.FlowID);
				parasList.add(this.ActivityID);
			}
		}

		parasList.add(this.ActivityName);
		parasList.add(this.ActivityType);
		parasList.add(this.OrderID);

		if (this.Flow.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				parasList.add("AC" + this.WriteContent(ai_Type));
			} else {
				parasList.add(this.WriteContent(ai_Type));
			}
		} else {
			parasList.add(this.ExportToXml("Activity", ai_Type));
		}

		parasList.add(MGlobal.getDbValue(this.Summary));
		parasList.add(this.ImageID);

		if (ab_Inst) {
			if (ai_SaveType == 1) {
				parasList.add(this.Flow.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.Flow.FlowID);
				parasList.add(this.ActivityID);
			}
		} else {
			if (ai_SaveType == 1) {
				parasList.add(this.Flow.FlowID);
				parasList.add(this.ActivityID);
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
	public void Open(Map<String, Object> ao_Set, int ai_Type)
			throws Exception {
		// entry.Flow.setFlowID(ao_Set.getInt("FlowID"));
		this.ActivityID = (Integer) ao_Set.get("ActivityID");
		this.ActivityName = (String) ao_Set.get("ActivityName");
		this.ActivityType = (Short) ao_Set.get("ActivityType");
		this.OrderID = (Integer) ao_Set.get("ActivityOrder");
		this.Summary = (String) ao_Set.get("ActivityDescribe");
		this.ImageID = (Integer) ao_Set.get("ImageID");
		String ls_Content = (String) ao_Set.get("ActivityContent");
		if (this.Flow.Cyclostyle.OpenType == EDataHandleType.OrignType) {
			if (ls_Content.substring(0, 2).equals("AC")) {
				this.ReadContent(ls_Content.substring(2, ls_Content.length()),
						1);
			} else {
				this.ReadContent(ls_Content, 0);
			}
		} else {
			this.ImportFormXml(ls_Content, 1);
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
		// 位置数据
		if (ai_Type == 1) {
			ao_Bag.Write("AL", this.Left);
			ao_Bag.Write("AT", this.Top);
			ao_Bag.Write("AW", this.Width);
			ao_Bag.Write("AH", this.Height);
			ao_Bag.Write("RAF", this.RemindAddFlag);
		} else {
			ao_Bag.Write("ml_Left", this.Left);
			ao_Bag.Write("ml_Top", this.Top);
			ao_Bag.Write("ml_Width", this.Width);
			ao_Bag.Write("ml_Height", this.Height);
			ao_Bag.Write("mi_RemindAddFlag", this.RemindAddFlag);
		}

		// 所包含的步骤处理子对象，当步骤类型为开始或处理步骤是才有
		if (this.Transact == null) {
			ao_Bag.Write("theTransact", false);
		} else {
			ao_Bag.Write("theTransact", true);
			this.Transact.Write(ao_Bag, ai_Type);
		}

		// 所包含的条件对象，当步骤类型为开始时可以没有，其他步骤都必须有，并且为逻辑处理条件
		if (this.Condition == null) {
			ao_Bag.Write("theCondition", false);
		} else {
			ao_Bag.Write("theCondition", true);
			this.Condition.Write(ao_Bag, ai_Type);
		}

		// 所包含的步骤嵌入子对象，当步骤类型为嵌入步骤是才有
		if (this.TumbleIn == null) {
			ao_Bag.Write("theTumbleIn", false);
		} else {
			ao_Bag.Write("theTumbleIn", true);
			this.TumbleIn.Write(ao_Bag, ai_Type);
		}

		// 所包含的步骤启动子对象，当步骤类型为启动步骤是才有
		if (this.Launch == null) {
			ao_Bag.Write("theLaunch", false);
		} else {
			ao_Bag.Write("theLaunch", true);
			this.Launch.Write(ao_Bag, ai_Type);
		}

		// 所包含的步骤通知子对象，当步骤类型为通知步骤是才有
		if (this.FYI == null) {
			ao_Bag.Write("theFYI", false);
		} else {
			ao_Bag.Write("theFYI", true);
			this.FYI.Write(ao_Bag, ai_Type);
		}

		// 所包含的步骤参与人，当步骤类型为开始时，步骤处理人为“[<拟稿人>]”
		// 当步骤为结束或分支步骤时没有该对象
		if (this.Participant == null) {
			ao_Bag.Write("theParticipant", false);
		} else {
			ao_Bag.Write("theParticipant", true);
			this.Participant.Write(ao_Bag, ai_Type);
		}

		// 扩展信息
		ao_Bag.Write((ai_Type == 1 ? "XML" : "ExtendXML"), this.getExtendXml());
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
		// 位置数据
		if (ai_Type == 1) {
			this.Left = ao_Bag.ReadInt("AL");
			this.Top = ao_Bag.ReadInt("AT");
			this.Width = ao_Bag.ReadInt("AW");
			this.Height = ao_Bag.ReadInt("AH");
			this.RemindAddFlag = ao_Bag.ReadInt("RAF");
		} else {
			this.Left = ao_Bag.ReadInt("ml_Left");
			this.Top = ao_Bag.ReadInt("ml_Top");
			this.Width = ao_Bag.ReadInt("ml_Width");
			this.Height = ao_Bag.ReadInt("ml_Height");
			this.RemindAddFlag = ao_Bag.ReadInt("mi_RemindAddFlag");
		}

		// 所包含的步骤处理子对象，当步骤类型为开始或处理步骤是才有
		Boolean lb_Value = (Boolean) ao_Bag.Read("theTransact");
		if (lb_Value) {
			this.Transact = new CTransact(this.Logon);
			this.Transact.Activity = this;
			this.Transact.Read(ao_Bag, ai_Type);
		}

		// 所包含的条件对象，当步骤类型为开始时可以没有，其他步骤都必须有，并且为逻辑处理条件
		lb_Value = (Boolean) ao_Bag.Read("theCondition");
		if (lb_Value) {
			this.Condition = new CCondition(this.Logon);
			this.Condition.Activity = this;
			this.Condition.Read(ao_Bag, ai_Type);
			if (this.Flow.ConditionMaxID < this.Condition.ConditionID)
				this.Flow.ConditionMaxID = this.Condition.ConditionID;
		}

		// 所包含的步骤嵌入子对象，当步骤类型为嵌入步骤是才有
		lb_Value = (Boolean) ao_Bag.Read("theTumbleIn");
		if (lb_Value) {
			this.TumbleIn = new CTumbleIn(this.Logon);
			this.TumbleIn.Activity = this;
			this.TumbleIn.Read(ao_Bag, ai_Type);
		}

		// 所包含的步骤启动子对象，当步骤类型为启动步骤是才有
		lb_Value = (Boolean) ao_Bag.Read("theLaunch");
		if (lb_Value) {
			this.Launch = new CLaunch(this.Logon);
			this.Launch.Activity = this;
			this.Launch.Read(ao_Bag, ai_Type);
		}

		// 所包含的步骤通知子对象，当步骤类型为通知步骤是才有
		lb_Value = (Boolean) ao_Bag.Read("theFYI");
		if (lb_Value) {
			this.FYI = new CFYI(this.Logon);
			this.FYI.Activity = this;
			this.FYI.Read(ao_Bag, ai_Type);
		}

		// 所包含的步骤参与人，当步骤类型为开始时，步骤处理人为“[<拟稿人>]”
		// 当步骤为结束或分支步骤时没有该对象
		lb_Value = (Boolean) ao_Bag.Read("theParticipant");
		if (lb_Value) {
			this.Participant = new CParticipant(this.Logon);
			this.Participant.Activity = this;
			this.Participant.Read(ao_Bag, ai_Type);
		}

		// 扩展信息
		String ls_Value = (String) ao_Bag.Read((ai_Type == 1 ? "XML"
				: "ExtendXML"));
		if (ls_Value != "")
			this.setExtendXml(ls_Value);
	}

	/**
	 * 获取某一个步骤被触发过的次数
	 * 
	 * @return
	 */
	public int getSpringNumber() {
		if (this.Flow.Cyclostyle.WorkItem == null)
			return -1;

		int li_Number = 0;
		for (CEntry lo_Entry : this.Flow.Cyclostyle.WorkItem.getEntries()) {
			if (lo_Entry.EntryType == EEntryType.VirtualEntry) {
				if (lo_Entry.FlowID == this.Flow.FlowID
						&& lo_Entry.ActivityID == this.ActivityID)
					li_Number++;
			}
		}

		return li_Number;
	}

	/**
	 * 获取某一个步骤最近一次的收件人
	 * 
	 * @return
	 */
	public String getLastParticipants() {
		if (this.Flow.Cyclostyle.WorkItem == null)
			return "";

		CEntry lo_LastEntry = null;

		for (CEntry lo_Entry : this.Flow.Cyclostyle.WorkItem.getEntries()) {
			if (lo_Entry.EntryType == EEntryType.VirtualEntry) {
				if (lo_Entry.FlowID == this.Flow.FlowID
						&& lo_Entry.ActivityID == this.ActivityID) {
					if (lo_LastEntry == null) {
						lo_LastEntry = lo_Entry;
					} else {
						if (lo_LastEntry.EntryID < lo_Entry.EntryID)
							lo_LastEntry = lo_Entry;
					}
				}
			}
		}

		if (lo_LastEntry == null)
			return "";

		String ls_Value = "";
		String ls_Str;
		for (CEntry lo_Entry : lo_LastEntry.ChildEntries) {
			if (lo_Entry.ProxyParticipantID == 0) {
				if (lo_Entry.PostParticipantID == 0) {
					ls_Str = "U" + String.valueOf(lo_Entry.ParticipantID) + ";";
				} else {
					ls_Str = "U" + String.valueOf(lo_Entry.PostParticipantID)
							+ ";";
				}
			} else {
				ls_Str = "U" + String.valueOf(lo_Entry.ProxyParticipantID)
						+ ";";
			}
			if (ls_Value.indexOf(ls_Str) == -1)
				ls_Value += ls_Str;
		}

		return ls_Value;
	}

	/**
	 * 解析步骤收件人
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String resolveParticipant() throws SQLException {
		if (this.Participant == null)
			return "";

		if (this.Participant.Participant.equals(""))
			return "";

		int ll_CreatorID = this.Flow.Cyclostyle.WorkItem.CreatorID; // 拟稿人用户标识
		int ll_CurPartID = this.Flow.Cyclostyle.WorkItem.CurEntry.ParticipantID; // 当前收件人用户标识

		String ls_StrOne = "";
		int ll_DeptID = 0;
		int ll_PosID = 0;

		String ls_Users = ""; // 固定收件人<用户>
		String ls_Participants = this.Participant.Participant; // 解析步骤参与人，格式如返回格式：用户名称[用户标识]
		String[] ls_PartEvery = ls_Participants.split(";"); // 步骤参与人[未解析]

		for (int i = 0; i < ls_PartEvery.length; i++) {
			if (MGlobal.isEmpty(ls_PartEvery[i]))
				break;

			ls_StrOne = MGlobal.right(ls_PartEvery[i],
					ls_PartEvery[i].length() - 1);
			String ls_Sign = MGlobal.left(ls_PartEvery[i], 1);
			if (ls_Sign.equals("U")) {
				ls_Users += ls_StrOne + ",";
			} else if (ls_Sign.equals("G")) {
				// 解析并替换固定收件人<分组>
				ls_Participants = ls_Participants.replaceAll(ls_PartEvery[i]
						+ ";", getGroupUsers(Integer.parseInt(ls_StrOne)));
			} else if (ls_Sign.equals("@")) {
				// 解析并替换用户角色
				ls_Participants = ls_Participants.replaceAll(ls_PartEvery[i]
						+ ";", getRoleUsers(Integer.parseInt(ls_StrOne)));
			} else if (ls_Sign.equals("&")) {
				// 解析并替换系统角色<拟稿人的>
				ls_Participants = ls_Participants.replaceAll(ls_PartEvery[i]
						+ ";", TUserAdmin.getSystemRoleUsers(this.Logon,
						ll_CreatorID, Integer.parseInt(ls_StrOne), "&", 1));
			} else if (ls_Sign.equals("#")) {
				// 解析并替换系统角色<当前收件人的>
				ls_Participants = ls_Participants.replaceAll(ls_PartEvery[i]
						+ ";", TUserAdmin.getSystemRoleUsers(this.Logon,
						ll_CurPartID, Integer.parseInt(ls_StrOne), "#", 1));
			} else if (ls_Sign.equals("%")) {
				// 解析并替换部门角色<部门“***”的“****”>
				int j = ls_PartEvery[i].indexOf("%", 1) + 1;
				ll_DeptID = Integer.parseInt(ls_PartEvery[i]
						.substring(1, j - 1));
				ll_PosID = Integer.parseInt(ls_PartEvery[i].substring(j),
						ls_PartEvery[i].length());
				ls_Participants = ls_Participants.replaceAll(ls_PartEvery[i]
						+ ";", TUserAdmin.getDeptRoleUsers(this.Logon,
						ll_DeptID, ll_PosID, 1, ""));
			} else {
			}
		}

		// 解析并替换固定用户
		if (ls_Users != "") {
			ls_Users = ls_Users.substring(0, ls_Users.length() - 1);
			List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate()
					.queryForList(
							"SELECT UserID, UserName FROM UserInfo WHERE UserID IN ("
									+ ls_Users + ")");
			for (int i = 0; i < lo_Set.size(); i++) {
				ls_Participants = ls_Participants.replaceAll("U"
						+ lo_Set.get(i).get("UserID") + ";",
						lo_Set.get(i).get("UserName") + "["
								+ lo_Set.get(i).get("UserID") + "];");
			}
			lo_Set = null;
		}
		if (MGlobal.isExist(ls_Participants)) {
			// 排除重复用户
			ls_PartEvery = ls_Participants.split(";");
			ls_Participants = ";";
			for (int i = 0; i < ls_PartEvery.length; i++) {
				if (MGlobal.isExist(ls_PartEvery[i])) {
					if (ls_Participants.indexOf(";" + ls_PartEvery[i] + ";") == -1) {
						ls_Participants += ls_PartEvery[i] + ";";
					}
				}
			}
			ls_Participants = MGlobal.right(ls_Participants,
					ls_Participants.length() - 1);
		}

		return ls_Participants;
	}

	// 解析分组用户
	// 返回格式：“用户名称[用户标识];...”
	private String getGroupUsers(int al_GroupID) throws SQLException {
		String ls_Value = "";
		List<Map<String, Object>> lo_Set = CSqlHandle
				.getJdbcTemplate()
				.queryForList(
						"SELECT UserID, UserName FROM UserInfo WHERE UserID IN "
								+ "(SELECT UserID FROM UserGroup WHERE GroupID = "
								+ Integer.toString(al_GroupID) + ")");
		for (int i = 0; i < lo_Set.size(); i++) {
			ls_Value += lo_Set.get(i).get("UserName") + "["
					+ lo_Set.get(i).get("UserID") + "];";
		}
		return ls_Value;
	}

	// 解析用户定义角色
	// 返回格式：“用户名称[用户标识]角色名称{@角色标识};...”
	private String getRoleUsers(int al_RoleID) throws SQLException {
		CRole lo_Role = this.Flow.Cyclostyle.getRoleById(al_RoleID);
		if (lo_Role == null)
			return "";

		int ll_CreatorID = this.Flow.Cyclostyle.WorkItem.CreatorID; // 拟稿人用户标识
		int ll_CurPartID = this.Flow.Cyclostyle.WorkItem.CurEntry.ParticipantID; // 当前收件人用户标识

		String ls_Users = ""; // 固定收件人<用户>
		String ls_StrOne = "";
		int ll_DeptID = 0; // 部门标识
		int ll_PosID = 0; // 职位标识

		String ls_Participants = lo_Role.Value; // 解析角色，格式如返回格式：用户名称[用户标识]
		String[] ls_PartEvery = ls_Participants.split(";"); // 角色人员[未解析]
		for (int i = 0; i < ls_PartEvery.length; i++) {
			if (MGlobal.isEmpty(ls_PartEvery[i]))
				break;

			ls_StrOne = MGlobal.right(ls_PartEvery[i],
					ls_PartEvery[i].length() - 1);
			String ls_Sign = ls_PartEvery[i].substring(0, 1);
			if (ls_Sign.equals("U") || ls_Sign.equals("G")) {
				// 解析并替换 固定收件<用户/分组>
				ls_Users += ls_StrOne + ",";
			} else if (ls_Sign.equals("@")) {
				// 解析并替换用户角色
				ls_Users += getRoleUsers(Integer.parseInt(ls_StrOne)) + ",";
			} else if (ls_Sign.equals("&")) {
				// 解析并替换系统角色<拟稿人的>
				ls_Users += TUserAdmin.getSystemRoleUsers(this.Logon,
						ll_CreatorID, Integer.parseInt(ls_StrOne), "&", 1)
						+ ",";
			} else if (ls_Sign.equals("#")) {
				// 解析并替换系统角色<当前收件人的>
				ls_Users += TUserAdmin.getSystemRoleUsers(this.Logon,
						ll_CurPartID, Integer.parseInt(ls_StrOne), "#", 1)
						+ ",";
			} else if (ls_Sign.equals("%")) {
				// 解析并替换部门角色<部门“***”的“****”>
				int j = ls_StrOne.indexOf("%") + 1;
				ll_DeptID = Integer.parseInt(ls_StrOne.substring(0, j - 1));
				ll_PosID = Integer.parseInt(MGlobal.right(ls_StrOne,
						ls_StrOne.length() - j));
				ls_StrOne = getUserIDs(TUserAdmin.getDeptRoleUsers(this.Logon,
						ll_DeptID, ll_PosID, (short) 1, ""));
				ls_Users += ls_StrOne;
			} else {
				ls_Users += ls_PartEvery[i] + ",";
			}
		}

		ls_Participants = "";
		// 解析并替换固定用户
		if (MGlobal.isExist(ls_Users)) {
			ls_Users = ls_Users.substring(0, ls_Users.length() - 1);
			List<Map<String, Object>> lo_Set = CSqlHandle
					.getJdbcTemplate()
					.queryForList(
							"SELECT UserID, UserName FROM UserInfo WHERE UserType IN (0, 2, 3, 4) AND (UserID IN ("
									+ ls_Users
									+ ") OR UserID IN (SELECT UserID FROM UserGroup WHERE GroupID IN ("
									+ ls_Users + ")))");
			for (int i = 0; i < lo_Set.size(); i++) {
				ls_Participants += lo_Set.get(i).get("UserName") + "["
						+ lo_Set.get(i).get("UserID") + "];";
			}
		}

		return ls_Participants;
	}

	/**
	 * 从完整的用户获取用户标识连接串，使用","分隔
	 * 
	 * @param as_UserValue
	 * @return
	 */
	public String getUserIDs(String as_UserValue) {
		if (as_UserValue.equals(""))
			return "";

		String[] ls_Array = as_UserValue.split(";");
		for (int i = 0; i < ls_Array.length; i++) {
			if (ls_Array[i] != "") {
				int j = ls_Array[i].indexOf("[") + 1;
				ls_Array[i] = ls_Array[i]
						.substring(j, ls_Array[i].length() - 1);
			}
		}
		return MGlobal.join(ls_Array, ",");
	}

}
