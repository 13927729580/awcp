package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EFlowType;
import org.szcloud.framework.workflow.core.emun.EParticipantRangeType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 工作流对象：是流程对象模型的最基本对象，它是其它流程子对象的总入口，对于流程中的各种处理方法和属性都在本对象中处理。
 * 
 * @author Jackie.Wang
 * 
 */
public class CFlow extends CBase {
	/**
	 * 初始化 无参构造方法
	 * 
	 * @param ao_Logon
	 */

	public CFlow() {
		super();
	}

	/**
	 * 初始化 有参构造方法
	 * 
	 * @param ao_Logon
	 */
	public CFlow(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#

	/**
	 * 所属的公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	/**
	 * 步骤集合
	 */
	public ArrayList<CActivity> Activities = new ArrayList<CActivity>();

	/**
	 * 根据步骤标识获取步骤对象
	 * 
	 * @param al_Id
	 *            步骤标识
	 * @return
	 */
	public CActivity getActivityById(int al_Id) {
		if (this.Activities == null)
			return null;
		for (CActivity lo_Act : this.Activities) {
			if (lo_Act.ActivityID == al_Id)
				return lo_Act;
		}
		return null;
	}

	/**
	 * 根据步骤名称获取步骤对象
	 * 
	 * @param as_Name
	 *            步骤名称
	 * @return
	 */
	public CActivity getActivityByName(String as_Name) {
		if (this.Activities == null)
			return null;
		for (CActivity lo_Act : this.Activities) {
			if (lo_Act.ActivityName.equals(as_Name))
				return lo_Act;
		}
		return null;
	}

	/**
	 * 通过步骤顺序号取流程步
	 * 
	 * @param al_OrderID
	 * @return
	 */
	public CActivity getActivityByOrder(int al_OrderID) {
		for (CActivity lo_Activity : this.Activities) {
			if (lo_Activity.OrderID == al_OrderID)
				return lo_Activity;
		}
		return null;
	}

	/**
	 * 步骤连线集合
	 */
	public ArrayList<CFlowLine> FlowLines = new ArrayList<CFlowLine>();

	/**
	 * 根据连线标识获取连线对象
	 * 
	 * @param al_Id
	 *            连线标识
	 * @return
	 */
	public CFlowLine getFlowLineById(int al_Id) {
		if (this.FlowLines == null)
			return null;
		for (CFlowLine lo_Line : this.FlowLines) {
			if (lo_Line.LineID == al_Id)
				return lo_Line;
		}
		return null;
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 流程标识
	 */
	public int FlowID = 0;

	/**
	 * 流程排列序号
	 */
	public int FlowIndex = 0;

	/**
	 * 流程名称
	 */
	public String FlowName = "未命名流程";

	/**
	 * 流程名称
	 * 
	 * @param value
	 */
	public void setFlowName(String value) {
		if (MGlobal.BeyondOfLength(value, 255))
			return; // 超过长度
		this.FlowName = value;
	}

	/**
	 * 流程模板类别
	 */
	public int FlowType = EFlowType.SimpleFlowType;

	/**
	 * 流程最大连线标识
	 */
	public int FlowLineMaxID = 0;

	/**
	 * 流程最大步骤标识
	 */
	public int ActivityMaxID = 0;

	/**
	 * 流程步骤条件的最大标识
	 */
	public int ConditionMaxID = 0;

	/**
	 * 流程说明
	 */
	public String Summary = null;

	/**
	 * 流程说明
	 * 
	 * @param value
	 */
	public void setSummary(String value) {
		if (MGlobal.BeyondOfLength(value, 2000))
			return;
		this.Summary = value;
	}

	/**
	 * 流程图尺寸-宽度
	 */
	public int FlowWidth = 800;

	/**
	 * 流程图尺寸-宽度
	 * 
	 * @param value
	 */
	public void setFlowWidth(int value) {
		int ll_MinWidth = getMinFlowWidth();
		if (value > ll_MinWidth) {
			this.FlowWidth = value;
		} else {
			this.FlowWidth = ll_MinWidth;
		}
	}

	/**
	 * 读取流程图最小宽度
	 * 
	 * @return
	 */
	public int getMinFlowWidth() {
		int ll_MinWidth = 1;
		for (CActivity lo_Activity : this.Activities) {
			if (ll_MinWidth < lo_Activity.Left + lo_Activity.Width)
				ll_MinWidth = lo_Activity.Left + lo_Activity.Width;
		}
		return ll_MinWidth;
	}

	/**
	 * 流程图尺寸-高度
	 */
	public int FlowHeight = 600;

	/**
	 * 流程图尺寸-高度
	 * 
	 * @param value
	 */
	public void setFlowHeight(int value) {
		int ll_MinHeight = getMinFlowHeight();
		if (value > ll_MinHeight) {
			this.FlowHeight = value;
		} else {
			this.FlowHeight = ll_MinHeight;
		}
	}

	/**
	 * 读取流程图最小高度
	 * 
	 * @return
	 */
	public int getMinFlowHeight() {
		int ll_MinHeight = 1;
		for (CActivity lo_Activity : this.Activities) {
			if (ll_MinHeight < lo_Activity.Top + lo_Activity.Height)
				ll_MinHeight = lo_Activity.Top + lo_Activity.Height;
		}
		return ll_MinHeight;
	}

	/**
	 * 流程图文件路径
	 */
	public String FlowImage = "";

	/**
	 * 是否是缺省流程
	 */
	public boolean DefaultFlow = true;

	/**
	 * 内存数据，在公文实例流转处理中，如果流程有改变，则保存流程的更改
	 */
	public boolean Changed = false;

	/**
	 * 特殊步骤名称
	 */
	public String SpecialActivityName = "特殊步骤";

	/**
	 * 特殊步骤名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setSpecialActivityName(String value) throws Exception {
		if (MGlobal.isEmpty(value))
			return;

		if (value.equals(this.PreActivityName)
				|| value.equals(this.NextActivityName)) {
			// 错误[1016]：步骤名称不能相同
			this.Raise(1016, "setSpecialActivityName", value);
			return;
		}

		for (CActivity lo_Activity : this.Activities) {
			if (lo_Activity.ActivityName.equals(value)) {
				// 错误[1016]：步骤名称不能相同
				this.Raise(1016, "setSpecialActivityName", value);
				return;
			}
		}

		this.SpecialActivityName = value;
	}

	/**
	 * 特殊步骤显示名称
	 */
	public String SpecialDisplayName = "特殊步骤";

	/**
	 * 转发到一个新步骤，该新步骤处理完成后再转回本步骤的步骤名称
	 */
	public String PreActivityName = "协同处理步骤";

	/**
	 * 转发到一个新步骤，该新步骤处理完成后再转回本步骤的步骤名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setPreActivityName(String value) throws Exception {
		if (MGlobal.isEmpty(value))
			return;

		if (value.equals(this.SpecialActivityName)
				|| value.equals(this.NextActivityName)) {
			// 错误[1016]：步骤名称不能相同
			this.Raise(1016, "setPreActivityName", value);
			return;
		}

		for (CActivity lo_Activity : this.Activities) {
			if (lo_Activity.ActivityName.equals(value)) {
				// 错误[1016]：步骤名称不能相同
				this.Raise(1016, "setPreActivityName", value);
				return;
			}
		}

		this.PreActivityName = value;
	}

	/**
	 * 转发到一个新步骤，该新步骤处理完成后再转回本步骤的步骤显示名称
	 */
	public String PreDisplayName = "协同处理步骤";

	/**
	 * 转发到一个新步骤，该新步骤处理完成后不需要转回本步骤直接继续回到流转的步骤名称
	 */
	public String NextActivityName = "加送处理步骤";

	/**
	 * 转发到一个新步骤，该新步骤处理完成后不需要转回本步骤直接继续回到流转的步骤名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setNextActivityName(String value) throws Exception {
		if (MGlobal.isEmpty(value))
			return;

		if (value.equals(this.SpecialActivityName)
				|| value.equals(this.PreActivityName)) {
			// 错误[1016]：步骤名称不能相同
			this.Raise(1016, "setPreActivityName", value);
			return;
		}

		for (CActivity lo_Activity : this.Activities) {
			if (lo_Activity.ActivityName.equals(value)) {
				// 错误[1016]：步骤名称不能相同
				this.Raise(1016, "setPreActivityName", value);
				return;
			}
		}

		this.NextActivityName = value;
	}

	/**
	 * 转发到一个新步骤，该新步骤处理完成后不需要转回本步骤直接继续回到流转的步骤显示名称
	 */
	public String NextDisplayName = "加送处理步骤";

	// 临时内存变量，当只打开流程时，所使用到的角色和表头属性内蓉

	/**
	 * 角色的XML字符串[标识+名称]
	 */
	public String RoleXml = "";

	/**
	 * 表头属性的XML字符串[标识+名称]
	 */
	public String PropXml = "";

	/**
	 * 权限的XML字符串[标识+名称]
	 */
	public String RightXml = "";

	/**
	 * 二次开发函数的XML字符串[标识+名称]
	 */
	public String ScriptXml = "";

	// #==========================================================================#
	// 常用过程或函数定义
	// #==========================================================================#
	/**
	 * 初始化
	 * 
	 * @param ao_Cyclostyle
	 * @param ao_Logon
	 */
	public CFlow(CCyclostyle ao_Cyclostyle, CLogon ao_Logon) {
		this.Logon = ao_Logon;
		this.Cyclostyle = ao_Cyclostyle;
		this.Activities = new ArrayList<CActivity>();
		this.FlowLines = new ArrayList<CFlowLine>();
	}

	/**
	 * 读取流程的XML字符串
	 * 
	 * @throws Exception
	 */
	public String getFlowXml() throws Exception {
		Document lo_Xml = MXmlHandle.LoadXml("<Flow />");
		this.Export(lo_Xml.getDocumentElement(), 0);
		return lo_Xml.getTextContent();
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属的公文模板对象
		this.Cyclostyle = null;

		// 步骤集合
		if (this.Activities != null) {
			while (this.Activities.size() > 0) {
				CActivity lo_Activity = this.Activities.get(0);
				this.Activities.remove(lo_Activity);
				lo_Activity.ClearUp();
				lo_Activity = null;
			}
			this.Activities = null;
		}

		// 步骤连线集合
		if (this.FlowLines != null) {
			while (this.FlowLines.size() > 0) {
				CFlowLine lo_Line = this.FlowLines.get(0);
				this.FlowLines.remove(lo_Line);
				lo_Line.ClearUp();
				lo_Line = null;
			}
			this.FlowLines = null;
		}

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
			String ls_ErrorMsg = "";
			for (CActivity lo_Activity : Activities) {
				ls_ErrorMsg += lo_Activity.IsValid(ai_SpaceLength + 4);
			}

			if (ls_ErrorMsg != "") {
				ls_ErrorMsg = MGlobal.addSpace(ai_SpaceLength) + "流程["
						+ this.FlowName + "]有错误: \n" + ls_ErrorMsg;
			}
			return ls_ErrorMsg;
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
			return "";
		}
	}

	/**
	 * 新建流程
	 * 
	 * @param as_FlowName
	 * @return
	 * @throws Exception
	 */
	public boolean newFlow(String as_FlowName) throws Exception {
		try {
			this.FlowName = as_FlowName;

			CActivity lo_Activity = insertActivity("开始",
					EActivityType.StartActivity);
			lo_Activity.Transact.setUserRight("2;");
			lo_Activity.activityMove((this.FlowWidth - lo_Activity.Width) / 3,
					0, -1, -1);

			return true;
		} catch (Exception e) {
			this.Raise(e, "NewFlow", as_FlowName);
			return false;
		}
	}

	/**
	 * 插入步骤
	 * 
	 * @param as_ActivityName
	 * @param ai_ActivityType
	 * @return
	 * @throws Exception
	 */
	public CActivity insertActivity(String as_ActivityName, int ai_ActivityType)
			throws Exception {
		try {
			// 步骤名称不可以为空
			if (as_ActivityName == null || as_ActivityName.equals(""))
				return null;

			if (this.Logon.GlobalPara.IsInsideTransact == false) {
				if (as_ActivityName == this.SpecialActivityName
						|| as_ActivityName == this.PreActivityName
						|| as_ActivityName == this.NextActivityName) {
					// 错误[1016]：步骤名称不能相同
					this.Raise(1016, "InsertActivity", as_ActivityName);
					return null;
				}
			}

			for (CActivity lo_Activity : this.Activities) {
				if (lo_Activity.ActivityName == as_ActivityName) {
					// 错误[1016]：步骤名称不能相同
					this.Raise(1016, "InsertActivity", as_ActivityName);
					return null;
				}
			}

			if (ai_ActivityType == EActivityType.StartActivity) {
				if (this.Activities.size() > 0) {
					// 错误[1017]：不能插入开始类型步骤
					this.Raise(1017, "InsertActivity", as_ActivityName);
					return null;
				}
			}

			if (!((ai_ActivityType > EActivityType.NotAnyActivity && ai_ActivityType <= EActivityType.SplitActivity)
					|| ai_ActivityType == EActivityType.StopActivity || ai_ActivityType == EActivityType.NotLimitedActivity)) {
				// 错误[1018]：插入非法类型步骤
				this.Raise(1018, "InsertActivity", as_ActivityName);
				return null;
			}

			CActivity lo_Activity = new CActivity(this.Logon);
			lo_Activity.Flow = this;
			this.ActivityMaxID++;
			lo_Activity.ActivityID = this.ActivityMaxID;
			lo_Activity.setActivityName(as_ActivityName);
			lo_Activity.setActivityType(ai_ActivityType);
			this.Activities.add(lo_Activity);
			lo_Activity.OrderID = this.Activities.size();

			return lo_Activity;
		} catch (Exception e) {
			this.Raise(e, "InsertActivity", null);
		}
		return null;
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
			this.SpecialActivityName = ao_Node.getAttribute("SActName");
			this.SpecialDisplayName = ao_Node.getAttribute("SDisName");
			this.PreActivityName = ao_Node.getAttribute("PActName");
			this.PreDisplayName = ao_Node.getAttribute("PDisName");
			this.NextActivityName = ao_Node.getAttribute("NActName");
			this.NextDisplayName = ao_Node.getAttribute("NDisName");
			this.FlowWidth = Integer.parseInt(ao_Node.getAttribute("Width"));
			this.FlowHeight = Integer.parseInt(ao_Node.getAttribute("Height"));

			NodeList lo_List = ao_Node.getElementsByTagName("Line");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CFlowLine lo_Line = new CFlowLine(this.Logon);
				lo_Line.Flow = this;
				lo_Line.Import(lo_Node, ai_Type);
				this.FlowLines.add(lo_Line);
				if (this.FlowLineMaxID < lo_Line.LineID)
					this.FlowLineMaxID = lo_Line.LineID;
				lo_Line = null;
			}
		} else {
			this.FlowID = Integer.parseInt(ao_Node.getAttribute("FlowID"));
			this.FlowName = ao_Node.getAttribute("FlowName");
			this.FlowLineMaxID = Integer.parseInt(ao_Node
					.getAttribute("FlowLineMaxID"));
			this.ActivityMaxID = Integer.parseInt(ao_Node
					.getAttribute("ActivityMaxID"));
			this.ConditionMaxID = Integer.parseInt(ao_Node
					.getAttribute("ConditionMaxID"));
			this.Summary = ao_Node.getAttribute("Summary");
			this.FlowWidth = Integer
					.parseInt(ao_Node.getAttribute("FlowWidth"));
			this.FlowHeight = Integer.parseInt(ao_Node
					.getAttribute("FlowHeight"));
			this.FlowImage = ao_Node.getAttribute("FlowImage");
			this.DefaultFlow = Boolean.parseBoolean(ao_Node
					.getAttribute("DefaultFlow"));
			this.SpecialActivityName = ao_Node
					.getAttribute("SpecialActivityName");
			this.SpecialDisplayName = ao_Node
					.getAttribute("SpecialDisplayName");
			this.PreActivityName = ao_Node.getAttribute("PreActivityName");
			this.PreDisplayName = ao_Node.getAttribute("PreDisplayName");
			this.NextActivityName = ao_Node.getAttribute("NextActivityName");
			this.NextDisplayName = ao_Node.getAttribute("NextDisplayName");
			this.Changed = Boolean
					.parseBoolean(ao_Node.getAttribute("Changed"));

			// 导入步骤
			NodeList lo_List = ao_Node.getElementsByTagName("Activity");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CActivity lo_Activity = new CActivity(this.Logon);
				lo_Activity.Flow = this;
				lo_Activity.Import(lo_Node, ai_Type);
				this.Activities.add(lo_Activity);
				if (this.ActivityMaxID < lo_Activity.ActivityID) {
					this.ActivityMaxID = lo_Activity.ActivityID;
				}
			}

