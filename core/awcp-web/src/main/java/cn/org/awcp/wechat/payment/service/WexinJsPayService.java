package cn.org.awcp.wechat.payment.service;

import static cn.org.awcp.wechat.util.Constant.*;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import cn.org.awcp.wechat.payment.domain.OrderInfo;
import cn.org.awcp.wechat.payment.util.WexinPayUtil;
import cn.org.awcp.wechat.util.ConstantPay;

/**
 * 微信jsapi支付
 * @author yqtao
 *
 */
@Service
public class WexinJsPayService {

	public  String getResult(OrderInfo order,Map<String,String> map){
		if("0".equals(ConstantPay.PAYMENT_TYPE)){
			return byNormalPay(order, map);
		}
		else{
			return byServicePay(order, map);
		}
	}

	//普通商户支付
	private String byNormalPay(OrderInfo order,Map<String,String> map){
		String appid = APPID;				//普通商户的微信公众号AppID
		String key = ConstantPay.KEY;		//普通商户的商户号秘钥
		String mch_id = ConstantPay.MCH_ID;	//普通商户的商户号
		
		String out_trade_no = order.getOutTradeNo();	//商户订单号
		String body = order.getBody();					//商品描述根据情况修改
		String total_fee = order.getTotalFee() + "";	// 总金额以分为单位，不带小数点	
		String attach = order.getAttach();

		//调用统一下单接口获取预支付单号prepay_id所需的参数
		Map<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", appid);		//公众号APPID
		parameters.put("mch_id", mch_id);	//商户号

		String spbill_create_ip = map.get("spbill_create_ip");	//终端IP	
		String notify_url = map.get("notify_url");
		String openid = map.get("openid");
		String nonce_str = WexinPayUtil.getNonceStr();
		String trade_type = "JSAPI";
		
		parameters.put("body", body);	//商品名称
		parameters.put("out_trade_no", out_trade_no);	//订单号 
		parameters.put("total_fee", total_fee);	//订单金额以分为单位，只能为整数 
		parameters.put("attach", attach);
		parameters.put("spbill_create_ip",  spbill_create_ip);	//客户端本地ip 
		parameters.put("notify_url",notify_url);			//支付回调地址
		parameters.put("trade_type", trade_type);			//支付方式为JSAPI支付 
		parameters.put("openid", openid);	//用户微信的openid，当trade_type为JSAPI的时候，该属性字段必须设置
		parameters.put("nonce_str", nonce_str);//随机字符串 
		parameters.put("limit_pay", "no_credit");//随机字符串 
		
		String sign = WexinPayUtil.createSign(parameters,key);	//签名
		parameters.put("sign", sign);

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
				 "<openid>" + openid  + "</openid>" + 
				 "<limit_pay>no_credit</limit_pay>" + 
				 "</xml>";
		String prepay_id = WexinPayUtil.getPrepayId(xml);
		
		//params传递到jsp页面供网页端调起支付API用,该调用需要签名
		Map<String, String> params = new TreeMap<String, String>();
		if(StringUtils.isBlank(prepay_id)){
			params.put("result","fail");
			params.put("msg","40002");
			return JSONArray.toJSONString(params);
		}

		String timeStamp = System.currentTimeMillis() + "";
		params.put("appId", appid);
		params.put("timeStamp", timeStamp);
		params.put("nonceStr", nonce_str);
		params.put("package", "prepay_id=" + prepay_id);
		//获取预支付单号prepay_id后，需要将它参与签名。 微信支付最新接口中，要求package的值的固定格式为prepay_id=...
		params.put("signType", "MD5");	//微信支付新版本签名算法使用MD5，不是SHA1
		/*
		 * 获取预支付prepay_id之后，需要再次进行签名，参与签名的参数有：appId、timeStamp、nonceStr、package、
		 * signType. 主意上面参数名称的大小写. 该签名用于前端js中WeixinJSBridge.invoke中的paySign的参数值
		 */
		String paySign = WexinPayUtil.createSign(params,key);	// 要签名
		params.put("paySign", paySign);
		//预支付单号，前端ajax回调获取。由于js中package为关键字，所以，这里使用packageValue作为key。
		params.put("packageValue", "prepay_id=" + prepay_id);	
		params.put("sendUrl", "");	//付款成功后，微信会同步请求我们自定义的成功通知页面，通知用户支付成功 
		params.put("result","OK");
		String json = JSONArray.toJSONString(params);
		return json;
	}

