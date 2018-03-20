package cn.org.awcp.wechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.org.awcp.wechat.dao.MenuDao;
import cn.org.awcp.wechat.domain.menu.WeChatButton;
import cn.org.awcp.wechat.domain.menu.WeChatMenu;
import cn.org.awcp.wechat.domain.message.MessageType;
import cn.org.awcp.wechat.util.MessageUtil;

/**
 * 服务类
 * @author yqtao
 *
 */
@Service
public class WeChatService {
	
	private static Logger logger = LoggerFactory.getLogger(WeChatService.class);
	
	@Autowired
	@Qualifier(value="handlerImp")
	private Handler handler;
	
	@Autowired
	private MenuDao menuDao;
	
	/**
	 * 构造微信菜单
	 * @return
	 */
	public WeChatMenu getWeChatMenu(){
		List<WeChatButton> buttons = new ArrayList<WeChatButton>();		//存储一级菜单的列表
		List<WeChatButton> subButton = new ArrayList<WeChatButton>();	//存储二级菜单的列表
		WeChatButton button = null;	//一级菜单
		List<Map<String,Object>> list = menuDao.getWeChatMenu();	//获取按顺序的菜单列表
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			String parentId = (String) map.get("parentId");
			//一级菜单
			if(parentId==null || "".equals(parentId)){
				if(button==null){
					button = new WeChatButton();	//第一次找到一级菜单时
				}
				else{
					/* 第一次之后找到一级菜单时,执行以下操作
					 * 1:将子菜单列表赋值到父菜单
					 * 2:将一级菜单添加到一级菜单列表
					 * 3:将button指向新的对象
					 * 4:将subButton指向新的对象
					 */
					button.setSub_button(subButton);
					buttons.add(button);
					button = new WeChatButton();
					subButton = new ArrayList<WeChatButton>(); 
				}
				button.setName((String)map.get("name"));
				button.setType(getType((String)map.get("type")));
				button.setKey((String)map.get("keyValue"));
				button.setUrl((String)map.get("url"));
			}
			//二级菜单
			else{
				WeChatButton btn = new WeChatButton();
				btn.setName((String)map.get("name"));
				btn.setType(getType((String)map.get("type")));
				btn.setKey((String)map.get("keyValue"));
				btn.setUrl((String)map.get("url"));
				subButton.add(btn);
				
			}
			//最后一项时
			if(i==list.size()-1){
				button.setSub_button(subButton);
				buttons.add(button);
			}
		}
		WeChatMenu menu = new WeChatMenu();
		menu.setButton(buttons);
		return menu;
	}
		
	/**
	 * 处理微信发来的请求
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request) {
		String respMessage = "";
		try {
			// xml请求解析
			// 调用消息工具类MessageUtil解析微信发来的xml格式的消息,解析的结果放在HashMap里;
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			String msgType = requestMap.get("MsgType");	// 消息类型
			
			StringBuffer info = new StringBuffer();
			for(String key :requestMap.keySet()){
				info.append(key + "=" + requestMap.get(key) + ",\t");
			}
			System.out.println("**********************************************");
			logger.info("request传过来的数据:"+info);
			System.out.println("**********************************************");
			// 文本消息
			if(msgType.equals(MessageType.TEXT)) {
				return handler.onText(requestMap);
			}
			
			// 图片消息
			if(msgType.equals(MessageType.IMAGE)) {
				return handler.onImage(requestMap);
			}
			
			//地理位置消息
			if(msgType.equals(MessageType.LOCATION)){
				return handler.onLocation(requestMap);
			}
			
			//事件消息
			if(msgType.equals(MessageType.EVENT)){
				return handler.onEvent(requestMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}
	
	//将数字转换为特定事物的菜单类型
	private String getType(String type){
		if("1".equals(type)) {
            return "click";
        }
		if("2".equals(type)) {
            return "view";
        }
		if("3".equals(type)) {
            return "scancode_push";
        }
		if("4".equals(type)) {
            return "scancode_waitmsg";
        }
		if("5".equals(type)) {
            return "pic_sysphoto";
        }
		if("6".equals(type)) {
            return "pic_photo_or_album";
        }
		if("7".equals(type)) {
            return "pic_weixin";
        }
		if("8".equals(type)) {
            return "location_select";
        }
		return null;
	}
	
}
