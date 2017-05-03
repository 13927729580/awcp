function initStyle(){
	//发送ajax请求
	$.ajax({
		type:"GET",
		url:"fd/style/getAllByAjax.do",
		async:false,
		//回调函数
		success:function(jsonData){
			var json = eval(jsonData);
			$.each(json,function(idx,item){
				var styleId = $("#styleId option").eq(0).val();
				if(item.pageId==styleId){
					$("#styleId").append("<option value=\""+item.pageId+"\" selected='selected'>"+item.name+"</option>");
				}else{
					$("#styleId").append("<option value=\""+item.pageId+"\">"+item.name+"</option>");
				}
			});
		},
	    error: function (XMLHttpRequest, textStatus, errorThrown) { 
              alert(errorThrown); 
	    }
	});
}

$("#addStyle").click(function(){
	top.dialog({
		id: 'add-Style' + Math.ceil(Math.random()*10000),
		title: '添加样式',
		url: "fd/style/edit.do",
		height:400,
		width:1000,
		onclose: function () {
			if (this.returnValue) {
				var ret= this.returnValue;
				$("#styleId").append("<option value=\""+ret.pageId+"\">"+ret.name+"</option>");
				$("#styleId option[value='"+ret.pageId+"']").attr("selected","selected");
			}
		}, 
	}).showModal(); 
	return false;
});