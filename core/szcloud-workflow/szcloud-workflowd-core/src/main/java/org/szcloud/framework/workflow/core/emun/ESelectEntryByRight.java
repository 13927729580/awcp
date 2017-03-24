package org.szcloud.framework.workflow.core.emun;

//流转状态实例列表选取类型(按处理的权限来分，该类型主要用于筛选流转条目)
		//流转系统特殊权限设置（对某一类模板生成的公文而言）：
		//   1、阅读权限：对未参与的公文具有查看起内容的权限
		//   2、新建权限：具有新建某一类公文实例的权限
		//   3、删除权限：具有删除某一类公文实例的权限
		//   4、查询权限：具有查询跟踪某一类公文实例的权限
		//   5、归档权限：具有对某一类公文实例归档的权限
		//   6、跟踪权限：具有对某一类公文实例跟踪的权限（流程修改）
		//   7、其他权限：扩展接口，提供以后使用
		//   8、完全控制权限：可以对某一类公文实例进行任何操作的权限，包括以上所列
		//       的各项权限外，还包括流程跟改、流程恢复、强制结束公文等权限
public class ESelectEntryByRight {
	public static final int ReadingRightEntry = 1; //#阅读权限
	public static final int CreateRightEntry = 2; //#新建权限
	public static final int DeleteRightEntry = 4; //#删除权限
	public static final int QueryRightEntry = 8; //#查询权限
	public static final int ToFileRightEntry = 16; //#归档权限
	public static final int TailRightEntry = 32; //#跟踪权限
	public static final int OtherRightEntry = 64; //#其他权限
	public static final int AllControlRightEntry = 128; //#完全控制权限
}
