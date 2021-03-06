package cn.org.awcp.core.utils;

//checked by ayesd
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;

/**
 * The security
 */
public class Security {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(Security.class);
	// public final static String ENCRYPTION_BASE64 = "base64";

	/**
	 * Encrypt the string with the MD5 arithmetic
	 * 
	 * @param s
	 *            Normal message that you want to convert.
	 * @return The Encrypt string.
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	public static String encodeToMD5(String s) throws NoSuchAlgorithmException {
		if (s == null)
			return null;
		StringBuffer digstr = new StringBuffer();
		MessageDigest MD = MessageDigest.getInstance("MD5");

		byte[] oldbyte = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			oldbyte[i] = (byte) s.charAt(i);
		}
		MD.update(oldbyte);
		byte[] newbyte = MD.digest(oldbyte);
		for (int i = 0; i < newbyte.length; i++) {
			digstr.append(newbyte[i]);
		}

		return digstr.toString();
	}

	/**
	 * BASE64 编码
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeToBASE64(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	/**
	 * BASE64 解码
	 * 
	 * @param s
	 * @return
	 */
	public static String decodeBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

	public static String encryptPassword(String password) {
		if (password == null) {
			return null;
		}
		return Blowfish.getInstance().encryptString(password);
	}

	/**
	 * Returns a decrypted version of the encrypted password. Encryption is
	 * performed using the Blowfish algorithm. The encryption key is stored as
	 * the Jive property "passwordKey". If the key is not present, it will be
	 * automatically generated.
	 *
	 * @param encryptedPassword
	 *            the encrypted password.
	 * @return the encrypted password.
	 * @throws UnsupportedOperationException
	 *             if encryption/decryption is not possible; for example, during
	 *             setup mode.
	 */
	public static String decryptPassword(String encryptedPassword) {
		if (encryptedPassword == null) {
			return null;
		}
		return Blowfish.getInstance().decryptString(encryptedPassword);
	}


	public static void main(String[] args) {
		try {
			String password = Security.encryptPassword("teemlink");
			logger.debug("encrypt: " + password);

			password = Security.decryptPassword(password);
			logger.debug("decrypt: " + password);

			// String pw = encodeToMD5("123");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
