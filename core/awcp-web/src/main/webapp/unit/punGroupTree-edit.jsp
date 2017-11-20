<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@page isELIgnored="false"%>
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
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="../resources/include/common_css.jsp"%><!-- 注意加载路径 -->
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">

</head>
<body id="main">
	<div class="container-fluid">
	
		<ul class="nav nav-tabs">
			<li><a
				href="<%=basePath%>unit/selfGroupGet.do">组织信息编辑</a>
			</li>
			<li class="active"><a href="javascript:;">组织树编辑</a></li>
			<li><a
				href="<%=basePath%>punPositionController/pageList.do?groupId=${vo.groupId}">职务</a>
			</li>
		</ul>
		<div style="margin-top: 20px;">
			<ul id="groups" class="ztree"></ul>
		</div>

	
		<div class="row navbar-fixed-bottom text-center" id="pagers">
			<sc:PageNavigation dpName="vos"></sc:PageNavigation>
		</div>
	</div>

	<%@ include file="../resources/include/common_js.jsp"%>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/pageTurn.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	<script type="text/javascript">
		  $(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
					  }
				  }
					
			  });
          	//新增
      		$("#addBtn").click(function(){
      			var url = "<%=basePath%>unit/intoPunUserBaseInfo.do";
      			location.href = url;
      			return false;
      		})
      		
      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoGet.do").submit();
      			}else{
      				alert("请选择一个用户进行操作");
      			}
      			return false;
      		});
      		
          	//delete
          	$("#deleteBtn").click(function(){
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
          			$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoDelete.do")
											.submit();
								}
								return false;
							})

		});
	</script>
	<script language="javascript">
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
	//alert(treeNode.id);
	location.href="<%=basePath%>punUserGroupController/getUserList.do?groupId=" + treeNode.id + "&rootGroupId="+groupId;
	//event.target.parentElement.removeAttribute("href");
    //alert(treeNode.tId + ", " + treeNode.name+","+treeNode.url);
    //event.stopPropagation();
};
function zTreeBeforeClick(treeId, treeNode, clickFlag) {
    return true;
};
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
        }else{
        	content +='<option value="2" >部门</option><option value="3" selected="selected">小组</option></select></div>';
        }
        content += '<div class="input-group">'+
				   '<span class="input-group-addon"><i class="icon-pencil"></i>&nbsp;&nbsp;序号</span>'+
                   '<input name="number" type="text" class="form-control" value="'+number+'" placeholder="序号"></div>';
		return content;
	};
	if (addBtn) addBtn.bind("click", function(){
		//alert(treeId+"-"+treeNode.tId)
		var d = dialog({
		    title: '添加菜单节点',
		    content: dialogContent(),
		    okValue: '确定',
		    ok: function () {
		    	var $groupName = $("input[name='groupName']");
		    	var $groupType = $("select[name='groupType']");
		    	var $number = $("input[name='number']");
		    	if($groupName.val().trim()==""){
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
	});
	if (editBtn) editBtn.bind("click", function(){
		var name=treeNode.name;
		var link=treeNode.url;
		var groupType=treeNode.groupType;
		var number=treeNode.number;
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
		    		//alert($groupName.val())
		    		var zTree = $.fn.zTree.getZTreeObj(treeId);
		    		treeNode.name = $groupName.val();
		    		treeNode.groupType=$groupType.val();
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
	});
};

function removeHoverDom(treeId, treeNode) {
	$("#addBtn_"+treeNode.tId).unbind().remove();
	$("#"+treeNode.tId+"_edit").unbind().remove();
};
function creatGroup(){
	var dataLi="";
	var dataMap = ${groupJson};
	/* $.each(dataMap,function(i,item){
		dataLi ='<li class="list-group-item"><ul id="tree'+i+'" class="ztree"></ul></li>';
		$(dataLi).appendTo(".CloudTreeGroup");
	}); */
	$.fn.zTree.init($("#groups"),setting,dataMap);
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
	creatGroup();
});
</script>
</body>
</html>
