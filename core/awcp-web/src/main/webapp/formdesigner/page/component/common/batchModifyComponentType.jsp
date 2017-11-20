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
			<button type="button" class="btn btn-success" id="batchModifyComponentType"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
		
			<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
			
<div class="form-group">
	<label class="col-md-2 control-label ">组件类型</label>
	<div class="col-md-4">
	<!--	<input name="componentType" class="form-control" id="componentType" type="text" value="">  -->
		<select class="form-control" name="componentType" id="componentType">
				<option value="">请选择</option>
				<option value="1001">单行文本框</option>
				<option value="1002">日期文本框</option>
				<option value="1003">多选复选框</option>		
				<option value="1004">radio单选按钮</option>		
				<option value="1005">多行输入框</option>		
				<option value="1006">下拉选项框</option>		
				<option value="1007">密码框</option>		
				<option value="1008">列框</option>		
				<option value="1009">标签</option>		
				<option value="1010">隐藏框</option>		
				<option value="1011">文件上传框</option>		
				<option value="1012">包含组件框</option>		
				<option value="1013">搜索组件框</option>		
				<option value="1014">间隔文本</option>			
				<option value="1015">tips提示</option>			
				<option value="1016">图片上传框</option>				
				<option value="1017">表格组件</option>	
				<option value="1018">纯文本</option>	
				<option value="1019">富文本框</option>
				<option value="1020">用户选择框</option>	
				<option value="1021">审批意见</option>	
				<option value="1022">审批状态</option>	
				<option value="1029">单人选择框</option>
				<option value="1035">签名</option>	
				<option value="1033">级联下拉框</option>
				<option value="1034">tab切换</option>
				<option value="1035">地图</option>
				<option value="1037">签名</option>
			<!-- <option value="1023">WORD编辑</option> -->
		</select>
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
			
			
			
			
			
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});/**/
			$("#componentType").formValidator({onFocus:"请输入类型",onCorrect:"符合要求"})
			.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请输入样式"});
			
			
			$("#batchModifyComponentType").click(function(){
				if($.formValidator.pageIsValid('1')){
					
					var componentType = $("#componentType").val();
					
					var dynamicPageId = $("#dynamicPageId").val();

					$.ajax({
						type: "POST",
						url: basePath + "component/batchModifyComponentType.do",
						data: "componentType="+encodeURI(componentType) +
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



