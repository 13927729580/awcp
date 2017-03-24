<%@page import="org.szcloud.framework.unit.controller.constants.SessionContants"%>
<%@page import="org.szcloud.framework.core.utils.SessionUtils"%>
<%@page import="org.szcloud.framework.unit.vo.PunManageUserBaseInfoVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
	<%
		SessionUtils.clearSession();
			PunManageUserBaseInfoVO user = (PunManageUserBaseInfoVO)SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
			if (user == null) {
		request.getRequestDispatcher("/manageAdmin/login.jsp").forward(
				request, response);
			} else {
		response.sendRedirect(request.getContextPath()
				+ "/manageAdmin/main.do");
			}
	%>
</body>
</html>