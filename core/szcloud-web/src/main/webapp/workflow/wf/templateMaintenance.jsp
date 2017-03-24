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
		          <li class="active">模板维护</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="deleteBtn">
					<i class="icon-edit">删除</i>
				</button>
				<button type="button" class="btn btn-success" id="authorityBtn">
					<i class="icon-edit">权限</i>
				</button>
				<button type="button" class="btn btn-info" id="searchBtn"
					data-toggle="collapse" data-target="#collapseButton">
					<i class="icon-search">  查询</i>
				</button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath%>workflow/wf/templateMaintenance.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="1" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">公文模板类型</span>
								<!--<input class="form-control" id="templateType" type="text"/>-->
								<select name="templateTypeId" class="form-control">
									<option value="" >请选择</option>
						     	 	<c:forEach items="${templateTypes}" var="item">
							  	 	   <option value="${item.TypeId}" <c:if test="${item.TypeId==templateTypeId}">selected="selected"</c:if> >${item.TypeName}</option>
							  		</c:forEach>
							  </select>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">名称</span>
								<input class="form-control" name="templateName" value="${templateName}" type="text"/>
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
				<form  action="<%=basePath%>/workflow/wf/templateMaintenance.do"  method="post" id="menuList">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>序号</th>
					        <th>模板名称</th>					        
					        <th>模板编码</th>
							<th>创建人</th>					        
					        <th>创建时间</th>
							<th>修改人</th>					        
					        <th>修改时间</th>
							<th>可用性</th>
							<th>顺序</th>
							<th>所属分类</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${models}" var="item">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${item.TemplateID}">
							<td>${item.TemplateID}</td>
							<td><a href="<%=basePath%>/workflow/wf/openTemplate.do?TemplateID=${item.TemplateID}" >${item.TemplateName}</a></td>
							<td>${item.TemplateCode}</td>
						    <td>${item.Creator}</td>
						    <td><fmt:formatDate value="${item.CreateDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${item.Editor}</td>
							<td><fmt:formatDate value="${item.EditDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						    <td>${item.TemplateIsValid == 1 ? '可用' : '不可用'}</td>
					        <td>${item.OrderID}</td>
							<td>${item.TypeName}</td>
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
			  
			  $("#deleteBtn").click(function(){
			  	if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
	          		$("#menuList").attr("action","<%=basePath%>/workflow/wf/templateManager.do").submit();
          		}
      			return false;
      		  });
      		  
      		  $("#authorityBtn").click(function(){
			  	if(count!=1){
          			alertMessage("请选择一项进行操作");
          			return false;
          		}
	          	$("#menuList").attr("action","<%=basePath%>/workflow/wf/templateAuthority.do").submit();
      			return false;
      		  });
		  });
		</script>
	</body>
</html>
