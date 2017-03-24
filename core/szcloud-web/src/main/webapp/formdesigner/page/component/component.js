/**
 * 
 */

var initPart = [ "component", "model", "act" ];
var commStore = {
	component : {
		url : {
			"1001" : "formdesigner/page/component/input.jsp",
			"1002" : "formdesigner/page/component/datetime.jsp",
			"1003" : "formdesigner/page/component/checkbox.jsp",
			"1004" : "formdesigner/page/component/radio.jsp",
			"1005" : "formdesigner/page/component/textArea.jsp",
			"1006" : "formdesigner/page/component/select.jsp",
			"1007" : "formdesigner/page/component/password.jsp"
		},
		storeId : "componentJsonArray",

		// 以store结尾 表示传值的类型为仓库 直接从页面组件中获取
		// 不以store结束，表述传值类型为单个对象，用于初始化dialog页面中的组件
		update : {
			modelStore : "modelJsonArray",
			componentStore : "componentJsonArray",
			component : "auto"
		},
		add : {
			componentStore : "componentJsonArray",
			modelStore : "modelJsonArray"
		},
		table : [ "label", "name", "showType", "dataSourceInView",
				"defaultValue", "order" ],
		unique : 'id',
		href : "label",
		order : "componentOrder",
	},
	validator : {
		url : "formdesigner/page/validator-edit.jsp",
		storeId : "validatorJsonArray",
		update : {
			componentStore : "componentJsonArray",
			validator : "auto",
			componentId : "id"
		},
		add : {
			componentStore : "componentJsonArray",
			componentId : "id"
		},
		table : [ "name", "typeInView", "remark" ],
		unique : "type",
		href : "name"
	},
	model : {
		url : {
			domain : "formdesigner/page/domainModel-edit.jsp",
			custom : "formdesigner/page/customModel-edit.jsp"
		},
		storeId : "modelJsonArray",
		update : {
			model : "auto"
		},
		add : {},
		table : [ "name", "typeInView", "description" ],
		unique : "modelCode",
		href : "name"
	},

	act : {
		url : "formdesigner/page/act-edit.jsp",
		storeId : "actJsonArray",
		update : {
			act : "auto"
		},
		add : {},
		table : [ "name", "typeShow", "iconShow" ],
		unique : "unique",
		href : "name"
	},
	modelItem : {
		url : "formdesigner/page/modelItem-edit.jsp",
		storeId : "modelItemJsonArray",
		update : {
			modelItem : "modelItemJsonArray"
		},
		add : {},
		table : [ "itemName", "dataType", "dataLength", "defaultValue",
				"allowNullInView" ],
		unique : "id",
		href : "itemName"
	},
	style : {
		url : "formdesigner/page/style-edit.jsp",
		storeId : "styleJsonArray",
		update : {
			style : "styleJsonArray"
		},
		add : {},
		table : [ "name", "script", "description" ],
		unique : "unique",
		href : "name"
	}

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

function up(objectType, t) {
	var object = commStore[objectType];
	var storeId = object.storeId;
	var onthis = $("#" + t);
	var getUp = onthis.prev();
	if ($(getUp).has("input").size() == 0) {
		alert("已经是第一个！");
		return;
	}
	// 更改Json中的数据
	var json = JSON.parse($("#" + storeId).val());
	var upPoid = $(getUp).find("input").val();
	var upOrder;
	var currentOrder;
	$.each(json, function(idx, item) {
		if (upPoid == item.poid) {
			upOrder = item.order;
		}
		if (t == item.poid) {
			currentOrder = item.order;
		}
	});
	$.each(json, function(idx, item) {
		if (t == item.poid) {
			json[idx].order = upOrder;
		}
		if (upPoid == item.poid) {
			json[idx].order = currentOrder;
		}
	});
	$("#" + storeId).val(JSON.stringify(json));
	$(onthis).after(getUp);
}

function down(objectType, t) {
	var object = commStore[objectType];
	var storeId = object.storeId;
	var onthis = $("#" + t);
	var getDown = onthis.next();

	if ($(getDown).has("input").size() == 0) {
		alert("已经是最后一个！");
		return;
	}
	// 更改Json中的数据
	var json = JSON.parse($("#" + storeId).val());

	var downPoid = $(getDown).find("input").val();
	var downOrder;
	var currentOrder;
	$.each(json, function(idx, item) {
		if (downPoid == item.poid) {
			downOrder = item.order;
		}
		if (t == item.poid) {
			currentOrder = item.order;
		}
	});
	$.each(json, function(idx, item) {
		if (t == item.poid) {
			json[idx].order = downOrder;
		}
		if (downPoid == item.poid) {
			json[idx].order = currentOrder;
		}
	});
	$("#" + storeId).val(JSON.stringify(json));
	$(getDown).after(onthis);
}

function setData(object, data) {
	var order = $("#" + object.order).val();
	var idStr = object.unique;
	var jsonStr = $("#" + object.storeId).val()
	var json = JSON.parse(jsonStr);
	var jsonData = JSON.parse(data);
	if ($.type(jsonData) === 'array') {
		var arr = new Array();
		$.each(jsonData, function(idx, item) {
			arr.push(item[idStr]);
		});
		$.each(json, function(idx, item) {
			if ($.inArray(item[idStr], arr) > -1) {
				json.splice(idx, 1);
				return false;
			}
		});
		$.each(jsonData, function(idx, item) {
			json.push(item);
		});

	} else {

		// 是否有排序字段
		if (empty(jsonData.order)) {
			jsonData.order = order;
			$("#" + object.order).val(parseInt(order) + 1);
		}
		// 添加时，判断是否已经存在，如果存在，则先删除，再添加(另外一种思路，找到存在的元素，然后通过$.extend进行深度拷贝)

		$.each(json, function(idx, item) {
			if (jsonData[idStr] == item[idStr]) {
				json.splice(idx, 1);
				return false;
			}
		});

		json.push(jsonData);
	}

	var jsonValue = JSON.stringify(json);
	$("#" + object.storeId).val(jsonValue);
}
function freshTable(objectType, store) {
	//alert(objectType);
	var object = commStore[objectType];
	//alert(object);
	// 标示符
	var unique = "id";
	// table 列
	var table = object.table;
	// 是否是超链接字段
	var href = object.href;
	$("#" + objectType + "t").empty();
	if (!empty(object.order)) {
		store.sort(function(a, b) {
			return a.order > b.order ? 1 : -1;
		});
	}
	$
			.each(
					store,
					function(idx, item) {
						var str = "<tr id='" + item[unique]
								+ "'><td><input type='checkbox' name='"
								+ objectType + "' value='" + item[unique]
								+ "'/></td>";

						$
								.each(
										table,
										function(i, t) {
											// 判断是否是href字段，若是加事件，不是else
											if (href == t) {
												if ("model" == objectType) {
													str += "<td><a href='javascript:void(0);' onclick='edit(\""
															+ objectType
															+ "\",\"update\""
															+ ",\""
															+ item[unique]
															+ "\",\""
															+ item.type
															+ "\")'>"
															+ item[t]
															+ "</a></td>";
												} else if ("component" == objectType) {
													str += "<td><a href='javascript:void(0);' onclick='edit(\""
															+ objectType
															+ "\",\"update\""
															+ ",\""
															+ item[unique]
															+ "\",\""
															+ item.type
															+ "\")'>"
															+ item[t]
															+ "</a></td>";
												} else {
													str += "<td><a href='javascript:void(0);' onclick='edit(\""
															+ objectType
															+ "\",\"update\""
															+ ",\""
															+ item[unique]
															+ "\")'>"
															+ item[t]
															+ "</a></td>";
												}

											} else if ("order" == t) {
												str += "<td><a href='javascript:void(0);' onclick='up(\""
														+ objectType
														+ "\",\""
														+ item[unique]
														+ "\")'>上移</a>|<a href='javascript:void(0);' onclick='down(\""
														+ objectType
														+ "\",\""
														+ item[unique]
														+ "\")'>下移</a></td>";
											} else {
												str += "<td>" + item[t]
														+ "</td>";
											}
										});
						str += "</tr>";

						$("#" + objectType + "t").append(str);
					});
	return false;
}
function add(objectType, action, id, subType) {
	var object = commStore[objectType];
	var urlStr;
	if ("component" == objectType) {
		if (empty(subType)) {
			alert("请选择组件类型");
		} else {
			var url = object.url;
			urlStr = url[subType];
		}

	} else if ("model" == objectType) {
		if (empty(subType)) {
			alert("请选择数据源类型");
		} else {
			var url = object.url;
			urlStr = url[subType];
		}
	} else {
		urlStr = object.url;
	}

	var postItems = object[action];
	var postData = {};
	if (!empty(id)) {
		postData[object.unique] = id;
	}
	// 遍历配置中要传的值 判断是否有id字段
	for ( var prop in postItems) {
		if ("Id" == prop.substring(prop.length - 2, prop.length)) {
			postData[prop] = $("#" + postItems[prop]).val();
		} else if ("Store" == prop.substring(prop.length - 5, prop.length)) {
			postData[prop] = JSON.parse($("#" + postItems[prop]).val());
		} else {
			if ("auto" == postItems[prop]) {
				var json = JSON.parse($("#" + object.storeId).val());
				$.each(json, function(idx, item) {
					if (item[object.unique] == id) {
						postData[prop] = item;
						return false;
					}
				});
			}
		}

	}
	dialog({
		// top.dialog({
		id : 'add-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : urlStr,
		data : postData,
		onshow : function() {
			console.log('onshow');
		},
		oniframeload : function() {
			console.log('oniframeload');
		},
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				setData(object, ret);
				var store = JSON.parse($("#" + object.storeId).val());
				freshTable(objectType, store)
			}
		}

	}).showModal();
	return false;

}

