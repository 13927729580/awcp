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
	var basePath = "<%=basePath%>";
	$(document).ready(function(){
		
		
		try {
			dialog = top.dialog.get(window);
			var pageId = dialog.data; // 获取对话框传递过来的数据
			
			dialog.height($(document).height());
			dialog.reset();
		} catch (e) {
			alert(e);
			return;
		}
		
		if(pageId != null){
			$.ajax({
			   		type: "POST",
			   		async:false,
			  		url: basePath + "component/getComponentListByPageId.do?dynamicPageId=" + pageId + "&pageSize=10000" ,
			  		success: function(data){
			  			//alert(data);
							if(data != null){
								
								$.each(data, function(index, item) {
									component = eval("(" + item.content + ")");
									if(component['componentType'] != "1009" && component['componentType'] != "1012"  && component['componentType'] != "1014"){
										
										
										var option = "<li><a href='javascript:;' data='" + component['dataItemCode']+ "' onClick=\"onClick('"+component['dataItemCode']+"','"+ component['pageId'] +"')\";>" +
											component['dataItemCode'] +"</a></li>";
							
										$("#itemCodeClass").append(option);
									}
									
								});
							}else{
								alert("加载组件失败");
							}
							
					   }
					});	
		}
		
		//var dataSourceArray = eval(data);
		
		//initializeDataSource(dataSourceArray);
		
		
		
		
		
	});
	
	function onClick(code,id) {
				dialog.close({'storeCode':code,'storeId':id}); // 关闭（隐藏）对话框
				dialog.close(name); // 关闭（隐藏）对话框
				dialog.remove();
			
	}
	
	
	
</script>
</body>
</html>