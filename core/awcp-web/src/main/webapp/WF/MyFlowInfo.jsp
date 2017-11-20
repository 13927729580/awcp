<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@ include file="/head/head.jsp"%>
<%
	MyFlowInfoModel flowInfoModel = new MyFlowInfoModel(request,
			response);
	flowInfoModel.initFlowInfo();
%>
<script type="text/javascript">
	if (window.opener && !window.opener.closed) {
		if (window.opener.name == "main") {
			window.opener.location.href = window.opener.location.href;
			window.opener.top.leftFrame.location.href = window.opener.top.leftFrame.location.href;
		}
	}
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content" >

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),
					WebUser.getName())%></strong>
			</div>
		</div>

		<div class="divCenter1">
				<section class="am-panel am-panel-default">
					<div class="am-panel-hd">
						<h3 class="am-panel-title">操作提示</h3>
					</div>
					<main class="am-panel-bd"> <%
 	if (WebUser.getIsWap()) {
 %>
					<fieldset class="am-box">
						<%=flowInfoModel.getMsg()%>
					</fieldset>
					<BR>
					<%
						} else {
					%>
					<fieldset class="am-box">
						<%=flowInfoModel.getMsg()%>
					</fieldset>
					<BR>
					<%
						}
					%> </main>
				</section>
			</div>
		</div>
</body>
</html>