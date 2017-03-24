package org.szcloud.framework.workflow.core.emun;

//流转状态实例Web端表现类型
public class ESelectEntryStyleType {
	public static final int ProjectEntryStyle = 1; //#项目应用
	public static final int DraftEntryStyle = 2; //#公文草稿
	public static final int TransactingEntryStyle = 4; //#待办公文
	public static final int TransactedEntryStyle = 8; //#已办公文
	public static final int SpecialEntryStyle = 16; //#特殊公文
	public static final int AdminEntryStyle = 32; //#公文管理
	public static final int TransferEntryStyle = 64; //#转发公文
	public static final int RelyedEntryStyle = 128; //#公文回收站
}
