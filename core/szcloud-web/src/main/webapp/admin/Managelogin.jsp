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

<title>政务云应用管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<link title="default" rel="stylesheet" type="text/css" href="<%=basePath%>resources/styles/login.css" />
<link rel="stylesheet" href="<%=basePath%>resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
</head>
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		class="bodyContainer">
		<tr class="szC-logoBar">
			<td align="left" colspan="2">
				<div class="szC-L-logo">
					<div class="szC-logo">
						<span></span>
					</div>
				</div>
			</td>
		</tr>
		<tr id="box" class="szC-form">
			<td align="center" colspan="2">
				<div class="login_box admin">
					<div class="login_temp"></div>
					<div class="login_form">
						<form action="<%=basePath%>admin/login.do" id="loginForm"
							method="post">
							<ul>
								<li><h1>管理员账号登录</h1></li>
								<div class="errorTip">${result}</div>
								<li><input type="text" id="orgCode" name="orgCode"
									title="组织机构代码为12位数字" class="form-control w300"
									value=""
									onfocus="javascript:if(this.value=='组织机构代码')this.value='';"
									onBlur="javascript:if(this.value=='')this.value='组织机构代码';"></li>
								<li><input type="text" id="userIdCardNumber"
									title="用户名为6-12位字母或数字" name="userIdCardNumber"
									class="form-control w300" value=""
									onfocus="javascript:if(this.value=='身份证号')this.value='';"
									onBlur="javascript:if(this.value=='')this.value='身份证号';"></li>
								<li><input type="text" id="userPwd" name="userPwd"
									title="密码至少6位" class="form-control w300" value=""
									onfocus="javascript:if(this.value=='登录密码')this.value='';"
									onBlur="javascript:if(this.value=='')this.value='登录密码';"></li>
								<li class="clearfix"><input type="text" id="verifyCode"
									name="verifyCode" title="4位校验码" class="form-control w160"
									value="验证码"
									onfocus="javascript:if(this.value=='验证码')this.value='';"
									onBlur="javascript:if(this.value=='')this.value='验证码';"
									style="float: left; display: inline-block;" /> <span
									style="float: left; padding-left: 18px; display: inline-block;">
										<a title="显示一组新字符"
										href="javascript:refreshVerifyCodeImage();"> <img
											id="verifyCodeImage"
											src="base/resources/content/images/code.jpg" alt=""
											title="验证图像" width="119" height="48" border="0">
									</a>
								</span>
								<li class="clearfix"><span class="rem fl"><input type="checkbox"
										style="margin: 0">&nbsp;记住密码</span> <span
									class="forg fr"><a href="javascript:void(0)">忘记密码？</a></span></li>
								<li class="clearfix"><a href="javascript:void(0)"
									class="btn btn-success w120 h36" id="loginButton">登录</a> <a href="javascript:void(0)"
									class="btn btn-primary w120 h36" >注册</a></li>
							</ul>
						</form>
					</div>
				</div>
			</td>
		</tr>
		<tr class="szC-logoBar">
			<td align="left"><span class="Flink fw14"
				style="font-size: 14px;"> <a href="javascript:void(0)">关于政务云</a>
					<a href="javascript:void(0)">客户服务</a> <a
					href="javascript:void(0)">隐私政策</a> <a
					href="javascript:void(0)">联系我们</a>
			</span></td>
			<td align="right"><span class="copyText"> 政务云 版权所有
					©2014-2018 </span></td>						
		</tr>
	</table>
	<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/tips/jquery.poshytip.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" ></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/formValidator4.1.0/formValidatorRegex.js"></script>
	
	<script language="javascript">


function refreshVerifyCodeImage() {//图形验证码 
	document.getElementById("verifyCodeImage").src = "<%=basePath%>VerifyCodeServlet?now="
		+ new Date();
};	
function FrameSet(){ 
	if($(window).width()<767)
		$("#box").css("height",$(window).height()-88);
	else
		$("#box").css("height",$(window).height()-160);
};	
$(function(){
	//FrameSet();	
	$("#loginButton").click(function() {
		$("#loginForm").submit();
	});
	$("#verifyCode").keydown(function(e){
		if(e.keyCode==13){
			$("#loginForm").submit();	
		}
	});
	refreshVerifyCodeImage();
	$.formValidator.initConfig({ mode: "AutoTip", submitOnce: true, debug: false, formID: "loginForm" });
	$("#orgCode").formValidator({ onEmpty: "请输入组织机构代码", 
		onFocus: "组织机构代码为10位，例如12345678-9", onCorrect: ""})
	.regexValidator({ regExp: "orgCode", dataType: "enum", onError: "组织机构代码格式不正确" });
	 
	$("#userIdCardNumber").formValidator({onShow:"请输入身份证号",onFocus:"为15或18位",onCorrect:"符合要求"}).
	regexValidator({regExp:"^(\\d{18,18}|\\d{15,15}|\\d{17,17}x)$",onError:"请输入正确的身份证号码"});

    $("#userPwd").formValidator({ onEmpty: "请输入密码", onFocus: "6~20个字符，包括字母、数字、特殊符号，区分大小写"})
		.inputValidator({ min: 6, max: 20, empty: { leftEmpty: false, rightEmpty: false, emptyError: "密码两边不能有空符号" }, 
			onError: "密码长度错误,请确认" });
  
    $("#verifycode").formValidator({ onShowText: "请输入验证码", 
    		onFocus: "请输入图片中的字符，不分区大小写", 
    		onEmpty: "请输入验证码"})
    	.inputValidator({ min: 1, onError: "请输入验证码" });
		
});
	
</script>
</body>
</html>
