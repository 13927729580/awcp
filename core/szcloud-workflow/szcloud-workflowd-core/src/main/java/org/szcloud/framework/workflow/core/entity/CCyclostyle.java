package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.business.TCyclostyle;
import org.szcloud.framework.workflow.core.business.TWorkAdmin;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EDocumentType;
import org.szcloud.framework.workflow.core.emun.EFileSaveType;
import org.szcloud.framework.workflow.core.emun.EFirstShowPage;
import org.szcloud.framework.workflow.core.emun.EWorkItemFlowFlag;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.szcloud.framework.workflow.core.module.ModDigest;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 公文模板对象：是公文数据处理对象模型的最基本对象，它是其它公文 数据处理对象的总入口，<br>
 * 对于公文数据中的所使用的流程、各种处理方 法和属性都在本对象中处理。
 * 
 * @author Jackie.Wang
 */
public class CCyclostyle extends CBase {

	/**
	 * 导出方式
	 */
	public static final int Const_ExImport_Type = 1;

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 权限集合
	 */
	public List<CRight> Rights = new ArrayList<CRight>();

	/**
	 * 根据权限标识获取权限
	 * 
	 * @param al_Id
	 * @return
	 */
	public CRight getRightById(int al_Id) {
		for (CRight lo_Right : this.Rights) {
			if (lo_Right.RightID == al_Id)
				return lo_Right;
		}
		return null;
	}

	/**
	 * 角色集合
	 */
	public ArrayList<CRole> Roles = new ArrayList<CRole>();

	/**
	 * 根据角色标识获取角色
	 * 
	 * @param al_Id
	 * @return
	 */
	public CRole getRoleById(int al_Id) {
		for (CRole lo_Role : this.Roles) {
			if (lo_Role.RoleID == al_Id)
				return lo_Role;
		}
		return null;
	}

	/**
	 * 二次开发函数集合
	 */
	public ArrayList<CScript> Scripts = new ArrayList<CScript>();

	/**
	 * 根据二次开发函数标识获取二次开发函数
	 * 
	 * @param al_Id
	 * @return
	 */
	public CScript getScriptById(int al_Id) {
		if (this.Scripts == null)
			return null;
		for (CScript lo_Script : this.Scripts) {
			if (lo_Script.ScriptID == al_Id)
				return lo_Script;
		}
		return null;
	}

	/**
	 * 数据存储表格集合
	 */
	public List<CTable> Tables = new ArrayList<CTable>();

	/**
	 * 根据数据存储表格标识获取数据存储表格
	 * 
	 * @param al_Id
	 * @return
	 */
	public CTable getTableById(int al_Id) {
		if (this.Tables == null)
			return null;
		for (CTable lo_Table : this.Tables) {
			if (lo_Table.TableID == al_Id)
				return lo_Table;
		}
		return null;
	}

	/**
	 * 表单集合
	 */
	public List<CForm> Forms = new ArrayList<CForm>();

	/**
	 * 根据表单标识获取表单对象
	 * 
	 * @param al_Id
	 * @return
	 */
	public CForm getFormById(int al_Id) {
		if (this.Forms == null)
			return null;
		for (CForm lo_Form : this.Forms) {
			if (lo_Form.FormID == al_Id)
				return lo_Form;
		}
		return null;
	}

	/**
	 * 流程集合
	 */
	public List<CFlow> Flows = new ArrayList<CFlow>();

	/**
	 * 根据流程标识获取流程
	 * 
	 * @param al_Id
	 * @return
	 */
	public CFlow getFlowById(int al_Id) {
		if (this.Flows == null)
			return null;
		for (CFlow lo_Flow : this.Flows) {
			if (lo_Flow.FlowID == al_Id)
				return lo_Flow;
		}
		return null;
	}

	/**
	 * 附件集合，是出正文外的各种普通附件的集合，其中每种附件有以下几种存在方式:
	 */
	public List<CDocument> Documents = new ArrayList<CDocument>();

	/**
	 * 根据附件标识获取附件
	 * 
	 * @param al_Id
	 * @return
	 */
	public CDocument getDocumentById(int al_Id) {
		if (this.Documents == null)
			return null;
		for (CDocument lo_Doc : this.Documents) {
			if (lo_Doc.DocumentID == al_Id)
				return lo_Doc;
		}
		return null;
	}

	/**
	 * 表头属性集合
	 */
	public List<CProp> Props = new ArrayList<CProp>();

	/**
	 * 根据表头属性标识获取表头属性
	 * 
	 * @param al_Id
	 * @return
	 */
	public CProp getPropById(int al_Id) {
		if (this.Props == null)
			return null;
		for (CProp lo_Prop : this.Props) {
			if (lo_Prop.PropID == al_Id)
				return lo_Prop;
		}
		return null;
	}

	/**
	 * 正文对象，是一种特殊的附件对象，在表现中以以下一种特殊的方式存储于内存中，在它的
	 * 类型中包括几种：RTFTypeBody、CommondBody、FileVersionBody、WordVersionBody
	 */
	public CDocument Body = null;

	/**
	 * 所属的工作流实例，只有在正式公文流转时才有
	 */
	public CWorkItem WorkItem = null;

	/**
	 * 包含的公文发布对象
	 */
	public CPublicTemplate PublicTemplate = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 公文模板标识
	 */
	public int CyclostyleID = -1;

	/**
	 * 公文模板名称
	 */
	public String CyclostyleName = "未命名公文模板";

	/**
	 * 公文模板代码<br>
	 * <br>
	 * 模板代码是一个公文模板可见的标识，可唯一代表公文模板的一个特征，<br>
	 * 唯一代码用来标识不同服 务器之间公文模板，以达到唯一识的效果，该代码建议规则为：<br>
	 * <br>
	 * 项目代号 服务器代码 + 流水系列号<br>
	 * <br>
	 * 其中: <br>
	 * 项目代号建议使用四位，由项目开始时定义，由字母加项目系列号组成，如：A005，F076等；<br>
	 * 服务器代号为系列号组成，如：172，054等；<br>
	 * 流水系列号为标识公文模板的代码，如：132，009等；<br>
	 * 因此，模板代码可以为B089981031
	 */
	public String CyclostyleCode = "";

	/**
	 * 获取系统自动生成模板代码
	 * 
	 * @return
	 */
	public String getAutoCyclostyleCode() {
		String ls_PropjectCode = "";
		String ls_ServerCode = "";
		String ls_Seq = "";

		ls_PropjectCode = this.Logon.getParaValue("PROJECT_NO");
		ls_ServerCode = this.Logon.getParaValue("SERVER_NO");

		if (ls_PropjectCode == null) {
			ls_PropjectCode = "T001";
		}
		if (ls_ServerCode == null) {
			ls_ServerCode = "001";
		}

		if (this.CyclostyleID == -1) {
			// 修改
			ls_Seq = CSqlHandle
					.getValueBySql("SELECT SequenceValue FROM SysSequence WHERE SequenceCode = \'CyclostyleID\'");
			if (ls_Seq == null) {
				ls_Seq = "1";
			}
			ls_Seq = ("00" + ls_Seq).substring(("00" + ls_Seq).length() - 3, 3);
		} else {
			ls_Seq = ("00" + this.CyclostyleID).substring(
					("00" + this.CyclostyleID).length() - 3, 3);
		}

		return ls_PropjectCode + ls_ServerCode + ls_Seq;
	}

	/**
	 * 公文模板作者标识
	 */
	public int AuthorID = 0;

	/**
	 * 公文模板作者
	 */
	public String Author = "";

	/**
	 * 分公司标识
	 */
	public int BelongID = 0;

	/**
	 * 公文模板创建时间
	 */
	public Date CreateDate = MGlobal.getNow();

	/**
	 * 公文模板的修改人标识
	 */
	public int EditorID = 0;

	/**
	 * 公文模板的修改人
	 */
	public String Editor = "";

	/**
	 * 公文模板的修改时间
	 */
	public Date EditDate = MGlobal.getNow();

	/**
	 * 公文模板分类标识
	 */
	public int TypeID = 0;

	/**
	 * 角色的最大标识
	 */
	public int RoleMaxID = 0;

	/**
	 * 表头属性的最大标识
	 */
	public int PropMaxID = 0;

	/**
	 * 附件的最大标识
	 */
	public int DocumentMaxID = 0;

	/**
	 * 权限的最大标识
	 */
	public int RightMaxID = 0;

	/**
	 * 数据存储表格的最大标识
	 */
	public int TableMaxID = 0;

	/**
	 * 数据表格的最大标识
	 */
	public int FormMaxID = 0;

	/**
	 * 数据元素标识的最大值
	 */
	public int FormCellMaxID = 0;

	/**
	 * 数据连线标识的最大值
	 */
	public int FormLineMaxID = 0;

	/**
	 * 表格单元标识的最大值
	 */
	public int FormTableMaxID = 0;

	/**
	 * 流程排列序号的标识
	 */
	public int FlowIndexMaxID = 0;

	/**
	 * 公文模板板二次函数标识
	 */
	public int ScriptMaxID = 0;

	/**
	 * Web端显示表格对象最大标识
	 */
	public int TabulationMaxID = 0;

	/**
	 * 是否有正文
	 */
	public boolean HaveBody = true;

	/**
	 * 设置是否有正文
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setHaveBody(boolean value) throws Exception {
		if (this.HaveBody == value)
			return;
		if (this.HaveBody) {
			this.Body = new CDocument();
			this.Body.Cyclostyle = this;
			this.Body.initDocument();
			this.DocumentMaxID++;
			this.Body.DocumentID = this.DocumentMaxID;
			this.Body.DocumentName = "公文正文";
			this.Body.DocumentType = EDocumentType.CommondBody;
			this.Body.DocumentExt = "txt";
		} else {
			this.Body.ClearUp();
			this.Body = null;
		}
	}

	/**
	 * 是否有附件
	 */
	public boolean HaveDocument = false;

	/**
	 * 是否有表单
	 */
	public boolean HaveForm = true;

	/**
	 * 公文模板流转时首先显示的页面
	 */
	public int FirstShowPage = EFirstShowPage.ShowBodyPage;

	/**
	 * 公文流转所使用的方式
	 */
	public int FlowFlag = EWorkItemFlowFlag.ConditionFlowRule;

	/**
	 * 打开方式，=0-Xml方式，=1-传统方式；
	 */
	public int OpenType = EDataHandleType.XmlType;

	/**
	 * 获取保存方式：=0-Xml方式；=1-传统方式；
	 * 
	 * @return
	 */
	public int getSaveType() {
		return EDataHandleType.XmlType;
	}

	/**
	 * 公文模板说明
	 */
	public String Summary = "";

	/**
	 * 内存变量，记录被删除的流程标识连接字符串，用[;]分隔
	 */
	public String DeletedFlowIDs = "";

