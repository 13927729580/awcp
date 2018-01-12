package cn.org.awcp.unit.message;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.util.DateFormaterUtil;

@Component("punNotificationService")
public class PunNotificationService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean pushNotify(String title, String content, String type, String url, String receiver, String... way) {

		type = StringUtils.defaultString(type, "system");
		String sql = "insert into p_un_notification(ID,TITLE,CONTENT,TYPE,MSG_URL,CREATE_TIME,CREATOR,CREATE_NAME,STATE) values(?,?,?,?,?,?,?,?,'1')";
		String msgId = UUID.randomUUID().toString();
		String createTime = DateFormaterUtil.dateToString(DateFormaterUtil.FORMART4, new Date());
		PunUserBaseInfoVO user = ControllerHelper.getUser();
		if (jdbcTemplate.update(sql, msgId, title, content, type, url, createTime, user.getUserId(),
				user.getName()) == 1 && this.pushNotifyUser(msgId, receiver)) {
			// 消息推送
			PunNotification notification = new PunNotification();
			notification.setContent(content);
			notification.setCreateTime(createTime);
			notification.setCreateName(user.getName());
			notification.setId(msgId);
			notification.setTitle(title);
			notification.setType(type);
			notification.setReceiver(receiver);
			notification.setMsgUrl(url);
			notification.send(way);
			return true;
		}
		throw new PlatformException("消息发送失败");

	}

	public boolean pushNotifyUser(String msgId, String receiver) {
		String sql = "insert into p_un_notify_user(MSG_ID,USER,IS_READ,STATE) values(?,?,'0','1')";
		String[] users = receiver.split(",");
		boolean result = true;
		for (String user : users) {
			if (StringUtils.isNotBlank(user)) {
				int r = jdbcTemplate.update(sql, msgId, user);
				if (r == 0) {
					result = false;
				}
			}
		}
		return result;
	}
}
