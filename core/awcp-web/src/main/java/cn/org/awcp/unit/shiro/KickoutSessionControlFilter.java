package cn.org.awcp.unit.shiro;

import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * shiro 强制退出用户
 * 
 * @author venson
 * @version 20180518
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

	/**
	 * 踢出前一个还是后一个
	 */
	private boolean kickoutAfter = false;
	/**
	 * 同个用户最大在线数
	 */
	private int maxSession = 1;

	private ShiroSessionDao sessionDao;

	@Autowired
	private RedisUtil redisUtil;

	public void setKickoutAfter(boolean kickoutAfter) {
		this.kickoutAfter = kickoutAfter;
	}

	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

	public void setSessionDao(ShiroSessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	private static final String KICKOUT_="kickout_";

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue){
		Subject subject= SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		if(session == null) {
			return true;
		}
		// 如果没有登录，直接进行之后的流程
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			return true;
		}
		Serializable sessionId = session.getId();
		if(session.getAttribute("FORCE_LOGOUT") != null){
			return false;
		}
		PunUserBaseInfoVO user=(PunUserBaseInfoVO)subject.getPrincipal();
		Deque<Serializable> deque = (Deque)redisUtil.get(KICKOUT_+user.getUserIdCardNumber());
		if (deque == null) {
			deque = new LinkedList<>();
		}
		// 如果队列里没有此session，放入队列
		if (!deque.contains(sessionId)) {
			deque.push(sessionId);
			redisUtil.set(KICKOUT_+user.getUserIdCardNumber(), deque);
			// 如果队列里的sessionId数超出最大会话数，开始踢人
			while (deque.size() > maxSession) {
				Serializable kickoutSession;
				//判断踢出前者还是后者
				if (kickoutAfter) {
					kickoutSession = deque.removeFirst();
				} else { // 否则踢出前者
					kickoutSession = deque.removeLast();
				}
				redisUtil.set(KICKOUT_+user.getUserIdCardNumber(), deque);
				// 设置会话的kickout属性表示踢出了
				sessionDao.forceout(kickoutSession);
			}
		}

		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		ControllerHelper.logout();
		return false;
	}


}
