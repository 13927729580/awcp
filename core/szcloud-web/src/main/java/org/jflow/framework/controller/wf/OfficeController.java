package org.jflow.framework.controller.wf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/WF")
public class OfficeController {

	@RequestMapping(value = "/FilesUpload", method = RequestMethod.POST)
	public void filesUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileStart = request.getHeader("Start");
		String path = request.getHeader("Path");
		String fileExtension = request.getHeader("Extension");
		String type = request.getHeader("Type");
		// logger.debug(fileStart);
		// logger.debug(path);
		// logger.debug(fileExtension);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			InputStream is = file.getInputStream();
			// word文件路径
			String fileNameR = path + File.separator + fileStart + fileExtension;
			// pdf文件路径
			String savePath = path + File.separator + fileStart + ".pdf";
			// OfficeToPDF.file_put_contents(is, fileNameR, savePath);
		}
	}
}
