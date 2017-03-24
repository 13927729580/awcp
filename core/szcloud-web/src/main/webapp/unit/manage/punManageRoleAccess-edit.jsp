<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>   
 <%@page isELIgnored="false"%> 
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8">
		<title>角色权限编辑</title>
		<link href="<%=basePath %>base/resources/zui/dist/css/zui.min.css" rel="stylesheet">
		<link href="base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
		<link href="base/resources/zui/assets/datetimepicker/css/datetimepicker.min.css" rel="stylesheet">
		<link href="base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/zui.css">
		<link title="default" rel="stylesheet" type="text/css" href="base/resources/content/styles/szCloud_common.css" />
		<link title="default" rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/style.css" />
		<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js" type="text/javascript"></script>
		<script src="base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
		<script src="base/resources/artDialog/dist/dialog.js"></script>
		<script src="base/resources/artDialog/lib/require.js"></script>
		<script src="base/resources/artDialog/lib/sea.js"></script>
		
		<link rel="stylesheet" href="resources/plugins/tips/tip-green/tip-green.css" type="text/css" />
		<link rel="stylesheet" href="resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
		
		
		<script src="resources/plugins/tips/jquery.poshytip.js"></script>
		<script src="resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" type="text/javascript" charset="UTF-8"></script>
		<script src="resources/plugins/formValidator4.1.0/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
		<script type="text/javascript">
		$(function(){
	   			$.formValidator.initConfig({formID:"submitForm",debug:false,onSuccess:function(){
				   	$("#submitForm").submit();
	    		},onError:function(){alert("错误，请看提示")}});
		$("#roleId").formValidator({onFocus:"请输入角色ID",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
		})
	</script>
	</head>
	<body class="C_formBody">
					
		<div style="margin-top: 10px;margin-left:10px ;width: 100%;float: left;">
        <form class="form-horizontal" id="submitForm" action="<%=basePath%>unit/punRoleAccessSave.do" method="post">
			    <div class="C_btnGroup">
					<div class="C_btns">
	                    <button class="btn btn-info" type="submit">保存</button>
                   </div>
                   
                </div>
                 
                <div class="C_addForm">
			          <legend class=" text-center">
			          		角色权限信息编辑
			          	<c:if test="${result!=null&&''!=result}">
			          		<span style="color:red">(${result})</span>
			          	</c:if>
			          </legend>
			          <div class="form-group">       	
			            <label class="col-md-1 control-label required">角色：</label>
			            <div class="col-md-4">
			               <input type="hidden" name="roleAccId" value="${vo.roleAccId}"/>
							 <input name="roleId" class="form-control" id="roleId" type="text" placeholder="" value="${vo.roleId}">
			            </div>
			            <label class="col-md-2 control-label required">操作：</label>
			            <div class="col-md-4">
							<input name="operType" class="form-control" id="operType" type="text" placeholder="" value="${vo.operType}">
<!-- 			           		<select name="operType" class="form-control"> -->
<%-- 							<c:forEach items="${operTypes}" var="operType"> --%>
<%-- 								<c:choose> --%>
<%-- 									<c:when test="${vo.operType} eq 'operType.dataValue'}"> --%>
<!-- 										hello -->
<%-- 										<option selected="selected" value="${operType.dataValue}"> --%>
<%-- 											${operType.dataKey}</option> --%>
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<%-- 										<option value="${operType.dataValue}">${operType.dataKey}</option> --%>
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose> --%>
<%-- 							</c:forEach> --%>
<!-- 						</select> -->
			            </div>
			        	
				</div>
				<div class="form-group"> 
					<label class="col-md-1 control-label required">资源：</label>
					<div class="col-md-4">
						<input type="text" class="form-control" name="resourceId" value="${vo.resourceId}" />
<%-- 						<c:if test="${fn:length(vos)==0}"> --%>
<%-- 							<input type="text" class="form-control" name="resIDs" value="${vo.resourceId}" /> --%>
<%-- 						</c:if> --%>
<%-- 						<c:forEach items="${vos}" var="vo" varStatus="status"> --%>
<%-- 							<c:if test="${status.count%5==0}"> --%>
<!-- 								<br /> -->
<%-- 							</c:if> --%>
<%-- 							<input type="checkbox" name="resIDs" value="${vo.resourceId}" />${vo.resourceName}&nbsp;&nbsp; --%>
<%-- 						</c:forEach> --%>
					</div>
				</div>
				 </form>
				  
			   </div>
       					
	</body>
</html>
