var item ={};//选中 人员
var membersID =[];//选中ID
var item_role ={};//选中 人员
var membersID_role =[];//选中ID
//机构编号 深圳市人民医院
var groupId =($("#groupID").val()==null?529614:$("#groupID").val());
$(document).ready(function(e) {
    $("#post").height($(window).height()-176);
	$("#post").css("overflow","auto");
	
	$("#postdepartment").height($(window).height()-386);
	$("#postdepartment").css("overflow","auto");
	
	$("#userlistPost").height($(window).height()-176);
	$("#userlistPost").css("overflow","auto");
	
	$("#role").height($(window).height()-176);
	$("#role").css("overflow","auto");
	
	$("#roledepartment").height($(window).height()-386);
	$("#roledepartment").css("overflow","auto");
	
	$("#userlistRole").height($(window).height()-176);
	$("#userlistRole").css("overflow","auto");
	
	$("#function").height($(window).height()-176);
	$("#function").css("overflow","auto");
	
	$("#functionlistRole").height($(window).height()-176);
	$("#functionlistRole").css("overflow","auto");
	
	$("#tab8").height($(window).height()-176);
	$("#tab8").css("overflow","auto");
	
	$("#tab9").height($(window).height()-176);
	$("#tab9").css("overflow","auto");
	
	initialPost();
	initialRole();
});

//*****************************
//岗位关联人员提示信息关闭
//*****************************

function closePostTip(){
	$("#postTip").hide();	
}

//*****************************
//角色关联人员提示信息关闭
//*****************************

function closeRoleTip(){
	$("#roleTip").hide();	
}


function closeFunctionTip(){
	$("#functionTip").hide();	
}
//*****************************
//获取数据
//*****************************

