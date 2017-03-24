<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/base/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>动态页面表单</title>
<base href="<%=basePath%>">
<%@include file="../include/head.jsp"%>
</head>
<body class="C_formBody">
<div class="C_btnGroup clearfix"><div class="C_btns">
<button class="btn btn-info" id="2cbbd98e-dd46-7e2e-cf20-7e4eb74e39c5"><i class="icon-save"></i>保存</button></div></div>

<div class="content">
<form class="form-horizontal" id="pageForm" action="" method="post">
	<div class="form-group">
		<label class="col-md-2 control-label">所属高新技术领域</label>
		<div class="col-md-4">
			<select name="belongtotech"></select>
		</div>
		<label class="col-md-2 control-label">子领域</label>
		<div class="col-md-4"><div class="input-group "><input id="birthday" name="birthday" class="form-control date form-date"  type="text" value="1990-3-11"readonly></div></div></div><div class="form-group"><label class="col-md-2 control-label">文章描述</label><div class="col-md-4"><textarea name="articleDescription" class="form-control " id="articleDescription" rows="4" placeholder="" >印度神油泰戈尔的诗集</textarea></div><label class="col-md-2 control-label">文章来源</label><div class="col-md-4"><label class="radio-inline" ><input type="radio" name="articleFrom" id="articleFrom" value="blog"/>blog</label><label class="radio-inline" ><input type="radio" name="articleFrom" id="articleFrom" value="网站"/>网站</label><label class="radio-inline" ><input type="radio" name="articleFrom" id="articleFrom" value=" 报纸"/> 报纸</label><label class="radio-inline" ><input type="radio" name="articleFrom" id="articleFrom" value="书籍"/>书籍</label></div><label class="col-md-2 control-label">爱好</label><div class="col-md-4"><label class="checkbox-inline"> <input type="checkbox" name="hobby" id="hobby" value="篮球"/>篮球</label><label class="checkbox-inline"> <input type="checkbox" name="hobby" id="hobby" value="音乐"/>音乐</label><label class="checkbox-inline"> <input type="checkbox" name="hobby" id="hobby" value="爬山"/>爬山</label><label class="checkbox-inline"> <input type="checkbox" name="hobby" id="hobby" value="阅读"/>阅读</label> </div><label class="col-md-2 control-label">菜单选择</label><div class="col-md-4"><select name="menuSelect" class="form-control " id="menuSelect" ><option value="添加权限" >添加权限</option><option value="删除权限" >删除权限</option><option value="部门管理" >部门管理</option><option value="模型配置" >模型配置</option><option value="组件配置" >组件配置</option> </select></div><label class="col-md-2 control-label">密码</label><div class="col-md-4"><input name="pwd" class="form-control" id="pwd" type="text" placeholder="" value="aaa"></div></div><div class="form-group"><label class="col-md-2 control-label">确认密码</label><div class="col-md-4"><input name="repwd" class="form-control" id="repwd" type="text" placeholder="" value="aaa"></div></div></form></div>






	
	<%@include file="../include/foot.jsp"%>
	<script type="text/javascript">
		
		
		
		$(document).ready(function(){
			
				$.formValidator.initConfig({submitOnce: true, debug: false, autoTip:true,formID: "pageForm" });
				$("#birthday").formValidator({onShow:"生日输入提示",empty:true,onFocus:"生日获取焦点提示"});
				$("#articleDescription").formValidator({onShow:"文章输入提示",empty:false,onFocus:"文章获取焦点提示"});
				$("#articleFrom").formValidator({onShow:"",onFocus:""});
				$("#hobby").formValidator({onShow:"最多五个",empty:true,onFocus:"可选1-5个"});
				$("#menuSelect").formValidator({onShow:"",empty:false,onFocus:""});
				$("#pwd").formValidator({onShow:"输入密码提示",empty:false,onFocus:"密码获取焦点提示"}).regexValidator({onError:"错误",regExp:"rex"});
				$("#repwd").formValidator({onShow:"输入确认密码",empty:false,onFocus:"确认密码获取焦点提示"}).compareValidator({ onError:"两次密码输入不一致",operateor:"=",desID:"pwd"});
				$("#2cbbd98e-dd46-7e2e-cf20-7e4eb74e39c5".click(
						function(){
							dialog(
									{	content:'what are the fuck you are doing man ',
										okValue:'确定',
										cancleValue:'取消',
										width:30,
										height:40});
						}
			

		});
		
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
	</script>


</body>

</html>