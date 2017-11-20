<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><title>
	
        驰骋工作流程管理系统
</title>
    
    <script language="JavaScript" src="./Comm/JScript.js" type="text/javascript"></script>
    <script language="JavaScript" src="./Comm/JS/Calendar/WdatePicker.js" defer="defer"
        type="text/javascript"></script>
    

     
    <link href="../Style/FormThemes/Table0.css" rel="stylesheet" type="text/css" />

    <base target="_self" />
</head>
<body topmargin="0" leftmargin="0" onkeypress="NoSubmit(event);" class="easyui-layout">
    <form method="post" action="DBInstall.aspx" id="form1" onkeypress="NoSubmit(event);">
<div class="aspNetHidden">
<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwUKLTYyMDY5NDE3NWRk/tClLNs+LORLnqxeWp1wW6VcMKOh+v7ae3gkbCc7e5g=" />
</div>

<link href='/WF/Comm/Style/Table0.css' rel='stylesheet' type='text/css' />
    <div id="mainPanel" region="center" border="true" border="false" class="mainPanel">
        
<div width=300px >
    <H3>ccflow 数据库修复与安装工具</H3><HR><fieldset width='100%' ><legend>&nbsp;提示&nbsp;</legend>数据已经安装，如果您要重新安装，您需要手工的清除数据库里对象。</fieldset><fieldset width='100%' ><legend>&nbsp;修复数据表&nbsp;</legend>把最新的版本的与当前的数据表结构，做一个自动修复, 修复内容：缺少列，缺少列注释，列注释不完整或者有变化。 <br> <a href='DBInstall.aspx?DoType=FixDB' >执行...</a>。<B><br><a href='/' >进入流程设计器</a></B></fieldset>
    </div>

    </div>
    </form>
</body>
</html>