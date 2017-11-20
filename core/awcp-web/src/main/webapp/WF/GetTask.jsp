<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/head/head.jsp"%>
<%
	String PageSmall = null;
	String PageID = Glo.getCurrPageID();
	if (PageID.toLowerCase().contains("smallsingle")) {
		PageSmall = "SmallSingle";
	} else if (PageID.toLowerCase().contains("small")) {
		PageSmall = "Small";
	} else {
		PageSmall = "";
	}

	String workId = request.getParameter("WorkID") == null?"0":request.getParameter("WorkID");
	long WorkID = Long.valueOf(workId);
	String fk_node = request.getParameter("FK_Node") == null?"0":request.getParameter("FK_Node");
	int FK_Node = Integer.valueOf(fk_node);
	String toNode = request.getParameter("ToNode") == null?"0":request.getParameter("ToNode");
	int ToNode = Integer.valueOf(toNode);
	String FK_Flow = request.getParameter("FK_Flow") == null?"":request.getParameter("FK_Flow");
	String DoType = request.getParameter("DoType") == null?"":request.getParameter("DoType");

	GetTaskModel model = new GetTaskModel(basePath, WorkID, FK_Node,
			ToNode, FK_Flow, DoType, PageID, PageSmall);
	model.init();
%>
<script type="text/javascript">
function Tackback(fk_flow, fk_node, toNode, workid) {
    if (confirm('您确定要执行取回操作吗？')){
    	var url = '<%=basePath%>WF/GetTask.jsp?DoType=Tackback&FK_Flow='
					+ fk_flow + '&FK_Node=' + fk_node + '&ToNode=' + toNode
					+ '&WorkID=' + workid;
			window.location.href = url;
		}
		//  var v = window.showModalDialog(url, 'sd', 'dialogHeight: 400px; dialogWidth: 500px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
		// window.location.href = window.location.href;
	}
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>取回审批</small>
			</div>
		</div>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<form class="am-form">
					<%=model.Pub1%>
				</form>
				<!-- <ul class="am-pagination am-pagination-right">
					<li class="am-disabled"><a href="#">&laquo;</a></li>
					<li class="am-active"><a href="#">1</a></li>
					<li><a href="#">2</a></li>
					<li><a href="#">3</a></li>
					<li><a href="#">4</a></li>
					<li><a href="#">5</a></li>
					<li><a href="#">&raquo;</a></li>
				</ul> -->
			</div>
		</div>

	</div>
</body>
</html>