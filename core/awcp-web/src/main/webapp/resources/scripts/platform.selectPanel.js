/**
 * Created by nsccsz-hc
 */
var memberObj = {};//搜索 人员
var item ={};//选中 人员
var membersID =[];//选中ID
var isSingle = $("#isSingle").val();

//部门数参数设置
var setting_group = {
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
        onClick:selectItemByGroup,
        beforeDblClick: zTreeBeforeDblClick
    }
};

//角色数参数设置
var setting_role = {
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
        onClick:selectItemByRole,
        beforeDblClick: zTreeBeforeDblClick
    }
};

//职位数参数设置
var setting_job = {
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
        onClick:selectItemByJob,
        beforeDblClick: zTreeBeforeDblClick
    }
};

function zTreeBeforeDblClick(){
	return false;
}

//根据角色ID查询用户
function selectItemByRole(event, treeId, treeNode) {
    var itemID = (arguments.length==0)?999999:treeNode.id,
        url = "punUserGroupController/getUserListByWhere.do";
    var data = {"role":itemID};
    $.post(url,data,function(data){
    	memberObj = data;
        dataToList(data);
    },"json");    
}

//根据职位ID查询用户
function selectItemByJob(event, treeId, treeNode) {
    var itemID = (arguments.length==0)?999999:treeNode.id,
        url = "punUserGroupController/getUserListByWhere.do";
    var data = {"job":itemID};
    $.post(url,data,function(data){
    	memberObj = data;
        dataToList(data);
    },"json");    
}

//根据部门ID查询用户
function selectItemByGroup(event, treeId, treeNode){
	var itemID = (arguments.length==0)?999999:treeNode.id,
        url = "punUserGroupController/getUserListByAjax.do";
    var data = {"groupId":itemID};
    $.get(url,data,function(data){
    	memberObj = data;
        dataToList(data);
    },"json");  
}

//根据姓名模糊查询用户
function subSearch(){
	var wq = $("#wq").val();
	var url = "punUserGroupController/getUserListByWhere.do";
    var data = {"name":wq};
    $.post(url,data,function(data){  
    	if(data!=null){
    		memberObj = data;
            dataToList(data);
    	}else{
    		alert("沒有結果");
    	}  	
    },"json");
}

//获取json数据拼接成html片段
function dataToList(data){
	if(data.length == 0){
        $(".select-panel .panel-body").html('<div class="tip">请从左侧选择菜单（用户）</div>');
    }else{
        var list = '<ul class="name-list clearfix">';
        $.each(data,function(index,item){
        	var checkClass = contains(membersID,item.userId)?"icon-check":"icon-check-empty";
            if(index < 4){
                list += (index==0)?'<li style="border-width:1px" title="'+item.name+'"><i data-id="'+item.userId+'" class="'+checkClass+'"></i>'+item.name+'</li>':'<li style="border-top-width:1px"><i data-id="'+item.userId+'" class="'+checkClass+'"></i>'+item.name+'</li>';
            } else if((index+1)%4 == 1){
                 list += '<li style="border-left-width:1px" title="'+item.name+'"><i data-id="'+item.userId+'" class="'+checkClass+'"></i>'+item.name+'</li>';
            } else{
                list += '<li title="'+item.name+'"><i data-id="'+item.userId+'" class="'+checkClass+'"></i>'+item.name+'</li>';
            }
        });
        list += '</ul>';
        $(".select-panel .panel-body").html(list);
    }		
}

function searchMember(){
	var memberName = $("#memberName").val();
	var result = jsonPath(memberObj, "$[?(@.name.indexOf('" + memberName + "')!==-1)]");
	if(result){
		dataToList(result)
	}else{
		alertMessage("查无此人");
		dataToList(memberObj);
		return false;
	}
}

//数组元素匹配 contains(membersID,val)
function contains(arr, val) {
	for(var i=0;i<arr.length;i++){
		if(arr[i] == val){
			return true;
		}
	}
	return false;
}

//角色
function searchRole(){
	$("#tab1").height(316);
	$("#tab1").css("border-left-color","transparent");
	var systemId = "";
	$.ajaxSetup({ 
		async : false
	}); 
	$.post(basePath + "component/getSystemId.do",function(data){
		systemId = data.systemId;
	});
	if(systemId != null){
		var treeNodes = [];
		var url = basePath + "unit/listRolesInSysByAjax.do?boxs=" + systemId;
		$.ajaxSetup({ 
			async : false
		}); 
		$.get(url,function(data){
			$.each(data,function(i,item){
				var nodes={
					id:item.roleId,
					pId:item.roleId,
					name:item.roleName
				}
				treeNodes.push(nodes);		 
			});
		});
		$.fn.zTree.init($("#tree1"), setting_role, treeNodes);//ztree 树加载 
	}
}

//岗位
function searchJob(){
	$("#tab1").height(316);
	$("#tab1").css("border-left-color","transparent");
	var treeNodes = [];
	var url = basePath + "punPositionController/pageListByAjax.do?groupId="+groupId;
	$.ajaxSetup({ 
		async : false
	}); 
	$.get(url,function(data){
		$.each(data,function(i,item){
			var nodes={
				id:item.positionId,
				pId:item.positionId,
				name:item.name
			}
			treeNodes.push(nodes);
		});
	});
	$.fn.zTree.init($("#tree1"), setting_job, treeNodes);//ztree 树加载 
}

//加载常用联系人树
function searchNormalPerson(){
    $(".select-panel .panel-body").html('<div class="tip">请从左侧选择菜单（用户）</div>');
	$("#tab1").height(316);
	$("#tab1").css("border-left-color","#ccc");
	var url = basePath + "common/user/queryCommonUser.do?";
	$.ajaxSetup({ 
		async : false
	}); 
	$.get(url,function(data){
		if(data!=null && data!=""){
			memberObj = data;
		 	dataToList(data);
		}
	});  
}

