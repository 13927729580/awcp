<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../resources/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>政务云应用管理平台</title>

<link rel="stylesheet" href="./base/resources/zui/dist/css/zui.css" >
<link rel="stylesheet" href="${ctxBase}/styles/theme_blue.css" >
<link rel="stylesheet" href="${ctxBase}/styles/szCloud_common.css" />
<link rel="stylesheet" href="./resources/plugins/zTree_v3/css/style.css" />
<link rel="stylesheet" type="text/css" href="./resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="./base/resources/artDialog/css/ui-dialog.css" >
<!--[if lt IE 9]>
  <script src="${ctxBase}/plugins/zui/assets/html5shiv.js"></script>
  <script src="${ctxBase}/plugins/zui/assets/respond.js"></script>
<![endif]-->
</head>
<body>
  <header>
 	<nav id="navbar" class="navbar navbar-inverse navbar-fixed-left navbar-collapsed" role="navigation" >
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
				<span class="sr-only">Toggle navigation</span> 
				<span class="icon-bar"></span>
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span>
			</button>
			<a id="LogoBar" class="navbar-brand" href="javascript:;" target="frameRight">后台管理</a>
		</div>
		<div class="collapse navbar-collapse" id="navbar-collapse">
			<ul class="CloudMenu"></ul>
			<div class="settingBtn">
		       <a class="btn btn-sm btn-primary" href="javascript:;">设置</a>
			   <a class="btn btn-sm" href="<%=basePath%>admin/logout.do">退出</a>
			</div>
		</div>
		
	</nav> 
  </header>
  
  
  <div class="C-contentFrame clearfix">
        
            <iframe src="<%=basePath%>admin/main.jsp"  name="main" scrolling="auto" frameborder="0" width="100%" height="100%"></iframe> 

  </div>
 
 

<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
<script src="${ctxBase}/scripts/jquery.cookies.js"></script>
<script src="base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
<script src="${ctxBase}/plugins/zTree_v3/js/jquery.ztree.all-3.5.js" language="javascript" type="text/javascript"></script>
<script src="${ctxBase}/scripts/jquery.ManageList.ui.js" language="javascript" type="text/javascript"></script>
<script src="base/resources/artDialog/dist/dialog-plus-min.js"></script>

<script language="javascript">
var setting = {
	view: {
		showLine: false,
		showIcon: showIconForTree
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};	

function showIconForTree(treeId, treeNode) {
	return !treeNode.isParent;
};

function creatMenu(){
	var dataLi="";
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=basePath%>admin/getZTreeNode.do",
		async:false,//同步
		data:{"userId":1,"groupId":1,"roleId":1},
		success:function(dataMap){
			 $.each(dataMap,function(i,item){
				 dataLi ='<div class="ztree_position"><div class="ztree_cover"></div><ul id="tree'+i+'" class="ztree"></ul></div>';//<li><div class="header"><span class="label">'+item[0].name+'</span><span class="arrow down"></span></div><ul id="tree'+i+'" class="ztree"></ul></li>
				  $(dataLi).appendTo(".CloudMenu");
				  $.fn.zTree.init($("#tree"+i),setting,item);
				  //$("#tree"+i+" > li").eq(0).remove();
			});		
		}
		
	});
}

$(document).ready(function(){
	creatMenu();
});
</script>


</body>
</html>
