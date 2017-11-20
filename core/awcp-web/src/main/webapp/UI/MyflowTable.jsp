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
						<li><a href="Myflow.jsp" class="am-cf"><span class="am-icon-file"></span>
								发起</a></li>
						<li><a href="MyflowTable.jsp"><span class="am-icon-file"></span>
								代办</a></li>
						<li><a href="MyflowForm.jsp" target=""><span class="am-icon-file"></span>
								在途</a></li>
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
		<!-- 表格数据 -->
		<div class="admin-content">

			<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>发起列表</small>
			</div>
		</div>

			<div class="am-g">
				<div class="am-u-sm-12 am-u-md-6">
					<div class="am-btn-toolbar">
						<div class="am-btn-group am-btn-group-xs">
							<button type="button" class="am-btn am-btn-default">
								<span class="am-icon-plus"></span> 新增
							</button>
							<button type="button" class="am-btn am-btn-default">
								<span class="am-icon-save"></span> 保存
							</button>
							<button type="button" class="am-btn am-btn-default">
								<span class="am-icon-archive"></span> 审核
							</button>
							<button type="button" class="am-btn am-btn-default">
								<span class="am-icon-trash-o"></span> 删除
							</button>
						</div>
					</div>
				</div>
				<div class="am-u-sm-12 am-u-md-3">
					<div class="am-form-group">
						<select data-am-selected="{btnSize: 'sm'}">
							<option value="option1">所有类别</option>
							<option value="option2">IT业界</option>
							<option value="option3">数码产品</option>
							<option value="option3">笔记本电脑</option>
							<option value="option3">平板电脑</option>
							<option value="option3">只能手机</option>
							<option value="option3">超极本</option>
						</select>
					</div>
				</div>
				<div class="am-u-sm-12 am-u-md-3">
					<div class="am-input-group am-input-group-sm">
						<input type="text" class="am-form-field"> <span
							class="am-input-group-btn">
							<button class="am-btn am-btn-default" type="button">搜索</button>
						</span>
					</div>
				</div>
			</div>

			<div class="am-g">
				<div class="am-u-sm-12">
					<form class="am-form">
						<table class="am-table am-table-striped am-table-hover table-main">
							<thead>
								<tr>
									<th class="table-check"><input type="checkbox" /></th>
									<th class="table-id">序</th>
									<th class="table-title">类别</th>
									<th class="table-type">流程</th>
									<th class="table-set">操作</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><input type="checkbox" /></td>
									<td>1</td>
									<td><a href="#">线性流程</a></td>
									<td><a href="#"><span class="am-icon-sign-out"></span>请假流程(按岗位控制走向)</a></td>
									<td>
										<div class="am-btn-toolbar">
											<div class="am-btn-group am-btn-group-xs">
												<button
													class="am-btn am-btn-default am-btn-xs am-text-secondary">
													<span class="am-icon-pencil-square-o"></span> 编辑
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-hide-sm-only">
													<span class="am-icon-copy"></span> 复制
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">
													<span class="am-icon-trash-o"></span> 删除
												</button>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td><input type="checkbox" /></td>
									<td>2</td>
									<td><a href="#">线性流程</a></td>
									<td><a href="#"><span class="am-icon-sign-out"></span>财务报销演示_12月29日09时30分</a></td>
									<td>
										<div class="am-btn-toolbar">
											<div class="am-btn-group am-btn-group-xs">
												<button
													class="am-btn am-btn-default am-btn-xs am-text-secondary">
													<span class="am-icon-pencil-square-o"></span> 编辑
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-hide-sm-only">
													<span class="am-icon-copy"></span> 复制
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">
													<span class="am-icon-trash-o"></span> 删除
												</button>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td><input type="checkbox" /></td>
									<td>3</td>
									<td><a href="#">同表单分合流</a></td>
									<td><a href="#"><span class="am-icon-sign-out"></span>同表单分合流(二)</a></td>
									<td>
										<div class="am-btn-toolbar">
											<div class="am-btn-group am-btn-group-xs">
												<button
													class="am-btn am-btn-default am-btn-xs am-text-secondary">
													<span class="am-icon-pencil-square-o"></span> 编辑
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-hide-sm-only">
													<span class="am-icon-copy"></span> 复制
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">
													<span class="am-icon-trash-o"></span> 删除
												</button>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td><input type="checkbox" /></td>
									<td>4</td>
									<td><a href="#">同表单分合流</a></td>
									<td><a href="#"><span class="am-icon-sign-out"></span>会议通知流程</a></td>
									<td>
										<div class="am-btn-toolbar">
											<div class="am-btn-group am-btn-group-xs">
												<button
													class="am-btn am-btn-default am-btn-xs am-text-secondary">
													<span class="am-icon-pencil-square-o"></span> 编辑
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-hide-sm-only">
													<span class="am-icon-copy"></span> 复制
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">
													<span class="am-icon-trash-o"></span> 删除
												</button>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td><input type="checkbox" /></td>
									<td>5</td>
									<td><a href="#">同表单分合流</a></td>
									<td><a href="#"><span class="am-icon-sign-out"></span>两个子线程分合流</a></td>
									<td>
										<div class="am-btn-toolbar">
											<div class="am-btn-group am-btn-group-xs">
												<button
													class="am-btn am-btn-default am-btn-xs am-text-secondary">
													<span class="am-icon-pencil-square-o"></span> 编辑
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-hide-sm-only">
													<span class="am-icon-copy"></span> 复制
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">
													<span class="am-icon-trash-o"></span> 删除
												</button>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td><input type="checkbox" /></td>
									<td>6</td>
									<td><a href="#">异表单分合流</a></td>
									<td><a href="#"><span class="am-icon-sign-out"></span>部门年计划流程</a></td>
									<td>
										<div class="am-btn-toolbar">
											<div class="am-btn-group am-btn-group-xs">
												<button
													class="am-btn am-btn-default am-btn-xs am-text-secondary">
													<span class="am-icon-pencil-square-o"></span> 编辑
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-hide-sm-only">
													<span class="am-icon-copy"></span> 复制
												</button>
												<button
													class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only">
													<span class="am-icon-trash-o"></span> 删除
												</button>
											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
						<div class="am-cf">
							共 6 条记录
							<div class="am-fr">
								<ul class="am-pagination">
									<li class="am-disabled"><a href="#">«</a></li>
									<li class="am-active"><a href="#">1</a></li>
									<li><a href="#">2</a></li>
									<li><a href="#">3</a></li>
									<li><a href="#">4</a></li>
									<li><a href="#">5</a></li>
									<li><a href="#">»</a></li>
								</ul>
							</div>
						</div>
						<hr />
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>