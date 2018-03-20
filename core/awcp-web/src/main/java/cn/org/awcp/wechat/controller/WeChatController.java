package cn.org.awcp.wechat.controller;

import cn.org.awcp.wechat.domain.JsapiTicket;
import cn.org.awcp.wechat.domain.WeChatValidationInfo;
import cn.org.awcp.wechat.domain.menu.WeChatMenu;
import cn.org.awcp.wechat.payment.util.WexinPayUtil;
import cn.org.awcp.wechat.service.WeChatService;
import cn.org.awcp.wechat.util.WeChatUtil;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static cn.org.awcp.wechat.util.Constant.APPID;
import static cn.org.awcp.wechat.util.Constant.TOKEN;

/**
 * 验证服务器地址的有效性
 * @author yqtao
 * @version 1.0
 *
 */
@Controller
@RequestMapping(value="/wechat")
public class WeChatController {

	private static Logger logger = LoggerFactory.getLogger(WeChatController.class);
	
	@Autowired
	private WeChatService weChatService;
	
	/**
	 * @param info 微信服务器传递过来的数据
	 * @return
	 */
	@RequestMapping(value="/api",method = RequestMethod.GET)
	@ResponseBody
	public String verifyInterface(WeChatValidationInfo info){
		String echostr = info.getEchostr();   
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败  
        if (WeChatUtil.checkSignature(TOKEN,info.getSignature(), info.getTimestamp(), info.getNonce())) {  
            return echostr;  
        } 
        else {  
            logger.info("不是微信服务器发来的请求,请小心!");  
            return null;
        }  
	}
	
	/**
	 * @param info	微信服务器传递过来的数据
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/api",method = RequestMethod.POST)
	@ResponseBody
	public String handleMesg(WeChatValidationInfo info,HttpServletRequest request,HttpServletResponse response) 
			throws UnsupportedEncodingException{
		if (WeChatUtil.checkSignature(TOKEN,info.getSignature(), info.getTimestamp(), info.getNonce())) {  
			//调用WeChatService类的processRequest方法接收、处理消息，并得到处理结果
	        String respMessage = weChatService.processRequest(request);
	        logger.info("response内容为:" + respMessage);
	        return respMessage;
        } 
		else {  
			return null;
        }  
	}

	/**
	 * 创建微信菜单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/createMenu",method=RequestMethod.POST)
	public Map<String,Object> createMenu(){
		WeChatMenu menu = weChatService.getWeChatMenu();
		WeChatUtil.delMenu();
		WeChatUtil.createMenu(menu);
		return null;
	}	

	/**
	 * 通过用户授权code获取Openid
	 * @param code
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/getOpenid")
    public String getOpenid(String code){
		return WeChatUtil.getOpenid(code);
	}
	
	/**
	 * 前端调用微信js初始化数据
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getWxJsData")
	public Map<String,Object> getWxJsData(String url){
		Map<String,Object> map = new HashMap<String,Object>();
		long timestamp = System.currentTimeMillis();
		String noncestr = WexinPayUtil.getNonceStr();
		map.put("timestamp", timestamp);
		map.put("appid", APPID);
		map.put("nonceStr", noncestr);
		map.put("signature", getSignature(noncestr, timestamp, JsapiTicket.getTicket(), url));
		return map;
	}
	
	private String getSignature(String noncestr,long timestamp,String jsapi_ticket,String url){
		TreeMap<String,Object> treeMap = new TreeMap<String,Object>();
		treeMap.put("noncestr", noncestr);
		treeMap.put("timestamp",timestamp );
		treeMap.put("jsapi_ticket", jsapi_ticket);
		treeMap.put("url", url);
		StringBuffer sb = new StringBuffer();
		for(String key:treeMap.keySet()){
			sb.append(key + "=" + treeMap.get(key) + "&");
		}
		if(sb.length()>1){
			sb.deleteCharAt(sb.length()-1);
		}
		String sign = new Sha1Hash(sb.toString()).toString();
		return sign;
	}
	
}
