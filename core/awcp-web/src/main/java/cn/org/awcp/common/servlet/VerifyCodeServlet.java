package cn.org.awcp.common.servlet;

import cn.org.awcp.common.security.VerifyCodeGenerator;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * VerifyCodeServlet
 * 
 * @author wsh
 *
 */
public class VerifyCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

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
	@Override
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
		SessionUtils.addObjectToSession(SessionContants.VERIFY_CODE, verifyCode);
		try {
			ServletOutputStream outStream = response.getOutputStream();
			ImageOutputStream imOut = ImageIO.createImageOutputStream(outStream);
			ImageIO.write(bi, "jpg", imOut);
			outStream.flush();
			// 关闭输出流
			outStream.close();
		} catch (IOException ex) {
			logger.info("ERROR", ex);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
