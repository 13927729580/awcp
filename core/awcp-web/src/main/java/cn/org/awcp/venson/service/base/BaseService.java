package cn.org.awcp.venson.service.base;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.org.awcp.venson.dao.BaseDao;

public abstract class BaseService {
	@Resource(name = "baseDaoImpl")
	protected BaseDao baseDao;

	protected String getStatement(Class<?> entityClass, String sqlId) {
		StringBuilder builder = new StringBuilder();
		builder.append(entityClass.getName());
		builder.append(BaseDao.MAPPER_PREFIX);
		builder.append(sqlId);
		return builder.toString();
	}

	protected String getStatement(String mapper, String sqlId) {
		StringBuilder builder = new StringBuilder();
		builder.append(mapper);
		builder.append(BaseDao.MAPPER_PREFIX);
		builder.append(sqlId);
		return builder.toString();
	}

	protected String getStatement(String mapper) {
		return getStatement(mapper, new Throwable().getStackTrace()[1].getMethodName());
	}

	protected Map<String, Object> makeMap(String[] names, Object... values) {
		int size = names.length;
		if (size != values.length) {
			throw new RuntimeException("names长度和values长度不一致");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < size; i++) {
			map.put(names[i], values[i]);
		}
		return map;
	}

	protected Map<String, Object> makeMap(Object... values) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < values.length; i++) {
			map.put("arg" + i, values[i]);
		}
		return map;
	}

}
