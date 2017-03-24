package org.szcloud.framework.workflow.core.emun;

/*
 * 公文流转所使用的方式[详细内容见对象TWorkItem头说明]
 */
public class EWorkItemFlowFlag {
	public static final int FreeFlowRule = 1; //#自由流转规则[最简单的流转]
	public static final int OrderFlowRule = 2; //#顺序流转规则[简单流转]
	public static final int ConditionFlowRule = 4; //#结构处理方式[依据触发条件，复杂流转]
}
