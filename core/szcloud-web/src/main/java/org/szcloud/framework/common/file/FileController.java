package org.szcloud.framework.common.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.szcloud.framework.core.utils.Tools;
import org.szcloud.framework.formdesigner.core.domain.Attachment;
import org.szcloud.framework.formdesigner.core.domain.Store;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.venson.service.FileService;
import org.szcloud.framework.venson.util.DocumentToHtml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import sun.misc.BASE64Decoder;

@Controller(value = "myFileController")
@RequestMapping("/common/file")
public class FileController {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(FileController.class);
	@Resource(name = "IFileService")
	private FileService fileService;

	@RequestMapping(value = "/upload")
	public String upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject rtn = new JSONObject();
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		List<Serializable> list = new ArrayList<>();
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			String uploadType = request.getParameter("uploadType");
			int type = StringUtils.isNumeric(uploadType) ? Integer.parseInt(uploadType) : FileService.DEFAULT;
			String isIndex = request.getParameter("isIndex");
			String filePath = null;
			// 判断是否是文件夹存储方式
			if (type == FileService.FOLDER) {
				// 获取文件夹
				String uploadFolder = request.getParameter("uploadFolder");
				filePath = getUploadPath(uploadFolder);
			}
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为"",说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						String fileName = file.getOriginalFilename();
						if (type == FileService.FOLDER) {
							fileName = filePath + fileName;
						}
						Serializable id = fileService.save(file.getInputStream(), file.getContentType(), fileName, type,
								"1".equals(isIndex));
						if (id != null) {
							list.add(id);
						}
					}
				}
			}
		}
		if (list.size() > 0) {
			rtn.put("flag", 1);
			rtn.put("msg", StringUtils.join(list, ","));
		} else {
			rtn.put("flag", 3);
			rtn.put("msg", "upload fail.");
		}
		response.getWriter().println(rtn.toJSONString());
		return null;
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/download")
	public String download(HttpServletRequest request, HttpServletResponse response, String fileId) throws IOException {
		boolean isSuccess = false;
		Attachment att = fileService.get(fileId);
		if (att != null) {
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition",
					"attachment; filename=" + ControllerHelper.processFileName(att.getFileName()));
			isSuccess = fileService.download(att, response.getOutputStream());
		}
		if (!isSuccess) {
			return "redirect:" + ControllerHelper.getBasePath() + "images/file_lose.png";
		}
		return null;
	}

	@RequestMapping(value = "/showPicture")
	public String showPicture(HttpServletRequest request, HttpServletResponse response, String id) throws IOException {
		// 设置缓存
		response.addHeader("Cache-Control", "max-age=86400");
		response.addHeader("Expires", new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toString());
		boolean isSuccess = fileService.download(id, response.getOutputStream());
		if (!isSuccess) {
			return "redirect:" + ControllerHelper.getBasePath() + "images/img_lose.png";
		}
		return null;
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
	@RequestMapping(value = "/getImage")
	public String getImage(String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 设置缓存
		response.addHeader("Cache-Control", "max-age=86400");
		response.addHeader("Expires", new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toString());
		boolean isSuccess = fileService.download(uuid, response.getOutputStream());
		if (!isSuccess) {
			return "redirect:" + ControllerHelper.getBasePath() + "images/img_lose.png";
		}
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
			Serializable id = fileService.save(new ByteArrayInputStream(bytes), "img.jpg", "img/jpg",
					FileService.MONGODB);
			// 返回访问图片的url到富文本框中，通过getImage方法访问图片
			resultObj.put("url", request.getContextPath() + "/common/file/getImage.do?uuid=" + id);
			return resultObj.toJSONString();
		} catch (Exception e) {
			return "";
		}
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

		response.setContentType("application/x-msdownload;");
		response.setHeader("Content-Disposition", "attachment;filename=download.zip");
		boolean isSuccess = fileService.batchDownload(fileIds, response.getOutputStream());
		if (!isSuccess) {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html;charset=UTF-8");
			out.println("文件不存在");
			out.close();
		}
		return null;
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
		Attachment att = fileService.get(id);
		InputStream in = fileService.getInputStream(att);
		JSONObject rtn = new JSONObject();
		if (in != null) {
			JSONObject o = transAttToJSON(att);
			rtn.put("result", "1");
			rtn.put("msg", o.toJSONString());
			rtn.put("status", 0);
			rtn.put("data", o.toJSONString());
			IOUtils.closeQuietly(in);
		} else {
			rtn.put("result", "3");
			rtn.put("status", 0);
			rtn.put("msg", "文件不存在");
		}
		return rtn.toJSONString();
	}

	private JSONObject transAttToJSON(Attachment att) {
		JSONObject o = new JSONObject();
		o.put("id", att.getId());
		o.put("length", att.getSize());
		String tmp = att.getFileName();
		tmp = tmp.substring(tmp.lastIndexOf("/") + 1);
		o.put("filename", tmp);
		return o;
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
		fileService.delete((Serializable[]) ids);
		rtn.put("result", "1");
		rtn.put("msg", "删除成功！");
		rtn.put("status", 0);
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
	public String remove(String[] ids, HttpServletRequest request, HttpServletResponse response) {
		JSONObject rtn = new JSONObject();
		fileService.delete(false, (Serializable[]) ids);
		rtn.put("result", "1");
		rtn.put("msg", "删除成功！");
		rtn.put("status", 0);
		return rtn.toJSONString();
	}

	@RequestMapping("/preview")
	public String preview(HttpServletResponse response, String fileId) throws Exception {
		Attachment att = Attachment.get(fileId);
		InputStream is = fileService.getInputStream(att);
		String msg = "";
		if (is != null) {
			String fileName = att.getFileName();
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
				} else if (ext.equals("jpg") || ext.equals("png") || ext.equals("bmp") || ext.equals("gif")) {
					// 图片,重定向到 showPicture.do?id=
					return "redirect:showPicture.do?id=" + fileId;
				} else if (ext.equals("pdf")) {
					// 直接在线预览pdf
					response.setContentType("application/pdf;charset=UTF-8");
					response.setHeader("Content-disposition", "inline; filename=" + fileName);
					ServletOutputStream out = response.getOutputStream();
					fileService.copy(is, out);
					out.flush();
					out.close();
					return null;
				} else if (ext.equals("html")) {
					// 直接在线预览pdf
					response.setContentType("text/html;charset=UTF-8");
					response.setHeader("Content-disposition", "inline; filename=" + fileName);
					ServletOutputStream out = response.getOutputStream();
					fileService.copy(is, out);
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
		Serializable uuid = null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,mp4,m4v,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		response.setContentType("text/html; charset=UTF-8");

		JSONObject obj = new JSONObject();

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		MultipartFile imgFile = multipartRequest.getFile("imgFile");
		String fileName = imgFile.getOriginalFilename();
		checkFile(request, extMap, obj, dirName, imgFile, fileName);
		if (obj.getInteger("error") == 1) {
			return obj.toJSONString();
		}
		try {
			uuid = fileService.save(imgFile.getInputStream(), imgFile.getContentType(), fileName, FileService.DEFAULT);
		} catch (FileNotFoundException e) {
			logger.info("ERROR", e);
			obj.put("error", 1);
			obj.put("message", "upload fail.");
			return obj.toJSONString();
		} catch (IOException e) {
			logger.info("ERROR", e);
			obj.put("error", 1);
			obj.put("message", "upload fail.");
			return obj.toJSONString();
		}
		obj.put("error", 0);
		// 返回访问图片的url到富文本框中，通过getImage方法访问图片
		obj.put("url", request.getContextPath() + "/common/file/getImage.do?uuid=" + uuid);
		return obj.toJSONString();

	}

	private void checkFile(HttpServletRequest request, HashMap<String, String> extMap, JSONObject obj, String dirName,
			MultipartFile imgFile, String fileName) {
		if (!ServletFileUpload.isMultipartContent(request)) {
			obj.put("error", 1);
			obj.put("message", "请选择文件！");
			obj.toJSONString();
			return;
		}
		if (!extMap.containsKey(dirName)) {
			obj.put("error", 1);
			obj.put("message", "目录名不正确！");
			obj.toJSONString();
			return;
		}
		// 获取原图片文件名字及文件扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		// 上传格式检查
		if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(fileExt)) {
			obj.put("error", 1);
			obj.put("message", "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
			obj.toJSONString();
			return;
		}
		if (imgFile.getSize() > 104857600) {
			obj.put("error", 1);
			obj.put("message", "上传文件过大！");
			obj.toJSONString();
			return;
		}
	}

	private String getUploadPath(String uploadFolder) {
		String filePath;
		Store store = Store.get(uploadFolder);
		String uploadPath = null;
		if (store != null && store.getContent() != null) {
			JSONObject content = JSON.parseObject(store.getContent());
			uploadPath = content.getString("filePath");
		}
		if (StringUtils.isBlank(uploadPath)) {
			uploadPath = ControllerHelper.getUploadPath();
		}
		uploadPath = uploadPath.replaceAll("\\\\", FileService.separator);
		if (!uploadPath.endsWith(FileService.separator)) {
			filePath = uploadPath + FileService.separator;
		} else {
			filePath = uploadPath;
		}
		return filePath;
	}
}
