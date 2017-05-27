/**
 * 获取流程提示信息，并弹出
 */
$(function(){
    var messages = $("#message").val();
	if(messages != null && messages != ''){
		 alertMessage(messages);
		 $("#message").val('');
	}
});

/**
 * 返回
 */
function returnButton(){
	history.go(-1);
};


/**
 * 公文中的“状态”按钮脚本
 */
function taskStatus(){
	var WorkID = parent.frames["main"].$("#WorkID").val();
	var NodeID = parent.frames["main"].$("#FK_Node").val();
	var FlowNo = parent.frames["main"].$("#workflowId").val();
	var FID = parent.frames["main"].$("#FID").val();
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '状态列表',
		url : basePath + "workflow/wf/taskStatus.do?WorkID=" + WorkID +"&NodeID="+NodeID+"&FID="+FID+"&FlowNo="+FlowNo+"&dialog=dialog",
		width : 900,
		height : 600,
		okValue:"确定",
		ok: function (){
			return true;
		}
	}).showModal();
};


/**
 * 公文中的“移交”按钮脚本
 */
function nodeShift(actId,masterDataSource){
	$("#actId").val(actId);
	$("#masterDataSource").val(masterDataSource);
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '状态列表',
		url : basePath + "formdesigner/page/component/userSelect/chosenShiftEmp.jsp",
		width : 900,
		height : 600,
		okValue:"确定",
		ok: function (){
			return true;
		}
	}).showModal();
};


/**
 * 工作挂起
 */
function hungUp(){
	var WorkID = parent.frames["main"].$("#WorkID").val();
	var NodeID = parent.frames["main"].$("#FK_Node").val();
	var FlowNo = parent.frames["main"].$("#workflowId").val();
	var FID = parent.frames["main"].$("#FID").val();
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '工作挂起',
		url : basePath + "document/view.do?dynamicPageId=1699&WorkID=" + WorkID +"&FK_Node="+NodeID+"&FID="+FID+"&FK_Flow="+FlowNo+"&dialog=dialog",
		width : 900,
		height : 600,
		okValue:"确定",
		ok: function (){
			return true;
		}
	}).showModal();
};


/**
 * 公文中的“状态”按钮脚本
 */
function taskStatusMobile(){
	//var basePath = "${basePath}";
	var entryID = $("#EntryID").val();
	var workItemID = $("#WorkItemID").val();
	location.href=basePath + "workflow/wf/taskMobileStatus.do?id=" + workItemID +"&entryID='entryID'" +"&dialog='dialog'";
};


/**
 * 公文中的“状态”按钮脚本
 */
function flowChart(){
	var workID=parent.frames["main"].document.getElementById("WorkID").value;
	var flowID=parent.frames["main"].document.getElementById("workflowId").value;
	var nodeID=parent.frames["main"].document.getElementById("FK_Node").value;

	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '批示意见--对话框',
		url : basePath+"WF/Admin/CCFlowDesigner/Truck.html?FK_Flow="+flowID+"&FK_Node="+nodeID+"&WorkID="+workID,
		width : 1130,
		height : 550
	}).showModal();
};



/**
 * 流程动作--保存
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @returns 
 */ 
function saveFlow(actId, name, master){
	//var actId = $(this).attr("id");
	$("#actId").val(actId);
	//修改公文名称
	var nameJson= "[{\"name\":\"name\",\"value\":\"" + name + "\"}]";
	$("#slectsUserIds").val(nameJson);
	//设置主表数据源的名称,当有多个数据源的时候，需要设置主表的数据源masterDataSource
	if(master != null && master != "")
		$("#masterDataSource").val(master);
	//$("#groupForm").submit();
	
	AJAX(1);
};


/**
 * 流程动作--保存
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @returns 
 */
function saveMobileFlow(actId, name, master){
	//var actId = $(this).attr("id");
	$("#actId").val(actId);
	//修改公文名称
	var nameJson= "[{\"name\":\"name\",\"value\":\"" + name + "\"}]";
	$("#slectsUserIds").val(nameJson);
	//设置主表数据源的名称,当有多个数据源的时候，需要设置主表的数据源masterDataSource
	if(master != null && master != "")
		$("#masterDataSource").val(master);
	//$("#groupForm").submit();
	
	AJAXMobile(1);
};



