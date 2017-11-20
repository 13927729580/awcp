<%@page import="org.jflow.framework.common.model.SearchModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String rptNo = request.getParameter("RptNo");
	String fkFlow = request.getParameter("FK_Flow");
	SearchModel sm = new SearchModel(request, response);
	String toolBar = sm.initToolBar();
	String pub = sm.initPub();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查询</title>
<style type="text/css">
html, body {
	height: 100%;
	margin: 0 auto;
}
</style>
<link href="<%=basePath%>WF/Comm/Style/Table0.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css"
	rel="stylesheet" type="text/css" />
<script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>WF/Comm/JS/Calendar/WdatePicker.js"
	type="text/javascript"></script>
<script language="JavaScript" src="<%=basePath%>WF/Comm/JScript.js"
	type="text/javascript"></script>
</head>
<body class="easyui-layout" onload="load()">
<input type="hidden" id="success" value="${success }" />
	<form method="post" id="form1" style="height: 100%">
		<div id="mainDiv" data-options="region:'center',title:'查询'"
			style="padding: 5px">
			<input type="hidden" id="FormHtml" name="FormHtml" value="">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',noheader:true,split:false"
					style="padding: 2px; height: auto; background-color: #E0ECFF; line-height: 30px">
					<%=toolBar%>
				</div>
				<div data-options="region:'center',noheader:true,border:false">
					<%=pub%>
				</div>
			</div>
		</div>
	</form>
</body>
<script type="text/javascript">
function OpenAttrs(ensName) {
    var url = './../../Sys/EnsAppCfg.jsp?EnsName=' + ensName;
    var s = 'dialogWidth=680px;dialogHeight=480px;status:no;center:1;resizable:yes'.toString();
    val = window.showModalDialog(url, null, s);
    window.location.href = window.location.href;
}
function DDL_mvals_OnChange(ctrl, ensName, attrKey) {

    var idx_Old = ctrl.selectedIndex;
    if (ctrl.options[ctrl.selectedIndex].value != 'mvals')
        return;
    if (attrKey == null)
        return;
    var timestamp = Date.parse(new Date());

    var url = 'SelectMVals.jsp?EnsName=' + ensName + '&AttrKey=' + attrKey + '&D=' + timestamp;
    var val = window.showModalDialog(url, 'dg', 'dialogHeight: 450px; dialogWidth: 450px; center: yes; help: no');
    if (val == '' || val == null) {
        ctrl.selectedIndex = 0;
    }
}
function __doPostBack(eventTarget, eventArgument) {
	var theForm = document.forms['form1'];
    if (!theForm) {
        theForm = document.form1;
    }
    if (!theForm.onsubmit || (theForm.onsubmit() != false)) {
        theForm.__EVENTTARGET.value = eventTarget;
        theForm.__EVENTARGUMENT.value = eventArgument;
        theForm.submit();
    }
}
function ddl_SelectedIndexChanged_GoTo(){
	var item = $("#GoTo").val();
    var tKey = new Date();
	var s1="D3";
	var s2="Contrast";
    if(item==s1||item==s2){
    	alert("此功能正在升级,敬请期待.");
    	return;
    }
    window.location="<%=basePath%>WF/Rpt/" + item + ".jsp?RptNo=<%=rptNo%>&FK_Flow=<%=fkFlow%>&T="+tKey;
	
}
function ToolBar1_ButtonClick(btnName){
	var tKey = new Date();
	$("#FormHtml").val($("#form1").html());
	var url = "<%=basePath%>WF/Rpt/Search.do?RptNo=<%=rptNo%>&FK_Flow=<%=fkFlow%>&btnName="
				+ btnName + "&T=" + tKey;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	function load()
	{
		var successStr=$("#success").val();
		
		if(successStr.length==0)
			{
				return;
			}else
				{
				alert(successStr);
				}
	}
</script>
</html>