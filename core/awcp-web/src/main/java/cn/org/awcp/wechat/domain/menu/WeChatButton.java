package cn.org.awcp.wechat.domain.menu;

import java.util.List;

/**
 * 微信一级菜单
 * @author yqtao
 *
 */
public class WeChatButton {
	//菜单的响应动作类型(必须)
	private String type;
	
	//菜单标题,不超过16个字节,子菜单不超过40个字节(必须)
	private String name;
	
	//click点击类型必须:菜单KEY值，用于消息接口推送,不超过128字节
	private String key;
	
	//view类型必须:网页链接，用户点击菜单可打开链接,不超过1024字节
	private String url;
	
	//media_id类型和view_limited类型必须:调用新增永久素材接口返回的合法media_id
	private String media_id;
	
	//二级菜单
	private List<WeChatButton> sub_button;

	public WeChatButton(){
		super();
	}
	
	/**
	 * 
	 * @param type	菜单的响应动作类型
	 * @param name	菜单标题
	 * @param key	菜单KEY值,click点击类型必须
	 * @param url	网页链接,view类型必须
	 * @param media_id		调用新增永久素材接口返回的合法media_id
	 * @param sub_button	二级菜单
	 */
	public WeChatButton(String type, String name, String key, String url,
			String media_id, List<WeChatButton> sub_button) {
		super();
		this.type = type;
		this.name = name;
		this.key = key;
		this.url = url;
		this.media_id = media_id;
		this.sub_button = sub_button;
	}

	public String toString() {
		String str = "button: {";
		if(type!=null){
			str += "type=" + type + ",";
		}
		if(name!=null){
			str += "name=" + name + ",";
		}
		if(key!=null){
			str += "key=" + key + ",";
		}
		if(url!=null){
			str += "url=" + url + ",";
		}
		if(media_id!=null){
			str += "media_id=" + media_id + ",";
		}
		if(sub_button!=null){
			str += "sub_button=" + sub_button + ",";
		}
		str += "}";
		return str;
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * @return
	 */
	public String getMedia_id() {
		return media_id;
	}

	/**
	 * 
	 * @param media_id
	 */
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<WeChatButton> getSub_button() {
		return sub_button;
	}

	/**
	 * 
	 * @param sub_button
	 */
	public void setSub_button(List<WeChatButton> sub_button) {
		this.sub_button = sub_button;
	}
}