/**
 * 流程动作--发送
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function sendFlow(actId, name, master, comment){

	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '批示意见--对话框',
		url : basePath+"formdesigner/page/component/userSelect/chosenFlowUsers.jsp?actId=" + actId + "&name=" +name  + "&master=" +master + "&comment=" +comment,
		width : 1130,
		height : 550
	}).showModal();
};

/**
 * 流程动作--发送
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function chosenAllUsers(actId, name, master, comment){

	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '批示意见--对话框',
		url : basePath+"formdesigner/page/component/userSelect/chosenFlowUsers.jsp?actId=" + actId + "&roleId=108&name=" +name  + "&master=" +master + "&comment=" +comment,
		width : 1130,
		height : 550
	}).showModal();
};

function chosenMobileGroupUsers(actId, name, master, comment){
	$("#sendMobileFlowName").val(name);
    $("#suggestion").val(comment);
    $("#actId").val(actId);
    $("#masterDataSource").val(master);
    $("#actId").val(actId);
    selectUser("selectUserId","deliver",101);
};


/**
 * 流程动作--加签
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function askForFlow(actId, name, master, comment){
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '批示意见--对话框',
		url : basePath+"formdesigner/page/component/userSelect/chosenFlowUsers.jsp?actId=" + actId + "&name=" +name  + "&master=" +master + "&comment=" +comment,
		width : 1130,
		height : 550
	}).showModal();
};



/**
 * 流程动作--发送
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function sendMobileFlow(actId, name, master, comment){
	$("#sendMobileFlowName").val(name);
    $("#suggestion").val(comment);
    $("#actId").val(actId);
    $("#masterDataSource").val(master);
    $("#actId").val(actId);
    selectUser("selectUserId","deliver");
}



/**
 * 流程动作--发送(工作安排,不弹窗选人)
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
*/
function sendFlowByWork(actId, name, master, comment,userId){
	//var basePath = ${basePath};
	$.ajax({
		type: "POST",
		url: basePath + "workflow/wf/selectDailog.do?type=" + 1,
		data:"",
		async : false,
		success: function(data){
			var result = data;
			if(result != null && result.length > 1){
				top.dialog({
					id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
					title : '批示意见--对话框',
					content: result,
					width : 1000,
					height : 300,
					okValue:"确定",
					ok: function () {
						$("#actId").val(actId);
						var nameJson= eval("({\"name\":\"name\",\"value\":\"" + name + "\"})");
						var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
						$("#slectsUserIds").val(userId);
						var json = top.$("#groupForm1").serializeArray();
						
						json.push(nameJson);json.push(commentJson);
						if(master != null && master != "")
							$("#masterDataSource").val(master);
						$("#slectsUserIds").val(JSON.stringify(json));
						//$("#groupForm").submit();
						AJAX(2);
						//return false;
					},
					cancelValue: '取消',
					cancel: function () {
						return true;
					}
				}).showModal();
			}
			
		   else if(result.length == 1){
		   
		        parent.frames["main"].document.getElementById("actId").value =actId;
		        parent.frames["main"].document.getElementById("masterDataSource").value =master;
				var nameJson= eval("({\"name\":\"name\",\"value\":\"" +name+ "\"})");
				var commentJson= eval("({\"name\":\"comment\",\"value\":\"" +comment+ "\"})");
				var activityJson= eval("[{\"name\":\"activity" +result+ "\",\"value\":\"" + userId + "\"}]");
				activityJson.push(nameJson);
				activityJson.push(commentJson);
				parent.frames["main"].document.getElementById("slectsUserIds").value = JSON.stringify(activityJson);
				AJAX(2);
			}
			
			else{	
				$("#actId").val(actId);
				if(master != null && master != "")
					$("#masterDataSource").val(master);
				var nameJson= eval("[{\"name\":\"name\",\"value\":\"" + name + "\"}]");
				//var json = top.$("#groupForm1").serializeArray();
				//json.push(nameJson);
				var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
				nameJson.push(commentJson);
				$("#slectsUserIds").val(JSON.stringify(nameJson));
				//$("#groupForm").submit();
				AJAX(2);
			}
		}
	});
}


