package cn.org.awcp.venson.dingding.message;

public class TextMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String content;

	public TextMessage(String content) {
		super();
		this.content = content;
	}

	@Override
	public String type() {
		return "text";
	}
}
