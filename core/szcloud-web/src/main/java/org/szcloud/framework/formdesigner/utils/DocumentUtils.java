package org.szcloud.framework.formdesigner.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.JdbcHandle;
import org.szcloud.framework.core.domain.SzcloudJdbcTemplate;
import org.szcloud.framework.core.utils.DateUtils;
import org.szcloud.framework.core.utils.Security;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.formdesigner.application.service.DocumentService;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.metadesigner.application.MetaModelService;
import org.szcloud.framework.metadesigner.vo.MetaModelVO;
import org.szcloud.framework.unit.service.PdataDictionaryService;
import org.szcloud.framework.unit.service.PunGroupService;
import org.szcloud.framework.unit.service.PunPositionService;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.service.PunUserGroupService;
import org.szcloud.framework.unit.vo.PdataDictionaryVO;
import org.szcloud.framework.unit.vo.PunGroupVO;
import org.szcloud.framework.unit.vo.PunPositionVO;
import org.szcloud.framework.unit.vo.PunRoleInfoVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.unit.vo.PunUserGroupVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;

/**
 * 文档操作
 */
public class DocumentUtils {

	private static final Logger logger = LoggerFactory.getLogger(DocumentUtils.class);

	private DocumentVO doc = null;
	private DynamicPageVO page = null;

	public DocumentUtils() {

	}

	private static AtomicInteger atomicInteger = new AtomicInteger(1);

	private static String today = DateUtils.format(new Date(), "yyyyMMdd");

	/**
	 * 根据时间日期生成编号
	 * 
	 * @param digit
	 *            位数
	 * @return
	 */
	public static String getNumber(int digit) {
		String newDate = DateUtils.format(new Date(), "yyyyMMdd");
		String num = null;
		// 如果当前日期不相等，则重新开始计值
		if (newDate.equals(today)) {
			num = atomicInteger.getAndIncrement() + "";
		} else {
			today = newDate;
			atomicInteger.set(2);
			num = "1";
		}
		// 补齐零位
		String zero = "";
		int len = digit - num.length();
		for (int i = 0; i < len; i++) {
			zero += "0";
		}
		num = zero + num;
		return newDate + num;
	}

	public DocumentUtils(DocumentVO doc, DynamicPageVO page) {
		this.doc = doc;
		this.page = page;
	}

