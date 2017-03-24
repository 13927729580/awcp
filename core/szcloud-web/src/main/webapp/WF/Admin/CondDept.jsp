<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/head/head1.jsp"%>
<%
	CondDeptModel condModel = new CondDeptModel(request, response);
	condModel.Page_Load();
%>
<script type="text/javascript">
function load() {
	var success = document.getElementById("success").value;
	if (success == "" || success == null) {
		return;
	} else {
		alert(success);
	}

}
function onsave()
{
	
		var str = "";
		var btnId="Btn_Save";
		var MyPK='<%=condModel.getMyPK()%>';
		var ToNodeID='<%=condModel.getToNodeID()%>';
		var HisCondType='<%=condModel.getHisCondType()%>';
		var FK_Flow='<%=condModel.getFK_Flow()%>';
		var FK_MainNode='<%=condModel.getFK_MainNode()%>';
		var FK_Node='<%=condModel.getFK_Node()%>';
		var FK_Attr='<%=condModel.getFK_Attr()%>';
		$("input[class='']:checkbox:checked").each(function() {
			str += $(this).attr("id")+",";
		})
		location.href="<%=basePath%>des/condDept_btn_Save_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&FK_Attr=" + FK_Attr + "&ToNodeID=" + ToNodeID+"&btnId="+btnId+"&str="+str;
}
function btn_Del_Click()
{
	
		var str = "";
		var btnId="Btn_Delete";
		var MyPK='<%=condModel.getMyPK()%>';
		var ToNodeID='<%=condModel.getToNodeID()%>';
		var HisCondType='<%=condModel.getHisCondType()%>';
		var FK_Flow='<%=condModel.getFK_Flow()%>';
		var FK_MainNode='<%=condModel.getFK_MainNode()%>';
		var FK_Node='<%=condModel.getFK_Node()%>';
		var FK_Attr='<%=condModel.getFK_Attr()%>';
		$("input[name='']:checkbox:checked").each(function() {
			str += $(this).attr("id")+",";
		})
		location.href="<%=basePath%>des/condDept_btn_Save_Click.do?MyPK=" + MyPK + "&FK_Flow=" + FK_Flow + "&FK_Node=" + FK_Node + "&FK_MainNode=" + FK_MainNode + "&CondType=" + HisCondType + "&FK_Attr=" + FK_Attr + "&ToNodeID=" + ToNodeID+"&btnId="+btnId+"&str="+str;
}
function SelectAll(cb_selectAll) {
    var arrObj = document.all;
    if (cb_selectAll.checked) {
        for (var i = 0; i < arrObj.length; i++) {
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
                arrObj[i].checked = true;
            }
        }
    } else {
        for (var i = 0; i < arrObj.length; i++) {
            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox')
                arrObj[i].checked = false;
        }
    }
}
</script>
</head>
<body onLoad="load()">
<input type="hidden" id="success" value="${success }"/>
<form method="post" action="" class="am-form" id="form1">
		<div id="rightFrame" data-options="region:'center',noheader:true">
			<div class="easyui-layout" data-options="fit:true">
			    <%=condModel.Pub1.toString()%>
			</div>
		</div>
	</form>
	
</body>
</html>