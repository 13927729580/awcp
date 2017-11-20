package cn.org.awcp.wechat.domain;

import org.apache.commons.lang3.StringUtils;

import cn.org.awcp.wechat.util.WeChatUtil;

/**
 * 使用jsapi接口凭证
 * @author yqtao
 *
 */
public class JsapiTicket {
	private String ticket;			//jsapi ticket 凭证

	private JsapiTicket() {
		super();
	}

    private static final JsapiTicket jsapiTicket = new JsapiTicket();

    /**
     * 获取ticket jsapi凭证
     * @return
     */
	public static synchronized String getTicket() {
		if(StringUtils.isBlank(jsapiTicket.ticket)){
			jsapiTicket.ticket = WeChatUtil.getJsapiTicket();
		}
		return jsapiTicket.ticket;
	}

	/**
	 * 设置jsapi凭证
	 * @param ticket 
	 */
	public static synchronized void setTicket(String ticket) {
		jsapiTicket.ticket = ticket;
	}

}
