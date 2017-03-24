<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<title>政务云应用管理平台</title>
<link rel="stylesheet"
	href="<%=basePath%>/resources/plugins/zui/dist/css/zui.css">
<link rel="stylesheet"
	href="<%=basePath%>/resources/styles/szCloud_system.css" />
</head>

<body>
	<div id="main">
		<div class="jumbotron text-center">
			<h2 class='text-success'>选择系统</h2>
			<p></p>
			<div class="row">
				<c:forEach items="${sysVOs}" var="vo">
					<div class="col-md-3 col-sm-4">
						<a href="manageAdmin/intoSystem.do?sysId=${vo.sysId}">
							<div class="alert alert-success with-icon">
								<i class="icon-desktop"></i>
								<div class="content">${vo.sysName}</div>
							</div>
						</a>
					</div>
				</c:forEach>			
			</div>
		</div>
	</div>
</body>
</html>