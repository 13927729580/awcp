<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'editmenu.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="StyleSheet" href="<%=basePath%>WF/Style/dtree.css" type="text/css" />
	<script type="text/javascript" src="<%=basePath %>resources/JqEdition/jquery-1.9.1.min.js" charset="utf-8"></script>
	<script type="text/javascript" src="<%=basePath%>WF/Scripts/dtree.js" charset="utf-8"></script>
  </head>
  
  <body>
   <div id="con" style="float: left; margin-top: 10px; margin-left: 6px; width: 98%;">
		<ul id="tags">
			 <li id="projectLi" class="selectTag" state="0"><a onclick="selectTag('project',this)">项目基本信息</a></li>
			 <li id="customerLi" state="10"><a onclick="selectTag('customer',this)">客户信息</a></li>
		</ul>
		<div id="tagContent" style="margin-bottom: 10px;">
			<iframe id="iframe" name="iframe" height="350px" style="width: 100%;" onload="TuneHeight();" scrolling="no"frameborder="no"></iframe>
		</div>
	</div>
  </body>
   <script type="text/javascript">
   $("#${tab}Li").children().click();
function selectTag(showContent, selfObj) {
	 var url;
	 var proId = $("#projectId").val();
	 var majorProjectId = $("#majorProjectId").val();
	 if(showContent=="project"){
	  url="<%=basePath%>center.jsp";
	  
	 }else if(showContent=="customer"){
	  url="<%=basePath%>treeselect.jsp";
	 } $("#tags .selectTag").removeClass("selectTag");
	 $(selfObj).parent().addClass("selectTag");
	 $("#iframe").attr("src",url);
	}
	//自动调整高度
	function TuneHeight(){ 
	    var frm=document.getElementById("iframe");
	 frm.style.height="350px";
	    var subWeb=document.frames?document.frames["iframe"].document:frm.contentDocument; 
	    if(frm != null && subWeb != null){
	        var height = subWeb.documentElement.scrollHeight;
	        if(height<350)height=350;
	        frm.style.height = height +"px";
	    }
	 //window.top.TuneHeight();
	}
   
   </script> 
</html>
