<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
 		<title>待办列表</title>
 		<%@ include file="../../resources/include/common_css.jsp" %>
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
		          <li><a href="javascript:;">个人公文</a></li>
		          <li class="active">待办任务</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="newTask">
					<i class="icon-edit">新建</i>
				</button>				
				<button type="button" class="btn btn-success" id="delete">
					<i class="icon-edit">删除</i>
				</button>
				<button type="button" class="btn btn-info" id="statusBtn"
					data-toggle="collapse" data-target="#collapseButton">
					<i class="icon-edit">  状态</i>
				</button>
				<button type="button" class="btn btn-info" id="searchBtn"
					data-toggle="collapse" data-target="#collapseButton">
					<i class="icon-search">  查询</i>
				</button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath%>workflow/wf/listPersonalTasks.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="1" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">名称</span>
								<input class="form-control" name="workItemName" value="${workItemName}" type="text"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<a class="btn" data-toggle="collapse" data-target="#collapseButton">取消</a>
						</div>
					</form>
				</div>
			</div>
			
			<div class="row" id="datatable">
				<form  action="<%=basePath%>/workflow/wf/listPersonalTasks.do"  method="post" id="menuList">	
	            <input type="hidden" name="currentPage" value="${currentPage}">
	            <input type="hidden" name="message" value="${message}" id="message">
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th data-width="50">序号</th>
					        <th>公文名称</th>
					        <th data-width="80">申请人</th>
					        <th data-width="140">创建时间</th>
					        <th data-width="100">最后经办人</th>
					        <th data-width="140">修改时间</th>
       						<th data-width="70">状态</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${models}" var="item">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${item.WorkItemID}"><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${item.WorkItemID}</td>
							<td><a href="<%=basePath%>workflow/wf/openTask.do?WorkItemID=${item.WorkItemID}&EntryID=${item.EntryID}&FlowTempleteID=${TemplateID}&flowTaskType=1" >${item.WorkItemName}</a></td>
						    <td>${item.Creator}</td>
						    <td><fmt:formatDate value="${item.CreateDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						    <td>${item.Editor}</td>
							<td><fmt:formatDate value="${item.EditDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						    <td>${item.WorkItemStatus == 0 ? '流转中' : '结束'}</td>
					       
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
		
		<link rel="stylesheet" href="<%=basePath%>base/resources/artDialog/css/ui-dialog.css">
		<script src="<%=basePath%>base/resources/artDialog/dist/dialog-min.js"></script>

		<script type="text/javascript">
		$(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#id").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
					  }
				  }
			  });
			  
			var message = $("#message").val();
			if(message != null && message != ''){
			 	alertMessage(message);
			}
          	
      		 $("#newTask").click(function(){
      			 $("#menuList").attr("action","<%=basePath%>/workflow/wf/templateList.do").submit();
      		 });
      		 
      		 $("#delete").click(function(){
      			 if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
      				$("#menuList").attr("action","<%=basePath%>/workflow/wf/deletePersonalTask.do").submit();
      			}
      			return false;
      		 });
      		 
      		 $("#statusBtn").click(function(){
      			if(count !=1){
          			alertMessage("请选择一项进行操作");
          			return false;
          		}
      			$("#menuList").attr("action","<%=basePath%>/workflow/wf/taskStatus.do?taskType=1").submit();
      			return false;
      		});

		 });	 
		</script>
	</body>
</html>
