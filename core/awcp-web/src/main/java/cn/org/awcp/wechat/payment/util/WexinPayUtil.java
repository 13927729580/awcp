package cn.org.awcp.wechat.payment.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.org.awcp.wechat.util.Constant;
import cn.org.awcp.wechat.util.ConstantPay;

public class WexinPayUtil {

	private static String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	private static String queryWxOrder = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	public static String getCodeUrl(String xml){
		try{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(createOrderURL);
			httpPost.addHeader("Connection", "keep-alive");
			httpPost.addHeader("Accept", "*/*");
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpPost.addHeader("Host", "api.mch.weixin.qq.com");
			httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpPost.addHeader("Cache-Control", "max-age=0");
			httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpPost.setEntity(new StringEntity(xml, Charset.forName("UTF-8")));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			String responseContent = null; // 响应内容
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			System.out.println(responseContent);
			InputStream is = new ByteArrayInputStream(responseContent.getBytes());
			return parseXml(is).get("code_url");
		} catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getPrepayId(String xml){
		try{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(createOrderURL);
			httpPost.addHeader("Connection", "keep-alive");
			httpPost.addHeader("Accept", "*/*");
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpPost.addHeader("Host", "api.mch.weixin.qq.com");
			httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpPost.addHeader("Cache-Control", "max-age=0");
			httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpPost.setEntity(new StringEntity(xml, Charset.forName("UTF-8")));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			String responseContent = null; // 响应内容
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			System.out.println(responseContent);
			InputStream is = new ByteArrayInputStream(responseContent.getBytes());
			return parseXml(is).get("prepay_id");
		} catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	public static Map<String, String> getWxOrder(String orderId){
		TreeMap<String,String> params = new TreeMap<String,String>();
		String appid = Constant.APPID;
		String mch_id = ConstantPay.MCH_ID;
		String nonce_str = getNonceStr();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		params.put("nonce_str", nonce_str);
		params.put("out_trade_no", orderId);
		String sign = createSign(params, ConstantPay.KEY);
		String xml = "<xml>" + 
					     "<appid>" + appid + "</appid>" + 
					     "<mch_id>" + mch_id + "</mch_id>" + 
					     "<nonce_str>" + nonce_str + "</nonce_str>" + 
					     "<sign>" + sign + "</sign>" + 
					     "<out_trade_no>" + orderId  + "</out_trade_no>" + 
					 "</xml>";
		try{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(queryWxOrder);
			httpPost.addHeader("Connection", "keep-alive");
			httpPost.addHeader("Accept", "*/*");
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpPost.addHeader("Host", "api.mch.weixin.qq.com");
			httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpPost.addHeader("Cache-Control", "max-age=0");
			httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpPost.setEntity(new StringEntity(xml, Charset.forName("UTF-8")));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			String responseContent = null; // 响应内容
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			System.out.println(responseContent);
			InputStream is = new ByteArrayInputStream(responseContent.getBytes());
			return parseXml(is);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/** 
     * 解析微信发来的请求（XML） 
     * @param request 
     * @return 
     * @throws Exception 
     */  
    @SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(InputStream is) throws Exception {  
        Map<String, String> map = new HashMap<String, String>();// 将解析结果存储在HashMap中         
        SAXReader reader = new SAXReader(); 			// 读取输入流 
        Document document = reader.read(is);  	  
        Element root = document.getRootElement();  		// 得到xml根元素
        List<Element> elementList = root.elements();	// 得到根元素的所有子节点
  
        // 遍历所有子节点  
        for (Element e : elementList) {
        	map.put(e.getName(), e.getText());  
        }
     
        is.close();   // 释放资源  
        return map;  
    }  

	/**
	 * 获取10位随机数
	 * @return
	 */
    public static String getNonceStr(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currTime = sdf.format(date);
		String strTime = currTime.substring(8, currTime.length());	//取时间部分
		Random random = new Random();
		String strRandom =  "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);	// 四位随机数	
		return strTime + strRandom;						
	}
    
    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     * @param packageParams
     * @param key
     * @return
     */
    public static String createSign(Map<String, String> packageParams,String key) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String,String>> es = packageParams.entrySet();
		Iterator<Entry<String,String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String,String> entry = (Map.Entry<String,String>) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = new Md5Hash(sb.toString()).toString().toUpperCase();
		return sign;
	}

}
