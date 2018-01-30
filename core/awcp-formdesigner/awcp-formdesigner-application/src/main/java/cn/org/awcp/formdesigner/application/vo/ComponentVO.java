package cn.org.awcp.formdesigner.application.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public class ComponentVO {
		
	private Integer componentType = Integer.MIN_VALUE;	//组件类型
	private String dynamicPageId;	// 所属页面
	private String tittle;		//组件的额外信息
	private String accessKey;	//激活元素的快捷键
	private String tabIndex;	//组件的tab键次序
	private String lang;		//使用的语言--使用语言代码
	private String dir;			//文字方向
	private String dataItemCode;//文字方向
	private String valueType;	//值类型
	private String formatString;//值格式
	private String defaultValueScript;	//默认值的值脚本
	private String formValidator;	//校验
	private String optionScript;	//多选控件的text选项；

	public static ComponentVO parseFromJson(String jsonStr){
		ComponentVO result = new ComponentVO();
		JSONObject o = JSON.parseObject(jsonStr);
		String componentType = o.getString("componentType");

		if(StringUtils.isNumeric(componentType)){
			result.setComponentType(Integer.parseInt(componentType));
		}

		result.setDynamicPageId(o.getString("dynamicPageId"));
		result.setDefaultValueScript(o.getString("defaultValueScript"));
		result.setTittle(o.getString("title"));
		result.setDataItemCode(o.getString("readonlyScript"));
		result.setDefaultValueScript(o.getString("defaultValueScript"));
		result.setDataItemCode(o.getString("dataItemCode"));
		result.setValueType(o.getString("valueType"));
		result.setOptionScript(o.getString("optionScript"));

		return result;
	}

	//				COMPONENT_TYPES.put("1008","ColumnComponent");
	//				COMPONENT_TYPES.put("1006","SelectComponent");
	//				COMPONENT_TYPES.put("1001","InputTextComponent");
	//				COMPONENT_TYPES.put("1002","DateTimeComponent");
	//				COMPONENT_TYPES.put("1003","CheckBoxComponent");
	//				COMPONENT_TYPES.put("1004","RadioComponent");
	//				COMPONENT_TYPES.put("1005","TextAreaComponent");
	//				COMPONENT_TYPES.put("1007","PasswordComponent");
	//				COMPONENT_TYPES.put("1009","LabelComponent");
	public boolean isValueType(){
		int componentType = this.componentType;
		return  componentType==1001 || componentType==1002 ||componentType==1003
				||componentType==1004 ||componentType==1005 || componentType==1006
				||componentType==1007||componentType==1012 || componentType==1010
				|| componentType==1011 || componentType==1016 || componentType==1019
				|| componentType==1020 || componentType==1029 || componentType==1030 || componentType==1031;
	}

	public boolean isSelectType(){
		return this.componentType == 1006;
	}

	public boolean isInputType(){
		return this.componentType == 1001 || this.componentType == 1005;
	}

	public boolean isDateTimeType(){
		return this.componentType == 1002;
	}

	public Integer getComponentType() {
		return componentType;
	}

	public void setComponentType(Integer componentType) {
		this.componentType = componentType;
	}

	public String getDynamicPageId() {
		return dynamicPageId;
	}

	public void setDynamicPageId(String dynamicPageId) {
		this.dynamicPageId = dynamicPageId;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * @param accessKey the accessKey to set
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * @return the tabIndex
	 */
	public String getTabIndex() {
		return tabIndex;
	}

	/**
	 * @param tabIndex the tabIndex to set
	 */
	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	/**
	 * @return the dataItemCode
	 */
	public String getDataItemCode() {
		return dataItemCode;
	}

	/**
	 * @param dataItemCode the dataItemCode to set
	 */
	public void setDataItemCode(String dataItemCode) {
		this.dataItemCode = dataItemCode;
	}

	/**
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the formatString
	 */
	public String getFormatString() {
		return formatString;
	}

	/**
	 * @param formatString the formatString to set
	 */
	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}

	/**
	 * @return the defaultValueScript
	 */
	public String getDefaultValueScript() {
		return defaultValueScript;
	}

	/**
	 * @param defaultValueScript the defaultValueScript to set
	 */
	public void setDefaultValueScript(String defaultValueScript) {
		this.defaultValueScript = defaultValueScript;
	}

	/**
	 * @return the formValidator
	 */
	public String getFormValidator() {
		return formValidator;
	}

	/**
	 * @param formValidator the formValidator to set
	 */
	public void setFormValidator(String formValidator) {
		this.formValidator = formValidator;
	}

	/**
	 * @return the optionScript
	 */
	public String getOptionScript() {
		return optionScript;
	}

	/**
	 * @param optionContent the optionContent to set
	 */
	public void setOptionScript(String optionContent) {
		this.optionScript = optionContent;
	}
	
}
