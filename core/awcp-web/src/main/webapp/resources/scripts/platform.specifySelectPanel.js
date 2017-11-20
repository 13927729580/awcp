/**
 * Created by nsccsz-hc
 */

var item = {};		//选中 人员
var membersID =[];	//选中ID

function dataToList(data){
	//获取json数据拼接成html片段
	if(data.length==0){
		$(".select-panel .panel-body").html('<div class="tip">当前用户没有下级.</div>');
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

//数组元素匹配 contains(membersID,val)
function contains(arr, val) {
	if (arr.indexOf(val) !== -1) {
		return true;
	} 
	else {
		return false;
	}
};

function GetRequest() {
	var url = location.search; //获取url中"?"符后的字串
	url=url.replace("NodeID","&NodeID");
	url=url.replace("FK_Flow","&FK_Flow");
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for(var i = 0; i < strs.length; i ++) {
			theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
} 


//加载下级用户
function searchSubUser(){
    $(".select-panel .panel-body").html('<div class="tip"></div>');
	var Request = new Object();
	Request = GetRequest();
	
	var url = "./common/user/queryReceiver.do?WorkID="+Request["WorkID"]+"&NodeID="+Request["NodeID"]+"&FK_Flow="+Request["FK_Flow"];
	
	$.ajaxSetup({ 
		async : false
	}); 
	$.get(url,function(data){
		if(data!=null && data!=""){
		 	memberObj = data;
		 	dataToList(data);
		}else{
		    //下级用户为空 
		    $(".select-panel .panel-body").html('<div class="tip">当前用户没有下级.</div>');
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
		}
		else {
			return;
		}        
    });
	 
	$("div.select-panel").find("i.icon-check-empty").each(function(){
		var _this = $(this);
		delete item[_this.data("id")];
		var index = membersID.indexOf(_this.data("id"));
		if(index>=0){
	  	    //删除该ID
			membersID.splice(index,1);
		}
		else{
			return;
		}
	});
    
    $(".member-panel .panel-heading").attr("membersID",membersID.join());
    if(membersID.length==0){
    	$(".member-panel .panel-body").html('<div class="tip">请从中间选择框选择用户</div>');
    }else{
	    var memberList = '<ul class="choice-list">';
	    $.each(item,function(i,n){        
	        memberList += '<li><span>'+n+'</span><a class="choice-close" data-id="'+i+'">×</a></li>'; 
	    });
	    memberList +='</ul>';
	    $(".member-panel .panel-body").html(memberList);
    }
};

function initUserSelectPanel(){
	//全选
	$("button[js=checkAll]").click(function(){
        var _this = $(this);       
		var $panel = _this.parents(".panel");
		$panel.find("i.icon-check-empty").addClass("icon-check").removeClass("icon-check-empty");
		_this.addClass("clicked");
		refreshSelect();              
    });
	
	//全不选
    $("button[js=unCheckAll]").click(function(){
        $("button[js=checkAll]").removeClass("clicked");
        var $panel = $(this).parents(".panel");
        $panel.find("i.icon-check").addClass("icon-check-empty").removeClass("icon-check");
        refreshSelect();
    });

	//全部删除已经选择的
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
	
	//选择panel中选项click事件
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

	//merber panel中选项click事件
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
        else{
			return;
		}
        if($(".member-panel .choice-list li").size()==0){
        	$(".member-panel  .panel-body").html('<div class="tip">请从中间选择框选择用户</div>');
        }
    });

}

//数据回显
function initUserSelected(){
	var datas = document.getElementById("datas").value;
	var url = "./unit/getNamesByUserIds.do?userIds="+datas;
	$.ajax({
		type:'post',
		url:'./unit/getNamesByUserIds.do?userIds='+datas,
		async:false,
		dataType:"html",
		success:function(data){
			if(data!="" && data!=null){
				var c = data.split(',');
				var v = datas.split(',');
				var jsonData = "["
				for(var i=0;i<c.length;i++){
					jsonData += "{";
					jsonData += "name:'"+c[i]+"'";
					jsonData += ",";
					jsonData += "userId:"+v[i];
					jsonData += "},";
				}
				jsonData = jsonData.substring(0,jsonData.length-1);
				jsonData += "]";
				var json = eval(jsonData);
				dataToList(json);
				$("button[js=checkAll]").click();
				refreshSelect(true);
				$(".select-panel .panel-body").html('<div class="tip">请从左侧选择菜单（用户）</div>');				
			}
		}
	});       
}

//页面加载后执行
$(function(){
	initUserSelectPanel();
    initUserSelected();
	setTimeout('searchSubUser()',200);	
});
