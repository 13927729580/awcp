package cn.org.awcp.dingding.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import BP.Tools.PinYinF4jUtils;
import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.core.utils.Security;
import cn.org.awcp.dingding.Env;
import cn.org.awcp.dingding.exception.OApiException;
import cn.org.awcp.dingding.helper.AuthHelper;
import cn.org.awcp.dingding.helper.EventChangeHelper;
import cn.org.awcp.dingding.service.DDRequestService;
import cn.org.awcp.dingding.utils.DdRequest;
import cn.org.awcp.dingding.utils.DingTalkEncryptException;
import cn.org.awcp.dingding.utils.DingTalkEncryptor;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.unit.core.domain.PunUserBaseInfo;
import cn.org.awcp.unit.service.PunGroupService;
import cn.org.awcp.unit.service.PunPositionService;
import cn.org.awcp.unit.service.PunRoleInfoService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.service.PunUserGroupService;
import cn.org.awcp.unit.vo.PunGroupVO;
import cn.org.awcp.unit.vo.PunPositionVO;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.unit.vo.PunUserGroupVO;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.exception.PlatformException;

@RestController
@RequestMapping("dingding")
public class DDController {

    private static final Log logger = LogFactory.getLog(DDController.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource(name = "punUserBaseInfoServiceImpl")
    PunUserBaseInfoService userService;

    @Resource(name = "punRoleInfoServiceImpl")
    PunRoleInfoService roleService;

    @Resource(name = "punPositionServiceImpl")
    private PunPositionService punPositionService;

    @Resource(name = "punGroupServiceImpl")
    PunGroupService groupService;

    @Resource(name = "punUserGroupServiceImpl")
    PunUserGroupService usergroupService;

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public ReturnResult config(@RequestParam("url") String url,
                               @RequestParam(value = Env.PARAM_AGENT_ID_NAME, required = false) String agentId) {
        ReturnResult result = ReturnResult.get();
        try {
            agentId = StringUtils.defaultString(agentId, Env.DEFAULT_AGENT_ID);
            String config = AuthHelper.getConfig(url, agentId);
            result.setData(JSON.parse(config));
        } catch (Exception e) {
            logger.debug("ERROR", e);
            result.setStatus(StatusCode.FAIL.setMessage("获取配置失败"));
        }
        return result;
    }

    @RequestMapping(value = "/corpId", method = RequestMethod.GET)
    public ReturnResult corpId() {
        ReturnResult result = ReturnResult.get();
        result.setData(Env.CORP_ID);
        return result;
    }

    @RequestMapping(value = "/spaceid", method = RequestMethod.GET)
    public ReturnResult spaceid(
            @RequestParam(value = "domain", required = false, defaultValue = "attachment") String domain,
            @RequestParam(value = "type", required = false, defaultValue = "add") String type) {
        ReturnResult result = ReturnResult.get();
        if (!"add".equals(type)) {
            DDRequestService.grantSpace(AuthHelper.getAccessToken(), domain, null, "download",
                    ControllerHelper.getUser().getUserIdCardNumber(), "/", null, "300");
            return result;
        }
        JSONObject json = DDRequestService.getSpace(AuthHelper.getAccessToken(), domain, null);
        if (json.get("errcode") == null) {
            DDRequestService.grantSpace(AuthHelper.getAccessToken(), domain, null, type,
                    ControllerHelper.getUser().getUserIdCardNumber(), "/", null, "300");
            result.setData(json.get("spaceid"));
        } else {
            result.setStatus(StatusCode.FAIL.setMessage(json.getString("errmsg")));
        }
        return result;
    }

    @RequestMapping(value = "sso", method = RequestMethod.GET)
    public ReturnResult sso(String code) {
        ReturnResult result = ReturnResult.get();
        // 判断是否已经存在登录用户,存在则直接返回
        PunUserBaseInfoVO current_user = ControllerHelper.getUser();
        if (current_user != null) {
            Map<String, Object> data = new HashMap<String, Object>();
            String userId = current_user.getUserIdCardNumber();
            List<PunUserGroupVO> userGroup = ControllerHelper.getUserGroup();
            if (userGroup != null && !userGroup.isEmpty()) {
                data.put("isAdmin", userGroup.get(0).getIsManager());
            }
            data.put("name", current_user.getName());
            data.put("userid", userId);
            result.setData(data);
            return result;
        } else {
            JSONObject json = DDRequestService.getUserInfo(AuthHelper.getAccessToken(), code);
            if (json == null) {
                logger.info("账号未找到，请检查用户信息！");
                result.setStatus(StatusCode.FAIL.setMessage("账号未找到，请检查用户信息！"));
                return result;
            }
            if (json.get("errcode") == null) {
                String userId = json.getString("userid");
                // 用户不存在则创建用户
                List<PunUserBaseInfoVO> users = userService.selectByIDCard(userId);
                if (users.isEmpty()) {
                    createOrUpdateUser(userId);
                }
                PunUserBaseInfoVO user = ControllerHelper.toLogin(userId, true);
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("isAdmin", json.getBoolean("is_sys"));
                data.put("name", user.getName());
                data.put("userid", userId);
                result.setData(data);
                return result;
            } else {
                result.setStatus(StatusCode.FAIL.setMessage(json.getString("errmsg")));
                return result;
            }

        }

    }

    @RequestMapping(value = "registerEvent")
    public ReturnResult registerEvent(@RequestParam(value = "type", required = false, defaultValue = "false") boolean type) {
        ReturnResult result = ReturnResult.get();
        try {
            String str;
            //是否是注册
            if (type) {
                str = EventChangeHelper.registerEventChange(AuthHelper.getAccessToken(),
                        Arrays.asList("user_add_org", "user_modify_org", "user_leave_org", 
                        		"org_dept_create","org_dept_modify", "org_dept_remove",
                                "label_user_change","label_conf_add","label_conf_del","label_conf_modify"),
                        Env.TOKEN, Env.ENCODING_AES_KEY, ControllerHelper.getBasePath() + "dingding/eventChangeReceive.do");
            } else {
                str = EventChangeHelper.updateEventChange(AuthHelper.getAccessToken(),
                		 Arrays.asList("user_add_org", "user_modify_org", "user_leave_org", 
                         		"org_dept_create","org_dept_modify", "org_dept_remove",
                                "label_user_change","label_conf_add","label_conf_del","label_conf_modify"),
                        Env.TOKEN, Env.ENCODING_AES_KEY, ControllerHelper.getBasePath() + "dingding/eventChangeReceive.do");
            }
            result.setStatus(StatusCode.SUCCESS).setData(str);
        } catch (OApiException e) {
            result.setStatus(StatusCode.FAIL.setMessage(e.getMessage()));
        }
        return result;
    }

    @RequestMapping(value = "sendMessage", method = RequestMethod.POST)
    public ReturnResult sendMessage(@RequestParam("url") String url, @RequestParam("type") String type,
                                    @RequestParam("json") String json, @RequestParam("title") String title, @RequestParam("ids") String ids) {
        ReturnResult result = ReturnResult.get();
        if (DocumentUtils.getIntance().sendMessage(url, type, JSON.parseObject(json, Map.class), title, ids)) {
            result.setStatus(StatusCode.SUCCESS);
            return result;
        }
        result.setStatus(StatusCode.FAIL);
        return result;
    }

    @RequestMapping(value = "eventChangeReceive")
    public JSONObject eventChangeReceive(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /** url中的签名 **/
        String msgSignature = request.getParameter("signature");
        /** url中的时间戳 **/
        String timeStamp = request.getParameter("timestamp");
        /** url中的随机字符串 **/
        String nonce = request.getParameter("nonce");

        /** post数据包数据中的加密数据 **/
        ServletInputStream sis = request.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(sis));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JSONObject jsonEncrypt = JSONObject.parseObject(sb.toString());

        String encrypt = jsonEncrypt.getString("encrypt");

        /** 对encrypt进行解密 **/
        DingTalkEncryptor dingTalkEncryptor = null;
        String plainText = null;
        // 对于DingTalkEncryptor的第三个参数，ISV进行配置的时候传对应套件的SUITE_KEY，普通企业传Corpid
        try {
            dingTalkEncryptor = new DingTalkEncryptor(Env.TOKEN, Env.ENCODING_AES_KEY, Env.CORP_ID);
            plainText = dingTalkEncryptor.getDecryptMsg(msgSignature, timeStamp, nonce, encrypt);
        } catch (DingTalkEncryptException e) {
            e.printStackTrace();
        }
        System.out.println(plainText);
        /** 对从encrypt解密出来的明文进行处理 **/
        JSONObject plainTextJson = JSONObject.parseObject(plainText);
        String eventType = plainTextJson.getString("EventType");
        JSONArray ids;
        StringBuilder builder;
        switch (eventType) {
            case "user_add_org":// 通讯录用户增加 do something
                logger.debug("-----------user_add_org-----------");
                ids = plainTextJson.getJSONArray("UserId");
                for (int i = 0; i < ids.size(); i++) {
                    String userId = ids.getString(i);
                    createOrUpdateUser(userId);
                }
                break;
            case "user_modify_org":// 通讯录用户更改 do something
                logger.debug("-----------user_modify_org-----------");
                ids = plainTextJson.getJSONArray("UserId");
                for (int i = 0; i < ids.size(); i++) {
                    String userId = ids.getString(i);
                    createOrUpdateUser(userId);
                }
                break;
            case "user_leave_org":// 通讯录用户离职 do something
                logger.debug("-----------user_leave_org-----------");
                ids = plainTextJson.getJSONArray("UserId");
                builder = new StringBuilder();
                builder.append("delete from p_un_user_base_info  ");
                for (int i = 0; i < ids.size(); i++) {
                    if (i == 0) {
                        builder.append(" where USER_ID_CARD_NUMBER=? ");
                    } else {
                        builder.append(" and USER_ID_CARD_NUMBER=? ");
                    }
                }
                jdbcTemplate.update(builder.toString(), ids.toArray(new Object[]{}));
                break;
            case "org_admin_add":// 通讯录用户被设为管理员 do something
                break;
            case "org_admin_remove":// 通讯录用户被取消设置管理员 do something
                break;
            case "org_dept_create":// 通讯录企业部门创建 do something
                ids = plainTextJson.getJSONArray("DeptId");
                for (int i = 0; i < ids.size(); i++) {
                    Long id = ids.getLong(i);
                    addOrUpdateGroup(id);
                }
                break;
            case "org_dept_modify":// 通讯录企业部门修改 do something
                ids = plainTextJson.getJSONArray("DeptId");
                for (int i = 0; i < ids.size(); i++) {
                    Long id = ids.getLong(i);
                    addOrUpdateGroup(id);
                }
                break;
            case "org_dept_remove":// 通讯录企业部门删除 do something
                ids = plainTextJson.getJSONArray("DeptId");
                builder = new StringBuilder();
                builder.append("delete from p_un_group  ");
                for (int i = 0; i < ids.size(); i++) {
                    if (i == 0) {
                        builder.append(" where group_id=? ");
                    } else {
                        builder.append(" and group_id=? ");
                    }
                }
                jdbcTemplate.update(builder.toString(), ids.toArray(new Object[]{}));
                break;
            case "label_user_change" : 	//员工角色信息发生变更
            	String action = plainTextJson.getString("action");
            	JSONArray labelIdList = plainTextJson.getJSONArray("LabelIdList");
            	JSONArray userIdList = plainTextJson.getJSONArray("UserIdList");
            	String sql = "";
            	if("add".equals(action)){
            		sql = "insert into dd_user_role(role_id,user_id) values(?,?)";
            	} else if("remove".equals(action)){
            		sql = "delete from dd_user_role where role_id=? and user_id=?";
            	}
            	if(StringUtils.isNotBlank(sql)){
            		for(Object role_id : labelIdList){
            			for(Object user_id : userIdList){
            				jdbcTemplate.update(sql, role_id,user_id);
            			}
            		}
            	}
            	break;
            case "label_conf_add":		//增加角色或者角色组
            case "label_conf_del":		//删除角色或者角色组
            case "label_conf_modify":	//修改角色或者角色组
            	break;
            case "org_remove":// 企业被解散 do something
                break;
            case "check_url":// do something
            default: // do something
                break;
        }

        /** 对返回信息进行加密 **/
        long timeStampLong = Long.parseLong(timeStamp);
        Map<String, String> jsonMap = null;
        try {
            jsonMap = dingTalkEncryptor.getEncryptedMap("success", timeStampLong, nonce);
        } catch (DingTalkEncryptException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.putAll(jsonMap);
        return json;
    }

    private void addOrUpdateGroup(Long id) {
        JSONObject dept = DDRequestService.getDeptById(AuthHelper.getAccessToken(), id + "");
        PunGroupVO group = groupService.findById(id);
        String name = dept.getString("name");
        String deptManagerUseridList = dept.getString("deptManagerUseridList");
        Long parentid = dept.getLong("parentid");
        parentid = parentid == null ? 0L : parentid;
        String order = dept.getString("order");
        if (group != null && group.getGroupId() != null) {
            group.setParentGroupId(parentid);
            group.setNumber(order);
            group.setGroupChName(name);
            groupService.addOrUpdate(group);
            // 同步部门主管
            if (StringUtils.isNotBlank(deptManagerUseridList)) {
                String[] managerUserIds = deptManagerUseridList.split("\\|");
                String sql = "update p_un_user_group set IS_MANAGER=0 where GROUP_ID=?";
                jdbcTemplate.update(sql, id);
                for (String user : managerUserIds) {
                    sql = "update p_un_user_group a left join p_un_user_base_info b ON a.user_id=b.user_id set a.IS_MANAGER=1 where a.GROUP_ID=? and b.USER_ID_CARD_NUMBER=?";
                    jdbcTemplate.update(sql, id, user);
                }
            }
        } else {
            this.jdbcTemplate.update("delete from p_un_group where GROUP_CH_NAME=?", name);
            this.jdbcTemplate.update(
                    "delete from p_un_user_group where GROUP_ID=(select GROUP_ID from p_un_group where GROUP_CH_NAME=? limit 0,1)",
                    name);
            String sql = "insert into p_un_group(group_id,parent_group_id,group_type,group_ch_name,pid,number) values(?,?,?,?,?,?)";
            jdbcTemplate.update(sql, id, parentid, "3", name, parentid, order);
        }
    }

    /**
     * 从钉钉同步用户到系统
     * @return
     */
    @RequestMapping(value = "syncUser", method = RequestMethod.GET)
    public ReturnResult syncUser() {
        ReturnResult result = ReturnResult.get();
        String token = AuthHelper.getAccessToken();
        // 获取钉钉所有部门
        JSONObject deptList = DDRequestService.getDeptList(token);
        if (deptList.containsKey("errcode")) {
            result.setStatus(StatusCode.SUCCESS).setData(-1);
            return result;
        }
        JSONArray depts = deptList.getJSONArray("department");
        for (int i = depts.size() - 1; i >= 0; i--) {
            JSONObject json = depts.getJSONObject(i);
            Long deptId = json.getLong("id");
            // 获取部门下的所有用户
            JSONObject userlist = DDRequestService.getSimpleDeptUser(token, deptId);
            JSONArray users = userlist.getJSONArray("userlist");
            for (int j = users.size() - 1; j >= 0; j--) {
                JSONObject user = users.getJSONObject(j);
                createOrUpdateUser(user.getString("userid"));
            }
        }
        initRole(token);
        result.setStatus(StatusCode.SUCCESS).setData(0);
        return result;
    }

    @SuppressWarnings("rawtypes")
	private void initRole(String accessToken){	
		String json = DdRequest.getRoleList(accessToken);
		if(StringUtils.isNotBlank(json)){
			jdbcTemplate.update("delete from dd_role_group");
			jdbcTemplate.update("delete from dd_role_info");
			jdbcTemplate.update("delete from dd_user_role");
			List list = (List) ((Map) JSON.parseObject(json).get("result")).get("list");
			String insertRoleGroupSql = "insert into dd_role_group(ID,group_name) values(?,?)";
			String insertRoleSql = "insert into dd_role_info(ID,role_name,role_group) values(?,?,?)";
			int tempId = 0;
			for(int i=0;i<list.size();i++){
				Map temp = (Map) list.get(i);
				String group_name = (String) temp.get("group_name");
				List roles = (List) temp.get("roles");
				if(tempId == 0){
					tempId = (int) ((Map)roles.get(0)).get("id") - 1;
				} else{
					tempId++;
				}
				int groupId = tempId;
				jdbcTemplate.update(insertRoleGroupSql, groupId,group_name);
				for(int j=0;j<roles.size();j++){
					Map role = (Map) roles.get(j);
					Integer id = (Integer) role.get("id");
					String role_name = (String) role.get("role_name");
					jdbcTemplate.update(insertRoleSql, id,role_name,groupId);
					intiUserRole(id + "", accessToken);
					tempId = id;
				}				
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void intiUserRole(String role_id,String accessToken){
		String insertSql = "insert into dd_user_role(role_id,user_id) values(?,?)";
		String json = DdRequest.getRoleSimplelist(accessToken, role_id);
		if(StringUtils.isNotBlank(json)){
			List list = (List) ((Map) JSON.parseObject(json).get("result")).get("list");
			for(int i=0;i<list.size();i++){
				String userid = (String) ((Map) list.get(i)).get("userid");
				jdbcTemplate.update(insertSql, role_id,userid);
			}
		}		
	}
    
    /**
     * @param userIdCard
     */
    private void createOrUpdateUser(String userIdCard) {
        JSONObject userInfo = DDRequestService.getUserInfoByUId(AuthHelper.getAccessToken(), userIdCard);
        String sql = "select user_id from p_un_user_base_info where mobile=?";
        String mobile = userInfo.getString("mobile");
        PunUserBaseInfoVO vo = null;
        try {
            long userId = this.jdbcTemplate.queryForObject(sql, Long.class, mobile);
            vo = userService.findById(userId);
            // 根据手机号找的要更新用户名
            vo.setUserIdCardNumber(userIdCard);
        } catch (DataAccessException e) {
            // 如果手机号没找到则根据用户名再查找一次，防止用户更换手机号
            List<PunUserBaseInfoVO> users = this.userService.selectByIDCard(userIdCard);
            if (!users.isEmpty() && users.size() == 1) {
                vo = users.get(0);
                // 更新手机号
                vo.setMobile(mobile);
            }
        }
        // 如果没有该用户则创建
        if (vo == null || vo.getUserId() == null) {
            vo = new PunUserBaseInfoVO();
            vo.setUserIdCardNumber(userIdCard);
            vo.setMobile(mobile);
            setUser(vo, userInfo, false);
            // 同步用户角色和部门
            Map<String, Object> params = new HashMap<>(16);
            addUserRole(vo, params);
            Long uId = userService.addOrUpdateUser(vo);
            params.clear();
            addUserGroup(userInfo, params, uId);
            // 如果存在则更新用户数据
        } else {
            setUser(vo, userInfo, true);
            // 同步用户部门
            Map<String, Object> params = new HashMap<>(16);
            PunUserBaseInfo user = BeanUtils.getNewInstance(vo, PunUserBaseInfo.class);
            user.setId(vo.getUserId());
            user.save();
            addUserGroup(userInfo, params, vo.getUserId());
        }
    }

    private void setUser(PunUserBaseInfoVO vo, JSONObject userInfo, boolean isUpdate) {
        String name = userInfo.getString("name");
        String img = userInfo.getString("avatar");
        String email = userInfo.getString("email");
        vo.setName(name);
        vo.setUserName(PinYinF4jUtils.getPinYin(name));
        vo.setUserHeadImg(img);
        vo.setUserEmail(email);
        if (!isUpdate) {
            vo.setUserPwd(Security.encryptPassword(SC.DEFAULT_PWD));
        }
    }

    private void addUserRole(PunUserBaseInfoVO vo, Map<String, Object> params) {
        // 获取并设置一个默认的角色
        params.put("roleName", "普通用户");
        PageList<PunRoleInfoVO> roles = roleService.queryPagedResult("eqQueryList", params, 0, 1, null);
        if (!roles.isEmpty()) {
            vo.setRoleList(Arrays.asList(roles.get(0).getRoleId()));
        }
    }

    private void addUserGroup(JSONObject userInfo, Map<String, Object> params, Long uId) {
        // 获取一个默认的岗位
        List<PunPositionVO> positions = punPositionService.findAll();
        Long poId = null;
        if (!positions.isEmpty()) {
            poId = positions.get(0).getPositionId();
        }
        JSONArray depts = userInfo.getJSONArray("department");
        int size = depts.size();
        // 删除用户原有的组织
        jdbcTemplate.update("delete from p_un_user_group where user_id=?", uId);
        // 插入新组织
        String sql = "insert into p_un_user_group(USER_ID,GROUP_ID,POSITION_ID,IS_MANAGER) values(?,?,?,?)";
        for (int i = 0; i < size; i++) {
            Long deptId = depts.getLong(i);
            // 同步部门
            addOrUpdateGroup(deptId);
            jdbcTemplate.update(sql, uId, deptId, poId, userInfo.getBoolean("isAdmin") ? 1 : 0);
        }
    }

    /**
     * 获取所有启用的微应用
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getEnableDdApps")
    public Map<String, Object> getEnableDdApps() {
        Map<String, Object> ret = new HashMap<String, Object>();
        String sql = "select t1.ID,t2.ID as typeID," + getTitleField() + ",img,url," + getTypeNameField()
                + " from dd_apps t1 " + "join dd_app_type t2 on t1.type=t2.ID where state=1 order by t2.ID,t1.ID";
        String typeSql = "select ID," + getTypeNameField() + " from dd_app_type";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql); // 按照微应用类型，微应用ID排序
        Map<Integer, List<Map<String, Object>>> apps = new LinkedHashMap<Integer, List<Map<String, Object>>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> temp = list.get(i);
            Integer typeID = (Integer) temp.get("typeID");
            if (apps.containsKey(typeID)) {
                List<Map<String, Object>> tempList = apps.get(typeID);
                tempList.add(temp);
            } else {
                List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
                tempList.add(temp);
                apps.put(typeID, tempList);
            }
        }
        List<Map<String, Object>> typeList = jdbcTemplate.queryForList(typeSql);
        Map<Integer, String> types = new HashMap<Integer, String>();
        for (int i = 0; i < typeList.size(); i++) {
            Map<String, Object> temp = typeList.get(i);
            String typeName = (String) temp.get("typeName");
            Integer typeID = (Integer) temp.get("ID");
            types.put(typeID, typeName);
        }
        ret.put("types", types);
        ret.put("apps", apps);
        return ret;
    }

    private String getTitleField() {
        if (ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE) {
            return "title,chSql as contentSql";
        } else {
            return "enTitle as title,enSql as contentSql";
        }
    }

    private String getTypeNameField() {
        if (ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE) {
            return "typeName";
        } else {
            return "enTypeName as typeName";
        }
    }

    /**
     * 获取所有的微应用
     *
     * @param typeID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllApps")
    public List<Map<String, Object>> getAllApps(String typeID) {
        String pSql = "select ID," + getTitleField() + ",img,state,parentID from dd_apps where type=?";
        String cSql = "select ID," + getTitleField() + ",img,state,parentID from dd_apps "
                + "where parentID in (select ID from dd_apps where type=?)";
        List<Map<String, Object>> pApp = jdbcTemplate.queryForList(pSql, typeID);
        List<Map<String, Object>> cApp = jdbcTemplate.queryForList(cSql, typeID);
        for (int i = 0; i < pApp.size(); i++) {
            Map<String, Object> temp = pApp.get(i);
            temp.put("children", getChildren((Long) temp.get("ID"), cApp));
        }
        return pApp;
    }

    private List<Map<String, Object>> getChildren(Long id, List<Map<String, Object>> list) {
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Long parentID = (Long) list.get(i).get("parentID");
            if (id.equals(parentID)) {
                ret.add(list.get(i));
            }
        }
        return ret;
    }

    /**
     * @param typeID    日志的类型
     * @param creators  创建者
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param hasRead   是否已读
     * @param onlyMe    是否是查看自己的
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getWorkLogList")
    public List<Map<String, Object>> getWorkLogList(String typeID, String creators, String startTime, String endTime,
                                                    String hasRead, String onlyMe) {
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        String ddUserId = DocumentUtils.getIntance().getUser().getUserIdCardNumber(); // 钉钉的用户ID
        String sql = "";
        if (StringUtils.isNotBlank(typeID)) {// 日志类型
            sql = "select " + getTitleField() + ",url,dynamicPageId from dd_apps where ID=" + typeID;
        } else {
            sql = "select " + getTitleField() + ",url,dynamicPageId from dd_apps where parentID=1";
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String title = (String) map.get("title");
            String url = (String) map.get("url");
            String contentSql = map.get("contentSql") + "";
            if (StringUtils.isNoneBlank(title) && StringUtils.isNoneBlank(contentSql)) {
                if (StringUtils.isNotBlank(onlyMe) && "Y".equals(onlyMe)) { // 查询自己写的日志
                    contentSql = contentSql.replace("select", "select distinct ID,Createtime,Creatorname,"
                            + "(select USER_HEAD_IMG from p_un_user_base_info where USER_ID_CARD_NUMBER=Creator) as headImg,");
                    contentSql += " where State='1' and Creator='" + ddUserId + "'";
                    if (StringUtils.isNotBlank(startTime)) {
                        sql += " and DATE_FORMAT(Createtime,'%Y-%m-%d')>='" + startTime + "'";
                    }
                    if (StringUtils.isNotBlank(endTime)) {
                        sql += " and DATE_FORMAT(Createtime,'%Y-%m-%d')<='" + endTime + "'";
                    }
                } else { // 查看发送给我的日志
                    contentSql = contentSql.replace("select", "select distinct t1.ID,t2.isRead,Createtime,Creatorname,"
                            + "(select USER_HEAD_IMG from p_un_user_base_info where USER_ID_CARD_NUMBER=Creator) as headImg,");
                    contentSql += " t1 join dd_log_read_info t2 on t1.ID=t2.fkID";
                    String where = " where 1=1 and State='1' and Creator<>'" + ddUserId + "' and t2.userId='" + ddUserId + "'";
                    if (StringUtils.isNotBlank(startTime)) {// 查看发送给我的日志(开始时间条件)
                        where += " and DATE_FORMAT(Createtime,'%Y-%m-%d')>='" + startTime + "'";
                    }
                    if (StringUtils.isNotBlank(endTime)) { // 查看发送给我的日志(结束时间条件)
                        where += " and DATE_FORMAT(Createtime,'%Y-%m-%d')<='" + endTime + "'";
                    }
                    if (StringUtils.isNotBlank(creators)) { // 查看发送给我的日志(发送人条件)
                        where += " and Creator in ('" + creators.replaceAll(",", "','") + "')";
                    }
                    if (StringUtils.isNotBlank(hasRead) && "N".equals(hasRead)) { // 查看发送给我的日志(未读的日志)
                        where += " and t2.isRead='N'";
                    }
                    contentSql += where;
                }
                List<Map<String, Object>> logList = jdbcTemplate.queryForList(contentSql);
                for (int j = 0; j < logList.size(); j++) {
                    Map<String, Object> temp = logList.get(j);
                    temp.put("title", title);
                    temp.put("url", url);
                    ret.add(temp);
                }
            }
        }
        ret.sort((map1, map2)->{
            Date createTime1 = (Date) map1.get("Createtime");
            Date createTime2 = (Date) map2.get("Createtime");
            if (createTime1.getTime() > createTime2.getTime()) {
                return -1;
            } else if (createTime1.getTime() == createTime2.getTime()) {
                return 0;
            } else {
                return 1;
            }
        });
        return ret;
    }

    /**
	 * 根据预设流程获取执行人
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value="/getFlowExcutor")
	public ReturnResult getFlowExcutor(HttpServletRequest request){
		ReturnResult result = new ReturnResult();
		String dynamicPageId = request.getParameter("dynamicPageId");
		if(StringUtils.isNotBlank(dynamicPageId)){
			try{
				List<String> list = new ArrayList<String>();
				String sql = "select JSON from p_fm_flow where PAGE_ID="
						+ "(select dynamicPageId from dd_apps where pcDynamicPageId=? or dynamicPageId=?)";
				String json = jdbcTemplate.queryForObject(sql,String.class, dynamicPageId,dynamicPageId);
				if(StringUtils.isNotBlank(json)){
					JSONArray arr = JSON.parseArray(json);
					List<String> leaders = getLeader();
					int size = leaders.size();
					for(Object obj : arr){
						Map<String,Object> map = (Map<String,Object>)obj;
						Object val = map.get("val");
						Object type = map.get("type");
						if("1".equals(type)){
							if(size == 0){
								//throw new PlatformException("没有找到该用户的主管");
								continue;
							} else{
								int level = Integer.parseInt(val + "");
								if(level > size){
									list.add(leaders.get(size - 1));
								} else{
									list.add(leaders.get(level - 1));
								}
							}
						} else if("3".equals(type)){
							List<String> tempList = JSON.parseArray(val + "", String.class);
							list.add(StringUtils.join(tempList.iterator(), ","));
						} else if("4".equals(type)){
							String role_id = (String) ((Map) val).get("role_id");
							//String role_name = (String) ((Map) val).get("role_name");
							List<String> rolelist = getUserInRole(role_id);
							if(rolelist.size() == 0){
								continue;
								//throw new PlatformException("没有找到拥有" + role_name + "角色的用户");
							} else{
								list.add(StringUtils.join(rolelist.iterator(), ","));
							}
						} else if("5".equals(type)){
							list.add(DocumentUtils.getIntance().getUser().getUserIdCardNumber());
						}
					}
					result.setData(list);
					if(list.size() == 0){
						throw new PlatformException("没有找到任何流程执行人");
					}
					return result;
				} else{
					throw new PlatformException("没有预设流程");
				}				
			} catch(DataAccessException e){
				e.printStackTrace();
				throw new PlatformException("Sql执行错误");
			}		
		} else{
			throw new PlatformException("没有关联动态页面ID");
		}	
	}
	
	//查找该用户的所有主管
	private List<String> getLeader(){
		Long userId = DocumentUtils.getIntance().getUser().getUserId();
		List<String> list = new ArrayList<String>();
		long groupId = jdbcTemplate.queryForObject("select GROUP_ID from p_un_user_group where USER_ID=? limit 1", 
				Long.class,userId);	//当前用户的部门
		String getLeaderSql = "select ifnull(GROUP_CONCAT(USER_ID_CARD_NUMBER),'') from p_un_user_base_info where USER_ID in "
				+ "(select USER_ID from p_un_user_group where IS_MANAGER='1' and GROUP_ID=?)";	
		String leader = jdbcTemplate.queryForObject(getLeaderSql, String.class,groupId);//当前用户所在部门的主管
		if(StringUtils.isNotBlank(leader)){
			list.add(leader);
		}
		String getGroupSql = "select PARENT_GROUP_ID from p_un_group where GROUP_ID=?";
		while(groupId!=1){//查找父部门的主管
			groupId = jdbcTemplate.queryForObject(getGroupSql,Long.class,groupId);
			leader = jdbcTemplate.queryForObject(getLeaderSql, String.class,groupId);
			if(StringUtils.isNotBlank(leader)){
				list.add(leader);
			}
		}	
		return list;
	}

	//获取某个角色的用户
	private List<String> getUserInRole(String roleId){
		String sql = "select user_id from dd_user_role where role_id=?";
		return jdbcTemplate.queryForList(sql,String.class,roleId);
	}

}
