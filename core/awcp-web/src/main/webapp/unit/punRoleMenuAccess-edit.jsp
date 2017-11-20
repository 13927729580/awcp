<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<div class="whole" style="margin:20px;">
<!-- 	<div class="C_btns"> -->
 		<%-- <a class="btn btn-info" href="<%=basePath%>unit/punRoleAccessList.do?currentPage=0">保存并返回</a> --%> 
<%-- 		<a class="btn btn-info" href="<%=basePath%>unit/listRolesInSys.do?boxs=${vo.sysId}">保存并返回</a> --%>
<!-- 	</div> -->
	<div style="margin-top:20px;">
      <div class="CloudTreeMenu"></div>    
   </div>
</div>
<p><%-- ${menuJson} --%></p>