//联动数据更新
function refreshSelect(){
	$("div.select-panel").find("i.icon-check").each(function(){
		var _this = $(this);
		item[_this.data("id")] = _this.parent().text();
		if(!contains(membersID,_this.data("id"))){
			membersID.push(_this.data("id"));    
		} else {
			return;
		}		        
    });
	
	$("div.select-panel").find("i.icon-check-empty").each(function(){
		var _this = $(this);
		delete item[_this.data("id")];
		var index = membersID.indexOf(_this.data("id"));
		if(index >= 0) {
			membersID.splice(index,1);
		} else {
			return;
		}
	});

    $(".member-panel .panel-heading").attr("membersID",membersID.join());
    if(membersID.length==0){
    	$(".member-panel .panel-body").html('<div class="tip">请从中间选择框选择用户</div>');
    } else{
	    var memberList = '<ul class="choice-list">';
	    $.each(item,function(i,n){        
	        memberList += '<li><span>' + n + '</span><a class="choice-close" data-id="' + i + '">×</a></li>';
	    });
	    memberList += '</ul>';
	    $(".member-panel .panel-body").html(memberList);
    }
};

//查询部门
function searchDept(){
	var treeNodes;	
	var url = basePath + "queryDeptTreeData.do?type=simple";
	var data = {boxs:groupId};
	$.ajaxSetup({ 
		async : false,
		datatype:"json"
	}); 
	$.post(url,function(data){ 	
		treeNodes=data;		 
	});
    $.fn.zTree.init($("#tree1"), setting_group, treeNodes);//ztree 树加载 
}

//初始化
function initUserSelectPanel(){
	if(isSingle=="Y"){//单人选择框时,隐藏全选,全不选,全删除按钮
		$("#checkAll,#unCheckAll,#deleteAll").hide();
	} else{
		//全选按钮事件
		$("#checkAll").click(function(){
	        var _this = $(this);      
	        var $panel = _this.parents(".panel");
	        $panel.find("i.icon-check-empty").addClass("icon-check").removeClass("icon-check-empty");
	        refreshSelect();       
	    });
		
		//全不选按钮事件
	    $("#unCheckAll").click(function(){
	        $("#checkAll").removeClass("clicked");
	        var $panel = $(this).parents(".panel");
	        $panel.find("i.icon-check").addClass("icon-check-empty").removeClass("icon-check");
	        refreshSelect();
	    });

	    //全删除按钮事件
		$("#deleteAll").click(function(){
			$(".member-panel .choice-list .choice-close").each(function(){
				var _this = $(this);
	    		var _thisID = _this.data("id"); 
	    		_this.parent().remove();
	    		delete item[_thisID];
	   			var index = membersID.indexOf(_thisID);
	    		if(index >= 0) {
	        		membersID.splice(index,1);     			
        			if($(".select-panel .name-list i").size() > 0){
        				$(".select-panel .name-list i[data-id=" + _thisID + "]").addClass("icon-check-empty").removeClass("icon-check");
        			}        			
	    		}
			});
			$(".member-panel .panel-heading").attr("membersID",membersID.join());
	        refreshSelect();
	    });
	}
			
    $("div.select-panel").on("click",".name-list li",function(){
        var _this = $(this);
        if (_this.children("i").hasClass("icon-check-empty")) {
        	if(isSingle=="Y"){
        		_this.siblings("li").each(function(){
        			if($(this).children("i").hasClass("icon-check")){
        				$(this).children("i").addClass("icon-check-empty").removeClass("icon-check");
        			}
        		});
        		item ={};
        		membersID =[];
        	}       	
            _this.children("i").addClass("icon-check").removeClass("icon-check-empty");
        }else{
            _this.children("i").addClass("icon-check-empty").removeClass("icon-check");
        };
        refreshSelect();
    });    

    $("div.member-panel").on("click",".choice-list .choice-close",function(){
        var _this = $(this);
        var _thisID = _this.data("id"); 
        _this.parent().remove();
        delete item[_thisID];
        var index = membersID.indexOf(_thisID);
        if(index >= 0) {
            membersID.splice(index,1);
            $(".member-panel .panel-heading").attr("membersID",membersID.join());//绑定已选数据
            $(".select-panel .name-list i[data-id=" + _thisID + "]").addClass("icon-check-empty").removeClass("icon-check");
            if($("#tree1 .name-list i").size()>0){
            	$("#tree1 .name-list i[data-id="+_thisID+"]").addClass("icon-check-empty").removeClass("icon-check");
            }
        } else {
        	return;
        }
        if($(".member-panel .choice-list li").size()==0){
        	$(".member-panel  .panel-body").html('<div class="tip">请从中间选择框选择用户</div>');
        }
    });
}

//数据回显
function initUserSelected(){
    var datas = $("#datas").val();
    var url = basePath + "unit/getNamesByUserIds.do?userIds=" + datas;
    $.ajax({
        type:'post',
        url:url,
        async:false,
        dataType:"html",
        success:function(data){
        	if(data!="" && data!=null){
        		var c = data.split(',');
        		var v = datas.split(',');
        		for(var i=0;i<c.length;i++){
        			item[v[i]] = c[i];
        			membersID.push(v[i]);
        		}
        		refreshSelect();
        	}
        }
    });
}

$(function(){
	initUserSelectPanel();
	searchDept();
    initUserSelected();
	setTimeout("searchNormalPerson()",200);	
});
