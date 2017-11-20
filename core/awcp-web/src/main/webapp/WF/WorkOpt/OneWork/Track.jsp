<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/head/head.jsp"%>
<%
	TruakModel tm = new TruakModel(request, response);
	tm.init();
	String FK_Flow = request.getParameter("FK_Flow")==null?"0":request.getParameter("FK_Flow");
	String OID = request.getParameter("WorkID").toString();
	int oid = 0;
	String FID = request.getParameter("FID")==null?"0":request.getParameter("FID");
	int fid = 0;
	String FK_Node = request.getParameter("FK_Node")==null?"0":request.getParameter("FK_Node");
	int fk_node = 0;
	if (OID != null && OID.length() > 0) {
		oid = Integer.parseInt(OID.toString());
	}
	if (FID != null && FID.length() > 0) {
		fid = Integer.parseInt(FID);
	}
	if (FK_Node != null && FK_Node.length() > 0) {
		fk_node = Integer.parseInt(FK_Node);
	}
	AthModel am = new AthModel(request, response, basePath, FK_Flow,
			oid, fid, fk_node);
	am.init();
	String DoType = request.getParameter("DoType");
	String FK_Node1 = request.getParameter("FK_Node")==null?"0":request.getParameter("FK_Node");
	String FK_Flow1 = request.getParameter("FK_Flow")==null?"0":request.getParameter("FK_Flow");
	String WorkID = request.getParameter("WorkID")==null?"0":request.getParameter("WorkID");
	int workID = 0;
	String FID1 = request.getParameter("FID")==null?"0":request.getParameter("FID");
	int fid1 = 0;
	if (WorkID != null && WorkID.length() > 0) {
		workID = Integer.parseInt(WorkID);
	}
	if (FID != null && FID.length() > 0) {
		fid = Integer.parseInt(FID);
	}
	OpModel op = new OpModel(request, response, basePath, DoType,
			FK_Node1, FK_Flow1, workID, fid1);
	op.init();
%>
</head>
<script type="text/javascript">
	function onSilverlightError(sender, args) {
		var appSource = "";
		if (sender != null && sender != 0) {
			appSource = sender.getHost().Source;
		}

		var errorType = args.ErrorType;
		var iErrorCode = args.ErrorCode;

		if (errorType == "ImageError" || errorType == "MediaError") {
			return;
		}
		var errMsg = "Silverlight 应用程序中未处理的错误 " + appSource + "\n";

		errMsg += "代码: " + iErrorCode + "    \n";
		errMsg += "类别: " + errorType + "       \n";
		errMsg += "消息: " + args.ErrorMessage + "     \n";

		if (errorType == "ParserError") {
			errMsg += "文件: " + args.xamlFile + "     \n";
			errMsg += "行: " + args.lineNumber + "     \n";
			errMsg += "位置: " + args.charPosition + "     \n";
		} else if (errorType == "RuntimeError") {
			if (args.lineNumber != 0) {
				errMsg += "行: " + args.lineNumber + "     \n";
				errMsg += "位置: " + args.charPosition + "     \n";
			}
			errMsg += "方法名称: " + args.methodName + "     \n";
		}
		alert(errMsg);
	}

	function appLoad() {
		var xamlObject = document.getElementById("silverlightControl");
		if (xamlObject != null) {
		}
	}
</script>
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
			url = './../HungUpOp.jsp?WorkID=' + workid + '&FK_Flow=' + fk_flow
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
		url = 'OP.jsp?DoType=' + doType + '&WorkID=' + workid + '&FK_Flow='
				+ fk_flow + '&FK_Node=' + fk_node;
		window.location.href = url;
	}
	function Takeback(workid, fk_flow, fk_node, toNode) {
		if (confirm('您确定要执行吗？') == false)
			return;
		var url = '../../GetTask.jsp?DoType=Tackback&FK_Flow=' + fk_flow
				+ '&FK_Node=' + fk_node + '&ToNode=' + toNode + '&WorkID='
				+ workid;
		window.location.href = url;
	}
	function UnSend(fk_flow, workID, fid) {

		//            var url = "CancelWork.aspx?WorkID=" + workID + "&FK_Flow=" + fk_flow+"&FID="+fid+"&FK_Node="+"";
		//            WinShowModalDialog_Accepter(url);

		if (confirm('您确定要执行撤销吗?') == false)
			return;

		<%-- var url = "<%=basePath%>WF/OP.jsp?DoType=UnSend&FK_Node=<%=FK_Node%>&FK_Flow=" + fk_flow
 				+ "&WorkID=" + workID + "&FID=" + fid; --%>
// 		$.post(url, null, function(msg) {
// 			$('#winMsg').html(msg);
// 			$('#winMsg').window('open');
// 		});
		var url = "<%=basePath%>WF/Do.jsp?DoType=UnSend&FK_Flow=" + fk_flow
		+ "&WorkID=" + workID + "&FID=" + fid+"&PageID=002";
		var v = window
		.showModalDialog(url, 'sd',
				'dialogHeight: 300px; dialogWidth: 700px;center: yes; help: no');
	}