			// 导入连线
			lo_List = ao_Node.getElementsByTagName("Line");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CFlowLine lo_Line = new CFlowLine(this.Logon);
				lo_Line.Flow = this;
				lo_Line.Import(lo_Node, ai_Type);
				this.FlowLines.add(lo_Line);
				if (this.FlowLineMaxID < lo_Line.LineID)
					this.FlowLineMaxID = lo_Line.LineID;
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
	 * @throws Exception
	 */
	@Override
	protected void Export(Element ao_Node, int ai_Type) throws Exception {
		if (ai_Type == 1) {
			ao_Node.setAttribute("SActName", this.SpecialActivityName);
			ao_Node.setAttribute("SDisName", this.SpecialDisplayName);
			ao_Node.setAttribute("PActName", this.PreActivityName);
			ao_Node.setAttribute("PDisName", this.PreDisplayName);
			ao_Node.setAttribute("NActName", this.NextActivityName);
			ao_Node.setAttribute("NDisName", this.NextDisplayName);
			ao_Node.setAttribute("Width", Integer.toString(this.FlowWidth));
			ao_Node.setAttribute("Height", Integer.toString(this.FlowHeight));
			for (CFlowLine lo_Line : FlowLines) {
				Element lo_Node = ao_Node.getOwnerDocument().createElement(
						"Line");
				ao_Node.appendChild(lo_Node);
				lo_Line.Export(lo_Node, ai_Type);
			}
		} else {
			ao_Node.setAttribute("FlowID", String.valueOf(this.FlowID));
			ao_Node.setAttribute("FlowName", this.FlowName);
			ao_Node.setAttribute("FlowLineMaxID",
					String.valueOf(this.FlowLineMaxID));
			ao_Node.setAttribute("ActivityMaxID",
					String.valueOf(this.ActivityMaxID));
			ao_Node.setAttribute("ConditionMaxID",
					String.valueOf(this.ConditionMaxID));
			ao_Node.setAttribute("Summary", this.Summary);
			ao_Node.setAttribute("FlowWidth", String.valueOf(this.FlowWidth));
			ao_Node.setAttribute("FlowHeight", String.valueOf(this.FlowHeight));
			ao_Node.setAttribute("FlowImage", this.FlowImage);

			ao_Node.setAttribute("DefaultFlow",
					String.valueOf(this.DefaultFlow));

			ao_Node.setAttribute("SpecialActivityName",
					this.SpecialActivityName);
			ao_Node.setAttribute("SpecialDisplayName", this.SpecialDisplayName);
			ao_Node.setAttribute("PreActivityName", this.PreActivityName);
			ao_Node.setAttribute("PreDisplayName", this.PreDisplayName);
			ao_Node.setAttribute("NextActivityName", this.NextActivityName);
			ao_Node.setAttribute("NextDisplayName", this.NextDisplayName);

			// 需要调试
			ao_Node.setTextContent(this.RoleXml + this.PropXml + this.RightXml
					+ this.ScriptXml);

			// 导出步骤
			ao_Node.setAttribute("ActivitiesCount",
					String.valueOf(this.Activities.size()));
			for (CActivity lo_Activity : this.Activities) {
				Element lo_Node = ao_Node.getOwnerDocument().createElement(
						"Activity");
				ao_Node.appendChild(lo_Node);
				lo_Activity.Export(lo_Node, ai_Type);
			}

			// 导出连线
			ao_Node.setAttribute("FlowLinesCount",
					String.valueOf(this.FlowLines.size()));
			for (CFlowLine lo_Line : this.FlowLines) {
				Element lo_Node = ao_Node.getOwnerDocument().createElement(
						"Line");
				ao_Node.appendChild(lo_Node);
				lo_Line.Export(lo_Node, ai_Type);
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
	 * @throws Exception
	 */
	@Override
	public void Save(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		// Add Save Code...
		ao_Set.put("FlowID", this.FlowID);
		ao_Set.put("TemplateID", this.Cyclostyle.CyclostyleID);
		ao_Set.put("FlowName", this.FlowName);
		ao_Set.put("FlowType", this.FlowType);
		ao_Set.put("FlowDescribe", this.Summary);
		String ls_ErrMsg = IsValid(0);
		ao_Set.put("FlowIsValid", (ls_ErrMsg.equals("") ? 1 : 0));
		ao_Set.put("FlowImage", this.FlowImage == null ? null : this.FlowImage);
		ao_Set.put("DefaultFlow", this.DefaultFlow ? 1 : 0);
		// 步骤的保存在模板保存中处理
		// 保存连线
		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			ao_Set.put("FlowContent", this.WriteContent(ai_Type));
		} else {
			ao_Set.put("FlowContent", this.ExportToXml("Flow", ai_Type));
		}
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型：=0-正常打；=1-短属性；
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
				ls_Sql = "INSERT INTO FlowInst "
						+ "(WorkItemID, FlowID, TemplateID, FlowName, FlowType, FlowIsValid, "
						+ "DefaultFlow, FlowContent, FlowDescribe, FlowImage) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE FlowInst SET "
						+ "TemplateID = ?, FlowName = ?, FlowType = ?, FlowIsValid = ?, "
						+ "DefaultFlow = ?, FlowContent = ?, FlowDescribe = ?, FlowImage = ? "
						+ "WHERE WorkItemID = ? AND FlowID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO FlowDefine "
						+ "(FlowID, TemplateID, FlowName, FlowType, FlowIsValid, "
						+ "DefaultFlow, FlowContent, FlowDescribe, FlowImage) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE FlowDefine SET "
						+ "FlowName = ?, FlowType = ?, FlowIsValid = ?, "
						+ "DefaultFlow = ?, FlowContent = ?, FlowDescribe = ?, FlowImage = ? "
						+ "WHERE FlowID = ? AND TemplateID = ?";
			}
		}
		return ls_Sql;
	}

