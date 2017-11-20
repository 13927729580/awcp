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
<html>
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>元数据页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="../../resources/include/common_form_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--edit页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="dataform">表单区（--输入组--按钮组）</div>
			-->
			
			<div class="row" id="dataform">
			<form class="form-horizontal" id="groupForm"
				action="dataSourceManage/update.do" method="post">
				
				<div class="form-group">
					<label class="col-md-2 control-label required">数据源名称</label>
					<div class="col-md-4">
						<input type="hidden" name="id" id="id" value="${vo.id}">
						<input name="name" class="form-control" id="name"
							type="text" placeholder="" value="${vo.name }">
						<input name="domain" class="form-control" id="domain"
							type="hidden" value="local">
					</div>
				
				</div>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		              	<button type="button" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
		              	<button type="button" class="btn btn-info" id="cancelBtn"><i class="icon-save"></i>取消</button>
		            </div>
		        </div>
			</form>
			</div>
		</div>
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
       		$(document).ready(function(){
       			try {
    				var dialog = top.dialog.get(window);
    			} catch (e) {
    				return;
    			}
       			$("#cancelBtn").click(function(){
       				dialog.close();
       			});
       			$("#saveBtn").click(function(){
       					var id = $("#id").val();
       					var name = $("#name").val();
       					var domain = $("#domain").val();
       					$.ajax({
       						url:"<%=basePath%>dataSourceManage/saveByAjax.do",
       						data:{id:id,name:name,domain:domain},
       						method:"post",
       						async:"fasle",
       						success:function(ret){
       							if("1"==ret){
       								dialog.close("添加成功！");
       								dialog.remove();
       							}else if("2"==ret){
       								dialog.close("更新成功！");
       								dialog.remove();
       							}else{
       								dialog.close("该数据源已存在！");
       								dialog.remove();
       							}
       						},
       						error : function(XMLHttpRequest, textStatus, errorThrown) {
       							alert(errorThrown);
       						}
       					});
       			});
       			
       			
		
		
				});
         </script>	
	</body>
</html>