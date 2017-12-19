/**
 * 
 */

var initPart = [ "component" ];
var commStore = {
	component : {
		url : {
			"1001" : "formdesigner/page/component/input/input.jsp",
			"1002" : "formdesigner/page/component/datetime/datetime.jsp",
			"1003" : "formdesigner/page/component/checkbox/checkbox.jsp",
			"1004" : "formdesigner/page/component/radio/radio.jsp",
			"1005" : "formdesigner/page/component/textArea/textArea.jsp",
			"1006" : "formdesigner/page/component/select/select.jsp",
			"1007" : "formdesigner/page/component/password/password.jsp",
			"1008" : "formdesigner/page/component/column/column.jsp",
			"1009" : "formdesigner/page/component/label/label.jsp",
			"1032" : "formdesigner/page/component/input/input.jsp"
		},
		storeId : "componentJsonArray",
		// 以store结尾 表示传值的类型为仓库 直接从页面组件中获取
		// 不以store结束，表述传值类型为单个对象，用于初始化dialog页面中的组件
		update : {
			componentStore : "componentJsonArray",
			component : "auto"
		},
		add : {
			componentStore : "componentJsonArray",
			modelStore : "modelJsonArray"
		},
		table : [ "name", "componentType", "dataItemCode", "title", "layoutName", "order" ],
		unique : 'pageId',
		href : "name",
		order : "componentOrder",
	},
	layout : {
		url : "formdesigner/page/layout/layoutEdit.jsp",
		storeId : "componentJsonArray",
		// 以store结尾 表示传值的类型为仓库 直接从页面组件中获取
		// 不以store结束，表述传值类型为单个对象，用于初始化dialog页面中的组件
		update : {
			componentStore : "componentJsonArray",
			component : "auto"
		},
		add : {
			componentStore : "componentJsonArray",
			modelStore : "modelJsonArray"
		},
		table : [ "name", "layoutType", "proportion", "order" ],
		unique : 'pageId',
		href : "name",
		order : "layOutOrder",
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

function addRow(objectType, comObject) {
//	alert(1);
	var object = commStore[objectType];
	// 标示符
	var unique = object.unique;
	// table 列
	var table = object.table;
	// 是否是超链接字段
	var href = object.href;
	item = eval("(" + comObject.content + ")");
	var str = "<tr id='" + item[unique] + "'><td><input type='checkbox' name='"
			+ objectType + "' value='" + item[unique] + "'/></td>";
	$.each(table, function(i, t) {
		// 判断是否是href字段，若是加事件，不是else
		if (href == t) {
			var name = (item['componentType']=='1008') ? item['columnName']:item[t];	//对于列组件，则显示列头名称，其他显示名称
			str += "<td><a href='javascript:void(0);' onclick='editComponent(\""
					+ item.componentType
					+ "\",\""
					+ item.dynamicPageId
					+ "\",\""
					+ item[unique]
					+ "\")'>"
					+ name
					+ "</a></td>";
		} else {
			if (item[t] != undefined) {
				if(t=='dataItemCode' && (item['componentType']=='1009' || item['componentType']=='1014')){			//对于label，列表显示文本
					str  += "<td>" + item['title'] + "</td>";
				} else if ( t == 'componentType') {
					str  += "<td>" + pageConstant.getComponentTypes().get(item[t])  + "</td>";
				}else if ( t == 'layoutName') {
					str += "<td><a href='javascript:void(0);' onclick='editLayout(\"layout\",\"update\",\""
						+ item.layoutId
						+ "\",\"null\",\""
						+ item.dynamicPageId
						+ "\")'>"
						+ item[t]
						+ "</a></td>";
				}else {
					str += "<td>" + item[t] + "</td>";
				}
				
			} else {
				if(t=='dataItemCode' && (item['componentType']=='1009' || item['componentType']=='1014')){			//对于label，列表显示文本
					str  += "<td>" + item['title'] + "</td>";
				} else if ( t == 'componentType') {
					str  += "<td>" + pageConstant.getComponentTypes().get(item[t])  + "</td>";
				} else{
					str += "<td/>";
				}
				
			}
		}
	});
	str += "</tr>";
	$("#" + objectType + "t").append(str);
}

function loadComponentTable(dynamicPageId) {
	var url = basePath + "component/getComponentListByPageId.do";
	var objectType = "component";
	$.ajax({
		type : "GET",
		url : url,
		data : "dynamicPageId=" + dynamicPageId + "&pageSize=9999",
		async : false,
		success : function(data) {
			// clear table
			$("#" + objectType + "t").empty();
			$.each(data, function(idx, item) {
				addRow(objectType, item);
			});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(errorThrown);
		}
	});
}

//function addPageComponent(objectType, action, pageId, subType, dynamicPageId,
//		dataSource) {
//	var object = commStore[objectType];
//	var urlStr;
//
//	if (empty(subType)) {
//		alert("请选择组件类型");
//	} else {
//		var url = object.url;
//		urlStr = url[subType];
//	}
//	var postData = {};
//	postData.pageId = pageId;
//	postData.dynamicPageId = dynamicPageId;
//	postData.dataSource = dataSource;
//	top.dialog({
//		id : 'add-dialog' + Math.ceil(Math.random() * 10000),
//		title : '载入中...',
//		url : urlStr,
//		data : postData,
//		width : 600,
//		onclose : function() {
//			if (this.returnValue) {
//				var ret = this.returnValue;
//				// setData(object,ret);
//				// var store=JSON.parse($("#"+object.storeId).val());
//				// addRow(objectType,ret)
//				loadComponentTable(dynamicPageId);
//			}
//		}
//
//	}).showModal();
//	return false;
//}
//
function edit(objectType, action, id, componentType, dynamicPageId) {
	if (empty(objectType)) {
		return false;
	}
	if (empty(action)) {
		return false;
	}
	var dataSourceString = $("#modelJsonArray").val();
	if (action == "add" || action == "update") {
		addPageComponent(objectType, action, id, componentType, dynamicPageId,
				dataSourceString);
	} else if (action == "remove") {
		remove(objectType, dynamicPageId);
	}
}

function remove(objectType, dynamicPageId) {
	var _selects = new Array();
	var loadUrl = "component/getComponentListByPageId.do"
	$(":checkbox[name=" + objectType + "]:checked").each(function() {
		var value = $(this).val();
		_selects.push(value);
	});
	$.ajax({
		url : "component/deleteByAjax.do",
		type : "POST",
		async : false,
		data : {
			_selects : _selects.join(",")
		},
		success : function(ret) {
			if ("1" == ret) {
				$(":checkbox[name=" + objectType + "]:checked").each(
						function() {
							var value = $(this).val();
							// actMap.remove(value);
						});
				// fresh();
				loadComponentTable(dynamicPageId);
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
}

//
//function getComponentShowType(componentType) {
//	var showType = componentType;
//	switch (componentType) {
//	case "1001":
//		showType = "单行文本框";
//		break;
//	case "1002":
//		showType = "日期选择框";
//		break;
//	case "1003":
//		showType = "多选框";
//		break;
//	case "1004":
//		showType = "单选框";
//		break;
//	case "1005":
//		showType = "多行文本框";
//		break;
//	case "1006":
//		showType = "下拉框";
//		break;
//	case "1007":
//		showType = "密码框";
//		break;
//	case "1008":
//		showType = "列框";
//		break;
//	default:
//		break;
//	}
//	return showType;
//}

$("#checkAllComponent").click(function() {
	if ($(this).prop("checked")) {
		$(":checkbox[name='component']").prop("checked", true);
	} else {
		$(":checkbox[name='component']").prop("checked", false);
	}
});