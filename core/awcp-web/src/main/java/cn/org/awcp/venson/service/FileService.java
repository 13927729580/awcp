package cn.org.awcp.venson.service;

import cn.org.awcp.venson.service.impl.FileServiceImpl.AttachmentVO;
import cn.org.awcp.venson.util.PlatfromProp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件上传通用接口（支持mongodb,folder）
 * 
 * @author Venson
 *
 */
public interface FileService {

	/** 文件存储类型为：Mongodb */
	int MONGODB = 0;
	/** 文件存储类型为：文件夹 */
	int FOLDER = 1;
	/** 文件存储类型为：FTP */
	int FTP = 2;
	/** 文件存储类型默认为：Mongodb */
	int DEFAULT = Integer.parseInt(PlatfromProp.getValue("default_upload_type"));

	String separator = "/";

	/**
	 *	保存文件
	 * @param input
	 * @param fileType
	 * @param fileName
	 * @param type
	 *            保存类型(1:mongodb2:文件夹3:ftp)
	 * @return
	 */
	String save(byte[] input, String fileType, String fileName, int type);

	/**
	 *	保存文件
	 * @param input
	 * @param fileType
	 * @param fileName
	 * @param type
	 *            保存类型(1:mongodb2:文件夹3:ftp)
	 * @param isIndex
	 *            是否需要建立文件索引
	 * @return
	 */
	String save(byte[] input, String fileType, String fileName, int type, boolean isIndex);

	/**
	 * 保存文件
	 * @param input
	 * @param fileType
	 * @param fileName
	 * @param type
	 *            保存类型(1:mongodb2:文件夹3:ftp)
	 * @param isIndex
	 *            是否需要建立文件索引
	 * @return
	 * @throws IOException
	 */
	String save(InputStream input, String fileType, String fileName, int type, boolean isIndex);

	/**
	 * 保存文件
	 * @param input
	 * @param fileType
	 * @param fileName
	 * @param type
	 *            保存类型(1:mongodb2:文件夹3:ftp)
	 * @return
	 * @throws IOException
	 */
	String save(InputStream input, String fileType, String fileName, int type);

	/**
	 * 
	 * @param input
	 * @param fileType
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	String save(InputStream input, String fileType, String fileName);

	/**
	 * 获取文件
	 * 
	 * @param id
	 * @return
	 */
	AttachmentVO get(String id);

	/**
	 * 获取文件
	 * 
	 * @param id
	 * @return
	 */
	InputStream getInputStream(String id);

	/**
	 * 获取文件
	 * 
	 * @param att
	 *            附件实例
	 * @return
	 */
	InputStream getInputStream(AttachmentVO att);

	/**
	 * 删除文件
	 * 
	 * @param isCheck
	 *            是否检查删除权限（上传人与删除人一致则可删）
	 * @param id
	 * @return
	 */
	boolean delete(boolean isCheck, String... id);

	/**
	 * 删除文件
	 * 
	 * @param id
	 * @return
	 */
	boolean delete(String... id);

	/**
	 * 下载文件
	 * 
	 * @param id
	 * @param out
	 * @return
	 */
	boolean download(String id, OutputStream out);

	/**
	 * 下载文件
	 * 
	 * @param att
	 *            附件实例
	 * @param out
	 * @return
	 */
	boolean download(AttachmentVO att, OutputStream out);

	/**
	 * 
	 * @param in
	 * @param out
	 * @return
	 */
	boolean copy(InputStream in, OutputStream out);

	/**
	 * 批量下载文件
	 * 
	 * @param ids
	 * @param out
	 * @return
	 */
	boolean batchDownload(String[] ids, OutputStream out);

	/**
	 * 建立文档搜索索引
	 * 
	 * @param input
	 *            文件流
	 * @param vo
	 *            文件
	 */
	void indexFileToSolr(InputStream input, AttachmentVO vo);

}