	/**
	 * 公文模板绑定图象
	 */
	public String BindImage = "";

	/**
	 * 模板发布使用类型<br>
	 * =0 - 不可使用<br>
	 * =1 - 在管理中使用[列表]<br>
	 * =2 - 在应用中使用<br>
	 * =4 - 在新建时使用
	 */
	public int IsPublicUsing = 0;

	/**
	 * 分页类型<br>
	 * =0 - 不分页显；<br>
	 * =1 - 分页显示；<br>
	 * =2 - 正文附件分页；
	 */
	public int PageType = 0;

	/**
	 * 首页显示内容<br>
	 * =0 - 正文 <br>
	 * =1 - 附件<br>
	 * >1 - 表单
	 */
	public int FirstType = 0;

	/**
	 * 表单显示采用样式
	 */
	public int FormStyle = 0;

	/**
	 * 同一用户在不同步骤同时具有处理公文的状态时的处理设置 <br>
	 * =0 - 相互独立处理 <br>
	 * =1 - 统一方式处理<br>
	 * =2 - 用户定义处理
	 */
	public int SamePartDealType = 0;

	/**
	 * 同一用户在不同步骤同时具有处理公文的状态时的处理设置<br>
	 * =0 - 相互独立处理 <br>
	 * =1 - 统一方式处理<br>
	 * =2 - 用户定义处理
	 * 
	 * @param value
	 */
	public void setSamePartDealType(int value) {
		this.SamePartDealType = value;
		if (this.SamePartDealType < 2) {
			ms_SamePartActivities = "";
		}
	}

	/**
	 * 同一用户在不同步骤同时处理步骤集合设置在this.SamePartDealType=2时生效
	 */
	private String ms_SamePartActivities = "";

	/**
	 * 同一用户在不同步骤同时处理步骤集合设置在this.SamePartDealType=2时生效
	 * 
	 * @return
	 */
	public String getSamePartActivities() {
		if (this.SamePartDealType < 2) {
			ms_SamePartActivities = "";
		}
		return ms_SamePartActivities;
	}

	/**
	 * 同一用户在不同步骤同时处理步骤集合设置在this.SamePartDealType=2时生效
	 * 
	 * @param value
	 */
	public void setSamePartActivities(String value) {
		if (this.SamePartDealType < 2) {
			ms_SamePartActivities = "";
		} else {
			ms_SamePartActivities = value;
		}
	}

	/**
	 * 外部引用的CSS文件
	 */
	public String CssFiles = null;

	/**
	 * 排列顺序
	 */
	public int OrderID = 0;

	/**
	 * 正文/附件存储位置类型
	 */
	public int FileSaveType = EFileSaveType.DefaultSaveType;

	/**
	 * 正文/附件存储位置类型
	 * 
	 * @param value
	 */
	public void setFileSaveType(int value) {
		this.FileSaveType = value;
		if (this.FileSaveType != EFileSaveType.RuleSaveType) {
			this.FileSaveID = 0;
		}
	}

	/**
	 * 正文/附件存储标识，当this.FileSaveType = RuleSaveType(3)时有效
	 */
	public int FileSaveID = 0;

	/**
	 * 正文/附件存储标识，当this.FileSaveType = RuleSaveType(3)时有效
	 * 
	 * @param value
	 */
	public void setFileSaveID(int value) {
		this.FileSaveID = (this.FileSaveType == EFileSaveType.RuleSaveType ? value
				: 0);
	}

	/**
	 * 正文/附件存储依据日期
	 */
	public Date FileSaveDate = MGlobal.getNow();

	/**
	 * 扩展字段定义的Xml对象
	 */
	private Document mo_ExtendFieldsXml = null;

	/**
	 * 扩展字段定义的Xml对象
	 * 
	 * @return
	 */
	public Document getExtendFieldsXml() {
		return mo_ExtendFieldsXml;
	}

	/**
	 * 扩展字段定义的Xml对象
	 * 
	 * @param value
	 */
	public void setExtendFieldsXml(Document value) {
		mo_ExtendFieldsXml = value;
	}

	/**
	 * 扩展字段定义的Xml
	 * 
	 * @return
	 */
	public String getExtendFields() {
		if (mo_ExtendFieldsXml == null)
			setExtendFields(null);
		return MXmlHandle.getXml(mo_ExtendFieldsXml);
	}

	/**
	 * 扩展字段定义的Xml
	 * 
	 * @param value
	 */
	public void setExtendFields(String value) {
		Document lo_Xml = MXmlHandle.LoadXml(value);
		if (lo_Xml == null)
			mo_ExtendFieldsXml = MXmlHandle.LoadXml("<FileField />");
		else
			mo_ExtendFieldsXml = lo_Xml;

		Element lo_Extend = mo_ExtendFieldsXml.getDocumentElement();

		// 修改
		Node lo_Node = MXmlHandle.getNodeByPath(lo_Extend, "FileField");
		if (lo_Node != null) {
			mo_FileFieldXml = MXmlHandle.LoadXml(MXmlHandle.getXml(lo_Node));
			lo_Extend.removeChild(lo_Node);
			if (this.Logon.getExistExtendField()) {
				AddExtendFields();
			}
		}

		if (mo_PrintTempXml == null) {
			mo_PrintTempXml = MXmlHandle.LoadXml("<PrintTemp />");
		}

		lo_Node = MXmlHandle.getNodeByPath(lo_Extend, "PrintTemp");
		if (lo_Node != null) {
			mo_PrintTempXml = MXmlHandle.LoadXml(MXmlHandle.getXml(lo_Node));
			lo_Extend.removeChild(lo_Node);
		}

		if (mo_PublicTempXml == null) {
			mo_PublicTempXml = MXmlHandle.LoadXml("<PublicTemp />");
		}

		lo_Node = MXmlHandle.getNodeByPath(lo_Extend, "PublicTemp");
		if (lo_Node != null) {
			mo_PublicTempXml = MXmlHandle.LoadXml(MXmlHandle.getXml(lo_Node));
			lo_Extend.removeChild(lo_Node);
		}

		if (MGlobal.isInt(lo_Extend.getAttribute("PageType"))) {
			this.PageType = Integer
					.parseInt(lo_Extend.getAttribute("PageType"));
		}

		if (MGlobal.isInt(lo_Extend.getAttribute("FirstType"))) {
			this.FirstType = Integer.parseInt(lo_Extend
					.getAttribute("FirstType"));
		}

		if (MGlobal.isInt(lo_Extend.getAttribute("FormStyle"))) {
			this.FormStyle = Integer.parseInt(lo_Extend
					.getAttribute("FormStyle"));
		}

		if (MGlobal.isInt(lo_Extend.getAttribute("SamePartDealType"))) {
			this.SamePartDealType = Integer.parseInt(lo_Extend
					.getAttribute("SamePartDealType"));
		}
		ms_SamePartActivities = lo_Extend.getAttribute("SamePartActivities");
		this.CssFiles = lo_Extend.getAttribute("CssFiles");
		this.IncludeFiles = lo_Extend.getAttribute("IncludeFiles");
		this.VBScript = lo_Extend.getAttribute("VBScript");
		this.JavaScript = lo_Extend.getAttribute("JavaScript");
	}

	/**
	 * 缺省增加附加字段(FI1~FI5,FSA1~FSA5,FSB1~FSB5)
	 */
	private void AddExtendFields() {
		if (mo_FileFieldXml == null)
			return;

		Element lo_Node = MXmlHandle.getNodeByPath(
				mo_ExtendFieldsXml.getDocumentElement(), "Field[@ID=\'121\']");
		if (lo_Node != null)
			return; // 已经增加了

		String ls_Sql = "SELECT * FROM WorkItemFieldDefine Field WHERE ID > 120 AND ID < 140 ORDER BY OrderIndex DESC";
		String ls_Xml = "<ExtendFields>"
				+ CSqlHandle.getXmlBySql(ls_Sql).replace("@@", "")
				+ "</ExtendFields>";
		Document lo_Xml = MXmlHandle.LoadXml(ls_Xml);
		if (lo_Xml != null)
			MXmlHandle.addNode(mo_ExtendFieldsXml.getDocumentElement(),
					lo_Xml.getDocumentElement());
		lo_Xml = null;
	}

	/**
	 * 归档字段定义，该定义内容存储于扩展字段中，格式如下： <FileField> <Field ID='#' Type='0/1/2'
	 * Source='*' /> ... ... </FileField>
	 */
	private Document mo_FileFieldXml = null;

	/**
	 * 归档字段定义，该定义内容存储于扩展字段中
	 * 
	 * @return
	 */
	public Document getFileFieldXml() {
		return mo_FileFieldXml;
	}

	/**
	 * 读取归档字段定义Xml内容
	 * 
	 * @return
	 */
	public String getFileFieldXmlValue() {
		if (mo_FileFieldXml == null)
			return "";
		return MXmlHandle.getXml(mo_FileFieldXml);
	}

	/**
	 * 设置归档字段定义Xml内容
	 * 
	 * @param value
	 */
	public void setFileFieldXmlValue(String value) {
		Document lo_Xml = MXmlHandle.LoadXml(value);
		if (lo_Xml != null)
			mo_FileFieldXml = lo_Xml;
	}

	/**
	 * 公文模板打印模板定义，该定义内容存储于扩展字段中，格式如下：
	 */
	private Document mo_PrintTempXml = null;

	/**
	 * 公文模板打印模板定义
	 * 
	 * @return
	 */
	public Document getPrintTempXml() {
		return mo_PrintTempXml;
	}

	/**
	 * 读取公文模板打印模板定义的XML字符串
	 * 
	 * @return
	 */
	public String getPrintTempXmlValue() {
		if (mo_PrintTempXml == null)
			return null;
		return MXmlHandle.getXml(mo_PrintTempXml);
	}

	/**
	 * 设置公文模板打印模板定义的XML字符串
	 * 
	 * @param value
	 */
	public void setPrintTempXmlValue(String value) {
		Document lo_Xml = MXmlHandle.LoadXml(value);
		if (lo_Xml != null)
			mo_PrintTempXml = lo_Xml;
	}

	/**
	 * 公文模板发布模板定义，该定义内容存储于扩展字段中，格式如下： <PublicTemp> <... /> </PublicTemp>
	 */
	private Document mo_PublicTempXml = null;

	/**
	 * 公文模板发布模板定义
	 * 
	 * @return
	 */
	public Document getPublicTempXml() {
		return mo_PublicTempXml;
	}

	/**
	 * 读取公文模板发布模板定义的XML字符串
	 * 
	 * @return
	 */
	public String getPublicTempXmlValue() {
		if (mo_PublicTempXml == null)
			return null;
		return mo_PublicTempXml.getTextContent();
	}

