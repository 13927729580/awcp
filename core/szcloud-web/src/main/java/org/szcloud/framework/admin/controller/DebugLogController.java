package org.szcloud.framework.admin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.formdesigner.core.domain.Calendar;
import org.szcloud.framework.formdesigner.core.domain.design.context.component.SimpleComponent;

@Controller
@RequestMapping("/debug")
public class DebugLogController {
	@RequestMapping("/view")
	public ModelAndView viewLog( @CookieValue(value="JSESSIONID") String sessionId) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("devAdmin/mylog");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		File file = new File(System.getProperty("catalina.base")
				+ File.separator + "logs" + File.separator
				+ "szcloud-"+sdf1.format(date) + ".log");
		StringBuilder sb = new StringBuilder("<hr>");
		BufferedReader br = null;

		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					file), "utf-8");// 考虑到编码格式
			br = new BufferedReader(read);// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				if (sb.length() != 0) {
					if(s.length() > 4 && s.substring(0,4).equals(sdf.format(date))){
						sb.append("<pre class=\"text-success\">"+s +"</pre>");
					} else if(s.length() > 2 && s.substring(0,2).equals("\t\t")){
						sb.append("<pre class=\"text-success\">"+s +"</pre>");
					} else{
						sb.append("<pre class=\"text-danger\">"+s +"</pre>");
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		mv.addObject("logs", sb.toString());
		return mv;
	}

	@RequestMapping("/clear")
	public ModelAndView deleteLog(@CookieValue(value="JSESSIONID") String sessionId) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/debug/view.do");
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		File file = new File(System.getProperty("catalina.base")
				+ File.separator + "logs" + File.separator
				+"szcloud-"+sdf1.format(date) + ".log");

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mv;
	}
}
