package cn.org.awcp.venson.controller;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.unit.core.domain.PunSystem;
import cn.org.awcp.unit.message.PunNotificationService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.service.PunUserRoleService;
import cn.org.awcp.unit.utils.EncryptUtils;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("anon")
@Api(value = "免登陆接口", description = "短信验证码，注册，校验用户等")
public class AnonController {
	private static final Log logger = LogFactory.getLog(AnonController.class);

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	@Autowired
	@Qualifier("punUserRoleServiceImpl")
	PunUserRoleService userRoleService;

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	private PunUserBaseInfoService userService;

    @Autowired
	private SzcloudJdbcTemplate jdbcTemplate;

	@Resource(name = "punNotificationService")
	private PunNotificationService punNotificationService;

	@Autowired
	private RedisUtil redisUtil;

    /**
     * 发送验证码
     *
     * @param verifyCode 验证码
     * @param type       0：手机号，1：邮箱
     * @param to         手机号或邮箱
     */
    @RequestMapping(value = "sendCode",method = RequestMethod.GET)
    @ApiOperation(value="根据图片验证码发送验证码")
    public ReturnResult sendCode(@ApiParam(value="类型：0 短信,1 邮箱")@RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                 @ApiParam(value="图片验证码")@RequestParam("verifyCode") String verifyCode, @ApiParam(value="手机号或者邮箱")@RequestParam("to") String to) {
        ReturnResult result = ReturnResult.get();
        if (!verifyCode.equalsIgnoreCase((String) SessionUtils.getObjectFromSession(SessionContants.VERIFY_CODE))) {
            return result.setStatus(StatusCode.FAIL).setMessage("验证码错误");
        }
        if(sendCode(result,type,to)){
            result.setStatus(StatusCode.SUCCESS);
        }
        return result;
    }

    private static final String SMS_CODE_SALT="sljksdf@34#s51";

    /**
     * 发送验证码
     *
     * @param key 验证码
     * @param type       0：手机号，1：邮箱
     * @param to         手机号或邮箱
     */
    @RequestMapping(value = "sendCode",method = RequestMethod.POST)
    @ApiOperation(value="根据约定的key值发送验证码")
    public ReturnResult sendCodeByKey(@ApiParam(value="类型：0 短信,1 邮箱")@RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                 @ApiParam(value="密钥")@RequestParam("key") String key, @ApiParam(value="手机号或者邮箱")@RequestParam("to") String to) {
        ReturnResult result = ReturnResult.get();
        if(!MD5Util.getMD5String(SMS_CODE_SALT+to+System.currentTimeMillis()/60000L).equals(key)){
           return result.setStatus(StatusCode.FAIL).setMessage("key值有误");
        }
        if(sendCode(result,type,to)){
            result.setStatus(StatusCode.SUCCESS);
        }
        return result;
    }
    private static final int SMS_CODE_EXPIRE=60*10;

    public boolean sendCode(ReturnResult result,int type ,String to){
        String key=SessionContants.SMS_VERIFY_CODE + to;
        if(redisUtil.get(key)!=null){
            long expire = redisUtil.getExpire(key) + 60;
            if(expire>SMS_CODE_EXPIRE){
                result.setStatus(StatusCode.FAIL).setMessage("请"+(expire-SMS_CODE_EXPIRE)+"S后再发！");
                return false;
            }
        }
        String code;
        // 0是发送短信验证码
        if (type == 0) {
            if (!CheckUtils.isPhoneLegal(to)) {
                result.setStatus(StatusCode.FAIL).setMessage("手机号有误");
                return false;
            }
            code=SMSUtil.send(to);
        } else {
            if (!CheckUtils.isLegalEmail(to)) {
                result.setStatus(StatusCode.FAIL).setMessage("邮箱格式有误");
                return false;
            }
            code=EmailUtil.sendVerificationEmail(to);
        }
        redisUtil.set(key, code,600);
        return true;
    }

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param to   手机号/邮箱
     */
    @RequestMapping(value = "checkCode", method = RequestMethod.POST)
    public ReturnResult checkCode(@RequestParam("code") String code, @RequestParam("to") String to) {
        ReturnResult result = ReturnResult.get();
        if (code.equals(redisUtil.get(SessionContants.SMS_VERIFY_CODE + to))) {
            result.setStatus(StatusCode.SUCCESS);
        } else {
            // 验证码错误
            result.setStatus(StatusCode.FAIL).setMessage("验证码错误");
        }
        return result;
    }

    /**
     * 检查用户信息是否已经被注册
     *
     * @param type  0：手机号，1：邮箱
     * @param value 手机号或邮箱
     */
    @ApiOperation(value="检查用户信息是否已经被注册")
    @RequestMapping(value = "checkUser", method = RequestMethod.POST)
    public ReturnResult checkUser(@ApiParam(value="类型：0 手机号,1 用户名,2 邮箱")@RequestParam(value = "type", defaultValue = "0", required = false) int type,
                                  String value) {
        ReturnResult result = ReturnResult.get();
        String sql;
        // 注册的校正
        if (type == 0) {
            if (!CheckUtils.isPhoneLegal(value)) {
                return result.setStatus(StatusCode.FAIL).setMessage("手机号有误");
            }
            sql="select count(1) from p_un_user_base_info where mobile=?";
            if (this.meta.queryOne(sql, value) != 0) {
                return result.setStatus(StatusCode.FAIL).setMessage("该号码已被注册");
            }
        } else if (type == 1) {
            if (!CheckUtils.isLegalUserName(value)) {
                return result.setStatus(StatusCode.FAIL).setMessage("用户名格式有误");
            }
            sql="select count(1) from p_un_user_base_info where USER_ID_CARD_NUMBER=?";
            if (this.meta.queryOne(sql,value) != 0) {
                return result.setStatus(StatusCode.FAIL).setMessage("该用户名已被注册");
            }
        } else if (type == 2) {
            if (!CheckUtils.isLegalEmail(value)) {
                return result.setStatus(StatusCode.FAIL).setMessage("邮箱格式有误");
            }
            sql="select count(1) from p_un_user_base_info where USER_EMAIL=?";
            if (this.meta.queryOne(sql, value) != 0) {
                return result.setStatus(StatusCode.FAIL).setMessage("该邮箱已被注册");
            }
        }
        result.setStatus(StatusCode.SUCCESS);
        return result;
    }

