package org.szcloud.framework.workflow.controller.wf;

import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.szcloud.framework.workflow.core.business.InnerWorkItem;
import org.szcloud.framework.workflow.core.business.NextActivityPara;
import org.szcloud.framework.workflow.core.business.TWorkItem;
import org.szcloud.framework.workflow.core.entity.CEntry;
import org.szcloud.framework.workflow.core.entity.CWorkItem;
import org.szcloud.framework.workflow.core.module.MGlobal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("workflow/wf")
public class SelectDailogParms {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(SelectDailogParms.class);
	/**
	 * 流转中的公文对象
	 */
	private CWorkItem mo_Item = null;

	private String dynamicPageId = "";

	/**
	 * 执行类型：=0-保存；=1-发送；=2-转发；=3-内部传阅；
	 */
	private int mi_RunType = 1;
	/**
	 * 
	 */
	private boolean mb_Boolean = true;

	private String basePath = "";

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("selectDailogType")
	public void selectDailogType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		mo_Item = (CWorkItem) session.getAttribute("WorkItemKey");

		mi_RunType = Integer.valueOf(request.getParameter("type"));

		basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";

		PrintWriter out = response.getWriter();
		out.write("true");
		out.flush();
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("selectDailog")
	public void selectDailog(HttpServletRequest request, HttpServletResponse response) throws Exception {

		mi_RunType = Integer.valueOf(request.getParameter("type"));
		dynamicPageId = request.getParameter("pageId");
		basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/";

		StringBuffer html = null;
		// if(HttpRequestDeviceUtils.isMobileDevice(request))
		html = setHtml(mo_Item);
		// else
		// html = setMobileHtml(mo_Item);

		PrintWriter out = response.getWriter();
		out.write(html.toString());
		out.flush();
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("goBackFlow")
	public Map goBackFlow(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		mo_Item = (CWorkItem) session.getAttribute("WorkItemKey");
		CEntry lo_Entry = mo_Item.getEntryById(mo_Item.CurEntry.OrginalID);
		// 返回信息
		Map resultMap = new HashMap();
		resultMap.put("users", String.valueOf(lo_Entry.ParticipantID));
		resultMap.put("activity", String.valueOf(lo_Entry.ActivityID));
		return resultMap;
	}

	/**
	 * 主程序
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		JSONArray jsonArray = JSONArray.parseArray(
				"[{\"name\":\"name1\",\"value\":\"简单流转逻辑实例1\"},{\"name\":\"name2\",\"value\":\"简单流转逻辑实例2\"}]");
		for (Object object : jsonArray) {
			JSONObject var = (JSONObject) object;
			logger.debug(var.get("name") + "");
		}

		logger.debug(Integer.parseInt("activity8".substring("activity".length())) + "");

		// CLogon lo_Logon = new CLogon(0, "admin", "admin", "192.168.0.1");
		//
		// CWorkItem ao_Item = TWorkItem.importFromFile(lo_Logon,
		// "D:\\TempData\\TMP4665187384835876166.xml");
		//
		// logger.debug(new
		// SelectDailogParms().setHtml(ao_Item).toString());

		// Document document =
		// MXmlHandle.ReadXml("e://c1ff147a-9ed5-4fa3-aad6-7a371826117e.xml");
		// Element rootElement = document.getDocumentElement();
		// Node cyclostyle = rootElement.getFirstChild();
		// Node rightNode = cyclostyle.getFirstChild();
		// Node roleNode = rightNode.getNextSibling().getNextSibling();
		// Node flowNode = roleNode.getNextSibling();
		// Node activityNode = flowNode.getFirstChild();
		// Node transactNode = activityNode.getFirstChild();
		// Node conditionNode = transactNode.getNextSibling();
		// Node participantNode = conditionNode.getNextSibling();
		// Node extendNode = participantNode.getNextSibling();

	}

	/**
	 * 设置页面Html内容
	 * 
	 * @param ao_Html
	 * @param ao_Item
	 */
	public StringBuffer setHtml(CWorkItem ao_Item) {
		StringBuffer ao_Html = new StringBuffer();

		ao_Html.append("<script type=\"text/javascript\">" + "function selectUsers(x, y){	" + "top.dialog({"
				+ "title: \"用户选择框\",		" + "url:\"" + basePath
				+ "formdesigner/page/component/userSelect/111.jsp\",		" + "onclose: function () {"
				+ "var value=this.returnValue;" + "var memberIDs=[];" + "var memberNames=[];" + "this.title('提交中…');"
				+ "if(value!=\"\"){" + "$.each(value,function(i,n){"

				+ "memberIDs.push(i);" + "memberNames.push(n);" + "});"
				+ "$(\"#activity\" + y).val(memberIDs.join(\",\"));				"
				// + "if(!$.isEmptyObject(value)){"
				+ "$(\"#selectUserName\" + x).html(memberNames.join(\",\"));					"
				+ "$(\"#selectUserName\" + x).append(\"<i class='icon-remove' style='position:absolute;top:8px;right:10px;z-index:99;cursor:pointer' "
				// +
				// "onclick=\"javascript:$(\"#kkk\").html(\"请选择用户\");return
				// false;\""
				+ "" + "" + "></i>\");"
				// + "}else{"
				// + "_obj.html(\"请选择用户\"); "
				// + "}"
				// + "alert($(\"#activity\" + x).val());"
				+ "}" + "return false;" + "}	" + "}).width(1000).showModal();}" + "" + "" + "</script>");

		return ao_Html;
	}

	/**
	 * 设置页面Html内容
	 * 
	 * @param ao_Html
	 * @param ao_Item
	 */
	public StringBuffer setMobileHtml(CWorkItem ao_Item) {
		StringBuffer ao_Html = new StringBuffer();
		InnerWorkItem lo_Inner = ao_Item.getWorkItemInterface();

		if (!TWorkItem.existSelectChoice(lo_Inner, mi_RunType))
			return ao_Html;

		ao_Html.append(
				"<div data-role=\"page\" id=\"popup\"><div data-role=\"header\" data-theme=\"b\"><h1>批示意见</h1></div>	<div role=\"main\" class=\"ui-content\"><form id=\"groupForm1\">");

		// setTitleHtml(ao_Html, lo_Inner);
		StringBuffer sb = null;
		if (mi_RunType == 1) {// 发送
			sb = setChoiceHtml(lo_Inner);
			sb.append(setParticipantHtml(lo_Inner, sb.length() > 5));
		} else if (mi_RunType == 2) {// 转发
			sb = setLapseToHtml(lo_Inner);
		} else if (mi_RunType == 3) {// 内部传阅
			sb = setReadingHtml(lo_Inner);
		}
		if (sb.length() > 5)
			ao_Html.append(sb);
		else
			return sb;
		ao_Html.append("</form>");
		ao_Html.append(
				"<a href=\"index.html\" data-rel=\"back\" class=\"ui-btn ui-shadow ui-corner-all ui-btn-a\">确定</a><a href=\"index.html\" data-rel=\"back\" class=\"ui-btn ui-shadow ui-corner-all ui-btn-a\">取消</a></div></div>");
		ao_Html.append("<script type=\"text/javascript\">" + "function selectUsers(x, y){	" + "top.dialog({"
				+ "title: \"用户选择框\",		" + "url:\"" + basePath
				+ "formdesigner/page/component/userSelect/chosenNames.jsp\",		" + "onclose: function () {"
				+ "var value=this.returnValue;" + "var memberIDs=[];" + "var memberNames=[];" + "this.title('提交中…');"
				+ "if(value!=\"\"){" + "$.each(value,function(i,n){"

				+ "memberIDs.push(i);" + "memberNames.push(n);" + "});"
				+ "$(\"#activity\" + y).val(memberIDs.join(\",\"));				"
				// + "if(!$.isEmptyObject(value)){"
				+ "$(\"#selectUserName\" + x).html(memberNames.join(\",\"));					"
				+ "$(\"#selectUserName\" + x).append(\"<i class='icon-remove' style='position:absolute;top:8px;right:10px;z-index:99;cursor:pointer' "
				// +
				// "onclick=\"javascript:$(\"#kkk\").html(\"请选择用户\");return
				// false;\""
				+ "" + "" + "></i>\");"
				// + "}else{"
				// + "_obj.html(\"请选择用户\"); "
				// + "}"
				// + "alert($(\"#activity\" + x).val());"
				+ "}" + "return false;" + "}	" + "}).width(1000).showModal();}" + "" + "" + "</script>");

		return ao_Html;
	}

	/**
	 * 设置标题Html内容
	 * 
	 * @param ao_Html
	 * @param ao_Item
	 * @param ao_Inner
	 */
	private void setTitleHtml(StringBuffer ao_Html, InnerWorkItem ao_Inner) {
		if (ao_Inner.getEntryID() != 3)
			return;

		ao_Html.append(
				"<span style=\"color: rgb(255, 0, 0);\">*</span><span style=\"height: 40px; line-height: 40px;\"><b>文件名称：</b></span>");
		ao_Html.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"3\"><tr>");

		ao_Html.append("<td><input name=\"name\" class=\"form-control\" style=\"width:100%;\" value=\""
				+ MGlobal.stringToHtml(mo_Item.WorkItemName) + "\" /></td>");

		ao_Html.append("</tr></table>");

	}

