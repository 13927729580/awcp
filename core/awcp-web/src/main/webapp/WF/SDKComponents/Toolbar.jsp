<%@page import="BP.WF.Template.PubLib.NodeFormType"%>
<%@page import="BP.WF.Template.PubLib.BatchRole"%>
<%@page import="BP.WF.Template.PubLib.ActionType"%>
<%@page import="BP.WF.Template.Ext.NodeToolbar"%>
<%@page import="BP.WF.Template.Ext.NodeToolbarAttr"%>
<%@page import="BP.WF.Template.Ext.NodeToolbars"%>
<%@page import="BP.WF.Template.ShowWhere"%>
<%@page import="BP.WF.Template.Flow"%>
<%@page import="BP.WF.Template.PubLib.PrintDocEnable"%>
<%@page import="BP.WF.Template.PubLib.CCRole"%>
<%@page import="BP.WF.Template.PubLib.WFState"%>
<%@page import="BP.WF.Template.BtnLab"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="BP.WF.*"%>
<%@page import="BP.WF.Entity.*"%>
<%@page import="BP.WF.Data.*"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.Port.*"%>
<%@page import="BP.Tools.*"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";

int fk_node = Integer.valueOf(request.getParameter("FK_Node"));
long workID = Long.valueOf(request.getParameter("WorkID"));
long fid = Long.valueOf(request.getParameter("FID"));
String fk_flow = request.getParameter("FK_Flow");
String isToolbar = request.getParameter("IsToobar");

boolean toolbar = true;
if (!StringHelper.isNullOrEmpty(isToolbar) && isToolbar == "0")
    toolbar = false;

String paras = request.getParameter("AtPara");

boolean isCC = false;
if (paras != null && paras.contains("IsCC=1"))
    isCC = true;

paras = request.getParameter("Paras");
if (paras != null && paras.contains("IsCC=1"))
    isCC = true;

BtnLab btn = new BtnLab(fk_node);
BP.WF.Template.Node node = new BP.WF.Template.Node(fk_node);

GenerWorkFlow gwf = new GenerWorkFlow();
gwf.setWorkID(workID);
gwf.RetrieveFromDBSources();

WFState workState = gwf.getWFState();

String appPath = Glo.getCCFlowAppPath(); //this.Request.ApplicationPath;
String msg = null;
boolean isInfo = false;

%>
<script type="text/javascript" language="javascript"
	src="<%=basePath%>WF/SDKComponents/Base/SDKData.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=basePath%>WF/DataUser/PrintTools/LodopFuncs.js"></script>
