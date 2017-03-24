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
<title>用户详细信息编辑</title>

<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->

</head>
<body id="main" class="inner-frame" style="width:760px;">
		<div class="container">
			<div class="row">
			<form class="form-horizontal" id="userForm"
				action="<%=basePath%>dev/updateSelfInfo.do" method="post">

				<div class="C_addForm">
					<div class="form-group">       	
			            <label class="col-xs-2 control-label required">用户名</label>
			            <div class="col-xs-4">
			               <input type="hidden" name="id" value="${vo.id}"/>
			               <input type="hidden" name="groupId" value="${vo.groupId}"/>
			               <input name="account" class="form-control" id="account" type="text" placeholder="" value="${vo.account}">		              
			            </div>
			            <label class="col-xs-2 control-label required">身份证号</label>
			            <div class="col-xs-4">
			                <input name="identityNumber" class="form-control" id="identityNumber" type="text" placeholder="" value="${vo.identityNumber}">
			            </div> 
			          </div>
					 <div class="form-group">       	
			            <label class="col-xs-2 control-label required">姓名</label>
			            <div class="col-xs-4">
			                <input name="name" class="form-control" id="name" type="text" placeholder="" value="${vo.name}">
			            </div>
			            
			            <label class="col-xs-2 control-label">组织机构代码</label>
			            <div class="col-xs-4">
			                <input name="company" class="form-control" id="company" disabled="disabled" type="text" placeholder="" value="${groupVO.code}" readonly>
			            </div>
					 </div>
					 <div class="form-group">       	
			            <label class="col-xs-2 control-label">办公电话</label>
			            <div class="col-xs-4">
			                <input name="telephone" class="form-control" id="telephone" type="text" placeholder="" value="${vo.telephone}">
			            </div>
			            <label class="col-xs-2 control-label">移动电话</label>
			            <div class="col-xs-4">
			                <input name="mobile" class="form-control" id="mobile" type="text" placeholder="" value="${vo.mobile}">
			            </div> 
					 </div>
					 <div class="form-group">       	
			            <label class="col-xs-2 control-label">邮箱</label>
			            <div class="col-xs-4">
			                <input name="email" class="form-control" id="email" type="text" placeholder="" value="${vo.email}">
			            </div> 
					 </div>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-xs-10">
		              	<button  class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
		              	<c:if test="${result!=null&&''!=result}">
							<span style="color: red">(${result})</span>
						</c:if>
		            </div>
		        </div>
			</form>

		</div>

		</div>

		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script type="text/javascript">
		$(function(){
			
   			$.formValidator.initConfig({formID:"userForm",debug:false,onSuccess:function(){
			   //	$("#userForm").submit();
    		},onError:function(){alert("错误，请看提示")}});
	$("#account").formValidator({onShow:"请输入用户名",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
	$("#name").formValidator({onShow:"请输入姓名",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
	/* $("#company").formValidator({onShow:"请输入公司",}).inputValidator({max:255,onError:"最长255位"}); */
	$("#telephone").formValidator({empty:true,onShow:"请输入联系电话",onFocus:"格式例如：0755-81234567或81234567"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
	$("#mobile").formValidator({empty:true,onShow:"请输入手机号码"}).inputValidator({min:11,max:11,onError:"手机号码必须是11位的,请确认"}).regexValidator({regExp:"mobile",dataType:"enum",onError:"你输入的手机号码格式不正确"});
	$("#email").formValidator({empty:true,onShow:"请输入邮箱"}).inputValidator({min:6,max:128,onError:"邮箱长度非法,请确认"}).regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:"邮箱格式不正确"});
	$("#identityNumber").formValidator({onShow:"请输入身份证号",onFocus:"为15或18位",onCorrect:"符合要求"}).regexValidator({regExp:"^(\\d{18,18}|\\d{15,15}|\\d{17,17}x)$",onError:"请输入正确的身份证号码"});
	})
	
	//保存按钮
	$("#saveBtn").click(function(){
		if(!$.formValidator.pageIsValid('1')){
			return false;
		}
		 $.ajax( {  
             type : "POST",  
             url : "<%=basePath%>dev/updateSelfInfo.do",  
             data : $("#userForm").serialize(),  
             success : function(data) {  
                 if ((data.status == 0)) { 
                     alert(data.message);   
                 } else {  
                    alert("修改失败。"); 
                 }  
             }  
         }); 
		return false;
	});
	
	</script>


</body>
</html>
