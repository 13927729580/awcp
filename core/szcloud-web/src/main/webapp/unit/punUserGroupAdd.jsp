<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
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
<title>用户管理</title>
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="../resources/include/common_css.jsp"%><!-- 注意加载路径 -->
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
				<li><a href="<%=basePath%>punUserGroupController/getUserList.do?groupId=${groupId }&rootGroupId=${rootGroupId }">人员信息管理</a></li>
				<li class="active">新增成员信息</li>
			</ul>
		</div>

		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="addBtn">
				<i class="icon-plus-sign"></i>关联				
			</button>
			<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
		</div>
		
		<div class="row" id="searchform">
				<div id="collapseButton" class="in">
					<form action="<%=basePath%>punUserGroupController/get.do"
						id="createForm">
						<input type="hidden" name="currentPage" value="0" />
						<input id="groupId" type="hidden" name="groupId" value="${groupId}"/>
						<input id="rootGroupId" type="hidden" name="rootGroupId" value="${rootGroupId}"/>							
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

	<form method="post" id="userGroup" action="punUserGroupController/save.do">
		<div class="row" id="datatable">
			<table class="table datatable table-bordered">
				<thead>
					<tr>
						<th class="hidden"><input type="hidden" name="currentPage" value="${currentPage}"></th>
						<th>姓名</th>
						<th>用户名</th>
						<th>工号</th>
					</tr>
				</thead>
				
				<tbody>
						<c:forEach items="${vos}" var="vo">
							<tr>
								<td class="hidden formData">
								<input id="boxs" type="name" value="${vo.userId}">
								<input id="groupId" type="hidden" name="groupId" value="${groupId}"/>
								<input id="rootGroupId" type="hidden" name="rootGroupId" value="${rootGroupId}"/>	
								</td>
								<td>${vo.name}</td>
								<td>${vo.userName}</td>
								<td>${vo.employeeId}</td>
							</tr>
						</c:forEach>
				</tbody>
				
			</table>
		</div>
		
		<!-- 以下职位选择无样式模板 -->
			<div class="row" id="datatable">
				 <div class="col-md-10">
				          <c:forEach items="${positionList}" var="p">
				              	<input name="positionId" type="checkbox" value="${p.positionId }"/>
			            		${p.name } &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		            	  </c:forEach>
			    </div>
			    <div class="col-md-10">
			            	  是否管理岗  <input id="isManager" name="isManager" type="checkbox" value="1"/>
				</div>
			</div>
		
		</form>
		
		<!-- 职位选择和分页之间冲突 -->
		<div class="row navbar-fixed-bottom text-center" id="pagers">
			<sc:PageNavigation dpName="vos"></sc:PageNavigation>
		</div>
	</div>

	<%@ include file="../resources/include/common_js.jsp"%>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/pageTurn.js"></script>
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
		  			 var positionIds = $("input:checkbox[name='positionId']:checked").length;
		  			if(count < 1){
		      			alert("请选择人员");
		      			return false;
		      		}
		      		if(positionIds <1 ){
		      			alert("请选择岗位");
		      			return false;
		      		}
		          	$("#userGroup").attr("action","<%=basePath%>punUserGroupController/save.do").submit();
		  			return false;
		  		});
			
		});
	</script>
</body>
</html>