function getMyJson(url,data){
	var result = "";
    $.ajax({
    type:"post",
    dataType:"json",
    url:url,
    async:false,//同步
    data:data,
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

//获取岗位树数据
function getPOSJson(){
  var treeNodes=[];
  var url = "../punPositionController/pageListByAjax.do?groupId="+groupId;
  $.ajaxSetup({ 
    async:false
  }); 
   $.get(url,function(data){
     $.each(data,function(i,item){
      var nodes={
          id:item.positionId,pId:item.positionId,name:item.name,click:"clickPost('"+item.positionId+"','"+item.name+"')"
        }
      treeNodes.push(nodes);
     });
   });
  return treeNodes;
}

//获取部门树数据
function getDeptJson(){
  var treeNodes=[];
  var url = "../unit/getGroupTreeByAjax.do?boxs="+groupId;

  $.ajaxSetup({ 
    async : false
  }); 
   $.get(url,function(data){
     $.each(data,function(i,item){
       treeNodes = item;
     });
   });
  return treeNodes;
}

//获取角色树数据
function getRoleJson(){
  var treeNodes=[];
  var url = "../unit/listRolesInSysByAjax.do?boxs="+groupId;
  $.ajaxSetup({ 
    async : false
  }); 
   $.get(url,function(data){
     $.each(data,function(i,item){
       var nodes={
          id:item.roleId,pId:item.roleId,name:item.roleName
        }
      treeNodes.push(nodes);
       
     });
   });
  return treeNodes;
}




//*****************************
//树配置
//*****************************

var setting = {
	view: {
		showLine: false,
		showIcon: false//showIconForTree
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};
 var setting_dept = {
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
        onClick:selectUserByDeptId,
        beforeDblClick: zTreeBeforeDblClick
    }
};

function zTreeBeforeDblClick(){
    return false;
};

//*****************************
//岗位关联人员，岗位树初始化
//*****************************

//var PostNodes = getMyJson("./punPositionController/pageListByAjax.do?groupId="+groupId);
var PostNodes = getPOSJson();
$.fn.zTree.init($("#tree1"), setting, PostNodes);

//*****************************
//岗位关联人员，组织树初始化
//*****************************

//var PersonNodes = getMyJson("./unit/getGroupTreeByAjax.do?boxs="+groupId);
var PersonNodes = getDeptJson();
$.fn.zTree.init($("#tree2"), setting_dept, PersonNodes);


//*****************************
//岗位关联人员，点击人员添加到面板
//*****************************

function personToPanel(id,name,deptId){
	if($("#postname").text().replace(/[\r\n\t\x]/g,"")==""){
		dialogAlert('Please select post then add user!');
		return false;
	}
	var userTag = false;
	$("#userlistPost").find("span").each(function(index, element) {			
		if($(this).attr("data-id") == id){
			userTag = true;
		}
	});
	if(!userTag){
		$("#userlistPost").append("<li class='btn btn-mini' type='button'><i class='icon-user text-gray'></i> <span class=username deptId="+deptId+"  data-id="+id+" data-name="+name+">"+name+"</span> <a class='choice-close' deptId="+deptId+" data-id="+id+">×</a></li>");
		($("#postdeptId").val()!="")?$("#postdeptId").val($("#postdeptId").val()+','+deptId):$("#postdeptId").val(deptId);
		($("#postMemberID").val()!="")?$("#postMemberID").val($("#postMemberID").val()+','+id):$("#postMemberID").val(id);
		($("#postMemberName").val()!="")?$("#postMemberName").val($("#postMemberName").val()+','+name):$("#postMemberName").val(name);						
	}
}


//*****************************
//岗位人员刷新数据
//*****************************
function refreshPostSelect(){
    $(".name-list i.icon-check").each(function(){
        var _this = $(this);
        item[_this.data("id")]=_this.parent().text();
        if(!contains(membersID,_this.data("id"))){
            membersID.push(_this.data("id"));    
        }else return;
        
    });
    $(".name-list i.icon-check-empty").each(function(){
        var _this = $(this);
        delete item[_this.data("id")];
        var index = membersID.indexOf(_this.data("id"));
        if(index>=0) membersID.splice(index,1);
        else return;
    });
    $(".member-panel .panel-heading").attr("membersID",membersID.join());
    if(membersID.length==0){
    	$(".member-panel .panel-body").html('<div class="tip">Select the user from the bottom of the box</div>');
    }else{
	    var memberList = '<ul class="choice-list">';
	    $.each(item,function(i,n){        
	        memberList += '<li><span>'+n+'</span><a class="choice-close" data-id="'+i+'">×</a></li>';
	    });
	    memberList +='</ul>';
	    $(".member-panel .panel-body").html(memberList);
    }
};
//*****************************
//点击岗位，初始化人员
//*****************************

function clickPost(postID,postName){
	$("#postname").html(postName);
	$("#postID").val(postID+','+postName);
	$("#postMemberID").val("");
	$("#postdeptId").val("");
	$("#postMemberName").val("");
	$("#userlistPost").html("");
	$(".postnamelist").html("");
	//查询岗位人员，初始化面板
	var PostPersons = getMyJson("../punUserGroupController/getUserListByWhere.do",{"job":postID});
	$.each(PostPersons,function(i,item){
		personToPanel(item.userId,item.name,item.deptId);
	});
}

//*****************************
//搜索岗位人员
//*****************************
function searchPostMember(){
	if($("#searchPostName").val()==""){
		alertMessage('Please input the post name.');	
		return false;
	}
  var PostPersons = getMyJson("../punAccessRelationController/getUserListByParas.do",{"name":$("#searchPostName").val(),"groupId":groupId});
	$("#userPostSearchList").html("");
	var list='<ul class="postnamelist clearfix">';
	$.each(PostPersons,function(index,item){
		var checkClass = contains($("#postMemberID").val(),item.userId)?"icon-check-sign":"icon-check-empty";			
        list += '<li title="'+item.name+'"><i deptId="'+item.deptId+'" data-id="'+item.userId+'" data-name="'+item.name+'"  class="'+checkClass+'"></i>'+item.name+'</li>';
	});
	list += '</ul>';
	$("#userPostSearchList").append(list);
	//initialPost();
}
//点击组织树节点展示部门下的人员
function selectUserByDeptId(event, treeId, treeNode){
  var itemID = (arguments.length==0)?999999:treeNode.id;
  var deptPersons= getMyJson("../punAccessRelationController/getUserListByDeptId.do",{"deptId":itemID,"groupId":groupId});
  $("#userPostSearchList").html("");
  if(deptPersons!=null&&deptPersons.length>0){
  var list='<ul class="postnamelist clearfix">';
  $.each(deptPersons,function(index,item){
        var checkClass = contains($("#postMemberID").val(),item.userId)?"icon-check-sign":"icon-check-empty";      
        list += '<li title="'+item.name+'"><i deptId="'+itemID+'" deptId="'+item.deptId+'" data-id="'+item.userId+'" data-name="'+item.name+'" class="'+checkClass+'"></i>'+item.name+'</li>';        
  });
  list += '</ul>';
  $("#userPostSearchList").append(list);
  //initialPost();
  }else{
     $("#userPostSearchList").html("<div class=tip>The current department has no user.</div>");
  }
}
//*****************************
//岗位与人员关联面板初始化
//*****************************
function initialPost(){
	$("button[hosPost=checkAll]").click(function(){
		if($("#postname").text().replace(/[\r\n\t\x]/g,"")==""){
			dialogAlert('Please select post then add user!');	
			return false;	
		}
        var _this = $(this);
        if(!_this.hasClass("clicked")){
            var $panel = _this.parents(".panel");
            $panel.find("i.icon-check-empty").addClass("icon-check-sign").removeClass("icon-check-empty");
            _this.addClass("clicked");
            refreshSelectPostMember();
        }else return false;        
    });
	$("button[hosPost=unCheckAll]").click(function(){
		if($("#postname").text().replace(/[\r\n\t\x]/g,"")==""){
			dialogAlert('Please select post then add user!');	
			return false;	
		}
        $("button[hosPost=checkAll]").removeClass("clicked");
        var $panel = $(this).parents(".panel");
        $panel.find("i.icon-check-sign").addClass("icon-check-empty").removeClass("icon-check-sign");
        refreshSelectPostMember();
    });
	   $(".select-panel").on("click",".postnamelist li",function(){
		    if($("#postname").text().replace(/[\r\n\t\x]/g,"")==""){
			  dialogAlert('Please select post then add user!');	
			  return false;	
		    }
        if ($(this).children("i").hasClass("icon-check-empty")) {
            $(this).children("i").removeClass("icon-check-empty").addClass("icon-check-sign");
      }else{
            $(this).children("i").removeClass("icon-check-sign").addClass("icon-check-empty");
      }
      $("button[hosPost=checkAll]").removeClass("clicked");
      refreshSelectPostMember();
    });
	$(".userlistPost").on("click",".btn .choice-close",function(){
        var _thisID = $(this).attr("data-id"); 
		 $(".postnamelist i.icon-check-sign").each(function(){
            if($(this).data("id")==_thisID){
				$(this).removeClass("icon-check-sign").addClass("icon-check-empty");
			}
        });
       $(this).parent().remove();
		refreshSelectPostMember();
    });
}

//*****************************
//右下角人员与已选人员同步
//*****************************
function refreshSelectPostMember(){
  $(".postnamelist i.icon-check-sign").each(function(){
		var sFlag = false;
    var _thisID = $(this).data("id");
		$("#userlistPost span.username").each(function() {
			if(_thisID == $(this).data("id")){
				sFlag = true;
			}
		});
		if(!sFlag){
		  $("#userlistPost").append("<li class='btn btn-mini'><i class='icon-user text-gray'></i> <span class=username deptId="+$(this).attr("deptId")+" data-id="+$(this).data("id")+" data-name="+$(this).data("name")+">"+$(this).data("name")+"</span> <a class='choice-close' data-id="+$(this).data("id")+" data-name="+$(this).data("name")+">×</a></li>");
		}
   });

   $(".postnamelist i.icon-check-empty").each(function(){
    var _thisID = $(this).data("id");
		$("#userlistPost span.username").each(function() {
			if(_thisID == $(this).data("id")){
				$(this).parent().remove();
			}
		});
    SelectPostToString();
  });
  if($("#userlistPost li").size()==0){
    $("#userlistPost").html('<div class="tip">Select the user from the bottom right of the box</div>');
  }else{
		$("#userlistPost .tip").remove();
	}
	SelectPostToString();
}

//*****************************
//已选择人员与字符串同步
//*****************************
function SelectPostToString(){
	$("#postMemberID").val("");
	$("#postMemberName").val("");
	$("#postdeptId").val("");
	$("#userlistPost span.username").each(function() {
        var _this = $(this);
		($("#postdeptId").val()!="")?$("#postdeptId").val($("#postdeptId").val()+','+_this.attr("deptId")):$("#postdeptId").val(_this.attr("deptId"));
		($("#postMemberID").val()!="")?$("#postMemberID").val($("#postMemberID").val()+','+_this.data("id")):$("#postMemberID").val(_this.data("id"));
		($("#postMemberName").val()!="")?$("#postMemberName").val($("#postMemberName").val()+','+_this.data("name")):$("#postMemberName").val(_this.data("name"));
    });
}

//*****************************
//角色树配置
//*****************************
//角色关联人员树配置
var setting_role1 = {
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
        onClick:clickRole1,
        beforeDblClick: zTreeBeforeDblClick
    }
};
var setting_role2 = {
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
        onClick:clickRole2,
        beforeDblClick: zTreeBeforeDblClick
    }
};
 var setting_dept_role = {
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
        onClick:selectUserByDeptId_role,
        beforeDblClick: zTreeBeforeDblClick
    }
};
//*****************************
//角色关联人员，角色树初始化
//*****************************

