<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@include file="../common/taglibs.jsp"%>
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


			<button type="button" class="btn btn-success" id="viewTaskForm">
				<i class="icon-edit"></i>完成
			</button>
			<c:if test="${delegationState != 'PENDING'}">
				<button type="button" class="btn btn-info" id="Delegate">
					<i class="icon-edit"></i>代理
				</button>
			</c:if>
			<c:if test="${delegationState == 'PENDING'}">
				<button type="button" class="btn btn-info" id="resolveTask">
					<i class="icon-edit"></i>处理
				</button>
			</c:if>
			<button type="button" class="btn btn-warning" id="rollback">
				<i class="icon-edit"></i>回退
			</button>
			<button type="button" class="btn btn-info" id="history">
				<i class="icon-edit"></i>历史
			</button>
			<!-- <button type="button" class="btn btn-warning" id="changeCounterSign"><i class="icon-edit"></i>加减签</button>-->
			<button type="button" class="btn btn-success" id="prepareJump">
				<i class="icon-edit"></i>自由跳转
			</button>

		</div>
			
			<!-- <div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">菜单名</span>
								<input name="menuName" class="form-control" id="menuName" type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon">地址</span>
								<input name="menuAddress" class="form-control" id="menuAddress" type="text"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">隶属于</span>
								<input name="parentMenuId" class="form-control" id="parentMenuId" type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon">菜单类型</span>
								<input name="menuType" class="form-control" id="menuType" type="text"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<a class="btn" data-toggle="collapse" data-target="#collapseButton">取消</a>
						</div>
					</form>
				</div>
			</div> -->
			
			<div class="row" id="datatable">
				<form  method="get" id="manuList">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>编号</th>
        					<th>名称</th>
       						 <th>创建时间</th>
       						 <th>负责人</th>
       						 <th>状态</th>
       						
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${models}" var="item">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${item.id}"> <input id="processInstanceId" type="hidden"
								value="${item.processInstanceId}"> 
								<input id="executionId" type="hidden"
								value="${item.executionId}"></td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${item.id}</td>
						    <td>${item.name}</td>
						    <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						    <td><tags:user userId="${item.assignee}"/></td>
						    <td>${item.suspended ? '挂起' : '激活'}</td>
					       
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</form>
			</div>
			
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
			</div>
			
		</div>
		
		<%@ include file="../../resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
		
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/artDialog/css/ui-dialog.css">
		<script src="<%=basePath%>resources/plugins/artDialog/dist/dialog-min.js"></script>

		<script type="text/javascript">
		$(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#id").removeAttr("name");
					  this.$table.find("tbody tr").find("input#processInstanceId").removeAttr("name");
					  this.$table.find("tbody tr").find("input#executionId").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#processInstanceId").attr("name","processInstanceId");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#executionId").attr("name","executionId");
					  }
				  }
					
			  });
        
          	
      		$("#viewTaskForm").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","${scopePrefix}/workflow/form/form-viewTaskForm.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		})

      		
      		$("#Delegate").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","workspace-prepareDelegateTask.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
          	
      		
      		$("#resolveTask").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","workspace-resolveTask.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
		  
      		$("#rollback").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","workspace-rollback.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		$("#history").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","workspace-viewHistory.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		$("#prepareJump").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","console-prepareJump.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
		 });	 
		</script>
	</body>
</html>
