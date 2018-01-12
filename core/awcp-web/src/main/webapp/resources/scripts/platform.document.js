//新增
function actNewRun(actId,pageId){
	$("#"+actId).attr("disabled","true");
	location.href = basePath + "document/view.do?dynamicPageId=" + pageId;
}

//编辑
function actUpdateRun(actId,pageId){
	if(count==1){
		var id = $("input[name='_selects']").val();
		$("#"+actId).attr("disabled","true");
		location.href = basePath + "document/view.do?dynamicPageId=" + pageId + "&id=" + id;
	}else{
		Comm.alert("请勾选一个选项");
	}
}

//返回
function actBackRun(actId,pageId){
	$("#"+actId).attr("disabled","true");
	location.href = basePath + "document/view.do?dynamicPageId=" + pageId;
}

function actRun(actId){
	$("#actId").val(actId);
	$("#"+actId).attr("disabled","true");
	$("#groupForm").attr("action",basePath + "document/excute.do").submit();	
}

function actRunWithNoValidators(actId){
	$("#actId").val(actId);
	$("#"+actId).attr("disabled","true");
	$("#groupForm").attr("action",basePath + "document/excute.do").submit();
}


function actDeleteRun(actId){
	if(count==0){
		Comm.alert("请至少勾选一个选项");
		return false;
	}
	$("#actId").val(actId);	
	$("#"+actId).attr("disabled","true");
	$("#groupForm").attr("action",basePath + "document/excute.do").submit();	
}

function checkIsNumber(obj){
	var value = $.trim($(obj).val());
	value = value.replace(/,/g,'');
	if(value && !$.isNumeric(value)){
		$(obj).focus();
		dialogAlert("Please input a number.");		
		return false;
	}
	else{
		return true;
	}
}

function formatNumber(num){
	var bool = false;
	if(num.indexOf(".")!=-1){
		bool = true;
	}
	var arr = num.split(".");
	var before = "";
	var after = "";
	if(arr.length==2){
		before = arr[0];
		after = arr[1];
	}
	else{
		before = arr[0];				
	}
	var beforeArrReverse = before.split("").reverse();  
	var temp = "";   
	for(var i = 0; i < beforeArrReverse.length; i ++ )   {   
		temp += beforeArrReverse[i] + ((i + 1) % 3 == 0 && (i + 1) != beforeArrReverse.length ? "," : "");   
	}   
	var result = "";
	if(after!=""){
		result = temp.split("").reverse().join("") + "." + after;
	}
	else{
		result = temp.split("").reverse().join("") + (bool?".":"");
	}	
	return result;
}

/**
 *通过组件名称获取组件  返回Jquery对象
 * @param name 
 */
function getComponentByName(name){
	var $component =$("[name='"+name+"']");
	return $component;
}

/**
 * 通过ajax 请求信息 刷新组件数据
 * @param component
 */
function refresh($component,data){
	$.ajax({
		type:'POST',
		data:data,
		dataType:"html",
		async:false,
		url:basePath+'document/refreshState.do?componentId='+$component.attr("id"),
		success:function(result){
			$component.empty();
			$component.append(result);
		},
		error : function(XMLHttpRequest,textStatus,errorThrown) {
			alert(errorThrown);
		}
	});
}

function empty(v) {
	switch (typeof v) {
	case 'undefined':
		return true;
	case 'string':
		if ($.trim(v).length == 0)
			return true;
		break;
	case 'boolean':
		if (!v)
			return true;
		break;
	case 'number':
		if (0 === v)
			return true;
		break;
	case 'object':
		if (null === v)
			return true;
		if (undefined !== v.length && v.length == 0)
			return true;
		for ( var k in v) {
			return false;
		}
		return true;
		break;
	}
	return false;
}
/**
 * 表单生成pdf，并下载到本地
 */
function downloadFile(actId,isTabs,formName) {
	var docId = $("#docId").val();
	var pageId = $("#dynamicPageId").val();
	$("#actId").val(actId);
	var formJson;
	if(!formName){
		formJson=$("#groupForm").serialize();
		/*var _selects = new Array();
		$("input[name='_selects']").each(function(){
			_selects.push($(this).val());			
		});
		formJson['_selects']=_selects.join(",");*/
	}else{
		formJson=$("#"+formName).serialize();
	}
	if(isTabs&&parent.hasOwnProperty("addTabs")){
		parent.addTabs({
			url: baseUrl+"document/excute.do?"+formJson,
			title: $("#dynamicPageName").val()+"_打印",
			close:true
		});
	}else{
		$.download(basePath + "document/excute.do",formJson,'post');
	}
}

//Ajax 文件下载 
jQuery.download = function(url, data, method){ 
	//获取url和data 
	if( url && data ){ 
		//data 是 string 或者 array/object 
		data = typeof data == 'string' ? data : jQuery.param(data); 
		//把参数组装成 form的 input 
		var inputs = ''; 
		jQuery.each(data.split('&'), function(){ 
			var pair = this.split('='); 
			inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />'; 
		}); 
		var pageSize = $("[data-pagesize]").val();
		if(pageSize != undefined){
			inputs+='<input type="hidden" name="pageSize" value="'+ pageSize +'" />';
		}
		if(data.indexOf("actId")==-1){
			var actId = $("#actId").val();
			inputs+='<input type="hidden" name="actId" value="'+ actId +'" />';
		}
		//request发送请求 
		jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>').appendTo('body').submit().remove(); 
	}; 
};




