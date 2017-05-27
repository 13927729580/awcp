package org.szcloud.framework.common.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.core.utils.mongodb.MongoDBUtils;
import org.szcloud.framework.formdesigner.core.domain.Attachment;
import org.szcloud.framework.formdesigner.core.domain.Store;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.unit.vo.PunSystemVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;
import org.szcloud.framework.venson.util.DocumentToHtml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;

import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
@Controller(value = "myFileController")
@RequestMapping("/common/file")
public class FileController {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	@RequestMapping(value = "/upload")
	public String upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject rtn = new JSONObject();
		Object obj3 = SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		if (obj3 instanceof PunUserBaseInfoVO) {
			PunUserBaseInfoVO user = (PunUserBaseInfoVO) obj3;
			// 创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				MongoClient client = MongoDBUtils.getMongoClient();
				DB db = client.getDB("myFiles");
				GridFS myFS = new GridFS(db);
				while (iter.hasNext()) {
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						// 如果名称不为"",说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {
							// 重命名上传后的文件名
							StringBuilder sb = new StringBuilder();
							sb.append(user.getUserId()).append("/").append(file.getOriginalFilename());
							String fileName = sb.toString();
							try {
								// 存到mangoDB
								String uuid = UUID.randomUUID().toString();
								GridFSInputFile input = myFS.createFile(file.getBytes());
								input.setContentType(file.getContentType());
								input.setFilename(fileName);
								input.put("id", uuid);
								input.put("user", user.getUserId());
								input.save();

								// 存入本地附件表
								Attachment att = new Attachment();
								att.setId(uuid);
								att.setStorageId(uuid);
								att.setFileName(fileName);
								att.setContentType(file.getContentType());
								att.setSize(file.getSize());
								att.setUserId(user.getUserId());
								att.setUserName(user.getName());
								att.setSystemId(ControllerHelper.getSystemId());
								att.save();
								rtn.put("flag", 1);
								rtn.put("msg", uuid);

							} catch (IllegalStateException e) {
								rtn.put("flag", 2);
								rtn.put("msg", "上传失败");
								e.printStackTrace();
							} catch (IOException e) {
								rtn.put("flag", 2);
								rtn.put("msg", "上传失败");
								e.printStackTrace();
							} finally {
								if (client != null) {
									client.close();
								}
							}
						} else {
							rtn.put("flag", 3);
							rtn.put("msg", "文件不存在");
						}
					} else {
						rtn.put("flag", 3);
						rtn.put("msg", "文件不存在");
					}
				}
			}
		}
		response.getWriter().println(rtn.toJSONString());
		return null;
	}

	private static final String upload_file = "uploadfile";

	/**
	 * 新增上传文件到指定目录下的文件夹
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/uploadToFolder")
	public String uploadToFolder(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject rtn = new JSONObject();
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					PunUserBaseInfoVO user = ControllerHelper.getUser();
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为"",说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {

						try {
							// 存到文件夹
							String suffix = file.getOriginalFilename()
									.substring(file.getOriginalFilename().lastIndexOf("."));
							String uuid = UUID.randomUUID().toString();
							String fileName = uuid + suffix;
							String uploadFolder = request.getParameter("uploadFolder");
							String uploadPath = null;
							Store store = Store.get(uploadFolder);
							if (store != null && store.getContent() != null) {
								JSONObject content = JSON.parseObject(store.getContent());
								uploadPath = content.getString("filePath");
							}
							if (uploadPath == null) {
								rtn.put("flag", 2);
								rtn.put("msg", "上传路径未定义");
								response.getWriter().println(rtn.toJSONString());
								return null;
							}
							File filePath = new File(uploadPath, fileName);
							if (!filePath.exists()) {
								filePath.mkdirs();
							}
							file.transferTo(filePath);

							// 存入本地附件表
							Attachment att = new Attachment();
							att.setId(uuid);
							att.setStorageId(filePath.getAbsolutePath());
							att.setFileName(file.getOriginalFilename());
							att.setContentType(file.getContentType());
							att.setSize(file.getSize());
							att.setUserId(user.getUserId());
							att.setUserName(user.getName());
							att.setSystemId(ControllerHelper.getSystemId());
							att.save();
							rtn.put("flag", 1);
							rtn.put("msg", uuid);

						} catch (IllegalStateException e) {
							rtn.put("flag", 2);
							rtn.put("msg", "上传失败");
							e.printStackTrace();
						} catch (IOException e) {
							rtn.put("flag", 2);
							rtn.put("msg", "上传失败");
							e.printStackTrace();
						}
					} else {
						rtn.put("flag", 3);
						rtn.put("msg", "文件不存在");
					}
				} else {
					rtn.put("flag", 3);
					rtn.put("msg", "文件不存在");
				}
			}
		}
		response.getWriter().println(rtn.toJSONString());
		return null;
	}

	/* 此方法适用于文件的替换 */
	@ResponseBody
	@RequestMapping(value = "/upload_word")
	public String upload_word(HttpServletRequest request, HttpServletResponse response) {
		JSONObject rtn = new JSONObject();
		String wordId = request.getParameter("fileName");
		if (wordId != null) {
			Object obj2 = Tools.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
			if (obj2 instanceof PunSystemVO) {
				PunSystemVO system = (PunSystemVO) obj2;
				Object obj3 = Tools.getObjectFromSession(SessionContants.CURRENT_USER);
				PunUserBaseInfoVO user = (PunUserBaseInfoVO) obj3;
				// 创建一个通用的多部分解析器
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
						request.getSession().getServletContext());
				// 判断 request 是否有文件上传,即多部分请求
				if (multipartResolver.isMultipart(request)) {
					// 转换成多部分request
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
					// 取得request中的所有文件名
					Iterator<String> iter = multiRequest.getFileNames();
					MongoClient client = MongoDBUtils.getMongoClient();
					DB db = client.getDB("myFiles");
					GridFS myFS = new GridFS(db);
					while (iter.hasNext()) {
						// 取得上传文件
						MultipartFile file = multiRequest.getFile(iter.next());
						if (file != null) {
							// 取得当前上传文件的文件名称
							String myFileName = file.getOriginalFilename();
							// 如果名称不为"",说明该文件存在，否则说明该文件不存在
							if (myFileName.trim() != "") {
								// 重命名上传后的文件名
								StringBuilder sb = new StringBuilder();
								String fileName = sb.toString();
								String result = get(wordId);
								JSONObject obj = JSONObject.parseObject(result);
								if (obj.getString("result").equals("1")) {
									String ids[] = { wordId };
									delete_word(ids, multiRequest, response);
								}
								try {

									// 存到mangoDB
									GridFSInputFile input = myFS.createFile(file.getBytes());
									input.setContentType(file.getContentType());
									input.setFilename(fileName);
									input.put("id", wordId);
									if (user != null) {
										input.put("user", user.getUserId());
									}

									input.put("sysId", system.getSysId());
									input.save();

									// 存入本地附件表
									Attachment att = new Attachment();
									att.setId(wordId);
									att.setStorageId(wordId);
									att.setFileName(fileName);
									att.setContentType(file.getContentType());
									att.setSize(file.getSize());
									att.setSystemId(system.getSysId());
									if (user != null) {
										att.setUserId(user.getUserId());
										att.setUserName(user.getName());
									}

									att.save();
									rtn.put("flag", 1);
									rtn.put("msg", wordId);

								} catch (IllegalStateException e) {
									rtn.put("flag", 2);
									rtn.put("msg", "上传失败");
									e.printStackTrace();
								} catch (IOException e) {
									rtn.put("flag", 2);
									rtn.put("msg", "上传失败");
									e.printStackTrace();
								} finally {
									if (client != null) {
										client.close();
									}
								}
							} else {
								rtn.put("flag", 3);
								rtn.put("msg", "文件不存在");
							}
						} else {
							rtn.put("flag", 3);
							rtn.put("msg", "文件不存在");
						}
					}
				}
			}
		}
		return rtn.toJSONString();
	}

	/**
	 * 上传附件，后台开发段使用，不控制权限，不校验权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadDirect")
	public String uploadDirect(HttpServletRequest request, HttpServletResponse response) {
		JSONObject rtn = new JSONObject();

		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			MongoClient client = MongoDBUtils.getMongoClient();
			DB db = client.getDB("myFiles");
			GridFS myFS = new GridFS(db);
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为"",说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						// 重命名上传后的文件名
						StringBuilder sb = new StringBuilder();
						sb.append("999999").append("/").append("999999").append("/").append("999999").append("/")
								.append(file.getOriginalFilename());
						String fileName = sb.toString();
						try {

							// 存到mangoDB
							String uuid = UUID.randomUUID().toString();
							GridFSInputFile input = myFS.createFile(file.getBytes());
							input.setContentType(file.getContentType());
							input.setFilename(fileName);
							input.put("id", uuid);
							input.put("user", "");
							input.put("sysId", "");
							input.save();

							// 存入本地附件表
							Attachment att = new Attachment();
							att.setId(uuid);
							att.setStorageId(uuid);
							att.setFileName(fileName);
							att.setContentType(file.getContentType());
							att.setSize(file.getSize());
							att.setSystemId(999999L);
							att.setUserId(999999l);
							att.setUserName("999999");
							att.save();
							rtn.put("flag", 1);
							rtn.put("msg", uuid);

						} catch (IllegalStateException e) {
							rtn.put("flag", 2);
							rtn.put("msg", "上传失败");
							e.printStackTrace();
						} catch (IOException e) {
							rtn.put("flag", 2);
							rtn.put("msg", "上传失败");
							e.printStackTrace();
						} finally {
							if (client != null) {
								client.close();
							}
						}
					} else {
						rtn.put("flag", 3);
						rtn.put("msg", "文件不存在");
					}
				} else {
					rtn.put("flag", 3);
					rtn.put("msg", "文件不存在");
				}
			}
		}
		return rtn.toJSONString();
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/download")
	public String download(HttpServletRequest request, HttpServletResponse response, String fileId) throws IOException {
		Attachment att = Attachment.get(fileId);
		InputStream is = null;
		String fileName = null;
		if (att != null && !att.getStorageId().equals(fileId)) {
			String filePath = att.getStorageId();
			is = new FileInputStream(filePath);
			fileName = att.getFileName();
		} else {
			MongoClient client = MongoDBUtils.getMongoClient();
			DB db = client.getDB("myFiles");
			GridFS myFS = new GridFS(db);

			DBObject query = new BasicDBObject("id", fileId);
			GridFSDBFile file = myFS.findOne(query);
			is = file.getInputStream();
			fileName = file.getFilename();
		}

		if (is != null) {
			int i;
			try {
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				response.setContentType("application/x-msdownload;");
				response.setHeader("Content-disposition",
						"attachment; filename=" + ControllerHelper.processFileName(fileName));
				OutputStream out = response.getOutputStream(); // 读取文件流
				i = 0;
				byte[] buffer = new byte[4096];
				while ((i = is.read(buffer)) != -1) { // 写文件流
					out.write(buffer, 0, i);
				}
				out.flush();
				out.close();

			} catch (IOException e) {

				e.printStackTrace();
			}

		} else {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			out.println("文件不存在");
			out.close();
		}
		return null;
	}

	/**
	 * 批量下载文件
	 * 
	 * 先将文件写到本地，然后封装到ZIP中下载
	 * 
	 * @param request
	 * @param response
	 * @param fileIds
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/batchDownload")
	public String batchDownload(HttpServletRequest request, HttpServletResponse response, String[] fileIds)
			throws IOException {

		// 生成的ZIP文件名为.zip
		String tmpFileName = "downloadFile.zip";
		// zip文件保存位置
		String FilePath = FileController.class.getResource("/").getFile().replaceFirst("WEB-INF/classes/", "")
				.replaceFirst("classes/", "") + "file_names/";// "c:/batchDownloadFile/";

		// 创建保存zip的文件夹
		File file1 = new File(FilePath);
		if (!file1.exists()) {
			file1.mkdirs();
		}

		// 下载的文件的临时保存位置
		String mogodbFilePath = FileController.class.getResource("/").getFile().replaceFirst("WEB-INF/classes/", "")
				.replaceFirst("classes/", "");
		mogodbFilePath += "file_down/";
		del(mogodbFilePath);
		File file2 = new File(mogodbFilePath);
		if (!file2.exists()) {
			file2.mkdirs();
		}

		byte[] buffer = new byte[1024];
		String strZipPath = FilePath + tmpFileName;
		ZipOutputStream out = null;
		FileInputStream in = null;
		try {
			if (directToPath(mogodbFilePath, fileIds)) {

				out = new ZipOutputStream(new FileOutputStream(strZipPath));
				File dirFile = new File(mogodbFilePath);

				// 获取该文件夹下的所有文件
				File[] file = dirFile.listFiles();
				for (int i = 0; i < file.length; i++) {
					in = new FileInputStream(file[i]);
					String fileName = file[i].getName();
					// 设置压缩文件内的字符编码，不然会变成乱码
					out.setEncoding("GBK");
					out.putNextEntry(new ZipEntry(fileName));

					int len;
					// 读入需要下载的文件的内容，打包到zip文件
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					in.close();
				}
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.closeEntry();
				out.close();
			}
			if (in != null) {
				in.close();
			}
		}

		BufferedInputStream buff = null;
		OutputStream myout = null;
		FileInputStream fis = null;

		try {
			response.setContentType("text/html; charset=UTF-8");
			// 创建file对象
			File file = new File(FilePath + tmpFileName);

			// 设置response的编码方式
			response.setContentType("application/octet-stream");

			// 写明要下载的文件的大小
			response.setContentLength((int) file.length());

			// 设置附加文件名
			// 解决中文乱码
			response.setHeader("Content-Disposition",
					"attachment;filename=" + ControllerHelper.processFileName(tmpFileName));

			// 读出文件到i/o流
			fis = new FileInputStream(file);
			buff = new BufferedInputStream(fis);

			byte[] b = new byte[1024];// 相当于我们的缓存

			long k = 0;// 该值用于计算当前实际下载了多少字节

			// 从response对象中得到输出流,准备下载
			myout = response.getOutputStream();

			// 开始循环下载
			while (k < file.length()) {

				int j = buff.read(b, 0, 1024);
				k += j;

				// 将b中的数据写到客户端的内存
				myout.write(b, 0, j);

			}

			// 将写入到客户端的内存的数据,刷新到磁盘
			myout.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (buff != null) {
					buff.close();
				}
				if (myout != null) {
					myout.close();
				}
				if (fis != null) {
					fis.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 删除目录
	 * 
	 * @param filepath
	 * @throws IOException
	 */
	public void del(String filepath) throws IOException {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
			}
		}
	}

	public boolean directToPath(String mogodbFilePath, String[] fileIds) throws IOException {
		boolean flag = false;

		// Mongo mongo = new Mongo();
		// DB db = mongo.getDB("test");
		// GridFS gridFS=new GridFS(db,"fs");

		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myFiles");
		GridFS myFS = new GridFS(db);

		List<GridFSDBFile> gridFSDBFileList = new ArrayList<GridFSDBFile>();
		// DBObject query=new BasicDBObject("userId", 1);
		// gridFSDBFileList = gridFS.find(query);

		for (String fileId : fileIds) {
			DBObject query = new BasicDBObject("id", fileId);
			GridFSDBFile file = myFS.findOne(query);
			gridFSDBFileList.add(file);
		}

		String fileName = null;
		try {
			// 循环所有文件
			if (gridFSDBFileList != null && gridFSDBFileList.size() > 0) {
				for (GridFSDBFile nb : gridFSDBFileList) {
					// 获取原文件名
					fileName = nb.get("filename").toString()
							.substring(nb.get("filename").toString().lastIndexOf("/") + 1);
					if (StringUtils.isNotBlank(fileName)) {
						int lin = fileName.lastIndexOf(".");
						if (lin < 0) {
							fileName += nb.getContentType().lastIndexOf(".") < 0 ? "." + nb.getContentType()
									: nb.getContentType();
						}
					}
					// fileName = new String(name1.getBytes("GBK"),"ISO8859-1");
					// 将mogodb中的文件写到指定文件夹
					InputStream is = nb.getInputStream();

					FileOutputStream fos = new FileOutputStream(mogodbFilePath + fileName);
					byte[] b = new byte[1024];
					while ((is.read(b)) != -1) {
						fos.write(b);
					}
					is.close();
					fos.close();

					// nb.writeTo(mogodbFilePath + fileName);
					// out2 = new FileOutputStream(mogodbFilePath + fileName);
					// os2 = new BufferedOutputStream(out2);
					// nb.writeTo(os2);
					flag = true;
				}
			}
		} catch (Exception e) {
			flag = false;
		} finally {
			// os2.flush();
			// os2.close();
			// out2.close();
		}
		return flag;
	}

	/**
	 * 
	 * 根据ID取得文件信息
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/get")
	public String get(String id) {
		Attachment att = Attachment.get(id);
		JSONObject rtn = new JSONObject();
		if (att != null && !att.getStorageId().equals(id)) {
			JSONObject o = transAttToJSON(att);
			rtn.put("result", "1");
			rtn.put("msg", o.toJSONString());
			rtn.put("status", 0);
			rtn.put("data", o.toJSONString());
		} else {
			MongoClient client = MongoDBUtils.getMongoClient();
			DB db = client.getDB("myFiles");
			GridFS myFS = new GridFS(db);
			DBObject query = new BasicDBObject("id", id);
			GridFSDBFile file = myFS.findOne(query);
			if (file != null) {
				JSONObject o = transAttToJSON(att);
				rtn.put("result", "1");
				rtn.put("msg", o.toJSONString());
				rtn.put("status", 0);
				rtn.put("data", o.toJSONString());
			} else {
				rtn.put("result", "3");
				rtn.put("status", 0);
				rtn.put("msg", "文件不存在");
			}
		}

		return rtn.toJSONString();
	}

	/**
	 * 
	 */
	private JSONObject transAttToJSON(Attachment att) {
		JSONObject o = new JSONObject();
		o.put("id", att.getId());
		o.put("length", att.getSize());
		String tmp = att.getFileName();
		tmp = tmp.substring(tmp.lastIndexOf("/") + 1);
		o.put("filename", tmp);
		return o;
	}

	@ResponseBody
	@RequestMapping(value = "/showPicture")
	public String showPicture(HttpServletRequest request, HttpServletResponse response, String id) {
		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myFiles");
		GridFS myFS = new GridFS(db);
		DBObject query = new BasicDBObject("id", id);
		response.setContentType("image/jpeg;");
		response.setHeader("Content-disposition", "inline; filename=img.jpg");
		try {
			GridFSDBFile file = myFS.findOne(query);
			if (file != null) {
				InputStream is = null;
				if (file.getContentType().toLowerCase().contains("video")) {
					is = new FileInputStream(ControllerHelper.getUploadPath("images") + "video_icon.png");
				} else {
					is = file.getInputStream();
				}
				int i;
				OutputStream out = response.getOutputStream(); // 读取文件流
				i = 0;
				byte[] buffer = new byte[4096];
				while ((i = is.read(buffer)) != -1) { // 写文件流
					out.write(buffer, 0, i);
				}
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}

		return null;
	}

	/**
	 * @param ids
	 *            需要删除的文件id
	 * @param request
	 * @param response
	 * @return {result,msg} result = 1 sucess result = 2 部分删除成功 result = 3 删除失败
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public String delete(String[] ids, HttpServletRequest request, HttpServletResponse response) {

		JSONObject rtn = new JSONObject();
		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myFiles");
		GridFS myFS = new GridFS(db);
		StringBuilder msg = new StringBuilder();
		int flag = 0;
		long userId = ControllerHelper.getUserId();
		// FIXME 控制值允许上传者才能下载 ?

		for (String id : ids) {
			Attachment att = Attachment.get(id);
			if (att != null && !att.getStorageId().equals(id)) {
				if (userId == att.getUserId()) {
					String filePath = att.getStorageId();
					new File(filePath).delete();
					att.remove();
					msg.append("文件[").append(att.getFileName()).append("]删除成功！|");
					flag++;
				} else {
					msg.append("您没有权限删除文件[").append(att.getFileName()).append("]！|");
				}

			} else {
				DBObject query = new BasicDBObject("id", id);
				GridFSDBFile file = myFS.findOne(query);
				if (file == null) {
					continue;
				}
				String filename = file.getFilename();
				if (filename.contains(userId + "")) {
					myFS.remove(query); // 删除mangoDb中的附件
					att.remove();
					msg.append("文件[").append(filename.substring(filename.lastIndexOf("/") + 1)).append("]删除成功！|");
					flag++;
				} else {
					msg.append("您没有权限删除文件[").append(filename.substring(filename.lastIndexOf("/") + 1)).append("]！|");
				}
			}

		}
		if (flag == 0) {
			rtn.put("result", "3");
			rtn.put("msg", "删除失败。原因：没有权限");
			rtn.put("message", "删除失败。原因：没有权限");
			rtn.put("status", -1);
		} else if (flag == ids.length) {
			rtn.put("result", "1");
			rtn.put("msg", "删除成功！");
			rtn.put("status", 0);
		} else {
			rtn.put("status", -1);
			rtn.put("result", "2");
			if (msg.length() > 0)
				msg.deleteCharAt(msg.length() - 1);
			rtn.put("message", msg.toString());
			rtn.put("msg", msg.toString());
		}

		return rtn.toJSONString();
	}

	/**
	 * @param ids
	 *            需要删除的文件id
	 * @param request
	 * @param response
	 * @return {result,msg} result = 1 sucess result = 2 部分删除成功 result = 3 删除失败
	 */
	@ResponseBody
	@RequestMapping(value = "/remove")
	public ReturnResult remove(@RequestParam("id") String id) {
		ReturnResult result = ReturnResult.get();
		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myFiles");
		GridFS myFS = new GridFS(db);
		DBObject query = new BasicDBObject("id", id);
		myFS.remove(query); // 删除mangoDb中的附件
		Attachment att = new Attachment(); // 删除对应的附件表记录
		att.setId(id);
		att.remove();
		result.setStatus(StatusCode.SUCCESS);
		return result;
	}

	/* 没有权限控制删除用于文件的替换 */
	@ResponseBody
	@RequestMapping(value = "/delete_word")
	public String delete_word(String[] ids, HttpServletRequest request, HttpServletResponse response) {
		JSONObject rtn = new JSONObject();
		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myFiles");
		GridFS myFS = new GridFS(db);
		// 开发者模式直接删除
		for (String id : ids) {
			DBObject query = new BasicDBObject("id", id);
			myFS.remove(query); // 删除mangoDb中的附件
			Attachment att = new Attachment(); // 删除对应的附件表记录
			att.setId(id);
			att.remove();
		}
		rtn.put("result", "1");
		rtn.put("msg", "删除成功！");
		return rtn.toJSONString();
	}

	/**
	 * kindeditor文件上传后台方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws FileUploadException
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadImg")
	public String uploadImege(HttpServletRequest request, HttpServletResponse response) throws FileUploadException {
		// uuid
		String uuid = UUID.randomUUID().toString();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,mp4,m4v,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		// 最大文件大小
		long maxSize = 10000000;

		response.setContentType("text/html; charset=UTF-8");

		JSONObject obj = new JSONObject();
		if (!ServletFileUpload.isMultipartContent(request)) {
			// out.println(getError("请选择文件。"));

			obj.put("error", 1);
			obj.put("message", "请选择文件！");
			return obj.toJSONString();
		}
		/*
		 * //检查目录 File uploadDir = new File(savePath);
		 * 
		 * //检查目录写权限 if(!uploadDir.canWrite()){
		 * //out.println(getError("上传目录没有写权限。")); obj.put("error", 1);
		 * obj.put("message", "上传目录没有写权限！"); return obj.toJSONString(); }
		 */

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!extMap.containsKey(dirName)) {
			// out.println(getError("目录名不正确。"));
			obj.put("error", 1);
			obj.put("message", "目录名不正确！");
			return obj.toJSONString();
		}

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		MultipartFile imgFile = multipartRequest.getFile("imgFile");
		// 获取原图片文件名字及文件扩展名
		String fileName = imgFile.getOriginalFilename();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		// 上传格式检查
		if (!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)) {
			// out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" +
			// extMap.get(dirName) + "格式。"));
			obj.put("error", 1);
			obj.put("message", "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
			return obj.toJSONString();
		}
		// 上传图片文件命名格式"上传时间数字_随机数"
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		if (imgFile.getSize() > maxSize) {
			obj.put("error", 1);
			obj.put("message", "上传文件过大！");
			return obj.toJSONString();
		}
		try {
			// 将图片保存到mogoDb服务器中
			MongoClient client = MongoDBUtils.getMongoClient();
			DB db = client.getDB("myFiles");
			GridFS myFS = new GridFS(db);
			InputStream in = imgFile.getInputStream();
			GridFSInputFile input = myFS.createFile(in);
			input.setFilename(newFileName.toString());
			input.put("id", uuid);
			input.put("user", "");
			input.put("sysId", "");
			input.save();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			obj.put("error", 1);
			obj.put("message", "上传文件失败");
			return obj.toJSONString();
		} catch (IOException e) {
			e.printStackTrace();
			obj.put("error", 1);
			obj.put("message", "上传文件失败");
			return obj.toJSONString();
		}
		obj.put("error", 0);
		// 返回访问图片的url到富文本框中，通过getImage方法访问图片
		obj.put("url", request.getContextPath() + "/common/file/getImage.do?uuid=" + uuid);
		return obj.toJSONString();

	}

	/**
	 * 富文本框中通过url访问上传都mogodb的图片
	 * 
	 * @param uuid
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/getImage")
	public String getImage(String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		MongoClient client = MongoDBUtils.getMongoClient();
		DB db = client.getDB("myFiles");
		GridFS myFS = new GridFS(db);
		DBObject query = new BasicDBObject("id", uuid);
		GridFSDBFile file = myFS.findOne(query);
		InputStream inputStream = file.getInputStream();
		/*
		 * int size = 0; for(;inputStream.read()!=-1;){ size ++; }
		 */
		byte[] tt = new byte[1024];

		// inputStream.read(temp);

		ServletOutputStream stream = response.getOutputStream();
		// stream.write(temp);
		int len = 0;
		while ((len = inputStream.read(tt)) != -1) {
			stream.write(tt);
		}
		stream.flush();
		stream.close();

		return null;
	}

	/**
	 * 将base64位编码转为图片，保存，返回图片路径
	 */
	@ResponseBody
	@RequestMapping(value = "/generateImageByBase64Str")
	public String generateImageByBase64Str(HttpServletRequest request, String imgStr) {// 对字节数组字符串进行Base64解码并生成图片
		JSONObject resultObj = new JSONObject();
		if (imgStr == null)
			return "";// 图像数据为空
		String[] imgStrs = imgStr.split(",");
		BASE64Decoder decoder = new BASE64Decoder();
		logger.debug(imgStr);
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(imgStrs[imgStrs.length - 1]);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpg图片
			/*
			 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); String
			 * bathPath =
			 * request.getSession().getServletContext().getRealPath("/") +
			 * "attached/image/"+sdf.format(new Date()); File attached = new
			 * File(bathPath); if(!attached.exists()){ attached.mkdirs(); }
			 * SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			 * String fileName = df.format(new Date()) + "_" + new
			 * Random().nextInt(1000) + ".jpg"; OutputStream out = new
			 * FileOutputStream(attached.getPath()+"/"+fileName);
			 * out.write(bytes); out.flush(); out.close();
			 */
			// 把图片保存到mogodb上
			MongoClient client = MongoDBUtils.getMongoClient();
			DB db = client.getDB("myFiles");
			GridFS myFS = new GridFS(db);
			GridFSInputFile input = myFS.createFile(bytes);
			String uuid = UUID.randomUUID().toString();
			input.setFilename(uuid);
			input.put("id", uuid);
			input.put("user", "");
			input.put("sysId", "");
			input.save();
			// 返回访问图片的url到富文本框中，通过getImage方法访问图片
			resultObj.put("url", request.getContextPath() + "/common/file/getImage.do?uuid=" + uuid);
			return resultObj.toJSONString();
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping("/preview")
	public String preView(HttpServletResponse response, String fileId) throws Exception {
		Attachment att = Attachment.get(fileId);
		InputStream is = null;
		String fileName = null;
		if (att != null && !att.getStorageId().equals(fileId)) {
			String filePath = att.getStorageId();
			is = new FileInputStream(filePath);
			fileName = att.getFileName();
		} else {
			MongoClient client = MongoDBUtils.getMongoClient();
			DB db = client.getDB("myFiles");
			GridFS myFS = new GridFS(db);

			DBObject query = new BasicDBObject("id", fileId);
			GridFSDBFile file = myFS.findOne(query);
			is = file.getInputStream();
		}
		String msg = "";
		if (is != null) {
			// 读取文件信息，判断格式，如果是文本，直接显示，如果是word，先进行转化，然后返回html地址，
			String fileType = Tools.getTypePart(fileName);
			String ext = fileType.toLowerCase();
			if (StringUtils.isBlank(fileType)) {
				msg = "文件格式不支持预览，请先下载后再打开。";
			} else {
				if (ext.equals("txt") || ext.equals("doc") || ext.equals("docx") || ext.equals("xls")
						|| ext.equals("xlsx")) {
					String rootPath = ControllerHelper.UPLOAD_PREVIEW_PATH;
					String htmlPath = ControllerHelper.getUploadPath(ControllerHelper.UPLOAD_PREVIEW_PATH);
					String htmlName = fileId + ".html";
					String position = htmlPath + htmlName;
					if (!new File(position).exists()) {
						DocumentToHtml.getInstance().toHtml(is, fileName, htmlPath, fileId + ".html");
					}
					return "redirect:" + rootPath + "/" + htmlName;
				} else if (ext.equals("tif") || ext.equals("tiff")) {
					// 将tif图片转jpg
					response.setContentType("image/jpg;charset=UTF-8");
					response.setHeader("Content-disposition", "inline; filename=preview.jpg");
					ServletOutputStream out = response.getOutputStream();
					TIFToJPG(is, out);
					out.flush();
					out.close();
					return null;
				} else if (ext.equals("jpg") || ext.equals("png") || ext.equals("bmp") || ext.equals("gif")) {
					// 图片,重定向到 showPicture.do?id=
					return "redirect:showPicture.do?id=" + fileId;
				} else if (ext.equals("pdf")) {
					// 直接在线预览pdf
					response.setContentType("application/pdf;charset=UTF-8");
					response.setHeader("Content-disposition", "inline; filename=" + fileName);
					ServletOutputStream out = response.getOutputStream();
					out.write(IOUtils.toByteArray(is));
					out.flush();
					out.close();
					return null;
				} else if (ext.equals("html")) {
					// 直接在线预览pdf
					response.setContentType("text/html;charset=UTF-8");
					response.setHeader("Content-disposition", "inline; filename=" + fileName);
					ServletOutputStream out = response.getOutputStream();
					out.write(IOUtils.toByteArray(is));
					out.flush();
					out.close();
					return null;
				} else {
					msg = "文件格式不支持预览，请先下载后再打开。";
				}
			}
		} else {
			msg = "文件不存在";
		}
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(msg);
		writer.flush();
		return null;
	}

	private void TIFToJPG(InputStream input, OutputStream out) throws IOException {
		ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", input, null);
		ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", out, null);
		encoder.encode(decoder.decodeAsRenderedImage());
	}

}
