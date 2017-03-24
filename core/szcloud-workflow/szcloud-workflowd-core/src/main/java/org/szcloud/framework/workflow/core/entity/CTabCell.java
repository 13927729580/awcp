package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.ETabCellAlignType;
import org.szcloud.framework.workflow.core.emun.ETabulationSizeType;
import org.szcloud.framework.workflow.core.emun.EUseColorType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 表单单元对象
 * 
 * @author Jackie.Wang
 * 
 */
public class CTabCell extends CBase {

	/**
	 * 初始化
	 */
	public CTabCell() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CTabCell(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		this.Tabulation = null;
		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的Web端显示表格对象
	 */
	public CTabulation Tabulation = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 表单单元对象标识
	 */
	public int TabCellID = -1;

	/**
	 * CSS Class定义
	 */
	public String CssClass = "";

	/**
	 * 表单单元对象横向开始索引
	 */
	public int StartX = 0;

	/**
	 * 表单单元对象横向终止索引
	 */
	public int StopX = 0;

	/**
	 * 表单单元对象纵向开始索引
	 */
	public int StartY = 0;

	/**
	 * 表单单元对象纵向终止索引
	 */
	public int StopY = 0;

	/**
	 * 颜色使用情况
	 */
	public int ColorType = EUseColorType.DefaultUseColor;

	/**
	 * 边框颜色
	 */
	public int BorderColor = 0;

	/**
	 * 边框高亮颜色
	 */
	public int BorderColorLight = 0;

	/**
	 * 边框背景颜色
	 */
	public int BorderColorDark = 0;

	/**
	 * 背景色
	 */
	public int BackColor = 0xFFFFFF;

	/**
	 * 表格单元内部横向间距
	 */
	public int ColSpan = 0;

	/**
	 * 表格单元内部纵向间距
	 */
	public int RowSpan = 0;

	/**
	 * 表格单元内部横向对齐方式
	 */
	public int HorAlign = ETabCellAlignType.DefaultAlign;

	/**
	 * 表格单元内部纵向对齐方式
	 */
	public int VerAlign = ETabCellAlignType.DefaultAlign;

	/**
	 * 表格单元内部包含内容，格式为：“T1;C5;BR;...”，其中: T -
	 * Web端显示表格对象TTabulation，其后的数字表示TTabulation的标识 C -
	 * 表单图元对象TFormCell，其后的数字表示TFormCell的标识 BR - 换行符号
	 */
	public String TabCellContent = "";

	/**
	 * 读取表格单元内部包含内容描述
	 */
	public String getTabCellContentDescribe() {
		if (this.TabCellContent == null)
			return "";

		String ls_Value = "";
		String[] ls_Array = this.TabCellContent.split(";");
		for (int i = 0; i <= (ls_Array.length - 1); i++) {
			String ls_Type = MGlobal.left(ls_Array[i], 1);
			if (ls_Type == "T") {
				int ll_Id = Integer.parseInt(MGlobal.right(ls_Array[i],
						ls_Array[i].length() - 1));
				CTabulation lo_Table = this.Tabulation.Form
						.getTabulationById(ll_Id);
				ls_Value += lo_Table.TabulationName + ";";
				break;
			}
			if (ls_Type == "C") {
				int ll_Id = Integer.parseInt(MGlobal.right(ls_Array[i],
						ls_Array[i].length() - 1));
				CFormCell lo_Cell = this.Tabulation.Form.getFormCellById(ll_Id);
				ls_Value += lo_Cell.CellName + ";";
				break;
			}
		}

		return ls_Value;
	}

	/**
	 * 是否被合并的表格单元
	 */
	public boolean UniteTabCell = false;

	/**
	 * 读取表格合并单元内部包含内容
	 * @throws Exception 
	 */
	public String getUniteTabCellContent() throws Exception {
		String ls_Content = "";
		CTabCell lo_TabCell = this.Tabulation.getTabCellByPosition(this.StartX,
				this.StartY, false);
		for (CTabCell lo_TabCellOne : this.Tabulation.TabCells) {
			if (lo_TabCell.StartX <= lo_TabCellOne.StartX
					& lo_TabCell.StopX >= lo_TabCellOne.StartX
					& lo_TabCell.StartY <= lo_TabCellOne.StartY
					& lo_TabCell.StopY >= lo_TabCellOne.StartY) {
				ls_Content += lo_TabCellOne.TabCellContent;
			}
		}
		return ls_Content;
	}

	/**
	 * 读取单元格的宽度
	 */
	public int getCellWidth() {
		int ll_Value = 0;
		String[] ls_Array = this.Tabulation.HorSpaces.split(";");
		for (int i = this.StartX - 1; i < this.StopX; i++) {
			ll_Value += Integer.parseInt(ls_Array[i]);
		}
		return ll_Value;
	}

	/**
	 * 设置单元格的宽度
	 */
	public void setCellWidth(int value) {
		int ll_Value = 0;
		if (value < 1)
			return;

		if (this.Tabulation.HorSpaceStyle == ETabulationSizeType.SizeByPercent) {
			if (value > 100)
				return;
		}

		String[] ls_Array = Tabulation.HorSpaces.split(";");
		for (int i = this.StartX - 1; i < this.StopX; i++) {
			ll_Value += Integer.parseInt(ls_Array[i]);
		}

		if (ll_Value == value)
			return;

		int ll_Other = 0;
		int ll_Every = (int) ((ll_Value - value) / (this.StopX - this.StartX + 1));
		if (Tabulation.HorSpaceStyle == ETabulationSizeType.SizeByPixels) {
			for (int i = this.StartX - 1; i < this.StopX; i++) {
				ls_Array[i] = Integer.toString((Integer.parseInt(ls_Array[i]))
						+ ll_Every);
			}
			String s = "";
			for (String ss : ls_Array) {
				s += ss;
			}
			this.Tabulation.HorSpaces = s;
			this.Tabulation.Rectangle.setRectWidth(Tabulation.Rectangle
					.getRectWidth() + ll_Value - value);
			return;
		} else {
			if (this.StopX - this.StartX + 1 == Tabulation.HorNumber) {
				return;
			}
			ll_Other = (int) ((value - ll_Value) / (Tabulation.HorNumber - (this.StopY
					- this.StartX + 1)));
			for (int i = 0; i < ls_Array.length; i++) {
				if (ls_Array[i] != "") {
					if (i > this.StartX && i < this.StopX) {
						ls_Array[i] = Integer.toString((Integer
								.parseInt(ls_Array[i])) + ll_Every);
					} else {
						ls_Array[i] = Integer.toString((Integer
								.parseInt(ls_Array[i])) + ll_Other);
					}
				}
			}
			Tabulation.Rectangle
					.setRectWidth(
							Tabulation.Rectangle.getRectWidth() + ll_Value
									- value);
		}
	}

	/**
	 * 读取单元格的高度
	 */
	public int getCellHeight() {
		int ll_Value = 0;
		String[] ls_Array = Tabulation.VerSpaces.split(";");
		for (int i = this.StartY - 1; i < this.StopY; i++) {
			ll_Value += Integer.parseInt(ls_Array[i]);
		}

		return ll_Value;
	}

	/**
	 * 设置单元格的高度
	 */
	public void setCellHeight(int value) {
		if (value < 1)
			return;

		if (Tabulation.VerSpaceStyle == ETabulationSizeType.SizeByPercent) {
			if (value > 100)
				return;
		}

		String[] ls_Array = Tabulation.VerSpaces.split(";");
		int ll_Value = 0;
		for (int i = this.StartY - 1; i < this.StopY; i++) {
			ll_Value += Integer.parseInt(ls_Array[i]);
		}

		if (ll_Value == value)
			return;

		int ll_Other = 0;
		int ll_Every = (int) ((ll_Value - value) / (this.StopY - this.StartY + 1));
		if (Tabulation.VerSpaceStyle == ETabulationSizeType.SizeByPixels) {
			for (int i = this.StartY - 1; i < this.StopY; i++) {
				ls_Array[i] = Integer.toString((Integer.parseInt(ls_Array[i]))
						- ll_Every);
			}
			String s = "";
			for (String ss : ls_Array) {
				s += ss;
			}
			Tabulation.VerSpaces=s;
			Tabulation.Rectangle.setRectHeight(
					Tabulation.Rectangle.getRectHeight() + ll_Value
							- value);
			return;
		} else {
			if (this.StopY - this.StartY + 1 == Tabulation.VerNumber) {
				return;
			}
			ll_Other = (int) ((value - ll_Value) / (Tabulation.VerNumber - (this.StopY
					- this.StartY + 1)));
			for (int i = 0; i < ls_Array.length; i++) {
				if (ls_Array[i] != "") {
					if (i >= 0 + this.StartY - 1 & i <= 0 + this.StopY - 1) {
						ls_Array[i] = Integer.toString((Integer
								.parseInt(ls_Array[i])) - ll_Every);
					} else {
						ls_Array[i] = Integer.toString((Integer
								.parseInt(ls_Array[i])) - ll_Other);
					}
				}
			}
			Tabulation.Rectangle.setRectHeight(
					Tabulation.Rectangle.getRectHeight() + ll_Value
							- value);
		}
	}

	/**
	 * 读取单元格所处的实际位置的左边距
	 * @throws Exception 
	 */
	public int getCellLeft() throws Exception {
		try {
			String[] ls_Array = null;
			int i = 0;
			int ll_Value = 0;
			ls_Array = Tabulation.HorSpaces.split(";");
			ll_Value = 0;
			for (i = 0; i <= 0 + this.StartX - 2; i++) {
				ll_Value = ll_Value + Integer.parseInt(ls_Array[i]);
			}
			if (Tabulation.HorSpaceStyle == ETabulationSizeType.SizeByPercent) {
				ll_Value = (int) (ll_Value
						* Tabulation.Rectangle.getRectHeight() / 100);
			}
			return Tabulation.getTabLeft() + ll_Value;
		} catch (Exception e) {
			this.Raise(e, "CellLeft Property", null);
		}
		return 0;
	}

	/**
	 * 读取单元格所处的实际位置的上边距
	 * @throws Exception 
	 */
	public int getCellTop() throws Exception {
		try {
			String[] ls_Array = null;
			int i = 0;
			int ll_Value = 0;
			ls_Array = Tabulation.VerSpaces.split(";");
			ll_Value = 0;
			for (i = 0; i <= 0 + this.StartY - 2; i++) {
				ll_Value = ll_Value + Integer.parseInt(ls_Array[i]);
			}
			if (Tabulation.VerSpaceStyle == ETabulationSizeType.SizeByPercent) {
				ll_Value = (int) (ll_Value
						* Tabulation.Rectangle.getRectHeight() / 100);
			}
			return Tabulation.getTabTop() + ll_Value;
		} catch (Exception e) {
			this.Raise(e, "CellTop Property", null);
		}
		return 0;
	}

	/**
	 * 当前对象是否可用
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
		if (ai_Type == 1 && ao_Node.hasAttribute("ID")) {
			this.TabCellID = Integer.parseInt(ao_Node.getAttribute("ID"));
			this.CssClass = ao_Node.getAttribute("CC");
			this.StartX = Integer.parseInt(ao_Node.getAttribute("SX"));
			this.StopX = Integer.parseInt(ao_Node.getAttribute("TX"));
			this.StartY = Integer.parseInt(ao_Node.getAttribute("SY"));
			this.StopY = Integer.parseInt(ao_Node.getAttribute("TY"));
			this.ColorType = Integer.parseInt(ao_Node.getAttribute("CT"));
			this.BorderColor = Integer.parseInt(ao_Node.getAttribute("DC"));
			this.BorderColorLight = Integer.parseInt(ao_Node
					.getAttribute("BCL"));
			this.BorderColorDark = Integer
					.parseInt(ao_Node.getAttribute("BCD"));
			this.BackColor = Integer.parseInt(ao_Node.getAttribute("BC"));
			this.ColSpan = Integer.parseInt(ao_Node.getAttribute("CS"));
			this.RowSpan = Integer.parseInt(ao_Node.getAttribute("RS"));
			this.HorAlign = Integer.parseInt(ao_Node.getAttribute("HA"));
			this.VerAlign = Integer.parseInt(ao_Node.getAttribute("VA"));
			this.TabCellContent = ao_Node.getAttribute("TC");
			this.UniteTabCell = Boolean
					.parseBoolean(ao_Node.getAttribute("UC"));
		} else {
			this.TabCellID = Integer
					.parseInt(ao_Node.getAttribute("TabCellID"));
			this.CssClass = ao_Node.getAttribute("CssClass");
			this.StartX = Integer.parseInt(ao_Node.getAttribute("StartX"));
			this.StopX = Integer.parseInt(ao_Node.getAttribute("StopX"));
			this.StartY = Integer.parseInt(ao_Node.getAttribute("StartY"));
			this.StopY = Integer.parseInt(ao_Node.getAttribute("StopY"));
			this.ColorType = Integer
					.parseInt(ao_Node.getAttribute("ColorType"));
			this.BorderColor = Integer.parseInt(ao_Node
					.getAttribute("BorderColor"));
			this.BorderColorLight = Integer.parseInt(ao_Node
					.getAttribute("BorderColorLight"));
			this.BorderColorDark = Integer.parseInt(ao_Node
					.getAttribute("BorderColorDark"));
			this.BackColor = Integer
					.parseInt(ao_Node.getAttribute("BackColor"));
			this.ColSpan = Integer.parseInt(ao_Node.getAttribute("ColSpan"));
			this.RowSpan = Integer.parseInt(ao_Node.getAttribute("RowSpan"));
			this.HorAlign = Integer.parseInt(ao_Node.getAttribute("HorAlign"));
			this.VerAlign = Integer.parseInt(ao_Node.getAttribute("VerAlign"));
			this.TabCellContent = ao_Node.getAttribute("TabCellContent");
			this.UniteTabCell = Boolean.parseBoolean(ao_Node
					.getAttribute("UniteTabCell"));
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
			ao_Node.setAttribute("ID", Integer.toString(this.TabCellID));
			ao_Node.setAttribute("CC", this.CssClass);
			ao_Node.setAttribute("SX", Integer.toString(this.StartX));
			ao_Node.setAttribute("TX", Integer.toString(this.StopX));
			ao_Node.setAttribute("SY", Integer.toString(this.StartY));
			ao_Node.setAttribute("TY", Integer.toString(this.StopY));
			ao_Node.setAttribute("CT", Integer.toString(this.ColorType));
			ao_Node.setAttribute("DC", Long.toString(this.BorderColor));
			ao_Node.setAttribute("BCL", Long.toString(this.BorderColorLight));
			ao_Node.setAttribute("BCD", Long.toString(this.BorderColorDark));
			ao_Node.setAttribute("BC", Long.toString(this.BackColor));
			ao_Node.setAttribute("CS", Integer.toString(this.ColSpan));
			ao_Node.setAttribute("RS", Integer.toString(this.RowSpan));
			ao_Node.setAttribute("HA", Integer.toString(this.HorAlign));
			ao_Node.setAttribute("VA", Integer.toString(this.VerAlign));
			ao_Node.setAttribute("TC", this.TabCellContent);
			ao_Node.setAttribute("UC", Boolean.toString(this.UniteTabCell));
		} else {
			ao_Node.setAttribute("TabCellID", Integer.toString(this.TabCellID));
			ao_Node.setAttribute("CssClass", this.CssClass);
			ao_Node.setAttribute("StartX", Integer.toString(this.StartX));
			ao_Node.setAttribute("StopX", Integer.toString(this.StopX));
			ao_Node.setAttribute("StartY", Integer.toString(this.StartY));
			ao_Node.setAttribute("StopY", Integer.toString(this.StopY));
			ao_Node.setAttribute("ColorType", Integer.toString(this.ColorType));
			ao_Node.setAttribute("BorderColor", Long.toString(this.BorderColor));
			ao_Node.setAttribute("BorderColorLight",
					Long.toString(this.BorderColorLight));
			ao_Node.setAttribute("BorderColorDark",
					Long.toString(this.BorderColorDark));
			ao_Node.setAttribute("BackColor", Long.toString(this.BackColor));
			ao_Node.setAttribute("ColSpan", Integer.toString(this.ColSpan));
			ao_Node.setAttribute("RowSpan", Integer.toString(this.RowSpan));
			ao_Node.setAttribute("HorAlign", Integer.toString(this.HorAlign));
			ao_Node.setAttribute("VerAlign", Integer.toString(this.VerAlign));
			ao_Node.setAttribute("TabCellContent", this.TabCellContent);
			ao_Node.setAttribute("UniteTabCell",
					Boolean.toString(this.UniteTabCell));
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
			ao_Bag.Write("ID", this.TabCellID);
			ao_Bag.Write("CC", this.CssClass);
			ao_Bag.Write("SX", this.StartX);
			ao_Bag.Write("TX", this.StopX);
			ao_Bag.Write("SY", this.StartY);
			ao_Bag.Write("TY", this.StopY);
			ao_Bag.Write("CT", this.ColorType);
			ao_Bag.Write("DC", this.BorderColor);
			ao_Bag.Write("BCL", this.BorderColorLight);
			ao_Bag.Write("BCD", this.BorderColorDark);
			ao_Bag.Write("BC", this.BackColor);
			ao_Bag.Write("CS", this.ColSpan);
			ao_Bag.Write("RS", this.RowSpan);
			ao_Bag.Write("HA", this.HorAlign);
			ao_Bag.Write("VA", this.VerAlign);
			ao_Bag.Write("TC", this.TabCellContent);
			ao_Bag.Write("UC", this.UniteTabCell);
		} else {
			ao_Bag.Write("ml_TabCellID", this.TabCellID);
			ao_Bag.Write("ms_CssClass", this.CssClass);
			ao_Bag.Write("mi_StartX", this.StartX);
			ao_Bag.Write("mi_StopX", this.StopX);
			ao_Bag.Write("mi_StartY", this.StartY);
			ao_Bag.Write("mi_StopY", this.StopY);
			ao_Bag.Write("mi_ColorType", this.ColorType);
			ao_Bag.Write("ml_BorderColor", this.BorderColor);
			ao_Bag.Write("ml_BorderColorLight", this.BorderColorLight);
			ao_Bag.Write("ml_BorderColorDark", this.BorderColorDark);
			ao_Bag.Write("ml_BackColor", this.BackColor);
			ao_Bag.Write("mi_ColSpan", this.ColSpan);
			ao_Bag.Write("mi_RowSpan", this.RowSpan);
			ao_Bag.Write("mi_HorAlign", this.HorAlign);
			ao_Bag.Write("mi_VerAlign", this.VerAlign);
			ao_Bag.Write("ms_TabCellContent", this.TabCellContent);
			ao_Bag.Write("mb_UniteTabCell", this.UniteTabCell);
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
			this.TabCellID = ao_Bag.ReadInt("ID");
			this.CssClass = ao_Bag.ReadString("CC");
			this.StartX = ao_Bag.ReadInt("SX");
			this.StopX = ao_Bag.ReadInt("TX");
			this.StartY = ao_Bag.ReadInt("SY");
			this.StopY = ao_Bag.ReadInt("TY");
			this.ColorType = ao_Bag.ReadInt("CT");
			this.BorderColor = ao_Bag.ReadInt("DC");
			this.BorderColorLight = ao_Bag.ReadInt("BCL");
			this.BorderColorDark = ao_Bag.ReadInt("BCD");
			this.BackColor = ao_Bag.ReadInt("BC");
			this.ColSpan = ao_Bag.ReadInt("CS");
			this.RowSpan = ao_Bag.ReadInt("RS");
			this.HorAlign = ao_Bag.ReadInt("HA");
			this.VerAlign = ao_Bag.ReadInt("VA");
			this.TabCellContent = ao_Bag.ReadString("TC");
			this.UniteTabCell = ao_Bag.ReadBoolean("UC");
		} else {
			this.TabCellID = ao_Bag.ReadInt("ml_TabCellID");
			this.CssClass = ao_Bag.ReadString("ms_CssClass");
			this.StartX = ao_Bag.ReadInt("mi_StartX");
			this.StopX = ao_Bag.ReadInt("mi_StopX");
			this.StartY = ao_Bag.ReadInt("mi_StartY");
			this.StopY = ao_Bag.ReadInt("mi_StopY");
			this.ColorType = ao_Bag.ReadInt("mi_ColorType");
			this.BorderColor = ao_Bag.ReadInt("ml_BorderColor");
			this.BorderColorLight = ao_Bag.ReadInt("ml_BorderColorLight");
			this.BorderColorDark = ao_Bag.ReadInt("ml_BorderColorDark");
			this.BackColor = ao_Bag.ReadInt("ml_BackColor");
			this.ColSpan = ao_Bag.ReadInt("mi_ColSpan");
			this.RowSpan = ao_Bag.ReadInt("mi_RowSpan");
			this.HorAlign = ao_Bag.ReadInt("mi_HorAlign");
			this.VerAlign = ao_Bag.ReadInt("mi_VerAlign");
			this.TabCellContent = ao_Bag.ReadString("ms_TabCellContent");
			this.UniteTabCell = ao_Bag.ReadBoolean("mb_UniteTabCell");
		}
	}

}
