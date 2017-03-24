package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 通知步骤子对象：流程对象模型中继承步骤对象的扩展对象，是步骤处理功能的一种表现形式，通知步骤只需要对步骤状态的只进行更改，而
 * 无须处理其他任何数据，对它所绑定的数据也只有查看的权限。
 * 
 * @author Jackie.Wang
 * 
 */
public class CFYI extends CBase {
	/**
	 * 初始化
	 */
	public CFYI() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CFYI(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 所属的步骤
		this.Activity = null;

		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的步骤
	 */
	public CActivity Activity = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
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
		if (MGlobal.BeyondOfLength(value, 255) == false) {
			return;
		}
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
	 * 使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 */
	private String ms_UserRight = "1;";

	/**
	 * 使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 * 
	 * @return
	 */
	public String getUserRight() {
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + ms_UserRight).indexOf(";"
					+ Integer.toString(lo_Right.RightID) + ";") + 1 > 0) {
				ls_Str += Integer.toString(lo_Right.RightID) + ";";
			}
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		return ls_Str;
	}

	/**
	 * 使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 * 
	 * @param value
	 */
	public void setUserRight(String value) {
		if (value == null) {
			ms_UserRight = "1;";
			return;
		}
		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value + ";").indexOf(";"
					+ String.valueOf(lo_Right.RightID) + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_UserRight = ls_Str;
	}

	/**
	 * 通知公文是否以公文传阅的形式存在
	 */
	public boolean FYIByRead = false;

	/**
	 * 读取使用权限设置，是权限定义名称连接的字符串，用“;”分隔
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
	 * 设置使用权限设置，是权限定义名称连接的字符串，用“;”分隔
	 * 
	 * @param value
	 */
	public void setUserRightName(String value) {
		if (value == null) {
			ms_UserRight = "1;";
			return;
		}

		String ls_Str = "";
		for (CRight lo_Right : this.Activity.Flow.Cyclostyle.Rights) {
			if ((";" + value + ";")
					.indexOf(";" + lo_Right.RightName + ";") > -1)
				ls_Str += String.valueOf(lo_Right.RightID) + ";";
		}
		if (ls_Str.equals(""))
			ls_Str = "1;";
		ms_UserRight = ls_Str;
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
		if (ai_Type == 1) {
			this.Parameter1 = ao_Node.getAttribute("P1");
			this.Parameter2 = ao_Node.getAttribute("P2");
			ms_UserRight = ao_Node.getAttribute("UR");
			if (ao_Node.getAttribute("FBR") != null) {
				this.FYIByRead = Boolean
						.parseBoolean(ao_Node.getAttribute("FBR"));
			}
		} else {
			this.Parameter1 = ao_Node.getAttribute("Parameter1");
			this.Parameter2 = ao_Node.getAttribute("Parameter2");
			ms_UserRight = ao_Node.getAttribute("UserRight");
			if (ao_Node.getAttribute("FYIByRead") != null) {
				this.FYIByRead = Boolean.parseBoolean(ao_Node
						.getAttribute("FYIByRead"));
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
			ao_Node.setAttribute("P1", this.Parameter1);
			ao_Node.setAttribute("P2", this.Parameter2);
			ao_Node.setAttribute("UR", ms_UserRight);
			ao_Node.setAttribute("FBR", Boolean.toString(this.FYIByRead));
		} else {
			ao_Node.setAttribute("Parameter1", this.Parameter1);
			ao_Node.setAttribute("Parameter2", this.Parameter2);
			ao_Node.setAttribute("UserRight", ms_UserRight);
			ao_Node.setAttribute("FYIByRead", Boolean.toString(this.FYIByRead));

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
			ao_Bag.Write("P1", this.Parameter1);
			ao_Bag.Write("P2", this.Parameter2);
			ao_Bag.Write("UR", ms_UserRight);
			ao_Bag.Write("FBR", this.FYIByRead);
		} else {
			ao_Bag.Write("ms_Parameter1", this.Parameter1);
			ao_Bag.Write("ms_Parameter2", this.Parameter2);
			ao_Bag.Write("ms_UserRight", ms_UserRight);
			ao_Bag.Write("mb_FYIByRead", this.FYIByRead);

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
			this.Parameter1 = ao_Bag.ReadString("P1");
			this.Parameter2 = ao_Bag.ReadString("P2");
			ms_UserRight = ao_Bag.ReadString("UR");
			this.FYIByRead = ao_Bag.ReadBoolean("FBR");
		} else {
			this.Parameter1 = ao_Bag.ReadString("ms_Parameter1");
			this.Parameter2 = ao_Bag.ReadString("ms_Parameter2");
			ms_UserRight = ao_Bag.ReadString("ms_UserRight");
			this.FYIByRead = ao_Bag.ReadBoolean("mb_FYIByRead");
		}
	}

}
