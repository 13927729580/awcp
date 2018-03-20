package cn.org.awcp.wechat.domain;

/**
 * 微信验证实体类
 * @author yqtao
 *
 */
public class WeChatValidationInfo {
	private String signature;	//包含token的字符串
	private String timestamp;	// 时间戳
	private String nonce;		// 随机数
	private String echostr;		// 随机字符串
	
	public WeChatValidationInfo() {
		super();
	}

	/**
	 * 
	 * @param signature	加密后标志
	 * @param timestamp	时间戳
	 * @param nonce		随机字符串
	 * @param echostr	返回给后台的字符窜
	 */
	public WeChatValidationInfo(String signature, String timestamp, String nonce,
			String echostr) {
		super();
		this.signature = signature;
		this.timestamp = timestamp;
		this.nonce = nonce;
		this.echostr = echostr;
	}

	@Override
    public String toString() {
		return "WeChatValidationInfo [signature=" + signature + ", timestamp=" + timestamp
				+ ", nonce=" + nonce + ", echostr=" + echostr + "]";
	}

	/**
	 * 
	 * @return
	 */
	public String getSignature() {
		return signature;
	}
	
	/**
	 * 
	 * @param signature
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTimestamp() {
		return timestamp;
	}
	
	/**
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNonce() {
		return nonce;
	}
	
	/**
	 * 
	 * @param nonce
	 */
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEchostr() {
		return echostr;
	}
	
	/**
	 * 
	 * @param echostr
	 */
	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}
	
}