/**
 * 流程动作--转发
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function  transmitFlow(actId, name, master, comment){
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '批示意见--对话框',
		url : basePath+"formdesigner/page/component/userSelect/chosenFlowUsers.jsp?actId=" + actId + "&name=" +name  + "&master=" +master + "&comment=" +comment,
		width : 1130,
		height : 550
	}).showModal();
	//var basePath = ${basePath};
	//var actId = $(this).attr("id");
	/*
	 * 
	$.ajax({
		type: "POST",
		url: basePath + "workflow/wf/selectDailog.do?type=" + 2,
		data:"",
		async : false,
		success: function(data){
			var result = data;
			if(result != null && result.length > 1){
				top.dialog({
					id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
					title : '批示意见--对话框',
					content: result,
					width : 1000,
					height : 300,
					okValue:"确定",
					ok: function () {	
						$("#actId").val(actId);
						var nameJson= eval("({\"name\":\"name\",\"value\":\"" + name + "\"})");
						var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
						var json = top.$("#groupForm1").serializeArray();
						json.push(nameJson);json.push(commentJson);
						if(master != null && master != "")
							$("#masterDataSource").val(master);
						$("#slectsUserIds").val(JSON.stringify(json));
						//$("#groupForm").submit();
						AJAX(2);
						//return false;
					},
					cancelValue: '取消',
					cancel: function () {
						return true;
					}
				}).showModal();
			}
			else if(result.length == 1){
				//if(master != null && master != "")
					//$("#masterDataSource").val(master);
				top.dialog({
					id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
					title : '批示意见--对话框',
					url : basePath+"formdesigner/page/component/userSelect/chosenFlowUsers.jsp?actId=" + actId + "&data=" +result + "&name=" +name  + "&master=" +master + "&comment=" +comment,
					width : 1130,
					height : 520
				}).showModal();
			}
			else{
				$("#actId").val(actId);
				if(master != null && master != "")
					$("#masterDataSource").val(master);
				var nameJson= eval("[{\"name\":\"name\",\"value\":\"" + name + "\"}]");
				//var json = top.$("#groupForm1").serializeArray();
				//json.push(nameJson);
				var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
				nameJson.push(commentJson);
				$("#slectsUserIds").val(JSON.stringify(nameJson));
				//$("#groupForm").submit();
				AJAX(2);
			}
		}
	});
	 */
}



