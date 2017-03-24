<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/head/head.jsp"%>
<%
	MyFlowModel myFlow = new MyFlowModel(request, response);
	myFlow.initFlow();
	if (!myFlow.getIsContinue()) {
		return;
	}
%>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>WF/Style/FormThemes/MyFlow.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>WF/Style/FormThemes/WinOpenEUI.css" />
<script type="text/javascript" language="javascript" src="<%=basePath%>DataUser/PrintTools/LodopFuncs.js"></script>
<script type="text/javascript" language="javascript" src="<%=basePath%>WF/Scripts/MyFlow.js"></script>

<style>
* {
	margin: 0px;
	padding: 0px;
}

input, body {
	font: 12px '微软雅黑', Arial, Helvetica, sans-serif
}

.input_text {
	border: 1px solid #CED5DB;
	height: 20px;
	width: 100px;
}

.input_button {
	background: #FED778;
	border: 1px solid #DEA203;
	color: #853433
}

table {
	border-collapse: collapse;
}

.table_list {
	margin-top: 30px;
}

.table_list tr td {
	border: 1px solid #CED5DB;
	text-align: center;
	line-height: 28px;
}

.td_red {
	color: #F00;
	font-weight: bold
}

.table_list tr:hover {
	background: #F1F1F1
}

#divCCForm {
	position: relative !important;
}
.preview{text-overflow:ellipsis;overflow:hidden;white-space:nowrap;width:45%;display: inline-block;}
</style>
<script type="text/javascript">
function ReturnVal(ctrl, url, winName) {
    if (ctrl && ctrl.value != "") {
        if (url.indexOf('?') > 0)
            url = url + '&CtrlVal=' + ctrl.value;
        else
            url = url + '?CtrlVal=' + ctrl.value;
    }
    if (typeof self.parent.TabFormExists != 'undefined') {
        var bExists = self.parent.TabFormExists();
        if (bExists) {
            self.parent.ChangTabFormTitleRemove();
        }
    }
    //    if (window.ActiveXObject) {
    //        //    var v = window.showModalDialog(url, winName, 'scrollbars=yes;resizable=yes;center=yes;minimize:yes;maximize:yes;dialogHeight: 650px; dialogWidth: 850px; dialogTop: 100px; dialogLeft: 150px;');
    //        var v = window.showModalDialog(url, winName, 'scrollbars=yes;resizable=yes;center=yes;minimize:yes;maximize:yes;dialogHeight: 650px; dialogWidth: 850px; dialogTop: 100px; dialogLeft: 150px;');
    //        if (v == null || v == '' || v == 'NaN') {
    //            return;
    //        }
    //        ctrl.value = v;
    //    }
    //    else {
    //        window.showModalDialog(url + '?temp=' + Math.random(), winName, 'scrollbars=yes;resizable=yes;center=yes;minimize:yes;maximize:yes;dialogHeight: 650px; dialogWidth: 850px; dialogTop: 100px; dialogLeft: 150px;');
    //    }

    //OpenJbox();
    if (window.ActiveXObject) {
        var v = window.showModalDialog(url, winName, 'scrollbars=yes;resizable=yes;center=yes;minimize:yes;maximize:yes;dialogHeight: 650px; dialogWidth: 850px; dialogTop: 100px; dialogLeft: 150px;');
        if (v == null || v == '' || v == 'NaN') {
            return;
        }
        ctrl.value = v;
    }
    else {
      //  try {
        	var v = window.showModalDialog(url, '', 'scrollbars=yes;resizable=yes;center=yes;resizable:no;minimize:yes;maximize:yes;dialogHeight: 650px; dialogWidth: 550px; dialogTop: 50px; dialogLeft: 650px;');
        	 if (v == null || v == '' || v == 'NaN') {
                 return;
             }
        	
        	ctrl.value = v;
        	//             //OpenJbox();
//             $.jBox("iframe:" + url, {
//                 title: '',
//                 width: 800,
//                 height: 350,
//                 buttons: { 'Sure': 'ok' },
//                 submit: function (v, h, f) {
//                     var row = h[0].firstChild.contentWindow.getSelected();
//                     ctrl.value = row.Name;
//                 }
//             });
//         } catch (e) {
//             alert(e);
//         }
    }
    
    if (typeof self.parent.TabFormExists != 'undefined') {
        var bExists = self.parent.TabFormExists();
        if (bExists) {
            self.parent.ChangTabFormTitle();
        }
    }
    //    if (v == null || v == '' || v == 'NaN') {
    //        return;
    //    }
    //    ctrl.value = v;
    return;
}
	function Send(){
		$.ajax({
			cache: true,
			type: "POST",
			url:"<%=basePath%>WF/MyFlow/SendWork.do",
			data:$('#mainFrom').serialize(),
		    success: function(data) {
		    	parseJson(data);
		    }
		});
	}
	
	function Save(){
		
		$.ajax({
				cache: true,
				type: "POST",
				url:"<%=basePath%>WF/MyFlow/SaveWork.do",
				data : $('#mainFrom').serialize(),
				success : function(data) {
					 $('#Btn_Save').popover({
						 content:'保存成功'
					 });
					 setTimeout("codefans()",3000);
					 parseJson(data);
			}
		});
	}
	function codefans()
	{
		$('#Btn_Save').popover('close');
	}

	function parseJson(data) {
		var obj = eval("(" + data + ")");
		if (obj.type == 'data') {
			var rows = obj.rows;
			for ( var key in rows) {
				var comp = $("#" + key + "");
				if (comp == null) {
					continue;
				}
				if (key.indexOf("TB_") != -1) {
					var input_text = $("input[id=" + key + "]");
					if (input_text == null) {
						comp.text(rows[key]);
					} else {
						comp.attr("value", rows[key]);
					}
				} else if (key.indexOf("DDL_") != -1) {
					comp.attr("value", rows[key]);
				} else if (key.indexOf("CB_") != -1) {
					if (rows[key] == '0') {
						comp.attr("checked", false);
					} else {
						comp.attr("checked", true);
					}
				}
			}
		} else if (obj.type == 'alert') {
			alert(obj.action);
		} else if (obj.type == 'url') {
			window.location.href = obj.action;
		} else if (obj.type == 'script') {
			eval(obj.action);
		} else if (obj.type == 'flowMsg') {
			document.getElementById("Message").innerHTML = obj.action;
		}
	}
	//然浏览器最大化.
	function ResizeWindow() {
		if (window.screen) { //判断浏览器是否支持window.screen判断浏览器是否支持screen     
			var myw = screen.availWidth; //定义一个myw，接受到当前全屏的宽     
			var myh = screen.availHeight; //定义一个myw，接受到当前全屏的高     
			window.moveTo(0, 0); //把window放在左上脚     
			window.resizeTo(myw, myh); //把当前窗体的长宽跳转为myw和myh     
		}
	}
	window.onload = ResizeWindow();

	function Change() {
		var btn = document.getElementById('Btn_Save');
		if (btn != null) {
			if (btn.value.valueOf('*') == -1)
				btn.value = btn.value + '*';
		}
	}
	
	function ReqDDL(ddlID) {
	    var v = document.getElementById('DDL_' + ddlID).options.length;
	    if (v == null) {
	        alert('没有找到ID=' + ddlID + '的下拉框控件.');
	    }
	    return v;
	}
