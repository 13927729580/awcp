package org.szcloud.framework.workflow.core.module;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 * 系统公共参数
 * #==========================================================================#
 * 本系统变量定义
 * 1、模块中的全程变量使用：g+缩写的变量类型+_+实际意义的英文名称(Global)
 * 2、模块中的私有变量使用：m+缩写的变量类型+_+实际意义的英文名称(Module)
 * 3、在过程或函数中的变量使用：l+缩写的变量类型+_+实际意义的英文名称(Procedure)
 * 4、在过程或函数中的参数变量使用：a+缩写的变量类型+_+实际意义的英文名称(Procedure)
 * 5、过程或函数名称使用：实际意义的英文名称
 * 6、模块中对外接口公共参数使用：实际意义的英文名称，若是对象可使用前缀：the
 * 7、固定变脸名称使用前缀：const
 * 8、枚举名称使用前缀：enum
 * 9、用户定义类型名称使用前缀：type
 * 10、单个字母可以表示过程或函数中的整形变量
 * 11、变量缩写规则如下：
 *            b —— Boolean
 *            y —— Byte
 *            i —— Integer、Enum
 *            l —— Long
 *            s —— String
 *            d —— Date
 *            f —— Single、Double
 *            v —— Variant
 *            o —— Object
 *            t —— Type
 * 12、非特殊情况尽量不使用无类型变量
 * #==========================================================================#
 *
 * 系统状态组成结构
 * #==========================================================================#
 *  + 所有状态
 *       + 开始
 *           开始状态
 *       + 批示<1>
 *           批示状态1
 *           批示状态2
 *           ...
 *       + 通知<1>
 *           通知状态1
 *           通知状态2
 *           ...
 *       + 批示<2>
 *           批示状态3
 *           批示状态4
 *           ...
 *       ...
 * #==========================================================================#
 *
 * 全局函数定义
 */
public class MGlobal {
	/*
	 * 常量定义
	 */

	/* 软件版本：=0-标准版本 */
	public final static int WF_SOFT_VERSION = 1;
	/* 软件版本：=1-企业版本 */
	public final static int MaxTemplate = 10;

	/* 内容是否保存为Xml方式 */
	public final static Boolean DATA_SAVE_XML = true;

	/* 16开纸大小尺寸定义 - 宽度，单位：Pixel(象素) */
	public final static int const16PaperWidth = 800;
	/* 16开纸大小尺寸定义 - 高度，单位：Pixel(象素) */
	public final static int const16PaperHeight = 1122;
	/* 默认系统错误号 */
	public final static int constMyUnhandledError = 9999;
	/* Web端表格转化成HTML的分隔符号 */
	public final static String constDepartSign = "\n";

	// 验证系统是否已经注册
	public final static String constRegsvrClassID = "{CF34D8D9-F02F-4366-AEB9-97263F993973}";

	/*
	 * 本组件入口应用程序
	 */
	public MGlobal() {
		// Add Initialize This Component
	}

	/*
	 * 是否处于一连线位置
	 */
	public static Boolean isInlineArea(int al_PointX, int al_PointY, int al_PointX1, int al_PointY1, int al_PointX2,
			int al_PointY2) {
		int ll_MinLength = 6;

		if (al_PointX1 > al_PointX2) {
			if (al_PointX > al_PointX1 + ll_MinLength | al_PointX < al_PointX2 - ll_MinLength)
				return false;
		} else {
			if (al_PointX > al_PointX2 + ll_MinLength | al_PointX < al_PointX1 - ll_MinLength)
				return false;
		}

		if (al_PointY1 > al_PointY2) {
			if (al_PointY > al_PointY1 + ll_MinLength | al_PointY < al_PointY2 - ll_MinLength)
				return false;
		} else {
			if (al_PointY > al_PointY2 + ll_MinLength | al_PointY < al_PointY1 - ll_MinLength)
				return false;
		}

		if ((al_PointX1 - al_PointX2) * (al_PointY1 - al_PointY2) == 0)
			return true;

		return false;
	}

	/**
	 * 把VB的颜色转化成Web端显示的颜色
	 */
	public static String colorToWebColor(Color ao_Color) {
		String ls_RColor = Integer.toHexString(ao_Color.getRed());
		if (ls_RColor.length() < 2)
			ls_RColor = "0" + ls_RColor;
		String ls_GColor = Integer.toHexString(ao_Color.getGreen());
		if (ls_GColor.length() < 2)
			ls_GColor = "0" + ls_GColor;
		String ls_BColor = Integer.toHexString(ao_Color.getBlue());
		if (ls_BColor.length() < 2)
			ls_BColor = "0" + ls_BColor;

		return "&H" + ls_RColor + ls_GColor + ls_BColor;
	}

	/**
	 * 把Web端显示的颜色转化成VB的颜色
	 */
	public static Color webColorToColor(String as_Color) {
		String ls_RColor = "&H" + as_Color.substring(2, 4);
		String ls_GColor = "&H" + as_Color.substring(4, 6);
		String ls_BColor = "&H" + as_Color.substring(6, 8);
		// Color.getHSBColor(h, s, b)
		// &HFF00FF
		return null;
	}

	/**
	 * 把VB的颜色转化成Web端显示的颜色
	 */
	public static int colorToRGB(Color al_Color) {
		return al_Color.getRGB();
	}

	/**
	 * 把VB的颜色转化成Web端显示的颜色
	 */
	public static Color rGBToColor(int al_Color) {
		// return null;
		// Color.
		return new Color(al_Color);
		// al_Color.getRGB();
	}

