package org.szcloud.framework.workflow.core.emun;

/*
 * 步骤类型
 */
public class EActivityType {
	/* 非任何类型 */
	public final static int NotAnyActivity = -1; 
	/* 开始步骤类型 */
	public final static int StartActivity = 0; 
	/* 处理步骤类型（包含内部传阅功能，并发） */
	public final static int TransactActivity = 1; 
	/* 通知步骤类型（包含内部传阅功能，并发） */
	public final static int FYIActivity = 2; 
	/* 嵌入（本地）步骤类型 */
	public final static int TumbleInActivity = 3; 
	/* 启动（远地）步骤类型 */
	public final static int LaunchActivity = 4; 
	/* 分支步骤类型 */
	public final static int SplitActivity = 5; 
	/* 结束步骤类型 */
	public final static int StopActivity = 9; 
	/* 
	 * 无限制条件步骤类型，此步骤类型的特点是在流转过程中管理员可以随时添加，它处理的
	 * 特点是由某一状态处理人触发，并送给相关人员处理完成后直接打回给当前状态处理人员，
	 * 提供继续流转，这种类型的步骤不参与整个流程逻辑的计算，在处理过程中，只充当修改
	 * 某些数据参数的作用，同时对权限的限制只在于管理员修改流程时使用，需要所有流转处
	 * 理人员(本步骤)完成后才能往回提交。本步骤目的是为了更加灵活的处理流转是出现一些
	 * 特殊情况，如办错文等，提供一个可反悔办文的机会。
	 */
	public final static int NotLimitedActivity = 19; 
}
