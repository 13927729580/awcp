<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
	<div style="margin: 20px 0 20px 0">
		<button type="button" class="btn btn-success" id="checkAll"><i class="icon-plus-sign"></i>全选</button>
		<button type="button" class="btn btn-info" id="uncheckAll"><i class="icon-trash"></i>全不选</button>
		<select style="height: 32px;padding: 5px 8px;font-size: 13px;line-height: 1.53846154;color: #222;
		vertical-align: middle;background-color: #fff;border: 1px solid #ccc; border-radius: 4px;" id="modules">
			<c:forEach items="${modules}" var="module">
				<option value="${module.ID }">${module.modularName }</option>
			</c:forEach>
		</select>
	</div>
	<div>
		<form class="form-horizontal" id="groupForm">
			<c:forEach items="${resultsMap}" var="res">
				<div class="form-group">
					<div class="col-md-12">${res.key}</div>
				</div>
				<div class="form-group">
					<c:forEach items="${res.value}" var="temp">
						<c:choose>
							<c:when test="${fn:contains(resoAuthor,temp.resourceId)}">
								<div class="col-md-2">
									<label class="checkbox-inline">
										<input name="optionsCheck" type="checkbox"  value="${temp.resourceId}" checked="checked"> ${temp.resourceName}
									</label>
								</div>
							</c:when>
							<c:otherwise>
								<div class="col-md-2">
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