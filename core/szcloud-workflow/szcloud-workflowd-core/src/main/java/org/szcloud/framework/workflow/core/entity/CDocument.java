package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EDocumentRightType;
import org.szcloud.framework.workflow.core.emun.EDocumentType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MFile;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 正文附件对象：公文流转处理对象模型中存储和操作公文正文和附加文 件的对象，是公文流转数据的一个主要组成部分。它可以分成版本控制
 * 和非版本控制形式，版本控制的形式有两种，一是以文件的拷贝形式存 在，即一个文件更改过以后生成的一个新版本，另一种是集成使用Word
 * 的版本控制功能，所有版本内容存储与一个Word文件中。
 * 
 * @author Jackie.Wang
 * 
 */
public class CDocument extends CBase {

	/**
	 * 初始化
	 * 
	 */
	public CDocument() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public CDocument(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	/**
	 * 临时文件转换和存储对象
	 */
	public CFileData FileData = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 附件标识
	 */
	public int DocumentID = 0;

	/**
	 * 附件名称
	 */
	public String DocumentName = "";

	/**
	 * 附件类型
	 */
	public int DocumentType = EDocumentType.NotAnyDocumentType;

	/**
	 * 附件类型
	 * 
	 * @param value
	 */
	public void setDocumentType(int value) {
		if (this.DocumentType == value)
			return;
		if (this.DocumentType == EDocumentType.NotAnyDocumentType) {
			this.DocumentType = value;
		} else {
			if (this.DocumentType <= EDocumentType.WordVersionBody) {
				if (value > EDocumentType.WordVersionBody) {
					this.DocumentType = EDocumentType.CommondBody;
				} else {
					this.DocumentType = value;
				}
			} else {
				if (value > EDocumentType.WordVersionBody) {
					this.DocumentType = value;
				} else {
					this.DocumentType = EDocumentType.CommondDocument;
				}
			}
		}

		if (this.DocumentType == EDocumentType.NotAnyDocumentType
				|| this.DocumentType == EDocumentType.DeletedFile
				|| this.DocumentType == EDocumentType.URLDocument
				|| this.DocumentType == EDocumentType.InstanceDocument) {
			if (this.FileData != null) {
				this.FileData.ClearUp();
				this.FileData = null;
			}
		} else {
			if (this.FileData == null) {
				this.FileData = new CFileData();
				this.FileData.Document = this;
			} else {
				this.FileData.setContent("");
			}
		}

		switch (this.DocumentType) {
		case EDocumentType.NotAnyDocumentType: // #非任何类型
			this.DocumentExt = "";
			break;
		case EDocumentType.DeletedFile: // #已被删除的正文或附件
			this.VersionID = 0;
			break;
		case EDocumentType.RTFTypeBody: // #RTF格式的普通正文
			this.DocumentExt = "htm";
			break;
		case EDocumentType.TextTypeBody: // #文本格式的普通正文
			this.DocumentExt = "txt";
			break;
		case EDocumentType.CommondBody: // #普通文件正文
			break;
		case EDocumentType.FileVersionBody: // #文件版本控制正文
			break;
		case EDocumentType.WordVersionBody: // #Word版本控制正文
			this.DocumentExt = "doc";
			break;
		case EDocumentType.CommondDocument: // #普通附件
			break;
		case EDocumentType.FileVersionDocument: // #文件版本控制附件
			break;
		case EDocumentType.WordVersionDocument: // #Word版本控制附件
			this.DocumentExt = "doc";
			break;
		case EDocumentType.URLDocument: // #URL链接附件
			this.DocumentExt = "url";
			break;
		case EDocumentType.InstanceDocument: // #公文链接附件
			this.DocumentExt = "wrk";
			break;
		case EDocumentType.ScanTifDocument: // #扫描成Tif(Tiff)的文件附件
			this.DocumentExt = "tif";
			break;
		case EDocumentType.ScanJpgDocument: // #扫描成jpg的文件附件[使用版本(DocumentVersion)来存储页数]
			this.DocumentExt = "jpg";
			break;
		}
	}

	/**
	 * 附件权限类型, 动态权限分配
	 */
	public int RightType = EDocumentRightType.DeleteDocument;

	/**
	 * 附件文件类型（主要为获取文件扩展名称）
	 */
	public String DocumentExt = "";

	/**
	 * 读取附件内容
	 * 
	 * @return
	 */
	public String getContent() {
		if (this.FileData == null)
			return null;
		return this.FileData.getContent();
	}

	/**
	 * 设置附件内容
	 * 
	 * @param value
	 */
	public void setContent(String value) {
		if (this.FileData == null)
			return;
		this.FileData.setContent(value);
	}

	/**
	 * 读取附件二进制内容
	 * 
	 * @return
	 */
	public byte[] getBytes() {
		if (this.FileData == null)
			return null;
		return this.FileData.getBytes();
	}

	/**
	 * 设置附件二进制内容
	 * 
	 * @param value
	 */
	public void setBytes(byte[] value) {
		if (this.FileData == null)
			return;
		this.FileData.setBytes(value);
	}

	/**
	 * 附件创建人员标识
	 */
	public int CreatorID = 0;
	/**
	 * 附件创建人员
	 */
	public String Creator = null;

	/**
	 * 附件创建时间
	 */
	public Date CreateDate = MGlobal.getNow();

	/**
	 * 附件的处理人员标识
	 */
	public int EditorID = 0;

	/**
	 * 附件的处理人员
	 */
	public String Editor = "";

	/**
	 * 附件的处理时间
	 */
	public Date EditDate = MGlobal.getInitDate();

	/**
	 * 附件原始标识（如果附件是版本控制，需要记住它的来源版本标识）
	 */
	public int OrignID = 0;

	/**
	 * 附件版本号
	 */
	public int VersionID = 0;

	/**
	 * 从服务器端下载到客户端的临时文件名称
	 */
	public String ReadFile = "";

	/**
	 * 从客户端上传到服务器端的临时文件名称
	 */
	public String WriteFile = "";

	/**
	 * 文件改变情况[=0-没有变化；=1-改变基本内容；=2-改变文件内容；=4-新附件；]
	 */
	public int ChangeType = 0;

	/**
	 * 原文件扩展名称，使用来在更新文件内容时删除原有的文件[内存数据]
	 */
	public String OldFileExt = "";

	/**
	 * 附件模板使用列表的XML字符串
	 */
	public String DocumentFileListXml = "";

	/**
	 * 附件模板使用列表的XML对象
	 * 
	 * @return
	 */
	public Document getDocumentFileList() {
		return MXmlHandle.LoadXml(this.DocumentFileListXml);
	}

	/**
	 * 文件修改时对应状态标识：状态标识是用来标记版本控制的附件的修改状态
	 */
	public int EntryID = 0;

	/**
	 * 文件页数
	 */
	public int FilePages = 0;

	/**
	 * 文件路径：实际文件物理路径，该路径直接记录附件的相对路径名称，主要针对外部导入的公文，<br>
	 * 不需要修改对应文件路径而处理，如Exflow导入进来的公文
	 */
	public String FilePath = "";

	// #==========================================================================#
	// 常用过程或函数定义
	// #==========================================================================#

	/**
	 * 初始化一些不可在构造函数执行的参数
	 */
	public void initDocument() {
		this.CreatorID = this.Logon.GlobalPara.UserID;
		this.Creator = this.Logon.GlobalPara.UserName;
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属的公文模板对象
		this.Cyclostyle = null;

		// 临时文件转换和存储对象
		if (this.FileData != null) {
			this.FileData.ClearUp();
			this.FileData = null;
		}

		super.ClearUp();
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
		this.DocumentID = Integer.parseInt(ao_Node.getAttribute("DocumentID"));
		this.DocumentName = ao_Node.getAttribute("DocumentName");
		this.DocumentType = Integer.parseInt(ao_Node
				.getAttribute("DocumentType"));
		this.DocumentExt = ao_Node.getAttribute("DocumentExt");
		this.CreatorID = Integer.parseInt(ao_Node.getAttribute("CreatorID"));
		this.Creator = ao_Node.getAttribute("Creator");
		this.CreateDate = MGlobal.stringToDate(ao_Node
				.getAttribute("CreateDate"));
		this.EditorID = Integer.parseInt(ao_Node.getAttribute("EditorID"));
		this.Editor = ao_Node.getAttribute("Editor");
		this.EditDate = MGlobal.stringToDate(ao_Node.getAttribute("EditDate"));
		this.OrignID = Integer.parseInt(ao_Node.getAttribute("OrignID"));
		this.VersionID = Integer.parseInt(ao_Node.getAttribute("VersionID"));
		this.ReadFile = ao_Node.getAttribute("ReadFile");
		this.WriteFile = ao_Node.getAttribute("WriteFile");
		this.ChangeType = Integer.parseInt(ao_Node.getAttribute("ChangeType"));
		this.OldFileExt = ao_Node.getAttribute("OldFileExt");
		if (ao_Node.getAttribute("EntryID") != null) {
			this.EntryID = Integer.parseInt(ao_Node.getAttribute("EntryID"));
		}
		if (ao_Node.getAttribute("Page") != null) {
			this.FilePages = Integer.parseInt(ao_Node.getAttribute("Page"));
		}
		if (ao_Node.getAttribute("FilePath") != null) {
			this.FilePath = (ao_Node.getAttribute("FilePath"));
		}
		if (ao_Node.getAttribute("DocumentFileListXML") != null) {
			this.DocumentFileListXml = (ao_Node
					.getAttribute("DocumentFileListXML"));
		}

		if (this.DocumentType == EDocumentType.TextTypeBody
				|| this.DocumentType == EDocumentType.RTFTypeBody) {
			FileData.setContent(ao_Node.getAttributeNode("Content")
					.getTextContent());
		}
		if (this.WriteFile != "") {
			this.DocumentExt = MFile.getFileExt(this.WriteFile); // 保持文件名称与扩展名称的一致性
		}
		if (this.Cyclostyle.DocumentMaxID < this.DocumentID) {
			this.Cyclostyle.DocumentMaxID = this.DocumentID;
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
		this.OldFileExt = this.DocumentExt;
		ao_Node.setAttribute("DocumentID", String.valueOf(this.DocumentID));
		ao_Node.setAttribute("DocumentName", this.DocumentName);
		ao_Node.setAttribute("DocumentType", String.valueOf(this.DocumentType));
		ao_Node.setAttribute("DocumentExt", this.DocumentExt);
		ao_Node.setAttribute("CreatorID", String.valueOf(this.CreatorID));
		ao_Node.setAttribute("Creator", this.Creator);
		ao_Node.setAttribute("CreateDate",
				MGlobal.dateToString(this.CreateDate));
		ao_Node.setAttribute("EditorID", String.valueOf(this.EditorID));
		ao_Node.setAttribute("Editor", this.Editor);
		ao_Node.setAttribute("EditDate", MGlobal.dateToString(this.EditDate));

		ao_Node.setAttribute("OrignID", String.valueOf(this.OrignID));
		ao_Node.setAttribute("VersionID", String.valueOf(this.VersionID));
		ao_Node.setAttribute("ReadFile", this.ReadFile);
		ao_Node.setAttribute("WriteFile", this.WriteFile);
		ao_Node.setAttribute("ChangeType", String.valueOf(this.ChangeType));
		ao_Node.setAttribute("OldFileExt", this.OldFileExt);
		ao_Node.setAttribute("EntryID", String.valueOf(this.EntryID));
		ao_Node.setAttribute("Page", String.valueOf(this.FilePages));

		ao_Node.setAttribute("FilePath", this.FilePath);
		ao_Node.setAttribute("DocumentFileListXML", this.DocumentFileListXml);

		if (this.DocumentType == EDocumentType.TextTypeBody
				| this.DocumentType == EDocumentType.RTFTypeBody) {
			ao_Node.setTextContent(this.FileData.getContent());
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
		this.Save(ao_Set, ai_Type, true);
	}

	/**
	 * 保存
	 * 
	 * @param ao_Set
	 *            数据集
	 * @param ai_Type
	 *            類型
	 * @param ab_Template
	 *            是否是模板
	 * @throws Exception
	 */
	public boolean Save(Map<String, Object> ao_Set, int ai_Type,
			Boolean ab_Template) throws Exception {
		if (ab_Template) {
			ao_Set.put("TemplateID", this.Cyclostyle.CyclostyleID);
		} else {
			ao_Set.put("WorkItemID", this.Cyclostyle.WorkItem.WorkItemID);
		}
		ao_Set.put("DocumentID", this.DocumentID);
		ao_Set.put("DocumentName", this.DocumentName);
		ao_Set.put("DocumentType", this.DocumentType);
		ao_Set.put("FileName", this.DocumentExt);
		ao_Set.put("CreateID", this.CreatorID);
		ao_Set.put("Creator", this.Creator);
		ao_Set.put("CreateDate", MGlobal.dateToSqlTime(this.CreateDate));
		if (this.EditorID > 0) {
			ao_Set.put("EditID", this.EditorID);
			ao_Set.put("Editor", this.Editor);
			ao_Set.put("EditDate", MGlobal.dateToSqlTime(this.EditDate));
		}
		ao_Set.put("OriginalID", this.OrignID);
		ao_Set.put("DocumentVersion", this.VersionID);
		if (ab_Template == false) {
			ao_Set.put("EntryID", this.EntryID);
			ao_Set.put("FilePages", this.FilePages);
			ao_Set.put("FilePath", this.FilePath.equals("") ? null
					: this.FilePath);
		}
		if (this.DocumentType == EDocumentType.CommondDocument
				|| this.DocumentType == EDocumentType.FileVersionDocument
				|| this.DocumentType == EDocumentType.WordVersionDocument) {
			if (this.FileData.getFileLength() > 0 && ab_Template) {
				ao_Set.put("DocumentContent", this.FileData.getContent());
			}
		}
		return true;
	}

	/**
	 * 保存
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
				ls_Sql = "INSERT INTO DocumentInst "
						+ "(WorkItemID, DocumentID, DocumentName, DocumentType, CreateID, Creator, CreateDate, "
						+ "EditID, Editor, EditDate, FileName, OriginalID, DocumentVersion, DocumentContent, "
						+ "EntryID, FilePages, FilePath) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE DocumentInst SET "
						+ "DocumentName = ?, DocumentType = ?, CreateID = ?, Creator = ?, CreateDate = ?, "
						+ "EditID = ?, Editor = ?, EditDate = ?, FileName = ?, OriginalID = ?, DocumentVersion = ?, DocumentContent = ?, "
						+ "EntryID = ?, FilePages = ?, FilePath = ? WHERE WorkItemID = ? AND DocumentID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO DocumentList "
						+ "(TemplateID, DocumentID, DocumentName, DocumentType, CreateID, Creator, CreateDate, "
						+ "EditID, Editor, EditDate, FileName, OriginalID, DocumentVersion, DocumentContent) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE DocumentList SET "
						+ "DocumentName = ?, DocumentType = ?, CreateID = ?, Creator = ?, CreateDate = ?, "
						+ "EditID = ?, Editor = ?, EditDate = ?, FileName = ?, OriginalID = ?, DocumentVersion = ?, DocumentContent = ? "
						+ "WHERE TemplateID = ? AND DocumentID = ?";
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
	public int Save(String str_State, final int ai_SaveType, int ai_Type,
			final boolean ab_Inst, int ai_Update) throws SQLException {
		final CDocument doc = this;

		List parasList = new ArrayList();
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				parasList.add(doc.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(doc.DocumentID);
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(doc.Cyclostyle.CyclostyleID);
				parasList.add(doc.DocumentID);
			}
		}

		parasList.add(doc.DocumentName);
		parasList.add(doc.DocumentType);
		parasList.add(doc.CreatorID);
		parasList.add(doc.Creator);
		parasList.add(MGlobal.dateToSqlTime(doc.CreateDate));
		if (doc.EditorID == 0) {
			parasList.add(0);
			parasList.add(null);
			parasList.add(null);
		} else {
			parasList.add(doc.EditorID);
			parasList.add(doc.Editor);
			parasList.add(MGlobal.dateToSqlTime(doc.EditDate));
		}
		parasList.add(doc.DocumentExt);
		parasList.add(doc.OrignID);
		parasList.add(doc.VersionID);

		if (ab_Inst) {
			parasList.add(null);// DocumentContent

			parasList.add(doc.EntryID);
			parasList.add(doc.FilePages);
			parasList.add((doc.FilePath.equals("") ? null : doc.FilePath));

			if (ai_SaveType == 1) {
				parasList.add(doc.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(doc.DocumentID);
			}
		} else {
			if (doc.DocumentType == EDocumentType.CommondDocument
					|| doc.DocumentType == EDocumentType.FileVersionDocument
					|| doc.DocumentType == EDocumentType.WordVersionDocument) {
				if (doc.FileData.getFileLength() > 0) {
					parasList.add(doc.FileData.getBytes());// DocumentContent
				} else {
					parasList.add(null);// DocumentContent
				}
			} else {
				parasList.add(null);// DocumentContent
			}

			if (ai_SaveType == 1) {
				parasList.add(doc.Cyclostyle.CyclostyleID);
				parasList.add(doc.DocumentID);
			}
		}
		return CSqlHandle.getJdbcTemplate().update(str_State,
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
		this.Open(ao_Set, ai_Type, true);
	}

	/**
	 * 打開
	 * 
	 * @param ao_Set
	 *            数据集
	 * @param ai_Type
	 *            類型
	 * @param ab_Template
	 *            是否是模板
	 * @throws Exception
	 */
	public boolean Open(Map<String, Object> ao_Set, int ai_Type,
			boolean ab_Template) throws Exception {
		// this.Cyclostyle.CyclostyleID = ao_Set.get("TemplateID")
		this.DocumentID = (Integer) ao_Set.get("DocumentID");
		this.DocumentName = (String) ao_Set.get("DocumentName");
		this.DocumentType = (Integer) ao_Set.get("DocumentType");
		this.DocumentExt = (String) ao_Set.get("FileName");
		this.CreatorID = (Integer) ao_Set.get("CreateID");
		this.Creator = (String) ao_Set.get("Creator");
		this.CreateDate = (Date) ao_Set.get("CreateDate");
		if (ao_Set.get("EditID") != null) {
			this.EditorID = (Integer) ao_Set.get("EditID");
			if (this.EditorID > 0) {
				this.Editor = (String) ao_Set.get("Editor");
				this.EditDate = (Date) ao_Set.get("EditDate");
			}
		}
		this.OrignID = (Integer) ao_Set.get("OriginalID");
		this.VersionID = (Integer) ao_Set.get("DocumentVersion");
		if (ab_Template == false) {
			if (ao_Set.get("EntryID") != null)
				this.EntryID = (Integer) ao_Set.get("EntryID");
			if (ao_Set.get("FilePages") != null)
				this.FilePages = (Integer) ao_Set.get("FilePages");

			if (ao_Set.get("FilePath") != null)
				this.FilePath = (String) ao_Set.get("FilePath");
		}
		if (ao_Set.get("DocumentContent") != null)
			this.FileData.setContent((String) ao_Set.get("DocumentContent"));
		if (this.Cyclostyle.DocumentMaxID < this.DocumentID)
			this.Cyclostyle.DocumentMaxID = this.DocumentID;

		return true;
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
		ao_Bag.Write("this.DocumentName", this.DocumentID);
		ao_Bag.Write("this.DocumentType", this.DocumentName);
		ao_Bag.Write("mi_DocumentType", this.DocumentType);
		ao_Bag.Write("this.CreatorID", this.DocumentExt);
		ao_Bag.Write("ml_CreatorID", this.CreatorID);
		ao_Bag.Write("this.CreateDate", this.Creator);
		ao_Bag.Write("md_CreateDate", this.CreateDate);
		ao_Bag.Write("ml_EditorID", this.EditorID);
		ao_Bag.Write("this.EditDate", this.Editor);
		ao_Bag.Write("this.OrignID", this.EditDate);
		ao_Bag.Write("this.VersionID", this.OrignID);
		ao_Bag.Write("mi_VersionID", this.VersionID);
		ao_Bag.Write("this.FilePages", this.EntryID);
		ao_Bag.Write("mi_FilePages", this.FilePages);
		ao_Bag.Write("this.DocumentFileListXml", this.FilePath);
		ao_Bag.Write("ms_DocumentFileListXML", this.DocumentFileListXml);
		ao_Bag.Write("Content", FileData.getContent());
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
		this.DocumentID = ao_Bag.ReadInt("ml_DocumentID");
		this.DocumentName = ao_Bag.ReadString("ms_DocumentName");
		this.DocumentType = ao_Bag.ReadInt("mi_DocumentType");
		this.DocumentExt = ao_Bag.ReadString("ms_DocumentExt");
		this.CreatorID = ao_Bag.ReadInt("ml_CreatorID");
		this.Creator = ao_Bag.ReadString("ms_Creator");
		this.CreateDate = ao_Bag.ReadDate("md_CreateDate");
		this.EditorID = ao_Bag.ReadInt("ml_EditorID");
		this.Editor = ao_Bag.ReadString("ms_Editor");
		this.EditDate = ao_Bag.ReadDate("md_EditDate");
		this.OrignID = ao_Bag.ReadInt("ml_OrignID");
		this.VersionID = ao_Bag.ReadInt("ml_VersionID");
		this.EntryID = ao_Bag.ReadInt("ml_EntryID");
		this.FilePages = ao_Bag.ReadInt("mi_FilePages");
		this.FilePath = ao_Bag.ReadString("ms_FilePath");
		this.DocumentFileListXml = (String) ao_Bag
				.Read("ms_DocumentFileListXML");

		FileData.setContent(ao_Bag.ReadString("Content"));
		if (Cyclostyle.DocumentMaxID < this.DocumentID) {
			Cyclostyle.DocumentMaxID = this.DocumentID;
		}
	}

	/**
	 * 设置缺省正文/附件定义内容-当公文正文未被初始化时(远程启动的接收公文情况)
	 * 
	 * @return
	 */
	public boolean setDefaultContent() {
		Document lo_Xml = MXmlHandle.LoadXml("<Root>"
				+ this.DocumentFileListXml + "</Root>");
		boolean lb_Result = false;
		if (lo_Xml.getDocumentElement().hasChildNodes()) {
			Element lo_Node = MXmlHandle.getNodeByPath(
					lo_Xml.getDocumentElement(),
					"DocumentFileList[@IsDefault='1']");
			if (lo_Node == null)
				lo_Node = (Element) lo_Xml.getDocumentElement().getFirstChild();
			this.OldFileExt = lo_Node.getAttribute("FilePath");
			lb_Result = true;
		}
		lo_Xml = null;
		return lb_Result;
	}

}
