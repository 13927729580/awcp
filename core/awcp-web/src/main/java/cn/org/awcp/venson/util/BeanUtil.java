package cn.org.awcp.venson.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanUtil {
	public static final int INSERT = 0;
	public static final int UPDATE = 1;
	public static final int DELETE = 1;
	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(BeanUtil.class);

	private BeanUtil() {
	}

	public static Map<String, Object> objectToMap(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				if (Modifier.isPrivate(field.getModifiers())) {
					Object val = field.get(obj);
					if (val != null) {
						map.put(field.getName(), field.get(obj));
					}
				}
			}
			return map;
		} catch (Exception e) {
			logger.debug("ERROR", e);
			return null;
		}
	}

	public static String getSQL(Map<String, Object> map, int type, String tableName) {
		final StringBuffer buffer = new StringBuffer();
		final StringBuffer values = new StringBuffer();
		buffer.append("INSERT INTO " + tableName + "(");
		if (type == INSERT) {
			Set<String> keys = map.keySet();
			for (String key : keys) {
				values.append(":" + key + ",");
				buffer.append(Underline2Camel.camelToUnderline(key) + ",");
			}
			buffer.delete(buffer.length() - 1, buffer.length());
			values.delete(values.length() - 1, values.length());
			buffer.append(") VALUES (");
			buffer.append(values);
			buffer.append(")");
			return buffer.toString();
		}
		return null;
	}
}
