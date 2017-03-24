package org.szcloud.framework.workflow.core.emun;

/*
 * 属性来源类型
 */
public class ECellPropSourceType {
	/* 固定不变的数值 */
	public static final int ChangelessValue = 0; 
	/* 来自于SQL语句所得 */
	public static final int ComeFromSQL = 1; 
	/* 来自于计算公式所得 */
	public static final int	ComeFromCaculate = 2; 
	/* 来源于表头属性 */
	public static final int	ComeFromDataProp = 3; 
}
