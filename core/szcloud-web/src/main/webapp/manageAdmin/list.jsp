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
<link rel="stylesheet" href="<%=basePath %>resources/styles/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath %>base/resources/artDialog/css/ui-dialog.css" >
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
			<div class="CloudMenu"></div>
			<div class="settingBtn btn-group center-block">
			   <a class="btn btn-sm btn-primary" href="javascript:;" id="allinfo">个人信息</a>
			   <a class="btn btn-sm btn-success" href="javascript:;" id="allchecker">修改密码</a>
			   <a class="btn btn-sm btn-warning" href="<%=basePath%>manageAdmin/logout.do">退出</a>
			</div>
		</div>	
	</nav> 
  </header>
  
  
  <div class="C-contentFrame clearfix">
        
            <iframe src="<%=basePath%>manageAdmin/main.jsp"  name="main" scrolling="auto" frameborder="0" width="100%" height="100%"></iframe> 

  </div>
 
 

<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
<script src="${ctxBase}/scripts/jquery.cookies.js"></script>
<script src="<%=basePath %>base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
<script src="${ctxBase}/plugins/zTree_v3/js/jquery.ztree.all-3.5.js" language="javascript" type="text/javascript"></script>
<script src="${ctxBase}/scripts/jquery.ManageList.ui.js" language="javascript" type="text/javascript"></script>
<script src="<%=basePath %>base/resources/artDialog/dist/dialog-plus-min.js"></script>

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
    $(".ztree a").removeClass("curSelectedNode");
    $(".ztree #"+treeNode.tId+"_a").addClass("curSelectedNode");
};
function creatMenu(){
	var dataLi="";
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=basePath%>manageAdmin/getZTreeNode.do",
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

<script type="text/javascript">	
	$(function(){
		
		$('#allchecker').bind("click", function(){
			var content='<div class="form-horizontal">'+
			'<div class="form-group">'+
			'<label class="col-md-3 control-label required">原密码</label>'+
		    '<div class="col-md-9"><input id="oldPwd" name="oldPwd" type="password" class="form-control"  placeholder="原密码"></div>'+
		    '</div><div class="form-group">'+
		    '<label class="col-md-3 control-label required">新密码</label>'+
		    '<div class="col-md-9"><input id="newPwd" name="newPwd" type="password" class="form-control"  placeholder="新密码"></div>'+
		    '</div><div class="form-group">'+
		    '<label class="col-md-3 control-label required">重复密码</label>'+
		    '<div class="col-md-9"><input id="newPwd2" name="newPwd2" type="password" class="form-control"  placeholder="重复新密码"></div>'+
		    '</div></div>';	
		    var message="";
			var d = dialog({
			    title: '修改密码',
			    content: content,
			    okValue: '确定',
			    ok: function () {
			    	var oldPwd = $("#oldPwd").val(),
			    	newPwd = $("#newPwd").val(),
			    	newPwd2 = $("#newPwd2").val(),
			    	reg = /^\s+|\s+$/g;
			    	if(oldPwd == ""){
			    		this.statusbar("<label style='color:#ee4e2f;padding-left:120px;' class='icon-remove-sign'>原密码必填</label>");
			    		return false;
			    	}
			    	if(newPwd == ""){
			    		this.statusbar("<label style='color:#ee4e2f;padding-left:120px;' class='icon-remove-sign'>新密码必填</label>");
			    		return false;
			    	}
			    	if(newPwd2 == ""){
			    		this.statusbar("<label style='color:#ee4e2f;padding-left:120px;' class='icon-remove-sign'>重复密码必填</label>");
			    		return false;
			    	}
			    	if(reg.test(oldPwd)||reg.test(newPwd)){
			    		this.statusbar("<label style='color:#ee4e2f;padding-left:120px;' class='icon-remove-sign'>密码不能以空格开始和结束</label>");
			    		return false;
			    	}
			    	if(newPwd == oldPwd){
			    		this.statusbar("<label style='color:#ee4e2f;padding-left:120px;' class='icon-remove-sign'>新密码不能和原密码一样</label>");
			    		return false;
			    	}
			    	if(newPwd!=newPwd2){
			    		this.statusbar("<label style='color:#ee4e2f;padding-left:120px;' class='icon-remove-sign'>新密码和重复密码输入不一致</label>");
			    		return false;
			    	}
					//修改密码
					$.ajax({
					   type: "POST",
					   url:"<%=basePath%>manage/updatePwd.do",
					   data: "oldPwd="+oldPwd+"&newPwd="+newPwd,
					   async:false,
					   success: function(data){
						  var msg = new $.Messager(data.message, {placement: 'top'});
						  msg.show();
						    if(data.status==0){
						    	result=true;
						    }else{
						    	result=false;
						    }
					   }
					});
					return result;
			        
			    },
			    cancelValue: '取消',
			    cancel: function () {}
			});
			d.width(450).showModal();
			return false;
		});
		$("#allinfo").click(function(){	
			top.dialog({ 	
				id: 'act-dialog' + Math.ceil(Math.random()*10000),
				title: '更新个人信息',
				url:"<%=basePath%>manage/getSelfInfo.do",
				height:500,
				width:1200,
				onclose: function () {
				}
			}).showModal();
			return false;
		});
	})	
	</script>	


</body>
</html>