package org.szcloud.framework.workflow.core.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.ECellPropSourceType;
import org.szcloud.framework.workflow.core.emun.EFormCellPropType;
import org.szcloud.framework.workflow.core.emun.EFormCellStyle;
import org.szcloud.framework.workflow.core.emun.EPropDataType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 表单图元属性对象，它反映了TFormCell对象的各种属性，这些对象的 属性在数据库中定义，<br>
 * 并由应用程序生成。
 * 
 * @author Jackie.Wang
 */
public class CCellProp extends CBase {

	/**
	 * 初始化
	 * 
	 */
	public CCellProp() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 */
	public CCellProp(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 所属表单图元对象
	 */
	public CFormCell FormCell = null;

	/**
	 * 属性标识
	 */
	public int PropID = 0;

	/**
	 * 属性代码
	 */
	public String PropCode = null;

	/**
	 * 属性代码
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setPropCode(String value) throws Exception {
		if (value == null || value.equals("")) {
			// #错误[1106]：表格单元的属性代码不能为空
			this.Raise(1106, "setPropCode", "");
			return;
		}

		if (this.PropCode == value)
			return;

		for (CCellProp lo_CellProp : this.FormCell.CellProps) {
			if (lo_CellProp.PropCode.equals(value)) {
				// #错误[1107]：表格单元的属性代码不能重复
				this.Raise(1107, "setPropCode", value);
				return;
			}
		}

		this.PropCode = value;
	}

	/**
	 * 属性名称
	 */
	public String PropName = null;

	/**
	 * 设置属性名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public void setPropName(String value) throws Exception {
		if (value == null || value.equals("")) {
			// #错误[1089]：表格单元的属性名称不能为空
			this.Raise(1089, "setPropName", "");
			return;
		}

		if (this.PropName.equals(value))
			return;

		for (CCellProp lo_CellProp : this.FormCell.CellProps) {
			if (lo_CellProp.PropName.equals(value)) {
				// #错误[1089]：表格单元的属性名称不能重复
				this.Raise(1089, "setPropName", "");
				return;
			}
		}

		this.PropName = value;
	}

	/**
	 * 属性初始值，保存属性的初始内容，如果是mi_PropType=CellWriteProp，则表示对应的属性名称
	 */
	public String InitValue = null;

	/**
	 * 读取属性初始值
	 * 
	 * @return
	 */
	public String getInitValue() {
		Date ld_Date = new Date();
		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			return this.InitValue;
		case EPropDataType.IntegerDataType:
			return this.InitValue;
		case EPropDataType.LongDataType:
			return this.InitValue;
		case EPropDataType.SingleDataType:
			return this.InitValue;
		case EPropDataType.DoubleDataType:
			return this.InitValue;
		case EPropDataType.DateDataType:
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			if (this.InitValue != null) {
				return fmt.format(this.InitValue);
			} else {
				return fmt.format(ld_Date);
			}
		default: // StringDataType, ImageDataType
			return this.InitValue;
		}
	}

	/**
	 * 设置属性初始值
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setInitValue(String value) throws Exception {
		try {
			Date ld_Date = new Date();
			switch (this.PropDataType) {
			case EPropDataType.BooleanDataType:
				this.InitValue = value;
				break;
			case EPropDataType.IntegerDataType:
				this.InitValue = value;
				break;
			case EPropDataType.LongDataType:
				this.InitValue = value;
				break;
			case EPropDataType.SingleDataType:
				this.InitValue = value;
				break;
			case EPropDataType.DoubleDataType:
				this.InitValue = value;
				break;
			case EPropDataType.DateDataType:
				if (ld_Date.toString() == value) {
					this.InitValue = "";
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"YYYY/MM/DD hh:mm:ss");
					this.InitValue = sdf.format(value);
				}
				break;
			default: // StringDataType, ImageDataType
				this.InitValue = value.toString();
				break;
			}
		} catch (Exception e) {
			this.Raise(e, "InitValue Property", null);
		}
	}

	/**
	 * 属性值数据类型
	 */
	public int PropDataType = EPropDataType.NotAnyDataType;

