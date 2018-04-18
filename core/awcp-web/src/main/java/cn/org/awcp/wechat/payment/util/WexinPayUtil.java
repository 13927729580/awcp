package cn.org.awcp.wechat.payment.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import cn.org.awcp.venson.util.MD5Util;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.CharEncoding.UTF_8;


public class WexinPayUtil {

    private final static Logger logger = LoggerFactory.getLogger(WexinPayUtil.class);

    private final static String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private final static String queryWxOrder = "https://api.mch.weixin.qq.com/pay/orderquery";


    public static TreeMap<String, String> unifiedOrder(Map<String, String> params) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(createOrderURL);
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + UTF_8);
            httpPost.addHeader("Host", "api.mch.weixin.qq.com");
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpPost.addHeader("Cache-Control", "max-age=0");
            httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            String xml=getRequestXml(params);
            logger.debug(xml);
            httpPost.setEntity(new StringEntity(xml, UTF_8));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseContent = EntityUtils.toString(entity, UTF_8);
            logger.debug(responseContent);
            InputStream is = new ByteArrayInputStream(responseContent.getBytes(UTF_8));
            return parseXml(is);
        } catch (IOException e) {
            logger.error("ERROR",e);
            return null;
        }
    }


    /**
     * 解析微信发来的请求（XML）
     *
     * @param is 流
     */
    @SuppressWarnings("unchecked")
    public static TreeMap<String, String> parseXml(InputStream is) {
        try {
            // 将解析结果存储在HashMap中
            TreeMap<String, String> map = new TreeMap<>();
            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(is);
            // 得到xml根元素
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
            // 释放资源
            return map;
        } catch (DocumentException e) {
            logger.error("ERROR",e);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 获取10位随机数
     */
    public static String getNonceStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currTime = sdf.format(date);
        //取时间部分
        String strTime = currTime.substring(8, currTime.length());
        Random random = new Random();
        // 四位随机数
        String strRandom = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
        return strTime + strRandom;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     *
     * @param params 参数
     * @param key APPId
     */
    public static String createSign(TreeMap<String, String> params, String key) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry: params.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);
        return new Md5Hash(sb.toString()).toString().toUpperCase();
    }

    /**
     * 将请求参数转换为xml格式的string
     *
     * @param params 请求参数
     */
    public static String getRequestXml(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry: params.entrySet()) {
            String k =  entry.getKey();
            Object v =  entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }



}
