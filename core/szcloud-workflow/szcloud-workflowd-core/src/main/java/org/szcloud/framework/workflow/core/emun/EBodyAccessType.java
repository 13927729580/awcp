package org.szcloud.framework.workflow.core.emun;

/*
 * 正文权限类型
 */
public class EBodyAccessType {
	/* 不可见 */
	public static final int BodyDisVisible = 0; 
	/* 只读 */
	public static final int BodyReadOnly = 1; 
	/* 可读写 */
	public static final int BodyReadWrite = 2; 
	/* 只读（最终版本）（针对Word版本控制有效、只读） */
	public static final int BodyLastVersion = 3; 
	/* 可读写（最终版本）（针对Word版本控制有效、可读写） */
	public static final int BodyLastReadWrite = 4; 
}
