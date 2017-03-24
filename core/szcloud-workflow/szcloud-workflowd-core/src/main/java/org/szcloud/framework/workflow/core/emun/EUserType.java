package org.szcloud.framework.workflow.core.emun;
  
/*
 * 用户类型
 * 用户权限定义：
 *    0、本系统禁止使用：不允许使用本系统；
 *    1、普通用户：对所参与的公文具有查询、参看内容、跟踪、监督以及定制私有流程的权限；
 *    2、系统管理员：对本系统的所有操作的权限(只有超级管理员才可以设置系统参数)；
 *    3、应用管理员：可以定制和更改(只能更改自己定义的)公共模板、并对其定制的模板分配使用模板权限；
 *    4、用户管理员：管理维护本系统的用户信息；
 *    5、分类管理员：管理某一分类系统的公共模板；
 *    6、外部用户（客户）：参与公文权限；
 *    7、特殊用户：扩展情况
 */
public class EUserType {
	public static final int InterdictUser = -1; //#本系统禁止使用
	public static final int CommondUser = 0; //#普通用户
	public static final int SystemAdministrator = 1; //#系统管理员(当UserID=1时，为超级系统管理员)
	public static final int ApplicationAdmin = 2; //#应用管理员(权限管理)
	public static final int UserAdmin = 4; //#用户管理员(用户管理)
	public static final int TypeAdmin = 8; //#分类管理员
	public static final int ExternalUser = 16; //#外部用户(客户)
	public static final int SpecificUser = 32; //#特殊用户
}
