package cn.org.awcp.unit.message;

import cn.org.awcp.extend.formdesigner.DocumentUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息通知实体类
 *
 * @author venson
 * @version 20171102
 */
public class PunNotification {
    public static final String KEY_NOTIFY = "notify";
    public static final String KEY_FLOW = "flow";
    private String id;
    private String title;
    private String content;
    private String msgUrl;
    private String type;
    private String createTime;
    private String createName;
    private String receiver;
    public final static String WECHAT = "wechat";
    public final static String IMPORTANT = "1";
    public final static String UNIMPORTANT = "0";
    public final static String SOCKET = UNIMPORTANT;
    public final static String DING_DING = IMPORTANT;

    public PunNotification(String title, String content, String msgUrl, String createName) {
        this.title = title;
        this.content = content;
        this.msgUrl = msgUrl;
        this.createName = createName;
    }

    public PunNotification() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void sendScoket() {
        Map<String, Object> map = new HashMap<>();
        String[] userId = this.receiver.split(",");
        for (String user : userId) {
            this.receiver = null;
            map.put(this.getType(), this);
            WebSocket.sendMessage(user, map);
        }
    }

    public void sendDD() {
        DocumentUtils.getIntance().sendMessage(this.msgUrl, "0", this.content, this.title, this.receiver);
    }

    public void send(String... way) {
        // 为空，默认走socket
        if (way == null || way.length == 0) {
            way = new String[]{UNIMPORTANT};
        }
        for (String w : way) {
            if (IMPORTANT.equals(w)) {
                this.sendDD();
            } else if (UNIMPORTANT.equals(w)) {
                this.sendScoket();
            } else if (WECHAT.equals(w)) {
                // NOT
            } else {
                this.sendScoket();
            }
        }
    }

    public static void sendFlow(String... users) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("flow", "freshFlow");
        for (String user : users) {
            WebSocket.sendMessage(user, map);
        }
    }

}