	/**
	 * 设置公文模板发布模板定义的XML字符串
	 * 
	 * @param value
	 */
	public void setPublicTempXmlValue(String value) {
		Document lo_Xml = MXmlHandle.LoadXml(value);
		if (lo_Xml != null) {
			mo_PublicTempXml = null;
			mo_PublicTempXml = lo_Xml;
		}
		lo_Xml = null;
	}

	/**
	 * 公文执行的前端脚本包含文件
	 */
	public String IncludeFiles = null;

	/**
	 * 公文执行的前端VBScript脚本
	 */
	public String VBScript = null;

	/**
	 * 公文执行的前端JaveScript脚本
	 */
	public String JavaScript;

	/**
	 * 读取正文类型
	 * 
	 * @return
	 */
	public int getBodyType() {
		if (this.HaveBody) {
			return this.Body.DocumentType;
		} else {
			return EDocumentType.NotAnyDocumentType;
		}
	}

	/**
	 * 设置正文类型
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setBodyType(int value) throws Exception {
		if (this.HaveBody) {
			if (this.Body.DocumentType == value)
				return;
		} else {
			if (value == EDocumentType.NotAnyDocumentType)
				return;
		}

		if (value == EDocumentType.NotAnyDocumentType) {
			this.Body.ClearUp();
			this.Body = null;
			this.HaveBody = false;
		} else {
			if (this.Body == null) {
				this.Body = new CDocument();
				this.Body.Cyclostyle = this;
				this.Body.initDocument();
				this.DocumentMaxID++;
				this.Body.DocumentID = this.DocumentMaxID;
				this.Body.DocumentName = "公文正文";
				this.Body.DocumentType = EDocumentType.TextTypeBody;
				this.Body.DocumentExt = "txt";
			}

			switch (value) {
			case EDocumentType.CommondBody:
				this.Body.setDocumentType(EDocumentType.CommondBody);
				break;
			case EDocumentType.FileVersionBody:
				this.Body.setDocumentType(EDocumentType.FileVersionBody);
				break;
			case EDocumentType.RTFTypeBody:
				this.Body.setDocumentType(EDocumentType.RTFTypeBody);
				break;
			case EDocumentType.WordVersionBody:
				this.Body.setDocumentType(EDocumentType.WordVersionBody);
				break;
			default: // TextTypeBody
				this.Body.setDocumentType(EDocumentType.TextTypeBody);
				break;
			}

			this.HaveBody = true;
		}
	}

	/**
	 * 读取是否在拟稿人编辑时需要使用版本控制
	 */
	public boolean getFirstIsVersion() {
		if (mo_ExtendFieldsXml == null) {
			return false;
		}
		if (mo_ExtendFieldsXml.getDocumentElement().getAttribute(
				"FirstIsVersion") == null) {
			return false;
		} else {
			return (mo_ExtendFieldsXml.getDocumentElement().getAttribute(
					"FirstIsVersion") == "1");
		}
	}

	/**
	 * 设置是否在拟稿人编辑时需要使用版本控制
	 * 
	 * @param value
	 */
	public void setFirstIsVersion(boolean value) {
		if (mo_ExtendFieldsXml == null) {
			return;
		}

		mo_ExtendFieldsXml.getDocumentElement().setAttribute("FirstIsVersion",
				value ? "1" : "0");
	}

	/**
	 * 初始化
	 */
	public CCyclostyle() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CCyclostyle(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 
	 * @return
	 */
	public String getCheckCode() {
		switch (MGlobal.getWeek()) {
		case 0:
			return ModDigest
					.DecodeString("LAV3KHL7V3DceAL7G9VHOVLEe9BEeVNcCVCc8Vcme7z9eckchhfANEz9NEMEEieVPVM9PEOE");
		case 1:
			return ModDigest
					.DecodeString("wcEHCcEiLVaKLEdc8VDEMA9hhhX7dEVKz9LA8VzcycL7L7X7bhMVh3CcLED7g7Km9Ke7eEKm");
		case 2:
			return ModDigest
					.DecodeString("EiMcCEN7CcD7N7OVBEOVLEPEN9BEeVEmC9ycBEycD7aKh3X7dVycbhw99hEmbmwAchKH9hhh");
		case 3:
			return ModDigest
					.DecodeString("NEd7ycKKaK9KNAEm8VzcViNVPVG9MAbKw9dE8VPVchVHh3BECch3w7L7KHwAdVVmbhd7dVNA");
		case 4:
			return ModDigest
					.DecodeString("Nchi93M7dcM7PEMEkcLEcHCVyckcCcV3PEhmkczcE3PEdEkcy9MVhmeAchM9wVcmEmOEKmah");
		case 5:
			return ModDigest
					.DecodeString("eEhmec9iKieVOEhmBEecCEcKC98VdEMcVmEi8VycbmCVPEBEcHMAe7hHf7wcd7MVyccmOVhi");
		case 6:
			return ModDigest
					.DecodeString("LEbheVeEEmVmMVycX7M7dVNVMcX7CcNEzcLE8VycfAMc9iBEMECEEiN7fAe7Mcf7d9EhLEOE");
		default:
			return "";
		}
	}

	/**
	 * 用户(包括角色)转化成显示方式<br>
	 * [U] - 用户 <br>
	 * [G] - 分组 <br>
	 * [@] - 用户角色 <br>
	 * [&] - 系统角色[拟稿人的] <br>
	 * [#] - 系统角色[当前收件人的] <br>
	 * [%] - 部门角色[当前收件人的] <br>
	 * [as_Value] - 用户值，格式为：U**;G**;#**;@**;...<br>
	 * 
	 * @param as_Value
	 * @return
	 * @throws Exception
	 */
	public String convertUserValueToDisplay(String as_Value) throws Exception {
		try {
			if (MGlobal.isEmpty(as_Value))
				return "";

			String ls_UserIDs = "";
			String ls_RoleIDs = "";
			String ls_SRoleIDs = "";
			String ls_DRoleIDs = "";
			String ls_DeptIDs = "";

			String[] ls_Array = as_Value.split(";");
			for (int i = 0; i < ls_Array.length; i++) {
				if (MGlobal.isExist(ls_Array[i])) {
					String ls_Sign = ls_Array[i].substring(0, 1);
					if (ls_Sign.equals("U") || ls_Sign.equals("G")) {
						ls_UserIDs += ls_Array[i].substring(1,
								ls_Array[i].length())
								+ ",";
					} else if (ls_Sign.equals("@")) {
						ls_RoleIDs += ls_Array[i].substring(1,
								ls_Array[i].length())
								+ ",";
					} else if (ls_Sign.equals("&") || ls_Sign.equals("#")) {
						ls_SRoleIDs += ls_Array[i].substring(1,
								ls_Array[i].length())
								+ ",";
					} else if (ls_Sign.equals("%")) {
						int j = ls_Array[i].indexOf("%", 1) + 1;
						ls_DeptIDs += ls_Array[i].substring(1, j) + ",";
						ls_DRoleIDs += ls_Array[i].substring(j + 1,
								ls_Array[i].length())
								+ ",";
					} else { // 固定收件人用户/分组>
						ls_UserIDs += ls_Array[i] + ",";
					}
				}
			}

			String ls_Value = ";" + as_Value + ";";
			String ls_Sql, ls_One = "", ls_Two = "";
			List<Map<String, Object>> lo_Set;
			if (ls_UserIDs != "") {
				ls_Sql = "SELECT UserID, UserName, UserType FROM UserInfo WHERE UserID IN ("
						+ ls_UserIDs + "-1)";
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				for (int i = 0; i < lo_Set.size(); i++) {
					ls_One = MessageFormat.format(";{0}{1};", ((Short) lo_Set
							.get(i).get("UserType") == 1 ? "G" : "U"), lo_Set
							.get(i).get("UserID"));
					ls_Two = ";" + lo_Set.get(i).get("UserName") + ";";
					ls_Value = ls_Value.replaceAll(ls_One, ls_Two);
				}
				lo_Set = null;
			}

			if (MGlobal.isExist(ls_RoleIDs)) {
				for (CRole lo_Role : Roles) {
					ls_One = ";@" + Integer.toString(lo_Role.RoleID) + ";";
					ls_Two = ";" + lo_Role.RoleName + ";";
					ls_Value = ls_Value.replaceAll(ls_One, ls_Two);
				}
			}

			if (MGlobal.isExist(ls_SRoleIDs)) {
				ls_Sql = "SELECT PositionID, PositionName FROM UserPosition WHERE PositionID IN ("
						+ ls_SRoleIDs + "-1)";
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				for (int i = 0; i < lo_Set.size(); i++) {
					ls_One = ";&" + lo_Set.get(i).get("PositionID") + ";";
					ls_Two = ";拟稿人的" + lo_Set.get(i).get("PositionName") + ";";
					ls_Value = ls_Value.replaceAll(ls_One, ls_Two);
					ls_One = ";#" + lo_Set.get(i).get("PositionID") + ";";
					ls_Two = ";当前收件人的" + lo_Set.get(i).get("PositionName")
							+ ";";
					ls_Value = ls_Value.replaceAll(ls_One, ls_Two);
				}
				lo_Set = null;
			}

			if (MGlobal.isExist(ls_DRoleIDs)) {
				ls_Sql = "SELECT DeptID, DeptName FROM DeptInfo WHERE DeptID IN ("
						+ ls_DeptIDs + "-1)";
				List<Map<String, Object>> lo_Dept = CSqlHandle
						.getJdbcTemplate().queryForList(ls_Sql);

				ls_Sql = "SELECT PositionID, PositionName FROM UserPosition WHERE PositionID IN ("
						+ ls_DRoleIDs + "-1)";
				lo_Set = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);

				for (int i = 0; i < lo_Set.size(); i++) {
					for (int j = 0; j < lo_Dept.size(); j++) {
						ls_One = ";%" + lo_Dept.get(j).get("DeptID") + "%";
						ls_One = ls_One + lo_Set.get(i).get("PositionID") + ";";
						ls_Two = ";部门[" + lo_Dept.get(j).get("DeptName") + "];";
						ls_Two = ls_Two + lo_Set.get(i).get("PositionName")
								+ ";";
						ls_Value = ls_Value.replaceAll(ls_One, ls_Two);
					}
				}
			}

			ls_Array = ls_Value.split(";");
			ls_Value = "";
			for (int i = 0; i < ls_Array.length; i++) {
				if (MGlobal.isExist(ls_Array[i])) {
					if ("UG@&#%".indexOf(ls_Array[i].substring(0, 1)) == -1) {
						ls_Value += ls_Array[i] + ";";
					}
				}
			}
			return ls_Value;
		} catch (Exception e) {
			this.Raise(e, "convertUserValueToDisplay", null);
			return "";
		}
	}

