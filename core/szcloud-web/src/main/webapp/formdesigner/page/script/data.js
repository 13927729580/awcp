
var dataSource=new Map();
var itemMap=new Map();
var sqlTableName="";
function initDataSource(){
	var model=$("#modelJsonArray").val();
	if(!empty(model)){
		var dataJson = JSON.parse(model);
		$.each(dataJson,function(idx,item){
			dataSource.put(item.id,item);
		});
	}
	freshDataTable();
}

$("#checkAllDatasource").click(function(){
	if($(this).prop("checked")){
		$(":checkbox[name='data']").prop("checked",true);
	}else{
		$(":checkbox[name='data']").prop("checked",false);
	}
	
});
function  freshSQL(){
	sqlTableName = models.get($("#modelCode").val());
	var sql="";
	if(!itemMap.isEmpty()){
		sql+="\"SELECT  "; 
		itemMap.each(function(key,value,index){
			if(value){
				sql+=key+",";
			}
		});
		if(","==sql.charAt(sql.length-1)){
			sql=sql.substring(0,sql.length-1);
		}
		sql+="  FROM  ";
		sql+=sqlTableName;
		sql+="\";";
	}
	$("#sqlScript").val(sql);
}
function freshDataTable(){//refreshActTable act table
	$("#datasourcebody").empty();
	dataSource.each(function(key,value,index){
		var str="<tr id='"+value.id+"'><td><input type='checkbox' name='"+"data"+"' value='"+value.id+"'/></td>";
		str+="<td><a href='javascript:void(0);' onclick='editDatasource(\""+value.id+"\")'>"+value.name+"</a></td>";
		str+="<td>"+value.description+"</td>";
		str+="</tr>";
		$("#datasourcebody").append(str);
	});
	return false;
}


function editDatasource(id){
	var to = null;
	if(!empty(id)){
		to=dataSource.get(id);
	}
	var pageId = $("#id").val();
	top.dialog({
		id: 'add-dataSource' + Math.ceil(Math.random()*10000),
		title: '数据源编辑',
		url: "fd/datadefine/edit.do",
		data:to,
		height : 600,
		width : 1000,
		onclose: function () {
			if (this.returnValue) {
				var ret= this.returnValue;
					dataSource.put(ret.id,ret);
					$("#modelJsonArray").val(dataSource.toJSON());
					$.ajax({
						url:"fd/updateData.do",
						async : false,
						type:"POST",
						data:{dataJson:dataSource.toJSON(),id:pageId},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							alert(errorThrown);
						},
						success:function(ret){
							if(ret=="1"){
								freshDataTable();
							}else{
								alert("出现异常！请重新操作！");
							}
							
						}
					});
				}
		}, 
	}).showModal(); 
	return false;
}

$("#deleteModel").click(function (){
	$(":checkbox[name='data']:checked").each(function(idx,item){
		var id = $(this).val();
		dataSource.remove(id);
	});
	var pageId = $("#id").val();
	$("#modelJsonArray").val(dataSource.toJSON());
	$.ajax({
		url:"fd/updateData.do",
		async : false,
		type:"POST",
		data:{dataJson:dataSource.toJSON(),id:pageId},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		},
		success:function(ret){
			if(ret=="1"){
				freshDataTable();
			}else{
				alert("出现异常！请重新操作");
			}
		}
	});
	return false;
});


$("#copyModel").click(function (){
	var count = $(":checkbox[name='data']:checked").size();
	var _selects=new Array();
	$(":checkbox[name='data']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	var pageId = $("#id").val();
	if(count < 1){
		alert("请选择数据源！");
		return false;
	}else if(count==1){
		top.dialog({
			id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
			title : '别名',
			url : basePath + "formdesigner/page/data/copyModel.jsp",
			width : 300,
			onclose : function() {
					var ret;
					if (this.returnValue) {
						 ret = this.returnValue;
					}
					var dataModel = dataSource.get(_selects[0]);
					var copy={};
					for(var s in dataModel){
						copy[s]=dataModel[s];
					}
					if(!empty(ret)){
						copy.name=ret;
					}
					copy.id=guidGenerator();
					dataSource.put(copy.id,copy);
					$("#modelJsonArray").val(dataSource.toJSON());
					$.ajax({
						url:"fd/updateData.do",
						async : false,
						type:"POST",
						data:{dataJson:dataSource.toJSON(),id:pageId},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							alert(errorThrown);
						},
						success:function(ret){
							if(ret=="1"){
								freshDataTable();
							}else{
								alert("出现异常！请重新操作");
							}
						}
					});
				
			}
		
		}).showModal();
	}else {
		$(":checkbox[name='data']:checked").each(function(idx,item){
			var id = $(this).val();
			var temp = dataSource.get(id);
			var copy={};
			for(var s in temp){
				copy[s]=temp[s];
			}
			copy.id=guidGenerator();
			dataSource.put(copy.id,copy);
		});
		$("#modelJsonArray").val(dataSource.toJSON());
		$.ajax({
			url:"fd/updateData.do",
			async : false,
			type:"POST",
			data:{dataJson:dataSource.toJSON(),id:pageId},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			},
			success:function(ret){
				if(ret=="1"){
					freshDataTable();
				}else{
					alert("出现异常！请重新操作");
				}
			}
		});
	}
	return false;
});


$("#copyOverPage").click(function (){
	var count = $(":checkbox[name='data']:checked").size();
	var pageId = $("#id").val();
	if(count < 1){
		alert("请选择数据源！");
		return false;
	}else {
		top.dialog({
			id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
			title : '别名',
			url : basePath + "formdesigner/page/data/copyModel.jsp",
			width : 300,
			onclose : function() {
					var ret;
					if (this.returnValue) {
						 ret = this.returnValue;
					}
					var dataJson = new Map();
					$(":checkbox[name='data']:checked").each(function(idx,item){
						var id = $(this).val();
						dataJson.put(id,dataSource.get(id));
					});
					$.ajax({
						url:"fd/copyDataOverPage.do",
						async : false,
						type:"POST",
						data:{dataJson:dataSource.toJSON(),targetId:ret.pageId},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							alert(errorThrown);
						},
						success:function(ret){
							if(ret=="1"){
								freshDataTable();
							}else{
								alert("出现异常！请重新操作");
							}
						}
					});
				
			}
		
		}).showModal();
	}
	return false;
});




$("#freshModel").click(function (){
	var _selects=new Array();
	var count = $(":checkbox[name='data']:checked").size();
	if(count<1){
		alert("请选择数据源！");
		return false;
	}
	$(":checkbox[name='data']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	var pageId = $("#id").val();
	$.ajax({
		url:"fd/datadefine/freshModel.do",
		async : false,
		type:"POST",
		data:{_selects:_selects.join(","),pageId:pageId},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		},
		success:function(ret){
			$(":checkbox[name='data']").prop("checked",false);
			$("#checkAllDatasource").prop("checked",false);
			if(ret=="0"){
				alert("出现异常！请重新操作");
			}else{
				alert("刷新成功！");
				if(!empty(ret)){
					$.each(ret,function(idx,item){
						dataSource.put(item.id,item);
					});
					$("#modelJsonArray").val(dataSource.toJSON());
				}
				
			}
		}
	});
	return false;
});


