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
<title>系统管理</title>
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="/resources/include/common_css.jsp"%><!-- 注意加载路径 -->
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
				<li><i class="icon-location-arrow icon-muted"></i></li>
<!-- 				<li><a href="#">开发者工作室</a></li> -->
				<!-- <li><a href="#">开发者</a></li> -->
				<li class="active">系统管理</li>
			</ul>
		</div>

		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="addBtn">
				<i class="icon-plus-sign"></i>新增
			</button>
			<button type="button" class="btn btn-info" id="deleteBtn">
				<i class="icon-trash"></i>删除
			</button>
			<button type="button" class="btn btn-warning" id="updateBtn">
				<i class="icon-edit"></i>修改
			</button>
			<button type="button" class="btn btn-success" id="onlineBtn">
			<i class="icon-edit"></i>发布申请
			</button> 
			<button type="button" class="btn btn-warning" id="offlineBtn">
			<i class="icon-edit"></i>下线申请
			</button>
<!-- 			<button type="button" class="btn btn-warning" id="menuBtn"> -->
<!-- 				<i class="icon-edit"></i>管理菜单 -->
<!-- 			</button> -->
<!-- 			<button type="button" class="btn btn-warning" id="roleBtn"> -->
<!-- 				<i class="icon-edit"></i>管理角色 -->
<!-- 			</button> -->
			<button type="button" class="btn btn-info" id="searchBtn"
				data-toggle="collapse" data-target="#collapseButton">
				<i class="icon-search"></i>
			</button>
		</div>

		<div class="row" id="searchform">
			<div id="collapseButton" class="collapse">
				<form action="<%=basePath%>dev/punSystemList.do"
					id="createForm">
					<input type="hidden" name="currentPage" value="0" />
					<div class="col-md-3">
						<div class="input-group">
							<span class="input-group-addon">系统名</span> <input name="sysName"
								class="form-control" id="sysName" value="${vo.sysName}" type="text" />
						</div>
					</div>
					<div class="col-md-3">
						<div class="input-group">
							<span class="input-group-addon">系统简称</span> <input name="sysShortName"
								class="form-control" id="sysShortName" value="${vo.sysShortName}" type="text" />
						</div>
					</div>
					<div class="col-md-3 btn-group">
						<button class="btn btn-primary" type="submit">提交</button>
						<a class="btn" data-toggle="collapse"
							data-target="#collapseButton">取消</a>
					</div>
				</form>
			</div>
		</div>

		<div class="row" id="datatable">
		<form method="post" id="userList">
			<input type="hidden" name="currentPage" value="${currentPage}">
			<table class="table datatable table-bordered">
				<thead>
					<tr>
						<th class="hidden"></th>
						<th>系统名</th>
						<th>系统简称</th>
						<th>系统地址</th>
						<th>发布状态</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData"><input id="boxs" type="name"
								value="${vo.sysId}"></td>
							<td><a href="<%=basePath%>dev/intoSystemCenter.do?boxs=${vo.sysId}">${vo.sysName}</a></td>
							<td>${vo.sysShortName}</td>
							<td>${vo.sysAddress}</td>
							<td><c:if test="${vo.sysStatus==1}">发布</c:if>
					                <c:if test="${vo.sysStatus==0}">未发布</c:if></td>
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

	<%@ include file="/resources/include/common_js.jsp"%>
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
          	//新增
      		$("#addBtn").click(function(){
      			var url = "<%=basePath%>dev/intoPunSystem.do";
      			location.href = url;
      			return false;
      		})
      		
      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){      				
      				$("#userList").attr("action","<%=basePath%>dev/punSystemGet.do").submit();
      				/* $(parent.document).find("#navbar").hide();
      				$(parent.document).find(".C-contentFrame").css({"width":"100%","marginLeft":0}); */
      			}else{
      				alertMessage("请选择一个系统进行操作");
      			}
      			return false;
      		});
          	
      		//menuBtn
      		$("#menuBtn").click(function(){
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>unit/intoPunMenuList.do").submit();
      			}else{
      				alertMessage("请选择一个系统进行操作");
      			}
      			return false;
      		});
      		
      		//roleBtn
      		$("#roleBtn").click(function(){
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>unit/listRolesInSys.do").submit();
      			}else{
      				alertMessage("请选择一个系统进行操作");
      			}
      			return false;
      		});
      		
      		//online
          	$("#onlineBtn").click(function(){
          		if(count!=1){
          			alertMessage("请选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定申请发布？")){
          			$("#userList").attr("action","<%=basePath%>dev/punSystemOnline.do").submit();
				}
				return false;
			});
          	
          	//offline
          	$("#offlineBtn").click(function(){
          		if(count!=1){
          			alertMessage("请选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定申请下线？")){
          			$("#userList").attr("action","<%=basePath%>dev/punSystemOffline.do").submit();
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
          			$("#userList").attr("action","<%=basePath%>dev/punSystemDelete.do")
											.submit();
								}
								return false;
							})
		});
	</script>
</body>
</html>
