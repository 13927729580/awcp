package org.szcloud.framework.workflow.core.emun;

//流转状态实例列表选取类型(按处理的状态来分，该类型主要用于筛选流转条目)
public class ESelectEntryByStatus {
	public static final int NotTransactEntry = 1; //#未处理
	public static final int TransactingEntry = 2; //#已阅读
	public static final int TransactedEntry = 4; //#已完成
	public static final int OverTimeEntry = 8; //#已过期
	public static final int HadRecallEntry = 16; //#已回收
	public static final int TrumbleInLaunchEntry = 32; //#嵌入或启动状态
	public static final int OtherEntry = 64; //#其他处理状态
}
