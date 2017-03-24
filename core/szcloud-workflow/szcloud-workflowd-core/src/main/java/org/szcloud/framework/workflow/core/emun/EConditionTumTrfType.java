package org.szcloud.framework.workflow.core.emun;

/*
 * 嵌入或启动处理情况
 */
public class EConditionTumTrfType {
	/* 已经发送 */
	public static final int HaveSendType = 0; 
	/* 成功流转 */
	public static final int FinishedFlow = 1; 
	/* 未成功流转 */
	public static final int UnFinishedFlow = 2; 
	/* 成功接收 */
	public static final int FinishedRecieve = 3; 
	/* 未成功接收 */
	public static final int UnFinishedRecieve = 4; 
}
