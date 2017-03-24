package org.szcloud.framework.workflow.core.emun;

/*
 * 表单图元权限类型（对于不是由表头属性和角色控制权限的图元权限控制）
 */
public class EFormCellAccessType {
	public static final int FormCellDisVisible = 0; //#不可见
	public static final int FormCellReadOnly = 1; //#只读
	public static final int FormCellValidHandle = 2; //#可操作
	public static final int FormCellNeedHandle = 3; //#必操作(主要指填充内容而言)
}
