<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/head/head.jsp"%>
<%
	String RefNo = request.getParameter("RefNo");
%>
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
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>设置</small>
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