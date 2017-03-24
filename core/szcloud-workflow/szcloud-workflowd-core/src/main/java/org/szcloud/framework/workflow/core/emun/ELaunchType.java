package org.szcloud.framework.workflow.core.emun;

/*
 * 启动参数类型
 */
public class ELaunchType {
	public static final int OneUserLaunchType = 0; //#只有一个启动用户；
	public static final int MutiUserLaunchType = 1; //#存在多个启动用户（不可选）；
	public static final int OneSelUserLaunchType = 2; //#存在多个启动用户（可选一个）；
	public static final int MutiSelUserLaunchType = 4; //#存在多个启动用户（可选多个）；
}
