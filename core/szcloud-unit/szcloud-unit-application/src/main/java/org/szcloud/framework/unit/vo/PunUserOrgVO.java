package org.szcloud.framework.unit.vo;

import org.szcloud.framework.core.domain.BaseEntity;

public class PunUserOrgVO extends BaseEntity{
	
	
	private Long userId;
	private Long orgId;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
