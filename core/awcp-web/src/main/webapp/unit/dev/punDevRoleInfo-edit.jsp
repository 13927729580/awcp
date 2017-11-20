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
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>菜单编辑页面</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
 		
		
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
		          <li class="active">组织机构与权限</li>
		          <li><a href="<%=basePath%>dev/punDevRoleInfoList.do">角色管理</a></li>
		          <li class="active">角色编辑</li>
		        </ul>
			</div>
			
			<div class="row" id="dataform">
			<form class="form-horizontal" id="groupForm" action="<%=basePath%>dev/punDevRoleInfoSave.do" method="post">
				<legend>
					角色编辑
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
				</legend>
				
		        <div class="form-group">       	
		            <label class="col-md-2 control-label required">角色名：</label>
		            <div class="col-md-4">
		            	<input type="hidden" name="id" value="${vo.id}"/>
		            	<input type="hidden" name="groupId" value="${vo.groupId}"/>
		                <input name="name" class="form-control" id="name" type="text" placeholder="" value="${vo.name}">
		            </div>
		            <label class="col-md-2 control-label required">角色编码：</label>
		            <div class="col-md-4">
		                <input name="code" class="form-control" id="roleCode" type="text" placeholder="" value="${vo.code}">
		            </div>
				</div>
		        <div class="form-group">       	
		            <label class="col-md-2 control-label">角色描述：</label>
		            <div class="col-md-10">
		            	<textarea name="description" class="form-control" id="description" rows="5">${vo.description }</textarea>
		            </div>
				</div>
				
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-1 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
						<a href="<%=basePath%>dev/punDevRoleInfoList.do?currentPage=1" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		          </div>
			</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
			$(function(){
		   		$.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
		   			$("button[type='submit']").addClass("disabled");
					//$("#groupForm").submit();
		    	},onError:function(){alertMessage("请填写带*的必填项")}});
		   		$("#name").formValidator({onShow:"请输入角色名称",onFocus:"至少1字符，至多10个汉字或20个英文字符",onCorrect:"符合要求"}).inputValidator({min:1, max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"请填写正确的角色名称"});
				$("#roleCode").formValidator({onShow:"请输入角色编码",onFocus:"至少1字符，至多10个汉字或20个英文字符",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"请填写正确的角色编码"});
				$("#description").formValidator({onShow:"请输入角色描述",onFocus:"至多100个汉字或200个英文字符",onCorrect:"符合要求"}).inputValidator({max:200,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写正确的角色描述"});
			})
		</script>			
	</body>
</html>
