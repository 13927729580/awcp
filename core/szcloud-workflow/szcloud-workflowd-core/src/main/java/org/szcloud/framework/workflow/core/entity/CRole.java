package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.ECaculateStatusType;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.ERoleType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 * 公文角色对象：公文处理对象模型中一些用户的使用在公文模板设计时无法确定实际参与人员，<br>
 * 而只能以其所拥有的功能而定，在实际流转中再做确定的公文参与者。它在动态的流转中运用于<br>
 * 流程步骤参与人的一部分，以使公文的流转可以在流转中才确定相应的处理人员。
 * 
 * @author Jackie.Wang
 * 
 */
public class CRole extends CBase {

	/**
	 * 初始化
	 */
	public CRole() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CRole(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属的公文模板对象
		this.Cyclostyle = null;

		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 角色标识
	 */
	public int RoleID = -1;

	/**
	 * 角色名称
	 */
	public String RoleName = "";

	/**
	 * 设置角色名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setRoleName(String value) throws Exception {
		if (value == null) {
			this.Raise(1093, "RoleName", "");
			return;
		}

		if (this.RoleName == value) {
			return;
		}
		if (MGlobal.BeyondOfLength(value, 50) == false) {
			return;
		}

		for (CRole lo_Role : Cyclostyle.Roles) {
			if (lo_Role.RoleName.equals(value)) {
				this.Raise(1094, "RoleName", "");
				return;
			}
		}

		this.RoleName = value;
	}

	/**
	 * 角色类型
	 */
	public int RoleType = ERoleType.AllUsers;

	/**
	 * 角色初始值
	 */
	public String InitValue = "";

	/**
	 * 读取显示角色初始值
	 * 
	 * @throws Exception
	 */
	public String getDisplayInitValue() throws Exception {
		return this.Cyclostyle.convertUserValueToDisplay(this.InitValue);
	}

	/**
	 * 外部设置角色初始值
	 * 
	 * @throws Exception
	 */
	public void setExteriorInitValue(String value) throws Exception {
		this.InitValue = this.Cyclostyle.filterValidUserValue(value);
	}

	/**
	 * 角色初试计算内容
	 */
	public String InitCacuCont = "";

	/**
	 * 角色发送计算内容
	 */
	public String SendCacuCont = "";

	/**
	 * 角色值，格式为：
	 */
	public String Value = "";

	/**
	 * 外部设置角色值
	 * 
	 * @throws Exception
	 */
	public void setExteriorValue(String value) throws Exception {
		this.Value = this.Cyclostyle.filterValidUserValue(value);
	}

	/**
	 * 读取显示角色值
	 * 
	 * @throws Exception
	 */
	public String getDisplayValue() throws Exception {
		return this.Cyclostyle.convertUserValueToDisplay(this.Value);
	}

	/**
	 * 角色范围值，该值可以是计算公式计算出来，保证有一个动态范围
	 */
	public String RoleRange = "";

	/**
	 * 读取显示角色范围值，该值可以是计算公式计算出来，保证有一个动态范围
	 * 
	 * @throws Exception
	 */
	public String getDisplayRoleRange() throws Exception {
		return this.Cyclostyle.convertUserValueToDisplay(this.RoleRange);
	}

	/**
	 * 外部设置角色范围值，该值可以是计算公式计算出来，保证有一个动态范围
	 * 
	 * @throws Exception
	 */
	public void setExteriorRoleRange(String value) throws Exception {
		this.RoleRange = this.Cyclostyle.filterValidUserValue(value);
	}

	/**
	 * 角色计算状态类型，该数据为内存数据，无须保存
	 */
	public int CaculateStatus = ECaculateStatusType.NeedNotCaculate;

	/**
	 * 角色选取人员时的最大限制人数(=0表示没有限制)
	 */
	public int MaxLimitedNum = 0;

	/**
	 * 角色选取人员时的最大限制人数(=0表示没有限制)
	 * 
	 * @param value
	 */
	public void setMaxLimitedNum(int value) {
		if (value < 0) {
			this.MaxLimitedNum = 0;
		} else {
			this.MaxLimitedNum = value;
		}
	}

	/**
	 * 角色描述
	 */
	public String Summary = "";

	/**
	 * 角色选择范围限制类型（可以相加） =1 - 只选择用户 =2 - 只选择分组 =4 - 只选择系统角色 =8 - 只选择用户角色（公文中定义的）
	 */
	public int SelectRangeType = 15;

	/**
	 * 角色选择选择限制范围标识，与“多级权限应用范围MUTI_RIGHT_FLAG”对应起来
	 */
	public int FlagId = 0;

	/**
	 * 当前对象是否可用
	 * 
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			String ls_Msg = "";
			if (this.RoleType == ERoleType.HaveRange) {
				if (this.RoleRange == null || this.RoleRange.equals(""))
					ls_Msg += MGlobal.addSpace(ai_SpaceLength) + "角色【"
							+ this.RoleName
							+ "】有错误：当角色类型为有范围时，需要设置角色的选择范围或计算公式；\n";
			}
			return ls_Msg;
		} catch (Exception ex) {
			this.Raise(ex, "IsValid", null);
			return ex.toString();
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
		if (ai_Type == 0 && ao_Node.hasAttribute("RoleID")) {
			this.RoleID = Integer.parseInt(ao_Node.getAttribute("RoleID"));
			this.RoleName = ao_Node.getAttribute("RoleName");
			this.RoleType = Integer.parseInt(ao_Node.getAttribute("RoleType"));
			this.Summary = ao_Node.getAttribute("Summary");
		}

		this.InitValue = ao_Node.getAttribute("InitValue");
		this.InitCacuCont = ao_Node.getAttribute("InitCacuCont");
		this.SendCacuCont = ao_Node.getAttribute("SendCacuCont");
		this.Value = ao_Node.getAttribute("Value");
		this.RoleRange = ao_Node.getAttribute("RoleRange");
		this.CaculateStatus = Integer.parseInt(ao_Node
				.getAttribute("CaculateStatus"));
		this.MaxLimitedNum = Integer.parseInt(ao_Node
				.getAttribute("MaxLimitedNum"));
		if (ao_Node.hasAttribute("SelectRangeType")) {
			this.SelectRangeType = Integer.parseInt(ao_Node
					.getAttribute("SelectRangeType"));
		}
		if (this.SelectRangeType == 0) {
			this.SelectRangeType = 15;
		}
		if (ao_Node.getAttribute("FlagId") != null) {
			this.FlagId = Integer.parseInt(ao_Node.getAttribute("FlagId"));
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
	 * @throws DOMException
	 */
	@Override
	protected void Export(Element ao_Node, int ai_Type) throws DOMException,
			Exception {
		if (ai_Type == 0) {
			ao_Node.setAttribute("RoleID", String.valueOf(this.RoleID));
			ao_Node.setAttribute("RoleName", this.RoleName);
			ao_Node.setAttribute("RoleType", String.valueOf(this.RoleType));
			ao_Node.setAttribute("DisplayValue", this.getDisplayValue());
			ao_Node.setAttribute("Summary", this.Summary);
		}

		ao_Node.setAttribute("InitValue", this.InitValue);
		ao_Node.setAttribute("InitCacuCont", this.InitCacuCont);
		ao_Node.setAttribute("SendCacuCont", this.SendCacuCont);
		ao_Node.setAttribute("Value", this.Value);
		ao_Node.setAttribute("RoleRange", this.RoleRange);
		ao_Node.setAttribute("CaculateStatus",
				Integer.toString(this.CaculateStatus));
		ao_Node.setAttribute("MaxLimitedNum",
				Integer.toString(this.MaxLimitedNum));
		ao_Node.setAttribute("SelectRangeType",
				Integer.toString(this.SelectRangeType));
		ao_Node.setAttribute("FlagId", Integer.toString(this.FlagId));
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
		ao_Set.put("TemplateID", this.Cyclostyle.CyclostyleID);
		ao_Set.put("RoleID", this.RoleID);
		ao_Set.put("RoleName", this.RoleName);
		ao_Set.put("RoleType", this.RoleType);
		ao_Set.put("RoleDescribe", this.Summary.equals("") ? null
				: this.Summary);

		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				ao_Set.put("RoleContent",
						"CR" + this.WriteContent(ai_Type));
			} else {
				ao_Set.put("RoleContent", this.WriteContent(ai_Type));
			}
		} else {
			ao_Set.put("RoleContent",
					this.ExportToXml("Role", ai_Type));
		}
	}

	/**
	 * 获取保存数据库执行状态对象
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
			boolean ab_Inst) {
		String ls_Sql = null;
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO RoleInst "
						+ "(WorkItemID, RoleID, RoleName, RoleType, RoleContent, RoleDescribe) "
						+ "VALUES (?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE RoleInst SET "
						+ "RoleName = ?, RoleType = ?, RoleContent = ?, RoleDescribe = ? "
						+ "WHERE WorkItemID = ? AND RoleID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO RoleList "
						+ "(TemplateID, RoleID, RoleName, RoleType, RoleContent, RoleDescribe) "
						+ "VALUES (?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE RoleList SET "
						+ "RoleName = ?, RoleType = ?, RoleContent = ?, RoleDescribe = ? "
						+ "WHERE TemplateID = ? AND RoleID = ?";
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
	 *            存储类型
	 * @param ab_Inst
	 *            是否实例
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws Exception
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type,
			boolean ab_Inst, int ai_Update) throws Exception {
		List parasList = new ArrayList();
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.RoleID);
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.RoleID);
			}
		}

		parasList.add(this.RoleName);
		parasList.add(this.RoleType);
		parasList.add(this.Summary.equals("") ? null : this.Summary);

		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				parasList.add("CR" + this.WriteContent(ai_Type));
			} else {
				parasList.add(this.WriteContent(ai_Type));
			}
		} else {
			parasList.add(this.ExportToXml("Role", ai_Type));
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
		// this.Cyclostyle.setCyclostyleID(ao_Set.getInt("TemplateID"));
		this.RoleID = (Integer) ao_Set.get("RoleID");
		this.RoleName = (String) ao_Set.get("RoleName");
		this.RoleType = (Short) ao_Set.get("RoleType");
		if (ao_Set.get("RoleDescribe") != null) {
			this.Summary = (String) ao_Set.get("RoleDescribe");
		}

		String ls_Text = (String) ao_Set.get("RoleContent");
		if (this.Cyclostyle.OpenType == EDataHandleType.OrignType) {
			if (ls_Text.substring(0, 2).equals("CR")) {
				this.ReadContent(ls_Text.substring(0, ls_Text.length()),
						ai_Type);

			} else {
				this.ReadContent(ls_Text, ai_Type);
			}
		} else {
			this.ImportFormXml(ls_Text, ai_Type);
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
		if (ai_Type == 0) {
			ao_Bag.Write("ml_RoleID", this.RoleID);
			ao_Bag.Write("ms_RoleName", this.RoleName);
			ao_Bag.Write("mi_RoleType", this.RoleType);
			ao_Bag.Write("ms_Summary", this.Summary);
		}
		ao_Bag.Write("ms_InitValue", this.InitValue);
		ao_Bag.Write("ms_InitCacuCont", this.InitCacuCont);
		ao_Bag.Write("ms_SendCacuCont", this.SendCacuCont);
		ao_Bag.Write("ms_Value", this.Value);
		ao_Bag.Write("ms_RoleRange", this.RoleRange);
		ao_Bag.Write("mi_CaculateStatus", this.CaculateStatus);
		ao_Bag.Write("mi_MaxLimitedNum", this.MaxLimitedNum);
		ao_Bag.Write("mi_SelectRangeType", this.SelectRangeType);
		ao_Bag.Write("ml_FlagId", this.FlagId);
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
		if (ai_Type == 0) {
			this.RoleID = ao_Bag.ReadInt("ml_RoleID");
			this.RoleName = ao_Bag.ReadString("ms_RoleName");
			this.RoleType = ao_Bag.ReadInt("mi_RoleType");
			this.Summary = ao_Bag.ReadString("ms_Summary");
		}
		this.InitValue = ao_Bag.ReadString("ms_InitValue");
		this.InitCacuCont = ao_Bag.ReadString("ms_InitCacuCont");
		this.SendCacuCont = ao_Bag.ReadString("ms_SendCacuCont");
		this.Value = ao_Bag.ReadString("ms_Value");
		this.RoleRange = ao_Bag.ReadString("ms_RoleRange");
		this.CaculateStatus = ao_Bag.ReadInt("mi_CaculateStatus");
		this.MaxLimitedNum = ao_Bag.ReadInt("mi_MaxLimitedNum");
		this.SelectRangeType = ao_Bag.ReadInt("mi_SelectRangeType");
		this.FlagId = ao_Bag.ReadInt("ml_FlagId");
	}

}