</script>
<script language="javascript" type="text/javascript">
	var kvs = null;
	function GenerPageKVs() {
		var ddls = null;
		ddls = parent.document.getElementsByTagName("select");
		kvs = "";
		for (var i = 0; i < ddls.length; i++) {
			var id = ddls[i].name;

			if (id.indexOf('DDL_') == -1) {
				continue;
			}
			var myid = id.substring(id.indexOf('DDL_') + 4);
			kvs += '~' + myid + '=' + ddls[i].value;
		}

		ddls = document.getElementsByTagName("select");
		for (var i = 0; i < ddls.length; i++) {
			var id = ddls[i].name;

			if (id.indexOf('DDL_') == -1) {
				continue;
			}
			var myid = id.substring(id.indexOf('DDL_') + 4);
			kvs += '~' + myid + '=' + ddls[i].value;
		}
		return kvs;
	}
	function UploadChange(btn) {
		document.getElementById(btn).click();
	}
	function HidShowSta() {
		if (document.getElementById('RptTable').style.display == "none") {
			document.getElementById('RptTable').style.display = "block";
			document.getElementById('ImgUpDown').src = "../Img/arrow_down.gif";
		} else {
			document.getElementById('ImgUpDown').src = "../Img/arrow_up.gif";
			document.getElementById('RptTable').style.display = "none";
		}
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
		document.getElementById('Img' + rowIdx).src = '../Img/' + alt + '.gif';
		document.getElementById('Img' + rowIdx).alert = alt;
		var i = 0
		for (i = 0; i <= 40; i++) {
			if (document.getElementById(rowIdx + '_' + i) == null)
				continue;
			if (sta == 'block') {
				document.getElementById(rowIdx + '_' + i).style.display = '';
			} else {
				document.getElementById(rowIdx + '_' + i).style.display = sta;
			}
		}
	}
	function NoSubmit(ev) {
		if (window.event.srcElement.tagName == "TEXTAREA")
			return true;

		if (ev.keyCode == 13) {
			window.event.keyCode = 9;
			// alert(' code=: ' + ev.keyCode + ' tagName:' + window.event.srcElement.tagName);
			ev.keyCode = 9;
			// alert('ok');
			//alert(ev.keyCode);
			return true;
			//                event.keyCode = 9;
			//                ev.keyCode = 9;
			//                window.event.keyCode = 9;
		}
		return true;
	}

	$(document).ready(function() {
		$("table.Table tr:gt(0)").hover(function() {
			$(this).addClass("tr_hover");
		}, function() {
			$(this).removeClass("tr_hover");
		});
		$('#tab1').load('<%=basePath %>WF/Admin/CCFlowDesigner/Truck.html?FID=<%=fid %>&FK_Flow=<%=FK_Flow %>&WorkID=<%=WorkID %>');
	});
	$(document).ready(function() {
		$('#someTabs').tabs({noSwipe: 1});
	});

	function WinOpen(url, winName) {
		var newWindow = window
				.open(
						url,
						winName,
						'height=800,width=1030,top='
								+ (window.screen.availHeight - 800)
								/ 2
								+ ',left='
								+ (window.screen.availWidth - 1030)
								/ 2
								+ ',scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
		newWindow.focus();
		return;
	}

	function ReinitIframe(frmID, tdID) {
		try {

			var iframe = document.getElementById(frmID);
			var tdF = document.getElementById(tdID);
			iframe.height = iframe.contentWindow.document.body.scrollHeight;
			iframe.width = iframe.contentWindow.document.body.scrollWidth;
			if (tdF.width < iframe.width) {
				tdF.width = iframe.width;
			} else {
				iframe.width = tdF.width;
			}

			tdF.height = iframe.height;
			return;

		} catch (ex) {

			return;
		}
		return;
	}
</script>

<body>

	<div class="am-tabs am-margin" data-am-tabs="{noSwipe: 1}">
		<ul class="am-tabs-nav am-nav am-nav-tabs">
			<li><a href="#tab1">轨迹图</a></li>
			<li><a href="#tab2">流程日志</a></li>
			<li><a href="#tab3">流程附件</a></li>
			<li><a href="#tab4">操作</a></li>
		</ul>

		<div class="am-tabs-bd">
			<div class="am-tab-panel am-fade am-in am-active" id="tab1">
				
			
				<%-- <div id="silverlightControlHost"
					style="height: 100%; width: 100%; text-align: center;">
					<object data="data:application/x-silverlight-2,"
						type="application/x-silverlight-2" width="100%" height="450">
						<param name="source"
							value="<%=basePath%>ClientBin/CCFlowDesigner.xap" />
						<param name="onLoad" value="appLoad" />
						<param name="onerror" value="onSilverlightError" />
						<param name="background" value="white" />
						<param name="minRuntimeVersion" value="2.0.31005.0" />
						<param name="initParams" value="platForm=JAVA,appName=">
						<param name="windowless" value="true" />
						<param name="autoUpgrade" value="true" />
						<a href="http://go.microsoft.com/fwlink/?LinkID=124807"
							style="text-decoration: none;"> <img
							src="http://go.microsoft.com/fwlink/?LinkId=108181"
							alt="Get Microsoft Silverlight" style="border-style: none" />
						</a>
					</object>
				</div> --%>
			</div>

			<div class="am-tab-panel am-fade am-in am-active" id="tab2">
				<div class="am-g" style="overflow-x:auto ">
					<div class="am-u-sm-12">
						<form class="am-form">
							<input type="hidden" id="FormHtml" name="FormHtml" value=""></input>
							<div class="txt">
								<%=tm.Pub1.toString()%>
								<%=tm.UCEn1.Pub.toString()%>
							</div>
						</form>
					</div>
				</div>
			</div>

			<div class="am-tab-panel am-fade am-in am-active" id="tab3">
				<div class="am-g">
					<div class="am-u-sm-12">
						<form method="post"
							action="Track.do?FK_Flow=002&amp;FK_Node=&amp;WorkID=103"
							id="am-form">
							<%=am.Pub1.toString()%>
						</form>
					</div>
				</div>
			</div>

			<div class="am-tab-panel am-fade am-in am-active" id="tab4">
				<div class="am-g">
					<div class="am-u-sm-12">
						<form method="post"
							action="Track.jsp?FK_Node=&amp;WorkID=101&amp;FK_Flow=001&amp;FID="
					 		id="am-form">
							<%=op.Pub2.toString()%>
						</form>
					</div>
				</div>
			</div>

		</div>
	</div>



	<%-- <!-- 左侧菜单 -->
	<!-- 左侧菜单栏 -->
	<div class="admin-sidebar am-offcanvas" id="admin-offcanvas">
		<div class="am-offcanvas-bar admin-offcanvas-bar">
			<ul class="am-list admin-sidebar-list">
				<li><a
					href='<%=basePath%>WF/track/ChartTrack.jsp?FK_Node=<%=tm.getFK_Node()%>&WorkID=<%=tm.getWorkID()%>&FK_Flow=<%=tm.getFK_Flow()%>&FID='><span>轨迹图</span></a>
				</li>
				<li><a
					href='<%=basePath%>UI/WorkOpt/OneWork/Track.jsp?FK_Node=<%=tm.getFK_Node()%>&WorkID=<%=tm.getWorkID()%>&FK_Flow=<%=tm.getFK_Flow()%>'><span>流程日志</span></a>
				</li>
				<li><a
					href='<%=basePath%>WF/Ath.jsp?FK_Node=&WorkID=103&FK_Flow=002&FID='><span>流程附件</span></a>
				</li>
				<li><a
					href='<%=basePath%>WF/OP.jsp?FK_Node=&WorkID=103&FK_Flow=002&FID='><span>操作</span></a>
				</li>
			</ul>
		</div>
	</div> --%>

</body>
</html>