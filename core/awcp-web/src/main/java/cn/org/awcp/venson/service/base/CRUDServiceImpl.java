package cn.org.awcp.venson.service.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import cn.org.awcp.venson.dao.BaseDao;
import cn.org.awcp.venson.dao.BaseModel;

@Service("crudService")
public class CRUDServiceImpl extends BaseService implements CRUDService {

	public <T> Serializable save(T entity) {
		Class<? extends Object> currenClass = entity.getClass();
		// 获取父类
		Class<?> parent = currenClass.getSuperclass();
		// 判断父类是不是模型基类
		boolean isModel = false;
		if (parent == BaseModel.class) {
			// 保存创建日期，创建人
			BaseModel model = (BaseModel) entity;
			model.preSave();
			isModel = true;
		}
		int id = baseDao.save(getStatement(currenClass, BaseDao.SAVE), entity);
		if (isModel) {
			BaseModel model = (BaseModel) entity;
			id = model.getId() == null ? model.getId() : 0;
		}
		return id;
	}

	public <T> void update(T entity) {
		Class<? extends Object> currenClass = entity.getClass();
		// 获取父类
		Class<?> parent = currenClass.getSuperclass();
		// 判断父类是不是模型基类
		if (parent == BaseModel.class) {
			// 保存创建日期，创建人
			BaseModel model = (BaseModel) entity;
			model.preUpdate();
		}
		baseDao.update(getStatement(currenClass, BaseDao.UPDATE), entity);
	}

	public <T> void delete(Class<T> entityClass, Object id) {
		baseDao.delete(getStatement(entityClass, BaseDao.DELETE), id);
	}

	public <T> T get(Class<T> entityClass, Object id) {
		return baseDao.get(getStatement(entityClass, BaseDao.GET), id);
	}

	public <T> List<T> findAll(Class<T> entityClass) {
		return baseDao.findAll(getStatement(entityClass, BaseDao.FINDALL));
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClass, int offset, int limit) {
		return baseDao.findAllByPage(getStatement(entityClass, BaseDao.FINDALL), offset, limit);
	}

	@Override
	public <T> Map<String, Object> query(Class<T> entityClass, Map<String, String[]> oldParams, int offset, int limit) {
		// 转换参数，将Map<String, String[]> ----> Map<String, Object>
		Map<String, Object> params = new HashMap<String, Object>();
		// 获取全部参数，排除offset和limit等参数
		for (Entry<String, String[]> entry : oldParams.entrySet()) {
			String key = entry.getKey();
			if (!key.equals("offset") && !key.equals("limit")) {
				// 添加参数，取第一个value值
				params.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", baseDao.findAllByPage(getStatement(entityClass, BaseDao.FINDALL), params, offset, limit));
		result.put("total", baseDao.get(getStatement(entityClass, BaseDao.COUNT), params));
		return result;
	}

}
