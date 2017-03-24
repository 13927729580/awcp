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
			<button type="button" class="btn btn-success" id="batchModifyLayout"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
		
			<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
			<div class="form-group">
				<label class="col-md-2 control-label required">布局</label>
				<div class="col-md-4">
					<div class="input-group">
					<input type="text" readonly="readonly" class="form-control" name="layoutName" id="layoutName">
					<div class="input-group-btn"> <button class="btn btn-default" type="button" id="layoutSelect">选择</button> </div>
					<input type="hidden" readonly="readonly"  class="form-control" name="layoutId" id="layoutId">
					</div>
				</div>
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
			
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			var data = null;
			
			
			var _selects = null;
			
			if(dialog != null){
				data = dialog.data;
				if(data != null && data._selects)
					_selects = data._selects;			//选择的组件id串
			}
			if(data.dynamicPageId && $("#dynamicPageId")){
				$("#dynamicPageId").val(data.dynamicPageId);
			}
			//initialize data source options
			
			//选择布局
			$('#layoutSelect').click(function(){
				if($("#dynamicPageId").val()){
					$.ajax({
						url: basePath + "layout/listLayoutInTree.do?dynamicPageId="+$("#dynamicPageId").val(),
						dataType:'json',
						success: function(msg){
							top.dialog({ 
								id: 'select-layout-dialog' + Math.ceil(Math.random()*1000000),
								title: '选择布局',
								url: basePath + "formdesigner/page/component/zTreeLayout.jsp",
								data:msg,
								onclose : function() {
									if (this.returnValue) {
										//alert(this.returnValue.id);
										$("#layoutName").val(this.returnValue.name);
										$("#layoutId").val(this.returnValue.id);
									}
								}
							}).show(document.getElementById("layoutName"));
					   	}
					});
				} else {
					alert("请先保存表单");
				}
			});
			
			
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});/**/
			$("#layoutId").formValidator({onFocus:"请选择布局",onCorrect:"符合要求"})
			.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择布局"});
			
			
			$("#batchModifyLayout").click(function(){
				if($.formValidator.pageIsValid('1')){
					
					var layoutId = $("#layoutId").val();
					var layoutName = $("#layoutName").val();
					var dynamicPageId = $("#dynamicPageId").val();

					$.ajax({
						type: "POST",
						url: basePath + "component/batchModifyLayout.do",
						data: "layoutId="+layoutId + "&layoutName="+layoutName +
						"&_selects="+_selects+"&dynamicPageId=" + dynamicPageId,
						async : false,
						success: function(data){
							if("1" == data){	
								alert("修改成功")
								if(dialog != null){
									dialog.close(data);
									dialog.remove();
								}
							}else{
								alert("修改失败");
							}
						}
					});	
				}
				return false;
			});
			
		});
		
		$("#cancelComponent").click(function(){
				if(dialog != null){
					dialog.close();
					dialog.remove();
				}
			});
		
		
	
	</script>



