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
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
	<link rel="stylesheet" href="<%=basePath%>resources/styles/uploader.css">
	<%@ include file="/resources/include/common_css.jsp"%>
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="dataform">
			<form class="form-horizontal" id="actForm" action="<%=basePath%>fd/act/save.do" method="post">
				<c:if test="${result!=null&&''!=result}">
					<span style="color: red">(${result})</span>
				</c:if>
				<%@ include file="comms/basicInfo.jsp"%>
				<div class="form-group ">
					<label class="col-md-1 control-label">参数脚本</label>
					<div class='col-md-5'>
						<textArea class=" form-control"  rows='4' name="extbute[script]" id="target">${act.extbute['script']}</textArea>
					</div>
					<label class="col-md-1 control-label">sql脚本</label>
					<div class='col-md-5'>
						<textArea class=" form-control"  rows='4' name="extbute[sqlScript]" id="sqlScript">${act.extbute['sqlScript']}</textArea>
					</div>
				</div>
				<div class="form-group ">
				<div class="col-md-2"><label class="control-label">excel模板上传</label></div>
				<div class="col-md-10">
					<div id='uploader' class="uploader" >
						<input type="hidden" class="orgfile" name="extbute[templateFileId]" value="${act.extbute['templateFileId']}" />
					    <div class="btns mb10">
					        <div class="picker"><i class="icon-upload-alt"></i> 上传附件</div>
					        <div class="btn btn-primary" id="MoreDownload"><i class="icon-download-alt"></i> 下载附件</div>
					        <div class="btn btn-primary" id="FileRemove"><i class="icon-trash"></i> 删除附件</div>
					    </div>
					    <div class="table table-bordered oldlist">
					    	<table class="table uploader-list">
								<thead>
									<tr>
										<th></th>
										<th>文件名称</th>
										<th>文件类型</th>
										<th>文件大小</th>
									</tr>
								</thead>
								<tbody></tbody>
								<tfoot>
									<tr><td colspan="4" class="tips"></td></tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>				
				</div>
				<%@ include file="comms/scripts.jsp"%>
			</form>
		</div>
	</div>
	<%@ include file="/resources/include/common_form_js.jsp"%>
	<script src="<%=basePath%>resources/scripts/map.js"></script>
	<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script src="<%=basePath%>formdesigner/page/script/act.js"></script>
	<script src="<%=basePath%>resources/plugins/webuploader-0.1.5/dist/webuploader.js"></script>
	<script src="<%=basePath%>resources/scripts/uploader.js"></script><!-- 上传控件实现方法 -->
	<script src="<%=basePath%>venson/js/common.js"></script>
	<script>
		var basePath="<%=basePath%>";
		$(document).ready(function() {
				try {
					var dialog = top.dialog.get(window);
				} catch (e) {
					return;
				}
				var radio = $(":radio[name='pattern']:checked").val();
				if (empty(radio)) {
					$(":radio[name='pattern']:first").prop("checked", "checked");
				}
				if(dialog){
					$("#dynamicPageId").attr("disabled","disabled");
				}
				
				$("#saveBtn").click(function() {
						$("#actType").removeAttr("disabled");
						$("#actClientScript").val("actNewRun("+$("#target").val()+");\n");
						if (dialog) {
							$("#dynamicPageId").removeAttr("disabled");
							var data = $("#actForm").serializeJSON();
							data.dynamicPageName=$("#dynamicPageId option:selected").text();
							var buttons = new Array();
							$(":checkbox[name='buttons']:checked").each(function() {
								buttons.push($(this).val());
							});
							data['buttons'] = buttons.join(",");
							$.ajax({	type : "POST",
										async : false,
										url : "fd/act/saveByAjax.do",
										data : data,
										success : function(ret) {
											var json = eval(ret);
											dialog.close(json);
											dialog.remove();
										},
										error : function(
												XMLHttpRequest,
												textStatus,
												errorThrown) {
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
					$("#uploader").loadUploader({uploaderStyle:"table",fileNumLimit:1,extensions:"xls,xlsx"});
			});

		
	
	</script>
</body>
</html>
