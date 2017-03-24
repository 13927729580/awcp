package org.szcloud.framework.workflow.core.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @category XML操作函数
 * @author Jackie.Wang
 * @date 2013-2-20
 * @version 1.0.0.0
 */
public class MXmlHandle {
	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(MXmlHandle.class);

	/**
	 * 写文件
	 * 
	 * @param as_FullFile
	 *            文件名称，包括路径
	 * @param as_XmlData
	 *            XML文件内容
	 * @return 返回是否成功的标记
	 */
	public static String SaveXml(String as_FullFile, String as_XmlData) {
		int i = as_FullFile.lastIndexOf("\\");
		String ls_Path = as_FullFile.substring(0, i + 1);
		String ls_File = as_FullFile.substring(i + 1, as_FullFile.length() - i - 1);
		return SaveXml(ls_Path, ls_File, as_XmlData);
	}

	/**
	 * 写文件
	 * 
	 * @param as_Path
	 *            文件路径
	 * @param as_FileName
	 *            文件名称，不包括路径
	 * @param as_XmlData
	 *            XML文件内容
	 * @return 返回是否成功的标记
	 */
	public static String SaveXml(String as_Path, String as_FileName, String as_XmlData) {
		try {
			File lo_Path = new File(as_Path);// 创建目录
			if (!lo_Path.exists()) // 目录不存在返回false
				lo_Path.mkdirs();// 创建一个目录
			lo_Path = null;

			FileOutputStream lo_Fos = new FileOutputStream(as_Path + as_FileName, false); // ,
																							// MODE_APPEND
			lo_Fos.write(as_XmlData.getBytes());
			lo_Fos.close();
			lo_Fos = null;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	/**
	 * 读文件
	 * 
	 * @param as_FullFile
	 *            文件名称，包括路径
	 * @return 返回XML对象
	 */
	public static Document ReadXml(String as_FullFile) {
		int i = as_FullFile.lastIndexOf("\\");
		String ls_Path = as_FullFile.substring(0, i + 1);
		String ls_File = as_FullFile.substring(i + 1, as_FullFile.length());
		return ReadXml(ls_Path, ls_File);
	}

	/**
	 * 读文件
	 * 
	 * @param as_Path
	 *            文件路径
	 * @param as_FileName
	 *            文件名称，不包括路径
	 * @return 返回XML对象
	 */
	public static Document ReadXml(String as_Path, String as_FileName) {
		try {
			InputStream lo_Stream = new FileInputStream(as_Path + as_FileName);
			DocumentBuilderFactory lo_Factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder lo_Builder = lo_Factory.newDocumentBuilder();
			Document lo_Doc = lo_Builder.parse(lo_Stream);
			lo_Stream.close();
			lo_Stream = null;
			lo_Builder = null;
			lo_Factory = null;

			return lo_Doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读XML内容
	 * 
	 * @param as_XmlData
	 *            XML内容
	 * @return 返回XML对象
	 */
	public static Document LoadXml(String as_XmlData) {
		try {
			DocumentBuilderFactory lo_Factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder lo_Builder = lo_Factory.newDocumentBuilder();
			Document lo_Doc = lo_Builder.parse(new InputSource(new StringReader(as_XmlData)));
			lo_Builder = null;
			lo_Factory = null;

			return lo_Doc;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取XML对象的XML内容
	 * 
	 * @param ao_Xml
	 *            XML对象
	 * @return XML内容
	 */
	public static String getXml(Document ao_Xml) {
		return getXml(ao_Xml.getDocumentElement());
	}

	/**
	 * 获取XML节点的XML内容
	 * 
	 * @param ao_Node
	 *            XML节点
	 * @return XML内容
	 */
	public static String getXml(Node ao_Node) {
		try {
			DOMSource lo_Source = new DOMSource(ao_Node);
			StringWriter lo_Writer = new StringWriter();
			Result lo_Result = new StreamResult(lo_Writer);
			Transformer lo_Transformer = TransformerFactory.newInstance().newTransformer();
			lo_Transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			lo_Transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");
			lo_Transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			lo_Transformer.transform(lo_Source, lo_Result);
			String ls_Value = lo_Writer.getBuffer().toString();
			lo_Result = null;
			lo_Transformer = null;
			lo_Writer = null;
			lo_Source = null;
			return ls_Value;
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 内部XML对象转成字符串
	 * 
	 * @param ao_Node
	 *            XML节点
	 * @return
	 */
	public static String getInterXml(Document ao_Xml) {
		return getInterXml(ao_Xml.getDocumentElement());
	}

	/**
	 * XML内部节点转成字符串
	 * 
	 * @param ao_Node
	 *            XML节点
	 * @return
	 */
	public static String getInterXml(Node ao_Node) {
		String ls_Value = getXml(ao_Node);
		if (ls_Value == null)
			return null;
		// 去除多余换行符和空格
		// ls_Value=ls_Value.replace("\r", "");
		// ls_Value=ls_Value.replace("\n", "");
		// ls_Value=ls_Value.replace(" ", "");
		int i = ls_Value.indexOf(">", ls_Value.indexOf(">") + 1) + 1;
		ls_Value = ls_Value.substring(i, ls_Value.length());
		i = ls_Value.lastIndexOf("<");
		return ls_Value.substring(0, i);

	}

	/**
	 * 根据路径获取XML节点下的子节点
	 * 
	 * @param ao_Node
	 * @param as_Path
	 *            路径，如：Item[@Abc='123']、Item[@name='测试']/Node[@id='2']
	 * @return
	 */
	public static Element getNodeByPath(Element ao_Node, String as_Path) {
		if (as_Path.indexOf("/") == -1) {
			if (as_Path.indexOf("[@") == -1) {
				return getNodeByName2(ao_Node, as_Path, null, null, null);
			} else {
				int i = as_Path.indexOf("[@");
				String ls_Name = as_Path.substring(0, i);
				int j = as_Path.indexOf("'", i);
				String ls_Attr = as_Path.substring(i + 2, j);
				String ls_Compare, ls_Value;
				if ("=<>!".indexOf(ls_Attr.substring(ls_Attr.length() - 2, ls_Attr.length() - 1)) == -1) {
					ls_Compare = ls_Attr.substring(ls_Attr.length() - 1, ls_Attr.length());
					ls_Attr = ls_Attr.substring(0, ls_Attr.length() - 1);
				} else {
					ls_Compare = ls_Attr.substring(ls_Attr.length() - 2, ls_Attr.length());
					ls_Attr = ls_Attr.substring(0, ls_Attr.length() - 2);
				}
				ls_Value = as_Path.substring(j + 1, as_Path.length() - 2);
				return getNodeByName2(ao_Node, ls_Name, ls_Attr, ls_Value, ls_Compare);
			}
		}
		String ls_Path = as_Path.substring(0, as_Path.indexOf("/") - 1);
		Element lo_Object = getNodeByPath(ao_Node, ls_Path);
		if (lo_Object == null)
			return null;
		ls_Path = as_Path.substring(as_Path.indexOf("/") + 1, as_Path.length() - 1);
		return getNodeByPath(lo_Object, ls_Path);
	}

	public static Element getNodeByPath2(Node ele, String as_Path) {
		Node node = ele.getFirstChild();
		int nodeType = -1;
		while (node != null) {
			nodeType = node.getNodeType();
			switch (nodeType) {
			case Node.ELEMENT_NODE:
				if (as_Path.indexOf("/") > 0) {
					String nodeName = as_Path.substring(0, as_Path.indexOf("/"));
					if (node.getNodeName().equals(nodeName)) {
						logger.debug(node.getNodeName() + " === " + as_Path.substring(as_Path.indexOf("/") + 1));
						getNodeByPath2(node, as_Path.substring(as_Path.indexOf("/") + 1));
					} else {
						logger.debug("+++++++" + node.getNodeName());
						getNodeByPath2(node, as_Path);
					}
				} else {
					Node attr = node.getAttributes().getNamedItem("x");
					logger.debug("=============" + node.getNodeName() + "::::" + attr);
					return (Element) node;
				}
				break;
			case Node.TEXT_NODE:
				break;
			}
			node = node.getNextSibling();
		}
		return null;
	}

	/**
	 * 根据名称获取XML对象根节点的第一个子节点
	 * 
	 * @param ao_XmlDoc
	 * @param as_Name
	 * @return
	 */
	public static Element getXmlNodeByName(Document ao_XmlDoc, String as_Name) {
		return getNodeByName2(((Document) ao_XmlDoc).getDocumentElement(), as_Name, null, null, null);
	}

	/**
	 * 根据名称获取XML节点的第一个子节点
	 * 
	 * @param ao_Node
	 * @param as_Name
	 * @return
	 */
	public static Element getNodeByName(Element ao_Node, String as_Name) {
		return getNodeByName2(ao_Node, as_Name, null, null, null);
	}

	/**
	 * 根据名称、属性、属性值获取XML节点的第一个子节点
	 * 
	 * @param ao_Node
	 * @param as_Name
	 * @param as_AttrName
	 * @param as_AttrValue
	 * @return
	 */
	public static Element getNodeByName1(Element ao_Node, String as_Name, String as_AttrName, String as_AttrValue) {
		return getNodeByName2(ao_Node, as_Name, as_AttrName, as_AttrValue, "=");
	}

	/**
	 * 根据名称、属性、属性值、比较符号获取XML节点的第一个子节点
	 * 
	 * @param ao_Node
	 * @param as_Name
	 * @param as_AttrName
	 * @param as_AttrValue
	 * @param as_Compare
	 * @return
	 */
	public static Element getNodeByName2(Element ao_Node, String as_Name, String as_AttrName, String as_AttrValue,
			String as_Compare) {
		try {
			NodeList lo_List = ao_Node.getElementsByTagName(as_Name);
			if (lo_List.getLength() == 0)
				return null;
			if (as_AttrName == null || as_AttrName.equals(""))
				return ((Element) lo_List.item(0));
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				if (compareTwoValue(as_Compare, lo_Node.getAttribute(as_AttrName), as_AttrValue))
					return lo_Node;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 比较两个值
	 * 
	 * @param as_Compare
	 *            比价符号
	 * @param as_Value1
	 *            值1
	 * @param as_Value2
	 *            值2
	 * @return
	 */
	public static boolean compareTwoValue(String as_Compare, String as_Value1, String as_Value2) {
		int li_Result = as_Value1.compareTo(as_Value2);
		if (li_Result < 0) {
			if (as_Compare.equals("<") || as_Compare.equals("<=") || as_Compare.equals("<>") || as_Compare.equals("!="))
				return true;
		} else if (li_Result > 0) {
			if (as_Compare.equals(">") || as_Compare.equals(">=") || as_Compare.equals("<>") || as_Compare.equals("!="))
				return true;
		} else {
			if (as_Compare.equals("=") || as_Compare.equals(">=") || as_Compare.equals("<="))
				return true;
		}
		return false;
	}

	/**
	 * 增加子节点
	 * 
	 * @param ao_Node
	 *            被增加的父节点
	 * @param ao_AddNode
	 *            需要增加的节点定义
	 * @return 返回增加的子节点
	 */
	public static Element addNode(Element ao_Node, Element ao_AddNode) {
		if (ao_Node == null || ao_AddNode == null)
			return null;
		Element lo_Node = ao_Node.getOwnerDocument().createElement(ao_AddNode.getNodeName());
		ao_Node.appendChild(lo_Node);
		copyNode(ao_AddNode, lo_Node);
		return lo_Node;
	}

	/**
	 * 根据名称增加子节点
	 * 
	 * @param ao_Node
	 *            被增加的父节点
	 * @param as_Name
	 *            节点名称
	 * @return
	 */
	public static Element addElement(Element ao_Node, String as_Name) {
		Element lo_Node = ao_Node.getOwnerDocument().createElement(as_Name);
		ao_Node.appendChild(lo_Node);
		return lo_Node;
	}

	/**
	 * 复制节点内容
	 * 
	 * @param ao_Source
	 *            来源节点
	 * @param ao_Goal
	 *            目标节点
	 */
	public static void copyNode(Element ao_Source, Element ao_Goal) {
		NamedNodeMap lo_Attrs = ao_Source.getAttributes();
		for (int i = 0; i < lo_Attrs.getLength(); i++) {
			Attr lo_Attr = (Attr) lo_Attrs.item(i);
			ao_Goal.setAttribute(lo_Attr.getName(), lo_Attr.getValue());
		}
		NodeList lo_List = ao_Source.getChildNodes();
		for (int i = 0; i < lo_List.getLength(); i++) {
			try {
				if (lo_List.item(i).getClass().toString()
						.equals("com.sun.org.apache.xerces.internal.dom.DeferredElementImpl")) {
					Element lo_Node = (Element) lo_List.item(i);
					Element lo_gNode = ao_Goal.getOwnerDocument().createElement(lo_Node.getNodeName());
					ao_Goal.appendChild(lo_gNode);
					copyNode(lo_Node, lo_gNode);
				}
			} catch (Exception ex) {

			}
		}
	}

	/**
	 * 排序XML对象内容
	 * 
	 * @param ao_Xml
	 *            XML对象
	 * @param as_Id
	 *            主键标识名称
	 * @param as_PId
	 *            外键（上级）标识名称
	 */
	public static void orderXml(Document ao_Xml, String as_Name, String as_Id, String as_PId) {
		if (ao_Xml == null || ao_Xml.getDocumentElement().hasChildNodes() == false)
			return;
		NodeList lo_List = ao_Xml.getDocumentElement().getElementsByTagName(as_Name);
		if (lo_List.getLength() == 1)
			return;

		Map<String, Element> lo_Map = new HashMap<String, Element>();
		Element lo_Node, lo_Parent;

		for (int i = 0; i < lo_List.getLength(); i++) {
			lo_Node = (Element) lo_List.item(i);
			lo_Map.put(lo_Node.getAttribute(as_Id), lo_Node);
		}

		for (int i = lo_List.getLength() - 1; i >= 0; i--) {
			lo_Node = (Element) lo_List.item(i);
			if (!MGlobal.isEmpty(lo_Node.getAttribute(as_PId))) {
				lo_Parent = lo_Map.get(lo_Node.getAttribute(as_PId));
				if (lo_Parent != null) {
					ao_Xml.getDocumentElement().removeChild(lo_Node);
					if (lo_Parent.hasChildNodes()) {
						lo_Parent.insertBefore(lo_Node, lo_Parent.getFirstChild());
					} else {
						lo_Parent.appendChild(lo_Node);
					}
				}
			}
		}

		lo_Map.clear();
		lo_Map = null;
	}

	/**
	 * 获取XML节点中某个属性内容(布尔型)
	 * 
	 * @param ao_Node
	 *            更新节点对象
	 * @param as_Name
	 *            属性名称
	 * @return
	 */
	public static boolean readBoolean(Element ao_Node, String as_Name) {
		if (MGlobal.isEmpty(ao_Node.getAttribute(as_Name)))
			return false;
		String ls_Value = ao_Node.getAttribute(as_Name).toLowerCase();
		return (ls_Value.equals("true") || ls_Value.equals("1"));
	}

	/**
	 * 获取XML节点中某个属性内容(短整型)
	 * 
	 * @param ao_Node
	 *            更新节点对象
	 * @param as_Name
	 *            属性名称
	 * @return
	 */
	public static short readShort(Element ao_Node, String as_Name) {
		if (MGlobal.isEmpty(ao_Node.getAttribute(as_Name)))
			return 0;
		return Short.parseShort(ao_Node.getAttribute(as_Name));
	}

	/**
	 * 获取XML节点中某个属性内容(整型)
	 * 
	 * @param ao_Node
	 *            更新节点对象
	 * @param as_Name
	 *            属性名称
	 * @return
	 */
	public static int readInt(Element ao_Node, String as_Name) {
		if (MGlobal.isEmpty(ao_Node.getAttribute(as_Name)))
			return 0;
		return Integer.parseInt(ao_Node.getAttribute(as_Name));
	}

	/**
	 * 获取XML节点中某个属性内容(长整型)
	 * 
	 * @param ao_Node
	 *            更新节点对象
	 * @param as_Name
	 *            属性名称
	 * @return
	 */
	public static long readLong(Element ao_Node, String as_Name) {
		if (MGlobal.isEmpty(ao_Node.getAttribute(as_Name)))
			return 0;
		return Long.parseLong(ao_Node.getAttribute(as_Name));
	}

	/**
	 * 获取XML节点中某个属性内容(日期型)
	 * 
	 * @param ao_Node
	 *            更新节点对象
	 * @param as_Name
	 *            属性名称
	 * @return
	 */
	public static Date readDate(Element ao_Node, String as_Name) throws SQLException {
		if (MGlobal.isEmpty(ao_Node.getAttribute(as_Name)))
			return null;
		return MGlobal.stringToDate(ao_Node.getAttribute(as_Name));
	}

	/**
	 * 获取XML节点中某个属性内容(日期时间型)
	 * 
	 * @param ao_Node
	 *            更新节点对象
	 * @param as_Name
	 *            属性名称
	 * @return
	 */
	public static Date readTime(Element ao_Node, String as_Name) {
		if (MGlobal.isEmpty(ao_Node.getAttribute(as_Name)))
			return null;
		return MGlobal.stringToDate(ao_Node.getAttribute(as_Name));
	}

	/**
	 * 设置XML节点某个属性内容(日期型)
	 * 
	 * @param ao_Node
	 *            更新节点对象
	 * @param as_Name
	 *            更新属性名称
	 * @param ad_Value
	 *            属性值
	 * @return
	 */
	public static void writeDate(Element ao_Node, String as_Name, Date ad_Value) {
		if (ad_Value == null) {
			ao_Node.setAttribute(as_Name, "");
		} else {
			ao_Node.setAttribute(as_Name, MGlobal.dateToString(ad_Value, "yyyy-m-d"));
		}
	}

	/**
	 * 设置XML节点某个属性内容(日期时间型)
	 * 
	 * @param ao_Node
	 *            更新节点对象
	 * @param as_Name
	 *            更新属性名称
	 * @param ad_Value
	 *            属性值
	 * @return
	 */
	public static void writeTime(Element ao_Node, String as_Name, Date ad_Value) {
		if (ad_Value == null) {
			ao_Node.setAttribute(as_Name, "");
		} else {
			ao_Node.setAttribute(as_Name, MGlobal.dateToString(ad_Value));
		}
	}

}
