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
 		<%@ include file="/resources/include/common_form_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--edit页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="dataform">表单区（--输入组--按钮组）</div>
			-->
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="<%=basePath%>devAdmin/intoSystem.do?sysId=1">首页</a></li>
		          <li><a href="<%=basePath %>metaModel/selectPage.do">元数据管理</a></li>
		          <li class="active">元数据属性编辑</li>
		        </ul>
			</div>
			<div class="row" id="dataform">
			<form class="form-horizontal" id="groupForm"
				action="dataSourceManage/save.do" method="post">
				<legend>
					元数据属性编辑
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
				</legend>
				<div class="form-group">
					<label class="col-md-2 control-label required">数据源名称</label>
					<div class="col-md-4">
						<input name="name" class="form-control" id="name"
							type="text" placeholder="">
					</div>
					<label class="col-md-2 control-label required">数据源类型</label>
					<div class="col-md-4">
						<select data-placeholder="请选择属性类型..." id="sourceType" class="chosen-select form-control" tabindex="2" name="sourceType">
									<option value="MYSQL" selected="selected">MYSQL</option>
									<option value="ORACLE">ORACLE</option>
									<option value="DB2">DB2</option>
									<option value="SQL SERVER">SQL SERVER</option>
									<option value="SYBASE">SYBASE</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">数据源连接</label>
					<div class="col-md-4">
						<input name="sourceUrl" class="form-control" id="sourceUrl"
							type="text" placeholder="">
					</div>
					<label class="col-md-2 control-label">数据源驱动</label>
					<div class="col-md-4">
						<input name="sourceDriver" class="form-control" id="sourceDriver"
							type="text" placeholder="" value="com.mysql.jdbc.Driver">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">用户名</label>
					<div class="col-md-4">
						<input name="userName" class="form-control" id="userName"
							type="text" placeholder="">
					</div>
					<label class="col-md-2 control-label required">密码</label>
					<div class="col-md-4">
						<input name="userPwd" class="form-control" id="userPwd"
							type="text" placeholder="">
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
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
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
       			
       			$("#test").click(function(){
       				$.ajax({
						type:"get",
						url:"<%=basePath%>dataSourceManage/testDataSource.do",
						data:{"sourceDriver":$("#sourceDriver").val(),"sourceUrl":$("#sourceUrl").val(),"userName":$("#userName").val(),"userPwd":$("#userPwd").val()},
						datatype:"json",
						success:function(date){
							if(date.id==1){
								alert("成功");
								$('#saveBtn').removeAttr("disabled");
							}else{
								alert("失败");
							}
						}
       				});
       			});
       			
       		});
		
		
		$(function(){
		   $.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
				$("#groupForm").submit();
				  return false;
		    },onError:function(){alert("请按提示正确填写内容");}});
		  	$("#itemName").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[\u4E00-\u9FA5A-Za-z0-9_]+$",onError:"输入格式不正确"});
		  	$("#itemCode").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[a-zA-Z][a-zA-Z0-9_]*$",onError:"输入格式不正确"}).ajaxValidator({
		  		type:"get",
		  		url:"metaModelItems/dataValidate.do?itemCode="+$("#modelCode").val()+"&modelId="+$("#modelId").val(),
		  		datatype:"json",
		  		success:function(date){
		  			var obj=eval('('+date+')');
		  			if(obj.id=='0'){
		  				return true;
		  			}else{
		  				return false;
		  			}
		  		},
		  		buttons:$("saveBtn"),
		  		error: function () { alert("元数据属性已经存在，请重新输入"); },
                onerror: "请不要重复添加",
                onwait: "正在验证是否重复,请稍候..."
		  	});
		  	$("#itemLength").formValidator({empty:true,onShow:"请输入正整数"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"});
		  	$("#itemType").formValidator({onshow:"（必填）",onfocus:"（必填）请选择选项",oncorrect: "（正确）"}).inputValidator({min:0,onerror:"请选择"});
		});
         </script>	
	</body>
</html>