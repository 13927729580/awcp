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
			-->
			
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="javascript:;">流程管理</a></li>
		          <li class="active">模板权限</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="newBtn">
					<i class="icon-edit">新建</i>
				</button>
				<button type="button" class="btn btn-success" id="deleteBtn">
					<i class="icon-edit">删除</i>
				</button>
			</div>			
			
			<div class="row" id="datatable">
				<form  action="<%=basePath%>/workflow/wf/templateAuthority.do"  method="post" id="menuList">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th data-width="560">模板名称</th>
					        <th>用户</th>
							<th>读取权限</th>					        
					        <th>新建权限</th>
							<th>删除权限</th>
							<th>查询权限</th>
							<th>归档权限</th>
							<th>跟踪权限</th>
							<th>应用权限</th>
							<th>完全控制权限</th>								
						</tr>
					</thead>
					<tbody>					
					<c:forEach items="${models}" var="item">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${item.RightIndex}">
							<td>${item.ModuleName}</td>
							<td>${item.UserName}</td>
						    <td>${item.ReadRight == 1 ? '有' : '无'}</td>
						    <td>${item.CreateRight == 1 ? '有' : '无'}</td>
							<td>${item.DeleteRight == 1 ? '有' : '无'}</td>
							<td>${item.QueryRight == 1 ? '有' : '无'}</td>
							<td>${item.FileRight == 1 ? '有' : '无'}</td>
							<td>${item.TailRight == 1 ? '有' : '无'}</td>
							<td>${item.AppRight == 1 ? '有' : '无'}</td>
							<td>${item.AllRight == 1 ? '有' : '无'}</td>						
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
			  $("#newBtn").click(function(){
      			$("#menuList").attr("action","<%=basePath%>/workflow/wf/createTemplateAuthority.do").submit();
      		  });
			
			  $("#deleteBtn").click(function(){
      			if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
      				$("#menuList").attr("action","<%=basePath%>/workflow/wf/deleteTemplateAuthority.do").submit();
      			}
      			return false;
      		});
		  });
		</script>
	</body>
</html>