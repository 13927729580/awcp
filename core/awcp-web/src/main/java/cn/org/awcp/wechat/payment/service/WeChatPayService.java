package cn.org.awcp.wechat.payment.service;

import cn.org.awcp.venson.util.BeanUtil;
import cn.org.awcp.wechat.payment.domain.WeChatOrderInfo;
import cn.org.awcp.wechat.payment.util.WexinPayUtil;
import cn.org.awcp.wechat.util.ConstantPay;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static org.apache.commons.lang3.CharEncoding.UTF_8;

/**
 * 微信支付
 *
 * @author venson
 * @version 20180410
 */
@Service
public class WeChatPayService {

    private final static Logger logger = LoggerFactory.getLogger(WeChatPayService.class);
    private final static String SUCCESS = "SUCCESS";
    private final static String RETURN_CODE = "return_code";
    private final static String RESULT_CODE = "result_code";

    /**
     * 普通商户支付
     *
     * @param order 订单
     * @param key   商户号key
     * @return
     */
    public Object pay(WeChatOrderInfo order, String key, WeChatPayCallbacks callbacks) {
        //设置随机数
        order.setNonce_str(WexinPayUtil.getNonceStr());
        Map<String, Object> map = BeanUtil.objectToMap(order);
        TreeMap<String, String> parameters = (TreeMap) new TreeMap<>(map);
        //进行签名
        String sign = WexinPayUtil.createSign(parameters, key);
        parameters.put("sign", sign);
        TreeMap<String, String> result = WexinPayUtil.unifiedOrder(parameters);
        logger.debug("result--------------" + Objects.toString(result));
        if (!SUCCESS.equals(result.get(RETURN_CODE)) || !SUCCESS.equals(result.get(RESULT_CODE))) {
            return callbacks.onResult(false, result, result.get("return_msg"));
        }else{
            return callbacks.onResult(true, result, "微信下单成功");
        }
    }

    public void notify(HttpServletRequest request, HttpServletResponse response, String key, WeChatPayCallbacks callbacks) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            TreeMap<String, String> result = WexinPayUtil.parseXml(request.getInputStream());
            logger.debug("result--------------" + Objects.toString(result));
            if (!SUCCESS.equals(result.get(RETURN_CODE))) {
                callbacks.onResult(false, result, result.get("return_msg"));
                out.write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>".getBytes(UTF_8));
                return;
            }
            // 先进行校验，是否是微信服务器返回的信息
            String checkSign = WexinPayUtil.createSign(result, key);
            //签名校验成功
            if (checkSign.equals(result.get("sign"))) {
                callbacks.onResult(true, result, "签名校验成功，支付成功");
                out.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>".getBytes(UTF_8));
            } else {
                callbacks.onResult(false, result, "签名校验失败，微信返回签名：" + result.get("sign") + "，服务器参数签名：" + checkSign);
                out.write("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[回调失败]]></return_msg></xml>".getBytes(UTF_8));
            }
        } catch (Exception e) {
            logger.error("ERROR", e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }


}
