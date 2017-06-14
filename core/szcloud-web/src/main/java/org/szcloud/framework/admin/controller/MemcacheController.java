package org.szcloud.framework.admin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.base.BaseController;
import org.szcloud.framework.formdesigner.utils.DocumentUtils;

@Controller
@RequestMapping("/cache")
public class MemcacheController extends BaseController {

	@ResponseBody
	@RequestMapping("/clear")
	public String clearCache(HttpServletResponse response,
			HttpServletRequest request) {
		String msg = "清理成功！";
		MemcachedClient mc = null;
		try {
			InputStream in = DocumentUtils.class.getClassLoader().getResourceAsStream("memcached.properties");
			Properties p = new Properties();
			p.load(in);
			String ips = p.getProperty("org.mybatis.caches.memcached.servers");
			List<InetSocketAddress> addresses =  AddrUtil.getAddresses(ips);
			mc = new MemcachedClient(addresses);
			mc.flush();			
		} catch (IOException e) {
			msg = "清理失败！";
			logger.info("ERROR", e);
		} finally {
			if( mc != null) {
				mc.shutdown();				
			}
		}
		return msg;
	}
}
