package cn.org.awcp.venson.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验工具类
 * @author venson
 * @version 20180523
 */
public final class CheckUtils {

    /**
     * 国内手机号校验正则表达式
     */
    public final static String REG_CHINA_MOBILE = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";

    /**
     * 用户名校验正则表达式
     */
    public final static String REG_USER_NAME = "[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+";

    /**
     * 邮箱校验正则表达式
     */
    public final static String REG_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    /**
     * 身份证正则表达式
     */
    public final static String REG_IDCARD = "\\d{17}[\\d|x]|\\d{15}";

    private CheckUtils() {
    }

    /**
     * 只校验位数
     *
     * @param str 待校验字符串
     * @return true or false
     */
    public static boolean isPhoneLegal(String str) {
        return StringUtils.isNotBlank(str) && str.length() == 11;
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 此方法中前三位格式有： 13+任意数 15+除4的任意数 18+除1和4的任意数
     * 17+除9的任意数 147
     *
     * @param str 待校验字符串
     * @return true or false
     */
    public static boolean isChinaPhoneLegal(String str) {
        Pattern p = Pattern.compile(REG_CHINA_MOBILE);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 是否是合法用户名
     *
     * @param str 待校验字符串
     * @return true or false
     */
    public static boolean isLegalUserName(String str) {
        Pattern p = Pattern.compile(REG_USER_NAME);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 是否是合法邮箱
     *
     * @param str 待校验字符串
     * @return true or false
     */
    public static boolean isLegalEmail(String str) {
        Pattern p = Pattern.compile(REG_EMAIL);
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }

    /**
     * 是否是合法身份证
     *
     * @param str 待校验字符串
     * @return true or false
     */
    public static boolean isLegalIdcard(String str) {
        Pattern p = Pattern.compile(REG_IDCARD);
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }

}
