package org.szcloud.framework.workflow.core.emun;

/*
 * 传递附件内容方式
 */
public class EImpressDocumentType {
	public static final int NotImpressDocument = 0; //#不传递附件
	public static final int ImpressAllDocument = 1; //#传递所有附件
	public static final int ImpressLastVersion = 2; //#传递最后一个版本的附件（对有版本控制的附件而言，而对非版本控制附件还是照传）
}
