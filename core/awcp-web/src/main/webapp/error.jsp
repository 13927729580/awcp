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
	<script src="base/resources/zui/dist/js/zui.js"></script>
	<script src="<%=basePath%>base/resources/artDialog/dist/dialog-plus-min.js"></script>
	<style>
		body{font-family: "Microsoft YaHei";}
		.errorPage{padding-top:20%;}
		.errorPage h1{text-align:center;}
		.errorPage h1 .icon-warning-sign{font-size:60px;}
		.errorPage h1 .status_message{font-size:32px;font-weight: normal;margin-left:30px;}
		.errorPage .buttons{text-align:center;}
	</style>
</head>
<body>
	<div class="errorPage">
		<h1 class="alert alert-info">
			<span><i class="icon-warning-sign"></i></span>
			<span class="status_message"></span>
		</h1>
		<div class="buttons">
			<a class="btn btn-primary" id="toIndexPage" href="javascript:void(0)">返回首页</a>
		</div>
		<pre style="display: none;">
			<%=request.getAttribute("javax.servlet.error.exception") %>
		</pre>
	</div>
	<script>
		$(function(){
			$("#toIndexPage").click(function(){
				parent.location.href = "<%=basePath%>manage/index.html";
			});
			
			var statusCode = "<%=request.getAttribute("javax.servlet.error.status_code") %>";
			
			var $statusMessage = $(".status_message");
			var statusInfo = [{"400":"错误请求","401":"未授权","403":"禁止访问","404":"找不到该页面","500":"请求服务器失败"}];
			if(statusCode=="500"){
				$(".buttons").append('<a class="btn btn-info" id="viewLog">查看错误日志</a>');
				$("#viewLog").click(function(){
					top.dialog({
						id: $.uuid(),
						title: '错误信息',
						width:800,
						height:400,
						content:$("pre").html(),
						onclose: function () {
						}
					}).showModal();
				});
			}
			
			$.each(statusInfo,function(n,item){
				$statusMessage.html("错误" + statusCode + ":" + item[statusCode]);
			});

		});
	</script>
</body>
</html>
