package cn.org.awcp.formdesigner.application.vo;

import java.util.List;

public class WorkflowVO {
	
	private String pageId;
	
	private List<WorkflowNodeVO> nodes;//结点
	
	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	public List<WorkflowNodeVO> getNodes() {
		return nodes;
	}

	public void setNodes(List<WorkflowNodeVO> nodes) {
		this.nodes = nodes;
	}
	
}
