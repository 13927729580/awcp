<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*"%>
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
<title>出现错误</title>
<link rel="stylesheet" href="base/resources/zui/dist/css/zui.css" >
<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
<script src="base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
<style>
	body{font-family: "Microsoft YaHei";}
	.errorPage{padding-top:20%;}
	.errorPage h1{text-align:center;}
	.errorPage h1 .icon-warning-sign{font-size:46px;}
	.errorPage h1 .status_message{font-size:24px;font-weight: normal;margin-left:30px;}
	.errorPage .buttons{text-align:center;}
</style>
</head>
<body>
	<div class="errorPage">
		<h1 class="alert alert-info">
			<span><i class="icon-warning-sign"></i></span>
			<span class="status_message">操作超时或未登录系统，无权访问。请点击下方按钮登录</span>
		</h1>
		<div class="buttons">
			<%-- <a class="btn btn-primary" href="javascript:;" data-url="<%=basePath%>manageAdmin">管理方登录</a> --%>
			<a class="btn btn-primary" href="javascript:;" data-url="<%=basePath%>login.html">重新登录</a>
			<%-- <a class="btn btn-primary" href="javascript:;" data-url="<%=basePath%>devAdmin">开发方登录</a> --%>
			<!-- <a class="btn" href="javascript:history.go(-1)">返回上一页</a> -->
		</div>
	</div>
	<script>
		$(function(){
			$("a.btn").on("click",function(){
				window.parent.location.href=$(this).data("url");
			})
		})
	</script>
</body>
</html>
