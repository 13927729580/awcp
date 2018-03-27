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
	<meta charset="utf-8">
	<title>函数编辑页面</title>
	<%@ include file="/resources/include/common_lte_css.jsp"%>
</head>
<body id="main">
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<form class="form-horizontal" id="groupForm" action="<%=basePath%>func/save.do" method="post">					
							<div class="form-group">
		           			 	<div class="col-md-offset-2 col-md-10">
		              				<c:if test="${message!=null&&''!=message}">
										<span style="color: red">(${message})</span>
									</c:if>
		            			</div>
		       				</div>
							<div class="form-group">
		           			 	<div class="col-md-offset-2 col-md-10">
		              				<button type="button" class="btn btn-success" id="saveBtn"></i>保存</button>
		              				<button type="button" class="btn btn-default" id="undoBtn"></i>取消</button>
		            			</div>
		       				</div>
						 	<input type="hidden" name="pageId" value="${vo.pageId}"/>	
					  		<input type="hidden" name="code" value="${vo.code}"/>	
							<div class="form-group">       	
					            <label class="col-md-2 control-label required">函数库名称</label>
					            <div class="col-md-10">
					                <input name="name" class="form-control" type="text" value="${vo.name}">
					            </div>
					 	    </div>
							<div class="form-group">       	
					            <label class="col-md-2 control-label required">描述</label>
					            <div class="col-md-10">
					                <input name="description" class="form-control" type="text" value="${vo.description}">
					            </div>  
					 	    </div>
							<div class="form-group">       	
					            <label class="col-md-2 control-label required">脚本</label>
					            <div class="col-md-10">
					                <textarea name="script" class="form-control" rows="20">${vo.script}</textarea>
					            </div>
							</div>						
						</form>
					</div>
				</div>
			</div>
		</div>
	</section>		
	<%@ include file="/resources/include/common_form_js.jsp" %>
	<script type="text/javascript">
        $(document).ready(function(){        	 				   	
	    	$("#saveBtn").click(function(){
	    		$("#groupForm").submit();
				return false;
			});
	    	
	    	$("#undoBtn").bind("click",function(){
	    		location.href = "<%=basePath %>func/list.do";
	    	});
        });
	</script>	
</body>
</html>
