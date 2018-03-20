package cn.org.awcp.dingding.message;

public class ConversationMessageDelivery {

	public String sender;
	public String cid;
	public String agentid;

	public ConversationMessageDelivery(String sender, String cid, String agentId) {
		this.sender = sender;
		this.cid = cid;
		this.agentid = agentId;
	}

}
