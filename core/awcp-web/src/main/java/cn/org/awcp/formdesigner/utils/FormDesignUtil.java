package cn.org.awcp.formdesigner.utils;

import cn.org.awcp.formdesigner.core.constants.FormDesignGlobal;


public class FormDesignUtil {
	/**
	 * 生成页面动作的编码
	 * 
	 * @return
	 */
	public static String initPageActCode(){
		return FormDesignGlobal.PAGE_ACT_CODE_PREFIX + System.currentTimeMillis();
	}
}
