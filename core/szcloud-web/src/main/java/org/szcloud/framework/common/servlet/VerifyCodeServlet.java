package org.szcloud.framework.common.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.szcloud.framework.common.security.VerifyCodeGenerator;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;

/**
 * VerifyCodeServlet
 * 
 * @author wsh
 *
 */
public class VerifyCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VerifyCodeServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		VerifyCodeGenerator vg = VerifyCodeGenerator.getInstance();
		Map<String, BufferedImage> result = vg.printImage();
		String verifyCode = result.keySet().iterator().next();
		BufferedImage bi = result.get(verifyCode);
		// 将ContentType设为"image/jpeg"，让浏览器识别图像格式。
		response.setContentType("image/jpeg");
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 2000);
		request.getSession().setAttribute(SessionContants.VERIFY_CODE, verifyCode);
		SessionUtils.addObjectToSession(SessionContants.VERIFY_CODE, verifyCode);
		try {
			ServletOutputStream outStream = response.getOutputStream();
			ImageOutputStream imOut = ImageIO.createImageOutputStream(outStream);
			ImageIO.write(bi, "jpg", imOut);
			outStream.flush();
			// 关闭输出流
			outStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
