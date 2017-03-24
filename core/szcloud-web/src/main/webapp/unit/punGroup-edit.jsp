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

		<ul class="nav nav-tabs">
			<li class="active"><a href="javascript:;">组织信息编辑</a></li>
			<li><a
				href="<%=basePath%>unit/punGroupEdit.do?boxs=${vo.groupId}">组织树编辑</a>
			</li>
			<li><a
				href="<%=basePath%>punPositionController/pageList.do?groupId=${vo.groupId}">职务</a>
			</li>
		</ul>
		<div style="margin-top: 20px;"></div>

		<div class="row" id="dataform">

			<form class="form-horizontal" id="groupForm"
				action="<%=basePath%>unit/punGroupSave.do" method="post">

				<div class="C_addForm">
					<legend class=" text-center">
						组织信息编辑
						<c:if test="${result!=null&&''!=result}">
							<span style="color: red">(${result})</span>
						</c:if>
					</legend>
					<div class="form-group">   
					 	<label class="col-md-1 control-label required">组名：</label>
			            <div class="col-md-4">
			            	<input type="hidden" name="groupId" value="${vo.groupId}"/>
			                <input name="groupChName" class="form-control" id="groupChName" type="text" placeholder="" value="${vo.groupChName}">
			            </div> 
						<label class="col-md-2 control-label required">组简称：</label>
			            <div class="col-md-4">
			                <input name="groupShortName" class="form-control" id="groupShortName" type="text" placeholder="" value="${vo.groupShortName}">
			            </div> 
				 </div>
					  <div class="form-group">   
					 	<label class="col-md-1 control-label">传真：</label>
			            <div class="col-md-4">
			                <input name="fax" class="form-control" id="fax" type="text" placeholder="" value="${vo.fax}">
			            </div> 
			            <label class="col-md-2 control-label required">组名（英文）：</label>
			            <div class="col-md-4">
			                <input name="groupEnName" class="form-control" id="groupEnName" type="text" placeholder="" value="${vo.groupEnName}">
			            </div>
					 </div>
					 <div class="form-group">   
					 	<label class="col-md-1 control-label required">	组织机构代码：</label>
			            <div class="col-md-4">
			                <input name="orgCode" class="form-control" id="orgCode" type="text" placeholder="" value="${vo.orgCode}">
			            </div> 
			            <label class="col-md-2 control-label">地址：</label>
			            <div class="col-md-4">
			                <input name="groupAddress" class="form-control" id="groupAddress" type="text" placeholder="" value="${vo.groupAddress}">
			            </div> 
					 </div>
				
					  <div class="form-group">   
					 	<label class="col-md-1 control-label">邮编：</label>
			            <div class="col-md-4">
			                <input name="zipCode" class="form-control" id="zipCode" type="text" placeholder="" value="${vo.zipCode}">
			            </div> 
			            <label class="col-md-2 control-label">联系电话：</label>
			            <div class="col-md-4">
			                <input name="contactNumber" class="form-control" id="contactNumber" type="text" placeholder="" value="${vo.contactNumber}">
			            </div>
					 </div>
					 <div class="form-group">   
			            <label class="col-md-1 control-label">业务范围：</label>
			            <div class="col-md-4">
			                <input name="groupBusinessSphere" class="form-control" id="groupBusinessSphere" type="text" placeholder="" value="${vo.groupBusinessSphere}">
			            </div> 
					 </div>
					 <div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
					<!-- 	<a href="<%=basePath%>unit/punMenuList.do?currentPage=1" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>   -->
		            </div>
		        </div>
			</form>

		</div>

	</div>

	<%@ include file="/resources//include/common_form_js.jsp"%>
	<script type="text/javascript">
		$(function() {
			$.formValidator.initConfig({
				formID : "groupForm",
				debug : false,
				onSuccess : function() {
					$("#groupForm").submit();
				},
				onError : function() {
					alert("错误，请看提示")
				}
			});
			$("#groupChName").formValidator({
				onShow : "请输入组名",
				onFocus : "至少1位",
				onCorrect : "符合要求"
			}).inputValidator({
				min : 1,
				max : 255,
				empty : {
					leftEmpty : false,
					rightEmpty : false,
					emptyError : "不能有空符号"
				},
				onError : "1~255位"
			});
			$("#groupShortName").formValidator({
				onShow : "请输入组简称",
				onFocus : "至少1位",
				onCorrect : "符合要求"
			}).inputValidator({
				min : 1,
				max : 30,
				empty : {
					leftEmpty : false,
					rightEmpty : false,
					emptyError : "不能有空符号"
				},
				onError : "1~30位"
			});
			$("#fax").formValidator({
				empty : true,
				onShow : "请输入传真",
				onFocus : "格式例如：0755-81234567或81234567"
			}).regexValidator(
					{
						regExp : "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",
						onError : "格式不正确"
					});
			$("#groupEnName").formValidator({
				onShow : "请输入组名称（英文）",
				onFocus : "至少1位",
				onCorrect : "符合要求"
			}).inputValidator({
				min : 1,
				max : 100,
				empty : {
					leftEmpty : false,
					rightEmpty : false,
					emptyError : "不能有空符号"
				},
				onError : "1~100位"
			});

			$("#orgCode").formValidator({
				onFocus : "请输入组织机构代码,例如12345678-9",
				onCorrect : "符合要求"
			}).regexValidator({
				regExp : "^[a-zA-Z0-9]{8}-[a-zA-Z0-9]$",
				onError : "请输入正确的组织机构代码"
			});
			$("#groupAddress").formValidator({
				onShow : "请输入地址",
			}).inputValidator({
				max : 255,
				onError : "最长255位"
			});
			$("#zipCode").formValidator({
				onShow : "请输入邮编",
			}).inputValidator({
				max : 10,
				onError : "最长10位"
			});
			$("#contactNumber")
					.formValidator({
						empty : true,
						onShow : "请输入联系电话",
						onFocus : "格式例如：0755-81234567或81234567"
					}).regexValidator(
							{
								regExp : "^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",
								onError : "格式不正确"
							});
			$("#groupBusinessSphere").formValidator({
				onShow : "请输入业务范围",
			}).inputValidator({
				max : 255,
				onError : "最长255位"
			});

		})
	</script>


</body>
</html>