	/**
	 * 筛选非法用户(包括角色)<br>
	 * [U] - 用户 <br>
	 * [G] - 分组 <br>
	 * [@] - 用户角色 <br>
	 * [&] - 系统角色[拟稿人的] <br>
	 * [#] - 系统角色[当前收件人的] <br>
	 * [%] - 部门角色[当前收件人的] <br>
	 * [as_Value] - 用户值，格式为：U**;G**;#**;@**;...
	 * 
	 * @param as_Value
	 * @return
	 * @throws Exception
	 */
	public String filterValidUserValue(String as_Value) throws Exception {
		try {
			if (as_Value == null || as_Value.equals("")) {
				return "";
			}
			String ls_Str = "";
			String ls_Sql = "";
			String ls_Value = "";
			String[] ls_Array = null;
			// String ls_One;
			// String ls_Two;
			String ls_UserIDs = "";
			ls_UserIDs = "";
			String ls_RoleIDs = "";
			ls_RoleIDs = "";
			String ls_SRoleIDs = "";
			ls_SRoleIDs = "";
			String ls_DRoleIDs = "";
			ls_DRoleIDs = "";
			String ls_DeptIDs = "";
			ls_DeptIDs = "";

			ls_Array = as_Value.split(";");
			for (int i = 0; i <= (ls_Array.length - 1); i++) {
				String str = ls_Array[i].substring(0, 1);
				if (str != null) {
					if (str == "U") {
					}
					if (str == "G") {
						ls_UserIDs = ls_UserIDs
								+ ls_Array[i].substring(ls_Array[i].length()
										- (ls_Array[i].length() - 1),
										ls_Array[i].length() - 1) + ",";
						break;
					}
					if (str == "@") {
						ls_RoleIDs = ls_RoleIDs
								+ ls_Array[i].substring(ls_Array[i].length()
										- (ls_Array[i].length() - 1),
										ls_Array[i].length() - 1) + ",";
						break;
					}
					if (str == "&") {
					}
					if (str == "#") {
						ls_SRoleIDs = ls_SRoleIDs
								+ ls_Array[i].substring(ls_Array[i].length()
										- (ls_Array[i].length() - 1),
										ls_Array[i].length() - 1) + ",";
						break;
					}
					if (str == "%") {
						int j = (ls_Array[i]).indexOf("%", 1) + 1;
						ls_DeptIDs = ls_DeptIDs
								+ ls_Array[i].substring(1, j - 2) + ",";
						ls_DRoleIDs = ls_DRoleIDs
								+ ls_Array[i].substring(ls_Array[i].length()
										- (ls_Array[i].length() - j),
										ls_Array[i].length() - j) + ",";
						break;
					}
				}
				// switch ()
				// {
				// case "U":
				// case "G":
				// ls_UserIDs = ls_UserIDs +
				// ls_Array[i].SubString(ls_Array[i].length() -
				// (ls_Array[i].length() - 1), ls_Array[i].length() - 1) + ",";
				// break;
				// case "@":
				// ls_RoleIDs = ls_RoleIDs +
				// ls_Array[i].SubString(ls_Array[i].length() -
				// (ls_Array[i].length() - 1), ls_Array[i].length() - 1) + ",";
				// break;
				// case "&":
				// case "#":
				// ls_SRoleIDs = ls_SRoleIDs +
				// ls_Array[i].SubString(ls_Array[i].length() -
				// (ls_Array[i].length() - 1), ls_Array[i].length() - 1) + ",";
				// break;
				// case "%":
				// j = (ls_Array[i]).IndexOf("%", 1) + 1;
				// ls_DeptIDs = ls_DeptIDs + ls_Array[i].SubString(1, j - 2) +
				// ",";
				// ls_DRoleIDs = ls_DRoleIDs +
				// ls_Array[i].SubString(ls_Array[i].length() -
				// (ls_Array[i].length() - j), ls_Array[i].length() - j) + ",";
				// break;
				// }
			}

			ls_Value = ";";
			List<Map<String, Object>> lo_Rec = null;
			if (ls_UserIDs != "") {
				ls_Sql = "SELECT UserID, UserName, UserType FROM UserInfo WHERE UserID IN ("
						+ ls_UserIDs + "-1)";
				lo_Rec = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				// lo_Rec.CursorLocation = ADODB.CursorLocationEnum.adUseClient;
				// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
				// ADODB.CursorTypeEnum.adOpenKeyset,
				// ADODB.LockTypeEnum.adLockReadOnly, -1);
				for (int i = 0; i < lo_Rec.size(); i++) {
					ls_Value = ls_Value
							+ ((Integer.parseInt(lo_Rec.get(i).get("UserType")
									.toString()) == 1) ? "G" : "U")
							+ lo_Rec.get(i).get("UserID") + ";";
					// lo_Rec.MoveNext();
				}
			}

			CRole lo_Role = new CRole();
			if (ls_RoleIDs != "") {
				for (CRole tempLoopVar_lo_Role : Roles) {
					lo_Role = tempLoopVar_lo_Role;
					ls_Value = ls_Value + "@"
							+ Integer.toString(lo_Role.RoleID) + ";";
				}
			}

			if (ls_SRoleIDs != "") {
				ls_Sql = "SELECT PositionID, PositionName FROM UserPosition WHERE PositionID IN ("
						+ ls_SRoleIDs + "-1)";
				lo_Rec = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				// lo_Rec.CursorLocation = ADODB.CursorLocationEnum.adUseClient;
				// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
				// ADODB.CursorTypeEnum.adOpenKeyset,
				// ADODB.LockTypeEnum.adLockReadOnly, -1);
				for (int i = 0; i < lo_Rec.size(); i++) {
					ls_Value = ls_Value + "&" + lo_Rec.get(i).get("PositionID")
							+ ";";
					ls_Value = ls_Value + "#" + lo_Rec.get(i).get("PositionID")
							+ ";";
					// lo_Rec.MoveNext();
				}
			}

			List<Map<String, Object>> lo_Dept = null;
			if (ls_DRoleIDs != "") {
				ls_Sql = "SELECT DeptID, DeptName FROM DeptInfo WHERE DeptID IN ("
						+ ls_DeptIDs + "-1)";
				lo_Dept = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				// lo_Dept.CursorLocation =
				// ADODB.CursorLocationEnum.adUseClient;
				// lo_Dept.Open(ls_Sql, GlobalPara.objConnection,
				// ADODB.CursorTypeEnum.adOpenKeyset,
				// ADODB.LockTypeEnum.adLockReadOnly, -1);
				//
				ls_Sql = "SELECT PositionID, PositionName FROM UserPosition WHERE PositionID IN ("
						+ ls_DRoleIDs + "-1)";
				lo_Rec = CSqlHandle.getJdbcTemplate().queryForList(ls_Sql);
				// lo_Rec.CursorLocation = ADODB.CursorLocationEnum.adUseClient;
				// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
				// ADODB.CursorTypeEnum.adOpenKeyset,
				// ADODB.LockTypeEnum.adLockReadOnly, -1);
				// while (lo_Rec.next()) {
				// while (lo_Dept.next()) {
				for (int i = 0; i < lo_Rec.size(); i++) {
					for (int j = 0; j < lo_Dept.size(); j++) {
						ls_Value = ls_Value + "%"
								+ lo_Dept.get(i).get("DeptID") + "%";
						ls_Value = ls_Value + lo_Rec.get(i).get("PositionID")
								+ ";";
						// lo_Dept.MoveNext();
					}
					// lo_Rec.MoveNext();
				}
				lo_Dept = null;
			}
			lo_Rec = null;

			ls_Array = as_Value.split(";");
			ls_Str = "";
			for (int i = 0; i <= (ls_Array.length - 1); i++) {
				if (ls_Array[i] != "") {
					if (ls_Value.indexOf(";" + ls_Array[i] + ";") + 1 > 0) {
						ls_Str = ls_Str + ls_Array[i] + ";";
					}
				}
			}
			return ls_Str;
		} catch (Exception e) {
			this.Raise(e, "FilterValidUserValue", null);
		}
		return "";
	}

