package cn.org.awcp.venson.controller;

import cn.jflow.common.util.PingYinUtil;
import cn.org.awcp.common.security.VerifyCodeGenerator;
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
import cn.org.awcp.venson.util.CheckUtils;
import cn.org.awcp.venson.util.EmailUtil;
import cn.org.awcp.venson.util.QRCoreUtil;
import cn.org.awcp.venson.util.SMSUtil;
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

	@Resource(name = "jdbcTemplate")
	private SzcloudJdbcTemplate jdbcTemplate;

	@Resource(name = "punNotificationService")
	private PunNotificationService punNotificationService;

    /**
     * 发送短信验证码
     *
     * @param verifyCode 验证码
     * @param type       0：手机号，1：邮箱
     * @param to         手机号或邮箱
     */
    @RequestMapping(value = "sendCode")
    public ReturnResult sendCode(@RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                 @RequestParam("verifyCode") String verifyCode, @RequestParam("to") String to) {
        ReturnResult result = ReturnResult.get();
        if (!verifyCode.equalsIgnoreCase((String) SessionUtils.getObjectFromSession(SessionContants.VERIFY_CODE))) {
            return result.setStatus(StatusCode.FAIL.setMessage("验证码错误"));
        }
        // 0是发送短信验证码
        String code;
        if (type == 0) {
            if (!CheckUtils.isChinaPhoneLegal(to)) {
                return result.setStatus(StatusCode.FAIL.setMessage("手机号有误"));
            }
            code = SMSUtil.send(to);
            String key = SessionContants.SMS_VERIFY_CODE + to;
            SessionUtils.addObjectToSession(key, code);
        } else {
            if (!CheckUtils.isLegalEmail(to)) {
                return result.setStatus(StatusCode.FAIL.setMessage("邮箱格式有误"));
            }
            code = EmailUtil.sendVerificationEmail(to);
            SessionUtils.addObjectToSession(SessionContants.SMS_VERIFY_CODE + to, code);
        }
        result.setStatus(StatusCode.SUCCESS);
        return result;
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
        if (code.equals(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + to))) {
            result.setStatus(StatusCode.SUCCESS);
        } else {
            // 验证码错误
            result.setStatus(StatusCode.FAIL.setMessage("验证码错误"));
        }
        return result;
    }

    /**
     * 检查手机否是否已经被注册
     *
     * @param type  0：手机号，1：邮箱
     * @param value 手机号或邮箱
     */
    @RequestMapping(value = "checkUser", method = RequestMethod.POST)
    public ReturnResult checkUser(@RequestParam(value = "type", defaultValue = "0", required = false) int type,
                                  String value) {
        ReturnResult result = ReturnResult.get();

        logger.debug(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + value));
        String sql;
        // 注册的校正
        if (type == 0) {
            if (!CheckUtils.isPhoneLegal(value)) {
                return result.setStatus(StatusCode.FAIL.setMessage("手机号有误"));
            }
            sql="select count(1) from p_un_user_base_info where mobile=?";
            if (this.meta.queryOne(sql, value) != 0) {
                return result.setStatus(StatusCode.FAIL.setMessage("该号码已被注册"));
            }
        } else if (type == 1) {
            if (!CheckUtils.isLegalUserName(value)) {
                return result.setStatus(StatusCode.FAIL.setMessage("用户名格式有误"));
            }
            sql="select count(1) from p_un_user_base_info where USER_ID_CARD_NUMBER=?";
            if (this.meta.queryOne(sql,value) != 0) {
                return result.setStatus(StatusCode.FAIL.setMessage("该用户名已被注册"));
            }
        } else if (type == 2) {
            if (!CheckUtils.isLegalEmail(value)) {
                return result.setStatus(StatusCode.FAIL.setMessage("邮箱格式有误"));
            }
            sql="select count(1) from p_un_user_base_info where USER_EMAIL=?";
            if (this.meta.queryOne(sql, value) != 0) {
                return result.setStatus(StatusCode.FAIL.setMessage("该邮箱已被注册"));
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
	@RequestMapping(value = "registerUser", method = RequestMethod.POST)
	public ReturnResult register(@RequestParam("code") String code, @RequestParam("name") String name,
			@RequestParam("phone") String phone, @RequestParam("password") String password) {
		ReturnResult result = ReturnResult.get();
		if (!code.equals(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + phone))) {
			// 验证码错误
			return result.setStatus(StatusCode.FAIL.setMessage("验证码错误"));
		}
        if(checkUser(0,phone).getStatus()!=0){
            return result;
        }
		try {
			// 该号码可以使用
			PunUserBaseInfoVO vo = new PunUserBaseInfoVO();
            String pinyinName = PingYinUtil.getPingYin(name);
            String userName = pinyinName +"_"+ VerifyCodeGenerator.getInstance().getRandString();
			vo.setUserIdCardNumber(userName);
			vo.setName(name);
			vo.setMobile(phone);
			// 随机姓名
			vo.setUserName(userName);
			vo.setUserPwd(EncryptUtils.encrypt(password));
			userService.addOrUpdateUser(vo);
			// 增加注册消息推送
			punNotificationService.pushNotifyUser("register", userName);
			// 关联企业用户
			result.setStatus(StatusCode.SUCCESS);
		} catch (Exception e) {
			result.setStatus(StatusCode.FAIL.setMessage("注册失败"));
			logger.debug("ERROR", e);
		}

		return result;
	}

    /**
     * 修改密码
     *
     * @param code     短信验证码
     * @param phone    手机号
     * @param password 密码
     */
    @RequestMapping(value = "modifyPassword", method = RequestMethod.POST)
    public ReturnResult modifyPassword(@RequestParam("code") String code, @RequestParam("phone") String phone,
                                       @RequestParam("password") String password) {
        ReturnResult result = ReturnResult.get();
        if (code.equals(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + phone))) {
            // 更新用户密码
            int r = meta.updateBySql("update p_un_user_base_info set USER_PWD=? where MOBILE=?",
                    EncryptUtils.encrypt(password), phone);
            if (r == 1) {
                result.setStatus(StatusCode.SUCCESS);
            } else {
                result.setStatus(StatusCode.FAIL.setMessage("该手机号尚未注册"));
            }
        } else {
            // 验证码错误
            result.setStatus(StatusCode.FAIL.setMessage("验证码错误"));
        }
        return result;
    }

    /**
     * 根据短信验证码登录
     *
     * @param code 验证码
     * @param phone 手机号
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ReturnResult login(@RequestParam("code") String code, @RequestParam("phone") String phone) {
        ReturnResult result = ReturnResult.get();
        if (code.equals(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + phone))) {
            String sql = "select USER_ID_CARD_NUMBER from p_un_user_base_info  where MOBILE=?";
            try {
                String userAccount = jdbcTemplate.queryForObject(sql, String.class, phone);
                ControllerHelper.toLogin(userAccount, true);
                result.setStatus(StatusCode.SUCCESS);
            } catch (DataAccessException e) {
                logger.debug("ERROR", e);
                result.setStatus(StatusCode.FAIL.setMessage("该手机号尚未注册，请先注册"));
            }
        } else {
            result.setStatus(StatusCode.FAIL.setMessage("验证码错误"));
        }
        return result;

    }


    /**
     * 创建二维码
     * @param content 二维码内容
     */
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
