package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.ELaunchType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 启动步骤子对象流程对象模型中继承步骤对象的扩展对象，是步骤处理功能的一种表现形式。远程启动是用于异地两个两个服务器之间的公文
 * 处理，需要有邮件系统的支持以及本系统的服务才能完成，原理如下： 本地服务器公文 -> 打包公文数据 -> 本地流转系统本地邮件系统 发送发送公文数据 ->
 * 异地邮件系统接收公文数据并存储到异地流 转系统指定的目录 -> 异地流转系统服务解包公文数据
 * 
 * @author Jackie.Wang
 * 
 */
public class CLaunch extends CBase {

	/**
	 * 初始化
	 */
	public CLaunch() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CLaunch(CLogon ao_Logon) {
		super(ao_Logon);

		this.CurLaunchData = new CLaunchData(this.Logon);
		this.CurLaunchData.Launch = this;
		this.LaunchDatas.add(this.CurLaunchData);
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
	public CImpressData ImpressData = new CImpressData();

	/**
	 * 启动参数数据设置
	 */
	public List<CLaunchData> LaunchDatas = new ArrayList<CLaunchData>();

	/**
	 * 当前启动参数
	 */
	public CLaunchData CurLaunchData = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 启动服务器ID
	 */
	public int LaunchServiceID = 0;

	/**
	 * 启动参数类型
	 */
	public int LaunchType = ELaunchType.OneUserLaunchType;

	// #==========================================================================#
	// 常用过程或函数定义
	// #==========================================================================#

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
			this.Raise(e, "IsValid", null);
			return e.toString();
		}
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 所属的步骤
		this.Activity = null;

		// 所包含的数据传递对象
		if (this.ImpressData != null) {
			this.ImpressData.ClearUp();
			this.ImpressData = null;
		}

		// 启动参数数据设置
		if (this.LaunchDatas != null) {
			while (this.LaunchDatas.size() > 0) {
				CLaunchData lo_LaunchData = this.LaunchDatas.get(0);
				this.LaunchDatas.remove(lo_LaunchData);
				lo_LaunchData.ClearUp();
				lo_LaunchData = null;
			}
			this.LaunchDatas = null;
		}

		// 当前启动参数
		this.CurLaunchData = null;

		super.ClearUp();
	}

	/**
	 * 根据本地用户标识获取某一个启动参数ai_DataIndex 索引值
	 * 
	 * @param al_UserID
	 *            本地用户标识
	 * @return
	 */
	public CLaunchData GetLaunchDataByUserID(int al_UserID) {
		for (CLaunchData lo_Data : this.LaunchDatas) {
			if (lo_Data.UserID == al_UserID) {
				return lo_Data;
			}
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
		this.LaunchServiceID = Integer.parseInt(ao_Node
				.getAttribute("LaunchServiceID"));
		this.LaunchType = Integer.parseInt(ao_Node.getAttribute("LaunchType"));
		while (this.LaunchDatas.size() > 0) {
			this.LaunchDatas.remove(0);
		}
		NodeList lo_List = ao_Node.getElementsByTagName("LaunchData");
		Element lo_Node;
		for (int i = 0; i < lo_List.getLength(); i++) {
			lo_Node = (Element) lo_List.item(i);
			CLaunchData lo_Data = new CLaunchData(this.Logon);
			lo_Data.Launch = this;
			this.LaunchDatas.add(lo_Data);
			lo_Data.Import(lo_Node, ai_Type);
		}

		lo_Node = (Element) ao_Node.getElementsByTagName("ImpressData").item(0);
		this.ImpressData.Import(lo_Node, ai_Type);
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
		ao_Node.setAttribute("LaunchServiceID",
				Integer.toString(this.LaunchServiceID));
		ao_Node.setAttribute("LaunchDataCount",
				Integer.toString(this.LaunchDatas.size()));
		ao_Node.setAttribute("LaunchType", Integer.toString(this.LaunchType));
		for (CLaunchData lo_Data : this.LaunchDatas) {
			Element lo_Node = ao_Node.getOwnerDocument().createElement(
					"LaunchData");
			ao_Node.appendChild(lo_Node);
			lo_Data.Export(lo_Node, ai_Type);
		}
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
		if (ai_Type == 1) {
			ao_Bag.Write("ID", this.LaunchServiceID);
			ao_Bag.Write("LT", this.LaunchType);
			ao_Bag.Write("LDC", this.LaunchDatas.size());
		} else {
			ao_Bag.Write("ml_LaunchServiceID", this.LaunchServiceID);
			ao_Bag.Write("mi_LaunchType", this.LaunchType);
			ao_Bag.Write("LaunchDataCount", this.LaunchDatas.size());
		}
		for (CLaunchData lo_Data : this.LaunchDatas) {
			lo_Data.Write(ao_Bag, ai_Type);
		}
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
		int li_Count = 0;
		if (ai_Type == 1) {
			this.LaunchServiceID = ao_Bag.ReadInt("ID");
			li_Count = ao_Bag.ReadInt("LDC");
			if (li_Count == 0) {
				this.LaunchType = ELaunchType.OneUserLaunchType;
			} else {
				this.LaunchType = ao_Bag.ReadInt("LT");
			}
		} else {
			this.LaunchServiceID = ao_Bag.ReadInt("ml_LaunchServiceID");
			li_Count = ao_Bag.ReadInt("LaunchDataCount");
			if (li_Count == 0) {
				this.LaunchType = ELaunchType.OneUserLaunchType;
			} else {
				this.LaunchType = ao_Bag.ReadInt("mi_LaunchType");
			}
		}
		for (int i = 0; i < li_Count; i++) {
			CLaunchData lo_Data = new CLaunchData(this.Logon);
			lo_Data.Launch = this;
			this.LaunchDatas.add(lo_Data);
			lo_Data.Read(ao_Bag, ai_Type);
		}
		this.ImpressData.Read(ao_Bag, ai_Type);
	}
}
