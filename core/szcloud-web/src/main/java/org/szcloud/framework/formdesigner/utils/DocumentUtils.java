package org.szcloud.framework.formdesigner.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.JdbcHandle;
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
import org.szcloud.framework.venson.util.MD5Util;

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

	public static String createMD5(String input, String salt) throws Exception {
		if (StringUtils.isNotBlank(salt)) {

			return MD5Util.getMD5StringWithSalt(input, salt);
		} else {

			return MD5Util.getMD5String(input);
		}
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
		return true;

	}

	public boolean updateDataFlow(String name) {
		DocumentService service = Springfactory.getBean("documentServiceImpl");
		service.updateModelDataFlow(page, doc, name);
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

}
