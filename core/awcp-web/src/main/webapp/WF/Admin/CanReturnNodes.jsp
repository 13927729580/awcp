<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/head/head1.jsp"%>
<%
	CanRetuenNodesModel canReturnNodes = new CanRetuenNodesModel(request, response, basePath);
	canReturnNodes.init();
%>
</head>
<body class="easyui-layout" leftmargin="0" topmargin="0" style="font-size:smaller">
	<form method="post" action="" class="am-form" id="form1">
		<input type="hidden" id="FormHtml" name="FormHtml" value="">
		<div>
			<%=canReturnNodes.pub.ListToString() %>
		</div>
	</form>
</body>
<script type="text/javascript">
function onSave(){
	/* var size = $("input[name^='CB_']:checked").size();
	if(size == 0){
		alert("请选择要退回的节点！");
		return;
	}	 */
	
	var param = window.location.search;
	$("#FormHtml").val($("#form1").html());
	var url = "<%=basePath%>DES/CanReturnNodesSave.do"+param;
	$("#form1").attr("action", url);
	$("#form1").submit();
}
</script>
</html>