    /**
     * 手机号注册用户
     * @param code 手机验证码
     * @param name 用户名
     * @param phone 手机号
     * @param password 密码
     */
    @ApiOperation(value="手机号注册用户")
	@RequestMapping(value = "registerUser", method = RequestMethod.POST)
	public ReturnResult register(@RequestParam("code") String code, @RequestParam("name") String name,
			@RequestParam("phone") String phone, @RequestParam("password") String password) {
		ReturnResult result = ReturnResult.get();
		if (!code.equals(SessionContants.SMS_VERIFY_CODE + phone)) {
			// 验证码错误
			return result.setStatus(StatusCode.FAIL).setMessage("验证码错误");
		}
        if(checkUser(0,phone).getStatus()!=0||checkUser(1,name).getStatus()!=0){
            return result;
        }
		try {
			// 该号码可以使用
			PunUserBaseInfoVO vo = new PunUserBaseInfoVO();
			vo.setUserIdCardNumber(name);
			vo.setName(name);
			vo.setMobile(phone);
			// 随机姓名
			vo.setUserName(name);
			vo.setUserPwd(EncryptUtils.encrypt(password));
            userService.addOrUpdateUser(vo);
			// 增加注册消息推送
			punNotificationService.pushNotifyUser("register", name);
			// 关联企业用户
			result.setStatus(StatusCode.SUCCESS);
		} catch (Exception e) {
			result.setStatus(StatusCode.FAIL).setMessage("注册失败");
			logger.debug("ERROR", e);
		}

		return result;
	}

    /**
     * 忘记密码
     *
     * @param code     短信验证码
     * @param phone    手机号
     * @param password 密码
     */
    @ApiOperation(value = "忘记密码")
    @RequestMapping(value = "modifyPassword", method = RequestMethod.POST)
    public ReturnResult modifyPassword(@RequestParam("code") String code, @RequestParam("phone") String phone,
                                       @RequestParam("password") String password) {
        ReturnResult result = ReturnResult.get();
        if (code.equals(redisUtil.get(SessionContants.SMS_VERIFY_CODE + phone))) {
            // 更新用户密码
            int r = meta.updateBySql("update p_un_user_base_info set USER_PWD=? where MOBILE=?",
                    EncryptUtils.encrypt(password), phone);
            if (r == 1) {
                result.setStatus(StatusCode.SUCCESS);
            } else {
                result.setStatus(StatusCode.FAIL).setMessage("该手机号尚未注册");
            }
        } else {
            // 验证码错误
            result.setStatus(StatusCode.FAIL).setMessage("验证码错误");
        }
        return result;
    }

    /**
     * 根据短信验证码登录
     *
     * @param code 验证码
     * @param phone 手机号
     */
    @ApiOperation(value = "根据短信验证码登录")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ReturnResult login(@RequestParam("code") String code, @RequestParam("phone") String phone) {
        ReturnResult result = ReturnResult.get();
        if (code.equals(redisUtil.get(SessionContants.SMS_VERIFY_CODE + phone))) {
            String sql = "select USER_ID_CARD_NUMBER from p_un_user_base_info  where MOBILE=?";
            try {
                String userAccount = jdbcTemplate.queryForObject(sql, String.class, phone);
                ControllerHelper.toLogin(userAccount, true);
                result.setStatus(StatusCode.SUCCESS);
            } catch (DataAccessException e) {
                logger.debug("ERROR", e);
                result.setStatus(StatusCode.FAIL).setMessage("该手机号尚未注册，请先注册");
            }
        } else {
            result.setStatus(StatusCode.FAIL).setMessage("验证码错误");
        }
        return result;

    }


    /**
     * 创建二维码
     * @param content 二维码内容
     */
    @ApiOperation(value = "创建二维码")
    @RequestMapping(value = "createQRCode", method = RequestMethod.GET)
    public void createQRCode(String content, HttpServletResponse response) {
        try {
            response.setContentType("image/jpg");
            QRCoreUtil.create(500, 500, content, response.getOutputStream());
        } catch (IOException e) {
            logger.debug("ERROR", e);
        }
    }

    /**
     * 获取系统名称
     *
     */
    @ApiOperation(value = "获取系统名称")
    @RequestMapping(value = "systemName", method = RequestMethod.GET)
    public ReturnResult systemName() {
        ReturnResult result = ReturnResult.get();
        List<PunSystem> system = PunSystem.findAll();
        if (system != null && !system.isEmpty()) {
            result.setData(system.get(0).getSysName());
        } else {
            result.setData("AWCP-全栈配置云开发平台");
        }
        return result;
    }
}
