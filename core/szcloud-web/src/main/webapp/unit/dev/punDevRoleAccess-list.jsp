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
 		<title>开发组织角色管理</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_css.jsp" %><!-- 注意加载路径 -->
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
		          <li><a href="#">组织机构与权限</a></li>
		          <!-- <li><a href="#">组织机构与权限</a></li> -->
		          <li class="active">开发组织角色管理</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
				<button type="button" class="btn btn-danger" id="accessBtn"><i class="icon-edit"></i>修改权限</button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">资源</span>
								<input name="resourceId" class="form-control" id="resourceId" value="${vo.resourceId}" type="text"/>
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
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>角色</th>
							<th>资源</th>
							<th>操作类型</th>
						</tr>
					</thead>
					<tbody>
					<form  method="post" id="userList">	
	            	<input type="hidden" name="currentPage" value="${currentPage}">		
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value="${vo.roleId}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${vo.roleId}</td>
							<td>${vo.resourceId}</td>
							<td>${vo.operType}</td>
							<!-- 如果出现td里面的文字过于长导致隔行的话，可以在td里面加上 class="text-ellipsis" 截断字数 -->
						</tr>
					</c:forEach>
					</form>
					</tbody>
				</table>
			</div>
			
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
			</div>
			
		</div>
		
		<%@ include file="/resources/include/common_js.jsp" %>
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
          	//add
      		$("#addBtn").click(function(){
      			var url = "<%=basePath%>dev/intoPunDevRoleAccess.do";
      			location.href = url;
      			return false;
      		})

      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>dev/punDevRoleAccessGet.do").submit();
      			}else{
      				alert("请选择一个用户进行操作");
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
          			$("#userList").attr("action","<%=basePath%>dev/punDevRoleAccessDelete.do").submit();
          		}
      			return false;
          	})
          	
          //updata Access
          	$("#accessBtn").click(function(){
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>dev/punDevSysMenuAccessEdit.do").submit();
      			}else{
      				alert("请选择一项进行操作");
      			}
      			return false;
      		});
          	
          	
          });
		</script>
	</body>
</html>
