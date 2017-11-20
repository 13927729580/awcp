<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>   
 <%@page isELIgnored="false"%> 
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
out.print("系统选择");
%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统选择页面</title>
</head>
<body>
	<input type="hidden" id="basePath" value="<%=basePath%>"/>
	<h1>${result}</h1><br/>
	<form action="punSystemList.do">
	<center>
	<table border="1">
		<tr>
			<td colspan="2">系统名：</td>
			<td colspan="2"><input type="text" name="sysName"></td>
			<td colspan="2">系统代称：</td>
			<td colspan="2">
				<input type="text" name="sysShortName">
				<input type="hidden" name="currentPage" value="0"/>
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center"><input type="submit" value="提交"></td>
		</tr>
		</table>
	</center>
	</form>
	<center>
	<form id="chooseSys" method="post">
	<table border="1">
		<tr>
			<th>选择</th>
			<th>系统名</th>
			<th>系统代称</th>
			<th>系统地址</th>
		</tr>
		<c:forEach items="${sysVOs}" var="vo">
			<tr>
				<td>
					<input type="radio" name="chooseSys" value="${vo.sysId}"/>
				</td>
				<td>
					${vo.sysName}
				</td>
				<td>
					${vo.sysShortName}
				</td>
				<td>
					${vo.sysAddress}
				</td>
			</tr>
		</c:forEach>
			<tr>
			<td align="center" colspan="8">
			<a href="<%=basePath%>unit/punSystemList.do?currentPage=1">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<c:if test="${currentPage>1}">
			<a href="<%=basePath%>unit/punSystemList.do?currentPage=${currentPage-1 }">上一页</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</c:if>
			<c:if test="${currentPage<maxPage}">
			<a href="<%=basePath%>unit/punSystemList.do?currentPage=${currentPage+1 }">下一页</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</c:if>
			<a href="<%=basePath%>unit/punSystemList.do?currentPage=${maxPage}">尾页</a>
			</td>
			</tr>
			<tr>
				<td align="center"><input type="button" id="chooseBtn" value="提交"/></td>
			</tr>
	</table>
	</form>
	</center>
	
	<script src="../base/resources/zui/assets/jquery.js"></script>
	<script type="text/javascript">
		$("#chooseBtn").click(function(){
			var basePath = $("#basePath").val();
			var url = basePath+"unit/inSysChooseForMem.do";
			$("#chooseSys").attr("action",url).submit();
			return false;
		})
	</script>
	
</body>
</html>