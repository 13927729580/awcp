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
 		<title>流程分类列表</title>
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
		          <li class="active">流程分类</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新建</button>
				<button type="button" class="btn btn-warning" id="editBtn"><i class="icon-edit"></i>编辑</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-success" id="exportBtn"><i class="icon-edit"></i>导出</button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="form-template-list.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">名称</span>
								<input name="bpmCategory_name" class="form-control" id="bpmCategory_name" type="text"/>
							</div>
						</div>						
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit" onclick="document.createForm.submit()">提交</button>
							<a class="btn" data-toggle="collapse" data-target="#collapseButton">取消</a>
						</div>
					</form>
				</div>
			</div>
			
			<div class="row" id="datatable">
				<form  method="get" id="manuList" action="bpm-category-list.do">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th class="sorting" name="id">编号</th>
        <th class="sorting" name="name">名称</th>
        <th class="sorting" name="code">标识</th>
        <th class="sorting" name="type">类型</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${models}" var="item">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${item.id}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
        <td>${item.id}</td>
        <td>${item.name}</td>
        <td>${item.code}</td>
        <td>${item.type == '1' ? '外部' : '内部'}</td>
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
					  this.$table.find("tbody tr").find("input#id").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
					  }
				  }
					
			  });
          	//add
      		$("#addBtn").click(function(){
      			var url = "form-template-input.do";
      			location.href = url;
      			return false;
      		})

      		//edit
      		$("#editBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","form-template-input.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
          	
      		//export 导出
      		$("#exportBtn").click(function(){
      			if(count > 0){
      				$("#manuList").attr("action","form-template-export.do").submit();
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
	          		$("#manuList").attr("action","form-template-remove.do").submit();
          		}
      			return false;
          	});
          	
          });	  
		  
		</script>
	</body>
</html>
