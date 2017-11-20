/**
 * BaseEntity 所有领域模型的基类
 * @author caoyong
 *
 */
package cn.org.awcp.core.domain;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import cn.org.awcp.core.utils.Springfactory;

/**
 * 抽象实体类，可作为所有领域实体的基类。
 *
 *
 */

public abstract class BaseEntity<E> implements Entity<E> {

	private static EntityRepository repository;

	private E id;

	public static EntityRepository getRepository() {
		if (repository == null)
			repository = Springfactory.getBean("repository");
		return BaseEntity.repository;
	}

	public Type getEntityClass() {
		Type sType = getClass().getGenericSuperclass();
		if (sType instanceof ParameterizedType) {
			Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
			return generics[0];
		}
		return null;

	}

	/**
	 * 设置仓储
	 * 
	 * @param repository
	 */
	public static void setRepository(EntityRepository repository) {
		BaseEntity.repository = repository;
	}

	private static final long serialVersionUID = 8882145540383345037L;

	public BaseEntity<E> save() {
		return getRepository().save(this);
	}

	public void remove() {
		getRepository().remove(this);
	}

	public static <E, T extends Entity<E>> T get(Class<T> clazz, E id) {
		return (T) getRepository().get(clazz, id);
	}

	public static <E, T extends Entity<E>> T getUnmodified(Class<T> clazz, T entity) {
		return (T) getRepository().getUnmodified(clazz, entity);
	}

	public static <E, T extends Entity<E>> T load(Class<T> clazz, E id) {
		return (T) getRepository().load(clazz, id);
	}

	public static <E, T extends Entity<E>> List<T> findAll(Class<T> clazz) {
		return getRepository().findAll(clazz);
	}

	public static <E, T extends Entity<E>> List<T> selectByExample(Class<T> clazz, BaseExample example) {
		return getRepository().selectByExample(clazz, example);
	}

	public E getId() {
		return id;
	}

	public void setId(E id) {
		this.id = id;
	}

}
