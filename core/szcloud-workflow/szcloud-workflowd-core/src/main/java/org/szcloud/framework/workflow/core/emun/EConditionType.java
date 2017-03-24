package org.szcloud.framework.workflow.core.emun;

/*
 * 条件类型（以下类型对条件的性质而言，是一个大的分类）
 * 对于条件类型的分类按条件的性质来分
 */
public class EConditionType {
	/* 非任何条件类型 */
	public static final int NotAnyCondition = -1; 
	/* 步骤条件类型 */
	public static final int ActivityCondition = 0; 
	/* 逻辑或条件类型 */
	public static final int LogicOrCondition = 1; 
	/* 逻辑且条件类型 */
	public static final int LogicAndCondition = 2; 
	/* 控制属性条件类型 */
	public static final int FlowPropCondition = 3; 
	/* 其他条件类型 */
	public static final int OtherCondition = 9; 
}
