<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String operation = request.getParameter("operation");
	String keywordId = request.getParameter("keywordId");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>菜单查询页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_css.jsp" %><!-- 注意加载路径 -->
	</head>
	<body id="main">
		<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="saveBtn"><i class="icon-plus-sign"></i>保存</button>
		</div>
		<div id="editDiv">
			<form class="form-horizontal" id="editForm" action="<%=basePath%>manage/saveOrUpdateKeyword.do" method="post">
			<input id="keywordId" name="id" type="hidden">
				<div class="form-group">
					<label class="col-md-2 control-label">关键字名：</label>
					<div class="col-md-4">
						<input name="dataValue" class="form-control" id="dataValue" type="text" >
					</div>
				</div>
			</form>
		</div>
		<%@ include file="/resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>

		<script type="text/javascript">
		var basePath = "<%=basePath%>";
		var operation = "<%=operation%>";
		var keywordId = "<%=keywordId%>";
		$(function(){
          	$("#saveBtn").click(function(){
          		//$("#editForm").submit();
          		
          		var dataValue=$("#dataValue").val();
          		$.ajax({
          			type:'post',
          			url:basePath + "manage/saveOrUpdateKeyword.do",
          			data:{id:keywordId,dataValue:dataValue},
          			success:function(msg){
          				if(msg=="0"){
          					alertMessage("该关键字已存在！");
          				}else{
          					try {
								var dialog = top.dialog.get(window);
							} catch (e) {
								return;
							}
          					
          					if(dialog != null){
								dialog.close(msg);
								dialog.remove();
							}
          					
          				}
          			}
          		});
          	});
          	if(operation=='2'){
          		$("#keywordId").val(keywordId);
          		$.ajax({
          			type:'post',
          			url:basePath + "manage/getKeywordById.do",
          			data:{id:keywordId},
          			success:function(msg){
          				$("#dataValue").val(msg.dataValue);
          			}
          		});
          	}
			
          });
          
		</script>
	</body>
</html>
