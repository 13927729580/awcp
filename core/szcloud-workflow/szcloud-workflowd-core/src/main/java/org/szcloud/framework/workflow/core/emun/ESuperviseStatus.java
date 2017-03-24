package org.szcloud.framework.workflow.core.emun;

//公文被监督人描述


		//#==========================================================================#
		//枚举定义
		//#==========================================================================#
public class ESuperviseStatus {
	public static final int NotReadSupervise = 0; //#未读；
	public static final int HaveReadSupervise = 1; //#已读；
	public static final int MustResponseSupervise = 2; //#必须回复；
	public static final int OnlyRemindSupervise = 4; //#只需提醒；
}
