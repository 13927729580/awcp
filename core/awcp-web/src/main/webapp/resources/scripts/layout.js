var $window=$(window);
var $body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html') : $('body')) : $('html,body'); // opera fix
$(function(){
	//fixed button
	var $buttons = $("#buttons");
	$buttons.addClass("navbar-fixed-top").css({"display":"inline","width":"auto","left":20,"top":30,"right":"100%"});
	//scroll to top
	$("<a href='javascript:;' class='toTop' onclick='scrollToTop()'><i class='icon-chevron-up'>&nbsp;</i>返回顶部</a>").appendTo("body");
	//load menu
	loadMenu();
	//reset layout
	$(".input-group .input-group-addon").each(function(item){
		if(!($(this).prev().length!=0&&$(this).next().length!=0)) $(this).removeClass("fix-border").removeClass("fix-padding");
	});
	//alret message
	$(".alert-dismissable .close").click(function(){
		$(this).parent().remove();
	});
	var $icon = $(".scrollMenu .icon");
	var $con = $(".scrollMenu .con");
	$icon.click(function(){
		if($con.css("display")=="none"){
			$con.show();
			$icon.removeClass("icon-chevron-down").addClass("icon-chevron-up");
		}else{
			$con.hide();
			$icon.removeClass("icon-chevron-up").addClass("icon-chevron-down");
		}
	});
});
function scrollToTop(){
	$body.animate({scrollTop:0},500);
	return false;
};
function loadMenu(){
	var menu = "<div class='scrollMenu'><h6>&nbsp;&nbsp;<i class='icon-list'>&nbsp;快捷导航</i>" +
			"<i class='icon icon-chevron-up'></i></h6><div class='con'>";
	/*var $mTitle = $(".control-label.mTitle");
	$mTitle.each(function(item){
		menu +="<a href='#"+$(this).attr("id")+"'>"+$(this).text()+"</a>";
	});*/
	var $label = $(".control-label");
	$label.each(function(item){
		if(!$(this).parents("div").hasClass("hidden")&&$(this).css("fontSize") == "16px"){
			menu +="<a href='#"+$(this).attr("id")+"' class='text-ellipsis' title='"+$(this).text()+"'>"+$(this).text()+"</a>";
		}
	});
	menu += "</div></div>";
	//$(menu).appendTo("#buttons");	
};
$window.bind('scroll resize load',function(){
	//fixed button
	var scrollTop = $window.scrollTop();
	if(scrollTop>100){		
		$(".toTop").show();
	}else{
		$(".toTop").hide();
	}
});

