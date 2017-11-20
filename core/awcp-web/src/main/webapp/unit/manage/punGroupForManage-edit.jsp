<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>   
 <%@page isELIgnored="false"%> 
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8">
		<title>组信息编辑</title>

		<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->
		
		
	</head>
	<body id="main"	>
	
		<div class="container-fluid">
				
				<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
					<li><i class="icon-location-arrow icon-muted"></i></li>
					<li class="active">组织机构与权限</li>
					<li><a href="<%=basePath%>manage/punManageAppGroupList.do?currentPage=0">应用组织管理</a></li>
					<li class="active">组织信息编辑</li>
				</ul>
			</div>
			
			<div class="row" id="dataform">
					
					<form class="form-horizontal" id="groupForm" action="<%=basePath%>manage/punManageAppGroupSave.do" method="post">
			    <!-- <div class="C_btnGroup">
					<div class="C_btns">
	                    <button class="btn btn-info" type="submit">保存</button>
                   </div>
                </div> -->
                 
                <div class="C_addForm">
			          <legend class=" text-center">
			          		组织信息编辑
			          	<c:if test="${result!=null&&''!=result}">
			          		<span style="color:red">(${result})</span>
			          	</c:if>
			          </legend>
				
					<input type="hidden" name="groupId" value="${vo.groupId}" />
					<input type="hidden" name="groupType" id="groupType" value="1"/>
					<input type="hidden" name="pid" id="pid" value="0"/>
					
				<div class="form-group">   
					 	<label class="col-md-1 control-label required">组名：</label>
			            <div class="col-md-4">
			                <input name="groupChName" class="form-control" id="groupChName" type="text" placeholder="" value="${vo.groupChName}">
			            </div> 
						<label class="col-md-2 control-label">组简称：</label>
			            <div class="col-md-4">
			                <input name="groupShortName" class="form-control" id="groupShortName" type="text" placeholder="" value="${vo.groupShortName}">
			            </div> 
				 </div>
					  <div class="form-group">  
					  	<label class="col-md-1 control-label required">	组织机构代码：</label>
			            <div class="col-md-4">
			                <input name="orgCode" class="form-control" id="orgCode" type="text" placeholder="" value="${vo.orgCode}">
			            </div>  
					 	
			            <label class="col-md-2 control-label">组名（英文）：</label>
			            <div class="col-md-4">
			                <input name="groupEnName" class="form-control" id="groupEnName" type="text" placeholder="" value="${vo.groupEnName}">
			            </div>
					 </div>
					 <div class="form-group">   
					 	<label class="col-md-1 control-label">传真：</label>
			            <div class="col-md-4">
			                <input name="fax" class="form-control" id="fax" type="text" placeholder="" value="${vo.fax}">
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
			</div>
			<c:if test="${empty vo.groupId}">
			    <div class="C_addForm">
			          <legend class=" text-center">
			          		管理员信息编辑
			          	<c:if test="${result!=null&&''!=result}">
			          		<span style="color:red">(${result})</span>
			          	</c:if>
			          </legend>
			          <div class="form-group">       	
			            <label class="col-md-1 control-label required">用户名</label>
			            <div class="col-md-4">
			               <input type="hidden" name="userId" value="${uo.userId}"/>
						   <input type="hidden" name="groupId" value="${uo.groupId}"/>
			               <input name="userName" class="form-control" id="userName" type="text" placeholder="" value="${uo.userName}">		              
			            </div>
			             <label class="col-md-2 control-label required">身份证号：</label>
			            <div class="col-md-4">
			                <input name="userIdCardNumber" class="form-control" id="userIdCardNumber" type="text" placeholder="" value="${uo.userIdCardNumber}">
			            </div> 
			            
					 </div>
					 <div class="form-group">   
					 	<label class="col-md-1 control-label required">密码：</label>
			            <div class="col-md-4">
			                <input name="userPwd" class="form-control" id="userPwd" type="password" placeholder="" value="">
			            </div> 
					 	    	
			            <label class="col-md-2 control-label required">重复密码：</label>
			            <div class="col-md-4">
			                <input name="password2" class="form-control" id="password2" type="password" placeholder="" value="">
			            </div>
					 </div>
					 <div class="form-group">       	
			            <label class="col-md-1 control-label">姓名：</label>
			            <div class="col-md-4">
			                <input name="name" class="form-control" id="name" type="text" placeholder="" value="${uo.name}">
			            </div>
			            
			            <label class="col-md-2 control-label">籍贯：</label>
			            <div class="col-md-4">
			                <input name="userBirthPlace" class="form-control" id="userBirthPlace" type="text" placeholder="" value="${uo.userBirthPlace}">
			            </div> 
					 </div>
					 <div class="form-group">       	
			            <label class="col-md-1 control-label">户籍地：</label>
			            <div class="col-md-4">
			                <input name="userHouseholdRegist" class="form-control" id="userHouseholdRegist" type="text" placeholder="" value="${uo.name}">
			            </div>
			            
			            <label class="col-md-2 control-label">居住地：</label>
			            <div class="col-md-4">
			                <input name="userDomicile" class="form-control" id="userDomicile" type="text" placeholder="" value="${uo.userDomicile}">
			            </div> 
					 </div>
					  <div class="form-group">       	
			            <label class="col-md-1 control-label">办公电话：</label>
			            <div class="col-md-4">
			                <input name="userOfficePhone" class="form-control" id="userOfficePhone" type="text" placeholder="" value="${uo.userOfficePhone}">
			            </div>
			            <label class="col-md-2 control-label">移动电话：</label>
			            <div class="col-md-4">
			                <input name="mobile" class="form-control" id="mobile" type="text" placeholder="" value="${uo.mobile}">
			            </div> 
					 </div>
					  <div class="form-group">       	
			            <label class="col-md-1 control-label">传真：</label>
			            <div class="col-md-4">
			                <input name="userFax" class="form-control" id="userFax" type="text" placeholder="" value="${uo.userFax}">
			            </div>
			            <label class="col-md-2 control-label">邮箱：</label>
			            <div class="col-md-4">
			                <input name="userEmail" class="form-control" id="userEmail" type="text" placeholder="" value="${uo.userEmail}">
			            </div> 
					 </div>
					 <div class="form-group">       	
			            <label class="col-md-1 control-label">工号：</label>
			            <div class="col-md-4">
			                <input name="employeeId" class="form-control" id="employeeId" type="text" placeholder="" value="${uo.employeeId}">
			            </div>
			            <label class="col-md-2 control-label">头衔：</label>
			            <div class="col-md-4">
			                <input name="userTitle" class="form-control" id="userTitle" type="text" placeholder="" value="${uo.userTitle}">
			            </div> 
					 </div>
					  <div class="form-group">       	
			            <label class="col-md-1 control-label">档案帐号：</label>
			            <div class="col-md-4">
			                <input name="userDossierNumber" class="form-control" id="userDossierNumber" type="text" placeholder="" value="${uo.userDossierNumber}">
			            </div>
			            <label class="col-md-2 control-label">办公室门牌号：</label>
			            <div class="col-md-4">
			                <input name="userOfficeNum" class="form-control" id="userOfficeNum" type="text" placeholder="" value="${uo.userOfficeNum}">
			            </div> 
					 </div>
					 </div>
			</c:if>
				<div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-1 col-md-10">
			              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
							<a href="<%=basePath%>manage/punManageAppGroupList.do?currentPage=0" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
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
		$("#parentGroupId").formValidator({onFocus:"请输入上级组织ID",onCorrect:"符合要求"}).inputValidator({max:20,onError:"最长20位"});
		$("#groupChName").formValidator({onFocus:"请输入组织中文名称",onCorrect:"符合要求"}).inputValidator({min:1,max:30,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
		$("#groupShortName").formValidator({onFocus:"请输入组织简称",onCorrect:"符合要求"}).inputValidator({max:30,onError:"最长30位"});
		$("#fax").formValidator({empty:true,onFocus:"请输入传真号码",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
		$("#groupEnName").formValidator({onFocus:"请输入组织英文名称",onCorrect:"符合要求"}).inputValidator({max:30,onError:"最长100位"});
		$("#orgCode").formValidator({onFocus:"请输入组织机构代码,例如12345678-9",onCorrect:"符合要求"}).regexValidator({regExp:"^[a-zA-Z0-9]{8}-[a-zA-Z0-9]$",onError:"请输入正确的组织机构代码"});
		$("#groupAddress").formValidator({onFocus:"请输入地址",onCorrect:"符合要求"}).inputValidator({max:255,onError:"最长255位"});
		$("#zipCode").formValidator({empty:true,onFocus:"请输入邮编",onCorrect:"符合要求"}).regexValidator({ regExp: "zipcode", dataType: "enum", onError: "邮编格式不正确" });
		$("#contactNumber").formValidator({empty:true,onShow:"请输入联系电话",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
		$("#groupBusinessSphere").formValidator({onFocus:"请输入业务范围",onCorrect:"符合要求"}).inputValidator({max:255,onError:"最长255位"});
		$("#userName").formValidator({onShow:"请输入用户名",onFocus:"至少1位,至多20位，并且不能有空格",onCorrect:"符合要求"}).regexValidator({regExp:"^\\S{1,20}$",onError:"请输入正确的用户名"});
		$("#userPwd").formValidator({onShow:"请输入密码",onFocus:"至少1位",onCorrect:"密码合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},onError:"密码不能为空,请确认"});
		$("#password2").formValidator({onShow:"输再次输入密码",onFocus:"至少1位",onCorrect:"密码一致"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"重复密码两边不能有空符号"},onError:"重复密码不能为空,请确认"}).compareValidator({desID:"userPwd",operateor:"=",onError:"2次密码不一致,请确认"});
		$("#name").formValidator({onShow:"请输入姓名",}).inputValidator({max:255,onError:"最长255位"});
		$("#userBirthPlace").formValidator({onShow:"请输入籍贯",}).inputValidator({max:255,onError:"最长255位"});
		$("#userHouseholdRegist").formValidator({onShow:"请输入户籍地",}).inputValidator({max:255,onError:"最长255位"});
		$("#userDomicile").formValidator({onShow:"请输入居住地",}).inputValidator({max:255,onError:"最长255位"});
		$("#userOfficePhone").formValidator({empty:true,onShow:"请输入联系电话",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
		$("#mobile").formValidator({empty:true,onShow:"请输入手机号码"}).inputValidator({min:11,max:11,onError:"手机号码必须是11位的,请确认"}).regexValidator({regExp:"mobile",dataType:"enum",onError:"你输入的手机号码格式不正确"});
		$("#userFax").formValidator({empty:true,onShow:"请输入传真号码",onFocus:"格式例如：0755-88888888"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
		$("#userEmail").formValidator({empty:true,onShow:"请输入邮箱"}).inputValidator({min:6,max:128,onError:"邮箱长度非法,请确认"}).regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:"邮箱格式不正确"});
		$("#employeeId").formValidator({empty:true,onShow:"请输入工号"}).inputValidator({max:30,onError:"最长30位"});
		$("#userTitle").formValidator({empty:true,onShow:"请输入头衔"}).inputValidator({max:30,onError:"最长30位"});
		$("#userDossierNumber").formValidator({empty:true,onShow:"请输入档案账号"}).inputValidator({max:50,onError:"最长50位"});
		$("#userOfficeNum").formValidator({empty:true,onShow:"请输入办公室门牌号"}).inputValidator({max:20,onError:"最长20位"});
		$("#userIdCardNumber").formValidator({onShow:"请输入身份证号",onFocus:"为15或18位",onCorrect:"符合要求"}).regexValidator({regExp:"^(\\d{18,18}|\\d{15,15}|\\d{17,17}x)$",onError:"请输入正确的身份证号码"});
		})
	</script>
		
	</body>
</html>
