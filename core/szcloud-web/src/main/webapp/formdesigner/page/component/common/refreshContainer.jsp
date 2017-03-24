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
			<button type="button" class="btn btn-success" id="quickModifyAlias"><i class="icon-plus-sign"></i>确定</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
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
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script src="<%=basePath%>/resources/plugins/echarts-2.2.1/src/component.js"></script>
		<script src="<%=basePath%>/formdesigner/scripts/form.cpcommons.js"></script>

	<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
			
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			
			var data = null;
			var _selects = null;
			
			if(dialog != null){
				data = dialog.data;
				if(data != null && data._selects){
					_selects = data._selects;
				}
					
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
			
			
	
			$("#cancelComponent").click(function(){
				if(dialog != null){
					dialog.close();
					dialog.remove();
				}
			});
			
			$("#quickModifyAlias").click(function(){
				var alias = $("#alias").val();
					$.ajax({
						type: "POST",
						url: basePath + "component/refreshContainerComponent.do",
						data: "alias="+alias + 
						"&_selects="+_selects,
						async : false,
						success: function(data){
							if("1" == data){	
								if(dialog != null){
									dialog.close(data);
									dialog.remove();
								}
							}else{
								dialog.close(data);
								dialog.remove();
							}
						}
					});	
				
				return false;
			});
			
		});
	
	</script>



