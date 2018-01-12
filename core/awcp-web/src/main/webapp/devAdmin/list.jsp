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

<link rel="stylesheet" href="<%=basePath %>base/resources/zui/dist/css/zui.css" >
<link rel="stylesheet" href="${ctxBase}/styles/theme_blue.css" >
<link rel="stylesheet" href="${ctxBase}/styles/szCloud_common.css" />
<link rel="stylesheet" href="${ctxBase}/styles/style.css" />
<link rel="stylesheet" href="<%=basePath %>./resources/styles/zTreeStyle/zTreeStyle.css">
<%-- <link rel="stylesheet" href="${ctxBase}/styles/zTreeStyle/szcloud.css"> --%>
<link rel="stylesheet" href="<%=basePath %>template/AdminLTE/css/artDialog/dialog.css" >
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
			<!--  -->
			<div class="CloudMenu"></div>
			
			<div class="settingBtn btn-group center-block">
			   <a class="btn btn-sm btn-primary" href="javascript:void(0);" id="allinfo">个人信息</a>
			   <a class="btn btn-sm btn-success" href="javascript:void(0);" id="allchecker">修改密码</a>
			   <a class="btn btn-sm btn-warning" href="javascript:void(0)" id="logOut">退出</a>
			</div>
		</div>		
	</nav> 
  </header>
  
  
  <div class="C-contentFrame clearfix">
        
            <iframe src="<%=basePath%>dev/intoSystemCenter.do?boxs=110"  name="main" scrolling="auto" frameborder="0" width="100%" height="100%"></iframe> 

  </div>
 
 

<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js"></script>
<script src="${ctxBase}/scripts/jquery.cookies.js"></script>
<script src="<%=basePath %>base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
<script src="<%=basePath %>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js" language="javascript" type="text/javascript"></script>
<script src="${ctxBase}/scripts/jquery.ManageList.ui.js" language="javascript" type="text/javascript"></script>
<script src="<%=basePath %>template/AdminLTE/js/artDialog/dialog-plus.js"></script>

<script language="javascript">
var setting = {
	view: {
		showLine: false,
		showIcon: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback:{
		onClick: zTreeOnClick
	}
};	

function showIconForTree(treeId, treeNode) {
	return !treeNode.isParent;
};
function zTreeOnClick(event, treeId, treeNode) {
    //alert(treeNode.tId + ", " + treeNode.name);
    $(".ztree a").removeClass("curSelectedNode");
    $(".ztree #"+treeNode.tId+"_a").addClass("curSelectedNode");
};


function creatMenu(){
	var dataLi="";
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=basePath%>devAdmin/getZTreeNode.do",
		async:false,//同步
		data:{"userId":1,"groupId":1,"roleId":1},
		success:function(dataMap){
			$.each(dataMap,function(i,item){
				 dataLi ='<div class="ztree_position"><div class="ztree_cover"></div><ul id="tree'+i+'" class="ztree"></ul></div>';//<li><div class="header"><span class="label">'+item[0].name+'</span><span class="arrow down"></span></div><ul id="tree'+i+'" class="ztree"></ul></li>
				  $(dataLi).appendTo(".CloudMenu");
				  $.fn.zTree.init($("#tree"+i),setting,item).expandAll(true);
				  //$("#tree"+i+" > li").eq(0).remove();
			});	
		}
		
	});
}
function newMenu(){
	var menu={

		    "3": [
		        {
		            "id": 5,
		            "pId": 0,
		            "name": "开发者工作室",
		            "url": "",
		            "target": "main",
		            "open": false,
		            "isParent": false,
		            "checked": false,
		            "groupType": null,
		            "iconSkin": null,
		            "click": null
		        }
		    ]

		}
		$.each(menu,function(i,item){
			 dataLi ='<div class="ztree_position"><div class="ztree_cover"></div><ul id="tree'+i+'" class="ztree"></ul></div>';//<li><div class="header"><span class="label">'+item[0].name+'</span><span class="arrow down"></span></div><ul id="tree'+i+'" class="ztree"></ul></li>
			  $(dataLi).appendTo(".CloudMenu");
			  $.fn.zTree.init($("#tree"+i),setting,item).expandAll(true);
			  //$("#tree"+i+" > li").eq(0).remove();
		});	
}
$(document).ready(function(){
	//creatMenu();
	newMenu();
	

	$('#allchecker').bind("click",function() {
		var content = '<div class="form-horizontal">'
				+ '<div class="form-group">'
				+ '<label class="col-md-3 control-label required">原密码</label>'
				+ '<div class="col-md-9"><input id="oldPwd" name="oldPwd" type="password" class="form-control"  placeholder="原密码"></div>'
				+ '</div><div class="form-group">'
				+ '<label class="col-md-3 control-label required">新密码</label>'
				+ '<div class="col-md-9"><input id="newPwd" name="newPwd" type="password" class="form-control"  placeholder="新密码"></div>'
				+ '</div><div class="form-group">'
				+ '<label class="col-md-3 control-label required">重复密码</label>'
				+ '<div class="col-md-9"><input id="newPwd2" name="newPwd2" type="password" class="form-control"  placeholder="重复新密码"></div>'
				+ '</div></div>';
		var d = dialog({
			title : '修改密码',
			content : content,
			okValue : '确定',
			ok : function() {
				var oldPwd = $("#oldPwd").val();
				var newPwd = $("#newPwd").val();
				var newPwd2 = $("#newPwd2").val();
				if (oldPwd == "") {
					alert("原密码必填");
					return false;
				}
				if (newPwd == "") {
					alert("新密码必填");
					return false;
				}
				if (newPwd2 == "") {
					alert("重复密码必填");
					return false;
				}
				if (newPwd == oldPwd) {
					alert("新密码不能和原密码一样");
					return false;
				}
				if (newPwd != newPwd2) {
					alert("新密码和重复密码输入不一致");
					return false;
				}
				if (!window.confirm("确定修改")) {
					return false;
				}
				//修改密码
				$.ajax({
					type : "POST",
					url : "<%=basePath%>unit/updatePwd.do",
					data : "oldPwd=" + oldPwd
							+ "&newPwd=" + newPwd,
					success : function(data) {
						if (data.status == 0) {
							alert(data.message);
							//alertMessage("修改成功");
						} else {
							alert(data.message);
							//alertMessage(data.message);
						}
					}
				})
	
				return true;
	
			},
			cancelValue : '取消',
			cancel : function() {
			}
		});
		d.width(450).showModal();
		return false;
	});
	//修改个人信息
	$("#allinfo").click(function() {
			top.dialog({
			id : 'act-dialog' + Math.ceil(Math.random() * 10000),
			title : '更新个人信息',
			url : "<%=basePath%>unit/getSelfInfo.do",
			height : 500,
			width : 1200,
			onclose : function() {
			}
			}).showModal();
		return false;
	});
});
//退出
$("#logOut").click(function(){
	$.get("<%=basePath%>logout.do",function(){
		location.href="<%=basePath%>login.html";
	});
})  
	</script>	




</body>
</html>