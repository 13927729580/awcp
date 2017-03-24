<%@page import="org.szcloud.framework.core.utils.SessionUtils"%>
<%@ page import="org.szcloud.framework.unit.vo.PunDevUserBaseInfoVO" %>
<%@ page import="org.szcloud.framework.unit.controller.constants.SessionContants" %>
<%
SessionUtils.clearSession();
PunDevUserBaseInfoVO devUserVO = (PunDevUserBaseInfoVO)SessionUtils.getObjectFromSession(SessionContants.CURRENT_USER);
if (devUserVO == null) {
	request.getRequestDispatcher("/devAdmin/login.jsp").forward(request, response);
}else{
	response.sendRedirect(request.getContextPath() + "/devAdmin/list.jsp");
}
%>
