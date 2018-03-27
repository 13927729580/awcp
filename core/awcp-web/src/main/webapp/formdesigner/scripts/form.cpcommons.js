/**
 * 组件的公共脚本方法；
 */

/**
 * 新增 or 修改组件信息
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function editComponent(componentType, dynamicPageId, componentId){
	if((componentId != null && componentId.trim() != '') || (dynamicPageId != null && dynamicPageId.trim() != '' && componentType != null && componentType.trim() != '')){
		var data = {"pageId" : componentId, "componentType" : componentType, "dynamicPageId" : dynamicPageId, "dataSource" : $("#modelJsonArray").val()};
		var url = "";
		if(componentId != null && componentId.trim() != ''){
			url += "componentId="+componentId;
		} 
		
		if(dynamicPageId != null && dynamicPageId.trim() != ''){
			url += url.length > 0 ? "&" : "";
			url += "dynamicPageId="+dynamicPageId;
		} 
		
		if(componentType != null && componentType.trim() != ''){
			url += url.length > 0 ? "&" : "";
			url += "componentType="+componentType;
		} 
		
		top.dialog({
			id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
			title : '载入中...',
			url : basePath + "component/toedit.do?"+url,
			data : data,
			width : 600,
			onclose : function() {
				if (this.returnValue) {
					var ret = this.returnValue;
					//loadComponentTable(dynamicPageId);
					loadComByCondition();
				}
			}

		}).showModal();
	}
	return false;
}

function preEditComponent(componentType, dynamicPageId, componentId){
	if((componentId != null && componentId.trim() != '') || (dynamicPageId != null && dynamicPageId.trim() != '' && componentType != null && componentType.trim() != '')){
		var data = {"pageId" : componentId, "componentType" : componentType, "dynamicPageId" : dynamicPageId, "dataSource" : $("#modelJsonArray").val()};
		var url = "";
		if(componentId != null && componentId.trim() != ''){
			url += "componentId="+componentId;
		} 
		
		if(dynamicPageId != null && dynamicPageId.trim() != ''){
			url += url.length > 0 ? "&" : "";
			url += "dynamicPageId="+dynamicPageId;
		} 
		
		if(componentType != null && componentType.trim() != ''){
			url += url.length > 0 ? "&" : "";
			url += "componentType="+componentType;
		} 
		
		top.dialog({
			id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
			title : '载入中...',
			url : basePath + "component/toedit.do?"+url,
			data : data,
			width : 600,
			onclose : function() {
				if (this.returnValue) {
					var ret = this.returnValue;
					//loadComponentTable(dynamicPageId);
					//loadComByCondition();
				}
			}

		}).showModal();
	}
	return false;
}

function helpComponent(cType,cName){
	if(cType != null && cType.trim() != ''){
		var helpUrl = basePath + "help/dynamicpage/component/"+cType+".html";
		top.dialog({
			id : 'help-dialog' + Math.ceil(Math.random() * 10000),
			title : cName + '使用帮助',
			url : helpUrl,
			width : 600,
			height : 450,
			onclose : function() {
				
			}

		}).showModal();
	}
	return false;
}

function modifiSameType(){
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId, "_selects" : _selects.join(",")};
	
	var url = "dynamicPageId="+dynamicPageId+"&_selects="+_selects.join(",");
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "component/toedits.do?"+url,
		data : data,
		width : 600,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}

	}).showModal();
	
	return false;
}

/**
 * 校验组件名称是否含有关键字
 * @param name
 * @returns {Boolean}
 */
function validateKeywords(name){
	var keywordArr = keywords.split(",");//keywords为是保存了关键字的字符串，在components.js中的全局变量
	var flag = true;
	for(var i=0;i<keywordArr.length;i++){
		if(keywordArr[i]==name){
			alert("注意！！！名称与关键字冲突！");
			flag=false;
			break;
		}
	}
	return flag;
}

function validateCheckOut(dynamicPageId){
	var flag;
	$.ajax({
		type:"POST",
		url:"fd/validateCheckOut.do?id=" + dynamicPageId,
		
		async:false,
		dataType:'json',
		//回调函数
		success:function(result){
			if(result.value == 1){
				flag = true;
			}else{
				alert(result);
				flag = false;
			}	   	
		},
  	 	 error: function (XMLHttpRequest, textStatus, errorThrown) { 
   	       alert(errorThrown); 
   	       flag = false;
   	 	 }
	});
	return flag;
}

/**
 * 批量修改数据源
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function modifiDataItemCode(){
	
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId, "dataSource" : $("#modelJsonArray").val(), "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/quickEditDateItem.jsp",
		data : data,
		width : 300,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 批量修改数据源
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function modifiDataAlias(){
	
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId, "dataSource" : $("#modelJsonArray").val(), "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/quickEditDateAlias.jsp",
		data : data,
		width : 300,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 批量修改布局
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function batchModifiLayout(){
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/batchModifyLayout.jsp",
		data : data,
		width : 300,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 批量修改样式
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function batchModifiStyle(){
	
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/batchModifyStyle.jsp",
		data : data,
		width : 300,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 批量修改打印行高
 * 
 */
