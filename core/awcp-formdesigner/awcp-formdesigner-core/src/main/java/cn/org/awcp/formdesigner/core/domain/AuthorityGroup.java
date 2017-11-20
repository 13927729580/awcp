package cn.org.awcp.formdesigner.core.domain;

import java.util.Date;

import cn.org.awcp.core.domain.BaseEntity;

public class AuthorityGroup extends BaseEntity<String> {

	private static final long serialVersionUID = -4341550141207809604L;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 所属页面ID
	 */
	private String dynamicPageId;
	/**
	 * 所属系统Id
	 */
	private Long systemId;
	/**
	 * 创建者
	 */
	private Long creater;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最后更新人
	 */
	private Long lastupdater;
	/**
	 * 最后更新时间
	 */
	private Date lastupdateTime;
	/**
	 * 排序字段
	 */
	private String order;
	/**
	 * 描述
	 */
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDynamicPageId() {
		return dynamicPageId;
	}

	public void setDynamicPageId(String dynamicPageId) {
		this.dynamicPageId = dynamicPageId;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getLastupdater() {
		return lastupdater;
	}

	public void setLastupdater(Long lastupdater) {
		this.lastupdater = lastupdater;
	}

	public Date getLastupdateTime() {
		return lastupdateTime;
	}

	public void setLastupdateTime(Date lastupdateTime) {
		this.lastupdateTime = lastupdateTime;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
