<%@page import="org.apache.http.HttpResponse"%>
<%@page import="org.apache.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="org.jflow.framework.common.model.TempLateModel" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	String load=request.getParameter("load");
	String path1=request.getSession().getServletContext().getRealPath("/DataUser/OfficeTemplate");
	String path2=request.getSession().getServletContext().getRealPath("/DataUser/OfficeOverTemplate");
	String path3=request.getSession().getServletContext().getRealPath("/DataUser/OfficeSeal");
	String path4=request.getSession().getServletContext().getRealPath("/DataUser/FlowDesc");
	TempLateModel tempLateModel=new TempLateModel(path1,path2,path3,path4,request,response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/jBox/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<link href="<%=basePath%>WF/Scripts/jBox/Skins/Blue/jbox.css"
	rel="stylesheet" type="text/css" />
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"
	type="text/javascript"></script>
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
</head>
<script type="text/javascript">
	$(function() {
		loadGrid();
	});

	function loadGrid() {
		$("#maingrid").datagrid({
			nowrap : true,
			fitColumns : true,
			fit : true,
			singleSelect : true,
			autoRowHeight : false,
			striped : true,
			collapsible : false,
			url : location.href + "&load=true",
			rownumbers : true,
			columns : [ [ {
				title : '名称 ',
				field : 'Name',
				width : 160,
				align : 'left',
				formatter : function(value, rec) {
					return rec.Name;
				}
			}, {
				title : '类型',
				field : 'Type'
			}, {
				title : '大小(KB)',
				field : 'Size'
			} ] ]
		});
	}
	function getSelected() {
		var row = $('#maingrid').datagrid('getSelected');
		return row;
	}

	function pageLoadding(msg) {
		$.jBox.tip(msg, 'loading');
	}
	function loaddingOut(msg) {
		$.jBox.tip(msg, 'success');
	}
</script>
<body>
	<form id="form1" runat="server">
		<div data-options="region:'center',iconCls:'icon-ok'">
			<div id='maingrid'>
				<%=tempLateModel.init(load) %>
			</div>
		</div>
	</form>
</body>
</html>