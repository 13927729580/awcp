package org.szcloud.framework.venson.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 持久化数据存储对象，以JSON数据格式进行存储
 * 
 * @author Venson
 *
 */
public class LocalStorage {
	protected static final Log logger = LogFactory.getLog(LocalStorage.class);
	public final static File awcpFile;
	public static final String EMPTY_JSON = "{}";
	private static JSONObject store;
	static {
		// 创建平台属性文件
		awcpFile = new File("awcp.json");
		// 如果文件不存在则初始化
		if (!awcpFile.exists()) {
			init();
		} else {
			store = getStorage();
		}

	}

	/**
	 * 将对象写入awcp.json
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void set(String key, Object value) {
		// 新增对象
		store.put(key, value);
		// 写入文件
		writeJSONToFile(awcpFile, store);

	}

	/**
	 * 将对象转为JSON写入到指定的文件
	 * 
	 * @param file
	 *            指定文件
	 * @param obj
	 *            写入对象
	 */
	public static void writeJSONToFile(File file, Object obj) {
		FileWriter writer = null;
		try {
			if (!awcpFile.exists()) {
				awcpFile.createNewFile();
			}
			// 创建文件写入对象
			writer = new FileWriter(file);
			// 开始写入
			JSON.writeJSONStringTo(obj, writer);
		} catch (IOException e) {
			logger.debug("ERROR", e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * 从awcp.json获取对象
	 * 
	 * @param key
	 *            键
	 * @param clazz
	 *            泛型
	 * @return
	 */
	public static <T> T get(String key, Class<T> clazz) {
		return store.getObject(key, clazz);
	}

	/**
	 * 从awcp.json获取对象
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static Object get(String key) {
		return get(key, Object.class);
	}

	/**
	 * 从awcp.json移除对象
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static Object remove(String key) {
		if (isEmpty())
			return null;
		Object data = store.remove(key);
		writeJSONToFile(awcpFile, store);
		return data;
	}

	/**
	 * 是否包含键值为key的映射
	 * 
	 * @param key键
	 * @return
	 */
	public static boolean hasKey(String key) {
		if (store.containsKey(key)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否是空的对象
	 */
	public static boolean isEmpty() {
		return store.isEmpty();
	}

	/**
	 * 清空awcp.json
	 */
	public static void clear() {
		init();
	}

	/**
	 * 初始化存储器
	 */
	private static void init() {
		store = new JSONObject();
		writeJSONToFile(awcpFile, store);
	}

	/**
	 * 获取平台JSON存储对象
	 * 
	 * @return
	 */
	public static JSONObject getStorage() {
		FileInputStream in = null;
		try {
			in = new FileInputStream(awcpFile);
			return JSON.parseObject(IOUtils.toByteArray(in), JSONObject.class);
		} catch (Exception e) {
			logger.debug("ERROR", e);
			// 解析出错则创建新对象
			return new JSONObject();
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
