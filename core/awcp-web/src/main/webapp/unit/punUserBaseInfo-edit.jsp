<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
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
<title>用户详细信息编辑</title>

<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->
<link rel="stylesheet" href="${basePath}resources/styles/content/uploader.css">
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<style type="text/css">
	.radio-inline, .checkbox-inline {
	    display: inline-block;
	    min-width: 23%;
	    padding-left: 20px;
	    margin-bottom: 0;
	    font-weight: 400;
	    vertical-align: middle;
	    cursor: pointer;
	}
	.radiodiv .checkbox-inline:FIRST-CHILD{
		margin-left: 10px;
	}
</style>
</head>
<body id="main">

		<div class="container-fluid">

			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
					<li><i class="icon-location-arrow icon-muted"></i></li>
					<li><a href="<%=basePath%>unit/punUserBaseInfoList.do">用户管理</a></li>
					<li class="active">用户详细信息编辑</li>
				</ul>
			</div>
			
			<div class="row" id="dataform">

			<form class="form-horizontal" id="userForm"
				action="<%=basePath%>unit/punUserBaseInfoSave.do" method="post" enctype="multipart/form-data">

				<div class="C_addForm">
					<legend class=" text-center">
						用户详细信息编辑
						<c:if test="${result!=null&&''!=result}">
			          		<span style="color:red">(${result})</span>
			          	</c:if>
					</legend>
					<input type="hidden" id="updatePassword" name="updatePassword" value="no"/>
					<div class="form-group">       	
			            <label class="col-md-1 control-label required">用户名</label>
			            <div class="col-md-4">
			               <input type="hidden" name="userId" value="${vo.userId}"/>
			               <input type="hidden" name="userName" value="${vo.userName}"/>
			               <input type="hidden" name="groupId" value="${vo.groupId}"/>
			               <input type="hidden" name="orgCode" value="${vo.orgCode}"/>
			               <input name="userIdCardNumber" class="form-control" id="userIdCardNumber" type="text" placeholder="" value="${vo.userIdCardNumber}">		              
			            </div>
			            <label class="col-md-2 control-label required">姓名：</label>
			            <div class="col-md-4">
			                <input name="name" class="form-control" id="name" type="text" placeholder="" value="${vo.name}">
			            </div>
			          </div>
			          <div class="form-group">
						<label class="col-md-1 control-label required">密码：</label>
						<div class="col-md-4">
							<input name="userPwd" class="form-control password_userPwd" id="userPwd"
								type="password" placeholder="" value="${vo.userPwd}">
							<div class="col-md-1"><label class="checkbox-inline text-ellipsis"> <input id="showPassword" name="showPassword" type="checkbox"> 显示密码</label></div>
						</div>

						<label class="col-md-2 control-label required">重复密码：</label>
						<div class="col-md-4">
							<input name="password2" class="form-control password_userPwd" id="password2"
								type="password" placeholder="" value="${vo.userPwd}">
							<div class="col-md-1"><label class="checkbox-inline text-ellipsis"> <input id="changePassword" name="changePassword" type="checkbox"> 修改密码</label></div>
						</div>
					  </div>
					
					 <div class="form-group">       	
			            <label class="col-md-1 control-label">户籍地：</label>
			            <div class="col-md-4">
			                <input name="userHouseholdRegist" class="form-control" id="userHouseholdRegist" type="text" placeholder="" value="${vo.userHouseholdRegist}">
			            </div>
			            
			            <label class="col-md-2 control-label">居住地：</label>
			            <div class="col-md-4">
			                <input name="userDomicile" class="form-control" id="userDomicile" type="text" placeholder="" value="${vo.userDomicile}">
			            </div> 
					 </div>
					 <div class="form-group">       	
			            <label class="col-md-1 control-label">办公电话：</label>
			            <div class="col-md-4">
			                <input name="userOfficePhone" class="form-control" id="userOfficePhone" type="text" placeholder="" value="${vo.userOfficePhone}">
			            </div>
			            <label class="col-md-2 control-label">移动电话：</label>
			            <div class="col-md-4">
			                <input name="mobile" class="form-control" id="mobile" type="text" placeholder="" value="${vo.mobile}">
			            </div> 
					 </div>
					 <div class="form-group">       	
			            <label class="col-md-1 control-label">传真：</label>
			            <div class="col-md-4">
			                <input name="userFax" class="form-control" id="userFax" type="text" placeholder="" value="${vo.userFax}">
			            </div>
			            <label class="col-md-2 control-label">邮箱：</label>
			            <div class="col-md-4">
			                <input name="userEmail" class="form-control" id="userEmail" type="text" placeholder="" value="${vo.userEmail}">
			            </div> 
					 </div>
					 <div class="form-group">       	
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
			            
					 </div>
					  <div class="form-group">       	
			            <label class="col-md-1 control-label">签名：</label>
			            <div class="col-md-4">
				                <div class="uploadPreview" style="text-align: center;">
										<input type="hidden" class="photo" name="signatureImg" value="${vo.signatureImg}"/>
								      	<div class="photo-con">
								      	    <img id="signatureImage_Img" style="width: 30px; height: 30px; margin-left: auto; margin-right: auto;" src="<%=basePath%>common/file/showPicture.do?id=${vo.signatureImg}" alt="点击选择图片"/>
								      	</div>
								      	<input type="file" id="file_signatureImage" name="file_signatureImage"/>
								<div class="photo-btn"><a class="btn delete" href="javascript:;">删除</a><span class="msg text-danger"></span></div>
							</div>
			            </div>
			             <label class="col-md-2 control-label">(0禁用，1正常，2审核)用户状态：</label>
			            <div class="col-md-4">
			                <input name="userStatus" class="form-control" id="userStatus" type="text" placeholder="1" value="${vo.userStatus}">
			            </div> 
					 </div>
					 <div class="form-group">
						 <label class="col-md-1 col-sm-1 control-label required">系统角色：</label>
						 <div class="col-md-11 col-sm-11"></div>
						 <div class="col-md-1 col-sm-1"></div>
						 <div class="col-md-11 col-sm-11 radiodiv">
						 	<c:forEach items="${roleVos}" var="vo">
								<c:choose>
									<c:when test="${vo.roleId==selectedRole||(fn:contains(selectedRole,vo.roleId)&&vo.roleId!=1)}">
										<label class="checkbox-inline"> <input name="roleList"
											type="checkbox" value="${vo.roleId}" checked="checked">
											${vo.roleName}
										</label>
									</c:when>
									<c:otherwise>
										<label class="checkbox-inline"> <input name="roleList"
											type="checkbox" value="${vo.roleId}"> ${vo.roleName}
										</label>
									</c:otherwise>
								</c:choose>
					 		</c:forEach>
						 </div>
					 </div>
					  <div class="form-group">
						 <label class="col-md-1  col-sm-1 control-label required">用户组织：</label>
						 <div class="col-md-11 col-sm-11"></div>
						 <div class="col-md-1 col-sm-1"></div>
						 <div class="col-md-11 col-sm-11 radiodiv">
						 	<input type="hidden" value="${selectedGroup}" id="positionGroupId" name="positionGroupId"/>
						 	<div id="groups" class="ztree">
						 		
						 	</div>
						 </div>
					 </div>
					  <div class="form-group">
						 <label class="col-md-1 col-sm-1 control-label required">用户职务：</label>
						 <div class="col-md-11 col-sm-11"></div>
						 <div class="col-md-1 col-sm-1"></div>
						 <div class="col-md-11 col-sm-11 radiodiv">
						 	<c:forEach items="${posiVos}" var="vo">
								<c:choose>
									<c:when test="${selectedPosition==vo.positionId}">
										<label class="checkbox-inline"> <input name="positionId"
											type="radio" value="${vo.positionId}" checked="checked">
											${vo.name}
										</label>
									</c:when>
									<c:otherwise>
										<label class="checkbox-inline"> <input name="positionId"
											type="radio" value="${vo.positionId}"> ${vo.name}
										</label>
									</c:otherwise>
								</c:choose>
						 	</c:forEach>
					 	</div>
					 </div>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
						<a href="<%=basePath%>unit/punUserBaseInfoList.do" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		        </div>
			</form>

		</div>

		</div>

		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/ajaxfileupload.js"></script>
		<script src="<%=basePath%>resources/scripts/uploadPreview.js"></script>
		<script src="<%=basePath%>resources/scripts/uploader.js"></script>
		<script src="<%=basePath%>venson/js/tree.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<%-- <script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script> --%>
		<script type="text/javascript">
			var basePath = '<%=basePath%>';
			//var key={"pid":"parentGroupId","id":"groupId","name":"groupChName"};
			//var root=new Tree(529614);
			var zTreeObj;
			var groups=$.get(basePath+"unit/listGroup.do",function(data){
				var setting = {
						view: {
							selectedMulti: false,
							showIcon: false
						},
						check: {
							enable: true,
							chkStyle: "radio",
							chkboxType: { "Y": "", "N": "" },
							radioType: "all"
						},
						data: {
							key: {
								name: "groupChName"
							},
							simpleData: {
								enable: true,
								idKey: "groupId",
								pIdKey: "parentGroupId",
								rootPId: 0
							}
						},
						callback:{
							onCheck: zTreeOnCheck,
						}
					}
			
				zTreeObj = $.fn.zTree.init($("#groups"), setting, data);
				var groupId = $("#positionGroupId").val();
				var node = zTreeObj.getNodeByParam("groupId", groupId, null);
				//选中节点并展开
				if(node){
					zTreeObj.checkNode(node,true,true);
					if(!zTreeObj.expandNode(node)){
						zTreeObj.expandAll(true);
					}
				}
				//root.findChild(data,key);
				//root.eachChild(0,function(l,c){
				//	var html='<div>'
				//	if(l!=0){
				//		for(var i=0;i<l;i++){
				//			html+="　"
				//		}
				//	}
					
				//	html+='<input name="positionGroupId" type="radio" value="'+c.id+'"> '+c.name+' </div>';
				//	$("#groups").append(html);
					
					
				//});
				$(":radio[value='${selectedGroup}']").prop("checked",true);
			},"json");
			
		function zTreeOnCheck(event, treeId, treeNode) {
			$("#positionGroupId").val(treeNode.checked?treeNode.groupId:"1")
		}

	
		$(function(){
			loadUploadPreview("signatureImage_Img","file_signatureImage",20,20);
   			$.formValidator.initConfig({formID:"userForm",debug:false,onSuccess:function(){
   				$("#userForm").submit();
    		},onError:function(){alert("错误，请看提示")}});
				$("#userIdCardNumber").formValidator({onShow:"请输入用户名",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填",max:50,onError:"最长50位"});
				$("#userPwd").formValidator({onShow:"请输入密码,默认密码是791013",onFocus:"至少1位,默认密码是791013",onCorrect:"密码合法"}).inputValidator({min:1,max:16,empty:{leftEmpty:false,rightEmpty:false,emptyError:"密码两边不能有空符号"},onError:"密码不能为空,请确认"});
				$("#password2").formValidator({onShow:"输再次输入密码",onFocus:"至少1位,默认密码是791013",onCorrect:"密码一致"}).inputValidator({min:1,max:16,empty:{leftEmpty:false,rightEmpty:false,emptyError:"重复密码两边不能有空符号"},onError:"重复密码不能为空,请确认"}).compareValidator({desID:"userPwd",operateor:"=",onError:"2次密码不一致,请确认"});
				$("#name").formValidator({onShow:"请输入姓名",}).inputValidator({min:2,max:30,onError:"长度在2～30位之间"});
				$("#userBirthPlace").formValidator({onShow:"请输入籍贯",}).inputValidator({max:255,onError:"最长255位"});
				$("#userHouseholdRegist").formValidator({onShow:"请输入户籍地",}).inputValidator({max:255,onError:"最长255位"});
				$("#userDomicile").formValidator({onShow:"请输入居住地",}).inputValidator({max:255,onError:"最长255位"});
				$("#userOfficePhone").formValidator({empty:true,onShow:"请输入联系电话",onFocus:"格式例如：0755-81234567或81234567"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
				$("#mobile").formValidator({empty:true,onShow:"请输入手机号码"}).inputValidator({min:11,max:11,onError:"手机号码必须是11位的,请确认"});
				$("#userFax").formValidator({empty:true,onShow:"请输入传真号码",onFocus:"格式例如：0755-81234567或81234567"}).regexValidator({regExp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onError:"格式不正确"});
				$("#userEmail").formValidator({empty:true,onShow:"请输入邮箱"}).inputValidator({min:6,max:128,onError:"邮箱长度非法,请确认"}).regexValidator({regExp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onError:"邮箱格式不正确"});
				$("#employeeId").formValidator({empty:true,onShow:"请输入工号"}).inputValidator({max:30,onError:"最长30位"});
				$("#userTitle").formValidator({empty:true,onShow:"请输入头衔"}).inputValidator({max:30,onError:"最长30位"});
				$("#userDossierNumber").formValidator({empty:true,onShow:"请输入档案账号"}).inputValidator({max:50,onError:"最长50位"});
				$("#userOfficeNum").formValidator({empty:true,onShow:"请输入办公室门牌号"}).inputValidator({max:20,onError:"最长20位"});
				$(":radio[name='roleList'],:radio[name='positionGroupId'],:radio[name='positionId']").formValidator({onCorrect:"符合要求"}).inputValidator({min:1,max:1,onError:"请选择一个"});
				
		});
		$("input[type='password']").attr("readonly", "readonly");	
		$("input[type='password']").each(function(i,e){
			if(!e.value){
				e.value=791013;
			}
		})
		$("#changePassword").click(function(){
						
						if($(this).is(':checked')) {
							$(".password_userPwd").removeAttr("readonly");
							$("#updatePassword").val("yes");
						}
						else{
							$(".password_userPwd").attr("readonly", "readonly");
							$("#updatePassword").val("no");
						}
		});
		$("#showPassword").click(function(){
						if($(this).is(':checked')) {
							$(".password_userPwd").attr("type","text");
						}
						else{
							$(".password_userPwd").attr("type","password");
						}
		});
	</script>


</body>
</html>
