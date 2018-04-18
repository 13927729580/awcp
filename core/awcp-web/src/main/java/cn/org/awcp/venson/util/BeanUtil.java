package cn.org.awcp.venson.util;

import cn.org.awcp.venson.exception.PlatformException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.*;

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

	public static void writeObject(Object obj, OutputStream out) {
		if (obj == null) {
			throw new PlatformException("写入对象为空");
		}
		ObjectOutputStream writer = null;
		try {
			writer = new ObjectOutputStream(out);
			writer.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(out);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T readObject(InputStream input) {
		ObjectInputStream reader = null;
		try {
			reader = new ObjectInputStream(input);
			return (T) reader.readObject();
		} catch (Exception e) {
			throw new PlatformException("对象转换出错，转换失败！");
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(input);
		}
	}

	public static Map<String, Object> objectToMap(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			Map<String, Object> map = new HashMap<>(declaredFields.length);
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

	public static String getInsertSQL(Map<String, Object> map, String tableName) {
		return getInsertSQL(map, tableName, null);
	}

	/**
	 * 自动生成插入语句
	 * 
	 * @param map
	 * @param tableName
	 *            表名
	 * @param idKey 主键名称
	 *            是否要创建删除语句
	 * @return
	 */
	public static String getInsertSQL(Map<String, Object> map, String tableName, String idKey) {
		final StringBuffer buffer = new StringBuffer();
		final StringBuffer values = new StringBuffer();
		if (idKey!=null) {
			// buffer.append("/*DELETE table `" + tableName + "` where id= " + id + " start
			// */\n");
			buffer.append("DELETE FROM `" + tableName + "` WHERE "+idKey+"='" + map.get(idKey) + "';\n");
		}
		// buffer.append("/*backup table `" + tableName + "` where id= " + id + " start
		// */\n");
		buffer.append("INSERT INTO `" + tableName + "`( ");
		Set<String> keys = map.keySet();
		for (String key : keys) {
			buffer.append("`" + key + "`,");
			Object value = map.get(key);
			if (value == null) {
				values.append("null,");
			} else {
				if (value instanceof String) {

					values.append(handSQL(value.toString()) + ",");
				} else if (value instanceof Date) {

					values.append("'" + DateFormaterUtil.dateToString(DateFormaterUtil.FORMART4, (Date) value) + "',");
				} else {
					values.append("'" + value + "',");
				}
			}
		}
		buffer.delete(buffer.length() - 1, buffer.length());
		values.delete(values.length() - 1, values.length());
		buffer.append(") VALUES (");
		buffer.append(values);
		buffer.append(" );\n");
		String str = buffer.toString();
		logger.debug(str);
		return str;
	}

	/**
	 * 根据表名备份当前数据库表和数据
	 * 
	 * @param jdbcTemplate
	 *            数据源
	 * @param tableName
	 *            表名
	 * @param inCludeData
	 *            是否包含数据
	 * @return
	 */
	public static String createTable(JdbcTemplate jdbcTemplate, String tableName, boolean inCludeData) {
		Map<String, Object> tableStru = jdbcTemplate.queryForMap("SHOW CREATE TABLE " + tableName);
		StringBuffer buffer = new StringBuffer();
		buffer.append("DROP TABLE IF EXISTS `" + tableName + "`;\n");
		buffer.append(tableStru.get("Create Table") + ";\n");
		if (inCludeData) {
			List<Map<String, Object>> data = jdbcTemplate.queryForList("select * from " + tableName);
			for (Map<String, Object> map : data) {
				buffer.append(BeanUtil.getInsertSQL(map, tableName, null));
			}
		}
		String str = buffer.toString();
		logger.debug(str);
		return str;
	}

	private static String handSQL(String x) {
		int stringLength = x.length();
		StringBuffer buf = new StringBuffer((int) (stringLength * 1.1));
		CharsetEncoder charsetEncoder = Charset.forName("UTF-8").newEncoder();
		buf.append('\'');
		for (int i = 0; i < stringLength; ++i) {
			char c = x.charAt(i);

			switch (c) {
			case 0: /* Must be escaped for 'mysql' */
				buf.append('\\');
				buf.append('0');

				break;

			case '\n': /* Must be escaped for logs */
				buf.append('\\');
				buf.append('n');

				break;

			case '\r':
				buf.append('\\');
				buf.append('r');

				break;

			case '\\':
				buf.append('\\');
				buf.append('\\');

				break;

			case '\'':
				buf.append('\\');
				buf.append('\'');

				break;

			case '"': /* Better safe than sorry */

				buf.append('"');

				break;

			case '\032': /* This gives problems on Win32 */
				buf.append('\\');
				buf.append('Z');

				break;

			case '\u00a5':
			case '\u20a9':
				// escape characters interpreted as backslash by mysql
				CharBuffer cbuf = CharBuffer.allocate(1);
				ByteBuffer bbuf = ByteBuffer.allocate(1);
				cbuf.put(c);
				cbuf.position(0);
				charsetEncoder.encode(cbuf, bbuf, true);
				if (bbuf.get(0) == '\\') {
					buf.append('\\');
				}
				// fall through

			default:
				buf.append(c);
			}
		}

		buf.append('\'');

		return buf.toString();
	}

	/**
	 * 如果传入对象为空，则实例化改对象
	 * 
	 * @param obj
	 *            对象
	 * @param clazz
	 *            实例化对象
	 * @return
	 */
	public static <T> T instance(T obj, Class<T> clazz) {
		if (obj == null) {
			try {
				obj = clazz.newInstance();
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * map移除对象
	 * @param map
	 * @param keys
	 */
	public static void removeMap(Map<String,?> map,String... keys){
		for(String key:keys){
			map.remove(key);
		}
	}
}
