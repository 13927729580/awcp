package cn.org.awcp.venson.concurrent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.venson.util.DateFormaterUtil;

public class TaskRepeater implements Job {

	private static final Log logger = LogFactory.getLog(TaskRepeater.class);
	private JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
	public static final String insertTaskSQL = "insert into oa_os_tasks(ID,PRE_TASKID,BELONG_PROGRAM,TASK_TYPE,TASK_CONTENT,TASK_ASSIGN,"
			+ "TASK_DESCRIBE,CREATOR,CREATE_TIME,TASK_STATE,TASK_PRIORITY,START_TIME,TASK_DEADLINE,TASK_LABELS,TODO_STATE,PUBLICITY,TASK_ATTACHMENT,LIST) "
			+ "values(:ID,:PRE_TASKID,:BELONG_PROGRAM,:TASK_TYPE,:TASK_CONTENT,:TASK_ASSIGN,:TASK_DESCRIBE,:CREATOR,:CREATE_TIME,:TASK_STATE,:TASK_PRIORITY,:START_TIME,"
			+ ":TASK_DEADLINE,:TASK_LABELS,:TODO_STATE,:PUBLICITY,:TASK_ATTACHMENT,:LIST)";

	public static final String repeat_every_day = "0 59 23 * * ?";
	public static final String repeat_every_week = "0 59 23 ? * L";
	public static final String repeat_every_month = "0 59 23 L * ?";
	public static final String repeat_every_year = "0 0 0 1 1 ?";
	public static final String repeat_every_never = "0 0 0 0 0 ?";

	public static final Map<String, Integer> COUNT_MAP = new HashMap<>();

	public TaskRepeater() {
	}

	public static String getCorn(int type) {
		switch (type) {
		case 1:
			return repeat_every_day;
		case 2:
			return repeat_every_week;
		case 3:
			return repeat_every_month;
		case 4:
			return repeat_every_year;
		default:
			return repeat_every_never;
		}
	}

	public static int getDay(int type) {
		switch (type) {
		case 1:
			return 1;
		case 2:
			return 7;
		case 3:
			return 30;
		case 4:
			return 365;
		default:
			return 0;
		}
	}

	private String copyData(Map<String, Object> data, JobDataMap map) {
		String ID = UUID.randomUUID().toString();
		data.put("ID", ID);
		// 重新设置时间
		data.put("CREATE_TIME", DocumentUtils.getIntance().getYMD());
		// 截至期限计算公式：截止期限=源数据截止期限+重复时长
		String TASK_DEADLINE = (String) data.get("TASK_DEADLINE");
		int type = map.getInt("type");
		Date date = DateFormaterUtil.stringToDate(TASK_DEADLINE);
		date = DateFormaterUtil.addDate(date, 3, getDay(type));
		data.put("TASK_DEADLINE", DateFormaterUtil.dateToString(date));
		NamedParameterJdbcTemplate nJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		nJdbcTemplate.update(insertTaskSQL, data);
		return ID;
	}

	private boolean addRepeater(JobDataMap map) {
		String id = map.getString("id");
		String sql = "select duration,end_time from oa_os_tasks_remind where re_type<>0 AND data_type=1 AND TASK_ID=?";
		Map<String, Object> repeatData;
		try {
			repeatData = jdbcTemplate.queryForMap(sql, id);
		} catch (DataAccessException e) {
			logger.debug("ID未找到或重复方式为不重复");
			COUNT_MAP.remove(id);
			return false;
		}
		// 先从缓存中取
		Integer count = COUNT_MAP.get(id);
		// 如果没有则属于第一次执行，从配置中取
		if (count == null) {
			count = map.getInt("count");
		}
		Integer duration = (Integer) repeatData.get("duration");
		if (duration != null) {
			// 校验执行次数
			if (count > duration) {
				logger.debug("重复次数已达上限");
				COUNT_MAP.remove(id);
				return false;
			}
		}
		// 校验截至日期
		String END_DATE = (String) repeatData.get("end_time");
		if (StringUtils.isNotBlank(END_DATE)) {
			if (DateFormaterUtil.stringToDate(END_DATE).before(new Date())) {
				logger.debug("超过截至日期");
				COUNT_MAP.remove(id);
				return false;
			}
		}
		// 1.找出源数据
		sql = "select * from oa_os_tasks where ID=?";
		Map<String, Object> oldTask = jdbcTemplate.queryForMap(sql, id);
		// 2.复制数据
		copyData(oldTask, map);
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
