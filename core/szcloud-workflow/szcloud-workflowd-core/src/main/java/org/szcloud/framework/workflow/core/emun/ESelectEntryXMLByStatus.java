package org.szcloud.framework.workflow.core.emun;

//流转状态实例XML选取类型(按处理的状态来分，该类型主要用于筛选流转条目)
public class ESelectEntryXMLByStatus {
	public static final int ProjectEntryXML = 0; //#项目应用
	public static final int DraftEntryXML = 1; //#公文草稿
	public static final int TransactingEntryXML = 2; //#待办公文
	public static final int TransactedEntryXML = 3; //#已办公文
	public static final int SpecialEntryXML = 4; //#特殊公文
	public static final int AdminEntryXML = 5; //#公文管理
	public static final int TransferEntryXML = 6; //#转发公文
	public static final int RelyedEntryXML = 7; //#公文回收站
}
