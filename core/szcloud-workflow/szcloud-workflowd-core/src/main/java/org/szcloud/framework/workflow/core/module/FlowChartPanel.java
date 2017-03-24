package org.szcloud.framework.workflow.core.module;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.szcloud.framework.workflow.core.entity.CWorkItem;
import org.szcloud.framework.workflow.core.entity.FlowChartVO;
import org.szcloud.framework.workflow.core.entity.FlowImageVO;

/**
 * 画带箭头的线及图片
 * 
 * @author think
 *
 */
public class FlowChartPanel {

	private List<String> x_coordinates_list;
	private List<String> y_coordinates_list;
	private List<FlowImageVO> flowImageList;

	public FlowChartPanel(CWorkItem workItem) {
		FlowChartVO flowchart = FlowChartUtils.getFlowParameters(workItem);
		this.x_coordinates_list = flowchart.getX_coordinates_list();
		this.y_coordinates_list = flowchart.getY_coordinates_list();
		this.flowImageList = flowchart.getFlowImageList();
	}

	public void paintFlowChartComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (FlowImageVO vo : flowImageList) {
			drawImage(vo, g2);
		}
		g2.setPaint(Color.black);
		for (int j = 0; j < x_coordinates_list.size(); j++) {
			String[] x_arr_coordinate = x_coordinates_list.get(j).split(";");
			String[] y_arr_coordinate = y_coordinates_list.get(j).split(";");
			for (int i = 0; i < x_arr_coordinate.length - 1; i++) {
				if (i == x_arr_coordinate.length - 2) {
					drawAL(Integer.parseInt(x_arr_coordinate[i]), Integer.parseInt(y_arr_coordinate[i]),
							Integer.parseInt(x_arr_coordinate[i + 1]), Integer.parseInt(y_arr_coordinate[i + 1]), g2);
				} else {
					drawLine(Integer.parseInt(x_arr_coordinate[i]), Integer.parseInt(y_arr_coordinate[i]),
							Integer.parseInt(x_arr_coordinate[i + 1]), Integer.parseInt(y_arr_coordinate[i + 1]), g2);
				}
			}

		}
	}

	public void drawImage(FlowImageVO vo, Graphics2D g) {
		g.setColor(Color.WHITE);
		String str = this.getClass().getResource("/").toString() + vo.getImgUrl();
		String imageUrl = "/" + str.replace("file:/", "");
		Image image;
		try {
			image = ImageIO.read(new File(imageUrl));// Toolkit.getDefaultToolkit().getImage(imageUrl);
			g.drawImage(image, vo.getX(), vo.getY(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String waterMarkContent = vo.getActivityName();

		g.setFont(new Font("宋体", Font.BOLD, 12)); // 字体、字型、字号
		g.setColor(Color.BLUE);
		if (vo.isCurActivity()) {
			g.setColor(Color.RED);
		}
		g.drawString(waterMarkContent, vo.getX() + 10, vo.getY() + 60); // 画文字
	}

	public static void drawLine(int sx, int sy, int ex, int ey, Graphics2D g2) {
		float lineWidth = 1.5f;
		g2.setStroke(new BasicStroke(lineWidth));
		if (sx < ex && sy < ey && (Math.abs(ex - sx) > 50) && (Math.abs(sy - ey) > 50)) {// 起点都小于终点
			sx = sx + 25;
			sy = sy + 25;
			ex = ex - 12;
		} else if (sx > ex && sy > ey && (Math.abs(ex - sx) > 50) && (Math.abs(sy - ey) > 50)) {// 起点都大于终点
			sx = sx - 6;
			sy = sy - 6;
			ex = ex + 25;
			ey = ey + 25;
		} else if (sx < ex && sy > ey && (Math.abs(ex - sx) > 50) && (Math.abs(sy - ey) > 50)) {// 往右上角
			sx = sx + 25;
			ex = ex - 6;
			ey = ey + 25;
		} else if (sx > ex && sy < ey && (Math.abs(ex - sx) > 50) && (Math.abs(sy - ey) > 50)) {// 往左下角
			sx = sx - 6;
			sy = sy + 25;
			ex = ex + 25;
			ey = ey + 6;
		} else if (sx < ex && (sy == ey || Math.abs(sy - ey) <= 50)) {// 往右
			sx = sx + 25;
			sy = sy + 12;
			ex = ex - 12;
			ey = ey + 12;
		} else if (sx > ex && (sy == ey || Math.abs(sy - ey) <= 50)) {// 往左
			sx = sx - 12;
			sy = sy + 12;
			ex = ex + 25;
			ey = ey + 12;
		} else if ((sx == ex || Math.abs(sx - ex) <= 50) && sy > ey) {// 往上
			sx = sx + 12;
			sy = sy - 12;
			ex = ex + 12;
			ey = ey + 25;
		} else if ((sx == ex || Math.abs(sx - ex) <= 50) && sy < ey) {// 往下
			sx = sx + 12;
			sy = sy + 25;
			ex = ex + 12;
			ey = ey - 12;
		}
		g2.drawLine(sx, sy, ex, ey);
	}

	public static void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2) {
		((Graphics2D) g2).setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		((Graphics2D) g2).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (sx < ex && sy < ey && (Math.abs(ex - sx) > 50) && (Math.abs(sy - ey) > 50)) {// 起点都小于终点
			sx = sx + 25;
			sy = sy + 25;
			ex = ex - 12;
		} else if (sx > ex && sy > ey && (Math.abs(ex - sx) > 50) && (Math.abs(sy - ey) > 50)) {// 起点都大于终点
			sx = sx - 6;
			sy = sy - 6;
			ex = ex + 25;
			ey = ey + 25;
		} else if (sx < ex && sy > ey && (Math.abs(ex - sx) > 50) && (Math.abs(sy - ey) > 50)) {// 往右上角
			sx = sx + 25;
			ex = ex - 6;
			ey = ey + 25;
		} else if (sx > ex && sy < ey && (Math.abs(ex - sx) > 50) && (Math.abs(sy - ey) > 50)) {// 往左下角
			sx = sx - 6;
			sy = sy + 25;
			ex = ex + 25;
			ey = ey + 6;
		} else if (sx < ex && (sy == ey || Math.abs(sy - ey) <= 50)) {// 往右
			sx = sx + 25;
			sy = sy + 12;
			ex = ex - 12;
			ey = ey + 12;
		} else if (sx > ex && (sy == ey || Math.abs(sy - ey) <= 50)) {// 往左
			sx = sx - 12;
			sy = sy + 12;
			ex = ex + 25;
			ey = ey + 12;
		} else if ((sx == ex || Math.abs(sx - ex) <= 50) && sy > ey) {// 往上
			sx = sx + 12;
			sy = sy - 12;
			ex = ex + 12;
			ey = ey + 25;
		} else if ((sx == ex || Math.abs(sx - ex) <= 50) && sy < ey) {// 往下
			sx = sx + 12;
			sy = sy + 25;
			ex = ex + 12;
			ey = ey - 12;
		}

		float lineWidth = 1.5f;
		g2.setStroke(new BasicStroke(lineWidth));

		double H = 10; // 箭头高度
		double L = 4; // 底边的一半
		int x3 = 0;
		int y3 = 0;
		int x4 = 0;
		int y4 = 0;
		double awrad = Math.atan(L / H); // 箭头角度
		double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
		double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
		double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
		double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
		double y_3 = ey - arrXY_1[1];
		double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
		double y_4 = ey - arrXY_2[1];

		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();
		// 画线
		g2.drawLine(sx, sy, ex, ey);
		//
		GeneralPath triangle = new GeneralPath();
		triangle.moveTo(ex, ey);

		triangle.lineTo(x3, y3);
		triangle.lineTo(x4, y4);
		triangle.closePath();
		// 实心箭头
		g2.fill(triangle);
		// 非实心箭头
		// g2.draw(triangle);

	}

	// 计算
	public static double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}

}