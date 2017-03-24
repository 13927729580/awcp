package org.szcloud.framework.workflow.core.base;


import java.util.Map;

import org.szcloud.framework.workflow.core.business.TPublicFunc;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 基础实体类，提供给各应用实体类继承使用
 */
public abstract class CBase {

	/**
	 * 基础登录类
	 */
	public CLogon Logon = null;

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public CBase() {
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public CBase(CLogon ao_Logon) {
		this.Logon = ao_Logon;
	}

	/**
	 * 注销
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		this.Logon = null;
	}

	/**
	 * 获取错误处理类
	 * 
	 * @return
	 */
	public CError Error() {
		if (this.Logon == null)
			return null;
		return this.Logon.Error;
	}

	/**
	 * 获取全局变量类
	 * 
	 * @return
	 */
	public CGlobalPara GlobalPara() {
		if (this.Logon == null)
			return null;
		return this.Logon.GlobalPara;
	}
	
	/**
	 * 获取公共函数操作类
	 * 
	 * @return
	 */
	public TPublicFunc PublicFunc() {
		if (this.Logon == null)
			return null;
		return this.Logon.getPublicFunc();
	}

	/**
	 * 从Xml内容导入到对象
	 * 
	 * @param as_XmlData
	 *            导入Xml数据
	 * @param ai_Type
	 *            导入类型：=0-正常导入；=1-短属性导入；
	 */
	public void ImportFormXml(String as_XmlData, int ai_Type) throws Exception {
		Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
		if (lo_Xml == null)
			return;
		this.Import(lo_Xml.getDocumentElement(), ai_Type);
		lo_Xml = null;
	}

	/**
	 * 从Xml节点导入到对象
	 * 
	 * @param ao_Node
	 *            导入节点
	 * @param ai_Type
	 *            导入类型：=0-正常导入；=1-短属性导入；
	 */
	protected abstract void Import(Element ao_Node, int ai_Type)
			throws Exception;

	/**
	 * 从对象导出到Xml内容
	 * 
	 * @param as_Name
	 *            Xml节点名称
	 * @param ai_Type
	 *            导出类型：=0-正常导出；=1-短属性导出；
	 * @return 导出的Xml内容
	 */
	public String ExportToXml(String as_Name, int ai_Type) throws Exception {
		Document lo_Xml = MXmlHandle.LoadXml("<" + as_Name + " />");
		this.Export(lo_Xml.getDocumentElement(), ai_Type);
		String ls_Xml = MXmlHandle.getXml(lo_Xml);
		lo_Xml = null;
		return ls_Xml;
	}

	/**
	 * 从对象导出到Xml节点
	 * 
	 * @param ao_Node
	 *            导出节点
	 * @param ai_Type
	 *            导出类型：=0-正常导出；=1-短属性导出；
	 */
	protected abstract void Export(Element ao_Node, int ai_Type)
			throws Exception;

	/**
	 * 对象保存到数据库行对象中
	 * 
	 * @param ao_Set
	 *            保存的结果集
	 * @param ai_Type
	 *            保存方式：=0-正常保存；=1-短属性保存；
	 */
	protected abstract void Save(Map<String, Object> ao_Set, int ai_Type)
			throws Exception;

	/**
	 * 从数据库行对象中读取数据到对象
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开方式：=0-正常打开；=1-短属性打开；
	 */
	public abstract void Open(Map<String, Object> ao_Set, int ai_Type)
			throws Exception;

	/**
	 * 对象打包文本串
	 * 
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 * @return
	 */
	public String WriteContent(int ai_Type) throws Exception {
		MBag lo_Bag = new MBag("");
		this.Write(lo_Bag, ai_Type);
		String ls_Text = lo_Bag.Content;
		lo_Bag.ClearUp();
		lo_Bag = null;
		return ls_Text;
	}

	/**
	 * 对象打包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 */
	protected abstract void Write(MBag ao_Bag, int ai_Type) throws Exception;

	/**
	 * 文本串解包对象
	 * 
	 * @param as_Text
	 *            导入内容
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 * @return
	 */
	public void ReadContent(String as_Text, int ai_Type) throws Exception {
		MBag lo_Bag = new MBag(as_Text);
		this.Read(lo_Bag, ai_Type);
		lo_Bag.ClearUp();
		lo_Bag = null;
	}

	/**
	 * 对象解包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导入；=1-短属性导入；
	 */
	protected abstract void Read(MBag ao_Bag, int ai_Type) throws Exception;

	/**
	 * 错误处理函数
	 * 
	 * @param al_ErrorId
	 *            错误编号
	 * @param as_Position
	 *            产生位置
	 * @param as_Parameter
	 *            执行引起参数
	 * @throws Exception 
	 */
	public void Raise(int al_ErrorId, String as_Position, String as_Parameter) throws Exception {
		this.Error().Raise(al_ErrorId, this, as_Position, as_Parameter);
	}

	/**
	 * 错误处理函数
	 * 
	 * @param ao_Exception
	 *            错误对象
	 * @param ao_Base
	 *            发生对象
	 * @param as_Position
	 *            产生位置
	 * @param as_Parameter
	 *            执行引起参数
	 * @throws Exception 
	 */
	public void Raise(Exception ao_Exception, String as_Position,
			String as_Parameter) throws Exception {
		this.Error().Raise(ao_Exception, this, as_Position, as_Parameter);
	}

	/**
	 * 记录错误
	 * 
	 * @param al_ErrorId
	 *            错误编号
	 * 
	 * @param ao_Base
	 *            发生对象
	 * 
	 * @param as_Position
	 *            产生位置
	 * 
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public void Record(int al_ErrorId, String as_Position, String as_Parameter) {
		this.Error().Record(al_ErrorId, this, as_Position, as_Parameter);
	}

	/**
	 * 记录错误
	 * 
	 * @param ao_Exception
	 *            错误对象
	 * 
	 * @param ao_Base
	 *            发生对象
	 * 
	 * @param as_Position
	 *            产生位置
	 * 
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public void Record(Exception ao_Exception, String as_Position,
			String as_Parameter) {
		this.Error().Record(ao_Exception, this, as_Position, as_Parameter);
	}

	/**
	 * 记录日志
	 * 
	 * @param as_Position
	 *            产生位置
	 * 
	 * @param as_Parameter
	 *            执行引起参数
	 */
	public void Record(String as_Position, String as_Parameter) {
		this.Error().Record(this, as_Position, as_Parameter);
	}
}
