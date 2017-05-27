package org.szcloud.framework.common.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;

/**
 * VerifyCodeGenerator
 * 
 * @author wsh
 *
 */
public class VerifyCodeGenerator {
	private static final VerifyCodeGenerator generator = new VerifyCodeGenerator();

	private final String ATTRIBUTE_NAME = SessionContants.VERIFY_CODE;
	// 图片的宽度
	private final int WIDTH = 15;
	// 图片的高度
	private final int HEIGHT = 46;
	// 字符串长度
	private final int CODE_LENGTH = 5;
	// 随机字符串范围
	private final String RAND_RANGE = "abcdefghijkmnpqrstuvwxyz23456789";

	private final char[] CHARS = RAND_RANGE.toCharArray();

	private Random random = new Random();

	private VerifyCodeGenerator() {
		//
	}

	public static VerifyCodeGenerator getInstance() {
		return generator;
	}

	/**
	 * 生成随机字符串
	 * 
	 * @return 随机字符串
	 */
	public String getRandString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < CODE_LENGTH; i++)
			sb.append(CHARS[random.nextInt(CHARS.length)]);
		return sb.toString();
	}

	/**
	 * 生成随机颜色
	 * 
	 * @param ll
	 *            产生颜色值下限(lower limit)
	 * @param ul
	 *            产生颜色值上限(upper limit)
	 * @return 生成的随机颜色对象
	 */
	private Color getRandColor(int ll, int ul) {
		if (ll > 255)
			ll = 255;
		if (ll < 1)
			ll = 1;
		if (ul > 255)
			ul = 255;
		if (ul < 1)
			ul = 1;
		if (ul == ll)
			ul = ll + 1;
		int r = random.nextInt(ul - ll) + ll;
		int g = random.nextInt(ul - ll) + ll;
		int b = random.nextInt(ul - ll) + ll;
		// rgb固定值
		r = 73;
		g = 237;
		b = 147;
		Color color = new Color(r, g, b);
		return color;
	}

	/**
	 * 生成指定字符串的图像数据
	 * 
	 * @param verifyCode
	 *            即将被打印的随机字符串
	 * @return 生成的图像数据
	 */
	private BufferedImage getImage(String verifyCode) {

		BufferedImage image = new BufferedImage(WIDTH * (CODE_LENGTH + 1), HEIGHT, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics graphics = image.getGraphics();

		// 画背景
		createBackground(graphics, WIDTH * 8, HEIGHT);
		// 画字符串
		drawCharacter(graphics, verifyCode);

		// 图像生效
		graphics.dispose();

		return image;
	}

	/**
	 * 将验证码的图像输出
	 * 
	 * @param request
	 *            用户的请求对象
	 * @param response
	 *            用户的响应对象
	 */
	public Map<String, BufferedImage> printImage() {
		// 获得随机验证码
		String verifyCode = this.getRandString();
		// 获得验证码的图像数据
		BufferedImage bi = this.getImage(verifyCode);
		Map<String, BufferedImage> result = new HashMap<String, BufferedImage>();
		result.put(verifyCode, bi);
		return result;
	}

	/**
	 * 检查输入的验证码是否正确，若用户输入的验证码与生成的验证码相符则返回true，否则返回false。
	 * 
	 * @param request
	 *            用户的请求对象
	 * @return 验证结果
	 */
	public boolean check(HttpServletRequest request) {
		if (((String) request.getParameter(ATTRIBUTE_NAME))
				.equalsIgnoreCase((String) request.getSession().getAttribute(ATTRIBUTE_NAME))) {
			request.getSession().removeAttribute(ATTRIBUTE_NAME);
			return true;
		}
		return false;
	}

	public boolean check(String verifyCode, HttpSession session) {
		if (StringUtils.isEmpty(verifyCode))
			return false;
		if (verifyCode.equalsIgnoreCase((String) session.getAttribute(ATTRIBUTE_NAME))) {
			session.removeAttribute(ATTRIBUTE_NAME);
			return true;
		}
		return false;
	}

	private void createBackground(Graphics g, int width, int height) {
		// 填充背景
		g.setColor(getRandColor(220, 250));
		g.fillRect(0, 0, width, height);
		// 加入干扰线条
		for (int i = 0; i < 10; i++) {
			g.setColor(getRandColor(40, 150));
			Random random = new Random();
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			g.drawLine(x, y, x1, y1);
		}
	}

	private void drawCharacter(Graphics g, String verifyCode) {

		String[] fontTypes = { "\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66" };

		char verifyCodes[] = verifyCode.toCharArray();
		for (int i = 0; i < verifyCodes.length; i++) {
			g.setColor(new Color(50 + random.nextInt(100), 50 + random.nextInt(100), 50 + random.nextInt(100)));
			g.setFont(new Font(fontTypes[random.nextInt(fontTypes.length)], Font.BOLD, 26));
			g.drawString(verifyCodes[i] + "", 15 * i + 5, 23 + random.nextInt(8));
			// g.drawString(r, i*w/4, h-5);
		}
	}
}
