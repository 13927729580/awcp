<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>菜单编辑页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="../../resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--edit页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="dataform">表单区（--输入组--按钮组）</div>
			-->
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="${basePath}main.jsp">首页</a></li>
		          <li><a href="${basePath}workflow/bpm/console-listProcessInstances.do">流程实例</a></li>
		          <li class="active">流程实例迁移</li>
		        </ul>
			</div>
			
			<div class="row" id="dataform">
			<form id="userRepoForm" method="post" action="<%=basePath%>workflow/bpm/console-migrateSave.do"  class="form-horizontal">
			<legend> 流程实例迁移	</legend>
				 <input id="demo_id" type="hidden" name="processInstanceId" value="${processInstanceId}">
				 <div class="form-group">
					<label class="col-md-2 control-label required" for="bpm-category_name">流程定义</label>
				    <div class="col-md-4">
				      <select name="processDefinitionId" class="form-control">
					    <c:forEach items="${processDefinitions}" var="item">
					    <option value="${item.id}">${item.id}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="col-md-offset-2 col-md-10">
				      <button id="submitButton" class="btn btn-success" ><i class="icon-save"></i>保存</button>
					  &nbsp;
				      <button type="button" onclick="history.back();" class="btn"><i class="icon-undo"></i>返回</button>
				    </div>
				  </div>
				</form>
			</div>
		</div>
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
		$(function() {
		    $("#userRepoForm").validate({
		        submitHandler: function(form) {
					bootbox.animate(false);
					var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
		            form.submit();
		        },
		        errorClass: 'validate-error'
		    });
		})
         </script>	
	</body>
</html>
