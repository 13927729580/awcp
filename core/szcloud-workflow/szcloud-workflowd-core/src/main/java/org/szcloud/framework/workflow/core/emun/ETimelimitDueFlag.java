package org.szcloud.framework.workflow.core.emun;

//过期类型
public class ETimelimitDueFlag {
	public static final int TimelimitDueNone = 0; //#无过期期限
	public static final int TimelimitDueBy = 1; //#相对过期类型
	public static final int TimelimitDueAbs = 2; //#绝对过期类型
	public static final int TimelimitDueProp = 4; //#属性过期类型[针对于某个属性-必须为日期类型]
}
