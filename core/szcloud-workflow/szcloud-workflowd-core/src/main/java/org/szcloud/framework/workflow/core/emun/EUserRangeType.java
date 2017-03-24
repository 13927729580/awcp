package org.szcloud.framework.workflow.core.emun;

/*
 * 选取用户权限SQL语句范围的类型(类型可叠加)
 */
public class EUserRangeType {
	public static final int DefaultRangeType = 0; //#缺省类型
	public static final int IncludeInvalidUses = 1; //#包含被禁止使用的用户类型
	public static final int IncludeRoleUsers = 2; //#包含用户角色类型
	public static final int OnlyExistGroups = 4; //#只包含分组情况
	public static final int OnlyExistUsers = 8; //#只包含用户情况
}
