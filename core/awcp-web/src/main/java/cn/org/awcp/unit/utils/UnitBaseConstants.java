package cn.org.awcp.unit.utils;

/**
 * 组织机构与权限常量
 * @author allen
 *
 */
public abstract class UnitBaseConstants {
	
	/**
	 * 系统状态：未发布
	 */
	public static final String SYS_TATUS_NOT_RELEASE = "0";
	
	/**
	 *系统状态：已发布
	 */
	public static final String SYS_TATUS_RELEASE = "1";
	
	/**
	 * 系统已关联
	 */
	public static final String SYS_RELATED = "1";
	
	/**
	 * 系统未关联
	 */
	public static final String SYS_NOT_RELATED = "0";
	
	/**
	 * 用户未关联角色
	 */
	public static final String USER_NOT_RELATED_ROLE = "0";
	
	/**
	 * 用户已关联角色
	 */
	public static final String USER_RELATED_ROLE = "1";
	
	public static final Long GROUP_AMDIN_ROLE_ID = 1L;
}
