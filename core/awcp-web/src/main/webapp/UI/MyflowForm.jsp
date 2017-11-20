<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>JFlow</title>
<link rel="stylesheet" href="assets/css/amazeui.min.css" />
<link rel="stylesheet" href="assets/css/admin.css">
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/app.js"></script>
</head>
<body>
	<!-- 头 -->
	<header class="am-topbar admin-header">

	<div class="am-topbar-brand">
		<strong>JFlow</strong><small>后台管理</small>
	</div>
	<div class="am-collapse am-topbar-collapse" id="topbar-collapse">
		<ul
			class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
			<li><a href="#"><span class="am-icon-user"></span> 个人资料</a></li>
			<!-- 必选加 am-dropdown，data-am-dropdown，超链接中必须加am-dropdown-toggle，data-am-dropdown-toggle-->
			<li class="am-dropdown" data-am-dropdown><a
				class="am-dropdown-toggle" data-am-dropdown-toggle href="#"><span
					class="am-icon-cog"></span> 设置</a>
				<ul class="am-dropdown-content">
					<li><a href="#"><span class="am-icon-user"></span> 修改密码</a></li>
					<li><a href="#"><span class="am-icon-home"></span> 更换风格</a></li>
					<li><a href="#"><span class="am-icon-sign-out"></span> 注销</a></li>
				</ul></li>
			<li><a href="#"><span class="am-icon-power-off"></span> 退出</a></li>
		</ul>
	</div>

	</header>


	<!-- 左侧菜单栏 -->
	<div class="admin-sidebar am-offcanvas" id="admin-offcanvas">
		<div class="am-offcanvas-bar admin-offcanvas-bar">
			<ul class="am-list admin-sidebar-list">
				<li><a href="#"><span class="am-icon-home"></span> 信息</a></li>
				<li class="admin-parent"><a class="am-cf"
					data-am-collapse="{target: '#collapse-nav'}"><span
						class="am-icon-pencil-square-o"></span> 处理<span
						class="am-icon-angle-right am-fr am-margin-right"></span></a>
					<ul class="am-list am-collapse admin-sidebar-sub am-in"
						id="collapse-nav">
						<li><a href="Myflow.jsp" class="am-cf"><span
								class="am-icon-file"></span> 发起</a></li>
						<li><a href="MyflowTable.jsp"><span class="am-icon-file"></span>
								代办</a></li>
						<li><a href="MyflowForm.jsp" target=""><span
								class="am-icon-file"></span> 在途</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 抄送</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 批处理</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 共享任务</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 挂起</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 查询</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 取回审批</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 已完成</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 成员</a></li>
						<li><a href="#"><span class="am-icon-file"></span> 设置</a></li>
					</ul></li>
			</ul>
		</div>
	</div>

	<!-- 内容 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>发起列表</small>
			</div>
		</div>
		<div class="admin-content">

			<div class="am-tabs am-margin" data-am-tabs>
				<ul class="am-tabs-nav am-nav am-nav-tabs">
					<li class="am-active"><a href="#tab1">基本信息</a></li>
					<li><a href="#tab2">详细描述</a></li>
					<li><a href="#tab3">SEO 选项</a></li>
				</ul>
				<div class="am-tabs-bd">
					<div class="am-tab-panel am-fade" id="tab1"></div>
					<div class="am-tab-panel am-fade" id="tab2" name="tab2">
						<form class="am-form">
							<div class="am-g am-margin-top">
								<div class="am-u-sm-4 am-u-md-2 am-text-right">文章标题</div>
								<div class="am-u-sm-8 am-u-md-4">
									<input type="text" class="am-input-sm">
								</div>
								<div class="am-hide-sm-only am-u-md-6">*必填，不可重复</div>
							</div>

							<div class="am-g am-margin-top">
								<div class="am-u-sm-4 am-u-md-2 am-text-right">文章作者</div>
								<div class="am-u-sm-8 am-u-md-4 am-u-end col-end">
									<input type="text" class="am-input-sm">
								</div>
							</div>

							<div class="am-g am-margin-top">
								<div class="am-u-sm-4 am-u-md-2 am-text-right">信息来源</div>
								<div class="am-u-sm-8 am-u-md-4">
									<input type="text" class="am-input-sm">
								</div>
								<div class="am-hide-sm-only am-u-md-6">选填</div>
							</div>

							<div class="am-g am-margin-top">
								<div class="am-u-sm-4 am-u-md-2 am-text-right">内容摘要</div>
								<div class="am-u-sm-8 am-u-md-4">
									<input type="text" class="am-input-sm">
								</div>
								<div class="am-u-sm-12 am-u-md-6">不填写则自动截取内容前255字符</div>
							</div>

							<div class="am-g am-margin-top-sm">
								<div class="am-u-sm-12 am-u-md-2 am-text-right admin-form-text">
									内容描述</div>
								<div class="am-u-sm-12 am-u-md-10">
									<textarea rows="10" placeholder="请使用富文本编辑插件"></textarea>
								</div>
							</div>
							<div class="am-margin">
								<button type="button" id="btn" class="am-btn am-btn-primary am-btn-xs">提交保存</button>
								<button type="button" class="am-btn am-btn-primary am-btn-xs">放弃保存</button>
							</div>
						</form>
					</div>
					<div class="am-tab-panel am-fade" id="tab3"></div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$("btn").click(function() {
			$("tab2").show();
		});
	});
</script>
</html>