<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="renderer" content="webkit">
<title>系统菜单编辑</title>
<link href="base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
<link href="base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/zui.min.css">
<link rel="stylesheet" type="text/css" href="./resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
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
<div class="whole" style="margin:20px;">
<!-- 	<ul class="nav nav-tabs"> -->
<!-- 	     <li> -->
<%-- 	       <a href="<%=basePath%>unit/intoPunSystem.do">系统信息编辑</a> --%>
<!-- 	     </li> -->
<!-- 	     <li class="active"> -->
<!-- 	       <a href="javascript:;">系统菜单编辑</a> -->
<!-- 	     </li> -->
<!-- 	   </ul> -->
	<div style="margin-top:20px;">
      <ul class="CloudTreeMenu list-group"></ul>    
   </div>
	<div class="C_btns">
		<button class="btn btn-info" type="button" onclick="addNode()">新增根节点</button>
	</div>
</div>
<%-- <p>${menuJson}</p> --%>
<script language="javascript">
var setting = {
	view: {
		showLine: true,
		showIcon: showIconForTree,
		addHoverDom: addHoverDom,
		removeHoverDom: removeHoverDom,
		selectedMulti: false
	},
	edit: {
		drag:{
			isCopy:false,
			isMove:false
		},
		enable: true,
		editNameSelectAll: true,
		showRemoveBtn: showRemoveBtn,
		showRenameBtn: false
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onRemove: zTreeOnRemove,
		beforeClick: zTreeBeforeClick
	}
};	

function zTreeOnRemove(event,treeId,treeNode){
	$.ajax({
		   type: "POST",
		   url: "<%=basePath%>dev/punMenuAJAXDelete.do",
		   data: "menuId="+treeNode.id,
		   success: function(data){
			    if(data.status==0){
			    	var zTree = $.fn.zTree.getZTreeObj(treeId);
			    	zTree.updateNode(treeNode);
			    	if(!treeNode.pId){
			    		$("#"+treeId).parent().remove();
			    	}
			    	alertMessage("删除成功");
			    }else{
			    	alertMessage(data.message);
			    }
		   }
		})	
}
function showIconForTree(treeId, treeNode) {
	return !treeNode.isParent;
};
function showRemoveBtn(treeId, treeNode) {
	return !treeNode.isParent;
}
function zTreeOnClick(event, treeId, treeNode,clickFlag) {
	//event.target.parentElement.removeAttribute("href");
    //alert(treeNode.tId + ", " + treeNode.name+","+treeNode.url);
    //event.stopPropagation();
};
function zTreeBeforeClick(treeId, treeNode, clickFlag) {
    return false;
};
var newCount = 1;
function addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
		+ "' title='新增菜单' onfocus='this.blur();'></span>"
		+"<span class='button edit' id='"+ treeNode.tId+"_edit"
		+ "' title='修改菜单' onfocus='this.blur();'></span>";
	sObj.after(addStr);
	var addBtn = $("#addBtn_"+treeNode.tId);
	var editBtn = $("#"+treeNode.tId+"_edit");
	if(addBtn) addBtn.bind("click",function(){
		top.dialog(
				{ id: 'add-dialog' + Math.ceil(Math.random()*10000),
					title: '新增菜单',
					url:'<%=basePath%>dev/menuEditDialog.do?parentMenuId='+treeNode.id,
					height:300,
					width:1000,
					onclose : function() {
						if (this.returnValue) {
							var result = this.returnValue;
							var zTree = $.fn.zTree.getZTreeObj(treeId);
							zTree.addNodes(treeNode, {id:result.menuId, pId:treeNode.id, name:result.menuName,url:result.menuAddress});
					    	alertMessage("添加成功");
						}
					}
				}
		).showModal();
		return false;
	});
	if (editBtn) editBtn.bind("click", function(){
		top.dialog(
				{ id: 'edit-dialog' + Math.ceil(Math.random()*10000),
					title: '修改菜单',
					url:'<%=basePath%>dev/punMenuGet.do?boxs='+treeNode.id,
					height:300,
					width:1000,
					onclose : function() {
						if (this.returnValue) {
							var result = this.returnValue;
							var zTree = $.fn.zTree.getZTreeObj(treeId);
							//zTree.addNodes(treeNode, {id:result.menuId, pId:treeNode.id, name:result.menuName,url:result.menuAddress});
							treeNode.name = this.returnValue.menuName;
							treeNode.url = this.returnValue.menuAddress;
					    	zTree.updateNode(treeNode);
							alertMessage("保存成功");
						}
					}
				}
		).showModal();
	});