	/**
	 * 获取当前公文文件存储路径
	 */
	public String getFileSavePath() {
		return this.Logon.getFileSavePath(this.CyclostyleID, this.FileSaveType,
				this.FileSaveID, this.FileSaveDate);
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @param ab_DeleteGuid
	 * @throws Exception
	 * @throws SQLException
	 */
	// ab_DeleteGuid 是否注销系统在数据库中存在的唯一标识
	public void ClearUp(boolean ab_DeleteGuid) throws Exception {
		// 角色集合
		if (this.Roles != null) {
			while (this.Roles.size() > 0) {
				CRole lo_Role = this.Roles.get(0);
				this.Roles.remove(lo_Role);
				lo_Role.ClearUp();
				lo_Role = null;
			}
			this.Roles = null;
		}

		// 二次开发函数集合
		if (this.Scripts != null) {
			while (Scripts.size() > 0) {
				CScript lo_Script = this.Scripts.get(0);
				this.Scripts.remove(lo_Script);
				lo_Script.ClearUp();
				lo_Script = null;
			}
			this.Scripts = null;
		}

		// 数据存储表格集合
		if (this.Tables != null) {
			while (Tables.size() > 0) {
				CTable lo_Table = Tables.get(0);
				this.Tables.remove(lo_Table);
				lo_Table.ClearUp();
				lo_Table = null;
			}
			this.Tables = null;
		}

		// 表单集合
		if (this.Forms != null) {
			while (this.Forms.size() > 0) {
				CForm lo_Form = this.Forms.get(0);
				this.Forms.remove(lo_Form);
				lo_Form.ClearUp();
				lo_Form = null;
			}
			this.Forms = null;
		}

		// 流程集合
		if (this.Flows != null) {
			while (this.Flows.size() > 0) {
				CFlow lo_Flow = this.Flows.get(0);
				this.Flows.remove(lo_Flow);
				lo_Flow.ClearUp();
				lo_Flow = null;
			}
			this.Flows = null;
		}

		// 附件集合
		if (this.Documents != null) {
			while (this.Documents.size() > 0) {
				CDocument lo_Document = this.Documents.get(0);
				this.Documents.remove(lo_Document);
				lo_Document.ClearUp();
				lo_Document = null;
			}
			this.Documents = null;
		}

		// 表头属性集合
		if (this.Props != null) {
			while (this.Props.size() > 0) {
				CProp lo_Prop = this.Props.get(0);
				this.Props.remove(lo_Prop);
				lo_Prop.ClearUp();
				lo_Prop = null;
			}
			this.Props = null;
		}

		// 正文对象
		if (this.Body != null) {
			this.Body.ClearUp();
			this.Body = null;
		}

		// 所包含的公文发布对象
		if (this.PublicTemplate != null) {
			this.PublicTemplate.ClearUp();
			this.PublicTemplate = null;
		}

		// 所属的工作流实例，只有在正式公文流转时才有
		this.WorkItem = null;

		// 所包含的环境变量
		this.Logon = null;

		// 清除扩展字段内容定义
		mo_ExtendFieldsXml = null;

		// 清除归档字段定义
		mo_FileFieldXml = null;

		// 清除公文模板打印模板定义
		mo_PrintTempXml = null;

		// 清除公文模板发布模板定义
		mo_PublicTempXml = null;

		super.ClearUp();
	}

	/**
	 * 通过表头属性名称取得表头属性对象
	 * 
	 * @param as_PropName
	 * @return
	 */
	public CProp getPropByName(String as_PropName) {
		for (CProp lo_Prop : Props) {
			if (lo_Prop.PropName.equals(as_PropName)) {
				return lo_Prop;
			}
		}
		return null;
	}

	/**
	 * 通过角色名称取得角色对象
	 * 
	 * @param as_RoleName
	 *            角色名称
	 * @return
	 */
	public CRole getRoleByName(String as_RoleName) {
		for (CRole lo_Role : Roles) {
			if (lo_Role.RoleName.equals(as_RoleName)) {
				return lo_Role;
			}
		}
		return null;
	}

	/**
	 * 根据内容取得实际值
	 * 
	 * @param as_Content
	 * @return
	 * @throws Exception
	 */
	public String getValueByContent(String as_Content) throws Exception {
		try {
			if (as_Content.toString().substring(0, 2) != "[<")
				return as_Content;

			int i = as_Content.toString().indexOf(">]") + 1;
			if (i == 0)
				return as_Content;

			String ls_Title = as_Content.toString().substring(
					Integer.parseInt("3") - 1, i - 3);
			String ls_Value = as_Content.toString().substring(
					as_Content.toString().length()
							- (as_Content.toString().length() - i - 1),
					as_Content.toString().length() - i - 1);

			if (ls_Title != null) {
				if (ls_Title == "SCRIPT") {
					if (WorkItem == null) {
						return as_Content;
					} else {
						return this.WorkItem
								.caculateScriptContentEval(ls_Value).toString();
					}
				}
				return CSqlHandle.getValueBySql(ls_Value);
			}
		} catch (Exception e) {
			this.Raise(e, "getValueByContent", null);
		}
		return null;
	}

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return
	 */
	public String IsValid(int ai_SpaceLength) {
		try {
			String ls_Msg = "";
			// 自由流转规则
			if ((this.FlowFlag & EWorkItemFlowFlag.FreeFlowRule) == EWorkItemFlowFlag.FreeFlowRule) {
				if (this.Rights.size() < 3) {
					ls_Msg += MGlobal.addSpace(ai_SpaceLength)
							+ "公文为自由流转时，必须有其他权限" + "\n";
				}
			}

			for (CRight lo_Right : this.Rights) {
				ls_Msg += lo_Right.IsValid(ai_SpaceLength + 4);
			}

			for (CRole lo_Role : this.Roles) {
				ls_Msg += lo_Role.IsValid(ai_SpaceLength + 4);
			}

			for (CScript lo_Script : this.Scripts) {
				ls_Msg += lo_Script.IsValid(ai_SpaceLength + 4);
			}

			for (CTable lo_Table : this.Tables) {
				ls_Msg += lo_Table.IsValid(ai_SpaceLength + 4);
			}

			for (CForm lo_Form : this.Forms) {
				ls_Msg += lo_Form.IsValid(ai_SpaceLength + 4);
			}

			// 非自由流转规则
			if (((this.FlowFlag & EWorkItemFlowFlag.OrderFlowRule) == EWorkItemFlowFlag.OrderFlowRule)
					|| ((this.FlowFlag & EWorkItemFlowFlag.ConditionFlowRule) == EWorkItemFlowFlag.ConditionFlowRule)) {
				if (this.Flows.size() == 0) {
					ls_Msg += MGlobal.addSpace(ai_SpaceLength)
							+ "公文为非自由流转时，缺少流程" + "\n";
				} else {
					for (CFlow lo_Flow : this.Flows) {
						ls_Msg += lo_Flow.IsValid(ai_SpaceLength + 4);
					}
				}
			}

			for (CDocument lo_Document : this.Documents) {
				ls_Msg += lo_Document.IsValid(ai_SpaceLength + 4);
			}

			for (CProp lo_Prop : this.Props) {
				ls_Msg += lo_Prop.IsValid(ai_SpaceLength + 4);
			}

			if (this.Body != null) {
				ls_Msg += this.Body.IsValid(ai_SpaceLength + 4);
			}

			if (ls_Msg.equals("")) {
				return "公文模板设计完整。";
			} else {
				return "公文模板设计存在以下问题：" + "\n" + ls_Msg;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	/**
	 * 从Xml节点导入到对象
	 * 
	 * @param ao_Node
	 *            导入节点
	 * @param ai_Type
	 *            导入类型：=0-正常导入；=1-短属性导入；
	 * @throws Exception
	 */
	@Override
	protected void Import(Element ao_Node, int ai_Type) throws Exception {
		this.CyclostyleID = Integer.parseInt(ao_Node
				.getAttribute("CyclostyleID"));
		this.CyclostyleName = ao_Node.getAttribute("CyclostyleName");
		this.CyclostyleCode = ao_Node.getAttribute("CyclostyleCode");
		this.Author = ao_Node.getAttribute("Author");
		this.AuthorID = Integer.parseInt(ao_Node.getAttribute("AuthorID"));
		this.CreateDate = MGlobal.stringToDate(ao_Node
				.getAttribute("CreateDate"));
		this.EditorID = Integer.parseInt(ao_Node.getAttribute("EditorID"));
		this.Editor = ao_Node.getAttribute("Editor");
		this.EditDate = MGlobal.stringToDate(ao_Node.getAttribute("EditDate"));
		this.TypeID = Integer.parseInt(ao_Node.getAttribute("TypeID"));
		this.RoleMaxID = Integer.parseInt(ao_Node.getAttribute("RoleMaxID"));
		this.PropMaxID = Integer.parseInt(ao_Node.getAttribute("PropMaxID"));
		this.DocumentMaxID = Integer.parseInt(ao_Node
				.getAttribute("DocumentMaxID"));
		this.RightMaxID = Integer.parseInt(ao_Node.getAttribute("RightMaxID"));
		this.TableMaxID = Integer.parseInt(ao_Node.getAttribute("TableMaxID"));
		this.FormMaxID = Integer.parseInt(ao_Node.getAttribute("FormMaxID"));
		this.FormCellMaxID = Integer.parseInt(ao_Node
				.getAttribute("FormCellMaxID"));
		this.FormLineMaxID = Integer.parseInt(ao_Node
				.getAttribute("FormLineMaxID"));
		this.FormTableMaxID = Integer.parseInt(ao_Node
				.getAttribute("FormTableMaxID"));
		this.ScriptMaxID = Integer
				.parseInt(ao_Node.getAttribute("ScriptMaxID"));
		this.HaveBody = Boolean.parseBoolean(ao_Node.getAttribute("HaveBody"));
		this.HaveDocument = Boolean.parseBoolean(ao_Node
				.getAttribute("HaveDocument"));
		this.HaveForm = Boolean.parseBoolean(ao_Node.getAttribute("HaveForm"));
		this.FirstShowPage = Integer.parseInt(ao_Node
				.getAttribute("FirstShowPage"));
		this.FlowFlag = Integer.parseInt(ao_Node.getAttribute("FlowFlag"));
		this.Summary = ao_Node.getAttribute("Summary");
		this.BindImage = ao_Node.getAttribute("BindImage");
		this.IsPublicUsing = Integer.parseInt(ao_Node
				.getAttribute("IsPublicUsing"));
		setExtendFields(ao_Node.getAttribute("ExtendFields"));
		this.OrderID = Integer.parseInt(ao_Node.getAttribute("OrderID"));
		if (ao_Node.hasAttribute("FileSaveType"))
			this.FileSaveType = Integer.parseInt(ao_Node
					.getAttribute("FileSaveType"));
		if (ao_Node.hasAttribute("FileSaveID"))
			this.FileSaveID = Integer.parseInt(ao_Node
					.getAttribute("FileSaveID"));
		if (ao_Node.hasAttribute("FileSaveDate"))
			this.FileSaveDate = MGlobal.stringToDate(ao_Node
					.getAttribute("FileSaveDate"));
		if (ao_Node.hasAttribute("BelongID"))
			this.BelongID = Integer.parseInt(ao_Node.getAttribute("BelongID"));

		// 权限集合
		if (!ao_Node.getAttribute("theRightsCount").equals("0")) {
			NodeList lo_List = ao_Node.getElementsByTagName("Right");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CRight lo_Right = new CRight(this.Logon);
				lo_Right.Cyclostyle = this;
				lo_Right.Import(lo_Node, ai_Type);
				this.Rights.add(lo_Right);
			}
		}

		// 角色集合
		if (!ao_Node.getAttribute("theRolesCount").equals("0")) {
			NodeList lo_List = ao_Node.getElementsByTagName("Role");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CRole lo_Role = new CRole(this.Logon);
				lo_Role.Cyclostyle = this;
				lo_Role.Import(lo_Node, ai_Type);
				this.Roles.add(lo_Role);
			}
		}

		// 二次开发函数集合
		if (!ao_Node.getAttribute("theScriptsCount").equals("0")) {
			NodeList lo_List = ao_Node.getElementsByTagName("Script");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CScript lo_Script = new CScript(this.Logon);
				lo_Script.Cyclostyle = this;
				lo_Script.Import(lo_Node, ai_Type);
				this.Scripts.add(lo_Script);
			}
		}

		// 数据存储表格集合
		if (!ao_Node.getAttribute("theTablesCount").equals("0")) {
			NodeList lo_List = ao_Node.getElementsByTagName("Table");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CTable lo_Table = new CTable(this.Logon);
				lo_Table.Cyclostyle = this;
				lo_Table.Import(lo_Node, ai_Type);
				this.Tables.add(lo_Table);
			}
		}

		// 表单集合
		if (!ao_Node.getAttribute("theFormsCount").equals("0")) {
			NodeList lo_List = ao_Node.getElementsByTagName("Form");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CForm lo_Form = new CForm(this.Logon);
				lo_Form.Cyclostyle = this;
				lo_Form.Import(lo_Node, ai_Type);
				this.Forms.add(lo_Form);
			}
		}