	/*
	 * 把特殊字符转化成XML格式的字符
	 */
	public static String stringToXml(String as_Value) {
		if (as_Value == null | as_Value.equals(""))
			return as_Value;
		String ls_Value = as_Value.replace("&", "&amp;");
		ls_Value = ls_Value.replace("\'", "&apos;");
		ls_Value = ls_Value.replace("\"", "&quot;");
		ls_Value = ls_Value.replace(">", "&gt;");
		return ls_Value.replace("<", "&lt;");
	}

	// 把XML格式的字符还原成特殊字符
	public static String xmlToString(String as_Value) {
		if (as_Value == null | as_Value.equals(""))
			return as_Value;
		String ls_Value = as_Value.replace("&lt;", "<");
		ls_Value = ls_Value.replace("&gt;", ">");
		ls_Value = ls_Value.replace("&quot;", "\"");
		ls_Value = ls_Value.replace("&apos;", "\'");
		return ls_Value.replace("&amp;", "&");
	}

	// 把特殊字符转化成HTML格式的字符
	public static String stringToHtml(String as_Value) {
		if (as_Value == null | as_Value.equals(""))
			return as_Value;
		String ls_Value = as_Value.replace("&", "&amp;");
		ls_Value = ls_Value.replace("\'", "&apos;");
		ls_Value = ls_Value.replace("\"", "&quot;");
		ls_Value = ls_Value.replace(">", "&gt;");
		ls_Value = ls_Value.replace("<", "&lt;");
		ls_Value = ls_Value.replace("\n", "<br/>");
		return ls_Value.replace("  ", " &nbsp;");
	}

	/**
	 * 把HTML格式的字符还原成特殊字符
	 * 
	 * @param as_Value
	 * @return
	 */
	public static String htmlToString(String as_Value) {
		if (as_Value == null | as_Value.equals(""))
			return as_Value;
		String ls_Value = as_Value.replace("&nbsp;", " ");
		ls_Value = ls_Value.replace("<br/>", "\n");

		ls_Value = ls_Value.replace("&lt;", "<");
		ls_Value = ls_Value.replace("&gt;", ">");
		ls_Value = ls_Value.replace("&quot;", "\"");
		ls_Value = ls_Value.replace("&apos;", "\'");
		return ls_Value.replace("&amp;", "&");
	}

	/**
	 * 把XML格式的内容转换成可传递到前台的内容
	 * 
	 * @param as_XMLValue
	 * @return
	 */
	public static String xmlToClient(String as_XmlData) {
		if (as_XmlData == null | as_XmlData.equals(""))
			return as_XmlData;
		String ls_Value = as_XmlData.replace("\"", "\'");
		ls_Value = ls_Value.replace("\n", "&vbnl;");
		ls_Value = ls_Value.replaceAll("\n", "&chr10;");
		return ls_Value.replace("\r", "&chr13;");
	}

	/**
	 * 获取指定长度字符的内容
	 * 
	 * @param as_Value
	 * @param ai_Length
	 * @return
	 */
	public static String getStaidString(String as_Value, int ai_Length) {
		if (as_Value.length() * 2 <= ai_Length)
			return as_Value;
		for (int i = ai_Length / 2; i <= ai_Length; i++) {
			if (as_Value.substring(0, i).getBytes().length > ai_Length)
				return as_Value.substring(0, i - 1);
		}
		return as_Value.substring(0, ai_Length);
	}

	/**
	 * 获取字符串的字节数
	 * 
	 * @param as_Value
	 * @return
	 */
	public static int getStringLength(String as_Value) {
		if (as_Value == null || as_Value.equals(""))
			return 0;
		return as_Value.getBytes().length;
	}

	/**
	 * 比较一个字符串是否越界
	 * 
	 * @param as_Value
	 * @param ai_MaxLength
	 * @return
	 */
	public static boolean BeyondOfLength(String as_Value, int al_MaxLength) {
		if (getStringLength(as_Value) > al_MaxLength)
			return true;
		return false;
	}

	/**
	 * 增加空格
	 * 
	 * @param ai_Length
	 * @return
	 */
	public static String addSpace(int ai_Length) {
		String ls_Value = "";
		for (int i = 0; i < ai_Length; i++) {
			ls_Value += " ";
		}
		return ls_Value;
	}

	/**
	 * 增加固定长度的字符内容
	 * 
	 * @param ai_Length
	 *            长度
	 * @param as_Text
	 *            字符
	 * @return
	 */
	public static String addTexts(int ai_Length, String as_Text) {
		String ls_Value = "";
		for (int i = 0; i < ai_Length; i++) {
			ls_Value += as_Text;
		}
		return ls_Value;
	}

	/**
	 * 获取唯一标识
	 */
	public static String getGuid() {
		return java.util.UUID.randomUUID().toString();
	}

	/**
	 * 获取初始日期
	 * 
	 * @return
	 */
	public static Date getInitDate() {
		return stringToDate("1900-1-1 0:0:0");
	}

