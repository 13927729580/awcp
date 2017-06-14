package org.szcloud.framework.admin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/debug")
public class DebugLogController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	@RequestMapping("/view")
	public ModelAndView viewLog() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("devAdmin/mylog");
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		File file = new File(System.getProperty("catalina.base") + File.separator + "logs" + File.separator + "szcloud-"
				+ sdf1.format(date) + ".log");

		StringBuilder sb = new StringBuilder("<hr>");
		BufferedReader br = null;

		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");// 考虑到编码格式
			br = new BufferedReader(read);// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				if (sb.length() != 0) {
					if (s.length() > 4 && StringUtils.isNumeric(s.substring(0, 2)) && !s.contains("ERROR")) {
						sb.append("<pre class=\"text-success\">" + s + "</pre>");
					} else {
						sb.append("<pre class=\"text-danger\">" + s + "</pre>");
					}
				}

			}
		} catch (IOException e) {
			logger.info("ERROR", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.info("ERROR", e);
				}
			}
		}

		mv.addObject("logs", sb.toString());
		return mv;
	}

	@RequestMapping("/clear")
	public ModelAndView deleteLog() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/debug/view.do");
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		File file = new File(System.getProperty("catalina.base") + File.separator + "logs" + File.separator + "szcloud-"
				+ sdf1.format(date) + ".log");

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			logger.info("ERROR", e);
		}
		return mv;
	}
}
