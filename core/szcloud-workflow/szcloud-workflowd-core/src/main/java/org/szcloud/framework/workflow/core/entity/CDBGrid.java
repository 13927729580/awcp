package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 表格处理对象 - 本对象提供当前系统的表单中的二次表格处理
 * 
 * @author Jackie.Wang
 * 
 */
public class CDBGrid extends CBase {

	/**
	 * 初始化
	 * 
	 */
	public CDBGrid() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public CDBGrid(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 所属的表格单元对象
	 */
	public CFormCell FormCell = null;

	/**
	 * 数据解释的XML对象
	 */
	private Document mo_XmlDefine = null;

	/**
	 * 读取数据解释的XML对象
	 * 
	 * @return
	 */
	public Document getXmlDefine() {
		return mo_XmlDefine;
	}

	/**
	 * 基本数据定义
	 */
	private Element mo_Base = null;

	/**
	 * 读取基本数据定义
	 * 
	 * @return
	 */
	public Element getBase() {
		return mo_Base;
	}

	/**
	 * 数据列定义
	 */
	private Element mo_Field = null;

	/**
	 * 读取数据列定义
	 * 
	 * @return
	 */
	public Element getField() {
		return mo_Field;
	}

	/**
	 * 数据内容定义
	 */
	private Element mo_Data = null;

	/**
	 * 读取数据内容定义
	 * 
	 * @return
	 */
	public Element getData() {
		return mo_Data;
	}

	/**
	 * 操作定义
	 */
	private Element mo_Handle = null;

	/**
	 * 读取操作定义
	 * 
	 * @return
	 */
	public Element getHandle() {
		return mo_Handle;
	}

	/**
	 * 公文对象
	 */
	private CWorkItem mo_WorkItem = null;

	/**
	 * 公文对象
	 * 
	 * @return
	 */
	public CWorkItem getWorkItem() {
		return mo_WorkItem;
	}

	/**
	 * 清除
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		this.FormCell = null;
		mo_XmlDefine = null;
		mo_Base = null;
		mo_Field = null;
		mo_Data = null;
		mo_Handle = null;
		mo_WorkItem = null;

		super.ClearUp();
	}

	/**
	 * 取得解释内容格式的XML字符串
	 * 
	 * @return
	 */
	public String getHtmlXmlStyle() {
		String ls_Xml = "<Style None=\'"
				+ MGlobal.stringToXml(mo_Base.getAttribute("EmptyDesc")
						.toString()) + "\'";
		ls_Xml += " Item=\'"
				+ MGlobal.stringToXml(mo_Base.getAttribute("ExistDesc")
						.toString()) + "\'>";

		for (int i = 0; i < mo_Field.getChildNodes().getLength(); i++) {
			Element lo_Node = (Element) mo_Field.getChildNodes().item(i);
			if (lo_Node.getNodeName() == "Column"
					&& lo_Node.getAttribute("DisplayType") == "1") {
				ls_Xml += "<Field Name=\'"
						+ MGlobal.stringToXml(lo_Node.getAttribute("Name"))
						+ "\'";
				ls_Xml += " Code=\'"
						+ MGlobal.stringToXml(lo_Node.getAttribute("Code"))
						+ "\'";
				ls_Xml += " Type=\'" + lo_Node.getAttribute("LType") + "\'";
				if (lo_Node.getAttribute("LWidth") != "")
					ls_Xml += " Width=\'" + lo_Node.getAttribute("LWidth")
							+ "\'";
				ls_Xml += " Class=\'" + lo_Node.getAttribute("LStyle") + "\'";
				if (lo_Node.getAttribute("LFormat") != "")
					ls_Xml += " Format=\'" + lo_Node.getAttribute("LFormat")
							+ "\'";
				String ls_Font = (lo_Node.getAttribute("Bold") == "1") ? "B"
						: "";
				ls_Font += (lo_Node.getAttribute("Italic") == "1" ? "I" : "");
				ls_Font += (lo_Node.getAttribute("Underline") == "1" ? "U" : "");
				ls_Xml += " Font=\'" + ls_Font + "\'";
				if (lo_Node.getAttribute("Align") == "1") {
					ls_Xml += " Align=\'right\'";
				} else if (lo_Node.getAttribute("Align") == "2") {
					ls_Xml += " Align=\'center\'";
				} else {
					ls_Xml += " Align=\'left\'";
				}
				if (lo_Node.getAttribute("Function") != "")
					ls_Xml += " onclick=\'"
							+ MGlobal.stringToXml(lo_Node
									.getAttribute("Function")) + "\'";
				if (lo_Node.getAttribute("URL") != "")
					ls_Xml += " href=\'"
							+ MGlobal.stringToXml(lo_Node.getAttribute("URL"))
							+ "\'";
				ls_Xml += ">";
				if (Integer.parseInt(lo_Node.getAttribute("LType").toString()) > 2) {
					for (int j = 0; j < lo_Node.getChildNodes().getLength(); j++) {
						Element lo_Child = (Element) lo_Node.getChildNodes()
								.item(j);
						ls_Xml += GetConditionStyle(lo_Child,
								Integer.parseInt(lo_Node.getAttribute("LType")));
					}
				}
				ls_Xml = ls_Xml + "</Field>";
			}
		}

		return ls_Xml + "</Style>";
	}

	/**
	 * 
	 * @param ao_Node
	 * @param ai_Type
	 * @return
	 */
	private String GetConditionStyle(Element ao_Node, int ai_Type) {
		String ls_Xml = "<Prop Code=\'" + ao_Node.getAttribute("Code") + "\'";
		ls_Xml += " Compare=\'"
				+ MGlobal.stringToXml(ao_Node.getAttribute("Compare")) + "\'";
		ls_Xml += " Value=\'"
				+ MGlobal.stringToXml(ao_Node.getAttribute("Value")) + "\'";
		if (ai_Type == 3 | ai_Type == 5) {
			ls_Xml += " Class=\'" + ao_Node.getAttribute("Class") + "\'";
			if (ao_Node.getAttribute("Color") != "")
				ls_Xml += " Color=\'" + ao_Node.getAttribute("Color") + "\'";
			if (ao_Node.getAttribute("bgColor") != "")
				ls_Xml += " bgColor=\'" + ao_Node.getAttribute("bgColor")
						+ "\'";
			String ls_Font = (ao_Node.getAttribute("Bold") == "1" ? "B" : "");
			ls_Font += (ao_Node.getAttribute("Italic") == "1" ? "I" : "");
			ls_Font += (ao_Node.getAttribute("Underline") == "1" ? "U" : "");
			ls_Xml += " Font=\'" + ls_Font + "\'";
			ls_Xml += " Display=\'"
					+ MGlobal.stringToXml(ao_Node.getAttribute("Content"))
					+ "\'>";
		} else {
			ls_Xml += " src=\'"
					+ MGlobal.stringToXml(ao_Node.getAttribute("Content"))
					+ "\'>";
		}
		if (ai_Type == 5 || ai_Type == 6) {
			for (int i = 0; i < ao_Node.getChildNodes().getLength(); i++) {
				Element lo_Node = (Element) ao_Node.getChildNodes().item(i);
				ls_Xml += GetConditionStyle(lo_Node, ai_Type);
			}
		}
		return ls_Xml + "</Prop>";
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

}
