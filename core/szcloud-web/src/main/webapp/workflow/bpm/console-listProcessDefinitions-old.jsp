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
 		<title>列表</title>
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
		          <li class="active">流程列表</li>
		        </ul>
			</div>
			-->
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="deployBtn">部署新流程</button>
				
				<button type="button" class="btn btn-success" id="graphBtn">流程图</button>
				
				<!-- <button type="button" class="btn btn-warning" id="viewXmlBtn"><i class="icon-edit"></i>查看XML</button> -->
				
				<button type="button" class="btn btn-success" id="bindBtn"><i class="icon-edit"></i>绑定</button>
				
<!-- 				<button type="button" class="btn btn-success" id="confBtn"><i class="icon-edit"></i>配置</button>  -->

				<button type="button" class="btn btn-info" id="editBtn"><i class="icon-edit"></i>修改模型</button>
				
				<!--<button type="button" class="btn btn-success" id="viewBtn"><i class="icon-edit"></i>查看资源</button>-->
				
			</div>
			
			<div class="row" id="datatable">
				<form  method="get" id="createForm" action="console-listProcessDefinitions.do">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>编号</th>
					       <!--  <th>代码</th> -->
					        <th>名称</th>
					       <!--  <th>分类</th> -->
					        <th>版本</th>
					        <th>描述</th>
					        <th>状态</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${models}" var="item">
						<tr>
							<td class="hidden formData"><input id="id" type="hidden"
								value="${item.id}"><input id="deploymentId" type="hidden"
								value="${item.deploymentId}"><input id="processDefinitionId" type="hidden"
								value="${item.id}">
								
								 </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${item.id}</td>
						    <%-- <td>${item.key}</td> --%>
						    <td>${item.name}</td>
						    <%-- <td>${item.category}</td> --%>
						    <td>${item.version}</td>
						    <td>${item.description}</td>
						    <td>
							  <c:if test="${item.suspended}">
							    挂起
					            <a href="console-activeProcessDefinition.do?id=${item.id}">(激活)</a>
							  </c:if>
							  <c:if test="${not item.suspended}">
							    激活
					            <a href="console-suspendProcessDefinition.do?id=${item.id}">(挂起)</a>
							  </c:if>
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
			<%@ include file="../../resources/include/common_form_css.jsp" %>
			<div id="deployInput" title="创建模型" style="display:none;width:80%">
        <form id="modelForm" action="deploy.do" method="post" enctype="multipart/form-data" class="form-horizontal">
       
		<table style="width: 500px;">
			<tr>
				<td>部署已有模板：</td>
				<td>
					<input type="file" name="file" id="file" />
				</td>
			</tr>
			<tr>
				<td>在线部署</td>
				<td>
					<input type="button" name="button" id="button" onclick="window.location.href = 'deploy_new.do'" value="在线设计" />
				</td>
			</tr>
		</table>
        </form>
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
					  this.$table.find("tbody tr").find("input#deploymentId").removeAttr("name");
					  this.$table.find("tbody tr").find("input#processDefinitionId").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#deploymentId").attr("name","deploymentId");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#processDefinitionId").attr("name","processDefinitionId");
					  }
				  }
					
			  });
			//graph
      		$("#graphBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","console-graphProcessDefinition.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});

      		//edit
      		$("#editBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","console-beforeUpdateProcess.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      	
      		$("#bindBtn").click(function(){
      			if(count == 1){

      				$("#createForm").attr("action","bpm-process-list-old.do").submit();
 
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
      		//conf 配置
      		$("#confBtn").click(function(){
      			if(count == 1){
      				$("#createForm").attr("action","bpm-conf-node-list.do").submit();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      		});
      		
      		$("#viewBtn").click(function(){
          		//var count = $("input:checkbox[name='boxs']:checked").length;
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}else{
          			$("#createForm").attr("action","console-listDeploymentResourceNames.do").submit();
          		}
          		return false;
          	});
      		
      		$("#deployBtn").click(function(){
      			var cont = document.getElementById('deployInput');
      			cont.style.display = 'block';
      			var deployModule = dialog({
				    title: '部署已有模板',
				    content:cont,
				    id: 'EF892L',
				    width:'300px',
				   
				    okValue: '部署',
				    ok: function () {
				    	if (!$('#file').val()) {
							alert('请选择文件！');
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
      			deployModule.showModal();
      			return false;
      		});
      		
      		
      		
          });
		  
		</script>
	</body>
</html>
