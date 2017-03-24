package org.szcloud.framework.workflow.core.emun;

/*
 * 传递正文内容方式
 */
public class EImpressBodyType {
	public static final int NotImpressBody = 0; //#不传递正文
	public static final int ImpressAsBody = 1; //#传递正文并替换为新实例的正文或回传实例的正文
	public static final int ImpressAsApp = 2; //#传递正文并作为一个附件保存
}
