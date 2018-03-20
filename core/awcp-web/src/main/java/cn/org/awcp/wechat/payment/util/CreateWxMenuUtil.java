package cn.org.awcp.wechat.payment.util;

import cn.org.awcp.wechat.domain.menu.WeChatButton;
import cn.org.awcp.wechat.domain.menu.WeChatMenu;
import cn.org.awcp.wechat.util.ConstantURL;
import cn.org.awcp.wechat.util.WeChatUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static cn.org.awcp.wechat.util.Constant.APPID;

public class CreateWxMenuUtil {

	public static void createMenu() throws UnsupportedEncodingException {
		WeChatUtil.delMenu();
		// WeChatUtil.createMenu(getQzwyMenu());
		// WeChatUtil.createMenu(getTestMenu());
		WeChatUtil.createMenu(getAWCPMenu());
	}

	public static WeChatMenu getQzwyMenu() throws Exception {
		String timestamp = System.currentTimeMillis() + "";
		// snsapi_base(不弹出授权页面,直接跳转,只能获取用户openid);snsapi_userinfo(弹出授权页面,可通过openid拿到昵称,性别,所在地)
		String url1 = URLEncoder.encode("http://www.maxic.cn/qzwy/index.do", "utf-8");
		url1 = ConstantURL.AUTHORIZE_URL.replace("APPID", APPID).replace("REDIRECT_URI", url1)
				.replace("SCOPE", "snsapi_base").replace("STATE", timestamp);
		String url2 = "http://www.maxic.cn/qzwy/register.do";
		url2 = ConstantURL.AUTHORIZE_URL.replace("APPID", APPID).replace("REDIRECT_URI", url2)
				.replace("SCOPE", "snsapi_base").replace("STATE", timestamp);
		String url3 = URLEncoder.encode("http://www.maxic.cn/qzwy/userCenter.do", "utf-8");
		url3 = ConstantURL.AUTHORIZE_URL.replace("APPID", APPID).replace("REDIRECT_URI", url3)
				.replace("SCOPE", "snsapi_base").replace("STATE", timestamp);
		System.out.println(url1 + "\n" + url2 + "\n" + url3);
		WeChatMenu menu = new WeChatMenu();
		List<WeChatButton> button = new ArrayList<WeChatButton>();
		button.add(new WeChatButton("view", "活动报名", null, url1, null, null));
		List<WeChatButton> subBtn1 = new ArrayList<WeChatButton>();
		subBtn1.add(new WeChatButton("view", "会员中心", null, url3, null, null));
		subBtn1.add(new WeChatButton("view", "会员注册", null, url2, null, null));
		button.add(new WeChatButton(null, "会员中心", null, null, null, subBtn1));
		menu.setButton(button);
		return menu;
	}

	public static WeChatMenu getTestMenu() {
		WeChatMenu menu = new WeChatMenu();
		List<WeChatButton> button = new ArrayList<WeChatButton>();
		button.add(new WeChatButton("view", "首页", null, "http://www.maxic.cn/awcp/index.html", null, null));
		menu.setButton(button);
		return menu;
	}

	public static WeChatMenu getAWCPMenu() throws UnsupportedEncodingException {
		WeChatMenu menu = new WeChatMenu();
		String timestamp = System.currentTimeMillis() + "";
		List<WeChatButton> button = new ArrayList<WeChatButton>();
		String url1 = URLEncoder.encode("https://www.awcp.org.cn/awcp/index.html", "utf-8");
		url1 = ConstantURL.AUTHORIZE_URL.replace("APPID", APPID).replace("REDIRECT_URI", url1)
				.replace("SCOPE", "snsapi_base").replace("STATE", timestamp);
		String url2 = "https://www.awcp.org.cn/awcp/projectHall.html";
		url2 = ConstantURL.AUTHORIZE_URL.replace("APPID", APPID).replace("REDIRECT_URI", url2)
				.replace("SCOPE", "snsapi_base").replace("STATE", timestamp);
		String url3 = URLEncoder.encode("https://www.awcp.org.cn/awcp/platformIntro.html", "utf-8");
		url3 = ConstantURL.AUTHORIZE_URL.replace("APPID", APPID).replace("REDIRECT_URI", url3)
				.replace("SCOPE", "snsapi_base").replace("STATE", timestamp);
		button.add(new WeChatButton("view", "核码平台", null, url1, null, null));
		button.add(new WeChatButton("view", "任务大厅", null, url2, null, null));
		button.add(new WeChatButton("view", "平台简介", null, url3, null, null));
		menu.setButton(button);
		return menu;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(getAWCPMenu());
	}
}
