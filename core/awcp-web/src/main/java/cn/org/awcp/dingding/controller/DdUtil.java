package cn.org.awcp.dingding.controller;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import cn.org.awcp.venson.controller.base.ControllerHelper;

public class DdUtil {

	/**
	 * 处理用户头像
	 * @param map
	 * @param name
	 * @param img
	 */
	public static void dealHeadImg(Map<String,Object> map,String name,String img){
		if(StringUtils.isNotBlank(img)){
			map.put("imgType", "img");
			map.put("img", img);
		}
		else{
			map.put("imgType", "divImg");
			map.put("subName", getSubName(name));
		}
	}
	
	//获取名字简写
	private static String getSubName(String name){
		if(isChinese(name)){
			if(name.length()>2){
				name = name.substring(name.length()-2);
			}
		}
		else{
			name = name.substring(0, 1);
		}
		return name;
	}

	//是否是汉字
	private static boolean isChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find()){
			flg = true;
		}
		return flg;
	}

	/**
	 * 获取文件图标的样式
	 * @param fileType
	 * @return
	 */
	public static String getCss(String fileType){
		String css = "icon_file_unknow";
		if("doc".equals(fileType) || "docx".equals(fileType)){
			css = "icon_file_word";
		} else if("xsl".equals(fileType) || "xslx".equals(fileType)){
			css = "icon_file_xsl";
		} else if("ppt".equals(fileType) || "pptx".equals(fileType)){
			css = "icon_file_ppt";
		} else if("zip".equals(fileType) || "rar".equals(fileType)){
			css = "icon_file_zip";
		} else if("video".equals(fileType)){
			css = "icon_file_video";
		} else if("pdf".equals(fileType)){
			css = "icon_file_pdf";
		} else if("mp3".equals(fileType)){
			css = "icon_file_mp3";
		} else if("jpg".equals(fileType)){
			css = "icon_file_jpg";
		} 
		return css;
	}
	
	/**
	 * 获取下来框值
	 * @param value
	 * @param options
	 * @return
	 */
	public static String getVal(String value,String options){
		String ret = "";
		String[] optionArr = options.split(";");
		for(int i=0;i<optionArr.length;i++){
			String option = optionArr[i];
			if(option.contains(value)){
				String[] arr = option.split("=");
				if(ControllerHelper.getLang()==Locale.SIMPLIFIED_CHINESE){
					ret = arr[1];
				}
				else{
					ret = arr[0];
				}
			}
		}
		return ret;
	}
}
