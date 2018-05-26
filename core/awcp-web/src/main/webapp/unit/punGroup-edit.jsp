<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<title>组信息编辑</title>
	<%@ include file="/resources/include/common_lte_css.jsp"%>
</head>
<body>
	<section class="content">
		<ul class="nav nav-tabs">
			<li class="active"><a href="javascript:;">组织信息编辑</a></li>
			<li><a href="<%=basePath%>unit/punGroupEdit.do?boxs=${vo.groupId}">组织树编辑</a></li>
			<li><a href="<%=basePath%>punPositionController/pageList.do?groupId=${vo.groupId}">职务</a></li>
		</ul>
		<div style="margin-top:20px;"></div>
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<form class="form-horizontal" id="groupForm" action="<%=basePath%>unit/punGroupSave.do" method="post">
							<input type="hidden" name="groupId" value="${vo.groupId}"/>
							<div class="C_addForm">
								<legend class=" text-center">
									组织信息编辑
									<c:if test="${result!=null&&''!=result}">
										<span style="color: red">(${result})</span>
									</c:if>
								</legend>
								<div class="form-group">
									<label class="col-md-1 control-label required">组名</label>
									<div class="col-md-4">
						                <input name="groupChName" class="form-control" id="groupChName" type="text" value="${vo.groupChName}">
						            </div> 
									<label class="col-md-2 control-label required">组简称</label>
						            <div class="col-md-4">
						                <input name="groupShortName" class="form-control" id="groupShortName" type="text" placeholder="" value="${vo.groupShortName}">
						            </div> 
							 </div>
								  <div class="form-group">   
								 	<label class="col-md-1 control-label">传真</label>
						            <div class="col-md-4">
						                <input name="fax" class="form-control" id="fax" type="text" placeholder="" value="${vo.fax}">
						            </div> 
						            <label class="col-md-2 control-label">组名（英文）</label>
						            <div class="col-md-4">
						                <input name="groupEnName" class="form-control" id="groupEnName" type="text" placeholder="" value="${vo.groupEnName}">
						            </div>
								 </div>
								 <div class="form-group">   
								 	<label class="col-md-1 control-label">	组织机构代码</label>
						            <div class="col-md-4">
						                <input name="orgCode" class="form-control" id="orgCode" type="text" placeholder="" value="${vo.orgCode}">
						            </div> 
						            <label class="col-md-2 control-label">地址</label>
						            <div class="col-md-4">
						                <input name="groupAddress" class="form-control" id="groupAddress" type="text" placeholder="" value="${vo.groupAddress}">
						            </div> 
								 </div>
							
								  <div class="form-group">   
								 	<label class="col-md-1 control-label">邮编</label>
						            <div class="col-md-4">
						                <input name="zipCode" class="form-control" id="zipCode" type="text" placeholder="" value="${vo.zipCode}">
						            </div> 
						            <label class="col-md-2 control-label">联系电话</label>
						            <div class="col-md-4">
						                <input name="contactNumber" class="form-control" id="contactNumber" type="text" placeholder="" value="${vo.contactNumber}">
						            </div>
								 </div>
								 <div class="form-group">   
						            <label class="col-md-1 control-label">业务范围</label>
						            <div class="col-md-4">
						                <input name="groupBusinessSphere" class="form-control" id="groupBusinessSphere" type="text" placeholder="" value="${vo.groupBusinessSphere}">
						            </div> 
								 </div>
								 <div class="form-group"><!-- 表单提交按钮区域 -->
					            <div class="col-md-offset-2 col-md-10">
					              	<a class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</a>
					            </div>
					        </div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</section>
	<script src="<%=basePath%>resources/scripts/en_Us_error_message.js"></script>
	<script src="<%=basePath%>template/AdminLTE/js/jquery.min.js"></script>
	<script src="<%=basePath%>venson/js/common.js"></script>	
	<script type="text/javascript">
		$(function() {
		    $("#saveBtn").on("click",function(){
                if(Comm.validOldForm()){
                    $("#groupForm").submit();
                }
            })
        })
	</script>


</body>
</html>
