package org.szcloud.framework.workflow.core.emun;

/*
 * 状态处理情况
 */
public class EEntryStatus {
	/* 未处理(已收到) */
	public static final int NotTransactStatus = 0; 
	/* 已阅读(打开过但未保存) */
	public static final int HadReadStatus = 1; 
	/* 正处理(打开过并且已保存过) */
	public static final int TransactingStatus = 2; 
	/* 已完成 */
	public static final int HadTransactStatus = 3; 
	/* 已过期 */
	public static final int HadOverdueStatus = 4; 
	/* 已撤回(已回收) */
	public static final int HadRecallStatus = 5; 
	/* 嵌入或启动状态 */
	public static final int TumbleInLaunchStatus = 6; 
	/* 系统帮忙处理完成的状态 */
	public static final int SystemTransactStatus = 7; 
	/* 其他状态 */
	public static final int OtherStatus = 9; 
	/* 嵌入或启动成功流转后的状态 */
	public static final int SucceedFlowStatus = 11; 
	/* 嵌入或启动失败流转后的状态 */
	public static final int FailFlowStatus = 12; 
	/* 启动成功接收后的状态 */
	public static final int SucceedRecieved = 13; 
	/* 启动失败接收后的状态 */
	public static final int FailRecieved = 14; 
}
