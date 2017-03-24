package org.szcloud.framework.workflow.core.emun;

/*
 * 用户登陆方式
 */
public class EUserLogonFlag {
	public static final int LogonByUserID = 0; //#通过用户标识登陆
	public static final int LogonByUserGUID = 1; //#通过用户唯一标识登陆
	public static final int LogonByUserCode = 2; //#通过用户代码登陆
	public static final int LogonByUserName = 3; //#通过用户名称登陆
	public static final int LogonByLogonGuid = 4; //#通过用户登陆唯一标识
}
