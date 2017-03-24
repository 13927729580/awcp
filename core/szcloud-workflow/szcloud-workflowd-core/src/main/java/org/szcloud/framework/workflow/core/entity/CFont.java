package org.szcloud.framework.workflow.core.entity;

import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 字体定义实体类
 * 
 * @author Jackie.Wang
 * 
 */
public class CFont {
	/**
	 * 是否粗体
	 */
	public boolean Bold = false;

	/**
	 * 字符集
	 */
	public String Charset = "gb2312";

	/**
	 * 是否斜体
	 */
	public boolean Italic = false;

	/**
	 * 字体名称
	 */
	public String Name = "宋体";

	/**
	 * 字体大小
	 */
	public double Size = 10.2;

	/**
	 * 是否具有删除线
	 */
	public boolean Strikethrough = false;

	/**
	 * 是否具有下划线
	 */
	public boolean Underline = false;

	/**
	 * 字体加重值
	 */
	public int Weight = 0;

	/**
	 * 导出字体到XML节点
	 * 
	 * @param ao_Node
	 *            Xml节点对象
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 * @return
	 */
	public boolean Export(Element ao_Node, int ai_Type) {
		if (ai_Type == 1) {
			ao_Node.setAttribute("FB", Boolean.toString(this.Bold));
			ao_Node.setAttribute("FC", this.Charset);
			ao_Node.setAttribute("FI", Boolean.toString(this.Italic));
			ao_Node.setAttribute("FN", this.Name);
			ao_Node.setAttribute("FS", Double.toString(this.Size));
			ao_Node.setAttribute("FK", Boolean.toString(this.Strikethrough));
			ao_Node.setAttribute("FU", Boolean.toString(this.Underline));
			ao_Node.setAttribute("FW", Integer.toString(this.Weight));
		} else {
			ao_Node.setAttribute("Bold", Boolean.toString(this.Bold));
			ao_Node.setAttribute("Charset", this.Charset);
			ao_Node.setAttribute("Italic", Boolean.toString(this.Italic));
			ao_Node.setAttribute("Name", this.Name);
			ao_Node.setAttribute("Size", Double.toString(this.Size));
			ao_Node.setAttribute("Strikethrough",
					Boolean.toString(this.Strikethrough));
			ao_Node.setAttribute("Underline", Boolean.toString(this.Underline));
			ao_Node.setAttribute("Weight", Integer.toString(this.Weight));
		}
		return true;
	}

	/**
	 * 从XML节点导入字体
	 * 
	 * @param ao_Node
	 *            Xml节点对象
	 * @param ai_Type
	 *            类型：=0-正常导入；=1-短属性导入；
	 * @return
	 */
	public boolean Import(Element ao_Node, int ai_Type) {
		if (ai_Type == 1 && ao_Node.hasAttribute("FB")) {
			this.Bold = ao_Node.getAttribute("FB").toLowerCase().equals("true");
			this.Charset = ao_Node.getAttribute("FC");
			this.Italic = ao_Node.getAttribute("FI").toLowerCase()
					.equals("true");
			this.Name = ao_Node.getAttribute("FN");
			this.Size = Double.parseDouble(ao_Node.getAttribute("FS"));
			this.Strikethrough = ao_Node.getAttribute("FK").toLowerCase()
					.equals("true");
			this.Underline = ao_Node.getAttribute("FU").toLowerCase()
					.equals("true");
			this.Weight = Integer.parseInt(ao_Node.getAttribute("FW"));
		} else {
			this.Bold = ao_Node.getAttribute("Bold").toLowerCase()
					.equals("true");
			this.Charset = ao_Node.getAttribute("Charset");
			this.Italic = ao_Node.getAttribute("Italic").toLowerCase()
					.equals("true");
			this.Name = ao_Node.getAttribute("Name");
			this.Size = Double.parseDouble(ao_Node.getAttribute("Size"));
			this.Strikethrough = ao_Node.getAttribute("Strikethrough")
					.toLowerCase().equals("true");
			this.Underline = ao_Node.getAttribute("Underline").toLowerCase()
					.equals("true");
			this.Weight = Integer.parseInt(ao_Node.getAttribute("Weight"));
		}
		return true;
	}

	/**
	 * 导出字体到XML内容
	 * 
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 * @return XML内容
	 */
	public String ExportXml(int ai_Type) {
		Document lo_Xml = MXmlHandle.LoadXml("<Font />");
		String ls_Xml = "";
		if (Export(lo_Xml.getDocumentElement(), ai_Type))
			ls_Xml = MXmlHandle.getXml(lo_Xml);
		lo_Xml = null;
		return ls_Xml;
	}

	/**
	 * 从XML内容导入字体
	 * 
	 * @param ai_Type
	 *            类型：=0-正常导入；=1-短属性导入；
	 * @param as_XmlData
	 *            XML内容
	 */
	public void ImportXml(int ai_Type, String as_XmlData) {
		Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
		Import(lo_Xml.getDocumentElement(), ai_Type);
		lo_Xml = null;
	}

	/**
	 * 导出字体
	 * 
	 * @param ao_Bag
	 *            数据包
	 * @param ai_Type
	 *            类型
	 * @return
	 */
	public boolean Write(MBag ao_Bag, int ai_Type) {
		if (ai_Type == 1) {
			ao_Bag.Write("FB", this.Bold);
			ao_Bag.Write("FC", this.Charset);
			ao_Bag.Write("FI", this.Italic);
			ao_Bag.Write("FN", this.Name);
			ao_Bag.Write("FS", this.Size);
			ao_Bag.Write("DK", this.Strikethrough);
			ao_Bag.Write("FU", this.Underline);
			ao_Bag.Write("FW", this.Weight);
		} else {
			ao_Bag.Write("Bold", this.Bold);
			ao_Bag.Write("Charset", this.Charset);
			ao_Bag.Write("Italic", this.Italic);
			ao_Bag.Write("Name", this.Name);
			ao_Bag.Write("Size", this.Size);
			ao_Bag.Write("Strikethrough", this.Strikethrough);
			ao_Bag.Write("Underline", this.Underline);
			ao_Bag.Write("Weight", this.Weight);
		}
		return true;
	}

	/**
	 * 导入字体
	 * 
	 * @param ao_Bag
	 *            数据包
	 * @param ai_Type
	 *            类型
	 * @return
	 */
	public boolean Read(MBag ao_Bag, int ai_Type) {
		if (ai_Type == 1) {
			this.Bold = (Boolean) ao_Bag.Read("FB");
			this.Charset = (String) ao_Bag.Read("FC");
			this.Italic = (Boolean) ao_Bag.Read("FI");
			this.Name = (String) ao_Bag.Read("FN");
			this.Size = (Double) ao_Bag.Read("FS");
			this.Strikethrough = (Boolean) ao_Bag.Read("FK");
			this.Underline = (Boolean) ao_Bag.Read("FU");
			this.Weight = ao_Bag.ReadInt("FW");
		} else {
			this.Bold = (Boolean) ao_Bag.Read("Bold");
			this.Charset = (String) ao_Bag.Read("Charset");
			this.Italic = (Boolean) ao_Bag.Read("Italic");
			this.Name = (String) ao_Bag.Read("Name");
			this.Size = (Double) ao_Bag.Read("Size");
			this.Strikethrough = (Boolean) ao_Bag.Read("Strikethrough");
			this.Underline = (Boolean) ao_Bag.Read("Underline");
			this.Weight = ao_Bag.ReadInt("Weight");
		}
		return true;
	}

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

}
