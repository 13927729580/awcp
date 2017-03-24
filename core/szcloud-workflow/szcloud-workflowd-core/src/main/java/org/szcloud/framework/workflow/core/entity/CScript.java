package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 二次开发函数对象：流转系统中存储二次开发函数的对象。
 * 
 * @author Jackie.Wang
 * 
 */
public class CScript extends CBase {

	/**
	 * 初始化
	 */
	public CScript() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CScript(CLogon ao_Logon) {
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
	 * Script函数标识
	 */
	public int ScriptID = 1;

	/**
	 * Script函数名称
	 */
	public String ScriptName = "";

	/**
	 * 设置Script函数名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setScriptName(String value) throws Exception {
		if (value == null) {
			// 错误[1022]：二次开发的名称不能为空
			this.Raise(1022, "setScriptName", "");
			return;
		}

		if (MGlobal.validString(value, "~`!@#$%^&*-+=\\|?/>.<{}[]\"\':;") == false) {
			return;
		}

		for (CScript lo_Script : Cyclostyle.Scripts) {
			if (!(lo_Script == this)) {
				if (lo_Script.ScriptName.equals(value)) {
					// 错误[1023]：二次开发的名称不能重复
					this.Raise(1023, "setScriptName", "");
					return;
				}
			}
		}

		this.ScriptName = value;
	}

	/**
	 * Script函数内容
	 */
	public String ScriptContent = "";

	/**
	 * Script函数描述
	 */
	public String Summary = "";

	/**
	 * 当前对象是否可用
	 * 
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			String ls_Msg = "";
			if (this.ScriptContent == null) {
				ls_Msg = MGlobal.addSpace(ai_SpaceLength) + "Script函数“"
						+ this.ScriptName + "”有错误：Script函数内容不能为空；" + "\n";
			} else {
				// 判断函数内容是否存在语法错误
				String ls_Result = validScript();
				if (ls_Result != "") {
					ls_Msg = MGlobal.addSpace(ai_SpaceLength) + "Script函数“"
							+ this.ScriptName + "”有错误：Script函数内容语法错误("
							+ ls_Result + ")；" + "\n";
				}
			}
			return ls_Msg;
		} catch (Exception ex) {
			this.Raise(ex, "IsValid", null);
			return ex.toString();
		}
	}

	/**
	 * 判断Script函数语法是否正确，返回值为空表示正确
	 */
	private String validScript() {
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
		this.ScriptID = Integer.parseInt(ao_Node.getAttribute("ScriptID"));
		this.ScriptName = ao_Node.getAttribute("ScriptName");
		this.ScriptContent = ao_Node.getAttribute("ScriptContent");
		this.Summary = ao_Node.getAttribute("Summary");
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
		ao_Node.setAttribute("ScriptID", String.valueOf(this.ScriptID));
		ao_Node.setAttribute("ScriptName", this.ScriptName);
		ao_Node.setAttribute("ScriptContent", this.ScriptContent);
		ao_Node.setAttribute("Summary", this.Summary);
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
	public void Save(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		// Add Save Code...
		ao_Set.put("TemplateID", this.Cyclostyle.CyclostyleID);
		ao_Set.put("ScriptID", this.ScriptID);
		ao_Set.put("ScriptName", this.ScriptName);
		ao_Set.put("ScriptContent", this.ScriptContent);
		ao_Set.put("ScriptDescribe", this.Summary == null ? null : this.Summary);
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
				ls_Sql = "INSERT INTO ScriptInst "
						+ "(WorkItemID, ScriptID, ScriptName, ScriptContent, ScriptDescribe) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE ScriptInst SET "
						+ "ScriptName = ?, ScriptContent = ?, ScriptDescribe = ? "
						+ "WHERE WorkItemID = ? AND ScriptID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO ScriptList "
						+ "(TemplateID, ScriptID, ScriptName, ScriptContent, ScriptDescribe) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE ScriptList SET "
						+ "ScriptName = ?, ScriptContent = ?, ScriptDescribe = ? "
						+ "WHERE TemplateID = ? AND ScriptID = ?";
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
				parasList.add(this.ScriptID);
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.ScriptID);
			}
		}

		parasList.add(this.ScriptName);
		parasList.add(this.ScriptContent);
		parasList.add(this.Summary == null ? null : this.Summary);

		if (ab_Inst) {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.ScriptID);
			}
		} else {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.ScriptID);
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
	 */
	@Override
	public void Open(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		// this.Cyclostyle.CyclostyleID = .Fields("TemplateID").Value
		this.ScriptID = (Integer) ao_Set.get("ScriptID");
		this.ScriptName = (String) ao_Set.get("ScriptName");
		this.ScriptContent = (String) ao_Set.get("ScriptContent");
		if (ao_Set.get("ScriptDescribe") != null) {
			this.Summary = (String) ao_Set.get("ScriptDescribe");
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
		ao_Bag.Write("ml_ScriptID", this.ScriptID);
		ao_Bag.Write("ms_ScriptName", this.ScriptName);
		ao_Bag.Write("ms_ScriptContent", this.ScriptContent);
		ao_Bag.Write("ms_Summary", this.Summary);
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
		this.ScriptID = ao_Bag.ReadInt("ml_ScriptID");
		this.ScriptName = ao_Bag.ReadString("ms_ScriptName");
		this.ScriptContent = ao_Bag.ReadString("ms_ScriptContent");
		this.Summary = ao_Bag.ReadString("ms_Summary");
	}

}
