<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<%@ include file="/resources/include/common_form_css.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
</head>
<body>
	<form id="dataSourceForm" action="">
		
		<ul class="ztree" id="itemCodeClass" />
		
	</form>

<%@ include file="/resources/include/common_form_js.jsp" %>
<script src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.min.js" language="javascript" type="text/javascript"></script>
<script type="text/javascript">
	var dialog;
	$(document).ready(function(){
		
		
		try {
			dialog = top.dialog.get(window);
			var data = dialog.data; // 获取对话框传递过来的数据
			
		//	if(data != null){
		//		$.fn.zTree.init($("#treeDemo"), setting, data).expandAll(true);
		//	}
			
			dialog.height($(document).height());
			dialog.reset();
		} catch (e) {
			alert(e);
			return;
		}
		var dataSourceArray = eval(data);
		
		initializeDataSource(dataSourceArray);
		
		
		
		function initializeDataSource(modelJsonArray){
		
			if(modelJsonArray && modelJsonArray.length > 0){
				for(var i = 0; i < modelJsonArray.length;i++){
					if(modelJsonArray[i].modelItemCodes){
						 var itemCodes = modelJsonArray[i].modelItemCodes.split(",");
						 for(var j = 0; j < itemCodes.length; j++){
							var option = "<li><a href='javascript:;' data='" + modelJsonArray[i].name + "." + itemCodes[j] + "' onClick=\"onClick('"+modelJsonArray[i].name+ "." + itemCodes[j] +"')\";>" +
								modelJsonArray[i].name + "." + itemCodes[j] +"</a></li>";
							
							$("#itemCodeClass").append(option);
						 }
					}
				}
			}
		}
		
	});
	
	function onClick(name) {

				dialog.close(name); // 关闭（隐藏）对话框
				dialog.remove();
			
	}
	
	
	
</script>
</body>
</html>