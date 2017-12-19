package cn.org.awcp.venson.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Date;

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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.core.utils.mongodb.MongoDBUtils;
import cn.org.awcp.formdesigner.core.domain.Attachment;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.service.FileService;
import cn.org.awcp.venson.util.MD5Util;

@Service("IFileService")
@Transactional
public class FileServiceImpl implements FileService {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(FileServiceImpl.class);

	@Override
	public String save(InputStream input, String fileType, String fileName, int type, boolean isIndex) {
		if (input == null) {
			return null;
		}
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			byte[] arr = IOUtils.toByteArray(input);
			String uuid = MD5Util.getFileMD5String(arr);
			AttachmentVO vo = this.get(uuid);
			Attachment att;
			// 判断附件在数据库中是否已经存在
			if (vo != null) {
				// 判断文件流是否存在
				InputStream inputStream = this.getInputStream(vo);
				// 如果存在则直接返回
				if (inputStream != null) {
					IOUtils.closeQuietly(in);
					return uuid;
				} else {
					// 不存在则移除数据库数据
					att = new Attachment();
					att.setId(vo.getId());
					att.remove();
				}
			} else {
				att = new Attachment();
			}
			in = new BufferedInputStream(new ByteArrayInputStream(arr));
			// 文件信息保存到附件表
			PunUserBaseInfoVO user = ControllerHelper.getUser();
			att.setId(uuid);
			att.setSize((long) in.available());
			att.setUserId(user.getUserId());
			att.setUserName(user.getName());
			att.setContentType(fileType);
			att.setSystemId(ControllerHelper.getSystemId());
			switch (type) {
			case FileService.MONGODB:
				GridFS myFS = getGridFS();
				GridFSInputFile gf = myFS.createFile(in);
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
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	@Override
	public AttachmentVO get(String id) {
		Attachment att = Attachment.get(Attachment.class, id);
		if (att != null) {
			AttachmentVO vo = new AttachmentVO();
			BeanUtils.copyProperties(Attachment.get(Attachment.class, id), vo, false);
			return vo;
		} else {
			return null;
		}
	}

	public static class AttachmentVO {
		private String id;
		private String storageId;

		private String fileName;

		private Long userId;

		private String userName;

		private String contentType;

		private Long systemId;

		private Long size;

		private Date createTime;
		private Date updateTime;

		private int type;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getStorageId() {
			return storageId;
		}

		public void setStorageId(String storageId) {
			this.storageId = storageId;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public Long getSystemId() {
			return systemId;
		}

		public void setSystemId(Long systemId) {
			this.systemId = systemId;
		}

		public Long getSize() {
			return size;
		}

		public void setSize(Long size) {
			this.size = size;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

	}

	@Override
	public boolean delete(boolean isCheck, String... id) {
		long userId = ControllerHelper.getUserId();
		boolean isSuccess = true;
		for (String i : id) {
			AttachmentVO att = get(i);
			if (att == null) {
				continue;
			}
			if (isCheck) {
				if (att.getUserId() != userId) {
					isSuccess = false;
					continue;
				}
			}
			switch (att.getType()) {
			case FileService.MONGODB:
				GridFS myFS = getGridFS();
				DBObject query = new BasicDBObject("id", att.getStorageId());
				myFS.remove(query);
				break;

			case FileService.FOLDER:
				new File(att.getStorageId()).delete();
				break;
			}
			Attachment atte = new Attachment();
			atte.setId(att.getId());
			atte.remove();
		}
		return isSuccess;
	}

	@Override
	public boolean delete(String... id) {
		return this.delete(true, id);
	}

	@Override
	public InputStream getInputStream(AttachmentVO att) {
		if (att == null) {
			return null;
		}
		InputStream in = null;
		switch (att.getType()) {
		case FileService.MONGODB:
			GridFS myFS = getGridFS();
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
	public InputStream getInputStream(String id) {
		return this.getInputStream(get(id));
	}

	@Override
	public boolean download(AttachmentVO att, OutputStream out) {
		return copy(getInputStream(att), out);
	}

	@Override
	public boolean download(String id, OutputStream out) {
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
	public boolean batchDownload(String[] ids, OutputStream out) {
		ZipOutputStream zipOut = null;
		try {
			zipOut = new ZipOutputStream(out);
			for (String id : ids) {
				AttachmentVO att = get(id);
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
	public String save(InputStream input, String fileType, String fileName, int type) {
		return this.save(input, fileType, fileName, type, false);
	}

	@Override
	public String save(InputStream input, String fileType, String fileName) {
		return this.save(input, fileType, fileName, FileService.DEFAULT, false);
	}

	@Autowired
	public SolrClient solrClient;

	private GridFS getGridFS() {
		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myFiles");
		return new GridFS(db);
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