package cn.org.awcp.wechat.task;

import org.springframework.stereotype.Service;

import cn.org.awcp.wechat.domain.AccessToken;
import cn.org.awcp.wechat.domain.JsapiTicket;
import cn.org.awcp.wechat.util.WeChatUtil;

/**
 * 使用spring task 来调度任务
 * @author yqtao
 *
 */
@Service
public class UpdateTokenAndTicketJob {

	public void update(){
		AccessToken.setAccessToken(WeChatUtil.getAccessToken());//修改AccessToken
		JsapiTicket.setTicket(WeChatUtil.getJsapiTicket());	 	//修改Jsapi Ticket
	}
}

