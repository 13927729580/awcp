package cn.org.awcp.wechat.payment.controller;


import cn.org.awcp.venson.controller.base.BaseController;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.util.PlatfromProp;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static cn.org.awcp.wechat.payment.domain.WeChatOrderInfo.TRADE_TYPE_NATIVE;
/**
 * 支付宝支付Controller
 *
 * @author venson
 * @version 20180424
 */
@Controller
@RequestMapping("/")
public class AlipayController extends BaseController {

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AlipayTradeAppPayRequest alipayTradeAppPayRequest;
    @Autowired
    private AlipayTradePagePayRequest alipayTradePagePayRequest;



    @RequestMapping(value = "ali/pay",method = RequestMethod.POST)
    @ResponseBody
    public Object app(String tradeType,String type, String id) throws Exception {
        ReturnResult result = ReturnResult.get();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", System.currentTimeMillis());
        bizContent.put("total_amount", "8888");
        bizContent.put("subject", "苹果X 256G");
        AlipayResponse response;
        if (TRADE_TYPE_NATIVE.equals(tradeType)) {
            bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
            alipayTradePagePayRequest.setBizContent(bizContent.toString());
            response = alipayClient.pageExecute(alipayTradePagePayRequest);
        } else {
            bizContent.put("product_code", "QUICK_MSECURITY_PAY");
            alipayTradeAppPayRequest.setBizContent(bizContent.toString());
            response = alipayClient.sdkExecute(alipayTradeAppPayRequest);
        }
        if (response.isSuccess()) {
            return result.setData(response.getBody());
        } else {
            return result.setStatus(StatusCode.FAIL).setMessage("下单失败：" + response.getMsg());
        }
    }


    @RequestMapping("anon/ali/pay/notify")
    @ResponseBody
    public Object notify(HttpServletRequest request) throws Exception {
        Map<String, String> params = mapObj2Str(wrapMap(request.getParameterMap()));
        // 验签
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                PlatfromProp.getValue("alipay.alipay_public_key"),
                PlatfromProp.getValue("alipay.charset"),
                PlatfromProp.getValue("alipay.sign_type"));
        //签名校验失败
        if (!signVerified) {
            return "failed";
        }
        //交易成功
        if("TRADE_SUCCESS".equals(params.get("trade_status"))){
            String out_trade_no=params.get("out_trade_no");
            return "success";
        }
        return "failed";
    }


    @RequestMapping("alipay/returnUrl")
    @ResponseBody
    public Object returnUrl(){
        return "success";
    }

}