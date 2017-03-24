<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>新建公文</title>
<link rel="stylesheet" href="<%=basePath%>resources/mobile/resources/styles/jquery.mobile-1.4.5.css">
<link rel="stylesheet" href="<%=basePath%>resources/mobile/resources/styles/font-awesome.min.css">
<link rel="stylesheet" href="<%=basePath%>resources/mobile/resources/styles/mobile.drable.css">
<link rel="shortcut icon" href="../favicon.ico">
<script src="<%=basePath%>resources/JqEdition/jquery-2.2.3.min.js"></script>
<script src="<%=basePath%>resources/mobile/js/jquery.mobile-1.4.5.js"></script>
<script src="<%=basePath%>resources/mobile/resources/scripts/jquery.drag.vendor.js"></script>
<script src="<%=basePath%>resources/mobile/resources/scripts/jquery.drag.adapter.js"></script>
<script src="<%=basePath%>resources/mobile/resources/scripts/jquery.dragloader.js"></script>
<style>
	html,body{
		overflow:hidden;
	}
	.ui-listview
	{
		height:auto;
	}
	.ui-navbar ul li{
			padding-right: 4px;
			padding-left: 4px;}
		.choose_div{
			height:auto;}
		.choice-list{
			list-style:none;
			margin-top:3px;
			margin-left:-40px;}
		.choice-list li:hover {
    		background-color: #FFF;
    		border-color: #C4C4C4;
   	 		box-shadow: 0px 1px 0px rgba(0, 0, 0, 0.1);}
   	 	.choice-list li {
    		padding: 5px 5px;
    		margin: 0px 0px 3px 6px;
    		line-height: 25px;
    		cursor: default;
    		border: 1px solid #DDD;
    		border-radius: 3px;
    		font-size:16px;
    		float:left;}
    	.choice-list a {
    		text-decoration: none;
    		color: #666;
    		font-family: Aril;
    		cursor: pointer;
    		margin-left:5px;}
    		.ui-link{
	text-decoration:none;
}
html,body{
	overflow:hidden;
}
</style>
</head>
<body>
<div data-role="page" id="hospage">
	<div data-role="header" style="height:40px;overflow:hidden;" data-position="fixed" class="ui-header-blue" id="header">
		<h3 style="font-size:20px"><b>新建公文</b></h3>
		<a href="#" data-rel="back" class="ui-btn-left ui-alt-icon ui-nodisc-icon ui-btn ui-icon-carat-l ui-btn-icon-notext ui-corner-all">Back</a>
		<!--<a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-icon-left ui-icon-gear">设置</a>-->
	</div>
	<div style="overflow:hidden;" class="ui-content" id="main1Content" role="main" data-role="content">
    	<div class="ui-field-contain" data-role="fieldcontain">
              <div class="ui-body-d ui-content">
                	<div data-role="collapsibleset">
                	<c:forEach items="${workTypeList}" var="item">
					   <div data-role="collapsible"  data-collapsed="true" >
	              		 <h6 onclick="getTemplateList('${item.No}')" id="getTemplateListClick">
	              		 	<a href="#" data-rel="dialog" data-transition="slideup" id="cylxr_div_a">
                                <h3 style="margin-top:0.8em;font-size:16px;color:#333;">${item.Name}类</h3>
                            </a>
	              		 </h6>
	              		 <fieldset data-role="controlgroup"  data-iconpos="right"> 
	              		 <ul id="a${item.No}" data-role="listview" id="chen_test_cylxr"  data-inset="true" data-filter="true" data-filter-placeholder="搜索 ...">
	              		 	 
						 </ul>
						 </fieldset>
					  </div>
					  </c:forEach>
				 </div>
              </div>
            </div>
    </div>
</div>
<script>
$(document).ready(function() {
    $("#header").removeClass("ui-bar-inherit");
	$("#btn-menu").removeAttr("class");
	$("#btn-menu").addClass("ui-nodisc-icon ui-btn-left btn-menu ");
	$("#navbar").addClass("hostabbar");
	$("#getTemplateListClick").trigger("click");
});
function getTemplateList(id)
{
	$.ajax({
		url:"<%=basePath%>oa/app/gettemplateListById.do?id="+id,
		type:"post",
		success:function(data){
			var arr = new Array();
			var str="";
			for(var i=0;i<data.length;i++){
				arr.push("<li><label for='cylxr717136' class='ui-btn ui-corner-all ui-btn-inherit'><span style='margin-left: 10px;float:left'><a class='ui-link' href='javascript:aclick(\"");
				arr.push(data[i].No.toString());
				arr.push("\",\"");
				arr.push(parseInt(data[i].No));
				arr.push("01\")'>");
				arr.push(data[i].Name);
				arr.push("</a></span></label></li>");
				
			}
			str = arr.join("");
			$("#a"+id).empty();
			$("#a"+id).append(str);
			str="";
			$("#a"+id).trigger('create');
		}
	}) 
}
function aclick(FK_Flow, NodeID)
{
	location.href="<%=basePath%>WF/MyFlow.jsp?FK_Flow="+FK_Flow+"&FK_Node="+NodeID+"&T=&app=1";
}
</script>
</body>
</html>