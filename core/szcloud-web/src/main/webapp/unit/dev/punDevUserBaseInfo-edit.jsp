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
					<li class="active">组织机构与权限</li>
					<li><a href="<%=basePath%>dev/punDevUserBaseInfoList.do">开发人员管理</a></li>
					<li class="active">开发人员信息编辑</li>
				</ul>
			</div>
			
			<div class="row" id="dataform">
			<legend class="text-center">
				<c:if test="${result!=null&&''!=result}">
	          		<span style="color:red">(${result})</span>
	          	</c:if>
			</legend>

			<form class="form-horizontal" id="userForm" action="<%=basePath%>dev/punDevUserBaseInfoSave.do" method="post">
					<div class="form-group">       	
			            <label class="col-md-1 control-label required">用户名</label>
			            <div class="col-md-4">
			               <input type="hidden" id="id" name="id" value="${vo.id}"/>
			               <input type="hidden" id="updatePassword" name="updatePassword" value="no"/>
			               <%-- <input type="hidden" name="orgCode" value="${vo.code}"/> --%>
			               <input type="hidden" name="groupId" value="${vo.groupId}"/>
			               <input name="account" class="form-control" id="account" type="text" placeholder="" value="${vo.account}">		              
			            </div>
			            <label class="col-md-2 control-label required">身份证号：</label>
			            <div class="col-md-4">
			                <input name="identityNumber" class="form-control" id="identityNumber" type="text" placeholder="" value="${vo.identityNumber}">
			            </div> 
			          </div>			          
					<div class="form-group">
						<label class="col-md-1 control-label required">密码：</label>
						<div class="col-md-4">
							<input name="password" class="form-control" id="password"
								type="password" placeholder="" value="123456">
						</div>

						<label class="col-md-2 control-label required">重复密码：</label>
						<div class="col-md-4">
							<input name="password2" class="form-control" id="password2"
								type="password" placeholder="" value="123456">
						</div>
						<label class="checkbox-inline text-ellipsis"> <input id="changePassword" name="changePassword" type="checkbox"> 修改密码</label>
					</div>
					 <div class="form-group">       	
			            <label class="col-md-1 control-label required">姓名：</label>
			            <div class="col-md-4">
			                <input name="name" class="form-control" id="name" type="text" placeholder="" value="${vo.name}">
			            </div>
			            
			           <label class="col-md-2 control-label">邮箱：</label>
			            <div class="col-md-4">
			                <input name="email" class="form-control" id="email" type="text" placeholder="" value="${vo.email}">
			            </div> 
					 </div>
					
					 <div class="form-group">       	
			            <label class="col-md-1 control-label">办公电话：</label>
			            <div class="col-md-4">
			                <input name="telephone" class="form-control" id="telephone" type="text" placeholder="" value="${vo.telephone}">
			            </div>
			            <label class="col-md-2 control-label">移动电话：</label>
			            <div class="col-md-4">
			                <input name="mobile" class="form-control" id="mobile" type="text" placeholder="" value="${vo.mobile}">
			            </div> 
					 </div>
					<%--  <div class="form-group">       	
			            <label class="col-md-1 control-label">工号：</label>
			            <div class="col-md-4">
			                <input name="employeeId" class="form-control" id="employeeId" type="text" placeholder="" value="${vo.employeeId}">
			            </div>
			            <label class="col-md-2 control-label">头衔：</label>
			            <div class="col-md-4">
			                <input name="userTitle" class="form-control" id="userTitle" type="text" placeholder="" value="${vo.userTitle}">
			            </div> 
					 </div>
					  <div class="form-group">       	
			            <label class="col-md-1 control-label">档案帐号：</label>
			            <div class="col-md-4">
			                <input name="userDossierNumber" class="form-control" id="userDossierNumber" type="text" placeholder="" value="${vo.userDossierNumber}">
			            </div>
			            <label class="col-md-2 control-label">办公室门牌号：</label>
			            <div class="col-md-4">
			                <input name="userOfficeNum" class="form-control" id="userOfficeNum" type="text" placeholder="" value="${vo.userOfficeNum}">
			            </div> 
					 </div> --%>
					<div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-1 col-md-10">
			              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
			              	<a href="<%=basePath%>dev/punDevUserBaseInfoList.do" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
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
				$("#account").formValidator({onShow:"请输入用户名",onFocus:"至少1位,至多30个英文字符",onCorrect:"符合要求"}).inputValidator({min:1,max:30,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"请输入正确的用户名"});
				$("#name").formValidator({onShow:"请输入姓名",onFocus:"至多10个汉字或20个英文字符"}).inputValidator({max:20,onError:"至多10个汉字或20个英文字符"});
				$("#telephone").formValidator({empty:true,onShow:"请输入联系电话",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
				$("#mobile").formValidator({empty:true,onShow:"请输入手机号码"}).inputValidator({min:11,max:11,onError:"手机号码必须是11位的,请确认"}).regexValidator({regExp:"mobile",dataType:"enum",onError:"你输入的手机号码格式不正确"});
				$("#email").formValidator({empty:true,onShow:"请输入邮箱"}).inputValidator({min:6,max:128,onError:"邮箱长度非法,请确认"}).regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:"邮箱格式不正确"});
				$("#password").formValidator({onShow:"请输入密码,默认密码是123456",onFocus:"至少1位,默认密码是123456",onCorrect:"密码合法"}).inputValidator({min:1,max:16,empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},onError:"密码不能为空,请确认"});
				$("#password2").formValidator({onShow:"输再次输入密码",onFocus:"至少1位,默认密码是123456",onCorrect:"密码一致"}).inputValidator({min:1,max:16,empty:{leftEmpty:false,rightEmpty:false,emptyError:"重复密码两边不能有空符号"},onError:"重复密码不能为空,请确认"}).compareValidator({desID:"password",operateor:"=",onError:"2次密码不一致,请确认"});
				
				$("#identityNumber").formValidator({onShow:"请输入身份证号",onFocus:"为15或18位",onCorrect:"符合要求"}).regexValidator({regExp:"^(\\d{18,18}|\\d{15,15}|\\d{17,17}x)$",onError:"请输入正确的身份证号码"});
				var userId = $("#id").val();
				$("input[type='password']").attr("readonly", "readonly");				
				$("#changePassword").click(function(){
					if($(this).is(':checked')) {
						$("input[type='password']").removeAttr("readonly");
						$("#updatePassword").val("yes");
					}
					else{
						$("input[type='password']").attr("readonly", "readonly");
						$("#updatePassword").val("no");
					}
				});
			})
		</script>


</body>
</html>
