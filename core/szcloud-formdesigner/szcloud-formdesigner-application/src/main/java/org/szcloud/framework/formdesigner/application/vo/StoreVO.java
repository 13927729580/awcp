package org.szcloud.framework.formdesigner.application.vo;

import java.io.Serializable;
import java.util.Date;

public class StoreVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4627045472998143241L;
	/**
	 * 
	 */
	
	private String id;
	private String code;
	private String name;
	private String content;
	private String description;
	private Long dynamicPageId;
	private String dynamicPageName;
	private Integer buttonGroup;
	private Integer order;
	private Long systemId;
	//用于数据回显不存放数据库
	private String scope;

	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	private Integer isCheckOut;
	private String checkOutUser;
	private String createdUser;
	
	private String updatedUser;
	
	/**
	 * 创建时间
	 */
	private Date created;
	/**
	 * 更改时间
	 */
	private Date updated;
	
	public Long getSystemId() {
		return systemId;
	}
	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
	public Integer getButtonGroup() {
		return buttonGroup;
	}
	public void setButtonGroup(Integer buttonGroup) {
		this.buttonGroup = buttonGroup;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getDynamicPageName() {
		return dynamicPageName;
	}
	public void setDynamicPageName(String dynamicPageName) {
		this.dynamicPageName = dynamicPageName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getDynamicPageId() {
		return dynamicPageId;
	}
	public void setDynamicPageId(Long dynamicPageId) {
		this.dynamicPageId = dynamicPageId;
	}
	public Integer getIsCheckOut() {
		return isCheckOut;
	}
	public void setIsCheckOut(Integer isCheckOut) {
		this.isCheckOut = isCheckOut;
	}
	public String getCheckOutUser() {
		return checkOutUser;
	}
	public void setCheckOutUser(String checkOutUser) {
		this.checkOutUser = checkOutUser;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public String getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	
	
}

