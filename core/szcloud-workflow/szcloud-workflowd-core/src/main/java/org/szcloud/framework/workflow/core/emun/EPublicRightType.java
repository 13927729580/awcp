package org.szcloud.framework.workflow.core.emun;

//权限类型
public class EPublicRightType {
	public static final int SharePublicRightType = 0; //#公共 —— 发布给所有用户
	public static final int RangePublicRightType = 1; //#限定范围发布 —— 发布给部分用户
	public static final int OtherPublicRightType = 2; //#其他（扩展） —— 在公文流转结束后自动发布公文
}
