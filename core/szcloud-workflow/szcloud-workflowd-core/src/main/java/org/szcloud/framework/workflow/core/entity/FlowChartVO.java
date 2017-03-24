package org.szcloud.framework.workflow.core.entity;

import java.io.Serializable;
import java.util.List;

public class FlowChartVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> x_coordinates_list;
	private List<String> y_coordinates_list;
	private List<FlowImageVO> flowImageList;
	
	public List<String> getX_coordinates_list() {
		return x_coordinates_list;
	}
	public void setX_coordinates_list(List<String> x_coordinates_list) {
		this.x_coordinates_list = x_coordinates_list;
	}
	public List<String> getY_coordinates_list() {
		return y_coordinates_list;
	}
	public void setY_coordinates_list(List<String> y_coordinates_list) {
		this.y_coordinates_list = y_coordinates_list;
	}
	public List<FlowImageVO> getFlowImageList() {
		return flowImageList;
	}
	public void setFlowImageList(List<FlowImageVO> flowImageList) {
		this.flowImageList = flowImageList;
	}

}
