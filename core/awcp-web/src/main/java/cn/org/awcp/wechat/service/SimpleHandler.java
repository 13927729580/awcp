package cn.org.awcp.wechat.service;

import cn.org.awcp.wechat.dao.MessageDao;
import cn.org.awcp.wechat.domain.AccessToken;
import cn.org.awcp.wechat.domain.event.EventType;
import cn.org.awcp.wechat.domain.message.Article;
import cn.org.awcp.wechat.domain.message.MessageType;
import cn.org.awcp.wechat.domain.message.NewsMessage;
import cn.org.awcp.wechat.domain.message.TextMessage;
import cn.org.awcp.wechat.util.ConstantURL;
import cn.org.awcp.wechat.util.RequestUtil;
import cn.org.awcp.wechat.util.XmlMsgBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * 处理微信事件
 * @author yqtao
 *
 */
@Service(value="handlerImp")
public class SimpleHandler implements Handler{
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 处理文本消息
	 */
	@Override
    public String onText(Map<String, String> requestMap) {
		String content = requestMap.get("Content");	// 消息内容
		String to = requestMap.get("ToUserName");		//公众号
		String from = requestMap.get("FromUserName");	//微信号OpenId
		if(content != null){
			String sql = "select keyword,ID,replyType,isFuzzy from wechat_keyword_reply";
			try{
				List<Map<String,Object>> keywordList = jdbcTemplate.queryForList(sql);
				String replyType ;
				String id;
				for(int i=0;i<keywordList.size();i++){
					if(content.equals(keywordList.get(i).get("keyword"))){
						replyType = (String) keywordList.get(i).get("replyType");
						id = (String) keywordList.get(i).get("ID");
						return respContent(replyType, id, from, to);
					}
				}
				for(int i=0;i<keywordList.size();i++){
					if("1".equals(keywordList.get(i).get("isFuzzy"))){
						if(content.indexOf((String)keywordList.get(i).get("keyword"))>-1){
							replyType = (String) keywordList.get(i).get("replyType");
							id = (String) keywordList.get(i).get("ID");
							return respContent(replyType, id, from, to);
						}
					}
				}
			}catch(DataAccessException e){
				e.printStackTrace();
				return respText(from, to, "您好。");
			}
		}		
		return respText(from, to, "您好。");
	}
	
	/**
	 * 处理事件消息
	 */
	@Override
    public String onEvent(Map<String, String> requestMap) {
		String result = null;	//存储响应的文本消息
		String event = requestMap.get("Event");			//事件的类型	
		String to = requestMap.get("ToUserName");		//公众号
		String from = requestMap.get("FromUserName");	//微信号OpenId
		//菜单点击事件处理
		if(event.equals(EventType.CLICK)){
			String eventKey = requestMap.get("EventKey");			
			String sql = "select keyword,ID,replyType from wechat_keyword_reply";
			try{
				List<Map<String,Object>> keywordList = jdbcTemplate.queryForList(sql);
				String replyType ;
				String id;
				for(int i=0;i<keywordList.size();i++){
					if(eventKey.equals(keywordList.get(i).get("keyword"))){
						replyType = (String) keywordList.get(i).get("replyType");
						id = (String) keywordList.get(i).get("ID");
						return respContent(replyType, id, from, to);
					}
				}									
			} catch(DataAccessException e){
				e.printStackTrace();
				return respText(from, to, "您好。");
			}			
		}
		//订阅事件处理
		if(event.equals(EventType.SUBSCRIBE)){
			Map<String,Object> reply = messageDao.getSubscribeReply();
			if(reply==null){//订阅事件处理没有设置时:使用默认回复
				return respText(from, to,"您好，欢迎关注qzwy-vip！");
			} else{
				String replyType = (String) reply.get("replyType");
				String id = (String) reply.get("ID");
				return respContent(replyType,id,from,to);
			}
		}
		//获取用户的地址
		if(event.equals(EventType.LOCATION)){
			String Longitude = requestMap.get("Longitude");
			String Latitude = requestMap.get("Latitude");
			System.out.println("Longitude=" + Longitude + ",Latitude=" + Latitude);
			return respText(from, to, "");
		}
		//取消订阅事件处理
		if(event.equals(EventType.UNSUBSCRIBE)){
			String sql = "delete from wechat_user_location where openId=?";
			jdbcTemplate.update(sql,from);
			result = "See you again.";
			return respText(from, to, result);
		}
		//扫描事件
		if(event.equals(EventType.SCAN)){
			result = "why?";
			return respText(from, to, result);
		}
		return respText(from, to, "您好。");
	}

	/**
	 * 处理图片消息
	 */
	@Override
    public String onImage(Map<String, String> requestMap) {
		String mediaId = requestMap.get("MediaId");
		String requestUrl = ConstantURL.DOWN_MEDIA_URL.replace("ACCESS_TOKEN" ,AccessToken.getAccessToken());
		requestUrl = requestUrl.replace("MEDIA_ID", mediaId);
		String fileName = "E:\\test.jsp";
		try {
			RequestUtil.downloadMedia(requestUrl, fileName);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 处理地理位置消息
	 */
	@Override
    public String onLocation(Map<String,String> requestMap){
		String locationX = requestMap.get("Location_X");
		String locationY = requestMap.get("Location_Y");
		String respContent = "您所在位置的纬度为:"+locationX+",经度为："+locationY;
		TextMessage msg = new TextMessage(requestMap.get("FromUserName"),requestMap.get("ToUserName"),
				System.currentTimeMillis(),MessageType.TEXT,respContent);
		return XmlMsgBuilder.create().text(msg).build();
	}
	
	//默认文本回复
	private String respText(String from,String to,String result){
		TextMessage msg = new TextMessage(from,to,System.currentTimeMillis(),MessageType.TEXT,result);
		return XmlMsgBuilder.create().text(msg).build();
	}
	
	//响应文本,图文回复
	private String respContent(String replyType,String id,String from,String to){
		String result = null;
		//文本消息
		if("0".equals(replyType)){
			result = messageDao.getTextMessage(id);
			if(result==null){
				return respText(from, to,"您好");
			}
			else{
				return respText(from,to,result);
			}
		}
		//图文消息
		if("1".equals(replyType)){
			List<Article> list = messageDao.getArticles(id,"0");
			if(list.size()==0){
				return respText(from, to,"您好");
			}
			NewsMessage newMsg = new NewsMessage();
			newMsg.setArticleCount(list.size());
			newMsg.setArticles(list);
			newMsg.setFromUserName(to);
			newMsg.setToUserName(from);
			newMsg.setCreateTime(System.currentTimeMillis());
			newMsg.setMsgType(MessageType.NEWS);
			return XmlMsgBuilder.create().news(newMsg).build();
		}
		//多图文消息
		if("2".equals(replyType)){
			List<Article> list = messageDao.getArticles(id,"1");
			if(list.size()==0){
				return respText(from, to,"您好");
			}
			NewsMessage newMsg = new NewsMessage();
			newMsg.setArticleCount(list.size());
			newMsg.setArticles(list);
			newMsg.setFromUserName(to);
			newMsg.setToUserName(from);
			newMsg.setCreateTime(System.currentTimeMillis());
			newMsg.setMsgType(MessageType.NEWS);
			return XmlMsgBuilder.create().news(newMsg).build();
		}
		return respText(from, to,"您好");
	}

}
