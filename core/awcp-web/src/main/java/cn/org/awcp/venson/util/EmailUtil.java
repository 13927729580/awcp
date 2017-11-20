package cn.org.awcp.venson.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmailUtil {
	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(EmailUtil.class);

	private EmailUtil() {
	}

	public static void main(String[] args) {
		sendVerificationEmail("744026144@qq.com");
	}

	/**
	 * 发送验证码邮件
	 * 
	 * @param email
	 *            接收人邮箱
	 */
	public static String sendVerificationEmail(String email) {
		String randomVcode = SMSUtil.createRandomVcode();
		String content = "【" + PlatfromProp.getValue("smsCompany") + "】验证码：" + randomVcode + "，如非本人操作，请忽略本邮件。";
		sendEmail(EMailType._QQ, email, "Verification", content);
		logger.debug("EMailCode------------------------" + randomVcode);
		return randomVcode;
	}

	/**
	 * 
	 * @param email
	 *            接收人邮箱
	 * @param subject
	 *            标题
	 * @param content
	 *            内容
	 */
	public static void sendEmail(EMailType type, String email, String subject, String content) {
		sendEmail(PlatfromProp.getValue("emailUser"), PlatfromProp.getValue("emailPwd"), type, email,
				PlatfromProp.getValue("emailUserAccount"), subject, content);
	}

	/**
	 * @param user
	 *            发件人的用户名
	 * @param password
	 *            授权码
	 * @param to
	 *            收件人电子邮箱
	 * @param from
	 *            发件人电子邮箱
	 * @param host
	 *            指定发送邮件的主机为
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	public static void sendEmail(final String user, final String password, EMailType type, String to, String from,
			String subject, String content) {
		Properties properties = System.getProperties(); // 获取系统属性
		// 设置邮件服务器
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.host", type.getHost());
		properties.put("mail.smtp.port", type.getPort());
		// 获取默认session对象
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session); // 创建默认的 MimeMessage 对象
			message.setFrom(new InternetAddress(from)); // Set From: 头部头字段
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // Set To: 头部头字段
			message.setSubject(subject); // Set Subject: 头部头字段
			message.setText(content); // 设置消息体
			Transport.send(message); // 发送消息
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	static enum EMailType {
		_QQ(587, "smtp.qq.com"), _163(25, "smtp.163.com");
		private String host;
		private int port;

		private EMailType(int port, String host) {
			this.host = host;
			this.port = port;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}
}
