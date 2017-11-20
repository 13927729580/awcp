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
<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>流程列表</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="../../resources/include/common_css.jsp" %><!-- 注意加载路径 -->
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
		          <li><a href="#">首页</a></li>
		          <li><a href="#">我的流程</a></li>
		          <li class="active">办结的流程</li>
		        </ul>
			</div>
			<!--
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
				<button type="button" class="btn btn-success" id="editBtn"><i class="icon-edit"></i>弹窗编辑</button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			
			<div class="row" id="searchform">
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
			</div>
			-->
			<div class="row" id="datatable">
			    <form  method="post" id="createForm" action="workspace-listCompletedProcessInstances.do">	
	            <input type="hidden" name="currentPage" value="${currentPage}">
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							   <th class="sorting" name="id">编号</th>
						        <th class="sorting" name="name">流程定义</th>
						        <th class="sorting" name="createTime">创建时间</th>
						        <th class="sorting" name="endTime">结束时间</th>
						        <th class="sorting" name="assignee">负责人</th>
						        <th width="170">&nbsp;</th>
						</tr>
					</thead>
					<tbody>							
					<c:forEach items="${models}" var="item">
				      <tr>
					    <td>${item.id}</td>
					    <td>${item.processDefinitionId}</td>
					    <td><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td><fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					    <td><tags:user userId="${item.startUserId}"/></td>
				        <td>
				          <a href="workspace-viewHistory.do?processInstanceId=${item.id}">历史</a>
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
		<!-- other hide div 
		<div class="hidden" id="editDiv">
			<form class="form-horizontal" id="editForm" action="#" method="post">
				<div class="form-group">
					<label class="col-md-2 control-label">父节点</label>
					<div class="col-md-4">
						<input name="parentMenuId" class="form-control" id="parentMenuId" type="text" placeholder="" value="10053">
					</div>
					<label class="col-md-2 control-label required">资源类型:</label>
					<div class="col-md-4">
						<input name="menuType" class="form-control" id="menuType" type="text" placeholder="" value="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">资源名称:</label>
					<div class="col-md-4">
						<input name="menuName" class="form-control" id="menuName" type="text" placeholder="" value="后台">
					</div>
					<label class="col-md-2 control-label required">资源地址:</label>
					<div class="col-md-4">
						<input name="menuAddress" class="form-control" id="menuAddress" type="text" placeholder="" value="backend" title="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">资源顺序:</label>
					<div class="col-md-4">
						<input name="menuSeq" class="form-control" id="menuSeq" type="text" placeholder="" value="1">
					</div>
				</div>
			</form>
		</div>-->
		<!-- other hide div -->
		<%@ include file="../../resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>

		<script type="text/javascript">
		  $(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
					  }
				  }
					
			  });
		  }
		</script>
	</body>
</html>
