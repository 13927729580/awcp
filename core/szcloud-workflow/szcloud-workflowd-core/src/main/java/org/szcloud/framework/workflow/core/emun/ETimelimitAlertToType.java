package org.szcloud.framework.workflow.core.emun;

//报警给...的类型
public class ETimelimitAlertToType {
	public static final int TimelimitAlertToNone = 0; //#没有报警人
	public static final int TimelimitAlertToCreator = 1; //#报警给创建人
	public static final int TimelimitAlertToNotResp = 2; //#报警给未响应人员
	public static final int TimelimitAlertToResped = 4; //#报警给已响应人员
	public static final int TimelimitAlertToOther = 8; //#报警给其他人
	public static final int TimelimitAlertToSendUser = 16; //#报警给送达人员
	public static final int TimelimitAlertToDue = 32; //#报警超过最大数目自动过期
}
