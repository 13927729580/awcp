package org.szcloud.framework.workflow.core.base;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CXml {
	
	/**
	 * 标识属性 
	 */
	public String Id = null;
	/**
	 * 名称属性
	 */
	public String Name = null;
	/**
	 * 父标识属性
	 */
	public String PId = null;
	
	/**
	 * 存储排序后的集合对象
	 */
	public Map<String, Element> Maps = null;
	
	/**
	 * XML对象
	 */
	public Document XmlDoc = null;
	
	/**
	 * 初始化
	 * @param ao_Xml XML对象
	 * @param as_Id 标识属性
	 * @param as_PId 父标识属性
	 * @param ab_Order 是否排序
	 */
	public CXml(Document ao_Xml, String as_Id, String as_PId, boolean ab_Order) {
		this.Id = as_Id;
		this.PId = as_PId;
		this.XmlDoc = ao_Xml;
	}
	
	
	
}
