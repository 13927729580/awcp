<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>组信息编辑</title>

<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->

</head>
<body id="main">

		<div class="container-fluid">

			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
					<li><i class="icon-location-arrow icon-muted"></i></li>
					<li class="active">组织机构与权限</li>
		          	<li><a href="<%=basePath%>dev/punDevGroupList.do?currentPage=0">开发组管理</a></li>
		          	<li class="active">开发组信息编辑</li>
				</ul>
			</div>
			
			<div class="row" id="dataform">
				<legend class=" text-center">
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
				</legend>
				<form class="form-horizontal" id="groupForm" action="<%=basePath%>dev/punDevGroupSave.do" method="post">
					<input type="hidden" name="id" value="${vo.id}" />
					<!-- <input type="hidden" name="type" id="groupType" value="1"/> -->
					<div class="form-group">
						<label class="col-md-1 control-label required">组名（中文）：</label>
						<div class="col-md-4">
							<input name="nameCn" class="form-control" id="nameCn"
								type="text" placeholder="" value="${vo.nameCn}">
						</div>
						<label class="col-md-2 control-label">组名（英文）：</label>
						<div class="col-md-4">
							<input name="nameEn" class="form-control" id="nameEn"
								type="text" placeholder="" value="${vo.nameEn}">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-1 control-label">组简称：</label>
						<div class="col-md-4">
							<input name="nameShort" class="form-control"
								id="nameShort" type="text" placeholder=""
								value="${vo.nameShort}">
						</div>
						<%-- <label class="col-md-2 control-label">组类型：</label>
						<div class="col-md-4">
							<input name="typeText" class="form-control" id="typeText" type="text"
								placeholder="" value="非根组织" readonly>
							<input type="hidden" name="type" value="${vo.type}" />
						</div> --%>
					</div>
					<%-- <div class="form-group">
						<label class="col-md-1 control-label">组织机构代码：</label>
						<div class="col-md-4">
							<input name="code" class="form-control" id="code" type="text"
								placeholder="" value="${vo.code}" readonly>
						</div>
					</div> --%>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-1 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
						<a href="<%=basePath%>dev/punDevGroupList.do?currentPage=0" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		        </div>
			</form>
		</div>

		</div>

		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script type="text/javascript">
			$(function(){
	   			$.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
					$("#groupForm").submit();
	    		},onError:function(){alert("错误，请根据提示输入")}});
				$("#nameCn").formValidator({onFocus:"请输入组织中文名称",onCorrect:"符合要求"}).inputValidator({min:1,max:30,empty:{leftEmpty:false,rightEmpty:false,emptyError:"请输入正确组织名称"},onError:"请输入正确组织名称"});
				$("#nameShort").formValidator({onFocus:"请输入组织简称",onCorrect:"符合要求"}).inputValidator({max:30,onError:"至多15个汉字或30英文字符"});
				$("#nameEn").formValidator({onFocus:"请输入组织英文名称",onCorrect:"符合要求"}).inputValidator({max:100,onError:"至多100个英文字符"});
			})
	</script>


</body>
</html>
