package org.szcloud.framework.unit.shiro;

/**
 * 资源操作类型的权限编码
 * @author wsh
 *
 */
public abstract class ShiroConstants {
	
	/**
	 * 资源可读权限编码为0001
	 * 
	 */
	public static final String RESOURCE_READ = "1";
	
	/**
	 * 资源可写权限编码为0010
	 * 
	 */
	public static final String RESOURCE_WRITE = "2";
	
	/**
	 * 资源可读写权限编码为0011
	 * 
	 */
	public static final String RESOURCE_RW = "3";
	
	/**
	 * 资源可执行权限编码为0100
	 * 
	 */
	public static final String RESOURCE_EXEC = "4";
	
	
}
