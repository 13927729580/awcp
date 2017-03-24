package org.szcloud.framework.workflow.core.module;

import javax.swing.JFrame;

public class DrawFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 活动图窗口大小
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;
	
	public DrawFrame() {
		this.setSize(800, 600);
		this.setVisible(false);
	}

	public static void main(final String[] args) {
		DrawFrame frame = new DrawFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}