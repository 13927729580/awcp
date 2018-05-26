package cn.org.awcp.unit.shiro;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.Criteria;
import cn.org.awcp.core.utils.Security;
import cn.org.awcp.unit.service.PunRoleAccessService;
import cn.org.awcp.unit.service.PunRoleInfoService;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.vo.PunRoleAccessVO;
import cn.org.awcp.unit.vo.PunRoleInfoVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.util.CookieUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

public class ShiroDbRealm extends AuthorizingRealm {
    /**
     * 日志对象
     */
    private static final Log logger = LogFactory.getLog(ShiroDbRealm.class);

    @Autowired
    @Qualifier("punUserBaseInfoServiceImpl")
    private PunUserBaseInfoService userService;

    @Autowired
    @Qualifier("punRoleInfoServiceImpl")
    private PunRoleInfoService roleService;

    public final static String SPLIT = "-_-";

    @Autowired
    @Qualifier("punRoleAccessServiceImpl")
    private PunRoleAccessService roleAccessService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return frontAuthorizationInfo();
    }

    /**
     * 前端授权
     *
     * @return
     */
    private SimpleAuthorizationInfo frontAuthorizationInfo() {
        SimpleAuthorizationInfo frontInfo = new SimpleAuthorizationInfo();
        List<String> roles = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        PunUserBaseInfoVO user = ControllerHelper.getUser();
        Map<String, Object> roleParams = new HashMap<>(2);
        roleParams.put("userId", user.getUserId());

        List<PunRoleInfoVO> roleVos = roleService.queryResult("queryByUser", roleParams);
        for (PunRoleInfoVO vo : roleVos) {
            roles.add(vo.getRoleName());
            roleIds.add(vo.getRoleId());
            logger.debug("roleName:" + vo.getRoleName());
        }
        // 给当前用户设置角色
        frontInfo.addRoles(roles);

        if (!roleIds.isEmpty()) {
            // 获取所有角色的权限
            // 将所有角色的权限置入permission中。
            BaseExample example = new BaseExample();
            Criteria criteria = example.createCriteria();

            criteria.andInLong("ROLE_ID", roleIds);
            List<PunRoleAccessVO> resultsAccessVOs = roleAccessService.selectByExample(example);
            List<String> permissions = new ArrayList<>();
            for (PunRoleAccessVO vo : resultsAccessVOs) {
                // 权限组成模式->操作类型：资源ID
                permissions.add(vo.getOperType() + ":" + vo.getResourceId());
            }
            logger.debug(Arrays.toString(permissions.toArray()));
            // 给当前用户设置权限
            frontInfo.addStringPermissions(permissions);
        }
        return frontInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        return verifyEnd(token);
    }

    /**
     * 根据端验证
     *
     * @return
     */
    private SimpleAuthenticationInfo verifyEnd(UsernamePasswordToken token) {
        String nameAndOrgcodeAndEnd = token.getUsername();
        String[] result = nameAndOrgcodeAndEnd.split(SPLIT);
        // 身份证号码
        String idCard = result[0];
        PunUserBaseInfoVO pvi;
        String password=new String(token.getPassword());
        // 查看是否属于免密校验
        if (result.length > 2) {
            password = result[2];
            pvi = findFrontUser(idCard, null);
        } else {
            pvi = findFrontUser(idCard, password);
        }
        return new SimpleAuthenticationInfo(pvi, password, getName());
    }

    /**
     * 校验前端用户
     *
     * @param idCard
     * @return
     */
    private PunUserBaseInfoVO findFrontUser(String idCard, String password) {
        Map<String, Object> m = new HashMap<>(2);
        m.put("userIdCardNumber", idCard);
        List<PunUserBaseInfoVO> pvis = this.userService.queryResult("eqQueryList", m);
        if (pvis.size() != 1) {
            CookieUtil.deleteCookie(SC.USER_ACCOUNT);
            CookieUtil.deleteCookie(SC.SECRET_KEY);
            throw new UnknownAccountException();
        }
        PunUserBaseInfoVO vo = pvis.get(0);
        if (password != null) {
            if (!password.equals(Security.decryptPassword(vo.getUserPwd()))) {
                throw new IncorrectCredentialsException();
            }
        }
        if (SC.USER_STATUS_DISABLED.equals(vo.getUserStatus())) {
            throw new LockedAccountException();
        }
        return vo;
    }
}
