package cn.org.awcp.wechat.domain.message;

/**
 * 回复图片消息（公众帐号——>普通用户）
 * @author yqtao
 */
public class ImageMessage extends BaseMessage {

    private String mediaId;

    public ImageMessage() {
    	super();
    }

    public ImageMessage(String toUserName, String fromUserName, long createTime,
			String msgType,String mediaId){
    	super(toUserName,fromUserName,createTime,msgType);
    	this.setMediaId(mediaId);
    }
    
    public ImageMessage(String MediaId) {
        
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
