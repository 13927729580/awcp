<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@ page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>页面动作编辑页面</title>
<%@ include file="/resources/include/common_css.jsp"%>

</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
				<li><i class="icon-location-arrow icon-muted"></i></li>
				<li><a href="#">首页</a></li>
				<li><a href="#">我的应用产品</a></li>
				<li class="active">页面动作编辑</li>
			</ul>
		</div>
		<div class="row" id="dataform">
			<form class="form-horizontal" id="actForm"
				action="<%=basePath%>fd/act/save.do" method="post">
				<c:if test="${result!=null&&''!=result}">
					<span style="color: red">(${result})</span>
				</c:if>
				<%@ include file="comms/basicInfo.jsp"%>
				<%@ include file="comms/scripts.jsp"%>
				<%@ include file="comms/confirm.jsp"%>
			</form>
		</div>


	</div>

	<%@ include file="/resources/include/common_form_js.jsp"%>
	<script src="<%=basePath%>resources/scripts/map.js"></script>
	<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script src="<%=basePath%>formdesigner/page/script/act.js"></script>
	<script>
		$(":radio[name='pattern']").click(function() {
			$("#scriptdiv").addClass("hidden");
			$("#selectworkflowdiv").addClass("hidden");
			if ("2" == $(this).val()) {
				$("#scriptdiv").removeClass("hidden");
				$("#workflowId option").eq(0).attr("selected", "selected");
			} else if ("3" == $(this).val()) {
				$("#selectworkflowdiv").removeClass("hidden");
				$("#startWorkflowScript").html(" ");
				$("#startWorkflowScript").val(" ");
			} else if ("1" == $(this).val()) {
				$("#startWorkflowScript").html(" ");
				$("#startWorkflowScript").val(" ");
				$("#workflowId option").eq(0).attr("selected", "selected");
			}

		});
		$(document).ready(function() {
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			var radio = $(":radio[name='pattern']:checked").val();
			if(empty(radio)){
				$(":radio[name='pattern']:first").prop("checked","checked");
			}
			if(dialog){
				$("#dynamicPageId").attr("disabled","disabled");
			}
            $.formValidator.initConfig({formID:"actForm",debug:false});
            $("#buttonGroup").formValidator({onFocus:"请输入按钮组"}).inputValidator({min:1,onError:"必填"});
            $("#name").formValidator({onFocus:"请输入名称"}).inputValidator({min:1,onError:"必填"});
            $("#order").formValidator({onFocus:"请输入序号"}).inputValidator({min:1,onError:"必填"});
			$("#saveBtn").click(function() {
				$("#actType").removeAttr("disabled");
				if (dialog) {
					$("#dynamicPageId").removeAttr("disabled");
					var data = $("#actForm").serializeJSON();
					data.dynamicPageName=$("#dynamicPageId option:selected").text();
					var buttons = new Array();
					$(":checkbox[name='buttons']:checked").each(function() {
						buttons.push($(this).val());
					});
					data['buttons'] = buttons.join(",");
					$.ajax({
						type : "POST",
						async : false,
						url : "fd/act/saveByAjax.do",
						data : data,
						success : function(ret) {
							var json = eval(ret);
							dialog.close(json);
							dialog.remove();
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							alert(errorThrown);
						}
					});
				} else {
					var name=$("#dynamicPageId option:selected").text();
					$("#actForm").append("<input type='hidden' name='dynamicPageName' value='"+name+"'/>");
					$("#actForm").submit();
				}
				return false;
			});
		});
	</script>
</body>
</html>
