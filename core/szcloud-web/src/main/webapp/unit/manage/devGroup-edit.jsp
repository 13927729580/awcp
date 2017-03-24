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
					<li  class="active">组织机构与权限</li>
					<li><a href= "<%=basePath%>manage/devGroupList.do?currentPage=0">开发组管理</a>
					<li class="active">组信息编辑</li>
				</ul>
			</div>
			
			<div class="row" id="dataform">

			<form class="form-horizontal" id="groupForm"
				action="<%=basePath%>manage/punDevGroupSave.do" method="post">

				<div class="C_addForm">
					<legend class=" text-center">
						组信息编辑
						<c:if test="${result!=null&&''!=result}">
							<span style="color: red">(${result})</span>
						</c:if>
					</legend>
					<input type="hidden" name="id" value="${vo.id}" />
					<input type="hidden" name="type" id="type" value="0"/><!-- 根组织 -->
					<input type="hidden" name="parentId" id="parentId" value="0"/>
					<div class="form-group">
						<label class="col-md-1 control-label required">组名：</label>
						<div class="col-md-4">
							<input name="nameCn" class="form-control" id="nameCn"
								type="text" placeholder="" value="${vo.nameCn}">
						</div>
						<label class="col-md-2 control-label required">组简称：</label>
						<div class="col-md-4">
							<input name="nameShort" class="form-control"
								id="nameShort" type="text" placeholder=""
								value="${vo.nameShort}">
						</div>
					</div>
					<div class="form-group">					
						<label class="col-md-1 control-label required"> 组织机构代码：</label>
						<div class="col-md-4">
							<input name="code" class="form-control" id="code"
								type="text" placeholder="" value="${vo.code}">
						</div>
						<label class="col-md-2 control-label required">组名（英文）：</label>
						<div class="col-md-4">
							<input name="nameEn" class="form-control" id="nameEn"
								type="text" placeholder="" value="${vo.nameEn}">
						</div>
					</div>
				</div>
				<c:if test="${empty vo.id}">
					<div class="C_addForm">
						<legend class=" text-center">
							管理员信息编辑
							<c:if test="${result!=null&&''!=result}">
								<span style="color: red">(${result})</span>
							</c:if>
						</legend>
						<div class="form-group">
							<label class="col-md-1 control-label required">用户名</label>
							<div class="col-md-4">
								<input type="hidden" name="userId" value="${uo.id}" /> <input
									type="hidden" name="groupId" value="${uo.groupId}" /> <input
									name="account" class="form-control" id="account" type="text"
									placeholder="" value="${uo.account}">
							</div>
							<label class="col-md-2 control-label required">身份证号：</label>
							<div class="col-md-4">
								<input name="identityNumber" class="form-control"
									id="identityNumber" type="text" placeholder=""
									value="${uo.identityNumber}">
							</div>

						</div>
						<div class="form-group">
							<label class="col-md-1 control-label required">密码：</label>
							<div class="col-md-4">
								<input name="password" class="form-control" id="password"
									type="password" placeholder="" value="">
							</div>

							<label class="col-md-2 control-label required">重复密码：</label>
							<div class="col-md-4">
								<input name="password2" class="form-control" id="password2"
									type="password" placeholder="" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label required">姓名：</label>
							<div class="col-md-4">
								<input name="name" class="form-control" id="name" type="text"
									placeholder="" value="${uo.name}">
							</div>							
							<label class="col-md-2 control-label">邮箱：</label>
							<div class="col-md-4">
								<input name="email" class="form-control" id="email"
									type="text" placeholder="" value="${uo.email}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">办公电话：</label>
							<div class="col-md-4">
								<input name="telephone" class="form-control"
									id="telephone" type="text" placeholder=""
									value="${uo.telephone}">
							</div>
							<label class="col-md-2 control-label">移动电话：</label>
							<div class="col-md-4">
								<input name="mobile" class="form-control" id="mobile"
									type="text" placeholder="" value="${uo.mobile}">
							</div>
						</div>
					</div>
				</c:if>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-1 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
					    <a href="<%=basePath%>manage/devGroupList.do?currentPage=0" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
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
		$("#parent").formValidator({onFocus:"请输入上级组织ID",onCorrect:"符合要求"}).inputValidator({max:20,onError:"最长20位"});
		$("#nameCN").formValidator({onFocus:"请输入组织中文名称",onCorrect:"符合要求"}).inputValidator({min:1,max:30,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
		$("#nameShort").formValidator({onFocus:"请输入组织简称",onCorrect:"符合要求"}).inputValidator({max:30,onError:"最长30位"});
		//$("#fax").formValidator({empty:true,onFocus:"请输入传真号码",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
		$("#nameEn").formValidator({onFocus:"请输入组织英文名称",onCorrect:"符合要求"}).inputValidator({max:30,onError:"最长100位"});
		$("#code").formValidator({onFocus:"请输入组织机构代码,例如12345678-9",onCorrect:"符合要求"}).regexValidator({regExp:"^[a-zA-Z0-9]{8}-[a-zA-Z0-9]$",onError:"请输入正确的组织机构代码"});
		//$("#groupAddress").formValidator({onFocus:"请输入地址",onCorrect:"符合要求"}).inputValidator({max:255,onError:"最长255位"});
		//$("#zipCode").formValidator({onFocus:"请输入邮编",onCorrect:"符合要求"}).inputValidator({max:10,onError:"最长10位"});
		//$("#contactNumber").formValidator({empty:true,onShow:"请输入联系电话",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
		//$("#groupBusinessSphere").formValidator({onFocus:"请输入业务范围",onCorrect:"符合要求"}).inputValidator({max:255,onError:"最长255位"});
		$("#account").formValidator({onShow:"请输入用户名",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
		$("#password").formValidator({onShow:"请输入密码",onFocus:"至少1位",onCorrect:"密码合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},onError:"密码不能为空,请确认"});
		$("#password2").formValidator({onShow:"输再次输入密码",onFocus:"至少1位",onCorrect:"密码一致"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"重复密码两边不能有空符号"},onError:"重复密码不能为空,请确认"}).compareValidator({desID:"password",operateor:"=",onError:"2次密码不一致,请确认"});
		$("#name").formValidator({onShow:"请输入姓名",}).inputValidator({max:255,onError:"最长255位"});
		//$("#userBirthPlace").formValidator({onShow:"请输入籍贯",}).inputValidator({max:255,onError:"最长255位"});
		//$("#userHouseholdRegist").formValidator({onShow:"请输入户籍地",}).inputValidator({max:255,onError:"最长255位"});
		//$("#userDomicile").formValidator({onShow:"请输入居住地",}).inputValidator({max:255,onError:"最长255位"});
		$("#telephone").formValidator({empty:true,onShow:"请输入联系电话",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
		$("#mobile").formValidator({empty:true,onShow:"请输入手机号码",onFocus:"格式例如：13800001111"}).inputValidator({min:11,max:11,onError:"手机号码必须是11位的,请确认"}).regexValidator({regExp:"mobile",dataType:"enum",onError:"你输入的手机号码格式不正确"});
		//$("#userFax").formValidator({empty:true,onShow:"请输入传真号码",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
		$("#email").formValidator({empty:true,onShow:"请输入邮箱",onFocus:"格式例如：hr@nsccsz.gov.cn"}).inputValidator({min:6,max:128,onError:"邮箱长度非法,请确认"}).regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:"邮箱格式不正确"});
		//$("#employeeId").formValidator({empty:true,onShow:"请输入工号"}).inputValidator({max:30,onError:"最长30位"});
		//$("#userTitle").formValidator({empty:true,onShow:"请输入头衔"}).inputValidator({max:30,onError:"最长30位"});
		//$("#userDossierNumber").formValidator({empty:true,onShow:"请输入档案账号"}).inputValidator({max:50,onError:"最长50位"});
		//$("#userOfficeNum").formValidator({empty:true,onShow:"请输入办公室门牌号"}).inputValidator({max:20,onError:"最长20位"});
		$("#identityNumber").formValidator({onShow:"请输入身份证号",onFocus:"为15或18位",onCorrect:"符合要求"}).regexValidator({regExp:"^(\\d{18,18}|\\d{15,15}|\\d{17,17}x)$",onError:"请输入正确的身份证号码"});
		})
	</script>


</body>
</html>
