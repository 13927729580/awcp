<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>动态页面表单</title>
	<base href="<%=basePath%>">
	<%@ include file="/resources/include/common_css.jsp" %>
</head>
<body id="main">
	<div class="container-fluid">
		<c:if test="${empty message}">
			<c:if test="${empty content}">
				<div class="panel panel-primary">
            		<div class="panel-heading">提示</div>
           			 <div class="panel-body">该页面尚未发布或者模版为空</div>
          		</div>
			</c:if>
			<c:if test="${!empty content}">
				<xmp>${content}</xmp>
			</c:if>
		</c:if>	
		<c:if test="${!empty message}">
			<div class="panel panel-danger">
            	<div class="panel-heading">错误</div>
            	<div class="panel-body">${message}</div>
          	</div>			
		</c:if>					
	</div>

	<%@ include file="/resources/include/common_js.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
	    	try {
			  	var dialog = top.dialog.get(window);
			}catch (e) {
			}
			if(dialog){
				dialog.reset();
			}  	
	    });
	</script>
</body>
</html>


