package org.szcloud.framework.workflow.core.core.util;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class ConvertObject {

	public static String convertString(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return (String) value;
		}

		return value.toString();
	}

	public static Integer convertInt(Object value) {
		if (value == null) {
			return 0;
		}

		if (value instanceof Number) {
			return ((Number) value).intValue();
		}

		return Integer.parseInt(value.toString());
	}

	public static Long convertLong(Object value) {
		if (value == null) {
			return 0L;
		}

		if (value instanceof Number) {
			return ((Number) value).longValue();
		}

		return Long.parseLong(value.toString());
	}

	public static Double convertDouble(Object value) {
		if (value == null) {
			return 0.0;
		}

		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}

		return Double.parseDouble(value.toString());
	}

	public static Float convertFloat(Object value) {
		if (value == null) {
			return 0F;
		}

		if (value instanceof Number) {
			return ((Number) value).floatValue();
		}

		return Float.parseFloat(value.toString());
	}

	public static Short convertShort(Object value) {
		if (value == null) {
			return 0;
		}

		if (value instanceof Number) {
			return ((Number) value).shortValue();
		}

		return Short.parseShort(value.toString());
	}

	public static Boolean convertBoolean(Object value) {
		if (value == null) {
			return false;
		}

		if (value instanceof Number) {
			return convertInt(value) == 1 ? true : false;
		}

		return false;
	}

	public static Date convertDate(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(value.toString());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}

		}

		return null;
	}

	public static Date convertDateTime(Object value) {
		if (value == null) {
			return null;
		}

		return null;
	}

	/**
	 * 把数组或者容器转换成为字符串形式 用逗号隔开
	 */
	public static String toSqlString(Object object) {
		if (object == null) {
			return null;
		}
		String returnString = "";
		if (object.getClass().isArray()) {
			int length = Array.getLength(object);
			for (int x = 0; x < length; x++) {
				if (x != 0) {
					returnString += ",";
				}
				Object value = Array.get(object, x);
				if (value instanceof Number) {
					returnString += value;
				} else {
					returnString += "'" + value + "'";
				}
			}
		} else {
			Collection<?> collection = (Collection<?>) object;
			Iterator<?> iterator = collection.iterator();
			int x = 0;
			while (iterator.hasNext()) {
				Object value = iterator.next();
				if (x != 0) {
					returnString += ",";
				}
				x++;
				if (value instanceof Number) {
					returnString += value;
				} else {
					returnString += "'" + value + "'";
				}
			}
		}
		return returnString;
	}

}
