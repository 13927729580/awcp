package cn.org.awcp.wechat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.awcp.wechat.domain.message.Article;
import cn.org.awcp.wechat.domain.message.BaseMessage;
import cn.org.awcp.wechat.domain.message.ImageMessage;
import cn.org.awcp.wechat.domain.message.MusicMessage;
import cn.org.awcp.wechat.domain.message.NewsMessage;
import cn.org.awcp.wechat.domain.message.TextMessage;
import cn.org.awcp.wechat.domain.message.VideoMessage;
import cn.org.awcp.wechat.domain.message.VoiceMessage;


/**
 * 微信自动回复,将返回结果封装xml字符串
 * @author yqtao
 *
 */
public class XmlMsgBuilder {
	
	private static Logger logger = LoggerFactory.getLogger(XmlMsgBuilder.class);
	

    private final StringBuffer msgBuf = new StringBuffer("<xml>\n");;

    /**
     * 创建XmlMsgBuilder对象
     */
    public static XmlMsgBuilder create() {
        return new XmlMsgBuilder();
    }

    /**
     * 创建消息体前缀(所有消息相同的部分)
     * @param msg
     *            
     */
    private void msgPrefix(BaseMessage msg) {
        msgBuf.append("<ToUserName><![CDATA[")
              .append(msg.getToUserName())
              .append("]]></ToUserName>\n");
        msgBuf.append("<FromUserName><![CDATA[")
              .append(msg.getFromUserName())
              .append("]]></FromUserName>\n");
        msgBuf.append("<CreateTime>").append(msg.getCreateTime()).append("</CreateTime>\n");
        msgBuf.append("<MsgType><![CDATA[").append(msg.getMsgType()).append("]]></MsgType>\n");
    }

    /**
     * 被动文本消息
     * @param msg
     *            
     */
    public XmlMsgBuilder text(TextMessage msg) {
        msgPrefix(msg);
        msgBuf.append("<Content><![CDATA[").append(msg.getContent()).append("]]></Content>\n");
        return this;
    }

    /**
     * 被动图像消息
     * @param msg
     *           
     */
    public XmlMsgBuilder image(ImageMessage msg) {
        msgPrefix(msg);
        msgBuf.append("<Image>");
        msgBuf.append("<MediaId><![CDATA[").append(msg.getMediaId()).append("]]></MediaId>\n");
        msgBuf.append("</Image>");
        return this;
    }

    /**
     * 被动语音消息
     * @param msg
     *           
     */
    public XmlMsgBuilder voice(VoiceMessage msg) {
        msgPrefix(msg);
        msgBuf.append("<Voice>");
        msgBuf.append("<MediaId><![CDATA[").append(msg.getMediaId()).append("]]></MediaId>\n");
        msgBuf.append("</Voice>\n");
        return this;
    }

    /**
     * 被动视频消息
     * @param msg
     *           
     */
    public XmlMsgBuilder video(VideoMessage msg) {
        msgPrefix(msg);
        msgBuf.append("<Video>");
        msgBuf.append("<MediaId><![CDATA[").append(msg.getMediaId()).append("]]></MediaId>\n");
        msgBuf.append("<Title><![CDATA[").append(msg.getTitle()).append("]]></Title>\n");
        msgBuf.append("<Description><![CDATA[")
              .append(msg.getDescription())
              .append("]]></Description>\n");
        msgBuf.append("</Video>\n");
        return this;
    }

    /**
     * 被动音乐消息
     * @param msg
     *            
     */
    public XmlMsgBuilder music(MusicMessage msg) {
        msgPrefix(msg);
        msgBuf.append("<Music>");
        msgBuf.append("<Title><![CDATA[").append(msg.getTitle()).append("]]></Title>\n");
        msgBuf.append("<Description><![CDATA[")
              .append(msg.getDescription())
              .append("]]></Description>\n");
        msgBuf.append("<MusicUrl><![CDATA[").append(msg.getMusicUrl()).append("]]></MusicUrl>\n");
        msgBuf.append("<HQMusicUrl><![CDATA[")
              .append(msg.getHqMusicUrl())
              .append("]]></HQMusicUrl>\n");
        msgBuf.append("<ThumbMediaId><![CDATA[")
              .append(msg.getThumbMediaId())
              .append("]]></ThumbMediaId>\n");
        msgBuf.append("</Music>\n");
        return this;
    }

    /**
     * 被动多图文消息
     * @param msg
     *            
     */
    public XmlMsgBuilder news(NewsMessage msg) {
        msgPrefix(msg);
        StringBuffer arts_buf = new StringBuffer("<Articles>\n");
        StringBuffer item_buf = new StringBuffer();
        for (Article art : msg.getArticles()) {
            item_buf.setLength(0);
            item_buf.append("<item>\n");
            item_buf.append("<Title><![CDATA[").append(art.getTitle()).append("]]></Title>\n");
            item_buf.append("<Description><![CDATA[")
                    .append(art.getDescription())
                    .append("]]></Description>\n");
            item_buf.append("<PicUrl><![CDATA[").append(art.getPicUrl()).append("]]></PicUrl>\n");
            item_buf.append("<Url><![CDATA[").append(art.getUrl()).append("]]></Url>\n");
            item_buf.append("</item>\n");
            arts_buf.append(item_buf);
        }
        arts_buf.append("</Articles>\n");
        msgBuf.append("<ArticleCount>").append(msg.getArticleCount()).append("</ArticleCount>\n");
        msgBuf.append(arts_buf);
        return this;
    }

    /**
     * 转发客服消息
     *
     * @param msg   客服消息
     */
    /*public XmlMsgBuilder transferCustomerService(CustomerServiceMsg msg) {
        msgPrefix(msg);
        return this;
    }*/

    /**
     * AES加密信息
     *
     * @param xml
     *            密文消息
     * @param msgSignature
     *            消息签名
     * @param timeStamp
     *            时间戳
     * @param nonce
     *            随机字符
     */
    /*public String encrypt(String xml, String msgSignature, String timeStamp, String nonce) {
        msgBuf.setLength(0);
        msgBuf.append("<xml>\n");
        msgBuf.append("<Encrypt><![CDATA[").append(xml).append("]]></Encrypt>\n");
        msgBuf.append("<MsgSignature><![CDATA[")
              .append(msgSignature)
              .append("]]></MsgSignature>\n");
        msgBuf.append("<TimeStamp>").append(timeStamp).append("</TimeStamp>\n");
        msgBuf.append("<Nonce><![CDATA[").append(nonce).append("]]></Nonce>\n");
        msgBuf.append("</xml>");
        return msgBuf.toString();
    }*/

    /**
     * 输出回复消息
     * @return 
     */
    public String build() {
        msgBuf.append("</xml>");
        logger.info(msgBuf.toString());
        return new String(msgBuf);
    }
}
