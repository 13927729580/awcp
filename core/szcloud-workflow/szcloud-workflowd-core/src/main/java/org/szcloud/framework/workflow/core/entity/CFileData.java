package org.szcloud.framework.workflow.core.entity;

import org.szcloud.framework.workflow.core.module.MFile;

/**
 * 临时文件转换和存储对象：用于正文附件对象中
 * 
 * @author Jackie.wang
 * 
 */
public class CFileData {

	// #==========================================================================#
	// 常量定义
	// #==========================================================================#
	/**
	 * 内存文件存储的最大长度，当文件的长度
	 */
	private static final int constMaxMemorySaveLength = 2000;

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的正文附件对象
	 */
	public CDocument Document = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 文件内容
	 */
	private String ms_Content = null;

	/**
	 * 读取文件内容（文本）
	 * 
	 * @return
	 */
	public String getContent() {
		try {
			if (ml_FileLength < constMaxMemorySaveLength) {
				if (ms_Content == null)
					return " ";
				return ms_Content;
			} else {
				return MFile.readTxtFile(ms_TempFileName);
			}
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 设置文件内容（文本）
	 * 
	 * @param value
	 */
	public void setContent(String value) {
		try {
			int ll_Length = value.length();
			if (ll_Length < constMaxMemorySaveLength) {
				if (ml_FileLength >= constMaxMemorySaveLength) {
					MFile.killFile(ms_TempFileName); // 替换原语句：Kill
					ms_TempFileName = "";
				}
				ms_Content = value;
			} else {
				if (ml_FileLength < constMaxMemorySaveLength) {
					ms_TempFileName = MFile.getTempFile("tmp");
				}
				MFile.writeTxtFile(ms_TempFileName, value);
				ms_Content = "";
			}
			ml_FileLength = ll_Length;
		} catch (Exception ex) {

		}
	}

	/**
	 * 读取文件内容（字节）
	 * 
	 * @return
	 */
	public byte[] getBytes() {
		try {
			if (ml_FileLength < constMaxMemorySaveLength) {
				if (ms_Content == null)
					return " ".getBytes();
				return ms_Content.getBytes();
			} else {
				return MFile.readFile(ms_TempFileName);
			}
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 设置文件内容（字节）
	 * 
	 * @param value
	 */
	public void setBytes(byte[] value) {
		try {
			int ll_Length = value.length;

			if (ll_Length < constMaxMemorySaveLength) {
				if (ml_FileLength >= constMaxMemorySaveLength) {
					MFile.killFile(ms_TempFileName); // 替换原语句：Kill
					ms_TempFileName = "";
				}
				ms_Content = value.toString();
			} else {
				if (ml_FileLength < constMaxMemorySaveLength) {
					ms_TempFileName = MFile.getTempFile("tmp");
				}
				MFile.writeFile(ms_TempFileName, value);
				ms_Content = "";
			}
			ml_FileLength = ll_Length;
		} catch (Exception ex) {

		}
	}

	/**
	 * 文件内容长度
	 */
	private int ml_FileLength = 0;

	/**
	 * 文件内容长度
	 * 
	 * @return
	 */
	public int getFileLength() {
		return ml_FileLength;
	}

	/**
	 * 文件内容长度
	 * 
	 * @param value
	 */
	public void setFileLength(int value) {
		ml_FileLength = value;
	}

	/**
	 * 临时文件名称
	 */
	private String ms_TempFileName = null;

	/**
	 * 临时文件名称
	 * 
	 * @return
	 */
	public String getTempFileName() {
		return ms_TempFileName;
	}

	/**
	 * 临时文件名称
	 * 
	 * @param value
	 */
	public void setTempFileName(String value) {
		ms_TempFileName = value;
	}

	// #==========================================================================#
	// 常用过程或函数定义
	// #==========================================================================#

	/**
	 * 注销
	 */
	public void ClearUp() {
		// 所属的正文附件对象
		this.Document = null;
	}

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return
	 */
	public String IsValid(int ai_SpaceLength) {
		return "";
	}

}
