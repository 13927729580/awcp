function initOrderBy(){
	var orderBy = $("#orderBy").val();	
	$("div.datatable .sort").each(function(){
		var $o = $(this);
		var tmp = $o.find("span").attr("id");
		var keyStr = tmp + " " + "DESC";
		var index = orderBy.indexOf(keyStr);
		var icon = $o.find("i");
		if(index != -1) {
			icon.attr("class","icon-caret-down");
		} else {
			icon.attr("class","icon-caret-up");
		}
	});
	
	$("body").on("click","div.datatable .sort",function(){
		var icon = $(this).find("i");
		var iconClass = icon.attr("class");
		/**获取当前列的ID值**/
		//var pIcon = $("table.datatable th").eq($(this).data("index"));
        //var colID = pIcon.attr("id");
        
		if(iconClass=="icon-caret-down"){//顺序
			icon.attr("class","icon-caret-up");
			orderByRun($(this), "ASC");
		}else{//倒序
			icon.attr("class","icon-caret-down");
			orderByRun($(this), "DESC");
		}
	});
}
function removeArr(arr, val) {
	    var index = indexOfArr(arr, val);
	    if (index > -1) {
	        arr.splice(index, 1);
	    }
	    return arr;
}
function indexOfArr(arr, val) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i].indexOf(val)!=-1)
            return i;
    }
    return -1;
}
function orderByRun(o, orderType){
	var tmp = o.find("span");
	var orderBy = $("#groupForm #orderBy").val();
	var columName = tmp.attr("id");
//	var newOrder=[];
//	if($.trim(orderBy)){
//		newOrder=orderBy.split(',');
//		newOrder=removeArr(newOrder,columName);
//		newOrder.push(columName+" "+orderType);
//	}else{
//		newOrder.push(columName+" "+orderType);
//	}
//	$("#groupForm #orderBy").val(newOrder.join(","));
	$("#groupForm #orderBy").val(columName+" "+orderType);
	$("#groupForm").attr("action",basePath + "document/view.do").submit();
}