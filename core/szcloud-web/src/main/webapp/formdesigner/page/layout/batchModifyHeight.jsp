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
			<button type="button" class="btn btn-success" id="batchModifyHeight"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
		
			<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
		<div class="form-group">
		<label class="col-md-2" title="">高度</label>	
	<div class="col-md-4">
		<div class="input-group">
			<input type="text" class="form-control" name="height" id="height" value="">
			<span class="input-group-addon">厘米</span>
			<select class="form-control" name="heightType" id="heightType" style="width:90px">
            	<option value="" selected="selected">请选择</option>
            	<option value="1">固定值</option>
                <option value="2">最小值</option>
            </select>
		</div>
		</div>
		</div>
		<div class="form-group">
	<label class="col-md-2" title="">宽度（列组件设置才有效）</label>	
	<div class="col-md-4">
		<div class="input-group">
			<input type="text" class="form-control" name="width" id="width" value="">
			<span class="input-group-addon">厘米</span>
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
		<script src="<%=basePath%>/formdesigner/page/component/component.js"></script>
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
			
			
			
			
			
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});/**/
			//$("#style").formValidator({onFocus:"请输入样式",onCorrect:"符合要求"})
			//.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请输入样式"});
			
			
			$("#batchModifyHeight").click(function(){
				if($.formValidator.pageIsValid('1')){
					
					var height = $("#height").val();
					var heightType = $("#heightType").val();
					var width = $("#width").val();
					
					var dynamicPageId = $("#dynamicPageId").val();

					$.ajax({
						type: "POST",
						url: basePath + "layout/batchModifyHeight.do",
						data: "height="+height + "&heightType="+heightType+"&width=" + width + "&_selects="+_selects+"&dynamicPageId=" + dynamicPageId,
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



