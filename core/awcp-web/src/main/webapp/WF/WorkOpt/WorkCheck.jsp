<%@page import="org.jflow.framework.common.model.WorkCheckM"%>
<%@ page language="java" isELIgnored="false" import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="BP.DA.*"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.Tools.StringHelper"%>
<%@page import="BP.Port.WebUser"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.WF.Entity.FrmWorkCheck"%>
<%@page import="BP.Tools.StringHelper"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" 
	+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	int FK_Node=Integer.valueOf(request.getParameter("FK_Node"));
 	long FID=Long.valueOf(StringHelper.isEmpty(request.getParameter("FID"), "0"));
 	String FK_Flow=request.getParameter("FK_Flow");
 	String s = request.getParameter("Paras");
 	boolean IsCC = false;
    if (s!=null && s.contains("IsCC"))
    	IsCC= true;
    String DoType=request.getParameter("DoType");
    
 	long WorkID=0;
    String workid = request.getParameter("OID");
    if (workid == null)
        workid = request.getParameter("WorkID");
    WorkID = Long.valueOf(workid);
    
    String table1 = "";
    String table2 = "";
    
    WorkCheckM wcModel = new WorkCheckM(request, response);
    wcModel.Page_Load();
%>
<!DOCTYPE>
<html>
<head>
<script type="text/javascript" language="javascript"> 
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
 
    function show_and_hide_tr(tb_id, obj) {
        $("#" + tb_id).find("tr").each(function (i) {
            i > 0 ? (this.style.display == "none" ? this.style.display = "" : this.style.display = "none") : ($(this).next().css("display") == "none" ? (obj.src = '../Img/Tree/Cut.gif') : (obj.src = '../Img/Tree/Add.gif'));
        });
    }
 
    function TBHelp(ctrl,enName, attrKey) {
        var url = "<%=basePath%>WF/Comm/HelperOfTBEUI.jsp?EnsName=" + enName + "&AttrKey=" + attrKey;
        var str = window.showModalDialog(url, 'sd', 'dialogHeight: 500px; dialogWidth:400px; dialogTop: 150px; dialogLeft: 200px; center: no; help: no');
        if (str == undefined)
            return;
        
        $("*[id$=" + ctrl + "]").focus().val(str);
        
    }
    function btn_Click() {
         $("#form1").submit();
    }

</script>
	<link href="<%=basePath%>WF/Comm/Style/Table.css" rel="stylesheet" type="text/css" /><link href="../Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>WF/Scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
</head>
<body leftMargin=0 topMargin=0 >
    <form id="form1" method="post" action="<%=basePath %>/WF/WorkOpt/WorkCheckSave.do?s=2&r=q&FK_Flow=<%=FK_Flow %>&FK_Node=<%=FK_Node %>&FID=<%=FID %>&WorkID=<%=WorkID %>"  style="word-break:break-all">
    	<%=wcModel.Pub1.toString() %>
    </form>
</body>

</html>
