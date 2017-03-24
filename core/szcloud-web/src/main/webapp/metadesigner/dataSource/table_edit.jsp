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
 		<title>数据源</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_form_css.jsp" %>
	</head>
	<body id="main">
		
			
		<div id="groupform">
	<div class="col-md-3">
		<div class="panel">
            <div class="panel-heading">数据库表</div>
            <div class="panel-body">
            	<c:forEach items="${tables}" var="table">
            		<ul><a href="<%=basePath%>dataSourceManage/table_edit.do?modelCode="+${tabel.modelCode}>${table.tableName}</a></ul>
            	</c:forEach>
            </div>
          </div>
	</div>
	<div class="col-md-9">
		<form  method="post" id="manuList">	
			<label class="col-md-2 control-label">SQL</label>
			<div class="col-md-4">
				<textarea name='sqlScript' id='sqlScript' rows='4' 
					class='form-control'>${sqlScript}</textarea>
			</div>
		</form>
		<div class="btn-group" style="margin-bottom:20px">
           <button type="button" class="btn btn-sm btn-primary">查询</button>
         </div>
		<form  method="post" id="manuList">	
        <input type="hidden" name="currentPage" value="${currentPage}">	
		<table class="table datatable table-bordered">
			<thead>
				<tr>
					<c:forEach items="${items}" var="item">
            			<th>${item.itemCode}</th>
            		</c:forEach>
				</tr>
			</thead>
			<tbody>
				
			<c:forEach items="${maps}" var="map">
				<tr>
						<c:forEach items="${items}" var="item">
            				<td>${map[item.itemCode]}</td>
            			</c:forEach>
				</tr>
			</c:forEach> 
			</tbody>
			
			<tfoot>
			</tfoot>
		</table>
		</form>
		<!-- <ul class="pager pager-pills" style="margin:0;">
		          <li class="previous disabled"><a href="javascript:;">«</a></li>
		          <li class="next"><a href="javascript:;">»</a></li>
		        </ul> -->
	</div>
</div>
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
       		$(document).ready(function(){
       			
       			});
         </script>	
	</body>
</html>