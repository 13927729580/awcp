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
			<button type="button" class="btn btn-success" id="quickModifyAlias"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
		
			<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
			<div class="form-group"><!-- 显示1列数据 -->
				<!-- 自己输入or下拉选择(dataname.itemcode) -->
				<label class="col-md-2 control-label required">数据源名称</label>
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
			
			if(dialog != null){
				//alert(typeName + "组件配置");
				dialog.title("批量修改数据源名称");
				dialog.width(300);
				dialog.height(400); 
				dialog.reset();     // 重置对话框位置
			}
			
			var data = null;
			
			var dataSource = null;
			var _selects = null;
			
			if(dialog != null){
				data = dialog.data;
				if(data != null && data.dataSource)
					dataSource = data.dataSource;
				
				if(data != null && data._selects)
					_selects = data._selects;
			}
			if(data.dynamicPageId && $("#dynamicPageId")){
				$("#dynamicPageId").val(data.dynamicPageId);
			}
			//initialize data source options
			//var dataSourceArray = eval(dataSource);
			//initializeDataSource(dataSourceArray);
			
			
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
				if($.formValidator.pageIsValid('1')){
					
					var alias = $("#alias").val();
					var dynamicPageId = $("#dynamicPageId").val();

					$.ajax({
						type: "POST",
						url: basePath + "component/batchModifyDataAlias.do",
						data: "alias="+alias + 
						"&_selects="+_selects+"&dynamicPageId=" + dynamicPageId,
						async : false,
						success: function(data){
							if("1" == data){	
								alert("修改成功")
								if(dialog != null){
									dialog.close(data);
									dialog.remove();
								}
							}else{
								alert("修改失败");
							}
						}
					});	
				}
				return false;
			});
			
		});
		
	
	</script>



