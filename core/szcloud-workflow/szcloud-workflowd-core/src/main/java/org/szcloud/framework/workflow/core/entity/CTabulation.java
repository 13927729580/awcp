package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.ETabCellAlignType;
import org.szcloud.framework.workflow.core.emun.ETabulationSizeType;
import org.szcloud.framework.workflow.core.emun.ETabulationType;
import org.szcloud.framework.workflow.core.emun.EUseColorType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Web端显示表格对象
 * 
 * @author Jackie.Wang
 * 
 */
public class CTabulation extends CBase {

	/**
	 * 初始化
	 */
	public CTabulation() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CTabulation(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 所属的表单定义
		this.Form = null;
		// 表单表格位置
		if (this.Rectangle != null) {
			this.Rectangle.ClearUp();
			this.Rectangle = null;
			// mo_Rectangle.Risize += mo_Rectangle_Risize;
		}
		// 表单单元集合
		if (this.TabCells != null) {
			while (this.TabCells.size() > 0) {
				CTabCell lo_TabCell = this.TabCells.get(0);
				this.TabCells.remove(lo_TabCell);
				lo_TabCell.ClearUp();
				lo_TabCell = null;
			}
			this.TabCells = null;
		}
		super.ClearUp();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的表单定义
	 */
	public CForm Form = null;

	/**
	 * 表单表格位置
	 */
	public CRectangle Rectangle = new CRectangle();

	/**
	 * 表单单元集合
	 */
	public List<CTabCell> TabCells = new ArrayList<CTabCell>();

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * Web端显示表格对象标识
	 */
	public int TabulationID = -1;

	/**
	 * Web端显示表格对象代码
	 */
	public String TabulationCode = "";

	/**
	 * Web端显示表格对象名称
	 */
	public String TabulationName = "";

	/**
	 * Web端显示表格对象类型
	 */
	public int TabulationType = ETabulationType.CommondTabulation;

	/**
	 * CSS Class定义
	 */
	public String CssClass = "";

	/**
	 * 边框设置定义 边框类型，是否存在边框，=0—>不存在;>=1—>存在[数字表示宽度];
	 */
	public int BorderStyle = 1;

	/**
	 * 颜色使用情况
	 */
	public int ColorType = EUseColorType.DefaultUseColor;

	/**
	 * 边框颜色
	 */
	public int BorderColor = EUseColorType.DefaultUseColor;

	/**
	 * 边框高亮颜色
	 */
	public int BorderColorLight = EUseColorType.DefaultUseColor;

	/**
	 * 边框背景颜色
	 */
	public int BorderColorDark = EUseColorType.DefaultUseColor;

	/**
	 * 背景色
	 */
	public int BackColor = EUseColorType.DefaultUseColor;

	/**
	 * 表格各单元横向间距
	 */
	public int CellPadding = 0;

	/**
	 * 表格各单元横向间距
	 */
	public int CellSpacing = 0;

	/**
	 * 表格单元横向数目
	 */
	public int HorNumber = 0;

	/**
	 * 表格单元纵向数目
	 */
	public int VerNumber = 0;

	/**
	 * 表格单元横向间距表示类型：=0 绝对值；=1 百分比；
	 */
	public int HorSpaceStyle = ETabulationSizeType.SizeByPixels;

	/**
	 * 设置表格单元横向间距表示类型：=0 绝对值；=1 百分比；
	 * 
	 * @param value
	 */
	public void setHorSpaceStyle(int value) {
		if (this.HorSpaceStyle == value) {
			return;
		}
		this.VerSpaceStyle = value;
		if (this.VerSpaceStyle == ETabulationSizeType.SizeByPercent) {
			// mo_Rectangle.setRectWidth( (100 *
			// mo_Rectangle.getRectWidth() /
			// Microsoft.VisualBasic.Compatibility.VB6.Support.PixelsToTwipsX(System.Windows.Forms.Screen.PrimaryScreen.Bounds.Width)));
		} else {
			// mo_Rectangle.setRectWidth( (0.01 *
			// mo_Rectangle.getRectWidth() *
			// Microsoft.VisualBasic.Compatibility.VB6.Support.PixelsToTwipsX(System.Windows.Forms.Screen.PrimaryScreen.Bounds.Width)));
		}
	}

	/**
	 * 表格单元纵向间距表示类型
	 */
	public int VerSpaceStyle = 0;

	/**
	 * 设置表格单元纵向间距表示类型
	 * 
	 * @param value
	 */
	public void setVerSpaceStyle(int value) {
		if (this.VerSpaceStyle == value) {
			return;
		}
		this.VerSpaceStyle = value;
		if (this.VerSpaceStyle == ETabulationSizeType.SizeByPercent) {
			// mo_Rectangle.setRectHeight( (100 *
			// mo_Rectangle.getRectHeight() /
			// Microsoft.VisualBasic.Compatibility.VB6.Support.PixelsToTwipsY(System.Windows.Forms.Screen.PrimaryScreen.Bounds.Height)));
		} else {
			// mo_Rectangle.setRectHeight( (0.01 *
			// mo_Rectangle.getRectHeight() *
			// Microsoft.VisualBasic.Compatibility.VB6.Support.PixelsToTwipsY(System.Windows.Forms.Screen.PrimaryScreen.Bounds.Height)))
			// ;
		}
	}

	/**
	 * 表格单元横向间距，使用“;”分隔，如：50;130;...
	 */
	public String HorSpaces = String.valueOf(MGlobal.const16PaperWidth - 100)
			+ ";";

	/**
	 * 表格单元纵向间距，使用“;”分隔，如：50;130;...
	 */
	public String VerSpaces = String.valueOf(MGlobal.const16PaperHeight - 100)
			+ ";";

	/**
	 * 读取所属的表格单元
	 * 
	 * @return
	 */
	public CTabCell getParentTabCell() {
		if (this.Form == null)
			return null;

		for (CTabulation lo_Tabulation : this.Form.Tabulations) {
			if (lo_Tabulation != this) {
				for (CTabCell lo_TabCell : lo_Tabulation.TabCells) {
					if ((";" + lo_TabCell.TabCellContent).indexOf(";T"
							+ Integer.toString(this.TabulationID) + ";") > -1) {
						return lo_TabCell;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 设置所属的表格单元
	 * 
	 * @param value
	 */
	public void setParentTabCell(CTabCell value) {
		CTabCell lo_TabCell = getParentTabCell();
		if (lo_TabCell != null) {
			lo_TabCell.TabCellContent = lo_TabCell.TabCellContent.replaceAll(
					"T" + Integer.toString(this.TabulationID) + ";", "");
		}
		if (value == null) {
			this.TabulationType = ETabulationType.CommondTabulation;
		} else {
			value.TabCellContent = value.TabCellContent + "T"
					+ Integer.toString(this.TabulationID) + ";";
			this.TabulationType = ETabulationType.TabCellTabulation;
		}
	}

	/**
	 * 读取表格元素的实际位置的左边距
	 * @throws Exception 
	 */
	public int getTabLeft() throws Exception {
		try {
			if (this.TabulationType == ETabulationType.TabCellTabulation) {
				return getParentTabCell().getCellLeft();
			}
			if (this.HorAlign == ETabCellAlignType.DefaultAlign
					|| this.HorAlign == ETabCellAlignType.LeftTopAlign) {
				return 0;
			}
			int li_Value = 0;
			if (this.HorSpaceStyle == ETabulationSizeType.SizeByPercent) {
				li_Value = (int) ((1 - 0.01 * this.Rectangle.getRectWidth()) * Form
						.getRectangle().getRectWidth());
			} else {
				li_Value = Form.getRectangle().getRectWidth()
						- this.Rectangle.getRectWidth();
			}
			if (li_Value <= 0) {
				return 0;
			}
			if (this.HorAlign == ETabCellAlignType.CenterAlign) {
				return (li_Value / 2);
			} else {
			}
		} catch (Exception e) {
			this.Raise(e, "TabLeft Property", null);
		}
		return 0;
	}

	/**
	 * 读取表格元素的实际宽度
	 * @throws Exception 
	 */
	public int getTabWidth() throws Exception {
		try {
			return this.Rectangle.getRectWidth();
		} catch (Exception e) {
			this.Raise(e, "TabWidth Property", null);
		}
		return 0;
	}

	/**
	 * 读取表格元素的实际高度
	 * @throws Exception 
	 */
	public int getTabHeight() throws Exception {
		try {
			return this.Rectangle.getRectHeight();
		} catch (Exception e) {
			this.Raise(e, "TabHeight Property", null);
		}
		return 0;
	}

	/**
	 * 读取表格元素的实际位置的上边距
	 * @throws Exception 
	 */
	public int getTabTop() throws Exception {
		try {
			if (this.TabulationType == ETabulationType.TabCellTabulation) {
				return getParentTabCell().getCellTop();
			}
			if (this.TabOrder == 1) {
				return 0;
			}
		} catch (Exception e) {
			this.Raise(e, "TabTop Property", null);
		}
		return 0;
	}

	/**
	 * 表格横向对齐方式
	 */
	public int HorAlign = ETabCellAlignType.DefaultAlign;

	/**
	 * 表格纵向对齐方式
	 */
	public int VerAlign = ETabCellAlignType.DefaultAlign;

	/**
	 * 表格单元横向对齐方式
	 */
	public int HorCellAlign = ETabCellAlignType.DefaultAlign;

	/**
	 * 表格单元纵向对齐方式
	 */
	public int VerCellAlign = ETabCellAlignType.DefaultAlign;

	/**
	 * 表格单元背景颜色
	 */
	public int CellBackColor = 0XFFFFFF;

	/**
	 * 表格单元边框颜色
	 */
	public int CellBorderColor = 0X0;

	/**
	 * 表格单元最大标识
	 */
	public int TabCellMaxID = 0;

	/**
	 * 表格排列顺序
	 */
	public int TabOrder = 0;

	/**
	 * 通过位置读取Web端显示表格单元 ai_HorPos 横坐标 ai_VerPos 纵坐标 ab_GetFromAll 是否从所有中选择
	 * @throws Exception 
	 */
	public CTabCell getTabCellByPosition(int ai_HorPos, int ai_VerPos,
			boolean ab_GetFromAll) throws Exception {
		try {
			if (ai_HorPos < 1 || ai_HorPos > this.HorNumber)
				return null;
			if (ai_VerPos < 1 || ai_VerPos > this.VerNumber)
				return null;

			for (CTabCell lo_TabCell : this.TabCells) {
				if (ab_GetFromAll) {
					if (lo_TabCell.StartX == ai_HorPos
							&& lo_TabCell.StartY == ai_VerPos)
						return lo_TabCell;
				} else {
					if (!lo_TabCell.UniteTabCell) {
						if ((lo_TabCell.StartX <= ai_HorPos & lo_TabCell
								.StopX >= ai_HorPos)
								&& (lo_TabCell.StartY <= ai_VerPos & lo_TabCell
										.StopY >= ai_VerPos))
							return lo_TabCell;
					}
				}
			}
		} catch (Exception e) {
			this.Raise(e, "GetTabCellByPosition", null);
		}
		return null;
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
	 * 移动表格到最后一个
	 */
	public void MoveLast() {
		if (this.TabulationType == ETabulationType.TabCellTabulation)
			return;

		// boolean lb_Boolean = false;
		int li_Value = 0;
		for (CTabulation lo_Tabulation : this.Form.Tabulations) {
			int li_Order = lo_Tabulation.TabOrder;
			if (li_Order > this.TabOrder) {
				if (this.TabOrder > 0) {
					lo_Tabulation.TabOrder = li_Order - 1;
				}
				if (li_Value < li_Order)
					li_Value = li_Order;
				// lb_Boolean = true;
			}
		}
		this.TabOrder = li_Value + 1;
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
		this.TabulationID = Integer.parseInt(ao_Node.getAttribute("ID"));
		if (ai_Type == 1 && ao_Node.hasAttribute("TC")) {
			this.TabulationCode = ao_Node.getAttribute("TC");
			this.TabulationName = ao_Node.getAttribute("TN");
			this.TabulationType = Integer.parseInt(ao_Node.getAttribute("TT"));
			this.CssClass = ao_Node.getAttribute("CC");
			this.BorderStyle = Integer.parseInt(ao_Node.getAttribute("BS"));
			this.ColorType = Integer.parseInt(ao_Node.getAttribute("CT"));
			this.BorderColor = Integer.parseInt(ao_Node.getAttribute("DC"));
			this.BorderColorLight = Integer.parseInt(ao_Node
					.getAttribute("BCL"));
			this.BorderColorDark = Integer
					.parseInt(ao_Node.getAttribute("BCD"));
			this.BackColor = Integer.parseInt(ao_Node.getAttribute("BC"));
			this.CellPadding = Integer.parseInt(ao_Node.getAttribute("CP"));
			this.CellSpacing = Integer.parseInt(ao_Node.getAttribute("CS"));
			this.HorNumber = Integer.parseInt(ao_Node.getAttribute("HN"));
			this.VerNumber = Integer.parseInt(ao_Node.getAttribute("VN"));
			this.HorSpaceStyle = Integer.parseInt(ao_Node.getAttribute("HSS"));
			this.VerSpaceStyle = Integer.parseInt(ao_Node.getAttribute("VSS"));
			this.HorSpaces = ao_Node.getAttribute("HS");
			this.VerSpaces = ao_Node.getAttribute("VS");
			this.HorAlign = Integer.parseInt(ao_Node.getAttribute("HA"));
			this.VerAlign = Integer.parseInt(ao_Node.getAttribute("VA"));
			this.HorCellAlign = Integer.parseInt(ao_Node.getAttribute("HCA"));
			this.VerCellAlign = Integer.parseInt(ao_Node.getAttribute("VCA"));
			this.CellBackColor = Integer.parseInt(ao_Node.getAttribute("CBC"));
			this.CellBorderColor = Integer
					.parseInt(ao_Node.getAttribute("CDC"));
			this.TabOrder = Integer.parseInt(ao_Node.getAttribute("TO"));
		} else {
			this.TabulationCode = ao_Node.getAttribute("Code");
			this.TabulationName = ao_Node.getAttribute("Name");
			this.TabulationType = Integer
					.parseInt(ao_Node.getAttribute("Type"));
			this.CssClass = ao_Node.getAttribute("CssClass");
			this.BorderStyle = Integer.parseInt(ao_Node
					.getAttribute("BorderStyle"));
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
			this.CellPadding = Integer.parseInt(ao_Node
					.getAttribute("CellPadding"));
			this.CellSpacing = Integer.parseInt(ao_Node
					.getAttribute("CellSpacing"));
			this.HorNumber = Integer
					.parseInt(ao_Node.getAttribute("HorNumber"));
			this.VerNumber = Integer
					.parseInt(ao_Node.getAttribute("VerNumber"));
			this.HorSpaceStyle = Integer.parseInt(ao_Node
					.getAttribute("HorSpaceStyle"));
			this.VerSpaceStyle = Integer.parseInt(ao_Node
					.getAttribute("VerSpaceStyle"));
			this.HorSpaces = ao_Node.getAttribute("HorSpaces");
			this.VerSpaces = ao_Node.getAttribute("VerSpaces");
			this.HorAlign = Integer.parseInt(ao_Node.getAttribute("HorAlign"));
			this.VerAlign = Integer.parseInt(ao_Node.getAttribute("VerAlign"));
			this.HorCellAlign = Integer.parseInt(ao_Node
					.getAttribute("HorCellAlign"));
			this.VerCellAlign = Integer.parseInt(ao_Node
					.getAttribute("VerCellAlign"));
			this.CellBackColor = Integer.parseInt(ao_Node
					.getAttribute("CellBackColor"));
			this.CellBorderColor = Integer.parseInt(ao_Node
					.getAttribute("CellBorderColor"));
			this.TabOrder = Integer.parseInt(ao_Node.getAttribute("TabOrder"));
		}

		// 导入表单表格位置
		Element lo_Node = MXmlHandle.getNodeByName(ao_Node, "Rectangle");
		this.Rectangle.Import(lo_Node, ai_Type);

		// 导入表单单元集合
		NodeList lo_List = ao_Node.getElementsByTagName("TabCell");
		for (int i = 0; i < lo_List.getLength(); i++) {
			lo_Node = (Element) lo_List.item(i);
			CTabCell lo_TabCell = new CTabCell(this.Logon);
			lo_TabCell.Tabulation = this;
			lo_TabCell.Import(lo_Node, ai_Type);
			this.TabCells.add(lo_TabCell);
			if (this.TabCellMaxID < lo_TabCell.TabCellID)
				this.TabCellMaxID = lo_TabCell.TabCellID;
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
		ao_Node.setAttribute("ID", Integer.toString(this.TabulationID));
		if (ai_Type == 1) {
			ao_Node.setAttribute("TC", this.TabulationCode);
			ao_Node.setAttribute("TN", this.TabulationName);
			ao_Node.setAttribute("TT", Integer.toString(this.TabulationType));
			ao_Node.setAttribute("CC", this.CssClass);
			ao_Node.setAttribute("BS", Integer.toString(this.BorderStyle));
			ao_Node.setAttribute("CT", Integer.toString(this.ColorType));
			ao_Node.setAttribute("DC", Long.toString(this.BorderColor));
			ao_Node.setAttribute("BCL", Long.toString(this.BorderColorLight));
			ao_Node.setAttribute("BCD", Long.toString(this.BorderColorDark));
			ao_Node.setAttribute("BC", Long.toString(this.BackColor));
			ao_Node.setAttribute("CP", Integer.toString(this.CellPadding));
			ao_Node.setAttribute("CS", Integer.toString(this.CellSpacing));
			ao_Node.setAttribute("HN", Integer.toString(this.HorNumber));
			ao_Node.setAttribute("VN", Integer.toString(this.VerNumber));
			ao_Node.setAttribute("HSS", Integer.toString(this.HorSpaceStyle));
			ao_Node.setAttribute("VSS", Integer.toString(this.VerSpaceStyle));
			ao_Node.setAttribute("HS", this.HorSpaces);
			ao_Node.setAttribute("VS", this.VerSpaces);
			ao_Node.setAttribute("HA", Integer.toString(this.HorAlign));
			ao_Node.setAttribute("VA", Integer.toString(this.VerAlign));
			ao_Node.setAttribute("HCA", Integer.toString(this.HorCellAlign));
			ao_Node.setAttribute("VCA", Integer.toString(this.VerCellAlign));
			ao_Node.setAttribute("CBC", Long.toString(this.CellBackColor));
			ao_Node.setAttribute("CDC", Long.toString(this.CellBorderColor));
			ao_Node.setAttribute("TO", Integer.toString(this.TabOrder));
		} else {
			ao_Node.setAttribute("Code", this.TabulationCode);
			ao_Node.setAttribute("Name", this.TabulationName);
			ao_Node.setAttribute("Type", Integer.toString(this.TabulationType));
			ao_Node.setAttribute("CssClass", this.CssClass);
			ao_Node.setAttribute("BorderStyle",
					Integer.toString(this.BorderStyle));
			ao_Node.setAttribute("ColorType", Integer.toString(this.ColorType));
			ao_Node.setAttribute("BorderColor", Long.toString(this.BorderColor));
			ao_Node.setAttribute("BorderColorLight",
					Long.toString(this.BorderColorLight));
			ao_Node.setAttribute("BorderColorDark",
					Long.toString(this.BorderColorDark));
			ao_Node.setAttribute("BackColor", Long.toString(this.BackColor));
			ao_Node.setAttribute("CellPadding",
					Integer.toString(this.CellPadding));
			ao_Node.setAttribute("CellSpacing",
					Integer.toString(this.CellSpacing));
			ao_Node.setAttribute("HorNumber", Integer.toString(this.HorNumber));
			ao_Node.setAttribute("VerNumber", Integer.toString(this.VerNumber));
			ao_Node.setAttribute("HorSpaceStyle",
					Integer.toString(this.HorSpaceStyle));
			ao_Node.setAttribute("VerSpaceStyle",
					Integer.toString(this.VerSpaceStyle));
			ao_Node.setAttribute("HorSpaces", this.HorSpaces);
			ao_Node.setAttribute("VerSpaces", this.VerSpaces);
			ao_Node.setAttribute("HorAlign", Integer.toString(this.HorAlign));
			ao_Node.setAttribute("VerAlign", Integer.toString(this.VerAlign));
			ao_Node.setAttribute("HorCellAlign",
					Integer.toString(this.HorCellAlign));
			ao_Node.setAttribute("VerCellAlign",
					Integer.toString(this.VerCellAlign));
			ao_Node.setAttribute("CellBackColor",
					Long.toString(this.CellBackColor));
			ao_Node.setAttribute("CellBorderColor",
					Long.toString(this.CellBorderColor));
			ao_Node.setAttribute("TabOrder", Integer.toString(this.TabOrder));
		}

		// 导出表单表格位置
		Element lo_Node = ao_Node.getOwnerDocument().createElement("Rectangle");
		ao_Node.appendChild(lo_Node);
		this.Rectangle.Export(lo_Node, ai_Type);

		// 导出表单单元集合
		for (CTabCell lo_TabCell : this.TabCells) {
			lo_Node = ao_Node.getOwnerDocument().createElement("TabCell");
			ao_Node.appendChild(lo_Node);
			lo_TabCell.Export(lo_Node, ai_Type);
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
			ao_Bag.Write("ID", this.TabulationID);
			ao_Bag.Write("TC", this.TabulationCode);
			ao_Bag.Write("TN", this.TabulationName);
			ao_Bag.Write("TT", this.TabulationType);
			ao_Bag.Write("CC", this.CssClass);
			ao_Bag.Write("BS", this.BorderStyle);
			ao_Bag.Write("CT", this.ColorType);
			ao_Bag.Write("DC", this.BorderColor);
			ao_Bag.Write("BCL", this.BorderColorLight);
			ao_Bag.Write("BCD", this.BorderColorDark);
			ao_Bag.Write("BC", this.BackColor);
			ao_Bag.Write("CP", this.CellPadding);
			ao_Bag.Write("CS", this.CellSpacing);
			ao_Bag.Write("HN", this.HorNumber);
			ao_Bag.Write("VN", this.VerNumber);
			ao_Bag.Write("HSS", this.HorSpaceStyle);
			ao_Bag.Write("VSS", this.VerSpaceStyle);
			ao_Bag.Write("HS", this.HorSpaces);
			ao_Bag.Write("VS", this.VerSpaces);
			ao_Bag.Write("HA", this.HorAlign);
			ao_Bag.Write("VA", this.VerAlign);
			ao_Bag.Write("HCA", this.HorCellAlign);
			ao_Bag.Write("VSA", this.VerCellAlign);
			ao_Bag.Write("CBC", this.CellBackColor);
			ao_Bag.Write("CDC", this.CellBorderColor);
			ao_Bag.Write("TO", this.TabOrder);

			// 导出表单表格位置
			this.Rectangle.Write(ao_Bag, ai_Type);

			// 导出表单单元集合
			ao_Bag.Write("TCC", this.TabCells.size());
			for (CTabCell lo_TabCell : TabCells) {
				lo_TabCell.Write(ao_Bag, ai_Type);
			}
		} else {
			ao_Bag.Write("ml_TabulationID", this.TabulationID);
			ao_Bag.Write("ms_TabulationCode", this.TabulationCode);
			ao_Bag.Write("ms_TabulationName", this.TabulationName);
			ao_Bag.Write("mi_TabulationType", this.TabulationType);
			ao_Bag.Write("ms_CssClass", this.CssClass);
			ao_Bag.Write("mi_BorderStyle", this.BorderStyle);
			ao_Bag.Write("mi_ColorType", this.ColorType);
			ao_Bag.Write("ml_BorderColor", this.BorderColor);
			ao_Bag.Write("ml_BorderColorLight", this.BorderColorLight);
			ao_Bag.Write("ml_BorderColorDark", this.BorderColorDark);
			ao_Bag.Write("ml_BackColor", this.BackColor);
			ao_Bag.Write("mi_CellPadding", this.CellPadding);
			ao_Bag.Write("mi_CellSpacing", this.CellSpacing);
			ao_Bag.Write("mi_HorNumber", this.HorNumber);
			ao_Bag.Write("mi_VerNumber", this.VerNumber);
			ao_Bag.Write("mi_HorSpaceStyle", this.HorSpaceStyle);
			ao_Bag.Write("mi_VerSpaceStyle", this.VerSpaceStyle);
			ao_Bag.Write("ms_HorSpaces", this.HorSpaces);
			ao_Bag.Write("ms_VerSpaces", this.VerSpaces);
			ao_Bag.Write("mi_HorAlign", this.HorAlign);
			ao_Bag.Write("mi_VerAlign", this.VerAlign);
			ao_Bag.Write("mi_HorCellAlign", this.HorCellAlign);
			ao_Bag.Write("mi_VerCellAlign", this.VerCellAlign);
			ao_Bag.Write("ml_CellBackColor", this.CellBackColor);
			ao_Bag.Write("ml_CellBorderColor", this.CellBorderColor);
			ao_Bag.Write("mi_TabOrder", this.TabOrder);

			// 导出表单表格位置
			this.Rectangle.Write(ao_Bag, ai_Type);

			// 导出表单单元集合
			ao_Bag.Write("TabCellsCount", TabCells.size());
			for (CTabCell lo_TabCell : this.TabCells) {
				lo_TabCell.Write(ao_Bag, ai_Type);
			}
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
		int li_Count = 0;
		CTabCell lo_TabCell = new CTabCell();
		if (ai_Type == 1) {
			this.TabulationID = ao_Bag.ReadInt("ID");
			this.TabulationCode = ao_Bag.ReadString("TC");
			this.TabulationName = ao_Bag.ReadString("TN");
			this.TabulationType = ao_Bag.ReadInt("TT");
			this.CssClass = ao_Bag.ReadString("CC");
			this.BorderStyle = ao_Bag.ReadInt("BS");
			this.ColorType = ao_Bag.ReadInt("CT");
			this.BorderColor = ao_Bag.ReadInt("DC");
			this.BorderColorLight = ao_Bag.ReadInt("BCL");
			this.BorderColorDark = ao_Bag.ReadInt("BCD");
			this.BackColor = ao_Bag.ReadInt("BC");
			this.CellPadding = ao_Bag.ReadInt("CP");
			this.CellSpacing = ao_Bag.ReadInt("CS");
			this.HorNumber = ao_Bag.ReadInt("HN");
			this.VerNumber = ao_Bag.ReadInt("VN");
			this.HorSpaceStyle = ao_Bag.ReadInt("HSS");
			this.VerSpaceStyle = ao_Bag.ReadInt("VSS");
			this.HorSpaces = ao_Bag.ReadString("HS");
			this.VerSpaces = ao_Bag.ReadString("VS");
			this.HorAlign = ao_Bag.ReadInt("HA");
			this.VerAlign = ao_Bag.ReadInt("VA");
			this.HorCellAlign = ao_Bag.ReadInt("HCA");
			this.VerCellAlign = ao_Bag.ReadInt("VSA");
			this.CellBackColor = ao_Bag.ReadInt("CBC");
			this.CellBorderColor = ao_Bag.ReadInt("CDC");
			this.TabOrder = ao_Bag.ReadInt("TO");
			// 导入表单表格位置
			this.Rectangle.Read(ao_Bag, ai_Type);

			// 导入表单单元集合
			li_Count = ao_Bag.ReadInt("TCC");
		} else {
			this.TabulationID = ao_Bag.ReadInt("ml_TabulationID");
			this.TabulationCode = ao_Bag.ReadString("ms_TabulationCode");
			this.TabulationName = ao_Bag.ReadString("ms_TabulationName");
			this.TabulationType = ao_Bag.ReadInt("mi_TabulationType");
			this.CssClass = ao_Bag.ReadString("ms_CssClass");
			this.BorderStyle = ao_Bag.ReadInt("mi_BorderStyle");
			this.ColorType = ao_Bag.ReadInt("mi_ColorType");
			this.BorderColor = ao_Bag.ReadInt("ml_BorderColor");
			this.BorderColorLight = ao_Bag.ReadInt("ml_BorderColorLight");
			this.BorderColorDark = ao_Bag.ReadInt("ml_BorderColorDark");
			this.BackColor = ao_Bag.ReadInt("ml_BackColor");
			this.CellPadding = ao_Bag.ReadInt("mi_CellPadding");
			this.CellSpacing = ao_Bag.ReadInt("mi_CellSpacing");
			this.HorNumber = ao_Bag.ReadInt("mi_HorNumber");
			this.VerNumber = ao_Bag.ReadInt("mi_VerNumber");
			this.HorSpaceStyle = ao_Bag.ReadInt("mi_HorSpaceStyle");
			this.VerSpaceStyle = ao_Bag.ReadInt("mi_VerSpaceStyle");
			this.HorSpaces = ao_Bag.ReadString("ms_HorSpaces");
			this.VerSpaces = ao_Bag.ReadString("ms_VerSpaces");
			this.HorAlign = ao_Bag.ReadInt("mi_HorAlign");
			this.VerAlign = ao_Bag.ReadInt("mi_VerAlign");
			this.HorCellAlign = ao_Bag.ReadInt("mi_HorCellAlign");
			this.VerCellAlign = ao_Bag.ReadInt("mi_VerCellAlign");
			this.CellBackColor = ao_Bag.ReadInt("ml_CellBackColor");
			this.CellBorderColor = ao_Bag.ReadInt("ml_CellBorderColor");
			this.TabOrder = ao_Bag.ReadInt("mi_TabOrder");
			// 导入表单表格位置
			this.Rectangle.Read(ao_Bag, ai_Type);

			// 导入表单单元集合
			li_Count = ao_Bag.ReadInt("TabCellsCount");
		}
		for (int i = 1; i <= li_Count; i++) {
			lo_TabCell = new CTabCell();
			lo_TabCell.Tabulation = this;
			lo_TabCell.Read(ao_Bag, ai_Type);
			this.TabCells.add(lo_TabCell);
			if (this.TabCellMaxID < lo_TabCell.TabCellID)
				this.TabCellMaxID = lo_TabCell.TabCellID;
		}
	}

}
