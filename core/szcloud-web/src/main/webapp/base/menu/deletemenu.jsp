<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
<title>Form demo</title>
<link rel="StyleSheet" href="<%=basePath%>css/source.css" type="text/css" />
<script type="text/javascript" src="<%=basePath%>resources/JqEdition/jquery-1.9.1.min.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>resources/plugins/datetime/WdatePicker.js"></script>
</head>
<body>
<table width="300" border="0" cellpadding="3" cellspacing="1" >
		<tr>
			<td>
				<input id="id1"  class="Wdate"  type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 70%;"/>
			</td>
		</tr>
</table>
<input type="button" name="SubmitButton" value="登陆" onclick="test();">
	</body>
	
	<script type="text/javascript">
		function test()
		{
			alert($("#id1").val() + "---");
		}
	</script>
</html>
