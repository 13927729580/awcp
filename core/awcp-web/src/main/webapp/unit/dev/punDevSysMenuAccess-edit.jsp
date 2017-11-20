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
<title>系统菜单编辑</title>
<link href="base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
<link href="base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/zui.min.css">
<link rel="stylesheet" type="text/css" href="resources/styles/zTreeStyle/zTreeStyle.css">
<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js" language="javascript" type="text/javascript"></script>
<script src="base/resources/content/scripts/jquery.ztree.all-3.5.js" language="javascript" type="text/javascript"></script>
<link rel="stylesheet" href="base/resources/artDialog/css/ui-dialog.css">
<script src="base/resources/artDialog/dist/dialog-min.js"></script>
<style>
ul,ul li {list-style:none;padding:0;margin:0;}
ul.CloudTreeMenu li .ztree {display: block;}
.input-group{margin-bottom:10px;}

</style>
</head>
<body>
<div class="whole" style="margin:20px;">
	
	<div class="row" id="breadcrumb">
		<ul class="breadcrumb">
			<li><i class="icon-location-arrow icon-muted"></i></li>
			<li class="active">组织机构与权限</li>
			<li><a href= "<%=basePath%>dev/punDevRoleInfoList.do?currentPage=0">角色管理</a></li>
			<li class="active">组信息编辑</li>
		</ul>
	</div>
	<div style="margin-top:20px;">
      <ul class="CloudTreeMenu"></ul>    
   </div>
	<div class="C_btns">
		<a class="btn btn-info" href="<%=basePath%>/dev/punDevRoleInfoList.do?currentPage=0">返回</a>
	</div>
</div>
<%-- <p>${menuJson}</p> --%>
<script language="javascript">
var roleId = ${vo.id};//***
var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: zTreeBeforeClick,
		beforeCheck: zTreeBeforCheck 
		//onCheck:zTreeOnCheck
	}
};	

function zTreeBeforeClick(){
	return false;
};
function zTreeBeforCheck(treeId, treeNode){
	//alertMessage(treeNode.id + ", " + treeNode.name + "," + treeNode.checked);
	var checked = treeNode.checked;
	var result = function(url,data){var val ;$.ajax({
		url:url,data:data,async:false,success:function(data){
			val = data.status=="0"?true:false;
		}
	});return val;};
	if(!checked){//添加权限
		checkResult = result("<%=basePath%>dev/punDevRoleAccessAJAXSave.do",{ roleId: roleId, resourceId: treeNode.id, access:"1"});
		checkResult || alertMessage("抱歉，操作失败");
		return checkResult;
		//checkResult?(return true):(alertMessage("抱歉，操作失败");return false;) 
	}else{//删除权限
		checkResult = result("<%=basePath%>dev/punDevRoleAccessAJAXDelete.do", { roleId: roleId, resourceId: treeNode.id })
		checkResult || alertMessage("抱歉，操作失败");
		return checkResult;
		//checkResult?(return true):(alertMessage("抱歉，操作失败");return false;)	
	}
		
};
function creatMenu(){
	var dataLi="";
	var dataMap = ${menuJson};
	$.each(dataMap,function(i,item){
		//alert(123)
		dataLi ='<li><ul id="tree'+i+'" class="ztree"></ul></li>';
		$(dataLi).appendTo(".CloudTreeMenu");
		$.fn.zTree.init($("#tree"+i),setting,item);
		  //$("#tree"+i+" > li").eq(0).remove();
	});
}

function alertMessage(message){
	var md = dialog({
	    content: message
	});
	md.show();
	setTimeout(function () {
	    md.close().remove();
	}, 2000);
}
$(document).ready(function(){
	creatMenu();
});
</script>
</body>
</html>