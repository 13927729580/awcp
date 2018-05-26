package cn.org.awcp.venson.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 用于模拟HTTP请求中GET/POST方式
 * 
 * @author venson
 *
 */
public class HttpUtils {
	/**
	 * 日志对象
	 */
	protected static final Log logger = LogFactory.getLog(HttpUtils.class);

	/**
	 * 发送GET请求
	 * 
	 * @param url
	 *            目的地址
	 * @return 远程响应结果
	 */
	public static String sendGet(String url) {
		return sendGet(url, null);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            目的地址
	 * @return 远程响应结果
	 */
	public static String sendPost(String url) {
		return sendPost(url, null);
	}

	/**
	 * 发送GET请求
	 * 
	 * @param url
	 *            目的地址
	 * @param parameters
	 *            请求参数，Map类型。
	 * @return 远程响应结果
	 */
	public static String sendGet(String url, Map<String, Object> parameters) {
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		try {
			String parameter=getStringParameter(parameters);
			if(!parameter.isEmpty()){
				if (url.contains("?")) {
					url = url + "&" + parameter;
				} else {
					url = url + "?" + parameter;
				}
			}
			// 创建URL对象
			URL connURL = new URL(url);
			// 打开URL连接
			HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
			// 设置通用属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// 建立实际的连接
			httpConn.connect();
			// 定义BufferedReader输入流来读取URL的响应,并设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return result.toString();
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            目的地址
	 * @param parameters
	 *            请求参数，Map类型。
	 * @return 远程响应结果
	 */
	public static String sendPost(String url, Map<String, Object> parameters) {
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			// 创建URL对象
			URL connURL = new URL(url);
			// 打开URL连接
			HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
			// 设置通用属性
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			// 设置POST方式
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			// 发送请求参数
			out.write(getStringParameter(parameters));
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，设置编码方式
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			String line;
			// 读取返回的内容
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			logger.info("ERROR", e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
		return result.toString();
	}

	private static String getStringParameter(Map<String, Object> parameters) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		if (parameters != null && parameters.size() > 0) {
            // 编码请求参数
            if (parameters.size() == 1) {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=")
                            .append(URLEncoder.encode(String.valueOf(parameters.get(name)), "UTF-8"));
                }
            } else {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=")
                            .append(URLEncoder.encode(String.valueOf(parameters.get(name)), "UTF-8")).append("&");
                }
				sb.delete(sb.length()-1,sb.length());
            }
        }
		return sb.toString();
	}

	/**
	 * * 从网络Url中下载文件
	 * @param address 下载地址
	 * @return byte数据
	 */
	public static byte[] downLoad(String address){
		InputStream inputStream=null;
		try {
			URL url = new URL(address);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(3*1000);
			conn.setReadTimeout(60*1000);
			//防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
			//得到输入流
			inputStream = conn.getInputStream();
			if(inputStream==null){
                return null;
            }else{
                return IOUtils.toByteArray(inputStream);
            }
		} catch (IOException e) {
			logger.debug("ERROR",e);
			return null;
		} finally {
			IOUtils.closeQuietly(inputStream);

		}

	}


}