/**
 * 流程动作--转发
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function  transmitMobileFlow(actId, name, master, comment){
	 $("#sendMobileFlowName").val(name);
     $("#suggestion").val(comment);
     $("#actId").val(actId);
     $("#masterDataSource").val(master);
	 $("#actId").val(actId);
	 selectUser("selectUserId","GuDing");
}


/**
 * 流程动作--传阅
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function circulationFlow(actId, name, master, comment){

						$("#actId").val(actId);
						//var json = top.$("#groupForm1").serializeArray();
						if(master != null && master != "")
						{
							$("#masterDataSource").val(master);
						}
						AJAX(2);
}

/**
 * 流程动作--抄送/已阅
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function copyToFlows(actId, name, master, comment){
	
	$("#actId").val(actId);
	
	if(master != null && master != "")
		$("#masterDataSource").val(master);
	var form = document.getElementById("groupForm");
	if(form == null)
	    form = parent.frames["main"].document.getElementById("groupForm");
	var nameJson= eval("({\"name\":\"name\",\"value\":\"" + name + "\"})");
	var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
	var json = $(form).serializeArray();
	json.push(nameJson);json.push(commentJson);
	$("#suggestion").val(comment);
	$("#slectsUserIds").val(JSON.stringify(json));
	$.ajax({
		type: "POST",
		url: basePath + "workflow/wf/excute.do",
		data: $(form).serialize(),
		async : false,
		success: function(data){
			parent.frames["main"].location.href = basePath + "workflow/wf/listPersonalTasks.do";
		}
	});
}
/**
 * 流程动作--抄送/已阅
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function copyToMobileFlows(actId, name, master, comment){
	
	$("#actId").val(actId);
	
	if(master != null && master != "")
	{
		$("#masterDataSource").val(master);
	}
	var form = document.getElementById("groupForm");
	if(form == null)
	    form = parent.frames["main"].document.getElementById("groupForm");
	var nameJson= eval("({\"name\":\"name\",\"value\":\"" + name + "\"})");
	var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
	var json = $(form).serializeArray();
	json.push(nameJson);json.push(commentJson);
	$("#suggestion").val(comment);
	$("#slectsUserIds").val(JSON.stringify(json));
	$.ajax({
		type: "POST",
		url: basePath + "workflow/wf/excute.do",
		data: $(form).serialize(),
		async : false,
		success: function(data){
			location.href = basePath + "oa/app/getEntryList.do";
		}
	});
}

/**
 * 流程动作--流程跳转
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function jumpFlows(actId, toNode, master, comment,defaultPerson){
	if(defaultPerson){
		$("#actId").val(actId);
		$("#suggestion").val(comment);
		$("#toNode").val(toNode);
		//var json = top.$("#groupForm1").serializeArray();
		if(master != null && master != "")
		{
			$("#masterDataSource").val(master);
		}
		AJAX(2);
	}else{
		top.dialog({
			id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
			title : '批示意见--对话框',
			url : basePath+"formdesigner/page/component/userSelect/chosenFlowUsers.jsp?actId=" + actId + "&toNode=" +toNode + "&master=" +master + "&comment=" +comment,
			width : 1130,
			height : 550
		}).showModal();
	}
}

/**
 * 流程动作--传阅
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function circulationFlows(actId, name, master, comment){

	$("#actId").val(actId);
	
	if(master != null && master != "")
	{
		$("#masterDataSource").val(master);
	}
	var form = document.getElementById("groupForm");
	if(form == null)
	    form = document.getElementById("groupForm");
	$.ajax({
		type: "POST",
		url: basePath + "workflow/wf/excute.do",
		data: $(form).serialize(),
		async : false,
		success: function(data){
			var dataJson = data;
			if(data.act=='1')
			{
				var d = top.dialog({
				id:'workFlowTest',
				title: '操作提示',
				width:'630',
				height:'auto',
				content: data.message,
				cancelValue: '关闭',
				cancel: function () {
					return false;	
				},
				cancel: false,
				button: [
						{
							value: '关闭',
							callback: function()
							{
								window.parent.location.href = basePath +"workflow/wf/listPersonalTasks.do";
							}
						}
					]
				});
			
				d.showModal();
				
			}else
			{
			top.dialog({
				id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
				title : '提示框',
				content: dataJson.message,
				width : 350,
				height : 50,
				cancel: false,
				okValue:"确定",
				ok: function () {
					if(dataJson.success == true){
					    document.getElementById("docId").value=dataJson.docId;
						if(type == 1){
							//保存意见数据
							saveText(dataJson,type);
							//保存正文数据
							saveMainText(dataJson.docId,type);
							return true;
						}else if(type == 2){
							saveText(dataJson,type);
							saveMainText(dataJson.docId,type);
							return true;
						}
					}else{
					}
				}
			}).showModal();
			}
		}
	});
}



/**
 * 流程动作--传阅
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function circulationMobileFlow(actId, name, master, comment){
	//var basePath = ${basePath};
	//var actId = $(this).attr("id");
	$.ajax({
		type: "POST",
		url: basePath + "workflow/wf/selectDailog.do?type=" + 3,
		data:"",
		async : false,
		success: function(data){
			var result = data;
			if(result != null && result.length > 1){
			
				$("#sendMobileFlowName").val(name);
        $("#suggestion").val(comment);
        $("#actId").val(actId);
        $("#masterDataSource").val(master);
        $("#actId").val(actId);
        selectUser("selectUserId","GuDing");
			}
			else if(result.length == 1){
				  //if(master != null && master != "")
          $("#masterDataSource").val(master);
          $("#sendMobileFlowName").val(name);
          $("#suggestion").val(comment);
          $("#actId").val(actId);
          $("#sendMobileFlowResult").val(result);
          selectUser("","deliver");
			}
			else{
				$("#actId").val(actId);
				if(master != null && master != "")
					$("#masterDataSource").val(master);
				var nameJson= eval("[{\"name\":\"name\",\"value\":\"" + name + "\"}]");
				//var json = top.$("#groupForm1").serializeArray();
				//json.push(nameJson);
				var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
				nameJson.push(commentJson);
				$("#slectsUserIds").val(JSON.stringify(nameJson));
				//$("#groupForm").submit();
				AJAXMobile(2);
			}
		}
	});
}

/**
 * 流程动作--办结
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function finishFlow(actId, name, master, comment){
	$("#actId").val(actId);
	var nameJson= eval("({\"name\":\"name\",\"value\":\"" + name + "\"})");
	var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
	var json = top.$("#groupForm1").serializeArray();
	json.push(nameJson);json.push(commentJson);
	if(master != null && master != "")
		$("#masterDataSource").val(master);
	$("#slectsUserIds").val(JSON.stringify(json));
	//$("#groupForm").submit();
	AJAX(2);
}


/**
 * 流程动作--办结
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function finishMobileFlow(actId, name, master, comment){
	$("#actId").val(actId);
	var nameJson= eval("({\"name\":\"name\",\"value\":\"" + name + "\"})");
	var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + comment + "\"})");
	var json = top.$("#groupForm1").serializeArray();
	json.push(nameJson);json.push(commentJson);
	if(master != null && master != "")
		$("#masterDataSource").val(master);
	$("#slectsUserIds").val(JSON.stringify(json));
	//$("#groupForm").submit();
	AJAXMobile(2);
}



/**
 * 流程动作--撤回
 * @param actId ： 保存动作的ID
 * @param comment ： 意见
 * @returns 
 */
