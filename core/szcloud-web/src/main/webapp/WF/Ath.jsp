<%@page import="org.jflow.framework.common.model.AthModel"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String FK_Flow=request.getParameter("FK_Flow");
	String OID=request.getParameter("WorkID").toString();
	int oid=0;
	String FID=request.getParameter("FID");
	int fid=0;
	String FK_Node=request.getParameter("FK_Node").toString();
	int fk_node=0;
	if(OID!=null && OID.length()>0){
		oid=Integer.parseInt(OID.toString());
	}
	if(FID!=null && FID.length()>0){
		fid=Integer.parseInt(FID);
	}
	if(FK_Node!=null && FK_Node.length()>0){
		fk_node=Integer.parseInt(FK_Node);
	}
	AthModel am=new AthModel(request,response,basePath,FK_Flow,oid,fid,fk_node);
	am.init();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程附件</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<link href="<%=basePath%>WF/Comm/Style/CommStyle.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>WF/Comm/Style/Table0.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css"
	rel="stylesheet" type="text/css" />
<!--<script src="../../Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script src="../../Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script> -->
<script type="text/javascript"
	src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>

<base target="_self" />
</head>
<body class="easyui-layout">

	<form method="post"
		action="Track.do?FK_Flow=002&amp;FK_Node=&amp;WorkID=103" id="form1">
		<div data-options="region:'west',split:true,title:'功能列表'"
			style="width: 200px;">
			<ul class="navlist">
				<li><div>
						<a href='<%=basePath %>WF/track/ChartTrack.jsp?FK_Node=<%=am.getFK_Node() %>&WorkID=<%=am.getWorkID() %>&FK_Flow=<%=am.getFK_Flow() %>&FID='><span
							class='nav'>轨迹图</span></a>
					</div></li>
				<li><div style='font-weight: bold'>
						<a href='<%=basePath %>WF/WorkOpt/OneWork/Track.jsp?FK_Node=<%=am.getFK_Node() %>&WorkID=<%=am.getWorkID() %>&FK_Flow=<%=am.getFK_Flow()%>'><span
							class='nav'>流程日志</span></a>
					</div></li>
				<li><div>
						<a href='<%=basePath%>WF/Ath.jsp?FK_Node=<%=am.getFK_Node() %>&WorkID=<%=am.getWorkID() %>&FK_Flow=<%=am.getFK_Flow() %>&FID=0'><span
							class='nav'>流程附件</span></a>
					</div></li>
				<li><div>
						<a href='<%=basePath %>WF/OP.jsp?FK_Node=<%=am.getFK_Node() %>&WorkID=<%=am.getWorkID() %>&FK_Flow=<%=am.getFK_Flow() %>&FID='><span
							class='nav'>操作</span></a>
					</div></li>
			</ul>
		</div>
		<%=am.Pub1.toString() %>
	</form>
</body>
</html>
