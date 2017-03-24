package org.szcloud.framework.workflow.core.emun;

/*
 * 附件文件类型
 */
public class EDocumentType {
	/* 非任何类型 */
	public static final int NotAnyDocumentType = -3; 
	/* 已被删除的正文或附件 */
	public static final int DeletedFile = -2; 
	/* RTF格式的普通正文 */
	public static final int RTFTypeBody = -1; 
	/* 文本格式的普通正文 */
	public static final int TextTypeBody = 0; 
	/* 普通文件正文 */
	public static final int CommondBody = 1; 
	/* 文件版本控制正文 */
	public static final int FileVersionBody = 2; 
	/* Word版本控制正文 */
	public static final int WordVersionBody = 3; 
	/* 普通附件 */
	public static final int CommondDocument = 4; 
	/* 文件版本控制附件 */
	public static final int FileVersionDocument = 5; 
	/* Word版本控制附件 */
	public static final int WordVersionDocument = 6; 
	/* URL链接附件 */
	public static final int URLDocument = 7; 
	/* 公文链接附件 */
	public static final int InstanceDocument = 8; 
	/* 扫描成Tif(Tiff)的文件附件 */
	public static final int ScanTifDocument = 9; 
	/* 扫描成jpg的文件附件[使用版本号(DocumentVersion)来存储页数] */
	public static final int ScanJpgDocument = 10; 
}
