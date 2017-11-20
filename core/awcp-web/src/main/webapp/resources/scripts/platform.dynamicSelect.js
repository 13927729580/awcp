/**
 * Created by nsccsz-hc
 */
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
var memberObj = {};//搜索 人员
var item ={};//选中 人员
var membersID =[];//选中ID
var showName="";
var showWhere="";
var returnVal="";
/*var nodeList = [];
function searchNode(){
    var zTree = $.fn.zTree.getZTreeObj("tree1");
    var value = $("#searchbox input").val();
    if(value!=""){
    nodeList = zTree.getNodesByParamFuzzy("name", value);
    updateNodes(true);}
    else updateNodes(false);
}
function updateNodes(highlight) {
	var zTree = $.fn.zTree.getZTreeObj("tree1");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}*/
function zTreeBeforeDblClick(){
	  return false;
};
function selectItem(event, treeId, treeNode) {
	if(name.length>45){
		   //组件被包含
		   name=name.substr(name.indexOf("_")+1,name.lenght);
	}
	var url = "./document/refreshDataGrid.do?componentId=" + name+"&dataFile=showDataItemCode";
    $.get(url,function(data){
    	showName=data['showName'].split(".")[1];
    	showWhere=data['showWhere'].split(".")[1];
    	returnVal=data['returnVal'].split(".")[1];
		memberObj = data;
        dataToList(data,treeNode);
    },"json");    
};
function dataToList(data,treeNode){//获取json数据拼接成html片段
		if(data.length==0){
            $(".select-panel .panel-body").html('<div class="tip">请点击左侧菜单</div>');
        }else{
            var list='<ul class="name-list clearfix">';
            $.each(data['data'],function(index,item){
            	if(item[showWhere]==treeNode.id){
                var checkClass = contains(membersID,item[returnVal])?"icon-check":"icon-check-empty";
                if(index<4){
                    list += (index==0)?'<li style="border-width:1px" title="'+item[showName]+'"><i data-id="'+item[returnVal]+'" class="'+checkClass+'"></i>'+item[showName]+'</li>':'<li style="border-top-width:1px"><i data-id="'+item[returnVal]+'" class="'+checkClass+'"></i>'+item[showName]+'</li>';
                }
                else if((index+1)%4 == 1){
                     list += '<li style="border-left-width:1px" title="'+item[showName]+'"><i data-id="'+item[returnVal]+'" class="'+checkClass+'"></i>'+item[showName]+'</li>';
                }
                else{
                    list += '<li title="'+item[showName]+'"><i data-id="'+item[returnVal]+'" class="'+checkClass+'"></i>'+item[showName]+'</li>';
                }
         
            	}
            });
            list += '</ul>';
            $(".select-panel .panel-body").html(list);
        }		
};
function searchMember(){
	var memberName = $("#memberName").val();
	var result = jsonPath(memberObj, "$[?(@.name=='"+memberName+"')]");
	if(result){
		dataToList(result)
	}else{
		alertMessage("没有结果,请输入全名");
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

function refreshSelect(){
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
    	$(".member-panel .panel-body").html('<div class="tip">请从下方选择框选择用户</div>');
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
	
	try {
	var dialog = top.dialog.get(window);
	} catch (e) {
	return;
	}
	var data = null;
			
	if(dialog != null){
	data = dialog.data;
	}	
	
	if(name!=null&&name!=""){
	if(name.length>45){
		   //组件被包含
		   name=name.substr(name.indexOf("_")+1,name.lenght);
	}
	var url = "./document/refreshDataGrid.do?componentId=" + name+"&dataFile=dataItemCode&currentPage=1&pageSize=10";
	var treeNodes=[];
	$.ajaxSetup({  
		async : false
	}); 
	 $.get(url,function(data){
	 		var id=data['zTreeId'].split('.')[1];
			var pid=data['pId'].split('.')[1];
			var name=data['zTreeName'].split('.')[1];
		 $.each(data['data'],function(i,item){
				var nodes={
					id:item[id],pId:item[pid],name:item[name]
				}
				treeNodes.push(nodes);
		 });

	 });
	
    $.fn.zTree.init($("#tree1"), setting,treeNodes);//ztree 树加载 
    //selectItem()//人员 加载
    $("button[js=checkAll]").click(function(){
        var _this = $(this);
        if(!_this.hasClass("clicked")){
            var $panel = _this.parents(".panel");
            $panel.find("i.icon-check-empty").addClass("icon-check").removeClass("icon-check-empty");
            _this.addClass("clicked");
            refreshSelect();
        }else return false;        
    });
    $("button[js=unCheckAll]").click(function(){
        $("button[js=checkAll]").removeClass("clicked");
        var $panel = $(this).parents(".panel");
        $panel.find("i.icon-check").addClass("icon-check-empty").removeClass("icon-check");
        refreshSelect();
    });
    $(".select-panel").on("click",".name-list li",function(){
        var _this = $(this);
        if (_this.children("i").hasClass("icon-check-empty")) {
            _this.children("i").addClass("icon-check").removeClass("icon-check-empty");
        }else{
            _this.children("i").addClass("icon-check-empty").removeClass("icon-check");
        };
        $("button[js=checkAll]").removeClass("clicked");
        refreshSelect();
    });
    $(".member-panel").on("click",".choice-list .choice-close",function(){
        var _this = $(this);
        var _thisID = _this.data("id"); 
        _this.parent().remove();
        delete item[_thisID];
        var index = membersID.indexOf(_thisID);
        if(index>=0) {
            membersID.splice(index,1);
            $(".member-panel .panel-heading").attr("membersID",membersID.join());//绑定已选数据
            $(".select-panel .name-list i[data-id="+_thisID+"]").addClass("icon-check-empty").removeClass("icon-check");
            $("button[js=checkAll]").removeClass("clicked");
        }
        else return;
    });
    
    }
});
