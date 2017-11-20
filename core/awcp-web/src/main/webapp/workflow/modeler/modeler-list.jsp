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
			
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="javascript:;">流程管理</a></li>
		          <li class="active">发布流程</li>
		        </ul>
			</div>
			-->
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新建模型</button>
				<button type="button" class="btn btn-warning" id="editBtn"><i class="icon-edit"></i>编辑</button>
				<button type="button" class="btn btn-success" id="deployBtn"><i class="icon-edit"></i>发布</button>
				<!-- <button type="button" class="btn btn-success" id="confBtn"><i class="icon-edit"></i>配置</button> --> 
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
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
				<form  method="get" id="createForm" action="modeler-list.do">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>编号</th>
							<th>键值</th>
							<th>名称</th>
							<th>版本</th>
							<!--<th>分类</th>-->
							<th>创建时间</th>
							<th>更新时间</th>
							<th>发布编号</th>
							<th>元素信息</th>
							<!-- <th>操作</th> -->
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${models}" var="vo">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${vo.id}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${vo.id}</td>
							<td>${vo.key}</td>
							<td>${vo.name}</td>
							<td>${vo.version}</td>
							<!--<td>${vo.category}</td>-->
							<td>${vo.createTime}</td>
							<td>${vo.lastUpdateTime}</td>
							<td>${vo.deploymentId}</td>
							<td class="text-ellipsis">${vo.metaInfo}</td>
							<%-- <td>
							  <a href="modeler-open.do?id=${vo.id}" target="_blank">编辑</a>
							  <a href="modeler-remove.do?id=${vo.id}">删除</a>
							  <a href="modeler-deploy.do?id=${vo.id}">发布</a>
					        </td> --%>
							<!-- 如果出现td里面的文字过于长导致隔行的话，可以在td里面加上 class="text-ellipsis" 截断字数 -->
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</form>
			</div>
			
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="models"></sc:PageNavigation> 
			</div>
			<%@ include file="../../resources/include/common_form_css.jsp" %>
			<div id="createModelTemplate" title="创建模型" style="display:none;width:80%">
        <form id="modelForm" action="modeler-open.do" method="post"  class="form-horizontal">
       
		<table style="width: 500px;">
			<tr>
				<td>名称：</td>
				<td>
					<input id="name" name="name" type="text" class="form-control"/>
				</td>
			</tr>
			<tr>
				<td>KEY：</td>
				<td>
					<input id="key" name="key" type="text" class="form-control"/>
				</td>
			</tr>
			<tr>
				<td>描述：</td>
				<td>
					<textarea id="description" name="description" class="form-control"></textarea>
				</td>
			</tr>
		</table>
        </form>
	</div>
		</div>
		
		<%@ include file="../../resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
		
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/artDialog/css/ui-dialog.css">
		<script src="<%=basePath%>resources/plugins/artDialog/dist/dialog-min.js"></script>

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
      			//var url = "modeler-open.do";
      			//location.href = url;
      			var cont = document.getElementById('createModelTemplate');
      			cont.style.display = 'block';
      			var createModule = dialog({
				    title: '创建模板',
				    content:cont,
				    id: 'EF893L',
				    width:'500px',
				    okValue: '创建',
				    ok: function () {
				    	if (!$('#name').val()) {
							alert('请填写名称！');
							$('#name').focus();
							return false;
						}
				    	this.title('正在提交..');
		                setTimeout(function() {
		                    location.reload();
		                }, 1000);
						$('#modelForm').submit();
				        return true;
				    },
		      		cancelValue: '取消',
				    cancel: function () { this.close();return false;}
				});
      			createModule.showModal();
      			return false;
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
          	
      		//deploy 发布
      		$("#deployBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","modeler-deploy.do").submit();
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
          	
        	
        	
        	$("#confBtn").click(function(){
          		//var count = $("input:checkbox[name='boxs']:checked").length;
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}else{
          			$("#createForm").attr("action","../bpm/bpm-conf-node-list.do").submit();
          		}
          		return false;
          	});
          	
          });
		  
		
		</script>
	</body>
</html>
