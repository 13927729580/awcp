package org.szcloud.framework.workflow.core.emun;

/*
 * 附件权限类型
 */
public class EDocumentAccessType {
	/* 不可见 */
	public static final int DocumentDisVisible = 0; 
	/* 只读 */
	public static final int DocumentReadOnly = 1; 
	/* 可读写（不可创建版本控制附件） */
	public static final int DocumentReadWrite = 2; 
	/* 可读写（可创建版本控制附件） */
	public static final int DocumentWriteVersion = 3; 
	/* 可删除，赋予附件最高权限 */
	public static final int DocumentDelete = 4; 
}
