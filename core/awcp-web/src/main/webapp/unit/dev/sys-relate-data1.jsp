<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>系统管理</title>
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="/resources/include/common_css.jsp"%><!-- 注意加载路径 -->
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row">
				<form class="form-horizontal" id="groupForm" method="post">
						
				<div class="form-group">
					<label class="col-md-2 control-label required">系统名称:</label>
					<input id="id" name="id" type="hidden" value="${sys.sysId}"> </input>
					<div class="col-md-4">
						<input name="menuName" class="form-control" id="menuName"
							type="text" placeholder="" value="${sys.sysName}" readonly="readonly">
					</div>
					<label class="col-md-2 control-label required">数据源:</label>
					<div class="col-md-4">
						<select data-placeholder="请选择数据源..." id="defaultDataSource" class="chosen-select form-control" tabindex="2" name="defaultDataSource">
							<c:forEach var="item" items="${dataVOs}">
								<option value="${item.id}"  >${item.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</form>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="relatesysBtn"><i class="icon-plus-sign"></i>确认</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>取消</button>
			</div>
	</div>

	<%@ include file="/resources/include/common_js.jsp"%>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
	<script type="text/javascript">
		  $(function(){
		  try {
				var dialog = top.dialog.get(window);
				} catch (e) {
					return;
				}
          	//新增
      		$("#relatesysBtn").click(function(){
      			$.ajax({
						type:"post",
						url:"<%=basePath%>dataSys/relate.do",
						data:{"systemId":$("#id").val(),"dataSourceId":$("#defaultDataSource").val()},
						success:function(data){
							if(data=='1'){
								alert("成功");
								dialog.close($("#id").val());
							}else{
								alert("失败");
								dialog.close($("#id").val());
							}
						}
       				});
      		});
          	//delete
          	$("#deleteBtn").click(function(){
      			dialog.close($("#id").val());
			})
		});
		 
	</script>
</body>
</html>
