<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>校验编辑页面</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
		
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <!-- <li><a href="#">首页</a></li>
		          <li><a href="#">我的应用产品</a></li> -->
		          <li class="active">自定义校验编辑</li>
		        </ul>
			</div>
			
			<div class="row" id="dataform">
				<form class="form-horizontal"  id="actForm"  action="<%=basePath%>fd/validator/saveCustomValidator.do" method="post" >
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
					<input type="hidden" name="pageId" id="id" value="${vo.pageId }"/>
					<input type="hidden" name="code" value="${vo.code}"/>
	        		<input type="hidden" name="isSelect" value="${isSelect}">	
					<div class="form-group">       	
				            <label class="col-md-2 control-label required">校验名称：</label>
				            <div class="col-md-4">
				                <input name="name" class="form-control" id="name" type="text" value="${vo.name}">
				            </div>
					 </div>
					  <div class="form-group">       	
				            <label class="col-md-2 control-label required">校验描述：</label>
				            <div class="col-md-4">
				                <input name="description" class="form-control" id="description" type="text" value="${vo.description}">
				            </div>  
			 		</div>
			 		<div class="form-group">
					<label class="col-md-2 control-label">客户端脚本</label>
					<div class="col-md-4">
						<textarea name="clientScript" class="form-control" id="clientScript"
							rows="8">${vo.clientScript}</textarea>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">服务器脚本</label>
					<div class="col-md-4">
						<textarea name="serverScript" class="form-control" id="serverScript"
							rows="8">${vo.serverScript}</textarea>
					</div>
				</div>
					<div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-2 col-md-10">
			              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
			              	<a href="<%=basePath%>fd/validator/list.do?currentPage=1" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
			            </div>
			       </div>
				</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
		$(document).ready(function(){
			$.formValidator.initConfig({formID:"actForm",debug:false,onSuccess:function(){
				$("#actForm").submit();
				return false;
		    },onError:function(){alert("请按提示正确填写内容");}});
		    $("#name").formValidator({empty:true,onShow:"请输入校验名称,不超过40字符",onCorrect:"符合格式要求"})
				.inputValidator({min:1,max:40,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写名称"});
         });
         </script>	
	</body>
</html>
