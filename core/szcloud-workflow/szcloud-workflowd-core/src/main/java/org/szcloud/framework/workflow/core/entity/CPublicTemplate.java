package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EPublicBodyType;
import org.szcloud.framework.workflow.core.emun.EPublicDocumentType;
import org.szcloud.framework.workflow.core.emun.EPublicFormType;
import org.szcloud.framework.workflow.core.emun.EPublicRightType;
import org.szcloud.framework.workflow.core.emun.EPublicTemplateType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 公文发布对象：公文处理对象中公文内容按照一定格式在公文流转中的某些步骤发布公文的定义。
 * 
 * @author Jackie.Wang
 * 
 */
public class CPublicTemplate extends CBase {

	/**
	 * 初始化
	 */
	public CPublicTemplate() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CPublicTemplate(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属的环境对象
		this.Cyclostyle = null;
		mo_XmlPublicContent = null;

		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 发布类型
	 */
	public int PublicType = EPublicTemplateType.DefaultPublicType;

	/**
	 * 发布权限
	 */
	public int RightType = EPublicRightType.SharePublicRightType;

	/**
	 * 发布的用户
	 */
	public String PublicUsers = "";

	/**
	 * 读取发布的名称
	 * 
	 * @throws Exception
	 */
	public String getPublicUserNames() throws Exception {
		String ls_Value = "";
		if ((";" + this.PublicUsers).indexOf(";0;") == -1) {
			return this.Cyclostyle.convertUserValueToDisplay(this.PublicUsers);
		} else {
			ls_Value = ";" + this.PublicUsers.replace(";0;", ";");
			ls_Value = ls_Value.substring(
					ls_Value.length() - (ls_Value.length() - 1),
					ls_Value.length() - 1);
			return this.Cyclostyle.convertUserValueToDisplay(ls_Value);
		}
	}

	/**
	 * 正文发布类型
	 */
	public int BodyType = EPublicBodyType.OrignStyleBody;

	/**
	 * 表单发布类型
	 */
	public int FormType = EPublicFormType.FileStyleForm;

	/**
	 * 附件发布类型
	 */
	public int DocumentType = EPublicDocumentType.ExistDocumentType;

	/**
	 * 发布内容定义 当PublicType=0时，由对应内容组成的相关内容的XML字符串，格式如下: <Fields Name='公文成文内容'
	 * Code='BumfSucceed' PrimaryKey='SucceedID'> <Field ID='#' Name='标题'
	 * Code='代码' Property='属性' Display='0/1' Color='*' Bold='0/1' Italic='0/1'
	 * Underline='0/1' InitValue='*' /> ...... </Fields>
	 * 当PublicType=1时，由对应数据库表格组成的相关内容的XML字符串，格式如下:
	 * <TABLE Name='*' Code='*' ConnectionString='*' PrimaryKey='*;*;...'>
	 * <Field ID='#' Name='标题' Code='代码' Property='属性' Display='0/1' Color='*'
	 * Bold='0/1' Italic='0/1' Underline='0/1' InitValue='*' />......
	 * </TABLE>
	 * 当PublicType=2时，该字段存储发布公文的VBScript内容，在客户端执行该代码;
	 * <ClientPublicCode>...</ClientPublicCode>
	 * 当PublicType=4时，该字段存储发布公文的VBScript内容，在服务器端执行该代码;
	 * <ServerPublicCode>...</ServerPublicCode>
	 */
	private String ms_PublicContent = "";

	/**
	 * 发布内容定义
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPublicContent() throws Exception {
		return MXmlHandle.getXml(getXmlPublicContent());
	}

	/**
	 * 发布内容定义
	 * 
	 * @param value
	 */
	public void setPublicContent(String value) {
		Document lo_Xml = MXmlHandle.LoadXml(value);
		if (lo_Xml != null) {
			mo_XmlPublicContent = null;
			mo_XmlPublicContent = lo_Xml;
		}
	}

	/**
	 * 发布内容定义Xml对象
	 */
	private Document mo_XmlPublicContent = null;

	/**
	 * 发布内容定义Xml对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public Document getXmlPublicContent() throws Exception {
		if (mo_XmlPublicContent == null) {
			if (ms_PublicContent == null || ms_PublicContent.equals("")) {
				ms_PublicContent = "<Root>" + getDefaultContent()
						+ "<ClientPublicCode/><ServerPublicCode/></Root>";
				mo_XmlPublicContent = MXmlHandle.LoadXml(ms_PublicContent);
			} else {
				mo_XmlPublicContent = MXmlHandle.LoadXml(ms_PublicContent);
			}
		}
		return mo_XmlPublicContent;
	}

	/**
	 * 是否生成自动生成流水号：=0 否；=1 是；
	 */
	public boolean AutoBuildSeq = true;

	/**
	 * 流水号代码前缀 当自动生成流水号时流水号前缀
	 */
	public String SequenceCode = "";

	/**
	 * 是否可重复发送
	 */
	public boolean RepeatPublic = false;

	/**
	 * 描述
	 */
	public String Describe = "";

	/**
	 * 取得缺省字段定义内容(XML)
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getDefaultContent() throws Exception {
		try {
			String[] ls_Names = null;
			String[] ls_Codes = null;
			int i = 0;
			String ls_Xml = "";
			ls_Names = "标题;流水号分类;文号;密级;缓存;主题;正文名称;附件名称;成文单位;主题;抄送;抄报;打印份数;内发"
					.split(";");
			ls_Codes = "FileTitle;SeqValue;TypeName;FileNo;SecretID;Emergency;FileSubject;BodyName;DocumentName;SucceedDept;MainSend;MakeACopyFor;CopyReport;PrintNumber;InnerSend"
					.split(";");
			ls_Xml = "<Fields Name=\'公文发布表\' Code=\'BumfSucceed\' PrimaryKey=\'SucceedID\'>";
			for (i = 0; i <= (ls_Names.length - 1); i++) {
				ls_Xml += "<Field ID=\'" + Integer.toString(i) + "\'";
				ls_Xml += " Name=\'" + ls_Names[i] + "\'";
				ls_Xml += " Code=\'" + ls_Codes[i] + "\'";
				ls_Xml += " Property=\'\' InitValue=\'\' Script=\'\'";
				ls_Xml = ls_Xml
						+ " Dislay=\'1\' Color=\'0\' Bold=\'0\' Italic=\'0\' Underline=\'0\' Format=\'\' />";
			}
			ls_Xml += "</Fields>";
			return ls_Xml;
		} catch (Exception e) {
			this.Raise(e, "GetDefaultContent", null);
		}
		return "";
	}

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			// Add Get Valid Code...
			return "";
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
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
	 */
	@Override
	protected void Import(Element ao_Node, int ai_Type) {
		this.PublicType = Integer.parseInt(ao_Node.getAttribute("PublicType"));
		this.RightType = Integer.parseInt(ao_Node.getAttribute("RightType"));
		this.PublicUsers = ao_Node.getAttribute("PublicUsers");
		this.BodyType = Integer.parseInt(ao_Node.getAttribute("BodyType"));
		this.FormType = Integer.parseInt(ao_Node.getAttribute("FormType"));
		this.DocumentType = Integer.parseInt(ao_Node
				.getAttribute("DocumentType"));
		ms_PublicContent = ao_Node.getAttribute("PublicContent");
		this.AutoBuildSeq = Boolean.parseBoolean(ao_Node
				.getAttribute("AutoBuildSeq"));
		this.SequenceCode = ao_Node.getAttribute("SequenceCode");
		this.Describe = ao_Node.getAttribute("Describe");
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
		ao_Node.setAttribute("PublicType", String.valueOf(this.PublicType));
		ao_Node.setAttribute("RightType", String.valueOf(this.RightType));
		ao_Node.setAttribute("PublicUsers", this.PublicUsers);
		ao_Node.setAttribute("BodyType", String.valueOf(this.BodyType));
		ao_Node.setAttribute("FormType", String.valueOf(this.FormType));
		ao_Node.setAttribute("DocumentType", String.valueOf(this.DocumentType));
		ao_Node.setAttribute("PublicContent", ms_PublicContent);
		ao_Node.setAttribute("AutoBuildSeq", String.valueOf(this.AutoBuildSeq));
		ao_Node.setAttribute("SequenceCode", this.SequenceCode);
		ao_Node.setAttribute("RepeatPublic", String.valueOf(this.RepeatPublic));
		ao_Node.setAttribute("Describe", this.Describe);
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
	public void Save(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		ao_Set.put("TemplateID", this.Cyclostyle.CyclostyleID);
		ao_Set.put("PublicType", this.PublicType);
		ao_Set.put("RightType", this.RightType);
		ao_Set.put("PublicUsers", MGlobal.getDbValue(this.PublicUsers));
		ao_Set.put("BodyType", this.BodyType);
		ao_Set.put("FormType", this.FormType);
		ao_Set.put("DocumentType", this.DocumentType);
		ao_Set.put("PublicContent", ms_PublicContent.equals("") ? null
				: ms_PublicContent);
		ao_Set.put("AutoBuildSeq", this.AutoBuildSeq ? 0 : 1);
		ao_Set.put("SequenceCode",
				MGlobal.getDbValue(this.SequenceCode));
		ao_Set.put("RepeatPublic", this.RepeatPublic ? 1 : 0);
		ao_Set.put("Describe", MGlobal.getDbValue(this.Describe));
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @param ab_Inst
	 *            是否实例
	 * @return
	 * @throws SQLException
	 */
	public static String getSaveState(int ai_SaveType, int ai_Type,
			boolean ab_Inst) throws SQLException {
		String ls_Sql = null;
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO PublicTemplateInst "
						+ "(WorkItemID, PublicType, RightType, PublicUsers, BodyType, "
						+ "DocumentType, FormType, PublicContent, AutoBuildSeq, "
						+ "SequenceCode, RepeatPublic, PublicNumber, Describe) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE PublicTemplateInst SET "
						+ "PublicType = ?, RightType = ?, PublicUsers = ?, BodyType = ?, "
						+ "DocumentType = ?, FormType = ?, PublicContent = ?, AutoBuildSeq = ?, "
						+ "SequenceCode = ?, RepeatPublic = ?, PublicNumber = ?, Describe = ? "
						+ "WHERE WorkItemID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO PublicTemplate "
						+ "(TemplateID, PublicType, RightType, PublicUsers, BodyType, "
						+ "DocumentType, FormType, PublicContent, AutoBuildSeq, "
						+ "SequenceCode, RepeatPublic, Describe) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE PublicTemplate SET "
						+ "PublicType = ?, RightType = ?, PublicUsers = ?, BodyType = ?, "
						+ "DocumentType = ?, FormType = ?, PublicContent = ?, AutoBuildSeq = ?, "
						+ "SequenceCode = ?, RepeatPublic = ?, Describe = ? "
						+ "WHERE TemplateID = ?";
			}
		}
		return ls_Sql;
	}

	/**
	 * 保存
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @param ab_Inst
	 *            是否实例
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws SQLException
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type,
			boolean ab_Inst, int ai_Update) throws SQLException {
		List parasList = new ArrayList();
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.CyclostyleID);
			}
		}

		parasList.add(this.PublicType);
		parasList.add(this.RightType);
		parasList.add(MGlobal.getDbValue(this.PublicUsers));
		parasList.add(this.BodyType);
		parasList.add(this.FormType);
		parasList.add(this.DocumentType);
		parasList.add((ms_PublicContent.equals("") ? null : ms_PublicContent));
		parasList.add((this.AutoBuildSeq ? 0 : 1));
		parasList.add(MGlobal.getDbValue(this.SequenceCode));
		parasList.add((this.RepeatPublic ? 1 : 0));
		parasList.add(MGlobal.getDbValue(this.Describe));

		if (ab_Inst) {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
			}
		} else {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.CyclostyleID);
			}
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
		// Add Open Code...
		// Cyclostyle.CyclostyleID = ao_Set.getInt("TemplateID");
		this.PublicType = (Integer) ao_Set.get("PublicType");
		this.RightType = (Integer) ao_Set.get("RightType");
		if (ao_Set.get("PublicUsers") != null) {
			this.PublicUsers = (String) ao_Set.get("PublicUsers");
		}
		this.BodyType = (Integer) ao_Set.get("BodyType");
		this.FormType = (Integer) ao_Set.get("FormType");
		this.DocumentType = (Integer) ao_Set.get("DocumentType");
		if (ao_Set.get("PublicContent") != null) {
			ms_PublicContent = (String) ao_Set.get("PublicContent");
		}
		this.AutoBuildSeq = ((Integer)ao_Set.get("AutoBuildSeq") == 0);
		if (ao_Set.get("SequenceCode") != null) {
			this.SequenceCode = (String) ao_Set.get("SequenceCode");
		}
		this.RepeatPublic = ((Integer)ao_Set.get("RepeatPublic") == 1);
		if (ao_Set.get("Describe") != null) {
			this.Describe = (String) ao_Set.get("Describe");
		}
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
		// ao_Bag.Write("ml_TemplateID", this.Cyclostyle.CyclostyleID);
		ao_Bag.Write("mi_PublicType", this.PublicType);
		ao_Bag.Write("mi_RightType", this.RightType);
		ao_Bag.Write("ms_PublicUsers", this.PublicUsers);
		ao_Bag.Write("mi_BodyType", this.BodyType);
		ao_Bag.Write("mi_FormType", this.FormType);
		ao_Bag.Write("mi_DocumentType", this.DocumentType);
		ao_Bag.Write("ms_PublicContent", ms_PublicContent);
		ao_Bag.Write("mb_AutoBuildSeq", this.AutoBuildSeq);
		ao_Bag.Write("ms_SequenceCode", this.SequenceCode);
		ao_Bag.Write("mb_RepeatPublic", this.RepeatPublic);
		ao_Bag.Write("ms_Describe", this.Describe);
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
		// this.Cyclostyle.CyclostyleID = ao_Bag.ReadInt("ml_TemplateID")
		this.PublicType = ao_Bag.ReadInt("mi_PublicType");
		this.RightType = ao_Bag.ReadInt("mi_RightType");
		this.PublicUsers = ao_Bag.ReadString("ms_PublicUsers");
		this.BodyType = ao_Bag.ReadInt("mi_BodyType");
		this.FormType = ao_Bag.ReadInt("mi_FormType");
		this.DocumentType = ao_Bag.ReadInt("mi_DocumentType");
		ms_PublicContent = ao_Bag.ReadString("ms_PublicContent");
		this.AutoBuildSeq = ao_Bag.ReadBoolean("mb_AutoBuildSeq");
		this.SequenceCode = ao_Bag.ReadString("ms_SequenceCode");
		this.RepeatPublic = ao_Bag.ReadBoolean("mb_RepeatPublic");
		this.Describe = ao_Bag.ReadString("ms_Describe");
	}

}
