package cn.org.awcp.venson.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 */
public class MD5Util {
	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(MD5Util.class);
	protected static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f'};

	protected static MessageDigest messagedigest = null;

	private MD5Util() {
	}

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			logger.info("ERROR", nsaex);
		}
	}

	/**
	 * 功能：加盐版的MD5.返回格式为MD5(密码+{盐值})
	 * 
	 */
	public static String getMD5StringWithSalt(String password, String salt) {
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException("password不能为null");
		}
		if (StringUtils.isBlank(salt)) {
			throw new IllegalArgumentException("salt不能为空");
		}
		if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
			throw new IllegalArgumentException("salt中不能包含 { 或者 }");
		}
		StringBuffer saltBuffer = new StringBuffer();
		saltBuffer.append(password);
		saltBuffer.append("{");
		saltBuffer.append(salt);
		saltBuffer.append("}");
		return getMD5String(saltBuffer.toString());
	}

	/**
	 * 功能：得到文件的md5值。
	 * 
	 */
	public static String getFileMD5String(File file) {
		try {
			return getFileMD5String(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 功能：得到文件的md5值。
	 * 
	 */
	public static String getFileMD5String(InputStream file) {
		if (file == null) {
			return null;
		}
		try {
			messagedigest.update(IOUtils.toByteArray(file));
			return bufferToHex(messagedigest.digest());
		} catch (IOException e) {
			return null;
		} finally {
			IOUtils.closeQuietly(file);
		}

	}

	/**
	 * 功能：得到文件的md5值。
	 * 
	 */
	public static String getFileMD5String(byte[] file) {
		if (file == null) {
			return null;
		}
		messagedigest.update(file);
		return bufferToHex(messagedigest.digest());

	}

	/**
	 * 功能：得到一个字符串的MD5值。
	 * 
	 */
	public static String getMD5String(String str) {
		return getMD5String(str.getBytes());

	}

	private static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte[] bytes) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte[] bytes, int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

}
