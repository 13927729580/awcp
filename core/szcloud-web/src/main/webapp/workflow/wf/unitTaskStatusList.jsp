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
 		<title>流程管理</title>
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
			
			
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="javascript:;">个人公文</a></li>
		          <li class="active">任务状态</li>
		        </ul>
			</div>
			-->
			<div class="row" id="buttons">
				<button type="button" class="btn" id="returnBtn">
					<i class="icon-edit">返回</i>
				</button>				
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath%>workflow/wf/draftList.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="1" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">名称</span>
								<input class="form-control" name="workItemName" value="${workItemName}" type="text"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<!--<a class="btn" data-toggle="collapse" data-target="#collapseButton">取消</a> -->
						</div>
					</form>
				</div>
			</div>
			
			<div class="row" id="datatable">
				<form  action="<%=basePath%>/workflow/wf/draftList.do"  method="post" id="menuList">	
	            <input type="hidden" name="currentPage" value="${currentPage}">
	            <input type="hidden" name="taskType" id="taskType" value="${taskType}">		
				<table class="table datatable table-bordered">
					<thead>
							<th data-width="130">办理时间</th>
							<th data-width="100">办理人</th>
					        <th data-width="160">办理步骤</th>
					        <th class="text-ellipsis">处理意见</th>
					        <th data-width="180">已转发给</th>
					        <!--<th data-width="100">状态</th>
					        <th data-width="160">送达步骤</th>
					        <th data-width="160">发送人</th>-->
					        <!--<th data-width="160">处理步骤</th>
							<th data-width="100">处理人</th>					        
					        <th data-width="100">收到时间</th>
					        
					        <th class="text-ellipsis">处理意见</th>
					        <th data-width="100">状态</th>
					        <th data-width="160">送达步骤</th>
					        <th data-width="160">发送人</th>-->
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${models}" var="item">
						<tr>
							<td><fmt:formatDate value="${item.FinishedDate}" pattern="yyyy-MM-dd HH:mm" /></td>
							<td>${item.Recipients}</td>
							<td>${item.ActivityName}</td>
							<td>${item.ChoiceContent}</td>
							<td>${item.SendRecipients}</td>
							<!--<td>${item.sendStep}</td>
							<td>${item.sender}</td>-->
							<!--<td>${item.activityName}</td>
							<td>${item.participant}</td>
							<td>${item.inceptDate}</td>
					        
							<td>${item.handleView}</td>
							<td>${item.handleStatus}</td>
							<td>${item.sendStep}</td>
							<td>${item.sender}</td>-->
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
			  
			  $("#returnBtn").click(function(){
			       if(${taskType} == 1)
      					$("#menuList").attr("action","<%=basePath%>/workflow/wf/unitPersonalTasks.do").submit();
      				else if(${taskType} == 2)
      				    $("#menuList").attr("action","<%=basePath%>/workflow/wf/unitHistoryTasks.do").submit();
      				else
      				    $("#menuList").attr("action","<%=basePath%>/workflow/wf/unitAllTasks.do").submit();
      			return false;
      		});
		  });
		</script>
	</body>
</html>
