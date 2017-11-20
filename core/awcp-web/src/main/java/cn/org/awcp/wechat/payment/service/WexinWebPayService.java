package cn.org.awcp.wechat.payment.service;

import static cn.org.awcp.wechat.util.Constant.*;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import cn.org.awcp.wechat.payment.domain.OrderInfo;
import cn.org.awcp.wechat.payment.util.WexinPayUtil;
import cn.org.awcp.wechat.util.ConstantPay;

/**
 * 微信网页扫码支付
 * @author yqtao
 *
 */
@Service
public class WexinWebPayService {
	
	/**
	 * 获取codeUrl
	 * @param order	简单订单信息
	 * @param map	支付所需其他参数
	 * @return
	 */
	public String getCodeUrl(OrderInfo order,Map<String,String> map){
		if("0".equals(ConstantPay.PAYMENT_TYPE)){
			return getCodeUrlForNormal(order, map);
		}
		else{
			return getCodeUrlForService(order, map);
		}
	}
	
	//服务商微信支付获取codeUrl
	private String getCodeUrlForService (OrderInfo order,Map<String,String> map){
		String appid = ConstantPay.PARENT_APPID;	//微信支付服务商的公众号AppID
		String key = ConstantPay.PARENT_KEY;		//微信支付服务商的商户号秘钥
		String mch_id = ConstantPay.PARENT_MCH_ID;	//微信支付服务商的商户号	
		String sub_mch_id = ConstantPay.SUB_MCH_ID;//从微信支付服务商申请的子商户号
		
		String spbill_create_ip = map.get("spbill_create_ip");	//终端IP	
		//微信支付成功后通知地址 必须要求80端口并且地址不能带参数,这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = map.get("notify_url");	
		String trade_type = "NATIVE";			
		String nonce_str = WexinPayUtil.getNonceStr();	//随机字符串
		String product_id = "shopping";
	
		String out_trade_no = order.getOutTradeNo();	//商户订单号
		String body = order.getBody();					//商品描述根据情况修改
		String total_fee = order.getTotalFee() + "";	// 总金额以分为单位，不带小数点	
		String attach = order.getAttach();				//附加参数

		Map<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid", appid);
		treeMap.put("mch_id", mch_id);
		treeMap.put("nonce_str", nonce_str);
		treeMap.put("body", body);
		treeMap.put("attach", attach);
		treeMap.put("out_trade_no", out_trade_no);		
		treeMap.put("total_fee", total_fee);	//金额
		treeMap.put("spbill_create_ip", spbill_create_ip);
		treeMap.put("notify_url", notify_url);
		treeMap.put("trade_type", trade_type);
		treeMap.put("product_id", product_id);
		treeMap.put("sub_mch_id", sub_mch_id);
		treeMap.put("limit_pay", "no_credit");//随机字符串 
		
		String sign = WexinPayUtil.createSign(treeMap,key);
		String xml = "<xml>" + 
					 "<appid>" + appid + "</appid>" + 
					 "<mch_id>" + mch_id + "</mch_id>" + 
					 "<nonce_str>" + nonce_str + "</nonce_str>" + 
					 "<sign>" + sign + "</sign>" + 
					 "<body><![CDATA[" + body + "]]></body>" + 
					 "<out_trade_no>" + out_trade_no  + "</out_trade_no>" + 
					 "<attach>" + attach + "</attach>" + 
					 "<total_fee>" + total_fee + "</total_fee>" + 
					 "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + 
					 "<notify_url>" + notify_url + "</notify_url>" + 
					 "<trade_type>" + trade_type + "</trade_type>" + 
					 "<product_id>" + product_id  + "</product_id>" + 
					 "<sub_mch_id>" + sub_mch_id + "</sub_mch_id>" +
					 "<limit_pay>no_credit</limit_pay>" + 
					 "</xml>";
		String codeUrl = "";
		System.out.println("提交到微信的xml数据为:"+xml);
		codeUrl = WexinPayUtil.getCodeUrl(xml);
		System.out.println("code_url----------------"+codeUrl);
		return codeUrl;
	}
	
	//普通商户支付获取codeUrl
	private String getCodeUrlForNormal(OrderInfo order,Map<String,String> map){	
		String appid = APPID;				//普通商户的微信公众号AppID
		String key = ConstantPay.KEY;		//普通商户的商户号秘钥
		String mch_id = ConstantPay.MCH_ID;	//普通商户的商户号				
		
		String spbill_create_ip = map.get("spbill_create_ip");	//终端IP	
		//微信支付成功后通知地址 必须要求80端口并且地址不能带参数,这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = map.get("notify_url");	
		String trade_type = "NATIVE";			
		String nonce_str = WexinPayUtil.getNonceStr();	// 随机字符串
		String product_id = "shopping";
		
		String out_trade_no = order.getOutTradeNo();	//商户订单号
		String body = order.getBody();					//商品描述根据情况修改
		String total_fee = order.getTotalFee() + "";	// 总金额以分为单位，不带小数点	
		String attach = order.getAttach();
		
		Map<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid", appid);
		treeMap.put("mch_id", mch_id);
		treeMap.put("nonce_str", nonce_str);
		treeMap.put("body", body);
		treeMap.put("attach", attach);
		treeMap.put("out_trade_no", out_trade_no);		
		treeMap.put("total_fee", total_fee);	//金额
		treeMap.put("spbill_create_ip", spbill_create_ip);
		treeMap.put("notify_url", notify_url);
		treeMap.put("trade_type", trade_type);
		treeMap.put("product_id", product_id);
		
		String sign = WexinPayUtil.createSign(treeMap,key);
		String xml = "<xml>" + 
					 "<appid>" + appid + "</appid>" + 
					 "<mch_id>" + mch_id + "</mch_id>" + 
					 "<nonce_str>" + nonce_str + "</nonce_str>" + 
					 "<sign>" + sign + "</sign>" + 
					 "<body><![CDATA[" + body + "]]></body>" + 
					 "<out_trade_no>" + out_trade_no  + "</out_trade_no>" + 
					 "<attach>" + attach + "</attach>" + 
					 "<total_fee>" + total_fee + "</total_fee>" + 
					 "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + 
					 "<notify_url>" + notify_url + "</notify_url>" + 
					 "<trade_type>" + trade_type + "</trade_type>" + 
					 "<product_id>" + product_id  + "</product_id>" + 
					 "</xml>";
		String codeUrl = "";
		System.out.println("提交到微信的xml数据为:"+xml);
		codeUrl = WexinPayUtil.getCodeUrl(xml);
		System.out.println("code_url----------------"+codeUrl);
		return codeUrl;		
	}
}
