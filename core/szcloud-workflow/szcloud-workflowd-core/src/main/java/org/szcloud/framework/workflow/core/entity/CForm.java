package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EFormAccessType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 表单定义对象：公文模板处理对象模型中存储和操作一些公文表单定义的对象，<br>
 * 是公文模板的一个基本组成部分，它是公文模板中表单的表现形式，同时通过它<br>
 * 与公文中表头属性的关系，得以公文数据存储与表现完美的结合在一起，它是本<br>
 * 系统设计的一个重点和难点对象。
 * 
 * @author Jackie.Wang
 */
public class CForm extends CBase {

	/**
	 * 初始化
	 */
	public CForm() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CForm(CLogon ao_Logon) {
		super(ao_Logon);
		mo_Rectangle.moveRectangle(50, 75, this.Width - 100, this.Height - 150);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	/**
	 * 表单图元的集合
	 */
	public List<CFormCell> FormCells = new ArrayList<CFormCell>();

	/**
	 * 根据标识获取表单图元
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CFormCell getFormCellById(int al_Id) {
		for (CFormCell lo_Cell : FormCells) {
			if (lo_Cell.CellID == al_Id)
				return lo_Cell;
		}
		return null;
	}

	/**
	 * 表单连线的集合
	 */
	public List<CFormLine> FormLines = new ArrayList<CFormLine>();

	/**
	 * 根据标识获取表单连线
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CFormLine getFormLineById(int al_Id) {
		for (CFormLine lo_Line : FormLines) {
			if (lo_Line.LineID == al_Id)
				return lo_Line;
		}
		return null;
	}

	/**
	 * 表单表格的集合
	 */
	public List<CFormTable> FormTables = new ArrayList<CFormTable>();

	/**
	 * 根据标识获取表单表格
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CFormTable getFormTableById(int al_Id) {
		for (CFormTable lo_Table : this.FormTables) {
			if (lo_Table.TableID == al_Id)
				return lo_Table;
		}
		return null;
	}

	/**
	 * 公文数据表格各边界大小
	 */
	private CRectangle mo_Rectangle = new CRectangle();

	/**
	 * 公文数据表格各边界大小
	 * 
	 * @return
	 */
	public CRectangle getRectangle() {
		return mo_Rectangle;
	}

	/**
	 * 公文数据表格各边界大小
	 * 
	 * @param value
	 */
	public void setRectangle(CRectangle value) {
		mo_Rectangle = value;
	}

	/**
	 * Web端显示表格对象的集合
	 */
	public List<CTabulation> Tabulations = new ArrayList<CTabulation>();

	/**
	 * 根据标识获取Web端显示表格对象
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CTabulation getTabulationById(int al_Id) {
		for (CTabulation lo_Tab : Tabulations) {
			if (lo_Tab.TabulationID == al_Id)
				return lo_Tab;
		}
		return null;
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 表格标识
	 */
	public int FormID = -1;

	/**
	 * 表格名称
	 */
	public String FormName = "未命名表单";

	/**
	 * 设置表格名称
	 * 
	 * @param value
	 */
	public void setFormName(String value) {
		if (MGlobal.BeyondOfLength(value, 50) == false) {
			return;
		}
		this.FormName = value;
	}

	/**
	 * 公文数据表格在流转时的使用权限，该数据为内存数据，无须保存
	 */
	public int Access = EFormAccessType.FormVisble;

	/**
	 * 公文数据表格宽度
	 */
	public int Width = MGlobal.const16PaperWidth;

	/**
	 * 设置公文数据表格宽度
	 * 
	 * @param value
	 */
	public void setWidth(int value) {
		if (value < 140) {
			return;
		}
		this.Width = value;
		if (mo_Rectangle.getRectLeft() > this.Width / 2) {
			mo_Rectangle.setRectLeft(this.Width / 2);
		}
		if (mo_Rectangle.getRectRight() > this.Width) {
			mo_Rectangle.setRectRight(this.Width);
		}
	}

	/**
	 * 公文数据表格高度
	 */
	public int Height = MGlobal.const16PaperHeight;

	/**
	 * 设置公文数据表格高度
	 * 
	 * @param value
	 */
	public void setHeight(int value) {
		if (value < 140) {
			return;
		}
		this.Height = value;
		if (mo_Rectangle.getRectTop() > this.Height / 2) {
			mo_Rectangle.setRectTop(this.Height / 2);
		}
		if (mo_Rectangle.getRectBottom() > this.Height) {
			mo_Rectangle.setRectBottom(this.Height);
		}
	}

	/**
	 * 读取公文数据表格实际内容高度
	 */
	public int getActualHeight() {
		int ll_Height = 0;
		for (CTabulation lo_Tabulation : Tabulations) {
			ll_Height += lo_Tabulation.Rectangle.getRectHeight();
		}
		return ll_Height;
	}

	/**
	 * 公文数据表格背景色
	 */
	public int BackColor = 0xFFFFFF;

	/**
	 * 公文数据表格描述
	 */
	public String Summary = "";

	/**
	 * 表格生成XML的内容
	 */
	public String FormContent = "";

	/**
	 * 表格在动态流转中的事件内容
	 */
	public String FormEventContent = "";

	/**
	 * 表单类型: =0 - 相对定位; =1 - 绝对定位; =9 - 外部表单;
	 */
	public int FormType = 0;

	/**
	 * 获取表单Xml定义
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFormXml() throws Exception {
		return this.ExportToXml("Form", 0);
	}

	// #==========================================================================#
	// 常用过程或函数定义
	// #==========================================================================#

	/**
	 * 清除释放对象的内存数据
	 */
	public void ClearUp() {
		try {
			// 所属的公文模板对象
			this.Cyclostyle = null;

			clearChild();

			super.ClearUp();
		} catch (Exception ex) {
		}
	}

	/**
	 * 清除子对象
	 * 
	 * @throws Exception
	 */
	private void clearChild() throws Exception {
		// 表单图元的集合
		if (this.FormCells != null) {
			while (this.FormCells.size() > 0) {
				CFormCell lo_Cell = this.FormCells.get(0);
				this.FormCells.remove(lo_Cell);
				lo_Cell.ClearUp();
				lo_Cell = null;
			}
			this.FormCells = null;
		}

		// 表单连线的集合
		if (this.FormLines != null) {
			while (this.FormLines.size() > 0) {
				CFormLine lo_Line = this.FormLines.get(0);
				FormLines.remove(lo_Line);
				lo_Line.ClearUp();
				lo_Line = null;
			}
			this.FormLines = null;
		}

		// 表单表格的集合
		if (this.FormTables != null) {
			while (this.FormTables.size() > 0) {
				CFormTable lo_Table = this.FormTables.get(0);
				FormTables.remove(lo_Table);
				lo_Table.ClearUp();
				lo_Table = null;
			}
			this.FormTables = null;
		}

		// 公文数据表格各边界大小
		if (mo_Rectangle != null) {
			mo_Rectangle.ClearUp();
			mo_Rectangle = null;
		}

		// Web端显示表格对象的集合
		if (this.Tabulations != null) {
			while (this.Tabulations.size() > 0) {
				CTabulation lo_Tabulation = this.Tabulations.get(0);
				Tabulations.remove(lo_Tabulation);
				lo_Tabulation.ClearUp();
				lo_Tabulation = null;
			}
			this.Tabulations = null;
		}
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
			String ls_Msg = "";
			for (CFormCell lo_Cell : this.FormCells) {
				ls_Msg += lo_Cell.IsValid(ai_SpaceLength + 4);
			}

			if (!ls_Msg.equals("")) {
				ls_Msg = MGlobal.addSpace(ai_SpaceLength) + "表格定义【"
						+ this.FormName + "】有错误：\n" + ls_Msg;
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
	 * @throws Exception
	 */
	@Override
	protected void Import(Element ao_Node, int ai_Type) throws Exception {
		if (ai_Type == 0 && ao_Node.hasAttribute("FormID")) {
			this.FormID = Integer.parseInt(ao_Node.getAttribute("FormID"));
			this.FormName = ao_Node.getAttribute("FormName");
			this.Summary = ao_Node.getAttribute("Summary");
		}
		this.Width = Integer.parseInt(ao_Node.getAttribute("Width"));
		this.Height = Integer.parseInt(ao_Node.getAttribute("Height"));
		this.BackColor = Integer.parseInt(ao_Node.getAttribute("BackColor"));
		this.FormContent = ao_Node.getAttribute("FormContent");
		if (!ao_Node.getAttribute("FormType").equals("")) {
			this.FormType = Integer.parseInt(ao_Node.getAttribute("FormType"));
		}

		// #导入公文数据表格各边界大小
		Element lo_Node = MXmlHandle.getNodeByName(ao_Node, "Rectangle");
		mo_Rectangle.Import(lo_Node, ai_Type);

		// #导入表格元素
		NodeList lo_List = ao_Node.getElementsByTagName(ai_Type == 1 ? "Cell"
				: "FormCell");
		for (int i = 0; i < lo_List.getLength(); i++) {
			lo_Node = (Element) lo_List.item(i);
			CFormCell lo_Cell = new CFormCell(this.Logon);
			lo_Cell.Form = this;
			lo_Cell.Import(lo_Node, ai_Type);
			this.FormCells.add(lo_Cell);
			if (this.Cyclostyle.FormCellMaxID < lo_Cell.CellID)
				this.Cyclostyle.FormCellMaxID = lo_Cell.CellID;
		}

		// #导入表格连线
		lo_List = ao_Node.getElementsByTagName(ai_Type == 1 ? "Line"
				: "FormLine");
		for (int i = 0; i < lo_List.getLength(); i++) {
			lo_Node = (Element) lo_List.item(i);
			CFormLine lo_Line = new CFormLine(this.Logon);
			lo_Line.Form = this;
			lo_Line.Import(lo_Node, ai_Type);
			this.FormLines.add(lo_Line);
			if (this.Cyclostyle.FormLineMaxID < lo_Line.LineID)
				this.Cyclostyle.FormLineMaxID = lo_Line.LineID;
		}

		// #导入表格单元
		lo_List = ao_Node.getElementsByTagName(ai_Type == 1 ? "Table"
				: "FormTable");
		for (int i = 0; i < lo_List.getLength(); i++) {
			lo_Node = (Element) lo_List.item(i);
			CFormTable lo_Table = new CFormTable(this.Logon);
			lo_Table.Form = this;
			lo_Table.Import(lo_Node, ai_Type);
			this.FormTables.add(lo_Table);
			if (this.Cyclostyle.FormTableMaxID < lo_Table.TableID)
				this.Cyclostyle.FormTableMaxID = lo_Table.TableID;
		}

		// #导入Web端显示表格对象的集合
		lo_List = ao_Node.getElementsByTagName("Tabulation");
		for (int i = 0; i < lo_List.getLength(); i++) {
			lo_Node = (Element) lo_List.item(i);
			CTabulation lo_Tabulation = new CTabulation(this.Logon);
			lo_Tabulation.Form = this;
			lo_Tabulation.Import(lo_Node, ai_Type);
			this.Tabulations.add(lo_Tabulation);
			if (this.Cyclostyle.TabulationMaxID < lo_Tabulation.TabulationID)
				this.Cyclostyle.TabulationMaxID = lo_Tabulation.TabulationID;
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
			ao_Node.setAttribute("FormID", Integer.toString(this.FormID));
			ao_Node.setAttribute("FormName", this.FormName);
			ao_Node.setAttribute("Summary", this.Summary);
		}
		ao_Node.setAttribute("Width", Integer.toString(this.Width));
		ao_Node.setAttribute("Height", Integer.toString(this.Height));
		ao_Node.setAttribute("BackColor", Long.toString(this.BackColor));
		ao_Node.setAttribute("FormContent", this.FormContent);
		ao_Node.setAttribute("FormType", Integer.toString(this.FormType));

		// #导出公文数据表格各边界大小
		Element lo_Node = MXmlHandle.addElement(ao_Node, "Rectangle");
		ao_Node.appendChild(lo_Node);
		mo_Rectangle.Export(lo_Node, ai_Type);

		// #导出表格元素
		if (ai_Type == 0)
			ao_Node.setAttribute("theFormCellsCount",
					Integer.toString(this.FormCells.size()));
		for (CFormCell lo_Cell : this.FormCells) {
			lo_Node = MXmlHandle.addElement(ao_Node, (ai_Type == 1 ? "Cell"
					: "FormCell"));
			ao_Node.appendChild(lo_Node);
			lo_Cell.Export(lo_Node, ai_Type);
		}

		// #导出表格连线
		if (ai_Type == 0)
			ao_Node.setAttribute("FormLinesCount",
					Integer.toString(this.FormLines.size()));
		for (CFormLine lo_Line : this.FormLines) {
			lo_Node = MXmlHandle.addElement(ao_Node, (ai_Type == 1 ? "Line"
					: "FormLine"));
			ao_Node.appendChild(lo_Node);
			lo_Line.Export(lo_Node, ai_Type);
		}

		// #导出表格单元
		if (ai_Type == 0)
			ao_Node.setAttribute("FormTablesCount",
					Integer.toString(this.FormTables.size()));
		for (CFormTable lo_Table : this.FormTables) {
			lo_Node = MXmlHandle.addElement(ao_Node, (ai_Type == 1 ? "Table"
					: "FormTable"));
			ao_Node.appendChild(lo_Node);
			lo_Table.Export(lo_Node, ai_Type);
		}

		// #导出Web端显示表格对象的集合
		if (ai_Type == 0)
			ao_Node.setAttribute("TabulationsCount",
					Integer.toString(this.Tabulations.size()));
		for (CTabulation lo_Tabulation : this.Tabulations) {
			lo_Node = MXmlHandle.addElement(ao_Node, "Tabulation");
			ao_Node.appendChild(lo_Node);
			lo_Tabulation.Export(lo_Node, ai_Type);
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
		ao_Set.put("TemplateID", Cyclostyle.CyclostyleID);
		ao_Set.put("FormID", this.FormID);
		ao_Set.put("FormName", this.FormName);
		ao_Set.put("FormDescribe", this.Summary == null ? null : this.Summary);

		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				ao_Set.put("FormContent", "FM" + this.WriteContent(ai_Type));
			} else {
				ao_Set.put("FormContent", this.WriteContent(ai_Type));
			}
		} else {
			ao_Set.put("FormContent", this.ExportToXml("Form", 1));
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
			boolean ab_Inst) throws SQLException {
		String ls_Sql = null;
		if (ab_Inst) {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO FormInst "
						+ "(WorkItemID, FormID, FormName, FormContent, FormDescribe) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE FormInst SET "
						+ "FormName = ?, FormContent = ?, FormDescribe = ? "
						+ "WHERE WorkItemID = ? AND FormID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO FormList "
						+ "(TemplateID, FormID, FormName, FormContent, FormDescribe) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE FormList SET "
						+ "FormName = ?, FormContent = ?, FormDescribe = ? "
						+ "WHERE TemplateID = ? AND FormID = ?";
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
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.CyclostyleID);
			}
		}
		if (ai_SaveType == 0) {
			parasList.add(this.FormID);
		}

		parasList.add(this.FormName);

		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				parasList.add("FM" + this.WriteContent(ai_Type));
			} else {
				parasList.add(this.WriteContent(ai_Type));
			}
		} else {
			parasList.add(this.ExportToXml("Form", ai_Type));
		}

