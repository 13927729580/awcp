package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.EActivityConditionType;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.emun.EConditionCompareType;
import org.szcloud.framework.workflow.core.emun.EConditionTumTrfType;
import org.szcloud.framework.workflow.core.emun.EConditionType;
import org.szcloud.framework.workflow.core.emun.ELineType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 条件实体类 条件对象：流程对象模型中的基本对象，它是联系流程对象模型中两个 步骤对象之间的关系，<br>
 * 同时它自身也可以是嵌套关系。在对象模型图中，它其实代表一个有向图的流向和连线。在流程的一个步骤<br>
 * 中存在有且只 有一个条件，而且该条件的类型为“且”或“或”，同时，该种条件只有是开始步骤的时候<br>
 * 可以不存在子条件，其余类型的步骤都需要有子条 件。
 * 
 * @author Jackie.Wang
 */
public class CCondition extends CBase {

	/**
	 * 初始化
	 */
	public CCondition() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public CCondition(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 删除连线的条件 <br>
	 * ao_Line——被删除的连线对象
	 * 
	 * @param ao_Line
	 * @return
	 * @throws Exception
	 */
	public boolean DeleteByLine(CFlowLine ao_Line) throws Exception {
		try {
			// 没有条件被删除
			if (ChildConditions == null) {
				return true;
			}
			if (ChildConditions.size() == 0) {
				return true;
			}

			// 删除子条件
			CCondition lo_Child = new CCondition();
			int i = 0;
			for (i = (int) ChildConditions.size(); i >= 1; i--) {
				lo_Child = ChildConditions.get(i);
				switch (lo_Child.ConditionType) {
				case EConditionType.ActivityCondition:
					if ((lo_Child.getGoalActivity() == ao_Line.GoalActivity)
							&& (lo_Child.Activity == ao_Line.SourceActivity)) {
						ChildConditions.remove(i);
						lo_Child.ClearUp();
						lo_Child = null;
					}
					break;
				case EConditionType.LogicAndCondition:
				case EConditionType.LogicOrCondition:
					if (lo_Child.DeleteByLine(ao_Line) == false) {
						return false;
					}
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			this.Raise(e, "DeleteByLine", null);
		}
		return false;
	}

	/**
	 * 所属的步骤，只有为逻辑辑条件类型时才可能有此对象
	 */
	public CActivity Activity = null;

	/**
	 * 获取源步骤
	 * 
	 * @return
	 */
	public CActivity getSourceActivity() {
		try {
			// #非步骤条件
			if (this.ConditionType != EConditionType.ActivityCondition) {
				this.SourceActivityID = -1;
				return null;
			}

			// #未赋源步骤
			if (this.SourceActivityID == -1)
				return null;

			// 取流程
			CFlow lo_Flow = null;
			if (this.Activity == null) {
				lo_Flow = this.ParentCondition.Activity.Flow;
			} else {
				lo_Flow = this.Activity.Flow;
			}

			for (CActivity lo_Activity : lo_Flow.Activities) {
				if (lo_Activity.ActivityID == this.SourceActivityID)
					return lo_Activity;
			}

			this.SourceActivityID = -1;
		} catch (Exception e) {
			this.SourceActivityID = -1;
		}
		return null;
	}

	/**
	 * 设置源步骤
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setSourceActivity(CActivity value) throws Exception {
		// #非步骤条件
		if (this.ConditionType != EConditionType.ActivityCondition)
			return;

		CFlow lo_Flow = this.ParentCondition.Activity.Flow;

		if (value == null) {
			this.SourceActivityID = -1;
			// #如果设置源步骤为空，则需要检查是否原来对应的连线还有条件绑定，如果没有，需要删除连线
			for (CFlowLine lo_Line : lo_Flow.FlowLines) {
				if (lo_Line.bindConditionNumber() == 0) {
					lo_Flow.FlowLines.remove(Integer.toString(lo_Line.LineID));
					lo_Line.ClearUp();
					lo_Line = null;
					break;
				}
			}
		} else {
			if (value.ActivityID == this.SourceActivityID) {
				return;
			}

			this.SourceActivityID = value.ActivityID;
			// #如果设置源步骤不为空，则需要检查是否原来对应的连线还有条件绑定，如果没有，需要删除连线
			// #对新加入的源步骤????连线，如没有须添加连线
			for (CFlowLine lo_Line : lo_Flow.FlowLines) {
				if (lo_Line.bindConditionNumber() == 0) {
					lo_Flow.FlowLines.remove(Integer.toString(lo_Line.LineID));
					lo_Line.ClearUp();
					lo_Line = null;
					break;
				}
			}

			// 判断是否新条件是否已存在连线
			for (CFlowLine lo_Line : lo_Flow.FlowLines) {
				if (lo_Line.GoalActivity == getGoalActivity()
						&& lo_Line.SourceActivity.ActivityID == this.SourceActivityID) {
					// 存在
					return;
				}
			}

			// 不存在需要添加
			CFlowLine lo_Line = new CFlowLine(this.Logon);
			lo_Line.LineID = lo_Flow.FlowLineMaxID + 1;
			lo_Flow.FlowLineMaxID = lo_Line.LineID;
			lo_Line.SourceActivity = value;
			// lo_Line.SourceActivity.Move += lo_Line.SourceActivity_Move;
			lo_Line.GoalActivity = getGoalActivity();
			// lo_Line.GoalActivity.Move += lo_Line.GoalActivity_Move;
			lo_Line.setLineType(ELineType.BeeLineType);
			lo_Flow.FlowLines.add(lo_Line);
		}
		return;
	}

	/**
	 * 读取目标步骤
	 * 
	 * @return
	 */
	public CActivity getGoalActivity() {
		if (this.Activity == null) {
			return this.ParentCondition.Activity;
		} else {
			return this.Activity;
		}
	}

	/**
	 * 所属的父条件，该对象与【Activity】是排斥存在
	 */
	public CCondition ParentCondition = null;

	/**
	 * 子条件对象集合，只有为逻辑条件类型时才有且必须有此对象
	 */
	public List<CCondition> ChildConditions = null;

	/**
	 * 根据对象标识获取子条件对象
	 * 
	 * @param al_Id
	 * @return
	 */
	public CCondition GetConditionByID(int al_Id) {
		for (CCondition lo_Cond : this.ChildConditions) {
			if (lo_Cond.ConditionID == al_Id)
				return lo_Cond;
		}
		return null;
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 条件标识
	 */
	public int ConditionID = -1;

	/**
	 * 条件类型（以下类型对条件的性质而言，是一个大的分类）
	 */
	public int ConditionType = EConditionType.NotAnyCondition;

	/**
	 * 条件类型
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setConditionType(int value) throws Exception {
		try {
			if (this.ConditionType == value) {
				return;
			}

			switch (this.ConditionType) {
			case EConditionType.NotAnyCondition: // #初次可更改条件类型
				this.ConditionType = value;
				if (this.ConditionType == EConditionType.LogicAndCondition
						| this.ConditionType == EConditionType.LogicOrCondition) {
					ChildConditions = new ArrayList<CCondition>();
					if (!(ParentCondition == null)) {
						Activity = ParentCondition.Activity;
					}
				} else {
					if (this.ConditionType == EConditionType.ActivityCondition
							& (!(ParentCondition == null))) {
						switch (ParentCondition.Activity.ActivityType) {
						case EActivityType.StartActivity:
						case EActivityType.FYIActivity:
						case EActivityType.TransactActivity:
							this.ActivityFNType = EActivityConditionType.AllFinished;
							this.ResponseChoice = "";
							break;
						case EActivityType.TumbleInActivity:
						case EActivityType.LaunchActivity:
							this.TumTrfType = EConditionTumTrfType.HaveSendType;
							break;
						default: // StopActivity
							break;
						}
					}
				}
				break;

			// #条件类型只有“且”和“或”类型才能被更改
			case EConditionType.LogicAndCondition:
				this.ConditionType = EConditionType.LogicOrCondition;
				break;
			case EConditionType.LogicOrCondition:
				this.ConditionType = EConditionType.LogicAndCondition;
				break;

			case EConditionType.OtherCondition: // #预留
				break;
			}
		} catch (Exception e) {
			this.Raise(e, "ConditionType", null);
		}
	}

	/**
	 * 步骤条件处理类型
	 */
	public int ActivityFNType = EActivityConditionType.AllFinished;

	/**
	 * 设置步骤条件处理类型
	 * 
	 * @param value
	 */
	public void setActivityFNType(int value) {
		if (this.ActivityFNType == value) {
			return;
		}
		CActivity lo_Activity = new CActivity();
		if (this.ConditionType == EConditionType.ActivityCondition) {
			lo_Activity = Activity;
			if (!(lo_Activity == null)) {
				if (lo_Activity.ActivityType == EActivityType.FYIActivity
						|| lo_Activity.ActivityType == EActivityType.TransactActivity) {
					this.ActivityFNType = value;
					if (this.ActivityFNType == EActivityConditionType.FinishedPercent
							|| this.ActivityFNType == EActivityConditionType.UnFinishedPercent) {
						if (this.CompareContent != null) {
							this.CompareContent = "50";
						}
						if (Integer.parseInt(this.CompareContent) < 1
								|| Integer.parseInt(this.CompareContent) > 99) {
							this.CompareContent = "50";
						}
					}
				}
			}
		}
	}

	/**
	 * 读取步骤条件处理类型名称
	 * 
	 * @return
	 */
	public String getActivityFNTypeName() {
		switch (this.ActivityFNType) {
		case EActivityConditionType.AllFinished:
			return "全部人员已完成";
		case EActivityConditionType.AtListOneFinished:
			return "至少一个人已完成";
		case EActivityConditionType.FinishedNumber:
			return "完成人数";
		case EActivityConditionType.FinishedPercent:
			return "完成百分比";
		case EActivityConditionType.UnFinishedNumber:
			return "未完成人数";
		case EActivityConditionType.UnFinishedPercent:
			return "未完成百分比";
		}
		return null;
	}

	/**
	 * 步骤处理的批示意见，如果为空，则表示所有处理情况
	 */
	public String ResponseChoice = null;

	/**
	 * 设置步骤处理的批示意见，如果为空，则表示所有处理情况
	 * 
	 * @param value
	 */
	public void setResponseChoice(String value) {
		if (this.ResponseChoice.equals(value))
			return;

		// #只有开始或处理步骤为源步骤的条件才有批示意见信息选项
		this.ResponseChoice = "";
		CActivity lo_Activity = new CActivity();
		if (this.ConditionType == EConditionType.ActivityCondition) {
			lo_Activity = Activity;
			if (!(lo_Activity == null)) {
				if (lo_Activity.ActivityType == EActivityType.StartActivity
						|| lo_Activity.ActivityType == EActivityType.TransactActivity) {
					if ((";" + lo_Activity.Transact.ResponseChoices)
							.indexOf(";" + value + ";") + 1 > 0) {
						this.ResponseChoice = value;
					}
				}
			}
		}
	}

	/**
	 * 条件比较符号
	 */
	public int CompareType = EConditionCompareType.EqualToType;

	/**
	 * 设置条件比较符号
	 * 
	 * @param value
	 */
	public void setCompareType(int value) {
		if (this.CompareType == value) {
			return;
		}
		// #只有在处理或通知步骤为源步骤的条件或控制属性条件才有比较符号
		CActivity lo_Activity = new CActivity();
		if (this.ConditionType == EConditionType.ActivityCondition) {
			lo_Activity = Activity;
			if (!(lo_Activity == null)) {
				if (lo_Activity.ActivityType == EActivityType.FYIActivity
						|| lo_Activity.ActivityType == EActivityType.TransactActivity) {
					this.CompareType = value;
				}
				if (lo_Activity.ActivityType == EActivityType.TumbleInActivity
						|| lo_Activity.ActivityType == EActivityType.LaunchActivity) {
					this.CompareType = value;
				}
			}
		}
		if (this.ConditionType == EConditionType.FlowPropCondition) {
			this.CompareType = value;
		}
	}

	/**
	 * 读取条件比较符号的名称
	 * 
	 * @return
	 */
	public String getCompareTypeName() {
		switch (this.CompareType) {
		case EConditionCompareType.EqualToType:
			return "0 - 等    于(=)";
		case EConditionCompareType.LargeType:
			return "1 - 大    于(>)";
		case EConditionCompareType.LargeEqualToType:
			return "2 - 大于等于(>=)";
		case EConditionCompareType.SmallType:
			return "3 - 小    于(<)";
		case EConditionCompareType.SmallEqualToType:
			return "4 - 小于等于(<=)";
		case EConditionCompareType.NotEqualToType:
			return "5 - 不 等 于(<>)";
		}
		return null;
	}

	/**
	 * 条件用于比较的内容
	 */
	public String CompareContent = null;

	/**
	 * 设置条件用于比较的内容
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setCompareContent(String value) throws Exception {
		try {
			if (this.CompareContent == value) {
				return;
			}
			// #只有在处理、嵌入、启动或通知步骤为源步骤的条件或控制属性条件才有比较符号
			CActivity lo_Activity = new CActivity();
			if (this.ConditionType == EConditionType.ActivityCondition) {
				lo_Activity = Activity;
				if (!(lo_Activity == null)) {
					if (lo_Activity.ActivityType == EActivityType.FYIActivity
							|| lo_Activity.ActivityType == EActivityType.TransactActivity) {
						// #比较的内容必须是数字
						if (!MGlobal.isNumber(value)) {
							return;
						}
						if (Integer.parseInt(value) >= 0) {
							if (this.ActivityFNType == EActivityConditionType.FinishedPercent
									|| this.ActivityFNType == EActivityConditionType.UnFinishedPercent) {
								if (Integer.parseInt(value) < 100) {
									this.CompareContent = Integer
											.toString(Integer.parseInt(value));
								}
							} else {
								this.CompareContent = Integer.toString(Integer
										.parseInt(value));
							}
						}
					}
					if (lo_Activity.ActivityType == EActivityType.TumbleInActivity
							|| lo_Activity.ActivityType == EActivityType.LaunchActivity) {
						// #比较的内容必须是数字
						if (!MGlobal.isNumber(value)) {
							return;
						}
						if (Integer.parseInt(value) >= 0) {
							this.CompareContent = Integer.toString(Integer
									.parseInt(value));
						}
					}
				}
			}
			if (this.ConditionType == EConditionType.FlowPropCondition) {
				this.CompareContent = value;
			}

			if (this.ConditionType == EConditionType.FlowPropCondition) {
				this.CompareContent = value;
			}
		} catch (Exception e) {
			this.Raise(e, "setCompareContent", null);
		}
	}

	/**
	 * 嵌入或启动处理情况
	 */
	public int TumTrfType = EConditionTumTrfType.HaveSendType;

	/**
	 * 设置嵌入或启动处理情况
	 * 
	 * @param value
	 */
	public void setTumTrfType(int value) {
		if (this.TumTrfType == value) {
			return;
		}
		// #只有在嵌入或启动步骤为源步骤的条件才有处理情况
		if (this.ConditionType == EConditionType.ActivityCondition) {
			if (this.Activity != null) {
				if (this.Activity.ActivityType == EActivityType.TumbleInActivity
						|| this.Activity.ActivityType == EActivityType.LaunchActivity) {
					this.TumTrfType = value;
				}
			}
		}
	}

	/**
	 * 取得对应绑定条件的表头属性
	 * 
	 * @return
	 */
	public CProp getBindProp() {
		if (ms_BindPropName.equals("{[<拟稿人岗位>]}")
				|| ms_BindPropName.equals("{[<当前处理人岗位>]}")) {
			return null;
		}

		CCyclostyle lo_Cyclostyle;

		if (this.Activity == null) {
			lo_Cyclostyle = this.ParentCondition.Activity.Flow.Cyclostyle;
		} else {
			lo_Cyclostyle = this.Activity.Flow.Cyclostyle;
		}

		if (lo_Cyclostyle == null)
			return null;

		for (CProp lo_Prop : lo_Cyclostyle.Props) {
			if (lo_Prop.PropName.equals(ms_BindPropName))
				return lo_Prop;
		}

		ms_BindPropName = "";
		return null;
	}

	/**
	 * 设置对应绑定条件的表头属性
	 * 
	 * @param value
	 */
	public void setBindProp(CProp value) {
		if (value == null) {
			ms_BindPropName = "";
		} else {
			ms_BindPropName = value.PropName;
		}
	}

	/**
	 * 设置嵌入或启动处理情况
	 */
	public int ConditionTumTrfType = EConditionTumTrfType.HaveSendType;

	/**
	 * 设置嵌入或启动处理情况
	 * 
	 * @param value
	 */
	public void setConditionTumTrfType(int value) {
		if (this.ConditionTumTrfType == value) {
			return;
		}
		// #只有在嵌入或启动步骤为源步骤的条件才有处理情况
		if (this.ConditionType == EConditionType.ActivityCondition) {
			CActivity lo_Activity = this.Activity;
			if (!(lo_Activity == null)) {
				if (lo_Activity.ActivityType == EActivityType.TumbleInActivity
						|| lo_Activity.ActivityType == EActivityType.LaunchActivity) {
					this.ConditionTumTrfType = value;
				}
			}
		}
	}

	/**
	 * 读取嵌入或启动处理情况名称
	 * 
	 * @return
	 */
	public String getConditionTumTrfTypeName() {
		switch (this.TumTrfType) {
		case EConditionTumTrfType.HaveSendType:
			return "已经发送";
		case EConditionTumTrfType.FinishedFlow:
			return "成功流转";
		case EConditionTumTrfType.UnFinishedFlow:
			return "未成功流转";
		default:
			return null;
		}
	}

	/**
	 * 源步骤标识：当条件为步骤条件类型时，条件的起源步骤标识
	 */
	public int SourceActivityID = -1;

	/**
	 * 目标步骤标识：当条件为步骤条件类型时，条件的目标步骤标识
	 */
	public int GoalActivityID = -1;

	/**
	 * 条件绑定的控制属性名程
	 */
	private String ms_BindPropName;

	/**
	 * 条件绑定的控制属性名程
	 * 
	 * @return
	 */
	public String getBindPropName() {
		if (ms_BindPropName.equals("{[<拟稿人岗位>]}")
				|| ms_BindPropName.equals("{[<当前处理人岗位>]}")) {
			return ms_BindPropName;
		}

		CCyclostyle lo_Cyclostyle;

		if (Activity == null) {
			lo_Cyclostyle = ParentCondition.Activity.Flow.Cyclostyle;
		} else {
			lo_Cyclostyle = Activity.Flow.Cyclostyle;
		}

		if (lo_Cyclostyle == null) {
			return "";
		}

		for (CProp lo_Prop : lo_Cyclostyle.Props) {
			if (lo_Prop.PropName.equals(ms_BindPropName)) {
				return lo_Prop.PropName;
			}
		}
		return "";
	}

	/**
	 * 条件绑定的控制属性名程
	 * 
	 * @param value
	 */
	public void setBindPropName(String value) {
		if (value == "{[<拟稿人岗位]}" || value == "{[<当前处理人岗位]}") {
			ms_BindPropName = value;
			return;
		}

		CProp lo_Prop = new CProp();
		CCyclostyle lo_Cyclostyle = new CCyclostyle();

		if (this.Activity == null) {
			lo_Cyclostyle = ParentCondition.Activity.Flow.Cyclostyle;
		} else {
			lo_Cyclostyle = Activity.Flow.Cyclostyle;
		}

		if (lo_Cyclostyle == null) {
			return;
		}

		for (CProp tempLoopVar_lo_Prop : lo_Cyclostyle.Props) {
			lo_Prop = tempLoopVar_lo_Prop;
			if (lo_Prop.PropName.equals(value)) {
				ms_BindPropName = value;
				return;
			}
		}

		ms_BindPropName = "";
	}

	/**
	 * 读取条件对应的连线对象
	 * 
	 * @return
	 */
	public CFlowLine getLine() {
		if (this.ConditionType != EConditionType.ActivityCondition)
			return null;

		CFlow lo_Flow = this.ParentCondition.Activity.Flow;
		for (CFlowLine lo_Line : lo_Flow.FlowLines) {
			if (lo_Line.SourceActivity.ActivityID == this.SourceActivityID
					&& this.ParentCondition.Activity == lo_Line.GoalActivity) {
				return lo_Line;
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
		this.Activity = null;
		this.ParentCondition = null;
		if (this.ChildConditions != null) {
			while (this.ChildConditions.size() > 0) {
				CCondition lo_Condition = this.ChildConditions.get(0);
				this.ChildConditions.remove(lo_Condition);
				lo_Condition.ClearUp();
				lo_Condition = null;
			}
			this.ChildConditions = null;
		}
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
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			String ls_ErrorMsg = "";
			switch (this.ConditionType) {
			case EConditionType.NotAnyCondition:
				return MGlobal.addSpace(ai_SpaceLength + 4) + "非法的条件类型\n";
			case EConditionType.LogicAndCondition:
			case EConditionType.LogicOrCondition:
				if (ChildConditions.size() == 0) {
					if (this.ParentCondition != null)
						return MGlobal.addSpace(ai_SpaceLength + 4)
								+ "逻辑条件没有子条件\n";
				} else {
					for (CCondition lo_Condition : this.ChildConditions) {
						ls_ErrorMsg += lo_Condition.IsValid(ai_SpaceLength + 4);
					}
				}
				break;
			case EConditionType.ActivityCondition:
				if (this.Activity == null) {
					ls_ErrorMsg += MGlobal.addSpace(ai_SpaceLength + 4)
							+ "条件的来源步骤未设置\n";
				}
				if (this.ActivityFNType > EActivityConditionType.AtListOneFinished) {
					if (this.CompareContent.equals("")) {
						ls_ErrorMsg += MGlobal.addSpace(ai_SpaceLength + 4)
								+ "条件的比较内容不能为空\n";
					}
				}
				break;

			case EConditionType.FlowPropCondition:
				if (getBindProp() == null && ms_BindPropName.equals("")) {
					ls_ErrorMsg += MGlobal.addSpace(ai_SpaceLength + 4)
							+ "条件的不存在控制属性\n";
				}
				if (this.CompareContent.equals("")) {
					ls_ErrorMsg += MGlobal.addSpace(ai_SpaceLength + 4)
							+ "条件的比较内容不能为空\n";
				}
				break;

			default: // OtherCondition
				break;
			}

			return ls_ErrorMsg;
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
			return e.toString();
		}
	}

	/**
	 * 插入子条件 <br>
	 * ai_ConditionType 条件类型 <br>
	 * av_Variant 根据条件类型的不同传递不同参数 <br>
	 * =ActivityCondition 传递条件源步骤 <br>
	 * =FlowPropCondition 传递条件控制属性 <br>
	 * =FlowPropCondition 传递条件控制属性
	 * 
	 * @param ai_ConditionType
	 * @param av_Variant
	 * @return
	 * @throws Exception
	 */
	public CCondition InsertChildCondition(int ai_ConditionType,
			CActivity ao_Activity, CProp ao_Prop) throws Exception {
		try {
			if (!(this.ConditionType == EConditionType.LogicOrCondition || this.ConditionType == EConditionType.LogicAndCondition)) {
				// 错误[1020]：非逻辑条件不能增加子条件
				this.Raise(1020, "InsertChildCondition",
						String.valueOf(ai_ConditionType));
				return null;
			}

			CCondition lo_Child = new CCondition();
			int ll_Id = this.Activity.Flow.ConditionMaxID + 1;
			this.Activity.Flow.ConditionMaxID = ll_Id;
			lo_Child.ConditionID = ll_Id;
			lo_Child.ParentCondition = this;
			lo_Child.setConditionType(ai_ConditionType);
			switch (lo_Child.ConditionType) {
			case EConditionType.ActivityCondition:
				lo_Child.setSourceActivity(ao_Activity);
				break;
			case EConditionType.FlowPropCondition:
				lo_Child.setBindProp(ao_Prop);
				break;
			case EConditionType.LogicAndCondition:
			case EConditionType.LogicOrCondition:
				lo_Child.Activity = this.Activity;
				break;
			case EConditionType.NotAnyCondition: // 新条件
				break;
			default:
				lo_Child.ClearUp();
				lo_Child = null;
				// 错误[1021]：
				this.Raise(1021, "InsertChildCondition",
						String.valueOf(ai_ConditionType));
				return null;
			}
			this.ChildConditions.add(lo_Child);
			return lo_Child;
		} catch (Exception e) {
			this.Raise(e, "InsertChildCondition",
					String.valueOf(ai_ConditionType));
			return null;
		}
	}

	/**
	 * 删除子条件
	 * 
	 * @param al_ConditionID
	 * @return
	 * @throws Exception
	 */
	public boolean DeleteChildCondition(int al_ConditionID) throws Exception {
		// 没有子条件需要删除
		if (this.ChildConditions == null) {
			return true;
		}
		if (this.ChildConditions.size() == 0) {
			return true;
		}

		for (CCondition lo_Child : ChildConditions) {
			if (lo_Child.ConditionID == al_ConditionID) {
				this.ChildConditions.remove(Integer.toString(al_ConditionID));
				lo_Child.ClearUp();
				lo_Child = null;
				return true;
			}
			if (lo_Child.ConditionType == EConditionType.LogicAndCondition
					|| lo_Child.ConditionType == EConditionType.LogicOrCondition) {
				if (lo_Child.DeleteChildCondition(al_ConditionID))
					return true;
			}
		}

		return false;
	}

	/**
	 * 取条件显示内容信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getConditionLabel() throws Exception {
		try {
			String ls_Label = "";

			switch (this.ConditionType) {
			case EConditionType.NotAnyCondition:
				return "新条件";
			case EConditionType.LogicOrCondition:
				ls_Label = "或";
				break;
			case EConditionType.LogicAndCondition:
				ls_Label = "且";
				break;
			case EConditionType.ActivityCondition:
				CActivity lo_Activity = this.Activity;
				if (lo_Activity == null) {
					ls_Label = "步骤[XXXX]条件成立";
				} else {
					switch (lo_Activity.ActivityType) {
					case EActivityType.StartActivity:
					case EActivityType.TransactActivity:
					case EActivityType.FYIActivity:
						switch (this.CompareType) {
						case EConditionCompareType.EqualToType:
							ls_Label = "等于";
							break;
						case EConditionCompareType.LargeType:
							ls_Label = "大于";
							break;
						case EConditionCompareType.LargeEqualToType:
							ls_Label = "大于等于";
							break;
						case EConditionCompareType.SmallType:
							ls_Label = "小于";
							break;
						case EConditionCompareType.SmallEqualToType:
							ls_Label = "小于等于";
							break;
						case EConditionCompareType.NotEqualToType:
							ls_Label = "不等于";
							break;
						}

						switch (this.ActivityFNType) {
						case EActivityConditionType.AllFinished:
							ls_Label = " 全部已处理 ";
							break;
						case EActivityConditionType.AtListOneFinished:
							ls_Label = " 至少一个已处理 ";
							break;
						case EActivityConditionType.FinishedNumber:
							ls_Label = " 已处理的人数 " + ls_Label + " "
									+ this.CompareContent;
							break;
						case EActivityConditionType.UnFinishedNumber:
							ls_Label = " 未处理的人数 " + ls_Label + " "
									+ this.CompareContent;
							break;
						case EActivityConditionType.FinishedPercent:
							ls_Label = " 已处理的百分比 " + ls_Label + " "
									+ this.CompareContent;
							break;
						case EActivityConditionType.UnFinishedPercent:
							ls_Label = " 未处理的百分比 " + ls_Label + " "
									+ this.CompareContent;
							break;
						}
						if (MGlobal.isExist(this.ResponseChoice)) {
							ls_Label = "意见为【" + this.ResponseChoice + "】"
									+ ls_Label;
						}

						ls_Label = "步骤【"
								+ this.getSourceActivity().ActivityName + "】"
								+ ls_Label;
						break;

					case EActivityType.TumbleInActivity:
					case EActivityType.LaunchActivity:
						switch (this.TumTrfType) {
						case EConditionTumTrfType.HaveSendType:
							ls_Label = "已经发送";
							break;
						case EConditionTumTrfType.FinishedFlow:
							ls_Label = "成功流转";
							break;
						case EConditionTumTrfType.UnFinishedFlow:
							ls_Label = "未成功流转";
							break;
						}
						ls_Label = "步骤“" + lo_Activity.ActivityName + "”"
								+ ls_Label;
						switch (this.CompareType) {
						case EConditionCompareType.EqualToType:
							ls_Label = ls_Label + "(返回值等于"
									+ this.CompareContent + ")";
							break;
						case EConditionCompareType.LargeType:
							ls_Label = ls_Label + "(返回值大于"
									+ this.CompareContent + ")";
							break;
						case EConditionCompareType.LargeEqualToType:
							ls_Label = ls_Label + "(返回值大于等于"
									+ this.CompareContent + ")";
							break;
						case EConditionCompareType.SmallType:
							ls_Label = ls_Label + "(返回值小于"
									+ this.CompareContent + ")";
							break;
						case EConditionCompareType.SmallEqualToType:
							ls_Label = ls_Label + "(返回值小于等于"
									+ this.CompareContent + ")";
							break;
						case EConditionCompareType.NotEqualToType:
							ls_Label = ls_Label + "(返回值不等于"
									+ this.CompareContent + ")";
							break;
						}
						break;

					case EActivityType.SplitActivity:
						ls_Label = "步骤“" + lo_Activity.ActivityName + "”条件成立";
						break;
					}
				}
				break;

			case EConditionType.FlowPropCondition:
				switch (this.CompareType) {
				case EConditionCompareType.EqualToType:
					ls_Label = "等于";
					break;
				case EConditionCompareType.LargeType:
					ls_Label = "大于";
					break;
				case EConditionCompareType.LargeEqualToType:
					ls_Label = "大于等于";
					break;
				case EConditionCompareType.SmallType:
					ls_Label = "小于";
					break;
				case EConditionCompareType.SmallEqualToType:
					ls_Label = "小于等于";
					break;
				case EConditionCompareType.NotEqualToType:
					ls_Label = "不等于";
					break;
				}

				if (getBindProp() == null) {
					if (ms_BindPropName == null) {
						ls_Label = "表头[XXXX] " + ls_Label + " "
								+ this.CompareContent;
					} else {
						ls_Label = "表头[" + ms_BindPropName + "]" + ls_Label
								+ " " + this.CompareContent;
					}
				} else {
					ls_Label = "表头[" + ms_BindPropName + "]" + ls_Label + " "
							+ this.CompareContent;
				}
				break;
			}
			return ls_Label;
		} catch (Exception e) {
			this.Raise(e, "ConditionLabel", null);
		}
		return "";
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
		int li_Count = -1;
		if (ai_Type == 1) {
			this.ConditionID = Integer.parseInt(ao_Node.getAttribute("ID"));
			this.ConditionType = Integer.parseInt(ao_Node.getAttribute("Type"));
			this.ActivityFNType = Integer.parseInt(ao_Node
					.getAttribute("ActivityFNType"));
			this.ResponseChoice = ao_Node.getAttribute("ResponseChoice");
			this.CompareType = Integer.parseInt(ao_Node
					.getAttribute("CompareType"));
			this.CompareContent = ao_Node.getAttribute("CompareContent");
			this.TumTrfType = Integer.parseInt(ao_Node
					.getAttribute("TumTrfType"));
			this.SourceActivityID = Integer.parseInt(ao_Node
					.getAttribute("SourceActivityID"));
			ms_BindPropName = ao_Node.getAttribute("BindPropName");
			if (MGlobal.isExist(ms_BindPropName))
				System.out.print(ms_BindPropName + "\n");

			li_Count = Integer.parseInt(ao_Node.getAttribute("ChildCount"));
		} else {
			this.ConditionID = Integer.parseInt(ao_Node
					.getAttribute("ConditionID"));
			this.ConditionType = Integer.parseInt(ao_Node
					.getAttribute("ConditionType"));
			this.ActivityFNType = Integer.parseInt(ao_Node
					.getAttribute("ActivityFNType"));
			this.ResponseChoice = ao_Node.getAttribute("ResponseChoice");
			this.CompareType = Integer.parseInt(ao_Node
					.getAttribute("CompareType"));
			this.CompareContent = ao_Node.getAttribute("CompareContent");
			this.TumTrfType = Integer.parseInt(ao_Node
					.getAttribute("TumTrfType"));
			this.SourceActivityID = Integer.parseInt(ao_Node
					.getAttribute("SourceActivityID"));
			ms_BindPropName = ao_Node.getAttribute("BindPropName");

			li_Count = Integer.parseInt(ao_Node
					.getAttribute("ChildConditionsCount"));
		}
		// 子条件对象集合，只有为逻辑条件类型时才有且必须有此对象

		if (li_Count == -1) {
			this.ChildConditions = null;
		} else {
			this.ChildConditions = new ArrayList<CCondition>();
			NodeList lo_List = ao_Node.getElementsByTagName("Condition");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CCondition lo_Condition = new CCondition(this.Logon);
				lo_Condition.ParentCondition = this;
				lo_Condition.Activity = this.Activity;
				lo_Condition.Import(lo_Node, ai_Type);
				this.ChildConditions.add(lo_Condition);
				if (this.Activity.Flow.ConditionMaxID < lo_Condition.ConditionID) {
					this.Activity.Flow.ConditionMaxID = lo_Condition.ConditionID;
				}
			}
		}
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
		if (ai_Type == 1) {
			ao_Node.setAttribute("ID", Integer.toString(this.ConditionID));
			ao_Node.setAttribute("Type", Integer.toString(this.ConditionType));
			ao_Node.setAttribute("ActivityFNType",
					Integer.toString(this.ActivityFNType));
			ao_Node.setAttribute("ResponseChoice", this.ResponseChoice);
			ao_Node.setAttribute("CompareType",
					Integer.toString(this.CompareType));
			ao_Node.setAttribute("CompareContent", this.CompareContent);
			ao_Node.setAttribute("TumTrfType",
					Integer.toString(this.TumTrfType));
			ao_Node.setAttribute("SourceActivityID",
					Integer.toString(this.SourceActivityID));
			ao_Node.setAttribute("BindPropName", ms_BindPropName);
			// 子条件对象集合，只有为逻辑条件类型时才有且必须有此对象
			if (this.ChildConditions == null) {
				ao_Node.setAttribute("ChildCount", "-1");
			} else {
				ao_Node.setAttribute("ChildCount",
						Integer.toString(ChildConditions.size()));
			}
		} else {
			ao_Node.setAttribute("ConditionID",
					Integer.toString(this.ConditionID));
			ao_Node.setAttribute("ConditionType",
					Integer.toString(this.ConditionType));
			ao_Node.setAttribute("ActivityFNType",
					Integer.toString(this.ActivityFNType));
			ao_Node.setAttribute("ResponseChoice", this.ResponseChoice);
			ao_Node.setAttribute("CompareType",
					Integer.toString(this.CompareType));
			ao_Node.setAttribute("CompareContent", this.CompareContent);
			ao_Node.setAttribute("TumTrfType",
					Integer.toString(this.TumTrfType));
			ao_Node.setAttribute("SourceActivityID",
					Integer.toString(this.SourceActivityID));
			ao_Node.setAttribute("BindPropName", ms_BindPropName);
			// 子条件对象集合，只有为逻辑条件类型时才有且必须有此对象
			if (this.ChildConditions == null) {
				ao_Node.setAttribute("ChildConditionsCount", "-1");
			} else {
				ao_Node.setAttribute("ChildConditionsCount",
						Integer.toString(ChildConditions.size()));
			}
		}
		if (this.ChildConditions != null) {
			for (CCondition lo_Condition : this.ChildConditions) {
				Element lo_Node = MXmlHandle.addElement(ao_Node, "Condition");
				lo_Condition.Export(lo_Node, ai_Type);
			}
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
	 */
	@Override
	protected void Write(MBag ao_Bag, int ai_Type) {
		if (ai_Type == 1) {
			ao_Bag.Write("ID", this.ConditionID);
			ao_Bag.Write("CT", this.ConditionType);
			ao_Bag.Write("FNT", this.ActivityFNType);
			ao_Bag.Write("RC", this.ResponseChoice);
			ao_Bag.Write("CMT", this.CompareType);
			ao_Bag.Write("CC", this.CompareContent);
			ao_Bag.Write("TT", this.TumTrfType);
			ao_Bag.Write("SA", this.SourceActivityID);
			ao_Bag.Write("BP", ms_BindPropName);

			// 子条件对象集合，只有为逻辑条件类型时才有且必须有此对象
			if (this.ChildConditions == null) {
				ao_Bag.Write("CCC", -1);
			} else {
				ao_Bag.Write("CCC", ChildConditions.size());
				for (CCondition lo_Condition : ChildConditions) {
					lo_Condition.Write(ao_Bag, ai_Type);
				}
			}
		} else {
			ao_Bag.Write("ml_ConditionID", this.ConditionID);
			ao_Bag.Write("mi_ConditionType", this.ConditionType);
			ao_Bag.Write("mi_ActivityFNType", this.ActivityFNType);
			ao_Bag.Write("ms_ResponseChoice", this.ResponseChoice);
			ao_Bag.Write("mi_CompareType", this.CompareType);
			ao_Bag.Write("ms_CompareContent", this.CompareContent);
			ao_Bag.Write("mi_TumTrfType", this.TumTrfType);
			ao_Bag.Write("ml_SourceActivityID", this.SourceActivityID);
			ao_Bag.Write("ms_BindPropName", ms_BindPropName);

			// 子条件对象集合，只有为逻辑条件类型时才有且必须有此对象
			if (this.ChildConditions == null) {
				ao_Bag.Write("ChildConditionsCount", -1);
			} else {
				ao_Bag.Write("ChildConditionsCount",
						this.ChildConditions.size());
				for (CCondition lo_Condition : ChildConditions) {
					lo_Condition.Write(ao_Bag, ai_Type);
				}
			}
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
		int li_Count = -1;
		if (ai_Type == 1) {
			this.ConditionID = ao_Bag.ReadInt("ID");
			this.ConditionType = ao_Bag.ReadInt("CT");
			this.ActivityFNType = ao_Bag.ReadInt("FNT");
			this.ResponseChoice = ao_Bag.ReadString("RC");
			this.CompareType = ao_Bag.ReadInt("CMT");
			this.CompareContent = ao_Bag.ReadString("CC");
			this.TumTrfType = ao_Bag.ReadInt("TT");
			this.SourceActivityID = ao_Bag.ReadInt("SA");
			ms_BindPropName = ao_Bag.ReadString("BP");

			li_Count = ao_Bag.ReadInt("CCC");
		} else {
			this.ConditionID = ao_Bag.ReadInt("ml_ConditionID");
			this.ConditionType = ao_Bag.ReadInt("ml_ConditionID");
			this.ActivityFNType = ao_Bag.ReadInt("mi_ActivityFNType");
			this.ResponseChoice = ao_Bag.ReadString("ms_ResponseChoice");
			this.CompareType = ao_Bag.ReadInt("mi_CompareType");
			this.CompareContent = ao_Bag.ReadString("ms_CompareContent");
			this.TumTrfType = ao_Bag.ReadInt("mi_TumTrfType");
			this.SourceActivityID = ao_Bag.ReadInt("ml_SourceActivityID");
			ms_BindPropName = ao_Bag.ReadString("ms_BindPropName");

			li_Count = ao_Bag.ReadInt("ChildConditionsCount");
		}

		// 子条件对象集合，只有为逻辑条件类型时才有且必须有此对象
		if (li_Count == -1) {
			this.ChildConditions = null;
		} else {
			this.ChildConditions = new ArrayList<CCondition>();
			for (int i = 1; i <= li_Count; i++) {
				CCondition lo_Child = new CCondition(this.Logon);
				lo_Child.ParentCondition = this;
				lo_Child.Activity = this.Activity;
				lo_Child.Read(ao_Bag, ai_Type);
				ChildConditions.add(lo_Child);
				if (this.Activity.Flow.ConditionMaxID < lo_Child.ConditionID) {
					this.Activity.Flow.ConditionMaxID = lo_Child.ConditionID;
				}
			}
		}
	}

}
