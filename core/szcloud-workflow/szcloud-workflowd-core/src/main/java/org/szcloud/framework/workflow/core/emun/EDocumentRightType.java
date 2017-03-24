package org.szcloud.framework.workflow.core.emun;

/*
 * 附件权限类型，该类型是对整个大权限类型下的小权限的分配
 */
public class EDocumentRightType {
	/* 不可访问 */
	public static final int DisVisibleDocument = 0; 
	/* 只读 */
	public static final int ReadOnlyDocument = 1; 
	/* 可读写 */
	public static final int ReadWriteDocument = 2; 
	/* 可删除 */
	public static final int DeleteDocument = 4; 
}
