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
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
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

	@ResponseBody
	@RequestMapping(value = "/appLogin")
	public PunAJAXStatusVO appLogin(@ModelAttribute("vo") PunUserBaseInfoVO vo, String valid, Model model,
			HttpServletRequest request) {
		PunAJAXStatusVO respStatus = new PunAJAXStatusVO();

		boolean isTokenValid = verifyToken(vo);
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("userIdCardNumber", vo.getUserName());

		PunUserBaseInfoVO pvi = null;
		try {
			pvi = this.userService.queryResult("eqQueryList", m).get(0);
		} catch (Exception e) {
			respStatus.setStatus(1);
			respStatus.setMessage("用户不存在");
			ContextHolderUtils.deleteCookie(SC.SECRET_KEY);
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
		} catch (UnknownAccountException uae) {
			respStatus.setStatus(1);
			respStatus.setMessage("用户不存在");
			ContextHolderUtils.deleteCookie(SC.SECRET_KEY);
		} catch (IncorrectCredentialsException ice) {
			respStatus.setStatus(1);
			respStatus.setMessage("登录失败，请核实登录信息");
			ContextHolderUtils.deleteCookie(SC.SECRET_KEY);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			respStatus.setStatus(1);
			respStatus.setMessage("登录失败，请核实登录信息");
			ContextHolderUtils.deleteCookie(SC.SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			respStatus.setStatus(1);
			ContextHolderUtils.deleteCookie(SC.SECRET_KEY);
			respStatus.setMessage("登录失败，请核实登录信息");
		}
		return respStatus;
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
		addChildren(resoVOs, pids, menu);
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

		replaceIconAndUrl(appTop);
		replaceIconAndUrl(appBottom);

		// 查找所有app中间菜单
		List<Menu> appMiddle = new ArrayList<Menu>();
		for (PunMenuVO vo : resoVOs) {
			if (vo.getType() == 2) {
				Menu menu = new Menu(vo.getMenuName(), getUrl(vo.getDynamicPageId(), vo.getMenuAddress()),
						getIcon(vo.getMenuIcon(), "apps/images/icon2.jpg"));
				appMiddle.add(menu);
				// 增加红点数据
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
	 * 替换url和图标
	 */
	private void replaceIconAndUrl(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			Long DID = (Long) map.get("DYNAMICPAGE_ID");
			String oriUrl = (String) map.get("url");
			map.put("url", getUrl(DID, oriUrl));
			String oriIcon = (String) map.get("icon");
			map.put("icon", getIcon(oriIcon, "apps/images/icon2.jpg"));
		}
	}

	/**
	 * 替换url
	 */
	private String getUrl(Long DID, String oriUrl) {
		String url;
		String base = ControllerHelper.getBasePath();
		if (DID != null) {
			url = base + "document/view.do?dynamicPageId=" + DID;
		} else {
			if (StringUtils.isBlank(oriUrl) || oriUrl.trim().equals("#")) {
				url = "#";
			} else {
				if (oriUrl.startsWith("http")) {
					url = oriUrl;
				} else {
					url = base + oriUrl;

				}
			}

		}
		// 如果都为空则显示#

		return url;
	}

	/**
	 * 添加子节点
	 */
	private void addChildren(List<PunMenuVO> resoVOs, Set<Long> pids, Menu menu) {
		for (PunMenuVO vo : resoVOs) {
			// 判断是否为子节点
			if (vo.getType() == 0 && vo.getParentMenuId() == menu.getId()) {
				// 查看图标是否为空，如果为空则显示默认图标
				Menu children = new Menu(vo.getMenuId(), vo.getMenuName(),
						getUrl(vo.getDynamicPageId(), vo.getMenuAddress()),
						getIcon(vo.getMenuIcon(), "images/icon/icon-blue/system.png"));
				children.setFlag(vo.getMenuFlag());
				children.setType(vo.getType());
				menu.add(children);
				// 判断子节点是否为父节点
				if (pids.contains(vo.getMenuId())) {
					addChildren(resoVOs, pids, children);
				}
			}
		}
	}

	/**
	 * 替换图标
	 */
	private String getIcon(String icon, String defaultImg) {
		if (StringUtils.isBlank(icon)) {
			icon = defaultImg;
		}
		return ControllerHelper.getBasePath() + icon;
	}

	private class Menu {

		private long id;
		private String name;
		private String url;
		private String target;
		private String icon;
		private List<Menu> children;
		private int flag;
		private int type;
		private int count;

		public Menu(long id, String name, String url, String icon) {
			this.id = id;
			this.name = name;
			this.url = url;
			this.icon = icon;
			this.children = new ArrayList<UnitBaseController.Menu>();
		}

		public Menu(String name, String url, String icon) {
			this.name = name;
			this.url = url;
			this.icon = icon;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public Menu() {
			this.children = new ArrayList<UnitBaseController.Menu>();
		}

		public void add(Menu menu) {
			this.children.add(menu);
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public List<Menu> getChildren() {
			return children;
		}

		public void setChildren(List<Menu> children) {
			this.children = children;
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

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
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		return new ModelAndView("redirect:" + basePath);
	}

	@RequestMapping(value = "errorHandle")
	private void errorHandle(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();

		logger.debug(url.toString());
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
