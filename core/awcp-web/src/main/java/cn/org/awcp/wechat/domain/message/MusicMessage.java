package cn.org.awcp.wechat.domain.message;


/**
 * 回复音乐消息（公众帐号——>普通用户）
 * @author yqtao
 *
 */
public class MusicMessage extends BaseMessage{
	
	private String title;
    private String description;
    private String musicUrl;
    private String hqMusicUrl;
    private String thumbMediaId;
    
    /**
     * 
     * @param toUserName
     * @param fromUserName
     * @param createTime
     * @param msgType
     * @param mediaId
     * @param title
     * @param description
     * @param musicUrl
     * @param hqMusicUrl
     * @param thumbMediaId
     */
	public MusicMessage(String toUserName, String fromUserName, long createTime,
			String msgType,String mediaId,String title, String description, String musicUrl,
			String hqMusicUrl, String thumbMediaId) {
		super(toUserName,fromUserName,createTime,msgType);
		this.title = title;
		this.description = description;
		this.musicUrl = musicUrl;
		this.hqMusicUrl = hqMusicUrl;
		this.thumbMediaId = thumbMediaId;
	}

	public MusicMessage() {
		super();
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

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 */
	public String getMusicUrl() {
		return musicUrl;
	}

	/**
	 * 
	 * @param musicUrl
	 */
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

	/**
	 * 
	 * @return
	 */
	public String getHqMusicUrl() {
		return hqMusicUrl;
	}

	/**
	 * 
	 * @param hqMusicUrl
	 */
	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}

	/**
	 * 
	 * @return
	 */
	public String getThumbMediaId() {
		return thumbMediaId;
	}

	/**
	 * 
	 * @param thumbMediaId
	 */
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

}
