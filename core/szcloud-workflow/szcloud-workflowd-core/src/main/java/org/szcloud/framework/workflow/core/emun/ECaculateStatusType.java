package org.szcloud.framework.workflow.core.emun;

/*
 * 计算状态类型（对表头属性或角色而言）
 */
public class ECaculateStatusType {
	/* 不计算 */
	public static final int NeedNotCaculate = 0; 
	/* 打开时计算 */
	public static final int CaculateOpenStatus = 1; 
	/* 发送时计算[在处理公文流转前] */
	public static final int CaculateSendStatus = 2; 
	/* 都计算[打开和发送] */
	public static final int CaculateBothStatus = 3; 
	/* 保存时计算 */
	public static final int CaculateSaveStatus = 4; 
	/* 全部计算[打开和发送、保存] */
	public static final int CaculateAllStatus = 7; 
	/* 发送后计算[在处理公文流转后] */
	public static final int CaculateSendAfterStatus = 8; 
}
