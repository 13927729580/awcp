package cn.org.awcp.formdesigner.utils;

import BP.WF.Dev2Interface;
import BP.WF.Flow;
import BP.WF.Work;
import cn.org.awcp.core.utils.*;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.application.service.DocumentService;
import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.core.domain.Document;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.metadesigner.application.MetaModelService;
import cn.org.awcp.metadesigner.vo.MetaModelVO;
import cn.org.awcp.unit.message.PunNotificationService;
import cn.org.awcp.unit.service.PunGroupService;
import cn.org.awcp.unit.service.PunPositionService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.service.PunUserGroupService;
import cn.org.awcp.unit.vo.*;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.util.HttpUtils;
import cn.org.awcp.venson.util.LocalStorage;
import cn.org.awcp.venson.util.MD5Util;
import cn.org.awcp.venson.util.PlatfromProp;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public abstract class BaseUtils {
    protected static final Log logger = LogFactory.getLog(BaseUtils.class);
    // 注入文档服务
    protected DocumentService documentService = Springfactory.getBean("documentServiceImpl");
    // 注入用户组织服务
    protected PunUserGroupService punUserGroupService = Springfactory.getBean("punUserGroupServiceImpl");
    // 注入用户服务
    protected PunUserBaseInfoService punUserBaseInfoService = Springfactory.getBean("punUserBaseInfoServiceImpl");
    // 注入岗位服务
    protected PunPositionService punPositionService = Springfactory.getBean("punPositionServiceImpl");
    // 注入组织服务
    protected PunGroupService punGroupService = Springfactory.getBean("punGroupServiceImpl");
    // 注入SpringJdbc
    protected JdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
    // 注入元数据模型服务
    protected MetaModelService metaModelService = Springfactory.getBean("metaModelServiceImpl");
    // 注入元数据操作服务
    protected MetaModelOperateService metaModelOperateService = Springfactory.getBean("metaModelOperateServiceImpl");
    // 消息推送
    protected PunNotificationService punNotificationService = Springfactory.getBean("punNotificationService");
    // 消息推送
    protected Cache cache = Springfactory.getBean("cache");

    /*
     * 获取项目路径
     *
     * @param request
     *
     * @return
     */
    public String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
                + "/";
        return basePath;
    }

    /**
     * MD5加密
     *
     * @param input
     * @param salt
     * @return
     * @throws Exception
     */
    public String createMD5(String input, String salt) {
        if (StringUtils.isNotBlank(salt)) {
            return MD5Util.getMD5StringWithSalt(input, salt);
        } else {
            return MD5Util.getMD5String(input);
        }
    }

    /**
     * 往session中写东西
     */
    public void putThingIntoSession(Object key, Object value) {
        SessionUtils.addObjectToSession(key, value);
    }

    /**
     * 从session中取东西
     *
     * @param key
     * @return
     */
    public Object getThingFromSession(String key) {
        return SessionUtils.getObjectFromSession(key);
    }

    /******************* 用户组织结构获取 *******************/
    /**
     * 获取当前用户
     *
     * @return
     */
    public PunUserBaseInfoVO getUser() {
        if (ControllerContext.getRequest() == null) {
            return null;
        }
        Object currentUser = SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
        // 如果当前用户为空则尝试去cookie中查找
        if (currentUser == null) {
            // 尝试用cookie登录
            if (!ControllerHelper.loginByCookie()) {
                return null;
            }
        }
        return (PunUserBaseInfoVO) currentUser;

    }

    /**
     * 根据用户查找其部门的领导
     *
     * @param userId
     * @return
     */
    public List<Long> getDirectManager(Long userId) {
        return getUsersManager(userId, false);
    }

    /**
     * 根据用户查找其上级部门的领导;
     *
     * @param userId
     * @return
     */
    public List<Long> getParentManager(Long userId) {
        return getUsersManager(userId, true);
    }

    /**
     * 根据用户Id查找用户
     *
     * @param userId
     * @return
     */
    public PunUserBaseInfoVO getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        PunUserBaseInfoVO vo = punUserBaseInfoService.findById(userId);
        return vo;
    }

    /**
     * 根据用户Id查找用户
     *
     * @param userId
     * @return
     */
    public List<PunPositionVO> getUserPositionById(String userId) {
        if (userId == null) {
            return null;
        }
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("userId", Long.parseLong(userId));
        List<PunPositionVO> voList = punPositionService.queryResult("getByUserID", queryParam);
        return voList;
    }

    /**
     * 根据用户Id查找用户
     *
     * @param userId
     * @return
     */
    public List<PunGroupVO> getUserGroupById(String userId) {
        if (userId == null) {
            return null;
        }

        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("userId", Long.parseLong(userId));
        List<PunGroupVO> voList = punGroupService.queryResult("getByUserID", queryParam);
        return voList;
    }

    /**
     * 获取用户的组织名称
     */
    public String getGroupName(Long userId) {
        if (userId == null) {
            userId = getUser().getUserId();
        }
        List<PunGroupVO> groups = getUserGroupById(userId + "");
        for (PunGroupVO punGroupVO : groups) {
            if (StringUtils.isNotBlank(punGroupVO.getGroupChName())) {
                return punGroupVO.getGroupChName();
            }
        }
        return "";
    }

    /**
     * 获取当前用户的组织名称
     */
    public String getGroupName() {
        return getGroupName(null);
    }

    /**
     * 获取用户的角色
     *
     * @return
     */
    public List<Long> getRoles() {
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

    /**
     * 用户是否有该角色
     *
     * @param roleIds
     * @return
     */
    public boolean hasRole(Long... roleIds) {
        List<Long> roles = getRoles();
        for (Long roleId : roleIds) {
            if (roles.contains(roleId)) {
                return true;
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
    public List<PunGroupVO> listGroupByUser(Long userId) {
        List<PunGroupVO> list = punGroupService.queryGroupByUserId(userId, 1, Integer.MAX_VALUE, null);
        return list;
    }

    /**
     * 新增或修改平台用户，操作范围限制当前登录用户所在单位的人员 如果用户不存在， 且password为空，则使用默认密码666666
     * password不为空，则使用password作为密码 如果用户存在 password为空，则不修改用户密码 password不为空，则修改用户密码
     *
     * @param idCard   身份证号码，即登录帐号
     * @param name     用户姓名
     * @param mobile   用户邮箱
     * @param email    用户电子邮件
     * @param password 用户密码
     * @return
     */
    public Long addOrUpdateUser(String idCard, String name, String mobile, String email, String password) {
        PunUserBaseInfoVO o = this.getUser();
        if (o != null) {
            Long groupId = o.getGroupId();
            // 首先查找是否存在该用户
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("groupId", groupId);
            params.put("userIdCardNumber", idCard);
            List<PunUserBaseInfoVO> users = punUserBaseInfoService.queryResult("eqQueryList", params);
            PunUserBaseInfoVO user = null;
            if (users != null && !users.isEmpty()) {
                if (users.size() != 1) {
                    logger.error("current user " + o.getUserIdCardNumber() + " will add user of |id:" + idCard
                            + ",name:" + name + ",moble:" + mobile + ",email:" + email + "| but duplicate.");
                } else {
                    user = users.get(0);
                }
            } else {
                user = new PunUserBaseInfoVO();
                if (StringUtils.isBlank(password)) {
                    user.setUserPwd(Security.encryptPassword(SC.DEFAULT_PWD));
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
                punUserBaseInfoService.updateUser(user);
                logger.info("current user " + o.getUserIdCardNumber() + " add or update user of |id:" + idCard
                        + ",name:" + name + ",moble:" + mobile + ",email:" + email + "| success.");
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
    public boolean removeUser(String userId) {
        if (StringUtils.isNumeric(userId)) {
            punUserBaseInfoService.delete(Long.parseLong(userId));
            return true;
        } else {
            return false;
        }
    }

    /********************** 数据源获取设置操作 *********************/
    /**
     * 获取数据源
     *
     * @param name 查询时:${modelAlias}_list;保存时:${modelAlias}
     * @return
     */
    public List<Map<String, String>> getDataContainer(String name) {
        DocumentVO doc = ControllerContext.getDoc();
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
        DocumentVO doc = ControllerContext.getDoc();
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
                logger.debug(" getDataItem params is container : " + container + " , item : " + item + " , | value is "
                        + value + " .");
                return value;
            } else {
                logger.debug(" getDataItem params is container : " + container + " , item : " + item
                        + " but  record is null.");
            }
        } else {
            logger.debug(" getDataItem params is container : " + container + " , item : " + item
                    + " but  DataContainer is null.");
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
        DocumentVO doc = ControllerContext.getDoc();
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
     * @param item      itemCode
     * @param index     索引
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
        DocumentVO doc = ControllerContext.getDoc();
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
        DocumentVO doc = ControllerContext.getDoc();
        if (doc != null) {
            if (doc.getListParams().get(name) == null) {
                doc.getListParams().get(name).add(data);
                return true;
            }
        }
        return false;
    }

    /**
     * 返回数据源中 模型属性的值
     *
     * @param dataSource 格式 ${modeCode.modeItemCode}
     * @return
     */
    public String getItemValue(String dataSource) {
        String[] source = dataSource.split("\\.");
        DocumentVO doc = ControllerContext.getDoc();
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
     * @return List<Map       <       String       ,       Object>>
     */
    public List<Map<String, Object>> getDictionary(String sql) {

        List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);
        return map;
    }

    /*************** 时间处理方法 ********************/

    /**
     * 当前时间
     *
     * @param formatStr 时间格式
     * @return
     */
    public String today(String formatStr) {
        Date nowDate = new Date();
        return DateUtils.format(nowDate, formatStr);
    }

    /**
     * 当前时间:yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public String today() {
        return today(DateUtils.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 当前时间 格式：YYYY_MM_DD_HH_MM_SS
     *
     * @return
     */
    public String getDay() {
        return today();
    }

    /**
     * 当前时间 格式：YYYY_MM_DD
     *
     * @return
     */
    public String getYMD() {
        return today(DateUtils.YYYY_MM_DD);
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
     * 获取当前的时间与diff的时间；
     *
     * @param diff (1:表示但前时间后一天 －1:表示但前时间的前一天)
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
     * 计算与当前月相隔多少月的时间
     *
     * @param num
     * @return
     */
    public String nextMonth(int num) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, num);
        SimpleDateFormat farmat = new SimpleDateFormat("yyyyMM");
        return farmat.format(c.getTime());
    }

    /************** 数据源的保存与修改 ***************/
    /**
     * 保存某个数据源中的数据 限定该数据源必须为非自定义类型的
     *
     * @param name
     * @return
     */
    public String saveData(String name) {
        return documentService.saveModelData(ControllerContext.getPage(), ControllerContext.getDoc(), name);
    }

    /**
     * 保存某个数据源中的数据 限定该数据源必须为非自定义类型的
     *
     * @param name
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Map saveDataFlow(String name, boolean masterDateSource) {

        return documentService.saveModelDataFlow(ControllerContext.getPage(), ControllerContext.getDoc(), name,
                masterDateSource);
    }

    /**
     * 将指定数据保存至数据库，限定该数据源必须为非自定义类型的
     *
     * @param map       数据 key是modelItemCode
     * @param modelCode
     * @return
     */
    public String saveModel(Map<String, String> map, String modelCode) {
        return documentService.insertModelData(map, modelCode);
    }

    /**
     * 将指定数据更新至数据库，限定该数据源必须为非自定义类型的
     *
     * @param map       数据 key是modelItemCode
     * @param modelCode
     * @return
     */
    public String updateModel(Map<String, String> map, String modelCode) {
        return documentService.updateModelData(map, modelCode);
    }

    /**
     * 将数据源数据 更新至数据库
     *
     * @param name 数据源名称
     * @return
     */
    public Object updateData(String name) {
        if (documentService.updateModelData(ControllerContext.getPage(), ControllerContext.getDoc(), name)) {
            return true;
        } else {
            return null;
        }
    }

    public Object updateDataFlow(String name) {
        if (documentService.updateModelDataFlow(ControllerContext.getPage(), ControllerContext.getDoc(), name)) {
            return true;
        } else {
            return null;
        }
    }

    /************** sql执行 ****************/

    /**
     * 传入参数执行sql
     *
     * @param sql
     * @param args
     */
    public int excuteUpdate(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    /**
     * 传入参数执行sql并返回自增ID
     *
     * @param sql
     * @param args
     */
    public int updateGetId(String sql, Object... args) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            int length = args.length;
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < length; i++) {
                ps.setObject(i+1,args[i]);
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * 执行sql 得到一个map结果集
     *
     * @param sql
     * @return map
     */
    public Map<String, Object> excuteQuery(String sql, Object... args) {
        List<Map<String, Object>> result = this.excuteQueryForList(sql, args);
        if (result.isEmpty() || result.size() > 1) {
            return null;
        } else {
            return result.get(0);
        }
    }

    /**
	 * 执行sql 得到一个map结果集
	 * 
	 * @param sql
	 * @return map
	 */
	public List<Map<String, Object>> excutePage(String sql, Object... args) {
		HttpServletRequest request = ControllerContext.getRequest();

		String ps = request.getParameter("pageSize");
		String cp = request.getParameter("currentPage");
		int pageSize = Integer.parseInt(StringUtils.isNumeric(ps) ? ps : "5");
		int currentPage = Integer.parseInt(StringUtils.isNumeric(cp) ? cp : "1");
		// 执行分页语句
		List<Map<String, Object>> data = jdbcTemplate
				.queryForList(sql + " limit " + (currentPage - 1) * pageSize + "," + pageSize, args);
		ReturnResult result = ControllerContext.getResult();
		if (data.isEmpty()) {
			result.setData(data).setTotal(0);
			return data;
		}
		// 获取总数
		Object count = jdbcTemplate.queryForObject(MySqlSmartCountUtil.getCountSql(sql, true), args, Object.class);
		result.setData(data).setTotal(count);
		return data;
	}

	/**
     * 执行sql 得到一个list结果集
     *
     * @param sql
     * @return List
     */
    public List<Map<String, Object>> excuteQueryForList(String sql, Object... args) {
        return this.jdbcTemplate.queryForList(sql, args);
    }

    /**
     * 执行sql 得到一个Object
     *
     * @param sql
     * @return Object
     */
    public Object excuteQueryForObject(String sql, Object... args) {
        Map<String, Object> result = this.excuteQuery(sql, args);
        if (result != null && !result.isEmpty() && result.size() == 1) {
            return result.values().iterator().next();
        } else {
            return null;
        }
    }

    private List<Long> getUsersManager(Long userId, boolean isParent) {
        List<PunUserGroupVO> list = null;
        if (isParent) {
            list = punUserGroupService.queryParentManager(userId);
        } else {
            list = punUserGroupService.queryDirectManager(userId);
        }
        List<Long> rtnList = new ArrayList<Long>();
        for (PunUserGroupVO vo : list) {
            rtnList.add(vo.getUserId());
        }
        return rtnList;
    }

    /**
     * 比较页面获取的值与通过sql获取的值是否相等
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public boolean equalsObject(Object obj1, Object obj2) {
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
        String itemValue = ControllerContext.getDoc().getRequestParams().get(name);
        if (StringUtils.isNotBlank(itemValue)) {
            return itemValue;
        } else {
            return "";
        }
    }

    /**
     * 复制数据源中的数据(单个)
     *
     * @param modelCode 模型编号
     * @param item      主键属性名称
     * @param value     主键值
     * @return
     */
    public Map<String, String> copyTableRecord(String modelCode, String item, String value) {
        Map<String, String> ret = new HashMap<String, String>();
        MetaModelVO model = metaModelService.queryByModelCode(modelCode);
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(model.getTableName()).append(" where ").append(item).append(" ='")
                .append(value).append("'");
        Map<String, Object> temp = new HashMap<String, Object>();
        temp = documentService.excuteQuery(sql.toString());
        for (Iterator<java.util.Map.Entry<String, Object>> iterator = temp.entrySet().iterator(); iterator.hasNext(); ) {
            java.util.Map.Entry<String, Object> entry = iterator.next();
            Object valueObject = entry.getValue();
            if (valueObject != null) {
                ret.put(entry.getKey(), valueObject.toString());
            }
        }
        ret.put(item, "");
        String id = documentService.insertModelData(ret, modelCode);
        ret.put(item, id);
        return ret;
    }

    /**
     * 更新某条记录中某个字段的值(主键类型为uuid)
     *
     * @param primarykey 主键名称
     * @param modelCode  模型编号
     * @param props      数据 key是列名，value为值
     * @return
     */
    public boolean updateTableData(String primarykey, String modelCode, Map<String, String> props) {
        MetaModelVO model = metaModelService.queryByModelCode(modelCode);
        StringBuilder sql = new StringBuilder();
        String primarykeyValue = props.get(primarykey);
        sql.append("select * from ").append(model.getTableName()).append(" where ").append(primarykey).append("='")
                .append(primarykeyValue).append("';");
        Map<String, Object> temp = new HashMap<String, Object>();
        temp = documentService.excuteQuery(sql.toString());
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
            return metaModelOperateService.update(ret, modelCode);
        } catch (Exception e) {
            logger.info("ERROR", e);
        }
        return false;
    }

    /**
     * 保存当前document文档，返回Id
     *
     * @return ID
     */
    public String saveDocument() {
        return documentService.save(ControllerContext.getDoc());
    }

    /**
     * 删除个数据源中的数据 限定该数据源必须为非自定义类型的
     *
     * @param page
     * @param recordId
     * @return
     */
    public boolean deleteData(DynamicPageVO page, String recordId) {
        return documentService.deleteModelData(page, recordId);
    }

    /***
     * 执行Http请求
     *
     * @param method
     *            请求方式: post/get
     * @param url
     *            请求路径
     * @param params
     *            参数
     * @return
     */
    public String executeHttpRequest(String method, String url, Map<String, Object> params) {
        if (params == null) {
            params = Collections.EMPTY_MAP;
        }
        if ("post".equals(method.toLowerCase())) { // post 请求
            return HttpUtils.sendPost(url, params);
        } else {
            return HttpUtils.sendGet(url, params);
        }
    }

    @SuppressWarnings("deprecation")
    public boolean isLegalIdCard(String cardNo, String realName) {
        if (this.jdbcTemplate.queryForObject("select count(1) from awcp_id_card_cache where cardno=? and realname=?",
                Integer.class, cardNo, realName) == 1) {
            return true;
        }
        HttpGet get = new HttpGet("http://aliyunverifyidcard.haoservice.com/idcard/VerifyIdcardv2?cardNo=" + cardNo
                + "&realName=" + URLEncoder.encode(realName));
        Header header = new BasicHeader("Authorization", "APPCODE " + PlatfromProp.getValue("idcard_AppCode"));
        get.addHeader(header);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String json = EntityUtils.toString(entity, "UTF-8");
                JSONObject result = JSON.parseObject(json);
                logger.debug(json);
                if (result.getInteger("error_code") == 0 && result.getJSONObject("result").getBooleanValue("isok")) {
                    this.excuteUpdate("insert into awcp_id_card_cache(cardno,realname,json) values(?,?,?)", cardNo,
                            realName, json);
                    return true;
                }
            }

        } catch (IOException e) {
            logger.debug("ERROR", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 校验银行卡号
     *
     * @param mobile   手机号
     * @param bankcard 卡号
     * @return
     */
    @SuppressWarnings("deprecation")
    public JSONObject isLegalBankIdCard(String bankcard, String mobile) {
        Map<String, Object> user = this.jdbcTemplate.queryForMap(
                "select identity_card,real_name from awcp_user where user_id=?", this.getUser().getUserId());

        HttpGet get = new HttpGet("https://aliyun-bankcard4-verify.apistore.cn/bank4?cardNo="
                + user.get("identity_card") + "&realName=" + URLEncoder
                .encode(String.valueOf(user.get("real_name")) + "&Mobile" + mobile + "&bankcard=" + bankcard));
        Header header = new BasicHeader("Authorization", "APPCODE " + PlatfromProp.getValue("idcard_AppCode"));
        get.addHeader(header);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        try {
            CloseableHttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String json = EntityUtils.toString(entity, "UTF-8");
                JSONObject result = JSON.parseObject(json);
                if (result != null && result.getInteger("error_code") == 0) {
                    return result.getJSONObject("result");
                }
            }

        } catch (IOException e) {
            logger.debug("ERROR", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * 执行流程
     *
     * @param id
     * @param flowId
     * @param pageId
     * @return
     */
    public boolean executeFlow(String id, String flowId, int pageId) {
        Map<String, Object> map = documentService
                .excuteQuery("select id from p_fm_document where RECORD_ID='" + id + "'");
        long workID = 0;
        String docId = null;
        if (!CollectionUtils.isEmpty(map)) {
            docId = (String) map.get("id");
            Document document = Document.get(Document.class, docId);
            workID = Long.parseLong(document.getWorkItemId());
            flowId = document.getWorkflowId();
        } else {
            Document document = new Document();
            docId = UUID.randomUUID().toString();
            document.setId(docId);
            document.setRecordId(id);
            Flow currFlow = new Flow(flowId);
            Work currWK = currFlow.NewWork();
            workID = currWK.getOID();
            document.setWorkflowId(flowId);
            document.setWorkItemId(workID + "");
            document.setDynamicPageId(pageId + "");
            document.setCreated(new Date());
        }
        if (workID != 0) {
            Dev2Interface.Node_SendWork(flowId, workID);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成编号
     *
     * @param prefix 前缀 例如N02018010900001中的NO
     * @param digit  补齐的位数
     * @return
     */
    public synchronized String getNumber(String prefix, int digit, boolean isRandom) {
        String newDate = DateUtils.format(new Date(), "yyyyMMdd");
        prefix = StringUtils.defaultString(prefix);
        String key = prefix + "_serial_" + digit;
        JSONObject json = LocalStorage.get(key, JSONObject.class);
        int atomicInt = 0;
        String today;
        if (json == null) {
            json = new JSONObject();
            today = newDate;
            atomicInt = 1;
        } else {
            atomicInt = json.getInteger("atomicInt");
            today = json.getString("today");
            // 如果当前日期不相等，则重新开始计值
            if (newDate.equals(today)) {
                atomicInt = atomicInt + 1;
            } else {
                today = newDate;
                atomicInt = 1;
            }
        }
        json.put("atomicInt", atomicInt);
        json.put("today", today);
        LocalStorage.set(key, json);
        String num = atomicInt + "";
        // 补齐零位
        String zero = "";
        int len = digit - num.length();
        for (int i = 0; i < len; i++) {
            if (isRandom) {
                zero += (int) (Math.random() * 9);
            } else {
                zero += "0";
            }
        }
        num = zero + num;
        return prefix + newDate + num;
    }

    public synchronized String getNumber(int digit, boolean isRandom) {
        return getNumber(null, digit, isRandom);
    }

    public synchronized String getNumber(String prefix, int digit) {
        return getNumber(prefix, digit, true);
    }

    public synchronized String getNumber(int digit) {
        return getNumber(null, digit, true);
    }

    /**
     * 每月自增编号
     *
     * @param prefix
     * @param digit
     * @param isRandom
     * @return
     */
    public synchronized String getMonthIncNumber(String prefix, int digit, boolean isRandom) {
        String newDate = DateUtils.format(new Date(), "yyyyMM");
        prefix = StringUtils.defaultString(prefix);
        String key = prefix + "_serial_" + digit;
        JSONObject json = LocalStorage.get(key, JSONObject.class);
        int atomicInt = 0;
        String today;
        if (json == null) {
            json = new JSONObject();
            today = newDate;
            atomicInt = 1;
        } else {
            atomicInt = json.getInteger("atomicInt");
            today = json.getString("today");
            // 如果当前日期不相等，则重新开始计值
            if (newDate.equals(today)) {
                atomicInt = atomicInt + 1;
            } else {
                today = newDate;
                atomicInt = 1;
            }
        }
        json.put("atomicInt", atomicInt);
        json.put("today", today);
        LocalStorage.set(key, json);
        String num = atomicInt + "";
        // 补齐零位
        String zero = "";
        int len = digit - num.length();
        for (int i = 0; i < len; i++) {
            if (isRandom) {
                zero += (int) (Math.random() * 9);
            } else {
                zero += "0";
            }
        }
        num = zero + num;
        return prefix + "-" + newDate + "-" + num;
    }

    public synchronized String getMonthIncNumber(String prefix, int digit) {
        return getMonthIncNumber(prefix, digit, false);
    }

    /**
     * 生产一个自增的编号
     *
     * @param prefix
     * @param digit
     * @return
     */
    public synchronized String getIncNumber(String prefix, int digit, boolean isRandom) {
        prefix = StringUtils.defaultString(prefix);
        String key = prefix + "_serial_" + digit;
        JSONObject json = LocalStorage.get(key, JSONObject.class);
        int atomicInt = 0;
        if (json == null) {
            json = new JSONObject();
            atomicInt = 1;
        } else {
            atomicInt = json.getInteger("atomicInt");
            atomicInt++;
        }
        json.put("atomicInt", atomicInt);
        LocalStorage.set(key, json);
        String num = atomicInt + "";
        // 补齐零位
        String zero = "";
        int len = digit - num.length();
        for (int i = 0; i < len; i++) {
            if (isRandom) {
                zero += (int) (Math.random() * 9);
            } else {
                zero += "0";
            }
        }
        num = zero + num;
        return StringUtils.defaultString(prefix) + num;
    }

    public synchronized String getIncNumber(int digit) {
        return getIncNumber(null, digit, false);
    }

    public synchronized String getIncNumber(String prefix, int digit) {
        return getIncNumber(prefix, digit, false);
    }

    public synchronized String getIncNumber(int digit, boolean isRandom) {
        return getIncNumber(null, digit, isRandom);
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public PunUserBaseInfoService getPunUserBaseInfoService() {
        return punUserBaseInfoService;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * 获取Spring管理的bean
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return Springfactory.getBean(beanName);
    }

    public void throwException(String msg) {
        if (msg == null) {
            throw new PlatformException();
        } else {
            throw new PlatformException(msg);
        }

    }

    public void throwException() {
        throwException(null);
    }

    public void setResultMessage(String message) {
        throwException(message);
    }

    /**
     * 门户手机用户消息推送(默认系统消息)
     *
     * @param title
     * @param url      消息链接
     * @param receiver 接收人（IDcardNumber）
     * @return
     */
    public boolean pushSystemNotify(String title, String content, String url, String receiver, String... way) {
        return pushNotify(title, content, null, url, receiver, way);
    }

    /**
     * 门户手机用户消息推送
     *
     * @param title    消息标题
     * @param content  消息内容
     * @param type     消息类型
     * @param url      消息链接
     * @param receiver 接收人（IDcardNumber）
     * @return
     */
    public boolean pushNotify(String title, String content, String type, String url, String receiver, String... way) {

        return punNotificationService.pushNotify(title, content, type, url, receiver, way);
    }

    public Cache getCache() {
        return cache;
    }

    /**
     * 判断当前用户是否有权限处理待办件
     *
     * @param WorkID  工作ID
     * @param FK_Node 节点ID
     * @return
     */
    public boolean isCanDo(String WorkID, String FK_Node) {
        if (StringUtils.isNumeric(WorkID) && StringUtils.isNumeric(FK_Node)) {
            return Dev2Interface.Flow_IsCanDoCurrentWork(null, Integer.parseInt(FK_Node), Long.parseLong(WorkID), ControllerHelper.getUser().getUserIdCardNumber() + "");
        }
        return false;
    }


    /**
     * 判断当前用户是当前流程发起人
     *
     * @param WorkID 工作ID
     * @return
     */
    public boolean isStarter(String WorkID) {
        String userId = this.getUser().getUserIdCardNumber();
        if (StringUtils.isNumeric(WorkID)) {
            return this.jdbcTemplate.queryForObject(
                    "select count(1) from wf_generworkflow where workid=? and starter=? and wfsta<>1 ", Integer.class,
                    WorkID, userId) == 1;
        }
        return false;
    }
}
