package org.szcloud.framework.workflow.core.emun;

/*
 * 数据取值来源类型
 */
public class EDataValueSourceType {
	/* 来源于系统字段 */
	public static final int ComeFromSystemField = 0;
	/* 来源于计算函数 */
	public static final int ComeFromScriptCaculate = 1;
	/* 来源于SQL查询语句 */
	public static final int ComeFromSQLQuery = 2;
}