/* 	var dialogContent = function(){
		var name,link;
		if(arguments.length==0){
		    name="";link="";
		}else if(arguments.length==2){
		    name=arguments[0];link=arguments[1];
		}
		var content='<div class="input-group">'+
		'<span class="input-group-addon"><i class="icon-pencil"></i>&nbsp;&nbsp;名称</span>'+
        '<input name="menuName" type="text" class="form-control" value="'+name+'" placeholder="名称"></div>'+
        '<div class="input-group"><span class="input-group-addon"><i class="icon-link"></i>&nbsp;&nbsp;链接</span>'+
        '<input name="menuLink" type="text" class="form-control" value="'+link+'" placeholder="链接"></div>';
		return content;
	}; */
	<%-- if (addBtn) addBtn.bind("click", function(){
		//alert(treeId+"-"+treeNode.tId)
		var d = dialog({
		    title: '添加菜单节点',
		    content: dialogContent(),
		    okValue: '确定',
		    ok: function () {
		    	var $menuName = $("input[name='menuName']");
		    	var $menuLink = $("input[name='menuLink']");
		    	if($menuName.val()==""){
		    		$menuName.parent().addClass("has-error");
		    		return false;
		    	}else{
		    		var zTree = $.fn.zTree.getZTreeObj(treeId);
					//添加菜单
					$.ajax({
					   type: "POST",
					   url: "<%=basePath%>unit/punMenuAJAXSave.do",
					   data: "parentMenuId="+treeNode.id+"&menuSeq="+(treeNode.children?treeNode.children.length:0)+"&menuName="+$menuName.val()+"&menuAddress="+$menuLink.val(),
					   success: function(data){
						    if(data.status==0){
						    	zTree.addNodes(treeNode, {id:data.data.menuId, pId:treeNode.id, name:$menuName.val(),url:$menuLink.val()});
						    	alertMessage("添加成功");
						    }else{
						    	alertMessage(data.message);
						    }
					   }
					})			
					
			        return true;
		    	}
		        
		    },
		    cancelValue: '取消',
		    cancel: function () {}
		});
		d.width(320).show();
		return false;
	}); --%>
<%-- 	if (editBtn) editBtn.bind("click", function(){
		var name=treeNode.name;
		var link=treeNode.url;
		var d = dialog({
		    title: '修改菜单节点',
		    content: dialogContent(name,link),
		    okValue: '确定',
		    ok: function () {
		    	var $menuName = $("input[name='menuName']");
		    	var $menuLink = $("input[name='menuLink']");
		    	if($menuName.val()==""){
		    		$menuName.parent().addClass("has-error");
		    		return false;
		    	}else{
		    		//alert($menuName.val())
		    		var zTree = $.fn.zTree.getZTreeObj(treeId);
		    		treeNode.name = $menuName.val();
		    		treeNode.url = $menuLink.val();
					//修改菜单,如果是根节点不传parentMenuId值
					var data;
					if(!treeNode.pId){
						data = "menuId="+treeNode.id+"&menuName="+$menuName.val()+"&menuAddress="+$menuLink.val();
					}else{
						data = "menuId="+treeNode.id+"&parentMenuId="+treeNode.pId+"&menuName="+$menuName.val()+"&menuAddress="+$menuLink.val();
					}
					$.ajax({
					   type: "POST",
					   url: "<%=basePath%>unit/punMenuAJAXSave.do",
					   data: data,
					   success: function(data){
						    if(data.status==0){
					    		zTree.updateNode(treeNode);
					    		alertMessage("修改成功");
						    }else{
						    	alertMessage(data.message);
						    }
					   }
					})		
			        return true;
		    	}
		        
		    },
		    cancelValue: '取消',
		    cancel: function () {}
		});
		d.width(320).show();
		return false;
	}); --%>
};

