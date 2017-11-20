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
 		<title>流程列表</title>
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
		          <li><a href="javascript:;">流程管理</a></li>
		          <li class="active">运行管理</li>
		        </ul>
			</div>
			-->
			<div class="row" id="buttons">
				<!--<button type="button" class="btn btn-success" id="migrateBtn"><i class="icon-edit"></i>迁移</button>
				-->
				<button type="button" class="btn btn-success" id="viewtaskBtn"><i class="icon-plus-sign"></i>查看任务</button>
				<button type="button" class="btn btn-warning" id="graphBtn"><i class="icon-edit"></i>流程图</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				
				<button type="button" class="btn btn-success" id="useTaskBtn"><i class="icon-edit"></i>更换流程</button>
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
				<form  method="get" id="createForm" action="console-listProcessInstances.do">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>编号</th>
					        <th>流程定义</th>
					        <th>环节</th>
					        <th>状态</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${models}" var="item">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${item.id}">
								<input id="processInstanceId" type="hidden"
								value="${item.id}">
								<input id="processDefinitionId" type="hidden"
								value="${item.processDefinitionId}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${item.id}</td>
						    <td>${item.processDefinitionId}</td>
						    <td>${item.activityId}</td>
						    <td>
							  <c:if test="${item.suspended}">
							    挂起
					            <a href="console-activeProcessInstance.do?id=${item.id}">(激活)</a>
							  </c:if>
							  <c:if test="${not item.suspended}">
							    激活
					            <a href="console-suspendProcessInstance.do?id=${item.id}" alt="挂起后，不能在启动实例">(挂起)</a>
							  </c:if>
							</td>
							
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
					  this.$table.find("tbody tr").find("input#id").removeAttr("name");
					  this.$table.find("tbody tr").find("input#processDefinitionId").removeAttr("name");
					  this.$table.find("tbody tr").find("input#processInstanceId").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#processDefinitionId").attr("name","processDefinitionId");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#processInstanceId").attr("name","processInstanceId");
					  }
				  }
					
			  });
          	//add
      		$("#addBtn").click(function(){
      			var url = "modeler-open.do";
      			location.href = url;
      			return false;
      		})

      		//migrate
      		$("#migrateBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","console-migrateInput.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
          	
      		//viewtask 
      		$("#viewtaskBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","console-listTasks.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
          	//delete
          	$("#deleteBtn").click(function(){
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
	          		$("#createForm").attr("action","console-removeProcessInstance.do").submit();
          		}
      			return false;
          	});
          	
          //查看流程图
      		$("#graphBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","workspace-viewHistory.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
          	//更换流程
      		$("#useTaskBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","console-listDefforChangeTasks.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
          });
		</script>
	</body>
</html>
