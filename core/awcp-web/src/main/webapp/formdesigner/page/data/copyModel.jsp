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
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="saveBtn"><i class="icon-plus-sign"></i>确定</button>
			<button type="button" class="btn btn-info" id="cancel"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<div class="form-group">
				<label class="col-md-2 control-label required">数据源别名</label>
				<div class="col-md-4">
					<input name="alias" class="form-control" id="alias" type="text" value="">
				</div>
			</div>
		</form>
	</div>

		<%@ include file="/resources/include/common_form_js.jsp" %>
		
		

	<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
				
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});/**/
			$("#alias").formValidator({onFocus:"请选择或输入数据源名称",onCorrect:"符合要求"})
				.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择或输入数据源名称"});
			
			
	
			$("#cancel").click(function(){
				if(dialog != null){
					dialog.close();
					dialog.remove();
				}
			});
			
			$("#saveBtn").click(function(){
					var alias = $("#alias").val();
					dialog.close(alias);
				return false;
			});
			
		});
	
	</script>



