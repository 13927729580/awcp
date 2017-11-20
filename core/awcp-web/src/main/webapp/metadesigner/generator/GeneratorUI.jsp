<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>代码生成器</title>
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->

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
				<li><a href="#">首页</a></li>

				<li class="active">代码生成器</li>
			</ul>
		</div>

		<div class="row" id="dataform">
			<form class="form-horizontal" id="groupForm"
				action="<%=basePath%>metaModel/generator.do" method="post">

				<legend>
					代码生成器
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
				</legend>
				<div class="form-group">
					<label class="col-md-2 control-label">命名空间名:</label>
					<div class="col-md-4">
						<input name="basePackage" class="form-control" id="basePackage"
							type="text" placeholder="">
					</div>
					<label class="col-md-2 control-label required">模块名:</label>
					<div class="col-md-4">
						<input name="persistence" class="form-control" id="persistence"
							type="text" placeholder="" value="core">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">生成路径:</label>
					<div class="col-md-4">
						<input name="outRoot" class="form-control" id="outRoot"
							type="text" placeholder="">
					</div>
					<label class="col-md-2 control-label required">模板路径:</label>
					<div class="col-md-4">
						<input name="templementSrc" class="form-control"
							id="templementSrc" type="text" placeholder="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">表名:</label>
					<div class="col-md-4">
						<input name="tableName" class="form-control" id="tableName"
							type="text" placeholder="">
					</div>
				</div>
				<div class="form-group">
					<!-- 表单提交按钮区域 -->
					<div class="col-md-offset-2 col-md-10">
						<button type="submit" class="btn btn-success" id="saveBtn">
							<i class="icon-save"></i>生成
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="/resources/include/common_form_js.jsp"%><!-- 注意加载路径 -->
	<script type="text/javascript">
		$(function() {
			$.formValidator.initConfig({
				formID : "groupForm",
				debug : false,
				onSuccess : function() {
					$("#groupForm").submit();
					return false;
				},
				onError : function() {
					alert("请按提示正确填写内容");
				}
			});
			$("#basePackage").formValidator({
				empty : true,
				onShow : "请输入命名空间",
				onCorrect : "符合格式要求"
			});
			$("#persistence").formValidator({
				onShow : "请输入模块名",
				onFocus : "至少1个长度",
				onCorrect : "合法"
			}).inputValidator({
				min : 1,
				empty : {
					leftEmpty : false,
					rightEmpty : false,
					emptyError : "不能有空符号"
				},
				onError : "不能为空,请确认"
			});
			$("#outRoot").formValidator({
				onShow : "请输入生成路径",
				onFocus : "至少1个长度",
				onCorrect : "合法"
			}).inputValidator({
				min : 1,
				empty : {
					leftEmpty : false,
					rightEmpty : false,
					emptyError : "不能有空符号"
				},
				onError : "不能为空,请确认"
			});
			$("#tableName").formValidator({
				onShow : "请输入表名",
				onFocus : "至少1个长度",
				onCorrect : "合法"
			}).inputValidator({
				min : 1,
				empty : {
					leftEmpty : false,
					rightEmpty : false,
					emptyError : "不能有空符号"
				},
				onError : "不能为空,请确认"
			});
			$("#templementSrc").formValidator({
				onShow : "请输入项目中模板的路径",
				onFocus : "至少1个长度",
				onCorrect : "合法"
			}).inputValidator({
				min : 1,
				empty : {
					leftEmpty : false,
					rightEmpty : false,
					emptyError : "不能有空符号"
				},
				onError : "不能为空,请确认"
			});
		});
		$("#saveBtn").click(function() {
			$("#groupForm").submit();
		})
	</script>
</body>
</html>
