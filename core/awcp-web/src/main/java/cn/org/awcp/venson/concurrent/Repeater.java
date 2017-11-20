package cn.org.awcp.venson.concurrent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.venson.util.DateFormaterUtil;

public class Repeater implements Job {

	private static final Log logger = LogFactory.getLog(Repeater.class);
	private JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
	public static final String insertTaskSQL = "insert into oa_os_tasks(ID,BELONG_PROGRAM,TASK_NAME,TASK_CONTENT,"
			+ "TASK_DESCRIBE,CREATOR,CREATE_TIME,TASK_STATE,TASK_PRIORITY,TASK_DEADLINE,TASK_LABELS,TODO_STATE,PUBLICITY,TASK_ATTACHMENT,LIST) "
			+ "values(:ID,:BELONG_PROGRAM,:TASK_NAME,:TASK_CONTENT,:TASK_DESCRIBE,:CREATOR,:CREATE_TIME,:TASK_STATE,:TASK_PRIORITY,:TASK_DEADLINE"
			+ ",:TASK_LABELS,:TODO_STATE,:PUBLICITY,:TASK_ATTACHMENT,:LIST)";
	public static final String insertTaskUserSQL = "insert into oa_os_user_tasks(TASK_ID,USER_ID,ADD_TIME,STATE) values(?,?,?,?)";

	public static final Map<String, Integer> COUNT_MAP = new HashMap<>();

	public Repeater() {
	}

	private String copyData(Map<String, Object> data, JobDataMap map) {
		String ID = UUID.randomUUID().toString();
		data.put("ID", ID);
		// 重新设置时间
		data.put("CREATE_TIME", DocumentUtils.getIntance().getYMD());
		// 截至期限计算公式：截止期限=源数据截止期限+重复时长
		String TASK_DEADLINE = (String) data.get("TASK_DEADLINE");
		String type = map.getString("type");
		Date date = DateFormaterUtil.stringToDate(TASK_DEADLINE);
		date = DateFormaterUtil.addDate(date, 3, Integer.parseInt(type));
		data.put("TASK_DEADLINE", DateFormaterUtil.dateToString(date));
		NamedParameterJdbcTemplate nJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		nJdbcTemplate.update(insertTaskSQL, data);
		return ID;
	}

	private boolean addRepeater(JobDataMap map) {
		String id = map.getString("id");
		String sql = "select REPEAT_TIME,END_DATE from oa_os_repeat where TASK_ID=?";
		Map<String, Object> repeatData = jdbcTemplate.queryForMap(sql, id);
		// 先从缓存中取
		Integer count = COUNT_MAP.get(id);
		// 如果没有则属于第一次执行，从配置中取
		if (count == null) {
			count = map.getInt("count");
		}
		// 校验执行次数
		if (count >= ((Integer) repeatData.get("REPEAT_TIME"))) {
			logger.debug("重复次数已达上限");
			COUNT_MAP.remove(id);
			return false;
		}
		// 校验截至日期
		String END_DATE = (String) repeatData.get("END_DATE");
		if (DateFormaterUtil.stringToDate(END_DATE).before(new Date())) {
			logger.debug("超过截至日期");
			COUNT_MAP.remove(id);
			return false;
		}
		// 1.找出源数据
		sql = "select * from oa_os_tasks where ID=?";
		Map<String, Object> oldTask = jdbcTemplate.queryForMap(sql, id);
		// 2.复制数据
		String ID = copyData(oldTask, map);
		// 3.查找是否有子任务，有的话也进行复制( 子任务先不考虑)
		// sql="select * from oa_os_tasks where PID=?";
		// List<Map<String, Object>> oldChildTask = jdbcTemplate.queryForList(sql,id);
		// 4.复制任务的活动人员
		sql = "select USER_ID from oa_os_user_tasks where TASK_ID=?";
		String today = DocumentUtils.getIntance().today();
		List<String> taskUsers = jdbcTemplate.queryForList(sql, String.class, id);
		for (String user : taskUsers) {
			jdbcTemplate.update(insertTaskUserSQL, ID, user, today, "1");
		}
		count++;
		COUNT_MAP.put(id, count);
		return true;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Scheduler scheduler = context.getScheduler();
		if (!addRepeater(context.getJobDetail().getJobDataMap())) {
			try {
				scheduler.shutdown();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}
}
