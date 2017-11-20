package cn.org.awcp.venson.dingding.message;

public class LinkMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String messageUrl;
	public String picUrl;
	public String title;
	public String text;

	public LinkMessage(String messageUrl, String picUrl, String title, String text) {
		super();
		this.messageUrl = messageUrl;
		this.picUrl = picUrl;
		this.title = title;
		this.text = text;
	}

	@Override
	public String type() {
		return "link";
	}
}
