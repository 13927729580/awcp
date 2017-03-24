	$(function(){
	var pageId = $("#id").val();
	if(pageId == ''){
		return false;
	}
	$.ajax({
		type:"post",
		url:basePath+"fd/workflow/getBindNodeList.do?pageId="+pageId,
		async:false,
		dataType:"json",
		success:function(data){
			$("#noteList").empty();
			$.each(data,function(index,item){
			
			   $("#noteList").append(""+
			   	"<tr id='nodetable_tr"+item.id+"_"+item.workflowId+"'>"+
				 "<td class='hidden formData'>"+
					"<input id='nodeIds' type='hidden' value='"+item.id+"_"+item.workflowId+"'></td>"+
				 "<td>"+item.name+"</td>"+
				 "<td>"+item.workflowName+"</td>"+
			   "</tr>"+
			   
			   "");
			
			});
		}
	})
	});
	
	$(function(){
		  var count=0;//默认选择行数为0
		  $('#nodetable').datatable({
			  checkable: true,
			  storage:false,
			  checksChanged:function(event){
				  this.$table.find("tbody tr").find("input#nodeIds").removeAttr("name");
				  var checkArray = event.checks.checks;
				  count = checkArray.length;//checkbox checked数量
				  for(var i=0;i<count;i++){//给隐藏数据机上name属性
					  var tr = document.getElementById(checkArray[i]);
					  $(tr).find("input#nodeIds").attr("name","nodeIds");
					  //this.$table.find("tbody tr#"+checkArray[i]).find("input#nodeIds").attr("name","nodeIds");
				  }
			  }
				
		  });
    	//add
		 $("#addWorkflowBtn").click(function(){	
			//动态页面id
			var pageId = $("#id").val();
			if(pageId != ''){
				top.dialog({ 	
					id: 'add-workflow-node-list-dialog' + Math.ceil(Math.random()*10000),
					title: '新增结点',
					url:"fd/workflow/notBindNodeList.do?pageId="+pageId,
					width:780,
					onclose: function () {
						if(this.returnValue){
							if(this.returnValue.result == '1'){
								reCreateTable(this.returnValue.msg);
							} else {
								alert(this.returnValue.msg);
							}
						}
					}
				}).showModal();
			} else {
				alert("请打开动态页面");
			}
			return false;
		});
		//delete
    	$("#deleteWorkflowBtn").click(function(){
    		if(count<1){
    			alert("请至少选择一项操作");
    			return false;
    		}
    		if(!window.confirm("确定删除？")){
    			return false;
    		}
			var check_array=$("input#nodeIds[name='nodeIds']");	
    		var nodeIds = "";
    		var pageId = $("#id").val();
			for(var i = 0; i< check_array.length;i++){
				if(undefined != check_array[i].value || "" !=check_array[i].value)
					nodeIds=nodeIds+check_array[i].value+",";
			}
			nodeIds = nodeIds.substr(0, nodeIds.length-1);
			if(nodeIds != ""){
	     		$.ajax({
	 				type : "POST",
					url : "fd/workflow/delNode.do",
	 				data : {"nodeIds":nodeIds,"pageId":pageId},
	 				success : function(data) { 					
	 					if(data.result == '1'){
							reCreateTable(data.msg);
						} else {
							alert(data.msg);
						}
	 				}
	 			});
			}
			return false;
    	});
    	//config
    	$("#configWorkflowBtn").click(function(){
    		if(count != 1){
    			alert("请选择一个节点操作");
    			return false;
    		}
    		
    		var check_array=$("input#nodeIds[name='nodeIds']");	
    		var nodeIds = check_array[0].value;
    		var pageId = $("#id").val();
    		
    		top.dialog({ 	
				id: 'config-workflow-node-vars-dialog' + Math.ceil(Math.random()*10000),
				title: '配置结点参数',
				url:"fd/workflow/nodeVariableEdit.do?nodeId="+nodeIds + "&pageId=" + pageId,
				width:780,
				data : {"dataSource" : $("#modelJsonArray").val()},//数据源信息带上
				onclose: function () {
					if(this.returnValue){
						//TODO 
						if(this.returnValue.result == '1'){
							reCreateTable(this.returnValue.msg);
						} else {
							alert(this.returnValue.msg);
						}
					}
				}
			}).showModal();
    		return false;
    	});
    	
    	
    //配置权限组页面
	$("#authorityWorkflowBtn").click(function(){
    		if(count != 1){
    			alert("请选择一组操作");
    			return false;
    		}
    		var check_array=$("input#nodeIds[name='nodeIds']");	
    		var nodeIds = check_array[0].value;
    		var pageId = $("#id").val();
    		var urlStr="formdesigner/page/workflow/workflow-authority-add.jsp";
		    var postData={};
		    postData.pageId=pageId;
		    postData.nodeIds=nodeIds;
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '载入中...',
			url: urlStr,
			data:postData,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					alert("保存成功");
				}
			}
			
		}).showModal();
		return false;
    	});
    	
    });
	
	/**
	 * 动态创建列表
	 * @param temp
	 */
	function reCreateTable(temp){
		if(temp==undefined)
			return false;
		var nodeHtm = "";
		temp = JSON.parse(temp);
		for(var i = 0; i < temp.length; i++){
			nodeHtm = nodeHtm+'<tr id="nodetable_tr'+temp[i].id+'_'+temp[i].workflowId +'">'+
				'<td class="hidden formData"><input id="nodeIds" type="hidden" value="'+temp[i].id+'_'+temp[i].workflowId +'"></td>'+
				'<td>'+temp[i].name+'</td>'+
				'<td>'+temp[i].workflowName+'</td>'+
			    '</tr>';
		}
		$("#nodetable tbody").html(nodeHtm);
		$("#nodetable").datatable("load");
	}