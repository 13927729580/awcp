/**
 * 
 */



		
		
		
		function searchLayoutInTree(){
			var dynamicPageId=$("#id").val();
			loadLayoutTree(dynamicPageId);
		}
		
		function loadLayoutTree(dynamicPageId){
			var url = basePath + "layout/getLayoutListByPageIdInTree.do";

			var rows = $("#rows1").val();
			var columns = $("#columns1").val();
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
					var json = eval(data);
					
					$.fn.zTree.init($("#tree1"), setting, json);
				},
			    error: function (XMLHttpRequest, textStatus, errorThrown) { 
	                  alert(errorThrown); 
			    }
			});
		}

	
	
	/**
	 * 布局数批量修改布局比
	 * @param type
	 * @returns {Boolean}
	 */
	function batchModifyTreeProportion(type){
		var _selects=new Array();
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getCheckedNodes(true);
		//var loadUrl = "layout/getLayoutListByPageId.do"
		for(var i=0;i<nodes.length;i++){
			_selects.push(nodes[i].id);
		}
		
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
					initLayoutTree(dynamicPageId);
					
				}
			}
			
		}).showModal();
		return false;
		
	}
	
	
	/**
	 * 布局树批量清除行高
	 * @param type
	 * @returns {Boolean}
	 */
	function batchTreeHeight(type){
		var _selects=new Array();
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getCheckedNodes(true);
		//var loadUrl = "layout/getLayoutListByPageId.do"
		for(var i=0;i<nodes.length;i++){
			_selects.push(nodes[i].id);
		}
		
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
					initLayoutTree(dynamicPageId);
					//loadLayoutTable(dynamicPageId);
					
				}
			}
		
		}).showModal();
		return false;
		
	}
	
	
	/**
	 * 布局树批量修改行高
	 * @param type
	 * @returns {Boolean}
	 */
	function batchModifyTreeHeight(type){
		var _selects=new Array();
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getCheckedNodes(true);
		//var loadUrl = "layout/getLayoutListByPageId.do"
		for(var i=0;i<nodes.length;i++){
			_selects.push(nodes[i].id);
		}
		
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
					initLayoutTree(dynamicPageId);
					
				}
			}
		
		}).showModal();
		return false;
		
	}
	
	
	/**
	 * 布局树批量修改对齐方式
	 * @param type
	 * @returns {Boolean}
	 */
	function batchModifyTreeAlign(type){
		var _selects=new Array();
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getCheckedNodes(true);
		//var loadUrl = "layout/getLayoutListByPageId.do"
		for(var i=0;i<nodes.length;i++){
			_selects.push(nodes[i].id);
		}
		
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
					initLayoutTree(dynamicPageId);
					
				}
			}
		
		}).showModal();
		return false;
		
	}
	
	
	
	/**
	 *布局树批量修改边框
	 */
	function batchModifyTreeBorder(type){
		var _selects=new Array();
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getCheckedNodes(true);
		//var loadUrl = "layout/getLayoutListByPageId.do"
		for(var i=0;i<nodes.length;i++){
			_selects.push(nodes[i].id);
		}
		
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
					initLayoutTree(dynamicPageId);
					
				}
			}
		
		}).showModal();
		return false;
		
	}

	
	/**
	 * 布局树批量修改
	 * @param type
	 * @returns {Boolean}
	 */
	function batchTreeModify(type){
		var _selects=new Array();
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getCheckedNodes(true);
		//var loadUrl = "layout/getLayoutListByPageId.do"
		for(var i=0;i<nodes.length;i++){
			_selects.push(nodes[i].id);
		}
		
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
					initLayoutTree(dynamicPageId)
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
					initLayoutTree(dynamicPageId);
					loadLayoutTable(dynamicPageId);
					loadComponentTable(dynamicPageId);
				}
			}
		
		}).showModal();
		return false;
	}
	
	
	/**
	 * 布局树的合并布局
	 * @param objectType
	 * @param dynamicPageId
	 * @returns {Boolean}
	 */
	function merageTreeLayout(objectType,dynamicPageId){
		var _selects=new Array();
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getCheckedNodes(true);
		//var loadUrl = "layout/getLayoutListByPageId.do"
		for(var i=0;i<nodes.length;i++){
			_selects.push(nodes[i].id);
		}
		$.ajax({
			url:"layout/merageLayoutByAjax.do",
			type:"POST",
			async:false,
			data:{_selects:_selects.join(",")},
			success:function(ret){
				if("1"==ret){
					initLayoutTree(dynamicPageId);
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
	
	
	/**
	 *布局树的复制布局
	 * @param objectType
	 * @param dynamicPageId
	 * @returns {Boolean}
	 */
	function copyTreeLayout(objectType,dynamicPageId){
		var _selects=new Array();
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getCheckedNodes(true);
		for(var i=0;i<nodes.length;i++){
			_selects.push(nodes[i].id);
		}
		$.ajax({
			url:"layout/copyLayoutByAjax.do",
			type:"POST",
			async:false,
			data:{_selects:_selects.join(",")},
			success:function(ret){
				if("1"==ret){
					initLayoutTree(dynamicPageId);
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
	
	
	$("#merageLayout1").click(function(){
		/*var count = $(":checkbox[name='layout']:checked").size();*/
		var count = $.fn.zTree.getZTreeObj("tree1").getCheckedNodes(true).length;
		if(count > 1){
			var dynamicPageId = $("#id").val();
			merageTreeLayout("layout",dynamicPageId);
			return false;
		}else{
			alert("请选择两个以上布局操作");
			return false;
		}
	});
	
	$("#copyLayout1").click(function(){	
		/*var count = $(":checkbox[name='layout']:checked").size();*/
		var count = $.fn.zTree.getZTreeObj("tree1").getCheckedNodes(true).length;
		if(count == 1){
			var dynamicPageId = $("#id").val();
			copyTreeLayout("layout",dynamicPageId);
			return false;
		}else{
			alert("请选择一个布局操作");
			return false;
		}
	});
	
	$("#refreshLayoutOrder1").click(function(){
		if(window.confirm("你确定要重置吗？")){
			var dynamicPageId = $("#id").val();
			refreshLayoutOrder(dynamicPageId);
		}
			return false;
	});
	
	
	
	//全选
    function CheckAllNodes() {
        var treeObj = $.fn.zTree.getZTreeObj("tree1");
        treeObj.checkAllNodes(true);
    }

    //全取消
    function CancelAllNodes() {
        var treeObj = $.fn.zTree.getZTreeObj("tree1");
        treeObj.checkAllNodes(false);
    }
    
    function loadCTabByLayout(dynamicPageId) {
    	var url = basePath + "component/getComponentListByPageId.do";
    	var objectType = "component";
    	$.ajax({
    		type : "GET",
    		url : url,
    		data : "dynamicPageId=" + dynamicPageId + "&pageSize=9999",
    		async : false,
    		success : function(data) {
    			// clear table
    			$("#" + objectType + "tt").empty();
    			$.each(data, function(idx, item) {
    				addRow_tree(objectType, item);
    			});
    		},
    		error : function(XMLHttpRequest, textStatus, errorThrown) {
    			alert(errorThrown);
    		}
    	});
    }
    
    
    function loadComByCondition(){
    	//var url = basePath + "component/loadComByCondition.do";
    	var url = basePath + "layout/loadComByCondition.do";
    	var cname = $("#cname").val();
    	var lname = $("#lname").val();
    	var dataCode = $("#dataCode").val();
    	var typeId = $("#typeId option:selected").val();
    	var dynamicPageId = $("#id").val();
    	var rowValue = $("#rowValue").val();
    	var colValue = $("#colValue").val();
    	var objectType = "component";
    	//两次编码	
    	lname = encodeURI(encodeURI(lname));
    	cname = encodeURI(encodeURI(cname));
    	dataCode = encodeURI(encodeURI(dataCode));
    	//alert(cname+","+lname+","+typeId);
    	$.ajax({
    		type:"GET",
    		url:url,
    		data:{
    			dynamicPageId:dynamicPageId,
    			cname:cname,
    			lname:lname,
    			dataCode:dataCode,
    			typeId:typeId,
    			rowValue:rowValue,
    			colValue:colValue,
    			pageSize:9999
    		},
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