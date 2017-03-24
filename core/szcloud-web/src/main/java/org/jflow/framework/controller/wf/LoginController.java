package org.jflow.framework.controller.wf;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jflow.framework.common.model.AjaxJson;
import org.jflow.framework.common.model.TempObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.unit.service.PunGroupService;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.utils.WhichEndEnum;
import org.szcloud.framework.unit.vo.PunGroupVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.common.SC;

import BP.Port.Emp;
import BP.Port.WebUser;

@Controller
@RequestMapping("/WF")
public class LoginController {

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	private PunUserBaseInfoService userService;// 用户Service
	@Autowired
	@Qualifier("punGroupServiceImpl")
	private PunGroupService groupService;// 组service

	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson login(TempObject object, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();

		try {
			// String url = request.getQueryString();
			// HashMap<String, String> map = this.getParamsMap(url, "utf-8");

			// HashMap<String,BaseWebControl> controls =
			// HtmlUtils.httpParser(object.getFormHtml(), request);

			Emp emp = new Emp(object.getLoginName());
			PunUserBaseInfoVO vo = new PunUserBaseInfoVO();
			vo.setOrgCode(SC.ORG_CODE);
			vo.setUserIdCardNumber(object.getLoginName());
			vo.setUserPwd(object.getLoginPass());

			Subject subject = SecurityUtils.getSubject();
			String plainToke = vo.getOrgCode() + "_" + vo.getUserIdCardNumber() + "_"
					+ WhichEndEnum.FRONT_END.getCode();
			UsernamePasswordToken token = new UsernamePasswordToken(plainToke,
					vo.getUserPwd() == null ? "" : vo.getUserPwd());

			try {

				subject.login(token);
				Map<String, Object> gParams = new HashMap<String, Object>();
				gParams.put("orgCode", vo.getOrgCode());
				List<PunGroupVO> groups = groupService.queryResult("eqQueryList", gParams);// 查询组信息
				if (groups.isEmpty())
					throw new UnknownAccountException();
				// 将当前用户和组写入session中
				PunGroupVO currentGroup = groups.get(0);
				PunUserBaseInfoVO currentUser = null;
				List<PunUserBaseInfoVO> users = userService.selectByIDCard(vo.getUserIdCardNumber());// 查询用户信息
				for (PunUserBaseInfoVO user : users) {
					if (currentGroup.getGroupId().equals(user.getGroupId())) {
						currentUser = user;
						break;
					}
				}
				if (currentUser == null)
					throw new UnknownAccountException();
				WebUser.SignInOfGener(emp);
				SessionUtils.addObjectToSession(SessionContants.CURRENT_USER, currentUser);// 用户
				SessionUtils.addObjectToSession(SessionContants.CURRENT_USER_GROUP, groups.get(0));// 组

				// 进入选择系统页面
			} catch (UnknownAccountException uae) {
				j.setSuccess(false);
				j.setMsg("账户名或者密码错误！");
			} catch (IncorrectCredentialsException ice) {
				j.setSuccess(false);
				j.setMsg("账户名或者密码错误！");
			} catch (AuthenticationException e) {
				e.printStackTrace();
				j.setSuccess(false);
				j.setMsg("账户名或者密码错误！");
			} catch (Exception e) {
				e.printStackTrace();
				j.setSuccess(false);
				j.setMsg("账户名或者密码错误！");
			}

			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("验证失败，" + e.getMessage());
			return j;
		}

	}

	@RequestMapping(value = "/Logout", method = RequestMethod.GET)
	public ModelAndView logout(TempObject object, HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mv = new ModelAndView();
		try {
			WebUser.Exit();
			// mv.setViewName("/WF/Login");
			mv.setViewName("redirect:" + "/WF/Login.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	private HashMap<String, String> getParamsMap(String queryString, String enc) {
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		if (queryString != null && queryString.length() > 0) {
			int ampersandIndex, lastAmpersandIndex = 0;
			String subStr, param, value;
			String[] paramPair;
			do {
				ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
				if (ampersandIndex > 0) {
					subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
					lastAmpersandIndex = ampersandIndex;
				} else {
					subStr = queryString.substring(lastAmpersandIndex);
				}
				paramPair = subStr.split("=");
				param = paramPair[0];
				value = paramPair.length == 1 ? "" : paramPair[1];
				try {
					value = URLDecoder.decode(value, enc);
				} catch (UnsupportedEncodingException ignored) {
				}
				paramsMap.put(param, value);
			} while (ampersandIndex > 0);
		}
		return paramsMap;
	}
}