	/**
	 * 设置意见选项Html内容
	 * 
	 * @param ao_Html
	 * @param ao_Item
	 * @param ao_Inner
	 */
	private StringBuffer setChoiceHtml(InnerWorkItem ao_Inner) {
		StringBuffer sb = new StringBuffer();
		if (MGlobal.isEmpty(ao_Inner.getResponseChoices()) || ao_Inner.getResponseCount() < 2)
			return sb;

		sb.append(
				"<span style=\"height: 40px; line-height: 40px;\"><b>您的意见选项：</b></span><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"3\"><tr height=\"25px\">");
		sb.append("<td class=\"FontT\" align=\"center\" width=\"20%\">您的选择</td>");
		sb.append("<td valign=\"top\" width=\"80%\">");
		sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");

		String[] ls_Array = ao_Inner.getResponseChoices().split(";");
		int i = 0;
		for (String ls_Text : ls_Array) {
			if (MGlobal.isExist(ls_Text)) {
				if (i == 0)
					sb.append(MessageFormat
							.format("<td width=\"10px\"><input type=\"radio\" id=\"choice{0}\" name=\"choice\" value=\"{1}\" checked=\"checked\">"
									+ ls_Text + "</input></td>", String.valueOf(i), ls_Text));
				else
					sb.append(MessageFormat
							.format("<td width=\"10px\"><input type=\"radio\" id=\"choice{0}\" name=\"choice\" value=\"{1}\" >"
									+ ls_Text + "</input></td>", String.valueOf(i), ls_Text));
				i++;
			}
		}
		sb.append("</table></td></tr></table>");
		return sb;
	}

