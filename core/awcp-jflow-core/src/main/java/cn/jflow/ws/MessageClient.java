package cn.jflow.ws;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import BP.Sys.SystemConfig;
/**
 * 其他系统webservice客户端实现
 * @author 裴孝峰
 * @date 2016-71-15
 */
public class MessageClient {

	private Client client = null;


	/**
	 * 判断是否存在
	 * @Title: userIsexit
	 * @author 裴孝峰
	 * @date 2016年7月15日
	 */
	public boolean userIsexit(String userCode){
		Object[] result;
		try {
			result = client.invoke("userIsexit", userCode);
			if(result.length>0){
				return (Boolean) result[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 发送消息
	 * @Title: sendMsg
	 * @author 裴孝峰
	 * @date 2016年7月15日
	 */
	public void sendMsg(String contentTitle, String contentLevel, String contentType, String contentText, String receiverType, String receiverCodes, String receiverNames, String[][] buttons, String type){
		try {
			client.invoke("sendMsg",new Object[]{contentTitle,contentLevel,contentType,contentText,receiverType,receiverCodes,receiverNames,null,type});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