<script type="text/javascript">
    function ShowUrl(obj) {

        var strTimeKey = "";
        var date = new Date();
        strTimeKey += date.getFullYear(); //年
        strTimeKey += date.getMonth() + 1; //月 月比实际月份要少1
        strTimeKey += date.getDate(); //日
        strTimeKey += date.getHours(); //HH
        strTimeKey += date.getMinutes(); //MM
        strTimeKey += date.getSeconds(); //SS
        
        var btnID = obj.id;

         if (btnID == 'Btn_Return') {
             //OpenUrl('ReturnMsg', '退回', '/WF/WorkOpt/ReturnWork.aspx?1=2', 500, 350);
             OpenUrlLocation('<%=basePath%>WF/WorkOpt/ReturnWork.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Track') {
             //OpenUrl('TrackMsg', '流程步骤', '<%=basePath%>WF/WorkOpt/OneWork/Track.jsp?1=2', 800, 500);
             //OpenUrlLocation('<%=basePath%>WF/WorkOpt/OneWork/Track.jsp?1=2');
             //WinOpen('<%=basePath%>WF/WorkOpt/OneWork/Track.jsp?1=2','流程轨迹',800,500);
             WinOpen('<%=basePath%>WF/Admin/CCFlowDesigner/Truck.html?FID=<%=fid%>&FK_Flow=<%=fk_flow%>&WorkID=<%=workID%>','流程轨迹',800,500);
             return;
         }

         if (btnID == 'Btn_SelectAccepter') {
            // OpenUrl('SelectAccepterMsg', '接受人', '<%=basePath%>/WF/WorkOpt/Accepter.jsp?1=2', 600, 450);
             OpenUrlLocation('<%=basePath%>/WF/WorkOpt/Accepter.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Askfor') {
            // OpenUrl('AskforMsg', '加签', '/WF/WorkOpt/AskFor.aspx?1=2', 600, 400);
             OpenUrlLocation( '<%=basePath%>WF/WorkOpt/Askfor.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Shift') {
             //OpenUrl('ShiftMsg', '移交', '/WF/WorkOpt/Forward.aspx?1=2', 600, 450);
             OpenUrlLocation('<%=basePath%>WF/WorkOpt/Forward.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_CC') {
             //OpenUrl('CCMsg', '抄送', '<%=basePath%>WF/WorkOpt/CC.jsp?1=2', 700, 450);
            OpenUrlLocation('<%=basePath%>WF/WorkOpt/CC.jsp?1=2');
            // OpenUrlLocation('<%=basePath%>WF/WorkOpt/StopFlow.jsp?FID=<%=fid%>&WorkID=<%=workID%>&FK_Node=<%=fk_node%>&FK_Flow=<%=fk_flow%>&s=<%=new Date()%>');
             //OpenUrlLocation('<%=basePath%>WF/WorkOpt/HungUp.jsp?DoType=HungUp&FID=<%=fid%>&WorkID=<%=workID%>&FK_Node=<%=fk_node%>&FK_Flow=<%=fk_flow%>&s=<%=new Date()%>');
             return;
         }

         if (btnID == 'Btn_Delete') {
         //if (confirm("是否真的需要删除?"))
              OpenUrlLocation('<%=basePath%>WF/DeleteWorkFlow.jsp?1=2');
             return;
         }

     if (btnID == 'Btn_CheckNote') {
            // OpenUrl('CCMsg', '审核', '/WF/WorkOpt/CCCheckNote.aspx?1=2', 700, 450);
         OpenUrlLocation('<%=basePath%>WF/WorkOpt/CCCheckNote.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Office') {
            // WinOpen( '<%=basePath%>WF/WorkOpt/WebOffice.jsp?1=2', '公文正文', 700, 450);
            OpenUrlLocation('<%=basePath%>WF/WorkOpt/WebOffice.jsp?1=2');
             return;
         }

         if (btnID == 'Btn_Read') {
            Application.data.ReadCC("<%=node.getNodeID()%>", "<%=workID%>",
					ReadCCResult, this);
		}

		if (btnID == 'Btn_Print') {
			printFrom();
			return;
		}

		//        if (btnID == 'Btn_Delete') {
		//             if (confirm("是否真的需要删除?"))
		//                        DelCase();
		//            return;
		//        }
	}
	function DeleteResult(json) {

	}
	function ReadCCResult(json) {
		if (json != "true") {
			alert('已阅失败!');
		} else {
			alert('已阅成功!');
			window.opener.document.location.reload();
			window.close();
		}
	}

	var LODOP;
	function printFrom() {
		LODOP = getLodop(document.getElementById('LODOP_OB'), document
				.getElementById('LODOP_EM'));
		LODOP.PRINT_INIT("打印表单");
		//    LODOP.ADD_PRINT_URL(30, 20, 746, "100%", location.href);
		LODOP.ADD_PRINT_HTM(0, 0, "100%", "100%", document
				.getElementById("divCCForm").innerHTML);
		// LODOP.ADD_PRINT_URL(0, 0, "100%", "100%", url);
		LODOP.SET_PRINT_STYLEA(0, "HOrient", 3);
		LODOP.SET_PRINT_STYLEA(0, "VOrient", 3);
		//		LODOP.SET_SHOW_MODE("MESSAGE_GETING_URL",""); //该语句隐藏进度条或修改提示信息
		//		LODOP.SET_SHOW_MODE("MESSAGE_PARSING_URL","");//该语句隐藏进度条或修改提示信息
		//  LODOP.PREVIEW();

		LODOP.PREVIEW();

	}

	function ShowFlowMessage() {
		//          $('#flowMessage').window(
		//        {
		//            closeable: false,
		//            title: "信息日志",
		//            modal: true,
		//            width: 800,
		//            height: 400,
		//            buttons: [{ text: '关闭', handler: function () {
		//                $('#flowMessage').dialog("close");
		//           }
		//           }]
		//        }
		//        );
	}
	function WinOpen(url, winName, width, height) {

		// 生成参数.
		_GetParas();

		// 把IsEUI处理一下，让对方的功能界面接收到此参数进行个性化处理.
		url = url + _paras + '&IsEUI=1';
		var newWindow = window
				.open(
						url,
						winName,
						'width='
								+ width
								+ ',height='
								+ height
								+ ',top=100,left=300,scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
		newWindow.focus();
		return;
	}

	function DelCase() {
		var strTimeKey = "";
		var date = new Date();
		strTimeKey += date.getFullYear(); //年
		strTimeKey += date.getMonth() + 1; //月 月比实际月份要少1
		strTimeKey += date.getDate(); //日
		strTimeKey += date.getHours(); //HH
		strTimeKey += date.getMinutes(); //MM
		strTimeKey += date.getSeconds(); //SS

		Application.data.delcase(
				<%=fk_flow%>
					,
				<%=fk_node%>
					,
				<%=workID%>
					,
				"", function(js) {
					if (js) {
						var str = js;
						if (str == "删除成功") {
							$.messager.alert('提示', '删除成功！');
						} else {
							$.messager.alert('提示', str);
						}
					}
				}, this);
	}
	function OpenUrlLocation(url) {
		_GetParas();
		url = url + _paras + '&IsEUI=1';
		
		//window.name = "dialogPage";
		//window.showModalDialog(url, "dialogPage");
		window.location.href = url;
	}
	
	function showModelDialog(url){
		_GetParas();
		url = url + _paras + '&IsEUI=1';
		window.name = "dialogPage";
		window.showModalDialog(url, "dialogPage");
	}
	function OpenUrl(divID, title, url, w, h) {

		// 生成参数.
		_GetParas();
		url = url + _paras + '&IsEUI=1';
		<%//System.out.println("appsetting:"+BP.Sys.SystemConfig.getAppSettings().get("SDKWinOpenType"));
        Object sdkwinopentype = BP.Sys.SystemConfig.getAppSettings().get("SDKWinOpenType");
        if (sdkwinopentype!=null && sdkwinopentype.equals("1"))
           {%>
	// 把IsEUI处理一下，让对方的功能界面接收到此参数进行个性化处理.
		try {
			window.parent.OpenJboxIfream(title, url, w, h);
		} catch (e) {
			OpenWindow(url, title, w, h);
		}
	<%} else {%>
		OpenWindow(url, title, w, h);
	<%}%>
	}

	$(function() {
		var html = $('#flowMessage').text();
		if (html != "" && html != null && html.length > 6) {
			ShowFlowMessage();
		}
	});

	function OpenWindow(url, title, w, h) {
		//        $('#windowIfrem').window(
		//        {
		//              content: ' <iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>',
		//            closeable: false,
		//            title: title,
		//            modal: true,
		//            width: w,
		//            height: h,
		//            buttons: [{ text: '关闭', handler: function () {
		//                $('#windowIfrem').dialog("close");
		//            }
		//            }]
		//        }
		//        );
	}

	var _paras = "";
	function _GetParas() {
		_paras = "";
		//获取其他参数
		var sHref = window.location.href;
		var args = sHref.split("?");
		var retval = "";
		if (args[0] != sHref) /*参数不为空*/
		{
			var str = args[1];
			args = str.split("&");
			for (var i = 0; i < args.length; i++) {
				str = args[i];
				var arg = str.split("=");
				if (arg.length <= 1)
					continue;

				//不包含就添加
				if (_paras.indexOf(arg[0]) == -1) {
					// xiaozhoupeng add 20150121 Start 解决公文获取wrokID=0的问题
					var value = 0;
					if ("WorkID" == arg[0]) {
						value = <%=workID%>
					} else if ("FID" == arg[0]) {
						value = <%=fid%>
					} else {
						value = arg[1];
					}
					_paras += "&" + arg[0] + "=" + value;
					// xiaozhoupeng add 20150121 End

					if (_paras.indexOf('WorkID') == -1) {
						_paras += "&WorkID=" +
						<%=workID%> ;
					}
					// _paras += "&" + arg[0] + "=" + arg[1];
				}
			}
		}
	}

	function closeWin() {
		if (window.dialogArguments && window.dialogArguments.window) {
			window.dialogArguments.window.location = window.dialogArguments.window.location;
		}
		if (window.opener) {
			if (window.opener.name && window.opener.name == "main") {
				window.opener.location.href = window.opener.location.href;
				window.opener.top.leftFrame.location.href = window.opener.top.leftFrame.location.href;
			}
		}
		window.close();
	}
</script>
<%
	if (toolbar) {
		
%>
<%
	if (!isCC && workState != WFState.Complete) {
%>
<%
if(node.getHisFormType() == NodeFormType.SelfForm){
	String sendJs = "";
	  //如果是自定义表单.
	if (node.getIsEndNode())
	{
		
		//如果当前节点是结束节点.
		if (btn.getSendEnable() && node.getHisBatchRole() != BatchRole.Group)
		{
			//如果启用了发送按钮.
			// 获取发送的JS
			sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+"if (SendSelfFrom()==false) return false;"+" Send();";
			%>
			<!--发送-->
			<input type="button" onclick="<%=sendJs %>" id="Btn_Send" value="<%=btn.getSendLab()%>" class="am-btn am-btn-primary am-btn-xs" />
			<%
		}
	}else{
			if (btn.getSendEnable() && node.getHisBatchRole() != BatchRole.Group)
			{
				//如果启用了发送按钮.
				if (btn.getSelectAccepterEnable() == 2)
				{
					//如果启用了选择人窗口的模式是【选择既发送】.
					%>
					<!--接受人-->
					<input type="button" onclick="javascript:OpenSelectAccepter('<%=fk_flow %>','<%=fk_node %>','<%=workID %>','<%=fid %>')" id="button" value="接受人" class="am-btn am-btn-primary am-btn-xs" />
					<%
					if (node.getHisFormType() == NodeFormType.DisableIt)
					{
						sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+" Send();";
					}
					else
					{
						sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+"if (SendSelfFrom()==false) return false;"+" Send();";
					}
					%>
					<!--发送-->
					<input type="button" onclick="<%=sendJs %>" id="Btn_Send" value="<%=btn.getSendLab()%>" class="am-btn am-btn-primary am-btn-xs" />
					<%
				}
				else
				{
					if (btn.getSendJS().trim().length() > 2)
					{
						sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+"if (SendSelfFrom()==false) return false;"+" Send();";
					}
					else
					{
						if (node.getHisFormType() == NodeFormType.DisableIt) {
							//this.Btn_Send.OnClientClick = "this.disabled=true;"; //this.disabled='disabled'; return true;";
							sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+" Send();";
						}
						else
						{
							sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+"if (SendSelfFrom()==false) return false;"+" Send();";
						}
					}
					
					%>
					<!--发送-->
					<input type="button" onclick="<%=sendJs %>" id="Btn_Send" value="<%=btn.getSendLab()%>" class="am-btn am-btn-primary am-btn-xs" />
					<%
				}
		}
		//处理保存按钮.
		if (btn.getSaveEnable()) {
		%>
		<input type="button" onclick="SaveSelfFrom();" id="Btn_Save"  value='<%=btn.getSaveLab()%>' class='am-btn am-btn-primary am-btn-xs' />
		<%
		}
	}
}else{
	if (workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
		// 获取发送的JS
		String sendJs = StringHelper.isEmpty(btn.getSendJS().replace("\"", "'"), "")+" Send();";
%>
<!--发送-->
<input type="button" onclick="if(SysCheckFrm()==false) return false;<%=sendJs %>" id="Btn_Send" value="<%=btn.getSendLab()%>" class="am-btn am-btn-primary am-btn-xs" />
<%
	}
%>
<!-- 保存-->
<%
	if (btn.getSaveEnable() && workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
%>
<input type="button" onclick="if(SysCheckFrm()==false) return false;Save()" id="Btn_Save" 
	value='<%=btn.getSaveLab()%>' class='am-btn am-btn-primary am-btn-xs' />
<%
	}
}
%>
<!-- 退回-->
<%
	if (!node.getIsStartNode() && btn.getReturnEnable() && workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
%>
<input type="button" onclick="ShowUrl(this)" id="Btn_Return"
	name="Btn_Return" value='<%=btn.getReturnLab()%>'
	class='am-btn am-btn-primary am-btn-xs' />
<%
	}
%>
<!-- 接受人-->
<%
	if (btn.getSelectAccepterEnable() != 0 && btn.getSelectAccepterEnable() != 2
					&& workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
%>
	<input type="button" onclick="ShowUrl(this)" id="Btn_SelectAccepter" value='<%=btn.getSelectAccepterLab()%>' class='am-btn am-btn-primary am-btn-xs' />
<%
	}
%>
<!-- 移交-->
<%
	// 如果不是退回类型的开始节点就不显示移交按钮
	if(node.getIsStartNode() && workState == WFState.ReturnSta){
		%>
		<input type="button" onclick="ShowUrl(this)" id="Btn_Shift" value='<%=btn.getShiftLab()%>' class='am-btn am-btn-primary am-btn-xs' />
		<%
	}else if (btn.getShiftEnable() && workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp
					&& !node.getIsStartNode()) {
%>
		<input type="button" onclick="ShowUrl(this)" id="Btn_Shift" value='<%=btn.getShiftLab()%>' class='am-btn am-btn-primary am-btn-xs' />
<%
	}
%>
<!-- 删除-->
<%
		if (btn.getDeleteEnable() != 0
					&& workState != WFState.Complete
					&& workState != WFState.Fix
					&& workState != WFState.HungUp) {
%>
			<input type="button" onclick="ShowUrl(this)" id="Btn_Delete" value='<%=btn.getDeleteLab()%>' class='am-btn am-btn-primary am-btn-xs' />
<%
		}
%>
<!-- 加签-->
<%
		if (btn.getAskforEnable() && workState != WFState.Complete && workState != WFState.Fix && workState != WFState.HungUp) {
%>
			<input type="button" onclick="ShowUrl(this)" id="Btn_Askfor" value='<%=btn.getAskforLab()%>' class='am-btn am-btn-primary am-btn-xs' />
<!-- 抄送-->
<%
		}
	if (btn.getCCRole().ordinal() == CCRole.HandAndAuto.ordinal() || btn.getCCRole().ordinal() == CCRole.HandCC.ordinal()) {
		if (workState != WFState.Complete && workState != WFState.Fix && workState != WFState.HungUp) {
%>
			<input type="button" onclick="ShowUrl(this)" id="Btn_CC" value='<%=btn.getCCLab()%>' class='am-btn am-btn-primary am-btn-xs' />
<%
		}

	}
%>
<!-- 查询 -->
<%

if (btn.getSearchEnable()){
	%>
    <input type="button" class="am-btn am-btn-primary am-btn-xs" value="<%=btn.getSearchLab()%>" enable=true onclick="WinOpen('<%=basePath%>WF/Rpt/Search.jsp?RptNo=ND<%=Integer.parseInt(fk_flow)%>MyRpt&FK_Flow=<%=fk_flow %>','dsd0')" />
<%
}
	} else {
			/* 如果是抄送. */
%>
<input type="button" onclick="ShowUrl(this)" id="Btn_Read" value="已阅"
	class='am-btn am-btn-primary am-btn-xs' />
<%
	FrmWorkCheck fwc = new FrmWorkCheck(fk_node);
			if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Enable) {
				/*如果没有启用,就显示出来可以审核的窗口. */
				String url = "";
%>
<input type="button" value='填写审核意见' id="Btn_CheckNote"
	onclick="ShowUrl(this)" class='am-btn am-btn-primary am-btn-xs' />
<%
	}
		}
%>

<!-- 轨迹-->
<%
	if (btn.getTrackEnable()) {
%>
<input type="button" onclick="ShowUrl(this)" id="Btn_Track"
	value='<%=btn.getTrackLab()%>'
	class='am-btn am-btn-primary am-btn-xs' />
<%
	}
%>
<%
	}
%>
<!-- 打印-->
<%
	if (btn.getPrintDocEnable()) {
		// xiaozhoupeng 20150122 add Start 打印rtf单据模板
		if (node.getHisPrintDocEnable().ordinal() == PrintDocEnable.PrintRTF
				.ordinal()) {
			String urlr = basePath + "WF/WorkOpt/PrintDoc.jsp?FK_Node="
					+ fk_node + "&FID=" + fid + "&WorkID=" + workID
					+ "&FK_Flow=" + fk_flow + "&s="
					+ DataType.dateToStr(new Date(), "yyMMddhhmmss");
%>
<input type="button" class="am-btn am-btn-primary am-btn-xs"
	value="<%=btn.getPrintDocLab()%>" enable=true
	onclick="WinOpen('<%=urlr%>','dsdd');" />
<%
	}
		// xiaozhoupeng 20150122 add End
		if (node.getHisPrintDocEnable().ordinal() == PrintDocEnable.PrintHtml
				.ordinal()) {
%>
<object id="LODOP_OB"
	classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width="0"
	height="0">
	<embed id="LODOP_EM" type="application/x-print-lodop" width="0"
		height="0"
		pluginspage="<%=basePath%>DataUser/PrintTools/install_lodop32.exe"></embed>
</object>
<input type="button" onclick="ShowUrl(this)" id="Btn_Print"
	value='<%=btn.getPrintDocLab()%>'
	class='am-btn am-btn-primary am-btn-xs' />
<%
	}
%>
<%
	}
%>
<!-- 公文-->
<%
	if (btn.getWebOfficeEnable() == 1) {
%>
<input type="button" onclick="ShowUrl(this)" id="Btn_Office"
	value='<%=btn.getWebOfficeLab()%>'
	class='am-btn am-btn-primary am-btn-xs' />
<%
	}

	//加载自定义的button.
	NodeToolbars bars = new NodeToolbars();
	bars.Retrieve(NodeToolbarAttr.FK_Node, fk_node);
	for (NodeToolbar bar : NodeToolbars.convertNodeToolbars(bars)) {
		
		if (bar.getShowWhere() == ShowWhere.Toolbar){
			StringBuilder urlr3 = new StringBuilder();
			urlr3.append(bar.getUrl()).append("?1=2").append("&FK_Node=").append(fk_node).append("&FID=").append(fid).append("&WorkID=").append(workID).append("&FK_Flow=").append(fk_flow);
			%>
			<input type="button" class="am-btn am-btn-primary am-btn-xs" value="<%=bar.getTitle() %>" enable=true onclick="WinOpen('<%=urlr3.toString()%>');" />
			<%
		}
	}
%>
<%
try {
	%>
	<!--  // 20150430 xiao_zhoupeng 注释，原因在低版本浏览器上不显示
	<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
	<div class="am-modal-dialog">
		<div class="am-modal-bd">
		 -->
		 <div style="margin-top:10px">
			<%
				// WorkFlow workFlow = new WorkFlow(node.getFK_Flow(), workID);
				if (workState != WFState.Complete) {
					switch (workState) {
					
					case AskForReplay: // 返回加签的信息.
					%><h2>加签信息:</h2><%
					String mysql = "SELECT * FROM ND"
							+ Integer.valueOf(node.getFK_Flow())
							+ "Track WHERE WorkID=" + workID + " AND "
							+ TrackAttr.ActionType + "="
							+ ActionType.ForwardAskfor.ordinal();
					DataTable mydt = BP.DA.DBAccess.RunSQLReturnTable(mysql);
					for (DataRow dr : mydt.Rows) {
						String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
						String worker = dr.getValue(TrackAttr.EmpFrom).toString();
						String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
						String rdt = dr.getValue(TrackAttr.RDT).toString();
						%>
						<div>
						--------------<%=workerName %>在<%=rdt %>加签--------------<hr/> 
						内容：<br/>
						<%=BP.DA.DataType.ParseText2Html(msgAskFor) %><br/><hr/>
						</div>
						<%
						isInfo = true;
					}
						break;
					case Askfor: //加签.
						%><h2>加签信息:</h2><%
						String sql = "SELECT * FROM ND"
								+ Integer.valueOf(node.getFK_Flow())
								+ "Track WHERE WorkID=" + workID + " AND "
								+ TrackAttr.ActionType + "="
								+ ActionType.AskforHelp.ordinal();
						DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
						for (DataRow dr : dt.Rows) {
							String msgAskFor = dr.getValue(TrackAttr.Msg).toString();
							String worker = dr.getValue(TrackAttr.EmpFrom).toString();
							String workerName = dr.getValue(TrackAttr.EmpFromT).toString();
							String rdt = dr.getValue(TrackAttr.RDT).toString();
							%>
							<div>
							--------------<%=workerName %>在<%=rdt %>加签--------------<hr/> 
							内容：<br/>
							<%=BP.DA.DataType.ParseText2Html(msgAskFor) + "<br>" + rdt + "<br> --<a href='./WorkOpt/AskForRe.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid + "' >回复加签意见</a> --"%><br/><hr/>
							</div>
							<%
							isInfo = true;
						 }
						break;
					case ReturnSta:
						%>
						<h2>退回信息:</h2>
						<%
						/* 如果工作节点退回了*/
						ReturnWorks rws = new ReturnWorks();
						rws.Retrieve(ReturnWorkAttr.ReturnToNode, fk_node,ReturnWorkAttr.WorkID, workID, ReturnWorkAttr.RDT);
						if (rws.size() != 0) {
							String msgInfo = "";
							for (ReturnWork rw : ReturnWorks.convertReturnWorks(rws)) {
							%>
								<div>
								----------------<%=rw.getReturnerName()%>在<%=rw.getRDT()%>退回----------------<hr/> 
								内容：<br /><%=rw.getNoteHtml()%><br/><hr/>
								</div>
							<%
							}
							isInfo = true;
							//gwf.WFState = WFState.Runing;ruhe huoqu yige div
							//gwf.DirectUpdate();
						}
						break;
					case Shift:
						/* 判断移交过来的。 */
						ShiftWorks fws = new ShiftWorks();
						BP.En.QueryObject qo = new BP.En.QueryObject(fws);
						qo.AddWhere(ShiftWorkAttr.WorkID, workID);
						qo.addAnd();
						qo.AddWhere(ShiftWorkAttr.FK_Node, fk_node);
						qo.addOrderBy(ShiftWorkAttr.RDT);
						qo.DoQuery();
						if (fws.size() >= 1) {
							//this.FlowMsg.AddFieldSet("移交历史信息");
							%><h2>移交信息:</h2><%
							for (ShiftWork fw : ShiftWorks.convertShiftWorks(fws)) {
								//msg = "@移交人[" + fw.getFK_Emp() + ","
								//		+ fw.getFK_EmpName() + "]。@接受人："
								//		+ fw.getToEmp() + "," + fw.getToEmpName()
								//		+ "。<br>移交原因@" + fw.getNoteHtml();
								//if (fw.getFK_Emp().equals(WebUser.getNo())){
								//	msg = "<b>" + msg + "</b>";
								//}

								//msg = msg.replace("@", "<br>@");
								%>
								<div>
								----------------<%=fw.getFK_EmpName()%>在<%=fw.getRDT()%>移交----------------<hr/> 
								内容：<br /><%=fw.getNoteHtml()%><br/><hr/>
								</div>
							<%
								//this.FlowMsg.Add(msg + "<hr>");
							isInfo = true;
							}
							//this.FlowMsg.AddFieldSetEnd();
						}
						//gwf.setWFState(WFState.Runing);
						//gwf.DirectUpdate();
						//isInfo = true;
						break;
					}

					//boolean isCanDo = true; // workFlow.IsCanDoCurrentWork(WebUser.getNo());
					//if (!isCanDo){
					//	workState = WFState.Complete;
					//}
				}
			%>
			
			</div>
			 <!--
			<div class="am-modal-footer">
				<span class="am-modal-btn">确定</span>
			</div>
		</div>
	</div>
	-->
	<%
	//if (isInfo) {
	%>
	<!-- 
	<button class="am-btn am-btn-primary am-btn-xs" data-am-modal="{target: '#my-alert'}">信息日志</button>
	<script type="text/javascript">
		var $modal = $('#my-alert');
		$modal.modal(); 
	</script>
	--> 
	<%
	//}
} catch (Exception e) {

     Flow fl = new Flow(fk_flow);
     GERpt rpt = fl.getHisGERpt();
     rpt.setOID(workID);
     rpt.Retrieve();

     if (rpt != null){
         workState = rpt.getWFState();
     }
     throw e;
}
%>


