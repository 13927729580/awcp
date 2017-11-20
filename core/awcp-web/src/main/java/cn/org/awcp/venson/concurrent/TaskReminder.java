package cn.org.awcp.venson.concurrent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.utils.DocumentUtils;

public class TaskReminder {
	private String id;
	private JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
	private int type;
	private static final int start_after = 0;

	public TaskReminder(String id, int type) {
		this.id = id;
		this.type = type;
		this.remind();
	}

	public void remind() {
		String sql = "SELECT duration FROM oa_os_tasks_remind WHERE data_type=0 AND tasks_state=? AND task_id=?";
		List<Integer> list = this.jdbcTemplate.queryForList(sql, Integer.class, type, id);
		ExecutorService service = Executors.newCachedThreadPool();
		for (int i : list) {
			service.execute(new Task(i));
		}
		service.shutdown();

	}

	class Task extends Thread {
		public int minute;

		public Task(int minute) {
			this.minute = minute;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(minute * 60L * 1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String sql;
			// 任务开始后,查找任务执行人，发送消息通知
			if (type == start_after) {
				sql = "select TASK_ASSIGN from oa_os_tasks where id=?";
				try {
					String users = jdbcTemplate.queryForObject(sql, String.class, id);
					String[] arr = users.split(",");
					for (String user : arr) {
						sendMessage(user);
					}
				} catch (DataAccessException e) {
				}
			}
			// 任务结束后，查找任务发起人，发送消息通知
			else {
				sql = "select creator from oa_os_tasks where id=?";
				try {
					String user = jdbcTemplate.queryForObject(sql, String.class, id);
					sendMessage(user);
				} catch (DataAccessException e) {
				}
			}

		}
	}

	public void sendMessage(String user) {
		String sql = "insert into p_un_notification(TITLE,MSG_URL,TYPE,CREATE_TIME,RECEIVER,ISREAD) values(?,?,?,?,?,?)";
		String url = "https://www.baidu.com?id=" + id;
		String query = "select count(1) from p_un_notification where MSG_URL=? and RECEIVER=?";
		String today = DocumentUtils.getIntance().today();
		Integer hasRecord = jdbcTemplate.queryForObject(query, Integer.class, url, user);
		if (hasRecord == 0) {
			jdbcTemplate.update(sql, "您有新的任务未处理，请及时处理!", url, "1", today, user, "0");
		}
	}
}
