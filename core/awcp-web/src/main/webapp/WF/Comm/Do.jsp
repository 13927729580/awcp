<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.jflow.framework.designer.model.DoModel"%>

<%@ include file="/head/head1.jsp"%>
<%
	DoModel dm=new DoModel(request,response);
	dm.Page_Load();
%>
<html>
<body>
	<form id="Form1" method="post" runat="server">
		<FONT face="宋体"></FONT>
	</form>
</body>
</html>