	/**
	 * 保存
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型：=0-正常打；=1-短属性；
	 * @param ab_Inst
	 *            是否实例
	 * @throws Exception
	 */
	public void Save(int ai_SaveType, int ai_Type, boolean ab_Inst,
			int ai_Update) throws Exception {
		String lo_State = this.getSaveState(ai_SaveType, ai_Type, ab_Inst);
		this.Save(lo_State, ai_SaveType, ai_Type, ab_Inst, 0);
	}

	/**
	 * 保存
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型：=0-正常打；=1-短属性；
	 * @param ab_Inst
	 *            是否实例
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws Exception
	 */
	public int Save(String str_State, final int ai_SaveType, final int ai_Type,
			final boolean ab_Inst, int ai_Update) throws Exception {
		final CFlow flow = this;

		List parasList = new ArrayList();
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				parasList.add(flow.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(flow.FlowID);
			}
			parasList.add(flow.Cyclostyle.CyclostyleID);
		} else {
			if (ai_SaveType == 0) {
				parasList.add(flow.FlowID);
				parasList.add(flow.Cyclostyle.CyclostyleID);
			}
		}

		parasList.add(flow.FlowName);
		parasList.add(flow.FlowType);

		if (ab_Inst) {
			parasList.add(1);
		} else {
			try {
				parasList.add((flow.IsValid(0).equals("") ? 1 : 0));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		parasList.add((flow.DefaultFlow ? 1 : 0));

		// 步骤的保存在模板保存中处理
		// 保存连线
		if (flow.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			try {
				parasList.add(flow.WriteContent(ai_Type));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				parasList.add(flow.ExportToXml("Flow", ai_Type));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		parasList.add(MGlobal.getDbValue(flow.Summary));
		parasList.add(MGlobal.getDbValue(flow.FlowImage));

		if (ab_Inst) {
			if (ai_SaveType == 1) {
				parasList.add(flow.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(flow.FlowID);
			}
		} else {
			if (ai_SaveType == 1) {
				parasList.add(flow.FlowID);
				parasList.add(flow.Cyclostyle.CyclostyleID);
			}
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
		this.Open(ao_Set, ai_Type, true);
	}

	/**
	 * 从..打开（重新实现）
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开类型
	 * @param ab_Template
	 *            打开内容：=true-打开模板；=false-打开实例；
	 * @return
	 * @throws Exception
	 */
	public boolean Open(Map<String, Object> ao_Set, int ai_Type,
			boolean ab_Template) throws Exception {
		this.FlowID = (Integer) ao_Set.get("FlowID");
		// this.Cyclostyle.setCyclostyleID(ao_Set.getInt("TemplateID"));
		this.FlowName = (String) ao_Set.get("FlowName");
		this.FlowType = (Short) ao_Set.get("FlowType");
		this.Summary = (String) ao_Set.get("FlowDescribe");
		// ao_Set.getInt("FlowIsValid");
		if (ao_Set.get("FlowImage") != null)
			this.FlowImage = (String) ao_Set.get("FlowImage");
		this.DefaultFlow = ((Short) ao_Set.get("DefaultFlow") == 1);

		// 打开步骤
		String ls_Sql = "";
		if (ab_Template) {
			ls_Sql = "SELECT * FROM FlowActivity WHERE FlowID = "
					+ Integer.toString(this.FlowID);
		} else {
			ls_Sql = "SELECT * FROM ActivityInst WHERE FlowID = "
					+ Integer.toString(this.FlowID) + " AND WorkItemID = "
					+ ao_Set.get("WorkItemID");
		}
		List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate().queryForList(
				ls_Sql);
		for (int i = 0; i < lo_Set.size(); i++) {
			CActivity lo_Activity = new CActivity(this.Logon);
			lo_Activity.Flow = this;
			lo_Activity.Open(lo_Set.get(i), ai_Type);
			this.Activities.add(lo_Activity);
			if (this.ActivityMaxID < lo_Activity.ActivityID) {
				this.ActivityMaxID = lo_Activity.ActivityID;
			}
		}
		lo_Set = null;

		// 打开流程连线
		String ls_Text = (String) ao_Set.get("FlowContent");
		if (this.Cyclostyle.OpenType == EDataHandleType.OrignType) {
			if (ls_Text.substring(0, 2).equals("FL")) {
				this.ReadContent(ls_Text.substring(2, ls_Text.length()), 1);
			} else {
				this.ReadContent(ls_Text, 0);
			}
		} else {
			this.ImportFormXml(ls_Text, 1);
		}

		return true;
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
			ao_Bag.Write("SAN", this.SpecialActivityName);
			ao_Bag.Write("SDN", this.SpecialDisplayName);
			ao_Bag.Write("PAN", this.PreActivityName);
			ao_Bag.Write("PDN", this.PreDisplayName);
			ao_Bag.Write("NAN", this.NextActivityName);
			ao_Bag.Write("NDN", this.NextDisplayName);
			ao_Bag.Write("FW", this.FlowWidth);
			ao_Bag.Write("FH", this.FlowHeight);
			// 导出连线
			ao_Bag.Write("FLC", this.FlowLines.size());
			for (CFlowLine lo_Line : this.FlowLines) {
				lo_Line.Write(ao_Bag, ai_Type);
			}
		} else {
			// ao_Bag.Write("ml_FlowID", this.FlowID);
			ao_Bag.Write("ms_FlowName", this.FlowName);
			ao_Bag.Write("ml_FlowLineMaxID", this.FlowLineMaxID);
			ao_Bag.Write("ml_ActivityMaxID", this.ActivityMaxID);
			ao_Bag.Write("ml_ConditionMaxID", this.ConditionMaxID);
			ao_Bag.Write("ms_Summary", this.Summary);
			ao_Bag.Write("ml_FlowWidth", this.FlowWidth);
			ao_Bag.Write("ml_FlowHeight", this.FlowHeight);
			ao_Bag.Write("ms_FlowImage", this.FlowImage);
			ao_Bag.Write("mb_DefaultFlow", this.DefaultFlow);
			ao_Bag.Write("ms_SpecialActivityName", this.SpecialActivityName);
			ao_Bag.Write("ms_SpecialDisplayName", this.SpecialDisplayName);
			ao_Bag.Write("ms_PreActivityName", this.PreActivityName);
			ao_Bag.Write("ms_PreDisplayName", this.PreDisplayName);
			ao_Bag.Write("ms_NextActivityName", this.NextActivityName);
			ao_Bag.Write("ms_NextDisplayName", this.NextDisplayName);

			// 导出步骤
			ao_Bag.Write("ActivitiesCount", this.Activities.size());
			for (CActivity lo_Activity : this.Activities) {
				lo_Activity.Write(ao_Bag, ai_Type);
			}

			// 导出连线
			ao_Bag.Write("FlowLinesCount", this.FlowLines.size());
			for (CFlowLine lo_Line : this.FlowLines) {
				lo_Line.Write(ao_Bag, ai_Type);
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
		if (ai_Type == 1) {
			this.SpecialActivityName = ao_Bag.ReadString("SAN");
			this.SpecialDisplayName = ao_Bag.ReadString("SDN");
			this.PreActivityName = ao_Bag.ReadString("PAN");
			this.PreDisplayName = ao_Bag.ReadString("PDN");
			this.NextActivityName = ao_Bag.ReadString("NAN");
			this.NextDisplayName = ao_Bag.ReadString("NDN");
			this.FlowWidth = ao_Bag.ReadInt("FW");
			this.FlowHeight = ao_Bag.ReadInt("FH");
			int li_Count = ao_Bag.ReadInt("FLC");
			for (int i = 0; i < li_Count; i++) {
				CFlowLine lo_Line = new CFlowLine(this.Logon);
				lo_Line.Flow = this;
				lo_Line.Read(ao_Bag, ai_Type);
				this.FlowLines.add(lo_Line);
				if (this.FlowLineMaxID < lo_Line.LineID) {
					this.FlowLineMaxID = lo_Line.LineID;
				}
				lo_Line = null;
			}
		} else {
			// this.FlowID = ao_Bag.Read("ml_FlowID")
			this.FlowName = ao_Bag.ReadString("ms_FlowName");
			this.FlowLineMaxID = ao_Bag.ReadInt("ml_FlowLineMaxID");
			this.ActivityMaxID = ao_Bag.ReadInt("ml_ActivityMaxID");
			this.ConditionMaxID = ao_Bag.ReadInt("ml_ConditionMaxID");
			this.Summary = ao_Bag.ReadString("ms_Summary");
			this.FlowWidth = ao_Bag.ReadInt("ml_FlowWidth");
			this.FlowHeight = ao_Bag.ReadInt("ml_FlowHeight");
			this.FlowImage = ao_Bag.ReadString("ms_FlowImage");
			this.DefaultFlow = ao_Bag.ReadBoolean("mb_DefaultFlow");
			this.SpecialActivityName = ao_Bag
					.ReadString("ms_SpecialActivityName");
			this.SpecialDisplayName = ao_Bag
					.ReadString("ms_SpecialDisplayName");
			this.PreActivityName = ao_Bag.ReadString("ms_PreActivityName");
			this.PreDisplayName = ao_Bag.ReadString("ms_PreDisplayName");
			this.NextActivityName = ao_Bag.ReadString("ms_NextActivityName");
			this.NextDisplayName = ao_Bag.ReadString("ms_NextDisplayName");

			// 导入步骤
			int li_Count = ao_Bag.ReadInt("ActivitiesCount");
			CActivity lo_Activity = new CActivity();
			for (int i = 1; i <= li_Count; i++) {
				lo_Activity = new CActivity(this.Logon);
				lo_Activity.Flow = this;
				lo_Activity.Read(ao_Bag, ai_Type);
				this.Activities.add(lo_Activity);
				if (this.ActivityMaxID < lo_Activity.ActivityID) {
					this.ActivityMaxID = lo_Activity.ActivityID;
				}
			}

			// 导入连线
			li_Count = ao_Bag.ReadInt("FlowLinesCount");
			CFlowLine lo_Line = new CFlowLine();
			for (int i = 1; i <= li_Count; i++) {
				lo_Line = new CFlowLine(this.Logon);
				lo_Line.Flow = this;
				lo_Line.Read(ao_Bag, ai_Type);
				this.FlowLines.add(lo_Line);
				if (this.FlowLineMaxID < lo_Line.LineID) {
					this.FlowLineMaxID = lo_Line.LineID;
				}
			}
		}
	}

	/**
	 * 插入特殊步骤
	 * 
	 * @param ai_Type
	 *            插入哪种特殊步骤<br>
	 *            =-2 表示转发给前一步骤[如果该步骤没有则创建]<br>
	 *            =-1 表示转发给后一步骤[如果该步骤没有则创建]<br>
	 *            =0 表示转发给特殊步骤[如果该步骤没有则创建]<br>
	 * @param ao_SourceActivity
	 *            触发步骤
	 * @return
	 * @throws Exception
	 */
	public CActivity insertSpecialActivity(int ai_Type,
			CActivity ao_SourceActivity) throws Exception {
		try {
			String ls_ActivityName = "";
			switch (ai_Type) {
			case -2:
				ls_ActivityName = this.PreActivityName;
				break;
			case -1:
				ls_ActivityName = this.NextActivityName;
				break;
			default: // 0
				ls_ActivityName = this.SpecialActivityName;
				break;
			}
			CActivity lo_Activity = getActivityByName(ls_ActivityName);
			if (lo_Activity != null) {// 已存在特殊步骤
				String ls_Str = lo_Activity.Transact.Parameter2;
				int i = ls_Str.indexOf("SourceActivity=\'") + 1;
				i = ls_Str.indexOf(";'", i);
				lo_Activity.Transact.setParameter2(ls_Str.substring(0, i)
						+ Integer.toString(ao_SourceActivity.ActivityID)
						+ ";"
						+ ls_Str.substring(ls_Str.length()
								- (ls_Str.length() - i), ls_Str.length() - i));
				return lo_Activity;
			}

			this.Logon.GlobalPara.IsInsideTransact = true;
			lo_Activity = insertActivity(ls_ActivityName,
					EActivityType.TransactActivity);
			this.Logon.GlobalPara.IsInsideTransact = false;

			switch (ai_Type) {
			case -2:
				lo_Activity.Participant.RangeType = EParticipantRangeType.ExistAllParticipant;
				lo_Activity.Transact.setUserRight(ao_SourceActivity.Transact
						.getNewBackRight());
				lo_Activity.Transact.SendLabel = "送回";
				break;
			case -1:
				lo_Activity.Participant.RangeType = EParticipantRangeType.ExistAllParticipant;
				lo_Activity.Transact.setUserRight(ao_SourceActivity.Transact
						.getNewSendRight());
				lo_Activity.Transact.SendLabel = ao_SourceActivity.Transact.SendLabel;
				break;
			default: // 0
				lo_Activity.Participant.Participant = ao_SourceActivity.Transact.SpecialUser;
				lo_Activity.Transact.setUserRight(ao_SourceActivity.Transact
						.getSpecialRight());
				lo_Activity.Transact.UseTransOnly = true;
				lo_Activity.Transact.setLapseToLabel("发送给");
				break;
			}
			lo_Activity.Transact
					.setParameter2("<Activity Special=\'"
							+ String.valueOf(ai_Type) + "\' SourceActivity=\'"
							+ Integer.toString(ao_SourceActivity.ActivityID)
							+ ";\' />");

			this.Changed = true;
			return lo_Activity;
		} catch (Exception ex) {
			this.Raise(ex, "insertSpecialActivity", "");
			return null;
		}
	}

	/**
	 * 删除步骤
	 * 
	 * @param al_ActivityID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteActivity(int al_ActivityID) throws Exception {
		try {
			CActivity lo_Activity = this.getActivityById(al_ActivityID);
			if (lo_Activity == null)
				return false;

			if (lo_Activity.ActivityType == EActivityType.StartActivity) {
				// 错误【1019】：开始类型步骤不能删除
				this.Raise(1019, "deleteActivity", "");
				return false;
			}

			// 删除步骤的连线
			for (int i = this.FlowLines.size(); i > 0; i--) {
				CFlowLine lo_Line = this.FlowLines.get(i - 1);
				if (lo_Line.SourceActivity == lo_Activity
						|| lo_Line.GoalActivity == lo_Activity) {
					if (deleteFlowLine(lo_Line.LineID) == false) {
						return false;
					}
					lo_Line = null;
				}
			}

			this.Activities.remove(lo_Activity);
			int li_Order = lo_Activity.OrderID;
			lo_Activity.ClearUp();
			lo_Activity = null;

			if (li_Order <= this.Activities.size()) {
				for (CActivity lo_Act : this.Activities) {
					if (lo_Act.OrderID > li_Order) {
						lo_Act.OrderID = lo_Act.OrderID - 1;
					}
				}
			}

			return true;
		} catch (Exception ex) {
			this.Raise(ex, "deleteActivity", "");
			return false;
		}
	}

	/**
	 * 插入连线
	 * 
	 * @param ao_SourceActivity
	 * @param ao_GoalActivity
	 * @return
	 * @throws Exception
	 */
	public CFlowLine insertFlowLine(CActivity ao_SourceActivity,
			CActivity ao_GoalActivity) throws Exception {
		try {
			if (ao_SourceActivity == null || ao_GoalActivity == null)
				return null;

			// 确定是否已存在连线
			for (CFlowLine lo_Line : this.FlowLines) {
				if ((lo_Line.SourceActivity == ao_SourceActivity)
						&& lo_Line.GoalActivity == ao_GoalActivity) {
					return lo_Line;
				}
			}

			// 插入连线
			CFlowLine lo_Line = new CFlowLine(this.Logon);
			lo_Line.Flow = this;
			this.FlowLineMaxID++;
			lo_Line.LineID = this.FlowLineMaxID;
			lo_Line.SourceActivity = ao_SourceActivity;
			// lo_Line.theSourceActivity.Move += lo_Line.theSourceActivity_Move;
			lo_Line.GoalActivity = ao_GoalActivity;
			// lo_Line.theGoalActivity.Move += lo_Line.theGoalActivity_Move;
			this.FlowLines.add(lo_Line);

			// 插入连线对应的条件
			ao_GoalActivity.insertCondition(ao_SourceActivity);

			return lo_Line;
		} catch (Exception ex) {
			ex.printStackTrace();
			this.Raise(ex, "insertFlowLine", ao_SourceActivity.ActivityName
					+ "->" + ao_GoalActivity.ActivityName);
			return null;
		}
	}

	/**
	 * 删除连线
	 * 
	 * @param al_LineID
	 * @return
	 */
	public boolean deleteFlowLine(int al_LineID) {
		try {
			CFlowLine lo_Line = this.getFlowLineById(al_LineID);
			if (lo_Line == null)
				return true;
			this.FlowLines.remove(lo_Line);

			for (CActivity lo_Activity : this.Activities) {
				if (!lo_Activity.deleteConditionByLine(lo_Line))
					return false;
			}
			lo_Line.ClearUp();
			lo_Line = null;
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 寻找连线(原SelectLineByPosition)
	 * 
	 * @param al_PointX
	 * @param al_PointY
	 * @return
	 */
	public CFlowLine getLineByPosition(int al_PointX, int al_PointY) {
		if (this.FlowLines.size() == 0) {
			return null;
		}

		for (CFlowLine lo_Line : this.FlowLines) {
			String[] ls_Xs = lo_Line.LinePointXs.split(";");
			String[] ls_Ys = lo_Line.LinePointYs.split(";");
			for (int i = 1; i < ls_Xs.length; i++) {
				if (MGlobal.isExist(ls_Xs[i])) {
					if (MGlobal.isInlineArea(al_PointX, al_PointY,
							Integer.parseInt(ls_Xs[i - 1]),
							Integer.parseInt(ls_Ys[i - 1]),
							Integer.parseInt(ls_Xs[i]),
							Integer.parseInt(ls_Ys[i]))) {
						return lo_Line;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 调整流程尺寸
	 * 
	 * @param al_Width
	 *            宽度
	 * @param al_Height
	 *            高度
	 */
	public void flowResize(int al_Width, int al_Height) {
		int ll_Width = getMinFlowWidth();
		int ll_Height = getMinFlowHeight();
		setFlowWidth(al_Width > ll_Width ? al_Width : ll_Width);
		setFlowHeight(al_Height > ll_Height ? al_Height : ll_Height);
	}

	/**
	 * 排列流程步骤的顺序
	 * 
	 * @param as_ActivityIDs
	 *            步骤标识的连接串，使用“;”分隔
	 */
	public void arrangeActivityOrder(String as_ActivityIDs) {
		if (MGlobal.isEmpty(as_ActivityIDs)) {
			int i = 1;
			for (CActivity lo_Activity : this.Activities) {
				lo_Activity.OrderID = i;
				i++;
			}
		} else {
			for (CActivity lo_Activity : this.Activities) {
				lo_Activity.OrderID = 0;
			}
			String[] ls_Array = as_ActivityIDs.split(";");
			int j = 1;
			for (int i = 0; i < ls_Array.length; i++) {
				if (MGlobal.isExist(ls_Array[i])) {
					CActivity lo_Activity = this.getActivityById(Integer
							.parseInt(ls_Array[i]));
					lo_Activity.OrderID = j;
					j++;
				}
			}
			for (CActivity lo_Activity : this.Activities) {
				if (lo_Activity.OrderID == 0) {
					lo_Activity.OrderID = j;
					j++;
				}
			}
		}
	}

}
