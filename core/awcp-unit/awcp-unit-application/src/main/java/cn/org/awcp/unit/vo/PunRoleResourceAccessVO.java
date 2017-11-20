package cn.org.awcp.unit.vo;

import java.io.Serializable;

public class PunRoleResourceAccessVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5752018171990870180L;
	private Long roleId;
	private Long operType;
	private Long resourceId;
	private Long resourceType;
	private String resourceName;
	private Long sysId;
	
	public Long getRoleId() {
		return roleId;
	}
	
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public Long getOperType() {
		return operType;
	}
	
	public void setOperType(Long operType) {
		this.operType = operType;
	}
	
	public Long getResourceType() {
		return resourceType;
	}
	
	public void setResourceType(Long resourceType) {
		this.resourceType = resourceType;
	}
	
	public String getResourceName() {
		return resourceName;
	}
	
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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

	@Override
	public String toString() {
		return "PunRoleResourceAccessVO [roleId=" + roleId + ", operType=" + operType + ", resourceId=" + resourceId
				+ ", resourceType=" + resourceType + ", resourceName=" + resourceName + ", sysId=" + sysId + "]";
	}
	
}
