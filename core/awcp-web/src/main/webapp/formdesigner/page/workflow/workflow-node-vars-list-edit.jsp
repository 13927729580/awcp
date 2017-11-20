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
 		<title>节点参数配置页面</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
	</head>
	<body id="main" class="inner-frame">
		<div class="container-fluid">
			<div class="row">
				<form action="<%=basePath%>fd/workflow/addNodeVariable.do"  method="post" id="manuList">	
					<input type="hidden" id="pageId" name="pageId" value="${pageId}"/>
					<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}"/>
					<input type="hidden" id="dataSourceArray" value="${dataSourceArray}"/>
					<div class="col-sm-4">
						<div class="input-group">
							<span class="input-group-addon">变量名</span>
							<input name="variableName" class="form-control" id="variableName" type="text"/>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="input-group">
							<span class="input-group-addon">值</span>
							<div class="input-group" id="variableSelectInput">
					            <input type="text" class="form-control sI_input" name="value" id="value">
					            <div class="input-group-btn">
					              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" tabindex="-1">
					                	<span class="caret"></span>
					              </button>
					              <ul class="dropdown-menu pull-right sI_select">
					              </ul>
					            </div>
					         </div>
						</div>
					</div>
					<div class="col-sm-4 btn-group">
						<button type="submit" class="btn btn-primary" id="addVarai">提交</button>
					</div>
				</form>
			</div>
			<div class="row">
				<table id="formDataTable" class="table table-bordered table-hover">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>序号</th>
							<th>变量</th>
							<th>值</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody >
						<c:if test="${!empty wVars }">
						<c:forEach items="${wVars}" var="wVar" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${wVar.variableName}<input type="hidden"  name="wVars.variableName" id="" value="${wVar.variableName}"/></td>
								<td>${wVar.value}<input type="hidden"  name="wVars.value" id="" value="${wVar.value}"/></td>
								<td><a href="<%=basePath%>fd/workflow/delNodeVariable.do?varName=${wVar.variableName}&pageId=${pageId}&nodeId=${nodeId}" class="mybtn">删除</a></td>
							</tr>
						</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/json2.js"></script>
		<script type="text/javascript">
   	 	var varArray = new Array();
	   	 function initializeDataSource(modelJsonArray){
	   		if(modelJsonArray && modelJsonArray.length > 0){
	   			for(var i = 0; i < modelJsonArray.length;i++){
	   				if(modelJsonArray[i].modelItemCodes){
	   					 var itemCodes = modelJsonArray[i].modelItemCodes.split(",");
	   					 for(var j = 0; j < itemCodes.length; j++){
	   						var option = "<li><a href='javascript:;' data='" + modelJsonArray[i].name + "." + itemCodes[j] + "'>" +
	   							modelJsonArray[i].name + "." + itemCodes[j] +"</a></li>";
	   						$("#variableSelectInput .sI_select").append(option);
	   					 }
	   				}
	   			}
	   		}
	   	}
   	 	
		$(document).ready(function(){
 			var d = $("#dataSourceArray").val();
 			initializeDataSource(JSON.parse(d));
 			$("body").on("click","#variableSelectInput a",function(){
 				var value = $(this).attr("data");
 				$("#variableSelectInput .sI_input").val(value);
 			});
		});
				
		</script>
	</body>
</html>