function modifiPrintHeight(){
	
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/batchModifyPrintHeight.jsp",
		data : data,
		width : 300,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 批量修改组件类型
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function modifiComponentType(){
	
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/batchModifyComponentType.jsp",
		data : data,
		width : 300,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 批量修改组件校验
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function modifiValidator(){
	
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/batchModifyValidator.jsp",
		data : data,
		width : 300,
		height: 1200,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 批量修改组件打印样式
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function modifiValidator(){
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/batchModifyPrintStyle.jsp",
		data : data,
		width : 300,
		height: 1200,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 批量修改是否允许为空
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function batchModifiAllowNull(allow){
	
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	var objectType = "component";
	var dynamicPageId = $("#id").val();
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	$.ajax({
		url:basePath + "component/batchModifiAllowNull.do?_selects=" + _selects.join(",") + "&allowNull=" + allow,
		type:"GET",
		async:false,
	//	data:{_selects:_selects.join(",")},
		success:function(ret){
			if("1"==ret){
				$(":checkbox[name="+objectType+"]:checked").each(function(){
					var value=$(this).val();
					//	actMap.remove(value);
				});
				//fresh();
				$(":checkbox[name='checkAllComponent']").prop("checked",false);
				loadComponentTable(dynamicPageId);
				alert("修改成功！");
			}else{
				alert("修改失败");
			}
			
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) { 
			alert(errorThrown); 
		}
	});
	return false;
}

/**
 * 批量修改，将勾选的组件取到同页面批量修改
 * @param componentType
 * @param dynamicPageId
 * @param componentId
 * @returns {Boolean}
 */
function batchModifiAll(allow){
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(","), "dataSource" : $("#modelJsonArray").val()};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url : basePath + "formdesigner/page/component/common/batchModifyAll.jsp",
		data : data,
		width : 1200,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				if("1"==ret){
					loadComponentTable(dynamicPageId);
					alert("修改成功！");
				}else{
					alert("修改失败！");
				}
				
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 带文本新增
 * @param 
 * @param 
 * @param 
 * @returns {Boolean}
 */
function quickComWithLabel(){
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId, "dataSource" : $("#modelJsonArray").val()};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '带文本新增...',
		url : basePath + "formdesigner/page/component/common/quickEditComponent.jsp",
		data : data,
		width : 600,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();

	return false;
}

/**
 * 以数据源新增
 * @param 
 * @param 
 * @param 
 * @returns {Boolean}
 */
function quickComByDataSource(){
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId, "dataSource" : $("#modelJsonArray").val()};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '以数据源新增...',
		url : basePath + "formdesigner/page/component/common/quickComByDataSource.jsp",
		data : data,
		width : 1200,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
}

/**
 * 根据validators.jsp中的validatJson，加载validators.jsp中的validatort的html
 */
function loadValidatorTable(){
	var validatorJson=$("#validatJson").val();
		if(validatorJson){
		$.ajax({
			type: "POST",
			url : basePath + "fd/validator/getResultsByAjax.do",
			data: "ids="+validatorJson,
			success: function(data){
				$("#validatort").html("");	
				//alert(data);
				if(data != null && data != "[]"){
					$.each(data,function(idx,item){
						$("#validatort").append("<tr id='"+item.pageId+"'><td><input type='checkbox' name='validator' value='"+item.pageId+"'/></td>" +
								"<td>"+item.name+"</td>" +
								"<td>"+item.validatorType+"</td>" +
								"<td  class=\"text-ellipsis\">"+item.description+"</td></tr>");
					});
				}
			}
		});
	} else {
		$("#validatort").html("");
	}
}

function initValidator(){
	loadValidatorTable();
}

//全局变量，tag的
var tagIndex = 0;

/**
 * 加载组件的通用属性值；
 */
function loadCommonComponentData(storeObject){
	//alert(333);
	//alert(storeObject.content);
	var componentObject = eval("("+storeObject.content+")");
	if(componentObject.pageId != null){
		if(componentObject.pageId)	$("#pageId").val(componentObject.pageId);
		if(componentObject.name)	$("#name").val(componentObject.name);
		$("#name").attr("readonly","true");
		/**新增属性by venson 2017-07-28*/
		if(componentObject.title)	$("#title").val(componentObject.title);
		if(componentObject.enTitle)	$("#enTitle").val(componentObject.enTitle);
		if(componentObject.placeholder)	$("#placeholder").val(componentObject.placeholder);
		if(componentObject.required=="1")$("#required").attr("checked", true);
		if(componentObject.extra)	$("#extra").val(componentObject.extra);
		if(componentObject.isSingle)$("#isSingle").val(componentObject.isSingle);
		if(componentObject.funType) $("#funType").val(componentObject.funType);
		/**新增属性 end*/
		
		if(componentObject.description) 	$("#description").val(componentObject.description);
		if(componentObject.order) 	$("#order").val(componentObject.order);
		if(componentObject.style) 	$("#style").val(componentObject.style);
		if(componentObject.alloworderby) { $("#alloworderby").val(componentObject.alloworderby).trigger("change");}
		if(componentObject.orderby) { $("#orderby").val(componentObject.orderby).trigger("change");}
		//if(componentObject.css) 	$("#css").val(componentObject.css);
		//if(componentObject.style) 	$("#style").val(componentObject.style);
		
		if(componentObject.dataItemCode)	$("#dataItemCode").val(componentObject.dataItemCode);// option[value='"+componentObject.dataItemCode+"']").attr("selected","true");
		
		//label
		if(componentObject.isRequired) {
			$("#isRequired").children("option[value='" + componentObject.isRequired + "']").attr("selected",true);
		}
		
		//附件类型
		if(componentObject.mtType)  $("[name = mtType]:checkbox").attr("checked", true);
		if(componentObject.mtType1)  $("[name = mtType1]:checkbox").attr("checked", true);
		if(componentObject.mtType2)  $("[name = mtType2]:checkbox").attr("checked", true);
		if(componentObject.mtType3)  $("[name = mtType3]:checkbox").attr("checked", true);
		if(componentObject.mtType4)  $("[name = mtType4]:checkbox").attr("checked", true);
		if(componentObject.mtType5)  $("[name = mtType5]:checkbox").attr("checked", true);
		
		if(componentObject.contentType)		$("#contentType option[value='"+componentObject.contentType+"']").attr("selected","true");
		if(componentObject.optionScript) 	$("#optionScript").val(componentObject.optionScript);
		
		//if(componentObject.defaultValueType) 	$("input[name='defaultValueType']:radio[value='"+componentObject.defaultValueType+"']").prop('checked','checked');	
		//if(componentObject.defaultValue) 	$("#defaultValue").val(componentObject.defaultValue);
		
		//state
		if(componentObject.hiddenScript) 	$("#hiddenScript").val(componentObject.hiddenScript);
		if(componentObject.disabledScript) 	$("#disabledScript").val(componentObject.disabledScript);
		if(componentObject.readonlyScript) 	$("#readonlyScript").val(componentObject.readonlyScript);
		if(componentObject.showScript) 	$("#showScript").val(componentObject.showScript);
		if(componentObject.onchangeScript) 	$("#onchangeScript").val(componentObject.onchangeScript);
		if(componentObject.defaultValueScript) 	$("#defaultValueScript").val(componentObject.defaultValueScript);
		if(componentObject.loadPageScript) 	$("#loadPageScript").val(componentObject.loadPageScript);
		
		if(componentObject.excelShowScript) 	$("#excelShowScript").val(componentObject.excelShowScript);
		//layout
		if(componentObject.layoutName) 	$("#layoutName").val(componentObject.layoutName);
		if(componentObject.layoutId) 	$("#layoutId").val(componentObject.layoutId);
		//validators
		if(componentObject.validateInputTip) 	$("#inputTip").val(componentObject.validateInputTip);
		if(componentObject.validateErrorTip) 	$("#errorTip").val(componentObject.validateErrorTip);
		if(componentObject.validateFocusTip) 	$("#focusTip").val(componentObject.validateFocusTip);
		if(componentObject.validateAllowNull) 	$("input[name='validateAllowNull']:radio[value='"+componentObject.validateAllowNull+"']").prop('checked','checked');
		
		//textArea
		if(componentObject.rowCount) 	$("#rowCount").val(componentObject.rowCount);
		
		//column
		if(componentObject.columnName) 	$("#columnName").val(componentObject.columnName);
		if(componentObject.sortName) 	$("#sortName").val(componentObject.sortName);
		if(componentObject.width) 	$("#width").val(componentObject.width);
		if(componentObject.pdfWidth) 	$("#pdfWidth").val(componentObject.pdfWidth);
		
		//file上传框
		if(componentObject.singleSize) 	$("#singleSize").val(componentObject.singleSize);
		if(componentObject.maxSize) 	$("#maxSize").val(componentObject.maxSize);
		if(componentObject.maxCount) 	$("#maxCount").val(componentObject.maxCount);
		if(componentObject.fileKind) 	$("#fileKind").val(componentObject.fileKind);
		if(componentObject.filePath) 	$("#filePath").val(componentObject.filePath);
		if(componentObject.showType) { 
			$("#showType").val(componentObject.showType).trigger("change");
		}
		if(componentObject.uploadType) { 
			$("#uploadType").val(componentObject.uploadType).trigger("change");
		}
		if(componentObject.isIndex) { 
			$("#isIndex").val(componentObject.isIndex).trigger("change");
		}
		//image
		if(componentObject.imageHeight) 	$("#imageHeight").val(componentObject.imageHeight);
		if(componentObject.imageWidth) 	$("#imageWidth").val(componentObject.imageWidth);
		
		
		//select
		if(componentObject.supportSearch) { $("input[name='supportSearch']").attr("checked","checked");}
		if(componentObject.supportMulti) { $("input[name='supportMulti']").attr("checked","checked");}
		if(componentObject.selectNumber) 	$("#selectNumber").val(componentObject.selectNumber);
		
		//包含页面组件
		if(componentObject.relatePageId) { 
			$("#relatePageId").val(componentObject.relatePageId).trigger("change");
		}
		
		//commondWords
		if(componentObject.workflowName){
		
			$("#workflowName").val(componentObject.workflowName);
			$("#workflowId").val(componentObject.workflowId);
			var nodes=[];
			for(var key in componentObject){
      			if(key.indexOf('flowNodes')!='-1'){
      			nodes.push(componentObject[key]);
      			}
	 		}
			
			//$("#submitSelect").click();
			showValue(nodes);
			
			if(componentObject.showType) 	$("#showType").val(componentObject.showType);
		    if(componentObject.freeFlow) { $("input[name='freeFlow']").attr("checked","checked");}
		
		}
		
		//日期dateType
		if(componentObject.dateType) { 
			$("#dateType").val(componentObject.dateType).trigger("change");
		}
		//样式库样式
		if(componentObject.css) { 		
			$("#css").val(componentObject.css).trigger("change");
		}
		
		
		if(componentObject.textRise) { $("#textRise").val(componentObject.textRise).trigger("change");}
		if(componentObject.fontfamily) { $("#fontfamily").val(componentObject.fontfamily).trigger("change");}
		if(componentObject.fontsize) { $("#fontsize").val(componentObject.fontsize).trigger("change");}
		if(componentObject.fontcolor) { $("#fontcolor").val(componentObject.fontcolor).trigger("change");}
		if(componentObject.backgroundcolor) { $("#backgroundcolor").val(componentObject.backgroundcolor).trigger("change");}
		if(componentObject.printAllOption) { $("#printAllOption").val(componentObject.printAllOption).trigger("change");}
		
		//checkbox
		if(componentObject.textstyle) { 
			var v = componentObject.textstyle;
			var array = v.split(",");
			for(var i = 0; i < array.length; i++) {
				var value = array[i];
				$("input[name='textstyle'][value='"+value+"']").attr("checked","checked");
			}
		}
		
		//列组件
		if(componentObject.columnPatten) { $("#columnPatten").val(componentObject.columnPatten);}
		if(componentObject.columnFormat) { 
			$("#columnFormat").val(componentObject.columnFormat).trigger("change");
		}
		
		
		if(componentObject.textalign) { $("#textalign").val(componentObject.textalign).trigger("change");}
		if(componentObject.textverticalalign) { $("#textverticalalign").val(componentObject.textverticalalign).trigger("change");}
		if(componentObject.textindent) { $("#textindent").val(componentObject.textindent);}
		if(componentObject.lineheight) { $("#lineheight").val(componentObject.lineheight);}
		
		//纯文本
		if(componentObject.textType) { $("#textType").val(componentObject.textType).trigger("change");}
		
		//搜索框
		if(componentObject.searchLocation) { $("#searchLocation").val(componentObject.searchLocation).trigger("change");}
		//表格组件 
		if(componentObject.dataAlias) { $("#dataAlias").val(componentObject.dataAlias);}
		if(componentObject.pageSize) 	$("#pageSize").val(componentObject.pageSize);
		if(componentObject.hasPager) { $("#hasPager").val(componentObject.hasPager).trigger("change");}
		if((componentObject.columns)){
			var data = componentObject.columns;
			
			var sortFun = function(column1,column2){
				if(column1.order-column2.order>0){
					return 1;
				} else{
					return -1;
				}
			}
			
			data.sort(sortFun);
			
			var index=0;
			$.each(data, function(index, item) {
				
				var str="<tr id=dataTr'"+index+"'>";
				str+="<td><input style='width:100%' name='columns[][columnTitle]' id='columnTitle-"+index+"' value='"+item.columnTitle+"' type=text" + "/>"+"</td>";
				str+="<td><input style='width:100%' name='columns[][columnField]' id='columnField-"+index+"' value='"+item.columnField+"' type=text" + "/>"+"</td>";
				str+="<td><input style='width:100%' name='columns[][columnWidth]' id='columnWidth-"+index+"' value='"+item.columnWidth+"' type=text" + "/>"+"</td>";
				str+="<td><input style='width:100%' name='columns[][order]' id='columnOrder-"+index+"' value='"+item.order+"' type=text" + "/>"+"</td>";
				str+="<td style='text-align:center;'><select class='editType' name='columns[][editType]' id='editType-'"+ index +"' value='"+item.editType+"'>";
				str+="<option "+(item.editType==0?"selected":"")+" value='0'>无</option><option "+(item.editType=="text"?"selected":"")+
				" value='text'>文本</option><option "+(item.editType=="numberbox"?"selected":"")+" value='numberbox'>数字</option><option "+
				(item.editType=="datebox"?"selected":"")+" value='datebox'>日期框</option><option "+(item.editType=="combobox"?"selected":"")
				+" value='combobox'>下拉框</option><option "+(item.editType=="checkbox"?"selected":"")+" value='checkbox'>选择框</option></select></td>";
				str+="<td><input class='editValue' "+(item.editType=="combobox"||item.editType=="checkbox"?"":"disabled='disabled'")+" style='width:100%' name='columns[][editValue]' id='editValue-"+index+"' value='"+item.editValue+"' type=text" + "/>"+"</td>";
				str+="<td style='text-align:center;'><a href='javascript:void(0)' class='removeTr'>删除</a>"+"</td>";
				str+="</tr>";
				index++;
				$("#columnsBody").append(str);
				
			});
		}
		if((componentObject.connditions)){
			var data = componentObject.connditions;
			var connIndex=0;
			$.each(data, function(index, item) {
				
				var str="<tr id=dataTr'"+connIndex+"'>";
				str+="<td><input style='width:100%' name='connditions[][paramKey]' id='paramKey-"+connIndex+"' value='"+item.paramKey + "'type=text" + "/>"+"</td>";
				str+="<td style='text-align:center;'>---></td>";
				
				str+="<td><input style='width:100%' name='connditions[][paramValue]' id='paramValue-"+connIndex+"' value='"+item.paramValue + "' type=text" + "/>"+"</td>";
				if(item.ifFinal=='1'){
					str+="<td style='text-align:center;'>"+"<input type='checkbox' name='connditions[][ifFinal]' id='sortable-'"+ connIndex +"' value='1' checked/></td>";
				}else{
					str+="<td style='text-align:center;'>"+"<input type='checkbox' name='connditions[][ifFinal]' id='sortable-'"+ connIndex +"' value='1' /></td>";
				}
				
				str+="<td style='text-align:center;'><a href='javascript:void(0)' class='removeConnTr'>删除</a>"+"</td>";
				str+="</tr>";
				connIndex++;
				$("#connditionBody").append(str);
				
			});
		}
		if((componentObject.param)){
			var data = componentObject.param;
			$.each(data, function(index, item) {				
				var str="<tr>";
				str += "<td><input type='text' style='width:75%' name='param[][exportParam]' value='" + item.exportParam + "'>"
					+ "<button class='btn btn-default exportParam' type='button'>选择</button>"
					+ "</td>";
				str += "<td>——》</td>";
				str += "<td><input type='text' style='width:75%' name='param[][storeParam]' value='" + item.storeParam + "'>"
				    + "<input type='hidden' name='param[][storeParamId]' value='" + item.storeParamId + "'>"
					+ "<button class='btn btn-default storeIdSelect' type='button'>选择</button>"
					+ "</td>";
				str += "<td><a href='javascript:void(0)' class='removeParamTr'>删除</a>"+"</td>";
				str += "</tr>";
				$("#paramentBody").append(str);
			});
		}
		
		//表格行操作组件
		if((componentObject.buttons)){
			var data = componentObject.buttons;		
			var sortFun = function(column1,column2){
				if(column1.order-column2.order>0){
					return 1;
				} else{
					return -1;
				}
			}			
			data.sort(sortFun);			
			$.each(data, function(index, item) {
				var selects = "<select class='form-control' name='buttons[][color]' value='" + item.color + "'>" + 
				  "<option value='btn-default' " + (item.color=='btn-default'?"selected":"") + ">白</option>" + 
				  "<option value='btn-primary' " + (item.color=='btn-primary'?"selected":"") + ">蓝</option>" + 
				  "<option value='btn-success' " + (item.color=='btn-success'?"selected":"") + ">绿</option>" + 
				  "<option value='btn-info' " + (item.color=='btn-info'?"selected":"") + ">青</option>" + 
				  "<option value='btn-danger' " + (item.color=='btn-danger'?"selected":"") + ">红</option>" +
				  "<option value='btn-warning' " + (item.color=='btn-warning'?"selected":"") + ">橙</option>" + 							 
				  "</select>";
				var str = "<tr>";			
				str += "<td style='padding:0px;'><input class='form-control' name='buttons[][title]' type='text' value='" + 
							item.title + "'/></td>";
				str += "<td style='padding:0px;'><input class='form-control' name='buttons[][className]' type='text' value='" + 
							item.className + "'/></td>";
				str += "<td style='padding:0px;'>" + selects + "</td>";
				str += "<td style='padding:0px;'><input class='form-control' name='buttons[][order]' type='text' value='" + 
							item.order + "'/></td>";
				str += "<td style='padding:0px;'><textarea class='form-control' name='buttons[][codes]' rows='4'>" 
							+ item.codes + "</textarea></td>";
				str += "<td style='padding:0px;'><textarea class='form-control' name='buttons[][severCodes]' rows='4'>" 
					+ item.severCodes + "</textarea></td>";
				str += "<td style='text-align:center;padding:0px;'><a href='javascript:void(0)' class='removeTr'>删除</a></td>";
				str += "</tr>";
				$("#buttonsBody").append(str);	
			});
		}
		
		
		
		if((componentObject.tags)){
			var data = componentObject.tags;
			var index=0;
			$.each(data, function(index, item) {
				
				var str="<tr id=dataTr'"+index+"'>";
				str+="<td><input name='tags[][tagsId]' style='width:100px;' id='tagsId-" + index + "' type=text" + " value='" + item.tagsId + "'/>"+"</td>";
				str+="<td><input name='tags[][tagsTitle]' style='width:250px;' id='tagsTitle-" + index + "' type=text" + " value='" + item.tagsTitle + "'/>"+"</td>"; 
				//str+="<td><select name='tags[][tagsType]' style='width:100px;' id='tagsType-" + index +"' class='form-control'><option value='1'>普通标签</option> <option value='2'>组标签</option> </select>"+"</td>";
				//str+="<td><input name='tags[][parentTags]' style='width:100px;' id='parentTags-" + index + "' type=text" + " value='" + item.parentTags + "'/>"+"</td>"; 
				str+="<td><select name='tags[][relatePageId]' style='width:250px;' id='relatePageId-" + index + "' class='form-control'>"+options+"<select></td>";
				//str+="<td><select name='tags[][isLoad]' style='width:100px;' id='isLoad-" + index + "' class='form-control'> <option value='1'>是</option> <option value='2'>否</option> </select>"+"</td>";
				
				str+="<td><input name='tags[][order]' style='width:100px;' id='order-" + index + "' type=text" + " value='" + item.order + "'/>"+"</td>";
				
				str+="<td><a href='javascript:void(0)' class='removeTr'>删除</a>"+"</td>";
				str+="</tr>";
				
				$("#tagsBody").append(str);
				$("#tagsType-" + index).val(item.tagsType);
				$("#relatePageId-" + index).val(item.relatePageId);
				$("#isLoad-" + index).val(item.isLoad);
				//alert("#isLoad-" + index+":"+item.isLoad);
				$("#relatePageId-" + index).select2();
				
				index++;
				tagIndex = index;
			});
			
		}
		
		if(componentObject.operateAdd) { $("input[name='operateAdd']").attr("checked","checked");}
		if(componentObject.operateDelete) { $("input[name='operateDelete']").attr("checked","checked");}
		if(componentObject.operateEdit) { $("input[name='operateEdit']").attr("checked","checked");}
		if(componentObject.operateSave) { $("input[name='operateSave']").attr("checked","checked");}
		if(componentObject.operateUndo) { $("input[name='operateUndo']").attr("checked","checked");}
		
		if(componentObject.alertPage) { 
			$("#alertPage").val(componentObject.alertPage).trigger("change");
		}
		if(componentObject.proportions) { $("#proportions").val(componentObject.proportions);}
		if(componentObject.lineHeight) { $("#lineHeight").val(componentObject.lineHeight);}
		if(componentObject.minLineCount) { $("#minLineCount").val(componentObject.minLineCount);}
		if(componentObject.maxLineCount) { $("#maxLineCount").val(componentObject.maxLineCount);}
		if(componentObject.pdfTemplatePage) { $("#pdfTemplatePage").val(componentObject.pdfTemplatePage);}
		if(componentObject.lineHeightType) { 
			$("#lineHeightType").val(componentObject.lineHeightType).trigger("change");
		}
		
		//包含组件
		if((componentObject.configures)){
			var data = componentObject.configures;
			var ddIndex=0;
			$.each(data, function(index, item) {
				
					var str="<tr id=dataTr'"+ddIndex+"'>";
					str+="<td>"+item.name+ "<input type='hidden' readonly='readonly'   name='configures["+ddIndex+"][pageId]' id='relateComId-"+ddIndex+"' value='"+item.pageId+"'/>"
					+ "<input type='hidden' readonly='readonly'   name='configures["+ddIndex+"][name]' id='relateComName-"+ddIndex+"' value='"+item.name+"'/>" 
					+ "<input type='hidden' readonly='readonly'   name='configures["+ddIndex+"][componentType]' id='relateComType-"+ddIndex+"' value='"+item.componentType+"'/>"
					+"</td>";

					
					str+="<td>"+"<input type='text'   name='configures["+ddIndex+"][dataItemCode]' id='dataItemCode-"+ddIndex+"' value='"+ item.dataItemCode + "'>"
					+	"<button class='btn btn-default dataItemCodeSelect' type='button' id='dataItemCodeSelect-"+ddIndex+"'>选择</button>"
					
					+"</td>";
					$("#dataItemCodeList").append(str);
					ddIndex++;
				
				
			});
			
		}
		
		
		
		//alert(4444);
		//alert(componentObject.validatJson)
		
		if(componentObject.validatJson) {
			//alert(5555);
			$("#validatJson").val(componentObject.validatJson);
			//alert($("#validatJson").val());
			initValidator();
		}
		//级联下拉框
		if(componentObject.select_select2_name) {
			$("#select_select2_name").val(componentObject.select_select2_name);
			$("#select_select2_label").val(componentObject.select_select2_label);
			$("#select_select2_sql").val(componentObject.select_select2_sql);
		}
        //tab页切换
        if(componentObject.tab_name){
            $("#tab_name").val(componentObject.tab_name);
            $("#tab_url").val(componentObject.tab_url);
        }

		//高德地图
		if(componentObject.ibs_height){
            $("#ibs_height").val(componentObject.ibs_height);
            $("#ibs_key").val(componentObject.ibs_key);
		}
		//搜索条件
		if(componentObject.selectOption){
			$("#selectOption").val(componentObject.selectOption);			
		}
		if(componentObject.selectLabel){
			$("#selectLabel").val(componentObject.selectLabel);
		}
		if(componentObject.selectName){
			$("#selectName").val(componentObject.selectName);		
		}
		if(componentObject.textLabel){
			$("#textLabel").val(componentObject.textLabel);
		}
		if(componentObject.textName){
			$("#textName").val(componentObject.textName);
		}
		if(componentObject.dateSelectLabel){
			$("#dateSelectLabel").val(componentObject.dateSelectLabel);
		}
		if(componentObject.dateSelectName){
			$("#dateSelectName").val(componentObject.dateSelectName);
		}
		if(componentObject.userSelectLabel){
			$("#userSelectLabel").val(componentObject.userSelectLabel);
		}
		if(componentObject.userSelectName){
			$("#userSelectName").val(componentObject.userSelectName);
		}
		
		//gridTable
		//easyUi 所有表格
		if(componentObject.gridType){
			$("#gridType").val(componentObject.gridType);
			$("#dataSource").val(componentObject.dataSource);
			$("#gridConf").val(componentObject.gridConf);
			$("#tableConf").val(componentObject.tableConf);
		}
		if(componentObject.dataSource){
			//
			$("#gridConf").val(componentObject.gridConf);
			$("#dataSource").val(componentObject.dataSource);
			$("#sonDataSource").val(componentObject.sonDataSource);
			$("#sonGridConf").val(componentObject.sonGridConf);
			$("#gridCellConf").val(componentObject.gridCellConf);
			$("#sonGridCellConf").val(componentObject.sonGridCellConf);
			$("#gridHeight").val(componentObject.gridHeight);
		}
		//表格
		if(componentObject.tableDataSources){
			$("#tableDataSources").val(componentObject.tableDataSources);
			$("#tableType").val(componentObject.tableType);
			$("#tableHasPager").val(componentObject.tableHasPager);
			$("#tablesingleSelect").val(componentObject.tablesingleSelect);
			$("#panelWidth").val(componentObject.panelWidth);
			$("#panelHeight").val(componentObject.panelHeight);
			$("#idField").val(componentObject.idField);
			//$("#tableWidth").val(componentObject.tableWidth);
			$("#textField").val(componentObject.textField);
			$("#comboLabel").val(componentObject.comboLabel);
			$("#labelPosition").val(componentObject.labelPosition);
			$("#fitColumns").val(componentObject.fitColumns);
		}
		//下拉表格
		if(true){
			
		}
		//行
		if(componentObject.cellConf){
			if(componentObject.setRowsAdd){
				$("#setRowsAdd").attr('checked','checked')
			}
			if(componentObject.contextMenu){
				$("#contextMenu").attr('checked','checked')
			}
			setEasyuiTable(componentObject);
		}
		//事件
		if(componentObject.onchangeScript){
			$("#onchangeScript").val(componentObject.onchangeScript);
		}
		
	}
}

//创建编辑的节点
function createNode(index,indexArr){
	var isEdit = 'none';
	//根据勾选 是否编辑
	if($('#setEdit').is(':checked')){
		isEdit = 'block';
	}
	$('th.editType').css('display',isEdit)
	
	var str="<tr class='editCellConf' id='dataTr"+index+"'>";
		//列名
		str+="<td><input type='text' class='form-control' name='celltxt"+index+"' id='celltxt"+index+"' placeholder='列名' ></td>";
		//字段名
		str+="<td><input type='text' class='form-control' name='cellfield"+index+"' id='cellfield"+index+"' placeholder='字段名' ></td>";
		//宽度
		str+="<td class='tdWidth'><input type='text' class='form-control' name='cellwidth"+index+"' id='cellwidth"+index+"' placeholder='宽度' value='80' ></td>";
		//对齐方式
		str+="<td><select class='form-control' name='cellalign"+index+"' id='cellalign"+index+"'>"
		+"<option value='left'>左对齐</option>"
		+"<option value='right'>右对齐</option>"
		+"<option value='center'>居中</option>"
		+"</select>"
		+"</td>";
		//编辑
		str+="<td class='edit' style='display:"+isEdit+"'><select class='form-control editType' name='celleditType"+index+"' id='celleditType"+index+"'>"
			+"<option value=''>不可编辑</option>"
			+"<option value='text'>文本</option>"
			+"<option value='combobox'>下拉选择</option>"
			+"<option value='checkbox'>多选框</option>"
			+"</select>"
			//编辑地址
			+"<input style='display:none' type='text' class='editSelect' name='editSelecturl"+index+"' id='editSelecturl"+index+"' placeholder='下拉框数据源'>"
			//编辑
			+"<input style='display:none' type='text' class='editSelect' name='editSelectfield"+index+"' id='editSelectfield"+index+"' placeholder='值字段'>"
			+"<input style='display:none' type='text' class='editSelect' name='editSelecttext"+index+"' id='editSelecttext"+index+"' placeholder='显示字段'>"
			+"</td>";
		//删除	
		str+="<td><button class='btn removeTr' type='button'>删除</button></td>";
		
		$("#easyuiTableCell").append(str);
		//编辑类型为下拉框   额外需要三个值  and 事件
		$("#dataTr"+index).find(".editType").attr('data',index);
		$('.editType').off().on('change',function(){
			var type = $("#tableType").val();
			var indexEvt = $(this).attr('data');
			if($(this).val() == 'combobox'){
			$(this).siblings('.editSelect').css('display','block');
			//添加框
				var htm = '<div class="form-group '+type+'">'
					htm +='<label class="col-md-2"><a href="javascript:openCodeMirror(\'hiddenScript\');">'+(Number(indexEvt)+1)+'列值改变：onchange(newValue, oldValue,rowData)</a></label>'	
					htm +='<div class="col-md-4">'
					htm +='<textarea name="onchangeScript'+indexEvt+'" id="onchangeScript'+indexEvt+'" rows="4" class="form-control"></textarea>'
					htm +='</div>'
					htm +='</div>'
				$('#easyui_event').append(htm)
			}else{
				$(this).siblings('.editSelect').css('display','none');
				//移除
				var htm = $("#onchangeScript"+indexEvt).parents("."+type);
				if(htm.length>0){
					htm.remove()
				}
			}
		})
		indexArr.push(index)
		$('#cellConf').val(indexArr);
}

function setEasyuiTable(componentObject){
	//允许编辑
	if(componentObject.setEdit){
		$("#setEdit").attr('checked','checked')
	}
	var cellConf = componentObject.cellConf;
	cellConf = cellConf.split(',');
	var indexArr = [];
	for (var index = 1; index <= cellConf.length; index++) {
		if(componentObject['cellfield'+index] && componentObject['celltxt'+index]){	
	/*---------------开始创建节点----------------*/
			createNode(index,indexArr);
	/*---------------创建节点结束----------------*/
			
			//赋值
			$("#celltxt"+index).val(componentObject['celltxt'+index]);
			$("#cellfield"+index).val(componentObject['cellfield'+index]);
			$("#cellalign"+index).val(componentObject['cellalign'+index]);
			//设置自动宽度隐藏此列
			if(componentObject.fitColumns=='true'){
				$("#cellwidth"+index).val(componentObject['cellwidth'+index]);
				$(".editCellConf").find(".tdWidth").css('display','none');
			}
			
			//事件
			if(componentObject.datagridClickScript){
				$("#datagridClickScript").val(componentObject.datagridClickScript);
			}
			if(componentObject.treegridClickScript){
				$("#treegridClickScript").val(componentObject.treegridClickScript);
			}
			if(componentObject.combogridChangeScript){
				$("#combogridChangeScript").val(componentObject.combogridChangeScript);
			}
			if(componentObject.LoadSuccessScript){
				$("#LoadSuccessScript").val(componentObject.LoadSuccessScript);
			}
			
			//编辑
			if(componentObject.setEdit){
				$("#celleditType"+index).val(componentObject['celleditType'+index]);
				if(componentObject['celleditType'+index] == "combobox"){
					$("#dataTr"+index).find('input.editSelect').css('display','block');
				}
				$("#editSelecturl"+index).val(componentObject['editSelecturl'+index]);
				$("#editSelectfield"+index).val(componentObject['editSelectfield'+index]);
				$("#editSelecttext"+index).val(componentObject['editSelecttext'+index]);
				
				var indexEvt = index; 
				if($("#celleditType"+index).val() == "combobox"){
					var type = componentObject.tableType;
					var htm = '<div class="form-group '+type+'">'
					htm +='<label class="col-md-2"><a href="javascript:openCodeMirror(\'hiddenScript\');">'+(Number(indexEvt)+1)+'列值改变：onchange(newValue, oldValue,rowData)</a></label>'	
					htm +='<div class="col-md-4">'
					htm +='<textarea name="onchangeScript'+indexEvt+'" id="onchangeScript'+indexEvt+'" rows="4" class="form-control">'+componentObject["onchangeScript"+index]+'</textarea>'
					htm +='</div>'
					htm +='</div>'
					$('#easyui_event').append(htm);
					$("#onchangeScript"+index).val(componentObject['onchangeScript'+index]);
				}
			}
		}
	}
}

function initializeDataSource(modelJsonArray){
	if(modelJsonArray && modelJsonArray.length > 0){
		for(var i = 0; i < modelJsonArray.length;i++){
			if(modelJsonArray[i].modelItemCodes){
				 var itemCodes = modelJsonArray[i].modelItemCodes.split(",");
				 for(var j = 0; j < itemCodes.length; j++){
					var option = "<li><a href='javascript:;' data='" + modelJsonArray[i].name + "." + itemCodes[j] + "'>" +
						modelJsonArray[i].name + "." + itemCodes[j] +"</a></li>";
					$("#selectInput .sI_select").append(option);
				 }
			}
		}
	}
}

function initializeLayout(){
	
}

function initializeDocument(componentType, typeName){
	$("#css").select2();
	$("#componentType").val(componentType);
	try {
		var dialog = top.dialog.get(window);
	} catch (e) {
		return;
	}
	var data = null;
	var componentId = null;
	var dataSource = null;
	if(dialog != null){
		data = dialog.data;
		if(data != null && data.pageId){
			componentId = data.pageId;
		}
		if(data != null && data.dataSource){
			dataSource = data.dataSource;
		}			
	}
	if(data.dynamicPageId && $("#dynamicPageId")){
		$("#dynamicPageId").val(data.dynamicPageId);
	}
	//initialize data source options
	var dataSourceArray = eval(dataSource);
	initializeDataSource(dataSourceArray);
	if(componentId != null){
		$.ajax({
		   type: "POST",
		   async:false,
		   url: basePath + "component/getComponentById.do",
		   data: "storeId="+componentId,
		   success: function(data){
				if(data != null && data.id != null){	
					loadCommonComponentData(data);
				}else {
					alert("加载组件失败");
				}
		   }
		});	
	}else if(data._selects){
		$("#name").val("not Allowed");
		$("#name").attr("readonly","true");
		$("#order").val(1);
		$("#order").attr("readonly","ture");
		$("#layoutId").val("无法批量");
		$("#layoutName").val("无法批量");
	}else {
		var uuid = guidGenerator();
		if($("#name"))	$("#name").val(uuid);
		$("#name").attr("readonly","true");	
	}

	if(dialog != null){
		setTimeout(function(){
			dialog.title(typeName + "组件配置");
			dialog.width(800);
			dialog.height($(top).height()-93); 
			dialog.reset();     // 重置对话框位置
		},200);	
	}
	$("#deleteValidator").click(function(){
		edit("validator","remove",null);
		return false;
	});		
	$("#addValidator").click(function(){
		edit("validator","add",null);
		return false;
	});

	$("body").on("click","#selectInput a",function(){
		var value = $(this).attr("data");
		$("#selectInput .sI_input").val(value);
	});
	
	$("#cancelComponent").click(function(){
		if(dialog != null){
			dialog.close();
			dialog.remove();
		}
	});
	
	$('#layoutSelect').click(function(){
		if($("#dynamicPageId").val()){
			$.ajax({
				url: basePath + "layout/listLayoutInTree.do?dynamicPageId="+$("#dynamicPageId").val(),
				dataType:'json',
				success: function(msg){
					top.dialog({ 
						id: 'select-layout-dialog' + Math.ceil(Math.random()*1000000),
						title: '选择布局',
						url: basePath + "formdesigner/page/component/zTreeLayout.jsp",
						data:msg,
						onclose : function() {
							if (this.returnValue) {
								//alert(this.returnValue.id);
								$("#layoutName").val(this.returnValue.name);
								$("#layoutId").val(this.returnValue.id);
							}
						}
					}).show();
			   	}
			});
		} else {
			alert("请先保存表单");
		}
	});
	
	//查看关键字
	$('#showKeywords').click(function(){
		top.dialog({ 
			id: 'keywords-dialog' + Math.ceil(Math.random()*1000000),
			title: '系统关键字',
			url: basePath + "formdesigner/page/component/keywords-list.jsp?operation=0",
			height:500,
			width:400,
			onclose : function() {
			}
		}).show();
	});
		
	$('#labelLayoutSelect').click(function(){
		if($("#dynamicPageId").val()){
			$.ajax({
				url: basePath + "layout/listLayoutInTree.do?dynamicPageId="+$("#dynamicPageId").val(),
				dataType:'json',
				success: function(msg){
					top.dialog({ 
						id: 'select-layout-dialog' + Math.ceil(Math.random()*1000000),
						title: '选择布局',
						url: basePath + "formdesigner/page/component/zTreeLayout.jsp",
						data:msg,
						onclose : function() {
							if (this.returnValue) {
								//alert(this.returnValue.id);
								$("#labelLayoutName").val(this.returnValue.name);
								$("#labelLayoutId").val(this.returnValue.id);
							}
						}
					}).show(document.getElementById("layoutName"));
			   	}
			});
		} else {
			alert("请先保存表单");
		}
	});
	
	$("#saveComponent").click(function(){
		var name = $("#name").val();
		if(!validateCheckOut($("#dynamicPageId").val())){	//校验是否已签出		
			return false;
		}
		if($.formValidator.pageIsValid('1')){					
			//ajax校验name是否会重复
			$.ajax({
				dataType : "json", 
				async : false, 
				url : basePath + "component/validateComponentNameInPage.do?dynamicPageId="+$("#dynamicPageId").val()+"&componentName="+$("#name").val()+"&componentId="+$("#pageId").val(),
				success :  function(data){    //name不会有重复，则提交保存
					if(data==true){
						var formJson=$("#componentForm").serializeJSON({useIntKeysAsArrayIndex: true});
						var textstyle = "";
						$("input[name='textstyle']:checked").each(function(){
							 textstyle += $(this).val() + ",";
						});
						textstyle = textstyle.substring(0,textstyle.length-1);
						if(textstyle != ""){
							formJson.textstyle = textstyle;
						}						
						var componentIds = $("#componentIds").val();
						var componentId = $("#pageId").val();
						var description =  $("#description").val();
						var dynamicPageId=$("#dynamicPageId").val();
						var sjContent = JSON.stringify(formJson);
						var order = $("#order").val();						
						var componentIds = "";						
						try {
							var dialog = top.dialog.get(window);
							if(dialog != null){
								data = dialog.data;
								if(data != null && data._selects){
									componentIds = data._selects;
								}						
							}
						} catch (e) {
							return;
						}						
						$.ajax({
							   type: "POST",
							   url: basePath + "component/save.do",
							   data:{
								   id:componentId,
								   componentIds:componentIds,
								   name:name,
								   description:description,
								   content:sjContent,
								   dynamicPageId:dynamicPageId,
								   order:order
								},
							   
							   async : false,
							   success: function(data){
									if(data != null){	
										$("#componentId").val(data.id);									
										if(dialog != null){
											dialog.close(data);
											dialog.remove();
										}
									}else{
										alert("保存失败");
										$("button").removeAttr("disabled");
									}
							   }
						});	
					}else{
						alert("该名称不可用，请更换名称");
						$("button").removeAttr("disabled");
					}
				},
			});					
		}
		return false;
	});
	
	$("#saveQuickComponent").click(function(){
		if(!validateCheckOut($("#id").val())){	//校验是否已签出			
			return false;
		}
		if($.formValidator.pageIsValid('1')){
			var formJson=$("#componentForm").serializeJSON();
			var name = $("#name").val();
			
			var description =  $("#description").val();
			var dynamicPageId=$("#dynamicPageId").val();
			var sjContent = JSON.stringify(formJson);
			$.ajax({
				type: "POST",
				url: basePath + "component/quickSave.do",
				data: "name="+name + 
				"&description="+description+"&content="+sjContent+
				"&dynamicPageId=" + dynamicPageId,
				async : false,
				success: function(data){
					if(data != null){	
						$("#componentId").val(data.id);
						
						if(dialog != null){
							dialog.close(data);
							dialog.remove();
						}
					}else{
						alert("保存失败");
					}
				}
			});	
		}
		return false;
	});
	
	$("#checkAll").click(function(){
		var v = $(this).prop("checked");
		$("#validatort input[type='checkbox'][name='validator']").each(function(){
			$(this).prop("checked",v);
		});
	});
	
	//校验库按钮
	$("#validatorStore").click(function(){
		top.dialog({ 
			id: 'select-validators-dialog' + Math.ceil(Math.random()*1000000),
			title: '选择校验函数',
			url: basePath + "fd/validator/list.do?isSelect=1",
			//data:msg,
			width : 1024,
			onclose : function() {
				//alert(this.returnValue._selects);
				if (this.returnValue) {
					//保存选择的校验id到validatorJson中,并自动给出顺序
					if($("#validatJson")){
						//存储id，以逗号分隔,出现的顺序就是校验的顺序
						var tmp = $("#validatJson").val();
						if(tmp == null){
							tmp = "";
						}
						//返回勾选的校验id，也是逗号分隔
						var tmp2 = this.returnValue;
						var array = tmp2.split(",");
						for(var i = 0; i < array.length; i++){
							var item = array[i];
							//把已经存在的过滤掉，不存在的加入进来
							if(tmp.indexOf(item) == -1){
								tmp += tmp == "" ? "" : ",";
								tmp += item;
							}
						}
						//alert(1111);
						$("#validatJson").val(tmp);
						//alert(tmp);
						//刷新校验Table
						loadValidatorTable();
					}
				}
				return true;
			}
		}).showModal();
		return false;
	});
	//移除校验按钮
	$("#delValidatorBtn").click(function(){
		//提示?
		if($("#validatJson")){
			//存储id，以逗号分隔,出现的顺序就是校验的顺序
			var tmp = $("#validatJson").val();
			//alert(tmp);
			$("#validatort input:checked[name='validator']").each(function(){
				//alert($(this).val());
				var ts = $(this).val();
				tmp = tmp.replace("," + ts + ",",",").replace(ts + ",","").replace("," + ts,"").replace(ts,"");
			});
			//alert(tmp);
			$("#validatJson").val(tmp);
			$("#checkAll").prop("checked",false);
			//alert(tmp);
			//alert($("#validatJson").val());
			//刷新校验Table
			loadValidatorTable();
		}
		return false;
	});
}

$("#copyComponent").click(function(){	
	var count = $(":checkbox[name='component']:checked").size();
	if(count > 0){
		if(!validateCheckOut($("#id").val())){	//校验是否已签出
			
			return false;
		}
		var dynamicPageId = $("#id").val();
		copyComponent("component",dynamicPageId);
		return false;
	}else{
		alert("请选择一个组件操作");
		return false;
	}
});

function copyComponent(objectType,dynamicPageId){
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name="+objectType+"]:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	$.ajax({
		url:basePath + "component/copyComponentByAjax.do?_selects=" + _selects.join(","),
		type:"GET",
		async:false,
	//	data:{_selects:_selects.join(",")},
		success:function(ret){
			if("1"==ret){
				$(":checkbox[name="+objectType+"]:checked").each(function(){
					var value=$(this).val();
					//	actMap.remove(value);
				});
				//fresh();
				$(":checkbox[name='checkAllComponent']").prop("checked",false);
				loadComponentTable(dynamicPageId);
				alert("复制成功！");
			}else{
				alert("复制失败");
			}
			
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) { 
			alert(errorThrown); 
		}
	});
	return false;
}


function guidGenerator() {
    var S4 = function() {
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    };
    return (S4()+S4()+S4()+S4()+S4()+S4()+S4()+S4());
}


$("#modifiDataItemCode").click(function(){
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	var count = $(":checkbox[name='component']:checked").size();
	if(count < 1){
		alert("请选择组件操作");
		return false;
	}
	
	var _selects=new Array();
	//var loadUrl = "layout/getLayoutListByPageId.do"
	$(":checkbox[name='component']:checked").each(function(){
		var value=$(this).val();
		_selects.push(value);
	});
	
	var dynamicPageId = $("#id").val();
	var data = { "dynamicPageId" : dynamicPageId, "dataSource" : $("#modelJsonArray").val(), "_selects" : _selects.join(",")};
	var url = "";
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '批量修改数据源',
		url : basePath + "formdesigner/page/component/common/quickEditDateItem.jsp",
		data : data,
		width : 300,
		onclose : function() {
			if (this.returnValue) {
				var ret = this.returnValue;
				loadComponentTable(dynamicPageId);
			}
		}
	
	}).showModal();
	
	return false;
});

