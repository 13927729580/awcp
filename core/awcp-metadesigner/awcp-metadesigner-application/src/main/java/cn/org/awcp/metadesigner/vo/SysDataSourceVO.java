package cn.org.awcp.metadesigner.vo;

import java.io.Serializable;

public class SysDataSourceVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4995025544755632260L;
	/*
	 * ID
	 */
	private String id;
	/*
	 * 系统ID
	 */
	private Long systemId;
	/*
	 * 数据源ID
	 */
	private String dataSourceId;
	/*
	 * 是否默认 数据源
	 */
	private Boolean isDefault = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "SysDataSourceVO [id=" + id + ", systemId=" + systemId + ", dataSourceId=" + dataSourceId
				+ ", isDefault=" + isDefault + "]";
	}

}
