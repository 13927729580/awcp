package cn.org.awcp.formdesigner.core.parse.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.formdesigner.core.domain.design.context.act.PageAct;

public class PageActBeanWorker {
	
	/**
	 * 配置信息转化为PageAct对象
	 * @param confContext
	 * @return PageAct PageAct对象
	 */
	public static PageAct convertConfToAct(String confContext) {
		JSONObject act  = JSON.parseObject(confContext);
		return convertConfToAct(act);
	}
	
	/**
	 * @Title: convertConfToAct 
	 * @Description: JSON对象转化为PageAct对象 
	 * @param object json对象
	 * @return PageAct    PageAct对象
	 * @throws
	 */
	public static PageAct convertConfToAct(JSONObject object) {
		PageAct act = JSON.parseObject(object.toJSONString(), PageAct.class);
		if(StringUtils.isBlank(act.getClientScript())){	//当没有客户端脚本时,设置默认的客户端脚本
			int type = act.getActType();
			switch(type){
			case 2001:	//保存
				act.setClientScript("actRun(\""+act.getPageId()+"\");");
				break;
			case 2002:	//返回
				act.setClientScript("actBackRun(\""+act.getPageId()+"\",\""+act.getExtbute().get("target")+"\");");
				break;
			case 2003:	//删除
				act.setClientScript("actDeleteRun(\""+act.getPageId()+"\");");
				break;
			case 2009:	//新增
				act.setClientScript("actNewRun(\""+act.getPageId()+"\",\""+act.getExtbute().get("target")+"\");");
				break;
			case 2010:	//编辑
				act.setClientScript("actUpdateRun(\""+act.getPageId()+"\",\""+act.getExtbute().get("target")+"\");");
				break;
			case 2014:
				act.setClientScript("downloadFile(\""+act.getPageId()+"\");");
				break;
			case 2015:
				act.setClientScript("actRunWithNoValidators(\""+act.getPageId()+"\");");
				break;
			case 2016:
				act.setClientScript("downloadFile(\""+act.getPageId()+"\");");
				break;
			default:
				act.setClientScript("actRunWithNoValidators(\""+act.getPageId()+"\");");
			}
		}else{
			act.setClientScript(StringEscapeUtils.unescapeHtml4(act.getClientScript()));
		}
		return act;
	}
	
	/**
	 * @Title: convertConfToPageActs 
	 * @Description: 配置字符串转化为PageAct map
	 * @param confContext 配置字符串
	 * @return Map<String,PageAct>   key:pageId--PageAct标识  value:PageAct对象 
	 * @throws
	 */
	public static Map<String, PageAct> convertConfToPageActs(String confContext) {
		Map<String, PageAct> map = new HashMap<String, PageAct>();
		JSONArray array = JSON.parseArray(confContext);
		for(int i = 0; i < array.size(); i++){
			JSONObject o = array.getJSONObject(i);
			PageAct pc = convertConfToAct(o);
			if(pc != null){
				map.put(pc.getPageId(), pc);
			}
		}
		return map;
	}
	
}
