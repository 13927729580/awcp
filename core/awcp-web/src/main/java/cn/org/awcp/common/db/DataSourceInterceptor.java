package cn.org.awcp.common.db;

import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.unit.service.PunUserBaseInfoService;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 数据源设置
 *
 * @author venson
 * @version 20180515
 */
public class DataSourceInterceptor extends HandlerInterceptorAdapter {

    /**
     * 数据源cookie键值名称
     */
    private static final String DATA_SOURCE_COOKIE_KEY = "group_name";

    /**
     * 数据源request参数键值名称
     */
    private static final String DATA_SOURCE_REQUEST_KEY = "request_group_name";

    @Autowired
    private DynamicDataSource dataSource;

    @Autowired
    @Qualifier("punUserBaseInfoServiceImpl")
    PunUserBaseInfoService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求连接是否指定数据源
        String dataSourceName = request.getParameter(DATA_SOURCE_REQUEST_KEY);
        if (StringUtils.isNotBlank(dataSourceName) && dataSource.get(dataSourceName) != null) {
            DynamicDataSource.setDataSource(dataSourceName);
            CookieUtil.addCookie(DATA_SOURCE_COOKIE_KEY, dataSourceName);
        } else {
            //从cookie中查找
            dataSourceName = CookieUtil.findCookie(DATA_SOURCE_COOKIE_KEY);
            if (StringUtils.isNotBlank(dataSourceName) && dataSource.get(dataSourceName) != null) {
                DynamicDataSource.setDataSource(dataSourceName);
            } else {
                DynamicDataSource.setDataSource(DynamicDataSource.MASTER_DATA_SOURCE);
            }

        }
        return checkUser();

    }

    /**
     * 检查当前数据源中的数据库是否存在当前用户
     */
    private boolean checkUser() throws IOException {
        Session session = SessionUtils.getCurrentSession();
        PunUserBaseInfoVO user = (PunUserBaseInfoVO) session.getAttribute(SessionContants.CURRENT_USER);
        //查看是否有用户存在
        if (user != null) {
            PunUserBaseInfoVO realUser = userService.findById(user.getUserId());
            //当前数据源中的数据库不存在此用户或当前用户和数据库用户不一致，则退出当前用户
            if (realUser == null || !realUser.equals(user)) {
                ControllerHelper.logout();
                return false;
            }
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        DynamicDataSource.clearDataSource();
    }
}
