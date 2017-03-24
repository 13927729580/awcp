<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.Graphics"%>
<%@page import="BP.Port.*"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.DTS.GenerSiganture"%>
<%@page import="BP.WF.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path+"/";
	String DoType = request.getParameter("DoType");
	// 	if(DoType==null){
	// 		DoType="1";
	// 	}
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
<title>通讯录</title>
<link rel="stylesheet" href="assets/css/amazeui.min.css" />
<link rel="stylesheet" href="assets/css/admin.css">
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/app.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
<script type="text/javascript">
	function DoUp(no, keys) {
		var url = "Do.jsp?DoType=EmpDoUp&RefNo=" + no + '&dt=' + keys;
		val = window
				.showModalDialog(
						url,
						'f4',
						'dialogHeight: 5px; dialogWidth: 6px; dialogTop: 100px; dialogLeft: 100px; center: yes; help: no');
		window.location.href = window.location.href;
		return;
	}
	function DoDown(no, keys) {
		var url = "Do.jsp?DoType=EmpDoDown&RefNo=" + no + '&sd=' + keys;
		val = window
				.showModalDialog(
						url,
						'f4',
						'dialogHeight: 5px; dialogWidth: 6px; dialogTop: 100px; dialogLeft: 100px; center: yes; help: no');
		window.location.href = window.location.href;
		return;
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
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>成员列表</small>
			</div>
		</div>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<form class="am-form">
					<%
						if (WebUser.getIsWap()) {
					%>
					<table class="am-table am-table-striped am-table-hover table-main" align="left" cellpadding="2"
						cellspacing="2">
						<tr>
							<td colspan="4" align="left" class="FDesc"><a
								href='Home.aspx'><img src='Img/Home.gif' border=0 />Home</a> -
								成员</td>
						</tr>
						<%
							BP.Port.Depts depts = new BP.Port.Depts();
								depts.RetrieveAllFromDBSource();

								BP.WF.Port.WFEmps emps = new BP.WF.Port.WFEmps();
								emps.RetrieveAllFromDBSource();

								int idx = 0;
								for (BP.Port.Dept dept : Depts.convertDepts(depts)) {
						%>
						<tr>
							<td colspan="4"><%=dept.getName()%></td>
						</tr>
						<%
							for (BP.WF.Port.WFEmp emp : BP.WF.Port.WFEmps
											.convertWFEmps(emps)) {
										if (emp.getFK_Dept() != dept.getNo())
											continue;
										idx++;
						%>
						<tr>
							<td><%=idx%></td>
							<td><%=emp.getName()%></td>
							<td><%=emp.getTel()%></td>
							<td><%=emp.getStas()%></td>
						</tr>
						<%
							}
								}
						%>
					</table>
					<%
						return;
						}
						String sql = "SELECT a.No,a.Name, b.Name as DeptName FROM Port_Emp a, Port_Dept b WHERE a.FK_Dept=b.No ORDER BY a.FK_Dept ";
						DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);

						BP.WF.Port.WFEmps emps = new BP.WF.Port.WFEmps();
						if (DoType != null) {
							emps.RetrieveAllFromDBSource();
						} else {
							emps.RetrieveAllFromDBSource();
						}
					%>
					<table width="100%" class="am-table am-table-striped am-table-hover table-main" align="left" cellpadding="2"
						cellspacing="2">
						<caption>成员</caption>
						<tr>
							<th class="table-title">IDX</th>
							<th class="table-title">部门</th>
							<th class="table-title">人员</th>
							<th class="table-title">Tel</th>
							<th class="table-title">Email</th>
							<th class="table-title">岗位</th>
							<th class="table-title">签名</th>
							<%
								if (WebUser.getNo().equals("admin")) {
							%>
							<th class="table-title">顺序</th>
							<%
								}
							%>
							<%
								if (DoType != null) {
									BP.WF.Port.WFEmp.DTSData();
									//this.GenerAllImg();
									BP.WF.Port.WFEmps empWFs = new BP.WF.Port.WFEmps();
									empWFs.RetrieveAll();

									for (BP.WF.Port.WFEmp emp : BP.WF.Port.WFEmps
											.convertWFEmps(empWFs)) {
										File file = new File(
												BP.Sys.SystemConfig.getPathOfDataUser()
														+ "Siganture\\" + emp.getNo() + ".jpg");
										File fl = new File(BP.Sys.SystemConfig.getPathOfDataUser()
												+ "Siganture\\" + emp.getName() + ".jpg");
										System.out.println(BP.Sys.SystemConfig.getPathOfDataUser()
												+ "Siganture\\" + emp.getNo() + ".jpg");
										if (file.exists() || fl.exists()) {
											continue;
										}
										String path1 = BP.Sys.SystemConfig.getPathOfDataUser()
												+ "Siganture\\T.jpg";
										String pathMe = BP.Sys.SystemConfig.getPathOfDataUser()
												+ "Siganture\\" + emp.getNo() + ".jpg";
										GenerSiganture.fileChannelCopy(
												new File(BP.Sys.SystemConfig.getPathOfDataUser()
														+ "Siganture\\Templete.jpg"),
												new File(path));
										GenerSiganture.writeImageLocal(
												pathMe,
												GenerSiganture.modifyImage(
														GenerSiganture.loadImageLocal(path1),
														emp.getName(), 90, 90));
										GenerSiganture.fileChannelCopy(new File(pathMe), new File(
												BP.Sys.SystemConfig.getPathOfDataUser()
														+ "Siganture\\" + emp.getName() + ".jpg"));
									}
								}
							%>
						</tr>
						<%
							//String keys = DateTime.Now.toString("MMddhhmmss");
							String keys = DataType.dateToStr(new Date(), "MMddhhmmss");
							String deptName = null;
							int idx = 0;
							EmpStations ess = new EmpStations();
							ess.RetrieveAll();
							for (DataRow dr : dt.Rows) {
								String fk_emp = dr.getValue("No").toString();
								if (fk_emp.equals("admin"))
									continue;
								idx++;
								if (!dr.getValue("DeptName").toString().equals(deptName)) {
									deptName = dr.getValue("DeptName").toString();
						%>
						<tr>
							<td><%=idx%></td>
							<td><%=deptName%></td>
							<%
								} else {
							%>
						
						<tr>
							<td><%=idx%></td>
							<td></td>
							<%
								}
							%>
							<td><%=fk_emp%>-<%=dr.getValue("Name")%></td>
							<%
								//BP.WF.Port.WFEmp emp = emps.GetEntityByKey(fk_emp) as BP.WF.Port.WFEmp;
									BP.WF.Port.WFEmp emp = (BP.WF.Port.WFEmp) emps
											.GetEntityByKey(fk_emp);
									if (emp != null) {
							%>
							<td></td>
							<td></td>
							<%
								String stas = "";
										for (EmpStation es : EmpStations.convertEmpStations(ess)) {
											if (!es.getFK_Emp().equals(emp.getNo()))
												continue;
											stas += es.getFK_StationT() + ",";
										}
							%>
							<td><%=stas%></td>
							<%
								} else {
							%>
							<td></td>
							<td></td>
							<td></td>
							<%
								}
							%>
							<td><img
								src='<%=basePath%>DataUser/Siganture/<%=fk_emp%>.jpg'
								border="1"
								onerror="this.src='<%=basePath%>DataUser/Siganture/UnName.jpg'" /></td>
							<%
								if (WebUser.getNo().equals("admin")) {
							%>
							<td><a
								href="javascript:DoUp('<%=emp.getNo()%>','<%=keys%>')"><img
									src='<%=basePath%>WF/Img/Btn/Up.GIF' border="0" /></a>-<a
								href="javascript:DoDown('<%=emp.getNo()%>','<%=keys%>')"><img
									src='<%=basePath%>WF/Img/Btn/Down.gif' border="0" /></a></td>
							<%
								}
							%>
						</tr>
						<%
							}
						%>
					</table>
				</form>
			</div>
		</div>
	</div>
	</div>
</body>
</html>