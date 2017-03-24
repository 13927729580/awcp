package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 嵌入步骤子对象：流程对象模型中继承步骤对象的扩展对象，是步骤处理功能的一种表现形式。
 * 
 * @author Jackie.Wang
 * 
 */
public class CTumbleIn extends CBase {

	/**
	 * 初始化
	 */
	public CTumbleIn() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CTumbleIn(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 所属的步骤
		this.Activity = null;

		// 所包含的数据传递对象
		if (!(this.ImpressData == null)) {
			this.ImpressData.ClearUp();
			this.ImpressData = null;
		}
		
		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的步骤
	 */
	public CActivity Activity = null;

	/**
	 * 所包含的数据传输对象
	 */
	public CImpressData ImpressData = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 是否使用父公文模板
	 */
	public boolean UseParentTemp = true;

	/**
	 * 如果不使用父公文模板，那么使用子公文模板的标识
	 */
	public int ChildCyclostyleID = 0;

	/**
	 * 读取如果不使用父公文模板，那么使用子公文模板的名称
	 */
	public String getChildCyclostyleName() {
		if (this.ChildCyclostyleID == 0)
			return "";
		return this.Logon.getWorkAdmin().getCyclostyleNameByID(
				this.ChildCyclostyleID);
	}

	/**
	 * 设置如果不使用父公文模板，那么使用子公文模板的名称
	 */
	public void setChildCyclostyleName(String value) {
		this.ChildCyclostyleID = this.Logon.getWorkAdmin().getCyclostyleIDByName(
				value);
	}

	/**
	 * 使用权限设置，是权限定义标识连接的字符串，用“；”分隔，该权限作用于使用同一公文模板 <br>
	 * 在嵌套子流程时，子流程开始步骤的权限
	 */
	private String ms_UserRight = "1;";

	/**
	 * 使用权限设置
	 * 
	 * @return
	 */
	public String getUserRight() {
		if (this.Activity.ActivityType == EActivityType.StartActivity)
			return ms_UserRight;

		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_UserRight).indexOf(";"
					+ String.valueOf(lo_Right.RightID) + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_UserRight = ls_Str;

		return ms_UserRight;
	}

	/**
	 * 使用权限设置
	 * 
	 * @param value
	 */
	public void setUserRight(String value) {
		if (this.Activity.ActivityType == EActivityType.StartActivity) {
			ms_UserRight = "2;";
			return;
		}
		if (value == null || value.equals("")) {
			ms_UserRight = "1;";
			return;
		}

		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value).indexOf(";"
					+ String.valueOf(lo_Right.RightID) + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		ms_UserRight = (ls_Str.equals("") ? "1;" : ls_Str);
	}

	/**
	 * 获取使用权限设置，是权限定义名称连接的字符串，用";"分隔
	 * 
	 * @return
	 */
	public String getUserRightName() {
		ms_UserRight = getUserRight();

		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_UserRight).indexOf(";"
					+ Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str += lo_Right.RightName + ";";
			}
		}
		return ls_Str;
	}

	/**
	 * 设置使用权限设置，是权限定义名称连接的字符串，用";"分隔
	 * 
	 * @param value
	 */
	public void setUserRightName(String value) {
		if (Activity.ActivityType == EActivityType.StartActivity) {
			return;
		}

		if (value == null || value == "") {
			ms_UserRight = "1;";
			return;
		}

		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_UserRight).indexOf(";" + lo_Right.RightName
					+ ";") + 1 > 0) {
				ls_Str += lo_Right.RightID + ";";
			}
		}

		ms_UserRight = (ls_Str.equals("") ? "1;" : ls_Str);
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
			return "";
		} catch (Exception e) {
			this.Raise(e, "IsValid()", null);
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
		this.UseParentTemp = Boolean.parseBoolean(ao_Node
				.getAttribute("UseParentTemp"));
		this.ChildCyclostyleID = Integer.parseInt(ao_Node
				.getAttribute("ChildCyclostyleID"));
		ms_UserRight = ao_Node.getAttribute("UserRight");
		NodeList lo_List = ao_Node.getElementsByTagName("ImpressData");
		for (int i = 0; i < lo_List.getLength(); i++) {
			Element lo_Node = (Element) lo_List.item(i);
			this.ImpressData.Import(lo_Node, ai_Type);
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
		ao_Node.setAttribute("UseParentTemp",
				Boolean.toString(this.UseParentTemp));
		ao_Node.setAttribute("ChildCyclostyleID",
				Integer.toString(this.ChildCyclostyleID));
		ao_Node.setAttribute("UserRight", ms_UserRight);
		Element lo_Node = ao_Node.getOwnerDocument().createElement("Data");
		ao_Node.appendChild(lo_Node);
		this.ImpressData.Export(lo_Node, ai_Type);
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
		ao_Bag.Write("mb_UseParentTemp", this.UseParentTemp);
		ao_Bag.Write("ml_ChildCyclostyleID", this.ChildCyclostyleID);
		ao_Bag.Write("ms_UserRight", ms_UserRight);

		this.ImpressData.Write(ao_Bag, ai_Type);
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
		this.UseParentTemp = ao_Bag.ReadBoolean("mb_UseParentTemp");
		this.ChildCyclostyleID = ao_Bag.ReadInt("ml_ChildCyclostyleID");
		ms_UserRight = ao_Bag.ReadString("ms_UserRight");

		this.ImpressData.Read(ao_Bag, ai_Type);
	}

}
