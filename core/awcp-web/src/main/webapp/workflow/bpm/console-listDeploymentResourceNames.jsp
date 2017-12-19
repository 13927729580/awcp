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
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>运行任务列表</title>
 		<%@include file="../../resources/include/common_css.jsp" %>
 		<%@include file="../common/taglibs.jsp"%>
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--list页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="buttons">按钮区</div>
			<div class="row" id="searchform">搜索区</div>
			<div class="row" id="datatable">数据展示区</div>
			<div class="row" id="pagers">分页</div>
			-->
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="../../main.jsp">首页</a></li>
		          <li><a href="console-listProcessDefinitions.do">流程管理</a></li>
		          <li class="active">流程资源</li>
		        </ul>
			</div>
			
			<!-- <div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="completeBtn"><i class="icon-plus-sign"></i>完成</button>
				<button type="button" class="btn btn-warning" id="historyBtn"><i class="icon-edit"></i>历史</button>
			</div> -->
		
			<div class="row" id="datatable">
				<form  method="get" id="createForm" action="console-listTasks.do">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>资源名称</th>
					       
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${deploymentResourceNames}" var="item">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${item}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${item}</td>
						   
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</form>
			</div>
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="models"></sc:PageNavigation> 
			</div>
			
		</div>
		<%@ include file="../../resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>

		<script type="text/javascript">
		  $(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  $(event.target).find("tbody tr").find("input#id").removeAttr("name");
					  $(event.target).find("tbody tr").find("input#processInstanceId").removeAttr("name");

					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  $(event.target).find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
						  $(event.target).find("tbody tr").eq(checkArray[i]).find("input#processInstanceId").attr("name","processInstanceId");
					  }
				  }
					
			  });
      		//complete
      		$("#completeBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","workspace-viewHistory.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
          	
      		//history
      		$("#historyBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","workspace-prepareCompleteTask.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
          });
		</script>
	</body>
</html>