package org.szcloud.framework.workflow.core.entity;


public class FlowImageVO {

	private String imgUrl;
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean isCurActivity;
	private String activityName;
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isCurActivity() {
		return isCurActivity;
	}
	public void setCurActivity(boolean isCurActivity) {
		this.isCurActivity = isCurActivity;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
}
