package cn.org.awcp.venson.dingding.message;

public class LightAppMessageDelivery {

	public String touser;
	public String toparty;
	public String agentid;

	public LightAppMessageDelivery(String toUsers, String toParties, String agentId) {
		this.touser = toUsers;
		this.toparty = toParties;
		this.agentid = agentId;
	}

}
