<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>菜单编辑页面</title>
<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="saveComponent"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<div class="row" id="tab">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#tab1" data-toggle="tab">基本</a></li>
					<li class=""><a href="#tab2" data-toggle="tab">按钮</a></li>
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1"><%@ include file="/formdesigner/page/component/rowOperation/basicInfo.jsp" %></div>
				<div class="tab-pane" id="tab2"><%@ include file="/formdesigner/page/component/rowOperation/buttons.jsp" %></div>
			</div>
		</form>
	</div>

	<%@ include file="/resources/include/common_form_js.jsp" %>
	<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script src="<%=basePath%>/resources/plugins/echarts-2.2.1/src/component.js"></script>
	<script src="<%=basePath%>/formdesigner/scripts/form.cpcommons.js"></script>
	<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
			initializeDocument("${componentType}","${_ComponentTypeName[componentType]}");
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			if(dialog != null){
				setTimeout(function(){
					dialog.width(1400);
					dialog.height($(top).height()-93); 
					dialog.reset();     // 重置对话框位置
				},500);	
			}
			
			$("#addButton").click(function(){
				var selects = "<select class='form-control' name='buttons[][color]'>" + 
				  			"<option value='btn-default'>白</option>" + 
				 	 		"<option value='btn-primary'>蓝</option>" + 
				  			"<option value='btn-success'>绿</option>" + 
				  			"<option value='btn-info'>青</option>" + 
				  			"<option value='btn-danger'>红</option>" +
				  			"<option value='btn-warning'>橙</option>" + 							 
				  			"</select>";
				var str = "<tr>";			
				str += "<td style='padding:0px;'><input class='form-control' name='buttons[][title]' type='text' /></td>";
				str += "<td style='padding:0px;'><input class='form-control' name='buttons[][className]' type='text' /></td>";
				str += "<td style='padding:0px;'>" + selects + "</td>";
				str += "<td style='padding:0px;'><input class='form-control' name='buttons[][order]' type='text' /></td>";
				str += "<td style='padding:0px;'><textarea rows='4' class='form-control' name='buttons[][codes]'></textarea></td>";
				str += "<td style='padding:0px;'><textarea rows='4' class='form-control' name='buttons[][severCodes]'></textarea></td>";
				str += "<td style='padding:0px;'><textarea rows='4' class='form-control' name='buttons[][hideCodes]'></textarea></td>";
				str += "<td style='text-align:center;padding:0px;'><a href='javascript:void(0)' class='removeTr'>删除</a></td>";
				str += "</tr>";
				$("#buttonsBody").append(str);
			});
			
			$("#buttonsBody").on("click",".removeTr",function(){
				$(this).parents("tr").remove();
			});
		});	
	</script>
</body>
</html>