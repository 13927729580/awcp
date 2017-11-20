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
 		<title>菜单编辑页面</title>
 		<%@ include file="../../resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="<%=basePath%>devAdmin/intoSystem.do?sysId=1">首页</a></li>
		          <li><a href="<%=basePath %>metaModel/queryResult.do">元数据管理</a></li>
		          <li class="active">元数据模型编辑</li>
		        </ul>
			</div>
			<div class="row" id="dataform">
				<form method="post" class="form-horizontal" id="inputForm" action="<%=basePath %>metaModel/save.do">
					<legend>
						元数据模型编辑
						<c:if test="${result!=null&&''!=result}">
							<span style="color: red">(${result})</span>
						</c:if>
					</legend>
					<div class="form-group">
				      <label class="col-md-2 control-label required">模型名称(中)</label>
				      <div class="col-md-4">
				      	 <input type="hidden" id="id" name="id" value="">
				         <input name="modelName" id="modelName" class="form-control" type="text" placeholder="" value="">
				      </div>
				      <label class="col-md-2 control-label required">模型名称(英)</label>
				      <div class="col-md-4">
				         <input name="modelCode" id="modelCode" class="form-control"type="text" placeholder="" value="">
				      </div>
				    </div>
				    <div class="form-group">
				    	<label class="col-md-2 control-label required">表名称</label>
					      <div class="col-md-4">
					         <input name="tableName" id="tableName" class="form-control" type="text" placeholder="" value="">
					      </div>
					      <label class="col-md-2 control-label">模型描述</label>
					      <div class="col-md-4">
					         <input name="modelDesc" id="modelDesc" class="form-control" type="text" placeholder="" value="">
					      </div>
					</div>
					<%-- <div class="form-group">
						<label class="col-md-2 control-label required">模型类型</label>
						<div class="col-md-4">
						<input type="hidden" id="modelType" value="${vo.modelType }">
							<select data-placeholder="请选择模型类型..." id="modelType" class="chosen-select form-control" tabindex="2" name="modelType">
								<option value="1" id="mt1">领域模型</option>
								<option value="2" id="mt2">系统模型</option>
							</select>
						</div>
						<label class="col-md-2 control-label required">模型分类</label>
						<div class="col-md-4">
							<select data-placeholder="请选择模型分类..." id="modelClassId" class="chosen-select form-control" tabindex="2" name="modelClassId">
								<c:forEach items="${classVos}" var="vo">
									<option value="${vo.id}">${vo.name}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label required" >数据源</label>
						<div class="col-md-4">
							<select data-placeholder="请选择数据源..." id="dataSourceId" class="chosen-select form-control" tabindex="2" name="dataSourceId">
								<option value="" >请选择</option>
								<c:forEach var="item" items="${dataSources}">
									<option value="${item.id}" <c:if test="${vo.dataSourceId==item.id}">selected="selected"</c:if> >${item.name}<c:if test="${defaultDataSourceId==item.id}">(默认数据源)</c:if></option>
								</c:forEach>
							</select>
						</div>
						
					</div>
					--%>
					 <div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-2 col-md-10">
			              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
							<a href="<%=basePath %>metaModel/queryResult.do" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
							<!-- <a href="queryResult.do" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a> -->
			            </div>
			         </div> 
				</form>
			</div>
		</div>
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript">
	    
		
		$(function(){
			   $.formValidator.initConfig({formID:"inputForm",debug:false,onSuccess:function(){
					$("#inputForm").submit();
					  return false;
			    },onError:function(){alert("请按提示正确填写内容");}});
			  	$("#modelName").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[\u4E00-\u9FA5A-Za-z0-9_]+$",onError:"输入格式不正确"});
			  	$("#modelDesc").formValidator({empty:true,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"});
			  	$("#dataSourceId").formValidator({empty:false,onShow:"请选择数据源"}).inputValidator({min:1,max:225,onError:"请选择数据源"});
			  	$("#tableName,#modelCode").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[a-zA-Z][a-zA-Z0-9_]*$",onError:"输入格式不正确"}).ajaxValidator({
			  		type:"get",
			  		url:"metaModel/dataValidate.do",
			  		datatype:"json",
			  		success:function(data){
			  			if(data==0){
			  				return true;
			  			}else{
			  				return false;
			  			}
			  		},
			  		buttons:$("saveBtn"),
			  		error: function () { alert("不能输入重复的表名"); },
	                onerror: "请不要重复添加",
	                onwait: "正在验证是否重复,请稍候..."
			  	});
			  	/**
			  	$("#projectName").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[a-zA-Z][a-zA-Z0-9_]*$",onError:"输入格式不正确"}).ajaxValidator({
			  		type:"get",
			  		url:"metaModel/dataValidate.do?projectName="+$("#projectName").val(),
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
			  		error: function () { alert("输入的值已经存在，请重新输入"); },
	                onerror: "请不要重复添加",
	                onwait: "正在验证是否重复,请稍候..."
			  	});*/
			  	
	         });
		
         </script>
         
	</body>
</html>
