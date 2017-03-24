package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.EImpressBodyType;
import org.szcloud.framework.workflow.core.emun.EImpressDocumentType;
import org.szcloud.framework.workflow.core.emun.EImpressFormType;
import org.szcloud.framework.workflow.core.emun.EStatePassType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 本地嵌入（使用另一个公文模板时）或远程启动传递数据对象
 * 
 * @author Jackie.Wang
 * 
 */
public class CImpressData extends CBase {
	/**
	 * 初始化
	 */
	public CImpressData() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CImpressData(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		this.CyclostyleName = "";
		this.FlowName = "";
		this.ImpressContent = "";
		this.ImpressAsDocument = true;
		this.ImpressBody = EImpressBodyType.ImpressAsApp;
		this.ImpressDocument = EImpressDocumentType.ImpressLastVersion;
		this.ImpressForm = EImpressFormType.NotImpressForm;
		this.ImpressFormPrintXml = "";
		this.ImpressRoles = "<ImpressRoles />";
		this.ImpressProps = "<ImpressProps />";
		this.ImpressOther = "";
		this.ReturnContent = "";
		this.ReturnAsDocument = true;
		this.ReturnBody = EImpressBodyType.ImpressAsApp;
		this.ReturnDocument = EImpressDocumentType.ImpressLastVersion;
		this.ReturnForm = EImpressFormType.NotImpressForm;
		this.ReturnFormPrintXml = "";
		this.ReturnRoles = "<ReturnRoles />";
		this.ReturnProps = "<ReturnProps />";
		this.ReturnOther = "";
		this.StatePToCType = EStatePassType.PassStateDataAsEntry;
		this.StateCToPType = EStatePassType.PassStateDataAsEntry;
		this.PassCommentType = 3;
		this.PassSignType = 0;
		this.PassVoiceType = 0;
		this.ExportScript = "";
		this.ImportScript = "";
		this.IsNeedReturn = true;
		mo_XMLObject = MXmlHandle.LoadXml("<Activity />");
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		initialize();
		// 清除临时变量
		mo_XMLObject = null;

		super.ClearUp();
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 传输的公文模板名称
	 */
	public String CyclostyleName = "";

	/**
	 * 公文模板中使用流程的名称,(空)-表示使用缺省
	 */
	public String FlowName = "";

	/**
	 * 需要传输数据的内容
	 */
	public String ImpressContent = "";

	/**
	 * 是否传输本公文作为子公文的一个附件
	 */
	public boolean ImpressAsDocument = true;

	/**
	 * 是否传输公文正文内容方式
	 */
	public int ImpressBody = EImpressBodyType.ImpressAsApp;

	/**
	 * 传输公文附件内容方式
	 */
	public int ImpressDocument = EImpressDocumentType.ImpressLastVersion;

	/**
	 * 传输公文表单的方式
	 */
	public int ImpressForm = EImpressFormType.NotImpressForm;

	/**
	 * 传输公文表单打印模板内容定义
	 */
	public String ImpressFormPrintXml = "";

	/**
	 * 公文传输角色内容的角色名称的XML字符串 格式为：< ImpressRoles>< Role Name='' Type='' Source=''
	 * />... ...< /ImpressRoles> <br>
	 * 其中：Name-属性名称；Type-数据源类型(=0-系统字段;1-计算公式;2-SQL语句)；Source-数据源；
	 */
	public String ImpressRoles = "<ImpressRoles />";

	/**
	 * 设置公文传输角色内容的角色名称的XML字符串
	 * 
	 * @param value
	 */
	public void setImpressRoles(String value) {
		mo_XMLObject = MXmlHandle.LoadXml(value);
		if (mo_XMLObject != null) {
			if (mo_XMLObject.getDocumentElement().getTagName() == "ImpressRoles")
				this.ImpressRoles = value;
		}
	}

	/**
	 * 公文传输表头属性内容的表头属性名称的XML字符串
	 */
	public String ImpressProps = "<ImpressProps />";

	/**
	 * 设置公文传输表头属性内容的表头属性名称的XML字符串
	 * 
	 * @param value
	 */
	public void setImpressProps(String value) {
		mo_XMLObject = MXmlHandle.LoadXml(value);
		if (mo_XMLObject != null) {
			if (mo_XMLObject.getDocumentElement().getTagName() == "ImpressProps")
				this.ImpressProps = value;
		}
	}

	/**
	 * 需要传输的一些其他信息
	 */
	public String ImpressOther = "";

	/**
	 * 需要回传数据的内容
	 */
	public String ReturnContent = "";

	/**
	 * 是否回传子公文作为本公文的一个附件
	 */
	public boolean ReturnAsDocument = true;

	/**
	 * 回传（从子公文传回父公文）公文正文内容的方式
	 */
	public int ReturnBody = EImpressBodyType.ImpressAsApp;

	/**
	 * 回传（从子公文传回父公文）公文附件内容的方式
	 */
	public int ReturnDocument = EImpressDocumentType.ImpressLastVersion;

	/**
	 * 回传（从子公文传回父公文）公文表单的方式
	 */
	public int ReturnForm = EImpressFormType.NotImpressForm;

	/**
	 * 回传（从子公文传回父公文）公文表单打印模板内容定义
	 */
	public String ReturnFormPrintXml = "";

	/**
	 * 需要回传（从子公文传回父公文）角色内容的角色名称的XML字符串 格式为：<ReturnRoles><Role Name='' Type=''
	 * Source='' />... ...</ReturnRoles>
	 * 其中：Name-属性名称；Type-数据源类型(=0-系统字段;1-计算公式;2-SQL语句)；Source-数据源；
	 */
	public String ReturnRoles = "<ReturnRoles />";

	/**
	 * 设置需要回传（从子公文传回父公文）角色内容的角色名称的XML字符串
	 * 
	 * @param value
	 */
	public void setReturnRoles(String value) {
		mo_XMLObject = MXmlHandle.LoadXml(value);
		if (mo_XMLObject != null) {
			if (mo_XMLObject.getDocumentElement().getTagName() == "ReturnRoles")
				this.ReturnRoles = value;
		}
	}

	/**
	 * 需要回传（从子公文传回父公文）表头属性内容的表头属性名称的XML字符串
	 */
	public String ReturnProps = "<ReturnProps />";

	/**
	 * 设置需要回传（从子公文传回父公文）表头属性内容的表头属性名称的XML字符串
	 * 
	 * @param value
	 */
	public void setReturnProps(String value) {
		mo_XMLObject = MXmlHandle.LoadXml(value);
		if (mo_XMLObject != null) {
			if (mo_XMLObject.getDocumentElement().getTagName() == "ReturnProps")
				this.ReturnProps = value;
		}
	}

	/**
	 * 需要回传（从子公文传回父公文）一些其他信息
	 */
	public String ReturnOther = "";

	/**
	 * 父流程到子流程状态数据传递类型
	 */
	public int StatePToCType = EStatePassType.PassStateDataAsEntry;

	/**
	 * 子流程回传到父流程状态数据传递类型
	 */
	public int StateCToPType = EStatePassType.PassStateDataAsEntry;

	/**
	 * 是否传输批示意见：=0-不传递；=1-父流程传递到子流程；=2-子流程回传到父流程
	 */
	public int PassCommentType = 3;

	/**
	 * 是否传输签名批示：=0-不传递；=1-父流程传递到子流程；=2-子流程回传到父流程
	 */
	public int PassSignType = 0;

	/**
	 * 是否传输声音批示：=0-不传递；=1-父流程传递到子流程；=2-子流程回传到父流程
	 */
	public int PassVoiceType = 0;

	/**
	 * 父流程导入子流程时采用的Script函数(为空则采用系统缺省方式处理)
	 */
	public String ExportScript = "";

	/**
	 * 子流程导入父流程时采用的Script函数(为空则采用系统缺省方式处理)
	 */
	public String ImportScript = "";

	/**
	 * 是否需要回传处理
	 */
	public boolean IsNeedReturn = true;

	/**
	 * 临时变量
	 */
	private Document mo_XMLObject = MXmlHandle.LoadXml("<Activity />");

	/**
	 * 临时变量
	 * 
	 * @return
	 */
	public Document getXMLObject() {
		return mo_XMLObject;
	}

	/**
	 * 临时变量
	 * 
	 * @param value
	 */
	public void setXMLObject(Document value) {
		mo_XMLObject = value;
	}

	/**
	 * 取传递角色、属性定义的XML对象
	 * 
	 * @param as_ObjectName
	 * @return
	 */
	public Document getDefineObject(String as_ObjectName) {
		if (as_ObjectName != null && as_ObjectName != "") {
			if (as_ObjectName.equals("ImpressRoles")) {
				if (Boolean.parseBoolean(MXmlHandle.getNodeByPath(
						mo_XMLObject.getDocumentElement(), this.ImpressRoles)
						.toString())) {
					MXmlHandle.getNodeByPath(mo_XMLObject.getDocumentElement(),
							"<ImpressRoles />");
				}
			}
			if (as_ObjectName.equals("ImpressProps")) {
				if (Boolean.parseBoolean(MXmlHandle.getNodeByPath(
						mo_XMLObject.getDocumentElement(), this.ImpressProps)
						.toString())) {
					MXmlHandle.getNodeByPath(mo_XMLObject.getDocumentElement(),
							"<ImpressProps />");
				}
			}
			if (as_ObjectName.equals("ReturnRoles")) {
				if (Boolean.parseBoolean(MXmlHandle.getNodeByPath(
						mo_XMLObject.getDocumentElement(), this.ReturnRoles)
						.toString())) {
					MXmlHandle.getNodeByPath(mo_XMLObject.getDocumentElement(),
							"<ReturnRoles />");
				}
			}
			if (as_ObjectName.equals("ReturnProps")) {
				if (Boolean.parseBoolean(MXmlHandle.getNodeByPath(
						mo_XMLObject.getDocumentElement(), this.ReturnProps)
						.toString())) {
					MXmlHandle.getNodeByPath(mo_XMLObject.getDocumentElement(),
							"<ReturnProps />");
				}
			}
		}
		return mo_XMLObject;
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
			this.CyclostyleName = (ao_Node.getAttribute("CN"));
			this.FlowName = (ao_Node.getAttribute("FN"));
			this.ImpressContent = (ao_Node.getAttribute("IC"));
			this.ImpressAsDocument = Boolean.parseBoolean(ao_Node
					.getAttribute("IAD"));
			this.ImpressBody = (Integer.parseInt(ao_Node.getAttribute("IB")));
			this.ImpressDocument = (Integer.parseInt(ao_Node.getAttribute("ID")));
			if (ao_Node.getAttribute("IF") != null) {
				this.ImpressForm = Integer.parseInt(ao_Node.getAttribute("IF"));
			}
			if (ao_Node.getAttribute("IX") != null) {
				this.ImpressFormPrintXml = ao_Node.getAttribute("IX");
			}
			this.ImpressRoles = (ao_Node.getAttribute("IR"));
			this.ImpressProps = (ao_Node.getAttribute("IP"));
			this.ImpressOther = (ao_Node.getAttribute("IO"));
			this.ReturnContent = (ao_Node.getAttribute("RC"));
			this.ReturnAsDocument = Boolean.parseBoolean(ao_Node
					.getAttribute("RAD"));
			this.ReturnBody = Integer.parseInt(ao_Node.getAttribute("RB"));
			this.ReturnDocument = Integer.parseInt(ao_Node.getAttribute("RD"));
			if (ao_Node.getAttribute("RF") != null) {
				this.ReturnForm = Integer.parseInt(ao_Node.getAttribute("RF"));
			}
			if (ao_Node.getAttribute("RX") != null) {
				this.ReturnFormPrintXml = (ao_Node.getAttribute("RX"));
			}
			this.ReturnRoles = (ao_Node.getAttribute("RR"));
			this.ReturnProps = (ao_Node.getAttribute("RP"));
			this.ReturnOther = (ao_Node.getAttribute("RO"));
			this.StatePToCType = Integer.parseInt(ao_Node.getAttribute("ST"));
			this.StateCToPType = Integer.parseInt(ao_Node.getAttribute("SP"));
			this.PassCommentType = Integer.parseInt(ao_Node.getAttribute("PC"));
			this.PassSignType = Integer.parseInt(ao_Node.getAttribute("PS"));
			this.PassVoiceType = Integer.parseInt(ao_Node.getAttribute("PV"));
			this.ExportScript = (ao_Node.getAttribute("ES"));
			this.ImportScript = (ao_Node.getAttribute("IS"));
			this.IsNeedReturn = Boolean.parseBoolean(ao_Node.getAttribute("NR"));
		} else {
			this.CyclostyleName = (ao_Node.getAttribute("CyclostyleName"));
			this.FlowName = (ao_Node.getAttribute("FlowName"));
			this.ImpressContent = (ao_Node.getAttribute("ImpressContent"));
			this.ImpressAsDocument = Boolean.parseBoolean(ao_Node
					.getAttribute("ImpressAsDocument"));
			this.ImpressBody = (Integer.parseInt(ao_Node
					.getAttribute("ImpressBody")));
			this.ImpressDocument = (Integer.parseInt(ao_Node
					.getAttribute("ImpressDocument")));
			if (ao_Node.getAttribute("ImpressForm") != null) {
				this.ImpressForm = Integer.parseInt(ao_Node
						.getAttribute("ImpressForm"));
			}
			if (ao_Node.getAttribute("ImpressFormPrintXml") != null) {
				this.ImpressFormPrintXml = ao_Node
						.getAttribute("ImpressFormPrintXml");
			}
			this.ImpressRoles = (ao_Node.getAttribute("ImpressRoles"));
			this.ImpressProps = (ao_Node.getAttribute("ImpressProps"));
			this.ImpressOther = (ao_Node.getAttribute("ImpressOther"));
			this.ReturnContent = (ao_Node.getAttribute("ReturnContent"));
			this.ReturnAsDocument = Boolean.parseBoolean(ao_Node
					.getAttribute("ReturnAsDocument"));
			this.ReturnBody = Integer
					.parseInt(ao_Node.getAttribute("ReturnBody"));
			this.ReturnDocument = Integer.parseInt(ao_Node
					.getAttribute("ReturnDocument"));
			if (ao_Node.getAttribute("ReturnForm") != null) {
				this.ReturnForm = Integer.parseInt(ao_Node
						.getAttribute("ReturnForm"));
			}
			if (ao_Node.getAttribute("ReturnFormPrintXml") != null) {
				this.ReturnFormPrintXml = (ao_Node
						.getAttribute("ReturnFormPrintXml"));
			}
			this.ReturnRoles = (ao_Node.getAttribute("ReturnRoles"));
			this.ReturnProps = (ao_Node.getAttribute("ReturnProps"));
			this.ReturnOther = (ao_Node.getAttribute("ReturnOther"));
			this.StatePToCType = Integer.parseInt(ao_Node
					.getAttribute("StatePToCType"));
			this.StateCToPType = Integer.parseInt(ao_Node
					.getAttribute("StateCToPType"));
			this.PassCommentType = Integer.parseInt(ao_Node
					.getAttribute("PassCommentType"));
			this.PassSignType = Integer.parseInt(ao_Node
					.getAttribute("PassSignType"));
			this.PassVoiceType = Integer.parseInt(ao_Node
					.getAttribute("PassVoiceType"));
			this.ExportScript = (ao_Node.getAttribute("ExportScript"));
			this.ImportScript = (ao_Node.getAttribute("ImportScript"));
			this.IsNeedReturn = Boolean.parseBoolean(ao_Node
					.getAttribute("IsNeedReturn"));
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
			ao_Node.setAttribute("CN", this.CyclostyleName);
			ao_Node.setAttribute("FN", this.FlowName);
			ao_Node.setAttribute("IC", this.ImpressContent);
			ao_Node.setAttribute("IAD", Boolean.toString(this.ImpressAsDocument));
			ao_Node.setAttribute("IB", Integer.toString(this.ImpressBody));
			ao_Node.setAttribute("ID", Integer.toString(this.ImpressDocument));
			ao_Node.setAttribute("IF", Integer.toString(this.ImpressForm));
			ao_Node.setAttribute("IX", this.ImpressFormPrintXml);
			ao_Node.setAttribute("IR", this.ImpressRoles);
			ao_Node.setAttribute("IP", this.ImpressProps);
			ao_Node.setAttribute("IO", this.ImpressOther);
			ao_Node.setAttribute("RC", this.ReturnContent);
			ao_Node.setAttribute("RAD", Boolean.toString(this.ReturnAsDocument));
			ao_Node.setAttribute("RB", Integer.toString(this.ReturnBody));
			ao_Node.setAttribute("RD", Integer.toString(this.ReturnDocument));
			ao_Node.setAttribute("RF", Integer.toString(this.ReturnForm));
			ao_Node.setAttribute("RX", this.ReturnFormPrintXml);
			ao_Node.setAttribute("RR", this.ReturnRoles);
			ao_Node.setAttribute("RP", this.ReturnProps);
			ao_Node.setAttribute("RO", this.ReturnOther);
			ao_Node.setAttribute("ST", Integer.toString(this.StatePToCType));
			ao_Node.setAttribute("SP", Integer.toString(this.StateCToPType));
			ao_Node.setAttribute("PC", Integer.toString(this.PassCommentType));
			ao_Node.setAttribute("PS", Integer.toString(this.PassSignType));
			ao_Node.setAttribute("PV", Integer.toString(this.PassVoiceType));
			ao_Node.setAttribute("ES", this.ExportScript);
			ao_Node.setAttribute("IS", this.ImportScript);
			ao_Node.setAttribute("NR", Boolean.toString(this.IsNeedReturn));
		} else {
			ao_Node.setAttribute("CyclostyleName", this.CyclostyleName);
			ao_Node.setAttribute("FlowName", this.FlowName);
			ao_Node.setAttribute("ImpressContent", this.ImpressContent);
			ao_Node.setAttribute("ImpressAsDocument",
					Boolean.toString(this.ImpressAsDocument));
			ao_Node.setAttribute("ImpressBody",
					Integer.toString(this.ImpressBody));
			ao_Node.setAttribute("ImpressDocument",
					Integer.toString(this.ImpressDocument));
			ao_Node.setAttribute("ImpressForm",
					Integer.toString(this.ImpressForm));
			ao_Node.setAttribute("ImpressFormPrintXml", this.ImpressFormPrintXml);
			ao_Node.setAttribute("ImpressRoles", this.ImpressRoles);
			ao_Node.setAttribute("ImpressProps", this.ImpressProps);
			ao_Node.setAttribute("ImpressOther", this.ImpressOther);
			ao_Node.setAttribute("ReturnContent", this.ReturnContent);
			ao_Node.setAttribute("ReturnAsDocument",
					Boolean.toString(this.ReturnAsDocument));
			ao_Node.setAttribute("ReturnBody", Integer.toString(this.ReturnBody));
			ao_Node.setAttribute("ReturnDocument",
					Integer.toString(this.ReturnDocument));
			ao_Node.setAttribute("ReturnForm", Integer.toString(this.ReturnForm));
			ao_Node.setAttribute("ReturnFormPrintXml", this.ReturnFormPrintXml);
			ao_Node.setAttribute("ReturnRoles", this.ReturnRoles);
			ao_Node.setAttribute("ReturnProps", this.ReturnProps);
			ao_Node.setAttribute("ReturnOther", this.ReturnOther);
			ao_Node.setAttribute("StatePToCType",
					Integer.toString(this.StatePToCType));
			ao_Node.setAttribute("StateCToPType",
					Integer.toString(this.StateCToPType));
			ao_Node.setAttribute("PassCommentType",
					Integer.toString(this.PassCommentType));
			ao_Node.setAttribute("PassSignType",
					Integer.toString(this.PassSignType));
			ao_Node.setAttribute("PassVoiceType",
					Integer.toString(this.PassVoiceType));
			ao_Node.setAttribute("ExportScript", this.ExportScript);
			ao_Node.setAttribute("ImportScript", this.ImportScript);
			ao_Node.setAttribute("IsNeedReturn",
					Boolean.toString(this.IsNeedReturn));
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
			ao_Bag.Write("CN", this.CyclostyleName);
			ao_Bag.Write("FN", this.FlowName);
			ao_Bag.Write("IC", this.ImpressContent);
			ao_Bag.Write("IAD", this.ImpressAsDocument);
			ao_Bag.Write("IB", this.ImpressBody);
			ao_Bag.Write("ID", this.ImpressDocument);
			ao_Bag.Write("IF", this.ImpressForm);
			ao_Bag.Write("IX", this.ImpressFormPrintXml);
			ao_Bag.Write("IR", this.ImpressRoles);
			ao_Bag.Write("IP", this.ImpressProps);
			ao_Bag.Write("IO", this.ImpressOther);
			ao_Bag.Write("RC", this.ReturnContent);
			ao_Bag.Write("RAD", this.ReturnAsDocument);
			ao_Bag.Write("RB", this.ReturnBody);
			ao_Bag.Write("RD", this.ReturnDocument);
			ao_Bag.Write("RF", this.ReturnForm);
			ao_Bag.Write("RX", this.ReturnFormPrintXml);
			ao_Bag.Write("RR", this.ReturnRoles);
			ao_Bag.Write("RP", this.ReturnProps);
			ao_Bag.Write("RO", this.ReturnOther);
			ao_Bag.Write("ST", this.StatePToCType);
			ao_Bag.Write("SP", this.StateCToPType);
			ao_Bag.Write("PC", this.PassCommentType);
			ao_Bag.Write("PS", this.PassSignType);
			ao_Bag.Write("PV", this.PassVoiceType);
			ao_Bag.Write("ES", this.ExportScript);
			ao_Bag.Write("IS", this.ImportScript);
			ao_Bag.Write("NR", this.IsNeedReturn);
		} else {
			ao_Bag.Write("ms_CyclostyleName", this.CyclostyleName);
			ao_Bag.Write("ms_FlowName", this.FlowName);
			ao_Bag.Write("ms_ImpressContent", this.ImpressContent);
			ao_Bag.Write("mb_ImpressAsDocument", this.ImpressAsDocument);
			ao_Bag.Write("mi_ImpressBody", this.ImpressBody);
			ao_Bag.Write("mi_ImpressDocument", this.ImpressDocument);
			ao_Bag.Write("mi_ImpressForm", this.ImpressForm);
			ao_Bag.Write("ms_ImpressFormPrintXml", this.ImpressFormPrintXml);
			ao_Bag.Write("ms_ImpressRoles", this.ImpressRoles);
			ao_Bag.Write("ms_ImpressProps", this.ImpressProps);
			ao_Bag.Write("ms_ImpressOther", this.ImpressOther);
			ao_Bag.Write("ms_ReturnContent", this.ReturnContent);
			ao_Bag.Write("mb_ReturnAsDocument", this.ReturnAsDocument);
			ao_Bag.Write("mi_ReturnBody", this.ReturnBody);
			ao_Bag.Write("mi_ReturnDocument", this.ReturnDocument);
			ao_Bag.Write("mi_ReturnForm", this.ReturnForm);
			ao_Bag.Write("ms_ReturnFormPrintXml", this.ReturnFormPrintXml);
			ao_Bag.Write("ms_ReturnRoles", this.ReturnRoles);
			ao_Bag.Write("ms_ReturnProps", this.ReturnProps);
			ao_Bag.Write("ms_ReturnOther", this.ReturnOther);
			ao_Bag.Write("mi_StatePToCType", this.StatePToCType);
			ao_Bag.Write("mi_StateCToPType", this.StateCToPType);
			ao_Bag.Write("mi_PassCommentType", this.PassCommentType);
			ao_Bag.Write("mi_PassSignType", this.PassSignType);
			ao_Bag.Write("mi_PassVoiceType", this.PassVoiceType);
			ao_Bag.Write("ms_ExportScript", this.ExportScript);
			ao_Bag.Write("ms_ImportScript", this.ImportScript);
			ao_Bag.Write("mb_IsNeedReturn", this.IsNeedReturn);
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
			this.CyclostyleName = ao_Bag.ReadString("CN");
			this.FlowName = ao_Bag.ReadString("FN");
			this.ImpressContent = ao_Bag.ReadString("IC");
			this.ImpressAsDocument = ao_Bag.ReadBoolean("IAD");
			this.ImpressBody = ao_Bag.ReadInt("IB");
			this.ImpressDocument = ao_Bag.ReadInt("ID");
			this.ImpressForm = ao_Bag.ReadInt("IF");
			this.ImpressFormPrintXml = ao_Bag.ReadString("IX");
			this.ImpressRoles = ao_Bag.ReadString("IR");
			this.ImpressProps = ao_Bag.ReadString("IP");
			this.ImpressOther = ao_Bag.ReadString("IO");
			this.ReturnContent = ao_Bag.ReadString("RC");
			this.ReturnAsDocument = ao_Bag.ReadBoolean("RAD");
			this.ReturnBody = ao_Bag.ReadInt("RB");
			this.ReturnDocument = ao_Bag.ReadInt("RD");
			this.ReturnForm = ao_Bag.ReadInt("RF");
			this.ReturnFormPrintXml = ao_Bag.ReadString("RX");
			this.ReturnRoles = ao_Bag.ReadString("RR");
			this.ReturnProps = ao_Bag.ReadString("RP");
			this.ReturnOther = ao_Bag.ReadString("RO");
			this.StatePToCType = ao_Bag.ReadInt("SC");
			this.StateCToPType = ao_Bag.ReadInt("SP");
			this.PassCommentType = ao_Bag.ReadInt("PC");
			this.PassSignType = ao_Bag.ReadInt("PS");
			this.PassVoiceType = ao_Bag.ReadInt("PV");
			this.ExportScript = ao_Bag.ReadString("ES");
			this.ImportScript = ao_Bag.ReadString("IS");
			this.IsNeedReturn = ao_Bag.ReadBoolean("NR");
		} else {
			this.CyclostyleName = ao_Bag.ReadString("ms_CyclostyleName");
			this.FlowName = ao_Bag.ReadString("ms_FlowName");
			this.ImpressContent = ao_Bag.ReadString("ms_ImpressContent");
			this.ImpressAsDocument = ao_Bag.ReadBoolean("mb_ImpressAsDocument");
			this.ImpressBody = ao_Bag.ReadInt("mi_ImpressBody");
			this.ImpressDocument = ao_Bag.ReadInt("mi_ImpressDocument");
			this.ImpressForm = ao_Bag.ReadInt("mi_ImpressForm");
			this.ImpressFormPrintXml = ao_Bag.ReadString("ms_ImpressFormPrintXml");
			this.ImpressRoles = ao_Bag.ReadString("ms_ImpressRoles");
			this.ImpressProps = ao_Bag.ReadString("ms_ImpressProps");
			this.ImpressOther = ao_Bag.ReadString("ms_ImpressOther");
			this.ReturnContent = ao_Bag.ReadString("ms_ReturnContent");
			this.ReturnAsDocument = ao_Bag.ReadBoolean("mb_ReturnAsDocument");
			this.ReturnBody = ao_Bag.ReadInt("mi_ReturnBody");
			this.ReturnDocument = ao_Bag.ReadInt("mi_ReturnDocument");
			this.ReturnForm = ao_Bag.ReadInt("mi_ReturnForm");
			this.ReturnFormPrintXml = ao_Bag.ReadString("ms_ReturnFormPrintXml");
			this.ReturnRoles = ao_Bag.ReadString("ms_ReturnRoles");
			this.ReturnProps = ao_Bag.ReadString("ms_ReturnProps");
			this.ReturnOther = ao_Bag.ReadString("ms_ReturnOther");
			this.StatePToCType = ao_Bag.ReadInt("mi_StatePToCType");
			this.StateCToPType = ao_Bag.ReadInt("mi_StateCToPType");
			this.PassCommentType = ao_Bag.ReadInt("mi_PassCommentType");
			this.PassSignType = ao_Bag.ReadInt("mi_PassSignType");
			this.PassVoiceType = ao_Bag.ReadInt("mi_PassVoiceType");
			this.ExportScript = ao_Bag.ReadString("ms_ExportScript");
			this.ImportScript = ao_Bag.ReadString("ms_ImportScript");
			this.IsNeedReturn = ao_Bag.ReadBoolean("mb_IsNeedReturn");
		}
	}
}
