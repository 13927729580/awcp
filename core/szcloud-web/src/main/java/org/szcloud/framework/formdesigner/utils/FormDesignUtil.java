package org.szcloud.framework.formdesigner.utils;

import org.szcloud.framework.formdesigner.constrants.FormDesignGlobal;


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
