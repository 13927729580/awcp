package org.szcloud.framework.workflow.core.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.emun.ECaculateStatusType;
import org.szcloud.framework.workflow.core.emun.EDataHandleType;
import org.szcloud.framework.workflow.core.emun.EPropDataType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.TPropertyBag;
import org.w3c.dom.Element;

/**
 * 表头属性对象：公文处理对象模型中存储表格数据的对象，是公文数据的重要组成部分，它的数据表现灵活，适合存储各种类型的数据。
 * 
 * @author Jackie.Wang
 * 
 */
public class CProp extends CBase {

	/**
	 * 初始化
	 */
	public CProp() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CProp(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 保存到...-原始
	 * 
	 * @return
	 */
	public String Write() {
		TPropertyBag lo_Bag = new TPropertyBag();
		lo_Bag.WriteContent("mi_Length", mi_Length);
		lo_Bag.WriteContent("ms_LowValue", ms_LowValue);
		lo_Bag.WriteContent("ms_UpValue", ms_UpValue);
		lo_Bag.WriteContent("ms_InitValue", ms_InitValue);
		lo_Bag.WriteContent("ms_OpenCacuContent", ms_OpenCacuContent);
		lo_Bag.WriteContent("ms_SendCacuContent", ms_SendCacuContent);
		lo_Bag.WriteContent("ml_TableID", ml_TableID);
		lo_Bag.WriteContent("ml_TableFieldID", ml_TableFieldID);
		String returnValue = lo_Bag.Content;
		lo_Bag = null;
		return returnValue;
	}

	/**
	 * 清除释放对象的内存数据
	 * 
	 * @throws Exception
	 */
	public void ClearUp() throws Exception {
		try {
			// 所属的公文模板对象
			this.Cyclostyle = null;

			// 当数据类型为RecordsetType时使用的内存变量
			if (mo_DataSource != null) {
//				mo_DataSource.close();
				mo_DataSource = null;
			}

			//
			if (ms_Values != null) {
				ms_Values.clear();
				ms_Values = null;
			}

			super.ClearUp();
		} catch (Exception e) {
			this.Raise(e, "ClearUp", null);
		}
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的公文模板对象
	 */
	public CCyclostyle Cyclostyle = null;

	/**
	 * 当数据类型为RecordsetType时使用的内存变量
	 */
	private Map<String, Object> mo_DataSource = null;

	/**
	 * 当数据类型为RecordsetType时使用的内存变量
	 * 
	 * @return
	 */
	public Map<String, Object> getDataSource() {
		return mo_DataSource;
	}

	/**
	 * 当数据类型为RecordsetType时使用的内存变量
	 * 
	 * @param value
	 */
	public void setDataSource(Map<String, Object> value) {
		mo_DataSource = value;
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 属性标识
	 */
	public int PropID = -1;

	/**
	 * 属性名称
	 */
	public String PropName = "";

	/**
	 * 设置属性名称
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setPropName(String value) throws Exception {
		if (MGlobal.isEmpty(value)) {
			// #错误[1037]：公文数据表头属性的名称不能为空
			this.Raise(1037, "setPropName", "");
			return;
		}

		if (this.PropName.equals(value))
			return;

		if (MGlobal.BeyondOfLength(value, 50) == false)
			return;

		for (CProp lo_Prop : this.Cyclostyle.Props) {
			if (lo_Prop.PropName.equals(value)) {
				// #错误[1090]：公文数据表头属性的名称不能重复
				this.Raise(1090, "setPropName", "");
				return;
			}
		}

		this.PropName = value;
	}

	/**
	 * 属性类型
	 */
	public int PropDataType = EPropDataType.NotAnyDataType;

	/**
	 * 设置属性类型
	 * 
	 * @param value
	 */
	public void setPropDataType(int value) {
		if (this.PropDataType == value) {
			return;
		}
		this.PropDataType = value;
		String ls_InitValue = ms_InitValue;
		String ls_LowValue = ms_LowValue;
		String ls_UpValue = ms_UpValue;

		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			ms_InitValue = Boolean.toString(MGlobal.getBool(ls_InitValue));
			ms_LowValue = "false";
			ms_UpValue = "true";
			break;
		case EPropDataType.IntegerDataType:
			if (ls_InitValue != "")
				ms_InitValue = Integer.toString(MGlobal.getInt(ls_InitValue));
			if (ls_LowValue != "")
				ms_LowValue = Integer.toString(MGlobal.getInt(ls_LowValue));
			if (ls_UpValue != "")
				ms_UpValue = Integer.toString(MGlobal.getInt(ls_UpValue));
			break;
		case EPropDataType.LongDataType:
			if (ls_InitValue != "")
				ms_InitValue = Long.toString(MGlobal.getLong(ls_InitValue));
			if (ls_LowValue != "")
				ms_LowValue = Long.toString(MGlobal.getLong(ls_LowValue));
			if (ls_UpValue != "")
				ms_UpValue = Long.toString(MGlobal.getLong(ls_UpValue));
			break;
		case EPropDataType.SingleDataType:
			if (ls_InitValue != "")
				ms_InitValue = Float.toString(MGlobal.getFloat(ls_InitValue));
			if (ls_LowValue != "")
				ms_LowValue = Float.toString(MGlobal.getFloat(ls_LowValue));
			if (ls_UpValue != "")
				ms_UpValue = Float.toString(MGlobal.getFloat(ls_UpValue));
			break;
		case EPropDataType.DoubleDataType:
			if (ls_InitValue != "")
				ms_InitValue = Double.toString(MGlobal.getDouble(ls_InitValue));
			if (ls_LowValue != "")
				ms_LowValue = Double.toString(MGlobal.getDouble(ls_LowValue));
			if (ls_UpValue != "")
				ms_UpValue = Double.toString(MGlobal.getDouble(ls_UpValue));
			break;
		case EPropDataType.DateDataType:
			if (ls_InitValue != "")
				ms_InitValue = MGlobal.dateToString(MGlobal
						.stringToDate(ls_InitValue));
			if (ls_LowValue != "")
				ms_LowValue = MGlobal.dateToString(MGlobal
						.stringToDate(ls_LowValue));
			if (ls_UpValue != "")
				ms_UpValue = MGlobal.dateToString(MGlobal
						.stringToDate(ls_UpValue));
			break;
		default: // StringDataType, ImageDataType
			break;
		}

		mi_Length = this.getLength();
	}

	/**
	 * 属性长度
	 */
	private int mi_Length = 0;

	/**
	 * 属性长度
	 * 
	 * @return
	 */
	public int getLength() {
		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			return 1;
		case EPropDataType.IntegerDataType:
			return 2;
		case EPropDataType.LongDataType:
			return 4;
		case EPropDataType.SingleDataType:
			return 4;
		case EPropDataType.DoubleDataType:
			return 8;
		case EPropDataType.DateDataType:
			return 4;
		case EPropDataType.ImageDataType:
			return -1;
		default: // StringDataType
			return mi_Length;
		}
	}

	/**
	 * 属性长度
	 * 
	 * @param value
	 */
	public void setLength(int value) {
		if (this.PropDataType == EPropDataType.StringDataType & value > 0) {
			mi_Length = value;
		}
	}

	/**
	 * 属性值下限
	 */
	private String ms_LowValue = "";

	/**
	 * 获取属性值下限
	 * 
	 * @return
	 */
	public String getLowValue() {
		if (ms_LowValue == null)
			return "";

		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			return Boolean.toString(Boolean.getBoolean(ms_LowValue));
		case EPropDataType.IntegerDataType:
			return Integer.toString(Integer.parseInt(ms_LowValue));
		case EPropDataType.LongDataType:
			return Integer.toString(Integer.parseInt(ms_LowValue));
		case EPropDataType.SingleDataType:
			return Float.toString(Float.parseFloat(ms_LowValue));
		case EPropDataType.DoubleDataType:
			return Double.toString(Double.parseDouble(ms_LowValue));
		case EPropDataType.DateDataType:
			return MGlobal.dateToString(MGlobal.stringToDate(ms_LowValue),
					"yyyy/MM/dd HH:mm:ss");
		case EPropDataType.ImageDataType:
			return "";
		default: // StringDataType
			return ms_LowValue;
		}
	}

