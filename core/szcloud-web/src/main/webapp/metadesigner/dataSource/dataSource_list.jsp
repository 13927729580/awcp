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
<!DOCTYPE html >
<html>
	<head>
		<title>元数据管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
		<%@ include file="../../resources/include/common_css.jsp" %>
	</head>
<body id="main">
	<div class="container-fluid">
			<%-- <div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="<%=basePath%>devAdmin/intoSystem.do?sysId=1">首页</a></li>
		          <li><a href="<%=basePath %>metaModel/selectPage.do">元数据管理</a></li>
		          <li class="active">元数据模型管理</li>
		        </ul>
			</div> --%>
			<div class="row" id="buttons">
				<div class="btn-group">
					<button type="button" class="btn  btn-success dropdown-toggle" data-toggle="dropdown">
						<i class="icon-plus-sign"></i>新增 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="javascript:void(0)" onclick="addSource(1);">内部数据源</a></li>
						<li><a href="javascript:void(0)" onclick="addSource(2);">外部数据源</a></li>
			
					</ul>
				</div>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="selectPage.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">数据源名称</span>
								<input name="name" class="form-control" id="name" type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon">数据源连接</span>
								<input name="sourceUrl" class="form-control" id="sourceUrl" type="text"/>
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
							<th>数据源名称</th>
				            <th>最大连接数(个)</th>
				            <th>超时时间(分钟)</th>
				            <th>类型</th>
						</tr>
					</thead>
					<tbody>
					<form  method="post" id="manuList">	
	            	<input type="hidden" name="currentPage" value="${currentPage}">		
					<c:forEach items="${list}" var="k">
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value="${k.id}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
								<td><a href="<%=basePath%>dataSourceManage/table.do?id=${k.id}">${k.name }</a></td>
								<td>${k.maximumConnectionCount }</td>
								<td>${k.maximumActiveTime }</td>
								<td>${k.domain}</td>
								 
						</tr>
					</c:forEach>
					</form>
					</tbody>
				</table>
			</div>
			
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="list"></sc:PageNavigation> 
			</div>
	</div>
		<%@ include file="../../resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
		<script type="text/javascript">
		
		function addSource(type){
			var turl="<%=basePath %>dataSourceManage/edit-ds-info.do";
			
			if(type==1){
				turl=turl+"?domain=local";
			}else if(type==2){
				turl=turl+"?domain=remote";
			}
			top.dialog({
					id: 'add-dataSource' + Math.ceil(Math.random()*10000),
					url: turl,
					title:"添加数据源",					
					onclose: function () {
						location.href = "<%=basePath%>dataSourceManage/selectPage.do";						
						}
					}).showModal(); 
				return false;
		}
		  $(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","id");
					  }
				  }
					
			  });
          	//add
      		$("#addBtn").click(function(){
      			top.dialog({
					id: 'add-dataSource' + Math.ceil(Math.random()*10000),
					url: "<%=basePath%>/dataSourceManage/get.do",
					title:"添加数据源",
					height:200,
					width:200,
					onclose: function () {
						location.href = "<%=basePath%>dataSourceManage/selectPage.do";	
						if(this.returnValue){
							alert(this.returnValue);
							}
						}
					}).showModal(); 
				return false;
      		});
      		

      		//update
 			$("#updateBtn").click(function(){
	if(count == 1){
		top.dialog({
			id: 'add-dataSource' + Math.ceil(Math.random()*10000),
			url: "<%=basePath%>/dataSourceManage/get.do?id="+$(":input[name='id']").val(),
			onclose: function () {
				location.href = "<%=basePath%>dataSourceManage/selectPage.do";	
			
			}
			}).showModal(); 
			return false;
				}else{
					alertMessage("请选择某个资源进行操作");
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
					$("#manuList").attr("action","remove.do").submit();
          		}
      			return false;
          	});
          	
          	//关联属性
          	$("#relationBtn").click(function(){
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(count>1){
          			alertMessage("只能选择一条数据");
          			return false;
          		}
          		else{
	          		$("#manuList").attr("action","<%=basePath%>metaModelItems/queryResultByParams.do?currentPage=1").submit();
          		}
          	});
          	
          });
	</script>
</body>
</html>