<%@page import="java.util.ArrayList"%>
<%@page import="BP.WF.Template.PubLib.FlowAppType"%>
<%@page import="BP.DA.DataRow"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.Port.WebUser"%>
<%@page import="BP.WF.Template.Flows"%>
<%@page import="BP.WF.Template.Flow"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.DA.DataTable"%>
<%@page import="BP.WF.Template.FlowSorts"%>
<%@page import="BP.WF.Template.FlowSort"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发起</title>
  <link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/Table0.css" rel="stylesheet" type="text/css" />
  
  <script type="text/javascript">
        function StartListUrl(appPath, url, fk_flow, pageid) {
            var v = window.showModalDialog(url, 'sd', 'dialogHeight: 550px; dialogWidth: 650px; dialogTop: 100px; dialogLeft: 150px; center: yes; help: no');
            //alert(v);
            if (v == null || v == "")
                return;

            window.location.href = appPath + '../MyFlow.jsp?FK_Flow=' + fk_flow + v;
        }
        function WinOpenIt(url) {
            var newWindow = window.open(url, '_blank', 'height=600,width=850,top=50,left=50,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
            newWindow.focus();
            return;
        }

        function WinOpen(url, winName) {
            var newWindow = window.open(url, winName, 'height=800,width=1030,top=' + (window.screen.availHeight - 800) / 2 + ',left=' + (window.screen.availWidth - 1030) / 2 + ',scrollbars=yes,resizable=yes,toolbar=false,location=false,center=yes,center: yes;');
            newWindow.focus();
            return;
        }
    </script>
  <style type="text/css">
        table
        {
            margin: 0px;
            padding: 0px;
        }
        td
        {
            font-family: Microsoft YaHei;
        }
        ul
        {
            margin-left: -10px;
            margin-top: 2px;
            font: 12px 宋体, Arial, Verdana;
        }
        span
        {
            font-size: 16px;
            font-family: Vijaya; 
            margin-right:5px;
        }    
        .noHaveIt{color:Red;}
        .haveIt{color:Blue;}
        li
        {
            list-style-image: url(Port/Img/li_q_4.gif);
            line-height: 20px;
            height: 20px;
            margin-top: 3px;
        }
        .CaptionMsg{background: url(Port/Img/TitleMsg.png);}
        caption{background: url(Port/Img/TitleBG.png);}
    </style>
</head>
<body>
 <%
 		String basePath = Glo.getCCFlowAppPath();
        Flows fls = new Flows();
        fls.RetrieveAll();

        FlowSorts ens = new FlowSorts();
        ens.RetrieveAll();

        DataTable dt = Dev2Interface.DB_GenerCanStartFlowsOfDataTable(WebUser.getNo());

        int cols = 3; //定义显示列数 从0开始。
        int widthCell = 100 / cols;

        String startHtml = "";
        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append("<table width=100% border=0>");
        sBuilder.append("<Caption ><div class='CaptionMsg' >发起流程</div></Caption>");//面板标题

        int idx = -1;
        boolean is1 = false;

        String timeKey = "s";
        ArrayList<FlowSort> flowsorts = FlowSorts.convertFlowSorts(ens);
        for(int i = 0; i< flowsorts.size() ; i++){
        	FlowSort en = flowsorts.get(i);
        	
            if (en.getParentNo().equals("0")
                || en.getParentNo().equals("")
                || en.getNo().equals(""))
                continue;

            idx++;
            if (idx == 0)
            {
                if (is1)
                    sBuilder.append("<tr bgcolor=AliceBlue>");
                else
                    sBuilder.append("<tr bgcolor=white>");

                is1 = !is1;
            }
            sBuilder.append("<td width='" + widthCell + "%' border=0 valign=top nowrap >");
            //输出类别.
            sBuilder.append(en.getName());
            sBuilder.append("<ul>");

            int index = 1;
            
            ArrayList<Flow> flows = Flows.convertFlows(fls);
            
            for(int t = 0; t< flows.size() ;t++){
            	Flow fl = flows.get(t);
           
                if (fl.getFlowAppType().equals(FlowAppType.DocFlow))
                    continue;

                if (!fl.getFK_FlowSort().equals(en.getNo()))
                    continue;

                boolean isHaveIt = false;
                for (DataRow dr : dt.Rows)
                {
                    if (!dr.getValue("No").toString().equals(fl.getNo()))
                        continue;
                    isHaveIt = true;
                    break;
                }

                String extUrl = "";
                if (fl.getIsBatchStart()) 
                	extUrl = "<div style='float:right;'><a href='"+basePath+"WF/BatchStart.jsp?FK_Flow=" + fl.getNo() + "' >批量发起</a>|<a href='/WF/Rpt/Search.aspx?RptNo=ND" +Integer.parseInt(fl.getNo()) + "MyRpt&FK_Flow=" + fl.getNo() + "'>查询</a>|<a href=\"javascript:WinOpen('/WF/Chart.aspx?FK_Flow=" + fl.getNo() + "&DoType=Chart&T=" + timeKey + "','sd');\"  >图</a></div>";
                 else
                    extUrl = "<div style='float:right;'><a  href='"+basePath+"WF/Rpt/Search.jsp?RptNo=ND" +Integer.parseInt(fl.getNo())  + "MyRpt&FK_Flow=" + fl.getNo() + "'>查询</a>|<a href=\"javascript:WinOpen('/WF/Chart.jsp?FK_Flow=" + fl.getNo() + "&DoType=Chart&T=" + timeKey + "','sd');\"  >流程图</a></div>";

                if (isHaveIt)
                {
                    if (Glo.getIsWinOpenStartWork() == 1)
                    {
                        sBuilder.append("<li><span class='haveIt'>" + index + "</span><a href=\"javascript:WinOpenIt('"+basePath+"WF/MyFlow.jsp?FK_Flow=" + fl.getNo() + "&FK_Node=" + Integer.parseInt(fl.getNo()) + "01&T=" + timeKey + "');\" >" + fl.getName() + "</a>" + extUrl + "</li>");
                    }
                    else if (Glo.getIsWinOpenStartWork() == 2)
                    {
                        sBuilder.append("<li><span class='haveIt'>" + index + "</span><a href=\"javascript:WinOpenIt('"+basePath+"WF/OneFlow/MyFlow.jsp?FK_Flow=" + fl.getNo() + "&FK_Node=" + Integer.parseInt(fl.getNo()) + "01&T=" + timeKey + "');\" >" + fl.getName() + "</a>" + extUrl + "</li>");
                    }
                    else
                    {
                        sBuilder.append("<li><span class='haveIt'>" + index + "</span><a href='MyFlow.jsp?FK_Flow=" + fl.getNo() + "&FK_Node=ND" +Integer.parseInt(fl.getNo()) + "01' >" + fl.getName() + "</a>" + extUrl + "</li>");
                    }
                }
                else
                {
                    sBuilder.append("<li><span class='noHaveIt'>" + index + "</span>" + fl.getName() + "</li>");
                }
                index += 1;
            }

            sBuilder.append("</ul>");
            sBuilder.append("</td>");
            if (idx == cols - 1)
            {
                idx = -1;
                sBuilder.append("</tr>");
            }
        }

        while (idx != -1)
        {
            idx++;
            if (idx == cols - 1)
            {
                idx = -1;
                sBuilder.append("</td>");
                sBuilder.append("</tr>");
            }
            else
            {
                sBuilder.append("</td>");
            }
        }
        sBuilder.append("</table>");
        startHtml = sBuilder.toString();
    %>
    <%=startHtml%>
</body>
</html>