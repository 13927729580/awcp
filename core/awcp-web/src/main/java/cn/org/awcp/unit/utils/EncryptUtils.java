package cn.org.awcp.unit.utils;

import cn.org.awcp.core.utils.Security;

/**
 * 
* @ClassName: EncryptUtils 
* @Description: 加密，解密工具
* @author ljw 
* @date 2014年9月3日 下午4:00:18 
*
 */
public class EncryptUtils {
	/**
	 * @param inStr
	 *            加密前的字符串
	 * @return 加密后的结果
	 */
	public static String encrypt(String inStr) {
		return Security.encryptPassword(inStr);
	}
	
	public static String decript(String inStr){
		return Security.decryptPassword(inStr);
	}

}
