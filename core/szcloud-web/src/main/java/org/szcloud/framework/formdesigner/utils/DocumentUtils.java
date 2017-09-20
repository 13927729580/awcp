package org.szcloud.framework.formdesigner.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.venson.controller.base.ControllerContext;
import org.szcloud.framework.venson.controller.base.ControllerHelper;

import com.alibaba.fastjson.JSON;

import BP.WF.Dev2Interface;

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

	public boolean isCanDo() {
		HttpServletRequest request = ControllerContext.getRequest();
		String WorkID = request.getParameter("WorkID");
		String FK_Node = request.getParameter("FK_Node");
		if (StringUtils.isNumeric(WorkID) && StringUtils.isNotBlank(FK_Node))
			return Dev2Interface.Flow_IsCanDoCurrentWork(FK_Node, Long.parseLong(WorkID),
					ControllerHelper.getUser().getUserIdCardNumber());
		return false;
	}

	public boolean isStarter() {
		HttpServletRequest request = ControllerContext.getRequest();
		String WorkID = request.getParameter("WorkID");
		String userId = this.getUser().getUserIdCardNumber();
		if (StringUtils.isNumeric(WorkID))
			return this.jdbcTemplate.queryForObject(
					"select count(1) from wf_generworkflow where workid=? and starter=? and wfsta<>1 ", Integer.class,
					WorkID, userId) == 1;
		return false;
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
		try {
			return this.excuteQuery("select " + field + " from " + table + " where " + idName + "='" + value + "'")
					.get(field);
		} catch (Exception e) {
			return null;
		}
	}

}
