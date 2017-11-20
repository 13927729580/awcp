package cn.org.awcp.venson.concurrent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.utils.DocumentUtils;

public class Reminder extends Thread {

	private static final Log logger = LogFactory.getLog(Reminder.class);
	private String id;
	private int seconds;
	private boolean flag = true;
	private JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
	private int count = 1;
	private String type;

	public Reminder(String id) {
		this.id = id;
	}

	@Override
	public void run() {
		while (flag) {
			// 如果消息提醒次数大于5次则终止提醒
			if (count > 5) {
				flag = false;
				type = "消息提醒次数超过5次！";
				break;
			}
			// 查找该任务是否已经满足条件,如果已经满足则取消定时器
			String sql = "select REMINDER from oa_os_tasks where TASK_STATE<>'0' and id=?";
			String second = null;
			try {
				second = jdbcTemplate.queryForObject(sql, String.class, id);
			} catch (DataAccessException e1) {
				// 如果状态为0或者没有找到则直接返回不进行推送
				flag = false;
				type = "任务已经结束！";
				break;
			}
			// 如果提醒时间间隔为空或者小于O则不开启线程
			if (second == null || second.equals("0")) {
				flag = false;
				type = "任务时间提醒间隔为空！";
				break;
			}

			if (!addReminder()) {
				flag = false;
				type = "未找到人员！";
				break;
			}
			seconds = Integer.parseInt(second);
			try {
				Thread.sleep(seconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		logger.debug(id + "：消息提醒已经终止，终止原因：" + type);
	}

	private boolean addReminder() {
		logger.debug(id + "：第" + count + "次--------------消息提醒---------------");
		String today = DocumentUtils.getIntance().today();
		String sql = "select USER_ID from oa_os_user_tasks where STATE<>'0' and TASK_ID=?";
		List<String> taskUsers = jdbcTemplate.queryForList(sql, String.class, id);
		if (taskUsers.isEmpty())
			return false;
		sql = "insert into p_un_notification(TITLE,MSG_URL,TYPE,CREATE_TIME,RECEIVER,ISREAD) values(?,?,?,?,?,?)";
		String url = "https://www.baidu.com?id=" + id;
		String query = "select count(1) from p_un_notification where MSG_URL=? and RECEIVER=?";
		for (String user : taskUsers) {
			Integer hasRecord = jdbcTemplate.queryForObject(query, Integer.class, url, user);
			if (hasRecord == 0) {
				jdbcTemplate.update(sql, "您有新的任务未处理，请及时处理!", url, "1", today, user, "0");
			}
		}
		count++;
		return true;
	}

	public static void main(String args[]) {
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(new Reminder("TEST"));
		service.shutdown();
	}
}
