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
 		<title>角色关联的用户管理</title>
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
		          <li><a href="#">角色管理</a></li>
		          <li class="active">角色关联的用户管理</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<c:if test="${relateOrNot == 0 }">
					<button type="button" class="btn btn-success" id="relateBtn"><i class="icon-plus-sign"></i>关联</button>
				</c:if>
				<c:if test="${relateOrNot == 1 }">
					<button type="button" class="btn btn-info" id="cancelBtn"><i class="icon-trash"></i>取消关联</button>
				</c:if>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath%>dev/devRoleRelateUserQuery.do"
						id="createForm">
						<input type="hidden" name="currentPage" value="0" />
						<input type="hidden" name="roleId" value="${roleId}"/>
						<!-- <div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">姓名</span>
								<input name="name" class="form-control" id="name" type="text"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">身份证号</span>
								<input name="userIdCardNumber" class="form-control" id="userIdCardNumber" type="text"/>
							</div>
						</div> -->
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">状态</span>
								<select name="relateOrNot" class="form-control">
                             		<option value="0">未关联</option>
                             		<option value="1">已关联</option>
                             	</select>
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
			<form  method="post" id="userList">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>姓名</th>
							<th>用户名</th>
							<th>工号</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${vos}" var="vo">
			          <tr> 	
			            <td class="hidden formData">
			            	<input id="boxs" type="hidden" value="${vo.userId}">
			            	<input type="hidden" name="currentPage" value="${currentPage}">	
			            	<input name="roleId" type="hidden" value="${roleId}"/>
			            </td>
			            <td>${vo.name}</td>
			            <td>${vo.userName}</td>
			            <td>${vo.employeeId}</td>
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
          	//关联
      		$("#relateBtn").click(function(){
      			if(count > 0){
      				$("#userList").attr("action","<%=basePath%>dev/devUserRelateRole.do").submit();
      			}else{
      				alert("请至少选择一个用户进行操作");
      			}
      			return false;
      		})
      		
      		//取消关联
      		$("#cancelBtn").click(function(){
      			if(count > 0){
      				$("#userList").attr("action","<%=basePath%>dev/cancelDevUserRoleRelation.do").submit();
      			}else{
      				alert("请至少选择一个用户进行操作");
      			}
      			return false;
      		});
          	
          });
		</script>
	</body>
</html>