		parasList.add((this.Summary.equals("") ? null : this.Summary));

		if (ab_Inst) {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
			}
		} else {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.CyclostyleID);
			}
		}
		if (ai_SaveType == 1) {
			parasList.add(this.FormID);
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
		this.FormID = (Integer) ao_Set.get("FormID");
		this.FormName = (String) ao_Set.get("FormName");
		if (ao_Set.get("FormDescribe") != null) {
			this.Summary = (String) ao_Set.get("FormDescribe");
		}

		String ls_Text = (String) ao_Set.get("FormContent");
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
		// #导出基本信息
		ao_Bag.Write("ml_FormID", this.FormID);
		ao_Bag.Write("ms_FormName", this.FormName);
		ao_Bag.Write("ml_Width", this.Width);
		ao_Bag.Write("ml_Height", this.Height);
		ao_Bag.Write("ml_BackColor", this.BackColor);
		ao_Bag.Write("ms_Summary", this.Summary);
		ao_Bag.Write("ms_FormContent", this.FormContent);
		ao_Bag.Write("mi_FormType", this.FormType);
		// #导出公文数据表格各边界大小
		mo_Rectangle.Write(ao_Bag, ai_Type);

		// #导出表格元素
		ao_Bag.Write("FormCellsCount", this.FormCells.size());
		for (CFormCell lo_Cell : this.FormCells) {
			lo_Cell.Write(ao_Bag, ai_Type);
		}
		// #导出表格连线
		ao_Bag.Write("FormLinesCount", this.FormLines.size());
		for (CFormLine lo_Line : this.FormLines) {
			lo_Line.Write(ao_Bag, ai_Type);
		}

		// #导出表格单元
		ao_Bag.Write("FormTablesCount", this.FormTables.size());
		for (CFormTable lo_Table : this.FormTables) {
			lo_Table.Write(ao_Bag, ai_Type);
		}

		// #导出Web端显示表格对象的集合
		ao_Bag.Write("TabulationsCount", this.Tabulations.size());
		for (CTabulation lo_Tabulation : this.Tabulations) {
			lo_Tabulation.Write(ao_Bag, ai_Type);
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
			this.Width = (Integer) ao_Bag.Read("FW");
			this.Height = (Integer) ao_Bag.Read("FH");
			this.BackColor = (Integer) ao_Bag.Read("BC");
			this.FormContent = (String) ao_Bag.Read("FC");
		} else {
			// #导入基本信息
			this.FormID = (Integer) ao_Bag.Read("ml_FormID");
			this.FormName = (String) ao_Bag.Read("ms_FormName");
			this.Width = (Integer) ao_Bag.Read("ml_Width");
			this.Height = (Integer) ao_Bag.Read("ml_Height");
			this.BackColor = (Integer) ao_Bag.Read("ml_BackColor");
			this.Summary = (String) ao_Bag.Read("ms_Summary");
			this.FormContent = (String) ao_Bag.Read("ms_FormContent");
			this.FormType = (Integer) ao_Bag.Read("mi_FormType");
		}

		// #导入公文数据表格各边界大小
		mo_Rectangle.Read(ao_Bag, ai_Type);

		int li_Count;

		// #导入表格元素
		if (ai_Type == 1) {
			li_Count = (Integer) ao_Bag.Read("FCC");
		} else {
			li_Count = (Integer) ao_Bag.Read("FormCellsCount");
		}
		for (int i = 0; i < li_Count; i++) {
			CFormCell lo_Cell = new CFormCell();
			lo_Cell.Form = this;
			lo_Cell.Read(ao_Bag, ai_Type);
			this.FormCells.add(lo_Cell);
		}

		// #导入表格连线
		if (ai_Type == 1) {
			li_Count = (Integer) ao_Bag.Read("FLC");
		} else {
			li_Count = (Integer) ao_Bag.Read("FormLinesCount");
		}
		for (int i = 0; i < li_Count; i++) {
			CFormLine lo_Line = new CFormLine();
			lo_Line.Form = this;
			lo_Line.Read(ao_Bag, ai_Type);
			this.FormLines.add(lo_Line);
		}

		// #导入表格单元
		if (ai_Type == 1) {
			li_Count = (Integer) ao_Bag.Read("FTC");
		} else {
			li_Count = (Integer) ao_Bag.Read("FormTablesCount");
		}
		for (int i = 0; i < li_Count; i++) {
			CFormTable lo_Table = new CFormTable();
			lo_Table.Form = this;
			lo_Table.Read(ao_Bag, ai_Type);
			this.FormTables.add(lo_Table);
		}

		// #导入Web端显示表格对象的集合
		if (ai_Type == 1) {
			li_Count = (Integer) ao_Bag.Read("TNC");
		} else {
			li_Count = (Integer) ao_Bag.Read("TabulationsCount");
		}
		for (int i = 0; i < li_Count; i++) {
			CTabulation lo_Tabulation = new CTabulation();
			lo_Tabulation.Form = this;
			lo_Tabulation.Read(ao_Bag, ai_Type);
			if (this.Cyclostyle.TabulationMaxID < lo_Tabulation.TabulationID)
				this.Cyclostyle.TabulationMaxID = lo_Tabulation.TabulationID;
			this.Tabulations.add(lo_Tabulation);
		}
	}

}
