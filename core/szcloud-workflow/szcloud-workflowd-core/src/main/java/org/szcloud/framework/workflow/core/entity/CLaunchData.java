package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.module.MBag;
import org.w3c.dom.Element;

/**
 * 启动步骤多用户数据定义对象
 * 
 * @author Jackie.Wang
 * 
 */
public class CLaunchData extends CBase {
	/**
	 * 初始化
	 */
	public CLaunchData() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CLaunchData(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 所属的启动步骤子对象
		this.Launch = null;

		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的启动步骤子对象
	 */
	public CLaunch Launch = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 数据索引
	 */
	public int DataIndex = 0;

	/**
	 * 启动服务器代码
	 */
	public String LaunchServiceCode = "";

	/**
	 * 启动服务器名称
	 */
	public String LaunchServiceName = "";

	/**
	 * 启动服务器的IP地址
	 */
	public String LaunchServiceIP = "";

	/**
	 * 启动服务器收件人的电子邮件地址
	 */
	public String LaunchServiceEMail = "";

	/**
	 * 远地启动用户名称
	 */
	public String LaunchUserName = "";

	/**
	 * 远地启动用户代码
	 */
	public String LaunchUserCode = "";

	/**
	 * 本地启动用户标识
	 */
	public int UserID = 0;

	/**
	 * 本地启动用户名称
	 */
	public String UserName = "";

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
			this.LaunchServiceCode = (ao_Node.getAttribute("SC"));
			this.LaunchServiceName = (ao_Node.getAttribute("SN"));
			this.LaunchServiceIP = (ao_Node.getAttribute("IP"));
			this.LaunchServiceEMail = (ao_Node.getAttribute("SM"));
			this.LaunchUserName = (ao_Node.getAttribute("LUN"));
			this.LaunchUserCode = (ao_Node.getAttribute("UC"));
			this.UserID = Integer.parseInt(ao_Node.getAttribute("UI"));
			this.UserName = (ao_Node.getAttribute("UN"));
		} else {
			this.LaunchServiceCode = (ao_Node.getAttribute("LaunchServiceCode"));
			this.LaunchServiceName = (ao_Node.getAttribute("LaunchServiceName"));
			this.LaunchServiceIP = (ao_Node.getAttribute("LaunchServiceIP"));
			this.LaunchServiceEMail = (ao_Node.getAttribute("LaunchServiceEMail"));
			this.LaunchUserName = (ao_Node.getAttribute("LaunchUserName"));
			this.LaunchUserCode = (ao_Node.getAttribute("LaunchUserCode"));
			this.UserID = Integer.parseInt(ao_Node.getAttribute("UserID"));
			this.UserName = (ao_Node.getAttribute("UserName"));
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
			ao_Node.setAttribute("SC", this.LaunchServiceCode);
			ao_Node.setAttribute("SN", this.LaunchServiceName);
			ao_Node.setAttribute("IP", this.LaunchServiceIP);
			ao_Node.setAttribute("SM", this.LaunchServiceEMail);
			ao_Node.setAttribute("LUN", this.LaunchUserName);
			ao_Node.setAttribute("UC", this.LaunchUserCode);
			ao_Node.setAttribute("UI", Integer.toString(this.UserID));
			ao_Node.setAttribute("UN", this.UserName);
		} else {
			ao_Node.setAttribute("LaunchServiceCode", this.LaunchServiceCode);
			ao_Node.setAttribute("LaunchServiceName", this.LaunchServiceName);
			ao_Node.setAttribute("LaunchServiceIP", this.LaunchServiceIP);
			ao_Node.setAttribute("LaunchServiceEMail", this.LaunchServiceEMail);
			ao_Node.setAttribute("LaunchUserName", this.LaunchUserName);
			ao_Node.setAttribute("LaunchUserCode", this.LaunchUserCode);
			ao_Node.setAttribute("UserID", Integer.toString(this.UserID));
			ao_Node.setAttribute("UserName", this.UserName);
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
			ao_Bag.Write("SC", this.LaunchServiceCode);
			ao_Bag.Write("SN", this.LaunchServiceName);
			ao_Bag.Write("IP", this.LaunchServiceIP);
			ao_Bag.Write("SM", this.LaunchServiceEMail);
			ao_Bag.Write("LUN", this.LaunchUserName);
			ao_Bag.Write("UC", this.LaunchUserCode);
			ao_Bag.Write("UI", this.UserID);
			ao_Bag.Write("UN", this.UserName);
		} else {
			ao_Bag.Write("ms_LaunchServiceCode", this.LaunchServiceCode);
			ao_Bag.Write("ms_LaunchServiceName", this.LaunchServiceName);
			ao_Bag.Write("ms_LaunchServiceIP", this.LaunchServiceIP);
			ao_Bag.Write("ms_LaunchServiceEMail", this.LaunchServiceEMail);
			ao_Bag.Write("ms_LaunchUserName", this.LaunchUserName);
			ao_Bag.Write("ms_LaunchUserCode", this.LaunchUserCode);
			ao_Bag.Write("ml_UserID", this.UserID);
			ao_Bag.Write("ms_UserName", this.UserName);
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
			this.LaunchServiceCode = ao_Bag.ReadString("SC");
			this.LaunchServiceName = ao_Bag.ReadString("SN");
			this.LaunchServiceIP = ao_Bag.ReadString("IP");
			this.LaunchServiceEMail = ao_Bag.ReadString("SM");
			this.LaunchUserName = ao_Bag.ReadString("LUN");
			this.LaunchUserCode = ao_Bag.ReadString("UC");
			this.UserID = ao_Bag.ReadInt("UI");
			this.UserName = ao_Bag.ReadString("UN");
		} else {
			this.LaunchServiceCode = ao_Bag.ReadString("ms_LaunchServiceCode");
			this.LaunchServiceName = ao_Bag.ReadString("ms_LaunchServiceName");
			this.LaunchServiceIP = ao_Bag.ReadString("ms_LaunchServiceIP");
			this.LaunchServiceEMail = ao_Bag.ReadString("ms_LaunchServiceEMail");
			this.LaunchUserName = ao_Bag.ReadString("ms_LaunchUserName");
			this.LaunchUserCode = ao_Bag.ReadString("ms_LaunchUserCode");
			this.UserID = ao_Bag.ReadInt("ml_UserID");
			this.UserName = ao_Bag.ReadString("ms_UserName");

		}
	}
}
