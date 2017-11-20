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
 		<title>菜单查询页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_css.jsp" %>
	</head>
	<body id="main">
	<div class="container-fluid">
		<!--整体布局结构 
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
	          <li class="active">菜单管理</li>
	        </ul>
		</div>
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
			<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
			<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
			<button type="button" class="btn btn-info" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
		</div>
		<div class="row" id="searchform">
			<div id="collapseButton" class="collapse">
				<form action="<%=basePath%>dev/punDevMenuList.do" id="createForm">
					<input type="hidden" name="currentPage" value="0" />
					<div class="col-xs-3">
						<div class="input-group">
							<span class="input-group-addon">菜单名</span>
							<input name="menuName" class="form-control" id="menuName" value="${vo.menuName}" type="text"/>
						</div>
					</div>
					<div class="btn-group">
						<button class="btn btn-primary" type="submit">提交</button>
						<button class="btn" data-toggle="in" data-target="#collapseButton">取消</button>
					</div>
				</form>
			</div>
		</div>
		<div class="row" id="datatable">
			<table class="table datatable table-bordered">
				<thead>
					<tr>
						<!-- <th width="10%" class="text-center"><input type="checkbox"
							value="" style="margin-top: -3px;"
							onclick="this.value=check('boxs')"></th> -->
						<th class="hidden"></th>
						<th>菜单名</th>
						<th>地址</th>
						<th>隶属于</th>
						<th >菜单类型</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData"><input name="boxs" type="hidden"
								value="${vo.menuId}"> <input type="hidden"
								name="currentPage" value="${currentPage}"></td>
							<td>${vo.menuName}</td>
							<td>${vo.menuAddress}</td>
							<td>${vo.parentMenu.menuName}</td>
							<td>${vo.menuType}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
				</tfoot>
			</table>
		</div>
		<div class="row navbar-fixed-bottom text-center" id="pagers">
			<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
		</div>
	</div>

		<div class="C_tabBox clearfix hide">
			<div class="C_tabList">
				<form method="post" id="manuList">
					<table class="table datatable table-bordered">
						<thead>
							<tr>
								<!-- <th width="10%" class="text-center"><input type="checkbox"
									value="" style="margin-top: -3px;"
									onclick="this.value=check('boxs')"></th> -->
								<th class="hidden"></th>
								<th>菜单名</th>
								<th>地址</th>
								<th>隶属于</th>
								<th >菜单类型</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${vos}" var="vo">
								<tr>
									<td class="hidden formData"><input name="boxs" type="hidden"
										value="${vo.menuId}"> <input type="hidden"
										name="currentPage" value="${currentPage}"></td>
									<td>${vo.menuName}</td>
									<td>${vo.menuAddress}</td>
									<td>${vo.parentMenuId}</td>
									<td>${vo.menuType}</td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
						</tfoot>
					</table>
				</form>
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
					  count = event.checks.checks.length;
				  }
					
			  });
          	//新增
      		$("#addBtn").click(function(){
      			var url = "<%=basePath%>dev/intoPunDevMenu.do";
      			location.href = url;
      			return false;
      		})
      		
      		//授权
      		$("#authorize").click(function(){
			var basePath = $("#basePath").val();
			var url = basePath+"dev/resAuthor.do";
			$("#ResAuthor").attr("action",url).submit();
			return false;
		    })

      		//update
      		$("#updateBtn").click(function(){
      			//var count = $("input:checkbox[name='boxs']:checked").length;
      			if(count == 1){
      				$("#manuList").attr("action","<%=basePath%>dev/intoPunDevMenu.do?").submit();
      			}else{
      				alert("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
          	//delete
          	$("#deleteBtn").click(function(){
          		//var count = $("input:checkbox[name='boxs']:checked").length;
          		if(count<1){
          			alert("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
	          		$("#manuList").attr("action","<%=basePath%>dev/punDevMenuDelete.do?").submit();
          		}
      			return false;
          	})
          	
          });
		  var checkflag = "false";
			function check(fieldName) {
			var field=document.getElementsByName(fieldName);
			if (checkflag == "false") {
				for (i = 0; i < field.length; i++) {
				field[i].checked = true;}
				checkflag = "true";
				return "Uncheck All"; }
			else {
				for (i = 0; i < field.length; i++) {
				field[i].checked = false; }
				checkflag = "false";
				return "Check All"; }
			}
		</script>
	</body>
</html>
