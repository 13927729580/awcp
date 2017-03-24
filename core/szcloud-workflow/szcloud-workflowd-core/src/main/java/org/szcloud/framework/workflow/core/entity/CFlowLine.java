package org.szcloud.framework.workflow.core.entity;

import java.awt.Color;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.EConditionType;
import org.szcloud.framework.workflow.core.emun.ELineDirection;
import org.szcloud.framework.workflow.core.emun.ELineType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 流程连线对象：流程对象模型中代表实际两个步骤对象之间的连线，它主要使用于有向图界面的表现功能。
 * 
 * @author Jackie.Wang
 * 
 */
public class CFlowLine extends CBase {
	/**
	 * 初始化
	 */
	public CFlowLine() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CFlowLine(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#

	/**
	 * 所属的流程
	 */
	public CFlow Flow = null;

	/**
	 * 源步骤
	 */
	public CActivity SourceActivity = null;

	/**
	 * 目标步骤
	 */
	public CActivity GoalActivity = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 连线标识
	 */
	public int LineID = -1;

	/**
	 * 连线类型
	 */
	public int LineType = ELineType.NotAnyLineType;

	/**
	 * 设置连线类型
	 * 
	 * @param value
	 */
	public void setLineType(int value) {
		if (this.LineType == value)
			return;
		this.LineType = value;
		if (this.LineType == ELineType.BeeLineType) {
			this.LinePointXs = Integer.toString(this.SourceActivity.Left
					+ this.SourceActivity.Width / 2)
					+ ";"
					+ Integer.toString(this.GoalActivity.Left
							+ this.GoalActivity.Width / 2) + ";";
			this.LinePointYs = Integer.toString(this.SourceActivity.Top
					+ this.SourceActivity.Height / 2)
					+ ";"
					+ Integer.toString(this.GoalActivity.Top
							+ this.GoalActivity.Height / 2) + ";";
		}
		if (this.LineType == ELineType.AnywayLineType) {
			this.LinePointXs = Integer.toString(this.SourceActivity.Left
					+ this.SourceActivity.Width / 2)
					+ ";"
					+ Integer.toString(this.GoalActivity.Left
							+ this.GoalActivity.Width / 2)
					+ ";"
					+ Integer.toString(this.GoalActivity.Left
							+ this.GoalActivity.Width / 2) + ";";
			this.LinePointYs = Integer.toString(this.SourceActivity.Top
					+ this.SourceActivity.Height / 2)
					+ ";"
					+ Integer.toString(this.SourceActivity.Top
							+ this.SourceActivity.Height / 2)
					+ ";"
					+ Integer.toString(this.GoalActivity.Top
							+ this.GoalActivity.Width / 2) + ";";
		}
	}

	/**
	 * 连线颜色
	 */
	public Color LineColor = Color.BLACK;

	/**
	 * 连线宽度：=1-细线；=2-普通线；=3-粗线；
	 */
	public int LineBorder = 1;

	/**
	 * 连线表现类型:=1—>实线;=2—>虚线;=4—>点划线
	 */
	public int LineStyle = 1;

	/**
	 * 线坐标内容串—横坐标
	 */
	public String LinePointXs = "";

	/**
	 * 线坐标内容串—纵坐标
	 */
	public String LinePointYs = "";

	/**
	 * 连线入口方向
	 */
	public int InDirection = ELineDirection.SouthwardDirection;

	/**
	 * 连线出口方向
	 */
	public int OutDirection = ELineDirection.NorthwardDirection;

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 所属的流程
		this.Flow = null;
		// 源步骤
		this.SourceActivity = null;
		// 目标步骤
		this.GoalActivity = null;

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
			// Add Get Valid Code...
			return "";
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
			return e.toString();
		}
	}

	/**
	 * 取连线所绑定条件的个数
	 * 
	 * @return
	 * @throws Exception 
	 */
	public int bindConditionNumber() throws Exception {
		try {
			return conditionNumber(GoalActivity.Condition);
		} catch (Exception e) {
			this.Raise(e, "BindConditionNumber", null);
		}
		return 0;
	}

	/**
	 * 取条件绑定连线的个数
	 * 
	 * @param ao_Condition
	 * @return
	 * @throws Exception 
	 */
	private int conditionNumber(CCondition ao_Condition) throws Exception {
		try {
			if (ao_Condition == null) {
				return 0;
			}
			CCondition lo_Child = new CCondition();
			int li_Count = 0;
			switch (ao_Condition.ConditionType) {
			case EConditionType.ActivityCondition:
				if (ao_Condition.Activity == SourceActivity) {
					return 1;
				}
				break;
			case EConditionType.LogicAndCondition:
			case EConditionType.LogicOrCondition:
				if (ao_Condition.ChildConditions == null) {
					return 0;
				}
				li_Count = 0;
				for (CCondition tempLoopVar_lo_Child : ao_Condition.ChildConditions) {
					lo_Child = tempLoopVar_lo_Child;
					li_Count = li_Count + conditionNumber(lo_Child);
				}
				return li_Count;
			default:
				return 0;
			}
		} catch (Exception e) {
			this.Raise(e, "ConditionNumber", null);
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
		if (ai_Type == 1) {
			this.LineID = Integer.parseInt((ao_Node.getAttribute("ID")));
			this.LineType = Integer.parseInt(ao_Node.getAttribute("Type"));
			this.LineColor = MGlobal.rGBToColor(Integer.parseInt(ao_Node
					.getAttribute("Color")));
			this.LineBorder = Integer.parseInt(ao_Node.getAttribute("Border"));
			this.LineStyle = Integer.parseInt(ao_Node.getAttribute("Style"));
			this.LinePointXs = ao_Node.getAttribute("PXs");
			this.LinePointYs = ao_Node.getAttribute("PYs");
			this.InDirection = Integer.parseInt(ao_Node.getAttribute("InDir"));
			this.OutDirection = Integer
					.parseInt(ao_Node.getAttribute("OutDir"));
			this.SourceActivity = this.Flow.getActivityById(Integer
					.parseInt(ao_Node.getAttribute("SActID")));
			this.GoalActivity = this.Flow.getActivityById(Integer
					.parseInt(ao_Node.getAttribute("GActID")));
		} else {
			this.LineID = Integer.parseInt(ao_Node.getAttribute("LineID"));
			this.LineType = Integer.parseInt(ao_Node.getAttribute("LineType"));
			this.LineColor = MGlobal.rGBToColor(Integer.parseInt(ao_Node
					.getAttribute("LineColor")));
			this.LineBorder = Integer.parseInt(ao_Node
					.getAttribute("LineBorder"));
			this.LineStyle = Integer
					.parseInt(ao_Node.getAttribute("LineStyle"));
			this.LinePointXs = ao_Node.getAttribute("LinePointXs");
			this.LinePointYs = ao_Node.getAttribute("LinePointYs");
			this.InDirection = Integer.parseInt(ao_Node
					.getAttribute("InDirection"));
			this.OutDirection = Integer.parseInt(ao_Node
					.getAttribute("OutDirection"));
			this.SourceActivity = this.Flow.getActivityById(Integer
					.parseInt(ao_Node.getAttribute("SourceActivityID")));
			this.GoalActivity = this.Flow.getActivityById(Integer
					.parseInt(ao_Node.getAttribute("GoalActivityID")));
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
			ao_Node.setAttribute("ID", Integer.toString(this.LineID));
			ao_Node.setAttribute("Type", Integer.toString(this.LineType));
			ao_Node.setAttribute("Color",
					Integer.toString(MGlobal.colorToRGB(this.LineColor)));
			ao_Node.setAttribute("Border", Integer.toString(this.LineBorder));
			ao_Node.setAttribute("Style", Integer.toString(this.LineStyle));
			ao_Node.setAttribute("PXs", this.LinePointXs);
			ao_Node.setAttribute("PYs", this.LinePointYs);
			ao_Node.setAttribute("InDir", Integer.toString(this.InDirection));
			ao_Node.setAttribute("OutDir", Integer.toString(this.OutDirection));
			ao_Node.setAttribute("SActID",
					Integer.toString(SourceActivity.ActivityID));
			ao_Node.setAttribute("GActID",
					Integer.toString(GoalActivity.ActivityID));
		} else {
			ao_Node.setAttribute("LineID", Integer.toString(this.LineID));
			ao_Node.setAttribute("LineType", Integer.toString(this.LineType));
			ao_Node.setAttribute("LineColor",
					Integer.toString(MGlobal.colorToRGB(this.LineColor)));
			ao_Node.setAttribute("LineBorder",
					Integer.toString(this.LineBorder));
			ao_Node.setAttribute("LineStyle", Integer.toString(this.LineStyle));
			ao_Node.setAttribute("LinePointXs", this.LinePointXs);
			ao_Node.setAttribute("LinePointYs", this.LinePointYs);
			ao_Node.setAttribute("InDirection",
					Integer.toString(this.InDirection));
			ao_Node.setAttribute("OutDirection",
					Integer.toString(this.OutDirection));
			ao_Node.setAttribute("SourceActivityID",
					Integer.toString(SourceActivity.ActivityID));
			ao_Node.setAttribute("GoalActivityID",
					Integer.toString(GoalActivity.ActivityID));
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
			ao_Bag.Write("ml_LineID", this.LineID);
			ao_Bag.Write("mi_LineType", this.LineType);
			ao_Bag.Write("ml_LineColor", MGlobal.colorToRGB(this.LineColor));
			ao_Bag.Write("mi_LineBorder", this.LineBorder);
			ao_Bag.Write("mi_LineStyle", this.LineStyle);
			ao_Bag.Write("ms_LinePointXs", this.LinePointXs);
			ao_Bag.Write("ms_LinePointYs", this.LinePointYs);
			ao_Bag.Write("mi_InDirection", this.InDirection);
			ao_Bag.Write("mi_OutDirection", this.OutDirection);
			ao_Bag.Write("SourceActivityID", this.SourceActivity.ActivityID);
			ao_Bag.Write("GoalActivityID", this.GoalActivity.ActivityID);
		} else {
			ao_Bag.Write("ID", this.LineID);
			ao_Bag.Write("LT", this.LineType);
			ao_Bag.Write("LC", MGlobal.colorToRGB(this.LineColor));
			ao_Bag.Write("LB", this.LineBorder);
			ao_Bag.Write("LS", this.LineStyle);
			ao_Bag.Write("PX", this.LinePointXs);
			ao_Bag.Write("PY", this.LinePointYs);
			ao_Bag.Write("DI", this.InDirection);
			ao_Bag.Write("DO", this.OutDirection);
			ao_Bag.Write("SA", this.SourceActivity.ActivityID);
			ao_Bag.Write("GA", this.GoalActivity.ActivityID);
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
		// #导入基本信息
		if (ai_Type == 1) {
			this.LineID = ao_Bag.ReadInt("ID");
			this.LineType = ao_Bag.ReadInt("LT");
			this.LineColor = MGlobal.rGBToColor(ao_Bag.ReadInt("LC"));
			this.LineBorder = ao_Bag.ReadInt("LB");
			this.LineStyle = ao_Bag.ReadInt("LS");
			this.LinePointXs = ao_Bag.ReadString("PX");
			this.LinePointYs = ao_Bag.ReadString("PY");
			this.InDirection = ao_Bag.ReadInt("DI");
			this.OutDirection = ao_Bag.ReadInt("DO");
			this.SourceActivity = this.Flow.getActivityById(ao_Bag
					.ReadInt("SA"));
			this.GoalActivity = this.Flow.getActivityById(ao_Bag.ReadInt("GA"));
		} else {
			this.LineID = ao_Bag.ReadInt("ml_LineID");
			this.LineType = ao_Bag.ReadInt("mi_LineType");
			this.LineColor = MGlobal.rGBToColor(ao_Bag.ReadInt("ml_LineColor"));
			this.LineBorder = ao_Bag.ReadInt("mi_LineBorder");
			this.LineStyle = ao_Bag.ReadInt("mi_LineStyle");
			this.LinePointXs = ao_Bag.ReadString("ms_LinePointXs");
			this.LinePointYs = ao_Bag.ReadString("ms_LinePointYs");
			this.InDirection = ao_Bag.ReadInt("mi_InDirection");
			this.SourceActivity = this.Flow.getActivityById(ao_Bag
					.ReadInt("theSourceActivityID"));
			this.GoalActivity = this.Flow.getActivityById(ao_Bag
					.ReadInt("theGoalActivityID"));
		}
	}

}
