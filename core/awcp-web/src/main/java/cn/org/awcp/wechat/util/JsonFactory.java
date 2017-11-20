package cn.org.awcp.wechat.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/** 
 * Json工厂类
 * 用于序列化java对象Json字符串或者反序列化字符串到java对象
 * @author yqtao
 */
public class JsonFactory {

	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(JsonFactory.class);
	
	private static ObjectMapper mapper = new ObjectMapper();  

	/**
	 * 对象装换为Json
	 */
	public static String encode2Json(Object obj){
		try {
			if(obj == null){
				return null;
			}
			return mapper.writeValueAsString(obj);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Json装换为对象
	 */
	@SuppressWarnings("unchecked")
	public static Object decodeObject(String json, @SuppressWarnings("rawtypes") Class valueType){
		try {
			return  mapper.readValue(json, valueType);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	
	 /** 
     * json字符串转Map 
     * auther:yqtao
     * @param jsonStr 
     * @return 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
     * @throws IOException 
     */ 
    @SuppressWarnings("unchecked")
	public static Map<String, Object> parseMap(String jsonStr) throws JsonParseException, JsonMappingException, IOException {  
        Map<String, Object> map = mapper.readValue(jsonStr, Map.class);  
        return map;  
    }  
   
    /** 
     * json字符串转 List对象 
     * auther:yqtao 
     * @param str   json字符串 
     * @param clazz 转换的类型 
     * @return 
     */ 
    public static <T> List<T> parseList(String str,Class<T> clazz){  
        return JSONArray.parseArray(str, clazz);  
    }  
    
}
