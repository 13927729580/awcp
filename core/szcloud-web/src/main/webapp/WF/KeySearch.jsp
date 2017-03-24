<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/head/head.jsp"%>
<head>
<title>驰骋工作流程管理系统</title>
<script type="text/javascript">
    function OpenIt(fk_flow, fk_node, workid) {
        var url = './WFRpt.jsp?WorkID=' + workid + '&FK_Flow=' + fk_flow + '&FK_Node=' + fk_node;
        var newWindow = window.open(url, 'card', 'width=700,top=50,left=50,height=500,scrollbars=yes,resizable=yes,toolbar=false,location=false');
        newWindow.focus();
        return;
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
    function buttonClick(btnName){
    	var textBox1 = $("#TextBox1").val();
    	if(textBox1 == null || textBox1 == ""){
    		return;
    	}
    	var date = new Date();
    	var isCheck = $("#CheckBox1").attr("checked");
    	var url = "<%=basePath%>WF/KeySearch.do?textBox1="+textBox1+"&btnName="+btnName+"&isCheck="+isCheck+"&s="+date;
		$("#form1").attr("action",url);
		$("#form1").submit();
    }
</script>
</head>
<body>
	<form method="post" class="am-form" action="" id="form1">
		<table class="am-table am-table-striped am-table-hover table-main">
			<th class='table-title' colspan="3">关键字全局查询</th>
			<tbody>
			<tr>
				<td style="text-align: right"><b>&nbsp;输入任何关键字:</b></td>
				<td>
				<input name="TextBox1" style="width: 300px"
						type="text" id="TextBox1" value="${text }"/>
				</td>
				<td>
				<span style="color: #0033CC; font-weight: bold;">
				<input id="CheckBox1" type="checkbox" name="CheckBox1" ${isCheck }/>仅查询我参与的流程</span><br />
				</td>
			</tr>
			</tbody>
		</table>
		<div class="am-margin">
			<button name="Btn_ByWorkID" id="Btn_ByWorkID" type="button" onclick="buttonClick('Btn_ByWorkID')"
				class="am-btn am-btn-primary am-btn-xs">按工作ID查</button>
			<button name="Btn_ByTitle" id="Btn_ByTitle" type="button" onclick="buttonClick('Btn_ByTitle')"
				class="am-btn am-btn-primary am-btn-xs">按流程标题字段关键字查</button>
		</div>
		${htmlStr }
	</form>
</body>
</html>
