package org.szcloud.framework.formdesigner.hallinterface.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropsUtil {
	private static Properties props = new Properties();
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);
	static {
		// 加载属性文件
		props = new Properties();
		try {
			props.load(PropsUtil.class.getClassLoader().getResourceAsStream("hallmap.properties"));
			logger.debug("PropsUtil.enclosing_method():" + PropsUtil.class.getClassLoader());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		if (key == null && "".equals(key)) {
			return null;
		}
		return props.getProperty(key);
	}

	public static Long getLongValue(String key) {
		if (key == null && "".equals(key)) {
			return null;
		}
		return new Long(props.getProperty(key));
	}

	/*
	 * public static MatterCode getMatterCode(String key) throws Exception {
	 * MatterCode code = new MatterCode(); String value = getValue(key); if
	 * (value == null || "".equals(value)) { return null; } String[] values =
	 * value.split(","); if(!values[0].equals("null")){ // 父类事项 MatterCode
	 * parent = new MatterCode(); parent.setParent(null); String parents =
	 * getValue(values[0]); if(parents == null || "".equals(parents)){ return
	 * null; } String[] str = parents.split(","); parent.setMatterName(str[0]);
	 * parent.setMatterCode(str[1]); parent.setMatterType(str[2]);
	 * code.setParent(parent); } // 事项 code.setHallMatterCode(values[1]);
	 * //大厅事项编码 code.setMatterCode(values[2]); //政务系统事项编码
	 * code.setMatterName(values[3]); //事项名称 code.setTimeLimit(values[4]);
	 * //办文时限 code.setChargeMoney(values[5]); //收费金额
	 * code.setMatterType(values[6]); //事项类别 if(values.length > 8){
	 * code.setProjName(values[8]); //项目名称 } //监察信息 String supviseInfo =
	 * getValue(values[3]); if(supviseInfo != null){ String[] supviseInfos =
	 * supviseInfo.split(",");
	 * code.setSupervise(Boolean.valueOf(supviseInfos[0]));
	 * code.setSupervisePMatterCode(supviseInfos[1]);
	 * code.setSuperviseSMatterCode(supviseInfos[2]);
	 * code.setUploadFileList(supviseInfos[3]); } return code; }
	 */
}
