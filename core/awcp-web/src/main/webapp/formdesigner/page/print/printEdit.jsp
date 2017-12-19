<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
			<form class="form-horizontal" id="printForm"
				action="<%=basePath%>fd/act/save.do" method="post">
				<c:if test="${result!=null&&''!=result}">
					<span style="color: red">(${result})</span>
				</c:if>
				
				<input type="hidden" id="pageId" name="pageId">
				<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="savePdfPrint"><i class="icon-plus-sign"></i>保存</button>
				
			</div>
				<div class="form-group">
					<label class="col-md-1 control-label">名称</label>
					<div class="col-md-5">
						<input name="name" class="form-control" id="name" type="text">
					</div>
					<label class="col-md-1 control-label">打印页面</label>
					<div class="col-md-5">
						<input type="hidden" class="form-control" id="select_dynamicPage" name="select_dynamicPage"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">打印文件名名称</label>
					<div class="col-md-5">
						<input name="pdfName" class="form-control" id="pdfName" type="text">
					</div>
					<label class="col-md-1 control-label">横向打印的页面</label>
					<div class="col-md-5">
						<input type="hidden" class="form-control" id="rotate_dynamicPage" name="rotate_dynamicPage"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">页眉</label>
					<div class="col-md-5">
						<div class="input-group">
							<span class="input-group-addon">文字</span>
							<input name="pageHeader" class="form-control" id="pageHeader" type="text"> 
							<span class="input-group-addon">位置</span>
							<select class=" form-control"  name="headLoc" id="headLoc">
								<option value="1" >左对齐</option>			
								<option value="2" >居中</option>			
								<option value="3" >右对齐</option>			
							</select>
						</div>
					</div>
					
			
					
					<label class="col-md-1 control-label">上下边距</label>
					<div class="col-md-5">
						<div class="input-group">
							<span class="input-group-addon">上边距</span>
							<input name="topDistance" class="form-control" id="topDistance" type="text"> 
							<span class="input-group-addon">下边距</span>
							<input name="botDistance" class="form-control" id="botDistance" type="text"> 
							
						</div> 
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">页脚</label>
					<div class="col-md-5">
						<div class="input-group">
							<span class="input-group-addon">文字</span>
							<input name="pageBottom" class="form-control" id="pageBottom" type="text"> 
							<span class="input-group-addon">位置</span>
							<select class=" form-control" tabindex="2" name="bottomLoc" id="bottomLoc">
								<option value="1" >左对齐</option>			
								<option value="2" >居中</option>			
								<option value="3" >右对齐</option>			
							</select>
						</div>
					</div>
					
					<label class="col-md-1 control-label">左右边距</label>
					<div class="col-md-5">
						<div class="input-group">
							
							<span class="input-group-addon">左边距</span>
							<input name="leftDistance" class="form-control" id="leftDistance" type="text"> 
							<span class="input-group-addon">右边距</span>
							<input name="rightDistance" class="form-control" id="rightDistance" type="text">
						</div> 
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否显示页数</label>
					<div class="col-md-5">
						<select class="form-control" tabindex="2" name="showPage" id="showPage">
							<option value="2" >否</option>	
							<option value="1" >是</option>							
						</select>
					</div>
				</div>
	<!-- 			
				<div class="form-group">
					<label class="col-md-1 control-label">字体行间距</label>
					<div class="col-md-5">
						<input name="fontSpace" class="form-control" id="fontSpace" type="text">
					</div>
					<label class="col-md-1 control-label">纸张方向</label>
					<div class="col-md-5">
						<select class=" form-control" tabindex="2" name="pageOrient" id="pageOrient">
							<option value="1" >垂直</option>			
							<option value="2" >水平</option>						
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-1 control-label">是否显示页数</label>
					<div class="col-md-5">
						<select class="form-control" tabindex="2" name="showPage" id="showPage">
							<option value="1" >否</option>			
							<option value="2" >是</option>						
						</select>
					</div>
					<label class="col-md-1 control-label">显示页码</label>
					<div class="col-md-5">
						<div class="col-md-5">
							<input name="pagination" class="form-control" id="pagination" type="text" placeholder="分号隔开，如：1；2；3">
						</div>
					</div>
				</div>
	-->			
			
			<div class="form-group">
				<div class="col-md-1"><label class="control-label">PDF模板上传</label></div>
				<div class="col-md-9">
					<div id='uploader' class="uploader" >
						<input type="hidden" class="orgfile" id="templateFileId" name="templateFileId" value="${act.extbute['templateFileId']}" />
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
			
			</form>
		</div>
	</div>
	<%@ include file="/resources/include/common_form_js.jsp"%>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/map.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/webuploader-0.1.5/dist/webuploader.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/uploader.js"></script><!-- 上传控件实现方法 -->
	<script src="<%=basePath%>venson/js/common.js"></script>
	<script type="text/javascript">
		var basePath = '${basePath}';
		//在更新时加载组件值;
		function loadPrintData(storeObject){
			var item = eval("("+storeObject.content+")");
			if(item.pageId != null){
				if(item.pageId) 	$("#pageId").val(item.pageId);
				if(item.name) $("#name").val(item.name);
				//if(item.select_dynamicPage) $("#select_dynamicPage").val(item.select_dynamicPage);
				if(item.select_dynamicPage){ 
				var data = eval("("+item.select_dynamicPage+")");
				$("#select_dynamicPage").select2("data",data);}
				if(item.pageHeader) $("#pageHeader").val(item.pageHeader);
				if(item.headLoc)	$("#headLoc option[value='"+item.headLoc+"']").attr("selected","true");
				if(item.topDistance) $("#topDistance").val(item.topDistance);
				if(item.botDistance) $("#botDistance").val(item.botDistance);
				if(item.pageBottom) $("#pageBottom").val(item.pageBottom);
				if(item.systemId) $("#systemId").val(item.systemId);
				if(item.bottomLoc)	$("#bottomLoc option[value='"+item.bottomLoc+"']").attr("selected","true");
				if(item.leftDistance) $("#leftDistance").val(item.leftDistance);
				if(item.rightDistance) $("#rightDistance").val(item.rightDistance);
				if(item.fontSpace) $("#fontSpace").val(item.fontSpace);
				if(item.pageOrient)	$("#pageOrient option[value='"+item.pageOrient+"']").attr("selected","true");
				if(item.pagination) $("#pagination").val(item.pagination);
				if(item.pdfName) $("#pdfName").val(item.pdfName);
				if(item.showPage)	$("#showPage option[value='"+item.showPage+"']").attr("selected","true");
				if(item.rotate_dynamicPage){ 
					var data = eval("("+item.rotate_dynamicPage+")");
					$("#rotate_dynamicPage").select2("data",data);
				}
				if(item.templateFileId) $("#templateFileId").val(item.templateFileId);
			}
		}
		$(document).ready(function() {
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			//判断是否有数据，初始化 所属模型
			var data = null;//dialog.data;
			
			var pageId = "";
			if(dialog != null){
				data = dialog.data;
				pageId = data.pageId;
				$("#pageId").val(data.pageId);
			}	
			//如果是编辑，则加载组件内容
			if(pageId){
				$.ajax({
					   type: "POST",
					   url: "<%=basePath%>print/getPrintById.do",
					   data: "storeId="+pageId,
					   success: function(data){
							if(data != null && data.id != null){	
								loadPrintData(data);
								$("#uploader").loadUploader({uploaderStyle:"table",fileNumLimit:1,extensions:"pdf"});
							}else{
								alert("保存失敗");
							}
					   }
					});	
			}
			
			$("#savePdfPrint").click(function(){
					var formJson=$("#printForm").serializeJSON();
					var name = $("#name").val();
					var pageId = $("#pageId").val();
					var pageSelect = $("#select_dynamicPage").select2("data");
					var pageSelectJson = JSON.stringify(pageSelect);
					var rotateSelect = $("#rotate_dynamicPage").select2("data");
					var rotateSelectJson = JSON.stringify(rotateSelect);
					formJson['select_dynamicPage'] = pageSelectJson;
					formJson['rotate_dynamicPage'] = rotateSelectJson;
					var sjContent = JSON.stringify(formJson);
					$.ajax({
						   type: "POST",
						   url: basePath + "print/save.do",
						   data: "id="+pageId + "&name="+name + 
						   "&content="+sjContent +
						   "&pageSelectJson="+pageSelectJson,
						   async : false,
						   success: function(data){
						   		if(data.success) {
						   			$("#pageId").val(data.msg.id);
						   			if(dialog != null){
										dialog.close(data);
										dialog.remove();
									}
						   		} else {
						   			alert(data.msg);
						   		}
						   }
					});	
			//	}
				return false;
			});
			
		});
		
		$(function(){
			var dynamicPageArray = new Array();
			$.ajax({
			   type: "POST",
			   async:false,
			   url: basePath + "fd/listJson.do",
			   success: function(data){
					if(data != null){
						$.each(data, function(index, item) {
							dynamicPageArray.push({id: item.id, text: item.name});
						});
					}else{
						alert("加载组件失败");
					}
			   }
			});	
			var element  = [{id: 1, text: 2},{id: 3, text: 4}];
      	 	if($.fn.select2){
      		 $("#select_dynamicPage").select2({ 
      			 //tags:["P:产品经理","D:开发甲","D:开发乙","D:开发丙","D:开发丁"],
      			 tags:	dynamicPageArray,
      			 placeholder: "请选择页面",
      			 tokenSeparators: [",", " "]
      		 });
      		 $("#select_dynamicPage").select2("container").find("ul.select2-choices").sortable({
    			containment: 'parent',
    			start: function() { $("#select_dynamicPage").select2("onSortStart"); },
    			update: function() { $("#select_dynamicPage").select2("onSortEnd"); }
			});
      		 $("#rotate_dynamicPage").select2({ 
      			 //tags:["P:产品经理","D:开发甲","D:开发乙","D:开发丙","D:开发丁"],
      			 tags:	dynamicPageArray,
      			 placeholder: "请选择页面",
      			 tokenSeparators: [",", " "]
      		 });
      		 $("#rotate_dynamicPage").select2("container").find("ul.select2-choices").sortable({
    			containment: 'parent',
    			start: function() { $("#rotate_dynamicPage").select2("onSortStart"); },
    			update: function() { $("#rotate_dynamicPage").select2("onSortEnd"); }
			});
      		}
       })
	</script>
</body>
</html>
