package org.szcloud.framework.venson.controller.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.venson.service.base.CRUDService;

public abstract class CRUDController<T> extends BaseController {
	@Autowired
	protected CRUDService crudService;
	private Class<T> entityClass;
	private String entityName;

	@RequestMapping("save")
	@ResponseBody
	public ReturnResult save(T entity) {
		ReturnResult returnResult = ReturnResult.get();
		Serializable id = crudService.save(entity);
		returnResult.setStatus(StatusCode.SUCCESS).setData(id);
		logger.debug("save {} success", getEntityName());
		return returnResult;
	}

	@RequestMapping("update")
	@ResponseBody
	public ReturnResult update(T entity) {
		ReturnResult returnResult = ReturnResult.get();
		crudService.update(entity);
		logger.debug("update {} success", getEntityName());
		return returnResult.setStatus(StatusCode.SUCCESS);
	}

	@RequestMapping("delete")
	@ResponseBody
	public ReturnResult delete(Integer id) {
		ReturnResult returnResult = ReturnResult.get();
		crudService.delete(getEntityClass(), id);
		returnResult.setStatus(StatusCode.SUCCESS).setData(id);
		logger.debug("delete {} success", getEntityName());
		return returnResult;
	}

	@RequestMapping("get")
	@ResponseBody
	public ReturnResult get(Integer id) {
		ReturnResult returnResult = ReturnResult.get();
		returnResult.setStatus(StatusCode.SUCCESS).setData(crudService.get(getEntityClass(), id));
		logger.debug("get {} success", getEntityName());
		return returnResult;
	}

	@RequestMapping("findAll")
	@ResponseBody
	public ReturnResult findAll() {
		ReturnResult returnResult = ReturnResult.get();
		returnResult.setStatus(StatusCode.SUCCESS).setData(crudService.findAll(getEntityClass()));
		logger.debug("findAll {} success", getEntityName());
		return returnResult;
	}

	@RequestMapping("query")
	@ResponseBody
	public ReturnResult query(HttpServletRequest request, @RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		ReturnResult returnResult = ReturnResult.get();
		Map<String, Object> result = crudService.query(getEntityClass(), request.getParameterMap(), offset, limit);
		returnResult.setStatus(StatusCode.SUCCESS).setData(result.get("data")).setTotal(result.get("tatol"));
		logger.debug("findAll {} success", getEntityName());
		return returnResult;
	}

	private String getEntityName() {
		if (entityName == null) {
			entityName = getEntityClass().getSimpleName();
		}
		return entityName;
	}

	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		if (entityClass == null) {
			Type sType = getClass().getGenericSuperclass();
			if (sType instanceof ParameterizedType) {
				Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
				entityClass = (Class<T>) (generics[0]);
			}
		}
		return entityClass;
	}
}
