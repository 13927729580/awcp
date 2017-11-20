<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.Port.*"%>
<%@page import="BP.WF.Template.CCListAttr"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.WF.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String FK_Flow = request.getParameter("FK_Flow") == null
			? ""
			: request.getParameter("FK_Flow");
	String DoType = request.getParameter("DoType") == null
			? ""
			: request.getParameter("DoType");
	String GroupBy = request.getParameter("GroupBy") == null
			? ""
			: request.getParameter("GroupBy");
	if (StringHelper.isNullOrEmpty(GroupBy)) {
		if ("CC".equals(DoType)) {
			GroupBy = "Rec";
		} else {
			GroupBy = "FlowName";
		}
	}
	String Sta = request.getParameter("Sta") == null ? "0" : request
			.getParameter("Sta");
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
<%!StringBuffer infoCtrl;

	public void Add(String str) {
		infoCtrl.append(str);
	}

	public void AddTable(String attr) {
		this.Add("<Table " + attr + " >");
	}

	public void AddTableEnd() {
		this.Add("</Table class='am-table am-table-striped am-table-hover table-main'>");
	}

	public void AddCaption(String str) {
		this.Add("\n<Caption >" + str + "</Caption>");
	}

	public void AddTR() {
		this.Add("\n<TR>");
	}

	public void AddTR(String attr) {
		this.Add("\n<TR " + attr + " >");
	}

	public void AddTREnd() {
		this.Add("\n</TR>");
	}

	public void AddTRSum() {
		this.Add("\n<TR class='TRSum' >");
	}

	public void AddTDTitle(String str) {
		this.Add("\n<TH >" + str + "</TH>");
	}

	public void AddTD(String attr, String str) {
		this.Add("\n<TD " + attr + " >" + str + "</TD>");
	}

	public void AddTDIdx(int idx) {
		this.Add("\n<TD class='Idx' nowrap>" + idx + "</TD>");
	}

	public void AddTDB(String attr, String str) {
		this.Add("\n<TD  " + attr + " nowrap=true ><b>" + str + "</b></TD>");
	}

	public void AddTDBigDoc(String str) {
		this.Add("\n<TD class='BigDoc' valign=top>" + str + "</TD>");
	}

	public void AddTDB(String str) {
		this.Add("\n<TD  nowrap=true ><b>" + str + "</b></TD>");
	}

	public void AddTD(String str) {
		if (null == str || "".equals(str))
			this.Add("\n<TD  nowrap >&nbsp;</TD>");
		else
			this.Add("\n<TD  nowrap >" + str + "</TD>");
	}

	public String GenerMenu(String basePath, String FK_Flow) {
		String msg = "<a href='" + basePath + "UI/CC.jsp?Sta=-1&FK_Flow="
				+ FK_Flow + "' >全部</a> - " + "<a href='" + basePath
				+ "UI/CC.jsp?Sta=0&FK_Flow=" + FK_Flow + "' >未读</a> - "
				+ "<a href='" + basePath + "UI/CC.jsp?Sta=1&FK_Flow=" + FK_Flow
				+ "' >已读</a> - " + "<a href='" + basePath
				+ "UI/CC.jsp?Sta=2&FK_Flow=" + FK_Flow + "' >删除</a>";
		return msg;
	}

	public void Bind(DataTable dt, String basePath, String FK_Flow,
			String GroupBy, String Sta) {
		String groupVals = "";
		for (DataRow dr : dt.Rows) {
			if (groupVals.contains("@" + dr.getValue(GroupBy) + ","))
				continue;
			groupVals += "@" + dr.getValue(GroupBy) + ",";
		}
		int colspan = 9;
		this.AddTable("width='100%' align=left");
		this.AddCaption("<img src='" + basePath + "WF/Img/CCSta/CC.gif' >"
				+ GenerMenu(basePath, FK_Flow));
		this.AddTR();
		this.AddTDTitle("ID");
		this.AddTDTitle("流程标题");
		this.AddTDTitle("内容");

		if (!"FlowName".equals(GroupBy))
			this.AddTDTitle("<a href='" + basePath
					+ "UI/CC.jsp?GroupBy=FlowName&DoType=CC&Sta=" + Sta
					+ "&FK_Flow=" + FK_Flow + "' >流程</a>");

		if (!"NodeName".equals(GroupBy))
			this.AddTDTitle("<a href='" + basePath
					+ "UI/CC.jsp?GroupBy=NodeName&DoType=CC&Sta=" + Sta
					+ "&FK_Flow=" + FK_Flow + "' >节点</a>");

		if (!"Rec".equals(GroupBy))
			this.AddTDTitle("<a href='" + basePath
					+ "UI/CC.jsp?GroupBy=Rec&DoType=CC&Sta=" + Sta
					+ "&FK_Flow=" + FK_Flow + "' >抄送人</a>");

		if ("1".equals(Sta))
			this.AddTDTitle("删除");
		this.AddTREnd();

		int i = 0;
		String[] gVals = groupVals.split("@");
		int gIdx = 0;
		for (String g : gVals) {
			if (StringHelper.isNullOrEmpty(g))
				continue;
			gIdx++;
			this.AddTR();
			this.AddTD(
					"colspan='" + colspan
							+ "' class='Sum' onclick=\"GroupBarClick('"
							+ basePath + "','" + gIdx + "')\" ",
					"<div style='text-align:left; float:left' ><img src='"
							+ basePath + "WF/Img/Min.gif' alert='Min' id='Img"
							+ gIdx + "'   border=0 />&nbsp;<b>"
							+ g.replace(",", "") + "</b>");
			this.AddTREnd();
			for (DataRow dr : dt.Rows) {
				if (!g.equals(dr.getValue(GroupBy) + ","))
					continue;
				this.AddTR("ID='" + gIdx + "_" + i + "'");
				i++;
				int sta = Integer.parseInt(dr.getValue("Sta").toString());
				this.AddTDIdx(i);
				if (0 == sta) {
					this.AddTDB(
							"Class='TTD' onclick=\"SetImg('" + basePath + "','"
									+ dr.getValue("MyPK") + "')\"",
							"<a href=\"javascript:WinOpenIt('"
									+ basePath
									+ "','"
									+ dr.getValue("MyPK")
									+ "','"
									+ dr.getValue("FK_Flow")
									+ "','"
									+ dr.getValue("FK_Node")
									+ "','"
									+ dr.getValue(CCListAttr.WorkID)
									+ "','"
									+ dr.getValue("FID")
									+ "','"
									+ dr.getValue("Sta")
									+ "');\" ><img src='"
									+ basePath
									+ "WF/Img/CCSta/0.png' id='I"
									+ dr.getValue("MyPK")
									+ "' class=Icon >"
									+ dr.getValue("Title")
									+ "</a><br>日期:"
									+ dr.getValue("RDT").toString()
											.substring(5));
				} else {
					this.AddTD(
							"Class='TTD'",
							"<a href=\"javascript:WinOpenIt('"
									+ basePath
									+ "','"
									+ dr.getValue("MyPK")
									+ "','"
									+ dr.getValue("FK_Flow")
									+ "','"
									+ dr.getValue("FK_Node")
									+ "','"
									+ dr.getValue(CCListAttr.WorkID)
									+ "','"
									+ dr.getValue("FID")
									+ "','"
									+ dr.getValue("Sta")
									+ "');\" ><img src='"
									+ basePath
									+ "WF/Img/CCSta/"
									+ dr.getValue("Sta")
									+ ".png' class=Icon >"
									+ dr.getValue("Title")
									+ "</a><br>日期:"
									+ dr.getValue("RDT").toString()
											.substring(5));
				}

				this.AddTDBigDoc(DataType.ParseText2Html(dr.getValue("Doc")
						.toString()));

				if (!"FlowName".equals(GroupBy)) {
					if (0 == sta) {
						this.AddTDB(dr.getValue("FlowName").toString());
					} else {
						this.AddTD(dr.getValue("FlowName").toString());
					}
				}
				if (!"NodeName".equals(GroupBy)) {
					if (0 == sta) {
						this.AddTDB(dr.getValue("NodeName").toString());
					} else {
						this.AddTD(dr.getValue("NodeName").toString());
					}
				}
				if (!"Rec".equals(GroupBy))
					this.AddTD(dr.getValue("Rec").toString());

				if ("1".equals(Sta))
					this.AddTD("<a href=\"javascript:DoDelCC('"
							+ dr.getValue("MyPK") + "');\"><img src='"
							+ basePath + "WF/Img/Btn/Delete.gif' /></a>");

				this.AddTREnd();
			}
		}
		this.AddTRSum();
		this.AddTD("colspan=" + colspan, "&nbsp;");
		this.AddTREnd();
		this.AddTableEnd();
	}%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>抄送列表</title>
