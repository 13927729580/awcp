<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.Port.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String name = request.getParameter("name");

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
<title></title>
<link rel="stylesheet" href="assets/css/amazeui.min.css" />
<link rel="stylesheet" href="assets/css/admin.css">
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/app.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
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
						<li><a href="<%=basePath%>UI/CC.jsp?name=<%=WebUser.getNo()%>"><span
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
				<form class="am-form">
					<table class="am-table am-table-striped am-table-hover table-main">
						<thead>
							<tr>
								<th class="table-check"><input type="checkbox" /></th>
								<TH class='table-id'>序</TH>
								<TH class='table-title'>是否读取</TH>
								<TH class='table-title'>标题</TH>
								<TH class='table-title'>流程</TH>
								<TH class='table-title'>发起时间</TH>
								<TH class='table-title'>发起人</TH>
								<TH class='table-title'>停留节点</TH>
								<TH class='table-title'>类型</TH>
							</tr>
						</thead>
						<tbody>
							<%
								DataTable dt = Dev2Interface.DB_GenerEmpWorksOfDataTable();
								String t = DataType.dateToStr(new Date(), "MMddhhmmss");
								if (null != dt) {
									int i = 0;
									for (DataRow dr : dt.Rows) {
										i++;
										String paras = dr.getValue("AtPara") == null ? "" : dr
												.getValue("AtPara").toString();
										String isRead = dr.getValue("isRead") == null ? "" : dr
												.getValue("isRead").toString();
							%>
							<tr>
								<td><input type="checkbox" /></td>
								<td><%=i%></td>
								<%
									if ("0".equals(isRead)) {
								%>
								<td><b>未阅</b></td>
								<td><a
									href="javascript:WinOpen('<%=basePath%>WF/MyFlow.jsp?FK_Flow=<%=dr.getValue("FK_Flow")%>&FK_Node=<%=dr.getValue("FK_Node")%>&WorkID=<%=dr.getValue("WorkID")%>&FID=<%=dr.getValue("FID")%>&IsRead=<%=isRead%>&Paras=<%=paras%>&T=<%=t%>')">
										<span class='am-icon-sign-out'></span><%=dr.getValue("Title")%>
								</a></td>
								<%
									} else {
								%>
								<td>已阅</td>
								<td><a
									href="javascript:WinOpen('<%=basePath%>WF/MyFlow.jsp?FK_Flow=<%=dr.getValue("FK_Flow")%>&FK_Node=<%=dr.getValue("FK_Node")%>&WorkID=<%=dr.getValue("WorkID")%>&FID=<%=dr.getValue("FID")%>&IsRead=<%=isRead%>&Paras=<%=paras%>&T=<%=t%>')">
										<span class='am-icon-sign-out'></span> <%=dr.getValue("Title")%>
								</a></td>
								<%
									}
								%>
								<td><%=dr.getValue("FlowName")%></td>
								<td><%=dr.getValue("RDT")%></td>
								<td><%=dr.getValue("StarterName")%></td>
								<td><%=dr.getValue("NodeName")%></td>
								<%
									if (paras.contains("IsCC")) {
								%>
								<td>抄送</td>
								<%
									} else {
								%>
								<td>发送</td>
								<%
									}
								%>
								</td>
							</tr>
							<%
								}
								}
							%>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>
	</div>
</body>
</html>