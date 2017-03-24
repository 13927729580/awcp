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
		          <li class="active">组织机构与权限</</li>
		          <li class="active">开发组管理</li>
		        </ul>
			</div>
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath%>manage/devGroupList.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">组名</span>
								<input name="nameCn" class="form-control" id="nameCn" type="text" value="${vo.nameCn}"/>
							</div>
							
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">组织机构代码</span>
								<input name="code" class="form-control" id="code" type="text" value="${vo.code}"/>
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
				<form  method="post" id="manuList">	
            	<input type="hidden" name="currentPage" value="${currentPage}">		
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>组名</th>
							<th>组织结构代码</th>
							<th>英文名</th>
							<th >缩写</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value="${vo.id}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${vo.nameCn}</td>
							<td>${vo.code}</td>
							<td>${vo.nameEn}</td>
							<td>${vo.nameShort}</td>
						</tr>
					</c:forEach>
					</tbody>
					<tfoot>
					</tfoot>
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
          	//add
      		$("#addBtn").click(function(){
      			var url = "<%=basePath%>manage/intoPunDevGroup.do";
      			location.href = url;
      			return false;
      		})
      		
      		//授权
      		$("#authorize").click(function(){
				var basePath = $("#basePath").val();
				var url = basePath+"unit/resAuthor.do";
				$("#ResAuthor").attr("action",url).submit();
				return false;
		    })

      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","<%=basePath%>manage/punDevGroupGet.do").submit();
      			}else{
      				alertMessage("请选择一个组织进行操作");
      			}
      			return false;
      		});
      		
          	//delete
          	$("#deleteBtn").click(function(){
          		//var count = $("input:checkbox[name='boxs']:checked").length;
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
	          		$("#manuList").attr("action","<%=basePath%>manage/punDevGroupDelete.do").submit();
          		}
      			return false;
          	});
          	
          });
		</script>
	</body>
</html>
