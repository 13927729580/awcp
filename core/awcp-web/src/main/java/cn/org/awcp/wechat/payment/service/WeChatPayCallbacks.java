package cn.org.awcp.wechat.payment.service;

import java.util.TreeMap;

/**
 * 微信回调
 *
 * @author venson
 * @version 20180410
 */
@FunctionalInterface
public interface WeChatPayCallbacks {

    /**
     * 回调结果
     *
     * @param isSuccess 成功或失败
     * @param params    参数
     * @param message   返回信息
     */
    Object onResult(boolean isSuccess, TreeMap<String, String> params, String message);

}
