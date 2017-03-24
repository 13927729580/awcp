<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/head/head.jsp"%>
<%
	/* String name = request.getParameter("name")==null?"":request.getParameter("name");
	try{
		if(!"".equals(name)){
			Emp emp = new Emp(name);
			//ContextHolderUtils.setResponse(response);
			WebUser.SignInOfGener(emp);
		}else{
			if("".equals(WebUser.getNo())){
				response.sendRedirect(basePath+"WF/Login.jsp?errNo=1");
			}
		}
	}catch(Exception e){
		response.sendRedirect(basePath+"WF/Login.jsp?errNo=1");
		return;
	} */
	if(StringHelper.isNullOrEmpty(WebUser.getNo())){
		response.sendRedirect(basePath+"WF/Login.jsp?errNo=1");
		return;
	}else{
		try{
			WebUser.getName();
		}catch(Exception e){
			response.sendRedirect(basePath+"WF/Login.jsp?errNo=1");
			return;
		}
	}
%>
</head>
<frameset rows="8%,92%" border="0">
	<frame src="Top.jsp" name="top" scrolling="no"></frame>
	<frameset cols="150px,800px" border="0">
		<frame src="Left.jsp" name="left" scrolling="auto"></frame>
		<frame src="Welcome.jsp" name="right" scrolling="auto"></frame>
	</frameset>
</frameset>
</html>
