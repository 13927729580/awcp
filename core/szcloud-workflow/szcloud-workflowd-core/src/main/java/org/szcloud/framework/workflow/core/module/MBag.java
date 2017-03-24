package org.szcloud.framework.workflow.core.module;

import java.text.MessageFormat;
import java.util.Date;

/**
 * 内部打包对象，该对象只提供本应用程序所使用
 * 
 * @author Jackie.Wang
 * @date 2015-1-15
 */
public class MBag {

	/**
	 * 内容
	 */
	public String Content = null;

	/**
	 * 当前指针所处位置
	 */
	public int Position = 0;

	/**
	 * 初始化...
	 * 
	 * @param as_Content
	 */
	public MBag(String as_Content) {
		this.Content = as_Content;
		if (this.Content == null)
			this.Content = "";
		this.Position = 0;
	}

	/**
	 * 注销...
	 */
	public void ClearUp() {
		this.Content = null;
	}

	/**
	 * 写内容
	 * 
	 * @param as_Name
	 * @param ab_Value
	 */
	public void Write(String as_Name, boolean ab_Value) {
		this.Content += "[" + as_Name + "](B)" + Boolean.toString(ab_Value);
	}

	/**
	 * 写内容
	 * 
	 * @param as_Name
	 * @param ab_Value
	 */
	public void Write(String as_Name, short ai_Value) {
		this.Content += "[" + as_Name + "](I)" + Short.toString(ai_Value);
	}

	/**
	 * 写内容
	 * 
	 * @param as_Name
	 * @param ab_Value
	 */
	public void Write(String as_Name, int al_Value) {
		this.Content += "[" + as_Name + "](L)" + Integer.toString(al_Value);
	}

	/**
	 * 写内容
	 * 
	 * @param as_Name
	 * @param ab_Value
	 */
	public void Write(String as_Name, float af_Value) {
		this.Content += "[" + as_Name + "](F)" + Float.toString(af_Value);
	}

	/**
	 * 写内容
	 * 
	 * @param as_Name
	 * @param ab_Value
	 */
	public void Write(String as_Name, Double af_Value) {
		this.Content += "[" + as_Name + "](U)" + Double.toString(af_Value);
	}

	/**
	 * 写内容
	 * 
	 * @param as_Name
	 * @param ab_Value
	 */
	public void Write(String as_Name, Date ad_Value) {
		// this.Content += "[" + as_Name + "](D)" + Date.toString(ad_Value);
	}

	/**
	 * 写内容
	 * 
	 * @param as_Name
	 * @param ab_Value
	 */
	public void Write(String as_Name, String as_Value) {
		if (MGlobal.isEmpty(as_Value)) {
			this.Content += MessageFormat.format("[{0}](S0)={1}\n", as_Name, as_Value);
		} else {
			this.Content += MessageFormat.format("[{0}](S{1})={2}\n", as_Name, String.valueOf(as_Value.length()), as_Value);
		}
	}

	/*
	 * 读内容
	 */
	public Object Read(String as_Name) {
		int i = this.Content.indexOf(as_Name, this.Position);
		if (i == -1)
			return null;

		i += as_Name.length() + 2;
		String ls_Text = this.Content.substring(i, i + 1);
		int j = this.Content.indexOf(")", i);
		int li_Length = 0;
		if (ls_Text.equals("S"))
			li_Length = Integer.parseInt(this.Content.substring(i + 1, j));
		else
			li_Length = this.Content.indexOf("\r\n", j);

		String ls_Value = "";
		if (li_Length > 0)
			ls_Value = this.Content.substring(j + 2, li_Length);
		this.Position = li_Length + 1;

		if (ls_Text.equals("B"))
			return Boolean.parseBoolean(ls_Value);
		if (ls_Text.equals("I"))
			return Short.parseShort(ls_Value);
		if (ls_Text.equals("L"))
			return Integer.parseInt(ls_Value);
		if (ls_Text.equals("F"))
			return Float.parseFloat(ls_Value);
		if (ls_Text.equals("U"))
			return Double.parseDouble(ls_Value);
		if (ls_Text.equals("D"))
			return MGlobal.stringToDate(ls_Value);
		if (ls_Text.equals("S"))
			return ls_Value;
		return null;
	}

	/*
	 * 读布尔型内容
	 */
	public boolean ReadBoolean(String as_Name) {
		return Boolean.parseBoolean(Read(as_Name).toString());
	}

	/*
	 * 读短整型内容
	 */
	public short ReadShort(String as_Name) {
		return Short.parseShort(Read(as_Name).toString());
	}

	/*
	 * 读整型内容
	 */
	public int ReadInt(String as_Name) {
		return Integer.parseInt(Read(as_Name).toString());
	}

	/*
	 * 读长整型内容
	 */
	public long ReadLong(String as_Name) {
		return Long.parseLong(Read(as_Name).toString());
	}

	/*
	 * 读单精度型内容
	 */
	public float ReadFloat(String as_Name) {
		return Float.parseFloat(Read(as_Name).toString());
	}

	/*
	 * 读双精度型内容
	 */
	public double ReadDouble(String as_Name) {
		return Double.parseDouble(Read(as_Name).toString());
	}

	/*
	 * 读日期型内容
	 */
	public Date ReadDate(String as_Name) {
		return (Date) Read(as_Name);
	}

	/*
	 * 读字符型内容
	 */
	public String ReadString(String as_Name) {
		return Read(as_Name).toString();
	}

}
