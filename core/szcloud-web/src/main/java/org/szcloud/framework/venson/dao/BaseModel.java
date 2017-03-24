package org.szcloud.framework.venson.dao;

import java.util.Date;

/**
 * 数据表基类 : 该类包含了数据表的公有属性和方法
 * 
 * @author venson
 * @version 2016-08-18
 */
public abstract class BaseModel {

	// 主键 id
	protected Integer id;

	// 修改人 modifier
	protected Integer modifier;

	// 修改时间 modify_time
	protected Date modifyTime;

	// 创建人 creator
	protected Integer creator;

	// 创建时间 create_time
	protected Date createTime;

	// 备注 remark
	protected String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getModifier() {
		return modifier;
	}

	public void setModifier(Integer modifier) {
		this.modifier = modifier;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void preSave() {
		this.createTime = new Date();
		this.creator = 1;
		this.modifier = 1;
		this.modifyTime = this.createTime;
	}

	public void preUpdate() {
		this.modifier = 1;
		this.modifyTime = new Date();
	}

}
