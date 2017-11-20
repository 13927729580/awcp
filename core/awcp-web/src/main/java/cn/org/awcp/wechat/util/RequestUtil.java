package cn.org.awcp.wechat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送请求,获取响应
 * @author yqtao
 *
 */
public class RequestUtil {

	private static Logger logger = LoggerFactory.getLogger(RequestUtil.class);
	
	/**
	 * doGet请求
	 * @param requestUrl
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String doGet(String requestUrl) throws URISyntaxException, ClientProtocolException, IOException{
		//requestUrl必须经过处理,否则有异常
		URL url = new URL(requestUrl);
		URI uri = new URI(url.getProtocol(), url.getHost() + ":" + url.getPort(),url.getPath(), url.getQuery(), null);
		logger.info("requestUrl:"+requestUrl);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String responseContent = null; // 响应内容
		HttpEntity entity = response.getEntity();
		responseContent = EntityUtils.toString(entity, "UTF-8");
		logger.info("响应json:"+responseContent);
		return responseContent;
	}
	
	/**
	 * doPost请求
	 * @param requestUrl
	 * @param json
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String doPost(String requestUrl,String json) throws URISyntaxException, ClientProtocolException, IOException{
		URL url = new URL(requestUrl);
		URI uri = new URI(url.getProtocol(), url.getHost() + ":" + url.getPort(),url.getPath(), url.getQuery(), null);
		logger.info("requestUrl:"+requestUrl);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		httpPost.addHeader("Content-type","application/json; charset=utf-8");  
		httpPost.setHeader("Accept", "application/json"); 
		httpPost.setEntity(new StringEntity(json, Charset.forName("UTF-8")));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		String responseContent = null; // 响应内容
		HttpEntity entity = response.getEntity();
		responseContent = EntityUtils.toString(entity, "UTF-8");
		logger.info("响应json:"+responseContent);
		return responseContent;
	}
	
	/** 
	 * 获取媒体文件 
	 * @param media_id 媒体文件id 
	 * @throws IOException 
	 * @throws UnsupportedOperationException 
	 * @throws URISyntaxException 
	 * 
	 */
	public static void downloadMedia(String requestUrl,String fileName) 
			throws UnsupportedOperationException, IOException, URISyntaxException { 
		URL url = new URL(requestUrl);
		URI uri = new URI(url.getProtocol(), url.getHost() + ":" + url.getPort(),url.getPath(), url.getQuery(), null);
		logger.info("requestUrl:"+requestUrl);
		CloseableHttpClient httpclient = HttpClients.createDefault();  
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);  
        HttpEntity entity = response.getEntity();  
        InputStream in = entity.getContent();  
        File file = new File(fileName);  
        try {  
            FileOutputStream out = new FileOutputStream(file);  
            int len = -1;  
            byte[] tmp = new byte[1024];  
            while ((len = in.read(tmp)) != -1) {  
            	out.write(tmp, 0, len);  // 注意这里如果用OutputStream.write(buff)的话,图片会失真,大家可以试试.
            }  
            out.flush();  
            out.close();  
        } finally {   
            in.close();  // 关闭低层流.
        }  
        httpclient.close(); 
	}
}
