package org.szcloud.framework.unit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.szcloud.framework.core.utils.Security;
import org.szcloud.framework.core.utils.SessionUtils;
import org.szcloud.framework.core.utils.Springfactory;
import org.szcloud.framework.core.utils.constants.SessionContants;
import org.szcloud.framework.metadesigner.application.MetaModelOperateService;
import org.szcloud.framework.unit.service.PunMenuService;
import org.szcloud.framework.unit.service.PunUserBaseInfoService;
import org.szcloud.framework.unit.utils.HMACSHA1;
import org.szcloud.framework.unit.utils.WhichEndEnum;
import org.szcloud.framework.unit.vo.PunAJAXStatusVO;
import org.szcloud.framework.unit.vo.PunMenuVO;
import org.szcloud.framework.unit.vo.PunRoleInfoVO;
import org.szcloud.framework.unit.vo.PunSystemVO;
import org.szcloud.framework.unit.vo.PunUserBaseInfoVO;
import org.szcloud.framework.venson.common.SC;
import org.szcloud.framework.venson.controller.base.ControllerHelper;
import org.szcloud.framework.venson.controller.base.ReturnResult;
import org.szcloud.framework.venson.controller.base.StatusCode;
import org.szcloud.framework.venson.entity.Menu;
import org.szcloud.framework.venson.util.HttpUtils;

import TL.ContextHolderUtils;

