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
 		<title>菜单编辑页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="../../resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--edit页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="dataform">表单区（--输入组--按钮组）</div>
			-->
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="${basePath}main.jsp">首页</a></li>
		          <li><a href="${basePath}workflow/bpm/bpm-process-list.do">流程绑定</a></li>
		          <li class="active">流程绑定</li>
		        </ul>
			</div>
			
			<div class="row" id="dataform">
			<form id="userRepoForm" method="post" action="<%=basePath%>workflow/bpm/bpm-bind.do"  class="form-horizontal">
			<legend>
			<c:if test="${model == null}"> 添加流程绑定 </c:if>
			<c:if test="${model != null}">绑定到已有  <input id="userRepo_id" type="hidden" name="id" value="${model.id}"></c:if>
					
				
				</legend>
	<div class="form-group">
		<label class="col-md-2 control-label required" for="bpm-process_bpmConfBaseId">流程定义</label>
	    <div class="col-md-4">
	    <input type="hidden" id="bpm-process_bpmConfBaseId" name="bpmConfBaseId"  value="${bpmConfBase.id}">
	    	${bpmConfBase.processDefinitionId}
	    </div>
	  </div>
				  <div class="form-group">
	<label class="col-md-2 control-label required" for="bpm-process_name">名称</label>
    <div class="col-md-4">
      <input id="bpm-process_name" type="text" name="name" value="${model.name}" size="40" class="form-control" minlength="1" maxlength="10">
    </div>
  </div>
  <div class="form-group">
	<label class="col-md-2 control-label required" for="bpm-process_bpmCategoryId">流程分类</label>
    <div class="col-md-4">
      <select id="bpm-process_bpmCategoryId" name="bpmCategoryId"  class="form-control">
      <c:forEach items="${bpmCategories}" var="item">
	    <option value="${item.id}" ${item.id==model.bpmCategory ? 'selected' : ''}>${item.name}</option>
	  </c:forEach>
	  </select>
    </div>
  </div>
  
  <div class="form-group">
	<label class="col-md-2 control-label required" for="bpm-process_priority">排序</label>
    <div class="col-md-4">
      <input id="bpm-process_priority" type="text" name="priority" value="${model.priority}" size="40" class="form-control" minlength="1" maxlength="50">
    </div>
  </div>
  <div class="form-group">
	<label class="col-md-2 control-label required" for="bpm-process_useTaskConf">配置任务负责人</label>
    <div class="col-md-4">
      <label class="radio-inline"><input id="bpm-process_useTaskConf_0" type="radio" name="useTaskConf" value="1" ${model.useTaskConf == 1 ? 'checked' : ''}>开启</label>
      <label class="radio-inline"><input id="bpm-process_useTaskConf_1" type="radio" name="useTaskConf" value="0" ${model.useTaskConf != 1 ? 'checked' : ''}>关闭</label>
    </div>
  </div>
  <div class="form-group">
	<label class="col-md-2 control-label required" for="bpm-process_descn">描述</label>
    <div class="col-md-4">
      <input id="bpm-process_descn" type="text" name="descn" value="${model.descn}" size="40" class="form-control" maxlength="50">
    </div>
  </div>

				  <div class="form-group">
					<label class="col-md-2 control-label required" for="bpm-category_priority">排序</label>
				    <div class="col-md-4">
				      <input id="bpm-category_priority" type="text" name="priority" value="${model.priority}" size="40" class="form-control" minlength="1" maxlength="50">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-md-offset-2 col-md-10">
				      <button id="submitButton" class="btn btn-success" ><i class="icon-save"></i>保存</button>
					  &nbsp;
				      <button type="button" onclick="history.back();" class="btn"><i class="icon-undo"></i>返回</button>
				    </div>
				  </div>
				</form>
			</div>
		</div>
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
		$(function() {
		    $("#userRepoForm").validate({
		        submitHandler: function(form) {
					bootbox.animate(false);
					var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
		            form.submit();
		        },
		        errorClass: 'validate-error'
		    });
		})
         </script>	
	</body>
</html>
