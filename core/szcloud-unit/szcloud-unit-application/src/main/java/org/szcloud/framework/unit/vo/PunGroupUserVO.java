package org.szcloud.framework.unit.vo;


public class PunGroupUserVO {
	private Long groupId;
//	private List<Long> userIds;
	private Long[] userIds;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
//	public List<Long> getUserIds() {
//		return userIds;
//	}
//	public void setUserIds(List<Long> userIds) {
//		this.userIds = userIds;
//	}
	public Long[] getUserIds() {
		return userIds;
	}
	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}
	
}