	/**
	 * 字符串转日期
	 * 
	 * @param as_Text
	 * @return
	 */
	public static Date stringToDate(String as_Text) {
		try {
			if (as_Text == null || as_Text.equals(""))
				return null;

			int li_Pos = as_Text.indexOf("AD");
			String ls_Text = as_Text.trim();
			SimpleDateFormat lo_Format = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
			if (li_Pos > -1) {
				ls_Text = ls_Text.substring(0, li_Pos) + "公元" + ls_Text.substring(li_Pos + "AD".length());// china
				lo_Format = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
			} else {
				if (ls_Text.indexOf(" ") == -1) {
					if (ls_Text.indexOf("-") > -1) {
						lo_Format = new SimpleDateFormat("yyyy-MM-dd");
					} else if (ls_Text.indexOf("/") > -1) {
						lo_Format = new SimpleDateFormat("yyyy/MM/dd");
					} else if (ls_Text.indexOf("年") > -1 && ls_Text.indexOf("月") > -1 && ls_Text.indexOf("日") > -1) {
						lo_Format = new SimpleDateFormat("yyyy年MM月dd日");
					} else {
						lo_Format = new SimpleDateFormat("yyyyMMddHHmmssZ");
					}
				} else {
					if (ls_Text.indexOf("/") > -1) {
						if (ls_Text.indexOf("am") > -1 || ls_Text.indexOf("pm") > -1) {
							lo_Format = new SimpleDateFormat("yyyy/MM/dd KK:mm:ss a");
						} else {
							if (ls_Text.length() - ls_Text.replaceAll(":", "").length() == 1) {
								lo_Format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
							} else {
								lo_Format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							}
						}
					} else if (ls_Text.indexOf("-") > -1) {
						if (ls_Text.indexOf("am") > -1 || ls_Text.indexOf("pm") > -1) {
							lo_Format = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
						} else {
							if (ls_Text.length() - ls_Text.replaceAll(":", "").length() == 1) {
								lo_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							} else {
								lo_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							}
						}
					} else {
						if (ls_Text.indexOf("am") > -1 || ls_Text.indexOf("pm") > -1) {
							lo_Format = new SimpleDateFormat("yyyy年MM月dd日 KK:mm:ss a");
						} else {
							if (ls_Text.length() - ls_Text.replaceAll(":", "").length() == 1) {
								lo_Format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
							} else {
								lo_Format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
							}
						}
					}
				}
			}
			ParsePosition lo_Pos = new ParsePosition(0);
			return lo_Format.parse(ls_Text, lo_Pos);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 日期转字符串
	 * 
	 * @param ad_Date
	 * @return
	 */
	public static String dateToString(Date ad_Date) {
		return dateToString(ad_Date, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 日期转字符串
	 * 
	 * @param ad_Date
	 * @param as_Format
	 * @return
	 */
	public static String dateToString(Date ad_Date, String as_Format) {
		try {
			if (ad_Date == null)
				return "";
			SimpleDateFormat lo_Format = new SimpleDateFormat(as_Format);
			return lo_Format.format(ad_Date);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static Date getNow() {
		return (new Date());
	}

	/**
	 * 获取当前数据库日期(短)
	 * 
	 * @return
	 */
	public static java.sql.Date getSqlNow() {
		return dateToSqlDate(new Date());
	}

	/**
	 * 获取当前数据库日期(长)
	 * 
	 * @return
	 */
	public static java.sql.Timestamp getSqlTimeNow() {
		return dateToSqlTime(new Date());
	}

	/**
	 * 获取与当前日期间隔的日期
	 * 
	 * @param ai_Day
	 *            天
	 * @param ai_Hour
	 *            小时
	 * @param ai_Minute
	 *            分钟
	 * @param ai_Second
	 *            秒
	 * @return
	 */
	public static Date getNow(int ai_Day, int ai_Hour, int ai_Minute, int ai_Second) {
		Date ld_Date = new Date();
		ld_Date.setTime(
				ld_Date.getTime() + 3600000 * ai_Day * 24 + 3600000 * ai_Hour + 60000 * ai_Minute + 1000 * ai_Second);
		return ld_Date;
	}

	/**
	 * 获取与当前日期间隔的日期
	 * 
	 * @param ai_Day
	 *            天
	 * @param ai_Hour
	 *            小时
	 * @param ai_Minute
	 *            分钟
	 * @param ai_Second
	 *            秒
	 * @return
	 */
	public static Date getDate(Date ad_Date, int ai_Day, int ai_Hour, int ai_Minute, int ai_Second) {
		Date ld_Date = new Date();
		ld_Date.setTime(
				ad_Date.getTime() + 3600000 * ai_Day * 24 + 3600000 * ai_Hour + 60000 * ai_Minute + 1000 * ai_Second);
		return ld_Date;
	}

	/**
	 * 比较两个日期
	 * 
	 * @param ad_Date1
	 * @param ad_Date2
	 * @return 返回： -1 - ad_Date1小于ad_Date2； 0 - ad_Date1等于ad_Date2； 1 -
	 *         ad_Date1大于ad_Date2；
	 */
	public static int compareDate(Date ad_Date1, Date ad_Date2) {
		long ll_Date1 = ad_Date1.getTime();
		long ll_Date2 = ad_Date2.getTime();
		if (ll_Date1 == ll_Date2)
			return 0;
		if (ll_Date1 > ll_Date2)
			return 1;
		return -1;
	}

	/**
	 * 返回今天为星期几
	 * 
	 * @return =0-星期天；=1-星期一；=2-星期二；=3-星期三；=4-星期四；=5-星期五；=6-星期六；
	 */
	public static int getWeek() {
		Calendar lo_Calendar = Calendar.getInstance();
		int i = lo_Calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (i < 0)
			i = 0;
		return i;
	}

	/**
	 * 返回日期为星期几
	 * 
	 * @param ad_Date
	 *            日期
	 * @return =0-星期天；=1-星期一；=2-星期二；=3-星期三；=4-星期四；=5-星期五；=6-星期六；
	 */
	public static int getWeek(Date ad_Date) {
		Calendar lo_Calendar = Calendar.getInstance();
		lo_Calendar.setTime(ad_Date);
		int i = lo_Calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (i < 0)
			i = 0;
		return i;
	}

	/**
	 * 返回日期为星期几(文字内容)
	 * 
	 * @param ad_Date
	 *            日期
	 * @param ai_Type
	 *            返回类型：=0-中文完整，如：星期二；=1-英文完整，如：Firday；=2-中文简写，如：三；=3-英文简写，如：Sun；
	 * @return =0-星期天；=1-星期一；=2-星期二；=3-星期三；=4-星期四；=5-星期五；=6-星期六；
	 */
	public static String getWeekName(Date ad_Date, int ai_Type) {
		String ls_Text;
		switch (ai_Type) {
		case 1:
			ls_Text = "Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday";
			break;
		case 2:
			ls_Text = "天,一,二,三,四,五,六";
			break;
		case 3:
			ls_Text = "Sun,Mon,Tue,Wed,Thu,Fri,Sat";
			break;
		default:
			ls_Text = "星期天,星期一,星期二,星期三,星期四,星期五,星期六";
			break;
		}
		return ls_Text.split(",")[getWeek(ad_Date)];
	}

	/**
	 * 返回今天为星期几(文字内容)
	 * 
	 * @param ai_Type
	 *            返回类型：=0-中文完整，如：星期二；=1-英文完整，如：Firday；=2-中文简写，如：三；=3-英文简写，如：Sun；
	 * @return =0-星期天；=1-星期一；=2-星期二；=3-星期三；=4-星期四；=5-星期五；=6-星期六；
	 */
	public static String getWeekName(int ai_Type) {
		String ls_Text;
		switch (ai_Type) {
		case 1:
			ls_Text = "Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday";
			break;
		case 2:
			ls_Text = "天,一,二,三,四,五,六";
			break;
		case 3:
			ls_Text = "Sun,Mon,Tue,Wed,Thu,Fri,Sat";
			break;
		default:
			ls_Text = "星期天,星期一,星期二,星期三,星期四,星期五,星期六";
			break;
		}
		return ls_Text.split(",")[getWeek()];
	}

	/**
	 * 
	 * @param as_Text
	 * @param ai_Length
	 * @return
	 */
	public static String left(String as_Text, int ai_Length) {
		if (as_Text == null || as_Text.length() <= ai_Length)
			return as_Text;
		return as_Text.substring(0, ai_Length);
	}

	/**
	 * 
	 * @param as_Text
	 * @param ai_Length
	 * @return
	 */
	public static String right(String as_Text, int ai_Length) {
		if (as_Text == null || as_Text.length() <= ai_Length)
			return as_Text;
		return as_Text.substring(as_Text.length() - ai_Length, as_Text.length());
	}

	/**
	 * 获取硬盘序列号
	 * 
	 * @return
	 */
	public static String getHdSerialInfo() {
		String line = "";
		String HdSerial = "";// 记录硬盘序列号
		try {
			Process proces = Runtime.getRuntime().exec("cmd /c dir c:");// 获取命令行参数
			BufferedReader buffreader = new BufferedReader(new InputStreamReader(proces.getInputStream()));
			while ((line = buffreader.readLine()) != null) {
				if (line.indexOf("卷的序列号是 ") != -1) { // 读取参数并获取硬盘序列号
					HdSerial = line.substring(line.indexOf("卷的序列号是 ") + "卷的序列号是 ".length(), line.length());
					break;
					// logger.debug(HdSerial);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return HdSerial;// 返回硬盘序列号
	}

	/**
	 * 校验字符串是否合法
	 * 
	 * @param as_Text
	 * @return
	 */
	public static Boolean validString(String as_Text) {
		return validString(as_Text, "~`!@#$%^&*()-+=\\|?/>.<,{}[]\"\':;");
	}

	/**
	 * 校验字符串是否合法
	 * 
	 * @param as_Text
	 *            被检验字符串
	 * @param as_InValidString
	 *            非法字符，缺省：~`!@#$%^&*()-+=\|?/>.<,{}[]"':;
	 * @return
	 */
	public static Boolean validString(String as_Text, String as_InValidString) {
		if (as_Text == null || as_Text.equals(""))
			return true;
		for (int i = 0; i < as_Text.length(); i++) {
			if (as_InValidString.indexOf(as_Text.substring(i, i + 1)) > -1) {
				// 错误[1065]：字符串中存在非法的字符
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否是布尔型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isBool(String as_Value) {
		if (as_Value.toLowerCase().equals("false") || as_Value.toLowerCase().equals("true"))
			return true;
		return false;
	}

	/**
	 * 判断字符串是否是字节型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isByte(String as_Value) {
		try {
			Byte.parseByte(as_Value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是短整型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isShort(String as_Value) {
		try {
			Short.parseShort(as_Value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是整型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isInt(String as_Value) {
		try {
			Integer.parseInt(as_Value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是长整型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isLong(String as_Value) {
		try {
			Long.parseLong(as_Value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是日期型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isDate(String as_Value) {
		try {
			if (stringToDate(as_Value) == null)
				return false;
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点（单精度）型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isFloat(String as_Value) {
		try {
			Float.parseFloat(as_Value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是双精度型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isDouble(String as_Value) {
		try {
			Double.parseDouble(as_Value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是数值型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean isNumber(String as_Value) {
		try {
			if (as_Value == null || as_Value.equals(""))
				return false;
			for (int i = 0; i < as_Value.length(); i++) {
				if (("0123456789").indexOf(as_Value.substring(i, i + 1)) == -1)
					return false;
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 判断字符串是否为空串
	 * 
	 * @param as_Value
	 *            字符串定义
	 * @return =true-空串；=false-非空串；
	 */
	public static boolean isEmpty(String as_Value) {
		if (as_Value == null || as_Value.equals(""))
			return true;
		return false;
	}

	/**
	 * 判断字符串是否为非空串
	 * 
	 * @param as_Value
	 *            字符串定义
	 * @return =true-非空串；=false-空串；
	 */
	public static boolean isExist(String as_Value) {
		if (as_Value == null || as_Value.equals(""))
			return false;
		return true;
	}

	/**
	 * 字符串转成布尔型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean getBool(String as_Value) {
		return Boolean.parseBoolean(as_Value);
	}

	/**
	 * 字符串转成布尔型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static boolean getBoolean(String as_Value) {
		return Boolean.parseBoolean(as_Value);
	}

	/**
	 * 字符串转成字节型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static Byte getByte(String as_Value) {
		try {
			return Byte.parseByte(as_Value);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 字符串转成短整型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static short getShort(String as_Value) {
		try {
			return Short.parseShort(as_Value);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 字符串转成整型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static int getInt(String as_Value) {
		try {
			return Integer.parseInt(as_Value);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 字符串转成长整型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static Long getLong(String as_Value) {
		try {
			return Long.parseLong(as_Value);
		} catch (Exception ex) {
			return Long.parseLong("0");
		}
	}

	/**
	 * 字符串转成浮点（单精度）型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static float getFloat(String as_Value) {
		try {
			return Float.parseFloat(as_Value);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * 字符串转成双精度型
	 * 
	 * @param as_Value
	 * @return
	 */
	public static double getDouble(String as_Value) {
		try {
			return Double.parseDouble(as_Value);
		} catch (Exception ex) {
			return 0;
		}
	}

	/**
	 * ASCII转字符
	 * 
	 * @param al_Ascii
	 * @return
	 */
	public static char chr(int al_Ascii) {
		return (char) al_Ascii;
	}

	/**
	 * 字符转ASCII
	 * 
	 * @param ac_Char
	 * @return
	 */
	public static int asc(char ac_Char) {
		return (int) ac_Char;
	}

	/**
	 * ASCII数组转字符串
	 * 
	 * @param al_Asciis
	 * @return
	 */
	public static String ascii2String(int[] al_Asciis) {
		StringBuffer lo_Buffer = new StringBuffer();
		for (int i = 0; i < al_Asciis.length; i++) {
			lo_Buffer.append(chr(al_Asciis[i]));
		}
		return lo_Buffer.toString();
	}

	/**
	 * ASCII内容串转字符串
	 * 
	 * @param as_Asciis
	 * @param as_Split
	 *            分隔符号
	 * @return
	 */
	public static String ascii2String(String as_Asciis, String as_Split) {
		String[] ls_Asciis = as_Asciis.split(as_Split);
		StringBuffer lo_Buffer = new StringBuffer();
		for (int i = 0; i < ls_Asciis.length; i++) {
			if (ls_Asciis[i] != null && ls_Asciis[i] != "")
				lo_Buffer.append(chr(Integer.parseInt(ls_Asciis[i])));
		}
		return lo_Buffer.toString();
	}

	/**
	 * 字符串转换为ASCII码数组
	 * 
	 * @param as_Value
	 * @return
	 */
	public static int[] string2Ascii(String as_Value) {//
		if (as_Value == null || "".equals(as_Value)) {
			return null;
		}

		char[] lc_Chars = as_Value.toCharArray();
		int[] ll_Array = new int[lc_Chars.length];

		for (int i = 0; i < lc_Chars.length; i++) {
			ll_Array[i] = asc(lc_Chars[i]);
		}
		return ll_Array;
	}

	/**
	 * 普通日期转成数据库日期(短)
	 * 
	 * @param ad_Date
	 * @return
	 */
	public static java.sql.Date dateToSqlDate(Date ad_Date) {
		if (ad_Date == null)
			return null;
		return (new java.sql.Date(ad_Date.getTime()));
	}

	/**
	 * 数据库日期(短)转成普通日期
	 * 
	 * @param ad_Date
	 * @return
	 */
	public static Date sqlDateToDate(java.sql.Date ad_Date) {
		if (ad_Date == null)
			return null;
		return (new Date(ad_Date.getTime()));
	}

	public static Date otherDateToDate(Object ad_Date) {
		if (ad_Date == null)
			return null;
		if (ad_Date instanceof java.sql.Timestamp)
			return sqlTimeToDate((java.sql.Timestamp) ad_Date);
		if (ad_Date instanceof java.sql.Date)
			return sqlDateToDate((java.sql.Date) ad_Date);
		return null;
	}

	/**
	 * 普通日期转成数据库日期(长)
	 * 
	 * @param ad_Date
	 * @return
	 */
	public static java.sql.Timestamp dateToSqlTime(Date ad_Date) {
		if (ad_Date == null)
			return null;
		return (new java.sql.Timestamp(ad_Date.getTime()));
	}

	/**
	 * 数据库日期(长)转成普通日期
	 * 
	 * @param ad_Date
	 * @return
	 */
	public static Date sqlTimeToDate(java.sql.Timestamp ad_Date) {
		if (ad_Date == null)
			return null;
		return (new Date(ad_Date.getTime()));
	}

	/**
	 * 获取数据类型
	 * 
	 * @param ao_Object
	 * @return
	 */
	public static String getType(Object ao_Object) {
		if (ao_Object == null)
			return null;
		String ls_Class = ao_Object.getClass().toString();
		int i = ls_Class.lastIndexOf(".");
		return ls_Class.substring(i + 1, ls_Class.length());
	}

	/**
	 * 获取两个日期之间的间隔天数
	 * 
	 * @return
	 */
	public static int getTwoDateDay(Date ad_Date1, Date ad_Date2) {
		long ll_Second = (ad_Date2.getTime() - ad_Date1.getTime()) / 1000;
		return ((int) (ll_Second / 3600 / 24));
	}

	/**
	 * 获取两个日期之间的间隔小时
	 * 
	 * @return
	 */
	public static int getTwoDateHour(Date ad_Date1, Date ad_Date2) {
		long ll_Second = (ad_Date2.getTime() - ad_Date1.getTime()) / 1000;
		return ((int) (ll_Second / 3600));
	}

	/**
	 * 获取两个日期之间的间隔分钟
	 * 
	 * @return
	 */
	public static int getTwoDateMinute(Date ad_Date1, Date ad_Date2) {
		long ll_Second = (ad_Date2.getTime() - ad_Date1.getTime()) / 1000;
		return ((int) (ll_Second / 60));
	}

	/**
	 * 获取两个日期之间的间隔秒
	 * 
	 * @return
	 */
	public static int getTwoDateSecond(Date ad_Date1, Date ad_Date2) {
		long ll_Second = (ad_Date2.getTime() - ad_Date1.getTime()) / 1000;
		return ((int) ll_Second);
	}

	/**
	 * 获取数据库存储字段内容
	 * 
	 * @param as_Value
	 *            原始值
	 * @return
	 */
	public static String getDbValue(String as_Value) {
		if (as_Value == null || as_Value.equals(""))
			return null;
		return as_Value;
	}

	/**
	 * 获取结果集中某个字符串字段的内容(布尔型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static boolean readBoolean(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return false;
		String ls_Value = ((String) ao_Set.get(as_Name)).toLowerCase();
		return (ls_Value.equals("true") || ls_Value.equals("1"));
	}

	/**
	 * 获取结果集中某个字符串字段的内容(短整型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static short readShort(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return 0;
		return (Short) ao_Set.get(as_Name);
	}

	/**
	 * 获取结果集中某个字符串字段的内容(整型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static int readInt(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return 0;
		if (ao_Set.get(as_Name) instanceof Short)
			return (Short) ao_Set.get(as_Name);
		else if (ao_Set.get(as_Name) instanceof Integer)
			return (Integer) ao_Set.get(as_Name);
		return 0;
	}

	/**
	 * 获取结果集中某个字符串字段的内容(长整型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static long readLong(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return 0;
		return (Long) ao_Set.get(as_Name);
	}

	public static Object readObject(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		return ao_Set.get(as_Name);
	}

	/**
	 * 获取结果集中某个字符串字段的内容(日期型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static Date readDate(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return getInitDate();
		return otherDateToDate(ao_Set.get(as_Name));
	}

	public static byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	public static byte[] readBytes(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return null;
		return toByteArray(readObject(ao_Set, as_Name));
	}

	public static void updateDate(List<Map<String, Object>> ao_Set, String as_Field, Date as_Value)
			throws SQLException {
		for (Map<String, Object> row : ao_Set) {
			writeDate(row, as_Field, as_Value);
		}
	}

	public static void updateBytes(Map<String, Object> ao_Set, String as_Field, byte[] bs) {
		ao_Set.put(as_Field, bs);
	}

	public static void updateInt(Map<String, Object> ao_Set, String as_Field, int as_Value) {
		writeInt(ao_Set, as_Field, as_Value);
	}

	public static void writeInt(Map<String, Object> row, String as_Name, int as_Value) {
		row.put(as_Name, as_Value);
	}

	public static void updateString(Map<String, Object> ao_Set, String as_Field, String as_Value) {
		writeString(ao_Set, as_Field, as_Value);
	}

	public static void updateTimestamp(Map<String, Object> ao_Set, String as_Field, java.sql.Timestamp as_Value) {
		ao_Set.put(as_Field, as_Value);
	}

	public static void updateObject(Map<String, Object> ao_Set, String as_Field, Object as_Value) {
		ao_Set.put(as_Field, as_Value);
	}

	public static void updateRow(List<Map<String, Object>> ao_Set) {

	}

	/**
	 * 获取结果集中某个字符串字段的内容(日期时间型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static Date readTime(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return getInitDate();
		return sqlTimeToDate((java.sql.Timestamp) ao_Set.get(as_Name));
	}

	/**
	 * 设置结果集中某个字符串字段的内容(日期型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 *            字段名称
	 * @param ad_Value
	 *            字段值，如果为初始值：1900-1-1则在更新数据库时为null
	 * @return
	 * @throws SQLException
	 */
	public static void writeDate(Map<String, Object> ao_Set, String as_Name, Date ad_Value) throws SQLException {
		if (ad_Value == null) {
			ao_Set.put(as_Name, null);
			return;
		}
		if (dateToString(ad_Value, "yyyy-M-d").equals("1900-1-1"))
			ao_Set.put(as_Name, null);
		else
			ao_Set.put(as_Name, MGlobal.dateToSqlDate(ad_Value));
	}

	/**
	 * 设置结果集中某个字符串字段的内容(日期时间型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 *            字段名称
	 * @param ad_Value
	 *            字段值，如果为初始值：1900-1-1 0:0:0则在更新数据库时为null
	 * @return
	 * @throws SQLException
	 */
	public static void writeTime(Map<String, Object> ao_Set, String as_Name, Date ad_Value) throws SQLException {
		if (ad_Value == null) {
			ao_Set.put(as_Name, null);
			return;
		}
		if (dateToString(ad_Value, "yyyy-M-d h:m:s").equals("1900-1-1 0:0:0"))
			ao_Set.put(as_Name, null);
		else
			ao_Set.put(as_Name, MGlobal.dateToSqlTime(ad_Value));
	}

	/**
	 * 设置结果集中某个字符串字段的内容(日期型)
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_Index
	 *            更新字段索引值
	 * @param ad_Value
	 *            字段值，如果为初始值：1900-1-1 0:0:0则在更新数据库时为null
	 * @return
	 * @throws SQLException
	 */
	public static void writeDate(PreparedStatement ao_State, int ai_Index, Date ad_Value) throws SQLException {
		if (ad_Value == null) {
			ao_State.setDate(ai_Index, null);
		} else if (dateToString(ad_Value, "yyyy-M-d").equals("1900-1-1")) {
			ao_State.setDate(ai_Index, null);
		} else {
			ao_State.setDate(ai_Index, MGlobal.dateToSqlDate(ad_Value));
		}
	}

	/**
	 * 设置结果集中某个字符串字段的内容(日期时间型)
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_Index
	 *            更新字段索引值
	 * @param ad_Value
	 *            字段值，如果为初始值：1900-1-1 0:0:0则在更新数据库时为null
	 * @return
	 * @throws SQLException
	 */
	public static void writeTime(PreparedStatement ao_State, int ai_Index, Date ad_Value) throws SQLException {
		if (ad_Value == null) {
			ao_State.setTimestamp(ai_Index, null);
		} else if (dateToString(ad_Value, "yyyy-M-d h:m:s").equals("1900-1-1 0:0:0")) {
			ao_State.setTimestamp(ai_Index, null);
		} else {
			ao_State.setTimestamp(ai_Index, MGlobal.dateToSqlTime(ad_Value));
		}
	}

	/**
	 * 获取结果集中某个字符串字段的内容(单精度型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static float readFloat(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return 0;
		return (Float) ao_Set.get(as_Name);
	}

	/**
	 * 获取结果集中某个字符串字段的内容(双精度型)
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static double readDouble(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return 0;
		return (Double) ao_Set.get(as_Name);
	}

	/**
	 * 获取结果集中某个字符串字段的内容，如果为空（null），则返回空串
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static String readString(Map<String, Object> ao_Set, String as_Name) throws SQLException {
		if (ao_Set.get(as_Name) == null)
			return "";
		if (ao_Set.get(as_Name) instanceof Short)
			return String.valueOf((Short) ao_Set.get(as_Name));
		if (ao_Set.get(as_Name) instanceof Integer)
			return String.valueOf((Integer) ao_Set.get(as_Name));
		if (ao_Set.get(as_Name) instanceof String)
			return (String) ao_Set.get(as_Name);
		return "";
	}

	public static String readString(Map<String, Object> ao_Set, int field_Index) throws SQLException {
		int i = 0;
		for (String key : ao_Set.keySet()) {
			if (i == (field_Index - 1))
				return String.valueOf(ao_Set.get(key));
		}
		return "";
	}

	/**
	 * 设置结果集中某个字符串字段的内容，如果为空串，则设置为空（null）
	 * 
	 * @param ao_Set
	 *            结果集值
	 * @param as_Name
	 * @return
	 * @throws SQLException
	 */
	public static void writeString(Map<String, Object> row, String as_Name, String as_Value) {
		if (as_Value == null || as_Value.equals("")) {
			row.put(as_Name, null);
		} else {
			row.put(as_Name, as_Value);
		}
	}

	/**
	 * 设置结果集中某个字符串字段的内容，如果为空串，则设置为空（null）
	 * 
	 * @param ao_State
	 *            更新状态对象
	 * @param ai_Index
	 *            更新字段索引值
	 * @param as_Value
	 *            更新值
	 * @return
	 * @throws SQLException
	 */
	public static void writeString(PreparedStatement ao_State, int ai_Index, String as_Value) throws SQLException {
		if (as_Value == null || as_Value.equals("")) {
			ao_State.setString(ai_Index, null);
		} else {
			ao_State.setString(ai_Index, as_Value);
		}
	}

	/**
	 * 获取字符串长度 由于Java是基于Unicode编码的，因此，一个汉字的长度为1，而不是2。
	 * 但有时需要以字节单位获得字符串的长度。例如，“123abc长城”按字节长度计算是10，而按Unicode计算长度是8。
	 * 为了获得10，需要从头扫描根据字符的Ascii来获得具体的长度
	 * 。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255。
	 * 因此，可以编写如下的方法来获得以字节为单位的字符串长度。
	 */
	public static int getWordCount(String as_Text) {
		if (as_Text == null || as_Text.equals(""))
			return 0;
		int ll_Length = 0;
		for (int i = 0; i < as_Text.length(); i++) {
			int ascii = Character.codePointAt(as_Text, i);
			if (ascii >= 0 && ascii <= 255)
				ll_Length++;
			else
				ll_Length += 2;

		}
		return ll_Length;
	}

	/**
	 * 通过正则表达式获取字符串长度 基本原理是将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**，或其他的也可以）。<br>
	 * 这样就可以直接例用length方法获得字符串的字节长度了
	 */
	public static int getWordCountRegex(String as_Text) {
		if (as_Text == null || as_Text.equals(""))
			return 0;
		return as_Text.replaceAll("[^\\x00-\\xff]", "**").length();
	}

	/**
	 * 按特定的编码格式获取长度
	 * 
	 * @param as_Text
	 * @param as_Code
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static int getWordCountCode(String as_Text, String as_Code) throws UnsupportedEncodingException {
		return as_Text.getBytes(as_Code).length;
	}

	/**
	 * 
	 * @param as_Array
	 * @param as_Split
	 * @return
	 */
	public static String join(String[] as_Array, String as_Split) {
		if (as_Array == null)
			return "";
		if (as_Array.length == 0)
			return "";
		String ls_Value = "";
		for (int i = 0; i < as_Array.length; i++) {
			if (!as_Array[i].equals("") || i < as_Array.length - 1)
				ls_Value += as_Array[i] + as_Split;
		}
		return ls_Value;
	}

	/**
	 * 替换字符串中的所有内容
	 * 
	 * @param as_Content
	 *            字符串
	 * @param as_Replace
	 *            被替换内容
	 * @param as_Text
	 *            替换内容
	 * @return
	 */
	public static String replace(String as_Content, String as_Replace, String as_Text) {
		if (as_Content == null || as_Content.equals(""))
			return "";
		String ls_Content = as_Content.replace(as_Replace, as_Text);
		while (ls_Content.indexOf(as_Replace) > -1) {
			ls_Content = ls_Content.replace(as_Replace, as_Text);
		}
		return ls_Content;
	}

	/**
	 * 获取SQL语句中日常使用到的IN条件的操作
	 * 
	 * @param as_Texts
	 *            内容连接串，使用“;”分隔
	 * @return
	 */
	public static String getSqlInText(String as_Texts) {
		if (as_Texts == null || as_Texts.equals(""))
			return "";
		String ls_Text = as_Texts.replaceAll(";", ",").trim();
		if (ls_Text.substring(ls_Text.length() - 1).equals(","))
			ls_Text = ls_Text.substring(0, ls_Text.length() - 1);
		return ls_Text;
	}

	/**
	 * 获取SQL语句中日常使用到的IN条件的操作
	 * 
	 * @param as_Texts
	 *            内容连接串，使用as_Split分隔
	 * @param as_Split
	 *            分隔符号
	 * @param ab_Number
	 *            是否为数值类型，如果为否，需要增加“','”
	 * @return
	 */
	public static String getSqlInText(String as_Texts, String as_Split, boolean ab_Number) {
		if (as_Texts == null || as_Texts.equals(""))
			return "";
		String ls_Text = "";
		if (ab_Number) {
			ls_Text = as_Texts.replaceAll(as_Split, ",").trim();
			if (ls_Text.substring(ls_Text.length() - 1).equals(","))
				ls_Text = ls_Text.substring(0, ls_Text.length() - 1);
		} else {
			ls_Text = as_Texts.replaceAll(as_Split, "','").trim();
			if (ls_Text.substring(ls_Text.length() - 3).equals("','"))
				ls_Text = "'" + ls_Text.substring(0, ls_Text.length() - 2);
		}
		return ls_Text;
	}

	/**
	 * 获取当前日期的年度
	 * 
	 * @return
	 */
	public static int getYear() {
		return getDateValue(getNow(), "yyyy");
	}

	/**
	 * 获取日期的年度
	 * 
	 * @param ad_Date
	 *            日期
	 * @return
	 */
	public static int getYear(Date ad_Date) {
		return getDateValue(ad_Date, "yyyy");
	}

	/**
	 * 获取当前日期的月份
	 * 
	 * @return
	 */
	public static int getMonth() {
		return getDateValue(getNow(), "MM");
	}

	/**
	 * 获取日期的月份
	 * 
	 * @param ad_Date
	 *            日期
	 * @return
	 */
	public static int getMonth(Date ad_Date) {
		return getDateValue(ad_Date, "MM");
	}

	/**
	 * 获取当前日期的天数
	 * 
	 * @return
	 */
	public static int getDay() {
		return getDateValue(getNow(), "dd");
	}

	/**
	 * 获取日期的天数
	 * 
	 * @param ad_Date
	 *            日期
	 * @return
	 */
	public static int getDay(Date ad_Date) {
		return getDateValue(ad_Date, "dd");
	}

	/**
	 * 获取当前日期的小时
	 * 
	 * @return
	 */
	public static int getHour() {
		return getDateValue(getNow(), "hh");
	}

	/**
	 * 获取日期的小时数
	 * 
	 * @param ad_Date
	 *            日期
	 * @return
	 */
	public static int getHour(Date ad_Date) {
		return getDateValue(ad_Date, "hh");
	}

	/**
	 * 获取当前日期的分钟
	 * 
	 * @return
	 */
	public static int getMinute() {
		return getDateValue(getNow(), "mm");
	}

	/**
	 * 获取日期的分钟
	 * 
	 * @param ad_Date
	 *            日期
	 * @return
	 */
	public static int getMinute(Date ad_Date) {
		return getDateValue(ad_Date, "mm");
	}

	/**
	 * 获取当前日期的秒数
	 * 
	 * @return
	 */
	public static int getSecond() {
		return getDateValue(getNow(), "ss");
	}

	/**
	 * 获取日期的秒数
	 * 
	 * @param ad_Date
	 *            日期
	 * @return
	 */
	public static int getSecond(Date ad_Date) {
		return getDateValue(ad_Date, "ss");
	}

	/**
	 * 获取日期的对应值
	 * 
	 * @param ad_Date
	 *            日期
	 * @param as_Format
	 *            格式：yyyy、MM、dd、hh、mm、ss、...
	 * @return
	 */
	public static int getDateValue(Date ad_Date, String as_Format) {
		SimpleDateFormat lo_Formatter = new SimpleDateFormat(as_Format);
		return Integer.parseInt(lo_Formatter.format(ad_Date));
	}

}