function refreshComponentOrder(dynamicPageId){
	
	$.ajax({
		url:"component/refreshComponentOrder.do?pageId="+dynamicPageId,
		type:"GET",
		async:false,
		success:function(ret){
			if("1"==ret){
				loadComponentTable(dynamicPageId);
			}else{
				alert("重置失败！");
			}
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) { 
			alert(errorThrown); 
		}
	});
	return false;
}

$("#refreshComponentOrder").click(function(){
	if(!validateCheckOut($("#id").val())){	//校验是否已签出
		
		return false;
	}
	if(window.confirm("你确定要重置吗？")){
		var dynamicPageId = $("#id").val();
		refreshComponentOrder(dynamicPageId);
	}
		return false;
});

$("#editName").click(function(){
	$("#name").removeAttr("readonly");
});


$("#refreshContainerComponent").click(function(){
	var count = $(":checkbox[name='component']:checked").size();
	var _selects=new Array();
	$(":checkbox[name='component']:checked").each(function(){
		_selects.push($(this).val());
	});
	
	if(count < 1){
		alert("请选择组件");
		return false;
	}else if(count==1){
		top.dialog({
			id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
			title : '别名',
			url : basePath + "formdesigner/page/component/common/refreshContainer.jsp",
			data : {"_selects" : _selects.join(",")},
			width : 300,
			onclose : function() {
				if (this.returnValue) {
					var ret = this.returnValue;
					if(ret!="1"){
						alert("重置失败！")
					}else{
						$(":checkbox[name='component']:checked").prop("checked","");
						$(":checkbox[name='checkAllComponent']").prop("checked",false);
					}
				}
			}
		
		}).showModal();
	}else{
		if(window.confirm("重置组件将恢复组件至默认状态，你确定要重置吗？")){
			$.ajax({
				url:"component/refreshContainerComponent.do",
				type:"POST",
				dataType:"json",
				data:{"_selects" : _selects.join(",")},
				success:function(ret){
					if("1"!=ret){
						alert("重置失败！");
					}else{
						$(":checkbox[name='component']:checked").prop("checked","");
						$("#checkAllComponent").prop("checked","");
					}
				},
				error:function(XMLHTTPRequest,textStatus,errorThrown){
					alert(errorThrown);
				}
				
			});
		}
		return false;
	}
	return false;
});

