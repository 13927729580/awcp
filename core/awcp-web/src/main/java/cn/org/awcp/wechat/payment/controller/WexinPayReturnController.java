package cn.org.awcp.wechat.payment.controller;

import static cn.org.awcp.wechat.util.Constant.APPID;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.wechat.payment.util.WexinPayUtil;
import cn.org.awcp.wechat.util.ConstantPay;

/**
 * 微信支付退款Controller
 *
 * @author Administrator
 */
@SuppressWarnings("deprecation")
@RequestMapping(value = "/wxPay")
@Controller
public class WexinPayReturnController {

    @Autowired
    private SzcloudJdbcTemplate jdbcTemplate;

    private static final String CRET_PATH = "/opt/program/servers/apache-tomcat-awcp/webapps/ROOT/WEB-INF/cret/apiclient_cert.p12";

    /**
     * 退款
     *
     * @param refundID：订单ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/payReturn")
    public Map<String, Object> payReturn(String refundID) {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("msg", "0");
        String sql = "select t3.totalFee,t3.ID,t2.ID as depositID from awcp_refund t1 join awcp_cash_deposit t2 "
                + "on t2.user_id=t1.user_id and t2.role_id=t1.role_id join awcp_order t3 on t2.ID=t3.ID  "
                + "where t1.state='0' and t2.pay_state='1' and t3.state='1' and t1.ID=?";
        try {
            Map<String, Object> order = jdbcTemplate.queryForMap(sql, refundID);
            String depositID = order.get("depositID") + "";
            String out_trade_no = order.get("ID") + "";                    //订单号
            String price = (int) ((double) order.get("totalFee") * 100) + "";        //价钱
            String appid = APPID;                //普通商户的微信公众号AppID
            String key = ConstantPay.KEY;        //普通商户的商户号秘钥
            String mch_id = ConstantPay.MCH_ID;    //普通商户的商户号
            String out_refund_no = UUID.randomUUID().toString().replaceAll("-", "");
            TreeMap<String, String> parameters = new TreeMap<String, String>();
            parameters.put("appid", appid);
            parameters.put("mch_id", mch_id);
            parameters.put("nonce_str", getNoncestr());
            parameters.put("out_trade_no", out_trade_no);
            parameters.put("out_refund_no", out_refund_no);
            parameters.put("total_fee", price);        //订单金额,单位为分
            parameters.put("refund_fee", price);        //退款金额,单位为分
            parameters.put("op_user_id", mch_id);
            String sign = WexinPayUtil.createSign(parameters, key);
            parameters.put("sign", sign);

            String reuqestXml = WexinPayUtil.getRequestXml(parameters);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(CRET_PATH));//放退款证书的路径
            try {
                keyStore.load(instream, mch_id.toCharArray());
            } finally {
                instream.close();
            }

            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            try {
                HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");//退款接口
                StringEntity reqEntity = new StringEntity(reuqestXml);
                reqEntity.setContentType("application/x-www-form-urlencoded");    // 设置类型
                httpPost.setEntity(reqEntity);
                CloseableHttpResponse response = httpclient.execute(httpPost);
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String responseContent = EntityUtils.toString(entity, "UTF-8");
                        InputStream is = new ByteArrayInputStream(responseContent.getBytes());
                        Map<String, String> result = WexinPayUtil.parseXml(is);
                        if ("SUCCESS".equalsIgnoreCase(result.get("return_code"))) {
                            if ("SUCCESS".equalsIgnoreCase(result.get("result_code"))) {
                                jdbcTemplate.beginTranstaion();
                                sql = "update awcp_refund set state='1' where ID=?";
                                jdbcTemplate.update(sql, refundID);
                                sql = "delete from awcp_cash_deposit where ID=?";
                                jdbcTemplate.update(sql, depositID);
                                sql = "update awcp_order set state='2' where ID=?";
                                jdbcTemplate.update(sql, out_refund_no);
                                ret.put("msg", "1");
                                jdbcTemplate.commit();
                            } else {
                                ret.put("msg", result.get("err_code_des"));
                            }
                        }
                    }
                    EntityUtils.consume(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                    jdbcTemplate.rollback();
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpclient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }


}