var treeRoleNodes=getRoleJson();
$.fn.zTree.init($("#tree3"), setting_role1, treeRoleNodes);


$.fn.zTree.init($("#tree5"), setting_role2, treeRoleNodes);
//*****************************
//角色关联人员，人员树初始化
//*****************************

var rolePersonNodes = getDeptJson();
$.fn.zTree.init($("#tree4"), setting_dept_role, rolePersonNodes);


//角色关联人员 点击组织树节点展示部门下的人员
function selectUserByDeptId_role(event, treeId, treeNode){
  var itemID = (arguments.length==0)?999999:treeNode.id;
  var deptPersons= getMyJson("../punAccessRelationController/getUserListByDeptId.do",{"deptId":itemID,"groupId":groupId});
  $("#userRoleSearchList").html("");
  if(deptPersons!=null&&deptPersons.length>0){
    var list='<ul class="rolenamelist clearfix">';
    $.each(deptPersons,function(index,item){
        var checkClass = contains($("#roleMemberID").val(),item.userId)?"icon-check-sign":"icon-check-empty";
        
                list += '<li title="'+item.name+'"><i data-id="'+item.userId+'" data-name="'+item.name+'" class="'+checkClass+'"></i>'+item.name+'</li>';
         
    });
    list += '</ul>';
    $("#userRoleSearchList").append(list);
  }else{
    $("#userRoleSearchList").html("<div class=tip>The current department has no user.</div>");
  }
}

