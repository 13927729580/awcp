package cn.org.awcp.venson.util;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码操作工具类
 * 
 * @author Venson
 *
 */
public class QRCoreUtil {
	/*
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(QRCoreUtil.class);

	private QRCoreUtil() {
	}

	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = DEFAULT_WIDTH;

	public static void main(String[] args) {
		String path = "g:/test/qrcode_awcp.png";
		String content = "http://www.maxic.cn/awcp/login.html";
		try {
			create(content, new FileOutputStream(path));
			logger.debug(read(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建二维码
	 * 
	 * @param content
	 *            内容
	 * @param out
	 *            输出位置
	 */
	public static void create(String content, OutputStream out) {
		create(DEFAULT_WIDTH, DEFAULT_HEIGHT, content, out);
	}

	/**
	 * 创建二维码
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param content
	 *            内容
	 * @param out
	 *            输出位置
	 */
	public static void create(int width, int height, String content, OutputStream out) {
		Map<EncodeHintType, Object> hints = new HashMap<>(3);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 纠错等级L,M,Q,H
		hints.put(EncodeHintType.MARGIN, 2); // 边距
		BitMatrix matrix;
		try {
			matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			MatrixToImageWriter.writeToStream(matrix, "png", out);
		} catch (Exception e) {
			logger.debug("ERROR", e);
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 读取二维码内容
	 * 
	 * @param in
	 *            读取的文件路径
	 * @return 二维码内容信息
	 */
	public static String read(String path) {
		try {
			return read(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			logger.debug("ERROR", e);
		}
		return null;
	}

	/**
	 * 读取二维码内容
	 * 
	 * @param in
	 *            读取的文件
	 * @return 二维码内容信息
	 */
	public static String read(InputStream in) {
		BufferedImage image = null;
		try {
			MultiFormatReader read = new MultiFormatReader();
			image = ImageIO.read(in);
			Binarizer binarizer = new HybridBinarizer(new BufferedImageLuminanceSource(image));
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Result res = null;
			res = read.decode(binaryBitmap);
			return res.getText();
		} catch (Exception e) {
			logger.debug("ERROR", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return null;
	}
}
