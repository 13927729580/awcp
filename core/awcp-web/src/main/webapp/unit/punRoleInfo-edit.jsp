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
		<title>角色信息编辑</title>
		<link href="base/resources/zui/dist/css/zui.min.css" rel="stylesheet">
		<link href="base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
		<link href="base/resources/zui/assets/datetimepicker/css/datetimepicker.min.css" rel="stylesheet">
		<link href="base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/zui.css">
		<link title="default" rel="stylesheet" type="text/css" href="base/resources/content/styles/szCloud_common.css" />
		<link title="default" rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/style.css" />
		<script src="./resources/JqEdition/jquery-1.10.2.js" type="text/javascript"></script>
		<script src="resources/plugins/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
		<script src="base/resources/artDialog/dist/dialog.js"></script>
		<script src="base/resources/artDialog/lib/require.js"></script>
		<script src="base/resources/artDialog/lib/sea.js"></script>
		
		<link rel="stylesheet" href="resources/plugins/tips/tip-green/tip-green.css" type="text/css" />
		<link rel="stylesheet" href="resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
		
		<script src="./resources/JqEdition/jquery-1.10.2.js" type="text/javascript"></script>
		<script src="resources/plugins/tips/jquery.poshytip.js"></script>
		<script src="resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" type="text/javascript" charset="UTF-8"></script>
		<script src="resources/plugins/formValidator4.1.0/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
		<script type="text/javascript">
		$(function(){
	   			$.formValidator.initConfig({formID:"creatForm",debug:false,onSuccess:function(){
				   	$("#creatForm").submit();
	    		},onError:function(){alert("错误，请看提示")}});
		$("#sysId").formValidator({onFocus:"请输入系统",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
		$("#roleName").formValidator({onFocus:"请输入角色名",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
		
		})
	</script>
	</head>
	<body class="C_formBody">
					
		<div style="margin-top: 10px;margin-left:10px ;width: 100%;float: left;">
        <form class="form-horizontal" id="creatForm" action="<%=basePath%>unit/punRoleInfoSave.do" method="post">
			    <div class="C_btnGroup">
					<div class="C_btns">
	                    <button class="btn btn-info" type="submit">保存</button>
                   </div>
                   
                </div>
                 
                <div class="C_addForm">
			          <legend class=" text-center">
			          		角色信息编辑
			          	<c:if test="${result!=null&&''!=result}">
			          		<span style="color:red">(${result})</span>
			          	</c:if>
			          </legend>
			          <div class="form-group">       	
			            <label class="col-md-1 control-label required">系统</label>
			            <div class="col-md-4">
			               <input type="hidden" name="roleId" value="${vo.roleId}"/>
							 <input name="sysId" class="form-control" id="sysId" type="text" placeholder="" value="${vo.sysId}">
			            </div>
			             <label class="col-md-2 control-label required">角色名：</label>
			            <div class="col-md-4">
			                <input name="roleName" class="form-control" id="roleName" type="text" placeholder="" value="${vo.roleName}">
			            </div> 
					 </div>
				  </form>
			   </div>
       					
	</body>
</html>
