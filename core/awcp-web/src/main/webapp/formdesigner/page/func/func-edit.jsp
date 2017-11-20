<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
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
 		<title>函数库页面</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">			
			<div class="row" id="dataform">
				<form class="form-horizontal"  id="funcForm"  action="<%=basePath%>func/save.do" method="post" >
					 	<c:if test="${message!=null&&''!=message}">
							<span style="color: red">(${message})</span>
						</c:if>
					 	<input type="hidden" name="pageId" value="${vo.pageId}"/>	
				  		<input type="hidden" name="code" value="${vo.code}"/>	
						<div class="form-group">       	
				            <label class="col-md-2 control-label required">函数库名称：</label>
				            <div class="col-md-10">
				                <input name="name" class="form-control" id="name" type="text" value="${vo.name}">
				            </div>
				 	    </div>
						<div class="form-group">       	
				            <label class="col-md-2 control-label required">描述：</label>
				            <div class="col-md-10">
				                <input name="description" class="form-control" id="description" type="text" value="${vo.description}">
				            </div>  
				 	    </div>
						<div class="form-group">       	
				            <label class="col-md-2 control-label required">脚本：</label>
				            <div class="col-md-10">
				                <textArea name="script" class="form-control" id="script" rows="20">${vo.script}</textArea>
				            </div>
						</div>
						<div class="form-group"><!-- 表单提交按钮区域 -->
	           			 	<div class="col-md-offset-2 col-md-10">
	              				<button type="button" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
	              				<a href="<%=basePath%>func/list.do" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
	            			</div>
	       				</div>
				</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript">
         	$(document).ready(function(){        	 	
			    $.formValidator.initConfig({
			    	formID:"funcForm",
			    	debug:false,
			    	onError:function(){
			    		alert("请按提示正确填写内容");
			    	}
			    });
			    
				$("#name").formValidator({
					empty:true,
					onShow:"请输入函数库名称",
					onFocus:""
				}).inputValidator({
					min:1,
					max:32,
					empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},
					onError:"不能为空,请确认"
				}).regexValidator({
					regExp:"[_a-zA-Z][_a-zA-Z0-9]*",
					dataType:"string",
					onError:"名称只能包含字母、数字、下划线(以字母或下划线开头)"
				});
				
				$("#description").formValidator({
					onShow:"请输入描述信息",
					onFocus:""
				}).inputValidator({
					min:1,
					empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},
					onError:"不能为空,请确认"
				});
				
		    	$("#script").formValidator({
		    		onShow:"请输入脚本",
		    		onFocus:""
		    	}).inputValidator({
		    		min:1,
		    		empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},
		    		onError:"不能为空,请确认"
		    	}); 
		
		    	$("#saveBtn").click(function(){
		    		$("#funcForm").submit();
					return false;
				});
         	});
         </script>	
	</body>
</html>
