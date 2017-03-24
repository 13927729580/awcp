package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 数据存储对象：公文处理对象模型中数据存储的数据库表格定义， 它与表头属性相辅相成，是本系统设计的一个重点和难点对象。
 * 
 * @author Jackie.Wang
 * 
 */
public class CTable extends CBase {

	/**
	 * 初始化
	 */
	public CTable() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CTable(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		// 所属的公文模板对象
		this.Cyclostyle = null;
		// 表格定义域（数据列定义）的集合
		if (this.Fields != null) {
			while (this.Fields.size() > 0) {
				CField lo_Field = this.Fields.get(0);
				this.Fields.remove(lo_Field);
				lo_Field.ClearUp();
				lo_Field = null;
			}
			this.Fields = null;
		}
		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	/**
	 * 表格定义域（数据列定义）的集合
	 */
	public List<CField> Fields = new ArrayList<CField>();

	/**
	 * 根据标识获取表格定义域（数据列定义）对象
	 * 
	 * @param al_Id
	 * @return
	 */
	public CField getFieldById(int al_Id) {
		for (CField lo_Field : this.Fields) {
			if (lo_Field.FieldID == al_Id) {
				return lo_Field;
			}
		}
		return null;
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 标识
	 */
	public int TableID = -1;

	/**
	 * 表格中文名称
	 */
	public String TableName = "";

	/**
	 * 设置表格中文名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setTableName(String value) throws Exception {
		if (value == null || value.equals("")) {
			// #错误：表格中文名称不能为空
			this.Raise(1095, "setTableName", "");
			return;
		}
		if (this.TableName == value) {
			return;
		}
		if (MGlobal.BeyondOfLength(value, 50) == false) {
			return;
		}

		for (CTable lo_Table : Cyclostyle.Tables) {
			if (lo_Table.TableName.equals(value)) {
				// #错误：表格中文名称不能重复
				this.Raise(1096, "setTableName", "");
				return;
			}
		}

		this.TableName = value;
	}

	/**
	 * 表格英文名称
	 */
	public String TableCode = "";

	/**
	 * 设置表格英文名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setTableCode(String value) throws Exception {
		if (value == null) {
			// #错误：表格英文名称不能为空
			this.Raise(1097, "setTableCode", "");
			return;
		}

		if (this.TableCode == value) {
			return;
		}
		if (MGlobal.BeyondOfLength(value, 50) == false) {
			return;
		}

		for (CTable lo_Table : Cyclostyle.Tables) {
			if (lo_Table.TableCode.equals(value)) {
				// #错误：表格英文名称不能重复
				this.Raise(1098, "setTableCode", "");
				return;
			}
		}

		this.TableCode = value;
	}

	/**
	 * 使用数据库的连接串代码提供与外部数据库数据操作的应用
	 */
	public String ConnCode = "";

	/**
	 * 表格列标识的最大值
	 */
	public int FieldMaxID = 0;

	/**
	 * 表格说明
	 */
	public String Summary = "";

	/**
	 * 取简单的建表SQL语句
	 */
	public String getCreateTableSql() {
		String ls_Str = "";
		CField lo_Field = new CField();
		String ls_Key = "";
		ls_Key = "";
		List<CTable> lo_FTables = new ArrayList<CTable>();
		List<CField> lo_FFields = new ArrayList<CField>();
		List<CField> lo_MFields = new ArrayList<CField>();
		int i = 0;
		ls_Str = "create table " + this.TableCode + " (" + "\n";
		for (CField tempLoopVar_lo_Field : Fields) {
			lo_Field = tempLoopVar_lo_Field;
			if (lo_Field.IsPrimaryKey) {
				ls_Key += lo_Field.FieldCode + ", ";
			}
			ls_Str = ls_Str + lo_Field.getFieldLabel() + "\n";
			addForginKey(lo_Field, lo_FTables, lo_FFields, lo_MFields);
		}
		if (ls_Key.equals("")) {
			ls_Str = ls_Str.substring(0, ls_Str.length() - 3) + ")" + "\n"
					+ "go";
		} else {
			ls_Str = ls_Str + "constraint PK_" + this.TableCode
					+ " primary key ("
					+ ls_Key.substring(0, ls_Key.length() - 2) + ")" + "\n";
			ls_Str = ls_Str + ")" + "\n" + "go";
		}

		for (i = 1; i <= lo_FTables.size(); i++) {
			ls_Str = ls_Str + "\n" + "\n";
			ls_Str = ls_Str + "alter table " + this.TableCode + "\n";
			ls_Str = ls_Str + MGlobal.addSpace(4) + "add constraint FK_"
					+ this.TableCode.substring(0, 8).toUpperCase();
			ls_Str = ls_Str
					+ "_REFERENCE_"
					+ lo_FTables.get(i).toString().substring(0, 8)
							.toUpperCase();
			ls_Str = ls_Str + " foreign key (" + lo_MFields.get(i) + ")" + "\n";
			ls_Str = ls_Str + MGlobal.addSpace(8) + " references "
					+ (lo_FTables.get(i)) + " (" + (lo_FFields.get(i)) + ")"
					+ "\n" + "go";
		}
		lo_FTables = null;
		lo_FFields = null;
		lo_MFields = null;
		return ls_Str;
	}

	/**
	 * 
	 */
	private void addForginKey(CField ao_Field, List<CTable> ao_Tables,
			List<CField> ao_Fields, List<CField> ao_MFields) {
		try {
			if (ao_Field.ForeignKey.equals("")) {
				return;
			}

			String ls_Table = ao_Field.getForeignKeyTable();
			String ls_Field = ao_Field.getForeignKeyField();

			String ls_Value = ao_Fields.get(Integer.parseInt(ls_Table))
					.toString();
			String ls_MValue = ao_MFields.get(Integer.parseInt(ls_Table))
					.toString();
			ao_Fields.remove(ls_Table);
			// ao_Fields.add(ls_Value + ", " + ls_Field, ls_Table);
			ao_MFields.remove(ls_Table);
			// ao_MFields.add(ls_MValue + ", " + ao_Field.getFieldCode(),
			// ls_Table, null, null);
		} catch (Exception e) {
			// ao_Tables.add(ls_Table, ls_Table, null, null);
			// ao_Fields.add(ls_Field, ls_Table, null, null);
			// ao_MFields.Add(ao_Field.FieldCode, ls_Table, null, null);
		}
	}

	/**
	 * 当前对象是否可用
	 * 
	 * @throws Exception
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			String ls_Msg = "";
			// Add Get Valid Code...
			for (CField lo_Field : this.Fields) {
				ls_Msg += lo_Field.IsValid(ai_SpaceLength + 4);
			}
			if (ls_Msg != "") {
				ls_Msg = "表格存储【" + this.TableName + "】有错误：" + "\n" + ls_Msg;
			}
			return ls_Msg;
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
		if (ai_Type == 0) {
			this.TableID = Integer.parseInt(ao_Node.getAttribute("TableID"));
			this.TableName = ao_Node.getAttribute("TableName");
			this.TableCode = ao_Node.getAttribute("TableCode");
			this.ConnCode = ao_Node.getAttribute("ConnCode");
			this.FieldMaxID = Integer.parseInt(ao_Node
					.getAttribute("FieldMaxID"));
			this.Summary = ao_Node.getAttribute("Summary");
		}

		NodeList lo_List = ao_Node.getElementsByTagName("Field");
		for (int i = 0; i < lo_List.getLength(); i++) {
			Element lo_Node = (Element) lo_List.item(0);
			CField lo_Field = new CField(this.Logon);
			lo_Field.Table = this;
			lo_Field.Import(lo_Node, ai_Type);
			this.Fields.add(lo_Field);
			if (this.FieldMaxID < lo_Field.FieldID)
				this.FieldMaxID = lo_Field.FieldID;
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
		if (ai_Type == 0) {
			ao_Node.setAttribute("TableID", String.valueOf(this.TableID));
			ao_Node.setAttribute("TableName", this.TableName);
			ao_Node.setAttribute("TableCode", this.TableCode);
			ao_Node.setAttribute("ConnCode", this.ConnCode);
			ao_Node.setAttribute("FieldMaxID", String.valueOf(this.FieldMaxID));
			ao_Node.setAttribute("Summary", this.Summary);

			ao_Node.setAttribute("FieldsCount",
					String.valueOf(this.Fields.size()));
		}

		for (CField lo_Field : Fields) {
			lo_Field.Export(ao_Node, ai_Type);
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
		ao_Set.put("TemplateID", this.Cyclostyle.CyclostyleID);
		ao_Set.put("TableID", this.TableID);
		ao_Set.put("TableName", this.TableName);
		ao_Set.put("TableCode", this.TableCode);
		ao_Set.put("ConnCode", this.ConnCode == null ? null : this.ConnCode);
		ao_Set.put("TableDescribe", this.Summary == null ? null : this.Summary);
		// 保存数据列
		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				ao_Set.put("TableContent", "TA" + this.WriteContent(ai_Type));
			} else {
				ao_Set.put("TableContent", this.WriteContent(ai_Type));
			}
		} else {
			ao_Set.put("TableContent", this.ExportToXml("Table", 1));
		}
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
			boolean ab_Inst) {
		String ls_Sql = null;
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO TableInst "
						+ "(WorkItemID, TableID, TableCode, TableName, ConnCode, TableDescribe, TableContent) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE TableInst SET "
						+ "TableCode = ?, TableName = ?, ConnCode = ?, TableDescribe = ?, TableContent = ? "
						+ "WHERE WorkItemID = ? AND TableID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO TableList "
						+ "(TemplateID, TableID, TableName, TableCode, ConnCode, TableContent, TableDescribe) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE TableList SET "
						+ "TableName = ?, TableCode = ?, ConnCode = ?, TableContent = ?, TableDescribe = ? "
						+ "WHERE TemplateID = ? AND TableID = ?";
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
	 * @throws Exception
	 */
	public int Save(String ao_State, int ai_SaveType, int ai_Type,
			boolean ab_Inst, int ai_Update) throws Exception {
		List parasList = new ArrayList();
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.TableID);
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.TableID);
			}
		}

		parasList.add(this.TableName);
		parasList.add(this.TableCode);
		parasList.add(MGlobal.getDbValue(this.ConnCode));
		// 保存数据列
		if (Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				parasList.add("TA" + this.WriteContent(ai_Type));
			} else {
				parasList.add(this.WriteContent(ai_Type));
			}
		} else {
			parasList.add(this.ExportToXml("Field", ai_Type));
		}
		parasList.add(MGlobal.getDbValue(this.Summary));

		if (ab_Inst) {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.TableID);
			}
		} else {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.TableID);
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
		// this.Cyclostyle.setCyclostyleID(ao_Set.get("TemplateID"));
		this.TableID = (Integer) ao_Set.get("TableID");
		this.TableName = (String) ao_Set.get("TableName");
		this.TableCode = (String) ao_Set.get("TableCode");
		if (ao_Set.get("ConnCode") != null) {
			this.ConnCode = (String) ao_Set.get("ConnCode");
		}
		if (ao_Set.get("TableDescribe") != null) {
			this.Summary = (String) ao_Set.get("TableDescribe");
		}
		// 打开数据字段
		String ls_Text = (String) ao_Set.get("TableContent");
		if (this.Cyclostyle.OpenType == EDataHandleType.OrignType) {
			if (ls_Text.substring(0, 2).equals("FM")) {
				this.ReadContent(ls_Text.substring(2, ls_Text.length()), 1);
			} else {
				this.ReadContent(ls_Text, 0);
			}
		} else {
			this.ImportFormXml(ls_Text, ai_Type);
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
		ao_Bag.Write("ml_TableID", this.TableID);
		ao_Bag.Write("ms_TableName", this.TableName);
		ao_Bag.Write("ms_TableCode", this.TableCode);
		ao_Bag.Write("ms_ConnCode", this.ConnCode);
		ao_Bag.Write("ml_FieldMaxID", this.FieldMaxID);
		ao_Bag.Write("ms_Summary", this.Summary);

		// 导出数据列
		ao_Bag.Write("FieldsCount", Fields.size());
		for (CField lo_Field : Fields) {
			lo_Field.Write(ao_Bag, ai_Type);
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
		this.TableID = ao_Bag.ReadInt("ml_TableID");
		this.TableName = ao_Bag.ReadString("ms_TableName");
		this.TableCode = ao_Bag.ReadString("ms_TableCode");
		this.ConnCode = ao_Bag.ReadString("ms_ConnCode");
		this.FieldMaxID = ao_Bag.ReadInt("ml_FieldMaxID");
		this.Summary = ao_Bag.ReadString("ms_Summary");
		// 导入数据列
		int li_Count = ao_Bag.ReadInt("FieldsCount");
		for (int i = 0; i < li_Count; i++) {
			CField lo_Field = new CField(this.Logon);
			lo_Field.Table = this;
			lo_Field.Read(ao_Bag, ai_Type);
			Fields.add(lo_Field);
		}
	}

}
