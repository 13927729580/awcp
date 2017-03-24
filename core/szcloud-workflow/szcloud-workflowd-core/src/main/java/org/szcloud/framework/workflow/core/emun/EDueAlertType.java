package org.szcloud.framework.workflow.core.emun;

/*
 * 期限报警执行类型
 */
public class EDueAlertType {
	/* 不使用期限报警 */
	public static final int NotAnyDueAlertType = 0; 
	/* 系统自动催办 */
	public static final int 	AutoDueAlertType = 1; 
	/* 用户手工催办：公共催办需要由二次开发函数设置来实现 */
	public static final int 	UserDueAlertType = 2; 
	/* 缺省系统自动催办，同时允许用户手工催办：公共催办需要由二次开发函数设置来实现 */
	public static final int AutoUserDueAlertType = 4; 	
}