	/**
	 * 属性值数据类型
	 * 
	 * @param value
	 */
	public void setPropDataType(int value) {
		if (this.PropDataType == value)
			return;
		this.PropDataType = value;

		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			this.InitValue = "False";
			break;
		case EPropDataType.IntegerDataType:
			this.InitValue = "0";
			break;
		case EPropDataType.LongDataType:
			this.InitValue = "0";
			break;
		case EPropDataType.SingleDataType:
			this.InitValue = "0";
			break;
		case EPropDataType.DoubleDataType:
			this.InitValue = "0";
			break;
		case EPropDataType.DateDataType:
			Date ld_Date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/DD hh:mm:ss");
			this.InitValue = sdf.format(ld_Date);
			break;
		default: // StringDataType, ImageDataType - 不用变化
			break;
		}
	}

	/**
	 * 属性类型
	 */
	public int PropType = EFormCellPropType.CellCtrlEvent;

	/**
	 * 设置属性类型
	 * 
	 * @param value
	 */
	public void setPropType(int value) {
		this.PropType = value;
		this.SourceType = ECellPropSourceType.ChangelessValue;
	}

	/**
	 * 属性来源类型
	 */
	public int SourceType = ECellPropSourceType.ChangelessValue;

	/**
	 * 设置属性来源类型
	 * 
	 * @param value
	 */
	public void setSourceType(int value) {
		switch (this.PropType) {
		case EFormCellPropType.CellCtrlReadProp:
		case EFormCellPropType.CellCtrlEvent:
		case EFormCellPropType.CellCtrlMethod:
			this.SourceType = (value == ECellPropSourceType.ComeFromDataProp ? ECellPropSourceType.ChangelessValue
					: value);
			break;
		case EFormCellPropType.CellWriteProp:
			this.SourceType = ECellPropSourceType.ComeFromDataProp;
			break;
		case EFormCellPropType.CtrlCellReadWrite:
		case EFormCellPropType.OtherCellProp:
			this.SourceType = ECellPropSourceType.ChangelessValue;
			break;
		}
	}

	// 以下内容定义动作变化事件的事件
	/**
	 * 事件名称
	 */
	public String EventTitle = null;

	/**
	 * 读取事件名称
	 * 
	 * @return
	 */
	public String getEventTitle() {
		if (this.PropType == EFormCellPropType.CellCtrlEvent) {
			if (MGlobal.isEmpty(this.EventTitle))
				return this.FormCell.getCellCode() + "_" + this.PropCode + "()";
			else
				return this.EventTitle;
		} else {
			return "";
		}
	}

	/**
	 * 设置事件名称
	 * 
	 * @param value
	 */
	public void setEventTitle(String value) {
		if (this.PropType == EFormCellPropType.CellCtrlEvent)
			this.EventTitle = value;
	}

	/**
	 * 事件内容
	 */
	public String EventContent = null;

	/**
	 * 设置事件内容
	 * 
	 * @param value
	 */
	public void setEventContent(String value) {
		if (this.PropType == EFormCellPropType.CellCtrlEvent)
			this.EventContent = value;
	}

	/**
	 * 事件描述
	 */
	public String EventSummary = null;

	/**
	 * 设置事件描述
	 * 
	 * @param value
	 */
	public void setEventSummary(String value) {
		if (this.PropType == EFormCellPropType.CellCtrlEvent)
			this.EventSummary = value;
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		this.FormCell = null;
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
			String ls_Msg = "";
			return ls_Msg;
		} catch (Exception e) {
			this.Raise(e, "IsValid", "");
			return e.toString();
		}
	}

	/**
	 * 导出对象打包内容-XML
	 * 
	 * @param ao_Node
	 * @return
	 * @throws Exception
	 */
	public boolean ExportB(Document ao_Node) throws Exception {
		try {
			this.exportEventXml(); // 导出事件内容
			ao_Node.getDocumentElement().setAttribute("ID",
					Integer.toString(this.PropID));
			ao_Node.getDocumentElement().setAttribute("Code", this.PropCode);
			ao_Node.getDocumentElement().setAttribute("Name", this.PropName);
			ao_Node.getDocumentElement().setAttribute("InitValue",
					this.InitValue);
			ao_Node.getDocumentElement().setAttribute("DataType",
					Integer.toString(this.PropDataType));
			ao_Node.getDocumentElement().setAttribute("Type",
					Integer.toString(this.PropType));
			ao_Node.getDocumentElement().setAttribute("SourceType",
					Integer.toString(this.SourceType));

			return true;
		} catch (Exception e) {
			this.Raise(e, "ExportB", null);
		}
		return false;
	}

	/**
	 * 导出事件内容
	 */
	private void exportEventXml() {
		if (this.PropType == EFormCellPropType.CellCtrlEvent) {
			this.InitValue = "<Event Title=\""
					+ MGlobal.stringToXml(this.EventTitle) + "\""
					+ " Content=\"" + MGlobal.stringToXml(this.EventContent)
					+ "\"" + " Summary=\""
					+ MGlobal.stringToXml(this.EventSummary) + "\" />";
		}
	}

	/**
	 * 导入事件内容
	 */
	private void importEventXml() {
		if (this.PropType != EFormCellPropType.CellCtrlEvent)
			return;

		Document lo_Xml = MXmlHandle.LoadXml(this.InitValue);
		if (lo_Xml != null) {
			this.EventTitle = lo_Xml.getDocumentElement().getAttribute("Title");
			this.EventContent = lo_Xml.getDocumentElement().getAttribute(
					"Content");
			this.EventSummary = lo_Xml.getDocumentElement().getAttribute(
					"Summary");
			lo_Xml = null;
		} else {
			this.EventTitle = "";
			this.EventContent = "";
			this.EventSummary = "";
		}
	}

	/**
	 * 属性描述
	 */
	public String PropDescribe = "";

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
			this.PropID = Integer.parseInt(ao_Node.getAttribute("ID"));
			this.PropCode = ao_Node.getAttribute("Code");
			this.PropName = ao_Node.getAttribute("Name");
			this.InitValue = ao_Node.getAttribute("InitValue");
			this.PropDataType = Integer.parseInt(ao_Node
					.getAttribute("DataType"));
			// 对以前参数错误的修改
			if (this.FormCell.CellStyle == EFormCellStyle.DTPickerStyle) {
				if ("Value;MinDate;MaxDate".indexOf(this.PropCode) + 1 > 0) {
					this.PropDataType = EPropDataType.StringDataType;
				}
			}
			this.PropType = Integer.parseInt(ao_Node.getAttribute("Type"));
			this.SourceType = Integer.parseInt(ao_Node
					.getAttribute("SourceType"));
		} else {
			this.PropID = Integer.parseInt(ao_Node.getAttribute("PropID"));
			this.PropCode = ao_Node.getAttribute("PropCode");
			this.PropName = ao_Node.getAttribute("PropName");
			this.InitValue = ao_Node.getAttribute("InitValue");
			this.PropDataType = Integer.parseInt(ao_Node
					.getAttribute("DataType"));
			this.PropType = Integer.parseInt(ao_Node.getAttribute("PropType"));
			this.SourceType = Integer.parseInt(ao_Node
					.getAttribute("SourceType"));
		}
		this.importEventXml(); // 导入事件内容
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
		this.exportEventXml(); // 导出事件内容
		if (ai_Type == 1) {
			ao_Node.setAttribute("ID", Integer.toString(this.PropID));
			ao_Node.setAttribute("Code", this.PropCode);
			ao_Node.setAttribute("Name", this.PropName);
			ao_Node.setAttribute("InitValue", this.InitValue);
			ao_Node.setAttribute("DataType",
					Integer.toString(this.PropDataType));
			ao_Node.setAttribute("Type", Integer.toString(this.PropType));
			ao_Node.setAttribute("SourceType",
					Integer.toString(this.SourceType));
		} else {
			ao_Node.setAttribute("PropID", Integer.toString(this.PropID));
			ao_Node.setAttribute("PropCode", this.PropCode);
			ao_Node.setAttribute("PropName", this.PropName);
			ao_Node.setAttribute("InitValue", this.InitValue);
			ao_Node.setAttribute("DataType",
					Integer.toString(this.PropDataType));
			ao_Node.setAttribute("PropType", Integer.toString(this.PropType));
			ao_Node.setAttribute("SourceType",
					Integer.toString(this.SourceType));
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
		this.exportEventXml(); // 导出事件内容
		if (ai_Type == 1) {
			ao_Bag.Write("ID", this.PropID);
			ao_Bag.Write("PC", this.PropCode);
			ao_Bag.Write("PN", this.PropName);
			ao_Bag.Write("IV", this.InitValue);
			ao_Bag.Write("DT", this.PropDataType);
			ao_Bag.Write("PT", this.PropType);
			ao_Bag.Write("ST", this.SourceType);
		} else {
			ao_Bag.Write("ml_PropID", this.PropID);
			ao_Bag.Write("ms_PropCode", this.PropCode);
			ao_Bag.Write("ms_PropName", this.PropName);
			ao_Bag.Write("ms_InitValue", this.InitValue);
			ao_Bag.Write("mi_PropDataType", this.PropDataType);
			ao_Bag.Write("mi_PropType", this.PropType);
			ao_Bag.Write("mi_SourceType", this.SourceType);
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
			this.PropID = ao_Bag.ReadInt("ID");
			this.PropCode = ao_Bag.ReadString("PC");
			this.PropName = ao_Bag.ReadString("PN");
			this.InitValue = ao_Bag.ReadString("IV");
			this.PropDataType = ao_Bag.ReadInt("DT");
			if (this.FormCell.CellStyle == EFormCellStyle.DTPickerStyle) // 对以前参数错误的修改
			{
				if ("Value;MinDate;MaxDate".indexOf(this.PropCode) + 1 > 0) {
					this.PropDataType = EPropDataType.StringDataType;
				}
			}
			this.PropType = ao_Bag.ReadInt("PT");
			this.SourceType = ao_Bag.ReadInt("ST");
		} else {
			this.PropID = ao_Bag.ReadInt("ml_PropID");
			this.PropCode = ao_Bag.ReadString("ms_PropCode");
			this.PropName = ao_Bag.ReadString("ms_PropName");
			this.InitValue = ao_Bag.ReadString("ms_InitValue");
			this.PropDataType = ao_Bag.ReadInt("ms_PropDataType");
			if (this.FormCell.CellStyle == EFormCellStyle.DTPickerStyle) // 对以前参数错误的修改
			{
				if ("Value;MinDate;MaxDate".indexOf(this.PropCode) + 1 > 0) {
					this.PropDataType = EPropDataType.StringDataType;
				}
			}
			this.PropType = ao_Bag.ReadInt("mi_PropType");
			this.SourceType = ao_Bag.ReadInt("mi_SourceType");
		}
		this.importEventXml(); // 导入事件内容
	}

}
