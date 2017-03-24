package org.szcloud.framework.workflow.core.module;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 用dom4j-1.6.1.jar使用的Xml操作对象
 * 
 * @author Jackie.Wang
 * 
 */
public class MXml {
	
	/**
	 * 载入XML内容
	 * @param as_XmlData
	 * @return
	 */
	public static Document loadXml(String as_XmlData) {
		try {
			return DocumentHelper.parseText(as_XmlData); // 将字符串转为XML
		} catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    } 
	
	/**
	 * 获取XML内容
	 * @param ao_Xml XML对象
	 * @return
	 */
	public static String getXml(Document ao_Xml) {
		return ao_Xml.asXML();
	}
	
	/**
	 * 获取XML节点内容
	 * @param ao_Node XML节点
	 * @return
	 */
	public static String getXml(Element ao_Node) {
		return ao_Node.asXML();
	}
		
}
