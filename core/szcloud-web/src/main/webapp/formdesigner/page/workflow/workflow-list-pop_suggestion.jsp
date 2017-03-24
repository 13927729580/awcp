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
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>流程列表页面</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="selectBtn"><i class="icon-plus-sign"></i>确定</button>
<!-- 				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button> -->
			</div>
			<div class="row" id="searchform">
				<form action="<%=basePath%>fd/workflow/templateList.do" id="createForm" class="clearfix" method="post">
					<input type="hidden" name="currentPage" value="${currentPage }" />
					<div class="col-sm-4">
						<div class="input-group">
							<span class="input-group-addon">流程名</span>
							<input name="workflowName" class="form-control" id="workflowName" type="text" value="${workflowName}"/>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="input-group">
							<span class="input-group-addon">流程类型</span>
							<select name="type" class="form-control" id="type">
								<option value="0">--请选择流程类型--</option>
								<c:forEach items="${categories }" var="c">
									<!--<option value="${c.TypeId }">${c.TypeName }</option>-->
									<c:choose>
										<c:when test="${c.TypeId == categoryId}">
											<option value="${c.TypeId }" selected="selected">${c.TypeName }</option>
										</c:when>
										<c:otherwise>
											<option value="${c.TypeId }">${c.TypeName }</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-sm-4 btn-group">
						<button class="btn btn-primary" type="submit">提交</button>
					</div>
				</form>
			</div>
			<form  method="post" id="manuList">	
				<input type="hidden" name="currentPage" value="${currentPage}">	
				<div class="row" id="datatable">
					<table class="table datatable table-bordered">
						<thead>
							<tr>
								<th class="hidden"></th>
								<th>流程名称</th>
								<th>流程类型</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${vos}" var="vo">
							<tr>
								<td class="hidden formData">
									<input id="templateID" type="hidden" value="${vo.templateID}">
									<input id="templateName" type="hidden" value="${vo.templateName}"></td>
								<td>${vo.templateName}</td>
								<td>${vo.typeName}</td>
							</tr>
							</c:forEach>
						</tbody>
						<tfoot>
						</tfoot>
					</table>
				</div>
			</form>
			
			<%-- --%> 
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
			</div>
		</div>
		
		<%@ include file="/resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			var count=0;//默认选择行数为0
			$('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#templateID").removeAttr("name");
					  this.$table.find("tbody tr").find("input#templateName").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#templateID").attr("name","tId");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#templateName").attr("name","tName");
					  }
				  }
			});
			var dialog;
			try {
				dialog = top.dialog.get(window);
				var data = dialog.data; // 获取对话框传递过来的数据
				if(data != null){
					//初始化？
				}
				dialog.height($(document).height());
				dialog.reset();
			} catch (e) {
				alert(e);
				return;
			}
			//保存
			$("#selectBtn").click(function(){
				if(count == 1){
					//alert($('input[name=_selects]:checked')[0]);
					var id = $("input[name='tId']").val();
					var name = $("input[name='tName']").val();			
					dialog.close({'id':id,'name':name}); // 关闭（隐藏）对话框
					dialog.remove();
				}else{
					alertMessage("请选择一个流程进行操作");
				}
				return false;
			});
		});
		</script>
	</body>
</html>
