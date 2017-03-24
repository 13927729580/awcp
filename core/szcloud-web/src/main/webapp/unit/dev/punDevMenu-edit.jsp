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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>菜单编辑页面</title>
		<%@ include file="/resources/include/common_form_css.jsp" %>
		
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="#">首页</a></li>
		          <li><a href="#">组织机构与权限</a></li>
		          <li class="active">菜单编辑</li>
		        </ul>
			</div>
			
			<div class="row" id="dataform">
				<form class="form-horizontal" id="groupForm" action="<%=basePath%>dev/punDevMenuSave.do" method="post">
                <div class="C_addForm">
			          <legend class=" text-center">
			          		菜单编辑
			          	<c:if test="${result!=null&&''!=result}">
			          		<span style="color:red">(${result})</span>
			          	</c:if>
			          </legend>
			          <div class="form-group">       	
			            <label class="col-md-1 control-label">父节点</label>
			            <div class="col-md-4">
			            	<input type="hidden" name="menuId" value="${vo.menuId}"/>
							 <input name="parentMenuId" class="form-control" id="parentMenuId" type="text" placeholder="" value="${vo.parentMenuId}">
			            </div>
			             <label class="col-md-2 control-label required">资源类型:</label>
			            <div class="col-md-4">
			                <input name="menuType" class="form-control" id="menuType" type="text" placeholder="" value="${vo.menuType}">
			            </div> 
			            </div>
			            <div class="form-group"> 
			            <label class="col-md-1 control-label required">资源名称:</label>
			            <div class="col-md-4">
			                <input name="menuName" class="form-control" id="menuName" type="text" placeholder="" value="${vo.menuName}">
			            </div> 
			            <label class="col-md-2 control-label required">资源地址:</label>
			            <div class="col-md-4">
			                <input name="menuAddress" class="form-control" id="menuAddress" type="text" placeholder="" value="${vo.menuAddress}">
			            </div> 
			            </div>
			            <div class="form-group"> 
			            <label class="col-md-1 control-label required">资源顺序:</label>
			            <div class="col-md-4">
			                <input name="menuSeq" class="form-control" id="menuSeq" type="text" placeholder="" value="${vo.menuSeq}">
			            </div> 
			            </div>
			            <div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-2 col-md-10">
			              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
							<a href="<%=basePath%>dev/punDevMenuList.do?currentPage=1" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
			            </div>
			          </div>
					 </div>
					
				  </form>
			</div>
		</div>			
			<%@ include file="/resources/include/common_form_js.jsp" %>	
			<script type="text/javascript">
         $(function(){
	//保存按钮
	$("#saveBtn").click(function(){});
		
	   $.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
			$("#groupForm").submit();
			  return false;
	    },onError:function(){alert("错误，请看提示")}});
	$("#parentMenuId").formValidator({empty:true,onShow:"请输入父节点",onCorrect:"符合格式要求"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
	$("#menuType").formValidator({onShow:"请输入资源类型",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
    $("#menuName").formValidator({onShow:"请输入资源名称",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
    $("#menuAddress").formValidator({onShow:"请输入资源地址",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
    $("#menuSeq").formValidator({onShow:"请输入资源顺序",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
         })
         </script>		
	</body>
</html>
