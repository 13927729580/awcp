package org.szcloud.framework.venson.controller;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.szcloud.framework.common.security.VerifyCodeGenerator;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.service.PunUserRoleService;
import org.szcloud.framework.unit.utils.EncryptUtils;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.common.SC;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;
import org.szcloud.framework.venson.service.QueryService;
import org.szcloud.framework.venson.util.CheckUtils;
import org.szcloud.framework.venson.util.SMSUtil;

@RestController
@RequestMapping("anon")
public class AnonController {
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

	/**
	 * 发送短信验证码
	 * 
	 * @param verifyCode
	 *            验证码
	 * @param mobile
	 *            手机号
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "getSMSVerifyCode")
	public ReturnResult getSMSVerifyCode(@RequestParam("verifyCode") String verifyCode,
			@RequestParam("mobile") String mobile) {
		ReturnResult result = ReturnResult.get();
		if (!CheckUtils.isChinaPhoneLegal(mobile)) {
			return result.setStatus(StatusCode.SUCCESS.setMessage("手机号有误")).setData(1);
		}
		if (!verifyCode.equalsIgnoreCase((String) SessionUtils.getObjectFromSession(SessionContants.VERIFY_CODE))) {
			return result.setStatus(StatusCode.SUCCESS.setMessage("验证码错误")).setData(1);
		}
		String code = SMSUtil.send(mobile);
		SessionUtils.addObjectToSession(SessionContants.SMS_VERIFY_CODE + mobile, code);
		result.setStatus(StatusCode.SUCCESS).setData(0);
		return result;
	}

	/**
	 * 校验短信验证码
	 * 
	 * @param SMSCode
	 *            短信验证码
	 * @param mobile
	 *            手机号
	 * @return
	 */
	@RequestMapping("checkSMSCode")
	public ReturnResult checkSMSCode(@RequestParam("SMSCode") String SMSCode, @RequestParam("mobile") String mobile) {
		ReturnResult result = ReturnResult.get();
		if (SMSCode.equals(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + mobile))) {
			result.setData(0);
		} else {
			// 验证码错误
			result.setData(1).setStatus(StatusCode.SUCCESS.setMessage("短信验证码错误"));
		}
		return result;
	}

	/**
	 * 检查手机否是否已经被注册
	 * 
	 * @param mobile
	 *            手机号
	 * @return
	 */
	@RequestMapping("checkNumber")
	public ReturnResult checkNumber(String mobile) {
		ReturnResult result = ReturnResult.get();
		// 注册的校正
		int count = this.meta.queryOne("select count(1) from p_un_user_base_info where mobile=?", mobile);
		if (count == 0) {
			// 该号码可以使用
			result.setData(0);
		} else {
			// 该号码已被注册
			result.setData(1).setStatus(StatusCode.SUCCESS.setMessage("该号码已被注册"));
		}
		return result;
	}

	@RequestMapping("registerUser")
	public ReturnResult registerUser(@RequestParam("SMSCode") String SMSCode, @RequestParam("mobile") String mobile,
			@RequestParam("password") String password, @RequestParam("role") long role) {
		ReturnResult result = ReturnResult.get();
		if (SMSCode.equals(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + mobile))) {
			// 该号码可以使用
			PunUserBaseInfoVO vo = new PunUserBaseInfoVO();
			vo.setUserIdCardNumber(UUID.randomUUID().toString());
			vo.setGroupId(SC.GROUP_ID);// 设置所属组织ID
			vo.setMobile(mobile);
			// 随机姓名
			vo.setUserName(VerifyCodeGenerator.getInstance().getRandString());
			vo.setUserPwd(EncryptUtils.encrypt(password));// 密码加密
			vo.setOrgCode(SC.ORG_CODE);
			vo.setRoleList(Arrays.asList(role));
			userService.addOrUpdateUser(vo);
			// 关联企业用户
			result.setData(0).setStatus(StatusCode.SUCCESS);
		} else
			// 验证码错误
			result.setData(1).setStatus(StatusCode.SUCCESS.setMessage("验证码错误"));

		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param SMSCode
	 *            短信验证码
	 * @param mobile
	 *            手机号
	 * @param password
	 *            密码
	 * @return
	 */
	@RequestMapping("modifyPassword")
	public ReturnResult modifyPassword(@RequestParam("SMSCode") String SMSCode, @RequestParam("mobile") String mobile,
			@RequestParam("password") String password) {
		ReturnResult result = ReturnResult.get();
		if (SMSCode.equals(SessionUtils.getObjectFromSession(SessionContants.SMS_VERIFY_CODE + mobile))) {
			// 更新用户密码
			meta.updateBySql("update p_un_user_base_info set USER_PWD=? where MOBILE=?", EncryptUtils.encrypt(password),
					mobile);
			result.setData(0);
		} else {
			// 验证码错误
			result.setData(1).setStatus(StatusCode.SUCCESS.setMessage("验证码错误"));
		}
		return result;
	}

}
