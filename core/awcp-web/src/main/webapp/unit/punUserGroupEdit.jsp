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
<meta charset="utf-8">
<title>组信息编辑</title>

<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->

</head>
<body id="main">

		<div class="container-fluid">

			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
					<li><i class="icon-location-arrow icon-muted"></i></li>
					<li><a >组织机构与权限</a></li>
					<li><a href="<%=basePath%>punUserGroupController/getUserList.do?groupId=${groupId}&rootGroupId=${rootGroupId}">人员信息管理</a></li>
					<li class="active">编辑岗位信息</li>
				</ul>
			</div>
			
		<form method="post" id="userGroup" action="punUserGroupController/update.do">
			<div class="row" id="buttons">
				<button class="btn btn-info" id="allchecker" type="submit">
					<i class="icon-save-sign"></i>保存
				</button>
		    </div>
			
			<div class="row" id="dataform">
	
			<input name="groupId" id="groupId" type="hidden" value="${groupId }">
	     	<input name="rootGroupId" id="rootGroupId" type="hidden" value="${rootGroupId }">
	     	<input name="userId" id="userId" type="hidden" value="${user.userId }">
			<table class="table table-condensed table-hover table-striped tablesorter table-fixed" id="tasktable">
	          <tfoot>
              </tfoot>
	           <div class="col-md-10">
				              <c:forEach items="${positionList}" var="p">
				              	<input name="positionId" type="checkbox" value="${p.positionId }" 
				              		<c:forEach items="${user.positionList}" var="g">
				              			<c:if test="${p.positionId == g.positionId }">checked="true"</c:if> 
				              		</c:forEach>
				              	/>
			            		${p.name }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			            	  </c:forEach>
			    </div>
			    <div class="col-md-10">
			    	<c:choose>
			    		<c:when test="${userGroup.isManager}">
			    			是否管理岗  <input id="isManager" name="isManager" type="checkbox" checked="checked"/>
			    		</c:when>
			    		<c:otherwise>
			           		是否管理岗  <input id="isManager" name="isManager" type="checkbox" value="1"/>
			           	</c:otherwise>
			        </c:choose>
				</div>
	      </table>
	      </div>
	  </form>

		    </div>

		</div>
		 <div class="C_pagerBox  clearfix">
	      <div class="C_pager" style="width:480px;">
	                <ul class="pager pager-loose">
						<sc:PageNavigation dpName="vos"></sc:PageNavigation>
	                </ul>            
	      </div>

		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script type="text/javascript">
		$('#allchecker').click( function(){
  			//alert("dd");
  			var url = <%=basePath%>+"punUserGroupController/update.do";
			$("#userGroup").attr("action",url).submit();
			return false;
  		});
	</script>


</body>
</html>
