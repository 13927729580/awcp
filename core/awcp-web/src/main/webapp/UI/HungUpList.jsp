<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.Port.WebUser"%>
<%@page import="BP.WF.Entity.GenerWorkFlowAttr"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.Port.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String PageSmall = null;
	String name = WebUser.getNo();

	try {
		Emp emp = new Emp(name);
		//ContextHolderUtils.setResponse(response);
		WebUser.SignInOfGener(emp);
	} catch (Exception e) {
		response.sendRedirect(basePath + "WF/Login.jsp?errNo=1");
		return;
	}
	String PageID = Glo.getCurrPageID();
	if (PageID.toLowerCase().contains("smallsingle")) {
		PageSmall = "SmallSingle";
	} else if (PageID.toLowerCase().contains("small")) {
		PageSmall = "Small";
	} else {
		PageSmall = "";
	}

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
	boolean IsHungUp = false;
	String isHungUp = request.getParameter("IsHungUp") == null
			? ""
			: request.getParameter("IsHungUp");
	if (StringHelper.isNullOrEmpty(isHungUp)) {
		IsHungUp = false;
	} else {
		IsHungUp = true;
	}
%>

<%!StringBuffer infoCtrl;

	public void Add(String str) {
		infoCtrl.append(str);
	}

	public void AddTable(String attr) {
		this.Add("<Table class=\"am-table am-table-striped am-table-hover table-main\""
				+ attr + " >");
	}

	public void AddTableEnd() {
		this.Add("</Table>");
	}

	public void AddCaption(String str) {
		this.Add("\n<th class='table-title' colspan='8'>" + str + "</th>");
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
		this.Add("\n<TRss>");
	}

	public void AddTDTitle(String str) {
		this.Add("\n<TH class='table-title'>" + str + "</TH>");
	}

	public void AddTD(String attr, String str) {
		this.Add("\n<TD " + attr + " >" + str + "</TD>");
	}

	public void AddTDIdx(int idx) {
		this.Add("\n<TD nowrap>" + idx + "</TD>");
	}

	public void AddTDB(String attr, String str) {
		this.Add("\n<TD  " + attr + " nowrap=true ><b>" + str + "</b></TD>");
	}

	public void AddTDBigDoc(String str) {
		this.Add("\n<TD valign=top>" + str + "</TD>");
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

	public void AddTDCenter(String str) {
		this.Add("\n<TD align=center nowrap >" + str + "</TD>");
	}

	public void BindList(DataTable dt, String basePath, String FK_Flow,
			String GroupBy, boolean IsHungUp, String PageID, String PageSmall) {
		boolean isPRI = Glo.getIsEnablePRI();
		String groupVals = "";
		for (DataRow dr : dt.Rows) {
			if (groupVals.contains("@" + dr.getValue(GroupBy) + ","))
				continue;
			groupVals += "@" + dr.getValue(GroupBy) + ",";
		}
		int colspan = 9;

		this.AddTable("align=left");
		this.AddCaption("<img src='" + basePath
				+ "WF/Img/Runing.gif' ><a href='" + basePath + "UI/" + PageID
				+ ".jsp?IsHungUp=1' >挂起工作</a>");
		String extStr = "";
		if (IsHungUp)
			extStr = "&IsHungUp=1";

		this.AddTR();
		this.AddTDTitle("ID");
		this.AddTDTitle("标题");

		if (!"FlowName".equals(GroupBy))
			this.AddTDTitle("<a href='" + basePath + "UI/" + PageID
					+ ".jsp?GroupBy=FlowName" + extStr + "' >流程</a>");

		if (!"NodeName".equals(GroupBy))
			this.AddTDTitle("<a href='" + basePath + "UI/" + PageID
					+ ".jsp?GroupBy=NodeName" + extStr + "' >节点</a>");

		if (!"StarterName".equals(GroupBy))
			this.AddTDTitle("<a href='" + basePath + "UI/" + PageID
					+ ".jsp?GroupBy=StarterName" + extStr + "' >发起人</a>");

		if (isPRI)
			this.AddTDTitle("<a href='" + basePath + "UI/" + PageID
					+ ".jsp?GroupBy=PRI" + extStr + "' >优先级</a>");

		this.AddTDTitle("发起日期");
		this.AddTDTitle("接受日期");
		this.AddTDTitle("期限");
		this.AddTDTitle("状态");
		this.AddTREnd();

		int i = 0;
		Date cdt = DataType.getDate();
		String[] gVals = groupVals.split("@");
		int gIdx = 0;
		for (String g : gVals) {
			if (StringHelper.isNullOrEmpty(g))
				continue;
			gIdx++;
			this.AddTR();
			if ("Rec".equals(GroupBy))
				this.AddTD(
						"colspan='" + colspan
								+ "'onclick=\"GroupBarClick('"
								+ gIdx + "')\" ",
						"<div style='text-align:left; float:left' ><img src='"
								+ basePath
								+ "WF/Img/Min.gif' alert='Min' id='Img" + gIdx
								+ "'   border=0 />&nbsp;<b>"
								+ g.replace(",", "") + "</b>");
			else
				this.AddTD(
						"colspan='" + colspan
								+ "'onclick=\"GroupBarClick('"
								+ gIdx + "')\" ",
						"<div style='text-align:left; float:left' ><img src='"
								+ basePath
								+ "WF/Img/Min.gif' alert='Min' id='Img" + gIdx
								+ "'   border=0 />&nbsp;<b>"
								+ g.replace(",", "") + "</b>");
			this.AddTREnd();
			for (DataRow dr : dt.Rows) {
				if (!g.equals(dr.getValue(GroupBy) + ","))
					continue;

				String sdt = dr.getValue(GenerWorkFlowAttr.SDTOfFlow)
						.toString();
				this.AddTR("ID='" + gIdx + "_" + i + "'");
				i++;
				this.AddTDIdx(i);
				if (Glo.getIsWinOpenEmpWorks()) {
					this.AddTD(
							"onclick=\"SetImg('I" + gIdx + "_" + i
									+ "')\"",
							"<a href=\"javascript:WinOpenIt('" + basePath
									+ "WF/MyFlow.jsp?FK_Flow="
									+ dr.getValue("FK_Flow") + "&FK_Node="
									+ dr.getValue("FK_Node") + "&FID="
									+ dr.getValue("FID") + "&WorkID="
									+ dr.getValue("WorkID")
									+ "&IsRead=0');\" ><img src='" + basePath
									+ "/WF/Img/Mail_UnRead.png' id='I" + gIdx
									+ "_" + i + "' border=0/>"
									+ dr.getValue("Title").toString());
				} else {
					this.AddTD("Class='TTD'",
							"<a href=\"" + basePath + "MyFlow" + PageSmall
									+ ".jsp?FK_Flow=" + dr.getValue("FK_Flow")
									+ "&FK_Node=" + dr.getValue("FK_Node")
									+ "&FID=" + dr.getValue("FID") + "&WorkID="
									+ dr.getValue("WorkID") + "\" >"
									+ dr.getValue("Title").toString());
				}

				if (!"FlowName".equals(GroupBy))
					this.AddTD(dr.getValue("FlowName").toString());

				if (!"NodeName".equals(GroupBy))
					this.AddTD(dr.getValue("NodeName").toString());

				if (!"StarterName".equals(GroupBy))
					this.AddTD(dr.getValue("Starter") + " "
							+ dr.getValue("StarterName"));

				if (isPRI)
					this.AddTD("<img src='" + basePath + "/WF/Img/PRI/"
							+ dr.getValue("PRI").toString()
							+ ".png' class='ImgPRI' />");

				this.AddTD(dr.getValue("RDT").toString().substring(5));
				this.AddTD(dr.getValue("SDTOfFlow").toString().substring(5));
				this.AddTD(dr.getValue("SDTOfNode").toString().substring(5));

				Date mysdt = DataType.ParseSysDate2DateTime(sdt);
				if (cdt.getTime() >= mysdt.getTime()) {
					if (DataType.dateToStr(cdt, "yyyy-MM-dd").equals(
							DataType.dateToStr(mysdt, "yyyy-MM-dd")))
						this.AddTDCenter("正常");
					else
						this.AddTDCenter("<font color=red>逾期</font>");
				} else {
					this.AddTDCenter("正常");
				}
				this.AddTREnd();
			}
		}
		this.AddTableEnd();
	}%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>挂起列表</title>
<link rel="stylesheet" href="assets/css/amazeui.min.css" />
<link rel="stylesheet" href="assets/css/admin.css">
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/amazeui.min.js"></script>
<script src="assets/js/app.js"></script>
<script type="text/javascript" src="<%=basePath%>WF/Comm/JScript.js"></script>
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
function GroupBarClick(rowIdx) {
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
function WinOpenIt(url) {
    var newWindow = window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
    newWindow.focus();
    return;
}
function SetImg(id) {
    document.getElementById(id).src ='<%=basePath%>WF/Img/Mail_Read.png';
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
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>挂起列表</small>
			</div>
		</div>

		<!-- 数据 -->

		<div class="am-g">
			<div class="am-u-sm-12">
				<form class="am-form">
					<%
						infoCtrl = new StringBuffer();
						this.BindList(Dev2Interface.DB_GenerHungUpList(), basePath,
								FK_Flow, GroupBy, IsHungUp, PageID, PageSmall);
					%>
					<%=infoCtrl.toString()%>
				</form>
			</div>
		</div>

	</div>
	</div>

</body>
</html>