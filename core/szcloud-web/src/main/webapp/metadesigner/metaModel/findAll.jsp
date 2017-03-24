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
		          <li><a href="<%=basePath %>metaModel/queryResult.do">元数据管理</a></li>
		          <li class="active">元数据模型管理</li>
		        </ul>
			</div> --%>
			<div class="row" id="buttons">
				<!-- <button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button> -->
				<div class="btn-group">
				<button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
					<i class="icon-plus-sign"></i>新增  <span class="caret"></span> 
				</button>
	            <ul class="dropdown-menu" role="menu">
	                <li><a href="#" id="addBtn">新增</a></li>
	                <li><a href="#" id="addbydb">已有数据源创建</a></li>
	            </ul>
	            </div>
				<!-- <button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除元数据及表</button> -->
				<button type="button" class="btn btn-info" id="deleteOnly"><i class="icon-trash"></i>仅删除元数据</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
				<button type="button" class="btn btn-warning" id="relationBtn"><i class="icon-edit"></i>关联属性</button>
				<button type="button" class="btn btn-info" id="releaseBtn"><i class="icon-trash">发布</i></button>
				<button type="button" class="btn btn-info" id="createTableBtn"><i class="icon-trash">生成数据库表</i></button>
				<button type="button" class="btn btn-info" id="batchUpdateDs"><i class="icon-trash">批量修改数据源</i></button>
				<button type="button" class="btn btn-info" id="synMetaBtn"><i class="icon-trash">同步</i></button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath %>metaModel/selectPage.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">模型名称(中)</span>
								<input name="modelName" class="form-control" id="modelName" type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon">模型名称(英)</span>
								<input name="modelCode" class="form-control" id="modelCode" type="text"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">数据库表名</span>
								<input name="tableName" class="form-control" id="tableName" type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon">所属项目</span>
								<input name="projectName" class="form-control" id="projectName" type="text"/>
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
							<th>所属分类</th>
				            <th>模型名称</th>
				            <th>模型编号</th>
				            <th>模型描述</th>
				            <th>数据表名</th>
				            <th>模型类型</th>
				            <th>状态</th>
						</tr>
					</thead>
					<tbody>
					
					<c:forEach items="${list}" var="k">
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value="${k.id}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
								<td class="text-break">${k.modelClassId }</td>
								<td class="text-break">${k.modelName }</td>
								<td class="text-break">${k.modelCode }</td>
								<td class="text-break">${k.modelDesc }</td>
								<td>${k.tableName }</td>
								<td class="text-break">
									<c:if test="${k.modelType==2 }">
										系统模型
									</c:if>
									<c:if test="${k.modelType==1 }">
										领域模型
									</c:if>
								</td>
								<td class="text-break">
									<c:if test="${k.modelSynchronization=='false' }">
										未发布
									</c:if>
									<c:if test="${k.modelSynchronization=='true' }">
										已发布
									</c:if>
								</td>
						</tr>
					</c:forEach>
					
					</tbody>
				</table>
				</form>
			</div>
			
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="list"></sc:PageNavigation> 
			</div>
	</div>
		<%@ include file="../../resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
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
      			var url = "toggle.do";
      			location.href = url;
      			return false;
      		});
          	
      		$("#addbydb").click(function(){
      			//alert(1);
      			var url = "<%=basePath %>dataSourceManage/edit-ds-info-temp.do";
      			location.href = url;
      			return false;
      		});
          	
      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","get.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
      		//只删除元数据
      		$("#deleteOnly").click(function(){
      			if(count < 1){
      				alertMessage("请至少选择一项进行操作");
          			return false;
      			}
      			if(window.confirm("确定删除？")){
					$("#manuList").attr("action","<%=basePath%>metaModel/removeModel.do").submit();
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
          		} else if(count>1){
          			alertMessage("只能选择一条数据");
          		} else{
	          		$("#manuList").attr("action","<%=basePath%>metaModelItems/queryResultByParams.do?currentPage=1").submit();
          		}
      			return false;
          	});
          	
          //release
          	$("#releaseBtn").click(function(){
          		$("#manuList").attr("action","<%=basePath%>metaModel/release.do").submit();
          		return false;
          	});
          	
          	//createTable
          	$("#createTableBtn").click(function(){
          		$("#manuList").attr("action","<%=basePath%>metaModel/createTable.do").submit();
          		return false;
          	});
          	
         	//createTable
          	$("#synMetaBtn").click(function(){
          		$("#manuList").attr("action","<%=basePath%>metaModel/synchronizedMeta.do").submit();
          		return false;
          	});
          	
          	//update
      		$("#batchUpdateDs").click(function(){
      			if(count>=1){
      				var _selects=new Array();
	
					$("input[name='id']").each(function(){
						var value=$(this).val();
						_selects.push(value);
					});
					//var data = {"_selects" : _selects.join(",")};
					top.dialog({
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						title : '载入中...',
						url : "<%=basePath %>metaModel/getDataSource.do?_selects=" + _selects.join(","),
						//data : data,
						width : 300,
						onclose : function() {
							if (this.returnValue) {
								var ret = this.returnValue;
								//loadComponentTable(dynamicPageId);
							}
						}
	
					}).showModal();
					return false;
      			}else{
      				alertMessage("请选择资源进行操作");
      			}
      			return false;
      		});
          	
          });
	</script>
</body>
</html>