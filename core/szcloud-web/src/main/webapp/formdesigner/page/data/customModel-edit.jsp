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
<html>
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>数据源-自定义模型</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
		
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="#">首页</a></li>
		          <li><a href="#">我的应用产品</a></li>
		          <li class="active">数据源-自定义模型</li>
		        </ul>
			</div>
			<div class="C_btnGroup clearfix">
						<div class="C_btns">
							<button class="btn btn-info" id="saveBtn">
								<i class="icon-save"></i>保存
							</button>
							<button class="btn btn-info" id="cancelBtn">
								<i class="icon-remove"></i>取消
							</button>
						</div>
			</div>
			<div class="row" id="dataform">
				<form class="form-horizontal"  id="datasourceForm"  >
					 	<c:if test="${result!=null&&''!=result}">
							<span style="color: red">(${result})</span>
						</c:if>
					 	<input type="hidden" name="id" value="" id="id"/>	
								<div class="form-group">
									<label class="col-md-1 control-label">类型：</label>
									<div class="col-md-5">
										<select id="type" class="form-control" tabindex="2" name="type">
											<option value="domain">领域模型</option>
											<option value="custom">自定义</option>
										</select>
									</div>
									<label class="col-md-1 control-label">名称：</label>
									<div class="col-md-5">
										<input type='text' name='name' id='name' value=""
											class='form-control' />
									</div>
								</div>
					
								<div class="form-group">
									<label class="col-md-1 control-label">描述：</label>
									<div class="col-md-5">
										<textarea name='description' id='description' rows='3'
											class='form-control'></textarea>
									</div>
								</div>
								<div class="form-group" id="content">
									<label class="col-md-1 control-label">执行脚本：</label>
									<div class="col-md-5">
										<textarea name='sqlScript' id='sqlScript' rows='3'
											class='form-control'></textarea>
									</div>
								</div>
				</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/json2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript" src="<%=basePath %>resources/scripts/map.js"></script>
		<script type="text/javascript" src="<%=basePath %>formdesigner/page/script/data.js"></script>
		<script type="text/javascript">
		
		
		
		
         $(document).ready(function(){
        	 try {
					var dialog = top.dialog.get(window);
				} catch (e) {
					return;
				}
				$("#type option[value='2']").attr("selected","true");
				$("#type").attr("disabled","disabled");
				var model = dialog.data;
				if(!empty(model))
					{
						 $("#name").val(model.name);
						 $("#description").val(model.description);
						 $("#id").val(model.id);
						 $("#sqlScript").val(model.sqlScript);
							
					}else{ 
						 $("#id").val(guidGenerator());
						}
				$("#saveBtn").click(function(){
					var formData = $("#datasourceForm").serializeJSON();
					formData.type="2";
					dialog.close(formData);
					dialog.remove();
				});
				
				$("#cancelBtn").click(function(){
					dialog.remove();
				});
         });
         </script>	
	</body>
</html>
