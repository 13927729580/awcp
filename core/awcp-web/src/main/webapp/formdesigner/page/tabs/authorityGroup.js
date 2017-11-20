$(function(){
	var dynamicPageId=$("#dynamicPageId").val();
	loadPowerTable(dynamicPageId);

});



function loadPowerTable(dynamicPageId) {
	
	var url = basePath + "authority/getGroupListByPageId.do";
	var objectType = "powerTable_body";
	$.ajax({
		type : "GET",
		url : url,
		data : "dynamicPageId=" + dynamicPageId + "&pageSize=9999",
		async : false,
		success : function(data) {
			$("#" + objectType).empty();
			$.each(data, function(idx, item) {
				var lastTime=item.lastupdateTime;
				var time=item.createTime;
				var lastupdateTime=new Date(lastTime);
				var createTime = new Date(time);
				$("#" + objectType).append(""+
				"<tr>"+
				"<td width='20'><input type='checkbox' name='authorityGroup' value='"+item.id+"'/></td>"+
				"<td><a href='javascript:void(0);' onclick='editAuthority(\"update\""+",\"" + item.id+"\")'>"+item.name+"</a></td>"+
				"<td>"+item.bakInfo+"</td>"+
				"<td>"+createTime.Format("yyyy-MM-dd hh:mm")+"</td>"+
				"<td>"+lastupdateTime.Format("yyyy-MM-dd hh:mm")+"</td>"+
				"<td>"+item.description+"</td>"+
				"<td width='50'>"+item.order+"</td>"+
				"</tr>"
				+"");
				
			});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}

$("#addAuthorityGroup").click(function(){

		var dynamicPageId = $("#dynamicPageId").val();
		editAuthority("add",dynamicPageId);
		return false;

});


	
function delAuthority(type){
	
		var dynamicPageId = $("#dynamicPageId").val();
		editAuthority("remove",dynamicPageId);
		return false;
}
	

function editAuthority(action,dynamicPageId){

			if(empty(dynamicPageId)){
				return false;
			}
			if(empty(action)){
				return false;
			}
			if(action=="add"){
				addAuthorityGroup(action,dynamicPageId);
			}else if(action=="update"){
				updateAuthorityGroup(action,dynamicPageId);
			}else if(action=="remove"){
				removeAuthority(dynamicPageId);
			}else if(action="addAuthority"){
				var pageId=$("#dynamicPageId").val();
				addAuthorityValue(dynamicPageId,pageId);
			}
	}
	
	
function updateAuthorityGroup(action,id){
		
		var urlStr="formdesigner/page/tabs/updateAuthorith.jsp";
		var dynamicPageId=$("#dynamicPageId").val();
		
		var postData={};
		
		postData.id=id;
		
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '载入中...',
			url: urlStr,
			data:postData,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					alert("保存成功");
					loadPowerTable(dynamicPageId);
				
				}
			}
			
		}).showModal();
		return false;
	}
	
	
function addAuthorityGroup(action,dynamicPageId){
		
		var urlStr="formdesigner/page/tabs/addAuthorith.jsp";
			
		var postData={};
		
		if(action=='add'){
			postData.dynamicPageId=dynamicPageId;
		}else if(action=='update'){
			postData.dynamicPageId=dynamicPageId;
		}
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '载入中...',
			url: urlStr,
			data:postData,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					alert("保存成功");
					loadPowerTable(dynamicPageId);
				
				}
			}
			
		}).showModal();
		return false;
	}
	
	
	$("#deleteAuthority").click(function(){
		var count = $(":checkbox[name='authorityGroup']:checked").size();
	
		if(count > 0){
			var dynamicPageId = $("#dynamicPageId").val();
		
			editAuthority("remove",dynamicPageId);
			return false;
		}else{
			alert("请选择权限组操作");
			return false;
		}
	});
	
	
function removeAuthority(dynamicPageId){
		var _selects=new Array();
		$(":checkbox[name='authorityGroup']:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		
		$.ajax({
			url:"authority/deleteByAjax.do",
			type:"POST",
			async:false,
			data:{_selects:_selects.join(",")},
			success:function(ret){
				if("1"==ret){
					//fresh();
					$(":checkbox[name='checkAllAuthority']").prop("checked",false);
					loadPowerTable(dynamicPageId);
					alert("删除成功！");
				}else{
					alert("删除失败！");
				}
				
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) { 
	              alert(errorThrown); 
		    }
		});
		return false;
	}
	
	
//权限配置
$("#addAuthorityValue").click(function(){
		var count = $(":checkbox[name='authorityGroup']:checked").size();
	
		if(count==1){
		  
			var componentId=$(":checkbox[name='authorityGroup']:checked").val();
			editAuthority("addAuthority",componentId);
			return false;
		}else if(count>1){
			alert("请选择一组权限组操作");
			return false;
		}else{
			alert("请选择权限组操作");
			return false;
		}
	});
	
	
function addAuthorityValue(dynamicPageId,pageId){
		
		var urlStr="formdesigner/page/tabs/addAuthorityValue.jsp";
		
		var postData={};
		
		postData.dynamicPageId=pageId;
		postData.componentId=dynamicPageId;
		
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '载入中...',
			url: urlStr,
			height:500,
			data:postData,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					alert("保存成功");
					loadPowerTable(dynamicPageId);
				
				}
			}
			
		}).showModal();
		return false;
	}	
	
//格式化日期
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
