package cn.org.awcp.wechat.domain.message;


/**
 * 回复语音消息（公众帐号——>普通用户）
 * @author yqtao
 *
 */
public class VoiceMessage extends BaseMessage{
	
	private String mediaId;

	public VoiceMessage(){
		super();
	}
	
	/**
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @param createTime
	 * @param msgType
	 * @param mediaId
	 */
    public VoiceMessage(String toUserName, String fromUserName, long createTime,
			String msgType,String mediaId) {
    	super(toUserName,fromUserName,createTime,msgType);
        this.mediaId = mediaId;
    }

    /**
     * 
     * @return
     */
    public String getMediaId() {
        return mediaId;
    }

    /**
     * 
     * @param mediaId
     */
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
