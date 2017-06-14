package org.szcloud.framework.unit.utils; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/** 
 * Json工厂类
 * 用于序列化java对象Json字符串或者反序列化字符串到java对象
 */
public class JsonFactory {

	/**
	 * 日志对象
	 */
	protected Log logger = LogFactory.getLog(getClass());
   private ObjectMapper mapper;  
   public JsonFactory(){
       mapper = new ObjectMapper();
   }

   public String encode2Json(Object obj){
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
   
   @SuppressWarnings("unchecked")
   public Object decodeObject(String json, @SuppressWarnings("rawtypes") Class valueType){
       try {
       return  mapper.readValue(json, valueType);
    }
    catch (Exception e) {
        logger.error(e.getMessage());
    }
       return null;
   }
   

}
