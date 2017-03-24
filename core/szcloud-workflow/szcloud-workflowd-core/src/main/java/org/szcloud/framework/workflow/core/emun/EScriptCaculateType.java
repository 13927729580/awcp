package org.szcloud.framework.workflow.core.emun;

//二次开发函数计算类型
public class EScriptCaculateType {
	//定义服务器端的计算内容
	public static final int CacuOpenForAllReadOnly = 0; //打开计算 — 针对所有参与人员在只读情况下
	public static final int CacuOpenForAll = 1; //打开计算 — 针对所有参与人员
	public static final int CacuOpenForFirst = 2; //打开前计算 — 针对第一个参与人员
	public static final int CacuBeforeSaveForAll = 3; //保存前计算 — 针对所有参与人员
	public static final int CacuAfterSaveForAll = 4; //保存后计算 — 针对所有参与人员
	public static final int CacuBeforeSaveForFirst = 5; //保存前计算 — 针对第一个参与人员
	public static final int CacuAfterSaveForFirst = 6; //保存后计算 — 针对第一个参与人员
	public static final int CacuBeforeSendForAll = 7; //发送前计算 — 针对所有参与人员
	public static final int CacuAfterSendForAll = 8; //发送后计算 — 针对所有参与人员
	public static final int CacuBeforeSendForFirst = 9; //发送前计算 — 针对第一个参与人员
	public static final int CacuAfterSendForFirst = 10; //发送后计算 — 针对第一个参与人员
	public static final int CacuStopWorkItem = 11; //公文流转结束后计算
	//CacuAfterUpdateData = 19            '数据更新后的计算
	//定义客户端的计算内容
	public static final int CacuAtOpenWorkItem = 12; //打开时计算
	public static final int CacuAtSendWorkItem = 13; //发送时计算
	public static final int CacuAtSaveWorkItem = 14; //保存时计算
	//不管是只读还是可读写，在打开时计算
	public static final int CacuOpenAtAnyStatus = 15; //在打开时计算 - 任意时候
	public static final int CacuOpenClientAnyStatus = 16; //在打开时计算 - 任意时候，客户端
}
