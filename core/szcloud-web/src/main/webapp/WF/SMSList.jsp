<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/head/head.jsp"%>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>系统消息</small>
			</div>
		</div>

		<!-- 数据 -->
		<div class="am-g">
			<div class="am-u-sm-12" id="listbox">
				<jsp:include page="SMSListInfo.jsp" />	
			</div>
		</div>

	</div>
</body>

</html>