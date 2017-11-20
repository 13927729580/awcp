package cn.org.awcp.unit.vo;

import java.io.Serializable;
import java.util.List;

public class PunResourceTreeVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3300406158962173426L;
	private String name;
	private List<PunResourceTreeNode> punResourceTreeNode;
	
	public List<PunResourceTreeNode> getPunResourceTreeNode() {
		return punResourceTreeNode;
	}
	
	public void setPunResourceTreeNode(List<PunResourceTreeNode> punResourceTreeNode) {
		this.punResourceTreeNode = punResourceTreeNode;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
