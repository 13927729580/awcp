<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>驰骋工作流程管理系统</title>
<script language="JavaScript" src="<%=basePath %>WF/Comm/JScript.js"
	type="text/javascript"></script>
<script language="JavaScript" src="<%=basePath %>WF/Scripts/jquery-1.7.2.min.js"
	type="text/javascript"></script>
<script src="<%=basePath %>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
<link href="<%=basePath %>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath %>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet"
	type="text/css" />
<script src="<%=basePath %>WF/Scripts/easyUI/easyloader.js" type="text/javascript"></script>
<link id="freeFormcss" rel="stylesheet" type="text/css" />
<link id="winOpencss" rel="stylesheet" type="text/css" />
<link href="<%=basePath %>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
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
    $(function () {
        var userStyle = "ccflow默认";
        $('#winOpencss').attr('href', 'Style/FormThemes/WFWinOpen.css');
        $('#freeFormcss').attr('href', 'Style/FormThemes/Table0.css');
    });
</script>
<base target="_self" />
</head>
<body topmargin="0" leftmargin="0" onkeypress="NoSubmit(event);" class="easyui-layout">
	<form method="post" id="form1" style="height: 100%">
		<div id="mainPanel" region="center" border="true" border="false" class="mainPanel">
			<table width="100%">
				<caption>关键字全局查询</caption>
				<tr>
					<td><b>&nbsp;输入任何关键字:</b>
					<input name="TextBox1" type="text" id="TextBox1" value="${text }"
						style="border-color: AliceBlue; border-style: Inset; font-size: Large; font-weight: bold; width: 259px;" />
						<span style="color: #0033CC; font-weight: bold;">
					
					<input id="CheckBox1" type="checkbox" name="CheckBox1" ${isCheck }/>仅查询我参与的流程</span> <br />
						&nbsp;<input type="button" name="Btn_ByWorkID" value="按工作ID查"
						id="Btn_ByWorkID" onclick="buttonClick('Btn_ByWorkID')" /> 
						<input type="button" name="Btn_ByTitle" value="按流程标题字段关键字查"
						id="Btn_ByTitle" onclick="buttonClick('Btn_ByTitle')" />
						<hr /></td>
				</tr>
				${htmlStr }
			</table>
		</div>
	</form>
</body>
</html>
