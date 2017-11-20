<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<meta charset="utf-8">
<title>数据字典编辑</title>
<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
				<li><i class="icon-location-arrow icon-muted"></i></li>
				<li class="active">组织机构与权限</li>
				<li><a href="<%=basePath%>manage/pdataDictionaryList.do?currentPage=0">数据字典管理</a></li>
				<li class="active">数据字典编辑</li>
			</ul>
		</div>

		<div class="row" id="dataform">
			<legend class=" text-center">
				<c:if test="${result!=null&&''!=result}">
					<span style="color: red">(${result})</span>
				</c:if>
			</legend>
			<form class="form-horizontal" id="dataDictForm" action="<%=basePath%>manage/pdataDictionarySave.do" method="post">
					<input type="hidden" name="id" value="${vo.id}"/>
					<div class="form-group">
						<label class="col-md-1 control-label required">编码：</label>
						<div class="col-md-4">
							<input name="code" class="form-control" id="code"
								type="text" placeholder="" value="${vo.code}">
						</div>
						<label class="col-md-2 control-label required">数据键：</label>
						<div class="col-md-4">
							<input name="dataKey" class="form-control"
								id="dataKey" type="text" placeholder=""
								value="${vo.dataKey}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label required">数据值：</label>
						<div class="col-md-4">
							<input name="dataValue" class="form-control" id="dataValue" type="text"
								placeholder="" value="${vo.dataValue}">
						</div>
						<label class="col-md-2 control-label required">数据顺序：</label>
						<div class="col-md-4">
							<input name="dataOrder" class="form-control" id="dataOrder"
								type="text" placeholder="" value="${vo.dataOrder}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label required">数据描述：</label>
						<div class="col-md-4">
							<input name="dictRemark" class="form-control" id="dictRemark"
								type="text" placeholder="" value="${vo.dictRemark}">
						</div>
					</div>

					<div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-1 col-md-10">
			              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
							<a href="<%=basePath%>manage/pdataDictionaryList.do?currentPage=0" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>   
			            </div>
		          	</div>
			</form>
		</div>

	</div>
	<%@ include file="/resources//include/common_form_js.jsp" %>
	<script type="text/javascript">
		$(function(){
   			$.formValidator.initConfig({formID:"dataDictForm",debug:false,onSuccess:function(){
			   	$("#dataDictForm").submit();
    		},onError:function(){alert("错误，请看提示")}});
			$("#code").formValidator({onFocus:"请输入编码,并以英文逗号(,)结尾",onCorrect:"符合要求"}).inputValidator({min:1,max:255,onError:"最长255位"}).regexValidator({regExp:"^[a-z0-9A-Z\,]+,$",onError:"请输入正确的编码"});
			$("#dataKey").formValidator({onFocus:"请输入数据键",onCorrect:"符合要求"}).inputValidator({min:1,max:50,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
			$("#dataValue").formValidator({onFocus:"请输入数据值",onCorrect:"符合要求"}).inputValidator({min:1,max:255,onError:"最长255位"});
			$("#dataOrder").formValidator({empty:true,onFocus:"请输入数据顺序(1~4位数字)",onCorrect:"符合要求"}).regexValidator({regExp:"^\\d{1,4}$",onError:"请输入正确的序号"});
			$("#dictRemark").formValidator({onFocus:"请输入数据描述",onCorrect:"符合要求"}).inputValidator({max:255,onError:"最长255位"});
		})
	</script>
</body>
</html>
