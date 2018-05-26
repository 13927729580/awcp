package cn.org.awcp.venson.util;

import cn.org.awcp.core.utils.Security;
import cn.org.awcp.venson.common.SC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PlatfromProp {
    private static Properties p = new Properties();

    /**
     * 读取properties配置文件信息
     */
    static {
        try {
            InputStream in = PlatfromProp.class.getClassLoader().getResourceAsStream("conf/awcp.properties");
            BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
            p.load(bfr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key得到value的值
     */
    public static String getValue(String key) {
        String value = p.getProperty(key);
        //判断是否属于加密类型
        if (SC.ENCRYPT_PASSWORD_KEY.contains(key)) {
            return Security.decryptPassword(value);
        } else {
            return value;
        }
    }
}