function edit(objectType, action, id, componentType) {
	if (empty(objectType)) {
		return false;
	}
	if (empty(action)) {
		return false;
	}
	if (action == "add") {
		add(objectType, action, id, componentType);
	} else if (action == "update") {
		add(objectType, action, id, componentType);
	} else if (action == "remove") {
		remove(objectType);
	}
}

function remove(objectType) {
	var object = commStore[objectType];
	var str = $("#" + object.storeId).val();
	var store;
	if (empty(str)) {
	} else {
		store = JSON.parse(str);
	}
	var idStr = object.unique;
	$("input:checkbox[name=" + objectType + "]:checked").each(function() {
		var value = $(this).val();
		$.each(store, function(idx, item) {
			if (item[idStr] == value) {
				store.splice(idx, 1);
				return false;
			}
		});

	});
	$("#" + object.storeId).val(JSON.stringify(store));
	freshTable(objectType, store);
	return false;
}

var keywords = "MYLayoutAndCom,MYpageJScript,MYpageActScript,MYvalidate,docId,dynamicPageId,workflowId,instanceId,taskId,nodeId,actId,update,dynamicPageName,flowTempleteId,workItemId,entryId,sortId,currentPage,pageSize,orderBy,allowOrderBy,,value,level,code,listPages,pFileName,content,phones,id,lastmodified,authorId,author_group,versions,parent,statelabel,initiator,auditDate,auditUser,auditorNames,state,stateint,lastmodifier,auditorList,recordId,tableName,staticHtmlId,showTotalCount,isLimitPage,pageSize,showReverseNum,reverseNumMode,reverseSortord";
$(function(){
	$.formValidator.initConfig({formID:"componentForm",debug:false,onSuccess:function(){
		
	},onError:function(){}});
	$("#name").formValidator({onShow:"请输入组件名称",onFocus:"至少1个长度",onCorrect:""}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空，请确认"})
		.functionValidator({fun:function(val){
		//var keywordArr=keywords.split(",");
		var flag = true;
		$.ajax({
			type:'post',
			url:basePath+"manage/valateKeyword.do",
			data:{keyword:val},
			success:function(msg){
				if(msg=='0'){
					flag=false;
				}
				if(!flag){
					alertMessage("注意！！！名称与关键字冲突！请点击名称右测“查看关键字按钮”查看系统关键字！");
					flag=true;
				}
			}
			
		});
		
	}});
});