//*****************************
//角色关联人员，点击人员添加到面板
//*****************************

function personToRolePanel(id,name){
  if($("#rolename").text().replace(/[\r\n\t\x]/g,"")==""){
    dialogAlert('Please select a role then add user.');
    return false;
  }
  var userTag = false;
  $("#userlistRole").find("span").each(function(index, element) {
      //if($(this).text().replace(/[\r\n\t\x]/g,"") == name){
      if($(this).attr("data-id") == id){
        userTag = true;
      }
    });
    if(!userTag){
      $("#userlistRole").append("<li class='btn btn-mini' type='button'><i class='icon-user text-gray'></i> <span class=username   data-id="+id+" data-name="+name+">"+name+"</span> <a class='choice-close' data-id="+id+">×</a></li>");
      ($("#roleMemberID").val()!="")?$("#roleMemberID").val($("#roleMemberID").val()+','+id):$("#roleMemberID").val(id);
      ($("#roleMemberName").val()!="")?$("#roleMemberName").val($("#roleMemberName").val()+','+name):$("#roleMemberName").val(name);
  }
}

//*****************************
//角色关联人员  点击角色，初始化人员
//*****************************

function clickRole1(event, treeId, treeNode){
  var roleId=treeNode.id;
  var roleName=treeNode.name;
  $("#rolename").html(roleName);
  $("#roleID").val(roleId+','+roleName);
  $("#userlistRole").html("");
  $("#roleMemberID").val("");
  $("#roleMemberName").val("");
  $(".rolenamelist").html("");
  //查询角色人员，初始化面板
  var RolePersons = getMyJson("../punUserGroupController/getUserListByWhere.do",{"role":roleId});
  $.each(RolePersons,function(i,item){
        personToRolePanel(item.userId,item.name);
  });  
}
function clickRole2(event, treeId, treeNode){
  var roleId=treeNode.id;
  jQuery("#sysEditFrame").attr("src","punRoleMenuAccessEdit.do?roleId="+roleId);
}







//*****************************
//角色与人员关联面板初始化
//*****************************
function initialRole(){
  $("button[hosRole=checkAll]").click(function(){
    if($("#rolename").text().replace(/[\r\n\t\x]/g,"")==""){
      dialogAlert('Please select a role then add user.'); 
      return false; 
    }
        var _this = $(this);
        if(!_this.hasClass("clicked")){
            var $panel = _this.parents(".panel");
            $panel.find("i.icon-check-empty").addClass("icon-check-sign").removeClass("icon-check-empty");
            _this.addClass("clicked");
            refreshSelectRoleMember();
        }else return false;        
    });
    $("button[hosRole=unCheckAll]").click(function(){
      if($("#rolename").text().replace(/[\r\n\t\x]/g,"")==""){
        dialogAlert('Please select a role then add user.'); 
        return false; 
      }
      $("button[hosRole=checkAll]").removeClass("clicked");
      var $panel = $(this).parents(".panel");
      $panel.find("i.icon-check-sign").addClass("icon-check-empty").removeClass("icon-check-sign");
      refreshSelectRoleMember();
    });
     $(".select-panel").on("click",".rolenamelist li",function(){
        if($("#rolename").text().replace(/[\r\n\t\x]/g,"")==""){
        dialogAlert('Please select a role then add user.'); 
        return false; 
        }
        if ($(this).children("i").hasClass("icon-check-empty")) {
            $(this).children("i").removeClass("icon-check-empty").addClass("icon-check-sign");
      }else{
            $(this).children("i").removeClass("icon-check-sign").addClass("icon-check-empty");
      }
      $("button[hosRole=checkAll]").removeClass("clicked");
      refreshSelectRoleMember();
    });
  $(".userlistRole").on("click",".btn .choice-close",function(){
    var _thisID = $(this).attr("data-id"); 
    $(".rolenamelist i.icon-check-sign").each(function(){
      if($(this).data("id")==_thisID){
        $(this).removeClass("icon-check-sign").addClass("icon-check-empty");
      }
    });
    $(this).parent().remove();
    refreshSelectRoleMember();
    });
}

