package org.szcloud.framework.workflow.core.base;

import java.util.Date;
import java.util.Map;

import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;

/**
 * 系统参数实体类
 * 
 * @author Jackie.wang
 * 
 */
public class CSysParameter {
	/**
	 * 参数代码
	 */
	public String Code = "";
	/**
	 * 参数名称
	 */
	public String Name = "";
	/**
	 * 参数类型：=-1-系统一参数（在组件运行时马上载入）；=0-系统二参数；=1-外部系统参数；
	 */
	public int Type = 0;
	/**
	 * 参数值
	 */
	public String Value = "";
	/**
	 * 参数描述
	 */
	public String Describe = null;
	/**
	 * 所属分类代码
	 */
	public String TypeCode = null;
	/**
	 * 更新时间
	 */
	public Date UpdateDate = null;

	/**
	 * 内存变量，当系统参数内容为Xml内容时
	 */
	private Document mo_Document = null;

	/**
	 * 内存变量，当系统参数内容为Xml内容时
	 * 
	 * @return
	 */
	public Document getDocument() {
		if (mo_Document == null)
			mo_Document = MXmlHandle.LoadXml(this.Value);
		return mo_Document;
	}

	/**
	 * 获取系统参数定义的Xml节点属性值（当系统参数内容为Xml内容时）
	 * 
	 * @param as_Attribute
	 *            节点属性
	 * @return 节点属性值
	 */
	public String getParaAttrValue(String as_Attribute) {
		Document lo_Xml = getDocument();
		if (lo_Xml == null)
			return "";
		return lo_Xml.getDocumentElement().getAttribute(as_Attribute);
	}

	/**
	 * 设置系统参数定义的Xml节点属性值（当系统参数内容为Xml内容时）
	 * 
	 * @param as_Attribute
	 *            节点属性
	 * @param as_Value
	 *            节点属性值
	 */
	public void setParaAttrValue(String as_Attribute, String as_Value) {
		Document lo_Xml = getDocument();
		if (lo_Xml == null) {
			mo_Document = MXmlHandle.LoadXml("<Root />");
		}
		mo_Document.getDocumentElement().setAttribute(as_Attribute, as_Value);
		this.Value = mo_Document.getDocumentElement().getTextContent();
	}

	/**
	 * 初始化
	 * 
	 * @param as_Code
	 *            参数代码
	 * @param as_Name
	 *            参数名称
	 * @param ai_Type
	 *            参数类型(0/1/2)
	 * @param as_Value
	 *            参数值
	 */
	public CSysParameter(String as_Code, String as_Name, int ai_Type,
			String as_Value) {
		this.Code = as_Code;
		this.Name = as_Name;
		this.Type = ai_Type;
		this.Value = as_Value;
	}

	/**
	 * 初始化（从数据库结果集）
	 * 
	 * @param ao_Set
	 *            结果集
	 * @param ai_Type
	 *            类型：=0-简单初始化；=1-完整初始化；
	 */
	public CSysParameter(Map<String, Object> ao_Set, int ai_Type) {
		try {
			this.Code = (String) ao_Set.get("ParameterCode");
			this.Name = (String) ao_Set.get("ParameterName");
			this.Type = (Short) ao_Set.get("ParameterType");
			this.Value = (String) ao_Set.get("ParameterValue");
			if (ai_Type == 1) {
				if (ao_Set.get("Describe") != null)
					this.Describe = (String) ao_Set.get("Describe");
				if (ao_Set.get("TypeCode") != null)
					this.TypeCode = (String) ao_Set.get("TypeCode");
				if (ao_Set.get("UpdateDate") != null)
					this.UpdateDate = (Date) ao_Set.get("UpdateDate");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 清除
	 */
	public void ClearUp() {
		mo_Document = null;
	}
	
}
