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
<title>系统选择页面</title>
<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
</head>
<body id="main">
	
	<div class="container-fluid">
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="saveBtn"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelBtn"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<input type="hidden" name="_selects" id="_selects"/>
			<div class="form-group">
				<label class="col-md-2 control-label required">选择系统</label>
				<div class="col-md-4">
					<select id="systemId"></select>
				</div>
			</div>
		</form>
	</div>

	<%@ include file="/resources/include/common_form_js.jsp" %>
	<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>

	<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
			$("#systemId").select2();
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				//return;
			}
			if(dialog != null){
				//alert(typeName + "组件配置");
				dialog.title("复制元数据到指定系统");
				dialog.width(300);
				dialog.height(400); 
				dialog.reset();     // 重置对话框位置
				var data = dialog.data;
				if(data != null && data.ids != null) {
					$("#_selects").val(data.ids);
				}
			}
			$("#cancelBtn").click(function(){
				if(dialog != null){
					dialog.close();
					dialog.remove();
				}
			});
			
			$("#saveBtn").click(function(){
				if($("#systemId").val()) {
					$.ajax({
						type: "POST",
						url: basePath + "metaModel/copyToSys.do",
						data: {_selects : $("#_selects").val(),systemId : $("#systemId").val()},
						async : false,
						success: function(data){
							if("1" == data.rtn){	
								alert("复制成功")
								if(dialog != null){
									dialog.close(data);
									dialog.remove();
								}
							}else{
								alert(data.msg);
							}
						}
					});						
				} else {
					alert("请选择系统");
				}
				return false;
			});
			
		});
		
	
	</script>