</script>
</head>
<body class="am-collapse admin-sidebar-sub am-in" topmargin="0"
	leftmargin="0" onkeypress="NoSubmit(event);" style='width: 100%; height: 100%;background-color: white'>
	<div style='height:<%=myFlow.getFromH()%>px;'>
		<div id='ToolBar' align="left" style="padding-top: 10px; padding-left: 80px">
			<jsp:include page="./SDKComponents/Toolbar.jsp">
				<jsp:param value="<%=myFlow.workId%>" name="WorkID" />
				<jsp:param value="<%=myFlow.fk_flow%>" name="FK_Flow" />
				<jsp:param value="<%=myFlow.fk_node%>" name="FK_Node" />
				<jsp:param value="<%=myFlow.fid%>" name="FID" />
				<jsp:param value="<%=myFlow.getHisFormType()%>" name="FormType" />
			</jsp:include>
		</div>
		<hr>
		<div style="word-break: break-all; padding-left: 30px; width: 900px;" class="Message" id='Message'></div>
		<form id="mainFrom" method="post">
			<input type="hidden" name="WorkID" value="<%=myFlow.workId%>" /> <input
				type="hidden" name="FK_Flow" value="<%=myFlow.fk_flow%>" /> <input
				type="hidden" name="FK_Node" value="<%=myFlow.fk_node%>" /> <input
				type="hidden" name="FID" value="<%=myFlow.fid%>" />
				<div id='divCCForm'>
				<%
				
				if(myFlow.getHisFormType()){
					%>
					<%=myFlow.selfFromPub %>
					<%
				}else{
					%>
					<jsp:include page="./SDKComponents/En.jsp">
					<jsp:param value="<%=myFlow.workId%>" name="WorkID" />
					<jsp:param value="<%=myFlow.fk_flow%>" name="FK_Flow" />
					<jsp:param value="<%=myFlow.fk_node%>" name="FK_Node" />
					<jsp:param value="<%=myFlow.fid%>" name="FID" />
					<jsp:param value="<%=myFlow.getHisFormType()%>" name="FormType"/>
				</jsp:include>
					<%
				}
				%>
				
			</div>

		</form>
	</div>
</body>
</html>