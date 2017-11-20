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
 		<title>角色编辑页面</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %>
		
	</head>
	<body id="main">
		<div class="container-fluid">
<!-- 			<div class="row" id="breadcrumb"> -->
<!-- 				<ul class="breadcrumb"> -->
<!-- 		          <li><i class="icon-location-arrow icon-muted"></i></li> -->
<!-- 		          <li><a href="#">首页</a></li> -->
<!-- 		          <li><a href="#">我的工作室</a></li> -->
<%-- 		          <li><a href="#">${sys.sysName }</a></li> --%>
<!-- 		          <li class="active">角色编辑</li> -->
<!-- 		        </ul> -->
<!-- 			</div> -->
			
			<div class="row" id="dataform">
				<legend class=" text-center">
					角色编辑
	          		<c:if test="${result!=null&&''!=result}">
	          			<span style="color:red">(${result})</span>
	          		</c:if>
	          	</legend>
        		<form class="form-horizontal" id="creatForm" action="<%=basePath%>unit/saveRoleInSys.do" method="post">
			          <input type="hidden" name="sysId" value="${vo.sysId}"/>
			          <input type="hidden" name="roleId" value="${vo.roleId}"/>
			          <div class="form-group">  
			             <label class="col-md-3 control-label required">角色名：</label>
			            <div class="col-md-4">
			                <input name="roleName" class="form-control" id="roleName" type="text" placeholder="" value="${vo.roleName}">
			            </div> 
					 </div>
			          <div class="form-group">  
			             <label class="col-md-3 control-label required">描述：</label>
			            <div class="col-md-4">
			                <input name="dictRemark" class="form-control" id="dictRemark" type="text" placeholder="" value="${vo.dictRemark}">
			            </div> 
					 </div>
					<div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-2 col-md-10">
			              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
							<a href="<%=basePath%>unit/listRolesInSys.do?boxs=${sysId}" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
			            </div>
		          </div>
			</form>
			</div>
		</div>
       	<%@ include file="/resources//include/common_form_js.jsp" %>						
       <script type="text/javascript">
		$(function(){
	   		$.formValidator.initConfig({formID:"creatForm",debug:false,onSuccess:function(){
				   	$("#creatForm").submit();
	    	},onError:function(){alert("错误，请看提示")}});
			$("#roleName").formValidator({onFocus:"请输入角色名",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"1~20位"});
			$("#dictRemark").formValidator({onFocus:"请输入角色描述",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:255,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"1~255位"});
		})
	</script>	
	</body>
</html>
