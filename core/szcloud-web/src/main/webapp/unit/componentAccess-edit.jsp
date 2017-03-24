<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
		
				<div class="row" id="dataform">
			<form class="form-horizontal" id="groupForm">
				<c:forEach items="${resultsMap}" var="res">
				<div class="form-group"> 
					<div class="col-md-12">${res.key}</div> 
					<c:forEach items="${res.value}" var="temp">
					<c:choose>
					<c:when test="${fn:contains(resoAuthor,temp.resourceId)}">
					<div class="col-md-3">
						<label class="checkbox-inline"> 
							<input name="optionsCheck" type="checkbox"  value="${temp.resourceId}" checked="checked"> ${temp.resourceName}
						</label> 
					</div>
					</c:when>
					<c:otherwise>
					<div class="col-md-3">
						<label class="checkbox-inline"> 
							<input name="optionsCheck" type="checkbox"  value="${temp.resourceId}"> ${temp.resourceName}
						</label> 
					</div>
					</c:otherwise>
					</c:choose>
					</c:forEach>
				</div>
				</c:forEach>
			</form>
			</div>
						