	/**
	 * 设置意见选项Html内容ForApp
	 */
	@ResponseBody
	@RequestMapping(value = "/getNodeList")
	private StringBuffer setChoiceHtmlForApp(HttpServletRequest request, HttpServletResponse response) {

		CWorkItem ao_Item = (CWorkItem) request.getSession().getAttribute("WorkItemKey");

		StringBuffer sb = new StringBuffer();

		if (ao_Item != null) {

			InnerWorkItem ao_Inner = ao_Item.getWorkItemInterface();
			if (!ao_Inner.getLapseTo())
				return sb;

			boolean lb_Boolean = false;
			for (NextActivityPara lo_Para : ao_Inner.getNextParas()) {
				if ((lo_Para.getTransactType() & 2) == 2) {
					lb_Boolean = true;
					break;
				}
			}
			if (!lb_Boolean)
				return sb;

			for (NextActivityPara lo_Para : ao_Inner.getNextParas()) {
				if ((lo_Para.getTransactType() & 2) == 2) {
					if (ao_Inner.getTransActivityNum() == 0) { // select
						sb.append(MessageFormat.format("<option value={0} selected=\"selected\">",
								String.valueOf(lo_Para.getActivityID())));
						sb.append(MessageFormat.format("{0}", lo_Para.getActivityName()));
					} else {
						sb.append(MessageFormat.format("<option value={0}>", String.valueOf(lo_Para.getActivityID())));
						sb.append(MessageFormat.format("{0}", lo_Para.getActivityName()));
					}
				}
			}

			/*
			 * sb.
			 * append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
			 * );
			 * 
			 * String[] ls_Array = ao_Inner.getResponseChoices().split(";"); int
			 * i = 0; for (String ls_Text : ls_Array) { if
			 * (MGlobal.isExist(ls_Text)) { if (i == 0) sb.append(MessageFormat.
			 * format("<td width=\"10px\"><input type=\"radio\" id=\"choice{0}\" name=\"choice\" value=\"{1}\" checked=\"checked\">"
			 * + ls_Text + "</input></td>", String.valueOf(i), ls_Text)); else
			 * sb.append(MessageFormat.
			 * format("<td width=\"10px\"><input type=\"radio\" id=\"choice{0}\" name=\"choice\" value=\"{1}\" >"
			 * + ls_Text + "</input></td>", String.valueOf(i), ls_Text)); i++; }
			 * } sb.append("</table></td></tr></table>");
			 */

			return sb;
		}

		return null;

	}

