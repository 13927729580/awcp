<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<title>组信息编辑</title>

<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->

</head>
<body id="main">

		<div class="container-fluid">

			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
					<li><i class="icon-location-arrow icon-muted"></i></li>
					<!-- <li><a >首页</a></li>
					<li><a >动态表单</a></li> -->
					<li class="active">动态页面模版编辑</li>
				</ul>
			</div>
			
			<div class="row" id="dataform">
			<c:if test="${result!=null&&''!=result}">
				<span style="color: red">(${result})</span>
			</c:if>
			<form class="form-horizontal" id="groupForm" action="<%=basePath%>pfmTemplateController/saveOrUpdate.do" method="post" enctype="multipart/form-data">
				<div class="C_addForm">
					<input type="hidden" name="id" value="${vo.id}" />
					<input type="hidden" name="fileLocation" value="${vo.fileLocation}" />
					<div class="form-group">
						<label class="col-md-1 control-label required">模板名称：</label>
						<div class="col-md-4">
							<input name="fileName" class="form-control" id="fileName"
								type="text" placeholder="" value="${vo.fileName}">
						</div>
						
						<label class="col-md-1 control-label required">模板描述：</label>
						<div class="col-md-4">
							<input name="description" class="form-control" id="description"
								type="text" placeholder="" value="${vo.description}">
						</div>
					</div>
					
					<div class="form-group">
						<%-- <c:if test="${not empty vo.id }">
							<label class="col-md-1 control-label required">模板附件：</label>
							<div class="col-md-4">
								<a href="<%=basePath%>${vo.fileLocation }" target="blank">查看模板</a>
							</div>
						</c:if> --%>
						<label class="col-md-1 control-label required">上传模板：</label>
						<div class="col-md-9">
							<input name="file"	id="file" type="file" />(已上传：${vo.fileLocation })
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label required">模版内容：</label>
						<div class="col-md-9">
							<textarea name='content'  rows='16' class='form-control'>${vo.content}</textarea>
						</div>
					</div>
				</div>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
						<a href="<%=basePath%>pfmTemplateController/pageList.do" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		        </div>
			</form>

		</div>

		</div>

		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script type="text/javascript">
		$(function(){
	   			$.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
				   	$("#groupForm").submit();
	    		},onError:function(){alert("错误，请看提示")}});
			$("#fileName").formValidator({onFocus:"请输入模板名称",onCorrect:"符合要求"}).inputValidator({min:1,max:30,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
		})
	</script>


</body>
</html>