/**
 * 
 */



	function empty(v){ 
			switch (typeof v){ 
				case 'undefined' : return true; 
				case 'string' : if($.trim(v).length == 0) return true; break; 
				case 'boolean' : if(!v) return true; break; 
				case 'number' : if(0 === v) return true; break; 
				case 'object' : 
				if(null === v) return true; 
				if(undefined !== v.length && v.length==0) return true; 
				for(var k in v){return false;} return true; 
				break; 
			} 
			return false; 
	}
		
		


		function addLayoutRow(objectType, comObject){
			var object = commStore[objectType];
			//标示符
			var unique=object.unique;
			//table 列
			var table=object.table;
			//是否是超链接字段
			var href=object.href;
			item = eval("(" + comObject.content + ")");
			var str="<tr id='"+item[unique]+"'><td><input type='checkbox' name='"+objectType+"' value='"+item[unique]+"'/></td>";
			
			$.each(table,function(i,t){
				//判断是否是href字段，若是加事件，不是else
				if(href==t){
						str+="<td><a href='javascript:void(0);' onclick='editLayout(\""+objectType+"\",\"update\""+",\""+item[unique]+"\",\""+item.componentType+"\",\"" + item.dynamicPageId+"\")'>"+item[t]+"</a></td>";
				}else{
					str+="<td>"+item[t]+"</td>";
				} 
			});
			str+="</tr>";
			$("#"+objectType+"t").append(str);
		}
		

		
		function searchLayout(){
			var dynamicPageId=$("#id").val();
			loadLayoutTable(dynamicPageId);
		}
		
		function loadLayoutTable(dynamicPageId){
			var url = basePath + "layout/getLayoutListByPageId.do";

			var rows = $("#rows").val();
			var columns = $("#columns").val();
			var objectType="layout";
			var postData={};
			postData.columns=columns;
			postData.rows=rows;
			postData.dynamicPageId=dynamicPageId;
			
			
			$.ajax({
				type:"GET",
				url:url,
				data:postData,
				async:false,
				success:function(data){
					//clear table
					$("#"+objectType+"t").empty();
					$.each(data,function(idx,item){
						addLayoutRow(objectType, item);
					});
				},
			    error: function (XMLHttpRequest, textStatus, errorThrown) { 
	                  alert(errorThrown); 
			    }
			});
		}

	function addPageLayout(objectType,action,pageId,dynamicPageId){
		var object = commStore[objectType];
		
		

		var urlStr=object.url;
			
		var postData={};
		
		if(action=='add'){
			postData.parentId=pageId;
			postData.dynamicPageId=dynamicPageId;
		}else if(action=='update'){
			postData.pageId=pageId;
			postData.dynamicPageId=dynamicPageId;
		}
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '载入中...',
			url: urlStr,
			data:postData,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					//setData(object,ret);
					//var store=JSON.parse($("#"+object.storeId).val());
					//addRow(objectType,ret)
					alert("保存成功");
					loadLayoutTable(dynamicPageId);
					initLayoutTree(dynamicPageId);
				}
			}
			
		}).showModal();
		return false;
	}
	

	function addLayout(type){
		var dynamicPageId = $("#id").val();
		editLayout("layout","add",null,type,dynamicPageId);
		return false;
	}
	
	//新增子布局
	function addChildLayout(type){
		var count = $(":checkbox[name='layout']:checked").size();
		if(count == 1){
			var dynamicPageId = $("#id").val();
			var pageId = $(":checkbox[name='layout']:checked:first").val();
			editLayout("layout","add",pageId,type,dynamicPageId);
			return false;
		}else{
			alert("请选择一个布局操作");
			return false;
		}
		
	}
	
	//批量修改布局占比
	function batchModifyProportion(type){
		
		if(!validateCheckOut($("#id").val())){	//校验是否已签出
			
			return false;
		}
		var _selects=new Array();
		//var loadUrl = "layout/getLayoutListByPageId.do"
		$(":checkbox[name='layout']:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		
		var dynamicPageId = $("#id").val();
		var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '批量修改布局占比...',
			url: "formdesigner/page/layout/batchModifyProportion.jsp",
			data:data,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					//setData(object,ret);
					//var store=JSON.parse($("#"+object.storeId).val());
					//addRow(objectType,ret)
					alert("保存成功");
					loadLayoutTable(dynamicPageId);
					
				}
			}
			
		}).showModal();
		return false;
		
	}
	//批量清除行高
	function batchHeight(type){
		
		var _selects=new Array();
		//var loadUrl = "layout/getLayoutListByPageId.do"
		$(":checkbox[name='layout']:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		
		var dynamicPageId = $("#id").val();
		var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
		top.dialog({
			id: 'clear-dialog' + Math.ceil(Math.random()*10000),
			title: '批量清除宽高',
			url: "formdesigner/page/layout/batchClearHeight.jsp",
			data:data,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					alert("保存成功");
					loadLayoutTable(dynamicPageId);
					
				}
			}
		
		}).showModal();
		return false;
		
	}
	
	//批量修改行高
	function batchModifyHeight(type){
		var _selects=new Array();
		//var loadUrl = "layout/getLayoutListByPageId.do"
		$(":checkbox[name='layout']:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		
		var dynamicPageId = $("#id").val();
		var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '批量修改布局占比...',
			url: "formdesigner/page/layout/batchModifyHeight.jsp",
			data:data,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					//setData(object,ret);
					//var store=JSON.parse($("#"+object.storeId).val());
					//addRow(objectType,ret)
					alert("保存成功");
					loadLayoutTable(dynamicPageId);
					
				}
			}
		
		}).showModal();
		return false;
		
	}
	
	//批量修改对齐方式
	function batchModifyAlign(type){
		var _selects=new Array();
		$(":checkbox[name='layout']:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		
		var dynamicPageId = $("#id").val();
		var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '批量修改布局占比...',
			url: "formdesigner/page/layout/batchModifyAlign.jsp",
			data:data,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					alert("保存成功");
					loadLayoutTable(dynamicPageId);
					
				}
			}
		
		}).showModal();
		return false;
		
	}
	//批量修改边框
	function batchModifyBorder(type){
		var _selects=new Array();
		$(":checkbox[name='layout']:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		
		var dynamicPageId = $("#id").val();
		var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '批量修改布局占比...',
			url: "formdesigner/page/layout/batchModifyBorder.jsp",
			data:data,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					alert("保存成功");
					loadLayoutTable(dynamicPageId);
					
				}
			}
		
		}).showModal();
		return false;
		
	}

	//批量修改
	function batchModify(type){
		
		if(!validateCheckOut($("#id").val())){	//校验是否已签出
			
			return false;
		}
		
		var _selects=new Array();
		$(":checkbox[name='layout']:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		
		var dynamicPageId = $("#id").val();
		var data = { "dynamicPageId" : dynamicPageId,  "_selects" : _selects.join(",")};
		editLayout("layout","update",_selects.join(","), null, dynamicPageId);
		loadLayoutTable(dynamicPageId);
		return false;
		
	}
	function editLayout(objectType,action,id,componentType,dynamicPageId){
			if(empty(objectType)){
				return false;
			}
			if(empty(action)){
				return false;
			}
			if(action=="add"){
				addPageLayout(objectType,action,id,dynamicPageId);
			}else if(action=="update"){
				addPageLayout(objectType,action,id,dynamicPageId);
			}else if(action=="remove"){
				removeLayout(objectType,dynamicPageId);
			}
	}
	
	function addQuickLayout(type){
		if(!validateCheckOut($("#id").val())){	//校验是否已签出	
			return false;
		}
		
		var dynamicPageId = $("#id").val();
		var pageId;
		var count = $(":checkbox[name='layout']:checked").size();
		if(count == 1){
			pageId = $(":checkbox[name='layout']:checked:first").val();
		}
		var postData={};
		
		
		postData.parentId=pageId;
		postData.dynamicPageId=dynamicPageId;
		
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '载入中...',
			url: "formdesigner/page/layout/quickLayoutEdit.jsp",
			data:postData,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					//setData(object,ret);
					//var store=JSON.parse($("#"+object.storeId).val());
					//addRow(objectType,ret)
					alert("保存成功");
					loadLayoutTable(dynamicPageId);
					
				}
			}
			
		}).showModal();
		return false;
	}
	
	function excelLayout(type){
		var dynamicPageId = $("#id").val();
		var postData={};
		
		postData.dynamicPageId=dynamicPageId;
		
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: 'excel形式新增',
			url: "formdesigner/page/layout/excelLayout.jsp",
			data:postData,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					//setData(object,ret);
					//var store=JSON.parse($("#"+object.storeId).val());
					//addRow(objectType,ret)
					alert("保存成功");
					loadLayoutTable(dynamicPageId);
					loadComponentTable(dynamicPageId);
				}
			}
		
		}).showModal();
		return false;
	}
	
	function removeLayout(objectType,dynamicPageId){
		
		if(!validateCheckOut(dynamicPageId)){	//校验是否已签出
			
			return false;
		}

		var _selects=new Array();
		//var loadUrl = "layout/getLayoutListByPageId.do"
		$(":checkbox[name="+objectType+"]:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		$.ajax({
			url:"layout/deleteByAjax.do",
			type:"POST",
			async:false,
			data:{_selects:_selects.join(",")},
			success:function(ret){
				if("1"==ret){
					//fresh();
					$(":checkbox[name='allLayout']").prop("checked",false);
					loadLayoutTable(dynamicPageId);
					alert("删除成功！");
				}else{
					alert("删除失败！");
				}
				
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) { 
	              alert(errorThrown); 
		    }
		});
		return false;
	}
	
	function merageLayout(objectType,dynamicPageId){
		var _selects=new Array();
		//var loadUrl = "layout/getLayoutListByPageId.do"
		$(":checkbox[name="+objectType+"]:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		$.ajax({
			url:"layout/merageLayoutByAjax.do",
			type:"POST",
			async:false,
			data:{_selects:_selects.join(",")},
			success:function(ret){
				if("1"==ret){
					$(":checkbox[name="+objectType+"]:checked").each(function(){
						var value=$(this).val();
					//	actMap.remove(value);
					});
					//fresh();
					$(":checkbox[name='allLayout']").prop("checked",false);
					loadLayoutTable(dynamicPageId);
					alert("合并成功！");
				}else if("-1"==ret){
					alert("所属不同父布局，无法合并");
				}else{
					alert("合并失败");
				}
				
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) { 
	              alert(errorThrown); 
		    }
		});
		return false;
	}
	
	function copyLayout(objectType,dynamicPageId){
		var _selects=new Array();
		//var loadUrl = "layout/getLayoutListByPageId.do"
		$(":checkbox[name="+objectType+"]:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		$.ajax({
			url:"layout/copyLayoutByAjax.do",
			type:"POST",
			async:false,
			data:{_selects:_selects.join(",")},
			success:function(ret){
				if("1"==ret){
					$(":checkbox[name="+objectType+"]:checked").each(function(){
						var value=$(this).val();
						//	actMap.remove(value);
					});
					//fresh();
					$(":checkbox[name='allLayout']").prop("checked",false);
					loadLayoutTable(dynamicPageId);
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
	function refreshLayoutOrder(dynamicPageId){
		$.ajax({
			url:"layout/refreshLayoutOrder.do?pageId="+dynamicPageId,
			type:"GET",
			async:false,
			success:function(ret){
				if("1"==ret){
					loadLayoutTable(dynamicPageId);
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
	$("#deleteLayout").click(function(){
		
		var count = $(":checkbox[name='layout']:checked").size();
		if(count > 0){
			var dynamicPageId = $("#id").val();
			editLayout("layout","remove",null,null,dynamicPageId);
			return false;
		}else{
			alert("请选择布局操作");
			return false;
		}
	});
	
	$("#merageLayout").click(function(){
		var count = $(":checkbox[name='layout']:checked").size();
		if(count > 1){
			var dynamicPageId = $("#id").val();
			merageLayout("layout",dynamicPageId);
			return false;
		}else{
			alert("请选择两个以上布局操作");
			return false;
		}
	});
	
	$("#copyLayout").click(function(){	
		var count = $(":checkbox[name='layout']:checked").size();
		if(count == 1){
			var dynamicPageId = $("#id").val();
			copyLayout("layout",dynamicPageId);
			return false;
		}else{
			alert("请选择一个布局操作");
			return false;
		}
	});
	$("#refreshLayoutOrder").click(function(){
		if(window.confirm("你确定要重置吗？")){
			var dynamicPageId = $("#id").val();
			refreshLayoutOrder(dynamicPageId);
		}
			return false;
	});
	
	$("#checkAllLayout").click(function(){
		if($(this).prop("checked")){
			$(":checkbox[name='layout']").prop("checked",true);
		}else{
			$(":checkbox[name='layout']").prop("checked",false);
		}
		
	});
	
	
	function loadComByCondition(){
    	//var url = basePath + "component/loadComByCondition.do";
    	var url = basePath + "layout/loadComByCondition.do";
    	var cname = $("#cname").val();
    	var dataCode = $("#dataCode").val();
    	var typeId = $("#typeId option:selected").val();
    	var dynamicPageId = $("#id").val();
    	var rowValue = $("#rowValue").val();
    	var colValue = $("#colValue").val();
    	var objectType = "component";
    	//两次编码	
    	cname = encodeURI(encodeURI(cname));
    	dataCode = encodeURI(encodeURI(dataCode));
    	//alert(cname+","+dataCode+","+typeId);
    	$.ajax({
    		type:"GET",
    		url:url,
    		data:
    			"dynamicPageId="+dynamicPageId
    			+"&cname="+cname
    			+"&dataCode="+dataCode
    			+"&typeId="+typeId
    			+"&rowValue="+rowValue
    			+"&colValue="+colValue
    			+"&pageSize="+9999
    		,
    		async:false,
    		success:function(data){
    			$("#"+objectType+"t").empty();
    			$.each(data,function(idx,item){
    				addRow(objectType,item);
    			});
    		},
    		error:function(XMLHttpRequest,textStatus,errorThrown){
    			alert(errorThrown);
    		}
    	});
    }