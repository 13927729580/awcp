<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.Port.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path;
	String RefNo = request.getParameter("RefNo");
	String name = WebUser.getNo();

	try {
		Emp emp = new Emp(name);
		//ContextHolderUtils.setResponse(response);
		WebUser.SignInOfGener(emp);
	} catch (Exception e) {
		response.sendRedirect(basePath + "WF/Login.jsp?errNo=1");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>设置</title>
<link rel="stylesheet" href="assets/css/amazeui.min.css" />
<link rel="stylesheet" href="assets/css/admin.css">
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/app.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
<script language=javascript>
	function DoAutoTo(fk_emp, empName) {
		if (window.confirm('您确定要把您的工作授权给[' + fk_emp + ']吗？') == false)
			return;
		var url = 'Do.jsp?DoType=AutoTo&FK_Emp=' + fk_emp;
		WinShowModalDialog(url, '');
		alert('授权成功，请别忘记收回。');
		window.location.href = 'Tools.jsp';
	}

	function ExitAuth(fk_emp) {
		if (window.confirm('您确定要退出授权登陆模式吗？') == false)
			return;

		var url = 'Do.jsp?DoType=ExitAuth&FK_Emp=' + fk_emp;
		WinShowModalDialog(url, '');
		window.location.href = 'Tools.jsp';
	}

	function TakeBack(fk_emp) {
		if (window.confirm('您确定要取消对[' + fk_emp + ']的授权吗？') == false)
			return;

		var url = 'Do.jsp?DoType=TakeBack';
		WinShowModalDialog(url, '');
		alert('您已经成功的取消。');
		window.location.reload();
	}

	function LogAs(fk_emp) {
		if (window.confirm('您确定要以[' + fk_emp + ']授权方式登陆吗？') == false)
			return;

		var url = 'Do.jsp?DoType=LogAs&FK_Emp=' + fk_emp;
		WinShowModalDialog(url, '');
		alert('登陆成功，现在您可以以[' + fk_emp + ']处理工作。');
		window.location.href = 'EmpWorks.jsp';
	}

	function CHPass() {
		var url = 'Do.jsp?DoType=TakeBack';
		// WinShowModalDialog(url,'');
		alert('密码修改成功，请牢记您的新密码。');
	}
	function loadPage(id, url) {
		$("#" + id).load(url);
	}
</script>
</head>
<body>
	<!-- 头 -->
	<header class="am-topbar admin-header">

	<div class="am-topbar-brand">
		<strong>JFlow</strong><h3>后台管理</h>
	</div>
	<div class="am-collapse am-topbar-collapse" id="topbar-collapse">
		<ul
			class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
			<li><a href="#"><span class="am-icon-user"></span> <%=WebUser.getName()%>
					▼</a></li>
			<!-- 必选加 am-dropdown，data-am-dropdown，超链接中必须加am-dropdown-toggle，data-am-dropdown-toggle-->
			<li><a href="#"><span class="am-icon-power-off"></span> 退出</a></li>
		</ul>
	</div>

	</header>

	<!-- 左侧菜单 -->
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
						<li><a
							href="<%=basePath%>UI/Default.jsp?name=<%=WebUser.getNo()%>"
							class="am-cf"><span class="am-icon-file"></span> 发起</a></li>
						<li><a
							href="<%=basePath%>UI/Todolist.jsp?name=<%=WebUser.getNo()%>"><span
								class="am-icon-star"></span> 代办(<%=Dev2Interface.getTodolist_EmpWorks()%>)</a></li>
						<li><a
							href="<%=basePath%>UI/Runing.jsp?name=<%=WebUser.getNo()%>"
							target=""><span class="am-icon-pencil-square-o"></span> 在途(<%=Dev2Interface.getTodolist_Runing()%>)</a></li>
						<li><a
							href="<%=basePath%>UI/CC.jsp?name=<%=WebUser.getNo()%>"><span
								class="am-icon-puzzle-piece"></span> 抄送(<%=Dev2Interface.getTodolist_CCWorks()%>)</a></li>
						<li><a href="<%=basePath%>UI/Batch.jsp"><span
								class="am-icon-th"></span> 批处理</a></li>
						<%
							if (Glo.getIsEnableTaskPool()) {
						%>
						<li><a href="<%=basePath%>UI/TaskPoolSharing.jsp"><span
								class="am-icon-bookmark"></span> 共享任务</a></li>
						<%-- d.add(106, 100, "共享任务", "<%=basePath%>WF/TaskPoolSharing.jsp"); --%>
						<%
							}
						%>
						<li><a href="<%=basePath%>UI/HungUpList.jsp"><span
								class="am-icon-file"></span> 挂起(<%=Dev2Interface.getTodolist_HungUpNum()%>)</a></li>
						<li><a href="<%=basePath%>UI/FlowSearch.jsp"><span
								class="am-icon-table"></span> 查询</a></li>
						<li><a href="<%=basePath%>UI/GetTask.jsp"><span
								class="am-icon-pencil-square-o"></span> 取回审批</a></li>
						<li><a href="<%=basePath%>UI/Complete.jsp"><span
								class="am-icon-star"></span> 已完成(<%=Dev2Interface.getTodolist_Complete()%>)</a></li>
						<li><a href="<%=basePath%>UI/Emps.jsp"><span
								class="am-icon-user"></span> 成员</a></li>
						<li><a href="<%=basePath%>UI/Tools.jsp"><span
								class="am-icon-cog"></span> 设置</a></li>
					</ul></li>
			</ul>
		</div>
	</div>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>代办列表</small>
			</div>
		</div>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<form class="am-form" method="post" action="Tools.jsp" id="form1"
					onkeypress="NoSubmit(event);">
					<table class="am-table am-table-striped am-table-hover table-main"
						border=0 width='100%' align='left'>
						<tr>
							<td colspan="20">系统设置</td>
						</tr>
						<tr>
							<td valign=top width='20%' align='center'>
								<%
									BP.WF.XML.Tools tools = new BP.WF.XML.Tools();
									tools.RetrieveAll();

									if (tools.size() == 0)
										return;
									String refno = RefNo;
									System.out.println(refno);
									if (refno == null)
										refno = "Per";
								%>
								<table
									class="am-table am-table-striped am-table-hover table-main"
									border="0" width="100%" align="left" id="ddd">
									<tr>
										<%
											System.out.println(WebUser.getIsWap());
											if (WebUser.getIsWap()) {
										%>
										<th><a href='Home.jsp'><img src='/WF/Img/Home.gif'
												border="0" />Home</a></th>
										<%
											}
										%>
									</tr>
									<tr>
										<td valign="top">
											<ul>
												<%
													for (BP.WF.XML.Tool tool : BP.WF.XML.Tools.convertTools(tools)) {
														if (tool.getNo().equals(refno)) {
												%>
												<li><a href="#"
													onclick="loadPage('aaa','ToolsWap.jsp?RefNo=<%=tool.getNo()%>')"><b><%=tool.getName()%></b></a></li>
												<%
													} else {
												%>
												<li><a href="#"
													onclick="loadPage('aaa','ToolsWap.jsp?RefNo=<%=tool.getNo()%>')"><%=tool.getName()%></a>

												</li>
												<%
													}
													}

													if (WebUser.getNo().equals("admin")) {
												%>
												<li><a href="#"
													onclick="loadPage('aaa','ToolsWap.jsp?RefNo=AdminSet')">网站设置</a></li>
												<%
													}
												%>
											</ul>
										</td>
									</tr>
								</table>
							</td>
							<td valign=top align='left' width='80%' id="aaa"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	</div>
</body>
</html>