	/**
	 * 设置意见选项Html内容ForApp
	 */
	@ResponseBody
	@RequestMapping(value = "/getActivity")
	private String Activity(HttpServletRequest request, HttpServletResponse response) {

		CWorkItem ao_Item = (CWorkItem) request.getSession().getAttribute("WorkItemKey");

		String id = "";
		if (ao_Item != null) {
			InnerWorkItem ao_Inner = ao_Item.getWorkItemInterface();
			NextActivityPara lo_Para = ao_Inner.getNextParas().get(0);
			id = String.valueOf(lo_Para.getActivityID());
		}

		return id;
	}

	/**
	 * 设置后继收件人Html内容
	 * 
	 * @param ao_Html
	 * @param ao_Item
	 * @param ao_Inner
	 */
	private StringBuffer setParticipantHtml(InnerWorkItem ao_Inner, boolean flag) {
		StringBuffer sb = new StringBuffer();
		boolean lb_Boolean = false;
		for (NextActivityPara lo_Para : ao_Inner.getNextParas()) {
			if ((lo_Para.getTransactType() & 1) == 1) {
				if (lo_Para.getType() > -1) {
					lb_Boolean = true;
					break;
				}
			}
		}
		if (!lb_Boolean)
			return sb;

		if (ao_Inner.getNextParasByType(1).size() == 1 && !flag) {
			NextActivityPara lo_Para = ao_Inner.getNextParas().get(0);
			return sb.append(lo_Para.getActivityID());
		}

		sb.append(
				"<table width=\"100%\" height=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" borderColor=\"white\" borderColorDark=\"white\" borderColorLight=\"menu\">");

		String ls_HideActIds = ";";
		sb.append("<span style=\"height: 40px; line-height: 40px;\"><b>下一流转步骤(节点)收件人：</b></span>");

		for (NextActivityPara lo_Para : ao_Inner.getNextParas()) {
			if (lo_Para.getType() > -1 && (lo_Para.getTransactType() & 1) == 1) {
				int i = lo_Para.getActivityID();
				if (ls_HideActIds.indexOf(";" + lo_Para.getActivityID() + ";") == -1) {
					mb_Boolean = false;
					if (lo_Para.getActivityID() == 0) {
						mb_Boolean = true;
						break;
					} else {
						sb.append("<div  onclick=\"selectUsers(" + i + "," + i
								+ ");\"><input type=\"text\" class=\"form-control\"  style=\"display: none;\" name=\"activity"
								+ i + "\" value=\"6\" id=\"activity" + i + "\" /><div id='selectUserName" + i
								+ "' name='selectUserName" + i
								+ "' class=\"chosenName text-ellipsis form-control\" style=\"color: rgb(153, 153, 153); position: relative; cursor: text;\">请选择用户</div></div>");
					}
				}
			}
		}
		return sb;
	}

