<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
  <%@include file="../common/taglibs.jsp"%>
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
 		<title>流程列表</title>
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
			
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="javascript:;">流程管理</a></li>
		          <li class="active">运行管理</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新建委托</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				
			</div>-->
			<div class="row" id="datatable">
				<form  method="get" id="createForm" action="console-listProcessInstances.do">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
						 <th class="sorting" name="id">编号</th>
        <th>委托人</th>
        
        <th>被委托人</th>
        <th>委托时间</th>
         <th>任务Id</th>
        <th>状态</th>
						</tr>
					</thead>
					<tbody>
					 <c:forEach items="${bpmDelegateHistories}" var="item">
      <tr>
        <td class="hidden formData"><input id="selectedItem" type="hidden"
								value="${item.id}"><input id="id" type="hidden"
								value="${item.id}"></td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
	    <td>${item.id}</td>
	    <td><tags:user userId="${item.assignee}"/></td>
	    <td><tags:user userId="${item.attorney}"/></td>
	    <td><fmt:formatDate value="${item.delegateTime}" pattern="yyyy-MM-dd"/></td>
	    <td>${item.taskId}</td>
	    <td>${item.status == 1 ? '有效' : '无效'} &nbps;<a href="delegate-looktaskHistories.do?taskId=${item.taskId}">查看</a></td>
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

		<script type="text/javascript">
		  $(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#selectedItem").removeAttr("name");
					  this.$table.find("tbody tr").find("input#id").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#selectedItem").attr("name","selectedItem");
					  }
				  }
			  });
          });
		</script>
	</body>
</html>
