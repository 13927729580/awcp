package cn.org.awcp.unit.vo;

import java.io.Serializable;

public class PunResourceVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7974454216355573998L;
	private Long resourceId;
	private Long sysId;
	private String resouType;
	private String relateResoId;
	private String whichEnd;
	private String resourceName;

	public PunResourceVO(){
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Long getSysId() {
		return sysId;
	}

	public void setSysId(Long sysId) {
		this.sysId = sysId;
	}

	public String getResouType() {
		return resouType;
	}

	public void setResouType(String resouType) {
		this.resouType = resouType;
	}

	public String getRelateResoId() {
		return relateResoId;
	}

	public void setRelateResoId(String relateResoId) {
		this.relateResoId = relateResoId;
	}

	public String getWhichEnd() {
		return whichEnd;
	}

	public void setWhichEnd(String whichEnd) {
		this.whichEnd = whichEnd;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	public String toString() {
		return "PunResourceVO [resourceId=" + resourceId + ", sysId=" + sysId + ", resouType=" + resouType
				+ ", relateResoId=" + relateResoId + ", whichEnd=" + whichEnd + ", resourceName=" + resourceName + "]";
	}
	
}

