var actMap = new Map();
var actView = new Map();
actView.put("actType.2000", "动作");
actView.put("actType.2001", "保存带校验");
actView.put("actType.2002", "返回");
actView.put("actType.2003", "删除");
actView.put("actType.2004", "启动流程");
actView.put("actType.2005", "流程调整");
actView.put("actType.2006", "流程回撤");
actView.put("actType.2007", "编辑审批人");
actView.put("actType.2008", "保存(带流程)");
actView.put("actType.2009", "新增");
actView.put("actType.2010", "更新");
actView.put("actType.2011", "流程发送");
actView.put("actType.2012", "打开");
actView.put("actType.2013", "审批");
actView.put("actType.2014", "Pdf打印");
actView.put("actType.2015", "保存不带校验");
actView.put("actType.2016", "Excel导出");
actView.put("actType.2017", "保存并返回");
actView.put("actType.2018", "流程转发");
actView.put("actType.2019", "流程传阅");
actView.put("actType.2020", "流程图");
actView.put("actType.2021", "流程归档");
actView.put("actType.2022", "流程办结");
actView.put("actType.2023", "流程退回");
actView.put("actType.2024", "加签");
actView.put("actType.2025", "移交");
actView.put("icon.1", "保存图标");
actView.put("icon.2", "返回");
actView.put("icon.3", "删除");

$(document).ready(function() {		
	//按钮样式设置
	var $btnSet = $("#js-btn-set"),					
		$btnSetIcon = $btnSet.find("#btnIcon"),
		$btnSetColor = $btnSet.find("#btnColor");	
	$btnSet.find(".icon-menu a").click(function(){//修改按钮的图标
		var icon = $(this).attr("class");
		$btnSetIcon.find("i").attr("class",icon);
		$btnSetIcon.find("input").val(icon);
	});	
	$btnSet.find(".color-menu span").click(function(){//修改按钮的颜色
		var common = "label label-dot";
		var color = $(this).data("color");
		$btnSetColor.find("span").attr("class",common + " label-" + color);
		$btnSetColor.find("input").val(color);
	});	
	
	//显示按钮的类型
	var actType = $("#actType").val();
	var actName = actView.get("actType." + actType);
	$("#selectActType").html("<option value='" + actType + "'>" + actName + "</option>")
});

// 从库中选择,添加到动态页面中
$("#actStore").click(function() {
top.dialog({
	id : 'act-store-dialog' + Math.ceil(Math.random() * 10000),
	title : '选择动作',
	url : basePath + 'fd/act/list.do?dialog=1&dynamicPageId=' + $("#id").val(),
	width : 800,
	onclose : function() {
		if (this.returnValue) {
			var ret = this.returnValue;
			$.each(ret, function(idx, item) {
				actMap.put(item.pageId, item);
			});
			freshActTable();
		}
	}
	}).showModal();
	return false;
});

// 弹出新增页面动作页面
function act_add(type) {
	top.dialog({
		id : 'add-dialog' + $.uuid(),
		title : '新增动作',
		url : basePath + "fd/act/edit.do?dialog=1&dynamicPageId=" + $("#id").val() + "&order=3&type=" + type,
		height : 540,
		width : 1000,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				actMap.put(ret.pageId, ret);
				freshActTable();
			}
		}
	}).showModal();
	return false;
}

// 编辑页面动作信息
function editAct(id) {// open dialog to update the act info
	top.dialog({
		id : 'act-dialog' + Math.ceil(Math.random() * 10000),
		title : '动作编辑',
		url : basePath + 'fd/act/edit.do?_selects=' + id,
		height : 540,
		width : 1000,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				actMap.put(ret.pageId, ret);
				freshActTable();
				alert("更新成功！");
			}
		}
	}).showModal();
	return false;
}

