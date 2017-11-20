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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>开发人员信息编辑</title>

<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->

</head>
<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
					<li><i class="icon-location-arrow icon-muted"></i></li>
					<li><a >首页</a></li>
					<li><a >组织机构与权限</a></li>
					<li class="active">开发人员个人密码修改</li>
				</ul>
			</div>
			
			<div class="row" id="dataform">
				<form class="form-horizontal" id="userForm" action="" method="post">
			         <div class="form-group">   
					 	<label class="col-md-6 control-label required">原密码：</label>
			            <div class="col-md-6">
			                <input name="oldPassword" class="form-control" id="oldPassword" type="password" placeholder="" value="">
			            </div> 
					 </div>
			          <div class="form-group">   
					 	<label class="col-md-6 control-label required">新密码：</label>
			            <div class="col-md-6">
			                <input name="newPassword" class="form-control" id="newPassword" type="password" placeholder="" value="">
			            </div> 
					 </div>
			          <div class="form-group">   
			            <label class="col-md-6 control-label required">重复新密码：</label>
			            <div class="col-md-6">
			                <input name="againPassword" class="form-control" id="againPassword" type="password" placeholder="" value="">
			            </div>
					 </div>
					 
					<div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-2 col-md-10">
			              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
			              	<button type="submit" class="btn btn-success" id="cancelBtn"><i class="icon-save"></i>取消</button>
			            </div>
			        </div>
				</form>
			</div>
		</div>

		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script type="text/javascript">
			$(function(){
	   			$.formValidator.initConfig({formID:"userForm",debug:false,onSuccess:function(){
				   	$("#userForm").submit();
	    		},onError:function(){alert("错误，请根据提示填写相关信息")}});
				$("#oldPassword").formValidator({empty:true,onShow:"请输入原始密码"}).inputValidator({max:30,onError:"最长30位"});
				$("#newPassword").formValidator({empty:true,onShow:"请输入新密码"}).inputValidator({max:50,onError:"最长50位"});
				$("#againPassword").formValidator({empty:true,onShow:"请重复输入新密码"}).inputValidator({max:20,onError:"最长20位"});
			})
		</script>


</body>
</html>
