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
<div class="whole" style="margin:20px;">
<!-- 	<ul class="nav nav-tabs"> -->
<!-- 	     <li> -->
<%-- 	       <a href="<%=basePath%>unit/intoPunSystem.do">系统信息编辑</a> --%>
<!-- 	     </li> -->
<!-- 	     <li class="active"> -->
<!-- 	       <a href="javascript:;">系统业务编辑</a> -->
<!-- 	     </li> -->
<!-- 	   </ul> -->
	<div style="margin-top:20px;">
      <ul class="CloudTreeMenu list-group"></ul>    
   </div>
	<div class="C_btns">
		<button class="btn btn-info" type="button" onclick="addNode()">新增业务计划</button>
	</div>
</div>
<%-- <p>${menuJson}</p> --%>
<script language="javascript">
var sysId = ${sysVO.sysId};
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
		   url: "<%=basePath%>business/bizItemAJAXDelete.do",
		   data: "itemId="+treeNode.id,
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
		+ "' title='新增计划' onfocus='this.blur();'></span>"
		+"<span class='button edit' id='"+ treeNode.tId+"_edit"
		+ "' title='修改计划' onfocus='this.blur();'></span>";
	sObj.after(addStr);
	var addBtn = $("#addBtn_"+treeNode.tId);
	var editBtn = $("#"+treeNode.tId+"_edit");
	if(addBtn) addBtn.bind("click",function(){
		top.dialog(
				{ id: 'add-dialog' + Math.ceil(Math.random()*10000),
					title: '新增业务计划',
					url:'<%=basePath%>business/punBizItemEdit.do?parentId='+treeNode.id+'&sysId='+sysId,
					height:500,
					width:1000,
					onclose : function() {
						if (this.returnValue) {
							var result = this.returnValue;
							var name = result.name;
							if(result.year != null)
								name = name + "(" + result.year + ")";
							var zTree = $.fn.zTree.getZTreeObj(treeId);
							zTree.addNodes(treeNode, {id:result.itemId, pId:treeNode.parentId, name:name,url:result.menuAddress});
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
					title: '修改业务计划',
					url:'<%=basePath%>business/punBizItemEdit.do?itemId='+treeNode.id,
					height:500,
					width:1000,
					onclose : function() {
						if (this.returnValue) {
							var result = this.returnValue;
							var name = result.name;
							if(result.year != null)
								name = name + "(" + result.year + ")";
							var zTree = $.fn.zTree.getZTreeObj(treeId);
							treeNode.name = name;
					    	zTree.updateNode(treeNode);
							alertMessage("保存成功");
						}
					}
				}
		).showModal();
	});
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
				title: '新增业务计划',
				url:'<%=basePath%>business/punBizItemEdit.do?sysId='+sysId,
				height:500,
				width:1000,
				onclose : function() {
					if (this.returnValue) {
						var i = $(".list-group-item").length+1;
						var dataLi ='<li class="list-group-item"><ul id="tree'+i+'" class="ztree"></ul></li>';
						var result = this.returnValue;
						$(dataLi).appendTo(".CloudTreeMenu");
				    	var item = {"id":result.itemId,"pId":0,"name":result.name + "(" + result.year + ")","open":false,"isParent":false};
						$.fn.zTree.init($("#tree"+i),setting,item);
				    	alertMessage("添加成功");
					}
				}
			}
	).showModal();
	return false;
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