<style type="text/css">
.TTD {
	word-wrap: break-word;
	　　word-break: normal;
}

.ImgPRI {
	width: 20px;
	height: 20px;
	border: 0px;
}
</style>
<link rel="stylesheet" href="assets/css/amazeui.min.css" />
<link rel="stylesheet" href="assets/css/admin.css">
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/app.js"></script>
<script type="text/javascript">
function NoSubmit(ev) {
    //if (window.event.srcElement.tagName == "TEXTAREA")return true;

    if (ev.keyCode == 13) {
        window.event.keyCode = 9;
        ev.keyCode = 9;
        return true;
    }
    return true;
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
    document.getElementById('Img' + rowIdx).src = '<%=basePath%>WF/Img/' + alt + '.gif';
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
function DoDelCC(mypk) {
    var url = '<%=basePath%>WF/Do.jsp?DoType=DelCC&MyPK=' + mypk;
    var v = window.showModalDialog(url, 'sd', 'dialogHeight: 10px; dialogWidth: 10px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
}
function WinOpenIt(appPath,ccid, fk_flow, fk_node, workid, fid, sta) {
    var url = '';
    if (sta == '0') {
        url = '<%=basePath%>WF/Do.jsp?DoType=DoOpenCC&FK_Flow=' + fk_flow + '&FK_Node=' + fk_node + '&WorkID=' + workid + '&FID=' + fid + '&Sta=' + sta + '&MyPK=' + ccid;
    }
    else {
        url = '<%=basePath%>WF/WorkOpt/OneWork/Track.jsp?FK_Flow=' + fk_flow + '&FK_Node=' + fk_node + '&WorkID=' + workid + '&FID=' + fid + '&Sta=' + sta + '&MyPK=' + ccid;
    }

    var newWindow = window.open(url, 'z', 'help:1;resizable:1;Width:680px;Height:420px');
    newWindow.focus();
    return;
}
function SetImg(appPath, id) {
    document.getElementById('I'+id).src ='<%=basePath%>WF/Img/CCSta/1.png';
	}
</script>
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
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>发起列表</small>
			</div>
		</div>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<form class="am-form">
					<%
						infoCtrl = new StringBuffer();
						if ("-1".equals(Sta)) {
							this.Bind(Dev2Interface.DB_CCList(WebUser.getNo()), basePath,
									FK_Flow, GroupBy, Sta);
						} else if ("0".equals(Sta)) {
							this.Bind(Dev2Interface.DB_CCList_UnRead(WebUser.getNo()),
									basePath, FK_Flow, GroupBy, Sta);
						} else if ("1".equals(Sta)) {
							this.Bind(Dev2Interface.DB_CCList_Read(WebUser.getNo()),
									basePath, FK_Flow, GroupBy, Sta);
						} else {
							this.Bind(Dev2Interface.DB_CCList_Delete(WebUser.getNo()),
									basePath, FK_Flow, GroupBy, Sta);
						}
					%>
					<%=infoCtrl.toString()%>
				</form>
			</div>
		</div>

	</div>
	</div>
</body>
</html>