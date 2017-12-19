package cn.org.awcp.unit.message;

import java.util.HashMap;
import java.util.Map;

public class PunNotification {
	public static final String KEY_NOTIFY = "notify";
	public static final String KEY_FLOW = "flow";
	private String id;
	private String title;
	private String content;
	private String msgUrl;
	private String type;
	private String createTime;
	private String createName;
	private String receiver;

	public PunNotification(String title, String content, String msgUrl, String createName) {
		this.title = title;
		this.content = content;
		this.msgUrl = msgUrl;
		this.createName = createName;
	}

	public PunNotification() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgUrl() {
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void sendScoket() {
		Map<String, Object> map = new HashMap<>();
		String[] userId = this.receiver.split(",");
		for (String user : userId) {
			this.receiver = null;
			map.put(this.getType(), this);
			WebSocket.sendMessage(user, map);
		}
	}

}
