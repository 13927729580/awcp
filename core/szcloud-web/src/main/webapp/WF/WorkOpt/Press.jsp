<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/head/head.jsp"%>
<%
	 String workId = request.getParameter("WorkID")==null?"0":request.getParameter("WorkID");
	 long WorkID=Long.valueOf(workId);
	 String  fid = request.getParameter("FID")==null?"0":request.getParameter("FID");
	 long FID=Long.valueOf(fid);
	 String fk_node = request.getParameter("FK_Node")==null?"0":request.getParameter("FK_Node");
	 int FK_Node=Integer.valueOf(fk_node);
	 String FK_Flow = request.getParameter("FK_Flow")==null?"":request.getParameter("FK_Flow");
	 
	GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
	//this.Pub1.AddFieldSet("请输入催办消息-" + gwf.Title);
	String pressInfo = "您好：" + gwf.getTodoEmps().replace(";","") + " \t\n \t\n  此工作需要您请尽快处理...... \t\n \t\n致! \t\n \t\n   " + WebUser.getName() + " " + BP.DA.DataType.getCurrentDataCNOfShort();
%>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">您好：<%=Glo.GenerUserImgSmallerHtml(WebUser.getNo(),WebUser.getName())%>   --   请输入催办消息</strong>
			</div>
		</div>
		
		<div class="am-g">
			<div class="am-u-sm-12">
				<form method="post" action="Press.do?WorkID=<%=WorkID%>&FK_Flow=<%=FK_Flow %>" class="am-form" id="form1">
					<table class="am-table am-table-striped am-table-hover table-main">
						<tr>
							<!-- <td width="20%"></td> -->
							<td>
								  <textarea name="TB_Msg" rows="7" cols="40" id="TB_Msg"><%=pressInfo %></textarea><BR>
							      <input type="submit" name="Btn_Send" value=" 催办 " id="Btn_Send" class="am-btn am-btn-primary am-btn-xs"/>&nbsp;
							      <input type="button" name="Btn_Cancel" value=" 取消 " onclick="window.close();" id="Btn_Cancel" class="am-btn am-btn-primary am-btn-xs"/>
							</td>
						</tr>
					</table>	
				</form>
			</div>
		</div>
	</div>	
</body>
</html>