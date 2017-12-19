package cn.org.awcp.unit.message;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.springframework.web.context.request.RequestAttributes;

import com.alibaba.fastjson.JSON;

import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.venson.common.SC;
import cn.org.awcp.venson.util.CookieUtil;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 *                 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/websocket/{id}", configurator = WebScoketConfigurator.class)
public class WebSocket {

	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(WebSocket.class);
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	public static ConcurrentHashMap<String, Set<Session>> webSocketSet = new ConcurrentHashMap<>();

	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(@PathParam("id") String id, Session session) {
		// 判断是否属于有效访问
		if (StringUtils.isBlank(id) || "null".equals(id.trim())) {
			close(session);
			return;
		}
		// 判断是否是否登录
		HandshakeRequest request = (HandshakeRequest) session.getUserProperties()
				.get(RequestAttributes.REFERENCE_REQUEST);
		ShiroHttpSession httpSession = (ShiroHttpSession) request.getHttpSession();
		if (httpSession.getAttribute(SessionContants.CURRENT_USER) == null) {
			close(session);
			return;
		}
		// 判断前后端登录用户是否一致
		Map<String, List<String>> headers = request.getHeaders();
		List<String> cookies = headers.get("Cookie");
		String text = cookies.get(0);
		String value = CookieUtil.findCookie(SC.USER_ACCOUNT, text);
		if (!id.equals(value)) {
			close(session);
			return;
		}
		// 将用户放入缓存实例中
		Set<Session> sessions = webSocketSet.get(id);
		if (sessions == null) {
			// 加锁创建实例
			synchronized (WebSocket.class) {
				if (sessions == null) {
					sessions = new HashSet<>();
					webSocketSet.put(id, sessions);
				}
			}
		}
		sessions.add(session);
	}

	/**
	 * 关闭连接
	 * 
	 * @param session
	 */
	private void close(Session session) {
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(@PathParam("id") String id, Session session) {
		// 移除缓存实例
		Set<Session> sessions = webSocketSet.get(id);
		if (sessions != null && !sessions.isEmpty()) {
			sessions.remove(session);
		}

	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 * @param session
	 *            可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		logger.debug(message);
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		logger.info("ERROR", error);
	}

	public static void sendMessage(String userId, Object message) {
		// 查看是否在线
		if (!WebSocket.webSocketSet.containsKey(userId))
			return;
		// 获取用户所有在线客户端
		Set<Session> sessions = WebSocket.webSocketSet.get(userId);
		if (sessions == null || sessions.isEmpty()) {
			WebSocket.webSocketSet.remove(userId);
			return;
		}
		Set<Session> remove = new HashSet<>();
		Iterator<Session> it = sessions.iterator();
		while (it.hasNext()) {
			Session session = (Session) it.next();
			if (session == null || !session.isOpen()) {
				remove.add(session);
				continue;
			}
			// 同步发送
			synchronized (session) {
				try {
					String text = JSON.toJSONString(message);
					session.getBasicRemote().sendText(text);
					logger.debug(text);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 清除无效的用户
		if (!remove.isEmpty()) {
			sessions.removeAll(remove);
		}
	}

}
