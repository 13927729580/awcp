<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/head/head.jsp"%>
<%
	String isCheckForever = request.getParameter("isCheckForever");
	String FK_Flow = (String) request.getParameter("FK_Flow");
	String WorkID = request.getParameter("WorkID");
	String FK_Node = request.getParameter("FK_Node");
	String FID = request.getParameter("FID");
	String title = request.getParameter("title");
	String DTOfUnHungUpPlan = request.getParameter("DTOfUnHungUpPlan");
	String isHungUp = request.getParameter("isHungUp");
%>
<script type="text/javascript" language="JavaScript">
function hungUpSubmit(obj){
	var RB_HungWay = $('input:radio:checked').val();
	var TB_RelData = $("#TB_RelData").val();
	var TB_Note = $("#TB_Note").val();
	var btn_id = obj.id;
	if(btn_id == "Btn_Cancel"){
		var url = "<%=basePath%>WF/MyFlow<%=Glo.getFromPageType()%>.jsp?FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>";
		window.location.href = url;
	}else{
		var url = "<%=basePath%>WF/WorkOpt/doHungUp.do?FK_Node=<%=FK_Node%>&FID=<%=FID%>&WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow%>&RB_HungWay="
					+ RB_HungWay
					+ "&TB_RelData="
					+ TB_RelData
					+ "&TB_Note="
					+ TB_Note;
			$("#form1").attr("action", url);
			$("#form1").submit();
		}
	}
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
<body>
	<div class="admin-content">
		<div class="am-g">
			<div class="am-u-sm-12">
				<form method="post" action="" id="form1">
					<div
						style="text-align: center; position: absolute; background-color: white; height: 100%; left: 15%; right: 15%">
						<table class='am-table am-table-striped am-table-hover table-main' style="text-align: left; width: 100%">
							<th class='table-title'>
								您好:<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),
					WebUser.getName())%>
							</th>
							<tr>
								<td style="text-align: center"><br>
										<table style="text-align: left; width: 500px">
											<tr>
												<td>
													<fieldset width='100%'>
														<legend>
															&nbsp;对工作<b><%=title%></b>挂起方式&nbsp;
														</legend>
														<%
															if (isCheckForever.equals("true")) {
														%>
														<input id="s" type="radio" name="s" value="RB_HungWay0"
															checked="checked" /><label for="RB_HungWay0">永久挂起</label><BR><input
															id="s" type="radio" name="s" value="RB_HungWay1" /><label
															for="RB_HungWay1">在指定的日期自动解除挂起.</label> <%
 	} else {
 %> <input
															id="s" type="radio" name="s" value="RB_HungWay0" /><label
															for="RB_HungWay0">永久挂起</label><BR><input id="s"
																type="radio" checked="checked" name="s"
																value="RB_HungWay1" /><label for="RB_HungWay1">在指定的日期自动解除挂起.</label>
																<%
																	}
																%> <BR>&nbsp;&nbsp;&nbsp;&nbsp;解除流程挂起的日期:<input
																	name="TB_RelData" type="text"
																	value="<%=DTOfUnHungUpPlan%>" maxlength="20"
																	id="TB_RelData" class="TBcalendar" />
													</fieldset>
													<fieldset width='100%'>
														<legend>&nbsp;挂起原因:&nbsp;</legend>
														<textarea name="TB_Note" rows="2" cols="70" id="TB_Note"
															style="height: 50px;"></textarea>
													</fieldset>&nbsp;&nbsp;&nbsp;&nbsp;
													<button type="button" onclick="hungUpSubmit(this)" name="Btn_OK" id="Btn_OK" class="am-btn am-btn-primary"><%=isHungUp%></button>
													<%-- <input type="button" name="Btn_OK"
													value="<%=isHungUp%>" id="Btn_OK"
													onclick="hungUpSubmit(this)" />  --%>
													<button type="button" name="Btn_Cancel" id="Btn_Cancel"
													onclick="hungUpSubmit(this)" class="am-btn am-btn-primary am-btn-xs">返回</button>
													<!-- <input type="button"
													name="Btn_Cancel" value=" 返回 " id="Btn_Cancel"
													onclick="hungUpSubmit(this)" /> -->
												</td>
											</tr>
										</table> <br></td>
							</tr>
						</table>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
