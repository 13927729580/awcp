package cn.org.awcp.wechat.domain.message;


/**
 * 回复视频消息（公众帐号——>普通用户）
 * @author yqtao
 *
 */
public class VideoMessage extends BaseMessage{
	private String mediaId;
	private String title;
	private String description;

	public VideoMessage() {
		super();
	}

	/**
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @param createTime
	 * @param msgType
	 * @param mediaId
	 * @param title
	 * @param description
	 */
	public VideoMessage(String toUserName, String fromUserName, long createTime,
			String msgType,String mediaId, String title, String description) {
		super(toUserName,fromUserName,createTime,msgType);
		this.mediaId = mediaId;
		this.title = title;
		this.description = description;
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

	/**
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