	/*******************************/
	/**
	 * 保存会议时同时保存会议参与企业的信息
	 * 
	 * @param id
	 * @param companys
	 */
	public void saveMeetingCompanyInfo(String meetingID) {
		SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		try {
			if (StringUtils.isNoneBlank(meetingID)) {
				String sql = "select companys from jf_meeting where ID=?";
				String companys = jdbcTemplate.queryForObject(sql, String.class, meetingID);
				sql = "insert into jf_meeting_reply(ID,userId,meetingID,isLeader) values(?,?,?,'Y')";
				for (String company : companys.split(",")) {
					jdbcTemplate.update(sql, UUID.randomUUID().toString(), company, meetingID);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 政策推荐
	 * 
	 * @param zcID
	 * @param userIds
	 */
	public void saveZcReply(String zcID, String[] userIds) {
		SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		try {
			if (StringUtils.isNoneBlank(zcID)) {
				String sql = "insert into jf_zc_reply(ID,userId,zcID,isLeader) values(?,?,?,'Y')";
				for (String userId : userIds) {
					jdbcTemplate.update(sql, UUID.randomUUID().toString(), userId, zcID);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加催办信息
	 * 
	 * @param fkID(会议ID或通知ID)
	 */
	public void addCbInfo(String[] idArr, String type) {
		SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) getUser();
		Long userId = user.getUserId();

		try {
			jdbcTemplate.beginTranstaion();
			String fkID = "";
			String sql = "";
			String ids = generateIds(idArr);
			if ("0".equals(type)) {
				sql = "select fk_sendID from jf_send_tz_feedback where ID=?";
				fkID = jdbcTemplate.queryForObject(sql, String.class, idArr[0]);
				sql = "insert into jf_cb(ID,cb_company,cb_date,cb_user,cb_type,fk_ID) select UUID(),company,now(),"
						+ userId + ",'" + type + "','" + fkID + "' from jf_send_tz_feedback where ID in (" + ids + ")";
				jdbcTemplate.update(sql);
			} else if ("1".equals(type)) {
				sql = "select meetingID from jf_meeting_reply where ID=?";
				fkID = jdbcTemplate.queryForObject(sql, String.class, idArr[0]);
				sql = "insert into jf_cb(ID,cb_company,cb_date,cb_user,cb_type,fk_ID) select UUID(),userId,now(),"
						+ userId + ",'" + type + "','" + fkID + "' from jf_meeting_reply where ID in (" + ids + ")";
				jdbcTemplate.update(sql);
			} else if ("2".equals(type)) {
				sql = "select questionnaire_ID from jf_wjdc_feedback where ID=?";
				fkID = jdbcTemplate.queryForObject(sql, String.class, idArr[0]);
				sql = "insert into jf_cb(ID,cb_company,cb_date,cb_user,cb_type,fk_ID) select UUID(),company,now(),"
						+ userId + ",'" + type + "','" + fkID + "' from jf_wjdc_feedback where ID in (" + ids + ")";
				jdbcTemplate.update(sql);
			}
			jdbcTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				jdbcTemplate.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	private String generateIds(String[] ids) {
		StringBuffer sb = new StringBuffer("");
		for (String id : ids) {
			sb.append("'" + id + "',");
		}
		if (sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/******************************/

	public static String getBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		return basePath;
	}

	/**
	 * 当前用户
	 * 
	 * @return
	 */
	public static PunUserBaseInfoVO getUser() {
		return (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
	}

	/**
	 * 根据用户查找其部门的领导
	 * 
	 * @param userId
	 * @return
	 */
	public static List<Long> getDirectManager(Long userId) {
		return getUsersManager(userId, false);
	}

	/**
	 * 根据用户查找其上级部门的领导;
	 * 
	 * @param userId
	 * @return
	 */
	public static List<Long> getParentManager(Long userId) {
		return getUsersManager(userId, true);
	}

	/**
	 * 获取当前文档
	 * 
	 * @return
	 */
	public DocumentVO getCurrentDocument() {
		return doc;
	}

	public static String numberFormat(String num) {
		String result = "";
		boolean bool = false;
		if (num.indexOf(".") != -1) {
			bool = true;
		}
		String before = "";
		String after = "";
		if (bool) {
			String[] arr = num.split(".");
			if (arr.length == 2) {
				before = arr[0];
				after = arr[1];
			} else {
				before = arr[0];
			}
		} else {
			before = num;
		}

		String[] beforeArrReverse = reverse(before).split("");
		String temp = "";
		for (int i = 0; i < beforeArrReverse.length; i++) {
			temp += beforeArrReverse[i] + ((i + 1) % 3 == 0 && (i + 1) != beforeArrReverse.length ? "," : "");
		}
		if (after != "") {
			result = reverse(temp) + "." + after;
		} else {
			result = reverse(temp) + (bool ? "." : "");
		}
		return result;
	}

	private static String reverse(String str) {
		StringBuffer sb = new StringBuffer(str);
		return sb.reverse().toString();
	}

	/**
	 * 获取数据源
	 * 
	 * @param name
	 *            查询时:${modelAlias}_list;保存时:${modelAlias}
	 * @return
	 */
	public List<Map<String, String>> getDataContainer(String name) {
		if (doc != null) {
			return doc.getListParams().get(name);
		} else {
			return null;
		}
	}

	/**
	 * 设置指定数据源的数据
	 * 
	 * @param name
	 * @param data
	 * @return
	 */
	public boolean setListDataContainer(String name, List<Map<String, String>> data) {
		if (doc != null) {
			if (doc.getListParams().get(name) == null) {
				doc.getListParams().put(name, data);
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取数据源中某一属性的值
	 * 
	 * @param container
	 * @param item
	 * @return
	 */
	public String getDataItem(String container, String item) {
		List<Map<String, String>> list = getDataContainer(container);
		if (list != null && !list.isEmpty()) {
			Map<String, String> data = list.get(0);
			if (data != null) {
				String value = data.get(item);
				logger.debug(" getDataItem params is container : {} , item : {} , | value is {} .",
						new Object[] { container, item, value });
				return value;
			} else {
				logger.debug(" getDataItem params is container : {} , item : {} but  record is null.", container, item);
			}
		} else {
			logger.debug(" getDataItem params is container : {} , item : {} but  DataContainer is null.", container,
					item);
		}
		return null;
	}

	/**
	 * 设置数据源指定属性的值
	 * 
	 * @param container
	 * @param item
	 * @param value
	 * @return
	 */
	public boolean setDataItem(String container, String item, String value) {
		if (doc != null) {
			if (doc.getListParams().get(container) == null || doc.getListParams().get(container).isEmpty()) {
				List<Map<String, String>> list = doc.getListParams().get(container);
				if (list == null) {
					list = new PageList<Map<String, String>>(new ArrayList<Map<String, String>>(),
							new Paginator(1, 10, 0));
				}
				Map<String, String> map = new HashMap<String, String>();
				list.add(map);
				doc.getListParams().put(container, list);
			}
			Map<String, String> data = doc.getListParams().get(container).get(0);
			if (data == null) {
				data = new HashMap<String, String>();
				data.put(item, value);
				doc.getListParams().get(container).add(data);
			} else {
				data.put(item, value);
			}
			return true;
		}
		return false;
	}

	/**
	 * 获取数据源中某一属性的值，需要指定是第几条数据
	 * 
	 * @param container
	 * @param item
	 *            itemCode
	 * @param index
	 *            索引
	 * @return
	 */
	public String getListDataItem(String container, String item, int index) {
		List<Map<String, String>> list = getDataContainer(container);
		if (list != null && !list.isEmpty()) {
			if (index >= list.size()) {
				return null;
			}
			Map<String, String> data = getDataContainer(container).get(index);
			if (data != null) {
				return data.get(item);
			}
		}
		return null;
	}

	/**
	 * 设置数据源中某一属性的值
	 * 
	 * @param container
	 * @param item
	 * @param value
	 * @param index
	 * @return
	 */
	public boolean setListDataItem(String container, String item, String value, int index) {
		if (doc != null) {
			if (doc.getListParams().get(container) == null || doc.getListParams().get(container).isEmpty()) {
				List<Map<String, String>> list = doc.getListParams().get(container);
				if (list == null) {
					list = new PageList<Map<String, String>>(new ArrayList<Map<String, String>>(),
							new Paginator(1, 10, 0));
				}
				Map<String, String> map = new HashMap<String, String>();
				list.add(map);
				doc.getListParams().put(container, list);
			}
			Map<String, String> data = doc.getListParams().get(container).get(index);
			if (data == null) {
				data = new HashMap<String, String>();
				data.put(item, value);
				doc.getListParams().get(container).add(data);
			} else {
				data.put(item, value);
			}
			return true;
		}
		return false;
	}

	/**
	 * 把数据添加至数据源
	 * 
	 * @param name
	 * @param data
	 * @return
	 */
	public boolean setDataContainer(String name, Map<String, String> data) {
		if (doc != null) {
			if (doc.getListParams().get(name) == null) {
				doc.getListParams().get(name).add(data);
				return true;
			}
		}
		return false;
	}

	/**
	 * 当前时间
	 * 
	 * @return
	 */
	public static String today() {
		Date nowDate = new Date();
		return DateUtils.format(nowDate);
	}

	/**
	 * 当前时间 格式：YYYY_MM_DD_HH_MM_SS
	 * 
	 * @return
	 */
	public static String getDay() {
		Date nowDate = new Date();
		return DateUtils.format(nowDate);
	}

	/**
	 * 当前时间 格式：YYYY_MM_DD
	 * 
	 * @return
	 */
	public static String getYMD() {
		Date nowDate = new Date();
		return DateUtils.format2YYYY_MM_DD(nowDate);
	}

	/**
	 * 将日期字符串由一种格式转为另一种格式(解决dd/MM/yyyy到yyyy-MM-dd)
	 * 
	 * @param dateStr
	 * @param pattern
	 * @param topattern
	 * @return
	 */
	public String formatDateStr(String dateStr, String pattern, String topattern) {
		return DateUtils.format(DateUtils.parseDate(dateStr, pattern), topattern);
	}

	/**
	 * 当前时间
	 * 
	 * @param formatStr
	 *            时间格式
	 * @return
	 */
	public String today(String formatStr) {
		Date nowDate = new Date();
		return DateUtils.format(nowDate, formatStr);
	}

	/**
	 * 获取当前的时间与diff的时间；
	 * 
	 * @param diff
	 *            (1:表示但前时间后一天 －1:表示但前时间的前一天)
	 * @return
	 */
	public String todayDiff(int diff) {
		Date nowDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		cal.add(Calendar.DATE, diff);
		return DateUtils.format(cal.getTime());
	}

	/**
	 * 传入时间与当前时间之间的天数之差
	 * 
	 * @param date
	 * @return
	 */
	public long dayDiff(Date date) {
		long dayDiff = 0;
		Date today = DateUtils.getDayBegin(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long time1 = cal.getTimeInMillis();
		cal.setTime(today);
		long time2 = cal.getTimeInMillis();
		dayDiff = (time2 - time1) / (1000 * 3600 * 24);
		return dayDiff;
	}

	/**
	 * 
	 * @return
	 */
	public static String getCode() {
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		String sql = "select count(id) as counter from oa_os_project_describe";
		Map<String, Object> result = jdbcTemplate.queryForMap(sql);
		String counter = result.get("counter").toString();
		int sz = Integer.valueOf(counter) + 1;
		DecimalFormat df = new DecimalFormat("0000");
		return df.format(sz);

	}

	/**
	 * 保存某个数据源中的数据 限定该数据源必须为非自定义类型的
	 * 
	 * @param name
	 * @return
	 */
	public String saveData(String name) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		return service.saveModelData(page, doc, name);
	}

	/**
	 * 保存某个数据源中的数据 限定该数据源必须为非自定义类型的
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map saveDataFlow(String name, boolean masterDateSource) throws Exception {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		return service.saveModelDataFlow(page, doc, name, masterDateSource);
	}

	/**
	 * 将指定数据保存至数据库，限定该数据源必须为非自定义类型的
	 * 
	 * @param map
	 *            数据 key是modelItemCode
	 * @param modelCode
	 * @return
	 */
	public String saveModel(Map<String, String> map, String modelCode) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		return service.insertModelData(map, modelCode);
	}

	/**
	 * 将指定数据更新至数据库，限定该数据源必须为非自定义类型的
	 * 
	 * @param map
	 *            数据 key是modelItemCode
	 * @param modelCode
	 * @return
	 */
	public String updateModel(Map<String, String> map, String modelCode) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		return service.updateModelData(map, modelCode);
	}

	/**
	 * 执行sql
	 * 
	 * @param sql
	 */
	public void excuteUpdate(String sql) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		service.excuteUpdate(sql);
	}

	/**
	 * 传入参数执行sql
	 * 
	 * @param sql
	 * @param args
	 */
	public void excuteUpdate(String sql, Object... args) {
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		jdbcTemplate.update(sql, args);
	}

	/**
	 * 执行sql 得到一个map结果集
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, Object> excuteQuery(String sql) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		return service.excuteQuery(sql);
	}

	/**
	 * 执行sql 得到一个list结果集
	 * 
	 * @param sql
	 * @return List
	 */
	public List<Map<String, Object>> excuteQueryForList(String sql) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		return service.excuteQueryForList(sql);
	}

	/**
	 * 检查项目流程是否可以关闭,如果可以关闭进行一些处理。 这个方法在项目流程,工作安排,施工任务等流程最后节点执行
	 * 
	 * @param projectID
	 *            项目ID
	 * @param flowName
	 *            流程名
	 * @param taskType
	 *            任务类型
	 */
	public void endProjectFlow(String projectID, String flowName, String taskType, String taskID) {
		if (flowName != null) {
			if (flowName.equals("Land Purchase")) {
				taskType = "2";
			} else if (flowName.equals("Development & Planning")) {
				taskType = "3";
			} else if (flowName.equals("Presales")) {
				taskType = "4";
			} else if (flowName.equals("Construction Preparation")) {
				taskType = "5";
			} else if (flowName.equals("Construction Financing")) {
				taskType = "6";
			} else if (flowName.equals("Construction")) {
				taskType = "7";
			} else if (flowName.equals("Ownership Transfer")) {
				taskType = "8";
			}
		}
		if (taskType != null) {
			if (taskType.equals("2")) {
				flowName = "Land Purchase";
			} else if (taskType.equals("3")) {
				flowName = "Development & Planning";
			} else if (taskType.equals("4")) {
				flowName = "Presales";
			} else if (taskType.equals("5")) {
				flowName = "Construction Preparation";
			} else if (taskType.equals("6")) {
				flowName = "Construction Financing";
			} else if (taskType.equals("7")) {
				flowName = "Construction";
			} else if (taskType.equals("8")) {
				flowName = "Ownership Transfer";
			}
		}
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		boolean bool = checkProjectFlow(jdbcTemplate, projectID, flowName, taskType, taskID);
		if (bool) {
			String sql = "update oa_os_project_flow set Status='Ended' where ProjectID=? and Flow=?";
			jdbcTemplate.update(sql, projectID, flowName);
			if (flowName == "Ownership Transfer") {
				String date = today("yyyy-MM-dd");
				sql = "update oa_os_project set closeDate=?,isClosed='Y' where ID=?";
				jdbcTemplate.update(sql, date, projectID);
			}
		}
	}

	private boolean checkProjectFlow(JdbcTemplate jdbcTemplate, String projectID, String flowName, String taskType,
			String taskID) {
		// 还没有完成的任务
		String sql = "select count(*) from oa_os_mytask where projectName='" + projectID + "' and taskType='" + taskType
				+ "' and status<>'Completed' and ID<>'" + taskID + "'";
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		if (count > 0) {
			return false;
		}
		// 没有完成的建筑施工任务
		if (taskType.equals("7")) {
			sql = "select count(*) from oa_os_construction where status<>'Completed' and ProjectName='" + projectID
					+ "' and ID<>'" + taskID + "'";
			count = jdbcTemplate.queryForObject(sql, Integer.class);
			if (count > 0) {
				return false;
			}
		}
		// 还没有设计的自动任务
		sql = "select count(*) from oa_os_automatic_task where ID not in "
				+ "(select autoTaskID from oa_os_project_auto_task where projectID ='" + projectID + "') and taskType='"
				+ taskType + "'";
		count = jdbcTemplate.queryForObject(sql, Integer.class);
		if (count > 0) {
			return false;
		}
		// 从项目流程流程完成情况表,结束流程才添加记录
		if (!flowName.equals("Construction Preparation") && !flowName.equals("Construction Financing")) {
			sql = "select count(*) from oa_os_project_flow_status where projectID='" + projectID + "' and flowName='"
					+ flowName + "'";
			count = jdbcTemplate.queryForObject(sql, Integer.class);
			if (count == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取变量的值
	 * 
	 * @param name
	 * @param project
	 * @return
	 */
	public String getValueFromSystem(String name, String project) {
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		String sql = "select count(*) from oa_os_system_static where name='" + name + "' and projectName='" + project
				+ "'";
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		if (count == 0) {
			sql = "select value from oa_os_system_static where name='" + name + "' and projectName='Global'";
		} else {
			sql = "select value from oa_os_system_static where name='" + name + "' and projectName='" + project + "'";
		}
		String value = jdbcTemplate.queryForObject(sql, String.class);
		return value;
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	public boolean deleteDocument(String docId) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		// service.deleteDoc(docId);
		return true;
	}

	public static String createMD5(String input, String charset) throws Exception {
		byte[] data;
		if (charset != null && !"".equals(charset)) {
			data = input.getBytes(charset);
		} else {
			data = input.getBytes();
		}
		MessageDigest messageDigest = getMD5();
		messageDigest.update(data);
		return byteArrayToHexString(messageDigest.digest());
	}

	private static MessageDigest getMD5() throws NoSuchAlgorithmException {
		return MessageDigest.getInstance("MD5");
	}

	private static String byteArrayToHexString(byte[] data) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		// 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
		char arr[] = new char[16 * 2];
		int k = 0; // 表示转换结果中对应的字符位置
		// 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
		for (int i = 0; i < 16; i++) {
			byte b = data[i]; // 取第 i 个字节
			// 取字节中高 4 位的数字转换, >>>为逻辑右移，将符号位一起右移
			arr[k++] = hexDigits[b >>> 4 & 0xf];
			// 取字节中低 4 位的数字转换
			arr[k++] = hexDigits[b & 0xf];
		}
		// 换后的结果转换为字符串
		return new String(arr);
	}

	/**
	 * 将数据源数据 更新至数据库
	 * 
	 * @param name
	 *            数据源名称
	 * @return
	 */
	public boolean updateData(String name) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		service.updateModelData(page, doc, name);
		// doc.setLastmodified(new Date(System.currentTimeMillis()));
		// service.save(doc);
		return true;

	}

	public boolean updateDataFlow(String name) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		service.updateModelDataFlow(page, doc, name);
		// doc.setLastmodified(new Date(System.currentTimeMillis()));
		// service.save(doc);
		return true;

	}

	/**
	 * 返回数据源中 模型属性的值
	 * 
	 * @param dataSource
	 *            格式 ${modeCode.modeItemCode}
	 * @return
	 */
	public String getItemValue(String dataSource) {
		String[] source = dataSource.split("\\.");
		if (source != null && source.length > 0) {
			String itemValue = " ";
			if (doc != null) {
				if (doc.getListParams() != null && !doc.getListParams().isEmpty()) {
					List<Map<String, String>> list = doc.getListParams().get(source[0] + "_list");
					if (list != null && list.size() > 0) {
						itemValue = list.get(0).get(source[1]);
					}
				}
			}
			if (StringUtils.isNotBlank(itemValue)) {
				return itemValue;
			} else {
				return " ";
			}
		}
		return "";
	}

	/**
	 * 查找数据字典 返回List<Map<String,Object>>结果集
	 * 
	 * @param sql
	 * @return List<Map<String,Object>>
	 */
	public static List<Map<String, Object>> getDictionary(String sql) {
		JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
		List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
		return map;

	}

	/**
	 * 获取多个数据字典对象
	 * 
	 * @param code
	 *            data_code
	 * @param level
	 *            层级 如果为空 默认1
	 * @return List<PdataDictionaryVO>
	 */
	public static List<PdataDictionaryVO> getDictionaryLikeCode(String code, String level) {
		PdataDictionaryService service = Springfactory.getBean("pdataDictionaryServiceImpl");
		// String sql = "select * from p_data_dictionary where code like
		// "+"\""+code+"%\"";
		if (StringUtils.isBlank(level.trim())) {
			level = "1";
		} else {
			level = level.trim();
		}
		code = code.trim();
		BaseExample example = new BaseExample();
		example.createCriteria().andLike("code", code).andEqualTo("level", level);
		List<PdataDictionaryVO> dataList = service.selectByExample(example);
		// List<Map<String,Object>> map = jdbcTemplate.queryForList(sql);
		return dataList;
	}

	/**
	 * 
	 * @param code
	 *            data_code 数据字典code
	 * @param level
	 *            层级 如果为空 默认1
	 * @param value
	 *            null（该参数已过时，兼容老代码）
	 * @return
	 */
	public static String getOptions(String code, String level, String value) {
		PdataDictionaryService service = Springfactory.getBean("pdataDictionaryServiceImpl");
		if (StringUtils.isBlank(level.trim())) {
			level = "1";
		} else {
			level = level.trim();
		}

		if (StringUtils.isBlank(code)) {
			return " ";
		} else {
			code = code.trim();
		}
		BaseExample example = new BaseExample();
		example.createCriteria().andLike("code", code + "%").andEqualTo("level", Integer.parseInt(level));
		example.setOrderByClause("ORDER ASC, ID ASC");
		List<PdataDictionaryVO> dataList = service.selectPagedByExample(example, 1, Integer.MAX_VALUE, null);
		StringBuilder sb = new StringBuilder();
		if (dataList != null && dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				PdataDictionaryVO vo = dataList.get(i);
				sb.append(vo.getCode()).append("=").append(vo.getDataValue()).append(";");
			}
		}
		return sb.toString();
	}

	/**
	 * 根据data_code 查找选项
	 * 
	 * @param code
	 * @return 所有选项 形式：'data_key=data_value;data_key=data_value;'
	 */
	public static String getItems(String code) {
		PdataDictionaryService service = Springfactory.getBean("pdataDictionaryServiceImpl");
		code = code.trim();
		if (StringUtils.isBlank(code)) {
			return " ";
		}
		BaseExample example = new BaseExample();
		example.createCriteria().andLike("code", code + "%").andEqualTo("level", 2);
		List<PdataDictionaryVO> dataList = service.selectPagedByExample(example, 1, Integer.MAX_VALUE, null);
		StringBuilder sb = new StringBuilder();
		for (PdataDictionaryVO vo : dataList) {
			sb.append(vo.getCode() + "=" + vo.getDataValue() + ";");
		}
		return sb.substring(0, sb.length() - 1).toString();
	}

	/**
	 * 使用code查找数据字典，返回 dataValue
	 * 
	 * @param code
	 * @return
	 */
	public static String getItem(String code) {
		PdataDictionaryService service = Springfactory.getBean("pdataDictionaryServiceImpl");
		if (StringUtils.isBlank(code)) {
			return " ";
		}
		PdataDictionaryVO vo = service.findByCode(code);
		if (vo != null) {
			return vo.getDataValue();
		}
		return " ";
	}

	/**
	 * 根据用户Id查找用户
	 * 
	 * @param userId
	 * @return
	 */
	public static PunUserBaseInfoVO getUserById(Long userId) {
		if (userId == null) {
			return null;
		}
		PunUserBaseInfoService service = Springfactory.getBean("punUserBaseInfoServiceImpl");
		PunUserBaseInfoVO vo = service.findById(userId);
		return vo;
	}

	/**
	 * 根据用户Id查找用户
	 * 
	 * @param userId
	 * @return
	 */
	public static List<PunPositionVO> getUserPositionById(String userId) {
		if (userId == null) {
			return null;
		}
		PunPositionService service = Springfactory.getBean("punPositionServiceImpl");
		Map queryParam = new HashMap();
		queryParam.put("userId", Long.parseLong(userId));
		List<PunPositionVO> voList = service.queryResult("getByUserID", queryParam);
		return voList;
	}

	/**
	 * 根据用户Id查找用户
	 * 
	 * @param userId
	 * @return
	 */
	public static List<PunGroupVO> getUserGroupById(String userId) {
		if (userId == null) {
			return null;
		}
		PunGroupService service = Springfactory.getBean("punGroupServiceImpl");
		Map queryParam = new HashMap();
		queryParam.put("userId", Long.parseLong(userId));
		List<PunGroupVO> voList = service.queryResult("getByUserID", queryParam);
		return voList;
	}

	/**
	 * 获取用户的组织名称
	 */
	public static String getGroupName(Long userId) {
		if (userId == null) {
			userId = DocumentUtils.getUser().getUserId();
		}
		List<PunGroupVO> groups = getUserGroupById(userId + "");
		for (PunGroupVO punGroupVO : groups) {
			if (StringUtils.isNotBlank(punGroupVO.getGroupShortName())) {
				return punGroupVO.getGroupShortName();
			}
		}
		return "";
	}

	/**
	 * 获取当前用户的组织名称
	 * 
	 */
	public static String getGroupName() {
		return getGroupName(null);
	}

	public static List<Long> getRoles() {
		List<PunRoleInfoVO> roleList = (List<PunRoleInfoVO>) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_ROLES);
		List<Long> roleIdList = new ArrayList<Long>();
		if (roleList != null && !roleList.isEmpty()) {
			for (PunRoleInfoVO role : roleList) {
				roleIdList.add(role.getRoleId());
			}
		}
		return roleIdList;
	}

	public static boolean hasRole(Long... roleIds) {
		List<PunRoleInfoVO> roleList = (List<PunRoleInfoVO>) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_ROLES);
		if (roleList != null && !roleList.isEmpty()) {
			for (PunRoleInfoVO role : roleList) {
				for (Long roleId : roleIds) {
					if (roleId == role.getRoleId()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 根据用户id查询用户所属的所有组，包括根节点（单位）
	 * 
	 * @param userId
	 * @return
	 */
	public static List<PunGroupVO> listGroupByUser(Long userId) {
		PunGroupService service = Springfactory.getBean("punGroupServiceImpl");
		List<PunGroupVO> list = service.queryGroupByUserId(userId, 1, Integer.MAX_VALUE, null);
		return list;
	}

	/**
	 * 删除平台用户，操作范围限制当前登录用户所在单位的人员 如果用户不存在， 且password为空，则使用默认密码666666
	 * password不为空，则使用password作为密码 如果用户存在 password为空，则不修改用户密码
	 * password不为空，则修改用户密码
	 * 
	 * @param idCard
	 *            身份证号码，即登录帐号
	 * @param name
	 *            用户姓名
	 * @param mobile
	 *            用户邮箱
	 * @param email
	 *            用户电子邮件
	 * @param password
	 *            用户密码
	 * @return
	 */
	public static Long addOrUpdateUser(String idCard, String name, String mobile, String email, String password) {
		Object o = SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		if (o != null && o instanceof PunUserBaseInfoVO) {
			PunUserBaseInfoVO creater = (PunUserBaseInfoVO) o;
			Long groupId = creater.getGroupId();
			// 首先查找是否存在该用户
			PunUserBaseInfoService userService = Springfactory.getBean("punUserBaseInfoServiceImpl");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("groupId", groupId);
			params.put("userIdCardNumber", idCard);
			List<PunUserBaseInfoVO> users = userService.queryResult("eqQueryList", params);
			PunUserBaseInfoVO user = null;
			if (users != null && !users.isEmpty()) {
				if (users.size() != 1) {
					logger.error("current user {} will add user of |id:{},name:{},moble:{},email:{}| but duplicate.",
							new Object[] { creater.getUserIdCardNumber(), idCard, name, mobile, email });
				} else {
					user = users.get(0);
				}
			} else {
				user = new PunUserBaseInfoVO();
				if (StringUtils.isBlank(password)) {
					user.setUserPwd(Security.encryptPassword("666666"));
				}
				user.setGroupId(groupId);
			}
			if (user != null) {
				user.setUserIdCardNumber(idCard);
				if (!StringUtils.isBlank(password)) {
					user.setUserPwd(Security.encryptPassword(password));
				}
				user.setName(name);
				user.setMobile(mobile);
				user.setUserEmail(email);
				userService.updateUser(user);
				logger.info("current user {} add or update user of |id:{},name:{},moble:{},email:{}| success.",
						new Object[] { creater.getUserIdCardNumber(), idCard, name, mobile, email });
				return user.getUserId();
			}
		}
		logger.error("not logging");
		return null;
	}

	/**
	 * 删除平台用户，操作范围限制当前登录用户所在单位的人员
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean removeUser(String userId) {
		Object o = SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		if (o != null && o instanceof PunUserBaseInfoVO && StringUtils.isNotBlank(userId)) {
			PunUserBaseInfoVO creater = (PunUserBaseInfoVO) o;
			Long groupId = creater.getGroupId();
			// 首先查找是否存在该用户
			PunUserBaseInfoService userService = Springfactory.getBean("punUserBaseInfoServiceImpl");
			Long id = Long.valueOf(userId);
			PunUserBaseInfoVO user = userService.findById(id);
			if (user != null) {
				if (user.getGroupId().longValue() == groupId.longValue()) {
					userService.addOrUpdateUser(user);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * ljw 2015-4-9 往session中写东西
	 */
	public static void putThingIntoSession(Object key, Object value) {
		SessionUtils.addObjectToSession(key, value);
	}

	/**
	 * ljw 2015-4-9 从session中取东西
	 * 
	 * @param key
	 * @return
	 */
	public static Object getThingFromSession(String key) {
		return SessionUtils.getObjectFromSession(key);
	}

	/**
	 * ljw 2015-1-22 生成水印号
	 * 
	 * @return 年+月+日+时+分+秒+毫秒
	 */
	public static String generateWatermark() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(date);
	}

	/**
	 * ljw 2015-1-22 生成流水号
	 * 
	 * @param type
	 *            类别代码
	 * @return 类别代码+年+月+日+时+分+秒+毫秒
	 */
	public static String generateSerialNum(String type) {
		String resultString = type + generateWatermark();
		return resultString;
	}

	private static List<Long> getUsersManager(Long userId, boolean isParent) {
		PunUserGroupService service = Springfactory.getBean("punUserGroupServiceImpl");
		List<PunUserGroupVO> list = null;
		if (isParent) {
			list = service.queryParentManager(userId);
		} else {
			list = service.queryDirectManager(userId);
		}
		List<Long> rtnList = new ArrayList<Long>();
		for (PunUserGroupVO vo : list) {
			rtnList.add(vo.getUserId());
		}
		return rtnList;
	}

	public static String getPhotoByUserId(Long userId) {
		String photo = "";
		try {
			JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
			String sql = "select headimage from oa_os_internal_users  where userId=" + userId
					+ " union select headimage from oa_os_external_users  where userId=" + userId;
			List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
			Map<String, Object> result = null;
			if (null != results && results.size() > 0) {
				result = results.get(0);
				if (null != result.get("headimage")) {
					photo = String.valueOf(result.get("headimage"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return photo;
	}

	public static String getOASystemCode(String code) {
		String year = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
		String month = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
		String day = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());

		try {
			JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
			int sequence = jdbcTemplate
					.queryForInt("SELECT sequenceValue FROM OA_SysSequence WHERE sequencecode = '" + code + "'");
			sequence = sequence + 1;
			jdbcTemplate.update(
					"UPDATE OA_SysSequence SET sequenceValue = " + sequence + " WHERE sequencecode = '" + code + "';");
			return code + year + month + String.format("%04d", sequence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 根据手机列表ID返回手机表单ID
	public static String getMPageIdByMListPageId(String mListPageId) {
		String ls_Sql = "select PAGEID_APP from p_un_page_binding where PAGEID_APP_LIST={0}";
		return JdbcHandle.getJdbcTemplate().queryForObject(MessageFormat.format(ls_Sql, mListPageId), String.class);
	}

	// 根据PC列表ID返回PC表单ID
	public static String getPCPageIdByPCListPageId(String pcListPageId) {
		String ls_Sql = "select PAGEID_PC from p_un_page_binding where PAGEID_PC_LIST={0}";
		return JdbcHandle.getJdbcTemplate().queryForObject(MessageFormat.format(ls_Sql, pcListPageId), String.class);
	}

	// 根据任意PageID返回手机表单ID
	public static String getMPageIDByDefaultId(String defaultId) {
		String ls_Sql = "select PAGEID_APP from p_un_page_binding where PAGEID_PC={0} or PAGEID_PC_LIST={1} or PAGEID_APP={2} or PAGEID_APP_LIST={3}";
		return JdbcHandle.getJdbcTemplate()
				.queryForObject(MessageFormat.format(ls_Sql, defaultId, defaultId, defaultId, defaultId), String.class);
	}

	// 根据任意PageId返回PC表单ID
	public static String getPCPageIDByDefaultId(String defaultId) {
		String ls_Sql = "select PAGEID_PC from p_un_page_binding where PAGEID_PC={0} or PAGEID_PC_LIST={1} or PAGEID_APP={2} or PAGEID_APP_LIST={3}";
		return JdbcHandle.getJdbcTemplate()
				.queryForObject(MessageFormat.format(ls_Sql, defaultId, defaultId, defaultId, defaultId), String.class);
	}

	// 比较页面获取的值与通过sql获取的值是否相等
	public static boolean equalsObject(Object obj1, Object obj2) {
		if (obj1 != null && obj2 != null) {
			if (obj1.toString().equals(obj2.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取前一页面传送的请求参数； 比如A页面通过条件aaa查询，跳转到B页面后，可通过该方法获取到A页面请求参数aaa的值
	 */
	public String getReqParamFromPrePage(String name) {
		String itemValue = doc.getRequestParams().get(name);
		if (StringUtils.isNotBlank(itemValue)) {
			return itemValue;
		} else {
			return "";
		}
	}

	/**
	 * 根据modelCode 创建数据源
	 * 
	 * @param modelcode
	 *            模型编码
	 * @return
	 */
	public Map<String, String> createModel(String modelcode) {
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}

	/**
	 * 校验某个组件
	 * 
	 * @param container
	 * @param item
	 * @return
	 */
	public boolean validateItem(String componentId) {
		return true;
	}

	/**
	 * 复制数据源中的数据(单个)
	 * 
	 * @param modelCode
	 *            模型编号
	 * @param item
	 *            主键属性名称
	 * @param value
	 *            主键值
	 * @return
	 */
	public Map<String, String> copyTableRecord(String modelCode, String item, String value) {
		Map<String, String> ret = new HashMap<String, String>();
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		MetaModelService modelService = Springfactory.getBean("metaModelServiceImpl");
		MetaModelVO model = modelService.queryByModelCode(modelCode);
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(model.getTableName()).append(" where ").append(item).append(" ='")
				.append(value).append("'");
		Map<String, Object> temp = new HashMap<String, Object>();
		temp = service.excuteQuery(sql.toString());
		for (Iterator<java.util.Map.Entry<String, Object>> iterator = temp.entrySet().iterator(); iterator.hasNext();) {

			java.util.Map.Entry<String, Object> entry = iterator.next();
			Object valueObject = entry.getValue();
			if (valueObject != null) {
				ret.put(entry.getKey(), valueObject.toString());
			}

		}
		ret.put(item, "");
		String id = service.insertModelData(ret, modelCode);
		ret.put(item, id);

		return ret;
	}

	/**
	 * 更新某条记录中某个字段的值(主键类型为uuid)
	 * 
	 * @param primarykey
	 *            主键名称
	 * @param modelCode
	 *            模型编号
	 * @param props
	 *            数据 key是列名，value为值
	 * @return
	 */
	public boolean updateTableData(String primarykey, String modelCode, Map<String, String> props) {
		MetaModelOperateService tableService = Springfactory.getBean("metaModelOperateServiceImpl");
		MetaModelService modelService = Springfactory.getBean("metaModelServiceImpl");
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		MetaModelVO model = modelService.queryByModelCode(modelCode);
		StringBuilder sql = new StringBuilder();
		String primarykeyValue = props.get(primarykey);
		sql.append("select * from ").append(model.getTableName()).append(" where ").append(primarykey).append("='")
				.append(primarykeyValue).append("';");
		Map<String, Object> temp = new HashMap<String, Object>();
		temp = service.excuteQuery(sql.toString());
		Map<String, String> ret = new HashMap<String, String>();
		for (String key : temp.keySet()) {
			if (temp.get(key) != null) {
				ret.put(key, temp.get(key).toString());
			}
		}
		for (String key : props.keySet()) {
			if (props.get(key) != null) {
				try {
					ret.put(key, props.get(key).toString());
				} catch (Exception e) {
					String numString = String.valueOf(props.get(key));
					if (numString.endsWith(".0")) {
						numString = numString.substring(0, numString.length() - 2);
					}
					ret.put(key, numString);
				}
			}
		}
		try {
			return tableService.update(ret, modelCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 校验某个文档
	 * 
	 * @param doc
	 * @return
	 */
	public boolean validateDocument(DocumentVO doc) {
		// FIXME
		return true;
	}

	/**
	 * 保存某个数据源中的数据 限定该数据源必须为非自定义类型的
	 * 
	 * @param name
	 * @return
	 */
	public Map<String, String> createModel() {

		return new HashMap<String, String>();
	}

	/**
	 * 保存当前document文档，返回Id
	 * 
	 * @return ID
	 */
	public String saveDocument() {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		return service.save(doc);
	}

	/**
	 * 删除个数据源中的数据 限定该数据源必须为非自定义类型的
	 * 
	 * @param name
	 * @return
	 */
	public boolean deleteData(DynamicPageVO page, String recordId) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		service.deleteModelData(page, recordId);
		return true;
	}

	public void updateTZIsRead(String fk_id, String userId) {
		String sql = "update jf_send_tz_feedback set IsRead=1 where fk_sendID='" + fk_id + "' and company='" + userId
				+ "'";
		String sqlcount = "select count(*) from jf_cb where fk_ID='" + fk_id + "' and cb_company='" + userId + "'";
		if (excuteQuery(sqlcount) != null) {
			String sqlupdate = "update jf_cb set IsRead='Y' where fk_ID='" + fk_id + "' and cb_company='" + userId
					+ "'";
			excuteUpdate(sqlupdate);
		}
		excuteUpdate(sql);
	}

	public Object getDeductDays(HttpServletRequest request) {
		String FK_Node = request.getParameter("FK_Node");
		Map<String, Object> map = this.excuteQuery("select DeductDays from wf_node a where a.NodeID='" + FK_Node + "'");
		if (map == null || map.isEmpty()) {
			return 0;

		} else {

			return map.get("DeductDays");
		}
	}

	/**
	 * 根据用户id获取用户的公司信息
	 */
	public Map<String, Object> getUserCompanyInfo(Long userId) {
		if (userId == null) {
			userId = DocumentUtils.getUser().getUserId();
		}
		String sql = "SELECT a.id,a.qy_name,a.hy_type,a.contacts,a.contact_number,a.qy_address FROM jf_qy_user_audit a LEFT JOIN jf_user_company b ON a.id=b.company_id WHERE b.user_id='"
				+ userId + "'";
		return this.excuteQuery(sql);
	}

	public Map<String, Object> getUserCompanyInfo() {
		return getUserCompanyInfo(null);
	}
}
