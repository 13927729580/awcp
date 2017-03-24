<%@page import="BP.WF.Glo"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.Port.WebUser"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String FK_Flow = request.getParameter("FK_Flow");
	String FK_Node = request.getParameter("FK_Node");
	String DoWhat = request.getParameter("DoWhat");
	String basePath = Glo.getCCFlowAppPath();
	
	if(null == WebUser.getNo()){
		response.sendRedirect(basePath+"WF//App//Simple//Login.jsp");
	}
	
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>ccflow的极速模式</title>
<link href="<%=basePath %>WF/Comm/Style/Table0.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath %>WF/Comm/Style/Table.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function repace_iframe_src(src){
		var iframe = document.getElementById("iframe_buttom");
		iframe.src = src;
		return false;
	}
</script>
</head>
<frameset rows="60,*" frameborder="no" border="0" framespacing="0">
	<frame src="<%=basePath %>WF/App/Simple/Top.jsp" noresize="noresize" frameborder="0" name="topFrame" target="main" marginwidth="0" marginheight="0" scrolling="no">
	<frameset rows="*" cols="195,*" id="frame">
		<frame src="<%=basePath %>WF/App/Simple/Left.jsp" name="leftFrame" noresize="noresize" marginwidth="0" marginheight="0" frameborder="1" scrolling="auto">
		<frame src="<%=basePath %>WF/App/Simple/ToDoList.jsp" name="main" marginwidth="0" marginheight="0" frameborder="1" scrolling="yes">
	</frameset>
</frameset>

</html>