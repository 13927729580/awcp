package org.szcloud.framework.unit.utils;

/**
 * 权限异常
 * @author allen
 *
 */
public class PermissionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionException(){
		super();
	}
	
	public PermissionException(String msg){
		super(msg);
	}
	
	public PermissionException(String msg, Throwable cause){
		super(msg,cause);
	}
}
