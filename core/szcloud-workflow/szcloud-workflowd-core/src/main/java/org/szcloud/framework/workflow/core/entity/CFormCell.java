package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.EBorderStyle;
import org.szcloud.framework.workflow.core.emun.ECellAppStyle;
import org.szcloud.framework.workflow.core.emun.EFormCellAccessType;
import org.szcloud.framework.workflow.core.emun.EFormCellStyle;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 表单图元对象：是组成公文数据表单对象的元素对象,它包括如下： 标题、文本输入框、下拉列表、日期输入框、带格式文本输入框、图象处理、
 * 签名、批示输入、数据表单等等
 * 
 * @author Jackie.Wang
 */
public class CFormCell extends CBase {

	/**
	 * 初始化
	 */
	public CFormCell() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CFormCell(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 常量定义
	// #==========================================================================#
	/**
	 * 横向间距
	 */
	public static final int constHorSpace = 2;
	/**
	 * 纵向间距
	 */
	public static final int constVerSpace = 2;
	/**
	 * 空格的大小
	 */
	public static final int constOneBlankSpace = 2;
	/**
	 * 换行符号的高度
	 */
	public static final int constOneLineSpace = 16;
	/**
	 * 两行之间存在控件时的高度
	 */
	public static final int constTwoLineSpace = 2;

	/**
	 * 
	 * @return
	 */
	public CTabCell getParentTabCell() {
		for (CTabulation lo_Tabulation : this.Form.Tabulations) {
			for (CTabCell lo_TabCell : lo_Tabulation.TabCells) {
				if (lo_TabCell.TabCellContent
						.indexOf("C" + (this.CellID) + ";") + 1 > 0) {
					return lo_TabCell;
				}
			}
		}
		return new CTabCell();
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的表单定义
	 */
	public CForm Form = null;

	/**
	 * 表格图元属性集合
	 */
	public List<CCellProp> CellProps = new ArrayList<CCellProp>();

	/**
	 * 根据标识获取表格图元属性
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CCellProp getCellPropById(int al_Id) {
		for (CCellProp lo_Prop : this.CellProps) {
			if (lo_Prop.PropID == al_Id) {
				return lo_Prop;
			}
		}
		return null;
	}

	/**
	 * 表单图元位置
	 */
	private CRectangle mo_Rectangle = new CRectangle();

	/**
	 * 表单图元位置
	 * 
	 * @return
	 */
	public CRectangle getRectangle() {
		return mo_Rectangle;
	}

	/**
	 * 表单图元位置
	 * 
	 * @param value
	 */
	public void setRectangle(CRectangle value) {
		mo_Rectangle = value;
	}

	/**
	 * 当表单图元类型为表单控件时的数据源，只用于内存数据操作使用
	 */
	private Map<String, Object>  mo_DataSource;

	/**
	 * 当表单图元类型为表单控件时的数据源，只用于内存数据操作使用
	 * 
	 * @return
	 */
	public Map<String, Object> getDataSource() {
		if (this.CellStyle != EFormCellStyle.DataGridStyle) {
			return null;
		}
		if (Integer.parseInt(getCellPropByCode("SourceType").getInitValue()) == 0) {
			return mo_DataSource;
		} else {
			return getBindProp().getDataSource();
		}
	}

	/**
	 * 当表单图元类型为表单控件时的数据源，只用于内存数据操作使用
	 * 
	 * @param value
	 */
	public void setDataSource(Map<String, Object> value) {
		if (this.CellStyle != EFormCellStyle.DataGridStyle) {
			return;
		}
		if (Integer.parseInt(getCellPropByCode("SourceType").getInitValue()) == 0) {
			mo_DataSource = value;
		} else {
			getBindProp().setDataSource(value);
		}
	}

	/**
	 * 表单图元字体
	 */
	private CFont mo_Font = new CFont();

	/**
	 * 表单图元字体
	 * 
	 * @return
	 */
	public CFont getFont() {
		return mo_Font;
	}

	/**
	 * 表单图元字体
	 * 
	 * @param value
	 */
	public void setFont(CFont value) {
		mo_Font = value;
	}

	/**
	 * DataGrid类型时表格处理对象
	 */
	private CDBGrid mo_DBGrid = null;

	/**
	 * DataGrid类型时表格处理对象
	 * 
	 * @return
	 */
	public CDBGrid getDBGrid() {
		return mo_DBGrid;
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 表单图元标识
	 */
	public int CellID = -1;

	/**
	 * 读取表格图元代码
	 */
	public String getCellCode() {
		// CellCode = StyleName & CStr(this.CellID)
		return ("Form" + Integer.toString(this.Form.FormID) + "Cell" + Integer
				.toString(this.CellID));
	}

	/**
	 * 表单图元名称
	 */
	public String CellName = "";

	/**
	 * CSS Class定义
	 */
	public String CssClass = "";

	/**
	 * 表单图元类型
	 */
	public int CellStyle = EFormCellStyle.NotAnyStyle;;

	/**
	 * 设置表单图元类型
	 * 
	 * @param value
	 * @throws SQLException
	 */
	public void setCellStyle(int value) {
		try {
			if ((this.CellStyle != EFormCellStyle.NotAnyStyle)
					|| (value == EFormCellStyle.NotAnyStyle)) {
				return;
			}
			this.CellStyle = value;

			String ls_Sql = "SELECT * FROM CellProperty WHERE CellID IN (SELECT CellID FROM CellType WHERE CellType = "
					+ String.valueOf(this.CellStyle) + ")";
			List<Map<String, Object>> lo_Set = CSqlHandle.getJdbcTemplate()
					.queryForList(ls_Sql);
			for (int i = 0; i < lo_Set.size(); i++) {
				CCellProp lo_Prop = insertCellProp((String) lo_Set.get(i).get(
						"PropCode"));
				lo_Prop.setPropName((String) lo_Set.get(i).get("PropName"));
				lo_Prop.setPropType((Integer) lo_Set.get(i).get("PropType"));
				lo_Prop.setPropDataType((Integer) lo_Set.get(i).get("DataType"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 表单图元背景色
	 */
	public int BackColor = 0xFFFFFF;

	/**
	 * 表单图元前景色
	 */
	public int ForeColor = 0x000000;

	/**
	 * 表单图元边框
	 */
	public int BorderStyle = EBorderStyle.NoBorderStyle;

	/**
	 * 读取对应的表头属性
	 */
	public CProp getBindProp() {
		CCellProp lo_CellProp = getCellPropByCode("PropName");
		if (lo_CellProp == null)
			return null;

		String ls_Name = lo_CellProp.getInitValue();
		if (ls_Name == null || ls_Name.equals(""))
			return null;

		for (CProp lo_Prop : this.Form.Cyclostyle.Props) {
			if (lo_Prop.PropName.equals(ls_Name))
				return lo_Prop;
		}
		return null;
	}

	/**
	 * 读取对应的角色
	 */
	public CRole getBindRole() {
		if (this.CellStyle != EFormCellStyle.RoleStyle)
			return null;

		String ls_Name = getCellPropByCode("RoleName").getInitValue();
		if (ls_Name == null || ls_Name.equals(""))
			return null;
		for (CRole lo_Role : this.Form.Cyclostyle.Roles) {
			if (lo_Role.RoleName.equals(ls_Name))
				return lo_Role;
		}
		return null;
	}

	/**
	 * 表单图元属性标识的最大值
	 */
	public int CellPropMaxID = 0;;

	/**
	 * 各种应用类型
	 */
	public int CellAppStyle = ECellAppStyle.DefaultCellAppStyle;

	/**
	 * 各种应用类型
	 * 
	 * @return
	 */
	public int getCellAppStyle(int ai_Style) {
		if ((this.CellAppStyle & ai_Style) == ai_Style) {
			return ai_Style;
		} else {
			return ECellAppStyle.DefaultCellAppStyle;
		}
	}

	/**
	 * 各种应用类型
	 * 
	 * @param value
	 */
	public void setCellAppStyle(int ai_Style) {
		setCellAppStyle(ai_Style, true);
	}

	/**
	 * 各种应用类型
	 * 
	 * @param value
	 */
	public void setCellAppStyle(int ai_Style, boolean ab_Value) {
		if (ab_Value) {
			this.CellAppStyle = (this.CellAppStyle | ai_Style);
		} else {
			if ((this.CellAppStyle & ai_Style) == ai_Style)
				this.CellAppStyle -= ai_Style;
		}
	}

	/**
	 * 表单图元显示操作的权限，是内存数据，只有在公文流转是有效
	 */
	public int Access = EFormCellAccessType.FormCellReadOnly;

	/**
	 * 通过代码取图元属性
	 * 
	 * @param as_PropCode
	 * @return
	 */
	public CCellProp getCellPropByCode(String as_PropCode) {
		for (CCellProp lo_Prop : this.CellProps) {
			if (lo_Prop.PropCode.equals(as_PropCode))
				return lo_Prop;
		}
		return null;
	}

	/**
	 * 清除释放对象的内存数据
	 */
	public void ClearUp() {
		try {
			// 所属的表单定义
			this.Form = null;
			// 表格图元属性集合
			if (this.CellProps != null) {
				while (this.CellProps.size() > 0) {
					CCellProp lo_Prop = CellProps.get(0);
					this.CellProps.remove(lo_Prop);
					lo_Prop.ClearUp();
					lo_Prop = null;
				}
				this.CellProps = null;
			}
			// 表单图元位置
			if (!(mo_Rectangle == null)) {
				mo_Rectangle.ClearUp();
				mo_Rectangle = null;
			}
			// 当表单图元类型为表单控件时的数据源，只用于内存数据操作使用
			if (mo_DataSource != null) {
//				mo_DataSource.close();
				mo_DataSource = null;
			}
			// 当表单图元类型为表单控件时的数据源，只用于内存数据操作使用
			if (mo_DataSource != null) {
//				mo_DataSource.close();
				mo_DataSource = null;
			}
			// 表单图元字体
			mo_Font = null;
			// DataGrid类型时表格处理对象
			mo_DBGrid = null;

			super.ClearUp();
		} catch (Exception ex) {
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
		String ls_Msg = "";
		try {
			String ls_RoleValues = "";
			String ls_ActValues = "";

			// 判断是否已经绑定属性
			CCellProp lo_CellProp = getCellPropByCode("PropName");
			if (lo_CellProp != null) {
				CProp lo_Prop = this.Form.Cyclostyle.getPropByName((lo_CellProp
						.getInitValue()));
				lo_Prop = Form.Cyclostyle.getPropByName((lo_CellProp
						.getInitValue()));
				if (lo_Prop == null) {
					if (this.CellStyle == EFormCellStyle.ImageStyle) {// 图像增加判断
						lo_CellProp = getCellPropByCode("ImageType");
						if (lo_CellProp.getInitValue().equals("1")) {
							ls_Msg += MGlobal.addSpace(ai_SpaceLength)
									+ "表格元素“" + this.CellName
									+ "”未设置对应的表头属性或不存在对应的表头属性" + "\n";
						}
						if (lo_CellProp.getInitValue().equals("1")) {
							ls_Msg += MGlobal.addSpace(ai_SpaceLength)
									+ "表格元素“" + this.CellName
									+ "未设置对应的表头属性或不存在对应的表头属性" + "\n";
						}
					} else {
						ls_Msg += MGlobal.addSpace(ai_SpaceLength) + "表格元素“"
								+ this.CellName + "”未设置对应的表头属性或不存在对应的表头属性"
								+ "\n";
					}
				}
			}

			// Add Get Valid Code...
			switch (this.CellStyle) {
			case EFormCellStyle.LabelStyle:
				break;
			// 验证标题来源是否正确
			case EFormCellStyle.EditStyle:
				break;
			// 验证属性是否存在
			case EFormCellStyle.MaskEditStyle:
				break;
			case EFormCellStyle.ButtonStyle:
				break;
			case EFormCellStyle.RadioStyle:
				break;
			case EFormCellStyle.CheckStyle:
				break;
			case EFormCellStyle.ComboStyle:
				break;
			case EFormCellStyle.DTPickerStyle:
				break;
			case EFormCellStyle.CaculateStyle:
				break;
			case EFormCellStyle.RoleStyle:
				lo_CellProp = getCellPropByCode("RoleName");
				CRole lo_Role = this.Form.Cyclostyle.getRoleByName((lo_CellProp
						.getInitValue().toString()));
				if (lo_Role == null) {
					ls_Msg += MGlobal.addSpace(ai_SpaceLength) + "表格元素“"
							+ this.CellName + "”未设置对应的角色或不存在对应的角色" + "\n";
				}
				break;
			case EFormCellStyle.CommentStyle:
			case EFormCellStyle.SignStyle:
			case EFormCellStyle.VoiceStyle:
				lo_CellProp = getCellPropByCode("ActivityNames");
				ls_ActValues = ";" + (lo_CellProp.getInitValue().toString());
				lo_CellProp = getCellPropByCode("RoleNames");
				ls_RoleValues = ";" + (lo_CellProp.getInitValue().toString());
				if ((ls_ActValues + ls_ActValues).equals(";;")) {
					ls_Msg += MGlobal.addSpace(ai_SpaceLength) + "表格元素“"
							+ this.CellName + "”未设置对应的步骤或角色" + "\n";
				} else {
					if (!ls_ActValues.equals(";")) {
						for (CFlow lo_Flow : this.Form.Cyclostyle.Flows) {
							for (CActivity lo_Act : lo_Flow.Activities) {
								ls_ActValues = ls_ActValues.replaceAll(";"
										+ lo_Act.ActivityName + ";", ";");
							}
						}
					}
					if (!ls_RoleValues.equals(";")) {
						for (CRole lo_RoleA : this.Form.Cyclostyle.Roles) {
							ls_RoleValues = ls_RoleValues.replaceAll(";"
									+ lo_RoleA.RoleName + ";", ";");
						}
					}
					if (!(ls_ActValues + ls_RoleValues).equals(";;")) {
						ls_Msg += MGlobal.addSpace(ai_SpaceLength) + "表格元素“"
								+ this.CellName + "”对应的步骤或角色设置不正确" + "\n";
					}
				}
				break;
			case EFormCellStyle.ImageStyle:
				break;

			case EFormCellStyle.LinkPageStyle:
				break;

			case EFormCellStyle.DataGridStyle:
				break;

			case EFormCellStyle.ActiveXStyle:
				break;

			case EFormCellStyle.NotAnyStyle:
				break;

			default:
				break;
			}
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
		}
		return ls_Msg;
	}

	/**
	 * 插入图元属性
	 * 
	 * @param as_PropCode
	 *            属性代码
	 * @return
	 * @throws Exception
	 */
	public CCellProp insertCellProp(String as_PropCode) throws Exception {
		CCellProp lo_Prop = new CCellProp(this.Logon);
		lo_Prop.FormCell = this;
		this.CellPropMaxID++;
		lo_Prop.PropID = this.CellPropMaxID;
		lo_Prop.setPropCode(as_PropCode);
		this.CellProps.add(lo_Prop);
		return lo_Prop;
	}

	/**
	 * 获取控件显示值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDisplayValue() throws Exception {
		String[] ls_Values = null;
		String[] ls_Displays = null;
		String ls_Value = "";
		CProp lo_Prop = new CProp();
		String ls_Display = "";
		int i = 0;
		switch (this.CellStyle) {
		case EFormCellStyle.ComboStyle:
			ls_Values = this.Form.Cyclostyle
					.getValueByContent(
							getCellPropByCode("Values").getInitValue())
					.toString().split(";");
			ls_Displays = Form.Cyclostyle.getValueByContent(
					getCellPropByCode("Captions").getInitValue()).split(";");
			if ((ls_Values.length - 1) == -1) {
				return "";
			}
			if ((ls_Displays.length - 1) == -1) {
				return "";
			}
			if ((ls_Values.length - 1) != (ls_Displays.length - 1)) {
				return "";
			}
			lo_Prop = getBindProp();
			if (lo_Prop == null) {
				ls_Value = getCellPropByCode("Value").getInitValue();
			} else {
				ls_Value = lo_Prop.getValue();
			}
			for (i = 0; i <= (ls_Values.length - 1); i++) {
				if (ls_Values[i] == ls_Value) {
					return ls_Displays[i];
				}
			}
			break;
		case EFormCellStyle.CheckStyle:
			lo_Prop = getBindProp();
			if (lo_Prop == null) {
				ls_Value = getCellPropByCode("Value").getInitValue();
			} else {
				ls_Value = lo_Prop.getValue();
			}
			if (ls_Value == getCellPropByCode("YesValue").getInitValue()) {
				return "[V]" + getCellPropByCode("Caption").getInitValue();
			} else {
				return "[X]" + getCellPropByCode("Caption").getInitValue();
			}
		case EFormCellStyle.RadioStyle:
			ls_Values = this.Form.Cyclostyle.getValueByContent(
					getCellPropByCode("Values").getInitValue()).split(";");
			ls_Displays = this.Form.Cyclostyle.getValueByContent(
					getCellPropByCode("Captions").getInitValue()).split(";");
			if ((ls_Values.length - 1) == -1) {
				return "";
			}
			if ((ls_Displays.length - 1) == -1) {
				return "";
			}
			if ((ls_Values.length - 1) != (ls_Displays.length - 1)) {
				return "";
			}
			lo_Prop = getBindProp();
			if (lo_Prop == null) {
				ls_Value = getCellPropByCode("Value").getInitValue();
			} else {
				ls_Value = lo_Prop.getValue();
			}
			ls_Display = "";
			for (i = 0; i <= (ls_Values.length - 1); i++) {
				if (i < (ls_Values.length - 1) || ls_Values[i] != "") {
					if (ls_Values[i] == ls_Value) {
						ls_Display = ls_Display + "⊙" + ls_Displays[i] + " ";
					} else {
						ls_Display = ls_Display + "〇" + ls_Displays[i] + " ";
					}
				}
			}
			return ls_Display;
		default:
			return "";
		}
		return "";
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
		// #导入基本信息
		if (ai_Type == 1 && ao_Node.hasAttribute("ID")) {// 未使用
			this.CellID = Integer.parseInt(ao_Node.getAttribute("ID"));
			this.CellName = ao_Node.getAttribute("CN");
			this.CssClass = ao_Node.getAttribute("CC");
			this.CellStyle = Integer.parseInt(ao_Node.getAttribute("CS"));
			this.BackColor = Integer.parseInt(ao_Node.getAttribute("BC"));
			this.ForeColor = Integer.parseInt(ao_Node.getAttribute("FC"));
			this.BorderStyle = Integer.parseInt(ao_Node.getAttribute("BS"));
			this.CellPropMaxID = Integer.parseInt(ao_Node.getAttribute("PM"));
			this.CellAppStyle = Integer.parseInt(ao_Node.getAttribute("CA"));
			if (!ao_Node.getAttribute("AC").equals(""))
				this.Access = Integer.parseInt(ao_Node.getAttribute("AC"));
		} else {
			this.CellID = Integer.parseInt(ao_Node.getAttribute("CellID"));
			this.CellName = ao_Node.getAttribute("CellName");
			this.CssClass = ao_Node.getAttribute("CssClass");
			this.CellStyle = Integer
					.parseInt(ao_Node.getAttribute("CellStyle"));
			this.BackColor = Integer
					.parseInt(ao_Node.getAttribute("BackColor"));
			this.ForeColor = Integer
					.parseInt(ao_Node.getAttribute("ForeColor"));
			this.BorderStyle = Integer.parseInt(ao_Node
					.getAttribute("BorderStyle"));
			this.CellPropMaxID = Integer.parseInt(ao_Node
					.getAttribute("CellPropMaxID"));
			this.CellAppStyle = Integer.parseInt(ao_Node
					.getAttribute("CellAppStyle"));
			if (!ao_Node.getAttribute("Access").equals(""))
				this.Access = Integer.parseInt(ao_Node.getAttribute("Access"));
		}

		// #导入字体
		Element lo_Node = MXmlHandle.getNodeByName(ao_Node, "Font");
		mo_Font.Import(lo_Node, ai_Type);

		// #导入位置
		lo_Node = MXmlHandle.getNodeByName(ao_Node, "Rectangle");
		mo_Rectangle.Import(lo_Node, ai_Type);

		// #导入属性
		NodeList lo_List = ao_Node.getElementsByTagName("Prop");
		for (int i = 0; i < lo_List.getLength(); i++) {
			lo_Node = (Element) lo_List.item(i);
			CCellProp lo_CellProp = new CCellProp(this.Logon);
			lo_CellProp.FormCell = this;
			lo_CellProp.Import(lo_Node, ai_Type);
			this.CellProps.add(lo_CellProp);
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
			ao_Node.setAttribute("ID", Integer.toString(this.CellID));
			ao_Node.setAttribute("CN", this.CellName); // 避免取空值
			ao_Node.setAttribute("CC", this.CssClass);
			ao_Node.setAttribute("CS", Integer.toString(this.CellStyle));
			ao_Node.setAttribute("BC", Long.toString(this.BackColor));
			ao_Node.setAttribute("FC", Long.toString(this.ForeColor));
			ao_Node.setAttribute("BS", Integer.toString(this.BorderStyle));
			ao_Node.setAttribute("PM", Integer.toString(this.CellPropMaxID));
			ao_Node.setAttribute("CA", Integer.toString(this.CellAppStyle));
			ao_Node.setAttribute("AC", Integer.toString(this.Access));
		} else {
			ao_Node.setAttribute("CellID", Integer.toString(this.CellID));
			ao_Node.setAttribute("CellName", this.CellName); // 避免取空值
			ao_Node.setAttribute("CssClass", this.CssClass);
			ao_Node.setAttribute("CellStyle", Integer.toString(this.CellStyle));
			ao_Node.setAttribute("BackColor", Long.toString(this.BackColor));
			ao_Node.setAttribute("ForeColor", Long.toString(this.ForeColor));
			ao_Node.setAttribute("BorderStyle",
					Integer.toString(this.BorderStyle));
			ao_Node.setAttribute("CellPropMaxID",
					Integer.toString(this.CellPropMaxID));
			ao_Node.setAttribute("CellAppStyle",
					Integer.toString(this.CellAppStyle));
			ao_Node.setAttribute("Access", Integer.toString(this.Access));
		}
		// #导出字体
		Element lo_Node = ao_Node.getOwnerDocument().createElement("Font");
		ao_Node.appendChild(lo_Node);
		mo_Font.Export(lo_Node, ai_Type);

		// #导出位置
		lo_Node = ao_Node.getOwnerDocument().createElement("Rectangle");
		ao_Node.appendChild(lo_Node);
		mo_Rectangle.Export(lo_Node, ai_Type);

		// #导出属性
		for (CCellProp lo_CellProp : this.CellProps) {
			lo_Node = ao_Node.getOwnerDocument().createElement("Prop");
			ao_Node.appendChild(lo_Node);
			lo_CellProp.Export(lo_Node, ai_Type);
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
		// #导出基本信息
		if (ai_Type == 1) {
			ao_Bag.Write("ID", this.CellID);
			ao_Bag.Write("CN", this.CellName); // 避免取空值
			ao_Bag.Write("CC", this.CssClass);
			ao_Bag.Write("CS", this.CellStyle);
			ao_Bag.Write("BC", this.BackColor);
			ao_Bag.Write("FC", this.ForeColor);
			ao_Bag.Write("BS", this.BorderStyle);
			ao_Bag.Write("PM", this.CellPropMaxID);
			ao_Bag.Write("CA", this.CellAppStyle);
		} else {
			ao_Bag.Write("ml_CellID", this.CellID);
			ao_Bag.Write("ms_CellName", this.CellName); // 避免取空值
			ao_Bag.Write("ms_CssClass", this.CssClass);
			ao_Bag.Write("mi_CellStyle", this.CellStyle);
			ao_Bag.Write("ml_BackColor", this.BackColor);
			ao_Bag.Write("ml_ForeColor", this.ForeColor);
			ao_Bag.Write("mi_BorderStyle", this.BorderStyle);
			ao_Bag.Write("ml_CellPropMaxID", this.CellPropMaxID);
			ao_Bag.Write("mi_CellAppStyle", this.CellAppStyle);
		}

		// #导出字体
		mo_Font.Write(ao_Bag, ai_Type);

		// #导出位置
		this.mo_Rectangle.Write(ao_Bag, ai_Type);

		// #导出属性
		if (ai_Type == 1) {
			ao_Bag.Write("CPC", this.CellProps.size());
		} else {
			ao_Bag.Write("CellPropsCount", CellProps.size());
		}
		for (CCellProp lo_CellProp : this.CellProps) {
			lo_CellProp.Write(ao_Bag, ai_Type);
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
			// #导入基本信息
			this.CellID = ao_Bag.ReadInt("ID");
			this.CellName = ao_Bag.ReadString("CN");
			this.CssClass = ao_Bag.ReadString("CC");
			this.CellStyle = ao_Bag.ReadInt("CS");
			this.BackColor = ao_Bag.ReadInt("BC");
			this.ForeColor = ao_Bag.ReadInt("FC");
			this.BorderStyle = ao_Bag.ReadInt("BS");
			this.CellPropMaxID = ao_Bag.ReadInt("PM");
			this.CellAppStyle = ao_Bag.ReadInt("CA");
		} else {
			// #导入基本信息
			this.CellID = ao_Bag.ReadInt("ml_CellID");
			this.CellName = ao_Bag.ReadString("ms_CellName");
			this.CssClass = ao_Bag.ReadString("ms_CssClass");
			this.CellStyle = ao_Bag.ReadInt("mi_CellStyle");
			this.BackColor = ao_Bag.ReadInt("ml_BackColor");
			this.ForeColor = ao_Bag.ReadInt("ml_ForeColor");
			this.BorderStyle = ao_Bag.ReadInt("mi_BorderStyle");
			this.CellPropMaxID = ao_Bag.ReadInt("ml_CellPropMaxID");
			this.CellAppStyle = ao_Bag.ReadInt("mi_CellAppStyle");
		}
		// #导入字体
		mo_Font.Read(ao_Bag, ai_Type);

		// #导入位置
		mo_Rectangle.Read(ao_Bag, ai_Type);

		// #导入属性
		int li_Count = 0;
		if (ai_Type == 1) {
			li_Count = ao_Bag.ReadInt("CPC");
		} else {
			li_Count = ao_Bag.ReadInt("CellPropsCount");
		}

		for (int i = 1; i <= li_Count; i++) {
			CCellProp lo_CellProp = new CCellProp(this.Logon);
			lo_CellProp.FormCell = this;
			lo_CellProp.Read(ao_Bag, ai_Type);
			CellProps.add(lo_CellProp);
		}
	}

}
