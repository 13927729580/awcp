package org.szcloud.framework.venson.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
//import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.szcloud.framework.core.utils.mongodb.MongoDBUtils;
import org.szcloud.framework.formdesigner.core.domain.Attachment;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.venson.service.FileService;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Service("IFileService")
@Transactional
public class FileServiceImpl implements FileService {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(FileServiceImpl.class);

	@Override
	public Serializable save(InputStream input, String fileType, String fileName, int type, boolean isIndex) {
		String uuid = UUID.randomUUID().toString();
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			// 文件信息保存到附件表
			PunUserBaseInfoVO user = ControllerHelper.getUser();
			Attachment att = new Attachment();
			att.setId(uuid);
			att.setSize((long) input.available());
			att.setUserId(user.getUserId());
			att.setUserName(user.getName());
			att.setContentType(fileType);
			att.setSystemId(ControllerHelper.getSystemId());
			switch (type) {
			case FileService.MONGODB:
				in = new BufferedInputStream(input);
				GridFSInputFile gf = getGIF(in);
				gf.setContentType(fileType);
				gf.setFilename(fileName);
				gf.put("id", uuid);
				gf.put("user", user.getUserId());
				gf.save();
				att.setFileName(fileName);
				att.setStorageId(uuid);
				att.setType(FileService.MONGODB);
				break;
			case FileService.FOLDER:
				// 将分隔符统一替换
				fileName = fileName.replaceAll("\\\\", FileService.separator);
				// 获取文件名称
				String name = fileName.substring(fileName.lastIndexOf(FileService.separator) + 1, fileName.length());
				att.setType(FileService.FOLDER);
				att.setStorageId(fileName);
				att.setFileName(name);
				String path = fileName.split(name)[0];
				in = new BufferedInputStream(input);
				// 判断目录是否存在
				File file = new File(path);
				boolean flag = true;
				if (!file.exists()) {
					flag = file.mkdirs();
				}
				if (flag) {
					out = new BufferedOutputStream(new FileOutputStream(fileName));
					IOUtils.copy(in, out);
				} else {
					return null;
				}
				break;
			case FileService.FTP:
				// unrealized
				break;
			}
			att.save();
			if (isIndex) {
				indexFilesSolr(input, fileName, fileType);
			}
			return uuid;
		} catch (Exception e) {
			logger.info("ERROR", e);
			return null;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	@Override
	public Attachment get(Serializable id) {
		return Attachment.get(id);
	}

	@Override
	public boolean delete(boolean isCheck, Serializable... id) {
		Long userId = ControllerHelper.getUserId();
		boolean isSuccess = true;
		for (Serializable i : id) {
			Attachment att = get(i);
			if (isCheck) {
				if (att.getUserId() != userId) {
					isSuccess = false;
					continue;
				}
			}
			switch (att.getType()) {
			case FileService.MONGODB:
				MongoClient client = MongoDBUtils.getMongoClient();
				DB db = client.getDB("myFiles");
				GridFS myFS = new GridFS(db);
				DBObject query = new BasicDBObject("id", att.getStorageId());
				myFS.remove(query);
				break;

			case FileService.FOLDER:
				new File(att.getStorageId()).delete();
				break;
			}
			att.remove();
		}
		return isSuccess;
	}

	@Override
	public boolean delete(Serializable... id) {
		return this.delete(true, id);
	}

	@Override
	public InputStream getInputStream(Attachment att) {
		if (att == null) {
			return null;
		}
		InputStream in = null;
		switch (att.getType()) {
		case FileService.MONGODB:
			MongoClient client = MongoDBUtils.getMongoClient();
			DB db = client.getDB("myFiles");
			GridFS myFS = new GridFS(db);
			DBObject query = new BasicDBObject("id", att.getStorageId());
			GridFSDBFile gif = myFS.findOne(query);
			if (gif != null) {
				in = gif.getInputStream();
			}
			break;

		case FileService.FOLDER:
			try {
				File file = new File(att.getStorageId());
				if (file.exists()) {
					in = new FileInputStream(file);
				}
			} catch (FileNotFoundException e) {
				logger.info("ERROR", e);
			}
			break;
		}
		return in;
	}

	@Override
	public InputStream getInputStream(Serializable id) {
		return this.getInputStream(get(id));
	}

	@Override
	public boolean download(Attachment att, OutputStream out) {
		return copy(getInputStream(att), out);
	}

	@Override
	public boolean download(Serializable id, OutputStream out) {
		return copy(getInputStream(id), out);
	}

	public boolean copy(InputStream in, OutputStream out) {
		if (in != null && out != null) {
			try {
				IOUtils.copy(in, out);
				return true;
			} catch (IOException e) {
				logger.info("ERROR", e);
				return false;
			} finally {
				IOUtils.closeQuietly(in);
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean batchDownload(Serializable[] ids, OutputStream out) {
		ZipOutputStream zipOut = null;
		try {
			zipOut = new ZipOutputStream(out);
			for (Serializable id : ids) {
				Attachment att = get(id);
				InputStream inputStream = getInputStream(att);
				if (inputStream != null) {
					// 设置压缩文件内的字符编码，不然会变成乱码
					zipOut.setEncoding("GBK");
					zipOut.putNextEntry(new ZipEntry(att.getFileName()));
					// 读入需要下载的文件的内容，打包到zip文件
					IOUtils.copy(inputStream, zipOut);
					// 关闭输入流
					IOUtils.closeQuietly(inputStream);
				}
			}
			return true;
		} catch (IOException e) {
			logger.info("ERROR", e);
			return false;
		} finally {
			// 关闭流
			try {
				zipOut.flush();
				zipOut.closeEntry();
			} catch (IOException e) {
				logger.info("ERROE", e);
			}
			IOUtils.closeQuietly(zipOut);
		}
	}

	@Override
	public Serializable save(InputStream input, String fileType, String fileName, int type) {
		return this.save(input, fileType, fileName, type, false);
	}

	@Override
	public Serializable save(InputStream input, String fileType, String fileName) {
		return this.save(input, fileType, fileName, FileService.DEFAULT, false);
	}

	@Autowired
	public SolrClient solrClient;

	private GridFSInputFile getGIF(InputStream input) {
		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myFiles");
		GridFS myFS = new GridFS(db);
		return myFS.createFile(input);
	}

	public void indexFilesSolr(InputStream input, String fileName, String fileType) {
		ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
		try {
			up.addContentStream(new IFileStream(input, fileName, fileType));
			String filename = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
			String suffix = fileName.substring(filename.length(), fileName.length());
			String nfn = filename + System.currentTimeMillis() + suffix;
			up.setParam("literal.id", nfn);
			up.setParam("literal.title", fileName);
			up.setParam("uprefix", "attr_");
			up.setParam("fmap.content", "attr_content");
			up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
			solrClient.request(up);
		} catch (Exception e) {
			logger.info("ERROR", e);
		}
	}

}

class IFileStream extends ContentStreamBase {
	private InputStream input;

	public IFileStream(InputStream input, String fileName, String fileType) throws IOException {

		this.contentType = fileType;
		this.name = fileName;
		this.size = (long) input.available();
		this.input = input;
		this.sourceInfo = "inputStream";
	}

	public InputStream getStream() throws IOException {
		return input;
	}

	/**
	 * If an charset is defined (by the contentType) use that, otherwise use a file
	 * reader
	 */
	@Override
	public Reader getReader() throws IOException {
		String charset = getCharsetFromContentType(contentType);
		return charset == null ? new InputStreamReader(getStream()) : new InputStreamReader(getStream(), charset);
	}

}