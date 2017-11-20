<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link title="default" rel="stylesheet" type="text/css" href="<%=basePath%>WF/Style/login.css" />
<link title="default" rel="stylesheet" type="text/css" href="<%=basePath%>WF/Style/jquery.alert.css" />

<script type="text/javascript" src="<%=basePath%>WF/Scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/jquery.alert.js"></script>
</head>
<body>
	<div class="head"></div>
	<div class="main">
		<div class="login">
			<table width="100%">
				<tr>
					<td>账户名</td>
					<td><input type="text" id="name" class="login_input" /></td>
					<td>密&nbsp;&nbsp;&nbsp;&nbsp;码</td>
					<td><input type="password" class="login_input" /></td>
					<td width="100"><input type="button" value="登陆"
						class="login_bt" onclick="onCheck();" /></td>
					<!-- <td><a href="javascript:winoper('forget_password.html')"
						target="" title="忘记密码">忘记密码?</a>&nbsp;&nbsp;&nbsp;&nbsp;<a
						href="javascript:winoper('change_password.html')" target=""
						title="修改密码">修改密码</a></td> -->
				</tr>
			</table>
		</div>
	</div>
	<div class="clear"></div>
	<div class="foot"></div>
</body>
</html>