	//服务商支付
	private String byServicePay(OrderInfo order,Map<String,String> map){
		SortedMap<String, String> params = new TreeMap<String, String>();
		
		String out_trade_no = order.getOutTradeNo();	//商户订单号
		String body = order.getBody();					//商品描述根据情况修改
		String total_fee = order.getTotalFee() + "";	// 总金额以分为单位，不带小数点	
		String attach = order.getAttach();
		
		String spbill_create_ip = map.get("spbill_create_ip");	//终端IP	
		String notify_url = map.get("notify_url");
		String openid = map.get("openid");
		String nonce_str = WexinPayUtil.getNonceStr();
		String trade_type = "JSAPI";
		
		String appid = ConstantPay.PARENT_APPID;	//微信支付服务商的公众号AppID
		String key = ConstantPay.PARENT_KEY;		//微信支付服务商的商户号秘钥
		String mch_id = ConstantPay.PARENT_MCH_ID;	//微信支付服务商的商户号	
		String sub_mch_id = ConstantPay.SUB_MCH_ID;//从微信支付服务商申请的子商户号
		String sub_appid = APPID;		//普通商户的微信公众号AppID

		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", appid);				//微信支付服务商公众号APPID 
		parameters.put("mch_id", mch_id);			//微信支付服务商商户号		
		parameters.put("sub_appid", sub_appid);		//子商户的公众号AppID
		parameters.put("sub_mch_id", sub_mch_id);  	//子商户号,通过服务商申请

		parameters.put("body", body);							//商品名称
		parameters.put("out_trade_no", out_trade_no); 			//订单号
		parameters.put("total_fee", total_fee);					//订单金额以分为单位，只能为整数
		parameters.put("spbill_create_ip",  spbill_create_ip);	// 客户端本地ip
		parameters.put("notify_url",notify_url);				//支付回调地址 
		parameters.put("trade_type", trade_type);				// 支付方式为JSAPI支付
		parameters.put("nonce_str", nonce_str);					//随机字符串 
		parameters.put("sub_openid", openid);					//使用sub_openid,用户在子商户的公众号的openid
		parameters.put("attach", attach);

		String sign = WexinPayUtil.createSign(parameters,key);
		parameters.put("sign", sign);

		String xml = "";
		String prepay_id = WexinPayUtil.getPrepayId(xml);
		if(StringUtils.isBlank(prepay_id)){
			params.put("result","fail");
			params.put("msg","40002");
			return JSONArray.toJSONString(params);
		}
		// 参数
		String timeStamp = System.currentTimeMillis() + "";

		params.put("appId", appid);		//必须使用服务商商户号
		params.put("timeStamp", timeStamp);
		params.put("nonceStr", nonce_str);
		// 获取预支付单号prepay_id后，需要将它参与签名。 微信支付最新接口中，要求package的值的固定格式为prepay_id=...
		params.put("package", "prepay_id=" + prepay_id);	
		params.put("signType", "MD5");	// 微信支付新版本签名算法使用MD5，不是SHA1
		/*
		 * 获取预支付prepay_id之后，需要再次进行签名，参与签名的参数有：appId、timeStamp、nonceStr、package、
		 * signType. 主意上面参数名称的大小写. 该签名用于前端js中WeixinJSBridge.invoke中的paySign的参数值
		 */
		String paySign = WexinPayUtil.createSign(params,key);	// 要签名
		params.put("paySign", paySign);
		//预支付单号，前端ajax回调获取。由于js中package为关键字，所以，这里使用packageValue作为key。 
		params.put("packageValue", "prepay_id=" + prepay_id);	
		params.put("sendUrl", "");	//付款成功后，微信会同步请求我们自定义的成功通知页面，通知用户支付成功
		String userAgent = map.get("user-agent");	//获取用户的微信客户端版本号，用于前端支付之前进行版本判断，微信版本低于5.0无法使用微信支付
		char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
		params.put("agent", new String(new char[] { agent }));
		params.put("result","OK");
		String json = JSONArray.toJSONString(params);
		return json;
	}

}
