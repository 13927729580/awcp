

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
 <head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<%@ include file="../../resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
<link rel="stylesheet" href="<%=basePath%>/resources/plugins/select2/select2.css"/>
<link rel="stylesheet" href="<%=basePath%>/resources/plugins/select2/select2-bootstrap.css"/>
<title>commondwords</title>
</head>
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/platform.twoWay.js"></script><!-- 流程选择 -->
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/platform.selectPanel.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zui/assets/kindeditor/kindeditor-min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zui/assets/kindeditor/lang/zh_CN.js"></script>
<body>
	<table class="table datatable table-bordered">
			<thead>
				<tr>
					<th class="hidden"></th>
					<th  class="text-ellipsis" data-flex="false" data-width="">处理步骤</th>
					<th  class="text-ellipsis" data-flex="false" data-width="">处理人</th>
					<th  class="text-ellipsis" data-flex="false" data-width="">收到时间</th>
					<th  class="text-ellipsis" data-flex="false" data-width="">处理时间</th>
					<th  class="text-ellipsis" data-flex="false" data-width="">状态</th>
					<th  class="text-ellipsis" data-flex="true" data-width="">处理意见</th>
					<th  class="text-ellipsis" data-flex="true" data-width="">送达步骤</th>
					<th  class="text-ellipsis" data-flex="true" data-width="">发送人</th>
				</tr>
			</thead>
	</table>
	<script type="text/javascript">
		$('table.datatable').datatable();
	</script>
</body>
</html>