package cn.org.awcp.unit.shiro;

import cn.org.awcp.venson.util.RedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 基于redis的sessionDao，缓存共享session
 * @author venson
 * @version 20180517
 */
public class ShiroSessionDao extends CachingSessionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroSessionDao.class);
    /**
     * 会话key
     */
    private final static String SHIRO_SESSION_ID_ = "shiro-session-id_";

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        redisUtil.set(getKey(sessionId), session, session.getTimeout() / 1000);
        LOGGER.debug("doCreate >>>>> sessionId={}", sessionId);
        return sessionId;
    }

    private String getKey(Serializable sessionId) {
        return SHIRO_SESSION_ID_ + sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = (Session)redisUtil.get(getKey(sessionId));
        LOGGER.debug("doReadSession >>>>> sessionId={}", sessionId);
        return session;
    }

    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要再更新了
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return;
        }
        redisUtil.set(getKey(session.getId()), session, session.getTimeout() / 1000);
        LOGGER.debug("doUpdate >>>>> sessionId={}", session.getId());
    }

    @Override
    protected void doDelete(Session session) {
        String sessionId = session.getId().toString();
        redisUtil.del(getKey(session.getId()));
        LOGGER.debug("doDelete >>>>> sessionId={}", sessionId);
    }

    /**
     * 获取会话列表
     * @return
     */
    @Override
    public Set<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<>();
        Set<byte[]> keys = redisUtil.keys(getKey("*"));
        if(keys != null && keys.size()>0){
            for(byte[] key : keys){
                Session s = (Session)redisUtil.get(new String(key));
                sessions.add(s);
            }
        }
        return sessions;
    }

    /**
     * 强制退出
     * @param sessionIds
     * @return
     */
    public int forceout(Serializable... sessionIds) {
        for (Serializable sessionId : sessionIds) {
            // 会话增加强制退出属性标识，当此会话访问系统时，判断有该标识，则退出登录
            Session session = doReadSession(sessionId);
            if(session!=null){
                session.setAttribute("FORCE_LOGOUT", "FORCE_LOGOUT");
                doUpdate(session);
            }
        }
        return sessionIds.length;
    }
}
