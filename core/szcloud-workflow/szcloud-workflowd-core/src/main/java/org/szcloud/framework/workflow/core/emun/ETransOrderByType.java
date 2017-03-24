package org.szcloud.framework.workflow.core.emun;

//顺序处理类型，当某一步骤的处理需要按一定的人员排列顺序处理时
public class ETransOrderByType {
	public static final int NotAndOrder = 0; //无处理顺序
	public static final int SystemUserOrder = 1; //系统序号排列顺序处理
	public static final int RoleUserOrder = 2; //角色人员处理
	public static final int UserDefineOrder = 3; //自定义处理顺序
}
