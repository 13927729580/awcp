<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统业务编辑</title>
<link href="base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
<link href="base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/zui.min.css">
<link rel="stylesheet" type="text/css" href="./resources/styles/zTreeStyle/zTreeStyle.css">
<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js" language="javascript" type="text/javascript"></script>
<script src="base/resources/content/scripts/jquery.ztree.all-3.5.js" language="javascript" type="text/javascript"></script>
<link rel="stylesheet" href="base/resources/artDialog/css/ui-dialog.css">
<script src="base/resources/artDialog/dist/dialog.js"></script>
<style>
ul,ul li {list-style:none;padding:0;margin:0;}
ul.CloudTreeMenu li .ztree {display: block;}
.input-group{margin-bottom:10px;}

</style>
</head>
<body>
<div id="bizList" class="whole" style="margin:20px;">

</div>
<script language="javascript">
$(function() {
	var bizType = '<%=request.getParameter("businessType")== null ? "" : request.getParameter("businessType")%>';
	var year = '<%=request.getParameter("year")== null ? "" : request.getParameter("year")%>';
	var sysId = '<%=request.getParameter("sysId")%>';
	initializeItemList(bizType, year, sysId);
});
function initializeItemList(bizType, year, sysId){
	var itemListStr = "";
	if(bizType == "" || year == "" || sysId == ""){
		itemListStr = "<span>所有的业务皆已关闭</span>"; 
		$("#bizList").append(itemListStr);
		return;
	}
	else{
		$.ajax({
			type : "POST",
			url : "<%=basePath%>business/querySystemBusinessItemVO.do?businessType=" + bizType + "&year=" + year + "&sysId="+sysId,
			success : function(data) 
			{
				if(data != null && data != "")
				{
					for(var i = 0; i < data.length;i++)
					{
						var item = data[i];
						var paddingCount=(item.level-1) * 20;
						itemListStr += "<span style='padding-left:" +paddingCount + "px'>";
						
						if(item.dynamicPageId != null && item.status == 1){
							itemListStr += "<a href='javascript:test("+item.dynamicPageId+")'>" + item.name + "</a>";
						}
						else if(item.status == 2)
						{
							itemListStr += item.name + "(截止"+ item.comment + ")";
						}
						else {
							itemListStr += item.name;
						}
						itemListStr += "</span><br>";
					}
				}
				else{
					itemListStr = "<span>所有的业务皆已关闭</span>"; 
				}
				$("#bizList").append(itemListStr);
			}
		});
	}
}
function test(dynamicPageId){
	var backId = $(this.parent.document).find("input#dynamicPageId").val();
	window.parent.location = "<%=basePath%>document/view.do?id=&dynamicPageId=" + dynamicPageId + "&backId=" + backId;
}
</script>
</body>
</html>