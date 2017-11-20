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
 		<title>模型</title>
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
		          <li class="active">发布流程</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				 <region:region-permission permission="user:create">
		  <button class="btn btn-success" onclick="location.href='form-template-input.do'"><i class="icon-plus-sign"></i>新建</button>
		  </region:region-permission>
		  <region:region-permission permission="user:delete">
		  <button class="btn btn-warning" onclick="table.removeAll()"><i class="icon-trash"></i>删除</button>
		 <!--  <button type="button" class="btn btn-warning" id="deleteBtn"><i class="icon-trash"></i>删除</button> -->
		  </region:region-permission>
		  <button class="btn btn-info" onclick="table.exportExcel()"><i class="icon-edit"></i>导出</button>
			</div>
			
			<!-- <div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">菜单名</span>
								<input name="menuName" class="form-control" id="menuName" type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon">地址</span>
								<input name="menuAddress" class="form-control" id="menuAddress" type="text"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">隶属于</span>
								<input name="parentMenuId" class="form-control" id="parentMenuId" type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon">菜单类型</span>
								<input name="menuType" class="form-control" id="menuType" type="text"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<a class="btn" data-toggle="collapse" data-target="#collapseButton">取消</a>
						</div>
					</form>
				</div>
			</div> -->
			
			<div class="row" id="datatable">
				<form  method="get" id="createForm" action="form-listDrafts.do">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th width="10" class="m-table-check"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th>
        <th class="sorting" name="code">编号</th>
        <th class="sorting" name="category">分类</th>
        <th class="sorting" name="status">状态</th>
        <th class="sorting" name="ref">引用</th>
        <th width="80">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${records}" var="item">
						<tr>
						<td class="hidden formData"><input id="selectedItem" type="hidden"
								value="${item.code}"> </td>
					      
					        <td>${item.code}</td>
					        <td>${item.category}</td>
					        <td>${item.status == 0 ? '流程草稿' : '任务草稿'}</td>
					        <td>${item.ref}</td>
					        <td>
					          <a href="form-viewStartForm.do?businessKey=${item.code}&bpmProcessId=${item.category}">发起流程</a>
					        </td>
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
					  this.$table.find("tbody tr").find("input#selectedItem").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#selectedItem").attr("name","selectedItem");
					  }
				  }
					
			  });
          	
      		//edit
      		$("#editBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","modeler-open.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
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
	          		$("#createForm").attr("action","modeler-remove.do").submit();
          		}
      			return false;
          	});
          	
          });
		  
		  $(function() {
		    	$('#create').click(function() {
		    		/* $('#createModelTemplate').show(); */
		    		 
		    	});
		    });
		</script>
	</body>
</html>
