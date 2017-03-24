<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>打印管理列表</title>
<base href="<%=basePath%>">
<%@ include file="/resources/include/common_css.jsp" %>
</head>

<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <!-- <li>首页</li>
		          <li>我的应用产品</li> -->
		          <li class="active">打印管理</li>
		        </ul>
		</div>
			
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="addPrint"><i class="icon-plus-sign"></i>新增</button>
			<button type="button" class="btn btn-info" id="deletePrint"><i class="icon-remove"></i>删除</button>
		</div>
		<div class="row" id="searchform">
				<div id="collapseButton" class="in">
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">名称</span>
								<input name="name" class="form-control" id="name" type="text" value=""/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary"  id="searchPrint">提交</button>
						</div>
				</div>
			</div>
		<div class="row" id="datatable" contenteditable="false">
			<table class="table table-bordered" id="componentTable" align="left">
				<thead>
					<tr>
						<th><input type="checkbox" id="checkAllPrint" /></th>
						<th>名称</th>
					</tr>
				</thead>
				<tbody id="printt">
				</tbody>
			</table>
		</div>
	</div>
	
	<%@ include file="/resources/include/common_js.jsp" %>
	<script type="text/javascript" src="<%=basePath%>formdesigner/page/script/print.js"></script>
	<script type="text/javascript">
		var basePath = "<%=basePath%>";
		
		$(document).ready(function() {
			loadPrint();
			
		});
	</script>
</body>
</html>