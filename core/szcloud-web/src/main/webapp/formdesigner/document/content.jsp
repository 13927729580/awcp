<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/base/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title> </title>
<base href="<%=basePath%>">
<%@include file="../include/head.jsp"%>
</head>
<body>
	${html}
	<%@include file="../include/foot.jsp"%>
	<script type="text/javascript">
		function actClick(o) {
			//set id of act (o.id) to hidden input 
			/**/var value = $(o).attr("id");
			$("#actId").val(value);
			//submit form
			$("#pageForm").submit();
		}

		if ($.fn.datetimepicker) {
			$('.form-datetime').datetimepicker({
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				forceParse : 0,
				showMeridian : 1,
				format : 'yyyy-mm-dd hh:ii'
			});
			$('.form-date').datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				format : 'yyyy-mm-dd'
			});
			$('.form-time').datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				maxView : 1,
				forceParse : 0,
				format : 'hh:ii'
			});
		}

		$(document).ready(function() {
			${script}
		});
	</script>
</body>

</html>