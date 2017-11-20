<%@page import="java.util.ArrayList"%>
<%@page import="BP.WF.Glo"%>
<%@page import="BP.DA.DataRow"%>
<%@page import="BP.WF.Template.PubLib.FlowAppType"%>
<%@page import="BP.WF.Template.Flow"%>
<%@page import="BP.WF.Template.FlowSort"%>
<%@page import="BP.Port.WebUser"%>
<%@page import="BP.WF.Dev2Interface"%>
<%@page import="BP.DA.DataTable"%>
<%@page import="BP.WF.Template.FlowSorts"%>
<%@page import="BP.WF.Template.Flows"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询</title>
<link href="<%=Glo.getCCFlowAppPath() %>DataUser/Style/Table0.css" rel="stylesheet" type="text/css" />
  
 <script type="text/javascript">
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
            margin-right: 5px;
        }
        .noHaveIt
        {
        }
        .haveIt
        {
            color: Blue;
             font-weight:bolder;
        }
        li
        {
            list-style-image: url(Img/li_q_4.gif);
            line-height: 20px;
            height: 20px;
            margin-top: 3px;
        }
        .CaptionMsgLong{background: url(Port/Img/TitleMsgLong.png) no-repeat;}
        caption{background: url(Port/Img/TitleBG.png);}
    </style>
</head>
<body>
<table style="width: 100%;">
        <caption class="CaptionMsgLong">
            按流程查询 - <a href="/WF/Rpt/EasySearchMyFlow.jsp">高级查询</a>
        </caption>
        <td>
            <div style='float: left'>
                提示：点击流程名称，进入流程数据查询。</div>
            <div style='float: right'>
                <a href="javascript:WinOpen('KeySearch.jsp',900,900);">关键字查询</a> - <a href="javascript:WinOpen('/WF/Comm/Search.jsp?EnsName=BP.WF.Data.GenerWorkFlowViews',900,900);">
                    综合查询</a> - <a href="javascript:WinOpen('/WF/Comm/Group.jsp?EnsName=BP.WF.Data.GenerWorkFlowViews',900,900); ">
                        综合分析</a> - <a href="javascript:WinOpen('/WF/Comm/Search.jsp?EnsName=BP.WF.WorkFlowDeleteLogs',900,900); ">
                            删除日志</a></div>
        </td>
        <tr>
            <td>
                <%
                    StringBuilder sBuilder = new StringBuilder();
                    String flowRptHtml = "";
                    Flows fls = new Flows();
                    fls.RetrieveAll();

                    FlowSorts ens = new FlowSorts();
                    ens.RetrieveAll();

                    DataTable dt = Dev2Interface.DB_GenerCanStartFlowsOfDataTable(WebUser.getNo());

                    int cols = 3; //定义显示列数 从0开始。
                    int widthCell = 100 / cols;
                    sBuilder.append("<table width=100% border=0>");
                    int idx = -1;
                    boolean is1 = false;

                    String timeKey = "s" ;
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
                                sBuilder.append("<tr bgcolor=AliceBlue >");
                            else
                                sBuilder.append("<tr bgcolor=white >");

                            is1 = !is1;
                        }

                        sBuilder.append("<td width='" + widthCell + "%' border=0 valign=top  nowrap >");
                        //输出类别.
                        //this.Pub1.AddFieldSet(en.Name);
                        sBuilder.append(en.getName());
                        sBuilder.append("<ul>");

                        //输出流程。
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

                            sBuilder.append("<li><span class='haveIt'>" + index + "</span> <a  href=\"javascript:WinOpen('"+Glo.getCCFlowAppPath() +"WF/Rpt/Search.jsp?RptNo=ND" +
                             Integer.parseInt(fl.getNo()) + "MyRpt&FK_Flow=" +fl.getNo() + "');\" >" +fl.getName() + "</a> " + "</li>");
                            index += 1;
                        }
                        // 输出流程。

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
                            sBuilder.append("<td></td>");
                            sBuilder.append("</tr>");
                        }
                        else
                        {
                            sBuilder.append("<td></td>");
                        }
                    }
                    sBuilder.append("</table>");
                    flowRptHtml = sBuilder.toString();
                %>
                <%=flowRptHtml%>
            </td>
        </tr>
    </table>
</body>
</html>