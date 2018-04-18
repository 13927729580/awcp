package cn.org.awcp.wechat.payment.controller;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.util.BeanUtil;
import cn.org.awcp.wechat.payment.domain.WeChatOrderInfo;
import cn.org.awcp.wechat.payment.service.WeChatPayService;
import cn.org.awcp.wechat.payment.util.IPHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

import static cn.org.awcp.wechat.payment.domain.WeChatOrderInfo.NO_CREDIT;
import static cn.org.awcp.wechat.payment.domain.WeChatOrderInfo.TRADE_TYPE_NATIVE;
import static cn.org.awcp.wechat.util.Constant.APPID;
import static cn.org.awcp.wechat.util.ConstantPay.KEY;
import static cn.org.awcp.wechat.util.ConstantPay.MCH_ID;

/**
 * 微信支付Controller
 *
 * @author venson
 * @version 20180410
 */
@RequestMapping(value = "/wechat")
@Controller
public class WeChatPayController {

    private final static Logger logger = LoggerFactory.getLogger(WeChatPayController.class);

    @Autowired
    private WeChatPayService weChatPayService;


    @Autowired
    private SzcloudJdbcTemplate jdbcTemplate;

    /**
     * 支付
     * @param request 请求
     * @param tradeType 支付类型
     * @param trainId 商品ID
     * @return
     */
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    @ResponseBody
    public Object pay(HttpServletRequest request,String tradeType, String trainId) {
        ReturnResult result = ReturnResult.get();
        Map<String, Object> train;
        try {
            train = jdbcTemplate.queryForMap("select title,content,teacher,fee from hxfr_train where id=?", trainId);
        } catch (DataAccessException e) {
            logger.debug("ERROR", e);
            return result.setStatus(StatusCode.FAIL.setMessage("数据没找到"));
        }
        String notify_url = ControllerHelper.getBasePath() + "wechat/pay/notify.do";
        String ipAddress = IPHelper.getIpAddr(request);
        String title = (String) train.get("title");
        double totalFee = (double) train.get("fee");
        long fee = Math.round(totalFee * 100);
        String outTradeNo = UUID.randomUUID().toString().replaceAll("-", "");
        WeChatOrderInfo orderInfo = new WeChatOrderInfo(APPID,tradeType, notify_url, outTradeNo, title, fee);
        //扫码支付类型
        if(TRADE_TYPE_NATIVE.equals(tradeType)){
            orderInfo.setProduct_id(trainId);
        }
        orderInfo.setMch_id(MCH_ID);
        orderInfo.setLimit_pay(NO_CREDIT);
        orderInfo.setSpbill_create_ip(ipAddress);
        return weChatPayService.pay(orderInfo, KEY, (isSuccess, params, message) -> {
            if (isSuccess) {
                String content = (String) train.get("content");
                String date = DocumentUtils.getIntance().today();
                PunUserBaseInfoVO user = DocumentUtils.getIntance().getUser();
                String phone = user.getMobile();
                String teacher = (String) train.get("teacher");
                String sql = "insert into hxfr_order(ID,user_id,train_id,title,content,totalFee,date,phone,teacher,createTime,type,state) "
                        + "values(?,?,?,?,?,?,?,?,?,?,'1','0')";
                jdbcTemplate.update(sql, outTradeNo, user.getUserId(), trainId, title, content, fee, date, phone, teacher, date);
                if(TRADE_TYPE_NATIVE.equals(tradeType)){
                    BeanUtil.removeMap(params,"sign","nonce_str","mch_id");
                    params.put("totalFee",totalFee+"");
                    params.put("outTradeNo",outTradeNo);
                    params.put("title",title);
                }else{
                    String prepay_id=params.get("prepay_id");
                    params.clear();
                    params.put("appId", APPID);
                    params.put("prepayId", prepay_id);
                }
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
    @RequestMapping(value = "/pay/notify")
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        weChatPayService.notify(request, response, KEY, (isSuccess, params, message) -> {
            if (isSuccess) {
                String sql = "update hxfr_order set state='1' where ID=? and state=0";
                jdbcTemplate.update(sql, params.get("out_trade_no"));
            } else {
                logger.error(message);
            }
            return null;
        });
    }


}
