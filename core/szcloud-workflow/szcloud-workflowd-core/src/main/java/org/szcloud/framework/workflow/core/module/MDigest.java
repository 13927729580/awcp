package org.szcloud.framework.workflow.core.module;

import org.omg.CORBA.INTERNAL;
import org.szcloud.framework.workflow.core.module.MD5Utils;

public class MDigest {

	// 变量定义
	private static int[] State = new int[5];

	private static int ByteCounter;

	private static byte[] ByteBuffer = new byte[64];

	// 加密文件
	// strFileName 文件名称
	public static String DigestFileToHexStr(String strFileName)
			throws Exception {
		String str = MD5Utils.createFileMD5(strFileName);
		return str;
	}

	// 加密字符串
	// strSourceString 被加密内容
	public static String DigestStrToHexStr(String strSourceString)
			throws Exception {
		String str = MD5Utils.createMD5(strSourceString);
		return str;
	}

	// 字符串转化成数组
	private static byte[] StringToArray(String strInString) {
		byte[] returnValue = null;// default(byte[]);
		int i = 0;
		byte[] byteBuffer = null;
		byteBuffer = new byte[strInString.length() + 1];
		for (i = 0; i <= strInString.length() - 1; i++) {
			byteBuffer[i] = Byte.parseByte(strInString.substring(i + 1 - 1, 1));
		}
		return byteBuffer;
	}

	// 内容相加
	public static String GetValues() {
		String returnValue = "";
		returnValue = LongToString(State[1]) + LongToString(State[2])
				+ LongToString(State[3]) + LongToString(State[4]);
		return returnValue;
	}

	//
	// Convert a Long to a Hex string
	// 转化长整型为16进制
	private static String LongToString(int Num) {
		String s = Integer.toHexString(Num).toUpperCase();
		return s;
	}

	// 解密
	// as_Content 被解密内容
	public static String DecodeString(String as_Content) {
		return as_Content;
	}

	// 解密
	// as_Content 被解密内容
	public static String DecodeString2(String as_Content) {
		try {
			if (as_Content.length() == 0) {
				return "";
			}
			byte[] lb_Buff = null;
			int i = 0;
			int j = 0;
			byte lb_Byte2 = 0;
			byte lb_Byte1 = 0;
			byte lb_Byte3 = 0;
			String ls_Value1 = "";
			String ls_Key = "";
			String ls_Value2 = "";

			ls_Key = "A7EVc9abKhimH34RSTUuv56jnoYZ0stIJWpqr12FGklxXQ8BCDwLMNdefgOPyz";
			j = (int) 0;

			for (i = 1; i <= as_Content.length(); i += 2) {
				ls_Value1 = as_Content.substring(i - 1, 1);
				ls_Value2 = as_Content.substring(i + 1 - 1, 1);
				lb_Byte2 = (byte) (ls_Key.indexOf(ls_Value1) + 0);
				lb_Byte3 = (byte) (ls_Key.indexOf(ls_Value2) + 0);
				// lb_Byte1 = lb_Byte3 / Math.pow(2, 3);
				// lb_Byte3 = lb_Byte3 - lb_Byte1 * Math.pow(2, 3);
				// Array.Resize(ref lb_Buff, j + 1);
				// lb_Buff[j] = lb_Byte1 * ls_Key.length() + lb_Byte2;
				// lb_Buff[j] = System.Convert.ToByte(lb_Buff[j] ^ lb_Byte3);
				j++;
			}

			// UPGRADE_ISSUE: 常量 vbUnicode 未升级。
			// 单击以获得更多信息:“ms-help://MS.VSCC.v80/dv_commoner/local/redirect.htm?keyword="55B59875-9A95-4B71-9D6A-7C294BF7139D"”
			// return
			// Strings.StrConv(System.Convert.ToString(System.Text.UnicodeEncoding.Unicode.GetString(lb_Buff)),
			// vbUnicode, 0);
		} catch (Exception e) {
			// goto l_Err;
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param as_Content
	 * @return
	 */
	public static String EncodeString(String as_Content) {
		return as_Content;
	}

}
