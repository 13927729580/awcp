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
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>菜单编辑页面</title>
 		<%@ include file="../../resources/include/common_css.jsp" %>
		
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
		          <li><a href="#">组织机构与权限</a></li>
		          <li class="active">菜单编辑</li>
		        </ul>
			</div>
			
			<div class="row" id="dataform">
			<form class="form-horizontal" id="groupForm"
				action="<%=basePath%>unit/punMenuSave.do" method="post">

				<legend>
					菜单编辑
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
				</legend>
				<div class="form-group">
					<label class="col-md-2 control-label">父节点</label>
					<div class="col-md-4">
						<input type="hidden" name="menuId" value="${vo.menuId}" /> <input
							name="parentMenuId" class="form-control" id="parentMenuId"
							type="text" placeholder="" value="${vo.parentMenuId}">
					</div>
					<label class="col-md-1 control-label required">资源类型:</label>
					<div class="col-md-4">
						<input name="menuType" class="form-control" id="menuType"
							type="text" placeholder="" value="${vo.menuType}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">资源名称:</label>
					<div class="col-md-4">
						<input name="menuName" class="form-control" id="menuName"
							type="text" placeholder="" value="${vo.menuName}">
					</div>
					<label class="col-md-1 control-label required">资源地址:</label>
					<div class="col-md-4">
						<input name="menuAddress" class="form-control" id="menuAddress"
							type="text" placeholder="" value="${vo.menuAddress}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">资源顺序:</label>
					<div class="col-md-4">
						<input name="menuSeq" class="form-control" id="menuSeq"
							type="text" placeholder="" value="${vo.menuSeq}">
					</div>
				</div>				
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
						<a href="<%=basePath%>unit/punMenuList.do?currentPage=1" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		          </div>
			</form>
			<hr>
			<form class="form-horizontal" id="flowForm" action="<%=basePath%>unit/punMenuSave.do" method="post">

				<legend>流程选择</legend>				
				<div class="form-group twoWay-panel" id="twoWay">
					<div class="col-md-offset-2 col-md-4 col-xs-4">
						<div class="panel twoWay-left">
				            <div class="panel-heading">流程一</div>
				            <div class="twoWay-status">
				            	<span>流程定义</span><span>环节</span><span>状态</span>
				            </div>
				            <ul class="list-group">
				              <li class="list-group-item" data-id="11"><span>activityTip:11:1234567</span><span>todo11</span><span>已审核</span></li>
				              <li class="list-group-item" data-id="12"><span>version:11:123</span><span>todo11</span><span>已审核</span></li>
				              <li class="list-group-item" data-id="13"><span>version:11:123</span><span>todo11</span><span>已审核</span></li>
				              <li class="list-group-item" data-id="14"><span>version:11:123</span><span>todo11</span><span>已审核</span></li>
				              <li class="list-group-item" data-id="15"><span>version:11:123</span><span>todo11</span><span>已审核</span></li>
				            </ul>
				        </div>
					</div>
					<div class="col-md-1 col-xs-2">
						<div class="twoWay-btns">
							<button class="btn btn-sm btn-forward" type="button"><i class="icon-forward"></i></button><br>
							<button class="btn btn-sm btn-backward" type="button"><i class="icon-backward"></i></button>
						</div>
					</div>
					<div class="col-md-4 col-xs-4">
						<div class="panel twoWay-right">
			            	<div class="panel-heading">流程二</div>
			            	<div class="twoWay-status">
				            	<span>流程定义</span><span>环节</span><span>状态</span>
				            </div>
				            <ul class="list-group">
				              <li class="list-group-item" data-id="21"><span>todo</span><span>version:11:123</span><span>已审核</span></li>
				              <li class="list-group-item" data-id="22"><span>todo</span><span>version:11:123</span><span>已审核</span></li>
				              <li class="list-group-item" data-id="23"><span>todo</span><span>version:11:123</span><span>已审核</span></li>
				              <li class="list-group-item" data-id="24"><span>todo</span><span>version:11:123</span><span>已审核</span></li>
				              <li class="list-group-item" data-id="25"><span>todo</span><span>version:11:123</span><span>已审核</span></li>
				            </ul>
				        </div>
					</div>
				</div>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
						<a href="#" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		          </div>
			</form>
			</div>
		</div>
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/platform.twoWay.js"></script>
		<script type="text/javascript">
         $(function(){
        	 
           $("#twoWay").twoWay();//流程加载
           
		   $.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
				$("#groupForm").submit();
				  return false;
		    },onError:function(){alert("请按提示正确填写内容");}});
			$("#parentMenuId").formValidator({empty:true,onShow:"请输入父节点",onCorrect:"符合格式要求"}).regexValidator({regExp:"num1",dataType:"enum",onError:"正整数格式不正确"});
			$("#menuType").formValidator({onShow:"请输入资源类型",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		    $("#menuName").formValidator({onShow:"请输入资源名称",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		    $("#menuAddress").formValidator({onShow:"请输入资源地址",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		    $("#menuSeq").formValidator({onShow:"请输入资源顺序",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
         });
         </script>	
	</body>
</html>
