package cn.org.awcp.formdesigner.core.domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.org.awcp.core.domain.BaseEntity;

public class Attachment extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String storageId;

	private String fileName;

	private Long userId;

	private String userName;

	private String contentType;

	private Long systemId;

	private Long size;

	// 创建时间
	private Date createTime;
	// 更新时间
	private Date updateTime;

	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStorageId() {
		return storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public static Attachment getByStorage(String storage) {
		return getRepository().findOne(Attachment.class.getName() + ".getByStorage", storage);
	}

	@Override
	public BaseEntity<String> save() {
		return getRepository().save(this, this.getId());
	}

	public InputStream getInputStreamByFolder() {
		FileInputStream is = null;
		if (this != null && StringUtils.isNotBlank(this.getStorageId())) {
			try {
				is = new FileInputStream(this.getStorageId());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return is;
	}

}
