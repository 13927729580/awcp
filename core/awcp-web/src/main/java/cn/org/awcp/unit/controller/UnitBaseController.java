package cn.org.awcp.unit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import TL.ContextHolderUtils;
import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.Security;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.unit.service.PunMenuService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.shiro.ShiroDbRealm;
import cn.org.awcp.unit.utils.WhichEndEnum;
import cn.org.awcp.unit.vo.PunMenuVO;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.entity.Menu;
import cn.org.awcp.venson.util.CookieUtil;
import cn.org.awcp.venson.util.HttpUtils;
import cn.org.awcp.venson.util.MD5Util;

@Controller
@RequestMapping("/")
public class UnitBaseController {

	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	@Qualifier("punUserBaseInfoServiceImpl")
	private PunUserBaseInfoService userService;// 用户Service
	@Autowired
	@Qualifier("punMenuServiceImpl")
	private PunMenuService resouService;// 资源Service

	@Autowired
	private MetaModelOperateService metaModelOperateServiceImpl;

	@ResponseBody
	@RequestMapping(value = "/appLogin", method = RequestMethod.POST)
	public ReturnResult appLogin(@RequestParam("userPwd") String userPwd, @RequestParam("userName") String userName,
			@RequestParam(value = "code", defaultValue = "3", required = false) String code,
			@RequestParam(value = "ALT", required = false) String ALT, String openid) {
		ReturnResult result = ReturnResult.get();
		// 判断是否密码是经过base64加密
		if (userPwd.contains("isAuth")) {
			userPwd = userPwd.substring("isAuth".length());
			userPwd = Security.decodeBASE64(userPwd);
			userPwd = userPwd.substring("BasicAuth:".length());
		}
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("userIdCardNumber", userName);
		PunUserBaseInfoVO pvi = null;
		try {
			pvi = this.userService.queryResult("eqQueryList", m).get(0);
		} catch (Exception e) {
			return result.setStatus(StatusCode.FAIL.setMessage("登录失败，请核实登录信息"));
		}
		Subject subject = SecurityUtils.getSubject();
		String plainToke = pvi.getOrgCode() + ShiroDbRealm.SPLIT + pvi.getUserIdCardNumber() + ShiroDbRealm.SPLIT
				+ WhichEndEnum.getOperChartType(code).getCode() + ShiroDbRealm.SPLIT + pvi.getUserPwd();
		UsernamePasswordToken token = new UsernamePasswordToken(plainToke, userPwd == null ? "" : userPwd);
		try {
			subject.login(token);
			if (SC.USER_STATUS_DISABLED.equals(pvi.getUserStatus())) {
				return result.setStatus(StatusCode.FAIL.setMessage("用户已禁用"));
			} else if (SC.USER_STATUS_DISABLED.equals(pvi.getUserStatus())) {
				return result.setStatus(StatusCode.FAIL.setMessage("用户审核中"));
			}
			ControllerHelper.doLoginSuccess(pvi);

			// setUserOpenid(openid, pvi.getUserId());
			List<PunRoleInfoVO> roles = (List<PunRoleInfoVO>) SessionUtils
					.getObjectFromSession(SessionContants.CURRENT_ROLES);
			String targetUrl = SC.TARGET_URL[0];
			for (PunRoleInfoVO role : roles) {
				if (role.getRoleName().equals("超级后台管理员")) {
					targetUrl = SC.TARGET_URL[1];
					break;
				}

			}
			result.setStatus(StatusCode.SUCCESS).setData(targetUrl);
			if (StringUtils.isNotBlank(ALT)) {
				String secretKey = CookieUtil.findCookie(SC.USER_ACCOUNT);
				if (secretKey == null || !secretKey.equals(pvi.getUserIdCardNumber())) {
					CookieUtil.addCookie(SC.SECRET_KEY,
							MD5Util.getMD5StringWithSalt(pvi.getUserIdCardNumber(), SC.SALT));
					CookieUtil.addCookie(SC.USER_ACCOUNT, pvi.getUserIdCardNumber());
				}

			}
			// 进入选择系统页面
			return result;
		} catch (Exception e) {
			logger.info("ERROR", e);
			return result.setStatus(StatusCode.FAIL.setMessage("登录失败，请核实登录信息"));
		}
	}

	private void setUserOpenid(String openid, Long user_id) {
		if (StringUtils.isNotBlank(openid)) {
			SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");
			String sql = "select ifnull(openid,'') as openid from awcp_user where user_id=?";
			String dbOpenid = jdbcTemplate.queryForObject(sql, String.class, user_id);
			if (StringUtils.isBlank(dbOpenid) || !dbOpenid.equals(openid)) {
				sql = "update awcp_user set openid=? where user_id=?";
				jdbcTemplate.update(sql, openid, user_id);
			}
		}
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
		PunUserBaseInfoVO user = ControllerHelper.getUser();
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
		url = StringUtils.isBlank(url) ? base + "manage/index.html" : url;
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
		parameters.put("APIId", "validSSO");
		String body = HttpUtils.sendGet("http://www.tongyuanmeng.com/awcp/api/executeAPI.do", parameters);
		// 若返回值不为空则为合法操作
		if (body != null && uid.equals(JSON.parseObject(body).getString("data"))) {
			if (ControllerHelper.toLogin(uid, false) != null)
				return new ModelAndView("redirect:" + url);
		}
		return mv;
	}

}
