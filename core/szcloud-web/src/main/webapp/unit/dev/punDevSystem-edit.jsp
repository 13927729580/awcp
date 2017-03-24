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
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>系统编辑页面</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
 		
		
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--edit页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="dataform">表单区（--输入组--按钮组）</div>
			-->
<!-- 			<div class="row" id="breadcrumb"> -->
<!-- 				<ul class="breadcrumb"> -->
<!-- 		          <li><i class="icon-location-arrow icon-muted"></i></li> -->
<!-- 		          <li class="active">系统编辑</li> -->
<!-- 		        </ul> -->
<!-- 			</div> -->
			
			<div class="row" id="dataform">
			<form class="form-horizontal" id="groupForm" action="<%=basePath%>dev/punSystemSave.do" method="post">
				<legend>
					系统编辑
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
				</legend>
				
		        <div class="form-group">       	
		            <label class="col-md-2 control-label required">系统名称：</label>
		            <div class="col-md-4">
		                <input name="sysName" class="form-control" id="sysName"
							type="text" placeholder="" value="${vo.sysName}">
		            </div>
		            <label class="col-md-2 control-label">系统代称：</label>
		            <div class="col-md-4">
		            	<input name="sysShortName" class="form-control" id="sysShortName"
							type="text" placeholder="" value="${vo.sysShortName}">
		            </div>
				</div>
		        <div class="form-group">       	
		            <label class="col-md-2 control-label">系统地址：</label>
		            <div class="col-md-4">
		            	<input type="hidden" name="sysId" value="${vo.sysId}" /> 
						<input type="hidden" name="sysCreater" value="${vo.sysCreater}" /> 
						<input type="hidden" name="groupId" value="${vo.groupId}"/>
						<input type="hidden" name="sysCreateGroup" value="${vo.sysCreateGroup}"/>
						<c:if test="${vo.sysId != null}">
							<input type="hidden" name="sysCreateTime" value="${vo.sysCreateTime}"/>
						</c:if>
						<input type="hidden" name="sysStatus" value="${vo.sysStatus}"/>
						<input name="sysAddress" class="form-control" id="sysAddress" type="text" placeholder="" value="${vo.sysAddress}">
		            </div>
		             <label class="col-md-2 control-label">默认数据源：</label>
		            <div class="col-md-4">
		            	<select data-placeholder="请选择数据源..." id="defaultDataSource" class="chosen-select form-control" tabindex="2" name="defaultDataSource">
							<c:forEach var="item" items="${ss}">
								<option value="${item.id}" <c:if test="${source.id==item.id}">selected="selected"</c:if> >${item.name}</option>
							</c:forEach>
						</select>
		            </div>
				</div>
				
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-1 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
		            </div>
		          </div>
			</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
			$(function(){
		   		$.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
		   			$("button[type='submit']").addClass("disabled");
		    	},onError:function(){alertMessage("请填写带*的必填项")}});
				$("#sysName").formValidator({onShow:"请输入系统名称",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:50,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"1~50位"});
				$("#sysShortName").formValidator({onShow:"请输入系统代称",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({max:30,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"最长30位"});
		   		$("#sysAddress").formValidator({onShow:"请输入系统地址",onCorrect:"符合要求"}).inputValidator({max:255,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"最长255位"});
			})
		</script>			
	</body>
</html>
