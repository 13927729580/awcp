<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@ page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="renderer" content="webkit">
	<title>组信息编辑</title>
	<%@ include file="/resources/include/common_lte_css.jsp"%>
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
</head>
<body>
	<section class="content">
		<ul class="nav nav-tabs">
			<li><a href="<%=basePath%>unit/selfGroupGet.do">组织信息编辑</a></li>
			<li class="active"><a href="javascript:;">组织树编辑</a></li>
			<li><a href="<%=basePath%>punPositionController/pageList.do?groupId=${vo.groupId}">职务</a></li>
		</ul>
		<div style="margin-top: 20px;"></div>
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<ul id="groups" class="ztree"></ul>
					</div>
				</div>
			</div>
		</div>
	</section>
	<script src="<%=basePath%>template/AdminLTE/js/jquery.min.js"></script>
	<script src="<%=basePath%>resources/scripts/en_Us_error_message.js"></script>
	<script src="<%=basePath%>template/AdminLTE/js/artDialog/dialog-plus.js"></script>
	<script src="<%=basePath%>venson/js/common.js"></script>	
	<script src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	<script>
		var groupId = ${vo.groupId};
		var setting = {
			view: {
				showLine: true,
				showIcon: false,//showIconForTree
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
				beforeClick: zTreeBeforeClick,
				onClick:zTreeOnClick
			}
		};	
		
		function zTreeOnRemove(event,treeId,treeNode){
			$.ajax({
				type: "POST",
				url: "<%=basePath%>unit/punGroupAJAXDelete.do",
				data: "groupId="+treeNode.id,
				success: function(data){
					if(data.status==0){
					    var zTree = $.fn.zTree.getZTreeObj(treeId);
					    zTree.updateNode(treeNode);
					    if(!treeNode.pId){
					    	$("#"+treeId).parent().remove();
					    }
					    Comm.alert("删除成功");
					}else{
					    Comm.alert(data.message);
					}
				}
			});
		}
		
		function showIconForTree(treeId, treeNode) {
			return !treeNode.isParent;
		}
		
		function showRemoveBtn(treeId, treeNode) {
			return !treeNode.isParent;
		}
		
		function zTreeOnClick(event, treeId, treeNode,clickFlag) {
			//location.href="<%=basePath%>punUserGroupController/getUserList.do?groupId=" + treeNode.id + "&rootGroupId="+groupId;
		}
		
		function zTreeBeforeClick(treeId, treeNode, clickFlag) {
		    return true;
		}
		
		var newCount = 1;
		function addHoverDom(treeId, treeNode) {
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='新增组织菜单' onfocus='this.blur();'></span>"
				+"<span class='button edit' id='"+ treeNode.tId+"_edit"
				+ "' title='修改组织菜单' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var addBtn = $("#addBtn_"+treeNode.tId);
			var editBtn = $("#"+treeNode.tId+"_edit");
			var dialogContent = function(){
				var name,groupType,number;
				if(arguments.length==0){
				    name="";groupType="";number="";
				}else if(arguments.length==3){
				    name=arguments[0];groupType=arguments[1];number=arguments[2];
				}
				var content='<div class="input-group">'+
				'<span class="input-group-addon"><i class="icon-pencil"></i>&nbsp;&nbsp;名称</span>'+
		        '<input name="groupName" type="text" class="form-control" value="'+name+'" placeholder="名称"></div>'+
		        '<div class="input-group">'+
				'<span class="input-group-addon"><i class="icon-pencil"></i>&nbsp;&nbsp;类型</span>'+
		        '<select name="groupType" class="form-control">';
		        if(groupType=="2"){
		        	content +='<option value="2" selected="selected">部门</option><option value="3">小组</option></select></div>';
		        } else if(groupType=="3"){
		        	content +='<option value="2">部门</option><option value="3" selected="selected">小组</option></select></div>';
		        } else{
		        	content +='<option value="3">小组</option><option value="2">部门</option></select></div>';
		        }
		        content += '<div class="input-group">'+
						   '<span class="input-group-addon"><i class="icon-pencil"></i>&nbsp;&nbsp;序号</span>'+
		                   '<input name="number" type="number" class="form-control" value="'+number+'" placeholder="序号"></div>';
				return content;
			};
			if (addBtn) addBtn.bind("click", function(){
				var d = dialog({
				    title: '添加菜单节点',
				    content: dialogContent(),
				    okValue: '确定',
				    ok: function () {
				    	var $groupName = $("input[name='groupName']");
				    	var $groupType = $("select[name='groupType']");
				    	var $number = $("input[name='number']");
				    	if(!$groupName.val()){
				    		$groupName.parent().addClass("has-error");
				    		return false;
				    	}else{
				    		var zTree = $.fn.zTree.getZTreeObj(treeId);
							//添加菜单
							$.ajax({
							   type: "POST",
							   url: "<%=basePath%>unit/punGroupAJAXSave.do",
							   data: "parentGroupId="+treeNode.id+"&groupSeq="+(treeNode.children?treeNode.children.length:0)+"&groupChName="+$groupName.val()+"&groupType="+$groupType.val()+"&orgCode=45575544-2&number="+$number.val(),
							   success: function(data){
								    if(data.status==0){
								    	zTree.addNodes(treeNode, {id:data.data.groupId, pId:treeNode.id, name:$groupName.val(), number:$number.val()});
								    	Comm.alert("添加成功");
								    }else{
								    	Comm.alert(data.message);
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
			});
			if (editBtn) editBtn.bind("click", function(){
				var name=treeNode.name;
				var link=treeNode.url;
				var groupType=treeNode.groupType;
				var number=treeNode.number;
				if(!number){
					number = "";
				}
				var d = dialog({
				    title: '修改菜单节点',
				    content: dialogContent(name,groupType,number),
				    okValue: '确定',
				    ok: function () {
				    	var $groupName = $("input[name='groupName']");
				    	var $number = $("input[name='number']");
				    	var $groupType = $("select[name='groupType']");
				    	if($groupName.val()==""){
				    		$groupName.parent().addClass("has-error");
				    		return false;
				    	}else{
				    		var zTree = $.fn.zTree.getZTreeObj(treeId);
				    		treeNode.name = $groupName.val();
				    		treeNode.groupType=$groupType.val();
				    		treeNode.number=$number.val();
							//修改菜单,如果是根节点不传parentGroupId值
							var data;
							if(!treeNode.pId){
								data = "groupId="+treeNode.id+"&groupChName="+$groupName.val()+"&groupType="+$groupType.val()+"&orgCode=45575544-2&number="+$number.val();
							}else{
								data = "groupId="+treeNode.id+"&parentGroupId="+treeNode.pId+"&groupChName="+$groupName.val()+"&groupType="+$groupType.val()+"&orgCode=45575544-2&number="+$number.val();
							}
							$.ajax({
							   type: "POST",
							   url: "<%=basePath%>unit/punGroupAJAXSave.do",
							   data: data,
							   success: function(data){
								    if(data.status==0){
							    		zTree.updateNode(treeNode);
							    		Comm.alert("修改成功");
								    }else{
								    	Comm.alert(data.message);
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
			});
		};
		
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
			$("#"+treeNode.tId+"_edit").unbind().remove();
		}
		
		function creatGroup(){
			var dataLi="";
			var dataMap = ${groupJson};
			$.fn.zTree.init($("#groups"),setting,dataMap);
		}
		
		$(document).ready(function(){
			creatGroup();
		});
</script>
</body>
</html>
