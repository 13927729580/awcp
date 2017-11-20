<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<style>
html{
	height:100%;
}
html,body{
	font-family: Microsoft YaHei,Arial;
	color:#333;
}
.welcome{
	padding-top: 20%;
	font-size: 24px;
	text-align: center;
	text-shadow:5px 2px 6px #747474;
	-webkit-text-shadow:5px 2px 6px #747474;
	-moz-text-shadow:5px 2px 6px #747474;
	-o-text-shadow:5px 2px 6px #747474;
}
</style>
</head>

<body>
	<div class="welcome">
		<p>欢迎进入政务云管理平台</p>
	</div>
</body>
</html>
