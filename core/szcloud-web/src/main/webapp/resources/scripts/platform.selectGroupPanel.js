/**
 * Created by nsccsz-hc
 */
 
 
var memberObj = {};//搜索 人员
var item ={};//选中 人员
var membersID =[];//选中ID

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
        onClick:selectItem,
        beforeDblClick: zTreeBeforeDblClick
    }
};

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




function zTreeBeforeDblClick(){
	  return false;
};
function selectItem(event, treeId, treeNode) {
	
	if(treeId!=undefined){
	  //标示为部门
	  parent.frames["main"].document.getElementById("slectsUserIds").value="10";
	}
    var itemID = (arguments.length==0)?999999:treeNode.id,
        url = "punUserGroupController/getUserListByAjax.do";
    var data = {"groupId":itemID};
    //todo Ajax
    $.get(url,data,function(data){
    	memberObj = data;
        dataToList(data);
    },"json");    
};

function selectItemByRole(event, treeId, treeNode) {
	
	parent.frames["main"].document.getElementById("slectsUserIds").value="";
	
    var itemID = (arguments.length==0)?999999:treeNode.id,
        url = "punUserGroupController/getUserListByWhere.do";
    var data = {"role":itemID};
    //todo Ajax
    $.post(url,data,function(data){
    	memberObj = data;
        dataToList(data);
    },"json");    
};

function selectItemByJob(event, treeId, treeNode) {
	parent.frames["main"].document.getElementById("slectsUserIds").value="";
	
    var itemID = (arguments.length==0)?999999:treeNode.id,
        url = "punUserGroupController/getUserListByWhere.do";
    var data = {"job":itemID};
    //todo Ajax
    $.post(url,data,function(data){
    	memberObj = data;
        dataToList(data);
    },"json");    
};

//根据组查询组人员
function selectItemByGroup(event, treeId, treeNode){
	parent.frames["main"].document.getElementById("slectsUserIds").value="";
	
	var itemID = (arguments.length==0)?999999:treeNode.id,
    url = "UserGroup/queryUser.do";
    var data = {"GroupID":itemID};
    $.post(url,data,function(data){
    	memberObj = data;
        dataToList(data);
    },"json"); 

};

function subSearch(){
	
	var zTree = $.fn.zTree.getZTreeObj("tree1");
	var wq=$("#wq").val();
	var itemID = (arguments.length==0)?999999:zTree.id,
        url = "punUserGroupController/getUserListByWhere.do";
    var data = {"groupId":itemID,"eq":wq};
    //todo Ajax
    $.post(url,data,function(data){
    
    	if(data!=null){
    		memberObj = data;
            dataToList(data);
    	}else{
    	  alert("沒有結果");
    	}
    	
    },"json");
};




function dataToList(data){//获取json数据拼接成html片段

		if(data.length==0){
            $(".select-panel .panel-body").html('<div class="tip">请从左侧选择菜单（用户）</div>');
        }else{
            var list='<ul class="name-list clearfix">';
            $.each(data,function(index,item){
               var checkClass = contains(membersID,item.userId)?"icon-check":"icon-check-empty";
                if(index<4){
                    list += (index==0)?'<li style="border-width:1px" title="'+item.name+'"><i data-id="'+item.userId+'" class="'+checkClass+'"></i>'+item.name+'</li>':'<li style="border-top-width:1px"><i data-id="'+item.userId+'" class="'+checkClass+'"></i>'+item.name+'</li>';
                }
                else if((index+1)%4 == 1){
                     list += '<li style="border-left-width:1px" title="'+item.name+'"><i data-id="'+item.userId+'" class="'+checkClass+'"></i>'+item.name+'</li>';
                }
                else{
                    list += '<li title="'+item.name+'"><i data-id="'+item.userId+'" class="'+checkClass+'"></i>'+item.name+'</li>';
                }
            });
            list += '</ul>';
            $(".select-panel .panel-body").html(list);
        }		

};
function searchMember(){
	var memberName = $("#memberName").val();
	var result = jsonPath(memberObj, "$[?(@.name.indexOf('"+memberName+"')!==-1)]");
	if(result){
		dataToList(result)
	}else{
		alertMessage("查无此人,请输入全名");
		dataToList(memberObj);
		return false;
	}
}
function contains(arr, val) {//数组元素匹配 contains(membersID,val)
  if (arr.indexOf(val) !== -1) {
    return true;
  } else {
    return false;
  }
};

