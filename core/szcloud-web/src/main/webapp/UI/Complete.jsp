<%@page import="BP.WF.Glo"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.*"%>
<%@page import="BP.Port.*"%>
<%@page import="BP.Sys.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";

	DataTable dt = null;
	String isAddCC = SystemConfig.getAppSettings().get("IsAddCC") == null ? ""
	: SystemConfig.getAppSettings().get("IsAddCC").toString();
	if ("1".equals(isAddCC)) {
		dt = Dev2Interface.DB_FlowComplete();
	} else {
		dt = Dev2Interface.DB_FlowCompleteAndCC();
	}
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
<title>完成列表</title>
<link rel="stylesheet" href="assets/css/amazeui.min.css" />
<link rel="stylesheet" href="assets/css/admin.css">
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/app.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
<style>
.TTD {
	word-wrap: break-word;
	　　word-break: normal;
}

.Icon {
	width: 16px;
	height: 16px;
}
</style>
<script type="text/javascript">
function NoSubmit(ev) {
    //if (window.event.srcElement.tagName == "TEXTAREA")return true;

    if (ev.keyCode == 13) {
        window.event.keyCode = 9;
        ev.keyCode = 9;
        return true;
    }
    return true;
}
</script>
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
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>完成列表</small>
			</div>
		</div>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<form class="am-form" method="post" id="form1">
					<table class="am-table am-table-striped am-table-hover table-main"
						align=center>
						<tr>
							<td colspan="7">发起列表</td>
						</tr>
						<thead>
							<tr>
								<TH class='table-title' nowrap="nowrap" width="5%">序</TH>
								<TH class='table-title' nowrap="nowrap">标题</TH>
								<TH class='table-title' nowrap="nowrap">流程</TH>
								<TH class='table-title' nowrap="nowrap">发起时间</TH>
								<TH class='table-title' nowrap="nowrap">发起人</TH>
								<TH class='table-title' nowrap="nowrap">停留节点</TH>
								<TH class='table-title' nowrap="nowrap">操作</TH>
							</tr>
						</thead>
						<tbody>
							<%
								if(null != dt){
																		int i = 0;
																		for(DataRow dr : dt.Rows){ 
																			i++;
																			String workid = dr.getValue("WorkID")==null?"":dr.getValue("WorkID").toString();
																			String fk_flow = dr.getValue("FK_Flow")==null?"":dr.getValue("FK_Flow").toString();
																			
																			String run = dr.getValue("Type")==null?"":dr.getValue("Type").toString();
							%>
							<tr>
								<td><%=i%></td>
								<%
									if("RUNNING".equals(run)){
								%>
								<td class="TTD"><a
									href="javascript:WinOpen('<%=basePath%>WF/WFRpt.jsp?FK_Flow=<%=fk_flow%>&WorkID=<%=workid%>')">
										<span class='am-icon-sign-out'>
										<%=dr.getValue("Title")%>
								</a></td>
								<%
									}else{
								%>
								<td class="TTD"><a
									href='javascript:WinOpenCC("<%=dr.getValue("MyPk")%>","<%=fk_flow%>","<%=dr.getValue("FK_Node")%>","<%=workid%>","<%=dr.getValue("FID")%>","<%=dr.getValue("Sta")%>")'>
										<span class='am-icon-sign-out'>
										<%=dr.getValue("Title")%></a></td>
								<%
									}
								%>
								<td><%=dr.getValue("FlowName")%></td>
								<td><%=dr.getValue("RDT")%></td>
								<td><%=dr.getValue("StarterName")%></td>
								<td><%=dr.getValue("NodeName")%></td>
								<%
									if("RUNNING".equals(run)){
								%>
								<td class="TTD"><a
									href="javascript:WinOpen('<%=basePath%>WF/MyFlow.jsp?FK_Flow=<%=fk_flow%>&CopyFormWorkID=<%=workid%>&CopyFormNode=<%=dr.getValue("FK_Node")%>')">
										<span class='am-icon-sign-out'>
										Copy发起流程
								</a></td>
								<%
									}else{
								%>
								<td>&nbsp;</td>
								<%
									}
								%>
							</tr>
							<%
								}}
							%>
						
						<tbody>
					</table>
				</form>
			</div>
		</div>

	</div>
	</div>
</body>
<script type="text/javascript">
	function WinOpenCC(ccid, fk_flow, fk_node, workid, fid, sta) {
        var url = '';
        if (sta == '0') {
            url = '<%=basePath%>WF/Do.jsp?DoType=DoOpenCC&FK_Flow=' + fk_flow + '&FK_Node=' + fk_node + '&WorkID=' + workid + '&FID=' + fid + '&Sta=' + sta + '&MyPK=' + ccid + "&T=" + dateNow;
        }
        else {
            url = '<%=basePath%>
	WF/WorkOpt/OneWork/Track.jsp?FK_Flow='
					+ fk_flow + '&FK_Node=' + fk_node + '&WorkID=' + workid
					+ '&FID=' + fid + '&Sta=' + sta + '&MyPK=' + ccid + "&T="
					+ dateNow;
		}
		//window.parent.f_addTab("cc" + fk_flow + workid, "抄送" + fk_flow + workid, url);
		var newWindow = window.open(url, 'z');
		newWindow.focus();
	}
</script>
</html>