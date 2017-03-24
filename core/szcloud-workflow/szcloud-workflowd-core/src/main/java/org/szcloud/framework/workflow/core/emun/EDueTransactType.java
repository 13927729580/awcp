package org.szcloud.framework.workflow.core.emun;

/*
 * 过期处理类型
 */
public class EDueTransactType {
	/* 已经处理过期情况，针对流转中的公文而言 */
	public static final int HaveTransactedDue = -1; 
	/* 无需处理 */
	public static final int NotAnyDueTransact = 0; 
	/* 按正常流转处理 */
	public static final int NormalDueTransact = 1; 
	/* 发送给指定用户 */
	public static final int SendToUserDueTransact = 2; 
	/* 发送给指定步骤 */
	public static final int SendToActivityDueTransact = 4; 
	/* 结束公文流转 */
	public static final int EndFlowDueTransact = 8; 
}
