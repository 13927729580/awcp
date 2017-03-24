package org.szcloud.framework.workflow.core.emun;

//工作流管理对象


		//#==========================================================================#
		//枚举定义
		//#==========================================================================#
		//取得流转系统公文列表左边功能目录树的内容的类别
public class ESelectFolderListType {
	public static final int SelectWorkStatus = 1; //按公文处理状态的分类目录
	public static final int SelectWorkType = 2; //按公文分类的分类目录
	public static final int SelectOtherFolder = 4; //其他各种目录：授权、系统维护等
	public static final int SelectAllFolder = 7; //选取所有目录
	public static final int SelectOnlyPropxy = 8; //授权只选择所有授权
	public static final int SelectPublicFolder = 16 ;//是否选择公文发布
}
