package org.szcloud.framework.formdesigner.core.domain.design.context.component.text;

import org.szcloud.framework.formdesigner.core.domain.design.context.component.SimpleComponent;
import org.szcloud.framework.formdesigner.core.domain.design.context.component.TextFormat;

public abstract class TextComponent extends SimpleComponent{
	protected String value;
	protected TextFormat format;
	
	public String getValue(){
		return this.value;
	}
	public void setValue(String value){
		this.value = value;
	}
	
	public TextFormat getFormat(){
		return this.format;
	}
	
	public void setFormat(TextFormat format){
		this.format = format;
	}
}
