<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="device-mobile screen-tablet">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
<meta name="renderer" content="webkit">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet"
	href="<%=basePath%>base/resources/zui/dist/css/zui_define.css">
<link rel="stylesheet"
	href="<%=basePath%>resources/styles/main.css">
<link rel="stylesheet"
	href="<%=basePath%>base/resources/artDialog/css/ui-dialog.css">
<link rel="stylesheet"
	href="<%=basePath%>resources/plugins/select2/select2.css">
<link rel="stylesheet"
	href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css">
<link rel="stylesheet"
	href="<%=basePath%>resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css">
<link rel="stylesheet"
	href="<%=basePath%>resources/styles/content/uploader.css">
<link rel="stylesheet"
	href="<%=basePath%>resources/styles/content/layout.css">
<style>
label.control-label {
	font-weight: bold;
}

.form-condensed .form-group {
	margin-bottom: 0px;
}

.check-all {
	text-align: center;
}

.hides {
	display: block;
	width: 0;
	height: 0;
	overflow: hidden;
}
</style>
<!--[if lt IE 9]>
	  <script src="<%=basePath%>resources/plugins/zui/assets/html5shiv.js"></script>
	  <script src="<%=basePath%>resources/plugins/zui/assets/respond.js"></script>
        
	<![endif]-->
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
<style id="poshytip-css-tip-yellowsimple" type="text/css">
div.tip-yellowsimple {
	visibility: hidden;
	position: absolute;
	top: 0;
	left: 0;
}

div.tip-yellowsimple table.tip-table, div.tip-yellowsimple table.tip-table td
	{
	margin: 0;
	font-family: inherit;
	font-size: inherit;
	font-weight: inherit;
	font-style: inherit;
	font-variant: inherit;
	vertical-align: middle;
}

div.tip-yellowsimple td.tip-bg-image span {
	display: block;
	font: 1px/1px sans-serif;
	height: 10px;
	width: 10px;
	overflow: hidden;
}

div.tip-yellowsimple td.tip-right {
	background-position: 100% 0;
}

div.tip-yellowsimple td.tip-bottom {
	background-position: 100% 100%;
}

div.tip-yellowsimple td.tip-left {
	background-position: 0 100%;
}

div.tip-yellowsimple div.tip-inner {
	background-position: -10px -10px;
}