function rollbackFlow(actId, comment){
	$("#actId").val(actId);
	//$("#groupForm").submit();
	var commentJson= eval("[{\"name\":\"comment\",\"value\":\"" + comment + "\"}]");
	$("#slectsUserIds").val(JSON.stringify(commentJson));
	AJAX(1);
}


/**
 * 流程动作--撤回
 * @param actId ： 保存动作的ID
 * @param comment ： 意见
 * @returns 
 */
function rollbackMobileFlow(actId, comment){
	$("#actId").val(actId);
	//$("#groupForm").submit();
	var commentJson= eval("[{\"name\":\"comment\",\"value\":\"" + comment + "\"}]");
	$("#slectsUserIds").val(JSON.stringify(commentJson));
	AJAXMobile(1);
}



/**
 * 流程动作--退回
 * @param actId ： 保存动作的ID
 * @param users ： 公文名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function gobackFlow(actId, master, comment,toNode){
	
			$("#actId").val(actId);
			if(!toNode){
				$("#toNode").val($("#FK_Node").val()-1);
			}else{
				$("#toNode").val(toNode);
			}
			if(master != null && master != "")
				$("#masterDataSource").val(master);
			AJAX(2);
	
}

/**
 * 流程动作--办结
 * @param actId ： 保存动作的ID
 * @param name ： 公文的名称
 * @param master ： 如果有主次数据源，需要制定哪个是主数据源
 * @param comment ：办理 意见
 * @returns 
 */
function gobackMobileFlow(actId, master, comment,toNode){
	$("#actId").val(actId);
	if(!toNode){
		$("#toNode").val($("#FK_Node").val()-1);
	}else{
		$("#toNode").val(toNode);
	}
	if(master != null && master != "")
		$("#masterDataSource").val(master);
	AJAXMobile(2);
}

/**
 * 流程动作--返回
 * @returns 
 */