//*****************************
//角色关联人员 右下角人员与已选人员同步
//*****************************
function refreshSelectRoleMember(){
  $(".rolenamelist i.icon-check-sign").each(function(){
    var sFlag = false;
    var _thisID = $(this).data("id");
    $("#userlistRole span.username").each(function() {
      if(_thisID == $(this).data("id")){
        sFlag = true;
      }
    });
    if(!sFlag){
      $("#userlistRole").append("<li class='btn btn-mini'><i class='icon-user text-gray'></i> <span class=username  data-id="+$(this).data("id")+" data-name="+$(this).data("name")+">"+$(this).data("name")+"</span> <a class='choice-close' data-id="+$(this).data("id")+" data-name="+$(this).data("name")+">×</a></li>");
    }
   });

   $(".rolenamelist i.icon-check-empty").each(function(){
    var _thisID = $(this).data("id");
    $("#userlistRole span.username").each(function() {
      if(_thisID == $(this).data("id")){
        $(this).parent().remove();
      }
    });
    SelectRoleToString();
  });
  if($("#userlistRole li").size()==0){
    $("#userlistRole").html('<div class="tip">Select the user from the bottom right of the box.</div>');
  }else{
    $("#userlistRole .tip").remove();
  }
  SelectRoleToString();
}

//*****************************
//已选择人员与字符串同步
//*****************************
function SelectRoleToString(){
  $("#roleMemberID").val("");
  $("#roleMemberName").val("");
  $("#userlistRole span.username").each(function() {
        var _this = $(this);
    ($("#roleMemberID").val()!="")?$("#roleMemberID").val($("#roleMemberID").val()+','+_this.data("id")):$("#roleMemberID").val(_this.data("id"));
    ($("#roleMemberName").val()!="")?$("#roleMemberName").val($("#roleMemberName").val()+','+_this.data("name")):$("#roleMemberName").val(_this.data("name"));
    });
}

//*****************************
//搜索角色人员
//*****************************
function searchRoleMember(){
  if($("#searchRoleName").val()==""){
    dialogAlert('Please input role name.');  
    return false;
  }
  var RolePersons = getMyJson("../punAccessRelationController/getUserListByParas.do",{"name":$("#searchRoleName").val(),"groupId":groupId});
  $("#userRoleSearchList").html("");
  if(RolePersons!=null&&RolePersons.length>0){
    var list='<ul class="rolenamelist clearfix">';
    $.each(RolePersons,function(index,item){
        var checkClass = contains($("#roleMemberID").val(),item.userId)?"icon-check-sign":"icon-check-empty";
        list += '<li title="'+item.name+'"><i data-id="'+item.userId+'" data-name="'+item.name+'" class="'+checkClass+'"></i>'+item.name+'</li>';         
    });
    list += '</ul>';
    $("#userRoleSearchList").append(list);
  }else{
    $("#userRoleSearchList").html("<div class=tip>No query results</div>");
  }
}

//*****************************
//角色关联功能  
//*****************************

function contains(arr, val) {//数组元素匹配 contains(membersID,val)
  if (arr.indexOf(val) !== -1) {
    return true;
  } else {
    return false;
  }
};

//*****************************
//tab标签切换清空数据
//*****************************
$("#myTab li").each(function() {
	$(this).bind("click",function(){
		var currentTabID = $(this).children(0).attr("href").substr(1,$(this).children(0).attr("href").length-1);
		$("div.tab-pane").each(function() {
			if($(this).attr("id") != currentTabID){
				$(this).find(".ptname").text("");
				$(this).find(".belong").html("");
				$(this).find(".skeyword").val("");
				$(this).find(".SearchList").html("<div class=tip>Please search personnel</div>");
			}
		});
		
	});
});