//角色
function searchRole(){
	//$("#search").hide();
	//隐藏部门、分组子tab
	$("#departTab").hide();
	$("#groupTab").hide();
	$("#tab1").height(316);
	$("#tab1").css("border-left-color","transparent");
	//获取系统id
	//var id=$("#pageId").val();
	var treeNodes=[];
	var url = basePath+"query/listRolesInSysByAjaxForJf.do?roles="+roleId;
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
   $.fn.zTree.init($("#tree1"), setting_role, treeNodes);//ztree 树加载 
   selectItem()//人员 加载
}

//岗位
function searchJob(){
	//$("#search").hide();
	//隐藏部门、分组子tab
	$("#departTab").hide();
	$("#groupTab").hide();
	$("#tab1").height(316);
	$("#tab1").css("border-left-color","transparent");
	var treeNodes=[];
	var url = "./punPositionController/pageListByAjax.do?groupId="+groupId;
	$.ajaxSetup({ 
		async : false
	}); 
	 $.get(url,function(data){
		 $.each(data,function(i,item){
			
			var nodes={
					id:item.positionId,pId:item.positionId,name:item.name
				}
			treeNodes.push(nodes);
			 
		 });
	 });
   $.fn.zTree.init($("#tree1"), setting_job, treeNodes);//ztree 树加载 
   selectItem()//人员 加载


}

//切换分组tab
function searchGroup(){
	$("#departTab").hide();
	$("#groupTab").show();
	$("#tab1").height(316);
	$("#tab1").css("border-left-color","#ccc");
	var treeNodes=[];
	var url = "./UserGroup/queryGroups.do";
	$.ajaxSetup({ 
		async : false
	}); 
	 $.get(url,function(data){
		 $.each(data,function(i,item){
			 var nodes={
					id:item["ID"],pId:item["ID"],name:item["GroupName"]
				}
			treeNodes.push(nodes);
			 
		 });
	 });
   $.fn.zTree.init($("#tree1"),setting_group, treeNodes);//ztree 树加载 
   selectItem()//人员 加载
}


//联动数据更新

function refreshSelect(modifyFlag){

	$("div.select-panel").find("i.icon-check").each(function(){
		        var _this = $(this);
		        item[_this.data("id")]=_this.parent().text();
		        if(!contains(membersID,_this.data("id"))){
		            membersID.push(_this.data("id"));    
		        }else return;
		        
     });
	$("div.select-panel").find("i.icon-check-empty").each(function(){
		        var _this = $(this);
		        delete item[_this.data("id")];
		        var index = membersID.indexOf(_this.data("id"));
		        if(index>=0) membersID.splice(index,1);
		        else return;
	});

   //是否修改编辑标志
   
	if(!modifyFlag)
	{
		     $("#tree1").find(".name-list i.icon-check").each(function(){
				        var _this = $(this);
				        item[_this.data("id")]=_this.parent().text();
				        if(!contains(membersID,_this.data("id"))){
				            membersID.push(_this.data("id"));    
				        }else return;
				        
			 });
		     $("#tree1").find(".name-list i.icon-check-empty").each(function(){
				        var _this = $(this);
				        delete item[_this.data("id")];
				        var index = membersID.indexOf(_this.data("id"));
				        if(index>=0) membersID.splice(index,1);
				        else return;
			  });    
	}
	else
	{
		$("#tree1").find(".name-list i").each(function(){
			 if(contains(membersID,$(this).data("id"))){
			  	$(this).removeClass("icon-check-empty").addClass("icon-check");
			 }
		});
	}
    
    $(".member-panel .panel-heading").attr("membersID",membersID.join());
    if(membersID.length==0){
    	$(".member-panel .panel-body").html('<div class="tip">请从中间选择框选择用户</div>');
    }else{
	    var memberList = '<ul class="choice-list">';
	    $.each(item,function(i,n){        
	        memberList += '<li><span>'+n+'</span><a class="choice-close" data-id="'+i+'">×</a></li>';//<span>樱桃</span><a class="search-choice-close" data-option-array-index="3"></a>
	    });
	    memberList +='</ul>';
	    $(".member-panel .panel-body").html(memberList);
    }
};




$(function(){
	initUserSelectPanel();
	searchRole();
    initUserSelected();
	
});



