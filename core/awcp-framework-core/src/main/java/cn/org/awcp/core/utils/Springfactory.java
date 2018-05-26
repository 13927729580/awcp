package cn.org.awcp.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Springfactory implements ApplicationContextAware {

	private static ApplicationContext context = null;

	private Springfactory() {
		super();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	/**
	 * 根据beanName名字取得bean
	 * 
	 * @param beanName
	 * @return
	 */
	public static <T> T getBean(String beanName) {
		return (T) context.getBean(beanName);
	}

	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	/**
	 * 是否包含bean
	 * @param beanName
	 * @return
	 */
	public static boolean containsBean(String beanName) {
		return context.containsBean(beanName);
	}

	/**
	 * 是否是单例
	 * @param beanName
	 * @return
	 */
	public static boolean isSingleton(String beanName) {
		return context.isSingleton(beanName);
	}

	/**
	 * bean的类型
	 * @param beanName
	 * @return
	 */
	public static Class getType(String beanName) {
		return context.getType(beanName);
	}

}

