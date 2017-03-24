package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.EDataType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.w3c.dom.Element;

/**
 * 数据存储定义域（数据列定义）对象。
 * 
 * @author Jackie.wang
 * 
 */
public class CField extends CBase {
	/**
	 * 初始化
	 */
	public CField() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CField(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 所属的数据存储对象
	 */
	public CTable Table = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 域标识
	 */
	public int FieldID = -1;

	/**
	 * 域英文名称（代码）
	 */
	public String FieldCode = "";

	/**
	 * 读取外键域
	 * 
	 * @return
	 */
	public String getForeignKeyField() {
		int i = this.ForeignKey.indexOf("[") + 1;
		if (i == 0)
			return "";
		return this.ForeignKey.substring(i + 1 - 1, this.ForeignKey.length()
				- i - 1);
	}

	/**
	 * 读取外键表
	 * 
	 * @return
	 */
	public String getForeignKeyTable() {
		int i = this.ForeignKey.indexOf("[") + 1;
		if (i == 0)
			return "";
		return this.ForeignKey.substring(0, i - 1);
	}

	/**
	 * 域英文名称（代码）
	 * 
	 * @param value
	 * @throws Exception 
	 */
	public void setFieldCode(String value) throws Exception {
		if (value == null || value.equals("")) {
			// 错误[1099]：域英文名称不能为空
			this.Raise(1099, "setFieldCode", String.valueOf(this.FieldID));
			return;
		}
		if (this.FieldCode == value)
			return;
		if (MGlobal.BeyondOfLength(value, 50) == false)
			return;

		for (CField lo_Field : this.Table.Fields) {
			if (lo_Field.FieldCode.equals(value)) {
				// 错误[1100]：域英文名称不能重复
				this.Raise(1100, "setFieldCode", String.valueOf(this.FieldID));
				return;
			}
		}

		this.FieldCode = value;
	}

	/**
	 * 域中文名称
	 */
	public String FieldName = "";

	/**
	 * 设置域中文名称
	 * 
	 * @param value
	 * @throws Exception 
	 */
	public void setFieldName(String value) throws Exception {
		if (MGlobal.isEmpty(value)) {
			// 错误[1101]：域中文名称不能为空
			this.Raise(1101, "setFieldName", String.valueOf(this.FieldID));
			return;
		}

		if (this.FieldName.equals(value)
				|| MGlobal.BeyondOfLength(value, 50) == false)
			return;

		for (CField lo_Field : this.Table.Fields) {
			if (lo_Field.FieldName.equals(value)) {
				// 错误[1102]：域中文名称不能重复
				this.Raise(1102, "setFieldName", String.valueOf(this.FieldID));
				return;
			}
		}

		this.FieldName = value;
	}

	/**
	 * 域数据类型
	 */
	public int FieldType = EDataType.adEmpty;

	/**
	 * 设置域数据类型
	 * 
	 * @param value
	 */
	public void setFieldType(int value) {
		this.DecimalDigits = 0;
		switch (value) {
		case EDataType.adSmallInt: // 整型
			this.FieldLength = 2;
			break;
		case EDataType.adInteger: // 长整型
			this.FieldLength = 4;
			break;
		case EDataType.adNumeric: // 数值型
			this.FieldLength = 18;
			this.DecimalDigits = 9;
			break;
		case EDataType.adChar: // 固定字符串型
			this.FieldLength = 10;
			break;
		case EDataType.adVarChar: // 可变字符串型
			this.FieldLength = 50;
			break;
		case EDataType.adDBTimeStamp: // 日期时间类型
			this.FieldLength = 4;
			break;
		case EDataType.adBinary: // 固定二进制字符串型
			this.FieldLength = 10;
			break;
		case EDataType.adVarBinary: // 可变二进制字符串型
			this.FieldLength = 50;
			break;
		case EDataType.adLongVarChar: // 大文本（Text)型
		case EDataType.adLongVarBinary: // 大二进制(Image)型
			this.FieldLength = -1;
			break;
		default: // #本系统不支持的类型
			break;
		}
		this.FieldType = value;
	}

	/**
	 * 域长度
	 */
	public int FieldLength = 0;

	/**
	 * 设置域长度
	 * 
	 * @param value
	 */
	public void setFieldLength(int value) {
		if (value < 1)
			return;
		if (!(this.FieldType == EDataType.adChar
				|| this.FieldType == EDataType.adVarChar
				|| this.FieldType == EDataType.adNumeric
				|| this.FieldType == EDataType.adBinary || this.FieldType == EDataType.adVarBinary))
			return;
		this.FieldLength = value;
		if (this.FieldType == EDataType.adNumeric) {
			if (this.FieldLength <= this.DecimalDigits)
				this.DecimalDigits = this.FieldLength - 1;
		}
	}

	/**
	 * 域小数点的长度（为adNumeric类型时）
	 */
	public int DecimalDigits = 0;

	/**
	 * 设置域小数点的长度（为adNumeric类型时）
	 * 
	 * @param value
	 */
	public void setDecimalDigits(int value) {
		if (this.FieldType == EDataType.adNumeric) {
			if (value >= 0 && value < this.FieldLength)
				this.DecimalDigits = value;
		}
	}

	/**
	 * 是否主键值
	 */
	public boolean IsPrimaryKey = false;

	/**
	 * 设置是否主键值
	 * 
	 * @param value
	 */
	public void setIsPrimaryKey(boolean value) {
		this.IsPrimaryKey = value;
		if (this.IsPrimaryKey)
			this.FieldIsNull = false;
	}

	/**
	 * 外键设置，格式为：表名称[外键域]
	 */
	public String ForeignKey = "";

	/**
	 * 域是否为空
	 */
	public boolean FieldIsNull = true;

	/**
	 * 设置域是否为空
	 * 
	 * @param value
	 */
	public void setFieldIsNull(boolean value) {
		if (this.IsPrimaryKey == false)
			this.FieldIsNull = value;
	}

	/**
	 * 域填充内容，该内容对应为表头属性字段或公文的特殊内容，如下定义： [名称]、[标识]、[拟稿人]、[创建时间]、[..]、..
	 * 用“[]”表示系统部分，从表公文系统字段对照(SystemFieldCollate)对照读取 其他内容则为表头属性的定义
	 */
	public String FieldContent = "";

	/**
	 * 域数据计算公式，按照计算公式计算出来的内容
	 */
	public String FieldScript = "";

	/**
	 * 数据列SQL语句
	 * 
	 * @return
	 */
	public String getFieldLabel() {
		String ls_Str = "";
		switch (this.FieldType) {
		case EDataType.adSmallInt: // 整型
			ls_Str = "smallint";
			break;
		case EDataType.adInteger: // 长整型
			ls_Str = "int";
			break;
		case EDataType.adNumeric: // 数值型
			ls_Str = "numeric(" + String.valueOf(this.FieldLength) + ","
					+ String.valueOf(this.DecimalDigits) + ")";
			break;
		case EDataType.adChar: // 固定字符串型
			ls_Str = "char(" + String.valueOf(this.FieldLength) + ")";
			break;
		case EDataType.adVarChar: // 可变字符串型
			ls_Str = "varchar(" + String.valueOf(this.FieldLength) + ")";
			break;
		case EDataType.adDBTimeStamp: // 日期时间类型
			ls_Str = "datetime";
			break;
		case EDataType.adBinary: // 固定二进制字符串型
			ls_Str = "binary(" + String.valueOf(this.FieldLength) + ")";
			break;
		case EDataType.adVarBinary: // 可变二进制字符串型
			ls_Str = "varbinary(" + String.valueOf(this.FieldLength) + ")";
			break;
		case EDataType.adLongVarChar: // 大文本（Text)类型
			ls_Str = "text";
			break;
		case EDataType.adLongVarBinary: // 大二进制(Image)类型
			ls_Str = "image";
			break;
		default: // #本系统不支持的类型
			ls_Str = "variant";
			break;
		}

		int i = 20 - this.FieldCode.length();
		int j = 16 - ls_Str.length();
		if (i < 1)
			i = 1;
		if (j < 1)
			j = 1;

		ls_Str = this.FieldCode + MGlobal.addSpace(i) + ls_Str
				+ MGlobal.addSpace(j);
		ls_Str += MGlobal.addSpace(4)
				+ (this.FieldIsNull ? "null," : "not null,");

		return ls_Str;
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		// 所属的数据存储对象
		this.Table = null;

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
		if (ai_Type == 1 && ao_Node.hasAttribute("ID")) {
			this.FieldID = Integer.parseInt((ao_Node.getAttribute("ID")));
			this.FieldCode = ao_Node.getAttribute("Code");
			this.FieldName = ao_Node.getAttribute("Name");
			this.FieldType = Integer.parseInt(ao_Node.getAttribute("Type"));
			this.FieldLength = Integer.parseInt(ao_Node.getAttribute("Length"));
			this.DecimalDigits = Integer.parseInt(ao_Node
					.getAttribute("DecimalDigits"));
			this.IsPrimaryKey = Boolean.parseBoolean(ao_Node
					.getAttribute("IsPrimaryKey"));
			this.ForeignKey = ao_Node.getAttribute("ForeignKey");
			this.FieldIsNull = Boolean.parseBoolean(ao_Node
					.getAttribute("IsNull"));
			this.FieldContent = (ao_Node.getAttribute("Content"));
			this.FieldScript = ao_Node.getAttribute("Script");
		} else {
			this.FieldID = Integer.parseInt((ao_Node.getAttribute("FieldID")));
			this.FieldCode = ao_Node.getAttribute("FieldCode");
			this.FieldName = ao_Node.getAttribute("FieldName");
			this.FieldType = Integer
					.parseInt(ao_Node.getAttribute("FieldType"));
			this.FieldLength = Integer.parseInt(ao_Node
					.getAttribute("FieldLength"));
			this.DecimalDigits = Integer.parseInt(ao_Node
					.getAttribute("DecimalDigits"));
			this.IsPrimaryKey = Boolean.parseBoolean(ao_Node
					.getAttribute("IsPrimaryKey"));
			this.ForeignKey = ao_Node.getAttribute("ForeignKey");
			this.FieldIsNull = Boolean.parseBoolean(ao_Node
					.getAttribute("FieldIsNull"));
			this.FieldContent = (ao_Node.getAttribute("FieldContent"));
			this.FieldScript = ao_Node.getAttribute("FieldScript");
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
			ao_Node.setAttribute("ID", Integer.toString(this.FieldID));
			ao_Node.setAttribute("Code", this.FieldCode);
			ao_Node.setAttribute("Name", this.FieldName);
			ao_Node.setAttribute("Type", Integer.toString(this.FieldType));
			ao_Node.setAttribute("Length", Integer.toString(this.FieldLength));
			ao_Node.setAttribute("DecimalDigits",
					Integer.toString(this.DecimalDigits));
			ao_Node.setAttribute("IsPrimaryKey",
					Boolean.toString(this.IsPrimaryKey));
			ao_Node.setAttribute("ForeignKey", this.ForeignKey);
			ao_Node.setAttribute("IsNull", Boolean.toString(this.FieldIsNull));
			ao_Node.setAttribute("Content", this.FieldContent);
			ao_Node.setAttribute("Script", this.FieldScript);
		} else {
			ao_Node.setAttribute("FieldID", Integer.toString(this.FieldID));
			ao_Node.setAttribute("FieldCode", this.FieldCode);
			ao_Node.setAttribute("FieldName", this.FieldName);
			ao_Node.setAttribute("FieldType", Integer.toString(this.FieldType));
			ao_Node.setAttribute("FieldLength",
					Integer.toString(this.FieldLength));
			ao_Node.setAttribute("DecimalDigits",
					Integer.toString(this.DecimalDigits));
			ao_Node.setAttribute("IsPrimaryKey",
					Boolean.toString(this.IsPrimaryKey));
			ao_Node.setAttribute("ForeignKey", this.ForeignKey);
			ao_Node.setAttribute("FieldIsNull",
					Boolean.toString(this.FieldIsNull));
			ao_Node.setAttribute("FieldContent", this.FieldContent);
			ao_Node.setAttribute("FieldScript", this.FieldScript);
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
		ao_Bag.Write("ml_FieldID", this.FieldID);
		ao_Bag.Write("ms_FieldCode", this.FieldCode);
		ao_Bag.Write("ms_FieldName", this.FieldName);
		ao_Bag.Write("mi_FieldType", this.FieldType);
		ao_Bag.Write("mi_FieldLength", this.FieldLength);
		ao_Bag.Write("mi_DecimalDigits", this.DecimalDigits);
		ao_Bag.Write("mb_IsPrimaryKey", this.IsPrimaryKey);
		ao_Bag.Write("ms_ForeignKey", this.ForeignKey);
		ao_Bag.Write("mb_FieldIsNull", this.FieldIsNull);
		ao_Bag.Write("ms_FieldContent", this.FieldContent);
		ao_Bag.Write("ms_FieldScript", this.FieldScript);
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
		this.FieldID = ao_Bag.ReadInt("ml_FieldID");
		this.FieldCode = ao_Bag.ReadString("ms_FieldCode");
		this.FieldName = ao_Bag.ReadString("ms_FieldName");
		this.FieldType = ao_Bag.ReadInt("mi_FieldType");
		this.FieldLength = ao_Bag.ReadInt("mi_FieldLength");
		this.DecimalDigits = ao_Bag.ReadInt("mi_DecimalDigits");
		this.IsPrimaryKey = ao_Bag.ReadBoolean("mb_IsPrimaryKey");
		this.ForeignKey = ao_Bag.ReadString("ms_ForeignKey");
		this.FieldIsNull = ao_Bag.ReadBoolean("mb_FieldIsNull");
		this.FieldContent = ao_Bag.ReadString("ms_FieldContent");
		this.FieldScript = ao_Bag.ReadString("ms_FieldScript");
	}
}
