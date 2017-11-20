package cn.org.awcp.venson.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.org.awcp.common.security.VerifyCodeGenerator;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.service.PunUserRoleService;
import cn.org.awcp.unit.utils.EncryptUtils;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.service.QueryService;
import cn.org.awcp.venson.util.CheckUtils;
import cn.org.awcp.venson.util.CookieUtil;
import cn.org.awcp.venson.util.EmailUtil;
import cn.org.awcp.venson.util.QRCoreUtil;
import cn.org.awcp.venson.util.SMSUtil;
import cn.org.awcp.wechat.util.WeChatUtil;

@RestController
@RequestMapping("anon")
public class AnonController {
	// 日志对象
	private static final Log logger = LogFactory.getLog(AnonController.class);

	@Resource(name = "queryServiceImpl")
	private QueryService query;

	@Resource(name = "metaModelOperateServiceImpl")
	private MetaModelOperateService meta;

	@Autowired
	@Qualifier("punUserRoleServiceImpl")
	PunUserRoleService userRoleService;// 用户角色关联Service

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	private PunUserBaseInfoService userService;// 用户Service

	@Resource(name = "jdbcTemplate")
	private SzcloudJdbcTemplate jdbcTemplate;

	/**
	 * 发送短信验证码
	 * 
	 * @param verifyCode
	 *            验证码
	 * @param phone
	 *            手机号
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "sendCode", method = RequestMethod.GET)
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
	 * @param code
	 *            验证码
	 * @param to
	 *            手机号/邮箱
	 * @return
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
	 * @param phone
	 *            手机号
	 * @return
	 */
	@RequestMapping(value = "checkUser", method = RequestMethod.POST)
	public ReturnResult checkUser(@RequestParam(value = "type", defaultValue = "0", required = false) int type,
			String value) {
		ReturnResult result = ReturnResult.get();

		System.out.println(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + value));
		// 注册的校正
		if (type == 0) {
			if (!CheckUtils.isChinaPhoneLegal(value)) {
				return result.setStatus(StatusCode.FAIL.setMessage("手机号有误"));
			}
			if (this.meta.queryOne("select count(1) from p_un_user_base_info where mobile=?", value) != 0) {
				return result.setStatus(StatusCode.FAIL.setMessage("该号码已被注册"));
			}
		} else if (type == 1) {
			if (!CheckUtils.isLegalUserName(value)) {
				return result.setStatus(StatusCode.FAIL.setMessage("用户名格式有误"));
			}
			if (this.meta.queryOne("select count(1) from p_un_user_base_info where USER_ID_CARD_NUMBER=?",
					value) != 0) {
				return result.setStatus(StatusCode.FAIL.setMessage("该用户名已被注册"));
			}
		} else if (type == 2) {
			if (!CheckUtils.isLegalEmail(value)) {
				return result.setStatus(StatusCode.FAIL.setMessage("邮箱格式有误"));
			}
			if (this.meta.queryOne("select count(1) from p_un_user_base_info where USER_EMAIL=?", value) != 0) {
				return result.setStatus(StatusCode.FAIL.setMessage("该邮箱已被注册"));
			}
		}
		result.setStatus(StatusCode.SUCCESS);
		return result;
	}

	// private long demander = 115L;
	// private long developer = 116L;
	// private long teclPartner = 117L;
	private long cityPartner = 118L;

	@RequestMapping(value = "registerUser", method = RequestMethod.POST)
	public ReturnResult registerUser(@RequestParam("code") String code, @RequestParam("name") String name,
			@RequestParam("phone") String phone, @RequestParam("password") String password,
			@RequestParam("role") long role, @RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "invite", required = false) String invite,
			@RequestParam(value = "openid", required = false) String openid) {
		ReturnResult result = ReturnResult.get();
		if (!code.equals(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + phone))) {
			// 验证码错误
			return result.setStatus(StatusCode.FAIL.setMessage("验证码错误"));
		}
		try {
			jdbcTemplate.beginTranstaion();
			// 该号码可以使用
			PunUserBaseInfoVO vo = new PunUserBaseInfoVO();
			vo.setUserIdCardNumber(name);
			vo.setGroupId(SC.GROUP_ID);// 设置所属组织ID
			vo.setMobile(phone);
			// 随机姓名
			vo.setUserName(VerifyCodeGenerator.getInstance().getRandString());
			vo.setUserPwd(EncryptUtils.encrypt(password));// 密码加密
			vo.setOrgCode(SC.ORG_CODE);
			// 插入角色
			vo.setRoleList(Arrays.asList(role));
			// 将选择角色保存进cookie
			CookieUtil.addCookie("awcp_user_role", role + "");
			Long userId = userService.addOrUpdateUser(vo);
			// 增加注册消息推送
			DocumentUtils.getIntance().pushNotifyUser("register", userId + "");
			// 关联企业用户
			result.setStatus(StatusCode.SUCCESS);
			jdbcTemplate.commit();
		} catch (Exception e) {
			result.setStatus(StatusCode.FAIL.setMessage("注册失败"));
			logger.debug("ERROR", e);
			try {
				jdbcTemplate.rollback();
			} catch (Exception e1) {
			}
		}

		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param SMSCode
	 *            短信验证码
	 * @param phone
	 *            手机号
	 * @param password
	 *            密码
	 * @return
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
	 * 自动登录与注册
	 * 
	 * @param openid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/autoLogin")
	public String autoLogin(String code) {
		String openid = WeChatUtil.getOpenid(code);
		String sql = "select t1.USER_ID_CARD_NUMBER from p_un_user_base_info t1 join awcp_user t2 "
				+ "on t1.USER_ID=t2.user_id where openid=?";
		try {
			String userAccount = jdbcTemplate.queryForObject(sql, String.class, openid);
			ControllerHelper.toLogin(userAccount, true);
			return "1";
		} catch (Exception e) {
			logger.debug("ERROR", e);
			return "0";
		}
	}

	@RequestMapping(value = "createQRCode", method = RequestMethod.GET)
	public void createQRCode(String content, HttpServletResponse response) {
		try {
			QRCoreUtil.create(500, 500, content, response.getOutputStream());
		} catch (IOException e) {
			logger.debug("ERROR", e);
		}
	}
}
