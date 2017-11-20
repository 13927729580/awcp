package cn.org.awcp.venson.dingding.message;

public class ImageMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String media_id;

	public ImageMessage(String mediaId) {
		super();
		media_id = mediaId;
	}

	@Override
	public String type() {
		return "image";
	}
}
