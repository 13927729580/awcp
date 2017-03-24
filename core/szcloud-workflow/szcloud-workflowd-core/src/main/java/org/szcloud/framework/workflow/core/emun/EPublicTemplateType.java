package org.szcloud.framework.workflow.core.emun;

//发布类型
public class EPublicTemplateType {
	public static final int NotPublicType = 0; //#不需要发布
	public static final int DefaultPublicType = 1; //#本系统缺省发布；
	public static final int PublicToExternalTable = 2; //#发布到一个外部表单中；
	public static final int UsingClientScript = 4; //#使用二次开发函数发布（客户端）；
	public static final int UsingServerScript = 8; //#使用二次开发函数发布（服务器端）；
}
