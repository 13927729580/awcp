package org.szcloud.framework.workflow.core.emun;

//报警类型
public class ETimelimitAlertFlag {
	public static final int TimelimitAlertNone = 0; //#无报警期限
	public static final int TimelimitAlertBy = 1; //#相对报警类型
	public static final int TimelimitAlertAbs = 2; //#绝对报警类型
	public static final int TimelimitAlertProp = 4; //#属性过期类型[针对于某个属性-必须为日期类型]
}
