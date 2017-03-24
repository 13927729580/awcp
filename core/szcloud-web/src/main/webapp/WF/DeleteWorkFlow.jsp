<%@page import="BP.WF.Template.PubLib.BtnAttr"%>
<%@page import="BP.WF.Template.PubLib.DelWorkFlowRole"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/head/head.jsp"%>
<%@page import="BP.Sys.*"%>
<%
	String FK_Node = request.getParameter("FK_Node") == null
	? ""
	: request.getParameter("FK_Node");
	String FID = request.getParameter("FID") == null ? "" : request
	.getParameter("FID");
	String WorkID = request.getParameter("WorkID") == null
	? ""
	: request.getParameter("WorkID");
	String FK_Flow = request.getParameter("FK_Flow") == null
	? ""
	: request.getParameter("FK_Flow");
%>
<script type="text/javascript">
	function OnChange(ctrl) {
		//        var text = ctrl.options[ctrl.selectedIndex].text;
		//        var user = text.substring(0, text.indexOf('='));
		//        var nodeName = text.substring(text.indexOf('>') + 1, 1000);
		//        var objVal = '您好' + user + ':';
		//        objVal += "  \t\n ";
		//        objVal += "  \t\n ";
		//        objVal += "   您处理的 “" + nodeName + "” 工作有错误，需要您重新办理． ";
		//        objVal += "\t\n   \t\n 礼! ";
		//        objVal += "  \t\n ";
		//        document.getElementById('ContentPlaceHolder1_ReturnWork1_Pub1_TB_Doc').value = objVal;
	}
</script>


</head>
<body topmargin="0" leftmargin="0" onkeypress="NoSubmit(event);">

	<div class="admin-content">
		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>工作删除</small>
			</div>
		</div>
		<div class="divCenter2">
			<form method="post"
				action="<%=basePath%>WF/DeleteWorkFlow.do?FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>"
				id="form1" onkeypress="NoSubmit(event);">
				<input type="hidden" name="BtnID" id="BtnID" value="" />
				<%
					BP.WF.Template.Node nd = new BP.WF.Template.Node(FK_Node);
				%>
				<table
					class="am-table am-table-bordered am-table-radius am-table-striped">
					<tr>
						<td colspan="2">删除方式: <select id='DDL1' name='DDL1'
							onchange='OnChange(this);' data-am-selected>
								<%
									// 彻底删除
									if (nd.getHisDelWorkFlowRole() == DelWorkFlowRole.DeleteReal) {
										SysEnum se = new SysEnum(BtnAttr.DelEnable,
												DelWorkFlowRole.DeleteReal.ordinal());
										// ddl.Items.add(new ListItem(se.getLab(), String.valueOf(se
										// .getIntKey())));
								%>
								<option value='<%=String.valueOf(se.getIntKey())%>'><%=se.getLab()%></option>
								<%
									}

									if (nd.getHisDelWorkFlowRole() == DelWorkFlowRole.DeleteAndWriteToLog) {
										// 删除并记录日志
										SysEnum se = new SysEnum(BtnAttr.DelEnable,
												DelWorkFlowRole.DeleteAndWriteToLog.ordinal());
								%>
								<option value='<%=String.valueOf(se.getIntKey())%>'><%=se.getLab()%></option>
								<%
									// ddl.Items.add(new ListItem(se.getLab(), String.valueOf(se
										// .getIntKey())));
									}

									if (nd.getHisDelWorkFlowRole() == DelWorkFlowRole.DeleteByFlag) {
										// 逻辑删除
										SysEnum se = new SysEnum(BtnAttr.DelEnable,
												DelWorkFlowRole.DeleteByFlag.ordinal());
										// ddl.Items.add(new ListItem(se.getLab(), String.valueOf(se
										// .getIntKey())));
								%>
								<option value='<%=String.valueOf(se.getIntKey())%>'><%=se.getLab()%></option>
								<%
									}

									if (nd.getHisDelWorkFlowRole() == DelWorkFlowRole.ByUser) {
										// 让用户来决定.
										SysEnums ses = new SysEnums(BtnAttr.DelEnable);
										for (Object se : ses) {
											DelWorkFlowRole role = DelWorkFlowRole
													.forValue(((SysEnum) se).getIntKey());
											if (role == DelWorkFlowRole.None) {
												continue;
											}
											if (role == DelWorkFlowRole.ByUser) {
												continue;
											}
											// ddl.Items.add(new ListItem(((SysEnum) se).getLab(), String
											// .valueOf(((SysEnum) se).getIntKey())));
								%>
								<option value='<%=String.valueOf(((SysEnum) se).getIntKey())%>'><%=((SysEnum) se).getLab()%></option>
								<%
									}
									}
								%>

						</select></td>
					</tr>
					<tr>
						<td colspan="2">
							<div class="am-form-group">
								<textarea class="" rows="15" cols='110' id="TB_Doc"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td><button type='submit' id='Btn_OK' name='Btn_OK'
								class='am-btn am-btn-primary'
								onclick="btnOk();document.getElementById('BtnID').value='Btn_OK';return confirm('您确定要执行吗?');">确定</button>
							<button type="submit" id='Btn_Cancel' name='Btn_Cancel'
								class='am-btn am-btn-primary'
								onclick="document.getElementById('BtnID').value='Btn_Cancel';">取消</button></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
