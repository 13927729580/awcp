package cn.org.awcp.wechat.payment.controller;

import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.util.PlatfromProp;
import cn.org.awcp.wechat.payment.domain.WeChatOrderInfo;
import cn.org.awcp.wechat.payment.service.WeChatPayService;
import cn.org.awcp.wechat.payment.util.IPHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.org.awcp.wechat.payment.domain.WeChatOrderInfo.TRADE_TYPE_NATIVE;

/**
 * 微信支付Controller
 *
 * @author venson
 * @version 20180410
 */
@RequestMapping(value = "/")
@Controller
public class WeChatPayController {

    private final static Logger logger = LoggerFactory.getLogger(WeChatPayController.class);

    @Autowired
    private WeChatPayService weChatPayService;


    /**
     * 支付
     * @param request 请求
     * @param tradeType 支付类型
     * @param type 附加数据
     * @param id ID
     * @return
     */
    @RequestMapping(value = "wechat/pay", method = RequestMethod.POST)
    @ResponseBody
    public Object pay(HttpServletRequest request,String tradeType,String type,String id) {
        ReturnResult result = ReturnResult.get();
        String ipAddress = IPHelper.getIpAddr(request);
        String outTradeNo = System.currentTimeMillis()+"";
        WeChatOrderInfo orderInfo = new WeChatOrderInfo(PlatfromProp.getValue("wechatpay.appid"),tradeType, PlatfromProp.getValue("wechatpay.notifyUrl"), outTradeNo, "苹果X 256G", 100);
        //扫码支付类型
        if(TRADE_TYPE_NATIVE.equals(tradeType)){
            orderInfo.setProduct_id(id);
        }
        orderInfo.setMch_id(PlatfromProp.getValue("wechatpay.mchid"));
        orderInfo.setSpbill_create_ip(ipAddress);
        return weChatPayService.pay(orderInfo, PlatfromProp.getValue("wechatpay.mchkey"), (isSuccess, params, message) -> {
            if (isSuccess) {
                result.setStatus(StatusCode.SUCCESS).setData(params);
            } else {
                result.setStatus(StatusCode.FAIL.setMessage(message));
                logger.error(message);
            }
            return result;
        });

    }


    /**
     * 支付回调接口
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "anon/wechat/pay/notify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        weChatPayService.notify(request, response, PlatfromProp.getValue("wechatpay.mchkey"), (isSuccess, params, message) -> {
            if (isSuccess) {
                String out_trade_no= params.get("out_trade_no");
            } else {
                logger.error(message);
            }
            return null;
        });
    }

}
