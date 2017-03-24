package org.szcloud.framework.workflow.core.emun;

/*
 * 收件人范围类型
 */
public class EParticipantRangeType {
	public static final int NotExistSelectParticipant = 0; //不存在可选收件人
	public static final int ExistAllParticipant = 1; //包含全体人员
	public static final int ExistInCaculate = 2; //包含于用计算公式计算的成员
}