// ajax to delete act and fresh table data
$("#deleteAct").click(function() {
	var _selects = new Array();
	$(":checkbox[name='act']:checked").each(function() {
		var value = $(this).val();
		_selects.push(value);
	});
	$.ajax({
		url : basePath + "fd/act/deleteByAjax.do",
		type : "POST",
		async : false,
		data : {
			_selects : _selects.join(",")
		},
		success : function(ret) {
			if ("1" == ret) {
				$(":checkbox[name='act']:checked").each(function() {
					var value = $(this).val();
					actMap.remove(value);
				});
				freshActTable();
				alert("删除成功！");
			} else {
				alert("删除失败！");
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
	return false;
});

// 刷新table
function freshActTable() {// refreshActTable act table
	$("#actdatabody").empty();
	actMap.each(function(key, value, index) {
		var str = "<tr id='" + value.pageId
				+ "'><td><input type='checkbox' name='act' value='"
				+ value.pageId + "'/></td>";
		str += "<td><a href='javascript:void(0);' onclick='editAct(\""
				+ value.pageId + "\")'>" + value.name + "</a></td>";
		str += "<td>" + actView.get("actType." + value.actType) + "</td>";
		str += "</tr>";
		$("#actdatabody").append(str);
	});
}

// 全选
$("#checkAllAct").click(function() {
	if ($(this).prop("checked")) {
		$(":checkbox[name='act']").prop("checked", true);
	} else {
		$(":checkbox[name='act']").prop("checked", false);
	}
});

// 批量修改动作类型
$("#batchModifyActType").click(function() {	
	var count = $(":checkbox[name='act']:checked").size();
	if(count < 1){
		alert("请选择");
		return false;
	}	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='act']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/act/comms/batchModifyActType.jsp",
		data : data,
		width : 300,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				initAct();
			}
		}	
	}).showModal();	
	return false;
});

// ajax to init actMap
function initActMap() {
	var pageId = $("#id").val();
	if (!empty(pageId)) {
		$.ajax({
			url : basePath + "fd/act/getByPageId.do?dynamicPageId=" + pageId,
			type : "GET",
			async : false,
			success : function(ret) {
				var json = eval(ret);
				$.each(json, function(idx, item) {
					actMap.put(item.pageId, item);
				});
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(errorThrown);
			}
		});
	}
	return false;
}

function initAct() {
	initActMap();
	freshActTable();
}

$("#contentType").change(function() {
	$("#label-1").hide();
	$("#label-2").hide();
	$("#label-3").hide();
	var label = $(this).val();
	$("#label-" + label).show();
});

$("#ajaxExcute").click(function(){
	var str = '';
	str+='var actId = $(this).attr("id");\n'		
	str+='$("#actId").val(actId.substring(actId.length-36));			//对应按钮ID\n'
	str+='$.ajax({\n'
	str+='	   type: "POST",\n'
	str+='	   url: basePath + "document/excuteOnly.do",	//调用后台.do\n'
		str+='	   data:$("#groupForm").serialize(),			//将整个表单参数传至后台，可自己定义传回去的参数\n'
			str+='	   async : false,\n'
				str+='	   success: function(data){\n'
					str+='		if(data=="1"){							//返回1表示执行成功，这里对应服务器脚本内容\n'
						str+='		alert("执行成功");\n'
						str+='		//var dialog = top.dialog.get(window);	//如果是对应弹窗的窗口，要求执行成功关闭窗口，则执行此处三行代码\n'
							str+='	//dialog.close(1);\n'
						str+='		//dialog.remove();\n'
						str+='	}else{\n'
							str+='	alert(data);			//不成功，则弹窗提示\n'					
					str+='		}\n'
				 str+='	   }\n'
				str+='	});\n'
	
	if($(this).prop("checked")){
		var clientScript = $("#clientScript").val();
		if(clientScript==''){
			$("#clientScript").val(str);
		}
	}else{
	
	}

});
	
$("#ajaxView").click(function(){
	var str = '';
	str+='var basePath = "${basePath}";\n'
	str+='var _selects = "";\n'
		str+='$("input[name=\'_selects\']").each(function(){\n'
			str+='_selects = _selects+$(this).val()+",";\n'
				str+='});\n'
					str+='if(_selects == null || _selects == ""){\n'
						str+=' alert("请先选择数据");\n'
						str+='return false;\n'
							str+='}\n'
								str+='var data = $("#createForm").serialize();\n'
									str+='data._selects = _selects;\n'
										str+='top.dialog({\n'
											str+='id : "edit-dialog" + Math.ceil(Math.random() * 10000),\n'
											str+='title : "载入中...",\n'
											str+='url : basePath + "document/view.do?dynamicPageId=1",	//弹出页面链接\n'
												str+='data : data,\n'
													str+='width : 600,\n'
														str+='onclose : function() {\n'
															str+='	if (this.returnValue) {					//弹框关闭后传回的值\n'
																str+='		var ret = this.returnValue;\n'
																	str+='	$("#groupForm").submit();			//刷新页面\n'
																		str+='	}\n'
																			str+='}\n'

																				str+='}).showModal();\n'
																					str+='return false;\n'
	
	if($(this).prop("checked")){
		var clientScript = $("#clientScript").val();
		if(clientScript==''){
			$("#clientScript").val(str);
		}
	}else{
	
	}
});