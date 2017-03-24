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
 		<title>数据库连接信息</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="dataform">
			<c:if test="${result!=null&&''!=result}">
				<span style="color: red">(${result})</span>
			</c:if>
			<form class="form-horizontal" id="groupForm" action="<%=basePath %>dataSourceManage/listTables.do" method="post">
				<div class="form-group">
					<label class="col-md-2 control-label required">数据源类型</label>
					<div class="col-md-4">
						<select data-placeholder="请选择属性类型..." id="sourceType" class="chosen-select form-control" tabindex="2" name="sourceType">
									<c:if test="${vo.sourceType == 'MYSQL'  || empty vo.sourceType}">
										<option value="MYSQL" selected="selected">MYSQL</option>
										<option value="ORACLE">ORACLE</option>
									</c:if>
									<c:if test="${vo.sourceType == 'ORACLE' }">
										<option value="MYSQL">MYSQL</option>
										<option value="ORACLE" selected="selected">ORACLE</option>
									</c:if>
						</select>
					</div>
					<label class="col-md-2 control-label">数据源驱动</label>
					<div class="col-md-4">
						<input name="sourceDriver" class="form-control" id="sourceDriver"
							type="text" placeholder="" value="com.mysql.jdbc.Driver">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">数据源连接</label>
					<div class="col-md-10">
						<input name="sourceUrl" class="form-control" id="sourceUrl"
							type="text" placeholder="" value="${vo.sourceUrl }">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">用户名</label>
					<div class="col-md-4">
						<input name="userName" class="form-control" id="userName"
							type="text" placeholder="" value="${vo.userName }">
					</div>
					<label class="col-md-2 control-label required">密码</label>
					<div class="col-md-4">
						<input name="userPwd" class="form-control" id="userPwd"
							type="text" placeholder="" value="${vo.userPwd }">
					</div>
				</div>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		            	<button type="button" class="btn btn-success" id="test"><i class="icon-save"></i>测试</button>
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
						<a href="<%=basePath %>dataSourceManage/selectPage.do" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		        </div>
			</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
       		$(document).ready(function(){
       			$('#saveBtn').attr('disabled',"true");
       			$("#sourceDriver").attr("readonly","readonly");
       			$("#sourceType").change(function(){
       				var v=$(this).val();
       				if(v=="MYSQL"){
       					$("#sourceDriver").attr("value","com.mysql.jdbc.Driver");
       				}
       				if(v=="SQL SERVER"){
       					$("#sourceDriver").attr("value","com.microsoft.sqlserver.jdbc.SQLServerDriver");
       				}
       				if(v=="DB2"){
       					$("#sourceDriver").attr("value","com.ibm.db2.jdbc.app.DB2.Driver");
       				}
       				if(v=="SYBASE"){
       					$("#sourceDriver").attr("value","com.sybase.jdbc.SybDriver");
       				}
       				if(v=="ORACLE"){
       					$("#sourceDriver").attr("value"," oracle.jdbc.OracleDriver");
       				}
       			});
       			
       			if($("#sourceType").val()=="MYSQL"){
   					$("#sourceDriver").attr("value","com.mysql.jdbc.Driver");
   				} else if($("#sourceType").val()=="ORACLE"){
   					$("#sourceDriver").attr("value"," oracle.jdbc.OracleDriver");
   				}
       			
       			
       			$("#test").click(function(){
       				$.ajax({
						type:"get",
						url:"<%=basePath%>dataSourceManage/testDataSource.do",
						data:{"sourceDriver":$("#sourceDriver").val(),"sourceUrl":$("#sourceUrl").val(),"userName":$("#userName").val(),"userPwd":$("#userPwd").val()},
						datatype:"json",
						success:function(date){
							if(date.id==1){
								alert("数据库连接成功");
								$('#saveBtn').removeAttr("disabled");
							}else{
								alert("数据库连接失败，请检查信息是否有错，或者网络无法访问");
							}
						}
       				});
       			});
       			
       			$(function(){
       			   $.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
       					$("#groupForm").submit();
       					  return false;
       			    },onError:function(){alert("请按提示正确填写内容");}});
       			  	$("#sourceUrl").formValidator({empty:false,onShow:"请输入数据库链接地址"});
       			  	$("#userName").formValidator({empty:false,onShow:"请输入用户名"});
       			  	$("#userPwd").formValidator({empty:false,onShow:"请输入密码"});
       			});
       			
       		});
		
		
		
         </script>	
	</body>
</html>