package org.szcloud.framework.workflow.core.emun;

/*
 * 取数据库结果集内容的存取方式
 */
public class EGetRecordsetContType {
	public static final int EmptyDataType = 0; //#取空结果集
	public static final int LastDataType = 1; //#取最近结果集（一条记录，EntryID = 0）
	public static final int LastEntryDataType = 2; //#取最近和当前结果集（两条记录，EntryID = 0，CurEntryID）
	public static final int EntryDataType = 3; //#取当前结果集（一条记录，EntryID = CurEntryID）
	public static final int AllDataType = 4; //#取所有结果集
}
