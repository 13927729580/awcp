<%@page import="BP.WF.Glo"%>
<%@page import="org.jflow.framework.common.model.PortModel"%>
<%@page import="BP.WF.DoWhatList" %>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	PortModel portModel= new PortModel(request, response);
	portModel.loadPage();
	
	String basePath = Glo.getCCFlowAppPath();
%>
<%	
	// 判断模式
	String doWhat = portModel.getDoWhat();
	
	if(doWhat.equals(DoWhatList.JiSu)){
		// 极速模式
		%>
		<jsp:include page="./App/Simple/Default.jsp"/>
		<%
	}else if(doWhat.equals(DoWhatList.Start5)){
		// 经典模式
		%>
		<jsp:include page="Default.jsp"/>
		<%
	}
%>