	/**
	 * 设置转发内容Html内容
	 * 
	 * @param ao_Html
	 * @param ao_Item
	 * @param ao_Inner
	 */
	private StringBuffer setLapseToHtml(InnerWorkItem ao_Inner) {
		StringBuffer sb = new StringBuffer();
		if (!ao_Inner.getLapseTo())
			return sb;

		boolean lb_Boolean = false;
		for (NextActivityPara lo_Para : ao_Inner.getNextParas()) {
			if ((lo_Para.getTransactType() & 2) == 2) {
				lb_Boolean = true;
				break;
			}
		}
		if (!lb_Boolean)
			return sb;

		sb.append(
				"<span style=\"height: 40px; line-height: 40px;\"><b>您的意见选项：</b></span><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"3\"><tr height=\"25px\">");
		sb.append("<td class=\"FontT\" align=\"center\" width=\"20%\">您的选择</td>");
		sb.append("<td valign=\"top\" width=\"80%\">");
		sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");

		for (NextActivityPara lo_Para : ao_Inner.getNextParas()) {
			if ((lo_Para.getTransactType() & 2) == 2) {
				if (ao_Inner.getTransActivityNum() == 0) { // 单选
					sb.append("<tr>");
					sb.append(MessageFormat.format(
							"<td width=\"16px\"><input type=\"radio\" id=\"choice{0}\" onclick=\"test({0})\" name=\"choice\" value=\"{0}\" /></td>",
							String.valueOf(lo_Para.getActivityID())));
					sb.append(MessageFormat.format("<td>{0}</td></tr>", lo_Para.getActivityName()));
				} else {
					sb.append("<tr>");
					sb.append(MessageFormat.format(
							"<td width=\"16px\"><input type=\"checkbox\" id=\"choice{0}\" name=\"choice\" value=\"{0}\" /></td>",
							String.valueOf(lo_Para.getActivityID())));
					sb.append(MessageFormat.format("<td>{0}</td></tr>", lo_Para.getActivityName()));
				}
			}
		}
		sb.append("</table></td></tr></table>");

		int i = 1;
		int x = 0;
		sb.append("<span id=\"lb_sp\" style=\"height: 40px; line-height: 40px;\"><b>下一流转收件人：</b></span>");
		sb.append("<table id=\"table\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"3\"><tr>");
		sb.append("<div id=\"divs\" onclick=\"selectUsers(" + i + "," + x
				+ ");\"><input type=\"text\" class=\"form-control\"  style=\"display: none;\" name=\"activity" + x
				+ "\" value=\"6\" id=\"activity" + x + "\" /><div id='selectUserName" + i + "' name='selectUserName" + i
				+ "' class=\"chosenName text-ellipsis form-control\" style=\"color: rgb(153, 153, 153); position: relative; cursor: text;\">请选择用户</div></div>");
		sb.append("</tr></table>");
		sb.append("<script type=\"text/javascript\">" + "function test(id){"
				+ "if($('#choice'+id).parents('td').next().html()=='结束'){" + "$('#lb_sp').hide();"
				+ "$('#divs').hide();" + "}" + "else" + "{" + "$('#lb_sp').show();" + "$('#divs').show();"
				+ "$('#divs').attr('onclick','selectUsers(1,'+id+')');"
				+ "$('#divs').children().eq(0).attr('name','activity'+id);"
				+ "$('#divs').children().eq(0).attr('id','activity'+id);" + "}" + "}" + "</script>");
		return sb;
	}

	/**
	 * 设置转发内容Html内容
	 * 
	 * @param ao_Html
	 * @param ao_Item
	 * @param ao_Inner
	 */
	private StringBuffer setReadingHtml(InnerWorkItem ao_Inner) {
		StringBuffer sb = new StringBuffer();
		if (!ao_Inner.getInsideReading())
			return sb;

		// sb.append("<span style=\"height: 40px; line-height:
		// 40px;\"><b>内部传阅收件人：</b></span>");
		// sb.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\"
		// cellspacing=\"3\"><tr>");
		// sb.append("<div onclick=\"selectUsers(0);\"><input type=\"text\"
		// class=\"form-control\" style=\"display: none;\" name=\"activity0\"
		// value=\"6\" id=\"activity0"
		// + "\" /><div id='selectUserName0' name='selectUserName0"
		// + "' class=\"chosenName text-ellipsis form-control\" style=\"color:
		// rgb(153, 153, 153); position: relative; cursor:
		// text;\">请选择用户</div></div>");
		// sb.append("</tr></table>");
		return sb.append(0);
	}

}
