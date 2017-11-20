<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/head/head.jsp"%>
<script type="text/javascript">
	function GenTemplete() {
		var title = $("#title").val();
		var num = $("#num").val();
		$("#form").attr("action","<%=basePath%>WF/ManagerModel.jsp?num="+num+"&title="+title);
		$("#form").submit();
	}
</script>
</head>
<body>
	<div class="am-u-md-6" style="width: 100%">
		<form class="am-form" id="form" method="post" action="">
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd">新建模块</div>

				<div class="am-g am-margin-top">
					<div class="am-u-sm-4 am-u-md-2 am-text-right">模块标题</div>
					<div class="am-u-sm-8 am-u-md-4">
						<input type="text" id="title" class="am-input-sm">
					</div>
					<div class="am-hide-sm-only am-u-md-6">标题名称用逗号分隔!注意逗号是中文逗号!</div>
				</div>
				<div class="am-g am-margin-top">
					<div class="am-u-sm-4 am-u-md-2 am-text-right">模块数量</div>
					<div class="am-u-sm-8 am-u-md-4">
						<input type="text" id="num" class="am-input-sm">
					</div>
					<div class="am-hide-sm-only am-u-md-6">数量与标题数量对应</div>
				</div>
			</div>
			<div class="am-margin">
				<A href="javascript:void(0)" onclick="GenTemplete()" id="ri"
					target="right"><button type="button"
						class="am-btn am-btn-success">生成模块</button></A>
			</div>
		</form>
	</div>
</body>
</html>