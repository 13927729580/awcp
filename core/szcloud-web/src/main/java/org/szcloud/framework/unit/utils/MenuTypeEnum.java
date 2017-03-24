package org.szcloud.framework.unit.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 菜单类型枚举
 * @author allen
 *
 */
public enum MenuTypeEnum {
	/**
	 * 链接
	 */
	MENU_LINK("menuAddress","1"),
	
	/**
	 * 动态表单
	 */
	MENU_DYNAMICPAGE("dynamicpage","2");
	
	private String key = "";
	private String value = "";
	
	private MenuTypeEnum(String key , String value){
		this.key = key;
		this.value = value;
	}
	public String getkey() {
		return key;
	}
	public String getvalue() {
		return value;
	}
	
	/**
	 * 根据key获取枚举
	 * @param key
	 * @return
	 */
	public static MenuTypeEnum getOperChartType(String key){
		for (MenuTypeEnum operChartType : values()) {
			if(StringUtils.equals(operChartType.getkey(), key)){
				return operChartType;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getMenuString(String key) {
		for (MenuTypeEnum menutype : values()) {
			if(StringUtils.equals(menutype.getkey(), key)){
				return menutype.getvalue();
			}
		}
		return null;
	}
}
