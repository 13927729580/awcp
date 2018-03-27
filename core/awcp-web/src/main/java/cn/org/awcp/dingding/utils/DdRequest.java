package cn.org.awcp.dingding.utils;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import cn.org.awcp.core.utils.DateUtils;
import cn.org.awcp.wechat.util.RequestUtil;

public class DdRequest {
	
	public static final String url = "https://eco.taobao.com/router/rest";
	public static final String GET_ROLE_LIST = "dingtalk.corp.role.list";	
	public static final String GET_ROLE_Simplelist = "dingtalk.corp.role.simplelist";
	public static final String GET_ROLE_GROUP = "dingtalk.corp.role.getrolegroup";
	
	public static Map<String,String> getParamsMap(Map<String,String> params,String method,String session){	
		String timestamp = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");				
		params.put("method",method);					//API接口名称。
		params.put("session",session);					//钉钉提供的授权Token
		params.put("timestamp",timestamp);				//时间戳，格式为yyyy-MM-dd HH:mm:ss
		params.put("format","json");					//响应格式。默认为xml格式，可选值：xml，json。
		params.put("v","2.0");							//是	API协议版本，可选值：2.0。
		params.put("simplify",Boolean.TRUE.toString());
		return params;
	}

	/**
	 * 获取企业角色列表
	 * @param accessToken
	 * @return
	 */
	public static String getRoleList(String accessToken){
		Map<String,String> params = new TreeMap<String,String>();
		DdRequest.getParamsMap(params,GET_ROLE_LIST,accessToken);		
		return RequestUtil.doPost(url, params);
	}
	
	/**
	 * 获取角色的员工列表
	 * @param accessToken
	 * @return
	 */
	public static String getRoleSimplelist(String accessToken,String role_id){
		Map<String,String> params = new TreeMap<String,String>();
		params.put("role_id", role_id);
		DdRequest.getParamsMap(params,GET_ROLE_Simplelist,accessToken);		
		return RequestUtil.doPost(url, params);
	}
	
	/**
	 * 获取角色组信息
	 * @param accessToken
	 * @param group_id
	 * @return
	 */
	public static String getRoleGroup(String accessToken,String group_id){
		Map<String,String> params = new TreeMap<String,String>();
		params.put("group_id", group_id);
		DdRequest.getParamsMap(params,GET_ROLE_GROUP,accessToken);		
		return RequestUtil.doPost(url, params);
	}
}
