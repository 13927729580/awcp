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
 		<title>用户库列表</title>
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
		          <li class="active">流程管理</li>
		        </ul>
			</div>
			-->
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增流程</button>
				<button type="button" class="btn btn-warning" id="editBtn"><i class="icon-edit"></i>修改流程</button>
				<button type="button" class="btn btn-warning" id="versionBtn"><i class="icon-edit"></i>版本管理</button>
				<!--<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>-->
				<button type="button" class="btn btn-info" id="viewBtn"><i class="icon-trash"></i>查看流程图</button>
				<button type="button" class="btn btn-info" id="editModelBtn"><i class="icon-trash"></i>修改模型</button>
				<button type="button" class="btn btn-success" id="exportModelBtn"><i class="icon-edit"></i>导出模型</button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
		
			</div>
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="bpm-process-list.do" method="post" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">名称</span>
								<input name="name" class="form-control" id="name" type="text"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">分类</span>
								 <select id="bpm-process_bpmCategoryId" name="category"  class="form-control">
						      <c:forEach items="${cates}" var="item">
							    <option value="${item.id}" ${item.id==model.bpmCategory ? 'selected' : ''}>${item.name}</option>
							  </c:forEach>
							  </select>
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
				<form  method="get" id="manuList" action="bpm-process-list.do">	
				 <input type="hidden" name="newVersion" id="newVersion" value="0">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
	            <table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>编号</th>
          					<th>流程名称</th>
         					 <th>分类</th>
          					<!--<th>流程Key</th>-->
         					 <th>当前版本</th>
         					 <th>排序</th>
       						 <th>是否显示</th>
						</tr>
					</thead>
					<tbody>
					
					<c:forEach items="${models}" var="vo" varStatus="status">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${vo.id}"> 
								<input id="bpmConfBaseId" type="hidden"
								value="${vo.confBaseId}">
								
								</td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							 <td>${status.count}</td>
					          <td>${vo.name}</td>
					          <td>${vo.catalogName}</td>
					         <!-- <td>${vo.processDefinitionKey}</td>-->
					          <td class="text-ellipsis">${vo.processDefinition}</td>
					          <td>${vo.priority}</td>
					          <td>${vo.isDisplay == 1?'是':'否'}</td>
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
					  this.$table.find("tbody tr").find("input#bpmConfBaseId").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#bpmConfBaseId").attr("name","bpmConfBaseId");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
					  }
				  }
					
			  });
          	//add
      		$("#addBtn").click(function(){
      			$("#manuList").attr("action","bpm-process-input.do").submit();
      			return false;
      		})

      		//edit
      		$("#editBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","bpm-process-input.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
          	
      		//conf 配置
      		$("#confBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","bpm-conf-node-list.do").submit();
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
	          		$("#manuList").attr("action","bpm-process-remove.do").submit();
          		}
      			return false;
          	});
          	
          //export 导出
      		$("#exportBtn").click(function(){
      			if(count > 0){
      				$("#manuList").attr("action","bpm-process-export.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
      		 //view 查看流程图
      		$("#viewBtn").click(function(){
      			if(count > 0){
      				var bid = document.getElementsByName("bpmConfBaseId");
      				if(bid[0]==null || bid[0].value == ""){
      					alert("当前流程还没有流程模型，请先创建");
      					$("#newVersion").attr("value","1");
      	                $("#manuList").attr("action","bpm-process-editModel.do").submit();
      				}else{
      					$("#manuList").attr("action","bpm-process-view.do").submit();
      				}
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
      		 //version 版本管理
      		$("#versionBtn").click(function(){
      			if(count > 0){
      				var bid = document.getElementsByName("bpmConfBaseId");
      				if(bid[0]==null || bid[0].value == ""){
      					alert("当前流程还没有版本，请创建");
      					$("#newVersion").attr("value","1");
      	                $("#manuList").attr("action","bpm-process-editModel.do").submit();
      				}else{
      					$("#manuList").attr("action","bpm-process-version.do").submit();
      				}
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
      		
      		 //exportModelBtn 版本管理
      		$("#exportModelBtn").click(function(){
      			if(count > 0){
      				var bid = document.getElementsByName("bpmConfBaseId");
      				if(bid[0]==null || bid[0].value == ""){
      					alert("当前流程还没有版本，请创建");
      					$("#newVersion").attr("value","1");
      	                $("#manuList").attr("action","bpm-process-editModel.do").submit();
      				}else{
      					$("#manuList").attr("action","bpm-model-export.do").submit();
      				}
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		//editModelBtnw 编辑流程模型
      		$("#editModelBtn").click(function(){
      			if(count > 0){
      				var bid = document.getElementsByName("bpmConfBaseId");
      				if(bid[0]==null || bid[0].value == ""){
      					alert("当前流程还没有流程模型，即将创建");
      					$("#newVersion").attr("value","1");
      	                $("#manuList").attr("action","bpm-process-editModel.do").submit();
      				}else{
	      				dialog({
	      				
	      				title: '选择提示',
	   					content: '选择“发布为新版本”后，正在运行的流程会继续按旧版本执行（可进版本管理里对运行流程进行修改），<br>选择“修改当前版”本后，所有的运行的流程都会按修改后的流程执行，请慎用此功能！',
					    button: [
					        {
					            value: '发布为新版本',
					            autofocus: true,
					            callback: function () {
					            	$("#newVersion").attr("value","1");
					               $("#manuList").attr("action","bpm-process-editModel.do").submit()
					            }
					        },
					        {
					            value: '修改当前版本',
					            callback: function () {
					               $("#manuList").attr("action","bpm-process-editModel.do").submit()
					            }
					        }
					    ]
						}).width(320).show();
      				}
      			return false;
      			
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
          });
		</script>
	</body>
</html>
