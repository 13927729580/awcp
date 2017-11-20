<%@page import="java.text.DateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/head/head.jsp"%>

<%
	System.out.println("basePath : "+basePath);
	int FK_Node=Integer.valueOf(request.getParameter("FK_Node")==null?"0":request.getParameter("FK_Node"));
    long FID=Long.valueOf(request.getParameter("FID")==null?"0":request.getParameter("FID"));
    long WorkID=Long.valueOf(request.getParameter("WorkID")==null?"0":request.getParameter("WorkID"));
    String FK_Flow=request.getParameter("FK_Flow")==null?request.getParameter("FlowNo"):request.getParameter("FK_Flow");
    String userNo = WebUser.getNo();
    String userName = WebUser.getName();
    
    DataTable dt = null;

    dt = BP.WF.Dev2Interface.DB_GenerWillReturnNodes(FK_Node,WorkID,FID);
    if (dt.Rows.size() == 0)
    {
        out.println("系统没有找到可以退回的节点.");
        return;
    }
    

    Node nd = new Node(FK_Node);
    
    String checkInfo = "";
    FrmWorkCheck fwc = new FrmWorkCheck(FK_Node);
    if (fwc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable)
    {
    	checkInfo = Dev2Interface.GetCheckInfo(FK_Flow, Long.valueOf(WorkID),Integer.valueOf(FK_Node));
        if (checkInfo.equals("同意"))
        	checkInfo = "";
    }
    else
    {
    	
   		  DateFormat format2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String reTime = format2.format(new Date());
        checkInfo = String.format("%s同志: \n  您处理的“%s”工作有错误，需要您重新办理．\n\n此致!!!   \n\n  %s", dt.getValue(0, "RecName"),
        		dt.getValue(0, "Name"), reTime);

    }
%>
<script type="text/javascript">

    function OnChange(ctrl) {
        var text = ctrl.options[ctrl.selectedIndex].text;
        var user = text.substring(0, text.indexOf('='));
        var nodeName = text.substring(text.indexOf('>') + 1, 1000);
        var objVal = '您好' + user + ':';
        objVal += "  \t\n ";
        objVal += "  \t\n ";
        objVal += "   您处理的 “" + nodeName + "” 工作有错误，需要您重新办理． ";
        objVal += "\t\n   \t\n 礼! ";
        objVal += "  \t\n ";

        try {
            document.getElementById("TB_Doc").value = objVal;
        } catch (e) {
        }
    }

    function TBHelp(ctrl, enName, attrKey) {
    	var explorer = window.navigator.userAgent;
        var url = "<%=basePath%>WF/Comm/HelperOfTBEUI.jsp?EnsName=" + enName
				+ "&AttrKey=" + attrKey;
		var str = "";
		/*if (explorer.indexOf("Chrome") >= 0) {//谷歌的
		     window.open(url, "sd", "left=200,height=500,top=150,width=400,location=yes,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no");
		 }
		 else {//IE,火狐的
		     str = window.showModalDialog(url, "sd", "dialogHeight:500px;dialogWidth:400px;dialogTop:150px;dialogLeft:200px;center:no;help:no");
		     if (str == undefined) return;
		     $("*[id$=" + ctrl + "]").focus().val(str);
		 } */
		str = window
				.showModalDialog(
						url,
						"sd",
						"dialogHeight:500px;dialogWidth:400px;dialogTop:150px;dialogLeft:200px;center:no;help:no");
		if (str == undefined)
			return;
		// $("*[id$=" + ctrl + "]").focus().val(str);
		document.getElementById(ctrl).value = str;
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
</script>
</head>
<body leftmargin="0" onkeypress="NoSubmit(event);" topmargin="0">
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">您好:<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%></strong>
			</div>
		</div>

		<div class="divCenter2" style="background-color: white">
			<form method="post"
				action="<%=basePath%>WF/WorkOpt/ReturnWork.do?FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>"
				id="form1">
					<table  class="am-table am-table-striped am-table-hover table-main"
						>
						<tr></tr>
						<tr>
							<td>
								<div align="center">
									<div align="center" style='height: 30px;'>
										<b>退回到:</b> <select name="DDL1" id="DDL1"
											onchange="OnChange(this);">
											<%
												for (DataRow dr : dt.Rows){
											%>
											<option
												value="<%=dr.getValue("No")%>@<%=dr.getValue("Rec")%>"><%=dr.getValue("RecName")%>=><%=dr.getValue("Name")%></option>
											<%
												}
											%>
										</select> <input type="hidden" name="BtnID" id="BtnID" />
										<!-- <button style="color: black;" type="button" name="Btn_OK"
									onclick="if(confirm('您确定要执行吗?')){document.getElementById('BtnID').value='Btn_OK';return true;}else{return false;}"
									class="am-btn am-btn-primary am-btn-xs" id="Btn_OK">确定</button> -->
										<input type="submit" class="am-btn am-btn-primary am-btn-xs"
											name="Btn_OK" value="确定"
											onclick="if(confirm('您确定要执行吗?')){document.getElementById('BtnID').value='Btn_OK';return true;}else{return false;}"
											id="Btn_OK" class="am-btn-primary Btn" />
										<!-- <button style="color: black;" type="button" name="Btn_Cancel" value="取消"
									onclick="document.getElementById('BtnID').value='Btn_Cancel';"
									class="am-btn am-btn-primary am-btn-xs">取消</button> -->
										<input type="submit" class="am-btn am-btn-primary am-btn-xs"
											name="Btn_Cancel" value="取消"
											onclick=" document.getElementById('BtnID').value='Btn_Cancel';"
											class="am-btn-primary Btn" />
										<%
											if (nd.getIsBackTracking()) {
										%>
										<input id="CB_IsBackTracking" type="checkbox"
											name="CB_IsBackTracking" /> <label for="CB_IsBackTracking">退回后是否要原路返回?</label>
										<%
											}
										%>
									</div>
									<div style='height: 4px;'></div>
									<div>
										<div style='float: left; display: block; width: 100%'>
											<a href="javascript:TBHelp('TB_Doc')"> <img
												src='<%=basePath%>WF/Img/Emps.gif' align='middle' border=0 />选择词汇
											</a>&nbsp;&nbsp;
										</div>
										<textarea name="TB_Doc" rows="15" cols="68" id="TB_Doc"><%=checkInfo%></textarea>
									</div>
								</div>
							</td>
						</tr>
					</table>
			</form>
		</div>
	</div>
</body>

<script type="text/javascript">
	$(document).ready(function() {

	});
</script>
</html>