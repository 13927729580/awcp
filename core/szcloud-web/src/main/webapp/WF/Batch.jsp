<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/head/head.jsp"%>
<%
	String fk_node = request.getParameter("FK_Node") == null?"0":request.getParameter("FK_Node");
	int FK_Node = Integer.valueOf(fk_node);

	String FK_MapData = "ND" + FK_Node;
	int ListNum = 12;
	String Key = request.getParameter("Key") == null?"":request.getParameter("Key");
	String DoType = request.getAttribute("DoType") == null?"":request.getAttribute("DoType").toString();
	String normMsg = request.getAttribute("normMsg") == null?"":request.getAttribute("normMsg").toString();

	BatchModel model = new BatchModel(basePath, FK_Node, FK_MapData, ListNum, Key, DoType, normMsg);
	model.init();
%>
<script type="text/javascript">
function SelectAll() {
    var arrObj = document.all;
    if (document.forms[0].checkedAll.checked) {
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
function BatchGroup() {
    var btn = document.getElementById("Btn_Group");
    if (btn) {
        btn.click();
    }
}
function Send_Click(){
	var FK_Node = '<%=FK_Node%>';
	var FK_MapData = '<%=FK_MapData%>';
	var ListNum = '<%=ListNum%>';
	
	if(confirm("您确定要执行吗？")){	
		if(vailCheckBox()){
			alert("您没有选择工作！");
			return false;
		}
		
		//$("#FormHtml").val($("#form1").html());
		var url = "<%=basePath%>WF/BatchSend.do?FK_Node="+FK_Node+"&FK_MapData="+FK_MapData+"&ListNum="+ListNum;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
}
function Group_Click(){
	var FK_Node = '<%=FK_Node%>';
	var FK_MapData = '<%=FK_MapData%>';
	var ListNum = '<%=ListNum%>';
	
	if(confirm("您确定要执行吗？")){	
		if(vailCheckBox()){
			alert("您没有选择工作！");
			return false;
		}
		
		var url = "<%=basePath%>WF/BatchGroup.do?FK_Node="+FK_Node+"&FK_MapData="+FK_MapData+"&ListNum="+ListNum;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
}
function Return_Click(){
	var FK_Node = '<%=FK_Node%>';
	var FK_MapData = '<%=FK_MapData%>';
	var ListNum = '<%=ListNum%>';
	
	if(confirm("您确定要执行吗？")){	
		if(vailCheckBox()){
			alert("您没有选择工作！");
			return false;
		}
		
		var url = "<%=basePath%>WF/BatchReturn.do?FK_Node="+FK_Node+"&FK_MapData="+FK_MapData+"&ListNum="+ListNum;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
}
function Delete_Click(){
	var FK_Node = '<%=FK_Node%>';
	var FK_MapData = '<%=FK_MapData%>';
	var ListNum = '<%=ListNum%>';
	
	if(confirm("您确定要执行吗？")){	
		if(vailCheckBox()){
			alert("您没有选择工作！");
			return false;
		}
		
		var url = "<%=basePath%>WF/BatchDelete.do?FK_Node=" + FK_Node
					+ "&FK_MapData=" + FK_MapData + "&ListNum=" + ListNum;
			$("#form1").attr("action", url);
			$("#form1").submit();
		}
	}
	function vailCheckBox() {
		var vail = true;
		$.ajax({
			url : "LoadWorId.do",
			type : 'post',
			dataType : 'json',
			async : false,
			success : function(data) {
				if (data != "") {
					var emps = eval(data);
					for ( var emp in emps) {
						var value = emps[emp].workid;
						var obj = $("#CB_" + value);
						if (obj.length <= 0)
							continue;
						if (obj.attr("checked")) {
							vail = false;
						}
					}
				}
			}
		});
		return vail;
	}
</script>
</head>
<body>
	<!-- 内容 -->
	<!-- 表格数据 -->
	<div class="admin-content">

		<div class="am-cf am-padding">
			<div class="am-fl am-cf">
				<strong class="am-text-primary am-text-lg">首页</strong> / <small>批处理列表</small>
			</div>
		</div>
		<!-- 数据 -->
		<div class="am-g">
			<div class="am-u-sm-12">
				<form method="post" action="" class="am-form" id="form1">
					<!-- <input type="hidden" id="FormHtml" name="FormHtml" value=""></input> -->
					<%=model.ui.ListToString()%>
				</form>
				<!-- <ul class="am-pagination am-pagination-right">
					<li class="am-disabled"><a href="#">&laquo;</a></li>
					<li class="am-active"><a href="#">1</a></li>
					<li><a href="#">2</a></li>
					<li><a href="#">3</a></li>
					<li><a href="#">4</a></li>
					<li><a href="#">5</a></li>
					<li><a href="#">&raquo;</a></li>
				</ul> -->
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$('#Btn_Group').hide();
	});
</script>
</html>