@Controller
@RequestMapping("/")
public class UnitBaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	private PunUserBaseInfoService userService;// 用户Service
	@Autowired
	@Qualifier("punMenuServiceImpl")
	private PunMenuService resouService;// 资源Service

	@Autowired
	private MetaModelOperateService metaModelOperateServiceImpl;
	private String errorCookie = "errorCookie";

	@ResponseBody
	@RequestMapping(value = "/appLogin")
	public PunAJAXStatusVO appLogin(@ModelAttribute("vo") PunUserBaseInfoVO vo, String valid, Model model,
			HttpServletRequest request) {
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();

		boolean isTokenValid = verifyToken(vo);
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("userIdCardNumber", vo.getUserName());
		Cookie errorCount = ContextHolderUtils.getCookie(errorCookie);
		if (errorCount != null) {
			String value = errorCount.getValue();
			if (Integer.parseInt(value) >= 3) {
				respStatus.setStatus(1);
				respStatus.setMessage("登录错误达3次，禁止登录10分钟！");
				return respStatus;
			}
		}
		PunUserBaseInfoVO pvi = null;
		try {
			pvi = this.userService.queryResult("eqQueryList", m).get(0);
		} catch (Exception e) {
			respStatus.setStatus(1);
			respStatus.setMessage("用户不存在");
			addErrorCount();
			return respStatus;
		}
		Subject subject = SecurityUtils.getSubject();
		String plainToke = pvi.getOrgCode() + "_" + vo.getUserName() + "_" + WhichEndEnum.FRONT_END.getCode();
		if (isTokenValid && !StringUtils.isEmpty(vo.getToken())) {
			plainToke += "_" + vo.getToken();
		}
		UsernamePasswordToken token = new UsernamePasswordToken(plainToke,
				vo.getUserPwd() == null ? "" : vo.getUserPwd());
		// token.setRememberMe(true);// 记住我功能
		try {
			subject.login(token);
			if ("0".equals(pvi.getUserStatus())) {
				respStatus.setStatus(1);
				respStatus.setMessage("用户已禁用");
				return respStatus;
			} else if ("2".equals(pvi.getUserStatus())) {
				respStatus.setStatus(1);
				respStatus.setMessage("用户审核中");
				return respStatus;
			}
			ControllerHelper.doLoginSuccess(pvi);
			respStatus.setStatus(0);
			respStatus.setMessage("登录成功");
			List<PunRoleInfoVO> roles = (List<PunRoleInfoVO>) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_ROLES);
			String targetUrl = SC.TARGET_URL[0];
			for (PunRoleInfoVO role : roles) {
				if (role.getRoleName().equals("超级后台管理员"))
					targetUrl = SC.TARGET_URL[1];
			}
			respStatus.setData(targetUrl);
			if (StringUtils.isNotBlank(request.getParameter("ATL"))) {
				Cookie secretKey = ContextHolderUtils.getCookie(SC.SECRET_KEY);
				if (secretKey == null) {
					int expiry = 30 * 24 * 60 * 60;
					ContextHolderUtils.addCookie(SC.SECRET_KEY, expiry,
							Security.encodeToMD5(pvi.getUserName() + SC.SALT));
					ContextHolderUtils.addCookie(SC.USER_ACCOUNT, expiry, pvi.getUserName());
				}

			}
			// 进入选择系统页面
			return respStatus;
		} catch (Exception e) {
			e.printStackTrace();
			respStatus.setStatus(1);
			ContextHolderUtils.deleteCookie(SC.SECRET_KEY);
			respStatus.setMessage("登录失败，请核实登录信息");
			addErrorCount();
		}
		return respStatus;
	}

	private void addErrorCount() {
		Cookie errorCount = ContextHolderUtils.getCookie(errorCookie);
		if (errorCount != null) {
			String value = errorCount.getValue();
			ContextHolderUtils.addCookie(errorCookie, 60 * 10, Integer.parseInt(value) + 1 + "");
		} else {
			ContextHolderUtils.addCookie(errorCookie, 60 * 10, "1");
		}
	}

	private boolean verifyToken(PunUserBaseInfoVO userVO) {
		boolean valid = false;
		if (StringUtils.isEmpty(userVO.getToken()))
			return valid;
		try {
			String data = userVO.getOrgCode() + "_" + userVO.getUserName() + "_" + userVO.getDate();
			String token = HMACSHA1.getSignature(data, "szsti");
			valid = token.equals(userVO.getToken());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}

	@ResponseBody
	@RequestMapping(value = "getUserInfo")
	public ReturnResult getUserInfo() {
		ReturnResult result = ReturnResult.get();
		PunUserBaseInfoVO pvi = ControllerHelper.getUser();
		if (pvi != null) {
			result.setStatus(StatusCode.SUCCESS).setData(pvi);
		} else {
			result.setStatus(StatusCode.NO_LOGIN.setMessage("用户未登录，请重新登录！"));
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "getUserMenu")
	public ReturnResult getUserMenu() {
		ReturnResult result = ReturnResult.get();
		// 当前系统
		PunSystemVO system = (PunSystemVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
		// 当前用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		List<PunRoleInfoVO> roles = (List<PunRoleInfoVO>) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_ROLES);

		Long userId = user.getUserId();// 用户ID
		Long sysId = system.getSysId();// 系统ID
		// Long roleId = roles.get(0).getRoleId();
		List<PunMenuVO> resoVOs1 = new ArrayList<PunMenuVO>();
		for (PunRoleInfoVO role : roles) {
			List<PunMenuVO> temp = resouService.getPunMenuUserRoleAndSys(userId, role.getRoleId(), sysId);
			resoVOs1.addAll(temp);
		}
		List<PunMenuVO> resoVOs = removeDuplicate(resoVOs1);
		// web菜单移除app中间菜单
		Iterator<PunMenuVO> it = resoVOs.iterator();
		while (it.hasNext()) {
			PunMenuVO punMenuVO = (PunMenuVO) it.next();
			if (punMenuVO.getType() == 2) {
				it.remove();
			}

		}
		// 查找所有的父Id
		Set<Long> pids = new HashSet<Long>();
		for (PunMenuVO vo : resoVOs) {
			pids.add(vo.getParentMenuId());
		}
		// 建立根节点
		Menu menu = new Menu(0, "root", "#", null);
		// 添加子节点
		menu.addChildren(resoVOs, pids, menu);
		result.setStatus(StatusCode.SUCCESS).setData(menu);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "getAppMenu")
	public ReturnResult getAppMenu() {
		ReturnResult result = ReturnResult.get();
		// 当前系统
		PunSystemVO system = (PunSystemVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
		// 当前用户
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
		List<PunRoleInfoVO> roles = (List<PunRoleInfoVO>) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_ROLES);

		Long userId = user.getUserId();// 用户ID
		Long sysId = system.getSysId();// 系统ID
		// Long roleId = roles.get(0).getRoleId();
		List<PunMenuVO> resoVOs1 = new ArrayList<PunMenuVO>();
		for (PunRoleInfoVO role : roles) {
			List<PunMenuVO> temp = resouService.getPunMenuUserRoleAndSys(userId, role.getRoleId(), sysId);
			resoVOs1.addAll(temp);
		}
		List<PunMenuVO> resoVOs = removeDuplicate(resoVOs1);
		// 查找所有app头部菜单
		List<Map<String, Object>> appTop = metaModelOperateServiceImpl.search(
				"SELECT menu_name AS name ,menu_icon AS icon,menu_address url,DYNAMICPAGE_ID DID FROM p_un_menu a WHERE a.TYPE=1");
		// 查找所有app底部菜单
		List<Map<String, Object>> appBottom = metaModelOperateServiceImpl.search(
				"SELECT menu_name AS name ,menu_icon AS icon,menu_address url,DYNAMICPAGE_ID DID FROM p_un_menu a WHERE a.TYPE=3");
		Menu.replaceIconAndUrl(appTop);
		Menu.replaceIconAndUrl(appBottom);

		// 查找所有app中间菜单
		List<Menu> appMiddle = new ArrayList<Menu>();
		for (PunMenuVO vo : resoVOs) {
			if (vo.getType() == 2) {
				Menu menu = new Menu(vo.getMenuName(), Menu.getUrl(vo.getDynamicPageId(), vo.getMenuAddress()),
						Menu.getIcon(vo.getMenuIcon(), "apps/images/icon2.jpg"));
				appMiddle.add(menu);
			}
		}
		resoVOs.clear();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("top", appTop);
		map.put("middle", appMiddle);
		map.put("bottom", appBottom);
		result.setStatus(StatusCode.SUCCESS).setData(map);
		return result;
	}

	/**
	 * 去除重复菜单项
	 * 
	 * @param list
	 * @return
	 */
	private List<PunMenuVO> removeDuplicate(List<PunMenuVO> list) {
		// 此处去重要使用LinkedHashSet，要保留原有顺序
		HashSet<PunMenuVO> hashSet = new LinkedHashSet<PunMenuVO>(list);
		hashSet.addAll(list);
		list.clear();
		list.addAll(hashSet);
		return list;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request) {
		Subject user = SecurityUtils.getSubject();
		user.logout();
		ContextHolderUtils.deleteCookie(SC.SECRET_KEY);
		return new ModelAndView("redirect:" + ControllerHelper.getBasePath());
	}

	@RequestMapping(value = "logsso")
	public ModelAndView logsso(@RequestParam("uid") String uid, @RequestParam("key") String key,
			@RequestParam(value = "url", required = false) String url) {
		String base = ControllerHelper.getBasePath();
		ModelAndView mv = new ModelAndView("redirect:" + base + "login.html");
		url = base + (StringUtils.isBlank(url) ? "atools/index.html" : url);
		// 判断是否已经存在登录用户
		PunUserBaseInfoVO current_user = (PunUserBaseInfoVO) SessionUtils
				.getObjectFromSession(SessionContants.CURRENT_USER);
		if (current_user != null) {
			// 判断当前已登录用户是否是指定的uid用户
			if (current_user.getUserIdCardNumber().equals(uid))
				return new ModelAndView("redirect:" + url);
			// 如果不是当前登录用户则注销当前用户
			else {
				Subject user = SecurityUtils.getSubject();
				user.logout();
			}
		}
		// 如果不存在已登录用户则进行校验key值得合法性
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("uid", uid);
		parameters.put("key", key);
		String body = HttpUtils.sendGet("http://192.168.7.253/services/getUser.aspx", parameters);
		// 若返回值不为空则为合法操作
		if (body != null && !body.isEmpty()) {
			// 进行登录操作
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("userIdCardNumber", uid);
			PunUserBaseInfoService userService = Springfactory.getBean("punUserBaseInfoServiceImpl");
			List<PunUserBaseInfoVO> pvis = userService.queryResult("eqQueryList", m);
			if (pvis.isEmpty()) {
				logger.info("账号未找到，请检查用户信息！");
				return mv;
			}
			PunUserBaseInfoVO pvi = pvis.get(0);
			Subject subject = SecurityUtils.getSubject();
			String plainToke = pvi.getOrgCode() + "_" + pvi.getUserName() + "_" + WhichEndEnum.FRONT_END.getCode();
			UsernamePasswordToken token = new UsernamePasswordToken(plainToke,
					pvi.getUserPwd() == null ? "" : Security.decryptPassword(pvi.getUserPwd()));
			subject.login(token);
			ControllerHelper.doLoginSuccess(pvi);
			return new ModelAndView("redirect:" + url);
		} else {
			return mv;
		}
	}

}