	/**
	 * 设置属性值下限
	 * 
	 * @param value
	 */
	public void setLowValue(String value) {
		ms_LowValue = value;
	}

	/**
	 * 属性值上限
	 */
	private String ms_UpValue = "";

	/**
	 * 获取属性值上限
	 */
	public String getUpValue() {
		if (ms_UpValue == null)
			return "";

		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			return Boolean.toString(Boolean.getBoolean(ms_UpValue));
		case EPropDataType.IntegerDataType:
			return Integer.toString(Integer.parseInt(ms_UpValue));
		case EPropDataType.LongDataType:
			return Integer.toString(Integer.parseInt(ms_UpValue));
		case EPropDataType.SingleDataType:
			return Float.toString(Float.parseFloat(ms_UpValue));
		case EPropDataType.DoubleDataType:
			return Double.toString(Double.parseDouble(ms_UpValue));
		case EPropDataType.DateDataType:
			return MGlobal.dateToString(MGlobal.stringToDate(ms_UpValue),
					"yyyy/MM/dd HH:mm:ss");
		case EPropDataType.ImageDataType:
			return "";
		default: // StringDataType
			return ms_UpValue;
		}
	}

	/**
	 * 设置属性值上限
	 */
	public void setUpValue(String value) {
		if (value == null)
			return;

		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			ms_UpValue = Boolean.toString(Boolean.getBoolean(value));
			break;
		case EPropDataType.IntegerDataType:
			ms_UpValue = Integer.toString(Integer.parseInt(value));
			break;
		case EPropDataType.LongDataType:
			ms_UpValue = Integer.toString(Integer.parseInt(value));
			break;
		case EPropDataType.SingleDataType:
			ms_UpValue = Float.toString(Float.parseFloat(value));
			break;
		case EPropDataType.DoubleDataType:
			ms_UpValue = Double.toString(Double.parseDouble(value));
			break;
		case EPropDataType.DateDataType:
			ms_UpValue = MGlobal.dateToString(MGlobal.stringToDate(value),
					"yyyy/MM/dd HH:mm:ss");
			break;
		case EPropDataType.ImageDataType:
			ms_UpValue = "";
			break;
		default: // StringDataType
			ms_UpValue = value;
			break;
		}
	}

	/**
	 * 属性初始化
	 */
	private String ms_InitValue = "";

	/**
	 * 属性初始化
	 * 
	 * @return
	 */
	public String getInitValue() {
		if (ms_InitValue == null)
			return "";

		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			return Boolean.toString(Boolean.getBoolean(ms_InitValue));
		case EPropDataType.IntegerDataType:
			return Integer.toString(Integer.parseInt(ms_InitValue));
		case EPropDataType.LongDataType:
			return Integer.toString(Integer.parseInt(ms_InitValue));
		case EPropDataType.SingleDataType:
			return Float.toString(Float.parseFloat(ms_InitValue));
		case EPropDataType.DoubleDataType:
			return Double.toString(Double.parseDouble(ms_InitValue));
		case EPropDataType.DateDataType:
			return MGlobal.dateToString(MGlobal.stringToDate(ms_InitValue),
					"yyyy/MM/dd HH:mm:ss");
		case EPropDataType.ImageDataType:
			return "";
		default: // StringDataType
			return ms_InitValue;
		}
	}

	/**
	 * 属性初始化
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setInitValue(String value) throws Exception {
		try {
			switch (this.PropDataType) {
			case EPropDataType.BooleanDataType:
				ms_InitValue = Boolean.toString(Boolean.getBoolean(value));
				break;
			case EPropDataType.IntegerDataType:
				ms_InitValue = Integer.toString(Integer.parseInt(value));
				break;
			case EPropDataType.LongDataType:
				ms_InitValue = Integer.toString(Integer.parseInt(value));
				break;
			case EPropDataType.SingleDataType:
				ms_InitValue = Float.toString(Float.parseFloat(value));
				break;
			case EPropDataType.DoubleDataType:
				ms_InitValue = Double.toString(Double.parseDouble(value));
				break;
			case EPropDataType.DateDataType:
				ms_InitValue = MGlobal.dateToString(MGlobal.getNow(),
						"yyyy/MM/dd HH:mm:ss");
				break;
			case EPropDataType.ImageDataType:
				ms_InitValue = "";
				break;
			default: // StringDataType
				ms_InitValue = value;
				break;
			}
		} catch (Exception e) {
			this.Raise(e, "setInitValue", null);
		}
	}

	/**
	 * 属性值，该内容由数据表格保存和读取
	 */
	private String ms_Value = "";

	/**
	 * 属性值，该内容由数据表格保存和读取
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getValue() throws Exception {
		try {
			switch (this.PropDataType) {
			case EPropDataType.BooleanDataType:
				return Boolean.toString(Boolean.getBoolean(ms_Value));
			case EPropDataType.IntegerDataType:
				return Integer.toString(Integer.parseInt(ms_Value));
			case EPropDataType.LongDataType:
				return Integer.toString(Integer.parseInt(ms_Value));
			case EPropDataType.SingleDataType:
				return Float.toString(Float.parseFloat(ms_Value));
			case EPropDataType.DoubleDataType:
				return Double.toString(Double.parseDouble(ms_Value));
			case EPropDataType.DateDataType:
				return MGlobal.dateToString(MGlobal.stringToDate(ms_Value),
						"yyyy/MM/dd HH:mm:ss");
			case EPropDataType.ImageDataType:
				return "";
			default: // StringDataType
				return ms_Value;
			}
		} catch (Exception e) {
			this.Raise(e, "getValue", null);
		}
		return "";
	}

	/**
	 * 属性值，该内容由数据表格保存和读取
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		ms_Value = "";
		switch (this.PropDataType) {
		case EPropDataType.BooleanDataType:
			if(!value.equals(""))
				ms_Value = Boolean.toString(Boolean.getBoolean(value));
			break;
		case EPropDataType.IntegerDataType:
			if(!value.equals(""))
				ms_Value = Integer.toString(Integer.parseInt(value));
			break;
		case EPropDataType.LongDataType:
			if(!value.equals(""))
				ms_Value = Integer.toString(Integer.parseInt(value));
			break;
		case EPropDataType.SingleDataType:
			if(!value.equals(""))
				ms_Value = Float.toString(Float.parseFloat(value));
			break;
		case EPropDataType.DoubleDataType:
			if(!value.equals(""))
				ms_Value = Double.toString(Double.parseDouble(value));
			break;
		case EPropDataType.DateDataType:
			if(!value.equals(""))
				ms_Value = MGlobal.dateToString(MGlobal.stringToDate(ms_Value),
					"yyyy/MM/dd HH:mm:ss");
			break;
		case EPropDataType.ImageDataType:
			ms_Value = "";
			break;
		default: // StringDataType
			ms_Value = value;
			break;
		}
	}

	/**
	 * 属性打开时计算内容
	 */
	private String ms_OpenCacuContent = "";

	/**
	 * 属性打开时计算内容
	 * 
	 * @return
	 */
	public String getOpenCacuContent() {
		return ms_OpenCacuContent;
	}

	/**
	 * 属性打开时计算内容
	 * 
	 * @param value
	 */
	public void setOpenCacuContent(String value) {
		ms_OpenCacuContent = value;
	}

	/**
	 * 属性发送时计算内容
	 */
	private String ms_SendCacuContent = "";

	/**
	 * 属性发送时计算内容
	 * 
	 * @return
	 */
	public String getSendCacuContent() {
		return ms_SendCacuContent;
	}

	/**
	 * 属性发送时计算内容
	 * 
	 * @param value
	 */
	public void setSendCacuContent(String value) {
		ms_SendCacuContent = value;
	}

	/**
	 * 表头属性计算状态类型，该数据为内存数据，无须保存
	 */
	private int mi_CaculateStatus = ECaculateStatusType.NeedNotCaculate;

	/**
	 * 表头属性计算状态类型，该数据为内存数据，无须保存
	 * 
	 * @return
	 */
	public int getCaculateStatus() {
		return mi_CaculateStatus;
	}

	/**
	 * 表头属性计算状态类型，该数据为内存数据，无须保存
	 * 
	 * @param value
	 */
	public void setCaculateStatus(int value) {
		mi_CaculateStatus = value;
	}

	/**
	 * 表头属性绑定表格标识
	 */
	private int ml_TableID = 0;

	/**
	 * 表头属性绑定表格标识
	 * 
	 * @return
	 */
	public int getTableID() {
		return ml_TableID;
	}

	/**
	 * 表头属性绑定表格标识
	 * 
	 * @param value
	 */
	public void setTableID(int value) {
		ml_TableID = value;
	}

	/**
	 * 读取表头属性绑定表格
	 */
	public CTable getBindTable() {
		if (ml_TableID == -1)
			return null;

		for (CTable lo_Table : this.Cyclostyle.Tables) {
			if (lo_Table.TableID == ml_TableID)
				return lo_Table;
		}
		ml_TableID = -1;
		ml_TableFieldID = -1;
		return null;
	}

	/**
	 * 设置表头属性绑定表格
	 */
	public void setBindTable(CTable value) {
		if (value == null) {
			ml_TableID = -1;
		} else {
			ml_TableID = value.TableID;
		}
	}

	/**
	 * 表头属性绑定表格域标识
	 */
	private int ml_TableFieldID = 0;

	/**
	 * 读取表头属性绑定表格域标识
	 */
	public int getTableFieldID() {
		return ml_TableFieldID;
	}

	/**
	 * 设置表头属性绑定表格域标识
	 * 
	 * @param value
	 */
	public void setTableFieldID(int value) {
		ml_TableFieldID = value;
	}

	/**
	 * 读取表头属性绑定表格域标识
	 */
	public CField getBindTableField() {
		if (ml_TableFieldID == -1)
			return null;

		CTable lo_Table = getBindTable();
		if (lo_Table == null)
			return null;

		for (CField lo_Field : lo_Table.Fields) {
			if (lo_Field.FieldID == ml_TableFieldID)
				return lo_Field;
		}

		ml_TableFieldID = -1;
		return null;
	}

	/**
	 * 设置表头属性绑定表格域标识
	 */
	public void setBindTableField(CField value) {
		if (value == null) {
			ml_TableFieldID = -1;
		} else {
			ml_TableID = value.Table.TableID;
			ml_TableFieldID = value.FieldID;
		}
	}

	/**
	 * 表头属性是否为数组类型，如果是，他的数据个数为，=0为非数组类型
	 */
	private int mi_PropNum = 0;

	/**
	 * 表头属性是否为数组类型，如果是，他的数据个数为，=0为非数组类型
	 * 
	 * @return
	 */
	public int getPropNum() {
		return mi_PropNum;
	}

	/**
	 * 表头属性是否为数组类型，如果是，他的数据个数为，=0为非数组类型
	 * 
	 * @param value
	 */
	public void setPropNum(int value) {
		if (value < 0) {
			mi_PropNum = 0;
		} else {
			mi_PropNum = value;
		}
		if (mi_PropNum == 0) {
			if (ms_Values != null) {
				ms_Values.clear();
				ms_Values = null;
			}
		} else {
			if (ms_Values == null) {
				ms_Values = new ArrayList<String>();
			}
			while (ms_Values.size() > mi_PropNum) {
				ms_Values.remove(ms_Values.size() - 1);
			}
			while (ms_Values.size() < mi_PropNum) {
				ms_Values.add("");
			}
		}
	}

	/**
	 * 属性值集合（当表头属性为数组类型时存在）
	 */
	private ArrayList<String> ms_Values = null;

	/**
	 * 属性值集合（当表头属性为数组类型时存在）
	 * 
	 * @return
	 */
	public ArrayList<String> getValues() {
		return ms_Values;
	}

	/**
	 * 属性值集合（当表头属性为数组类型时存在）
	 * 
	 * @param value
	 */
	public void setValues(ArrayList<String> value) {
		ms_Values = value;
	}

	// #==========================================================================#
	// 常用过程或函数定义
	// #==========================================================================#

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
		if (ai_Type == 0 && ao_Node.hasAttribute("PropID")) {
			this.PropID = Integer.parseInt(ao_Node.getAttribute("PropID"));
			this.PropName = ao_Node.getAttribute("PropName");
			this.PropDataType = Integer.parseInt(ao_Node
					.getAttribute("PropDataType"));
		}
		mi_Length = Integer.parseInt(ao_Node.getAttribute("Length"));
		ms_LowValue = ao_Node.getAttribute("LowValue");
		ms_UpValue = ao_Node.getAttribute("UpValue");
		ms_InitValue = ao_Node.getAttribute("InitValue");
		ms_Value = ao_Node.getAttribute("Value");
		ms_OpenCacuContent = ao_Node.getAttribute("OpenCacuContent");
		ms_SendCacuContent = ao_Node.getAttribute("SendCacuContent");
		ml_TableID = Integer.parseInt(ao_Node.getAttribute("TableID"));
		ml_TableFieldID = Integer
				.parseInt(ao_Node.getAttribute("TableFieldID"));
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
			ao_Node.setAttribute("PropID", Integer.toString(this.PropID));
			ao_Node.setAttribute("PropName", this.PropName);
			ao_Node.setAttribute("PropDataType",
					Integer.toString(this.PropDataType));
		}
		ao_Node.setAttribute("Length", Integer.toString(mi_Length));
		ao_Node.setAttribute("LowValue", ms_LowValue);
		ao_Node.setAttribute("UpValue", ms_UpValue);
		ao_Node.setAttribute("InitValue", ms_InitValue);
		ao_Node.setAttribute("OpenCacuContent", ms_OpenCacuContent);
		ao_Node.setAttribute("SendCacuContent", ms_SendCacuContent);
		ao_Node.setAttribute("TableID", Integer.toString(ml_TableID));
		ao_Node.setAttribute("TableFieldID", Integer.toString(ml_TableFieldID));
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
		ao_Set.put("PropID", this.PropID);
		ao_Set.put("PropName", this.PropName);
		ao_Set.put("DataType", this.PropDataType);

		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				ao_Set.put("PropContent", "CP" + this.WriteContent(ai_Type));
			} else {
				ao_Set.put("PropContent", this.WriteContent(ai_Type));
			}
		} else {
			ao_Set.put("PropContent", this.ExportToXml("Prop", ai_Type));
		}
	}

	/**
	 * 获取保存数据库执行状态对象
	 * 
	 * @param ao_Connection
	 *            数据库连接对象；
	 * @param ao_Connection
	 *            数据库连接对象；
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
				ls_Sql = "INSERT INTO PropInst "
						+ "(WorkItemID, PropID, PropName, DataType, PropContent) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE PropInst SET "
						+ "PropName = ?, DataType = ?, PropContent = ? "
						+ "WHERE WorkItemID = ? AND PropID = ?";
			}
		} else {
			if (ai_SaveType == 0) {
				ls_Sql = "INSERT INTO PropList "
						+ "(TemplateID, PropID, PropName, DataType, PropContent) "
						+ "VALUES (?, ?, ?, ?, ?)";
			} else {
				ls_Sql = "UPDATE PropList SET "
						+ "PropName = ?, DataType = ?, PropContent = ? "
						+ "WHERE TemplateID = ? AND PropID = ?";
			}
		}
		return ls_Sql;
	}

	/**
	 * 保存
	 * 
	 * @param ao_Connection
	 *            数据库连接对象；
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
				parasList.add(this.PropID);
			}
		} else {
			if (ai_SaveType == 0) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.PropID);
			}
		}

		parasList.add(this.PropName);
		parasList.add(this.PropDataType);

		if (this.Cyclostyle.getSaveType() == EDataHandleType.OrignType) {
			if (ai_Type == 1) {
				parasList.add("CP" + this.WriteContent(ai_Type));
			} else {
				parasList.add(this.WriteContent(ai_Type));
			}
		} else {
			parasList.add(this.ExportToXml("Prop", ai_Type));
		}

		if (ab_Inst) {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.WorkItem.WorkItemID);
				parasList.add(this.PropID);
			}
		} else {
			if (ai_SaveType == 1) {
				parasList.add(this.Cyclostyle.CyclostyleID);
				parasList.add(this.PropID);
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
		this.PropID = (Integer) ao_Set.get("PropID");
		this.PropName = (String) ao_Set.get("PropName");
		this.PropDataType = (Short) ao_Set.get("DataType");

		String ls_Text = (String) ao_Set.get("PropContent");
		if (this.Cyclostyle.OpenType == EDataHandleType.OrignType) {
			if (ls_Text.substring(0, 2).equals("CP")) {
				this.ReadContent(ls_Text.substring(0, ls_Text.length()),
						ai_Type);

			} else {
				this.ReadContent(ls_Text, ai_Type);
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
		ao_Bag.Write("ml_PropID", this.PropID);
		ao_Bag.Write("ms_PropName", this.PropName);
		ao_Bag.Write("mi_PropDataType", this.PropDataType);
		ao_Bag.Write("mi_Length", mi_Length);
		ao_Bag.Write("ms_LowValue", ms_LowValue);
		ao_Bag.Write("ms_UpValue", ms_UpValue);
		ao_Bag.Write("ms_InitValue", ms_InitValue);
		ao_Bag.Write("ms_OpenCacuContent", ms_OpenCacuContent);
		ao_Bag.Write("ms_SendCacuContent", ms_SendCacuContent);
		ao_Bag.Write("ml_TableID", ml_TableID);
		ao_Bag.Write("ml_TableFieldID", ml_TableFieldID);
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
		this.PropID = ao_Bag.ReadInt("ml_PropID");
		this.PropName = ao_Bag.ReadString("ms_PropName");
		this.PropDataType = ao_Bag.ReadInt("mi_PropDataType");
		mi_Length = ao_Bag.ReadInt("mi_Length");
		ms_LowValue = ao_Bag.ReadString("ms_LowValue");
		ms_UpValue = ao_Bag.ReadString("ms_UpValue");
		ms_InitValue = ao_Bag.ReadString("ms_InitValue");
		ms_OpenCacuContent = ao_Bag.ReadString("ms_OpenCacuContent");
		ms_SendCacuContent = ao_Bag.ReadString("ms_SendCacuContent");
		ml_TableID = ao_Bag.ReadInt("ml_TableID");
		ml_TableFieldID = ao_Bag.ReadInt("ml_TableFieldID");
	}

}
