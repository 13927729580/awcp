package org.szcloud.framework.workflow.core.emun;

//意见签名使用权限类型
public class EResponseRight {
	public static final int PublicResponseRight = 0; //公开（缺省，不需要设定）；
	public static final int RecipientsResponseRight = 1; //公文参与人员；
	public static final int ActivityResponseRight = 2; //公文中的某些步骤；
	public static final int RoleResponseRight = 4; //公文中的某些角色；
	public static final int ExterUserResponseRight = 8; //外部用户（非公文参与人）；
	public static final int CacuScriptResponseRight = 16; //计算公式，该类型只针对打开公文时有效，并且在打开公文时如果返回True则可见，否则不可见；
}
