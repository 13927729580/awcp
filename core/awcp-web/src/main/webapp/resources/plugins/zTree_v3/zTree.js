//引入相应的css/js
var cs = document.createElement("link");
cs.setAttribute("rel","stylesheet");
cs.setAttribute("type","text/css");
cs.setAttribute("href", location.protocol + "//" + location.host + "/" +"resources/styles/zTreeStyle/szcloud.css");
document.getElementsByTagName("head")[0].appendChild(cs);

//配置参数
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

function selectItem(event, treeId, treeNode) {
	return false;
};

function zTreeBeforeDblClick(){
	  return false;
};

//加载数据
$(function(){
	var componentId = $("#treePageId").val();
	if(componentId!=null && typeof(componentId) != 'undefined'){
		var url = "../document/refreshDataGrid.do?componentId=" + componentId + "&dataFile=dataItemCode&currentPage=1&pageSize=10";
		var treeNodes = [];
		$.ajaxSetup({  
			async : false
		}); 
		$.get(url,function(data){
		 	var id = data['zTreeId'].split('.')[1];
			var pid = data['pId'].split('.')[1];
			var name = data['zTreeName'].split('.')[1];
			$.each(data['data'],function(i,item){
				var nodes = {
					id:item[id],
					pId:item[pid],
					name:item[name]
				}
				treeNodes.push(nodes);
			});
		});		
	    $.fn.zTree.init($("#tree1"), setting,treeNodes);//ztree 树加载 
	}
});