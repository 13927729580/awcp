package cn.org.awcp.wechat.domain.message;


/** 
 * 回复文本消息（公众帐号——>普通用户）
 * @author yqtao 
 */
public class TextMessage extends BaseMessage {
	
    private String content;  	// 消息内容  

    public TextMessage(){
    	super();
    }
    
    /**
     * 
     * @param toUserName
     * @param fromUserName
     * @param createTime
     * @param msgType
     * @param content	回复内容
     */
    public TextMessage(String toUserName, String fromUserName, long createTime,
			String msgType,String content){
    	super(toUserName,fromUserName,createTime,msgType);
    	this.content = content;
    }

    /**
     * 
     * @return
     */
	public String getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
