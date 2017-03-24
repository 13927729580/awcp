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

	<!-- 内容 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>发起列表</small>
			</div>
		</div>
		
		
		<!-- 表格数据 -->
		<div class="am-g">
      <div class="am-u-sm-12">
        <table class="am-table am-table-bd am-table-striped admin-content-table">
          <thead>
          <tr>
            <th>序</th><th>类别</th><th>流程</th><th>管理</th>
          </tr>
          </thead>
          <tbody>
          <tr><td>1</td><td>线性流程</td><td><a href="#"><span class="am-icon-sign-out"></span>请假流程(按岗位控制走向)</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>2</td><td>线性流程</td><td><a href="#"><span class="am-icon-sign-out"></span>财务报销演示_12月29日09时30分</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>3</td><td>同表单分合流</td><td><a href="#"><span class="am-icon-sign-out"></span>同表单分合流(二)</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>4</td><td>同表单分合流</td><td><a href="#"><span class="am-icon-sign-out"></span>会议通知流程</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>5</td><td>同表单分合流</td><td><a href="#"><span class="am-icon-sign-out"></span>两个子线程分合流</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>6</td><td>异表单分合流</td><td><a href="#"><span class="am-icon-sign-out"></span>部门年计划流程</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>7</td><td>异表单分合流</td><td><a href="#"><span class="am-icon-sign-out"></span>新航线考察流程</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>8</td><td>通讯基站工单流程</td><td><a href="#"><span class="am-icon-sign-out"></span>网优-父流程(省局监控分合流子流程)</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>9</td><td>跳转</td><td><a href="#"><span class="am-icon-sign-out"></span>跳转1-处理人就是提交人1-1</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>10</td><td>跳转</td><td><a href="#"><span class="am-icon-sign-out"></span>跳转1-处理人就是提交人1-2</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>11</td><td>跳转</td><td><a href="#"><span class="am-icon-sign-out"></span>跳转1-处理人就是提交人1-3</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>12</td><td>方向条件</td><td><a href="#"><span class="am-icon-sign-out"></span>测试方向条件-部门方向条件</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>13</td><td>方向条件</td><td><a href="#"><span class="am-icon-sign-out"></span>测试方向条件-开发者参数</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          <tr><td>14</td><td>方向条件</td><td><a href="#"><span class="am-icon-sign-out"></span>测试方向条件-按SQL</a></td>
            <td>
              <div class="am-dropdown" data-am-dropdown>
                <button class="am-btn am-btn-default am-btn-xs am-dropdown-toggle" data-am-dropdown-toggle><span class="am-icon-cog"></span> <span class="am-icon-caret-down"></span></button>
                <ul class="am-dropdown-content">
                  <li><a href="#">1. 编辑</a></li>
                  <li><a href="#">3. 删除</a></li>
                </ul>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
		
		
	</div>
</body>
</html>