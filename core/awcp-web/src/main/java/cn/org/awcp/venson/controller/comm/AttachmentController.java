package cn.org.awcp.venson.controller.comm;

import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.util.DateFormaterUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.*;

@RequestMapping("/attachment")
@Controller("attachment")
@ResponseBody
public class AttachmentController {
	private static final Log logger = LogFactory.getLog(AttachmentController.class);
	private static final Set<String> IMAGE_TYPE = new HashSet<String>();
	private static final Set<String> EXCLE_TYPE = new HashSet<String>();
	private static final Set<String> WORD_TYPE = new HashSet<String>();
	static {
		IMAGE_TYPE.add(ControllerHelper.CONTENT_TYPE_IMAGE_JPG);
		IMAGE_TYPE.add(ControllerHelper.CONTENT_TYPE_IMAGE_PNG);
		EXCLE_TYPE.add(ControllerHelper.CONTENT_TYPE_EXCEL);
		EXCLE_TYPE.add(ControllerHelper.CONTENT_TYPE_EXCEL_93);
		EXCLE_TYPE.add(ControllerHelper.CONTENT_TYPE_EXCEL_97);
		WORD_TYPE.add(ControllerHelper.CONTENT_TYPE_WORD_93);
		WORD_TYPE.add(ControllerHelper.CONTENT_TYPE_WORD_97);
	}

	@RequestMapping(value = "editorUpload")

	public void uploadUEditor(@RequestParam(required = false) CommonsMultipartFile attachment) throws IOException {
		Map<String, String> result = new HashMap<>(3);
		if (attachment != null) {
			Date now = new Date();
			String today = DateFormaterUtil.dateToString(DateFormaterUtil.FORMART2, now);
			String rootPath = ControllerHelper.getUploadPath("/upload/news/content");
			String preSavePath = rootPath + File.separator + today + File.separator;
			String imgPreHttp = "upload/news/content/" + today + "/";
			String fileName = java.util.UUID.randomUUID().toString();
			buildDir(preSavePath);
			String originName = attachment.getOriginalFilename();
			String fileExtension = originName.substring(originName.lastIndexOf("."));
			InputStream inputStream = attachment.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(preSavePath + fileName + fileExtension);
			IOUtils.copy(inputStream, outputStream);
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
			result.put("url", imgPreHttp + fileName + fileExtension);
			result.put("original", originName);
		}
		result.put("state", "SUCCESS");
		ControllerHelper.renderJSON(ControllerHelper.CONTENT_TYPE_HTML, result);
	}

	@RequestMapping(value = "uploadAttachment")

	public void uploadAttachment(@RequestParam(required = false) CommonsMultipartFile[] attachment) throws IOException {
		processUpload(attachment, true, ControllerHelper.ATTACHMENT_UPLOAD_PATH, null);
	}

	@RequestMapping(value = "uploadImg")

	public void uploadImg(@RequestParam(required = false) CommonsMultipartFile[] attachment) throws IOException {
		processUpload(attachment, false, ControllerHelper.IMAGE_UPLOAD_PATH, IMAGE_TYPE);
	}

	@RequestMapping(value = "excelUpload")

	public void uploadExcel(@RequestParam(required = false) CommonsMultipartFile[] attachment) throws IOException {
		processUpload(attachment, true, ControllerHelper.EXCEL_UPLOAD_PATH, EXCLE_TYPE);
	}

	@RequestMapping(value = "wordUpload")

	public void uploadWord(@RequestParam(required = false) CommonsMultipartFile[] attachment) throws IOException {
		processUpload(attachment, true, ControllerHelper.WORD_UPLOAD_PATH, WORD_TYPE);
	}