function initUserSelectPanel(){

	$("button[js=checkAll]").click(function(){
        var _this = $(this);
        //if(!_this.hasClass("clicked")){
            var $panel = _this.parents(".panel");
            $panel.find("i.icon-check-empty").addClass("icon-check").removeClass("icon-check-empty");
            _this.addClass("clicked");
            refreshSelect();
        //}else return false;        
    });
    $("button[js=unCheckAll]").click(function(){
        $("button[js=checkAll]").removeClass("clicked");
        var $panel = $(this).parents(".panel");
        $panel.find("i.icon-check").addClass("icon-check-empty").removeClass("icon-check");
        refreshSelect();
    });

	$("button[js=deleteAll]").click(function(){
		$(".member-panel .choice-list .choice-close").each(function(){
				var _this = $(this);
        		var _thisID = _this.data("id"); 
        		_this.parent().remove();
        		delete item[_thisID];
       			 var index = membersID.indexOf(_thisID);
        		if(index>=0) {
            		membersID.splice(index,1);
            			$(".member-panel .panel-heading").attr("membersID",membersID.join());//绑定已选数据
            			if($(".select-panel .name-list i").size()>0){
            				$(".select-panel .name-list i[data-id="+_thisID+"]").addClass("icon-check-empty").removeClass("icon-check");
            			}
            			if($("#tree1 .name-list i").size()>0){
            				$("#tree1 .name-list i[data-id="+_thisID+"]").addClass("icon-check-empty").removeClass("icon-check");
            			}
           			   $("button[js=checkAll]").removeClass("clicked");
        		}
		})
		

        refreshSelect();
    });
	
    $("div.select-panel").on("click",".name-list li",function(){
        var _this = $(this);
        if (_this.children("i").hasClass("icon-check-empty")) {
            _this.children("i").addClass("icon-check").removeClass("icon-check-empty");
        }else{
            _this.children("i").addClass("icon-check-empty").removeClass("icon-check");
        };
        $("button[js=checkAll]").removeClass("clicked");
        refreshSelect();
    });
    

   if($("#tree1").find("ul.name-list").size()>0){
		    $("#tree1").on("click",".name-list li",function(){
		        var _this = $(this);
		        if (_this.children("i").hasClass("icon-check-empty")) {
		            _this.children("i").addClass("icon-check").removeClass("icon-check-empty");
		        }else{
		            _this.children("i").addClass("icon-check-empty").removeClass("icon-check");
		        };
		        refreshSelect();
		    });
    }

    $("div.member-panel").on("click",".choice-list .choice-close",function(){
        var _this = $(this);
        var _thisID = _this.data("id"); 
        _this.parent().remove();
        delete item[_thisID];
        var index = membersID.indexOf(_thisID);
        if(index>=0) {
            membersID.splice(index,1);
            $(".member-panel .panel-heading").attr("membersID",membersID.join());//绑定已选数据
            $(".select-panel .name-list i[data-id="+_thisID+"]").addClass("icon-check-empty").removeClass("icon-check");
            if($("#tree1 .name-list i").size()>0){
            	$("#tree1 .name-list i[data-id="+_thisID+"]").addClass("icon-check-empty").removeClass("icon-check");
            }
            $("button[js=checkAll]").removeClass("clicked");
        }
        else return;
        if($(".member-panel .choice-list li").size()==0){
        	$(".member-panel  .panel-body").html('<div class="tip">请从中间选择框选择用户</div>');
        }
    });

}



//数据回显
function initUserSelected(){
        
          var datas=document.getElementById("datas").value;
          
          
          var url = "./unit/getNamesByUserIds.do?userIds="+datas;
  

          $.ajax({
              type:'post',
              url:'./unit/getNamesByUserIds.do?userIds='+datas,
              async:false,
              dataType:"html",
              success:function(data){
                if(data!="" && data!=null){
                var c=data.split(',');
                var v=datas.split(',');
                var jsonData="["
                for(var i=0;i<c.length;i++){
                  jsonData+="{";
                  jsonData+="name:'"+c[i]+"'";
                  jsonData+=",";
                  jsonData+="userId:"+v[i];
                  jsonData+="},";
                }
                jsonData=jsonData.substring(0,jsonData.length-1);
                jsonData+="]";
                var json=eval(jsonData);
                //memberObj=json;
                dataToList(json);
                $("button[js=checkAll]").click();
                refreshSelect(true);
                $(".select-panel .panel-body").html('<div class="tip">请从左侧选择菜单（用户）</div>');
                }
              }
            });

        
}
