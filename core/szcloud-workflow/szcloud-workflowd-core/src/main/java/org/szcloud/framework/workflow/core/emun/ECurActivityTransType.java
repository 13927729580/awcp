package org.szcloud.framework.workflow.core.emun;

/*
 * 当前步骤处理类型
 */
public class ECurActivityTransType {
	/* 按正常步骤处理 */
	public static final int CommondTransType = 0; 
	/* 按转发特殊步骤处理 */
	public static final int SpecialTransType = 1; 
	/* 按前插特殊步骤处理 */
	public static final int SendPreTransType = 2; 
	/* 按后加特殊步骤处理 */
	public static final int SendNextTransType = 3; 
}
