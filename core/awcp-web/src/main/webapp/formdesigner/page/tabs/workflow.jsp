<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
		
<div class="btn-group mb10">
	<button type="button" class="btn btn-sm btn-info" id="addWorkflowBtn"><i class="icon-plus-sign"></i>新增</button>
	<button type="button" class="btn btn-sm btn-info" id="deleteWorkflowBtn"><i class="icon-remove"></i>删除</button>
	<button type="button" class="btn btn-sm btn-info" id="configWorkflowBtn"><i class="icon-plus-sign"></i>配置节点参数</button>
	<button type="button" class="btn btn-sm btn-info" id="authorityWorkflowBtn"><i class="icon-plus-sign"></i>配置权限组</button>
</div>

<form method="post">
	<table class="table datatable table-bordered" id="nodetable">
		<thead>
			<tr>
				<th class="hidden"></th>
				<th>结点名称</th>
				<th>流程名称</th>
			</tr>
		</thead>
		<tbody id="noteList">	
		<c:forEach items="${nodeList}" var="nVo">
			<tr id="nodetable_tr${nVo.id}_${nVo.workflowId}">
				<td class="hidden formData">
					<input id="nodeIds" type="hidden" value="${nVo.id}_${nVo.workflowId}"></td>
				<td>${nVo.name}</td>
				<td>${nVo.workflowName}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</form>


