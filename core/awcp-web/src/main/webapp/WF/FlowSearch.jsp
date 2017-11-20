<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<%@ include file="/head/head.jsp"%>
<%
	FlowSearchModel model = new FlowSearchModel(request, response);
	String htmlStr = model.init();
%>
<script type="text/javascript">
	$(function() {
		var userStyle = "ccflow默认";
		$('#winOpencss').attr('href', 'Style/FormThemes/WFWinOpen.css');
		$('#freeFormcss').attr('href', 'Style/FormThemes/Table0.css');

	});
	function WinOpen(url) {
        var newWindow = window.open(url, 'z', 'height=500,width=600,top=100,left=400,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
        newWindow.focus();
        return;
    }
	function Dtl(fk_flow) {
		WinOpen('DtlSearch.jsp?FK_Flow=' + fk_flow, 'ss');
	}
	function GroupBarClick(rowIdx) {
		var alt = document.getElementById('Img' + rowIdx).alert;
		var sta = 'block';
		if (alt == 'Max') {
			sta = 'block';
			alt = 'Min';
		} else {
			sta = 'none';
			alt = 'Max';
		}
		document.getElementById('Img' + rowIdx).src = '<%=basePath%>WF/Img/' + alt + '.gif';
		document.getElementById('Img' + rowIdx).alert = alt;
		var i = 0;
		for (i = 0; i <= 5000; i++) {
			if (document.getElementById(rowIdx + '_' + i) == null)
				continue;
			if (sta == 'block') {
				document.getElementById(rowIdx + '_' + i).style.display = '';
			} else {
				document.getElementById(rowIdx + '_' + i).style.display = sta;
			}
		}
	}
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>流程查询</small>
			</div>
		</div>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<form class="am-form">
				<%=htmlStr %>
				</form>
			</div>
		</div>

	</div>
	</div>
</body>
</html>