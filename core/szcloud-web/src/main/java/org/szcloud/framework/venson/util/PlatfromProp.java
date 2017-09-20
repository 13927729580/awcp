package org.szcloud.framework.venson.util;

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
			InputStream in = SMSUtil.class.getClassLoader().getResourceAsStream("conf/awcp.properties");
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
		return p.getProperty(key);
	}
}
