package org.szcloud.framework.workflow.core.emun;

//以下内容定义意见签名权限设置
		//针对公文中的意见批示、签名批示和声音批示，设定有关权限限制用户对该批示意见的访问
		
		//批示类型
public class EResponseType {
	public static final int CommentResponseType = 1; //意见；
	public static final int SignResponseType = 2; //签名；
	public static final int VoiceResponseType = 4 ;//声音；
}
