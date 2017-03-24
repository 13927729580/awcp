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
 		<%@ include file="../resources/include/common_css.jsp" %><!-- 注意加载路径 -->
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
		          <!-- 
		          <li><a href="<%=basePath %>unit/punRelateSys.do">系统管理（已关联）</a></li>
		           -->
		          <li><a href="<%=basePath %>unit/manageRole.do?boxs=${sysId}">角色管理</a></li>
		          <li class="active">角色关联的用户管理</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-info" id="addBtn"><i class="icon-edit"></i>新增关联用户</button>
				<button type="button" class="btn btn-info" id="cancelBtn"><i class="icon-trash"></i>取消关联</button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form  method="post" action="<%=basePath%>unit/roleRelateUserQuery.do"
						id="createForm">
						<input type="hidden" name="currentPage" value="0" />
						<input type="hidden" name="roleId" value="${roleId}"/>						
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">身份证号</span>
								<input name="userIdCardNumber" class="form-control" id="userIdCardNumber" type="text" value="${vo.userIdCardNumber}"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">姓名</span>
								<input name="name" class="form-control" id="name" type="text" value="${vo.name}"/>
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
							<th data-width="150px">身份证号</th>
							<th data-width="100px">姓名</th>
							<th>用户名</th>
							<th>移动电话</th>
							<th>办公电话</th>
							<th>头衔</th>
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
			            <td>${vo.userIdCardNumber}</td>
			            <td>${vo.name}</td>
			            <td>${vo.userName}</td>
			            <td>${vo.mobile}</td>
			            <td>${vo.userOfficePhone}</td>
			            <td>${vo.userTitle}</td>
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
	 
	 <%@ include file="../resources/include/common_js.jsp" %>
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
      		
      		//新增关联用户
			$("#addBtn").click(function() {
					top.dialog({
						id : 'add-dialog' + Math.ceil(Math.random() * 10000),
						title : '新增关联用户',
						url : "<%=basePath%>unit/roleNotRelateUserQuery.do?roleId=${roleId}&sysId=${sysId}",
						height : 500,
						width : 1200,
						onclose : function() {
							alertMessage(this.returnValue);
							window.location = "<%=basePath%>unit/roleRelateUserQuery.do?roleId=${roleId}&sysId=${sysId}";
						}
					}).showModal();
					return false;
				});

				//取消关联
				$("#cancelBtn").click(function() {
				if (count > 0) {
					$("#userList").attr("action","<%=basePath%>unit/cancelUserRoleRelation.do").submit();
      			}else{
      				alert("请至少选择一个用户进行操作");
      			}
      			return false;
      		});
          	
          });
		  function alertMessage(message){
				var md = dialog({
				    content: message
				});
				md.show();
				setTimeout(function () {
				    md.close().remove();
				}, 2000);
			}
		</script>
	</body>
</html>
