package cn.org.awcp.unit.controller;

import cn.org.awcp.core.utils.Security;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
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
import cn.org.awcp.venson.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户登录
 */
@Controller
@RequestMapping("/")
@Api(value = "登录接口", description = "登录，退出，获取登录用户信息等")
public class UnitBaseController {

    /**
     * 日志对象
     */
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    @Qualifier("punUserBaseInfoServiceImpl")
    private PunUserBaseInfoService userService;

    @Autowired
    @Qualifier("punMenuServiceImpl")
    private PunMenuService resouService;

    @Autowired
    private MetaModelOperateService metaModelOperateServiceImpl;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录
     *
     * @param userPwd  用户密码
     * @param userName USER_ID_CARD_NUMBER或者手机号
     * @param code
     * @param ALT
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/appLogin", method = RequestMethod.POST)
    @ApiOperation(value = "登录")
    public ReturnResult appLogin(@ApiParam(value = "客户端类型") @RequestHeader(value = "X-Requested-With", defaultValue = "WEB", required = false) String clientType,
    @ApiParam(value = "密码", required = true) @RequestParam("userPwd") String userPwd, @ApiParam(value = "用户名", required = true) @RequestParam("userName") String userName,
                                 @ApiParam(value = "登录端", defaultValue = "3") @RequestParam(value = "code", defaultValue = "3", required = false) String code,
                                 @ApiParam(value = "是否生成免登密钥") @RequestParam(value = "ALT", defaultValue = "false", required = false) boolean ALT,
                                 @ApiParam(value = "记住我") @RequestParam(value = "rememberMe", defaultValue = "false", required = false) boolean rememberMe) {
        ReturnResult result = ReturnResult.get();
        // 判断是否密码是经过base64加密
        if (userPwd.contains("isAuth")) {
            userPwd = userPwd.substring("isAuth".length());
            userPwd = Security.decodeBASE64(userPwd);
            userPwd = userPwd.substring("BasicAuth:".length());
        }
        Subject subject = SecurityUtils.getSubject();
        String plainToke = userName + ShiroDbRealm.SPLIT + WhichEndEnum.getOperChartType(code).getCode();
        UsernamePasswordToken token = new UsernamePasswordToken(plainToke, userPwd);
        token.setRememberMe(rememberMe);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            return result.setStatus(StatusCode.FAIL).setMessage("帐号不存在！");
        } catch (IncorrectCredentialsException e) {
            return result.setStatus(StatusCode.FAIL).setMessage("密码错误！");
        } catch (LockedAccountException e) {
            return result.setStatus(StatusCode.FAIL).setMessage("帐号已锁定！");
        }
        PunUserBaseInfoVO pvi = (PunUserBaseInfoVO) subject.getPrincipal();
        ControllerHelper.doLoginSuccess(pvi);
        if ("APP".equals(clientType)) {
            String secretKey = ControllerHelper.setSecretKey(pvi, -1);
            Map<String,Object> map=new HashMap<>(2);
            map.put("secretKey",secretKey);
            map.put("uid",pvi.getUserIdCardNumber());
            map.put("sid",SessionUtils.getCurrentSession().getId());
            result.setStatus(StatusCode.SUCCESS).setData(map);
        } else {
            List<PunRoleInfoVO> roles = (List<PunRoleInfoVO>) SessionUtils
                    .getObjectFromSession(SessionContants.CURRENT_ROLES);
            String targetUrl = SC.TARGET_URL[0];
            for (PunRoleInfoVO role : roles) {
                if ("超级后台管理员".equals(role.getRoleName())) {
                    targetUrl = SC.TARGET_URL[1];
                    break;
                }
            }
            result.setStatus(StatusCode.SUCCESS).setData(targetUrl);
            if (ALT) {
                String secretKey = CookieUtil.findCookie(SC.SECRET_KEY);
                if (secretKey == null || !secretKey.equals(pvi.getUserIdCardNumber())) {
                    ControllerHelper.setSecretKey(pvi, -1);
                }
            }
        }
        return result;

    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getUserInfo",method = RequestMethod.GET)
    @ApiOperation(value="当前登录用户信息")
    public ReturnResult getUserInfo() {
        ReturnResult result = ReturnResult.get();
        PunUserBaseInfoVO pvi = ControllerHelper.getUser();
        if (pvi != null) {
            PunUserBaseInfoVO vo = new PunUserBaseInfoVO();
            BeanUtils.copyProperties(pvi, vo, "userPwd");
            result.setStatus(StatusCode.SUCCESS).setData(vo);
        } else {
            result.setStatus(StatusCode.NO_LOGIN).setMessage("用户未登录，请重新登录！");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/userHeadImg", method = RequestMethod.GET)
    @ApiOperation(value="更换当前用户头像")
    public ReturnResult updateUserHeadImg(String userHeadImg) {
        ReturnResult result=ReturnResult.get();
        if (StringUtils.isNotBlank(userHeadImg)) {
            PunUserBaseInfoVO user = ControllerHelper.getUser();
            Long userId = user.getUserId();
            metaModelOperateServiceImpl.updateBySql("update p_un_user_base_info set USER_HEAD_IMG=? where USER_ID=? ",
                    userHeadImg, userId);
            user.setUserHeadImg(userHeadImg);
            Session session = SessionUtils.getCurrentSession();
            session.setAttribute(SessionContants.CURRENT_USER,user);
            DocumentUtils.getIntance().updateSession(session);
            result.setStatus(StatusCode.SUCCESS).setData(userHeadImg);
        }else{
            result.setStatus(StatusCode.FAIL).setMessage("userHeadImg不能为空");
        }
        return result;
    }

    /**
     * 用户菜单点击次数加1
     *
     * @param menuId
     */
    @ResponseBody
    @RequestMapping(value = "shortcut/{id}", method = RequestMethod.GET)
    public void shortcutGet(@PathVariable("id") String menuId) {
        if (!StringUtils.isNumeric(menuId)) {
            return;
        }
        Long userId = ControllerHelper.getUserId();
        Object id = metaModelOperateServiceImpl
                .queryObject("select id from p_un_menu_count where user_id=? and menu_id=?", userId, menuId);
        if (id.equals(0)) {
            metaModelOperateServiceImpl.updateBySql(
                    "insert into p_un_menu_count(user_id,menu_id,click_count) values(?,?,1)", userId, menuId);
        } else {
            metaModelOperateServiceImpl.updateBySql("update p_un_menu_count set click_count=click_count+1 where id=?",
                    id);
        }
    }

    /**
     * 获取用户常用菜单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "shortcuts", method = RequestMethod.GET)
    public ReturnResult shortcuts() {
        ReturnResult result = ReturnResult.get();
        Long userId = ControllerHelper.getUserId();
        List<Map<String, Object>> menus = metaModelOperateServiceImpl.search(
                "SELECT distinct b.MENU_ID id,b.MENU_NAME name,b.MENU_ICON icon,b.MENU_ADDRESS,b.DYNAMICPAGE_ID FROM p_un_menu_count a LEFT JOIN p_un_menu b ON a.menu_id=b.menu_id LEFT JOIN p_un_user_role c ON a.user_id=c.USER_ID LEFT JOIN p_un_role_access d ON c.ROLE_ID=d.ROLE_ID  LEFT JOIN  p_un_resource e ON e.RESOURCE_ID=d.RESOURCE_ID WHERE e.RELATE_RESO_ID=a.menu_id AND a.user_id=? ORDER BY click_count DESC limit 0,8",
                userId);
        // 处理数据格式
        List<Map<String, Object>> menuIds = menus.stream().map(m -> {
            m.put("url", Menu.getUrl((String) m.get("DYNAMICPAGE_ID"), (String) m.get("MENU_ADDRESS")));
            m.put("icon", Menu.getIcon((String) m.get("icon"), "fa-circle-o"));
            m.remove("DYNAMICPAGE_ID");
            m.remove("MENU_ADDRESS");
            return m;
        }).collect(Collectors.toList());
        result.setStatus(StatusCode.SUCCESS).setData(menuIds);
        return result;
    }

    /**
     * 移除用户一个或所有常用菜单
     *
     * @param menuId
     */
    @ResponseBody
    @RequestMapping(value = "shortcut/{id}", method = RequestMethod.DELETE)
    public void shortcutRemove(@PathVariable("id") String menuId) {
        Long userId = ControllerHelper.getUserId();
        // 移除所有
        if ("all".equals(menuId)) {
            metaModelOperateServiceImpl.updateBySql("delete from p_un_menu_count where user_id=?", userId);
        } else {
            if (StringUtils.isNumeric(menuId)) {
                metaModelOperateServiceImpl.updateBySql("delete from p_un_menu_count where user_id=? and menu_id=?",
                        userId, menuId);
            }
        }
    }

    /**
     * 获取PC端用户菜单
     *
     * @return
     */
    @SuppressWarnings("unchecked")
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
        List<PunMenuVO> resoVOs1 = new ArrayList<PunMenuVO>();
        for (PunRoleInfoVO role : roles) {
            List<PunMenuVO> temp = resouService.getPunMenuUserRoleAndSys(userId, role.getRoleId(), sysId);
            resoVOs1.addAll(temp);
        }
        Collections.sort(resoVOs1,(o1,o2)->o1.getMenuSeq() - o2.getMenuSeq());
        List<PunMenuVO> resoVOs = removeDuplicate(resoVOs1);
        // web菜单移除app中间菜单
        resoVOs = resoVOs.stream().filter(menu -> menu.getType() != 2).collect(Collectors.toList());
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

    /**
     * 获取App页面的菜单
     *
     * @return
     */
    @SuppressWarnings("unchecked")
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

    // 去除重复菜单项
    private List<PunMenuVO> removeDuplicate(List<PunMenuVO> list) {
        // 此处去重要使用LinkedHashSet，要保留原有顺序
        HashSet<PunMenuVO> hashSet = new LinkedHashSet<PunMenuVO>(list);
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

    /**
     * 用户登出
     *
     * @return
     */
    @ApiOperation(value="用户登出")
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout()throws IOException {
        ControllerHelper.logout();
    }

    /**
     * 用于单点登录
     *
     * @param uid
     * @param key
     * @param url
     * @return
     */
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
            if (current_user.getUserIdCardNumber().equals(uid)) {
                return new ModelAndView("redirect:" + url);
            }
            // 如果不是当前登录用户则注销当前用户
            else {
                Subject user = SecurityUtils.getSubject();
                user.logout();
            }
        }
        // 如果不存在已登录用户则进行校验key值得合法性
        // 若返回值不为空则为合法操作
        String code=(String)redisUtil.get(SC.SECRET_KEY+uid);
        if (uid.equals(code)) {
            if (ControllerHelper.toLogin(uid, false) != null) {
                return new ModelAndView("redirect:" + url);
            }
        }
        return mv;
    }

}
