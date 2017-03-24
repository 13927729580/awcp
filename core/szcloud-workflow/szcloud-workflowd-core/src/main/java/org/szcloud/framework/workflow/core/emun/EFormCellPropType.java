package org.szcloud.framework.workflow.core.emun;

/*
 * 属性类型
 */
public class EFormCellPropType {
	public static final int CellCtrlReadProp = 0; //控件只读属性；
	public static final int CellCtrlEvent = 1; //控件事件；
	public static final int CellCtrlMethod = 2; //控件方法；
	public static final int CellWriteProp = 3; //控件可读写属性（与数据交互的属性）；
	public static final int CtrlCellReadWrite = 4; //控件控制读或写的属性；
	public static final int OtherCellProp = 9; 	 //其他；
}
