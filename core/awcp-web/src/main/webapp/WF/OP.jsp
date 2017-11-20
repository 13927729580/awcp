
<%@page import="org.jflow.framework.common.model.OpModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String DoType=request.getParameter("DoType");
	String FK_Node=request.getParameter("FK_Node");
	String FK_Flow=request.getParameter("FK_Flow");
	String WorkID=request.getParameter("WorkID");
	int workID=0;
	String FID=request.getParameter("FID");
	int fid=0;
	if(WorkID!=null && WorkID.length()>0){
		workID=Integer.parseInt(WorkID);
	}
	if(FID!=null && FID.length()>0){
		fid=Integer.parseInt(FID);
	}
	OpModel op=new OpModel(request,response,basePath,DoType,FK_Node,FK_Flow,workID,fid);
	op.init();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>操作</title>
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
<link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet"
	type="text/css" />
<script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js"
	type="text/javascript"></script>

<script src="<%=basePath%>WF/Comm/JScript.js" type="text/javascript"></script>
<script type="text/javascript">
	function NoSubmit(ev) {
		if (window.event.srcElement.tagName == "TEXTAREA")
			return true;

		if (ev.keyCode == 13) {
			window.event.keyCode = 9;
			ev.keyCode = 9;
			return true;
		}
		return true;
	}
	function DoFunc(doType, workid, fk_flow, fk_node) {

		if (doType == 'Del' || doType == 'Reset') {
			if (confirm('您确定要执行吗？') == false)
				return;
		}

		var url = '';
		if (doType == 'HungUp' || doType == 'UnHungUp') {
			url = '<%=basePath%>WF/WorkOpt/HungUp.jsp?WorkID=' + workid + '&FK_Flow=' + fk_flow
					+ '&FK_Node=' + fk_node;
			var str = window
					.showModalDialog(url, '',
							'dialogHeight: 350px; dialogWidth:500px;center: no; help: no');
			if (str == undefined)
				return;
			if (str == null)
				return;
			//this.close();
			window.location.href = window.location.href;
			return;
		}
		url = '<%=basePath%>WF/OP.jsp?DoType=' + doType + '&WorkID=' + workid + '&FK_Flow='
				+ fk_flow + '&FK_Node=' + fk_node;
		window.location.href = url;
	}
	function Takeback(workid, fk_flow, fk_node, toNode) {
		if (confirm('您确定要执行吗？') == false)
			return;
		var url = '<%=basePath%>WF/GetTask.jsp?DoType=Tackback&FK_Flow=' + fk_flow
				+ '&FK_Node=' + fk_node + '&ToNode=' + toNode + '&WorkID='
				+ workid;
		window.location.href = url;
	}
	function UnSend(fk_flow, workID, fid) {

		//            var url = "CancelWork.aspx?WorkID=" + workID + "&FK_Flow=" + fk_flow+"&FID="+fid+"&FK_Node="+"";
		//            WinShowModalDialog_Accepter(url);

		if (confirm('您确定要执行撤销吗?') == false)
			return;

		var url = "<%=basePath%>WF/OP.jsp?DoType=UnSend&FK_Node=&FK_Flow=" + fk_flow
				+ "&WorkID=" + workID;
		$.post(url, null, function(msg) {
			$('#winMsg').html(msg);
			$('#winMsg').window('open');
		});
	}
</script>

<base target="_self" />
</head>
<body class="easyui-layout">
	<form method="post"
		action="OP.aspx?FK_Node=&amp;WorkID=101&amp;FK_Flow=001&amp;FID="
		id="form1">
		<div class="aspNetHidden">
			<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE"
				value="/wEPDwULLTEwMzkwMjAzNzRkZMKr+CiBS9vo5yOAfDxLLwd9BXHcaC729z+aXdEmjhWU" />
		</div>

		<div data-options="region:'west',split:true,title:'功能列表'"
			style="width: 200px;">
			<ul class="navlist">
				<li>
					<div>
						<a href='<%=basePath %>WF/track/ChartTrack.jsp?FK_Node=<%=op.getFK_Node() %>&WorkID=<%=op.getWorkID() %>&FK_Flow=<%=op.getFK_Flow() %>&FID='><span
							class='nav'>轨迹图</span></a>
					</div>
					</li>
				<li>
					<div>
						<a href='<%=basePath %>WF/WorkOpt/OneWork/Track.jsp?FK_Node=<%=op.getFK_Node() %>&WorkID=<%=op.getWorkID() %>&FK_Flow=<%=op.getFK_Flow()%>'><span
							class='nav'>流程日志</span></a>
					</div>
					</li>
				<li><div>
						<a href='<%=basePath %>WF/Ath.jsp?FK_Node=<%=op.getFK_Node() %>&WorkID=<%=op.getWorkID() %>&FK_Flow=<%=op.getFK_Flow() %>&FID='><span
							class='nav'>流程附件</span></a>
					</div></li>
				<li><div style='font-weight: bold'>
						<a href='<%=basePath %>WF/OP.jsp?FK_Node=<%=op.getFK_Node() %>&WorkID=<%=op.getWorkID() %>&FK_Flow=<%=op.getFK_Flow() %>&FID='><span
							class='nav'>操作</span></a>
					</div></li>

			</ul>
		</div>
		<%=op.Pub2.toString() %>
	</form>
</body>
</html>
