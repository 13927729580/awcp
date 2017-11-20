<%@page import="org.jflow.framework.common.model.TruakModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	
	TruakModel tm = new TruakModel(request, response);
	tm.init();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
    <link href="<%=basePath%>WF/Comm/Style/CommStyle.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>WF/Comm/Style/Table0.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>WF/Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="<%=basePath%>WF/Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="<%=basePath%>WF/Scripts/easyUI/jquery-1.8.0.min.js" type="text/javascript"></script>
    <script src="<%=basePath%>WF/Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
        $(document).ready(function () {
            $("table tr:gt(0)").hover(
                function () { $(this).addClass("tr_hover"); },
                function () { $(this).removeClass("tr_hover"); }
                );
        });

        function WinOpen(url, winName) {
            var newWindow = window.open(url, winName, 'height=800,width=1030,top=' + (window.screen.availHeight - 800) / 2 + ',left=' + (window.screen.availWidth - 1030) / 2 + ',scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
            newWindow.focus();
            return;
        }
        function ReinitIframe(frmID, tdID) {
            try {

                var iframe = document.getElementById(frmID);
                var tdF = document.getElementById(tdID);
                iframe.height = iframe.contentWindow.document.body.scrollHeight;
                iframe.width = iframe.contentWindow.document.body.scrollWidth;
                if (tdF.width < iframe.width) {
                    tdF.width = iframe.width;
                } else {
                    iframe.width = tdF.width;
                }

                tdF.height = iframe.height;
                return;

            } catch (ex) {

                return;
            }
            return;
        }
    </script>
    
    <base target="_self" />
	<style type="text/css">
		.ActionType{
			    width:16px;
			    height:16px;
			}
	</style>
</head>
<body class="easyui-layout">
    <form id="form1" runat="server">
    <div data-options="region:'center',noheader:true">
    <!--     <uc1:TruakUC ID="TruakUC1" runat="server" /> -->
        	<%=tm.Pub1.toString()%>
			<%=tm.UCEn1.Pub.toString()%>
    </div>
    </form>
</body>
</html>