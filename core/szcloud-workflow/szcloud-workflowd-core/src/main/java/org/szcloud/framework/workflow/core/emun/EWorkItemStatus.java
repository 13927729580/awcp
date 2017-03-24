package org.szcloud.framework.workflow.core.emun;
/*
 * 实例状态
 */
public class EWorkItemStatus {
	public static final  int ExteralStatus = -1; //#预留的其它状态，如公文流转出现错误等；
	public static final  int WorkFlowing = 0; //#流转中；
	public static final  int HaveFinished = 1; //#已结束；
	public static final  int HavePigeonholed = 2; //#已归档；
	public static final  int HaveDeleted = 3; //#已删除（被留在垃圾箱中）；
	public static final  int QuiteDeleted = 4; //#已被完全删除；
	public static final  int PauseFlowing = 5; //#被暂停流转；
	public static final  int OtherItemStatus = 9; //#其它未定状态；
}
