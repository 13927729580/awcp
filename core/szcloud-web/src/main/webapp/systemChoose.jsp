<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>     
 <%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">

<title>政务云应用管理平台</title>
<link rel="stylesheet" href="resources/plugins/zui/dist/css/zui.css" >
<link rel="stylesheet" href="resources/styles/szCloud_system.css" /> 
<!--[if lt IE 9]>
  <script src="${ctxBase}/plugins/zui/assets/html5shiv.js"></script>
  <script src="${ctxBase}/plugins/zui/assets/respond.js"></script>
<![endif]-->
<style>
	.row a{display:block;}
	#main{font-family:'MicroSoft Yahei'}
</style>
</head>
<body>
<div id="main">
	<div class="jumbotron text-center">
		<h2>选择系统</h2>
      	<p></p>      	
      	<div class="row">
      	<c:forEach items="${sysVOs}" var="vo">
            <div class="col-md-6">
            	<a href="intoSystem.do?sysId=${vo.sysId}" >
            	<div class="alert alert-primary with-icon">
                	<i class="icon-desktop"></i><div class="content">${vo.sysName}</div>
                </div>
                </a>
            </div>
		</c:forEach>
		<shiro:hasRole name="admin">  
            <div class="col-md-6">
            	<a href="intoSystem.do?sysId=0" >
            	<div class="alert alert-primary with-icon">
                	<i class="icon-desktop"></i><div class="content">系统管理</div>
                </div>
                </a>
            </div>
   		</shiro:hasRole>  
        </div>   
		
	
	</div>
</div>
</body>
</html>