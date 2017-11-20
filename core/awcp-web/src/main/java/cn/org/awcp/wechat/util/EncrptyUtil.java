package cn.org.awcp.wechat.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA-1加密
 * @author yqtao
 *
 */
public class EncrptyUtil {

	/**
	 * 加密算法,shiro框架已经有实现.
	 * @param str
	 * @return
	 */
    public static String crpty(String str){
    	String result = null;
    	MessageDigest md = null; 
    	try{
    		md = MessageDigest.getInstance("SHA-1");	// 将三个参数字符串拼接成一个字符串进行sha1加密  
    		byte[] digest = md.digest(str.getBytes()); 
    		result = byteToStr(digest); 
    	} catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }    	
    	return result;
    }
  
    //将字节数组转换为十六进制字符串  
    private static String byteToStr(byte[] byteArray) {  
        String strDigest = "";  
        for (int i = 0; i < byteArray.length; i++) {  
            strDigest += byteToHexStr(byteArray[i]);  
        }  
        return strDigest;  
    }  
  
    //将字节转换为十六进制字符串   
    private static String byteToHexStr(byte mByte) {  
        char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
        char[] tempArr = new char[2];  
        tempArr[0] = digit[(mByte >>> 4) & 0X0F];  
        tempArr[1] = digit[mByte & 0X0F];  
  
        String s = new String(tempArr);  
        return s;  
    }  
}