	private void buildDir(String path) {
		File file = new File(path.toString());
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	@RequestMapping("attachmentDownload")
	public void downloadAttachment(String fileName, @RequestParam(required = false) String downName) throws Exception {
		String path = ControllerHelper.getUploadPath(ControllerHelper.ATTACHMENT_ROOT_PATH) + fileName;
		processDownload(ControllerHelper.CONTENT_TYPE_STREAM, fileName, downName, path);

	}

	@RequestMapping("wordDownload")
	public void downloadWord(String fileName, @RequestParam(required = false) String downName) throws Exception {
		String path = ControllerHelper.getUploadPath(ControllerHelper.WORD_UPLOAD_PATH) + fileName;
		processDownload(ControllerHelper.CONTENT_TYPE_STREAM, fileName, downName, path);

	}

	@RequestMapping(value = "excelDownload")
	public void downloadExcel(String fileName, @RequestParam(required = false) String downName) throws Exception {
		String path = ControllerHelper.getDownloadPath() + fileName;
		processDownload(ControllerHelper.CONTENT_TYPE_EXCEL_93, fileName, downName, path);
	}

	private String processFile(String path, List<CommonsMultipartFile> attachments, String relativePath)
			throws FileNotFoundException, IOException {
		return list2String(processFile1(path, attachments, relativePath));
	}

	private void processUpload(CommonsMultipartFile[] attachment, boolean isString, String uploadPath,
			Set<String> validType) throws FileNotFoundException, IOException {
		ReturnResult returnResult = ReturnResult.get();
		// 判断是否存在文件
		if (attachment == null || attachment[0].isEmpty()) {
			logger.debug("未找到有效文件");
			returnResult.setStatus(StatusCode.SUCCESS.setMessage("file must not be null"));
		} else {
			// 校验文件类型，大小
			List<CommonsMultipartFile> files = validateUpload(attachment, validType);
			if (files != null) {
				Date now = new Date();
				String today = DateFormaterUtil.dateToString(DateFormaterUtil.FORMART2, now);
				// 获取上传路径
				String rootPath = ControllerHelper.getUploadPath(uploadPath);
				String SavePath = rootPath + File.separator + today + File.separator;
				String relativePath = uploadPath.substring(1) + "/" + today + "/";
				// 如果路径不存在则创建
				buildDir(SavePath);
				Object uploadFileName = null;
				// isString如果为true则返回字符串格式的数据，否则返回List<Map<String,String>>数据
				if (isString) {
					uploadFileName = processFile(SavePath.toString(), files, relativePath);
				} else {
					uploadFileName = processFile1(SavePath.toString(), files, relativePath);
				}
				returnResult.setStatus(StatusCode.SUCCESS).setData(uploadFileName);
				logger.debug("uploadFileName:" + uploadFileName);
			}
		}
		ControllerHelper.renderJSON(ControllerHelper.CONTENT_TYPE_HTML);
	}

	private void processDownload(String contentType, String fileName, String downName, String path)
			throws UnsupportedEncodingException, IOException {
		downName = validDownload(path, fileName, downName);
		if (downName != null) {
			ControllerHelper.makeAttachment(contentType,
					new String[] { ControllerHelper.CONTENT_DISPOSITION_ATTACHMENT, downName }, path);
		} else {
			ControllerHelper.renderJSON(ControllerHelper.CONTENT_TYPE_HTML);
		}
	}

	@RequestMapping("deleteAttachment")

	public void deleteAttachment(String fileName) throws Exception {
		String path = ControllerHelper.getUploadPath(fileName);
		logger.debug("deleteAttachment:" + path);
		new File(path.substring(0, path.length() - 1)).delete();
	}

	private String list2String(List<Map<String, String>> data) {
		StringBuffer buffer = new StringBuffer();
		for (Map<String, String> map : data) {
			buffer.append("&builderNames=");
			buffer.append(map.get("builderNames"));
			buffer.append("&fileNames=");
			buffer.append(map.get("fileNames"));
			buffer.append("&fileTypes=");
			buffer.append(map.get("fileTypes"));
			buffer.append("&fileSizes=");
			buffer.append(map.get("fileSizes"));
			buffer.append("&url=");
			buffer.append(map.get("url"));
		}
		return buffer.toString();
	}

	private List<Map<String, String>> processFile1(String path, List<CommonsMultipartFile> attachments,
			String relativePath) throws FileNotFoundException, IOException {
		StringBuilder destinationPathBuilder = new StringBuilder();
		String fileExtension = null;
		String fileName = null;
		List<Map<String, String>> list = new LinkedList<Map<String, String>>();
		for (CommonsMultipartFile attachment : attachments) {
			// 获取文件后缀
			String originName = attachment.getOriginalFilename();
			fileExtension = originName.substring(originName.lastIndexOf("."));
			// 获取文件名
			fileName = ControllerHelper.makeFileName(fileExtension);
			// 防止多个文件上传路径重复
			destinationPathBuilder.delete(0, destinationPathBuilder.length());
			// 生成保存路径
			destinationPathBuilder.append(path);
			destinationPathBuilder.append(File.separator);
			destinationPathBuilder.append(fileName);
			// 保存文件
			InputStream inputStream = attachment.getInputStream();
			FileOutputStream outputStream = new FileOutputStream(destinationPathBuilder.toString());
			IOUtils.copy(inputStream, outputStream);
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
			// 返回结果
			Map<String, String> map = new HashMap<>(5);
			map.put("builderNames", fileName);
			map.put("url", relativePath + fileName);
			map.put("fileNames", originName);
			map.put("fileTypes", attachment.getContentType());
			map.put("fileSizes", String.valueOf(attachment.getSize()));
			list.add(map);
		}
		return list;
	}

	private String validDownload(String path, String fileName, String downName) throws UnsupportedEncodingException {
		ReturnResult returnResult = ReturnResult.get();
		logger.debug(path + ",fileName:" + fileName + ",downName:" + downName);
		if (StringUtils.isBlank(fileName)) {
			returnResult.setStatus(StatusCode.PARAMETER_ERROR.setMessage("file name must not be null"));
			return null;
		}
		if (!new File(path).exists()) {
			returnResult.setStatus(
					StatusCode.FAIL.setMessage(ControllerHelper.getMessage("messages.error.file.not.exist")));
			return null;
		}
		if (StringUtils.isBlank(downName)) {
			String ext = path.substring(path.lastIndexOf("."));
			downName = "download" + ext;
		}
		return downName;
	}

	private List<CommonsMultipartFile> validateUpload(CommonsMultipartFile[] attachment, Set<String> allowType,
			Long allowSize) {
		ReturnResult returnResult = ReturnResult.get();
		List<CommonsMultipartFile> files = new LinkedList<CommonsMultipartFile>();
		// 文件保存校验
		for (CommonsMultipartFile file : attachment) {
			logger.debug("文件类型：" + file.getContentType());
			// 检查上传文件类型
			if (allowType != null) {
				if (!allowType.contains(file.getContentType())) {
					returnResult.setStatus(StatusCode.FAIL
							.setMessage(ControllerHelper.getMessage("messages.error.content.type.not.allowed")));
					return null;
				}
				// 检查文件上传大小
			} else if (allowSize != null) {
				if (allowSize < file.getSize()) {
					returnResult.setStatus(
							StatusCode.FAIL.setMessage(ControllerHelper.getMessage("messages.error.file.too.large")));
					return null;
				}
			}
			if (file.getSize() > 0) {
				files.add(file);
			}
		}
		logger.debug("size:" + files.size());
		return files;
	}

	private List<CommonsMultipartFile> validateUpload(CommonsMultipartFile[] attachment, Set<String> allowType) {
		return validateUpload(attachment, allowType, null);
	}

}
