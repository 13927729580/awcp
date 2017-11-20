<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/base/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>页面列表</title>
<link href="<%=basePath %>base/resources/zui/dist/css/zui.min.css" rel="stylesheet">
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/tips/tip-green/tip-green.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
		
		<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js" type="text/javascript"></script>
		<script src="<%=basePath%>resources/plugins/tips/jquery.poshytip.js"></script>
		<script src="<%=basePath%>resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" type="text/javascript" charset="UTF-8"></script>
		<script src="<%=basePath%>resources/plugins/formValidator4.1.0/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
		
		<link href="<%=basePath%>base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
		<link href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.min.css" rel="stylesheet">
		<link href="<%=basePath%>base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="<%=basePath%>base/resources/zui/dist/css/zui.min.css">
		<link title="default" rel="stylesheet" type="text/css" href="<%=basePath%>base/resources/content/styles/szCloud_common.css" />
		<link title="default" rel="stylesheet" type="text/css" href="../resources/styles/style.css" />
		<script src="<%=basePath%>base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
		<script src="<%=basePath%>base/resources/artDialog/dist/dialog.js"></script>
		<script src="<%=basePath%>base/resources/artDialog/lib/require.js"></script>
		<script src="<%=basePath%>base/resources/artDialog/lib/sea.js"></script>
</head>
<body>


<!-- 基本情况 -->
<form id="pageForm" action="" method="post">
	
	<div id="modelinfo">
		
		<input type="text" id="groupId" name="groupId" value="${value }">
		<table>
			<thead>
				<tr>
					<th>职务名称</th>
					<th><input type="input"  id="name" name="name" value="${name }"/></th>
				</tr>
			</thead>
			
		</table>
	</div>
</form>

<script type="text/javascript">
	$.ready(function(){
		var dialog = top.dialog.get(window);//top必须加，指代主页面
		//dialog.title('请输入');
		var groupId = dialog.data.groupId; // 获取主页面传递过来的数据
		dialog.title(groupId);
		$("#groupId").val(groupId);
		$('#ok').on('click', function () {//子页面按钮id为ok
			/* var val = $('#validatorType').val();
			dialog.close(val);//返回值给主页面、 */
			dialog.remove();
		});
	});

</script>
</body>
</html>