		// 流程集合
		if (!ao_Node.getAttribute("theFlowsCount").equals("0")) {
			NodeList lo_List = ao_Node.getElementsByTagName("Flow");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CFlow lo_Flow = new CFlow(this.Logon);
				lo_Flow.Cyclostyle = this;
				lo_Flow.Import(lo_Node, ai_Type);
				this.FlowIndexMaxID++;
				lo_Flow.FlowIndex = this.FlowIndexMaxID;
				this.Flows.add(lo_Flow);
			}
		}

		// 附件集合
		if (!ao_Node.getAttribute("theDocumentsCount").equals("0")) {
			CDocument lo_Document;
			NodeList lo_List = ao_Node.getElementsByTagName("Document");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				int ll_Id = Integer
						.parseInt(lo_Node.getAttribute("DocumentID"));
				switch (ll_Id) {
				case -1:// 新建
					if (ll_Id == -2) {
						// 删除临时文件
						TWorkAdmin.deleleTempFileName(this.Logon,
								lo_Node.getAttribute("WriteFile"));
					} else {
						// 新附件
						lo_Document = TCyclostyle.insertDocument(this,
								lo_Node.getAttribute("DocumentName"), ll_Id);
						lo_Document.DocumentExt = lo_Node
								.getAttribute("DocumentExt");
						lo_Document.WriteFile = lo_Node
								.getAttribute("WriteFile");
						lo_Document.ChangeType = 4;
						if (lo_Node.hasAttribute("Page"))
							lo_Document.FilePages = Integer.parseInt(lo_Node
									.getAttribute("Page"));
					}
					break;
				default:
					lo_Document = new CDocument(this.Logon);
					lo_Document.Cyclostyle = this;
					lo_Document.initDocument();
					lo_Document.Import(lo_Node, ai_Type);
					this.Documents.add(lo_Document);
				}
			}
		}

		// 正文
		if (ao_Node.getAttribute("theBodyCount") == "1") {
			Element lo_Node = (Element) ao_Node.getElementsByTagName("Body")
					.item(0);
			this.Body = new CDocument(this.Logon);
			this.Body.Cyclostyle = this;
			this.Body.initDocument();
			this.Body.Import(lo_Node, ai_Type);
		}

		// 表头属性集合
		if (!ao_Node.getAttribute("thePropsCount").equals("0")) {
			NodeList lo_List = ao_Node.getElementsByTagName("Prop");
			for (int i = 0; i < lo_List.getLength(); i++) {
				Element lo_Node = (Element) lo_List.item(i);
				CProp lo_Prop = new CProp(this.Logon);
				lo_Prop.Cyclostyle = this;
				lo_Prop.Import(lo_Node, ai_Type);
				this.Props.add(lo_Prop);
			}
		}

		// 公文发布对象
		if (ao_Node.getAttribute("ExistPublicTemplate") == "1") {
			this.PublicTemplate = new CPublicTemplate(this.Logon);
			this.PublicTemplate.Cyclostyle = this;
			Element lo_Node = (Element) ao_Node.getElementsByTagName(
					"PublicTemplate").item(0);
			this.PublicTemplate.Import(lo_Node, ai_Type);
		}
	}

	/**
	 * 从对象导出到Xml节点
	 * 
	 * @param ao_Node
	 *            导出节点
	 * @param ai_Type
	 *            导出类型：=0-正常导出；=1-短属性导出；
	 * @throws Exception
	 * @throws DOMException
	 */
	@Override
	protected void Export(Element ao_Node, int ai_Type) throws DOMException,
			Exception {
		ao_Node.setAttribute("CyclostyleID", String.valueOf(this.CyclostyleID));
		ao_Node.setAttribute("CyclostyleName", this.CyclostyleName);
		ao_Node.setAttribute("CyclostyleCode", this.CyclostyleCode);
		ao_Node.setAttribute("Author", this.Author);
		ao_Node.setAttribute("AuthorID", String.valueOf(this.AuthorID));
		ao_Node.setAttribute("CreateDate",
				MGlobal.dateToString(this.CreateDate));
		ao_Node.setAttribute("EditorID", String.valueOf(this.EditorID));
		ao_Node.setAttribute("Editor", this.Editor);
		ao_Node.setAttribute("EditDate", MGlobal.dateToString(this.EditDate));
		ao_Node.setAttribute("TypeID", String.valueOf(this.TypeID));
		ao_Node.setAttribute("RoleMaxID", String.valueOf(this.RoleMaxID));
		ao_Node.setAttribute("PropMaxID", String.valueOf(this.PropMaxID));
		ao_Node.setAttribute("DocumentMaxID",
				String.valueOf(this.DocumentMaxID));
		ao_Node.setAttribute("RightMaxID", String.valueOf(this.RightMaxID));
		ao_Node.setAttribute("TableMaxID", String.valueOf(this.TableMaxID));
		ao_Node.setAttribute("FormMaxID", String.valueOf(this.FormMaxID));
		ao_Node.setAttribute("FormCellMaxID",
				String.valueOf(this.FormCellMaxID));
		ao_Node.setAttribute("FormLineMaxID",
				String.valueOf(this.FormLineMaxID));
		ao_Node.setAttribute("FormTableMaxID",
				String.valueOf(this.FormTableMaxID));
		ao_Node.setAttribute("ScriptMaxID", String.valueOf(this.ScriptMaxID));
		ao_Node.setAttribute("HaveBody", String.valueOf(this.HaveBody));
		ao_Node.setAttribute("HaveDocument", String.valueOf(this.HaveDocument));
		ao_Node.setAttribute("HaveForm", String.valueOf(this.HaveForm));
		ao_Node.setAttribute("FirstShowPage",
				String.valueOf(this.FirstShowPage));
		ao_Node.setAttribute("FlowFlag", String.valueOf(this.FlowFlag));
		ao_Node.setAttribute("Summary", this.Summary);
		ao_Node.setAttribute("BindImage", this.BindImage);
		ao_Node.setAttribute("IsPublicUsing",
				String.valueOf(this.IsPublicUsing));
		ao_Node.setAttribute("ExtendFields", this.getExtendFields());
		ao_Node.setAttribute("OrderID", String.valueOf(this.OrderID));
		ao_Node.setAttribute("FileSaveType", String.valueOf(this.FileSaveType));
		ao_Node.setAttribute("FileSaveID", String.valueOf(this.FileSaveID));
		ao_Node.setAttribute("FileSaveDate",
				MGlobal.dateToString(this.FileSaveDate));
		ao_Node.setAttribute("BelongID", String.valueOf(this.BelongID));

		// 权限集合
		ao_Node.setAttribute("theRightsCount",
				String.valueOf(this.Rights.size()));
		for (CRight lo_Right : this.Rights) {
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Right");
			lo_Right.Export(lo_Node, ai_Type);
		}

		// 角色集合
		ao_Node.setAttribute("theRolesCount", String.valueOf(this.Roles.size()));
		for (CRole lo_Role : this.Roles) {
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Role");
			lo_Role.Export(lo_Node, ai_Type);
		}

		// 二次开发函数集合
		ao_Node.setAttribute("theScriptsCount",
				String.valueOf(this.Scripts.size()));
		for (CScript lo_Script : this.Scripts) {
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Script");
			lo_Script.Export(lo_Node, ai_Type);
		}

		// 数据存储表格集合
		ao_Node.setAttribute("theTablesCount",
				String.valueOf(this.Tables.size()));
		for (CTable lo_Table : this.Tables) {
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Table");
			lo_Table.Export(lo_Node, ai_Type);
		}

		// 表单集合
		ao_Node.setAttribute("theFormsCount", String.valueOf(this.Forms.size()));
		for (CForm lo_Form : this.Forms) {
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Form");
			lo_Form.Export(lo_Node, ai_Type);
		}

		// 流程集合
		ao_Node.setAttribute("theFlowsCount", String.valueOf(this.Flows.size()));
		for (CFlow lo_Flow : this.Flows) {
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Flow");
			lo_Flow.Export(lo_Node, ai_Type);
		}

		// 附件集合
		ao_Node.setAttribute("theDocumentsCount",
				String.valueOf(this.Documents.size()));
		for (CDocument lo_Document : this.Documents) {
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Document");
			lo_Document.Export(lo_Node, ai_Type);
		}

		// 正文
		if (this.Body == null) {
			ao_Node.setAttribute("theBodyCount", "0");
		} else {
			ao_Node.setAttribute("theBodyCount", "1");
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Body");
			this.Body.Export(lo_Node, ai_Type);
		}

		// 表头属性集合
		ao_Node.setAttribute("thePropsCount", String.valueOf(this.Props.size()));
		for (CProp lo_Prop : this.Props) {
			Element lo_Node = MXmlHandle.addElement(ao_Node, "Prop");
			lo_Prop.Export(lo_Node, ai_Type);
		}

		// 公文发布对象
		if (this.PublicTemplate == null) {
			ao_Node.setAttribute("ExistPublicTemplate", "0");
		} else {
			ao_Node.setAttribute("ExistPublicTemplate", "1");
			Element lo_Node = MXmlHandle.addElement(ao_Node, "PublicTemplate");
			this.PublicTemplate.Export(lo_Node, ai_Type);
		}
	}

	/**
	 * 对象保存到数据库行对象中
	 * 
	 * @param ao_Set
	 *            保存的结果集
	 * @param ai_Type
	 *            保存方式：=0-正常保存；=1-短属性保存；
	 * @throws Exception
	 */
	@Override
	public void Save(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		Save(ao_Set, ai_Type, false);
	}

	/**
	 * 保存模板
	 * 
	 * @param ao_Set
	 *            模板结果集
	 * @param ai_Type
	 *            类型：=0-正常打；=1-短属性；
	 * @param ab_SaveInst
	 *            是否保存实例
	 * @throws Exception
	 */
	public void Save(Map<String, Object> ao_Set, int ai_Type,
			Boolean ab_SaveInst) throws Exception {
		ao_Set.put("TemplateID", this.CyclostyleID);
		ao_Set.put("TemplateName", this.CyclostyleName);
		if (!ab_SaveInst)
			ao_Set.put("TemplateCode", this.CyclostyleCode);

		ao_Set.put("CreateID", this.AuthorID);
		ao_Set.put("Creator", this.Author);
		ao_Set.put("CreateDate", MGlobal.dateToSqlDate(this.CreateDate));
		ao_Set.put("COMP_ID", this.BelongID);

		ao_Set.put("EditID", this.EditorID);
		if (this.EditorID == 0) {
			ao_Set.put("Editor", null);
			ao_Set.put("EditDate", null);
		} else {
			ao_Set.put("Editor", this.Editor);
			ao_Set.put("EditDate", MGlobal.dateToSqlDate(this.EditDate));
		}

		ao_Set.put("TypeID", this.TypeID);
		ao_Set.put("HaveBody", (this.HaveBody ? 1 : 0));
		ao_Set.put("HaveDocument", (this.HaveDocument ? 1 : 0));
		ao_Set.put("HaveForm", (this.HaveForm ? 1 : 0));
		ao_Set.put("FirstPage", (this.FirstShowPage == 0 ? 0 : 1));
		ao_Set.put("FlowFlag", this.FlowFlag
				+ (getSaveType() == EDataHandleType.OrignType ? 0 : 8));
		ao_Set.put("TemplateDescribe", MGlobal.getDbValue(this.Summary));
		ao_Set.put("BindImage", (this.BindImage.equals("") ? null
				: this.BindImage));
		if (ab_SaveInst) {
			ao_Set.put("FileSaveType", this.FileSaveType);
			ao_Set.put("FileSaveID", this.FileSaveID);
			ao_Set.put("FileSaveDate", MGlobal.dateToSqlDate(this.FileSaveDate));
		} else {
			ao_Set.put("IsPublicUsing", this.IsPublicUsing);
			ao_Set.put("OrderID", this.OrderID);
		}
		ao_Set.put("ExtendFields", getExtendFields());
		String ls_Msg = IsValid(0);
		ao_Set.put("TemplateIsValid", (ls_Msg.equals("") ? 1 : 0));
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型：=0-正常打；=1-短属性；
	 * @param ab_Inst
	 *            是否实例
	 * @return
	 * @throws SQLException
	 */
	public String getSaveState(int ai_SaveType, int ai_Type, boolean ab_Inst)
			throws SQLException {
		String ls_Sql = null;
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO TemplateInst "
						+ "(TemplateID, TemplateName, TypeID, CreateID, Creator, CreateDate, "
						+ "EditID, Editor, EditDate, HaveBody, HaveDocument, HaveForm, FirstPage, FlowFlag, "
						+ "TemplateDescribe, TemplateIsValid, BindImage, ExtendFields, COMP_ID, "
						+ "FileSaveType, FileSaveID, FileSaveDate, WorkItemID) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE TemplateInst SET "
						+ "TemplateID = ?, TemplateName = ?, TypeID = ?, CreateID = ?, Creator = ?, CreateDate = ?, "
						+ "EditID = ?, Editor = ?, EditDate = ?, HaveBody = ?, HaveDocument = ?, HaveForm = ?, FirstPage = ?, FlowFlag = ?, "
						+ "TemplateDescribe = ?, TemplateIsValid = ?, BindImage = ?, ExtendFields = ?, COMP_ID = ?, "
						+ "FileSaveType = ?, FileSaveID = ?, FileSaveDate = ? WHERE WorkItemID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO TemplateDefine "
						+ "(TemplateID, TemplateName, TypeID, CreateID, Creator, CreateDate, "
						+ "EditID, Editor, EditDate, HaveBody, HaveDocument, HaveForm, FirstPage, FlowFlag, "
						+ "TemplateDescribe, TemplateIsValid, BindImage, ExtendFields, COMP_ID, "
						+ "IsPublicUsing, OrderID, TemplateCode) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE TemplateDefine "
						+ "SET TemplateName = ?, TypeID = ?, CreateID = ?, Creator = ?, CreateDate = ?, "
						+ "EditID = ?, Editor = ?, EditDate = ?, HaveBody = ?, HaveDocument = ?, HaveForm = ?, FirstPage = ?, FlowFlag = ?, "
						+ "TemplateDescribe = ?, TemplateIsValid = ?, BindImage = ?, ExtendFields = ?, COMP_ID = ?, "
						+ "IsPublicUsing = ?, OrderID = ?, TemplateCode = ? WHERE TemplateID = ?";
			}
		}
		return ls_Sql;
	}

	/**
	 * 保存
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型：=0-正常打；=1-短属性；
	 * @param ab_Inst
	 *            是否实例
	 * @throws SQLException
	 */
	public void Save(int ai_SaveType, int ai_Type, boolean ab_Inst)
			throws SQLException {
		String lo_State = this.getSaveState(ai_SaveType, ai_Type, ab_Inst);
		this.Save(lo_State, ai_SaveType, ai_Type, ab_Inst, 0);
	}

	/**
	 * 保存
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型：=0-正常打；=1-短属性；
	 * @param ab_Inst
	 *            是否实例
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws SQLException
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type,
			boolean ab_Inst, int ai_Update) throws SQLException {
		List parasList = new ArrayList();
		if (ab_Inst || ai_SaveType == 0)
			parasList.add(this.CyclostyleID);
		parasList.add(this.CyclostyleName);
		parasList.add(this.TypeID);
		// li_Index = 4
		parasList.add(this.AuthorID);
		parasList.add(this.Author);
		parasList.add(this.CreateDate);
		// li_Index = 7
		parasList.add(this.EditorID);
		parasList.add(this.Editor);
		parasList.add(this.EditDate);
		// li_Index = 10
		parasList.add((this.HaveBody ? 1 : 0));
		parasList.add((this.HaveDocument ? 1 : 0));
		parasList.add((this.HaveForm ? 1 : 0));
		parasList.add((this.FirstShowPage == 0 ? 0 : 1));
		parasList.add(this.FlowFlag
				+ (getSaveType() == EDataHandleType.OrignType ? 0 : 8));
		parasList.add(this.Summary);
		if (ab_Inst) {
			parasList.add(1);
		} else {
			if (this.IsValid(0).equals("")) {
				parasList.add(1);
			} else {
				parasList.add(0);
			}
		}

		parasList.add(this.BindImage);
		parasList.add(getExtendFields());
		parasList.add(this.BelongID);
		if (ab_Inst) {
			parasList.add(this.FileSaveType);
			parasList.add(this.FileSaveID);
			parasList.add(this.FileSaveDate);
			parasList.add(this.WorkItem.WorkItemID);
		} else {
			parasList.add(this.IsPublicUsing);
			parasList.add(this.OrderID);
			parasList.add(this.CyclostyleCode);
			if (ai_SaveType == 1)
				parasList.add(this.CyclostyleID);
		}

		return CSqlHandle.getJdbcTemplate().update(ao_State,
				parasList.toArray());
	}

	/**
	 * 从数据库行对象中读取数据到对象
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开方式：=0-正常打开；=1-短属性打开；
	 * @throws Exception
	 */
	@Override
	public void Open(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		Open(ao_Set, ai_Type, false);
	}

	/**
	 * 打开模板
	 * 
	 * @param ao_Set
	 *            模板结果集
	 * @param ai_Type
	 *            类型：=0-正常打；=1-短属性；
	 * @param ab_OpenInst
	 *            是否打开实例
	 * @throws SQLException
	 */
	public void Open(Map<String, Object> ao_Set, int ai_Type,
			Boolean ab_OpenInst) throws Exception {
		this.CyclostyleID = MGlobal.readInt(ao_Set, "TemplateID");
		this.CyclostyleName = MGlobal.readString(ao_Set, "TemplateName");
		if (!ab_OpenInst)
			this.CyclostyleCode = MGlobal.readString(ao_Set, "TemplateCode");

		this.AuthorID = MGlobal.readInt(ao_Set, "CreateID");
		this.Author = MGlobal.readString(ao_Set, "Creator");
		this.CreateDate = MGlobal.readDate(ao_Set, "CreateDate");
		this.BelongID = MGlobal.readInt(ao_Set, "COMP_ID");

		this.EditorID = MGlobal.readInt(ao_Set, "EditID");
		this.Editor = MGlobal.readString(ao_Set, "Editor");
		this.EditDate = MGlobal.readDate(ao_Set, "EditDate");

		this.TypeID = MGlobal.readInt(ao_Set, "TypeID");

		this.HaveBody = (MGlobal.readInt(ao_Set, "HaveBody") == 1);
		this.HaveDocument = (MGlobal.readInt(ao_Set, "HaveDocument") == 1);
		this.HaveForm = (MGlobal.readInt(ao_Set, "HaveForm") == 1);
		this.FirstShowPage = (MGlobal.readInt(ao_Set, "FirstPage") == 1 ? 1 : 0);
		int li_Flag = MGlobal.readInt(ao_Set, "FlowFlag");
		if ((li_Flag & 8) == 8) {
			this.FlowFlag = li_Flag - 8;
			this.OpenType = EDataHandleType.XmlType;
		} else {
			this.FlowFlag = li_Flag;
			this.OpenType = EDataHandleType.OrignType;
		}

		this.Summary = MGlobal.readString(ao_Set, "TemplateDescribe");
		this.BindImage = MGlobal.readString(ao_Set, "BindImage");

		if (ab_OpenInst) {
			if (ao_Set.get("FileSaveType") == null) {
				this.FileSaveType = 0;
				this.FileSaveID = 0;
			} else {
				this.FileSaveType = MGlobal.readInt(ao_Set, "FileSaveType");
				this.FileSaveID = MGlobal.readInt(ao_Set, "FileSaveID");
				this.FileSaveDate = MGlobal.readDate(ao_Set, "FileSaveDate");
			}
		} else {
			this.IsPublicUsing = MGlobal.readInt(ao_Set, "IsPublicUsing");
			this.OrderID = MGlobal.readInt(ao_Set, "OrderID");
		}
		if (ao_Set.get("ExtendFields") != null)
			setExtendFields(MGlobal.readString(ao_Set, "ExtendFields"));
	}

	/**
	 * 对象打包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 * @throws Exception
	 */
	@Override
	protected void Write(MBag ao_Bag, int ai_Type) throws Exception {
		ao_Bag.Write("ml_CyclostyleID", -1); // this.CyclostyleID
		ao_Bag.Write("ms_CyclostyleCode", this.CyclostyleName);
		ao_Bag.Write("ms_CyclostyleCode", this.CyclostyleCode);

		ao_Bag.Write("ml_AuthorID", this.AuthorID);
		ao_Bag.Write("ms_Author", this.Author);
		ao_Bag.Write("md_CreateDate", this.CreateDate);

		ao_Bag.Write("ml_EditorID", this.EditorID);
		ao_Bag.Write("this.EditDate", this.Editor);
		ao_Bag.Write("md_EditDate", this.EditDate);

		ao_Bag.Write("this.RoleMaxID", this.TypeID);

		ao_Bag.Write("this.PropMaxID", this.RoleMaxID);
		ao_Bag.Write("this.DocumentMaxID", this.PropMaxID);
		ao_Bag.Write("ml_DocumentMaxID", this.DocumentMaxID);
		ao_Bag.Write("this.TableMaxID", this.RightMaxID);
		ao_Bag.Write("this.FormMaxID", this.TableMaxID);
		ao_Bag.Write("ml_FormMaxID", this.FormMaxID);
		ao_Bag.Write("ml_FormCellMaxID", this.FormCellMaxID);
		ao_Bag.Write("this.FormTableMaxID", this.FormLineMaxID);
		ao_Bag.Write("ml_FormTableMaxID", this.FormTableMaxID);
		ao_Bag.Write("ml_ScriptMaxID", this.ScriptMaxID);

		ao_Bag.Write("mb_HaveBody", this.HaveBody);
		ao_Bag.Write("mb_HaveDocument", this.HaveDocument);
		ao_Bag.Write("mb_HaveForm", this.HaveForm);
		ao_Bag.Write("mi_FirstShowPage", this.FirstShowPage);
		ao_Bag.Write("this.Summary", this.FlowFlag);
		ao_Bag.Write("ms_Summary", this.Summary);
		ao_Bag.Write("this.IsPublicUsing", this.BindImage);
		ao_Bag.Write("mi_IsPublicUsing", this.IsPublicUsing);
		// ao_Bag.Write( "ms_ExtendFields", mo_ExtendFieldsXML.xml);
		ao_Bag.Write("ms_ExtendFields", this.getExtendFields());

		ao_Bag.Write("this.FileSaveType", this.OrderID);
		ao_Bag.Write("this.FileSaveID", this.FileSaveType);
		ao_Bag.Write("ml_FileSaveID", this.FileSaveID);
		ao_Bag.Write("this.BelongID", this.FileSaveDate);

		ao_Bag.Write("ml_BelongID", this.BelongID);

		// 权限集合
		if (this.Rights == null) {
			ao_Bag.Write("theRightsCount", -1);
		} else {
			ao_Bag.Write("theRightsCount", this.Rights.size());
			for (CRight lo_Right : this.Rights) {
				lo_Right.Write(ao_Bag, ai_Type);
			}
		}

		// 角色集合
		if (this.Roles == null) {
			ao_Bag.Write("theRolesCount", -1);
		} else {
			ao_Bag.Write("theRolesCount", this.Roles.size());
			for (CRole lo_Role : this.Roles) {
				lo_Role.Write(ao_Bag, ai_Type);
			}
		}

		// 二次开发函数集合
		if (this.Scripts == null) {
			ao_Bag.Write("theScriptsCount", -1);
		} else {
			ao_Bag.Write("theScriptsCount", this.Scripts.size());
			for (CScript lo_Script : this.Scripts) {
				lo_Script.Write(ao_Bag, ai_Type);
			}
		}

		// 数据存储表格集合
		if (this.Tables == null) {
			ao_Bag.Write("theTablesCount", -1);
		} else {
			ao_Bag.Write("theTablesCount", this.Tables.size());
			for (CTable lo_Table : this.Tables) {
				lo_Table.Write(ao_Bag, ai_Type);
			}
		}

		// 表单集合
		if (this.Forms == null) {
			ao_Bag.Write("theFormsCount", -1);
		} else {
			ao_Bag.Write("theFormsCount", this.Forms.size());
			for (CForm lo_Form : this.Forms) {
				lo_Form.Write(ao_Bag, ai_Type);
			}
		}

		// 流程集合
		if (this.Flows == null) {
			ao_Bag.Write("theFlowsCount", -1);
		} else {
			ao_Bag.Write("theFlowsCount", this.Flows.size());
			for (CFlow lo_Flow : this.Flows) {
				lo_Flow.Write(ao_Bag, ai_Type);
			}
		}

		// 附件集合
		if (this.Documents == null) {
			ao_Bag.Write("theDocumentsCount", -1);
		} else {
			ao_Bag.Write("theDocumentsCount", this.Documents.size());
			for (CDocument lo_Document : this.Documents) {
				lo_Document.Write(ao_Bag, ai_Type);
			}
		}

		// 正文
		if (this.Body == null) {
			ao_Bag.Write("theBodyCount", -1);
		} else {
			ao_Bag.Write("theBodyCount", 1);
			this.Body.Write(ao_Bag, ai_Type);
		}

		// 表头属性集合
		if (this.Props == null) {
			ao_Bag.Write("thePropsCount", -1);
		} else {
			ao_Bag.Write("thePropsCount", this.Props.size());
			for (CProp lo_Prop : this.Props) {
				lo_Prop.Write(ao_Bag, ai_Type);
			}
		}

		// 公文发布对象
		if (this.PublicTemplate == null) {
			ao_Bag.Write("ExistPublicTemplate", 0);
		} else {
			ao_Bag.Write("ExistPublicTemplate", 1);
			this.PublicTemplate.Write(ao_Bag, ai_Type);
		}
	}

	/**
	 * 对象解包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导入；=1-短属性导入；
	 * @throws Exception
	 */
	@Override
	protected void Read(MBag ao_Bag, int ai_Type) throws Exception {
		// 基本信息
		this.CyclostyleID = ao_Bag.ReadInt("ml_CyclostyleID");
		this.CyclostyleName = ao_Bag.ReadString("ms_CyclostyleName");
		this.CyclostyleCode = ao_Bag.ReadString("this.AuthorID");
		this.AuthorID = ao_Bag.ReadInt("ml_AuthorID");
		this.Author = ao_Bag.ReadString("ms_Author");
		this.CreateDate = ao_Bag.ReadDate("md_CreateDate");

		this.EditorID = ao_Bag.ReadInt("ml_EditorID");
		this.Editor = ao_Bag.ReadString("ms_Editor");
		this.EditDate = ao_Bag.ReadDate("this.EditorID");

		this.TypeID = ao_Bag.ReadInt("ml_TypeID");

		this.RoleMaxID = ao_Bag.ReadInt("ml_RoleMaxID");
		this.PropMaxID = ao_Bag.ReadInt("ml_PropMaxID");
		this.DocumentMaxID = ao_Bag.ReadInt("ml_DocumentMaxID");
		this.RightMaxID = ao_Bag.ReadInt("ml_RightMaxID");
		this.TableMaxID = ao_Bag.ReadInt("ml_TableMaxID");
		this.FormMaxID = ao_Bag.ReadInt("ml_FormMaxID");
		this.FormCellMaxID = ao_Bag.ReadInt("ml_FormCellMaxID");
		this.FormLineMaxID = ao_Bag.ReadInt("ml_FormLineMaxID");
		this.FormTableMaxID = ao_Bag.ReadInt("ml_FormTableMaxID");
		this.ScriptMaxID = ao_Bag.ReadInt("ml_ScriptMaxID");

		this.HaveBody = ao_Bag.ReadBoolean("mb_HaveBody");
		this.HaveDocument = ao_Bag.ReadBoolean("mb_HaveDocument");
		this.HaveForm = ao_Bag.ReadBoolean("mb_HaveForm");
		this.FirstShowPage = ao_Bag.ReadInt("mi_FirstShowPage");
		this.FlowFlag = ao_Bag.ReadInt("mi_FlowFlag");
		this.Summary = ao_Bag.ReadString("ms_Summary");
		this.BindImage = ao_Bag.ReadString("ms_BindImage");
		this.IsPublicUsing = ao_Bag.ReadInt("mi_IsPublicUsing");
		setExtendFields(ao_Bag.ReadString("ms_ExtendFields"));
		this.OrderID = ao_Bag.ReadInt("ml_OrderID");
		this.FileSaveType = ao_Bag.ReadInt("mi_FileSaveType");
		this.FileSaveID = ao_Bag.ReadInt("ml_FileSaveID");
		this.FileSaveDate = ao_Bag.ReadDate("md_FileSaveDate");

		this.BelongID = ao_Bag.ReadInt("ml_BelongID");

		// 子对象

		// 权限集合
		int li_Count = ao_Bag.ReadInt("theRightsCount");
		for (int i = 0; i < li_Count; i++) {
			CRight lo_Right = new CRight(this.Logon);
			lo_Right.Cyclostyle = this;
			lo_Right.Read(ao_Bag, ai_Type);
			this.Rights.add(lo_Right);
		}

		// 角色集合
		li_Count = ao_Bag.ReadInt("theRolesCount");
		for (int i = 0; i < li_Count; i++) {
			CRole lo_Role = new CRole(this.Logon);
			lo_Role.Cyclostyle = this;
			lo_Role.Read(ao_Bag, ai_Type);
			this.Roles.add(lo_Role);
		}

		// 二次开发函数集合
		li_Count = ao_Bag.ReadInt("theScriptsCount");
		for (int i = 0; i < li_Count; i++) {
			CScript lo_Script = new CScript(this.Logon);
			lo_Script.Cyclostyle = this;
			lo_Script.Read(ao_Bag, ai_Type);
			this.Scripts.add(lo_Script);
		}

		// 数据存储表格集合
		li_Count = ao_Bag.ReadInt("theTablesCount");
		for (int i = 0; i < li_Count; i++) {
			CTable lo_Table = new CTable(this.Logon);
			lo_Table.Cyclostyle = this;
			lo_Table.Read(ao_Bag, ai_Type);
			this.Tables.add(lo_Table);
		}

		// 表单集合
		li_Count = ao_Bag.ReadInt("theFormsCount");
		for (int i = 0; i < li_Count; i++) {
			CForm lo_Form = new CForm(this.Logon);
			lo_Form.Cyclostyle = this;
			lo_Form.Read(ao_Bag, ai_Type);
			this.Forms.add(lo_Form);
		}

		// 流程集合
		li_Count = ao_Bag.ReadInt("theFlowsCount");
		for (int i = 0; i < li_Count; i++) {
			CFlow lo_Flow = new CFlow(this.Logon);
			lo_Flow.Cyclostyle = this;
			lo_Flow.Read(ao_Bag, ai_Type);
			this.FlowIndexMaxID++;
			lo_Flow.FlowIndex = this.FlowIndexMaxID;
			this.Flows.add(lo_Flow);
		}

		// 附件集合
		li_Count = ao_Bag.ReadInt("theDocumentsCount");
		for (int i = 0; i < li_Count; i++) {
			CDocument lo_Doc = new CDocument(this.Logon);
			lo_Doc.Cyclostyle = this;
			lo_Doc.Read(ao_Bag, ai_Type);
			this.Documents.add(lo_Doc);
		}

		// 正文
		li_Count = ao_Bag.ReadInt("theBodyCount");
		if (li_Count == 1) {
			this.Body = new CDocument(this.Logon);
			this.Body.Cyclostyle = this;
			this.Body.initDocument();
			this.Body.Read(ao_Bag, ai_Type);
		}

		// 表头属性集合
		li_Count = ao_Bag.ReadInt("thePropsCount");
		for (int i = 0; i < li_Count; i++) {
			CProp lo_Prop = new CProp(this.Logon);
			lo_Prop.Cyclostyle = this;
			lo_Prop.Read(ao_Bag, ai_Type);
			this.Props.add(lo_Prop);
		}

		// 公文发布对象
		li_Count = ao_Bag.ReadInt("ExistPublicTemplate");
		if (li_Count == 1) {
			this.PublicTemplate = new CPublicTemplate(this.Logon);
			this.PublicTemplate.Cyclostyle = this;
			this.PublicTemplate.Read(ao_Bag, ai_Type);
		}
	}

}
