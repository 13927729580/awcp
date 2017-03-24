package org.szcloud.framework.workflow.core.emun;

/*
 * 传递公文表单方式
 */
public class EImpressFormType {
	public static final int NotImpressForm = 0; //#不传递公文表单
	public static final int ImpressFormAsVirDoc = 1; //#转成Word附件传递（不保留附件）
	public static final int ImpressFormAsDocument = 2; //#转成Word附件传递（保留附件）
}
