package cn.org.awcp.wechat.domain.message;

/** 
 * 消息基类(公众帐号——>普通用户)
 * @author yqtao 
 */
public class BaseMessage {
	
    private String toUserName;  	// 接收方帐号(收到的OpenID)
    private String fromUserName;  	// 开发者微信号 
    private long createTime;  		// 消息创建时间 （整型） 
    private String msgType;  		// 消息类型(text/music/news)
    
    /**
     * 
     * @param toUserName	接收方帐号
     * @param fromUserName	开发者微信号 
     * @param createTime	消息创建时间戳
     * @param msgType		消息类型
     */
	public BaseMessage(String toUserName, String fromUserName, long createTime,
			String msgType) {
		super();
		this.toUserName = toUserName;
		this.fromUserName = fromUserName;
		this.createTime = createTime;
		this.msgType = msgType;
	}

	public BaseMessage() {
		super();
	}

	/**
	 * 
	 * @return
	 */
	public String getToUserName() {
		return toUserName;
	}

	/**
	 * 
	 * @param toUserName
	 */
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	/**
	 * 
	 * @return
	 */
	public String getFromUserName() {
		return fromUserName;
	}

	/**
	 * 
	 * @param fromUserName
	 */
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	/**
	 * 
	 * @return
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * 
	 * @param createTime
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * 
	 * @return
	 */
	public String getMsgType() {
		return msgType;
	}

	/**
	 * 
	 * @param msgType
	 */
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
   
}
