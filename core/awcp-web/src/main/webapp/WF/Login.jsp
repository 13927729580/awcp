<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>Jflow</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<link title="default" rel="stylesheet" type="text/css" href="<%=basePath%>WF/Style/login.css" />
<link title="default" rel="stylesheet" type="text/css" href="<%=basePath%>WF/Style/jquery.alert.css" />

<script type="text/javascript" src="<%=basePath%>WF/Scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Scripts/jquery.alert.js"></script>
<style type="text/css">
	.tab
	{
		margin-left: 180px;	
		width:90%;
	}
</style>
</head>
<body>

	<div class="head"></div>
	<div class="main">
		<div class="login">
			<form method="post" action="" id="form1">
				<table class="tab">
					<tr>
						<td>账户名</td>
						<td><input type="text" name="loginName" id="name" class="login_input" /></td>
						<td>密&nbsp;&nbsp;&nbsp;&nbsp;码</td>
						<td><input type="password" name="loginPass" id="password" class="login_input" /></td>
						<td width="100"><button type="button" style="font-size: 15;"
							class="login_bt" onclick="onCheck();"><strong>登&nbsp;&nbsp;&nbsp;录</strong></button></td>
						<!-- <td><a href="javascript:winoper('forget_password.html')"
							target="" title="忘记密码">忘记密码?</a>&nbsp;&nbsp;&nbsp;&nbsp;<a
							href="javascript:winoper('change_password.html')" target=""
							title="修改密码">修改密码</a></td> -->
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div class="clear"></div>
</body>

<script type="text/javascript">
	$(document).keydown(function(e) {
		if (e.keyCode == 13) {
			onCheck();
		}
	});

	/* var errNo = GetQueryString("errNo");
	if(null != errNo && "" != errNo){
		if("1" == errNo)$("#name").alert("请填写正确的账户名！");
	} */
	
	function onCheck() {
		var name = $.trim($("#name").val());
		if (name.length == 0) {
			$("#name").alert("请填写账户名！");
			return false;
		}
		var password = $.trim($("#password").val());
		if (password.length == 0) {
			$("#password").alert("请填写密码！");
			return false;
		}
		
		$.ajax({
			url:'<%=basePath%>WF/Login.do',
			type:'post', //数据发送方式
			dataType:'json', //接受数据格式
			data:$('#form1').serialize(),
			async: false ,
			error: function(data){},
			success: function(data){
				json = eval(data);
				if(json.success){
					window.location.href = "<%=basePath%>WF/Default.jsp";
				}else{
					$("#password").alert(json.msg);
				}
			}
		});
		
		//window.location.href = "<%=basePath%>WF/Default.jsp?name=" + name;
	}
	function GetQueryString(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return unescape(r[2]);
		return null;
	}
</script>
</html>
