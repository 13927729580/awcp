package cn.org.awcp.wechat.payment.controller;

import java.io.BufferedOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.wechat.payment.domain.OrderInfo;
import cn.org.awcp.wechat.payment.service.WexinJsPayService;
import cn.org.awcp.wechat.payment.service.WexinWebPayService;
import cn.org.awcp.wechat.payment.util.IPHelper;
import cn.org.awcp.wechat.payment.util.WexinPayUtil;
import cn.org.awcp.wechat.util.ConstantPay;

/**
 * 微信支付Controller
 * 
 * @author Administrator
 *
 */
@RequestMapping(value = "/wxPay")
@Controller
public class WexinPaymentController {

	@Autowired
	private WexinWebPayService wexinPayService;

	@Autowired
	private WexinJsPayService wexinJsPayService;

	@Resource
	private SzcloudJdbcTemplate jdbcTemplate;

	/**
	 * 网页web扫码支付
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/webPay")
	public ModelAndView webPay(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("payment/wexin/wexinWebPay");

		String notify_url = ControllerHelper.getBasePath() + "wxPay/webNotify.do";
		Map<String, String> otherParams = new HashMap<String, String>();
		otherParams.put("notify_url", notify_url);
		String spbill_create_ip = IPHelper.getIpAddr(request);
		otherParams.put("spbill_create_ip", spbill_create_ip);
		String body = request.getParameter("body");
		String outTradeNo = DocumentUtils.getIntance().getNumber("CZ", 8);
		String totalFee = request.getParameter("totalFee");
		int fee = (int) (Double.parseDouble(totalFee) * 100);
		OrderInfo order = new OrderInfo(outTradeNo, body, outTradeNo, fee);
		PunUserBaseInfoVO user = DocumentUtils.getIntance().getUser();
		if ("1".equals(StringUtils.trim(user.getDeptId()))) {
			order.setTotalFee(1);
		}
		String codeUrl = wexinPayService.getCodeUrl(order, otherParams);

		String sql = "insert into awcp_order(ID,userId,totalFee,body,createTime,type) values(?,?,?,?,?,'pcRecharge')";
		jdbcTemplate.update(sql, outTradeNo, user.getUserId(), totalFee, body, new Date());

		mav.addObject("orderNo", outTradeNo);
		mav.addObject("subject", body);
		mav.addObject("totalAmount", totalFee);
		mav.addObject("payment_result", codeUrl);
		return mav;
	}

	/**
	 * web扫码支付回调接口
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/webNotify")
	public void webNotify(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = null; // 支付成功后，微信回调返回的信息
		try {
			map = WexinPayUtil.parseXml(request.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			SortedMap<String, String> parameters = new TreeMap<String, String>(); // 用于验签
			for (String keyValue : map.keySet()) {
				if (!"sign".equals(keyValue)) {
					parameters.put(keyValue, map.get(keyValue));
				}
			}
			String out_trade_no = map.get("out_trade_no");
			boolean config = true;
			if (StringUtils.isBlank(out_trade_no)) {
				config = false;
			}
			if (!"SUCCESS".equalsIgnoreCase(map.get("return_code"))
					|| !"SUCCESS".equalsIgnoreCase(map.get("result_code"))) {
				config = false;
			}
			if (config) {
				// 先进行校验，是否是微信服务器返回的信息
				String checkSign = WexinPayUtil.createSign(parameters, ConstantPay.KEY);
				if (checkSign.equals(map.get("sign"))) {// 如果签名和服务器返回的签名一致，说明数据没有被篡改过
					String sql = "select userId,totalFee,state from awcp_order where ID=?";
					Map<String, Object> order = jdbcTemplate.queryForMap(sql, out_trade_no);
					if ("0".equals(order.get("state"))) {
						jdbcTemplate.beginTranstaion();
						Long userId = (Long) order.get("userId"); // 用户ID
						Double totalFee = (Double) order.get("totalFee"); // 支付金额
						sql = "update awcp_order set state='1' where ID=?"; // 修改订单状态为1(已支付)
						jdbcTemplate.update(sql, out_trade_no);
						System.out.println("===============================|||||||||||||||||||||||||||||||||||||||");
					}
				}
			}
		} catch (Exception e) {
			jdbcTemplate.rollback();
		}
	}

	/**
	 * 公众号jsapi支付
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jsPay")
	public String jsPay(HttpServletRequest request) {
		String body = request.getParameter("body");
		String outTradeNo = request.getParameter("outTradeNo");
		String totalFee = request.getParameter("totalFee");
		String memberId = request.getParameter("memberId");
		if (memberId == null) {
			memberId = "";
		}
		long fee = Math.round(Double.parseDouble(totalFee) * 100);
		OrderInfo order = new OrderInfo(outTradeNo, body, memberId, fee);
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		if ("1".equals(StringUtils.trim(user.getDeptId()))) {
			order.setTotalFee(1);
		}
		Map<String, String> otherParams = new HashMap<String, String>();
		String notify_url = ControllerHelper.getBasePath() + "wxPay/jsPayNotify.do";
		otherParams.put("notify_url", notify_url);
		String spbill_create_ip = IPHelper.getIpAddr(request);
		otherParams.put("spbill_create_ip", spbill_create_ip);
		otherParams.put("openid", getUserOpenid(user.getUserId()));
		String json = wexinJsPayService.getResult(order, otherParams);
		return json;
	}

	private String getUserOpenid(long userId) {
		String sql = "select openid from awcp_user where user_id=?";
		return jdbcTemplate.queryForObject(sql, String.class, userId);
	}

	@ResponseBody
	@RequestMapping(value = "/jsPayNotify")
	public void jsPayNotify(HttpServletRequest request, HttpServletResponse response) {
		String resXml = "";
		Map<String, String> map = null; // 支付成功后，微信回调返回的信息
		try {
			map = WexinPayUtil.parseXml(request.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			jdbcTemplate.beginTranstaion();
			SortedMap<String, String> parameters = new TreeMap<String, String>(); // 用于验签
			for (String keyValue : map.keySet()) {
				if (!"sign".equals(keyValue)) {
					parameters.put(keyValue, map.get(keyValue));
				}
			}
			String out_trade_no = map.get("out_trade_no");
			String openid = map.get("openid");
			boolean config = true;
			if (StringUtils.isBlank(out_trade_no) || StringUtils.isBlank(openid)) {
				resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";
				config = false;
			}
			if ("FAIL".equalsIgnoreCase(map.get("result_code").toString())) {
				resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";
				config = false;
			}
			if (config) {
				// 先进行校验，是否是微信服务器返回的信息
				String checkSign = WexinPayUtil.createSign(parameters, ConstantPay.KEY);
				if (checkSign.equals(map.get("sign"))) {// 如果签名和服务器返回的签名一致，说明数据没有被篡改过
					onSuccess(map);
					resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg></xml>";
					System.out.println("===============================|||||||||||||||||||||||||||||||||||||||");
				} else {
					resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[回调失败]]></return_msg></xml>";
				}
			}
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
			jdbcTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
			jdbcTemplate.rollback();
		}
	}

	private void onSuccess(Map<String, String> map) {
		String sql = "select userId,totalFee,state,type from awcp_order where ID=?";
		String out_trade_no = map.get("out_trade_no"); // 订单ID
		Map<String, Object> order = jdbcTemplate.queryForMap(sql, out_trade_no);
		Long userId = (Long) order.get("userId"); // 用户ID
		Double totalFee = (Double) order.get("totalFee"); // 支付金额
		String state = (String) order.get("state"); // 支付状态(0:未支付,1:已支付)
		String type = (String) order.get("type"); // 类型
		if ("0".equals(state)) {// 订单还未处理
			sql = "update awcp_order set state='1' where ID=?";
			jdbcTemplate.update(sql, out_trade_no);
			if ("developer".equals(type)) {// 程序员升级vip
				String memberId = map.get("attach"); // 程序员vip等级(1:钻石,2:黄金,3:白银)
				sql = "select count(*) from awcp_dev_member_user where user_id=?";
				int count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
				if (count == 1) {
					sql = "update awcp_dev_member_user set member_id=?,createTime=?,pay_state=1,pay_amount=pay_amount+"
							+ totalFee + " where user_id=?";
					jdbcTemplate.update(sql, memberId, new Date(), userId);
				} else if (count == 0) {
					sql = "insert into awcp_dev_member_user(user_id,member_id,pay_amount,pay_state,createTime) values(?,?,?,1,?)";
					jdbcTemplate.update(sql, userId, memberId, totalFee, new Date());
				}
			} else if ("company".equals(type)) {// 企业升级vip
				sql = "select count(*) from awcp_vip_company where user_id=?";
				int count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
				if (count == 1) {
					sql = "update awcp_vip_company set pay_time=?,pay_state=1,pay_amount=pay_amount+" + totalFee
							+ " where user_id=?";
					jdbcTemplate.update(sql, new Date(), userId);
				} else if (count == 0) {
					sql = "insert into awcp_vip_company(user_id,pay_amount,pay_state,pay_time) values(?,?,1,?)";
					jdbcTemplate.update(sql, userId, totalFee, new Date());
				}
			} else if ("mbRecharge".equals(type)) {// 平台充值
			}
		}
	}
}