div.tip-yellowsimple div.tip-arrow {
	visibility: hidden;
	position: absolute;
	overflow: hidden;
	font: 1px/1px sans-serif;
}
</style>
</head>
<body id="main">
	<div class="container">
		<div class="row" id="buttons">
			<button id="saveBtn" title="" class=" btn   btn-default" target="">
				<i class="icon-file"></i> 保存
			</button>
		</div>
		<div class="row" id="newTitle">
			<label class="control-label "style="width: 100%; text-align: center; font-weight: 800; font-size: 24px;">系统配置 </label>
		</div>
		<div class="row">
			<form class="form-horizontal form-condensed" id="groupForm"	role="customForm" method="post" style="border: #333 1px solid">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table-c">
					<tbody>
						<tr>
							<td class="col-xs-6" colspan="6" style="text-align: center;">
								<label class="control-label " style="text-align: center;" id="45dc106c-dcb5-484e-8d63-b06d371a68ce" name="eb40bd561facd7ff9248c65c8bc669a6"> 系统名称 </label>
							</td>
							<td class="col-xs-6" colspan="6" style="text-align: center;">
								<input type="hidden" name="sysId" value="${sysId}" id="sysId">
								<input type="hidden" name="id" value="${id}" id="id">  
								<input type="text" class="form-control" style="" name="sysName" value="${sysName}" id="sysName" title="">
							</td>
						</tr>
						<tr>
							<td class="col-xs-6" colspan="6" style="text-align: center;">
								<label class="control-label " style="text-align: center;" id="2e168c83-7889-49b6-890b-6dcadd9aaea2"	name="261bd3fc08c3501c1671a098977c2b76"> 系统logo </label>
							</td>
							<td class="col-xs-6" colspan="6" style="text-align: center;">
								<div class="uploadPreview">
									<input type="hidden" class="photo" name="d3a96d40abbb0a6817efa58f4407528e" value="">
	      							<div class="photo-con"><center><img id="5555b3b8-f36a-4d15-b33d-4dd25c825673_Img" alt="点击选择图片" style="width: 120px; height: 120px;"></center></div>
	      							<input type="file" id="5555b3b8-f36a-4d15-b33d-4dd25c825673" name="5555b3b8-f36a-4d15-b33d-4dd25c825673">
	      							<div class="photo-btn"><a class="btn submit" href="javascript:;">上传</a><a class="btn delete" href="javascript:;">删除</a></div>
								</div>
							</td>

						</tr>

						
					</tbody>
				</table>
			</form>
		</div>
	</div>

	<script type="text/javascript"
		src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>base/resources/zui/dist/js/zui.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>base/resources/artDialog/dist/dialog-plus-min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/tips/jquery.poshytip.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/formValidator4.1.0/formValidatorRegex.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/ajaxfileupload.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/uploadPreview.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/map.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/common.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/platform.document.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/webuploader-0.1.5/dist/webuploader.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/uploader.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/select2/select2.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/ntkoWord.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/highChart/highcharts.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/highChart/exporting.js"></script>
	<script>
		function initTable() {
			if ($("div.datatable").size() > 0) {
				$("div.datatable").remove();
			}
			$("table.table").removeAttr("style");
			var count = 0;//默认选择行数为0

			$("table.datatable").datatable(
					{
						checkable : true,
						datareload : true,
						checksChanged : function(event) {
							this.$table.find("tbody tr").find("input#boxs")
									.removeAttr("name");
							var checkArray = event.checks.checks;
							count = checkArray.length;
							for (var i = 0; i < count; i++) {//给隐藏数据机上name属性
								this.$table.find("tbody tr").eq(checkArray[i])
										.find("input#boxs").attr("name",
												"_selects");
							}
						}

					});
			$("#datatable").find("div.datatable-rows").find("table tr td")
					.each(function() {
						$(this).css("text-align", "left");
					});
			$("#datatable").find("div.datatable-rows").find(
					"table tr td:first-child").each(function() {
				$(this).css("text-align", "center");
			});

		}
	</script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>base/resources/zui/assets/kindeditor/kindeditor-min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>base/resources/zui/assets/kindeditor/lang/zh_CN.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/zTree_v3/zTree.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/dynamicSelect.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/workFlow.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/plugins/commondWords/commondWords.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/defineNote.js"></script>


	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/platform.dataGrid.js"></script>

	<script type="text/javascript">
		var basePath = '<%=basePath%>';
	</script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/plateform.userSelect.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/plateform.subUserSelect.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/plateform.sqlUserSelect.js"></script>
	<script type="text/javascript">
		$("document").ready(function() {
			$(".chosenName").css("height", "auto");
			$(".disabled").find("input,select,textarea,button,a").attr("disabled", "disabled");
			initTable();
			

			$("label").css("text-align", "center");
			$("td").css("text-align", "center");
			$("div.webuploader-container").css("float", "left");
			$("div.mb10").css("margin-top", "10px");
			$("div.mb10").addClass("clearfix");
			$("div.uploader-list").css("overflow", "hidden");
			$("div.picker").css("float", "left");
			$(".uploader-list tfoot td").css("border-left","none");
			$(".radio-inline").css("line-height", "20px");
			$(".radio-inline").css("margin-left", "10px");
			$("div.datatable-head-span").css("width", "100%");
			
			$("table.table thead tr th").each(function() {
					$(this).css("text-align", "center");
			});

			$("#datatable").find("div.datatable-head").find("table").css("table-layout", "fixed");
			$("#datatable").find("div.datatable-rows").find("table tr td").each(function() {$(this).css("text-align", "left");});
			$("#datatable").find("div.datatable-rows").find("table tr td:first-child").each(function() {
								$(this).css("text-align", "center");
			});

			$('.form-date').datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				format : 'yyyy-mm-dd'
			});
			$('.form-date-long-medium').datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				forceParse : 0,
				showMeridian : 1,
				format : 'yyyy-mm-dd hh:ii'
			});
			$('.form-time-medium').datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				maxView : 1,
				forceParse : 0,
				format : 'hh:ii'
			});
			$('.form-date-long-long').datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				maxView : 1,
				forceParse : 0,
				format : 'yyyy-mm-dd hh:ii:ss'
			});
			$('.form-year').datetimepicker({
				language : 'zh-CN',
				autoclose : 1,
				startView : 4,
				minView : 4,
				forceParse : 0,
				format : 'yyyy'
			});
			$('.form-year-month').datetimepicker({
				language : 'zh-CN',
				autoclose : 1,
				startView : 3,
				minView : 3,
				forceParse : 0,
				format : 'yyyy-mm'
			});
			$('.form-date-long-medium').datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				maxView : 1,
				forceParse : 0,
				format : 'yyyy-mm-dd hh:ii'
			});
			loadUploadPreview("5555b3b8-f36a-4d15-b33d-4dd25c825673_Img","5555b3b8-f36a-4d15-b33d-4dd25c825673",200,200);
		
			$("div.datatable-head-span").css("width", "100%");
		});
		$("#saveBtn").click(function() {
			$("#groupForm").attr("action","<%=basePath%>unit/sysConfig.do").submit()
		});

		
	</script>

	<script>
		$("table").each(
				function(index, element) {
					var currentTable = $(this);
					if (currentTable.parent().parent("td").length > 0) {
						currentTable.find("tr").eq(0).find("td").css(
								"border-top", "none");
						currentTable.find("tr").each(
								function(index, element) {
									$(this).children().eq(0).css("border-left",
											"none");
								});
					}
				});

		function changeCheck() {

			var input = $("[name=f6f17be2df328b1ebaef3597f8453cb7]");
			var str = "";
			for (var i = 0; i < input.length; i++) {
				if (input[i].checked) {
					if (input[i].value != "") {
						str += input[i].value + ";";
					}
				}
			}
			$.ajax({
				type : "post",
				url : basePath + "os/mytaskBytype4.do",
				data : "str=" + str,
				success : function(msg) {
					if (msg == "1") {
						parent.frames["main"].$("#030a1b87-a436-496a-a062-7fe467ab2611").val("1");

					} else {
						parent.frames["main"].$("#030a1b87-a436-496a-a062-7fe467ab2611").val("0");

					}

				}

			});

		}
		function formatUploadTable() {
			$("#datatable-uploadListTable").find("table.table").css("table-layout", "fixed");
		}
	</script>

	<div class="tip-yellowsimple">
		<div class="tip-inner tip-bg-image"></div>
		<div class="tip-arrow tip-arrow-top tip-arrow-right tip-arrow-bottom tip-arrow-left"></div>
	</div>
</body>
</html>