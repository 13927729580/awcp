package cn.org.awcp.extend.formdesigner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.org.awcp.formdesigner.utils.BaseUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.dingding.Env;
import cn.org.awcp.dingding.helper.AuthHelper;
import cn.org.awcp.dingding.message.OAMessage;
import cn.org.awcp.dingding.message.OAMessage.Body;
import cn.org.awcp.dingding.message.OAMessage.Body.Form;
import cn.org.awcp.dingding.message.OAMessage.Head;
import cn.org.awcp.dingding.service.DDRequestService;
import cn.org.awcp.venson.util.LocalStorage;

/**
 * 文档操作
 */
public class DocumentUtils extends BaseUtils {

	private static DocumentUtils utils = new DocumentUtils();

	public static DocumentUtils getIntance() {
		return utils;
	}

	private DocumentUtils() {
	}

	/**
	 * 获取当前文档
	 * 
	 * @return
	 */
	public DocumentVO getCurrentDocument() {
		return ControllerContext.getDoc();
	}

	// *******************************钉钉发送OA消息-begin************************************************
	public boolean sendMessage(String url, String type, String json, String title, String ids, String agentId) {
		return sendMessage(url, type, JSON.parseObject(json, Map.class), title, ids, agentId, null, null);
	}

	public boolean sendMessage(String url, String type, String json, String title, String ids) {
		return sendMessage(url, type, JSON.parseObject(json, Map.class), title, ids, null, null, null);
	}

	public boolean sendMessage(String url, String type, Map<?, ?> content, String title, String ids) {
		return sendMessage(url, type, content, title, ids, null, null, null);
	}


	/**
	 * 钉钉OA消息发送
	 * 
	 * @param url
	 *            客户端点击消息时跳转到的H5地址
	 * @param type
	 *            0：用户，1：群组
	 * @param content
	 *            消息body键值对内容
	 * @param title
	 *            标题
	 * @param ids
	 *            推送用户或群组的ID，逗号分隔,
	 * @param agentId
	 *            微应用Id
	 * @param img
	 *            消息body的图片
	 * @param bodyContent
	 *            消息body的content
	 * @return
	 */
	public boolean sendMessage(String url, String type, Map<?, ?> content, String title, String ids, String agentId,
			String img, String bodyContent) {
		// ,替换成|
		ids = ids.replaceAll(",", "|");
		OAMessage oa = new OAMessage();
		Head head = new Head();
		head.bgcolor = "FFBBBBBB";
		head.text = title;
		oa.head = head;
		Body body = new Body();
		oa.body = body;
		String userName = ControllerHelper.getUser().getName();
		body.author = userName;
		body.title = title;
		// 如果agentId为空则先从参数中查找，如果没找到则赋予默认值
		if (StringUtils.isBlank(agentId)) {
			agentId = ControllerContext.getRequest().getParameter(Env.PARAM_AGENT_ID_NAME);
			if (StringUtils.isBlank(agentId)) {
				agentId = Env.DEFAULT_AGENT_ID;
			}
		}
		String insertSql = "insert into dd_send_message(MSG_ID,MSG_TYPE,MSG_CONTENT,MSG_URL,MSG_RECEIVE,CREATOR,CREATE_TIME,TO_USER,forbiddenUserId,invalidparty,invaliduser) values(?,?,?,?,?,?,?,?,?,?,?)";
		String gotoUrl = url;
		if (content.containsKey("validRepeat")) {
			gotoUrl += "&validRepeat="
					+ content.get("validRepeat").toString().replaceAll(StringUtils.SPACE, StringUtils.EMPTY).trim();
			content.remove("validRepeat");
			if (this.jdbcTemplate.queryForObject(
					"select COUNT(1) from dd_send_message where MSG_URL=? and MSG_RECEIVE=?", Integer.class, gotoUrl,
					ids) != 0) {
				logger.debug("钉钉消息发送:该消息已经发送过，禁止再发。");
				return false;
			}
		}
		// 表单内容
		List<Form> forms = new ArrayList<>();
		body.form = forms;
		for (Object m : content.keySet()) {
			Form form = new Form();
			form.key = m + "：";
			form.value = content.get(m) + "";
			if (StringUtils.isNotBlank(form.value)) {
				forms.add(form);
			}
		}
		if (StringUtils.isNotBlank(img)) {
			body.image = img;
		}
		if (StringUtils.isNotBlank(bodyContent)) {
			body.content = bodyContent;
		}
		// 打开时不显示分享和菜单
		if (gotoUrl.contains("?")) {
			gotoUrl += "&showmenu=false&dd_share=false";
		} else {
			gotoUrl += "?showmenu=false&dd_share=false";

		}
		oa.message_url = gotoUrl;
		oa.pc_message_url = gotoUrl;
		JSONObject jobj;
		if ("0".equals(type)) {
			jobj = DDRequestService.sendMessage(AuthHelper.getAccessToken(), ids, "", agentId, oa.type(), oa);
		} else {
			jobj = DDRequestService.sendMessage(AuthHelper.getAccessToken(), "", ids, agentId, oa.type(), oa);
		}
		if (jobj != null) {
			if (jobj.get("errcode") == null) {
				// 发送消息保存至服务器
				excuteUpdate(insertSql, jobj.get("messageId"), oa.type(), JSON.toJSONString(oa), gotoUrl, ids, userName,
						today(), type, jobj.get("forbiddenUserId"), jobj.get("invalidparty"), jobj.get("invaliduser"));
				logger.debug("钉钉消息发送成功:" + jobj.get("messageId"));
			} else {
				// 发送消息保存至服务器
				excuteUpdate(insertSql, "ERROR", oa.type(), JSON.toJSONString(oa), gotoUrl, ids, userName, today(),
						type, jobj.get("errmsg"), jobj.get("errmsg"), jobj.get("errmsg"));
				logger.debug("钉钉消息发送失败：" + jobj.get("errmsg"));
			}
		} else {
			// 发送消息保存至服务器
			excuteUpdate(insertSql, "ERROR", oa.type(), JSON.toJSONString(oa), gotoUrl, ids, userName, today(), type,
					"ERROR", "ERROR", "ERROR");
			logger.debug("钉钉消息发送失败");
			return false;
		}
		return true;
	}
	// *******************************钉钉发送OA消息-end************************************************

	/**
	 * 根据ID查询指定表并返回指定字段
	 * 
	 * @param table
	 *            要查询的表名
	 * @param field
	 *            要查询的字段
	 * @param idName
	 *            主键
	 * @param value
	 *            主键值
	 */
	public Object queryObjectById(String table, String field, String idName, Object value) {
		return this.excuteQueryForObject("select " + field + " from " + table + " where " + idName + "=?", value);
	}


	public String getLocal() {
		if (ControllerHelper.getLang() == Locale.SIMPLIFIED_CHINESE) {
			return "zh";
		} else {
			return "en";
		}
	}

	public void clearDDToken() {
		LocalStorage.remove(Env.CORP_ID);
	}

}
