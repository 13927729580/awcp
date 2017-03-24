<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="BP.Port.*"%>
<%@page import="org.jflow.framework.common.model.TaskPoolSharingModel"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.DA.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	TaskPoolSharingModel tpsm = new TaskPoolSharingModel(request,
	response);
	String htmlStr = tpsm.init();
	String name = WebUser.getNo();

	try {
		Emp emp = new Emp(name);
		//ContextHolderUtils.setResponse(response);
		WebUser.SignInOfGener(emp);
	} catch (Exception e) {
		response.sendRedirect(basePath + "WF/Login.jsp?errNo=1");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>驰骋工作流程管理系统</title>
<link rel="stylesheet" href="assets/css/amazeui.min.css" />
<link rel="stylesheet" href="assets/css/admin.css">
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/app.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
<script type="text/javascript">
$(function () {
    var userStyle = "ccflow默认";
    $('#winOpencss').attr('href', 'Style/FormThemes/WFWinOpen.css');
    $('#freeFormcss').attr('href', 'Style/FormThemes/Table0.css');

});
var NS4 = (document.layers);
var IE4 = (document.all);
var win = window;
var n = 0;
function findInPage(str) {
    alert(document.getElementById('string1'));
    str = document.getElementById('string1').value;
    //    alert(str);
    var txt, i, found;
    if (str == "")
        return false;
    if (NS4) {
        if (!win.find(str))
            while (win.find(str, false, true))
                n++;
        else
            n++;
        if (n == 0)
            alert("对不起！没有你要找的内容。");
    }
    if (IE4) {
        txt = win.document.body.createTextRange();
        for (i = 0; i <= n && (found = txt.findText(str)) != false; i++) {
            txt.moveStart("character", 1);
            txt.moveEnd("textedit");
        }
        if (found) {
            txt.moveStart("character", -1);
            txt.findText(str);
            txt.select();
            txt.scrollIntoView();
            n++;
        }
        else {
            if (n > 0) {
                n = 0;
                findInPage(str);
            }
            else
                alert("对不起！没有你要找的内容。");
        }
    }
    return false;
}

function SetImg(appPath, id) {
    document.getElementById(id).src = appPath + 'WF/Img/Mail_Read.png';
}

function GroupBarClick(appPath, rowIdx) {
    var alt = document.getElementById('Img' + rowIdx).alert;
    var sta = 'block';
    if (alt == 'Max') {
        sta = 'block';
        alt = 'Min';
    } else {
        sta = 'none';
        alt = 'Max';
    }
    document.getElementById('Img' + rowIdx).src = appPath + 'WF/Img/' + alt + '.gif';
    document.getElementById('Img' + rowIdx).alert = alt;
    var i = 0;
    for (i = 0; i <= 5000; i++) {
        if (document.getElementById(rowIdx + '_' + i) == null)
            continue;

        if (sta == 'block') {
            document.getElementById(rowIdx + '_' + i).style.display = '';
        } else {
            document.getElementById(rowIdx + '_' + i).style.display = sta;
        }
      
    }
}
function WinOpenIt(url, workid) {
    if (window.confirm('您确定要申请下来该任务吗？') == false) {
        return;
    }
    var doUrl = '<%=basePath%>WF/Do.jsp?ActionType=DoAppTask&WorkID='
				+ workid;
		var str = window
				.showModalDialog(
						doUrl,
						'',
						'dialogHeight: 50px; dialogWidth:50px; dialogTop: 100px; dialogLeft: 100px; center: no; help: no');
		if (str == undefined)
			return;
		if (str == null)
			return;
		var newWindow = window
				.open(
						url,
						'_blank',
						'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
		newWindow.focus();
		window.location.href = window.location.href;
		return;
	}
</script>
<style>

.input_text {
	border: 1px solid #CED5DB;
	height: 20px;
	width: 100px;
}

.input_button {
	background: #FED778;
	border: 1px solid #DEA203;
	color: #853433
}

table {
	border-collapse: collapse;
}

.table_list {
	margin-top: 30px;
}

.table_list tr td {
	border: 1px solid #CED5DB;
	text-align: center;
	line-height: 28px;
}

.td_red {
	color: #F00;
	font-weight: bold
}

.table_list tr:hover {
	background: #F1F1F1
}
</style>
</head>
<body>
	<!-- 头 -->
	<header class="am-topbar admin-header">

	<div class="am-topbar-brand">
		<strong>JFlow</strong><small>后台管理</small>
	</div>
	<div class="am-collapse am-topbar-collapse" id="topbar-collapse">
		<ul
			class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
			<li><a href="#"><span class="am-icon-user"></span> <%=WebUser.getName()%>
					▼</a></li>
			<!-- 必选加 am-dropdown，data-am-dropdown，超链接中必须加am-dropdown-toggle，data-am-dropdown-toggle-->
			<li><a href="#"><span class="am-icon-power-off"></span> 退出</a></li>
		</ul>
	</div>

	</header>

	<!-- 左侧菜单 -->
	<!-- 左侧菜单栏 -->
	<div class="admin-sidebar am-offcanvas" id="admin-offcanvas">
		<div class="am-offcanvas-bar admin-offcanvas-bar">
			<ul class="am-list admin-sidebar-list">
				<li><a href="#"><span class="am-icon-home"></span> 信息</a></li>
				<li class="admin-parent"><a class="am-cf"
					data-am-collapse="{target: '#collapse-nav'}"><span
						class="am-icon-pencil-square-o"></span> 处理<span
						class="am-icon-angle-right am-fr am-margin-right"></span></a>
					<ul class="am-list am-collapse admin-sidebar-sub am-in"
						id="collapse-nav">
						<li><a
							href="<%=basePath%>UI/Default.jsp?name=<%=WebUser.getNo()%>"
							class="am-cf"><span class="am-icon-file"></span> 发起</a></li>
						<li><a
							href="<%=basePath%>UI/Todolist.jsp?name=<%=WebUser.getNo()%>"><span
								class="am-icon-star"></span> 代办(<%=Dev2Interface.getTodolist_EmpWorks()%>)</a></li>
						<li><a
							href="<%=basePath%>UI/Runing.jsp?name=<%=WebUser.getNo()%>"
							target=""><span class="am-icon-pencil-square-o"></span> 在途(<%=Dev2Interface.getTodolist_Runing()%>)</a></li>
						<li><a
							href="<%=basePath%>UI/CC.jsp?name=<%=WebUser.getNo()%>"><span
								class="am-icon-puzzle-piece"></span> 抄送(<%=Dev2Interface.getTodolist_CCWorks()%>)</a></li>
						<li><a href="<%=basePath%>UI/Batch.jsp"><span
								class="am-icon-th"></span> 批处理</a></li>
						<%
							if (Glo.getIsEnableTaskPool()) {
						%>
						<li><a href="<%=basePath%>UI/TaskPoolSharing.jsp"><span
								class="am-icon-bookmark"></span> 共享任务</a></li>
						<%-- d.add(106, 100, "共享任务", "<%=basePath%>WF/TaskPoolSharing.jsp"); --%>
						<%
							}
						%>
						<li><a href="<%=basePath%>UI/HungUpList.jsp"><span
								class="am-icon-file"></span> 挂起(<%=Dev2Interface.getTodolist_HungUpNum()%>)</a></li>
						<li><a href="<%=basePath%>UI/FlowSearch.jsp"><span
								class="am-icon-table"></span> 查询</a></li>
						<li><a href="<%=basePath%>UI/GetTask.jsp"><span
								class="am-icon-pencil-square-o"></span> 取回审批</a></li>
						<li><a href="<%=basePath%>UI/Complete.jsp"><span
								class="am-icon-star"></span> 已完成(<%=Dev2Interface.getTodolist_Complete()%>)</a></li>
						<li><a href="<%=basePath%>UI/Emps.jsp"><span
								class="am-icon-user"></span> 成员</a></li>
						<li><a href="<%=basePath%>UI/Tools.jsp"><span
								class="am-icon-cog"></span> 设置</a></li>
					</ul></li>
			</ul>
		</div>
	</div>


	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>共享任务</small>
			</div>
		</div>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<%=htmlStr %>
			</div>
		</div>

	</div>
	</div>
</body>
</html>