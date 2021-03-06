package cn.org.awcp.venson.api;

import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.util.DateFormaterUtil;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class TableDesc {
	public static String TYPE_INTEGER = "int";
	public static String TYPE_STIING = "string";
	public static String TYPE_DATE = "date";
	public static String TYPE_DATE_TIME = "datetime";
	public static String TYPE_FLOAT = "float";
	public static String TYPE_DOUBLE = "double";
	private String Field;
	private String Type;
	private String Null;
	private String Key;
	private String Default;
	private String Extra;

	public String getField() {
		return Field;
	}

	public void setField(String field) {
		Field = field;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getNull() {
		return Null;
	}

	public void setNull(String null1) {
		Null = null1;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getDefault() {
		return Default;
	}

	public void setDefault(String default1) {
		Default = default1;
	}

	public String getExtra() {
		return Extra;
	}

	public void setExtra(String extra) {
		Extra = extra;
	}

	/**
	 * 判断是否是主键
	 */
	public boolean isPrimaryKey() {
		if ("PRI".equals(Key)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取字段类型
	 */
	public String getFieldType() {
		// 判断类型是否是数值类型的数据
		if (this.Type.contains("int")) {
			return TableDesc.TYPE_INTEGER;
			// 判断类型是否是字符串类型的数据
		} else if (this.Type.contains("char") || this.Type.contains("text")) {
			return TableDesc.TYPE_STIING;
			// 判断类型是否是日期类型的数据
		} else if (this.Type.equals(TableDesc.TYPE_DATE)) {
			return TableDesc.TYPE_DATE;
			// 判断类型是否是日期类型的数据
		} else if (this.Type.equals(TableDesc.TYPE_DATE_TIME)) {
			return TableDesc.TYPE_DATE_TIME;
			// 判断类型是否是单精度浮点类型的数据
		} else if (this.Type.equals(TableDesc.TYPE_FLOAT)) {
			return TableDesc.TYPE_FLOAT;
			// 判断类型是否是双精度浮点类型的数据
		} else if (this.Type.equals(TableDesc.TYPE_DOUBLE)) {
			return TableDesc.TYPE_DOUBLE;
			// 其它数据
		} else {
			return this.Type;
		}
	}

	/**
	 * 设置默认的值
	 */
	public void setDeafultValue(Map<String, String> params) {
		if ("creator".equals(this.Field) || "userId".equals(this.Field) || "USER_ID".equals(this.Field)) {
			params.put(this.Field, ControllerHelper.getUserId() + "");
		} else if ("CREATE_DATE".equals(this.Field) || "createDate".equals(this.Field)) {
			params.put(this.Field, DateFormaterUtil.dateToString(new Date()));
		} else if ("CREATE_TIME".equals(this.Field) || "createTime".equals(this.Field)) {
			params.put(this.Field, DateFormaterUtil.dateToString(DateFormaterUtil.FORMART4, new Date()));
		}
	}

	/**
	 * 设置默认的主键
	 */
	public void setPrimaryValue(Map<String, String> params) {
		if (this.isPrimaryKey()) {
			// 判断字段类型
			if (this.getFieldType() == TableDesc.TYPE_STIING) {
				params.put(this.Field, UUID.randomUUID().toString());
			}
		}
	}

}
