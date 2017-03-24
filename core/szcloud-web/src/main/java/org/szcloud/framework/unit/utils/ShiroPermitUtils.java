package org.szcloud.framework.unit.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.szcloud.framework.unit.shiro.ShiroConstants;

/**
 * 判断对资源操作类型的各种权限
 * @author wsh
 *
 */
public abstract class ShiroPermitUtils {
	
	/**
	 * 资源可读
	 * @return
	 */
	public static boolean read() {
		Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(ShiroConstants.RESOURCE_READ);
	}
	
	/**
	 * 资源可写
	 * @return
	 */
	public static boolean write() {
		Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(ShiroConstants.RESOURCE_WRITE);		
	}
	
	/**
	 * 资源可读写
	 * @return
	 */
	public static boolean readWrite() {
		Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(ShiroConstants.RESOURCE_RW);
	}
	
	/**
	 * 资源可执行
	 * @return
	 */
	public static boolean exec() {
		Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(ShiroConstants.RESOURCE_EXEC);
	}

}
