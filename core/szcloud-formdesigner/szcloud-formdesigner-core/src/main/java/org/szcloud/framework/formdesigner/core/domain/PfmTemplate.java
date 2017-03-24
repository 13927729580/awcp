package org.szcloud.framework.formdesigner.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.szcloud.framework.core.domain.BaseEntity;
import java.util.*;

public class PfmTemplate extends BaseEntity{
	private Long id;
	private String fileName;
	private Long sysId;
	private String description;
	private String fileLocation;
	private String content;
	public PfmTemplate(){
	}

	public PfmTemplate(Long id){
		this.id = id;
	}

	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
	public void setFileName(String value) {
		this.fileName = value;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	public void setSysId(Long value) {
		this.sysId = value;
	}
	
	public Long getSysId() {
		return this.sysId;
	}
	public void setDescription(String value) {
		this.description = value;
	}
	
	public String getDescription() {
		return this.description;
	}
	public void setFileLocation(String value) {
		this.fileLocation = value;
	}
	
	public String getFileLocation() {
		return this.fileLocation;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}

