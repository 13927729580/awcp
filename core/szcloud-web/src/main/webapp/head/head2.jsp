<%@taglib uri='http://java.sun.com/jstl/core_rt' prefix='c'%>
<%@taglib uri='http://java.sun.com/jstl/fmt_rt' prefix='fmt'%>
<%@taglib uri='http://java.sun.com/jsp/jstl/functions' prefix='fn'%>
<%@page import="java.util.Date"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.Graphics"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="BP.Sys.SystemConfig"%>
<%@page import="BP.Tools.StringHelper"%>

<%@page import="BP.WF.Glo"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.WF.DTS.GenerSiganture"%>

<%@page import="BP.WF.Template.Node"%>
<%@page import="BP.WF.Template.Nodes"%>
<%@page import="BP.WF.Template.CCListAttr"%>
<%@page import="BP.WF.Template.DeliveryWay"%>
<%@page import="BP.WF.Template.Flow"%>
<%@page import="BP.WF.Template.HungUp"%>
<%@page import="BP.WF.Template.ActionType"%>

<%@page import="BP.WF.Entity.TrackAttr"%>
<%@page import="BP.WF.Entity.FrmWorkCheck"%>
<%@page import="BP.WF.Entity.FrmWorkCheckSta"%>
<%@page import="BP.WF.Entity.ReturnWork"%>
<%@page import="BP.WF.Entity.ReturnWorks"%>
<%@page import="BP.WF.Entity.GenerWorkFlow"%>
<%@page import="BP.WF.Entity.GenerWorkerListAttr"%>
<%@page import="BP.WF.Entity.GenerWorkFlowAttr"%>
<%@page import="BP.WF.Entity.GenerWorkerList"%>
<%@page import="BP.WF.Entity.GenerWorkerLists"%>

<%@page import="BP.Port.Emp"%>
<%@page import="BP.Port.Dept"%>
<%@page import="BP.Port.Depts"%>
<%@page import="BP.Port.WebUser"%>
<%@page import="BP.Port.EmpStation"%>
<%@page import="BP.Port.EmpStations"%>

<%@page import="BP.DA.DataTable"%>
<%@page import="BP.DA.DataRow"%>
<%@page import="BP.DA.DataType"%>
<%@page import="BP.DA.DBAccess"%>

<%@page import="org.jflow.framework.common.model.*"%>
<%@page import="org.jflow.framework.designer.model.*"%>
<%@page
	import="org.jflow.framework.controller.wf.workopt.AllotTaskController"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><%=SystemConfig.getSysName()%></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.">
<meta name="renderer" content="webkit">

<link href="<%=basePath%>Demo/ZUI/docs/css/zui.min.css" rel="stylesheet">
<link
	href="<%=basePath%>Demo/ZUI/dist/lib/datetimepicker/datetimepicker.min.css"
	rel="stylesheet">
<link href="<%=basePath%>Demo/ZUI/docs/css/doc.css" rel="stylesheet">
<link href="<%=basePath%>Demo/ZUI/docs/css/mycss.css"
	rel="stylesheet"><!-- 自己的样式 -->
	<link href="<%=basePath%>Demo/ZUI/docs/css/ui-dialog.css"
	rel="stylesheet">



<script src="<%=basePath%>Demo/ZUI/dist/lib/jquery/jquery.js"></script>
<script src="<%=basePath%>Demo/ZUI/docs/js/zui.js"></script>
<script
	src="<%=basePath%>Demo/ZUI/dist/lib/datetimepicker/datetimepicker.min.js"></script>
<script
	src="<%=basePath%>Demo/ZUI/dist/lib/kindeditor/kindeditor.min.js"></script>
<script src="<%=basePath%>Demo/ZUI/docs/js/doc.js"></script>
<script
	src="<%=basePath%>Demo/ZUI/assets/google-code-prettify/prettify.js"></script><!-- chosen -->

<script
	src="<%=basePath%>Demo/ZUI/dist/lib/kindeditor/kindeditor.min.js"></script>
<script
	src="<%=basePath%>Demo/ZUI/assets/kindeditor/kindeditor-all-min.js"></script>
<script src="<%=basePath%>Demo/ZUI/assets/html5shiv.js"></script>
<script src="<%=basePath%>Demo/ZUI/assets/respond.js"></script>
<script src="<%=basePath%>Demo/ZUI/docs/js/jquery.validate.min.js"></script>
<script src="<%=basePath%>Demo/ZUI/docs/js/dialog-min.js"></script>
<script src="<%=basePath%>Demo/ZUI/docs/js/sea.js"></script>