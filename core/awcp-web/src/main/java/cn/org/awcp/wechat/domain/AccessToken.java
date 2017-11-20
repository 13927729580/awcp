package cn.org.awcp.wechat.domain;

import org.apache.commons.lang3.StringUtils;

import cn.org.awcp.wechat.util.WeChatUtil;

/** 
 * 微信通用接口凭证   
 * @author yqtao 
 */  
public class AccessToken {  
    private String access_token;  	// 获取到的凭证  
    
    private AccessToken() {
		
	}

    private static final AccessToken accessToken = new AccessToken();

	/**
	 * 获取接口通用凭证
	 * @return
	 */
	public synchronized static String getAccessToken() {
		if(StringUtils.isBlank(accessToken.access_token)){
			accessToken.access_token = WeChatUtil.getAccessToken();
		}
		return accessToken.access_token;
	}

	/**
	 * 设置接口通用凭证
	 * @param access_token	
	 */
	public synchronized static void setAccessToken(String access_token) {
		accessToken.access_token = access_token;
	}

}  