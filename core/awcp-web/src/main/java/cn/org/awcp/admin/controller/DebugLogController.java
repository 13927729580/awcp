package cn.org.awcp.admin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.org.awcp.core.utils.DateUtils;
import cn.org.awcp.venson.controller.base.ControllerHelper;

/**
 * 后台展示日志
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/debug")
public class DebugLogController {

	/**
	 * 展示日志
	 * 
	 * @return
	 */
	@RequestMapping("/view")
	public ModelAndView viewLog() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("devAdmin/mylog");
		String filePath = System.getProperty("catalina.base") + File.separator + "logs" + File.separator + "catalina."
				+ DateUtils.format(new Date(), "yyyy-MM-dd") + ".log";
		String result = "<hr/>" + randomAccessReadFile(filePath);
		mv.addObject("logs", result);
		return mv;
	}

	private String randomAccessReadFile(String filePath) {
		RandomAccessFile rf = null;
		StringBuffer sb = new StringBuffer();
		try {
			rf = new RandomAccessFile(filePath, "r"); // r:可读,w：可写
			long len = rf.length(); // 文件长度
			if (len == 0) {
				return "";
			}
			long nextend = len - 1; // 指针是从0到length-1
			String line = null;
			int c = -1;
			int i = 400000;
			rf.seek(nextend); // seek到最后一个字符
			while (nextend >= 0 && i >= 0) {
				c = rf.read();
				// 只有行与行之间才有\r\n，这表示读到每一行上一行的末尾的\n，而执行完read后， 指针指到了这一行的开头字符
				if (c == '\n') {
					line = rf.readLine();
					if (line != null) {
						// RandomAccessFile的readLine方法读取文本为ISO-8859-1，需要转化为utf-8
						line = new String(line.getBytes("ISO-8859-1"), "gbk");
						if (line.length() > 4 && StringUtils.isNumeric(line.substring(0, 2))
								&& !line.contains("ERROR")) {
							sb.insert(0, "<pre class=\"text-success\">" + line + "</pre>");
						} else {
							sb.insert(0, "<pre class=\"text-danger\">" + line + "</pre>");
						}
					}
				}
				// 这一句必须在这个位置，如果在nextend--后，那么导致进0循环后去seek-1索引，报异常
				// 如果在read()以前，那么导致进入0循环时，因为read指针到1，第一行少读取一个字符
				rf.seek(nextend);
				if (nextend == 0) {// 当文件指针退至文件开始处，输出第一行
					line = rf.readLine();
					if (line != null) {
						// RandomAccessFile的readLine方法读取文本为ISO-8859-1，需要转化为utf-8
						line = new String(line.getBytes("ISO-8859-1"), "gbk");
						if (line.length() > 4 && StringUtils.isNumeric(line.substring(0, 2))
								&& !line.contains("ERROR")) {
							sb.insert(0, "<pre class=\"text-success\">" + line + "</pre>");
						} else {
							sb.insert(0, "<pre class=\"text-danger\">" + line + "</pre>");
						}
					}
				}
				i--;
				nextend--;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(rf);
		}
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private String readFile(String filePath) {
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(filePath), "utf-8");// 考虑到编码格式
			br = new BufferedReader(read);// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				if (s.length() > 4 && StringUtils.isNumeric(s.substring(0, 2)) && !s.contains("ERROR")) {
					sb.append("<pre class=\"text-success\">" + s + "</pre>");
				} else {
					sb.append("<pre class=\"text-danger\">" + s + "</pre>");
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
		if (sb.length() > 200000) {
			sb.substring(sb.length() - 200000);
			sb.substring(sb.indexOf("<pre class=\"text-success\">"));
		}
		return sb.toString();
	}

	/**
	 * 下载日志
	 * 
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/downloadLog")
	public String downloadLog(HttpServletResponse response) {
		String date = DateUtils.format(new Date(), "yyyy-MM-dd");
		String filePath = System.getProperty("catalina.base") + File.separator + "logs" + File.separator + "catalina."
				+ date + ".log";
		String fileName = "catalina." + date + ".log";
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		if (is != null) {
			int i;
			try {
				response.setContentType("application/x-msdownload;");
				response.setHeader("Content-disposition",
						"attachment; filename=" + ControllerHelper.processFileName(fileName));
				OutputStream out = response.getOutputStream(); // 读取文件流
				i = 0;
				byte[] buffer = new byte[4096];
				while ((i = is.read(buffer)) != -1) { // 写文件流
					out.write(buffer, 0, i);
				}
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 清空日志
	 */
	@RequestMapping("/clear")
	public ModelAndView deleteLog() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/debug/view.do");
		String date = DateUtils.format(new Date(), "yyyy-MM-dd");
		File file = new File(System.getProperty("catalina.base") + File.separator + "logs" + File.separator + "catalina."
				+ date + ".log");
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
