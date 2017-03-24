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
<title>政务云应用管理平台</title>
<%@ include file="/resources/include/common_form_css.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
</head>
<body>
	<ul id="treeDemo" class="ztree"></ul>

<%@ include file="/resources/include/common_form_js.jsp" %>
<script src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.min.js" language="javascript" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var setting = {
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onClick
			}
		};
		var dialog;
		try {
			dialog = top.dialog.get(window);
			var data = dialog.data; // 获取对话框传递过来的数据
			if(data != null){
				$.fn.zTree.init($("#treeDemo"), setting, data).expandAll(true);
			}
			//dialog.height($(document).height());
			dialog.height(600);
			dialog.reset();
		} catch (e) {
			alert(e);
			return;
		}
		function onClick(event, treeId, treeNode, clickFlag) {
			var isParent = treeNode.isParent;
			if(isParent){
				//alert("我是父亲");
			} else {
				dialog.close({'id':treeNode.id,'name':treeNode.name}); // 关闭（隐藏）对话框
				dialog.remove();
			}
		}	
	});
</script>
</body>
</html>