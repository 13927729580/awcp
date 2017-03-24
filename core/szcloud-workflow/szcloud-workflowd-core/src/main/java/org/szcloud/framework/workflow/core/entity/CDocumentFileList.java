package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.w3c.dom.Element;

public class CDocumentFileList {

	/**
	 * 模板标识（TemplateID）
	 * 
	 */
	public int TemplateID = 0;

	/**
	 * 附件标识（DocumentID）
	 */
	public int DocumentID = 0;

	/**
	 * 文件标识（FileID）
	 */
	public int FileID = 0;

	/**
	 * 名称（FileName）
	 */
	public String FileName = "";

	/**
	 * 文件路径(包括文件名称和扩展名称)（FilePath）
	 */
	public String FilePath = "";

	/**
	 * 是否缺省（IsDefault）
	 */
	public boolean IsDefault = false;

	/**
	 * 描述（Describe）
	 */
	public String Describe = "";

	/**
	 * 从数据库行对象中读取数据到对象
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开方式：=0-正常打开；=1-短属性打开；
	 * @throws Exception
	 */
	public void Open(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		if (ai_Type == 0) {
			TemplateID = (Integer) ao_Set.get("TemplateID");
			DocumentID = (Integer) ao_Set.get("DocumentID");
		}
		FileID = (Integer) ao_Set.get("FileID");
		FileName = (String) ao_Set.get("FileName");
		FilePath = (String) ao_Set.get("FilePath");
		IsDefault = ((Integer) ao_Set.get("IsDefault") == 1);
		if (ao_Set.get("Describe") != null)
			Describe = (String) ao_Set.get("Describe");
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
	public void Save(Map<String, Object> ao_Set, int ai_Type) throws Exception {
		ao_Set.put("TemplateID", TemplateID);
		ao_Set.put("DocumentID", DocumentID);
		ao_Set.put("FileID", FileID);
		ao_Set.put("FileName", FileName);
		ao_Set.put("FilePath", FilePath);
		ao_Set.put("IsDefault", (IsDefault ? 1 : 0));
		ao_Set.put("Describe", (Describe.equals("") ? null : Describe));
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ao_Connection
	 *            数据库连接对象；
	 * @param ai_SaveType
	 *            保存类型：=0-插入；=1-更新；
	 * @param ai_Type
	 *            存储类型
	 * @return
	 * @throws SQLException
	 */
	public static String getSaveState(int ai_SaveType, int ai_Type)
			throws SQLException {
		String ls_Sql = null;
		if (ai_SaveType == 0) {
			ls_Sql = "INSERT INTO DocumentFileList "
					+ "(TemplateID, DocumentID, FileID, FileName, FilePath, IsDefault, Describe) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		} else {
			ls_Sql = "UPDATE DocumentFileList SET "
					+ "FileName = ?, FilePath = ?, IsDefault = ?, Describe = ? "
					+ "WHERE TemplateID = ? AND DocumentID = ? AND FileID = ?";
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
	 * @param ai_Update
	 *            更新类型：=0-缺省更新；=1-最后更新；=2-单独更新；
	 * @throws SQLException
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type, int ai_Update)
			throws Exception {
		List parasList = new ArrayList();

		if (ai_SaveType == 0) {
			parasList.add(TemplateID);
			parasList.add(DocumentID);
			parasList.add(FileID);
		}

		parasList.add(FileName);
		parasList.add(FilePath);
		parasList.add((IsDefault ? 1 : 0));
		parasList.add((Describe.equals("") ? null : Describe));

		if (ai_SaveType == 1) {
			parasList.add(TemplateID);
			parasList.add(DocumentID);
			parasList.add(FileID);
		}

		return CSqlHandle.getJdbcTemplate().update(ao_State,
				parasList.toArray());
	}

	/**
	 * 从Xml节点导入到对象
	 * 
	 * @param ao_Node
	 *            导入节点
	 * @param ai_Type
	 *            导入类型：=0-正常导入；=1-短属性导入；
	 */
	public void Import(Element ao_Node, int ai_Type) {
		if (ai_Type == 0) {
			TemplateID = Integer.parseInt(ao_Node.getAttribute("TemplateID"));
			DocumentID = Integer.parseInt(ao_Node.getAttribute("DocumentID"));
		}
		FileID = Integer.parseInt(ao_Node.getAttribute("FileID"));
		FileName = ao_Node.getAttribute("FileName");
		FilePath = ao_Node.getAttribute("FilePath");
		IsDefault = Boolean.parseBoolean(ao_Node.getAttribute("IsDefault"));
		Describe = ao_Node.getAttribute("Describe");
	}

	/**
	 * 导出：实体类->Xml节点对象
	 * 
	 * @param ao_Node
	 *            Xml节点对象
	 */
	public void Export(Element ao_Node, int ai_Type) {
		if (ai_Type == 0) {
			ao_Node.setAttribute("TemplateID", String.valueOf(TemplateID));
			ao_Node.setAttribute("DocumentID", String.valueOf(DocumentID));
		}
		ao_Node.setAttribute("FileID", String.valueOf(FileID));
		ao_Node.setAttribute("FileName", FileName);
		ao_Node.setAttribute("FilePath", FilePath);
		ao_Node.setAttribute("IsDefault", String.valueOf(IsDefault));
		ao_Node.setAttribute("Describe", Describe);
	}

}