function goBack(){
	var href = $('#CloudMenu', parent.document).find("a.curSelectedNode").attr("href");

	if(href ==null || href == ""){
		if($("#slectsUserIds").val != null && $("#slectsUserIds").val != "")
			href = "/workflow/wf/templateList.do";
		else
			href = "/oa/deskTop/selectPersonCalendar.do";
	}
	parent.frames["main"].location.href = basePath + href;
	
	//var box = window.parent.document.getElementById("CloudMenu");
	//$(box).find("a.curSelectedNode").attr("href");
}

//流程流转后弹出框

function popFlowInfoDialog(data,type)
{
		var d = top.dialog({
			id:'workFlowTest',
			title: '操作提示',
			width:'630',
			height:'auto',
			content: "操作成功",
			cancelValue: '关闭',
			cancel: function () {
				//location.href = basePath +"workflow/wf/listPersonalTasks.do";		
				return false;	
			},
			cancel: false,
			button: [
					{
						value: '关闭',
						callback: function()
						{
							if(type==2)
								location.href = basePath +"workflow/wf/listPersonalTasks.do";
							else if(type==3)
							    parent.frames["main"].location.href = basePath +"workflow/wf/listPersonalTasks.do";
						}
					}
				]
		});
		
		d.showModal();
}

			function showOtherUser(){
	 			var d = top.dialog({
					title: '用户选择',
					width:'240',
					content: '此页面为用户选择页面',
					okValue: '确定',
					ok: function () {
						alert('您选择了确定');
					},
					cancelValue: '取消',
					cancel: function () {
						alert('您选择了关闭');
					}
				});
				d.showModal();
			}


function AJAX(type){
	var form = document.getElementById("groupForm");
	if(form == null)
	    form = parent.frames["main"].document.getElementById("groupForm");
	$.ajax({
		type: "POST",
		url: basePath + "workflow/wf/excute.do",
		data: $(form).serialize(),
		async : false,
		success: function(data){
			var dataJson = data;
			if(data.act=='1')
			{
				popFlowInfoDialog(data,type);
				
				
			}else
			{
			top.dialog({
				id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
				title : '提示框',
//				content: dataJson.message,//
				content: "操作成功",//
				width : 350,
				height : 50,
				cancel: false,
				okValue:"确定",
				ok: function () {
					if(dataJson.success == true){
					    document.getElementById("docId").value=dataJson.docId;
						if(type == 1){
							//保存意见数据
							saveText(dataJson,type);
							//保存正文数据
							saveMainText(dataJson.docId,type);
							//parent.frames["main"].location.href = basePath + "workflow/wf/openTask.do?WorkItemID=" + dataJson.WorkItemID + "&EntryID=" + dataJson.EntryID + "&flowTaskType=1&workType=1";
							location.reload();
							return true;
						}else if(type == 2){
							saveText(dataJson,type);
							saveMainText(dataJson.docId,type);
							parent.frames["main"].location.href = basePath + "workflow/wf/listHistoryTasks.do";
							//parent.frames["main"].location.reload();
							return true;
						}
						
					}else{
					
					}
				}
			}).showModal();
			}
		}
	});
}


/**
*
*
*/
function AJAXMobile(type){
	var form =$("#groupForm");
	$.ajax({
		type: "POST",
		url: basePath + "workflow/wf/excute.do",
		data: $(form).serialize(),
		async : false,
		success: function(data){
			var dataJson = data;
				
			    $.alert.open(dataJson.message,{确定: '确定'},
                        function(button) {
                            if (!button)
                                $.alert.open('提示框已退出');
                            else
                               
                            if(dataJson.success == true){
                             $("#docId").val(dataJson.docId);
                             if(type == 1){
              
                              location.href = basePath + "workflow/wf/openTask.do?WorkItemID=" + dataJson.WorkItemID + "&EntryID=" + dataJson.EntryID + "&flowTaskType=1&workType=1";
                              return true;
                              }else if(type == 2){
              
                              location.href = basePath + "oa/app/getEntryList.do";
                              return true;
                              }
                          }    
                               
                        }
                    );

		}
	});
}


