package org.szcloud.framework.workflow.core.emun;

/*
 * 步骤条件处理类型
 */
public class EActivityConditionType 
{
	/* 全部人员已完成 */
	public final static int AllFinished = 0;
	/* 至少一个人已完成 */
	public final static int AtListOneFinished = 1; 
	/* 完成人数 */
	public final static int FinishedNumber = 2; 
	/* 完成百分比 */
	public final static int FinishedPercent = 3; 
	/* 未完成人数 */
	public final static int UnFinishedNumber = 4; 
	/* 未完成百分比 */
	public final static int UnFinishedPercent = 5;
}
