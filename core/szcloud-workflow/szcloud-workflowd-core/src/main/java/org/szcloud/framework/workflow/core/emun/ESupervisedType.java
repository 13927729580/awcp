package org.szcloud.framework.workflow.core.emun;

//人工监督对象：公文流转过程中人工监视流转情况，主要是使用在一些
		//处理时间相对较长，而又需要实时的人为控制而设计，起到对公文处理
		//监督的作用，监督是赋予参与公文处理人员的特殊权利，每一个参与公
		//文的人员，都有权限发送监督，超级系统管理员，参与公文的人员（只
		//针对本人发起的监督）、当前公文分类的管理员有权删除监督，而对于
		//参与公文的人员，有权把设置监督的情况更改成没有被监督人员情况。
		
		//#==========================================================================#
		//枚举定义
		//#==========================================================================#
		//#被监督人的类型
public class ESupervisedType {

	public static final int NotAnyBody = 0; //#没有被监督人员
	public static final int NotTransactor = 1; //#未处理人员（未读、已读）
	public static final int HadTransactor = 2; //#已处理人员（除未处理人员以外）
	public static final int AllTransactor = 3; //#所有处理人员（包括未处理人员和已处理人员）
	public static final int OtherTransactor = 4; //#用户选定监督人员（在所有处理人员中选取）
}
