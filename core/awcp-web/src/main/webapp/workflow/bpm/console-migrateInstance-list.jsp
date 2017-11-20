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
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>实例迁移</title>
 		<%@include file="../../resources/include/common_css.jsp" %>
<%--  		<%@include file="../common/taglibs.jsp"%> --%>
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="dataform">
			<form class="form-horizontal" id="flowForm" action="console-migrateInstance.do" method="post">
				<legend>实例迁移</legend>				
				<div class="form-group twoWay-panel" id="twoWay">
					<div class="col-md-offset-2 col-md-4 col-xs-4">
						<div class="panel twoWay-left">
				            <div class="panel-heading">${procDefs[0]}
				            <input id="fromProDefId" name="fromProDefId" type="hidden" value="${procDefs[0]}">
				            <input id="processId" name="processId" type="hidden" value="${processId}">
				            </div>
				            <div class="twoWay-status">
				            	<span>实例Id</span><span>流转环节</span><span>状态</span>
				            </div>
				            <ul class="list-group">
				            <c:forEach items="${fromInstances}" var="item1">
								 <li class="list-group-item" data-id="${item1.id }"><span>${item1.id}</span>
								 <span>${item1.activityId }</span>
								 <span>
									 <c:if test="${item1.suspended}">挂起</c:if>
									  <c:if test="${not item1.suspended}">激活</c:if>
								</span></li>
							</c:forEach>
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
			            	<div class="panel-heading">${procDefs[1] }
			            	<input id="toProDefId" name="toProDefId" type="hidden" value="${procDefs[1]}">
			            	</div>
			            	<div class="twoWay-status">
				            	<span>实例Id</span><span>流转环节</span><span>状态</span>
				            </div>
				            <ul class="list-group">
				            <c:forEach items="${toInstances}" var="item2">
								<li class="list-group-item" data-id="${item2.id }"><span>${item2.id}</span>
								 <span>${item2.activityId }</span>
								 <span>
									 <c:if test="${item2.suspended}">挂起</c:if>
									  <c:if test="${not item2.suspended}">激活</c:if>
								</span></li>							
							</c:forEach>
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