function DIALOG(message){
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		//title : '提示框',
		content: message,
		okValue:"确定",
		ok: function () {	
			return true;
		}
	}).showModal();
};


/**
 * 保存按鈕的客戶端腳本
 */
function clientSaveButton(DataSource, commentId,element) {
	if ($.formValidator.pageIsValid('1')) {
		var actId = element.attributes["id"].value; 
		var now = new Date();
		var nowStr = now.format("yyyy-MM-dd");
		var name = "项目预售_" + nowStr;
		if (commentId != null && commentId != "") {
			var comment = document.getElementById(commentId).value;
			saveFlow(actId, name, DataSource, comment);
		}else{
			saveFlow(actId, name, DataSource);
		}
	}
};

/**
 * 发送按鈕的客戶端腳本
 */
function clientSendButton(DataSource, commentId,element) {
	if ($.formValidator.pageIsValid('1')) {
		var actId = element.attributes["id"].value;
		var now = new Date();
		var nowStr = now.format("yyyy-MM-dd");
		var name = "项目预售_" + nowStr;
		if (commentId != null && commentId != "") {
			var comment = document.getElementById(commentId).value;
			sendFlow(actId, name, DataSource, comment);
		}else{
			sendFlow(actId, name, DataSource);
		}
	}
};


/**
 * 转发按鈕的客戶端腳本
 */
function clientCirculationButton(DataSource, commentId,element) {
	if ($.formValidator.pageIsValid('1')) {
		var actId = element.attributes["id"].value;
		var now = new Date();
		var nowStr = now.format("yyyy-MM-dd");
		var name = "创标 _" + nowStr;
		if (commentId != null && commentId != "") {
			var comment = parent.frames["main"].document.getElementById(commentId).value;
			circulationFlow(actId, name, DataSource, comment);
		}else{
			circulationFlow(actId, name, DataSource);
		}
	}
};


/**
 * 退回按鈕的客戶端腳本
 */
function clientGobackButton(DataSource, commentId, element) {
	var actId = element.attributes["id"].value;
	var now = new Date();
	var nowStr = now.format("yyyy-MM-dd");
	if (commentId != null && commentId != "") {
		var comment = parent.frames["main"].document.getElementById(commentId).value;
		gobackFlow(actId, DataSource, comment);
	}else{
		gobackFlow(actId, DataSource);
	}
};


/**
 * 流程办结按鈕的客戶端腳本
 */
function clientFinishButton(DataSource, commentId, element) {
	var actId = element.attributes["id"].value;
	var now = new Date();
	var nowStr = now.format("yyyy-MM-dd");
	var name = "开发规划 _" + nowStr;
	if (commentId != null && commentId != "") {
		var comment = parent.frames["main"].document.getElementById(commentId).value;
		finishFlow(actId, name, DataSource, comment);
	}else{
		finishFlow(actId, name, DataSource);
	}
};


/**
 * 加签按鈕的客戶端腳本
 */
function clientAskForButton(DataSource, commentId, element) {
	var actId = element.attributes["id"].value;
	var now = new Date();
	var nowStr = now.format("yyyy-MM-dd");
	var name = "划拨申请_" + nowStr;
	if (commentId != null && commentId != "") {
		var comment = parent.frames["main"].document.getElementById(commentId).value;
		askForFlow(actId, name, DataSource, comment);
	} else {
		askForFlow(actId, name, DataSource);
	}
};
//  不保存到流程 客户端脚本
function clientNoSaveFlowButton(dynamicPageId,element){
var actId = element.attributes["id"].value;
$("#actId").val(actId);
if($.formValidator.pageIsValid('1')){
$.ajax({
	   type: "POST",
	   url: basePath + "document/excuteOnly.do",
	   data:$("#groupForm").serialize(),
	   async : false,
	   success: function(data){
			if(data==1){
			     alert("保存成功");
                             window.location.href=basePath+ "document/view.do?id=&dynamicPageId="+dynamicPageId;             
							 
			}else{
				alert(data);
			}
	   }
});
}
}