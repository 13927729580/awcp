function sendCloudAjax(url,data){
	var result="";
	$.ajax({
		type:"post",
		dataType:"json",
		url:url,
		async:false,//同步
		data:data,
		contentType:"application/json;charset=UTF-8",
		success:function(dataMap){
			result = dataMap;
		},
		fail:function(e){
			alert(e);
			result = false;
		}

	});
	return result;
}

var setting = {
  view: {
        showLine: false,
        showIcon: false
    },
	data: {
		simpleData: {
			enable: true
		}
	},
  callback:{
    onClick:showonClick,
    beforeDblClick: zTreeBeforeDblClick
  }
};
function zTreeBeforeDblClick(){
    return false;
};
function showonClick(event, treeId, treeNode){
  if(treeNode.url==''){
    var atarget=treeNode.target;
    if(treeNode.isParent){
      jQuery("#"+atarget).attr("src","about:blank");
    }else{
      jQuery("#"+atarget).attr("src",treeNode.remindUrl);
    }
  }
}
var keyword = $("#keyword").val();
var url = $("#url").val();

var zNodes =  sendCloudAjax("treeView/getTreeViewListJson.do",{"keyword":keyword,"url":url});
$(document).ready(function(){
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	//默认点击当前登录人所在部门的节点
  var node = treeObj.getNodeByParam("id", $("#groupId").val(), null);
  jQuery("#"+node.tId).find("a").get(0).click();
});