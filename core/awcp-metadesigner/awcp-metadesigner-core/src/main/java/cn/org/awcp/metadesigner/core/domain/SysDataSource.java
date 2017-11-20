package cn.org.awcp.metadesigner.core.domain;

import cn.org.awcp.core.domain.BaseEntity;

public class SysDataSource extends BaseEntity<String> {

	private static final long serialVersionUID = 1636699772682203735L;
	/*
	 * 系统ID
	 */
	private Long systemId;
	/*
	 * 数据源ID
	 */
	private String dataSourceId;
	/*
	 * 是否默认数据源
	 */
	private Boolean isDefault = false;

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

}