function removeHoverDom(treeId, treeNode) {
	$("#addBtn_"+treeNode.tId).unbind().remove();
	$("#"+treeNode.tId+"_edit").unbind().remove();
};
function creatMenu(){
	var dataLi="";
	var dataMap = ${menuJson};
	$.each(dataMap,function(i,item){
		//alert(123)
		dataLi ='<li class="list-group-item"><ul id="tree'+i+'" class="ztree"></ul></li>';
		$(dataLi).appendTo(".CloudTreeMenu");
		$.fn.zTree.init($("#tree"+i),setting,item);
		  //$("#tree"+i+" > li").eq(0).remove();
	});
}
function addNode(){
	
	top.dialog(
			{ id: 'add-root-dialog' + Math.ceil(Math.random()*10000),
				title: '新增根节点',
				url:'<%=basePath%>dev/menuEditDialog.do?parentMenuId='+0,
				height:300,
				width:1000,
				onclose : function() {
					if (this.returnValue) {
						var i = $(".list-group-item").length+1;
						var dataLi ='<li class="list-group-item"><ul id="tree'+i+'" class="ztree"></ul></li>';
						var result = this.returnValue;
						$(dataLi).appendTo(".CloudTreeMenu");
				    	var item = {"id":result.menuId,"pId":0,"name":result.menuName,"url":result.menuAddress,"target":"main","open":false,"isParent":false};
						$.fn.zTree.init($("#tree"+i),setting,item);
				    	alertMessage("添加成功");
					}
				}
			}
	).showModal();
	return false;
	<%-- var content='<div class="input-group">'+
	'<span class="input-group-addon"><i class="icon-pencil"></i>&nbsp;&nbsp;名称</span>'+
    '<input name="menuName" type="text" class="form-control" value="" placeholder="名称"></div>'+
    '<div class="input-group"><span class="input-group-addon"><i class="icon-link"></i>&nbsp;&nbsp;链接</span>'+
    '<input name="menuLink" type="text" class="form-control" value="" placeholder="链接"></div>';
	var d = dialog({
	    title: '新增菜单节点',
	    content: content,
	    okValue: '确定',
	    ok: function () {
	    	var $menuName = $("input[name='menuName']");
	    	var $menuLink = $("input[name='menuLink']");
	    	if($menuName.val()==""){
	    		$menuName.parent().addClass("has-error");
	    		return false;
	    	}else{
				//新增菜单
				//var data = "menuId="+treeNode.id+"&parentMenuId="+treeNode.pId+"&menuName="+$menuName.val()+"&menuAddress="+$menuLink.val();
				var i = $(".list-group-item").length+1;
				var dataLi ='<li class="list-group-item"><ul id="tree'+i+'" class="ztree"></ul></li>';
				$.ajax({
					   type: "POST",
					   url: "<%=basePath%>unit/punMenuAJAXSave.do",
					   data: "parentMenuId=0&menuSeq="+i+"&menuName="+$menuName.val()+"&menuAddress="+$menuLink.val(),
					   success: function(data){
						    if(data.status==0){
						    	$(dataLi).appendTo(".CloudTreeMenu");
						    	var item = {"id":data.data.menuId,"pId":0,"name":$menuName.val(),"url":$menuLink.val(),"target":"main","open":false,"isParent":false};
								$.fn.zTree.init($("#tree"+i),setting,item);
						    	alertMessage("添加成功");
						    }else{
						    	alertMessage(data.message);
						    }
					   }
					})	
		        return true;
	    	}
	        
	    },
	    cancelValue: '取消',
	    cancel: function () {}
	});
	d.width(320).show(); --%>
// 	return false;
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