package org.szcloud.framework.unit.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.szcloud.framework.core.domain.BaseEntity;
import java.util.*;
public class PunManageUserRoleVO{
	private Long userRoleId;
	private Long roleId;
	private Long userId;

	public PunManageUserRoleVO(){
	}

	public PunManageUserRoleVO(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public void setUserRoleId(Long value) {
		this.userRoleId = value;
	}
	
	public Long getUserRoleId() {
		return this.userRoleId;
	}
	public void setRoleId(Long value) {
		this.roleId = value;
	}
	
	public Long getRoleId() {
		return this.roleId;
	}
	public void setUserId(Long value) {
		this.userId = value;
	}
	
	public Long getUserId() {
		return this.userId;
	}

}

