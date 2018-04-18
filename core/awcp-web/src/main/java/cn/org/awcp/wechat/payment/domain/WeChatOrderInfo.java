package cn.org.awcp.wechat.payment.domain;

/**
 * 订单信息
 *
 * @author Administrator
 */
public class WeChatOrderInfo {

    public final static String NO_CREDIT="no_credit";
    /**
     * 交易类型 APP支付
     */
    public final static String TRADE_TYPE_APP="APP";
    /**
     * 交易类型 公众号或小程序支付
     */
    public final static String TRADE_TYPE_JSAPI="JSAPI";
    /**
     * 交易类型 扫码支付
     */
    public final static String TRADE_TYPE_NATIVE="NATIVE";
    //APPID
    private String appid;
    //子商户APPID
    private String sub_appid;
    //商户ID
    private String mch_id;
    //子商户ID
    private String sub_mch_id;
    //商品订单号
    private String out_trade_no;
    //商品描述
    private String body;
    //交易类型
    private String trade_type;
    private String attach;
    //标价金额(分)
    private long total_fee;
    //客户端IP
    private String spbill_create_ip;
    //回调地址
    private String notify_url;
    //openId
    private String openid;
    //产品ID
    private String product_id;
    //随机数
    private String nonce_str;
    //排除的支付方式
    private String limit_pay;



    public WeChatOrderInfo(String appid,String trade_type, String notify_url,String out_trade_no, String body, long total_fee) {
        this.appid=appid;
        this.trade_type=trade_type;
        this.notify_url=notify_url;
        this.out_trade_no = out_trade_no;
        this.body = body;
        this.total_fee = total_fee;
    }


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSub_appid() {
        return sub_appid;
    }

    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public long getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(long total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getLimit_pay() {
        return limit_pay;
    }

    public void setLimit_pay(String limit_pay) {
        this.limit_pay = limit_pay;
    }
}
